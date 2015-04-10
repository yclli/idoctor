package com.sjtu.idoctor.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.SharedPreferences;
import android.util.Log;

import com.sjtu.idoctor.model.AreaCacheBean;
import com.sjtu.idoctor.model.ElderCacheBean;
import com.sjtu.idoctor.model.User;
import com.sjtu.idoctor.service.IdoctorService;

public class DBUtils {

	IdoctorService idocService;
	User adminUser;
	
	
	public DBUtils(SharedPreferences preferences){
		adminUser = new User(preferences.getInt("id", 0));
		adminUser.setUsername(preferences.getString("username", ""));
		adminUser.setGeroId(preferences.getInt("geroId", 0));
		adminUser.setDigest(preferences.getString("digest", ""));
		idocService = new IdoctorService(adminUser.getDigest(), adminUser);
	}
	
	public List<HashMap<String,String>> getAllRoom(){
		List<HashMap<String,String>> roomList = new ArrayList<HashMap<String,String>>();
		List<AreaCacheBean> areas = new ArrayList<AreaCacheBean>();
		AreaCacheBean building = new AreaCacheBean();
		AreaCacheBean floor = new AreaCacheBean();
		AreaCacheBean room = new AreaCacheBean();
		String roomString = "";
		int pId = 0;
		areas = idocService.getAreas(1, pId);
		for(int i=0; i<areas.size(); i++){
			building = areas.get(i);
			String buildingPart = building.getName();
			List<AreaCacheBean> floors = idocService.getAreas(2, building.getId());
			for(int j=0; j<floors.size(); j++){
				floor = floors.get(j);
				String floorPart = floor.getName();
				List<AreaCacheBean> rooms = idocService.getAreas(3, floor.getId());
				for(int k=0; k<rooms.size(); k++){
					room = rooms.get(k);
					roomString = buildingPart+'-'+floorPart+'-'+room.getName();
					HashMap<String,String> hashMap = new HashMap<String,String>();
					hashMap.put("roomName", roomString);
					hashMap.put("areaId", room.getId()+"");
					roomList.add(hashMap);
					roomString = "";
				}
			}	
		}
		return roomList;
	}
	
	public List<HashMap<String,String>> getELder(String areaId){
		List<HashMap<String, String>> elderList = new ArrayList<HashMap<String, String>>();
		List<ElderCacheBean> elders = new ArrayList<ElderCacheBean>();
		ElderCacheBean elder;
		elders = idocService.getElderByArea(Integer.parseInt(areaId));
		for (int i=0; i<elders.size(); i++){
			elder = elders.get(i);
			HashMap<String,String> hashMap = new HashMap<String,String>();
			hashMap.put("elderName", elder.getName());
			hashMap.put("elderId", elder.getId()+"");
			elderList.add(hashMap);
			
		}
		return elderList;
	}
	
}
