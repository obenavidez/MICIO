package com.panzyma.nm.viewdialog;

import com.panzyma.nm.view.ViewDevolucionEdit; 
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment; 
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button; 

public class DialogCosteoDevolucion extends DialogFragment {

	public static final String FRAGMENT_TAG = "DialogCosteoDevolucion";

	public static DialogCosteoDevolucion nr = new DialogCosteoDevolucion(); 
	private ViewDevolucionEdit parent;  
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		LayoutInflater inflater = parent.getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
		
		View view = inflater.inflate(R.layout.costeodevolucion, null); 
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
                  
            }
		});
		
		Dialog dialog = builder.create(); 
		
        return dialog;		
	}
	
	public static DialogCosteoDevolucion newInstance(
			ViewDevolucionEdit _parent) {
		if (nr == null)
			nr = new DialogCosteoDevolucion(); 
		nr.setParent(_parent);
		return nr;
	}
	
	public void setParent(ViewDevolucionEdit _parent) {
		this.parent = _parent;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) 
   	{   		
		super.onDismiss(dialog);
	}
	
	@Override
	public void onStart()
	{
	    super.onStart();    
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d != null)
	    {
	        Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
	        positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                { 
                		dismiss();   			
                }
            });    
	        
	    }
	}
}
