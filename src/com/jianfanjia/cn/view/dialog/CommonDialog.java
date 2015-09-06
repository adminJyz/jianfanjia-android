package com.jianfanjia.cn.view.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;

public class CommonDialog extends Dialog {
	public DialogInterface.OnClickListener listener;
	protected View barDivider;
	protected FrameLayout container;
	protected View content;
	private final int contentPadding;

	protected TextView headerVw;
	protected Button negativeBt;
	protected Button positiveBt;
	protected DialogInterface.OnClickListener dismissClick = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};

	public CommonDialog(Context context) {
		this(context, R.style.common_dialog);
	}

	public CommonDialog(Context context, int defStyle) {
		super(context, defStyle);
		contentPadding = (int) getContext().getResources().getDimension(
				R.dimen.space_8);
		init(context);
	}

	protected CommonDialog(Context context, boolean flag,
			DialogInterface.OnCancelListener listener) {
		super(context, flag, listener);
		contentPadding = (int) getContext().getResources().getDimension(
				R.dimen.space_8);
		init(context);
	}

	@SuppressLint("InflateParams")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void init(final Context context) {
		setCancelable(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		content = LayoutInflater.from(context).inflate(
				R.layout.dialog_common, null);
		headerVw = (TextView) content.findViewById(R.id.common_title);
		container = (FrameLayout) content.findViewById(R.id.content_container);
		barDivider = content.findViewById(R.id.button_bar_divider);
		positiveBt = (Button) content.findViewById(R.id.positive_bt);
		negativeBt = (Button) content.findViewById(R.id.negative_bt);
		super.setContentView(content);
	}

	public TextView getTitleTextView() {
		return headerVw;
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.dismiss();
	}

	public void setContent(View view) {
		setContent(view, contentPadding);
	}

	public void setContent(View view, int padding) {
		container.removeAllViews();
		container.setPadding(padding, padding, padding, padding);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		container.addView(view, lp);
	}

	@Override
	public void setContentView(int i) {
		setContent(null);
	}

	@Override
	public void setContentView(View view) {
		setContentView(null, null);
	}

	@Override
	public void setContentView(View view,
			android.view.ViewGroup.LayoutParams layoutparams) {
		throw new Error("Dialog: User setContent (View view) instead!");
	}

	public void setItems(BaseAdapter adapter,
			AdapterView.OnItemClickListener onItemClickListener) {
		ListView listview = new ListView(content.getContext());
		listview.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
		listview.setDivider(null);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(onItemClickListener);
		setContent(listview, 0);
	}

	public void setNegativeButton(int negative,
			DialogInterface.OnClickListener listener) {
		setNegativeButton(getContext().getString(negative), listener);
	}

	public void setNegativeButton(String text,
			final DialogInterface.OnClickListener listener) {
		if (!TextUtils.isEmpty(text)) {
			negativeBt.setText(text);
			negativeBt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					if (listener != null)
						listener.onClick(CommonDialog.this, 0);
					else
						dismissClick.onClick(CommonDialog.this, 0);
				}
			});
			negativeBt.setVisibility(View.VISIBLE);
		} else {
			negativeBt.setVisibility(View.GONE);
		}
		if (positiveBt.getVisibility() == View.VISIBLE
				|| negativeBt.getVisibility() == View.VISIBLE)
			barDivider.setVisibility(View.VISIBLE);
		else
			barDivider.setVisibility(View.GONE);
	}

	public void setPositiveButton(int positive,
			DialogInterface.OnClickListener listener) {
		setPositiveButton(getContext().getString(positive), listener);
	}

	public void setPositiveButton(String positive,
			final DialogInterface.OnClickListener listener) {
		if (!TextUtils.isEmpty(positive)) {
			positiveBt.setText(positive);
			positiveBt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					if (listener != null)
						listener.onClick(CommonDialog.this, 0);
					else
						dismissClick.onClick(CommonDialog.this, 0);
				}
			});
			positiveBt.setVisibility(View.VISIBLE);
		} else {
			positiveBt.setVisibility(View.GONE);
		}
		if (positiveBt.getVisibility() == View.VISIBLE
				|| negativeBt.getVisibility() == View.VISIBLE)
			barDivider.setVisibility(View.VISIBLE);
		else
			barDivider.setVisibility(View.GONE);
	}

	@Override
	public void setTitle(int title) {
		setTitle((getContext().getResources().getString(title)));
	}

	@Override
	public void setTitle(CharSequence title) {
		if (title != null && title.length() > 0) {
			headerVw.setText(title);
			headerVw.setVisibility(View.VISIBLE);
		} else {
			headerVw.setVisibility(View.GONE);
		}
	}
}
