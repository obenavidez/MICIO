package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST; 
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SOLICITAR_DESCUENTO;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.auxiliar.ErrorMessage; 
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol; 
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.model.ModelSolicitudDescuento; 
import com.panzyma.nm.serviceproxy.EncabezadoSolicitud; 
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.viewdialog.DialogSolicitudDescuento;

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
				onSaveDataToLocalHost((EncabezadoSolicitud) msg.obj);
				return true;
			case LOAD_DATA_FROM_LOCALHOST: 
				onLoadALLDataFromLocalHost(bdl.getLong("idrecibo"));
				return true;
			case SOLICITAR_DESCUENTO:
				bdl=msg.getData();
				enviarSolicitudDescuento((EncabezadoSolicitud) msg.obj);
				return true;
		}				
		return false;
	}
	
	private void enviarSolicitudDescuento(final EncabezadoSolicitud solicitud) 
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
						int cont=0;
						StringBuilder nota=null;
						String credenciales="";
						
						credenciales = SessionManager.getCredentials(); 
						if(credenciales=="")
							return;
						 
						List<SolicitudDescuento> _detalles=solicitud.getDetalles();
						List<SolicitudDescuento> detalles=new ArrayList<SolicitudDescuento>();
						nota = new StringBuilder("Solicito aprobación para otorgar descuento a lo siguiente:\n"); 
						
						for(SolicitudDescuento sd:_detalles)
						{
							if(sd.getPorcentaje()>0.0 && !sd.getJustificacion().equals("")) 
							{   
								cont+=1;
								if(cont>1)
									nota.append("** Documento : Factura # ");
								else
									nota.append("   Documento : Factura # ");
								nota.append(sd.getFactura().getNoFactura());
								nota.append("\t / Justificación: ");
								nota.append(sd.getJustificacion()+"\n");								
								detalles.add(sd);
							}
							  
							
						} 
						solicitud.setDetalles(detalles);
						if(solicitud!=null && solicitud.getDetalles()!=null && solicitud.getDetalles().size()!=0)
							RegistrarSolicituDescuento(solicitud);
						else
							return;
						
						long rs = ModelRecibo.solicitarDescuentoOcacional(credenciales, solicitud.getRecibo(), nota.toString());
						if(rs!= 0)
						{
							solicitud.setCodigoEstado(DialogSolicitudDescuento.DOC_STATUS_ENVIADO);
							solicitud.setDescripcionEstado(DialogSolicitudDescuento.DESC_DOC_STATUS_ENVIADO);
							RegistrarSolicituDescuento(solicitud);
							
							Processor.notifyToView(
								getController(),
								ControllerProtocol.REQUEST_SOLICITUD_DESCUENTO,
								0,
								0,
								nota.toString()); 
						}
						
						
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
	
	private void onSaveDataToLocalHost(EncabezadoSolicitud solicitud) 
	{		
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.SEND_DATA_FROM_SERVER,
					0,
					0,
					ModelSolicitudDescuento.RegistrarSolicituDescuento(solicitud)
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
	
	private EncabezadoSolicitud RegistrarSolicituDescuento(EncabezadoSolicitud solicitud) 
	{		
		try 
		{
			return	ModelSolicitudDescuento.RegistrarSolicituDescuento(solicitud);
		} catch (Exception e) { 
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		} 
		
		return null;
	}
	
	
	private void onLoadALLDataFromLocalHost(long idrecibo) 
	{ 
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.C_DATA,
					0,
					0,
					ModelSolicitudDescuento.obtenerEncabezadoSolicitud(idrecibo)
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
