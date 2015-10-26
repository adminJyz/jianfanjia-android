package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:评价设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class PingjiaActivity extends BaseActivity implements
        OnClickListener, ApiUiUpdateListener {
    private static final String TAG = PingjiaActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private RatingBar bar = null;
    private RatingBar speedBar = null;
    private RatingBar attudeBar = null;
    private EditText contentEdit = null;
    private Button btn_commit = null;

    private String requirementid = null;
    private String designerid = null;

    private int respond_speed = 0;
    private int service_attitude = 0;


    @Override
    public void initView() {
        initMainHeadView();
        bar = (RatingBar) findViewById(R.id.ratingBar);
        speedBar = (RatingBar) findViewById(R.id.speedBar);
        attudeBar = (RatingBar) findViewById(R.id.attudeBar);
        contentEdit = (EditText) findViewById(R.id.contentEdit);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        Intent intent = this.getIntent();
        Bundle commentBundle = intent.getExtras();
        requirementid = commentBundle.getString(Global.REQUIREMENT_ID);
        designerid = commentBundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "requirementid:" + requirementid + " designerid:" + designerid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_pingjia_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.pingjiaText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }


    @Override
    public void setListener() {
        speedBar.setOnRatingBarChangeListener(speedListener);
        attudeBar.setOnRatingBarChangeListener(attitudeListener);
        btn_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_commit:
                String content = contentEdit.getText().toString().trim();
                evaluateDesignerByUser(requirementid, designerid, service_attitude, respond_speed, content, "0");
                break;
            default:
                break;
        }
    }

    private OnRatingBarChangeListener speedListener = new OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            respond_speed = (int) rating;
            LogTool.d(TAG, "respond_speed:" + respond_speed);
        }
    };

    private OnRatingBarChangeListener attitudeListener = new OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            service_attitude = (int) rating;
            LogTool.d(TAG, "service_attitude:" + service_attitude);
        }
    };

    //评价设计师
    private void evaluateDesignerByUser(String requirementid, String designerid, int service_attitude, int respond_speed, String comment, String is_anonymous) {
        JianFanJiaClient.evaluateDesignerByUser(PingjiaActivity.this, requirementid, designerid, service_attitude, respond_speed, comment, is_anonymous, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        LogTool.d(TAG, "data:" + data);
        makeTextLong(data.toString());
    }


    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pingjia;
    }
}