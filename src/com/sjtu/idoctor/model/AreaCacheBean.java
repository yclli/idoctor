package com.sjtu.idoctor.model;

import java.util.List;

import com.google.gson.Gson;

public class AreaCacheBean {
    private int id;
    private int parentId;
    private String parentIds;
    private int type;
    private int level;
    private String name;
    private String fullName;
    private List<AreaCacheBean> areaList;
    
    public AreaCacheBean() {
	}
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<AreaCacheBean> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<AreaCacheBean> areaList) {
		this.areaList = areaList;
	}
	
	@Override
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
