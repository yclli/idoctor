package com.sjtu.idoctor.model;

/**
 * 心率数据的数据模型
 * @author Zhuolun Li
 */
public class HeartRateCacheBean{
	
	private int doctorId;
	private String heartRate;
	private String time;
	
	public HeartRateCacheBean(){
	}
	/**
	 * 构造函数
	 * @param doctorId
	 * @param heartRate
	 */
	public HeartRateCacheBean(int doctorId, String heartRate){
		this.setDoctorId(doctorId);
		this.setHeartRate(heartRate);
	}
	/**
     * 获取测量医生ID
     * @return doctorId
     */
	public int getDoctorId(){
		return doctorId;
	}
	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}
	/**
     * 获取心率
     * @return heartRate
     */
	public String getHeartRate(){
		return heartRate;
	}
	public void setHeartRate(String heartRate){
		this.heartRate = heartRate;
	}
	/**
     * 获取测量时间
     * @return time
     */
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time = time;
	}
}
