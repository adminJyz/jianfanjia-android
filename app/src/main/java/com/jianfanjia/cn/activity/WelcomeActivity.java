package com.jianfanjia.cn.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.UpdateVersion;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * @author fengliang
 * @ClassName: WelcomeActivity
 * @Description: 欢迎
 * @date 2015-8-29 上午9:30:21
 */
public class WelcomeActivity extends BaseActivity implements ApiUiUpdateListener {
    private Handler handler = new Handler();
    private boolean first;// 用于判断导航界面是否显示
    private boolean isLoginExpire;// 是否登录过去
    private boolean isLogin;// 是否登录过
    private UpdateVersion updateVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        first = dataManager.isFirst();
        LogTool.d(this.getClass().getName(), "first=" + first);
        checkVersion();
//        initData();
    }

    // 检查版本
    private void checkVersion() {
        UiHelper.checkNewVersion(this, new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        if (data != null) {
                            updateVersion = JsonParser
                                    .jsonToBean(data.toString(),
                                            UpdateVersion.class);
                            if (updateVersion != null) {
                                if (Integer.parseInt(updateVersion
                                        .getVersion_code()) > MyApplication
                                        .getInstance().getVersionCode()) {
                                    showNewVersionDialog(WelcomeActivity.this,
                                            String.format(getString(R.string.new_version_message),
                                                    updateVersion.getVersion_name()),
                                            updateVersion);
                                } else {
//                                    makeTextLong(getString(R.string.no_new_version));
                                    handler.postDelayed(runnable, 2000);
                                }
                            }
                        }
                    }

                    @Override
                    public void loadFailture(String error_msg) {

                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



    /**
     * 显示新版本对话框
     *
     * @param activity
     * @param message
     * @param updateVersion
     */
    public void showNewVersionDialog(final Activity activity, String message, final UpdateVersion updateVersion) {
        CommonDialog dialog = DialogHelper
                .getPinterestDialog(activity);
        dialog.setTitle("版本更新");
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UiHelper.startUpdateService(activity, updateVersion.getDownload_url());
                        if(updateVersion.getUpdatetype().equals(Global.REC_UPDATE)){
                            handler.postDelayed(runnable, 2000);
                        }
                    }

                });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (updateVersion.getUpdatetype().equals(Global.REC_UPDATE)) {
                    handler.postDelayed(runnable, 2000);
                } else {
                    WelcomeActivity.this.finish();
                }
            }

        });
        dialog.show();
    }

    protected void initData(){
        handler.postDelayed(runnable, 2000);
    }

    @Override
    public void initView() {
        LogTool.d(this.getClass().getName(), "initView");
//        handler.postDelayed(runnable, 2000);
    }

    @Override
    public void setListener() {
        // TODO Auto-generated method stub
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loadFailture(String error_msg) {
        startActivity(LoginNewActivity_.class);
        finish();
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (!first) {
                isLogin = dataManager.isLogin();
                isLoginExpire = dataManager.isLoginExpire();
                LogTool.d(this.getClass().getName(), "not first");
                if (!isLogin) {
                    LogTool.d(this.getClass().getName(), "not login");
                    startActivity(LoginNewActivity_.class);
                    finish();
                } else {
                    if (!isLoginExpire) {// 登录未过期，添加cookies到httpclient记录身份
                        LogTool.d(this.getClass().getName(), "not expire");
                        startActivity(MainActivity.class);
                        finish();
                    } else {
                        LogTool.d(this.getClass().getName(), "expire");
                        MyApplication.getInstance().clearCookie();
                        JianFanJiaClient.login(WelcomeActivity.this, dataManager.getAccount(), dataManager.getPassword(), WelcomeActivity.this, WelcomeActivity.this);
                    }
                }
            } else {
                LogTool.d(this.getClass().getName(), "启动导航");
                startActivity(NavigateActivity.class);
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

}
