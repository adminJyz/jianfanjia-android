package com.jianfanjia.cn.config;

/**
 * Description:全局变量类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 12:47
 */
public class Global {

    public static final String REGISTER_INFO = "register_info";
    public static final String REGISTER = "register";

    public static final String REQUIREMENT_INFO = "requirement_info";
    public static final String REQUIREMENT_ID = "requirement_id";

    public static final String PROCESS_INFO = "process_info";

    public static final String DESIGNER_INF0 = "designer_info";
    public static final String DESIGNER_ID = "designer_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String EVALUATION = "evaluation";
    public static final String PLAN_ID = "plan_id";
    public static final String PLAN_DETAIL = "plan_datail";
    public static final String IMAGE_ID = "image_id";
    public static final String DESIGNER_NAME = "designer_name";
    public static final String TOPIC_ID = "topic_id";
    public static final String TO = "to";
    public static final String SITE_ID = "site_id";


    public static final String PLAN_STATUS0 = "0";//已预约但没有响应
    public static final String PLAN_STATUS1 = "1";//已拒绝业主
    public static final String PLAN_STATUS2 = "2";//已响应但没有确认量房
    public static final String PLAN_STATUS3 = "3";//提交了方案
    public static final String PLAN_STATUS4 = "4";//方案未被选中
    public static final String PLAN_STATUS5 = "5";//方案已经选中
    public static final String PLAN_STATUS6 = "6";//已确认量房但没有方案
    public static final String PLAN_STATUS7 = "7";//设计师无响应导致响应过期
    public static final String PLAN_STATUS8 = "8";//设计师规定时间内没有提交方案

    public static final String REQUIREMENT_STATUS0 = "0";//未预约任何设计师
    public static final String REQUIREMENT_STATUS1 = "1";//预约过设计师但是没有一个设计师响应过
    public static final String REQUIREMENT_STATUS2 = "2";//有一个或多个设计师响应但没有人量完房
    public static final String REQUIREMENT_STATUS3 = "3";//有一个或多个设计师提交了方案但是没有选定方案
    public static final String REQUIREMENT_STATUS4 = "4";//选定了方案但是还没有配置合同
    public static final String REQUIREMENT_STATUS5 = "5";//配置了工地
    public static final String REQUIREMENT_STATUS6 = "6";//有一个或多个设计师量完房但是没有人上传方案
    public static final String REQUIREMENT_STATUS7 = "7";//配置了合同但是没有配置工地
}
