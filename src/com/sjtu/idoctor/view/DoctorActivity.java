package com.sjtu.idoctor.view;

import java.util.ArrayList;
import java.util.List;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.R.id;
import com.sjtu.idoctor.R.layout;
import com.sjtu.idoctor.R.menu;
import com.sjtu.idoctor.model.DoctorCacheBean;
import com.sjtu.idoctor.utils.DBUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class DoctorActivity extends Activity {
	
	private SharedPreferences preferences = null;
	private SharedPreferences.Editor editor;
	DBUtils dbu;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	private Button summitBtn;
	private List<DoctorCacheBean> doctorList;
	private List<String>  doclist;
	private String docName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor);
		
		doclist = new ArrayList<String> ();
		preferences = getSharedPreferences("Doctor", Activity.MODE_PRIVATE);
		dbu = new DBUtils(preferences);
		
		new GetDoctorListThread().start();
		
		spinner = (Spinner) findViewById(R.id.doctor);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				docName = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		summitBtn = (Button) findViewById(R.id.doctorsummit);
		summitBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int docId = findDocIdByName(docName);
				editor = preferences.edit();
				editor.putString("doctorName", docName);
				editor.putInt("doctorId", docId);
				editor.commit();
				startActivity(new Intent(DoctorActivity.this, MainActivity.class));
				finish();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class GetDoctorListThread extends Thread{
		@Override
		public void run(){
			Message msg = new Message();
			doctorList = dbu.getDoctors();
			msg.what=1;
			handler.sendMessage(msg);
		}
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what==1){
				for(int i=0; i<doctorList.size(); i++){
					doclist.add(doctorList.get(i).getName());
				}
				adapter = new ArrayAdapter<String>(DoctorActivity.this, android.R.layout.simple_spinner_item, doclist);
				spinner.setAdapter(adapter);
			}
		}
	};
	
	public int findDocIdByName(String docname){
		int docid = 0;
		for(int i=0; i<doctorList.size(); i++){
			if(doctorList.get(i).getName().equals(docname))
				docid = doctorList.get(i).getStaffId();
		}
		return docid;
	}
	
}
