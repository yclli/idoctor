package com.sjtu.idoctor.view;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.model.DocScheduleCacheBean;
import com.sjtu.idoctor.model.DoctorCacheBean;
import com.sjtu.idoctor.model.ElderCacheBean;
import com.sjtu.idoctor.model.RoomItemCacheBean;
import com.sjtu.idoctor.model.User;
import com.sjtu.idoctor.utils.DBUtils;
import com.sjtu.idoctor.view.fragment.BloodPressureFragment;
import com.sjtu.idoctor.view.fragment.DoctorListFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private DBUtils dbu;
	private List<HashMap<String, String>> roomEntity;
	private List<RoomItemCacheBean> roomItemList = new ArrayList<RoomItemCacheBean>();
	private List<DoctorCacheBean> doctorList;
	private ArrayList<HashMap<String,Object>> elderNameList;
	private Bitmap bitmap;
	
	private String elderName="";
	private String building = "";
	private String floor = "";
	private String roomNo="";
	private String areaId="";
	
	
	private SimpleAdapter _buildingGVAdapter = null;
	private SimpleAdapter _floorGVAdapter = null;
	private SimpleAdapter _roomGVAdapter = null;
	private SimpleAdapter _elderGVAdapter = null;
	private GridView building_gv;
	private GridView floor_gv;
	private GridView room_gv;
	private GridView elder_gv;
	private ImageView doctor_ph;
	private TextView doctor_tx;
	private TextView building_tx;
	private TextView floor_tx;
	private LinearLayout room_ly;
	private LinearLayout right_ll;
	
	List<HashMap<String,String>> DoctorList = null; 
	List<HashMap<String,String>> elderInfoList = null;
	
	private Button roomBtn = null;
	private Button elderBtn = null;
	private Button itemBtn = null;
	private int rightTimer = 1;

	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			if(msg.what==3){ //生成房间信息
				genRoomItemList();
				String[] buildingArray = null;
				String[] floorArray = null;
				String[] localroomArray = null;
				ArrayList<String> listTemp = MainActivity.this.genBuildings();
				buildingArray = listTemp.toArray(new String[listTemp.size()]);
				Log.i("idoc","MainActivity.this.building"+MainActivity.this.building);
				Log.i("idoc","MainActivity.this.floor"+MainActivity.this.floor);
				if(MainActivity.this.building.equals("")){
					MainActivity.this.building = MainActivity.this.roomItemList.get(0).getBuilding();
					
					listTemp = MainActivity.this.genFloors(MainActivity.this.building);
					Log.i("idoc","floorArray length:"+listTemp.size());
					floorArray = listTemp.toArray(new String[listTemp.size()]);
					MainActivity.this.floor = floorArray[0];
				}else if(MainActivity.this.floor.equals("")){
					listTemp = MainActivity.this.genFloors(MainActivity.this.building);
					Log.i("idoc","floorArray length:"+listTemp.size());
					floorArray = listTemp.toArray(new String[listTemp.size()]);
					MainActivity.this.floor = floorArray[0];
				}else{
					listTemp = MainActivity.this.genFloors(MainActivity.this.building);
					floorArray = listTemp.toArray(new String[listTemp.size()]);
					Log.i("idoc","floorArray length:"+floorArray.length);
				}
				listTemp = MainActivity.this.genWantedRooms(MainActivity.this.building,MainActivity.this.floor);
				localroomArray = listTemp.toArray(new String[listTemp.size()]);
				
				
				ArrayList<HashMap<String,Object>> item1List = new ArrayList<HashMap<String,Object>>();
				ArrayList<HashMap<String,Object>> item2List = new ArrayList<HashMap<String,Object>>();
				ArrayList<HashMap<String,Object>> item3List = new ArrayList<HashMap<String,Object>>();

				Log.i("idoc","after get itemlist");
				for(int i=0;i<buildingArray.length;i++){
					HashMap<String, Object> map = new HashMap<String, Object>();  
		            map.put("building_Str", buildingArray[i]);// 按序号做ItemText  
		            item1List.add(map);
				}
				Log.i("idoc","floorArray length:"+floorArray.length);
				for(int i=0;i<floorArray.length;i++){
					HashMap<String, Object> map = new HashMap<String, Object>();  
		            map.put("floor_Str", floorArray[i]);// 按序号做ItemText  
		            item2List.add(map);
				}
				Log.i("idoc","item2List size: "+item2List.size());
				for(int i=0;i<localroomArray.length;i++){
					HashMap<String, Object> map = new HashMap<String, Object>();  
		            map.put("room_Str", localroomArray[i]);// 按序号做ItemText  
		            item3List.add(map);
				}
				
				_buildingGVAdapter = new SimpleAdapter(MainActivity.this,item1List,R.layout.building_item,new String[]{"building_Str"},new int[]{R.id.buildingItemBtn});
				_floorGVAdapter = new SimpleAdapter(MainActivity.this,item2List,R.layout.floor_item,new String[]{"floor_Str"},new int[]{R.id.floorItemBtn});
				_roomGVAdapter = new SimpleAdapter(MainActivity.this,item3List,R.layout.room_item,new String[] {"room_Str"}, new int[] {R.id.roomItemBtn});
				
				
				MainActivity.this.building_gv.setAdapter(_buildingGVAdapter);
				building_tx.setText(MainActivity.this.building);
				floor_tx.setText(MainActivity.this.floor);
				MainActivity.this.floor_gv.setAdapter(_floorGVAdapter);
				MainActivity.this.room_gv.setAdapter(_roomGVAdapter);
				if(rightTimer==1){
					return;
				}
			}else if(msg.what==10){//选择房间事件
				new GetElderThread().start();
				
			}else if(msg.what==11){//获取当前房间老人
				_elderGVAdapter = new SimpleAdapter(MainActivity.this,elderNameList,R.layout.elder_item,new String[] {"elder_name"}, new int[] {R.id.elder_btn});
				MainActivity.this.elder_gv.setAdapter(_elderGVAdapter);
				
			}else if(msg.what==5){//确定当前医生
				if(doctorList.size()!=0){
					doctor_ph.setImageBitmap(bitmap);
					doctor_tx.setText("当前医生："+doctorList.get(0).getName());
					editor = preferences.edit();
					editor.putString("doctorName", doctorList.get(0).getName());
					editor.putInt("doctorId", doctorList.get(0).getStaffId());
					editor.commit();
				}
				else{
					docFragment();
				}
			}else if(msg.what ==4){
				Toast.makeText(MainActivity.this, "网络链接失败", 50000).show();
			}
		}
	};
	
	
	/**
	 * 获取列表的线程
	 */
	private class GetRoomListThread extends Thread{
		
		@Override
		public void run(){
			Log.d("idoc", "thread running 1111");
			Message msg = new Message();
			roomEntity = dbu.getAllRoom();
			if(roomEntity!=null){
				while(roomEntity.size()<2){}
				if(roomEntity.size()>1){
					Log.d("idoc", "try to get the room list");
					msg.what = 3;
					Log.d("idoc", "get list success");
				}else{
					msg.what = 4;
					Log.d("idoc", "fail to get list success");
				}
			}else{
				msg.what=4;
			}
			mHandler.sendMessage(msg);
		}
	}
	
	/**
	 *获取当班医生的线程
	 */
	private class GetCurrentDoctorListThread extends Thread{
		@Override
		public void run(){
			Message msg = new Message();
			
			List<DocScheduleCacheBean> docSchedule = dbu.getCurrentDoctor();
			List<DoctorCacheBean> doclist = dbu.getDoctors();
			doctorList = new ArrayList<DoctorCacheBean>();
			for(int j=0; j<docSchedule.size(); ++j){
				DoctorCacheBean a = findDoctorByStaffId(doclist, docSchedule.get(j).getStaffId());
				doctorList.add(a);
			}
			if(doctorList.size()!=0){
				try {
					URL url=new URL(doctorList.get(0).getPhotoSrc());
					InputStream is= url.openStream();
					bitmap = BitmapFactory.decodeStream(is);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			msg.what=5;
			mHandler.sendMessage(msg);
		}
	}
	
	/**
	 *获取房间老人的线程
	 */
	private class GetElderThread extends Thread{
		@Override
		public void run(){
			Message msg = new Message();
			
			elderNameList = new ArrayList<HashMap<String,Object>>();
			elderInfoList = dbu.getELder(areaId);
			for(int i=0;i<elderInfoList.size();i++){
				HashMap<String, Object> map = new HashMap<String, Object>();  
				map.put("elder_name", elderInfoList.get(i).get("elderName"));
				map.put("elder_id", elderInfoList.get(i).get("elderId"));
				elderNameList.add(map);
			}
			
			msg.what=11;
			mHandler.sendMessage(msg);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		preferences = getSharedPreferences("Doctor", Activity.MODE_PRIVATE);
		
		dbu = new DBUtils(preferences);
		
		right_ll = (LinearLayout) findViewById(R.id.right_ll);
		doctor_ph = (ImageView) findViewById(R.id.doctor_photo);
		doctor_tx = (TextView) findViewById(R.id.doctor_title);
		roomBtn = (Button) findViewById(R.id.room_btn);
		elderBtn = (Button) findViewById(R.id.elder_btn);
		itemBtn = (Button) findViewById(R.id.item_btn);		
		
		new GetRoomListThread().start();
		new GetCurrentDoctorListThread().start();
	}

	
	@Override
	public void onResume(){
		super.onResume();
		
		Log.d("idoc","room number is "+roomNo);
		Log.d("idoc","elder name is "+elderName);
		
		if(roomNo.equals("")){
			toggleFirstPage();		
		}else if(elderName.equals("")){
			toggleSecondPage();
			Message msg = new Message();
			msg.what = 10;
			mHandler.sendEmptyMessage(msg.what);
		}else{
			toggleThirdPage();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_doctor) {
			docFragment();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class roomItemClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
		}
	}
	
	public void toggleFirstPage(){
		room_ly = (LinearLayout) getLayoutInflater().inflate(R.layout.room_ly, null);
		room_gv = (GridView) room_ly.findViewById(R.id.room_gv);
		building_gv = (GridView) room_ly.findViewById(R.id.building_gv);
		building_tx = (TextView) room_ly.findViewById(R.id.building_divider).findViewById(R.id.building_text);
		floor_gv = (GridView) room_ly.findViewById(R.id.floor_gv);
		floor_tx = (TextView) room_ly.findViewById(R.id.floor_divider).findViewById(R.id.floor_text);
		right_ll.removeAllViews();
		right_ll.addView(room_ly, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT); 
		
		if(roomEntity!=null){
			Message msg = new Message();
			msg.what = 3;
			mHandler.sendEmptyMessage(msg.what);
		}
	}
	
	public void toggleSecondPage(){
		right_ll.removeAllViews();
		LinearLayout elder_ll = (LinearLayout) getLayoutInflater().inflate(R.layout.elder_ll,null);
		right_ll.addView(elder_ll, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		elder_gv = (GridView) elder_ll.findViewById(R.id.elder_gv);
		Button roomNoBtn = (Button) elder_ll.findViewById(R.id.roomNo);
		roomNoBtn.setText(roomNo);
	}
	
	public void toggleThirdPage(){
		right_ll.removeAllViews();
		LinearLayout measureItem = (LinearLayout) getLayoutInflater().inflate(R.layout.measure_item,null);
		right_ll.addView(measureItem, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	}
	
	public void roomButtonClick(View v){
		Button btnV = (Button) v;
		roomNo = this.building+"-"+this.floor+"-"+btnV.getText().toString();
		areaId = getAreaIdByRoomNo(btnV.getText().toString());
		toggleSecondPage();
		
		toggleRight(2);
		
		Message msg = new Message();
		msg.what = 10;
		mHandler.sendEmptyMessage(msg.what);
	}
	
	
	public void floorButtonClick(View v){
		Button btnV = (Button) v;
		this.floor = btnV.getText().toString();
		Message msg = new Message();
		msg.what = 3;
		this.mHandler.sendEmptyMessage(msg.what);
	}
	
	public void buildingButtonClick(View v){
		
	}
	
	public void elderClick(View v){
		Button btnV = (Button) v;
		elderName = btnV.getText().toString();
		
		toggleRight(3);
		toggleThirdPage();
	}
	
	public void roomBtnClicked(View v){
		toggleRight(1);
		toggleFirstPage();
		elderName = "";
		roomNo = "";
	}
	
	public void elderBtnClicked(View v){
		if(roomNo.equals("")){
			Toast.makeText(this, "请先选择房间再选择老人！", 3000).show();
		}else{
			Log.d("idoc","elder btn clicked");
			elderName = "";
			toggleRight(2);
			toggleSecondPage();
			Message msg = new Message();
			msg.what = 10;
			mHandler.sendEmptyMessage(msg.what);
			
		}
	}
	
	public void itemBtnClicked(View v){
		if(rightTimer != 3){
			Toast.makeText(this,"请先选择老人！",3000).show();
		}
	}
	
	public void toggleRight(int toCounter){
		rightTimer = toCounter;
		if(toCounter == 1){
			roomBtn.setTextColor(Color.rgb(255, 255, 255));
			elderBtn.setTextColor(Color.rgb(0, 0, 0));
			itemBtn.setTextColor(Color.rgb(0, 0, 0));
		}else if(toCounter == 2){
			roomBtn.setTextColor(Color.rgb(0, 0, 0));
			elderBtn.setTextColor(Color.rgb(255, 255, 255));
			itemBtn.setTextColor(Color.rgb(0, 0, 0));
		}else if(toCounter == 3){
			roomBtn.setTextColor(Color.rgb(0,0,0));
			elderBtn.setTextColor(Color.rgb(0, 0, 0));
			itemBtn.setTextColor(Color.rgb(255, 255, 255));
		}
	}
	
	public void beginMeasure(View view){
		String localRoomNo = this.roomNo;
		String localElderName = this.elderName;
		String itemName = view.getTag().toString();
		
		int indexOfelder = -1;
		for(int i = 0;i<elderInfoList.size();i++){
			if(elderName.equals(elderInfoList.get(i).get("elderName")) ){
				indexOfelder = i;
				break;
			}
		}
		String elderID = "";
		if(indexOfelder>-1){
			elderID = elderInfoList.get(indexOfelder).get("elderId");
		}
		
		Intent measureIntent = null;
		
		if(itemName.equals("测量体温")){
			measureIntent = new Intent(this,TemperatureMeasureActivity.class);
		}else if(itemName.equals("测量血压")){
			measureIntent = new Intent(this,BloodPressureFragment.class);
		}else if(itemName.equals("心电图")){
			measureIntent = new Intent(this,ConfirmActivity.class);
			//Toast.makeText(this,"新功能正在开发中，敬请期待",3000).show();
			//return;
		}
		if(measureIntent == null){
			Log.i("idoc","in is null");
		}else if(elderName == null){
			Log.i("idoc","elderName is null");
		}
		editor = preferences.edit();
		editor.putString("elderName", elderName);
		editor.putString("itemName", itemName);
		editor.putString("elderId", elderID);
		editor.putString("roomNo", roomNo);
		editor.commit();
		startActivity(measureIntent);
	}
	
	public void genRoomItemList(){
		if(this.roomEntity.size()>0){
			this.roomItemList.clear();
			for(int i = 0;i<this.roomEntity.size();i++){
				RoomItemCacheBean localRoomItem = new RoomItemCacheBean();
				String roomName = this.roomEntity.get(i).get("roomName");
				localRoomItem.setBuilding(roomName.split("-")[0]);
				localRoomItem.setFloor(roomName.split("-")[1]);
				localRoomItem.setRoomNo(roomName.split("-")[2]);
				localRoomItem.setAreaId(this.roomEntity.get(i).get("areaId"));
				this.roomItemList.add(localRoomItem);
			}
			Collections.sort(roomItemList);
		}
	}
	
	public ArrayList<String> genBuildings(){
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0;i<this.roomItemList.size();i++){
			if(!list.contains(this.roomItemList.get(i).getBuilding())){
				list.add(this.roomItemList.get(i).getBuilding());
			}
		}
		
		return list;
	}
	
	public ArrayList<String> genFloors(String building){
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0;i<this.roomItemList.size();i++){
			if(this.roomItemList.get(i).getBuilding().equals(building)){
				if(!list.contains(this.roomItemList.get(i).getFloor())){
					list.add(this.roomItemList.get(i).getFloor());
				}
			}
		}
		
		return list;
	}
	
	public ArrayList<String> genWantedRooms(String building,String floor){
		ArrayList<String> list = new ArrayList<String>();
		String[] result = null;
		Log.i("idoc","building "+building+";floor "+floor);
		for(int i=0;i<this.roomItemList.size();i++){
			Log.i("idoc","local building "+this.roomItemList.get(i).getBuilding()+";local floor "+this.roomItemList.get(i).getFloor()+";local room "+this.roomItemList.get(i).getRoomNo());
			if(this.roomItemList.get(i).getBuilding().equals(building)&&this.roomItemList.get(i).getFloor().equals(floor)){
				list.add(this.roomItemList.get(i).getRoomNo());
			}
		}
		Log.i("idoc-result","list length "+list.size());
		return list;
	}
	
	public ArrayList<String> genFloorRooms(String building){
		String[] result = null;
		Log.i("idoc-building",building);
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0;i<this.roomItemList.size();i++){
			Log.i("idoc",this.roomItemList.get(i).getBuilding());
			if(this.roomItemList.get(i).getBuilding().equals(building)){
				Log.i("idoc","relevante");
				String localRoomItem = "";
				localRoomItem = this.roomItemList.get(i).getBuilding();
				list.add(localRoomItem);
			}
		}
		Log.i("idoc",""+list.size());
		result = list.toArray(new String[1]);
		if(result == null){
			Log.i("idoc","result is still empty");
		}
		
		return list;
	}

	public String getAreaIdByRoomNo(String room){
		String area = "";
		for(int i=0; i<this.roomItemList.size(); i++){
			if(this.roomItemList.get(i).getRoomNo().equals(room)){
				area = this.roomItemList.get(i).getAreaId();
			}
		}
		return area;
	}
	
	public DoctorCacheBean findDoctorByStaffId(List<DoctorCacheBean> dl, int sid){
		DoctorCacheBean d = null;
		for (int i=0; i<dl.size(); i++){
			if(dl.get(i).getStaffId() == sid)
				d = dl.get(i);
		}
		return d;
	}
	
	public void docFragment(){
		DoctorListFragment df = new DoctorListFragment();
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(android.R.id.content, df);
		ft.addToBackStack("");
		ft.commit();
	}
}
