package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST; 

import java.util.ArrayList;

import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.model.ModelSolicitudDescuento;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public final  class BSolicitudDescuentoM  extends BBaseM {

	protected String TAG;
	private Bundle bdl;
	
	public BSolicitudDescuentoM() {}
	
	@Override
	public boolean handleMessage(Message msg) throws Exception 
	{ 
		bdl=msg.getData();
		switch (msg.what) 
		{
			case LOAD_DATA_FROM_LOCALHOST: 
				onLoadALLDataFromLocalHost(bdl.getLong("idrecibo"));
				return true;
		}				
		return false;
	}

	private void onLoadALLDataFromLocalHost(long idrecibo) 
	{ 
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.C_DATA,
					0,
					0,
					ModelSolicitudDescuento.obtenerSolicitudes(idrecibo)
					);
		} catch (Exception e) { 
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		} 
	}

}
