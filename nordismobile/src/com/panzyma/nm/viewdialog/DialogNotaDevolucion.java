package com.panzyma.nm.viewdialog;

import com.panzyma.nm.view.ViewDevolucionEdit;
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogNotaDevolucion extends DialogFragment {
	
	public static final String FRAGMENT_TAG = "dialogNotaDevolucion";

	public static DialogNotaDevolucion nr = new DialogNotaDevolucion();
	private String nota;
	private EditText tboxNota;
	private ViewDevolucionEdit parent;
	RespuestaNotaDevolucion mylisterner;
	TextView tv;

	public interface RespuestaNotaDevolucion {
		public abstract void onButtonClick(String nota);
	}

	public void establecerRespuestaNotaDevolucion(
			RespuestaNotaDevolucion listener) {
		this.mylisterner = listener;
	}

	public static DialogNotaDevolucion newInstance(String nota,
			ViewDevolucionEdit _parent) {
		if (nr == null)
			nr = new DialogNotaDevolucion();
		nr.setNota(nota);
		nr.setParent(_parent);
		return nr;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getNota() {
		return this.nota;
	}

	public void setParent(ViewDevolucionEdit _parent) {
		this.parent = _parent;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		LayoutInflater inflater = parent.getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
		
		View view = inflater.inflate(R.layout.nota_recibo, null);
		tboxNota = (EditText) view.findViewById(R.id.rectbox_nota);
		tv = (TextView) view.findViewById(R.id.rectxv_nota);
		tv.setText("Nota de la Devolución");
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
                  
            }
		});
		builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
			 @Override
             public void onClick(DialogInterface d, int which) {
				Fragment prev = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
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
                		tboxNota.setError("Ingrese Nota de la Devolución");
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
