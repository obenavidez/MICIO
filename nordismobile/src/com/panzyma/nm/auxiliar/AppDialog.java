package com.panzyma.nm.auxiliar;

import com.panzyma.nm.viewdialog.GenericCustomDialog;
import com.panzyma.nm.viewdialog.GenericCustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.viewdialog.GenericCustomDialog.OnDismissDialogListener;

import android.app.Activity;
import android.view.View;

public class AppDialog {

	static Object lock=new Object();
	static Object lock2=new Object(); 
	private static Boolean response=false;
    private static  Activity context;
	private static android.support.v4.app.FragmentManager manager;
	private static GenericCustomDialog dialog;
	
    public static Boolean responseDialog(final String msg,final int type)
   	{
    	response=false;	
    	context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog = GenericCustomDialog.newInstance("Eliminar",msg,type);
				dialog.setOnActionDialogButtonClickListener(new OnActionButtonClickListener()
				{
					@Override
					public void onButtonClick(View _dialog, int actionId) {
						if(actionId == GenericCustomDialog.OK_BUTTOM) 
							response= true;
						else 
							response= false;
						
						dialog.dismiss();
					}
				});
				dialog.setOnDismissDialogListener(new OnDismissDialogListener() {
					@Override
					public void onDismiss() {
						synchronized (lock) { 
							lock.notify(); 
						} 
					}
				});
		    	dialog.show(manager, "dialog");
			}
    	});
    	
    	synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
    	return response;
   	}
    
    public static void setContext(Activity _context)
	{
		context=_context;
	}
    public static void setContext(android.support.v4.app.FragmentManager _manager)
	{
    	manager = _manager;
    }
    
}
