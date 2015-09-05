package com.jianfanjia.cn.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaApiClient;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 
 * @ClassName: UserByOwnerInfoActivity
 * @Description:用户个人信息(业主)
 * @author fengliang
 * @date 2015-8-18 下午12:11:49
 * 
 */
public class UserByOwnerInfoActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = UserByOwnerInfoActivity.class.getName();
	private TextView ownerinfo_back = null;
	private Button btn_confirm = null;

	@Override
	public void initView() {
		ownerinfo_back = (TextView) this.findViewById(R.id.ownerinfo_back);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		get_Owner_Info();
	}

	@Override
	public void setListener() {
		ownerinfo_back.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ownerinfo_back:
			finish();
			break;
		case R.id.btn_confirm:
			break;
		default:
			break;
		}
	}

	private void get_Owner_Info() {
		JianFanJiaApiClient.get_Owner_Info(UserByOwnerInfoActivity.this,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						LogTool.d(TAG, "onStart()");
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						LogTool.d(TAG, "JSONObject response:" + response);
						// try {
						// if (response.has(Constant.DATA)) {
						// progressDialog.dismiss();
						// makeTextShort(getString(R.string.login_success));
						// LoginUserBean loginUserBean = JsonParser
						// .jsonToBean(response.get(Constant.DATA)
						// .toString(),
						// LoginUserBean.class);
						// loginUserBean.setPass(mPassword);
						// MyApplication.getInstance().saveLoginUserInfo(
						// loginUserBean);
						// startActivity(MainActivity.class);
						// finish();
						// } else if (response.has(Constant.ERROR_MSG)) {
						// progressDialog.dismiss();
						// makeTextLong(response.get(Constant.ERROR_MSG)
						// .toString());
						// }
						// } catch (JSONException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// makeTextLong(getString(R.string.tip_login_error_for_network));
						// }
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						LogTool.d(TAG,
								"Throwable throwable:" + throwable.toString());
						makeTextLong(getString(R.string.tip_login_error_for_network));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						LogTool.d(TAG, "throwable:" + throwable);
						makeTextLong(getString(R.string.tip_login_error_for_network));
					};
				});
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_owner_info;
	}

}
