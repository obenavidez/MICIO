package com.panzyma.nm.auxiliar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Util {
	
	public static class Message {
		
		@SuppressLint("ShowToast")
		public static Toast buildToastMessage(Context cnt, String msg,int duration)
		{
			Toast toast= Toast.makeText(cnt,msg,duration);  
			toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
			return toast;
		}
	}

}
