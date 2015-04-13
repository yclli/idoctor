package com.sjtu.idoctor.model;


public class TemperatureCacheBean{
	
	public int doctorId;
	public String temperature;
	public String time;
	
	public TemperatureCacheBean(){
	}
	
	public TemperatureCacheBean(int doctorId, String temprature){
		this.setDoctorId(doctorId);
		this.setTemperature(temprature);
	}
	
	public int getDoctorId(){
		return doctorId;
	}
	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}
	
	public String getTemperature(){
		return temperature;
	}
	public void setTemperature(String temprature){
		this.temperature = temprature;
	}
	
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time = time;
	}
}
