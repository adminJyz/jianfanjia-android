package com.jianfanjia.cn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.SectionItemAdapterBack;
import com.jianfanjia.cn.adapter.SectionViewPageAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.SectionInfo;
import com.jianfanjia.cn.bean.SectionItemInfo;
import com.jianfanjia.cn.bean.ViewPagerItem;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickCallBack;
import com.jianfanjia.cn.interf.UploadImageListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.ImageUtils;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.SuperSwipeRefreshLayout;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DateWheelDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author fengliang
 * @ClassName:SiteManageFragment
 * @Description:工地管理
 * @date 2015-8-26 上午11:14:00
 */
@EActivity(R.layout.activity_my_process_detail)
public class MyProcessDetailActivity extends BaseAnnotationActivity implements ItemClickCallBack, UploadImageListener {
    private static final String TAG = MyProcessDetailActivity.class.getName();
    private static final int TOTAL_PROCESS = 7;// 7道工序

    // Header View
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;

    @ViewById(R.id.process_viewpager)
    ViewPager processViewPager;
    @ViewById(R.id.process__listview)
    ListView detailNodeListView;
    @ViewById(R.id.process_head_layout)
    MainHeadView mainHeadView;
    @ViewById(R.id.process_pull_refresh)
    SuperSwipeRefreshLayout process_pull_refresh;
    @StringArrayRes(R.array.site_procedure)
    String[] proTitle = null;

    private SectionItemAdapterBack sectionItemAdapter = null;
    private SectionViewPageAdapter sectionViewPageAdapter = null;
    private List<ViewPagerItem> processList = new ArrayList<ViewPagerItem>();
    private List<String> imageList;
    private List<SectionInfo> sectionInfos;
    private SectionInfo sectionInfo = null;
    private ProcessInfo processInfo = null;
    private String processId = null;// 默认的工地id

    private int currentPro = -1;// 当前进行工序
    private int currentList = -1;// 当前展开第一道工序
    private int lastPro = -1;// 上次进行的工序

    private File mTmpFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (currentList == -1) {
            currentList = dataManager.getCurrentList();
        }
    }

    @AfterViews
    public void initView() {
        initPullRefresh();
        initMainHead();
        initScrollLayout();
        initListView();
        initProcessInfo();
    }

    private void initPullRefresh() {
        process_pull_refresh.setHeaderView(createHeaderView());// add headerView
        process_pull_refresh.setTargetScrollWithLayout(true);
        process_pull_refresh
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

                    @Override
                    public void onRefresh() {
                        textView.setText("正在刷新");
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
//                        initdata();
                        loadCurrentProcess();
                    }

                    @Override
                    public void onPullDistance(int distance) {
                        // pull distance
                    }

                    @Override
                    public void onPullEnable(boolean enable) {
                        textView.setText(enable ? "松开刷新" : "下拉刷新");
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setRotation(enable ? 180 : 0);
                    }
                });
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(process_pull_refresh.getContext())
                .inflate(R.layout.layout_head, null);
        progressBar = (ProgressBar) headerView.findViewById(R.id.pb_view);
        textView = (TextView) headerView.findViewById(R.id.text_view);
        textView.setText("下拉刷新");
        imageView = (ImageView) headerView.findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.mipmap.icon_arrow);
        progressBar.setVisibility(View.GONE);
        return headerView;
    }

    private void initProcessInfo() {
        Intent intent = getIntent();
        processInfo = (ProcessInfo) intent.getSerializableExtra(Global.PROCESS_INFO);
        if (processInfo != null) {
            processId = processInfo.get_id();
            if (NetTool.isNetworkAvailable(this)) {
                loadCurrentProcess();
            } else {
//                processInfo = dataManager.getProcessInfoById(processId);
            }
        } else {
            processInfo = dataManager.getProcessInfoById(Constant.DEFAULT_PROCESSINFO_ID);
        }

    }

    private void loadCurrentProcess() {
        if (processId != null) {
            JianFanJiaClient.get_ProcessInfo_By_Id(this, dataManager.getDefaultProcessId(), this, this);
        }
    }

    private void initMainHead() {
        mainHeadView.setBackLayoutVisable(View.GONE);
        mainHeadView.setRightTitle("切换工地");
    }

    // 初始化数据
    private void initData() {
        if (processInfo != null) {
            mainHeadView.setMianTitle(processInfo.getCell() == null ? ""
                    : processInfo.getCell());// 设置标题头
            currentPro = MyApplication.getInstance().getPositionByItemName(
                    processInfo.getGoing_on());
            if (currentList == -1 || lastPro != currentPro) {
                currentList = currentPro;
                lastPro = currentPro;
            }
            sectionInfos = processInfo.getSections();
            sectionInfo = sectionInfos.get(currentList);
            setScrollHeadTime();
            LogTool.d(TAG, sectionInfos.size() + "--sectionInfos.size()");
            sectionItemAdapter = new SectionItemAdapterBack(getApplication(),
                    currentList, sectionInfos, this);
            detailNodeListView.setAdapter(sectionItemAdapter);
            processViewPager.setCurrentItem(currentList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sectionItemAdapter != null) {
            sectionItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentList != -1) {
            dataManager.setCurrentList(currentList);
        }
    }

    private void initScrollLayout() {
        for (int i = 0; i < proTitle.length; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem();
            viewPagerItem.setResId(getApplication().getResources()
                    .getIdentifier("icon_home_normal" + (i + 1), "drawable",
                            getApplication().getPackageName()));
            viewPagerItem.setTitle(proTitle[i]);
            viewPagerItem.setDate("");
            processList.add(viewPagerItem);
        }
        for (int i = 0; i < 3; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem();
            viewPagerItem.setResId(R.mipmap.icon8_home_normal);
            viewPagerItem.setTitle("");
            viewPagerItem.setDate("");
            processList.add(viewPagerItem);
        }
        // --------------------------
        sectionViewPageAdapter = new SectionViewPageAdapter(this, processList,
                new ViewPagerClickListener() {

                    @Override
                    public void onClickItem(int potition) {
                        Log.i(TAG, "potition=" + potition);
                        if (sectionInfos != null) {
                            if (potition < TOTAL_PROCESS) {
                                currentList = potition;
                                sectionInfo = sectionInfos.get(currentList);
                                sectionItemAdapter.setPosition(currentList);
                                processViewPager.setCurrentItem(potition);
                            }
                        }
                    }

                });
        processViewPager.setAdapter(sectionViewPageAdapter);
        processViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageSelected(int arg0) {
                if (sectionInfos != null) {
                    if (arg0 < TOTAL_PROCESS) {
                        currentList = arg0;
                        sectionInfo = sectionInfos.get(currentList);
                        Log.i(TAG, "sectionInfo=" + sectionInfo.getName());
                        sectionItemAdapter.setPosition(currentList);
                    }
                }
            }
        });
    }

    private void setScrollHeadTime() {
        if (sectionInfos != null) {
            for (int i = 0; i < proTitle.length; i++) {
                ViewPagerItem viewPagerItem = sectionViewPageAdapter.getList()
                        .get(i);
                if (sectionInfos.get(i).getStart_at() > 0) {
                    viewPagerItem.setDate(DateFormatTool.covertLongToString(
                            sectionInfos.get(i).getStart_at(), "M.dd")
                            + "-"
                            + DateFormatTool.covertLongToString(sectionInfos
                            .get(i).getEnd_at(), "M.dd"));
                }
                if (sectionInfos.get(i).getStatus() != Constant.NOT_START) {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_checked" + (i + 1),
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                } else {
                    int drawableId = getApplication().getResources()
                            .getIdentifier("icon_home_normal" + (i + 1),
                                    "mipmap",
                                    getApplication().getPackageName());
                    viewPagerItem.setResId(drawableId);
                }
            }
            sectionViewPageAdapter.notifyDataSetChanged();
        }
    }

    private void initListView() {
        detailNodeListView.setFocusable(false);
        detailNodeListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (sectionItemAdapter.isHasCheck()) {
                    if (position == 0) {
                        boolean isCanClickYanshou = true;
                        for (SectionItemInfo sectionItemInfo : sectionInfo
                                .getItems()) {
                            if (Constant.FINISH != Integer
                                    .parseInt(sectionItemInfo.getStatus())) {
                                isCanClickYanshou = false;
                                break;
                            }
                        }
                        if (isCanClickYanshou) {
                            sectionItemAdapter.setCurrentOpenItem(position);
                        }
                    } else {
                        sectionItemAdapter.setCurrentOpenItem(position);
                    }
                } else {
                    sectionItemAdapter.setCurrentOpenItem(position);
                }
            }
        });

    }

    @Override
    public void click(int position, int itemType) {
        LogTool.d(TAG, "position:" + position + "itemType:" + itemType);
        switch (itemType) {
            case Constant.CONFIRM_ITEM:
                confirmFinishDialog();
                break;
            case Constant.IMG_ITEM:
                break;
            case Constant.COMMENT_ITEM:
                Bundle bundle = new Bundle();
                bundle.putString(Global.TOPIC_ID, processId);
                bundle.putString(Global.TO, processInfo.getFinal_designerid());
                startActivity(CommentActivity.class, bundle);
                break;
            case Constant.DELAY_ITEM:
                delayDialog();
                break;
            case Constant.CHECK_ITEM:
                Bundle checkBundle = new Bundle();
                checkBundle.putString(Constant.PROCESS_NAME, sectionInfo.getName());
                checkBundle
                        .putInt(Constant.PROCESS_STATUS, sectionInfo.getStatus());
                startActivity(CheckActivity.class, checkBundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadSuccess(Object data) {
//        mPullRefreshScrollView.onRefreshComplete();
        process_pull_refresh.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        if(data != null){
            processInfo = JsonParser.jsonToBean(data.toString(),ProcessInfo.class);
            initData();
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(getString(R.string.tip_error_internet));
        process_pull_refresh.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
//        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void preLoad() {
        // TODO Auto-generated method stub
    }

    @Override
    public void click(int position, int itemType, List<String> imageUrlList) {
        switch (itemType) {
            case Constant.IMG_ITEM:
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constant.IMAGE_LIST,
                        (ArrayList<String>) imageUrlList);
                bundle.putInt(Constant.CURRENT_POSITION, position);
                startActivity(ShowPicActivity.class, bundle);
                break;
            case Constant.ADD_ITEM:
                imageList = imageUrlList;
                showPopWindow(getWindow().getDecorView());
                break;
            default:
                break;
        }
    }

    @Override
    public void firstItemClick() {

        mTmpFile = UiHelper.getTempPath();
        if (mTmpFile != null) {
            Intent cameraIntent = UiHelper.createShotIntent(mTmpFile);
            startActivityForResult(cameraIntent, Constant.REQUESTCODE_CAMERA);
        } else {
            makeTextLong("没有sd卡，无法打开相机");
        }
    }

    @Override
    public void secondItemClick() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, Constant.REQUESTCODE_LOCATION);
    }

    private void delayDialog() {
        DateWheelDialog dateWheelDialog = new DateWheelDialog(this,
                Calendar.getInstance());
        dateWheelDialog.setTitle("选择时间");
        dateWheelDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String dateStr = StringUtils
                                .getDateString(((DateWheelDialog) dialog)
                                        .getChooseCalendar().getTime());
                        LogTool.d(TAG, "dateStr:" + dateStr);
                        /*postReschedule(processInfo.get_id(),
                                processInfo.getUserid(),
								processInfo.getFinal_designerid(),
								sectionInfo.getName(), dateStr);*/
                    }
                });
        dateWheelDialog.setNegativeButton(R.string.no, null);
        dateWheelDialog.show();
    }

    private void confirmFinishDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(this);
        dialog.setTitle("确认完工");
        dialog.setMessage("确认完工吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        confirmProcessItemDone(processInfo.get_id(),
                                sectionInfo.getName(),
                                sectionItemAdapter.getCurrentItem());
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    // 提交改期
    private void postReschedule(String processId, String userId,
                                String designerId, String section, String newDate) {
        LogTool.d(TAG, "processId:" + processId + " userId:" + userId
                + " designerId:" + designerId + " section:" + section
                + " newDate:" + newDate);
        JianFanJiaClient.postReschedule(this, processId, userId,
                designerId, section, newDate, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {

                    }

                    @Override
                    public void loadSuccess(Object data) {
                        loadCurrentProcess();
                    }

                    @Override
                    public void loadFailture(String error_msg) {

                    }
                }, this);
    }

    // 确认完工装修流程小节点
    private void confirmProcessItemDone(String siteId, String section,
                                        String item) {
        LogTool.d(TAG, "siteId:" + siteId + " section:" + section + " item:"
                + item);
        JianFanJiaClient.processItemDone(this, siteId, section,
                item, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {

                    }

                    @Override
                    public void loadSuccess(Object data) {
                        loadCurrentProcess();
                    }

                    @Override
                    public void loadFailture(String error_msg) {

                    }
                }, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUESTCODE_CAMERA:// 拍照
                mTmpFile = new File(dataManager.getPicPath());
                if (mTmpFile != null) {
                    Bitmap imageBitmap = ImageUtil.getImage(mTmpFile.getPath());
                    LogTool.d(TAG, "imageBitmap:" + imageBitmap);
                    if (null != imageBitmap) {
                        upload_image(imageBitmap);
                    }
                }
                break;
            case Constant.REQUESTCODE_LOCATION:// 本地选取
                if (data != null) {
                    Uri uri = data.getData();
                    LogTool.d(TAG, "uri:" + uri);
                    if (null != uri) {
                        Bitmap imageBitmap = ImageUtil.getImage(ImageUtils
                                .getImagePath(uri, this));
                        if (null != imageBitmap) {
                            upload_image(imageBitmap);
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_CONFIG_SITE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String temStr = (String) bundle.get("Key");
                        LogTool.d(TAG, "temStr" + temStr);
                        if (null != temStr) {
                            initProcessInfo();
                            if (processInfo != null) {
                                initData();
                            } else {
                                // loadempty
                                loadCurrentProcess();
                            }
                        }
                    }
                }
                break;
            case Constant.REQUESTCODE_CHANGE_SITE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String processId = (String) bundle.get("ProcessId");
                        LogTool.d(TAG, "processId=" + processId);
                        if (null != processId
                                && dataManager.getDefaultProcessId() != processId) {
                            // loadempty
                            loadCurrentProcess();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void upload_image(Bitmap bitmap) {
        JianFanJiaClient.uploadImage(this, bitmap, new ApiUiUpdateListener() {
            @Override
            public void preLoad() {

            }

            @Override
            public void loadSuccess(Object data) {
                String itemName = sectionItemAdapter
                        .getCurrentItem();
                JianFanJiaClient.submitImageToProcess(MyProcessDetailActivity.this,
                        processInfo.get_id(),
                        sectionInfo.getName(),
                        itemName,
                        dataManager
                                .getCurrentUploadImageId(), new ApiUiUpdateListener() {
                            @Override
                            public void preLoad() {

                            }

                            @Override
                            public void loadSuccess(Object data) {
                                loadCurrentProcess();
                                if (mTmpFile != null
                                        && mTmpFile
                                        .exists()) {
                                    mTmpFile.delete();
                                }
                            }

                            @Override
                            public void loadFailture(String error_msg) {

                            }
                        }, this);
            }

            @Override
            public void loadFailture(String error_msg) {

            }
        }, this);

    }

    @Override
    public void onSuccess(String msg) {
        LogTool.d(TAG, "msg===========" + msg);
        if ("success".equals(msg)) {
            LogTool.d(TAG, "--------------------------------------------------");
            if (mTmpFile != null && mTmpFile.exists()) {
                mTmpFile.delete();
            }
            // loadCurrentProcess();
            // sectionInfo.getItems()
            if (dataManager.getCurrentUploadImageId() != null
                    && imageList != null) {
                imageList.add(imageList.size() - 1,
                        dataManager.getCurrentUploadImageId());
                sectionItemAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFailure() {
        LogTool.d(TAG, "==============================================");
        if (mTmpFile != null && mTmpFile.exists()) {
            mTmpFile.delete();
        }
    }

}
