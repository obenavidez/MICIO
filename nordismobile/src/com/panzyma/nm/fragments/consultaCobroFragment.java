package com.panzyma.nm.fragments;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BCobroM.Accion;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.serviceproxy.CCobro;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.CobroViewHolder;
import com.panzyma.nordismobile.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@InvokeBridge(bridgeName = "BCobroM")
public class consultaCobroFragment extends Fragment implements Handler.Callback {
	
	public String TAG=consultaCobroFragment.class.getSimpleName();
	private ProgressDialog waiting;
	private List<CCobro> cobros = new ArrayList<CCobro>();
	String titulo = "";
	private GenericAdapter adapter = null;
	private ListView listaGenerica;
	private TextView txtenty;
	private EditText txtFiltro;
	private TextView headerGrid;
	private TextView gridheader;
	
	public enum ActionMenu {
		COBROS_DIA, COBROS_SEMANA, COBROS_MES, IMPRIMIR 
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.cobro, container, false);
	}
	

	@Override
	public void onStart() {
		super.onStart();
		NMApp.controller.setView(this); 
		SessionManager.setContext(getActivity());
		initComponent();
		CargarCobros();
	}
	
	@Override
	public void onDetach ()
	{
		Log.d(TAG, "OnDetach");
		NMApp.getController().setView((Callback)getActivity()); 
		super.onDetach();
	}
	
	@Override
    public void onStop() {
        super.onStop(); 
		NMApp.getController().setView((Callback)getActivity());  
        Log.d(TAG, "onStop");
    }
	
	private void CargarCobros(){
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Obteniendo Información ...", true, false);
			
			Message msg = new Message(); 
			msg.what =Accion.COBROS_DEL_MES.getActionCode();
			NMApp.getController().getInboxHandler().sendMessage(msg); 
		}
		catch (Exception e) {
				e.printStackTrace();
		}
	}


	@Override
	public boolean handleMessage(Message msg) {
	
		if (waiting != null) waiting.dismiss();
		if( msg.what < 3) 
		{
			Accion response = Accion.toInt(msg.what);
			
			switch (response) {
				case COBROS_DEL_DIA: break;
				case COBROS_DEL_SEMANA: break;
				case COBROS_DEL_MES:
					System.out.print("YA LLego");
					cobros = (ArrayList<CCobro>)msg.obj;
					MostrarCobros();
					
					break;
			}
		}
		return false;
	}
	
	private void MostrarCobros(){
		
		if(cobros.size() > 0){
			
			adapter = new GenericAdapter<CCobro, CobroViewHolder>( this.getActivity().getApplicationContext(),CobroViewHolder.class,cobros,R.layout.detalle_cobro);
			listaGenerica.setAdapter(adapter);
			//adapter.notifyDataSetChanged();
			
		}
	}
	
	private void initComponent(){
		listaGenerica = (ListView) getActivity().findViewById(R.id.cobrosList);
		txtenty = (TextView) getActivity().findViewById(R.id.ctxtview_enty);
		headerGrid = (TextView) getActivity().findViewById(R.id.cxctextv_header2);
		//btnMenu = (Button)getActivity().findViewById(R.id.btnMenu);
		txtFiltro = (EditText) getActivity().findViewById(R.id.cxctextv_filtro);
		gridheader = (TextView)  getActivity().findViewById(R.id.ctextv_gridheader);
		//gridheader.setVisibility(View.INVISIBLE);
		
		gridheader.setText("VENTAS DEL DIA (0)");
	}

}
