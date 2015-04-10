package com.sjtu.idoctor.model;

import java.io.Serializable;


public class ElderCacheBean extends User implements Serializable{
	
	private static final long serialVersionUID = -737490097890850021L;
	private int careLevel;
	private int areaId;
	
	public ElderCacheBean(int id) {
		super(id);
	}

	public int getCareLevel() {
		return careLevel;
	}

	public void setCareLevel(int careLevel) {
		this.careLevel = careLevel;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
}
