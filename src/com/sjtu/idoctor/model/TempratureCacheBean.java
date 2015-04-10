package com.sjtu.idoctor.model;


public class TempratureCacheBean{
	
	public String doctorId;
	public String temprature;
	public String time;
	
	public TempratureCacheBean(){
	}
	
	public TempratureCacheBean(String doctorId, String temprature){
		this.setDoctorId(doctorId);
		this.setTemprature(temprature);
	}
	
	public String getDoctorId(){
		return doctorId;
	}
	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}
	
	public String getTemprature(){
		return temprature;
	}
	public void setTemprature(String temprature){
		this.temprature = temprature;
	}
	
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time = time;
	}
}
