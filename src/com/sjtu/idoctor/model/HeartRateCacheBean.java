package com.sjtu.idoctor.model;


public class HeartRateCacheBean{
	
	public String doctorId;
	public String heartRate;
	public String time;
	
	public HeartRateCacheBean(){
	}
	
	public HeartRateCacheBean(String doctorId, String heartRate){
		this.setDoctorId(doctorId);
		this.setHeartRate(heartRate);
	}
	
	public String getDoctorId(){
		return doctorId;
	}
	public void setDoctorId(String doctorId){
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
