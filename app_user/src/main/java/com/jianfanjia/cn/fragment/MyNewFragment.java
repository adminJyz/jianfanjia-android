package com.jianfanjia.cn.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.common.CommentListActivity;
import com.jianfanjia.cn.activity.my.AboutActivity;
import com.jianfanjia.cn.activity.my.BindingAccountActivity;
import com.jianfanjia.cn.activity.my.CollectActivity;
import com.jianfanjia.cn.activity.my.CustomerServiceActivity;
import com.jianfanjia.cn.activity.my.FeedBackActivity;
import com.jianfanjia.cn.activity.my.NoticeActivity;
import com.jianfanjia.cn.activity.my.UserInfoActivity;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.common.tool.TDevice;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;
import com.jianfanjia.cn.view.layout.BadgeView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:我的
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyNewFragment extends BaseFragment {
    private static final String TAG = MyNewFragment.class.getName();
    public static final int REQUESTCODE_USERINFO = 0;

    @Bind(R.id.notify_layout)
    RelativeLayout notifyLayout;

    @Bind(R.id.collect_layout)
    RelativeLayout my_collect_layout;

    @Bind(R.id.setting_layout)
    RelativeLayout setting_layout;

    @Bind(R.id.kefu_layout)
    RelativeLayout kefu_layout;

    @Bind(R.id.frag_my_info_layout)
    RelativeLayout my_info_layout;

    @Bind(R.id.feedback_layout)
    RelativeLayout feedback_layout;

    @Bind(R.id.clear_cache_layout)
    RelativeLayout clearCacheLayout;

    @Bind(R.id.call_layout)
    RelativeLayout callPhoneLayout;

    @Bind(R.id.comment_layout)
    RelativeLayout commentLayout;

    @Bind(R.id.binding_account_layout)
    RelativeLayout bindingAccountLayout;

    @Bind(R.id.cache_size)
    TextView cacheSizeView;

    @Bind(R.id.setting_scrollview)
    ScrollView scrollView;

    @Bind(R.id.head_img)
    ImageView head_img;

    @Bind(R.id.user_head_img)
    ImageView user_head_img;

    @Bind(R.id.frag_my_name)
    TextView my_name;

    @Bind(R.id.frag_my_account)
    TextView my_account;

    @Bind(R.id.notify_count_text)
    public BadgeView noticeCountView;

    @Bind(R.id.comment_count_text)
    public BadgeView commentCountView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        setListener();
        return view;
    }

    public void initView() {
        commentCountView.setVisibility(View.GONE);
        noticeCountView.setVisibility(View.GONE);
        cacheSizeView.setText(UiHelper.caculateCacheSize());
        //动态计算imageview的宽高
        head_img.setLayoutParams(new FrameLayout.LayoutParams((int) TDevice.getScreenWidth(), (int) (880 / (1242 /
                TDevice.getScreenWidth()))));
        getUnReadMessageCount(Constant.searchMsgCountType1, Constant.searchMsgCountType2);
    }

    protected void initMyInfo() {
        String imgPath = dataManager.getUserImagePath();
        LogTool.d(TAG, "imgPath=" + imgPath);
        if (!imgPath.contains(Constant.DEFALUT_PIC_HEAD)) {
//            imageShow.displayScreenWidthThumnailImage(getActivity(), imgPath, head_img);
            imageShow.displayImageHeadWidthThumnailImage(getActivity(), imgPath, user_head_img);
        } else {
            user_head_img.setImageResource(R.mipmap.icon_default_head);
        }
    }

    @OnClick({R.id.notify_layout, R.id.collect_layout, R.id.frag_my_info_layout, R.id.kefu_layout, R.id
            .setting_layout, R.id.feedback_layout, R.id.clear_cache_layout, R.id.call_layout, R.id.comment_layout, R
            .id.binding_account_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notify_layout:
                startActivity(NoticeActivity.class);
                break;
            case R.id.collect_layout:
                startActivity(CollectActivity.class);
                break;
            case R.id.frag_my_info_layout:
                startActivityForResult(UserInfoActivity.class, REQUESTCODE_USERINFO);
                break;
            case R.id.kefu_layout:
                startActivity(CustomerServiceActivity.class);
                break;
            case R.id.setting_layout:
                startActivity(AboutActivity.class);
                break;
            case R.id.feedback_layout:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.clear_cache_layout:
                onClickCleanCache();
                break;
            case R.id.call_layout:
                UiHelper.callPhoneIntent(getContext(), getString(R.string.app_phone));
                break;
            case R.id.comment_layout:
                startActivity(CommentListActivity.class);
                break;
            case R.id.binding_account_layout:
                startActivity(BindingAccountActivity.class);
                break;
            default:
                break;
        }
    }

    private void setListener() {
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMyInfo();
        my_name.setText(TextUtils.isEmpty(dataManager.getUserName()) ? getResources().getString(R.string.ower) :
                dataManager.getUserName());
        my_account.setText(TextUtils.isEmpty(dataManager.getAccount()) ? "" : "账号：" + dataManager.getAccount());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogTool.d(TAG, "isHidden =" + hidden);
        if (!hidden) {
//            getUnReadMessageCount(Constant.searchMsgCountType1, Constant.searchMsgCountType2);
        }
    }

    private void getUnReadMessageCount(String[]... selectLists) {
        UiHelper.getUnReadMessageCount(new ApiCallback<ApiResponse<List<Integer>>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<List<Integer>> apiResponse) {
                List<Integer> countList = apiResponse.getData();
                if (countList != null) {
                    if (countList.get(0) > 0) {
                        noticeCountView.setVisibility(View.VISIBLE);
                        noticeCountView.setText(countList.get(0) + "");
                    } else {
                        noticeCountView.setVisibility(View.GONE);
                    }
                    if (countList.get(1) > 0) {
                        commentCountView.setVisibility(View.VISIBLE);
                        commentCountView.setText(countList.get(1) + "");
                    } else {
                        commentCountView.setVisibility(View.GONE);
                        commentCountView.setText("");
                    }
                }
            }

            @Override
            public void onFailed(ApiResponse<List<Integer>> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        }, selectLists);
    }

    /**
     * 清空缓存
     */
    private void onClickCleanCache() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle("清空缓存");
        dialog.setMessage("确定清空缓存吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.getInstance().clearAppCache();
                        cacheSizeView.setText("0KB");
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUESTCODE_USERINFO:
                initMyInfo();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_new;
    }
}
