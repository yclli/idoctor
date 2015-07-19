package com.sjtu.idoctor.model;

import java.io.Serializable;

/**
 * 老人的数据模型
 * @author Zhuolun Li
 *
 */
public class ElderCacheBean extends User implements Serializable{
	
	private static final long serialVersionUID = -737490097890850021L;
	private int elderId;
	private int careLevel;
	private int areaId;
	
	public ElderCacheBean(int id) {
		super(id);
	}
	/**
	 * 获取老人护理等级
	 * @return careLevel
	 */
	public int getCareLevel() {
		return careLevel;
	}

	public void setCareLevel(int careLevel) {
		this.careLevel = careLevel;
	}
	/**
	 * 获取老人所在区域ID
	 * @return areaId
	 */
	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取老人ID
	 * @return elderId
	 */
	public int getElderId() {
		return elderId;
	}

	public void setElderId(int elderId) {
		this.elderId = elderId;
	}
}
