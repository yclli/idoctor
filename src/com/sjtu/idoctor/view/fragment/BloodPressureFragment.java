package com.sjtu.idoctor.view.fragment;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.sjtu.idoctor.R;
import com.sjtu.idoctor.model.BloodPressureCacheBean;
import com.sjtu.idoctor.model.HeartRateCacheBean;
import com.sjtu.idoctor.thread.BloodPressureThread;
import com.sjtu.idoctor.utils.ClsUtils;
import com.sjtu.idoctor.utils.DBUtils;
import com.sjtu.idoctor.utils.Functions;
import com.sjtu.idoctor.utils.XXTEA;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BloodPressureFragment extends FragmentActivity{
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	public static final String bp7Address = "8C:DE:52:08:D7:1A";
	private BluetoothDevice mBTDevice = null;
	private BluetoothAdapter mBTAdapter = null;
	private BluetoothSocket mBTSocket = null;
	private BluetoothReceiver mBTReceiver =null;
	private final static String BPDEVICE = "BP7";
	public byte[] socketReadResult = new byte[256];
	public int commandNum = 1; //对应源程序中的commandSequenceNum
	public byte[] receivedCommand = null;
	public static String elderID = "";
	public static String roomNo = "";
	public static String elderName = "";
	public static int doctorID;
	public static String itemName = "";
	private DBUtils dbu;
	private boolean deviceConnected = false;
	public final byte[] ANGEL_YES = {-95,58,0,0,0};
	//public BloodPressureThread bpThread = null;
	public BloodPressureThread mBPThread = null;
	private boolean resultRead = false;
	private int diastolicPressure = 0;
	private int systolicPressure = 0;
	private int heartRate = 0;
	private int processTimer = 0;
	private boolean isMeassuring = false;
	
	
	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Log.d("idoc-handler","begin to handle message:message what ="+msg.what);
			if(msg.what == 4){
				//Toast.makeText(TiwenActivity.this, "asdgg", 3000).show();
			}else if(msg.what == 10){//有返回数据，需要unpackage后判断是否ack
				byte[] arrayOfByte = BloodPressureFragment.this.mBPThread.bufReciveFromBTSocket;
				int len = BloodPressureFragment.this.mBPThread.lenReciveFromBTSocket;
				Log.d("handler","handle msg 10");
				BloodPressureFragment.this.Unpackage(arrayOfByte, len);
			}else if(msg.what == 12){
				sendIdentifyCommand();
			}else if(msg.what == 13){
				Button startBtn = (Button) findViewById(R.id.start_btn);
				if(BloodPressureFragment.this.processTimer == 0){
					Toast.makeText(BloodPressureFragment.this, "验证成功，可以准备测量了！", 3000).show();
					startBtn.setText("准备");
					BloodPressureFragment.this.processTimer++;
				}else if(BloodPressureFragment.this.processTimer == 1){
					startBtn.setText("测量");
					BloodPressureFragment.this.processTimer++;
				}else if(BloodPressureFragment.this.processTimer == 2){
					startBtn.setText("准备");
					BloodPressureFragment.this.isMeassuring = false;
					BloodPressureFragment.this.processTimer = 1;
				}
			}else if(msg.what == 100){
				byte[] arrayOfByte = BloodPressureFragment.this.receivedCommand;
				Log.d("measure-result","result array is "+Functions.Bytes2HexString(arrayOfByte, arrayOfByte.length));
				int[] result = new int[4];
				result[0] = 0xFF & arrayOfByte[2];
				result[1] = 0xFF & arrayOfByte[3];
				result[2] = 0xFF & arrayOfByte[4];
				result[3] = 0xFF & arrayOfByte[5];
				
				BloodPressureFragment.this.diastolicPressure = result[0]+result[1];
				BloodPressureFragment.this.systolicPressure = result[1];
				BloodPressureFragment.this.heartRate = result[2];
				
				BloodPressureFragment.this.UpdateResult();
				//Toast.makeText(BloodPressure.this, "测得的结果为："+(result[0]+result[1])+"-----"+result[1]+"-----"+result[2], 8000).show();
			}else if(msg.what == 1000){
				Log.d("idoc-bp","socket error");
				mBTDevice = null;
				try{
					mBTSocket.close();
					BloodPressureFragment.this.mBPThread.stopRequest();
				}
				catch(Exception e){
					Log.d("idoc-stop","close socket error");
				}
				mBTSocket = null;
				socketReadResult = null;
				commandNum = 1; //对应源程序中的commandSequenceNum
				receivedCommand = null;
				deviceConnected = false;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bp_activity_main);
		this.mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		
		preferences = getSharedPreferences("Doctor", Activity.MODE_PRIVATE);
		elderName = preferences.getString("elderName", "");
		elderID = preferences.getString("elderId", "");
		roomNo = preferences.getString("roomNo", "");
		itemName = preferences.getString("itemName", "");
		doctorID = preferences.getInt("doctorId", 11);
		
		processTimer = 0;
		
		dbu = new DBUtils(preferences);
		
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
        //myWebView1.loadUrl("file:///android_asset/blood_pressure.html?elderID="+elderID);
        myWebView1.loadUrl("");
        
/*        WebView myWebView3 = (WebView) findViewById(R.id.webView2);
        myWebView3.getSettings().setJavaScriptEnabled(true);
        myWebView3.getSettings().setLoadWithOverviewMode(true);
        myWebView3.getSettings().setSupportZoom(true);
        myWebView3.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView3.getSettings().setBuiltInZoomControls(true);
        myWebView3.loadUrl("file:///android_asset/heart_rate.html?elderID="+elderID);*/
/*
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		mBTDevice = null;
		try{
			mBTSocket.close();
			this.mBPThread.stopRequest();
		}
		catch(Exception e){
			Log.d("idoc-stop","close socket error");
		}
		mBTSocket = null;
		socketReadResult = null;
		commandNum = 1; //对应源程序中的commandSequenceNum
		receivedCommand = null;
		deviceConnected = false;
		//public BloodPressureThread bpThread = null;
		
		resultRead = false;
		diastolicPressure = 0;
		systolicPressure = 0;
		heartRate = 0;
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public BloodPressureThread fragmentBPThread = null; 

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.bp_fragment_main, container,
					false);	
			Log.d("idoc-bp","Create blood preassure thread!");
			WebView myWebView1 = (WebView) rootView.findViewById(R.id.webView1);
	        myWebView1.getSettings().setJavaScriptEnabled(true);
	        myWebView1.getSettings().setLoadWithOverviewMode(true);
	        myWebView1.getSettings().setSupportZoom(true);
	        myWebView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	        myWebView1.getSettings().setBuiltInZoomControls(true);
	        //myWebView1.loadUrl("file:///android_asset/blood_pressure.html");
	        myWebView1.loadUrl("");
			return rootView;
		}
	}
	
	public void bpMeasureClick(View v){
		if(processTimer == 0){
			startIdentify(v);
		}else if(processTimer == 1){
			sendAngleReady(v);
		}else if(processTimer == 2 && !isMeassuring){
			isMeassuring = true;
			measureBegin(v);
		}else if(processTimer == 2 && isMeassuring){
			Toast.makeText(BloodPressureFragment.this, "正在测量中，请勿重复点击测量按钮", 3000).show();
		}
	}
	
	public void startIdentify(View v){
		if(!mBTAdapter.isEnabled()){
			Toast.makeText(BloodPressureFragment.this, "请重新打开蓝牙然后重新点击测量!", 5000).show();
		}else{
			Set<BluetoothDevice> devices=mBTAdapter.getBondedDevices();
			BluetoothDevice localBTDevice = null;
			mBTReceiver = new BluetoothReceiver();
			String nameList = "";
			if(devices.size()>0){
				for(Iterator iterator = devices.iterator();iterator.hasNext();){
					localBTDevice = (BluetoothDevice)iterator.next();
					String name = localBTDevice.getName();
					String localAddress = localBTDevice.getAddress();
					String localDeviceName = localBTDevice.getName();
					Log.d("idoc bp",localDeviceName);
					int index = localDeviceName.indexOf(BPDEVICE);
					Log.d("idoc bp",index+"");
					Log.d("idoc",name);
					nameList += name+"||";
					Log.d("idoc-device",localAddress);
					if(index>-1){
						//BluetoothMsg.BlueToothAddress = device.getAddress();
						//Toast.makeText(TiwenActivity.this, "tiwenji found and bonded",5000).show();
						Log.d("idoc-tiwen", "one bp device found");
						break;
					}
					localBTDevice = null;
				}
				nameList.substring(0,nameList.length()-2);
				
				if(localBTDevice != null){
					this.mBTDevice = localBTDevice;
					try{
						Thread.currentThread().sleep(200);
						Log.d("idoc-tiwen","after sleep, try to create socket");
						this.mBTSocket = localBTDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
						this.mBTSocket.connect();
					}
					catch(Exception e){
						Log.d("idoc-socket","create socket exception");
						if(e !=  null){
							String exception = e.getMessage();
							Log.d("idoc-tiwen-exception","exception:"+exception);
						}
					}
				}
			}
		}
		
		if(mBTSocket==null){
			Toast.makeText(this, "连接血压仪失败，请检查血压仪是否打开", 3000).show();
		}else{
			mBPThread = new BloodPressureThread(mBTDevice,mBTSocket);
			mBPThread.mainHandler = this.mHandler;
			this.mBPThread.start();
			try{
				Thread.currentThread().sleep(100);
			}
			catch(Exception e){
				Log.d("idoc-sleep","exception during sleep");
			}
			
			while(this.mBPThread.mInputStream == null||this.mBPThread.mOutputStream == null){}
			Message msg = new Message();
			msg.what = 12;
			BloodPressureFragment.this.mHandler.sendEmptyMessage(msg.what);
			Log.d("idoc-tiwen","connect sokcet success");
		}
	}
	
	public void sendIdentifyCommand(){	
		
		if(this.mBPThread.mOutputStream == null){
			Log.d("idoc-identify","outputstream is still empty");
		}
		
		Log.d("idoc-identify","identify automatically");
		IdentifyDevice t1 = new IdentifyDevice();
		Thread identifyThread = new Thread(t1);
		identifyThread.start();
	}
	
	public void UpdateResult(){
		TextView upperView = (TextView) findViewById(R.id.diastolicPressure);
		upperView.setText(this.diastolicPressure+"");
		TextView lowerView = (TextView) findViewById(R.id.systolicPressure);
		lowerView.setText(this.systolicPressure+"");
		TextView heartView = (TextView) findViewById(R.id.heartRate);
		heartView.setText(this.heartRate+"");
		Message msg = new Message();
		msg.what = 13;
		this.mHandler.sendEmptyMessage(msg.what);
	}
	
	public class IdentifyDevice implements Runnable{
		private Thread runThread;
		public boolean finishFlag = false;
		@Override
		public void run(){
			runThread = Thread.currentThread();
/*			try{
				Thread.currentThread().sleep(100);
				Log.d("idoc-thread","thread sleep after");
			}
			catch(Exception e){
				Log.d("idoc-thread","exception appears during sleep");
			}*/
			
			byte[] arrayOfByte1 = new byte[16];
			byte[] arrayOfByte2 = new byte[16];
		    byte[] arrayOfByte3 = new byte[16];
		    byte[] arrayOfByte4 = new byte[16];
		    byte[] arrayOfByte6;
		    byte[] arrayOfByte7;
			byte[] arrayOfByte5 = { 126, -82, -121, 56, -126, 98, 40, 35, 12, -44, -70, -32, -105, -74, -59, 3 };//对应CommBlueTooth中Identification方法里面的arrayOfBytes5
			
			new Random(System.currentTimeMillis()).nextBytes(arrayOfByte1);
			
			
			for(int i = 0;i<16;i++){
				if(arrayOfByte1[i]<0){
					arrayOfByte1[i] = (byte)(0 - arrayOfByte1[i]);
				}
			}

			arrayOfByte6 = reverseByteArray(arrayOfByte1);
			arrayOfByte7 = new byte[18];
			arrayOfByte7[0] = -95;
			arrayOfByte7[1] = -6;
			
			Log.d("Identification","commandNum====="+commandNum);
					
			for(int j=0;j<16;j++){
				arrayOfByte7[(j + 2)] = arrayOfByte6[j];
			}

			byte[] arrayOfByte20 = new byte[23];
			arrayOfByte20 = PackageCommand(arrayOfByte7);

			String tempStr = Bytes2HexString(arrayOfByte20,arrayOfByte20.length);
			Log.d("idoc","send arrayOfByte20 : "+ tempStr);
			mBPThread.send(arrayOfByte20);

			int len = 0;
			while(len == 0){
				try{
					Thread.currentThread().sleep(100);
					Log.d("idoc-thread","thread sleep after");
				}
				catch(Exception e){
					Log.e("idoc-thread","thread exception during sleep");
				}
				
				len = BloodPressureFragment.this.mBPThread.lenReciveFromBTSocket;
			}
			byte[] bytesTemp = BloodPressureFragment.this.mBPThread.bufReciveFromBTSocket;
			BloodPressureFragment.this.mBPThread.bufReciveFromBTSocket = null;
			
			Log.d("idoc-thread","len:"+len);
			
			if(bytesTemp != null){
				BloodPressureFragment.this.Unpackage(bytesTemp, len);

				try{
					Thread.currentThread().sleep(100);
					Log.d("idoc-thread","thread sleep after");
				}
				catch(Exception e){
					Log.e("idoc-thread","thread exception during sleep");
				}
				
				BloodPressureFragment.this.mBPThread.readReady = true;

				byte[] arrayOfByte8;
				arrayOfByte8 = BloodPressureFragment.this.receivedCommand;
				
				String afb8 = Bytes2HexString(arrayOfByte8,arrayOfByte8.length);
				Log.d("idoc-thread","arrayOfByte8 is : "+afb8);
				if(arrayOfByte8 == null){
					Log.d("idoc-log","arrayOfByte8 is null");
				}
				else{
					if(arrayOfByte8[2]!=16){
						Log.d("idoc-unpackage","need ack");
						int length = arrayOfByte8[3];
						SendACK(length);
						Log.d("idoc-unpackage","sendack over");
					}
					
					for(int m=0;m<16;m++){
						arrayOfByte4[m] = arrayOfByte8[(m+2)];
						arrayOfByte2[m] = arrayOfByte8[(m + 18)];
						arrayOfByte3[m] = arrayOfByte8[(m + 34)];
					}
					byte[] arrayOfByte9;
					Log.i("CommBlueTooth Identification", "ID      =" + Bytes2HexString(arrayOfByte4, 16));
					Log.i("CommBlueTooth Identification", "R1_prime=" + Bytes2HexString(arrayOfByte2, 16));
			        Log.i("CommBlueTooth Identification", "R2_prime=" + Bytes2HexString(arrayOfByte3, 16));
			        arrayOfByte9 = XXTEA.encrypt(reverseByteArray(arrayOfByte4), arrayOfByte5);
			        byte[] arrayOfByte10 = XXTEA.encrypt(reverseByteArray(arrayOfByte2), arrayOfByte9);
			        Log.i("CommBlueTooth Identification", "R1_new  =" + Bytes2HexString(arrayOfByte10, 16));
			        Log.i("CommBlueTooth Identification", "R1      =" + Bytes2HexString(arrayOfByte1, 16));
			        byte[] arrayOfByte11 = XXTEA.encrypt(reverseByteArray(arrayOfByte3), arrayOfByte9);
			        byte[] arrayOfByte12 = reverseByteArray(arrayOfByte11);
			        byte[] arrayOfByte13 = new byte[18];
			        arrayOfByte13[0] = -95;
			        arrayOfByte13[1] = -4;
			        
			        for(int k=0;k<16;k++){
			        	arrayOfByte13[(k + 2)] = arrayOfByte12[k];
			        }
			        
			        commandNum += 2;
			        byte[] sendBytes = new byte[23];
			        sendBytes[0] = -80;
			        sendBytes[1] = (byte)(20);
			        sendBytes[2] = 0;
			        sendBytes[3] = (byte)commandNum;
			        int sum = 0;
			        sum = sendBytes[2] + sendBytes[3];
			        for(int m = 0; m<18;m++){
			        	sendBytes[m+4]=arrayOfByte13[m];
			        	sum += sendBytes[m+4];
			        }
			        sendBytes[22] = (byte)sum;
			        BloodPressureFragment.this.mBPThread.send(sendBytes);
			        this.finishFlag = true;
			        BloodPressureFragment.this.deviceConnected = true;
			        
				}
				Message msg = new Message();
				msg.what = 13;
				BloodPressureFragment.this.mHandler.sendEmptyMessage(msg.what);
			}
			
			
		}
		
	}

	public void sendAngleReady(View v){
		byte[] arrayOfByte1 = {-95,49,0,0,75,2,9,1,11,79};
		byte[] arrayOfByte2 = new byte[15];
		arrayOfByte2 = this.PackageCommand(arrayOfByte1);
		
		if(this.mBPThread!=null){
			try{
				this.mBPThread.send(arrayOfByte2);
			}
			catch(Exception e){
				if(e!=null){
					String msg = e.getMessage();
				}
			}
			
			Message msg = new Message();
			msg.what = 13;
			BloodPressureFragment.this.mHandler.sendEmptyMessage(msg.what);
		}
		//
		
	}
	
	public void measureBegin(View v){
		byte[] arrayOfByte = PackageCommand(this.ANGEL_YES);
		if(this.mBPThread!=null){
			try{
				this.mBPThread.send(arrayOfByte);
				Log.d("idoc-thread","send angel_yes after");
			}catch(Exception e){
				
			}
		}	
	}
		
	public byte[] reverseByteArray(byte[] paramArrayOfByte)
	{
	  byte[] arrayOfByte = new byte[16];
	  for (int i = 0;i < 4 ; i++)
	  {
	    arrayOfByte[i] = paramArrayOfByte[(3 - i)];
	    arrayOfByte[(i + 4)] = paramArrayOfByte[(7 - i)];
	    arrayOfByte[(i + 8)] = paramArrayOfByte[(11 - i)];
	    arrayOfByte[(i + 12)] = paramArrayOfByte[(15 - i)];
	  }
	  return arrayOfByte;
	}
	
	public String Bytes2HexString(byte[] paramArrayOfByte, int paramInt){
		String str = "";
		if(paramInt<256){
			for(int j=0;j<paramInt;j++){
				String str2 = Integer.toHexString(0xFF & paramArrayOfByte[j]);
				if(str2.length()==1){
					str2 = '0'+str2;
				}
				str +=str2.toUpperCase();
			}
		}
		return str;
	}
	
	public void submit(View v){
		String result = "";
		TextView dp = (TextView)findViewById(R.id.diastolicPressure);
		TextView sp = (TextView)findViewById(R.id.systolicPressure);
		TextView hr = (TextView)findViewById(R.id.heartRate);
		
		if(dp.getText()==""&&sp.getText()==""&&hr.getText()==""){
			Toast.makeText(BloodPressureFragment.this,"请先测量再提交",3000).show();
			return;
		}
		
		int elder_id = Integer.parseInt(this.elderID);
		int doctor_id = this.doctorID;
		
		BloodPressureCacheBean bPressure = new BloodPressureCacheBean(doctor_id,
														Float.valueOf(dp.getText()+""),
														Float.valueOf(sp.getText()+""));
		HeartRateCacheBean hRate = new HeartRateCacheBean(doctor_id, hr.getText()+"");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     
        String date = sDateFormat.format(new java.util.Date());
        bPressure.setTime(date);
        hRate.setTime(date);
		
		boolean submit = dbu.insertBloodPressure(elder_id, bPressure) & 
						dbu.insertHeartRate(elder_id, hRate);
		
		if(submit){
			Toast.makeText(BloodPressureFragment.this, "提交数据成功", 3000).show();
		}else{
			Toast.makeText(BloodPressureFragment.this, "提交数据失败，请重新测量!", 3000).show();
		}
	}
	
	public void cancel(View v){
		
	}
	
	public void back(View v){
		finish();
	}
	
	public byte[] PackageCommand(byte[] arrayOfByte){
		byte[] result = new byte[5+arrayOfByte.length];
		result[0] = -80;
		result[1] = (byte)(2+arrayOfByte.length);
		result[2] = 0;
		result[3] = (byte)this.commandNum;
		int sum = result[2]+result[3];
		
		for(int i = 0;i<arrayOfByte.length;i++){
			result[i+4]=arrayOfByte[i];
			sum += arrayOfByte[i];
		}
		
		result[4+arrayOfByte.length]=(byte)sum;
		
		return result;
	}
	
	public void Unpackage(byte[] paramArrayOfByte,int paramInt){
		if(paramInt<6)paramInt = 5;
		this.receivedCommand = new byte[paramInt-5];
		if(paramArrayOfByte[0] != -96){
			Log.e("Unpackage error","Package head error");
			return;
		}
		
		if(paramArrayOfByte[2]!=-16){
			Log.e("Unpackage error","need Ack");
			SendACK(paramArrayOfByte[3]);
		}
		
		for(int i=0;i<paramInt-5;i++){
			this.receivedCommand[i] = paramArrayOfByte[i+4];
		}
		
		if(this.receivedCommand.length>1&&this.receivedCommand[0]==-95&&this.receivedCommand[1]==54){
			BloodPressureFragment.this.resultRead = true;
			Message msg = new Message();
			msg.what = 100;
			BloodPressureFragment.this.mHandler.sendEmptyMessage(msg.what);
		}
		
		Log.i("receivedCommand","received command is :"+Bytes2HexString(this.receivedCommand,this.receivedCommand.length));
	}

	public void SendACK(int paramInt){
		byte[] arrayOfByte = new byte[6];
		arrayOfByte[0] = -80;
		arrayOfByte[1] = 3;
		arrayOfByte[2] = -96;
		arrayOfByte[3] = (byte)(paramInt +1);
		arrayOfByte[4] = -95;
		arrayOfByte[5] = (byte)(arrayOfByte[2] + arrayOfByte[3] + arrayOfByte[4]);
		this.mBPThread.send(arrayOfByte);
		this.commandNum += 2;
		Log.d("idoc-command-number","current command number is : "+commandNum);
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
					Toast.makeText(BloodPressureFragment.this, device.getName()+"not bonded",5000).show();
				}
				try{
					ClsUtils.setPin(device.getClass(), device, strPwd);
					ClsUtils.createBond(device.getClass(), device);
					ClsUtils.cancelPairingUserInput(device.getClass(), device);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
				try{
					BloodPressureFragment.this.commandNum = 1;
					BloodPressureFragment.this.mBTSocket.close();
					Log.d("idoc-bp","try to close the socket");
					BloodPressureFragment.this.mBPThread.stopRequest();
				}
				catch(Exception e){
					Log.d("idoc-exception","close disconnected socket failed");
				}
				
				Log.d("idoc-bt-connection","remote device disconnected");
			}
		}
	}
}
