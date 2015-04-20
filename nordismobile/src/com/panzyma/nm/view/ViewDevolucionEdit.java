package com.panzyma.nm.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.interfaces.Editable;
import com.panzyma.nordismobile.R;

public class ViewDevolucionEdit extends ActionBarActivity implements
Handler.Callback, Editable
{

	private CheckBox ckboxnovencidodev;
	private Spinner cboxmotivodev;
	private Spinner cboxtramitedev;
	private Spinner cboxtipodev;
	private CheckBox ckboxncinmeditata;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devolucion_edit);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		com.panzyma.nm.NMApp.getController().setView(this);
		initComponent();
	}
	
	public void initComponent() 
	{
		ckboxnovencidodev=(CheckBox) findViewById(R.id.devchk_typodevolucion);
		ckboxncinmeditata=(CheckBox) findViewById(R.id.devchk_ncinmediata);
		cboxmotivodev=(Spinner) findViewById(R.id.devcombox_motivo);
		cboxtramitedev=(Spinner) findViewById(R.id.devcombox_tramite);
		cboxtipodev=(Spinner) findViewById(R.id.devcombox_tipo);
		
		
		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
				R.array.motivodevolucion, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cboxmotivodev.setAdapter(adapter2); 
		
		adapter2 = ArrayAdapter.createFromResource(this,
				R.array.tramite, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cboxtramitedev.setAdapter(adapter2);
		
		adapter2 = ArrayAdapter.createFromResource(this,
				R.array.tipodevolucion, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cboxtipodev.setAdapter(adapter2);
	}

	@Override
	public Object getBridge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
