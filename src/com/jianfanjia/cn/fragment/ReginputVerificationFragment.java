package com.jianfanjia.cn.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.MainActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.cache.DataManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.interf.FragmentListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.NetTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * @version 1.0
 * @Description 注册选择角色Fragment
 * @author zhanghao
 * @date 2015-8-21 上午9:15
 * 
 */
public class ReginputVerificationFragment extends BaseFragment {
	private static final String TAG = ReginputVerificationFragment.class
			.getName();
	private FragmentListener fragemntListener = null;
	private Button nextView = null;// 下一步
	private TextView backView = null;// 返回
	private EditText mEdVerif = null;// 验证码输入框
	private ImageView indicatorView = null;// 指示器
	private TextView proTipView = null;// 提示操作

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fragemntListener = (FragmentListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView(View view) {
		nextView = (Button) view.findViewById(R.id.btn_commit);
		backView = (TextView) view.findViewById(R.id.goback);
		mEdVerif = (EditText) view.findViewById(R.id.et_verification);
		indicatorView = (ImageView) view.findViewById(R.id.indicator);
		indicatorView.setImageResource(R.drawable.rounded_register3);
		proTipView = (TextView) view.findViewById(R.id.register_pro);
		proTipView.setText(getString(R.string.verification_code_sended));
	}

	@Override
	public void setListener() {
		nextView.setOnClickListener(this);
		backView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:
			String verif = mEdVerif.getText().toString().trim();
			if (checkInput(verif)) {
				MyApplication.getInstance().getRegisterInfo().setCode(verif);
				makeTextLong(MyApplication.getInstance().getRegisterInfo()
						.toString());
				register(MyApplication.getInstance().getRegisterInfo());
			}
			break;
		case R.id.goback:
			fragemntListener.onBack();
			break;
		default:
			break;
		}
	}

	private boolean checkInput(String verifCode) {
		if (TextUtils.isEmpty(verifCode)) {
			makeTextShort(getResources().getString(R.string.hint_verification));
			mEdVerif.requestFocus();
			return false;
		}
		if (!NetTool.isNetworkAvailable(getActivity())) {
			makeTextShort(getResources().getString(R.string.tip_no_internet));
			return false;
		}
		return true;
	}

	/**
	 * 注册提交
	 * 
	 * @param registerInfo
	 */
	private void register(RegisterInfo registerInfo) {
		JianFanJiaApiClient.register(getActivity(), registerInfo,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						try {
							if (response.has(Constant.DATA)) {
								makeTextShort(getString(R.string.register_success));
								LoginUserBean loginUserBean = JsonParser
										.jsonToBean(response.get(Constant.DATA)
												.toString(),
												LoginUserBean.class);
								DataManager.getInstance().saveLoginUserInfo(
										loginUserBean);
								dataManager.setLogin(true);
								startActivity(MainActivity.class);
								getActivity().finish();
							} else if (response.has(Constant.ERROR_MSG)) {
								makeTextLong(response.get(Constant.ERROR_MSG)
										.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							makeTextLong(getString(R.string.register_failure));
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_no_internet));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_no_internet));
					};
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_register_input_verification;
	}
}
