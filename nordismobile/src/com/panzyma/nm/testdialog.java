package com.panzyma.nm;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.panzyma.nordismobile.R;
//import android.app.Dialog;

public class testdialog extends AlertDialog{

	Context ctx;
	public testdialog(Context context) {
		super(context, android.R.style.Theme_Translucent);  
		ctx=context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customdialog); 
	    
		RelativeLayout rl=(RelativeLayout)findViewById(R.id.rldialog);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
	    																 android.view.ViewGroup.LayoutParams.WRAP_CONTENT);	
		
		ScrollView scroll=new ScrollView(this.getContext());
		 
		params.setMargins(30,10, 10, 10);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		TextView tv2 = new TextView(this.getContext());
		tv2.setText("test dialogo....");
		params.setMargins(30,10, 10, 10);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		
		tv2.setLayoutParams(params);
		rl.addView(tv2);
		
	    
	    
	    
	} 

}
