package com.sjtu.idoctor.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class ClsUtils {
	static public boolean createBond(Class btClass, BluetoothDevice btDevice)
			throws Exception
	{
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * 与设备解除配�?参�?源码：platform/packages/apps/Settings.git
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean removeBond(Class btClass, BluetoothDevice btDevice)
			throws Exception
	{
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	static public boolean setPin(Class btClass, BluetoothDevice btDevice,
			String str) throws Exception
	{
		try
		{
			Method removeBondMethod = btClass.getDeclaredMethod("setPin",
					new Class[]
					{byte[].class});
			Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
					new Object[]
					{str.getBytes()});
			Log.e("returnValue", "" + returnValue);
		}
		catch (SecurityException e)
		{
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	// 取消用户输入
	static public boolean cancelPairingUserInput(Class btClass,
			BluetoothDevice device)

	throws Exception
	{
		Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
		// cancelBondProcess()
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	// 取消配对
	static public boolean cancelBondProcess(Class btClass,
			BluetoothDevice device)

	throws Exception
	{
		Method createBondMethod = btClass.getMethod("cancelBondProcess");
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	/**
	 * 
	 * @param clsShow
	 */
	static public void printAllInform(Class clsShow)
	{
		try
		{
			// 取得�?��方法
			Method[] hideMethod = clsShow.getMethods();
			int i = 0;
			for (; i < hideMethod.length; i++)
			{
				Log.e("method name", hideMethod[i].getName() + ";and the i is:"
						+ i);
			}
			// 取得�?��常量
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++)
			{
				Log.e("Field name", allFields[i].getName());
			}
		}
		catch (SecurityException e)
		{
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
