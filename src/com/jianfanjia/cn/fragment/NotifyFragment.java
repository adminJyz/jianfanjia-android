package com.jianfanjia.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.MyFragmentPagerAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.SelectItem;
import com.jianfanjia.cn.view.TabPageIndicator;

/**
 * 
 * @ClassName: NotifyFragment
 * @Description: 提醒
 * @author fengliang
 * @date 2015-8-26 下午12:09:18
 * 
 */
public class NotifyFragment extends BaseFragment {
	private static final String TAG = NotifyFragment.class.getName();
	private TabPageIndicator mPageIndicator = null;
	private MyFragmentPagerAdapter adapter = null;
	private ViewPager mPager = null;// 页卡内容
	private List<SelectItem> listViews = new ArrayList<SelectItem>(); // Tab页面列表

	@Override
	public void initView(View view) {
		mPageIndicator = (TabPageIndicator) view
				.findViewById(R.id.page_indicator);
		mPager = (ViewPager) view.findViewById(R.id.vPager);
		initItem();
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	private void initItem() {
		SelectItem caigouItem = new SelectItem(new CaiGouNotifyFragment(),
				"采购提醒");
		SelectItem fukuanItem = new SelectItem(new FuKuanNotifyFragment(),
				"付款提醒");
		SelectItem yanqiItem = new SelectItem(new YanQiNotifyFragment(), "延期提醒");
		listViews.add(caigouItem);
		listViews.add(fukuanItem);
		listViews.add(yanqiItem);
		adapter = new MyFragmentPagerAdapter(fragmentManager, listViews);
		mPager.setAdapter(adapter);
		mPager.setOffscreenPageLimit(1);
		mPageIndicator.setViewPager(mPager, 0);
		mPageIndicator
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int pos) {

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_notify;
	}

}
