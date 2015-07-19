package com.sjtu.idoctor.model;

/**
 * 血压数据的数据模型
 * @author Zhuolun Li
 */
public class BloodPressureCacheBean{
	
	private int doctorId;
	private float diastolicPressure;
	private float systolicPressure;
	private String time;
	
	public BloodPressureCacheBean(){
	}
	/**
	 * 构造函数
	 * @param 医生ID
	 * @param 舒张压
	 * @param 收缩压
	 */
	public BloodPressureCacheBean(int doctorId, float diastolicPressure, float systolicPressure){
		this.setDoctorId(doctorId);
		this.setDiastolicPressure(diastolicPressure);
		this.setSystolicPressure(systolicPressure);
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
     * 获取舒张压
     * @return diastolicPressure
     */
	public float getDiastolicPressure(){
		return diastolicPressure;
	}
	public void setDiastolicPressure(float diastolicPressure){
		this.diastolicPressure = diastolicPressure;
	}
	/**
     * 获取收缩压
     * @return systolicPressure
     */
	public float getSystolicPressure(){
		return systolicPressure;
	}
	public void setSystolicPressure(float systolicPressure){
		this.systolicPressure = systolicPressure;
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
