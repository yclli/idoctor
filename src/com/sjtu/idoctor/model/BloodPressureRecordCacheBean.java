package com.sjtu.idoctor.model;

import java.util.ArrayList;
import java.util.List;


public class BloodPressureRecordCacheBean{
	
	public int id;
	public String name;
	public List<item> bpList;
	
	public BloodPressureRecordCacheBean(){
	}
	
	public BloodPressureRecordCacheBean(int id, String name){
		this.setId(id);
		this.setName(name);
		this.bpList = new ArrayList<BloodPressureRecordCacheBean.item>();
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public List<item> getBpList(){
		return bpList;
	}
	public void setBpList(List<item> bpList){
		this.bpList = bpList;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public class item{
		
    	int id;
        float diastolicPressure;
        float systolicPressure;
        String times;
        
        public item(int id, float diastolicPressure, float systolicPressure, String times) {
			this.id = id;
			this.diastolicPressure = diastolicPressure;
			this.systolicPressure = systolicPressure;
			this.times = times;
		}
        
        public int getId(){
        	return id;
        }
        
        public float getDiastolicPressure(){
        	return diastolicPressure;
        }
        
        public float getSystolicPressure(){
        	return systolicPressure;
        }
        
        public String getTimes(){
        	return times;
        }
    }
	
}
