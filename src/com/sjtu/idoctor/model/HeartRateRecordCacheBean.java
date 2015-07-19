package com.sjtu.idoctor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 心率数据记录的数据模型
 * @author Zhuolun Li
 */
public class HeartRateRecordCacheBean{
	
	public int id;
	public String name;
	public List<item> hrList;
	
	public HeartRateRecordCacheBean(){
	}
	/**
	 * 构造函数
	 * @param id
	 * @param name
	 */
	public HeartRateRecordCacheBean(int id, String name){
		this.setId(id);
		this.setName(name);
		this.hrList = new ArrayList<HeartRateRecordCacheBean.item>();
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
     * 获取心率记录列表
     * @return hrList
     */
	public List<item> getHrList(){
		return hrList;
	}
	public void setTpList(List<item> hrList){
		this.hrList = hrList;
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
     * 心率记录对象
     */
	public class item{
		
    	int id;
        int heartRate;
        String times;
        
        public item(int id, int heartRate, String times) {
			this.id = id;
			this.heartRate = heartRate;
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
         * 获取心率
         * @return heartRate
         */
        public int getHeartRate(){
        	return heartRate;
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
