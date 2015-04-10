package com.sjtu.idoctor.utils;

public class Functions {
	public static String Bytes2HexString(byte[] paramArrayOfByte, int paramInt){ 
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
}
