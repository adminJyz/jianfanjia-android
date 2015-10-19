package com.jianfanjia.cn.http.request;

import android.content.Context;

import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.bean.DesignerUpdateInfo;

public class UserByDesignerInfoUpdateRequestApi extends BaseRequest{
	
	private DesignerUpdateInfo designerUpdateInfo;
	
	public UserByDesignerInfoUpdateRequestApi(Context context, DesignerUpdateInfo designerUpdateInfo) {
		super(context);
		this.designerUpdateInfo = designerUpdateInfo;
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
		super.onSuccess(data);
		/*String data = baseResponse.getData().toString();
		if(data != null){
			DesignerInfo designerInfo = JsonParser.jsonToBean(data,
					DesignerInfo.class);
			dataManager.setDesignerInfo(designerInfo);
		}*/
	}

	public DesignerUpdateInfo getDesignerUpdateInfo() {
		return designerUpdateInfo;
	}

	public void setDesignerUpdateInfo(DesignerUpdateInfo designerUpdateInfo) {
		this.designerUpdateInfo = designerUpdateInfo;
	}
	
}
