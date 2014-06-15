package com.panzyma.nm.fragments;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BLogicM;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nm.viewmodel.vmFicha;
import com.panzyma.nm.viewmodel.vmRecibo;
import com.panzyma.nordismobile.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CuentasPorCobrarFragment extends Fragment implements
		Handler.Callback {

	public final static String ARG_POSITION = "position";
	public final static String OBJECT = "cliente";
	int mCurrentPosition = -1;
	private Cliente cliente = null;
	private Context fcontext = null;
	private GenericAdapter adapter = null;
	private NMApp nmapp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			cliente = (Cliente) savedInstanceState.getParcelable(OBJECT);
		}
		return inflater.inflate(R.layout.ficha_cliente, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		Bundle args = getArguments();
		if (args != null) {
			cliente = (Cliente) args.getParcelable(OBJECT);
			mCurrentPosition = args.getInt(ARG_POSITION);
			cargarEncabezadoCliente(cliente, mCurrentPosition);
		} else if (mCurrentPosition != -1) {
			cargarEncabezadoCliente(cliente, mCurrentPosition);
		}
	}

	private void cargarEncabezadoCliente(Cliente cliente2, int mCurrentPosition2) {
		try {
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(BLogicM.class.toString());
			nmapp.getController().setEntities(this, new BLogicM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(ControllerProtocol.LOAD_DATA_FROM_SERVER);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getSucursalId() {
		return cliente.getIdSucursal();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what){
		case ControllerProtocol.C_DATA:
			
			break;
		}
		return false;
	}

}
