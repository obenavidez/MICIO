package com.panzyma.nm.viewdialog;

import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; 
import android.widget.TextView;

public class DevolucionProductoCantidad extends DialogFragment
{ 
	private static DevolucionProductoCantidad dpc; 	
	DevolucionProductoLote plote;
	private String productname;
	 
	
	public static DevolucionProductoCantidad newInstance(String _productname,DevolucionProductoLote _plote) 
	{
		if(dpc==null)
			dpc = new DevolucionProductoCantidad(_productname,_plote);  
		else
		{
			dpc.setLote(_plote);
			dpc.setNombreProducto(_productname);
		}
		
	    return dpc;
	}
	
	DevolucionProductoCantidad(){}
	
	DevolucionProductoCantidad(String _productname,DevolucionProductoLote _plote){
		productname=_productname;
		plote=_plote;
	}
	
	private void setLote(DevolucionProductoLote _plote){		
		plote=_plote;
	}
	
	private void setNombreProducto(String _productname){		
		productname=_productname;
	}
	
	private String getNombreProducto(){		
		return productname;
	}
	
	escucharModificacionProductoLote mylisterner;
	
	private EditText qtydelivered;
	private EditText qtyreturn;
	private Button btnOK;
	private Button btnCancel;
	private TextView tboxproducto;
	private EditText tboxlote;
	
	public interface escucharModificacionProductoLote {
		public abstract void onButtonClick(DevolucionProductoLote plote);
	}
	public void obtenerProductoLoteModificado(escucharModificacionProductoLote listener){
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
		
		View view = inflater.inflate(R.layout.devolvercantidad, null);
    	
		btnOK=(Button) view.findViewById(R.id.btnOK);
		btnCancel=(Button) view.findViewById(R.id.btnCancel);

		btnOK.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) { 
				
				int cantidad = 0;
				int cantidadespachada = 0;
				if (qtyreturn.getText().toString().trim() == "")
					return;

				cantidad = Integer.parseInt(qtyreturn.getText().toString());
				cantidadespachada = Integer.parseInt(qtydelivered.getText().toString());
				if (cantidad <= 0) {
					qtyreturn.setError("cantidad a devolver debe ser mayor a cero");
					return;
				}

				if (cantidad > cantidadespachada && plote.getCantidadDespachada()!=0) 
				{
					qtyreturn.setError("cantidad a devolver debe ser menor a la cantidad despachada("+cantidadespachada+")");
					return;
				}					
				plote.setCantidadDevuelta(cantidad); 
				mylisterner.onButtonClick(new  DevolucionProductoLote(plote));
				dismiss();
				
				
			}});
		
		btnCancel.setOnClickListener(new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) { 
				dismiss();
				
			}});
		
		tboxproducto=(TextView) view.findViewById(R.id.tv_Producto); 
		tboxproducto.setText(""+ getNombreProducto());
		
		tboxlote=(EditText) view.findViewById(R.id.et_lote);
		
		qtydelivered=(EditText) view.findViewById(R.id.et_cantDespachada); 
		
		tboxlote.setText(""+ plote.getNumeroLote());
		tboxlote.setEnabled(false);
		
		qtydelivered.setText(""+ plote.getCantidadDespachada());
		qtydelivered.setEnabled(false);

		qtyreturn=(EditText) view.findViewById(R.id.et_cantDevolver);  
		if(plote.getCantidadDevuelta()!=0)
			qtyreturn.setText(""+ plote.getCantidadDevuelta());
		qtyreturn.requestFocus();
		alert.setView(view);
 
		alert.setCancelable(false);     	
    	
        return alert.create();   	
    } 
	
}
