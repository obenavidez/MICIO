package com.panzyma.nm.viewdialog;
  
import com.panzyma.nm.view.ViewReciboEdit; 
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface; 
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
 

public class DialogNotaRecibo extends DialogFragment 
{


	public static DialogNotaRecibo nr = new DialogNotaRecibo();
	
	private String nota;
	
	private EditText tboxNota;
	
	private ViewReciboEdit parent;
	
	RespuestaNotaRecibo mylisterner;
	public interface RespuestaNotaRecibo {
		public abstract void onButtonClick(String nota);
	}
	
	public void establecerRespuestaNotaRecibo(RespuestaNotaRecibo listener){
		this.mylisterner=listener;
	}
	
	public static DialogNotaRecibo newInstance(String nota,ViewReciboEdit _parent) 
	{
		if(nr==null)
			nr = new DialogNotaRecibo(); 
		nr.setNota(nota);
		nr.setParent(_parent); 
	    return nr;
	}

	public void setNota(String nota){
		this.nota=nota;
	}
	
	public String getNota(){
		return this.nota;
	}
	
	public void  setParent(ViewReciboEdit _parent)
	{
		this.parent=_parent;
	} 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{		 

		
		LayoutInflater inflater = parent.getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
		
		View view = inflater.inflate(R.layout.nota_recibo, null);
		tboxNota = (EditText) view.findViewById(R.id.rectbox_nota);
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
                  
            }
		});
		builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
			 @Override
             public void onClick(DialogInterface d, int which) {
				Fragment prev = getFragmentManager().findFragmentByTag("dialogNotaRecibo");
			    if (prev != null) {
			        DialogFragment df = (DialogFragment) prev;
			        df.dismiss();
			    }	
				
             }
		});	
		
		Dialog dialog = builder.create();
		tboxNota.setText(getNota());
		
        return dialog;			
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
                	if(tboxNota.getText().toString().equals(""))
                		tboxNota.setError("Ingrese Nota del Recibo");
                	else
                	{
                		mylisterner.onButtonClick(tboxNota.getText().toString());
                		dismiss(); 
                	}
											
					 					
                }
            });    
	        
	    }
	}	
   	
   	
}
