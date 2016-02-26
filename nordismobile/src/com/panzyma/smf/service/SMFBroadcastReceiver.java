package com.panzyma.smf.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SMFBroadcastReceiver extends BroadcastReceiver
{
	// restart service every hours.
	  private static final long REPEAT_TIME =3600*1000;

	  @Override
	  public void onReceive(Context context, Intent intent) 
	  {
		  	AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		    Intent intenservice = new Intent(context, SMFService.class);
		    PendingIntent pending = PendingIntent.getBroadcast(context, 0, intenservice,
		        PendingIntent.FLAG_CANCEL_CURRENT);
		    Calendar cal = Calendar.getInstance();
		    // start 10 minute after boot completed
		    cal.add(Calendar.MINUTE, 10); 
		
		    service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);
	  }
}
