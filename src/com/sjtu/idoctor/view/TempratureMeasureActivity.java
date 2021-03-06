package com.sjtu.idoctor.view;

import java.util.List;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.model.TempratureCacheBean;
import com.sjtu.idoctor.utils.DBUtil;

public class TempratureMeasureActivity extends Activity{
	private boolean timeChanged = false;
	private Context mContext = null;
	public String elderID = "";
	public String roomNo = "";
	public String itemName = "";
	public String elderName = "";
	public String doctorID = "8";
	private DBUtil dbUtil = null;
	public List<TempratureCacheBean> tempList = null;
	// Time scrolled flag
	private boolean timeScrolled = false;
	private TextView temperature;
	private int T_START = 35;
	private int T_END = 44;
	private Button cancel_button;
	private Button submit_button;
	private  WheelView hours;
	private  WheelView mins;
	private boolean recentDataReady = false;
	
/*	public void TempratureMeasureActivity(){
		
	}*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temprature);

		Intent intent =getIntent();
		elderName = intent.getStringExtra("com.sjtu.idoctor.elderName");
		elderID = intent.getStringExtra("com.sjtu.idoctor.elderID");
		Log.d("idoc",elderID);
		roomNo = intent.getStringExtra("com.sjtu.idoctor.roomNo");
		itemName = intent.getStringExtra("com.sjtu.idoctor.itemName");
		

		TextView roomNameTv = (TextView) findViewById(R.id.current_room);
		TextView elderNameTv = (TextView) findViewById(R.id.current_elder);
		TextView itemNameTv = (TextView) findViewById(R.id.current_item);
		
		roomNameTv.setText(roomNo);
		elderNameTv.setText(elderName);
		itemNameTv.setText(itemName);
		
		WebView myWebView1 = (WebView) findViewById(R.id.webView1);
        myWebView1.getSettings().setJavaScriptEnabled(true);
        myWebView1.getSettings().setLoadWithOverviewMode(true);
        myWebView1.getSettings().setSupportZoom(true);
        myWebView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView1.getSettings().setBuiltInZoomControls(true);
        myWebView1.loadUrl("file:///android_asset/temperature.html?elderID="+elderID);
		
		
		hours = (WheelView) findViewById(R.id.hour);
		hours.setViewAdapter(new NumericWheelAdapter(this, T_START, T_END));
		
		mins = (WheelView) findViewById(R.id.mins);
		mins.setViewAdapter(new NumericWheelAdapter(this, 0, 9, "%01d"));
		mins.setCyclic(true);

//		final TimePicker picker = (TimePicker) findViewById(R.id.time);
//		picker.setIs24HourView(true);
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
//					picker.setCurrentHour(hours.getCurrentItem());
//					picker.setCurrentMinute(mins.getCurrentItem());
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
//				picker.setCurrentHour(hours.getCurrentItem());
//				picker.setCurrentMinute(mins.getCurrentItem());
				timeChanged = false;
			}
		};

		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);
		
		SharedPreferences mySharedPreferences = getSharedPreferences("Doctor",Activity.MODE_PRIVATE);
		dbUtil = new DBUtil(mySharedPreferences);
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
		        String t = integer_part+"."+float_part;
		        Log.e("temperature",t);
		        
		        int elder_id = Integer.parseInt(TempratureMeasureActivity.this.elderID);
		        int doctor_id = Integer.parseInt(TempratureMeasureActivity.this.doctorID);
		        
		        boolean submit = dbUtil.UpdateInspection(elder_id, doctor_id, "temperature", t);
		        Log.d("idoc-submit",submit?"true":"false");
		        if(submit){
		        	Toast.makeText(TempratureMeasureActivity.this, "提交成功", 3000).show();
		        }
		        else{
		        	Toast.makeText(TempratureMeasureActivity.this,"网络传输出错，请再次提交",3000).show();
		        }
		    }  
		});  
	}
}
