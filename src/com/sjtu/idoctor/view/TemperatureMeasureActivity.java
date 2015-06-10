package com.sjtu.idoctor.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.model.DocScheduleCacheBean;
import com.sjtu.idoctor.model.DoctorCacheBean;
import com.sjtu.idoctor.model.TemperatureCacheBean;
import com.sjtu.idoctor.utils.DBUtils;

public class TemperatureMeasureActivity extends Activity{
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	private boolean timeChanged = false;
	private Context mContext = null;
	private int greoID;
	private String userName;
	private String digest;
	private String elderID;
	private String roomNo;
	private String itemName;
	private String elderName;
	private String t = null;
	private int doctorID;
	private DBUtils dbu;
	private List<TemperatureCacheBean> tempList = null;
	private List<HashMap<String,String>> tpRecord = null;
	// Time scrolled flag
	private boolean timeScrolled = false;
	private TextView temperature;
	private int T_START = 35;
	private int T_END = 44;
	private Button cancel_button;
	private Button submit_button;
	private WebView myWebView1;
	private  WheelView hours;
	private  WheelView mins;
	private boolean recentDataReady = false;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what==1){
				String send = "";
				send = send + tpRecord.get(0).get("times") + "," + tpRecord.get(0).get("temperature");
				for (int j=1; j<tpRecord.size(); j++){
					send = send + ";" + tpRecord.get(j).get("times") + "," + tpRecord.get(j).get("temperature");
				}
				myWebView1.loadUrl("javascript:getResult(\'"+send+"\')");
			}else if(msg.what==2){
				Toast.makeText(TemperatureMeasureActivity.this, "提交成功", 3000).show();
			}else if(msg.what==3){
				Toast.makeText(TemperatureMeasureActivity.this,"网络传输出错，请再次提交",3000).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperature);
		
		preferences = getSharedPreferences("Doctor", Activity.MODE_PRIVATE);
		greoID = preferences.getInt("geroId", 0);
		userName = preferences.getString("username", "");
		digest = preferences.getString("digest", "");
		elderName = preferences.getString("elderName", "");
		elderID = preferences.getString("elderId", "");
		roomNo = preferences.getString("roomNo", "");
		itemName = preferences.getString("itemName", "");
		doctorID = preferences.getInt("doctorId", 11);

		TextView roomNameTv = (TextView) findViewById(R.id.current_room);
		TextView elderNameTv = (TextView) findViewById(R.id.current_elder);
		TextView itemNameTv = (TextView) findViewById(R.id.current_item);
		
		roomNameTv.setText(roomNo);
		elderNameTv.setText(elderName);
		itemNameTv.setText(itemName);
		
		myWebView1 = (WebView) findViewById(R.id.webView1);
        myWebView1.getSettings().setJavaScriptEnabled(true);
        myWebView1.getSettings().setLoadWithOverviewMode(true);
        myWebView1.getSettings().setSupportZoom(true);
        myWebView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView1.getSettings().setBuiltInZoomControls(true);
        myWebView1.setWebChromeClient(new WebChromeClient() {});
        myWebView1.loadUrl("file:///android_asset/temperature.html");
        
		
		hours = (WheelView) findViewById(R.id.hour);
		hours.setViewAdapter(new NumericWheelAdapter(this, T_START, T_END));
		
		mins = (WheelView) findViewById(R.id.mins);
		mins.setViewAdapter(new NumericWheelAdapter(this, 0, 9, "%01d"));
		mins.setCyclic(true);

		temperature = (TextView) findViewById(R.id.temperature_show_number);
		cancel_button = (Button) findViewById(R.id.t_cancel_button);
		submit_button = (Button) findViewById(R.id.t_submit_button);
		init_button();
		// set current time
//		Calendar c = Calendar.getInstance();
//		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curHours = 2;
//		int curMinutes = c.get(Calendar.MINUTE);
		int curMinutes = 0;
		hours.setCurrentItem(curHours);
		mins.setCurrentItem(curMinutes);

//		picker.setCurrentHour(curHours);
//		picker.setCurrentMinute(curMinutes);

		// add listeners
		addChangingListener(mins, "min");
		addChangingListener(hours, "hour");

		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					timeChanged = true;
					updateTextView(hours.getCurrentItem(),mins.getCurrentItem());
					timeChanged = false;
				}
			}
		};
		hours.addChangingListener(wheelListener);
		mins.addChangingListener(wheelListener);

		OnWheelClickedListener click = new OnWheelClickedListener() {
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		};
		hours.addClickingListener(click);
		mins.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			@Override
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
				updateTextView(hours.getCurrentItem(),mins.getCurrentItem());
				timeChanged = false;
			}
		};

		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);
		
		dbu = new DBUtils(preferences);
		new GetTemperatureRecordThread().start();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		mContext = this;
	}
	
	
	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}
	private void updateTextView(int hour,int min){
//		Log.d("ddd",hour+min+"");
		hour =hour+T_START;
		temperature.setText(String.valueOf(hour)+"."+String.valueOf(min));
	}
	private void init_button(){
		cancel_button.setOnClickListener(new View.OnClickListener(){
			@Override
		    public void onClick(View v){
				finish();
			}
		});
		
		submit_button.setOnClickListener(new View.OnClickListener() {  
		      
		    @Override  
		    public void onClick(View v) {  
		        // TODO Auto-generated method stub  
		        Log.i("TEST", "Should post to server");  
		        int integer_part=hours.getCurrentItem()+T_START;
		        int float_part=mins.getCurrentItem();
		        t = integer_part+"."+float_part;
		        Log.e("temperature",t);
		        
		        new PostTemperatureRecordThread().start();
		    }  
		});  
	}
	
	private class GetTemperatureRecordThread extends Thread{
		@Override
		public void run(){
			Message msg = new Message();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");    
			Calendar calendar = Calendar.getInstance();
			calendar.roll(Calendar.DAY_OF_YEAR, +1);
			String endDate = sdf.format(calendar.getTime());
			calendar.roll(Calendar.DAY_OF_YEAR, -5);
			String startDate = sdf.format(calendar.getTime());
			
			tpRecord = dbu.getTemperature(Integer.parseInt(elderID), startDate, endDate);
			if(tpRecord != null){
				msg.what=1;
				mHandler.sendMessage(msg);
			}
		}
	}
	
	private class PostTemperatureRecordThread extends Thread{
		@Override
		public void run(){
			Message msg = new Message();
			int elder_id = Integer.parseInt(TemperatureMeasureActivity.this.elderID);
	        int doctor_id = TemperatureMeasureActivity.this.doctorID;
	        TemperatureCacheBean temperature = new TemperatureCacheBean(doctor_id, t);
	        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     
	        String date = sDateFormat.format(new java.util.Date());  
	        temperature.setTime(date);
	        
	        boolean submit = dbu.insertTemperature(elder_id, temperature);
			if(submit == true){
				msg.what=2;
				mHandler.sendMessage(msg);
			}else{
				msg.what=3;
				mHandler.sendMessage(msg);
			}
		}
	}
}
