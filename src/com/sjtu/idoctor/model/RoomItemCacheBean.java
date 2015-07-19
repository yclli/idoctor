package com.sjtu.idoctor.model;

/**
 * 房间的数据模型<br>
 * <p>这个模型是一个与老系统主页面对接的对象,将区域ID与房间联系起来
 * @author Zhuolun Li
 */
public class RoomItemCacheBean implements Comparable {
	
	private String building;
	private String floor;
	private String roomNo;
	private String areaId;
	
	public RoomItemCacheBean(){
	}
	
	public void setBuilding(String building){
		this.building = building;
	}
	/**
	 * 获取房间楼栋
	 * @return building
	 */
	public String getBuilding(){
		return building;
	}
	
	public void setFloor(String floor){
		this.floor = floor;
	}
	/**
	 * 获取房间楼层
	 * @return floor
	 */
	public String getFloor(){
		return floor;
	}
	
	public void setRoomNo(String roomNo){
		this.roomNo = roomNo;
	}
	/**
	 * 获取房间号码
	 * @return roomNo
	 */
	public String getRoomNo(){
		return roomNo;
	}
	
	public void setAreaId(String areaId){
		this.areaId = areaId;
	}
	/**
	 * 获取房间的区域ID
	 * @return areaId
	 */
	public String getAreaId(){
		return areaId;
	}
	
	@Override
	public int compareTo(Object o){
		RoomItemCacheBean a = (RoomItemCacheBean) o;
		String room = a.getRoomNo();
		return this.roomNo.compareTo(room);
	}
}
