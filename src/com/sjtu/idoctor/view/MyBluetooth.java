package com.sjtu.idoctor.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sjtu.idoctor.model.BluetoothCacheBean;
import com.sjtu.idoctor.utils.DBUtil;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MyBluetooth extends Activity{
	private String elderName = null;
	private String roomNo = null;
	private String elderID = null;
	private String itemName = null;
	private String command = "AT+SMRS=1\r\nAT+SMST\r\n";
	private String ecg_command = "AT+SMTP=0\r\n";
	private String getID_command = "AT+DCSN\r\n";
	Context mContext;
	private int mState;
	private String address;
	private BluetoothDevice device = null;
	private BluetoothSocket socket = null;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private receiveThread mReceiveThread = null;
	private ConnectedThread mConnectedThread = null;
	DBUtil dbUtil = new DBUtil();
	public String data="";

	
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			if(msg.what == 11){
				//Toast.makeText(mContext, data, 5000).show();
				String str = dbUtil.insertECG(elderID, "8", "2015/1/14", "20", "2015/1/14 13:34:29.125", data);
				//Toast.makeText(MyBluetooth.this, str, 1000).show();
			}
			else if(msg.what == 5){
				try{
					Log.d("idoc","handle msg 5");
				}
				catch(Exception e){}
				
			}

		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = this;
		
		Intent intent =getIntent();
		elderName = intent.getStringExtra("elderName");
		elderID = intent.getStringExtra("elderID");
		roomNo = intent.getStringExtra("roomNo");
		itemName = intent.getStringExtra("itemName");

		final LinearLayout layout_body = new LinearLayout(this);
		layout_body.setOrientation(LinearLayout.VERTICAL);
		for(int i=0;i<2;i++){
			Button startButton = new Button(this);
			if(i==0){
				startButton.setText("上传到服务器");
			}
			else{
				startButton.setText("测量");
			}
			startButton.setId(i);
			startButton.setOnClickListener(startOnClickListener);		
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_body.addView(startButton,lp);
		}
		setContentView(layout_body);
		init();
	}

	private void init(){
		address = BluetoothCacheBean.BlueToothAddress;
		Log.d("idoc","address "+address);
		Log.d("idoc","init success");
	}
	
	private View.OnClickListener startOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int i = arg0.getId();
			byte[] localCommand;
			try{
				if(i==0){
					String str = dbUtil.insertECG(elderID, "126", "2015/1/14", "20", "2015/1/14 13:34:29.125", "07340738");
					Toast.makeText(MyBluetooth.this, str, 1000).show();
				}else{
					mReceiveThread = new receiveThread();
					MyBluetooth.this.mReceiveThread.start();
					
/*					localCommand = MyBluetooth.this.command.getBytes();
					
					Log.d("idoc","receive thread over");
					
					MyBluetooth.this.socket.getOutputStream().write(localCommand);
					ConnectedThread mmThread = new ConnectedThread(MyBluetooth.this.socket);
					mmThread.start();*/
				}
			}
			catch(Exception e){
/*				String msg = e.getMessage().toString();
				Log.d("idoc","failed "+msg);*/
				Log.d("idoc", "write command failed");
			}
			
			//socket.getOutputStream().write(MyBluetooth.this.ecg_command.getBytes());
		}
	};
	
	@Override
	public void onResume(){
		
		device = mBluetoothAdapter.getRemoteDevice(address);
		Log.d("idoc","start connect");
		super.onResume();
	}
	
	private synchronized void setState(int paramInt){
		try{
			this.mState=paramInt;
			return;
		}
		catch(Exception e){
			
		}
	}
	
	
	private class receiveThread extends Thread{
	    private InputStream mmInStream;
	    private OutputStream mmOutStream;
	    private BluetoothSocket mmSocket;

	    public void receiveThread(){

	    }
	    
	    public void cancel(){
	    	try{
	    		mmSocket.close();
	    		MyBluetooth.this.socket.close();
	    		return;
	    	}
	    	catch(IOException e){
	    		Log.e("cancel","",e);
	    	}
	    }
	    
		@Override
		public void run(){
			//byte[] paramArrayOfByte = MyBluetooth.this.ecg_command.getBytes();
			try{
				Thread.currentThread().sleep(1000);
				MyBluetooth.this.socket = MyBluetooth.this.device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
				MyBluetooth.this.socket.connect();
				
				mConnectedThread = new ConnectedThread(MyBluetooth.this.socket);
				
				mmSocket = MyBluetooth.this.socket;
				Log.d("idoc-thread","connect over");
				//socket.getOutputStream().write(paramArrayOfByte);
				Log.d("idoc",MyBluetooth.this.command);
				try{
					Thread.currentThread().sleep(1000);
					byte[] localCommand = MyBluetooth.this.ecg_command.getBytes();
					MyBluetooth.this.socket.getOutputStream().write(localCommand);
					Log.d("idoc", "write ecg_command success");
					
					try{
						Thread.currentThread().sleep(1000);
						localCommand = MyBluetooth.this.command.getBytes();
						MyBluetooth.this.socket.getOutputStream().write(localCommand);
						Log.d("idoc", "write command success");
						mConnectedThread.start();
					}
					catch(Exception e){
						
					}
				}
				catch(Exception e){
					Log.d("idoc", "write ecg_command failed");
					Toast.makeText(MyBluetooth.this, "write command failed", 1000).show();
					Message msg = new Message();
					msg.what = 5;
					MyBluetooth.this.mHandler.sendMessage(msg);
				}
				//this.mmOutStream.write(paramArrayOfByte);
			}
			catch(IOException e){ 
				Log.d("idoc-error","connect failed");
				Log.e("connect","",e);
			}
			catch(Exception e){
				
			}
		}
	}
	
	private class ConnectedThread extends Thread{
		private InputStream mmInStream;
		private OutputStream mmOutStream;
		private final BluetoothSocket mmSocket;
		private int[] ECGbuffer;
		private int ECGstep;
		
		public ConnectedThread(BluetoothSocket btSocket){
			mmSocket = btSocket;
			ECGstep = 0;
			try{
				mmInStream = btSocket.getInputStream();
			}
			catch(Exception e){
				mmInStream = null;
			}
			
			try{
				mmOutStream = btSocket.getOutputStream();
			}
			catch(Exception e){
				mmOutStream = null;
			}
		}
		
		@Override
		public void run(){
			byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	        int localdata = 0;
	        int index = 0;
	        int sumFlag = 0;
	        String ECGstr = "";
	        Message msg  = new Message();
	        ECGbuffer = new int[5000];
	        for(;index<5000;index++){
	        	ECGbuffer[index] = 0;
	        }
	        index = 0;

	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
	                ECGstr = bytes+";";
	                if(ECGstep>4998){
	                	msg.what = 11;
	                	MyBluetooth.this.data = ECGstr+"";
	                	MyBluetooth.this.mHandler.sendMessage(msg);
	                	ECGstep = 0;
	                }
	                
	                if(bytes>60&&index==0){
	                	sumFlag = 1;
	                }
	                
	                if(sumFlag>0){
	                	localdata = localdata*100+bytes;
	                	index++;
	                }
	                
	                if(index>5 && sumFlag == 1){
	                	sumFlag = 0;
	                	//msg.what = 11;//取好数据，显示出来并传送到服务器
	                	
	                	//MyBluetooth.this.data = localdata+"";
	                	//MyBluetooth.this.mHandler.sendMessage(msg);
	                }
	                Log.d("bluetooth data",bytes+"");
	                // Send the obtained bytes to the UI Activity
	                //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
	            } catch (IOException e) {
	                break;
	            }
	        }
		}
		
	}
}
