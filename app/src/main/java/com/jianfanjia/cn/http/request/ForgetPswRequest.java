package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.JsonParser;

import java.util.Calendar;

/**
 * Description: com.jianfanjia.cn.http.request
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-27 17:40
 */
public class ForgetPswRequest extends BaseRequest{

    public ForgetPswRequest(Context context) {
        super(context);
        url = url_new.UPDATE_PASS_URL;
    }

    @Override
    public void onSuccess(Object data) {
        super.onSuccess(data);
       /* if (data != null) {
            LoginUserBean loginUserBean = JsonParser.jsonToBean((String) data, LoginUserBean.class);
            loginUserBean.setPass(password);
            dataManager.saveLoginUserInfo(loginUserBean);
            dataManager.setLogin(true);
            dataManager.savaLastLoginTime(Calendar.getInstance()
                    .getTimeInMillis());
        }*/
    }


}
