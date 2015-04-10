package com.sjtu.idoctor.model;

import java.io.Serializable;

import com.google.gson.Gson;

public  class User implements Serializable{
	
	private static final long serialVersionUID = 2340304011060815823L;
	
	private int id;
	private int geroId;
	private String name;
	private String username;
	private String password;//set for all user
	private String gender;
	private int age;
    private String photoUrl;
	private String digest;
	
	public User(int id) {
		this.setId(id);
	}
	
    public User(String username, String password) {
        this.setUsername(username); 
        this.setPassword(password);
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public int getGeroId() {
		return geroId;
	}

	public void setGeroId(int geroId) {
		this.geroId = geroId;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhotoUrl() {
		return photoUrl;
	}


	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

    @Override
    public String toString(){
    	Gson gson = new Gson();
    	return gson.toJson(this);
    }
}
