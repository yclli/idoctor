package com.sjtu.idoctor.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Xml;

public class HttpConnSoap {
	public final String TIME_OUT = "操作超时";
	public String HOST;
	public String GEROID;
	
	public HttpConnSoap(SharedPreferences sharedPreferences){
		HOST = sharedPreferences.getString("HOST","");
		GEROID = sharedPreferences.getString("GEROID","");		
	}
	
	public String InsertData(String methodName,String elder_id,String doctor_id,String date_str,String duration_str,String start_time,String data){
		String resp = "";
		String ServerUrl = HOST+methodName+"?elder_id="+elder_id+"&doctor_id="+doctor_id+"&date_str="+date_str+"&duration_str="+"&start_time="+start_time+"&data="+data;
		
		Log.d("bowen",ServerUrl);
		
		HttpGet  httpRequest  = new HttpGet(ServerUrl);
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(httpRequest);
			if(httpResp.getStatusLine().getStatusCode()==200){
				resp = EntityUtils.toString(httpResp.getEntity(),"UTF-8");
			}
			else{
				Log.d("bowen",Integer.toString(httpResp.getStatusLine().getStatusCode()));
			}
		}
		catch(Exception e){
			resp="error";
		}
		
		return resp;
	}
	
	public InputStream GetDevicesByRoom(String roomNo){
		InputStream list = null;
		String ServerUrl = HOST+"selectBluetoothDeviceByRoom?roomNo="+roomNo;
		InputStream result = null;
		HttpGet httpRequest = new HttpGet(ServerUrl);
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(httpRequest);
			
			if(httpResp.getStatusLine().getStatusCode()==200){
				list = httpResp.getEntity().getContent();
				//result = EntityUtils.toString(httpResp.getEntity(),"UTF-8");
			}
		}catch(Exception e){
		}
		
		return list;
	}
	
	public String GetBasicInfo(String methodName,ArrayList<String> Parameters, ArrayList<String> ParValues){
		List<NameValuePair> params = null;
		List<String> list = new ArrayList<String>();
		String ServerUrl = HOST+methodName;
		Log.d("bowen", ServerUrl);
		String soapAction = "http://tempuri.org/"+methodName;
		String result = null;
		HttpGet  httpRequest  = new HttpGet(ServerUrl);
		try{
			HttpClient httpClient = new DefaultHttpClient();
			Log.d("bowen","before execute");
			HttpResponse httpResp = httpClient.execute(httpRequest);
			Log.d("bowen","after execute");
			if(httpResp.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(httpResp.getEntity(),"UTF-8");
			}
			else{
				Log.d("bowen",Integer.toString(httpResp.getStatusLine().getStatusCode()));
			}
		}
		catch(Exception e){
			result = "error";
		}
		
		return result;
	}
	
	public 	InputStream GetInfoList(String methodName,ArrayList<String> Parameters, ArrayList<String> ParValues){
		String ServerUrl = HOST+methodName+"?";
		String paras = "";
		List<String> parasList = new ArrayList<String>();
		for(int i = 0;i<Parameters.size();i++){
			paras=paras +Parameters.get(i)+"="+ParValues.get(i);
			if(i!=Parameters.size()-1){
				paras = paras+"&";
			}
		}
		
		ServerUrl += paras;
		
		Log.i("bowen","url:"+ServerUrl);

		HttpGet  httpRequest  = new HttpGet(ServerUrl);
		InputStream result = null;
		
		try{
			Log.d("bowen","before execute");
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(httpRequest);
			
			if(httpResp.getStatusLine().getStatusCode()==200){
				result = httpResp.getEntity().getContent();
				//result = EntityUtils.toString(httpResp.getEntity(),"UTF-8");
			}
			else{
				Log.i("bowen","bad http resp");
				Log.d("bowen",Integer.toString(httpResp.getStatusLine().getStatusCode()));
				result = null;
			}
			return result;
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public InputStream GetLastNDaysInspectionData(int elder_id, String device_type){
		InputStream is = null;
		String serverUrl = HOST+"getLastDaysRecord?elder_id="+elder_id+"&device_type="+device_type;
		HttpGet http = new HttpGet(serverUrl);
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(http);
			
			if(httpResp.getStatusLine().getStatusCode() == 200){
				is = httpResp.getEntity().getContent();
			}else{
				Log.d("bowen-soap", Integer.toString(httpResp.getStatusLine().getStatusCode()));
				is = null;
			}
			return is;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
			
		}
	}
	//public bool updateInspection(int elder_id, int doctor_id, string device_type, string result)
	public boolean UpdateInspection(int elder_id, int doctor_id, String device_type, String result){
		String httpStr = HOST+"updateInspection?elder_id="+elder_id+"&doctor_id="+doctor_id+"&device_type="+device_type+"&result="+result;
		HttpGet http = new HttpGet(httpStr);
		
		Log.d("bowen debug","try to submit temperature");
		Log.d("bowen debug","http url is "+httpStr);
		
		
		try{
			HttpClient httpClient =  new DefaultHttpClient();
			HttpResponse httpResp = httpClient.execute(http);
			
			if(httpResp.getStatusLine().getStatusCode() == 200){
				Log.d("bowen http","inspection data submit status code is 200");
				InputStream is = httpResp.getEntity().getContent();
				XmlPullParser parser = Xml.newPullParser();
				
				return XMLParse.isTrue(is);
				
			}else{
				return false;
			}
		}
		catch(Exception e){
			if(e!=null){
				String msg = e.getMessage();
				Log.d("bowen-Soap", msg);
			}
			return false;
		}
	}
}







