package com.sjtu.idoctor.model;

import java.util.List;

public class HttpWrapper<T> {
	private int status;
	private String error;
//	private int total;
//	private String nextPageString;
	private List<T> entities;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public List<T> getEntities() {
		return entities;
	}
	public void setEntities(List<T> entities) {
		this.entities = entities;
	}
	
	public boolean isOk(){
		return status==200;
	}
	
	public T getEntity(){//get single entity
		if(entities!=null && entities.size()>0){
			return entities.get(0);
		}
		return null;
	}
		
}
