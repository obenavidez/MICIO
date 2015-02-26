package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.TasaCambio;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

@InvokeBridge(bridgeName = "BReciboM")
public class TasaCambioFragment extends DialogFragment implements Handler.Callback{

	private static TasaCambioFragment tasa ;
	private View view;
	private ViewRecibo parent;
	private Context context;
	// Singleton 
	public static TasaCambioFragment newInstance (){
		if(tasa == null) tasa = new TasaCambioFragment();	
		return tasa;
	}

	private void getData(){
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
		builder.setTitle("Tasa de Cambio del día");
		builder.setView(view);
		getData();
		return  builder.create();
	}

	@Override
	public boolean handleMessage(Message msg) {
		
		switch (msg.what) 
		{	
			case ControllerProtocol.GET_TASA_CAMBIO:
		
			List<TasaCambio> lista = msg.obj ==null ? new ArrayList<TasaCambio>() : ( ArrayList<TasaCambio>)msg.obj ; 
		 	if(lista.size()==0){
		 		AppDialog.showMessage(context,"","No se encuentra registrada tasa de cambio.",DialogType.DIALOGO_ALERTA);
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

}
