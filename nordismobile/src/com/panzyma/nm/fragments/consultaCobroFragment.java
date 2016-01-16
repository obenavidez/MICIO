package com.panzyma.nm.fragments;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BCobro.Accion;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@InvokeBridge(bridgeName = "BCobroM")
public class consultaCobroFragment extends Fragment implements Handler.Callback {
	
	
	private ProgressDialog waiting;
	
	public enum ActionMenu {
		VENTAS_DIA, VENTAS_SEMANA, VENTAS_MES, IMPRIMIR 
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.cobro, container, false);
	}
	

	@Override
	public void onStart() {
		CargarVentas();
	}
	
	private void CargarVentas(){
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Obteniendo Información ...", true, false);
			NMApp.getController().getInboxHandler().sendEmptyMessage(Accion.VENTAS_DEL_DIA.getActionCode());
		}
		catch (Exception e) {
				e.printStackTrace();
		}
	}


	@Override
	public boolean handleMessage(Message msg) {
		Accion response = Accion.toInt(msg.what);
		switch (response) {
			case VENTAS_DEL_DIA:break;
		}
		return false;
	}
	
	

}
