package com.sjtu.idoctor.business;

public final class Url {
//	public static final String HOME_URL = "http://202.120.38.227";
//	public static final String SOAP_URL = "http://202.120.38.227/laobanHealthcare/Service1.asmx/";
//	public static final String IMG_URL = "http://202.120.38.227:8088/HouseCare/get_elder_photo.jsp";
//	public static final String DOWNLOAD_URL = "http://202.120.38.227:8088/HouseCare/get_elder_photo.jsp";
	public static final String HOME_URL = "http://121.41.46.189";
	public static final String SOAP_URL = "http://121.41.46.189:8088/icare/Service1.asmx/";
	public static final String IMG_URL = "http://121.41.46.189/HouseCare/elderphoto";
	public static final String IMG_URL_STAFF = "http://121.41.46.189/HouseCare/staffphoto";
//	public static final String UPDATE_URL = "http://121.41.46.189:8080/HouseCare/download";
	public static final String UPDATE_URL = "http://121.41.46.189/HouseCare/upload/icarer.apk";
	
	/**
     * Base URL for all requests
     */
    public static final String URL_BASE = "http://202.120.38.227:8088";
    
    public static final String URL_PREFFIX="/resthouse/api/service";
//    public static final String URL_PREFFIX =   "/resthouse/api/web";
    
    public static final String URL_DOWNLOAD_FRAG = "/download";
    public static final String URL_DOWNLOAD = URL_PREFFIX + URL_DOWNLOAD_FRAG;
    
    //区域
    public static final String URL_AREAS_FRAG = "/gero/{gid}/area";
    public static final String URL_AREAS = URL_PREFFIX + URL_AREAS_FRAG;
    
    public static final String URL_AREA_FRAG =     "/gero/{gid}/area/{aid}";
    public static final String URL_AREA = URL_PREFFIX + URL_AREA_FRAG;
    
    //用于URL参数digest的密钥
    public static final String URL_USER_KEY =    "/resthouse/pad/login";
    
    //用户（医生）
    public static final String URL_USER_FRAG =     "/user/{uid}";
    public static final String URL_USER = URL_PREFFIX + URL_USER_FRAG;
    
    //医生列表
    public static final String URL_STAFFS_FRAG = "/gero/{gid}/staff";
    public static final String URL_STAFFS = URL_PREFFIX + URL_STAFFS_FRAG;
    
    // 获取某员工的上班信息列表
    //public static final String URL_SCHEDULE_PLAN_FRAG = "/gero/{gid}/staff/{sid}/schedule";
    //public static final String URL_SCHEDULE_PLAN = URL_PREFFIX + URL_SCHEDULE_PLAN_FRAG;
    
    //老人
    public static final String URL_USER_ELDERS_FRAG =     "/gero/{gid}/elder";
    public static final String URL_USER_ELDERS = URL_PREFFIX + URL_USER_ELDERS_FRAG;
    
    //老人健康数据
    public static final String URL_ELDER_TEMPERATURE_FRAG =     "/gero/{gid}/elder/{eid}/temperature";
    public static final String URL_ELDER_TEMPERATURE = URL_PREFFIX + URL_ELDER_TEMPERATURE_FRAG;
    public static final String URL_ELDER_BLOODPRESSURE_FRAG =     "/gero/{gid}/elder/{eid}/blood_pressure";
    public static final String URL_ELDER_BLOODPRESSURE = URL_PREFFIX + URL_ELDER_BLOODPRESSURE_FRAG;
    public static final String URL_ELDER_HEARTRATE_FRAG =     "/gero/{gid}/elder/{eid}/heart_rate";
    public static final String URL_ELDER_HEARTRATE = URL_PREFFIX + URL_ELDER_HEARTRATE_FRAG;
    
}
