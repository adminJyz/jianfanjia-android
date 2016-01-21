package com.jianfanjia.cn.designer.activity.requirement;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.common.CommentActivity;
import com.jianfanjia.cn.designer.adapter.DesignerPlanAdapter;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.bean.PlanInfo;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.interf.ItemClickListener;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:设计师方案列表
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerPlanListActivity extends BaseActivity implements OnClickListener, ApiUiUpdateListener, ItemClickListener, PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = DesignerPlanListActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private PullToRefreshRecycleView designer_plan_listview = null;
    private List<PlanInfo> designerPlanList = new ArrayList<PlanInfo>();
    private String requirementid = null;
//    private String designerid = null;
    private RequirementInfo requirementInfo = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle designerBundle = intent.getExtras();
        requirementInfo = (RequirementInfo)designerBundle.getSerializable(Global.REQUIREMENT_INFO);
        if(requirementInfo != null){
            requirementid = requirementInfo.get_id();
        }
        LogTool.d(TAG, "requirementid:" + requirementid);
        initMainHeadView();
        designer_plan_listview = (PullToRefreshRecycleView) findViewById(R.id.designer_plan_listview);
        designer_plan_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        designer_plan_listview.setLayoutManager(new LinearLayoutManager(this));
        designer_plan_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        designer_plan_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).showLastDivider().build());
        getDesignerPlansList(requirementid, dataManager.getUserId());
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_plan_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.planText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        designer_plan_listview.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDesignerPlansList(requirementid, dataManager.getUserId());
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    //业主获取我的方案
    private void getDesignerPlansList(String requestmentid, String designerid) {
        JianFanJiaClient.getDesignerPlansByUser(DesignerPlanListActivity.this, requestmentid, designerid, this, this);
    }

    @Override
    public void preLoad() {
        showWaitDialog(R.string.loading);
    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
        hideWaitDialog();
        designerPlanList = JsonParser.jsonToList(data.toString(), new TypeToken<List<PlanInfo>>() {
        }.getType());
        LogTool.d(TAG, "designerPlanList:" + designerPlanList);
        if (null != designerPlanList && designerPlanList.size() > 0) {
            DesignerPlanAdapter adapter = new DesignerPlanAdapter(this, designerPlanList, this);
            designer_plan_listview.setAdapter(adapter);
        }
        designer_plan_listview.onRefreshComplete();
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
        hideWaitDialog();
        designer_plan_listview.onRefreshComplete();
    }

    @Override
    public void onCallBack(int position, int pos) {
        LogTool.d(TAG, "position:" + position + "  pos:" + pos);
        PlanInfo planInfo = designerPlanList.get(position);
        LogTool.d(TAG, "planInfo:" + planInfo);
        String planid = planInfo.get_id();
        LogTool.d(TAG, "planid:" + planid);
        startToActivity(planInfo, requirementInfo);
    }

    @Override
    public void onItemCallBack(int position, int itemType) {
        LogTool.d(TAG, "itemType:" + itemType);
        PlanInfo planInfo = designerPlanList.get(position);
        LogTool.d(TAG, "planInfo:" + planInfo);
        String planid = planInfo.get_id();
        String designerid = planInfo.getDesignerid();
        LogTool.d(TAG, "planid:" + planid + " designerid:" + designerid);
        switch (itemType) {
            case Constant.PLAN_COMMENT_ITEM:
                Intent commentIntent = new Intent(DesignerPlanListActivity.this, CommentActivity.class);
                Bundle commentBundle = new Bundle();
                commentBundle.putString(Global.TOPIC_ID, planid);
                commentBundle.putString(Global.TO, designerid);
                commentBundle.putString(Global.TOPICTYPE, Global.TOPIC_PLAN);
                commentIntent.putExtras(commentBundle);
                startActivityForResult(commentIntent, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            case Constant.PLAN_PREVIEW_ITEM:
                startToActivity(planInfo, requirementInfo);
                break;
            default:
                break;
        }
    }

    private void startToActivity(PlanInfo planInfo, RequirementInfo requirement) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(Global.PLAN, planInfo);
        planBundle.putSerializable(Global.REQUIRE, requirement);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_GOTO_COMMENT:
                getDesignerPlansList(requirementid, dataManager.getUserId());
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_plan_list;
    }

}
