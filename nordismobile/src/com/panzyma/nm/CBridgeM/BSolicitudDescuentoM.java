package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST; 
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SOLICITAR_DESCUENTO;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.model.ModelSolicitudDescuento;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public  class BSolicitudDescuentoM  extends BBaseM {

	protected String TAG;
	private Bundle bdl;
	
	public BSolicitudDescuentoM() {}
	
	@Override
	public boolean handleMessage(Message msg) throws Exception 
	{ 
		bdl=msg.getData();
		switch (msg.what) 
		{
			case SAVE_DATA_FROM_LOCALHOST:
				onSaveDataToLocalHost((List<SolicitudDescuento>) msg.obj);
				return true;
			case LOAD_DATA_FROM_LOCALHOST: 
				onLoadALLDataFromLocalHost(bdl.getLong("idrecibo"));
				return true;
			case ControllerProtocol.SEND_DATA_FROM_SERVER: 
				return true;
			case SOLICITAR_DESCUENTO:
				bdl=msg.getData();
				enviarSolicitudDescuento((List<SolicitudDescuento>) msg.obj);
				return true;
		}				
		return false;
	}
	
	private void enviarSolicitudDescuento(final List<SolicitudDescuento> solicitudes) 
	{
		try 
		{
			getPool().execute(new Runnable() 
			{
				@Override
				public void run() 
				{
					
					try 
					{   
						String credenciales="";
						credenciales = SessionManager.getCredentials(); 
						if(credenciales=="")
							return;
						
						for(SolicitudDescuento sd:solicitudes)
						{
							String textenviar="Solicito aprobación para otorgar descuento para las siguientes facturas";
							if(sd.getPorcentaje()>0.0 && !sd.getJustificacion().equals("")) 
							{
								
							}
							else
								continue; 
							
						}
						ModelSolicitudDescuento.RegistrarSolicitudes()
						
					} catch (Exception e)
					{ 
						
						try 
						{
							Processor.notifyToView(
									getController(),
									ERROR,
									0,
									0,
									new ErrorMessage("Error interno en el registro de recibo",e.toString(), "\n Causa: "
													+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}				
					
				}
			});
		}catch(Exception e){ 
				e.printStackTrace(); 
		}		
		 
	}	
	
	private void onSendDataToServer(List<SolicitudDescuento> solicitudes)
	{
		
	}

	private void onSaveDataToLocalHost(List<SolicitudDescuento> solicitudes) 
	{		
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.SEND_DATA_FROM_SERVER,
					0,
					0,
					ModelSolicitudDescuento.RegistrarSolicitudes(solicitudes)
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
