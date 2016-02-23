package com.panzyma.nm.CBridgeM;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Message;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;

public abstract class BBaseM {

	private Controller<?, ?> controller;
	private ThreadPool pool;
	private Context context;

	public BBaseM() {
		super();
	}

	public Controller<?, ?> getController() {
		if (controller == null)
			controller = NMApp.getController();
		return controller;
	}

	public ThreadPool getPool() {
		if (pool == null)
			pool = NMApp.getThreadPool();
		return pool;
	}

	public Context getContext() {
		if (context == null)
			context = NMApp.getContext();
		return context;
	}
	
	public ContentResolver getResolver(){
		return getContext().getContentResolver();
	}
	
	public abstract boolean handleMessage(Message msg) throws Exception;

}
