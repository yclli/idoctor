package com.sjtu.idoctor.model;

import java.io.Serializable;

public class DoctorCacheBean extends User implements Serializable {

	private static final long serialVersionUID = -737490097890850021L;
	private int staffId;
	
	
	public DoctorCacheBean(int id) {
		super(id);
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
}
