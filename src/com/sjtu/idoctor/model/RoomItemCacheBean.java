package com.sjtu.idoctor.model;

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
	
	public String getBuilding(){
		return building;
	}
	
	public void setFloor(String floor){
		this.floor = floor;
	}
	
	public String getFloor(){
		return floor;
	}
	
	public void setRoomNo(String roomNo){
		this.roomNo = roomNo;
	}
	
	public String getRoomNo(){
		return roomNo;
	}
	
	public void setAreaId(String areaId){
		this.areaId = areaId;
	}
	
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
