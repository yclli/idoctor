package com.sjtu.idoctor.model;

import java.util.ArrayList;
import java.util.List;


public class HeartRateRecordCacheBean{
	
	public int id;
	public String name;
	public List<item> hrList;
	
	public HeartRateRecordCacheBean(){
	}
	
	public HeartRateRecordCacheBean(int id, String name){
		this.setId(id);
		this.setName(name);
		this.hrList = new ArrayList<HeartRateRecordCacheBean.item>();
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public List<item> getHrList(){
		return hrList;
	}
	public void setTpList(List<item> hrList){
		this.hrList = hrList;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public class item{
		
    	int id;
        int heartRate;
        String times;
        
        public item(int id, int heartRate, String times) {
			this.id = id;
			this.heartRate = heartRate;
			this.times = times;
		}
        
        public int getId(){
        	return id;
        }
        
        public int getHeartRate(){
        	return heartRate;
        }
        
        public String getTimes(){
        	return times;
        }
    }
	
}
