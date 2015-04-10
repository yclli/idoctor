package com.sjtu.idoctor.utils;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class OpUtil {
	public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }
	public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
	
    public static String dealItem(String[] input, String name) {
    	String output = "";
    	for(int i=0; i<input.length; i++) {
    		if(!input[i].equals("")) {
    			if(!name.equals(""))
    				output += name+":"+input[i]+",";
    			else
    				output+= input[i]+",";
    		}
    	}
    	return output;
    }
    
    public static String[] listToArray(List<String> l){
    	String[] a = new String[l.size()];
    	for(int i=0;i<l.size();i++){
    		a[i] = l.get(i);
    	}
    	return a;
    }
    public static String[] filterBuilding(List<String> longRoomList,String prefix){
    	// add prefix to get a full entry in preference
    	if(longRoomList==null){
    		return null;
    	}
    	List<String> building_info = new ArrayList<String>();
    	for(int i=0;i<longRoomList.size();++i){
    		String [] slice = longRoomList.get(i).split("-");
    		if(slice.length==4&&!building_info.contains(prefix+slice[0])){
    			building_info.add ( prefix+slice[0]);
    		}
    	}
    	return listToArray(building_info);
    }
    
    public static String[] filterFloor(List<String> longRoomList, String buildingNumber, String prefix){
    	/**/
    	if(longRoomList==null){
    		return null;
    	}
    	List<String> floor_info = new ArrayList<String>();
    	for(int i=0;i<longRoomList.size();++i){
    		String [] slice = longRoomList.get(i).split("-");
    		if(slice.length==4&&slice[0].equals(buildingNumber)&&!floor_info.contains(prefix+slice[1])){
    			floor_info.add(prefix+slice[1]);
    		}
    	}
    	return listToArray(floor_info);
    }
    
    public static String[] filterRoom(List<String> longRoomList,String buildingNumber,String floorNumber, String prefix){
    	if(longRoomList==null){
    		return null;
    	}
    	List<String> room_info = new ArrayList<String>();
    	for(int i=0;i<longRoomList.size();++i){
    		String [] slice = longRoomList.get(i).split("-");
    		if(slice.length==4&&
    				slice[0].equals(buildingNumber)&&
    				slice[1].equals(floorNumber)&&
    				!room_info.contains(prefix+slice[2])){
    			room_info.add ( prefix+slice[2]);
    		}
    	}
    	return listToArray(room_info);
    }

}
