package com.sjtu.idoctor.model;


public class BloodPressureCacheBean{
	
	public String doctorId;
	public float diastolicPressure;
	public float systolicPressure;
	public String time;
	
	public BloodPressureCacheBean(){
	}
	
	public BloodPressureCacheBean(String doctorId, float diastolicPressure, float systolicPressure){
		this.setDoctorId(doctorId);
		this.setDiastolicPressure(diastolicPressure);
		this.setSystolicPressure(systolicPressure);
	}
	
	public String getDoctorId(){
		return doctorId;
	}
	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}
	
	public float getDiastolicPressure(){
		return diastolicPressure;
	}
	public void setDiastolicPressure(float diastolicPressure){
		this.diastolicPressure = diastolicPressure;
	}
	
	public float getSystolicPressure(){
		return systolicPressure;
	}
	public void setSystolicPressure(float systolicPressure){
		this.systolicPressure = systolicPressure;
	}
	
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time = time;
	}
}
