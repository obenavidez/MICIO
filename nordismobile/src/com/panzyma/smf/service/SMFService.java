package com.panzyma.smf.service;

import com.panzyma.nm.CBridgeM.BDevolucionM;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class SMFService extends Service{

	 private boolean isRunning;
	 private Context context;
	 Thread backgroundThread;
	    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 this.context = this;
	     this.isRunning = false;
	     this.backgroundThread = new Thread(myTask);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY; 
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
		this.isRunning = false;
	}
	
	@Override
	public IBinder onBind(Intent intent) { 
		return null;
	}

	private Runnable myTask = new Runnable() {
        public void run() {
        	try 
        	{
				BDevolucionM.EnviarDevolucion(context);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            stopSelf();
        }
    };
}
