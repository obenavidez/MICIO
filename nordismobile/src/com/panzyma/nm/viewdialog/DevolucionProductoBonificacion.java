package com.panzyma.nm.viewdialog; 
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog; 
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; 
import android.widget.TextView;

public class DevolucionProductoBonificacion extends DialogFragment
{ 
	private static DevolucionProductoBonificacion dpc; 	
	int cantidaddevolver;
	private String productname;
	 
	
	public static DevolucionProductoBonificacion newInstance(String _productname,int  cantidaddevolver) 
	{
		if(dpc==null)
			dpc = new DevolucionProductoBonificacion(_productname,cantidaddevolver);  
		else
		{
			dpc.setCantidadDevolver(cantidaddevolver);
			dpc.setNombreProducto(_productname);
		}
		
	    return dpc;
	}
	
	DevolucionProductoBonificacion(){}
	
	DevolucionProductoBonificacion(String _productname,int _cantidaddevolver){
		productname=_productname;
		cantidaddevolver=_cantidaddevolver;
	}
	
	private void setCantidadDevolver(int _cantidaddevolver){		
		cantidaddevolver=_cantidaddevolver;
	}
	
	private int getCantidadDevolver(){		
		return cantidaddevolver;
	}
	private void setNombreProducto(String _productname){		
		productname=_productname;
	}
	
	private String getNombreProducto(){		
		return productname;
	}
	
	escucharModificacionProducto mylisterner;
	
	private EditText qtybonif;
	private EditText qtyreturn;
	private Button btnOK;
	private Button btnCancel;
	private TextView tboxproducto; 
	
	public interface escucharModificacionProducto {
		public abstract void onButtonClick(int cantbonifdev);
	}
	public void obtenerProductoModificado(escucharModificacionProducto listener){
		this.mylisterner=listener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);   
	}
	@SuppressLint("InflateParams") 
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());  
    	
    	LayoutInflater inflater = getActivity().getLayoutInflater(); 
		
		View view = inflater.inflate(R.layout.devolverbonificacion, null);
    	
		btnOK=(Button) view.findViewById(R.id.btnOK);
		btnCancel=(Button) view.findViewById(R.id.btnCancel);

		btnOK.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) { 
				
				int cantidad = 0; 
				if (qtyreturn.getText().toString().trim() == "")
					return;
				
				if (qtybonif.getText().toString().trim() == "")
					return;

				cantidad = Integer.parseInt(qtybonif.getText().toString()); 
				if (cantidad <= 0) {
					qtybonif.setError("cantidad a bonificar debe ser mayor a cero");
					return;
				} 			 
				mylisterner.onButtonClick(cantidad);
				dismiss();
				
				
			}});
		
		btnCancel.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) { 
				dismiss();
				
			}});
		
		tboxproducto=(TextView) view.findViewById(R.id.tv_Producto); 
		tboxproducto.setText(""+ getNombreProducto());
		 
		
		qtybonif=(EditText) view.findViewById(R.id.et_cantBonfDevolver); 
		qtybonif.requestFocus(); 

		qtyreturn=(EditText) view.findViewById(R.id.et_cantDevolver);  
		qtyreturn.setText(""+getCantidadDevolver());
		alert.setView(view);
 
		alert.setCancelable(false);     	
    	
        return alert.create();   	
    } 
	
}
