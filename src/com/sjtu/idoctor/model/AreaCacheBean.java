package com.sjtu.idoctor.model;

import java.util.List;

import com.google.gson.Gson;

/**
 * 区域的数据模型
 * @author Zhuolun Li
 */
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
    
    /**
     * 获取ID
     * @return id
     */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
     * 获取直接父节点ID
     * @return parentId
     */
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	/**
     * 获取父节点ID列表
     * @return parentIds
     */
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	/**
     * 获取区域类型
     * @return type
     */
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
     * 获取区域层次（根节点为0）
     * @return level
     */
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	/**
     * 获取区域名称
     * @return name
     */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
     * 获取区域全称
     * @return fullName
     */
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
     * 获取子区域列表
     * @return areaList
     */
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
