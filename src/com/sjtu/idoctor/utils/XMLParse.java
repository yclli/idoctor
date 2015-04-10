package com.sjtu.idoctor.utils;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.sjtu.idoctor.model.TempratureCacheBean;;

public class XMLParse{
	public static ArrayList<String> paraseCommentInfors (InputStream inputStream){
		ArrayList<String> list = new ArrayList<String>();
		XmlPullParser parser = Xml.newPullParser();
		
		try{
			parser.setInput (inputStream, "UTF-8");  
			int eventType = parser.getEventType();
			boolean evenOdd = false;
			/*String info = new String();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer buffer = new StringBuffer();
			String inputString = "";
			while ((inputString = in.readLine()) != null){
				buffer.append(inputString);
			}
			
			inputString = buffer.toString();*/
			
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				switch (eventType){
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.TEXT:
						if(!evenOdd){
							evenOdd = true;
						}else{
							String content = parser.getText();
							Log.d("bowen-content",content);
							list.add(content);
							evenOdd = false;
						}
						break;
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if(name.equalsIgnoreCase("string")){
/*							if(parser.next() == XmlPullParser.TEXT){
								String content = parser.getText();
								Log.d("bowen",content);
								list.add(content);
							}*/
						}
						Log.d("bowen", "get string");
/*						if (name.equalsIgnoreCase ("Review") ){
							info = new String(); 
						}
						else if (name.equalsIgnoreCase ("userID") )
						{
							eventType = parser.next();
							info.setUserID (parser.getText() );
						}
						else if (name.equalsIgnoreCase ("userName") )
						{
							eventType = parser.next();
							info.setUserName (parser.getText() );
						}
						else if (name.equalsIgnoreCase ("reviewInfo") )
						{
							eventType = parser.next();
							info.setReviewInfo (parser.getText() );
						}
						else if (name.equalsIgnoreCase ("reviewDate") )
						{
							eventType = parser.next();
							info.setReviewDate (parser.getText() );
						}*/
						break;
					case XmlPullParser.END_TAG:
						/*if (parser.getName().equalsIgnoreCase ("Review") )
						{
							list.add (info);
							info =null;
						}*/
						break;
				}
				eventType = parser.next();
			}
			inputStream.close();
		}
		catch (Exception e) {
			 e.printStackTrace();
		}
		
		Log.d("bowen","XMLParse end");
		for(int j=0;j<list.size();j++){
			Log.d("bowen", "listItem="+list.get(j));
		}
		Log.d("bowen","listlength="+list.size());
		return list; 
	}
/*
	public static ArrayList<TempratureCacheBean> paraseTempratureInfo(InputStream inputStream){
		ArrayList<TempratureCacheBean> list = new ArrayList<TempratureCacheBean>();
		XmlPullParser parser = Xml.newPullParser();
		int ValueNumber = TempratureCacheBean.CONTENTNUMBER;
		
		try{
			parser.setInput(inputStream, "UTF-8");  
			int eventType = parser.getEventType();
			boolean evenOdd = false;
			int index = 0;
			boolean newFlag = true;
			TempratureCacheBean tmp = null;
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch (eventType){
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.TEXT:
					if(evenOdd){
						evenOdd = false;
						String text = parser.getText();
						
						if(newFlag){
							newFlag = false;
							tmp = new TempratureCacheBean();
						}
						
						if(index == 0){
							tmp.recordID =  text;
							index = (index+1)%ValueNumber;
						}else if(index == 1){
							tmp.recordDate = text.split(" ")[0];
							index = (index+1)%ValueNumber;
						}else if(index == 2){
							tmp.tempValue = text;
							index = (index+1)%ValueNumber;
						}else if(index == 3){
							newFlag = true;
							tmp.recordTime = text;
							list.add(tmp);
							index = (index+1)%ValueNumber;
							evenOdd = true;	//若不添加则会因为inputStream中string标签不成对而导致连续读两次"/n"
						}
					}else 
					{
						evenOdd = true;
					}
					break;
				}
				eventType = parser.next();
			}
			inputStream.close();
		}
		catch(Exception e){
			if(e != null){
				String msg = e.getMessage().toString();
				list = null;
			}
		}
		
		return list;
	}
	*/
	public static boolean isTrue(InputStream inputStream){
		boolean isTrue = false;
		XmlPullParser parser = Xml.newPullParser();
		
		try{
			parser.setInput(inputStream, "UTF-8");
			int eventType = parser.getEventType();
			boolean oddEven = false;
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.TEXT:
					if(oddEven == false){
						Log.d("bowen-xml-false", parser.getText());
						if(parser.getText().equals("true")){
							isTrue = true;
						}else{
							isTrue = false;
						}
						oddEven = true;
					}else{
						Log.d("bowen-xml", parser.getText());
						oddEven = false;
					}
					break;
					
				
				}
				
				eventType = parser.next();
			}
			
		}
		catch(Exception e){
			isTrue = false;
		}
		
		return isTrue;
	}
}


