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
	
	@SuppressWarnings("unchecked")
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
				if(msg!=null && msg.obj instanceof EncabezadoSolicitud)
					enviarSolicitudDescuento((EncabezadoSolicitud) msg.obj);
				else if(msg!=null)
				{
					ArrayList<Object> data=(ArrayList<Object>) msg.obj;					
					enviarSolicitudDescuento((EncabezadoSolicitud)data.get(2),new String[]{String.valueOf(data.get(0)),(String) data.get(1)});
				}
					
				return true;
		}				
		return false;
	}
	
	private void enviarSolicitudDescuento(final EncabezadoSolicitud solicitud,final String...extra) 
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
						nota = new StringBuilder("** Solicito aprobación para otorgar descuento a lo siguiente:\n"); 
						 
						if(extra!=null && extra.length>0)
						{  
							nota = new StringBuilder(""); 
							for(SolicitudDescuento sd:_detalles)
							{
								cont+=1;
								if(cont==1)
									nota.append("** Solicito aprobación para otorgar el %"+extra[0]+ " a las siguientes facturas:\n");
															
								nota.append(sd.getFactura().getNoFactura()+",");
								sd.setPorcentaje(Float.valueOf(extra[0]));
								sd.setJustificacion(extra[1]);
								detalles.add(sd);
								if(cont==_detalles.size())
									nota.append("\nJustificación: "+extra[1]); 
								
							}							
						}
						else
						{
							for(SolicitudDescuento sd:_detalles)
							{ 
								if(sd.getPorcentaje()>0.0 && !sd.getJustificacion().equals("")) 
								{    												
									nota.append("Factura: # "+sd.getFactura().getNoFactura()+",Descuento : %"+sd.getPorcentaje()+",  Justificación: "+sd.getJustificacion()+"\n");									  							
									detalles.add(sd);
								} 							
							}
						}
						 
						solicitud.setDetalles(detalles);
						if(solicitud!=null && solicitud.getDetalles()!=null && solicitud.getDetalles().size()!=0)
							RegistrarSolicituDescuento(solicitud);
						else
						{//return;
							Processor.notifyToView(
									getController(),
									ControllerProtocol.C_QUIT,
									0,
									0,
									null);
							return;
						}
						
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
									new ErrorMessage("Error interno en el registro de recibo",e.toString(), ""));
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
