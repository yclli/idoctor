package com.sjtu.idoctor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 体温数据记录的数据模型
 * @author Zhuolun Li
 */
public class TemperatureRecordCacheBean{
	
	private int id;
	private String name;
	private List<item> tpList;
	
	public TemperatureRecordCacheBean(){
	}
	/**
	 * 构造函数
	 * @param id
	 * @param name
	 */
	public TemperatureRecordCacheBean(int id, String name){
		this.setId(id);
		this.setName(name);
		this.tpList = new ArrayList<TemperatureRecordCacheBean.item>();
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
     * 获取体温记录列表
     * @return tpList
     */
	public List<item> getTpList(){
		return tpList;
	}
	public void setTpList(List<item> tpList){
		this.tpList = tpList;
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
     * 体温记录对象
     */
	public class item{
		
    	int id;
        String temperature;
        String times;
        
        public item(int id, String temperature, String times) {
			this.id = id;
			this.temperature = temperature;
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
         * 获取体温
         * @return temperature
         */
        public String getTemperature(){
        	return temperature;
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
