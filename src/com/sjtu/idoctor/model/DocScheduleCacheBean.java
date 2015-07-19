package com.sjtu.idoctor.model;

import java.sql.Date;

/**
 * 医生排班信息的数据模型
 * @author Zhuolun Li
 *
 */
public class DocScheduleCacheBean {
	private int id;
	private int staffId;
	private String name;
	
	/**
	 * 获取员工ID
	 * @return staffId
	 */
	public int getStaffId() {
		return staffId;
	}
	
	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}
	/**
	 * 获取信息ID
	 * @return id
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 获取员工名称
	 * @return name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
