package com.sjtu.idoctor.model;


public class HeartRateCacheBean{
	
	private int doctorId;
	private String heartRate;
	private String time;
	
	public HeartRateCacheBean(){
	}
	
	public HeartRateCacheBean(int doctorId, String heartRate){
		this.setDoctorId(doctorId);
		this.setHeartRate(heartRate);
	}
	
	public int getDoctorId(){
		return doctorId;
	}
	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}
	
	public String getHeartRate(){
		return heartRate;
	}
	public void setHeartRate(String heartRate){
		this.heartRate = heartRate;
	}
	
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time = time;
	}
}
