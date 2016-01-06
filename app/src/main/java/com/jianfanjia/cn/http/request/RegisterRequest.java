package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Calendar;

public class RegisterRequest extends BaseRequest {

    private RegisterInfo registerInfo;

    public RegisterRequest(Context context, RegisterInfo registerInfo) {
        super(context);
        this.registerInfo = registerInfo;
        url = url_new.REGISTER_URL;
    }

    @Override
    public void all() {
        // TODO Auto-generated method stub
        super.all();

    }

    @Override
    public void pre() {
        // TODO Auto-generated method stub
        super.pre();
    }

    @Override
    public void onSuccess(Object data) {
        if (data != null) {
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
            LoginUserBean loginUserBean = JsonParser.jsonToBean(data.toString(),LoginUserBean.class);
            loginUserBean.setPass(registerInfo.getPass());
            dataManager.saveLoginUserBean(loginUserBean);
        }
    }

}
