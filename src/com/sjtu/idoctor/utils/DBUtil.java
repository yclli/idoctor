package com.sjtu.idoctor.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.SharedPreferences;
import android.util.Log;

import com.sjtu.idoctor.model.TemperatureCacheBean;;

public class DBUtil {
	private ArrayList<String> arrayList=new ArrayList<String>();
	private ArrayList<String> brrayList = new ArrayList<String>();
	private ArrayList<String> crrayList = new ArrayList<String>();
	private List<HashMap<String, String>> infoList = new ArrayList<HashMap<String, String>>();
	private HttpConnSoap Soap;// = new HttpConnSoap();
	private String HOST;
	private String GEROID;
	
	public DBUtil(){
		
	}
	
	public DBUtil(SharedPreferences sharedPreferences){
		Soap = new HttpConnSoap(sharedPreferences);
		HOST = sharedPreferences.getString("HOST","");
		GEROID = sharedPreferences.getString("GEROID","");	
		Log.i("bowen","HOST"+HOST);
		Log.i("bowen","GEROID:"+GEROID);
	}
	
	public static Connection getConnection(){
		Connection con =null;
		try{
			//Class.forName("laobanHealthcare.sqlserver");
			//con = DriverManager.getConnection("jdbc:mysql://192.168.0.106:3306/test?useUnicode=true&characterEncoding=UTF-8","root","initial"); 
		}
		catch(Exception e){
			
		}
		
		return con;
	}
	
	
	public String insertECG(String elder_id,String doctor_id,String date_str,String duration_str,String start_time,String data){
		String str = "";
		str = Soap.InsertData("ECGtransmit", elder_id, doctor_id, date_str, duration_str, start_time, data);
		
		return str;
	}
	
	public String getInfo(){
		String list = null;
		
		list = Soap.GetBasicInfo("selectAllDoctor",arrayList,brrayList);
		
		return list;
	}
	
	public List<String> getAllRoom(){
		List<String> list = new ArrayList<String>();
		//List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		infoList.clear();
		arrayList.add("gero_id");
		brrayList.add(GEROID);
		
		Log.i("bowen", "before turn to soap");
		

		InputStream allRoom = Soap.GetInfoList("selectAllRoom",arrayList,brrayList);
		if(allRoom == null){
			for(int i = 0;i<10;i++){
				HashMap<String,String> hasMap = new HashMap<String,String>();
				hasMap.put("elderId",(i+1032)+"");
				hasMap.put("elderName", "elder"+(i+1));
				hasMap.put("elderRoom", "A-"+(i/3+1)+"-"+(i/3+1)+"0"+(i%3+1));
				infoList.add(hasMap);
				list.add(hasMap.get("elderRoom"));
			}
			Log.i("bowen","soap result is empty");
			//return null;
		}else{
			crrayList = XMLParse.paraseCommentInfors (allRoom);
			
			Log.i("bowen", "begin to make list");
			Log.i("bowen", "crraylength"+crrayList.size());
			if(crrayList.size()<3){
				return null;
			}
			try{
				for(int j = 0;j < crrayList.size();j += 3){
					HashMap<String, String> hasMap= new HashMap<String,String>();
					hasMap.put("elderId", crrayList.get(j));
					hasMap.put("elderName", crrayList.get(j+1));
					hasMap.put("elderRoom", crrayList.get(j+2));
					Log.i("bowen",crrayList.get(j)+crrayList.get(j+1)+crrayList.get(j+2));
					
					String item3=crrayList.get(j+2);
					Log.i("bowen","item 3 is:"+item3);
					String localRoomStr = item3.split("-")[0]+"-"+item3.split("-")[1]+"-"+item3.split("-")[2];
					if(!list.contains(localRoomStr)){
						Log.i("bowen","add item 3--"+localRoomStr+"into list");
						list.add(localRoomStr);
					}
					infoList.add(hasMap);
					Log.i("bowen","add to infoList");
				}
			}catch(Exception e){
				
			}
		}	
		
		Log.d("bowen","getAllRoom end");
		
		return list;
	}
	
	public List<HashMap<String,String>> getELder(String roomNo){
		List<HashMap<String,String>> elderList = new ArrayList<HashMap<String,String>>();
		
		if(infoList.size()>0){
			for(int i = 0; i <infoList.size();i++){
				String elderBedStr = infoList.get(i).get("elderRoom");
				String elderRoomNo = elderBedStr.split("-")[0]+'-'+elderBedStr.split("-")[1]+'-'+elderBedStr.split("-")[2];
				Log.d("bowen-elderRoomNo",elderRoomNo);
				if(roomNo.equals(elderRoomNo)){
					HashMap<String,String> hasMap = new HashMap<String,String>();
					hasMap.put("elderId", infoList.get(i).get("elderId"));
					hasMap.put("elderName",infoList.get(i).get("elderName"));
					elderList.add(hasMap);
				}
			}
		}
		
		return elderList;
	}
	
	public List<HashMap<String,String>> getAllDoctor(){
		List<HashMap<String,String>> doctorList = new ArrayList<HashMap<String ,String>>();

		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("gero_id");
		brrayList.add(GEROID);
		
		Log.d("bowen", "before turn to soap");
		
		InputStream allRoom = Soap.GetInfoList("selectAllDoctor",arrayList,brrayList);
		if(allRoom == null){
			return null;
		}
		crrayList = XMLParse.paraseCommentInfors (allRoom);

		for(int j = 0;j < crrayList.size();j += 2){
			HashMap<String, String> hasMap= new HashMap<String,String>();
			hasMap.put("doctorId", crrayList.get(j));
			hasMap.put("doctorName", crrayList.get(j+1));
			
			doctorList.add(hasMap);
		}
		
		return doctorList;
	}
/*
	public List<TempratureCacheBean> getLastNDayTemprature(int elder_id){
		List<TempratureCacheBean> tempratureDataList = new ArrayList<TempratureCacheBean>();
		//List<String> tempratureDataItem = new ArrayList<String>();
		
		InputStream localIs = Soap.GetLastNDaysInspectionData(elder_id, "temprature");
		if(localIs != null){
			tempratureDataList = XMLParse.paraseTempratureInfo(localIs);
		}
		else{
			tempratureDataList = null;
		}		
		
		return tempratureDataList;
	}
	*/
	public boolean UpdateInspection(int elder_id, int doctor_id, String device_type, String result){
		return Soap.UpdateInspection(elder_id, doctor_id, device_type, result);
	}
}





















