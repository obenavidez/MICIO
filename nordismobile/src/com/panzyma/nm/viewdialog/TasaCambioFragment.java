package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.auxiliar.AppDialog.OnButtonClickListener;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.TasaCambio;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.TasaCambioViewHolder;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@InvokeBridge(bridgeName = "BReciboM")
public class TasaCambioFragment extends DialogFragment implements Handler.Callback{

	private static TasaCambioFragment tasa ;
	private View view;
	private ViewRecibo parent;
	private Context context;
	ProgressDialog pd;
	private ListView lvtasas;
	private GenericAdapter adapter;
	static AlertDialog dialog =null;
	private int positioncache;
	
	// Singleton 
	public static TasaCambioFragment newInstance (){
		if(tasa == null) tasa = new TasaCambioFragment();	
		return tasa;
	}

	private void get_Tasa_Cambios(){
		NMApp.getController().getInboxHandler().sendEmptyMessage(ControllerProtocol.GET_TASA_CAMBIO);
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
		
		parent=(ViewRecibo) getActivity();
		NMApp.getController().setView(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
		LayoutInflater inflater = parent.getLayoutInflater();
		view = inflater.inflate(R.layout.layout_tasa_cambio, null);
		lvtasas=(ListView) view.findViewById(R.id.bnflv_tasacambio);
		//builder.setTitle("Tasa de Cambio del día");
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(pd!=null)
					pd.dismiss();
				dismiss();
			}
		});
		builder.setOnKeyListener(keyListener);
		get_Tasa_Cambios();
		dialog = builder.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@Override
	public boolean handleMessage(Message msg) {
		
		switch (msg.what) 
		{	
			case ControllerProtocol.GET_TASA_CAMBIO:
		
			List<TasaCambio> lista = msg.obj ==null ? new ArrayList<TasaCambio>() : ( ArrayList<TasaCambio>)msg.obj ; 
		 	if(lista.size()==0){
		 		AppDialog.showMessage(context,"","No hay tasas de cambio registradas.",DialogType.DIALOGO_ALERTA,new OnButtonClickListener() {
					@Override
					public void onButtonClick(AlertDialog alert, int actionId) {
						dialog.dismiss();
						alert.dismiss();
					}
				});
		 	}
		 	else
		 	{
		 		adapter = new GenericAdapter(getActivity(),TasaCambioViewHolder.class,lista, R.layout.detalle_tasa_cambio);  
		 		lvtasas.setAdapter(adapter);   
		 		lvtasas.setOnItemClickListener(new OnItemClickListener() {
					@Override
					 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if((parent.getChildAt(positioncache))!=null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position;				            	 			
		            	adapter.setSelectedPosition(position); 
		            	view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_item_selected));	
					}
		 			
		        });
		 	}
		 	break;
		}
		return false;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) { 
	    NMApp.getController().setView(parent);
		super.onDismiss(dialog);
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
