package com.sjtu.idoctor.model;

import java.io.Serializable;

/**
 * 医生的数据模型
 * @author Zhuolun Li
 *
 */
public class DoctorCacheBean extends User implements Serializable {

	private static final long serialVersionUID = -737490097890850021L;
	private int staffId;
	private String photoSrc = "";
	
	/**
	 * 构造函数
	 * @param id
	 * @param staffId
	 * @param name
	 * @param photoUrl
	 */
	public DoctorCacheBean(int id, int staffId, String name, String photoUrl){
		super(id);
		this.setStaffId(staffId);
		super.setName(name);
		this.setPhotoSrc(photoSrc);
	}
	/**
	 * 构造函数
	 * @param id
	 */
	public DoctorCacheBean(int id) {
		super(id);
	}
	/**
	 * 获取医生ID
	 * @param staffId
	 */
	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
	/**
	 * 获取医生图像URL
	 * @param photoSrc
	 */
	public String getPhotoSrc() {
		return photoSrc;
	}
	
	public void setPhotoSrc(String photoSrc) {
		this.photoSrc = photoSrc;
	}
}
