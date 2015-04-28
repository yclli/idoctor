package com.sjtu.idoctor.model;

import java.sql.Date;


public class DocScheduleCacheBean {
	private int id;
	private int staffId;
	private String name;
	
	public int getStaffId() {
		return staffId;
	}
	
	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
