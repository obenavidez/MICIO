package com.panzyma.nm.fragments;

import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@InvokeBridge(bridgeName = "BCobroM")
public class consultaCobroFragment extends Fragment{
	
	public enum ActionMenu {
		VENTAS_DIA, VENTAS_SEMANA, VENTAS_MES, IMPRIMIR 
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.cobro, container, false);
	}
	
	
	
	
	

}
