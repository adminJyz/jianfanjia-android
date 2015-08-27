package com.jianfanjia.cn.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * @ClassName: ProcessViewPager
 * @Description: �Զ���ViewPager
 * @author fengliang
 * @date 2015-8-27 ����10:57:31
 * 
 */
public class ProcessViewPager extends ViewPager {

	public ProcessViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		this.getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

}
