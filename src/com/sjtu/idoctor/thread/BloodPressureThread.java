package com.sjtu.idoctor.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.sjtu.idoctor.utils.Functions;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BloodPressureThread extends Thread{
	public static final String bp7Address = "8C:DE:52:08:D7:1A";
	public InputStream mInputStream = null;
	public OutputStream mOutputStream = null;
	public BluetoothSocket mBTsocket = null;
	public byte[] bufReciveFromBTSocket = new byte[256];
	public int lenReciveFromBTSocket = 0;
	public String commandOriginalRecive = null;
	public Handler mainHandler = null;
	public boolean readReady = false;
	public BluetoothDevice mBTDevice = null;
	public BluetoothAdapter mBTAdapter = null;
	private volatile boolean stopRequested;
	private Thread runThread;
	private int readErrorNum = 0;
	
	public BloodPressureThread(BluetoothDevice btDevice, BluetoothSocket btSocket){
		this.mBTsocket = btSocket;
		this.mBTDevice = btDevice;
		
		this.mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		
		try{
			this.mInputStream = btSocket.getInputStream();
			this.mOutputStream = btSocket.getOutputStream();
		}catch(IOException e){
			Log.d("bowen-init","create bpThread failed");
		}
	}
	
	public boolean send(byte[] paramArrayOfByte){
		try{
			if(mInputStream != null){
				Log.d("bowen-send",Functions.Bytes2HexString(paramArrayOfByte, paramArrayOfByte.length));
				mOutputStream.write(paramArrayOfByte,0,paramArrayOfByte.length);
				mOutputStream.flush();
				Log.d("bowen-write","write   "+paramArrayOfByte.toString());
				
				return true;
			}
			else return false;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public byte[] read(){
		try{
			if(this.mInputStream == null){
				Log.d("bowen-read","bp thread outputstream is still null");
			}
			
			Log.d("bowen-read","begin to read");
			
			byte[] arrayOfByte = new byte[256];
			
			this.lenReciveFromBTSocket = mInputStream.read(arrayOfByte);
			this.bufReciveFromBTSocket = arrayOfByte;
			//MainActivity.this.socketReadResult = this.bufReciveFromBTSocket;
			this.commandOriginalRecive = Functions.Bytes2HexString(this.bufReciveFromBTSocket, this.lenReciveFromBTSocket);
			
			Log.d("bowen-read","read result is len:"+this.lenReciveFromBTSocket+"   data:"+this.commandOriginalRecive);
			
			if(this.lenReciveFromBTSocket != 55){
				Message msg = new Message();
				msg.what = 10;
				mainHandler.sendEmptyMessage(msg.what);		
				Log.d("bowen-read","send message after");
			}
			
			if(readErrorNum>0){
				readErrorNum--;
			}
			
			return arrayOfByte; 
		}
		catch(Exception e){
			readErrorNum++;
			Log.d("bowen-socket","read command failed");
			if(e != null){
				Log.d("bowen-socket-exception","read command exception: "+e.getMessage());
			}
			return null;
		}
	}
	
	@Override
	public void run(){
		Log.d("bowen-tiwen","connect sokcet success");
		runThread = Thread.currentThread();
		stopRequested = false;
		try{
			InputStream localIS = this.mBTsocket.getInputStream();
			mInputStream = localIS;
			OutputStream localOS = this.mBTsocket.getOutputStream();
			mOutputStream = localOS;
			
			byte[] buffer = new byte[256];
			int bytes;
			int flag = 0;
			int lastFlag = -1;
			String inputStr = "";
			int exceptionNum = 0;
							
			while(true){
				if(mInputStream!=null && !readReady){
					Thread.currentThread().sleep(100);
					break;
				}
			}
							
			while(true){
				if(mInputStream != null){
					try{
						if(readErrorNum>10){
							Log.d("bowen-thread","socket error");
							break;
						}
						read();
					}
					catch(Exception e){
					}
				}else{
					break;
				}
			}
			Message msg = new Message();
			msg.what = 1000;
			mainHandler.sendEmptyMessage(msg.what);
		}
		catch(Exception e){
			
		}
	}
	
	public void stopRequest() {
		stopRequested = true;
		
		if ( runThread != null ) {
			this.mInputStream=null;
			this.mOutputStream = null;
			this.mainHandler = null;
			this.mBTDevice = null;
			this.runThread = null;
			try{
				this.mBTsocket.close();
			}
			catch(IOException e){
				Log.d("bowen-thread","bp socket closing failed");
			}
			runThread.interrupt();
		}
	}
}
