package com.sjtu.idoctor.model;

import java.io.Serializable;

public class DoctorCacheBean extends User implements Serializable {

	private static final long serialVersionUID = -737490097890850021L;
	private int staffId;
	private String photoSrc = "";
	
	public DoctorCacheBean(int id, int staffId, String name, String photoUrl){
		super(id);
		this.setStaffId(staffId);
		super.setName(name);
		this.setPhotoSrc(photoSrc);
	}
	
	public DoctorCacheBean(int id) {
		super(id);
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
	
	public String getPhotoSrc() {
		return photoSrc;
	}
	
	public void setPhotoSrc(String photoSrc) {
		this.photoSrc = photoSrc;
	}
}
