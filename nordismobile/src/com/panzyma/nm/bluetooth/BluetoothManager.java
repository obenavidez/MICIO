package com.panzyma.nm.bluetooth;

import java.util.ArrayList;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

public class BluetoothManager 
{

	private static BluetoothManager instance=null;
	private BluetoothAdapter mAdapter;
	private ArrayList<BluetoothDevice> mDeviceList;
	private BluetoothDevice  mDevice=null;
	private boolean wasSearch=false;
	private Object lock;
	
    public BluetoothManager() throws Exception
	{
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mDeviceList = new ArrayList<BluetoothDevice>();
		// Get a BluetoothSocket for a connection with the given BluetoothDevice
		if (mAdapter == null)
			throw new Exception("El Dispositivo no soporta comunicación bluetooth");
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		UserSessionManager.getContext().registerReceiver(mReceiver, filter);		
	}
	
	public static BluetoothManager getInstace() throws Exception{
		if(instance == null) 
		{
			synchronized(BluetoothManager.class) { 
				 instance = new BluetoothManager();
			}
	        
        }
        return instance;
	}
	
	public synchronized BluetoothDevice getDevice(){
		return mDevice;
	}
	
	public synchronized boolean isActived(){
		if(mAdapter!=null && mAdapter.isEnabled())
			return true;
		return false;
	}
	
	public synchronized  boolean wasSearch()
	{
		return wasSearch;
	}
	
	private Handler puente = new Handler() 
	{
		  @Override
		  public void handleMessage(Message msg) {
			//  if(isActived() && wasSearch() && getDevice()==null) 
		  }
	};
	
	public synchronized void activateBluetooth() throws Exception
	{ 
		 wasSearch=false;
		if(mAdapter!=null && mAdapter.isEnabled())
		{  
			mDevice=findBT();
			if (mDevice == null)
				throw new Exception("No se ha configurado la impresora...");
			return;
		}else{
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			UserSessionManager.getContext().startActivity(intent);
			
			UserSessionManager._context.runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{ 
				    try 
					{
						
						NMApp.getThreadPool().execute(new Runnable()
						{ 
						

							@Override
							public void run()
						    {					 
								try 
								{					 
									while (true) 
									{
										if((isActived() && wasSearch() && getDevice()==null) || getDevice()!=null)
											break;
									}
									synchronized (lock) {
										lock.notify();
									}
									
								} catch (Exception e) {
									
									e.printStackTrace();
								}
						    }});
						
						
						
						
					} catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
		    });
			
		    synchronized(lock)
	        {
	            try {
	            	lock.wait();
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
	        }
		    
		    
		}		 
		
	} 
	 
	@SuppressWarnings("static-access")
	public synchronized BluetoothDevice findBT() 
	{
		 wasSearch=true;
		ArrayList<BluetoothDevice> pairedDevices =mDeviceList;
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getAddress().equals(
						SessionManager.getImpresora().obtenerMac())) {
					return device;
				}

			}
		}
		return null;
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {	

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction();
			if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) 
			{
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

				if (state == BluetoothAdapter.STATE_ON) 
				{  
					if(mAdapter.getBondedDevices().isEmpty())
						mAdapter.startDiscovery();
					else
					{
						mDeviceList.addAll(mAdapter.getBondedDevices()); 
						mDevice=findBT();						
					}
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
			{
				mDevice=findBT();
			}
		}
	};
	
	
} 