package com.sjtu.idoctor.model;

import java.util.ArrayList;
import java.util.List;


public class TemperatureRecordCacheBean{
	
	public int id;
	public String name;
	public List<item> tpList;
	
	public TemperatureRecordCacheBean(){
	}
	
	public TemperatureRecordCacheBean(int id, String name){
		this.setId(id);
		this.setName(name);
		this.tpList = new ArrayList<TemperatureRecordCacheBean.item>();
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public List<item> getTpList(){
		return tpList;
	}
	public void setTpList(List<item> tpList){
		this.tpList = tpList;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public class item{
		
    	int id;
        String temperature;
        String times;
        
        public item(int id, String temperature, String times) {
			this.id = id;
			this.temperature = temperature;
			this.times = times;
		}
        
        public int getId(){
        	return id;
        }
        
        public String getTemperature(){
        	return temperature;
        }
        
        public String getTimes(){
        	return times;
        }
    }
	
}
