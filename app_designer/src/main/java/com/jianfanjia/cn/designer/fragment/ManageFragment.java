package com.jianfanjia.cn.designer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.request.designer.GetProcessListRequest;
import com.jianfanjia.cn.designer.Event.MessageEvent;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.SettingContractActivity;
import com.jianfanjia.cn.designer.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewBusinessRequirementActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewRequirementActivity;
import com.jianfanjia.cn.designer.adapter.MySiteAdapter;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.SiteProcessItem;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description:工地管理
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ManageFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = ManageFragment.class.getName();
    public static final int ITEM_PRIVIEW = 10;
    public static final int ITEM_CONTRACT = 20;
    public static final int ITEM_PLAN = 30;
    public static final int ITEM_GOTOO_SITE = 40;

    @Bind(R.id.manage_head)
    MainHeadView mainHeadView;

    @Bind(R.id.manage_pullfefresh)
    PullToRefreshRecycleView manage_pullfefresh;

    @Bind(R.id.emptyPullRefreshScrollView)
    PullToRefreshScrollView emptyPullRefresh;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    @Bind(R.id.frag_req_rootview)
    LinearLayout rootLayout;

    @Bind(R.id.process_tip_text)
    TextView process_tip_text;

    private String[] proTitle = null;
    private List<Process> processList = new ArrayList<Process>();
    private List<SiteProcessItem> siteProcessList = new ArrayList<SiteProcessItem>();
    private MySiteAdapter adapter = null;
    private String processId = null;
    private int itemPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        initMainHeadView();
        emptyPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getProcessList();
            }
        });
        proTitle = getActivity().getApplication().getResources().getStringArray(
                R.array.site_procedure);
        setProcessList();
        manage_pullfefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        manage_pullfefresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        manage_pullfefresh.setItemAnimator(new DefaultItemAnimator());
        manage_pullfefresh.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
        manage_pullfefresh.setOnRefreshListener(this);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle("我的工地");
        mainHeadView.setBackgroundTransparent();
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @OnClick({R.id.error_include, R.id.process_tip_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_include:
                getProcessList();
                break;
            case R.id.process_tip_text:
                Intent gotoMyProcess = new Intent(getActivity(), MyProcessDetailActivity.class);
                gotoMyProcess.putExtra(Global.PROCESS_ID, processId);
                startActivity(gotoMyProcess);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(MessageEvent event) {
        LogTool.d(TAG, "event:" + event.getEventType());
        switch (event.getEventType()) {
            case Constant.UPDATE_MANAAGE_FRAGMENT:

                break;
            default:
                break;
        }
    }

    private void getProcessList() {
        GetProcessListRequest getProcessListRequest = new GetProcessListRequest();
        Api.getProcessList(getProcessListRequest, new ApiCallback<ApiResponse<List<Process>>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<List<Process>> apiResponse) {
                processList = apiResponse.getData();
                LogTool.d(TAG, "processList:" + processList);
                if (null != processList && processList.size() > 0) {
                    adapter = new MySiteAdapter(getActivity(), processList, siteProcessList, new
                            ClickCallBack() {
                                @Override
                                public void click(int position, int itemType) {
                                    Process process = processList.get(position);
                                    processId = process.get_id();
                                    LogTool.d(TAG, "processId:" + processId + "  itemPosition:" + itemPosition);
                                    switch (itemType) {
                                        case ITEM_PRIVIEW:
                                            Intent gotoPriviewRequirement = null;
                                            if (process.getRequirement().getDec_type().equals(Global
                                                    .DEC_TYPE_BUSINESS)) {
                                                gotoPriviewRequirement = new Intent(getActivity(),
                                                        PreviewBusinessRequirementActivity.class);
                                            } else {
                                                gotoPriviewRequirement = new Intent(getActivity(),
                                                        PreviewRequirementActivity.class);
                                            }
                                            gotoPriviewRequirement.putExtra(Global.REQUIREMENT_INFO, process
                                                    .getRequirement());
                                            startActivity(gotoPriviewRequirement);
                                            break;
                                        case ITEM_CONTRACT:
                                            Intent viewContractIntent = new Intent(getActivity(),
                                                    SettingContractActivity
                                                            .class);
                                            Bundle contractBundle = new Bundle();
                                            contractBundle.putSerializable(Global.REQUIREMENT_INFO, process
                                                    .getRequirement());
                                            contractBundle.putSerializable(Global.PLAN_DETAIL, process.getPlan());
                                            viewContractIntent.putExtras(contractBundle);
                                            startActivity(viewContractIntent);
                                            break;
                                        case ITEM_PLAN:
                                            Intent viewPlanIntent = new Intent(getActivity(),
                                                    PreviewDesignerPlanActivity
                                                            .class);
                                            Bundle planBundle = new Bundle();
                                            planBundle.putSerializable(Global.PLAN_DETAIL, process.getPlan());
                                            planBundle.putSerializable(Global.REQUIREMENT_INFO, process
                                                    .getRequirement());
                                            viewPlanIntent.putExtras(planBundle);
                                            startActivity(viewPlanIntent);
                                            break;
                                        case ITEM_GOTOO_SITE:
                                            Intent gotoMyProcess = new Intent(getActivity(),
                                                    MyProcessDetailActivity
                                                            .class);
                                            gotoMyProcess.putExtra(Global.PROCESS_ID, processId);
                                            startActivity(gotoMyProcess);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                    manage_pullfefresh.setAdapter(adapter);
                    manage_pullfefresh.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    manage_pullfefresh.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.findViewById(R.id.empty_contentLayout).setLayoutParams(//动态设置内容高度，防止滚动
                            new RelativeLayout.LayoutParams(rootLayout.getWidth(), rootLayout.getHeight() -
                                    mainHeadView.getHeight()));
                    errorLayout.setVisibility(View.GONE);
                }
                manage_pullfefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
            }

            @Override
            public void onFailed(ApiResponse<List<Process>> apiResponse) {
                manage_pullfefresh.onRefreshComplete();
                emptyPullRefresh.onRefreshComplete();
                if (null != adapter) {
                    manage_pullfefresh.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                } else {
                    manage_pullfefresh.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void setProcessList() {
        for (int i = 0; i < proTitle.length; i++) {
            SiteProcessItem item = new SiteProcessItem();
            item.setRes(getResources()
                    .getIdentifier("icon_home_bg" + (i + 1), "drawable",
                            getActivity().getApplication().getPackageName()));
            item.setTitle(proTitle[i]);
            siteProcessList.add(item);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getProcessList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getProcessList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getProcessList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_manage2;
    }

}
