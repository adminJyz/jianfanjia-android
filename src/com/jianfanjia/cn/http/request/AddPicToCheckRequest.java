package com.jianfanjia.cn.http.request;

import java.util.Calendar;

import android.content.Context;

import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseRequest;
import com.jianfanjia.cn.base.BaseResponse;
import com.jianfanjia.cn.bean.LoginUserBean;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.tools.LogTool;

public class AddPicToCheckRequest extends BaseRequest {
	
	private String processId;
	
	private String section;
	
	private String key;
	
	private String imageId;
	
	public AddPicToCheckRequest(Context context,String processId,String section,String key,String imageId) {
		super(context);
		this.processId = processId;
		this.section = section;
		this.key = key;
		this.imageId = imageId;
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
	public void onSuccess(BaseResponse baseResponse) {
		if (baseResponse.getMsg() != null) {
			ProcessInfo processInfo = dataManager.getDefaultProcessInfo();
			if(processInfo != null){
				LogTool.d(this.getClass().getName(), "processInfo != null");
				processInfo.addImageToCheck(section, key, imageId);
				dataManager.setCurrentProcessInfo(processInfo);
				dataManager.saveProcessInfo(processInfo);
			}
		}
	}
	
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}


}
