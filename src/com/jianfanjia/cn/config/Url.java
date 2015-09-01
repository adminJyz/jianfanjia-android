package com.jianfanjia.cn.config;

/**
 * 
 * @ClassName: Url
 * @Description:服务器地址及相关接口类
 * @author fengliang
 * @date 2015-8-18 下午12:06:07
 * 
 */
public class Url {
//	public static String SEVER_IP = "www.jianfanjia.com";
	public static String SEVER_IP = "192.168.1.107";
	public static String SEVER_PORT = "80";
	public static final String HTTPROOT = "http://" + SEVER_IP + ":"
			+ SEVER_PORT + "/api/v1/";
	// 登录
	public static final String LOGIN_URL = HTTPROOT + "login";
	// 注册
	public static final String REGISTER_URL = HTTPROOT + "signup";
	// 获取短信验证码
	public static final String GET_CODE_URL = HTTPROOT + "send_verify_code";
	// 忘记密码
	public static final String UPDATE_PASS_URL = HTTPROOT + "update_pass";
	// 登出
	public static final String SIGNOUT_URL = HTTPROOT + "signout";
	// 业主获取需求
	public static final String REQUIREMENT = HTTPROOT + "user/requirement";
	// 业主发送工地配置和配置工地
	public static final String PROCESS = HTTPROOT + "user/process";
	// 获取业主的设计师
	public static final String GET_OWER_DESIGNER = "user/designer";
	// 获取业主的个人资料
	public static final String GET_OWER_INFO = "user/info";
	// 获取设计师的个人资料
	public static final String GET_DESIGNER_INFO = "designer/info";
	// 获取设计师的业主
	public static final String GET_DESIGNER_OWNER = "designer/user";
	//获取设计师的工地列表
	public static final String GET_DESIGNER_PROCESS ="designer/process/list";
	
	
	//上传图片到装修流程
	public static final String POST_PROCESS_IMAGE = "process/image";
	//评价装修流程
	public static final String POST_PROCESS_COMMENT ="process/comment";
	
	//获取图片
	public static final String GET_IMAGE ="image/";
	
	
}
