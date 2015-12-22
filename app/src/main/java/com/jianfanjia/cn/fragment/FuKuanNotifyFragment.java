package com.jianfanjia.cn.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.PayNotifyAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: FuKuanNotifyFragment
 * @Description: 付款提醒
 * @date 2015-8-26 下午1:08:44
 */
public class FuKuanNotifyFragment extends BaseFragment {
    private static final String TAG = FuKuanNotifyFragment.class.getName();
    private RecyclerView fukuanListView = null;
    private List<NotifyMessage> payList = new ArrayList<NotifyMessage>();
    private PayNotifyAdapter payAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        payAdapter = new PayNotifyAdapter(getActivity(), payList);
        fukuanListView.setAdapter(payAdapter);
    }

    @Override
    public void initView(View view) {
        fukuanListView = (RecyclerView) view.findViewById(R.id.tip_pay__listview);
        fukuanListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fukuanListView.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        fukuanListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogTool.d(TAG, "FuKuanNotifyFragment 可见");
            initData();
        } else {
            LogTool.d(TAG, "FuKuanNotifyFragment 不可见");
        }
    }

    private void initData() {
        payList = notifyMessageDao.getNotifyListByType(Constant.FUKUAN_NOTIFY);
        LogTool.d(TAG, "payList:" + payList);
    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fukuan_notify;
    }

}
