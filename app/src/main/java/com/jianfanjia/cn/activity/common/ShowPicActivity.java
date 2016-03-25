package com.jianfanjia.cn.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.ShowPicPagerAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ViewPagerClickListener;

import butterknife.Bind;

public class ShowPicActivity extends SwipeBackActivity implements
        ViewPagerClickListener, OnPageChangeListener {
    private static final String TAG = ShowPicActivity.class.getName();

    @Bind(R.id.showpicPager)
    protected ViewPager viewPager;

    @Bind(R.id.pic_tip)
    protected TextView tipView;

    private ShowPicPagerAdapter showPicPagerAdapter;
    private List<String> imageList = new ArrayList<String>();
    private int currentPosition;// 当前第几张照片
    private int totalCount = 0;
    private String tipText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent(getIntent());
        this.initView();
    }

    private void getDataFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentPosition = bundle.getInt(Constant.CURRENT_POSITION, 0);
            imageList = bundle.getStringArrayList(Constant.IMAGE_LIST);
            totalCount = imageList.size();
        }
    }

    private void setTipText() {
        tipText = (currentPosition + 1) + "/" + totalCount;
        tipView.setText(tipText);
    }

    public void initView() {
        showPicPagerAdapter = new ShowPicPagerAdapter(this, imageList, this);
        viewPager.setAdapter(showPicPagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        viewPager.setOnPageChangeListener(this);
        setTipText();
    }

    @Override
    public void setListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_showpic;
    }

    @Override
    public void onClickItem(int potition) {
        appManager.finishActivity(this);
    }

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
        currentPosition = arg0;
        setTipText();
    }

}
