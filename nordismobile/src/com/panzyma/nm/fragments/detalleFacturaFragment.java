package com.panzyma.nm.fragments;

import java.util.ArrayList;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BVentaM.Petition;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.serviceproxy.CDetalleFactura;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.DetallesFacturaHolder;
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

@InvokeBridge(bridgeName = "BVentaM")
public class detalleFacturaFragment extends DialogFragment  implements Handler.Callback {

	private  final int MOSTRAR_DETALLE = 3;
	private ProgressDialog waiting;
	private static detalleFacturaFragment detalle ;
	private Context context;
	static AlertDialog dialog =null;
	private View view;
	ArrayList<CDetalleFactura> listadetalle;
	private GenericAdapter adapter;
	private ListView listviewdetalle;
	long  factura_id;
	
	// Singleton 
	public static detalleFacturaFragment newInstance (long pfactura_id){
			if(detalle == null) detalle = new detalleFacturaFragment(pfactura_id);	
			return detalle;
	} 
	
	private detalleFacturaFragment(long pfactura_id){
		factura_id = pfactura_id;
	}
	
	
	@Override
	public void onStart()
	{
	    super.onStart();    
	    context = getActivity();
	    AlertDialog d = (AlertDialog)getDialog();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		NMApp.controller.setView(this); 
		SessionManager.setContext(getActivity());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.lista_detalle_factura, null);
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(waiting!=null)
					waiting.dismiss();
				dismiss();
			}
		});
		builder.setOnKeyListener(keyListener);
		dialog = builder.create();
		ObtenerDetalle();
		
		listviewdetalle = (ListView) view.findViewById(R.id.listview_detalle_factura);
		
		return dialog;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		if(waiting!=null)waiting.dismiss();
		if( msg.what < 4) 
		{
			Petition p =  Petition.toInt(msg.what);
			switch (p) 
			{ 
				case OBTENER_DETALLE_FACTURA: 
					listadetalle  =(ArrayList<CDetalleFactura>) msg.obj;
					MostrarDetalle();
					break;
			
			}
		}
		return false;
	}

	
	
	
	private void ObtenerDetalle(){
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Obteniendo Información ...", true, false);
			
			Message msg = new Message(); 
			msg.what = Petition.OBTENER_DETALLE_FACTURA.getActionCode(); 
			msg.obj = factura_id;
			NMApp.getController().getInboxHandler().sendMessage(msg); 
		}
		catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void MostrarDetalle(){
	
		adapter = new GenericAdapter(getActivity(),DetallesFacturaHolder.class,listadetalle, R.layout.detalles_factura_item);  
		listviewdetalle.setAdapter(adapter);   
		
		
	}
	
	OnKeyListener keyListener = new OnKeyListener() 
	{ 
		  @Override
		  public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) 
		  {
			if (keyCode == KeyEvent.KEYCODE_BACK) 
			{        	
			  	dismiss();
			    return true;
			}		  
			return false;	 
		  } 
	}; 
	
}
