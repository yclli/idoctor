package com.sjtu.idoctor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 血压数据记录的数据模型
 * @author Zhuolun Li
 */
public class BloodPressureRecordCacheBean{
	
	private int id;
	private String name;
	private List<item> bpList;
	
	public BloodPressureRecordCacheBean(){
	}
	/**
	 * 构造函数
	 * @param 老人ID
	 * @param 老人名称
	 */
	public BloodPressureRecordCacheBean(int id, String name){
		this.setId(id);
		this.setName(name);
		this.bpList = new ArrayList<BloodPressureRecordCacheBean.item>();
	}
	/**
     * 获取老人ID
     * @return id
     */
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	/**
     * 获取血压记录列表
     * @return bpList
     */
	public List<item> getBpList(){
		return bpList;
	}
	public void setBpList(List<item> bpList){
		this.bpList = bpList;
	}
	/**
     * 获取老人名称
     * @return name
     */
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	/**
     * 血压记录对象
     */
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
        /**
         * 获取测量医生ID
         * @return id
         */
        public int getId(){
        	return id;
        }
        /**
         * 获取舒张压
         * @return diastolicPressure
         */
        public float getDiastolicPressure(){
        	return diastolicPressure;
        }
        /**
         * 获取收缩压
         * @return systolicPressure
         */
        public float getSystolicPressure(){
        	return systolicPressure;
        }
        /**
         * 获取记录时间
         * @return time
         */
        public String getTimes(){
        	return times;
        }
    }
	
}
