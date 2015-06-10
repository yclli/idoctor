package com.sjtu.idoctor.model;


public class BloodPressureCacheBean{
	
	private int doctorId;
	private float diastolicPressure;
	private float systolicPressure;
	private String time;
	
	public BloodPressureCacheBean(){
	}
	
	public BloodPressureCacheBean(int doctorId, float diastolicPressure, float systolicPressure){
		this.setDoctorId(doctorId);
		this.setDiastolicPressure(diastolicPressure);
		this.setSystolicPressure(systolicPressure);
	}
	
	public int getDoctorId(){
		return doctorId;
	}
	public void setDoctorId(int doctorId){
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
