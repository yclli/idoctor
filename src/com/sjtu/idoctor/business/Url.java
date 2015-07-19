package com.sjtu.idoctor.business;

/**
 * 存放URL路径
 * @author Zhuolun Li
 */
public final class Url {
	
	/**
     * 所有请求的基础URL
     */
	public static final String URL_BASE = "http://121.41.46.189";
	//public static final String URL_BASE = "http://202.120.38.227:8088";
	
    public static final String URL_PREFFIX="/api/service";
    
    /**
     * 获取养老院的全部区域
     */
    public static final String URL_AREAS_FRAG = "/gero/{gid}/area";
    public static final String URL_AREAS = URL_PREFFIX + URL_AREAS_FRAG;
    
    /**
     * 获取养老院某区域的子区域
     */
    public static final String URL_AREA_FRAG =     "/gero/{gid}/area/{aid}";
    public static final String URL_AREA = URL_PREFFIX + URL_AREA_FRAG;
  
    /**
     * 用于URL参数digest的密钥
     */
    public static final String URL_USER_KEY =    "/pad/login";
    
    /**
     * 用户（医生）
     */
    public static final String URL_USER_FRAG =     "/user/{uid}";
    public static final String URL_USER = URL_PREFFIX + URL_USER_FRAG;
    
    /**
     * 员工列表（医生）
     */
    public static final String URL_STAFFS_FRAG = "/gero/{gid}/staff";
    public static final String URL_STAFFS = URL_PREFFIX + URL_STAFFS_FRAG;
    
    /**
     * 获取员工的上班信息列表(养老院维度)
     */
    public static final String URL_SCHEDULE_PLAN_FRAG = "/gero/{gid}/schedule";
    public static final String URL_SCHEDULE_PLAN = URL_PREFFIX + URL_SCHEDULE_PLAN_FRAG;
    
    /**
     * 获取养老院的老人列表
     */
    public static final String URL_USER_ELDERS_FRAG =     "/gero/{gid}/elder";
    public static final String URL_USER_ELDERS = URL_PREFFIX + URL_USER_ELDERS_FRAG;
    
    /**
     * 获取与上传老人体温数据
     */
    public static final String URL_ELDER_TEMPERATURE_FRAG =     "/gero/{gid}/elder/{eid}/temperature";
    public static final String URL_ELDER_TEMPERATURE = URL_PREFFIX + URL_ELDER_TEMPERATURE_FRAG;
    /**
     * 获取与上传老人血压数据
     */
    public static final String URL_ELDER_BLOODPRESSURE_FRAG =     "/gero/{gid}/elder/{eid}/blood_pressure";
    public static final String URL_ELDER_BLOODPRESSURE = URL_PREFFIX + URL_ELDER_BLOODPRESSURE_FRAG;
    /**
     * 获取与上传老人心率数据
     */
    public static final String URL_ELDER_HEARTRATE_FRAG =     "/gero/{gid}/elder/{eid}/heart_rate";
    public static final String URL_ELDER_HEARTRATE = URL_PREFFIX + URL_ELDER_HEARTRATE_FRAG;
    
}
