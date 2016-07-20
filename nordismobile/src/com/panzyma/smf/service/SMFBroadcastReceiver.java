package com.panzyma.smf.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SMFBroadcastReceiver extends BroadcastReceiver
{
	// restart service every hours. 

	  @Override
	  public void onReceive(Context context, Intent intent) 
	  { 
	    	Intent background = new Intent(context, SMFService.class);
	    	context.startService(background);   
	  }
}
