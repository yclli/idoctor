package com.sjtu.idoctor.model;

/**
 * 体温数据的数据模型
 * @author Zhuolun Li
 */
public class TemperatureCacheBean{
	
	private int doctorId;
	private String temperature;
	private String time;
	
	public TemperatureCacheBean(){
	}
	/**
	 * 构造函数
	 * @param doctorId
	 * @param temprature
	 */
	public TemperatureCacheBean(int doctorId, String temprature){
		this.setDoctorId(doctorId);
		this.setTemperature(temprature);
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
     * 获取体温
     * @return temperature
     */
	public String getTemperature(){
		return temperature;
	}
	public void setTemperature(String temprature){
		this.temperature = temprature;
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
