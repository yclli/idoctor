package com.sjtu.idoctor.view;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.model.BluetoothCacheBean;
import com.sjtu.idoctor.utils.ClsUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class ConfirmActivity extends Activity{
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	public static String elderName = "";
	public static String roomNo = "";
	public static String elderID = "";
	public static String itemName = "";
	private BluetoothAdapter bluetoothAdapter = null;
	private BluetoothReceiver bluetoothReceiver =null;
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	public static BluetoothSocket btSocket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("idoc","onCreate");
		setContentView(R.layout.activity_mainw);
		//btn_3.setOnClickListener(new DiscoveryBluetoothListener());
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//获得BluetoothAdapter对象
		// Register the BroadcastReceiverIntentFilter
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		bluetoothReceiver=new BluetoothReceiver(); 
		registerReceiver(bluetoothReceiver, filter); 
		// Don't forget to unregister during onDestroy

		preferences = getSharedPreferences("Doctor", Activity.MODE_PRIVATE);
		elderName = preferences.getString("elderName", "");
		elderID = preferences.getString("elderId", "");
		roomNo = preferences.getString("roomNo", "");
		itemName = preferences.getString("itemName", "");
		
		TextView roomNameTv = (TextView) findViewById(R.id.current_room);
		TextView elderNameTv = (TextView) findViewById(R.id.current_elder);
		TextView itemNameTv = (TextView) findViewById(R.id.current_item);
		
		
		roomNameTv.setText(roomNo);
		elderNameTv.setText(elderName);
		itemNameTv.setText(itemName);
		
		
	}
	
	public class BluetoothReceiver extends BroadcastReceiver{
		String strPwd="1234";
		@Override		
		public void onReceive(Context context, Intent intent) {			
			// TODO Auto-generated method stub			
			String action =intent.getAction();			
			if(BluetoothDevice.ACTION_FOUND.equals(action)){				
				BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);				
				Log.d("idoc",device.getAddress());			
				Log.d("idoc",device.getName());
				Log.d("idoc",String.valueOf(device.getBondState()));
//				Toast.makeText(MainActivity.this, device.getName(), 5000).show();
				if(device.getBondState() == BluetoothDevice.BOND_NONE){	
					Toast.makeText(ConfirmActivity.this, device.getName()+"not bonded",5000).show();
				}
				try{
					ClsUtils.setPin(device.getClass(), device, strPwd);
					ClsUtils.createBond(device.getClass(), device);
					ClsUtils.cancelPairingUserInput(device.getClass(), device);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void sendMessage_1(View view){
		BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice device = null;
		String nameList = "";
		if(adapter!=null){
			if(!adapter.isEnabled()){
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivity(intent);
			}else{
				adapter.startDiscovery();
				Log.d("idoc Xie","正在扫描设备");
			}
			
			
			Set<BluetoothDevice> devices=adapter.getBondedDevices();
			if(devices.size()>0){
				for(Iterator iterator = devices.iterator();iterator.hasNext();){
					device = (BluetoothDevice)iterator.next();
					String name = device.getName();
/*					if(device.getAddress().equals("00:06:71:00:23:47"));
					{
						BluetoothMsg.BlueToothAddress = device.getAddress();
					}*/
					Log.d("idoc",name);
					nameList += name+"||";
					if(name.equals("10320130810072")){
						BluetoothCacheBean.BlueToothAddress = device.getAddress();
					}
				}
				nameList.substring(0,nameList.length()-2);
				Toast.makeText(ConfirmActivity.this, nameList,5000).show();
				
			}
		}
	}
	
	public void sendMessage_2(View view){
		// Create a BroadcastReceiver for ACTION_FOUND
		bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		bluetoothAdapter.startDiscovery();
	}
	
	public void sendMessage_3(View view){
		if(BluetoothCacheBean.BlueToothAddress != null){
			Toast.makeText(ConfirmActivity.this, "启动ECG设备", 1000).show();
			Log.d("idoc","starting device");
			Intent in=new Intent(this,MyBluetooth.class);
			

			in.putExtra("elderName",elderName);
			in.putExtra("itemName", itemName);
			in.putExtra("elderID", elderID);
			in.putExtra("roomNo", roomNo);
			
			startActivity(in);
		}else{
			Toast.makeText(ConfirmActivity.this, "no available device", 3000).show();
		} 
	}
	
	protected void onDestroy() {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        unregisterReceiver(bluetoothReceiver);  
    } 
	
	private void connect(BluetoothDevice btDev){
		UUID uuid = UUID.fromString(SPP_UUID);
		try{
			btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
			Log.d("BlueToothTestActivity", "开始连接...");
			btSocket.connect();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
