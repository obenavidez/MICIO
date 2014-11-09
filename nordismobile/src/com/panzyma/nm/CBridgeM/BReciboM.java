package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.C_FACTURACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SEND_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.SOLICITAR_DESCUENTO;
import static com.panzyma.nm.controller.ControllerProtocol.APLICAR_DESCUENTO; 
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.Enumeration;
import java.util.Hashtable;
import org.json.JSONArray; 
import android.annotation.SuppressLint;
import android.app.AlertDialog; 
import android.os.Bundle;
import android.os.Parcelable; 
import android.os.Message;
import android.util.Log;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.NumberUtil; 
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.bluetooth.BluetoothConnection;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.serviceproxy.RespuestaEnviarRecibo;
import com.panzyma.nm.viewmodel.vmRecibo;

@SuppressWarnings("rawtypes")
public final class BReciboM extends BBaseM {

	private static final String logger = BReciboM.class.getSimpleName();

	private Object lock=new Object();
	boolean OK = false;
	ArrayList<Producto> obj = new ArrayList<Producto>();
	JSONArray jsonA = new JSONArray();
	static boolean imprimir = false;
    static boolean pagarOnLine = false;
    Bundle bdl=null;

	protected String TAG;
	
	public BReciboM() {}
	
	@Override
	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) 
		{
			case SAVE_DATA_FROM_LOCALHOST:
			    bdl=msg.getData();
				Parcelable [] arrayParcelable = bdl.getParcelableArray("facturasToUpdate");			
				ArrayList<Factura> facturasToUpdate = new ArrayList<Factura>();				
				Object [] list = Arrays.copyOf(arrayParcelable, arrayParcelable.length , Factura[].class);
				for(Object obj: list){
					facturasToUpdate.add((Factura)obj);
				}	
				arrayParcelable = bdl.getParcelableArray("notasDebitoToUpdate");	
				ArrayList<CCNotaDebito> notasDebitoToUpdate = new ArrayList<CCNotaDebito>();
				list = Arrays.copyOf(arrayParcelable, arrayParcelable.length , CCNotaDebito[].class);
				for(Object obj: list){
					notasDebitoToUpdate.add((CCNotaDebito)obj);
				}
				arrayParcelable = bdl.getParcelableArray("notasCreditoToUpdate");	
				ArrayList<CCNotaCredito> notasCreditoToUpdate = new ArrayList<CCNotaCredito>();
				list = Arrays.copyOf(arrayParcelable, arrayParcelable.length , CCNotaCredito[].class);
				for(Object obj: list){
					notasCreditoToUpdate.add((CCNotaCredito)obj);
				}
				onSaveDataToLocalHost((ReciboColector)bdl.getParcelable("recibo"), facturasToUpdate, notasDebitoToUpdate, notasCreditoToUpdate);
				break;
			case LOAD_DATA_FROM_LOCALHOST:
				onLoadALLDataFromLocalHost();
				return true;
			case LOAD_ITEM_FROM_LOCALHOST: 
				bdl=msg.getData();
				onLoadItemFromLocalHost(bdl.getInt("idrecibo"));
				return true;
			case DELETE_DATA_FROM_LOCALHOST:
				bdl=msg.getData();			
				onDeleteDataFromLocalHost(bdl.getInt("idrecibo"));
				break;
			case C_FACTURACLIENTE:
				bdl = msg.getData();				
				onLoadDocumentosClienteFromLocalhost(bdl.getLong("sucursalID"));
				break; 
			case LOAD_DATA_FROM_SERVER:
				// onLoadALLData_From_LocalHost();
				return true;
			case UPDATE_ITEM_FROM_SERVER:
				// onUpdateItem_From_Server();
				return true;
			case SOLICITAR_DESCUENTO:
				bdl=msg.getData();
				solicitarDescuentoOcacional((ReciboColector)bdl.getParcelable("recibo"),bdl.getString("notas"));
				return true;
				
			case APLICAR_DESCUENTO:
				bdl=msg.getData();
				aplicarDescuentoOcacional((ReciboColector)bdl.getParcelable("recibo"));
				return true;
			case SEND_DATA_FROM_SERVER:  
				bdl=msg.getData();
				Parcelable [] arrayParcelable2 = bdl.getParcelableArray("facturasToUpdate");			
				ArrayList<Factura> facturasToUpdate2 = new ArrayList<Factura>();
				Object [] list2 = Arrays.copyOf(arrayParcelable2, arrayParcelable2.length , Factura[].class);
				for(Object obj: list2){
					facturasToUpdate2.add((Factura)obj);
				}	
				arrayParcelable = bdl.getParcelableArray("notasDebitoToUpdate");	
				ArrayList<CCNotaDebito> notasDebitoToUpdate2 = new ArrayList<CCNotaDebito>();
				list = Arrays.copyOf(arrayParcelable, arrayParcelable.length , CCNotaDebito[].class);
				for(Object obj: list){
					notasDebitoToUpdate2.add((CCNotaDebito)obj);
				}
				arrayParcelable = bdl.getParcelableArray("notasCreditoToUpdate");	
				ArrayList<CCNotaCredito> notasCreditoToUpdate2 = new ArrayList<CCNotaCredito>();
				list = Arrays.copyOf(arrayParcelable, arrayParcelable.length , CCNotaCredito[].class);
				for(Object obj: list){
					notasCreditoToUpdate2.add((CCNotaCredito)obj);
				}
				enviarRecibo((ReciboColector)bdl.getParcelable("recibo"), facturasToUpdate2, notasDebitoToUpdate2, notasCreditoToUpdate2 );
				break; 
			case ControllerProtocol.IMPRIMIR:
				bdl=msg.getData();
				ImprimirReciboColector((ReciboColector)bdl.getParcelable("recibo"),false); 
				break;
		}
		
		return false;
	}
	
	private void aplicarDescuentoOcacional(final ReciboColector recibo)
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
						String credenciales = "";
						credenciales = SessionManager.getCredentials(); 					
						if(credenciales != "") 
							Processor.notifyToView(getController(),
									ControllerProtocol.REQUEST_APLICAR_DESCUENTO,
									0,
									0,
									ModelRecibo.aplicarDescuentoOcacional(credenciales, recibo)
									);
							 
						
					} catch (Exception e) { 
						Log.e(TAG, "Error en aplicar descuento ocasional", e);
						try {
							Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno en la sincronización con la BDD",e.toString(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) { 
							e1.printStackTrace();
						}
					}
				}
			});
		} catch (InterruptedException e) 
		{ 
			e.printStackTrace();
		}
		
	}
	
	private void solicitarDescuentoOcacional(final ReciboColector recibo, final String notas) 
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
						if(credenciales!="")
						{
							long rs=0;
							if(ModelConfiguracion.yaseEnvioSolicitud(getContext(), recibo.getReferencia()))
							{
								Processor.notifyToView(
										getController(),
										ControllerProtocol.NOTIFICATION,
										0,
										0,
										NotificationMessage.newInstance("","Solicitud descuento ya fue enviada con anterioridad.",""));
									ModelConfiguracion.guardarSolicitudDescuentoRec(getContext(),
											recibo.getReferencia(),
											notas);
								return;
							}
							rs = ModelRecibo.solicitarDescuentoOcacional(credenciales, recibo, notas);
							if(rs!= 0)
							{
								Processor.notifyToView(
									getController(),
									ControllerProtocol.NOTIFICATION,
									0,
									0,
									NotificationMessage.newInstance("","La solicitud descuento fue enviada a la central con exito",""));
								ModelConfiguracion.guardarSolicitudDescuentoRec(getContext(),
										recibo.getReferencia(), 
										notas);
							}
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

	private void saveRecibo( ReciboColector recibo,ArrayList<Factura> facturasToUpdate, ArrayList<CCNotaDebito> notasDebitoToUpdate, ArrayList<CCNotaCredito> notasCreditoToUpdate) throws Exception
	{ 	
		//Guardando localmente el recibo
		DatabaseProvider.registrarRecibo(recibo, getContext(), facturasToUpdate, notasDebitoToUpdate, notasCreditoToUpdate);
	}

	private void onSaveDataToLocalHost(final ReciboColector recibo, final ArrayList<Factura> facturasToUpdate, final ArrayList<CCNotaDebito> notasDebitoToUpdate, final ArrayList<CCNotaCredito> notasCreditoToUpdate) 
	{
		try 
		{
			getPool().execute(new Runnable() {
				@Override
				public void run() {
					
					try 
					{ 
						saveRecibo(recibo, facturasToUpdate, notasDebitoToUpdate, notasCreditoToUpdate);	 
						Processor.notifyToView(
											getController(),
											ControllerProtocol.NOTIFICATION,
											ControllerProtocol.SAVE_DATA_FROM_LOCALHOST,
											0,
											recibo
											);
					} catch (Exception e) 
					{ 
						Log.e(TAG, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											getController(),
											ERROR,
											0,
											0,
											ErrorMessage.newInstance(
													"Error interno",
													e.getMessage(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}				
					
				}
			});
		}catch(Exception e){
			Log.e(logger, "Error in the update thread", e);
			try {
				Processor
						.notifyToView(
								getController(),
								ERROR,
								0,
								0,
								new ErrorMessage(
										"Error interno en el registro de recibo",
										e.toString(), "\n Causa: "
												+ e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}		
	}

	private void onLoadItemFromLocalHost(final Integer idrecibo) {
		try {
			getPool().execute(new Runnable() {
				@Override
				public void run() {
					Processor.send_ViewReciboEditToView(ModelRecibo.getReciboByID(getResolver(),idrecibo), getController());
				}
			});
		}catch(Exception e){
			Log.e(TAG, "Error in the update thread", e);
			try {
				Processor
						.notifyToView(
								getController(),
								ERROR,
								0,
								0,
								ErrorMessage.newInstance(
										"Error interno",
										e.getMessage(), "\n Causa: "
												+ e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}

	private void onDeleteDataFromLocalHost(final int reciboID) {
		try {
			getPool().execute(new Runnable() {

				@Override
				public void run() {

					try 
					{						
						int idrecibo = reciboID;
						ModelRecibo.borraReciboByID(getResolver(), idrecibo, getContext());
						//Obtener el reciboid max generado
						int maxid = ModelConfiguracion.getMaxReciboID(getContext());
						//Obteniendo el IDRecibo
						Integer idrec =idrecibo-Integer.parseInt(NumberUtil.setFormatPrefijo(ModelConfiguracion.getDeviceID(getContext()), idrecibo));
						//Actualizando el id máximo de recibo generado
						ModelConfiguracion.setMaxReciboId(getContext(), (maxid<idrec)?idrec:maxid);
						Processor.send_ViewDeleteReciboToView(1, getController());
						
					} catch (Exception e) 
					{
						Log.e(logger, "Error in the update thread", e);
						try 
						{
							Processor.notifyToView(
											getController(),
											ERROR,
											0,
											0,
											new ErrorMessage(
													"Error interno en la sincronización con la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		} catch (Exception e) {			
		}
		
	}

	private void onLoadALLDataFromLocalHost() {
		try {
			getPool().execute(new Runnable() {

				@Override
				public void run() 
				{
					try 
					{						
						ArrayList<vmRecibo> vmr = ModelRecibo.getArrayCustomerFromLocalHost(getResolver());
						Processor.send_ViewReciboToView( vmr, getController());
					} catch (Exception e) 
					{
						Log.e(logger, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											getController(),
											ERROR,
											0,
											0,
											new ErrorMessage(
													"Error interno en la sincronización con la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void onLoadDocumentosClienteFromLocalhost(final Long sucursalID) {
		try {
			this.getPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						// Parameters params = viewcc.get_FacturaParameters();
						Processor.send_ViewReciboEditToView(
								ModelCliente.getClienteBySucursalID(
										getResolver(),
										sucursalID,
										0), getController());

					} catch (Exception e) {
						e.printStackTrace();
						try {
							Processor
									.notifyToView(
											getController(),
											ERROR,
											0,
											0,
											new ErrorMessage(
													"Error en la sincronización con el servidor",
													e.toString(), "\nCausa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void enviarRecibo(final ReciboColector recibo, final ArrayList<Factura> facturasToUpdate, final ArrayList<CCNotaDebito> notasDebitoToUpdate, final ArrayList<CCNotaCredito> notasCreditoToUpdate)
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
						credenciales=SessionManager.getCredentials();						
						if(credenciales!="")
						{							 
					        imprimir = true;
					        pagarOnLine = true;        
					        //Si se está fuera de covertura, salir        
					        if (recibo.getCodEstado().compareTo("PAGADO_OFFLINE") == 0) 
					        {
					        	imprimir = false;
					            if (!SessionManager.isPhoneConnected()) 
					            {
					            	Processor.notifyToView(getController(),ERROR,0,0,
											new ErrorMessage(
													          "Error en el Modulo Recibo.",
													          "Error en el proceso de envio del recibo", "\nCausa: "
															  + "Falta de covertura."
															 )
									      );  
					                return;
					            }
					            
					        } else 
					        {                
					            if (!SessionManager.isPhoneConnected()) 
					            {	
					            	pagarOnLine = false;
					            	Processor.notifyToView(getController(),ERROR,0,0,
											new ErrorMessage(
													          "Error en el Modulo Recibo.",
													          "Error en el proceso de envio del recibo", "\nCausa: "
															  + "El recibo no será enviado por falta de cobertura\r\nEl recibo será impreso y quedará pendiente de enviarse."
															 )
									      );   
					            
					            }
					        } 
					       
							//Guardando cambios en el Dispositivo 
					        saveRecibo(recibo,facturasToUpdate, notasDebitoToUpdate, notasCreditoToUpdate);  
					        if (pagarOnLine) 
							{
								
								 Processor.notifyToView(getController(),ControllerProtocol.NOTIFICATION_DIALOG2,
									0,0,"Enviando recibo al servidor central");
								RespuestaEnviarRecibo rs=ModelRecibo.enviarRecibo(credenciales,recibo);
								if (rs == null) 
								{
									Processor.notifyToView(getController(),ERROR,0,0,
											new ErrorMessage(
													          "Error en el Modulo Recibo.",
													          "Error en el proceso de envio del recibo", "\nCausa: "
															  + "No se conocen las causas."
															 )
									      ); 
								}
								else if (rs.getNuevoEstado().getCodigo().compareTo("RECHAZADO_FECHA") == 0) 
				                {
								
									Processor.notifyToView(getController(),ERROR,0,0,
											new ErrorMessage(
													          "Error en el Modulo Recibo.",
													          "El recibo fue rechazado por el servidor", "\nCausa: "
															  + "Fecha inválida."
															 )
									      ); 
				                }
								//Actualizar información del nuevo estado del recibo
				                recibo.setObjEstadoID(rs.getNuevoEstado().getId());
				                recibo.setCodEstado(rs.getNuevoEstado().getCodigo());
				                recibo.setDescEstado(rs.getNuevoEstado().getDescripcion());
				                recibo.setNumero(rs.getNumeroCentral()); 
				                
				                //Guardando cambios en el Dispositivo 
				                saveRecibo(recibo,facturasToUpdate, notasDebitoToUpdate, notasCreditoToUpdate); 
				                
				                Processor.notifyToView(getController(),ControllerProtocol.NOTIFICATION_DIALOG2,
										0,0,"Actualindo estado de cuenta del cliente");
				                //Trayendo información del Cliente actualizada desde el servidor y guadarla localmente automaticamente 
								Cliente cliente=BClienteM.actualizarCliente(getContext(), SessionManager.getCredenciales(),recibo.getObjSucursalID());
								//actualizando el cliente en el hilo principal
								recibo.setCliente(cliente);								
								
				                //Salvar los cambios en el hilo pricipal
				                Processor.notifyToView(getController(),ControllerProtocol.ID_REQUEST_ENVIARPEDIDO,imprimir?1:0,0,recibo);
							}
							else
							{								 
				               //Poner estado de recibo en PAGADO_OFFLINE                   
								recibo.setCodEstado("PAGADO_OFFLINE");
								recibo.setDescEstado("Registrado"); 	
				               //Guardando cambios en el Dispositivo 
				                saveRecibo(recibo,facturasToUpdate, notasDebitoToUpdate, notasCreditoToUpdate); 
				                //enviar los cambios en el hilo pricipal
				                Processor.notifyToView(getController(),ControllerProtocol.ID_REQUEST_ENVIARPEDIDO,imprimir?1:0,0,recibo);
							}
							ModelConfiguracion.borrarEnvioSolicitud(getContext(),recibo.getReferencia());
							
						} 
						else
							Processor.notifyToView(getController(),0,0,0,null);
					} catch (Exception e) 
					{ 
						
						try 
						{
							Processor.notifyToView(getController(),ERROR,0,0,
													new ErrorMessage(
															          "Error en el proceso de enviar el recibo al servidor",
															          e.toString(), "\nCausa: "
																	  + e.getCause()
																	 )
											      );
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					}
				}
			});
			 
			
		} catch (Exception e) 
		{ 
		} 
	}
    
	@SuppressWarnings("unused")
	private void enviarImprimirRecibo(final ReciboColector recibo)
	{		
		//reciboEdit.runOnUiThread(new Runnable()
		try {
			getPool().execute(new Runnable()
			{
				@Override
				public void run() 
				{ 
					 AppDialog.showMessage(getContext(),"Confirme por favor.!!!","Desea Imprimir el Recibo?",AppDialog.DialogType.DIALOGO_CONFIRMACION,new AppDialog.OnButtonClickListener() 
					 {						 
							@Override
			    			public void onButtonClick(AlertDialog _dialog, int actionId) 
			    			{
			    				if(actionId == AppDialog.OK_BUTTOM) 
			    				{		    					
			    					try 
			    					{  		    						
										String credenciales="";
										credenciales=SessionManager.getCredentials(); 
										if(credenciales!="")
										{ 
											if(recibo.getReferencia()==0)							
								                onSaveDataToLocalHost(recibo, null, null, null);//GUARDANDO LOCALMENTE EL RECIBO QUE SE VA ENVIAR AL SERVIDOR CENTRAL
											ImprimirReciboColector(recibo, false);
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			    				}	
			    				synchronized(lock)
			    				{
			    					lock.notify();
			    				}
			    			}
					  }); 
			      }
			});
		} catch (InterruptedException e1) {			
			e1.printStackTrace();
		}
		
        synchronized(lock)
        {
            try {
            	lock.wait();
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
        }
		
	}
	@SuppressLint("UseValueOf")
	@SuppressWarnings({ "unchecked", "unused" })
	public  void ImprimirReciboColector(ReciboColector rcol,boolean reimpresion) 
	{   
		try
		{
	        String recibo = "";  
	        String monedaNac = Cobro.getMoneda(getContext());
	          
	        //Encabezado del recibo    
	        recibo += "T 7 0 120 2 Distribuidora Panzyma - DISPAN\r\n";        
	        recibo += "T 7 0 170 33 Recibo de Colector\r\n";
	        recibo += "LINE 0 70 575 70 1\r\n";
	        
	        //Fecha y número
	        recibo += "T 7 0 0 80 Fecha:\r\n";
	        recibo += "T 7 0 155 80 " + DateUtil.idateToStr(rcol.getFecha()) + "\r\n";
	        recibo += "T 7 0 0 110 Recibo:\r\n";
	        recibo += "T 7 0 155 110 " + Cobro.getNumeroRecibo(getContext(),rcol.getReferencia()) + "\r\n"; 
	        
	        //Monto total del recibo
	        recibo += "RIGHT 575\r\n"; 
	        recibo += "T 7 0 0 110 Monto: C$ " + StringUtil.formatReal(rcol.getTotalRecibo()) + "\r\n"; 
	        recibo += "LEFT\r\n";
	        
	        //Nombre del cliente
	        String nombreCliente = rcol.getCliente().getNombreLegalCliente();
	        String linea1 = nombreCliente;
	        String linea2 = "";
	        if (nombreCliente.length() > 35) {
	            int idxLastSpace = nombreCliente.substring(0, 35).lastIndexOf(' ');
	            linea1 = nombreCliente.substring(0, idxLastSpace);
	            linea2 = nombreCliente.substring(idxLastSpace, nombreCliente.length());
	        }
	        
	        int y = 140;
	        recibo += "T 7 0 0 " + y + " Recibimos de:\r\n";        
	        recibo += "T 7 0 155 " + y + " " + linea1.trim() + "\r\n";
	        if (linea2.length() > 0) {
	            y += 20;
	            recibo += "T 7 0 155 " + y + " " + linea2.trim() + "\r\n";
	        }
	        
	        //Monto en letras        
	        String montoLetras = NumberUtil.convertNumberToLetter(rcol.getTotalRecibo());
	        linea1 = montoLetras;
	        linea2 = "";
	        String linea3 = "";
	        if (montoLetras.length() > 35) {
	            int idxLastSpace = montoLetras.substring(0, 35).lastIndexOf(' ');
	            linea1 = montoLetras.substring(0, idxLastSpace);
	            linea2 = montoLetras.substring(idxLastSpace, montoLetras.length());
	            montoLetras = linea2;           
	            if (montoLetras.length() > 35) {
	                idxLastSpace = montoLetras.substring(0, 35).lastIndexOf(' ');
	                linea2 = montoLetras.substring(0, idxLastSpace);
	                linea3 = montoLetras.substring(idxLastSpace, montoLetras.length());
	            }
	        }       
	         
	        y += 30;
	        recibo += "T 7 0 0 " + y + " El monto de:\r\n";
	        recibo += "T 7 0 155 " + y + " " + linea1.trim() + "\r\n";      
	        if (linea2.length() > 0) {
	            y += 20;
	            recibo += "T 7 0 155 " + y + " " + linea2.trim() + "\r\n";
	        }
	        if (linea3.length() > 0) {
	            y += 20;
	            recibo += "T 7 0 155 " + y + " " + linea3.trim() + "\r\n";
	        }
	        
	        //Concepto
	        y += 30;
	        recibo += "T 7 0 0 " + y + " En concepto:\r\n";
	        recibo += "T 7 0 155 " + y + " Abono/cancelación de facturas y/o\r\n";
	        y += 20;
	        recibo += "T 7 0 155 " + y + " notas de débito que se detallan.\r\n";
	        
	        //Encabezados de detalle de documentos que se están pagando
	        y += 45;
	        recibo += "T 7 0 0 " + y + " Doc\r\n";
	        recibo += "T 7 0 50 " + y + " Numero\r\n";
	        recibo += "T 7 0 170 " + y + " A/C\r\n";        
	        recibo += "RIGHT 360\r\n"; 
	        recibo += "T 7 0 0 " + y + " Monto\r\n";
	        recibo += "RIGHT 480\r\n"; 
	        recibo += "T 7 0 0 " + y + " Desc.\r\n";
	        recibo += "RIGHT 575\r\n"; 
	        recibo += "T 7 0 0 " + y + " Reten.\r\n";
	        recibo += "LEFT\r\n";
	        y += 30;
	        recibo += "LINE 0 " + y + " 575 " + y + " 1\r\n";
	        y += 10;
	        
	        
	        ArrayList<String> stock_list = new ArrayList<String>();
	        stock_list.add("stock1");
	        stock_list.add("stock2");
	        String[] stockArr = new String[stock_list.size()];
	        stockArr = stock_list.toArray(stockArr);
	        
	        float totalPago = 0, totalDesc = 0, totalRet = 0, totalOtros = 0;
	        boolean hayDocAbonados = false;
	        //Imprimiendo detalle de facturas que se están pagando
	        if ((rcol.getFacturasRecibo().size()!=0)) 
	        {
	        	
	        	
	            ReciboDetFactura[] ff =new ReciboDetFactura[rcol.getFacturasRecibo().size()];
	            ff=rcol.getFacturasRecibo().toArray(ff); 
	            for(int i=0; i<ff.length; i++) {
	                ReciboDetFactura f = ff[i];
	                hayDocAbonados = hayDocAbonados || f.isEsAbono();
	                
	                if (i > 0) y += 30;
	                
	                //Tipo doc, número y tipo pago
	                recibo += "T 7 0 0 " + y + " FA\r\n";
	                recibo += "T 7 0 50 " + y + " " + f.getNumero() + "\r\n";
	                recibo += "T 7 0 180 " + y + " " + (f.isEsAbono() ? "A" : "C") + "\r\n";
	                
	                //Monto
	                recibo += "RIGHT 360\r\n"; 
	                recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(f.getMonto()) + "\r\n";
	                totalPago += f.getMonto();
	                
	                //Descuento
	                float desc = StringUtil.round(f.getMontoDescEspecifico() + f.getMontoDescOcasional() + f.getMontoDescPromocion(), 2);
	                recibo += "RIGHT 480\r\n"; 
	                recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(desc) + "\r\n";
	                totalDesc += desc;
	                
	                //Retención
	                float ret = StringUtil.round(f.getMontoRetencion() + f.getMontoOtrasDeducciones(), 2);
	                recibo += "RIGHT 575\r\n"; 
	                recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(ret) + "\r\n";
	                recibo += "LEFT\r\n";
	                totalRet += ret;
	            }
	        }
	        
	        //Imprimiendo detalle de notas de débito que se están pagando
	        if (rcol.getNotasDebitoRecibo().size() != 0) 
	        {
	        	 ReciboDetND[] dd  =new ReciboDetND[rcol.getNotasDebitoRecibo().size()];
	             dd=rcol.getNotasDebitoRecibo().toArray(dd); 
	        	 
	            for(int i=0; i<dd.length; i++) {
	                ReciboDetND d = dd[i];     
	                hayDocAbonados = hayDocAbonados || d.isEsAbono();
	                           
	                y += 30;
	                
	                //Tipo doc, número y tipo pago
	                recibo += "T 7 0 0 " + y + " ND\r\n";
	                recibo += "T 7 0 50 " + y + " " + d.getNumero() + "\r\n";
	                recibo += "T 7 0 180 " + y + " " + (d.isEsAbono() ? "A" : "C") + "\r\n";
	                
	                //Monto
	                recibo += "RIGHT 360\r\n"; 
	                recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(d.getMontoPagar()) + "\r\n";
	                totalPago += d.getMontoPagar();
	            }
	        }
	        
	        //Imprimiendo totales de pagos
	        y += 30;
	        recibo += "LINE 0 " + y + " 575 " + y + " 1\r\n";
	        y += 10;
	        
	        //Monto Total
	        recibo += "RIGHT 360\r\n"; 
	        recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(StringUtil.round(totalPago, 2)) + "\r\n";
	                
	        //Descuento Total        
	        recibo += "RIGHT 480\r\n"; 
	        recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(StringUtil.round(totalDesc, 2)) + "\r\n";
	                
	        //Retención Total        
	        recibo += "RIGHT 575\r\n"; 
	        recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(StringUtil.round(totalRet, 2)) + "\r\n";
	        recibo += "LEFT\r\n";
	       
	        //Imprimiendo encabezado de detalle de formas de pago
	        y += 55;        
	        recibo += "T 7 0 0 " + y + " F/Pago\r\n";
	        recibo += "T 7 0 80 " + y + " Banco\r\n";
	        recibo += "RIGHT 340\r\n";
	        recibo += "T 7 0 0 " + y + " Monto\r\n";
	        recibo += "LEFT\r\n";
	        recibo += "T 7 0 360 " + y + " Fecha\r\n";
	        recibo += "T 7 0 470 " + y + " Numero\r\n";
	        y += 30;
	        recibo += "LINE 0 " + y + " 575 " + y + " 1\r\n";
	        y += 10;
	        
	        //Imprimiendo detalle de otras formas de pago        
	        Hashtable hashTasas = new Hashtable();
	        if (rcol.getFormasPagoRecibo().size() != 0) {
	            ReciboDetFormaPago[] pp = new  ReciboDetFormaPago[rcol.getFormasPagoRecibo().size()];
	            pp=rcol.getFormasPagoRecibo().toArray(pp);
	            for(int i=0; i<pp.length; i++) {
	                ReciboDetFormaPago p = pp[i];                
	                if (i > 0) y += 30;
	                
	                //Forma de pago, entidad bancaria
	                recibo += "T 7 0 0 " + y + " " + p.getCodFormaPago() + "\r\n";
	                if (p.getCodEntidad().length() > 0) 
	                    recibo += "T 7 0 80 " + y + " " + p.getCodEntidad() + "\r\n";
	                
	                //Monto
	                String moneda = p.getCodMoneda();                 
	                if (!hashTasas.containsKey(moneda) && (moneda.compareTo(monedaNac) != 0))
	                    hashTasas.put(moneda, new Float(p.getTasaCambio()));
	                    
	                recibo += "RIGHT 340\r\n";
	                recibo += "T 7 0 0 " + y + " " + moneda + " " + StringUtil.formatReal(p.getMonto()) + "\r\n";
	                recibo += "LEFT\r\n";
	        
	                //Fecha y número si pago no es efectivo
	                if (p.getCodFormaPago().compareTo("EFEC") == 0) continue;
	                
	                if (p.getFecha() > 0) recibo += "T 7 0 360 " + y + " " + DateUtil.idateToStrYY(p.getFecha()) + "\r\n";
	                if (p.getNumero().length() > 0) recibo += "T 7 0 470 " + y + " " + p.getNumero() + "\r\n";
	            }
	        }
	        
	        //Imprimiendo detalle de notas de crédito aplicadas
	        if (rcol.getNotasCreditoRecibo().size() != 0) 
	        {
	            ReciboDetNC[] cc = new ReciboDetNC[rcol.getNotasCreditoRecibo().size()];
	            cc= rcol.getNotasCreditoRecibo().toArray(cc);
	            for(int i=0; i<cc.length; i++) 
	            {
	                ReciboDetNC c = cc[i];                
	                y += 30;
	                
	                //Forma de pago
	                recibo += "T 7 0 0 " + y + " NC\r\n";
	                                
	                //Monto                
	                recibo += "RIGHT 340\r\n";
	                recibo += "T 7 0 0 " + y + " " + monedaNac + " " + StringUtil.formatReal(c.getMonto()) + "\r\n";
	                recibo += "LEFT\r\n";
	        
	                //Número de NC
	                recibo += "T 7 0 470 " + y + " " + c.getNumero() + "\r\n";
	            }
	        }
	        
	        y += 30;
	        recibo += "LINE 0 " + y + " 575 " + y + " 1\r\n";
	        y += 40;
	                
	        // Resumen del Recibo
	        recibo += "T 7 0 0 " + y + " Resumen\r\n";
	        y += 30; 
	        recibo += "LINE 0 " + y + " 575 " + y + " 1\r\n";
	        y += 5;           
	            
	        //Total Facturas
	        if (rcol.getTotalFacturas() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " +Total Facturas:\r\n";
	            recibo += "RIGHT 400\r\n";
	            recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalFacturas()) + "\r\n";
	            y += 30;
	        }
	        
	        if (rcol.getTotalND() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " +ND:\r\n";
	            recibo += "RIGHT 400\r\n";
	            recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalND()) + "\r\n";
	            y += 30;
	        }
	        
	        if (rcol.getTotalInteres() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " +Interés Moratorio:\r\n";
	            recibo += "T 7 0 200 " + y + " " + StringUtil.formatReal(rcol.getTotalInteres()) + "\r\n";
	            y += 30;
	        }
	    
	        if (rcol.getTotalNC() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " -NC:\r\n";
	            recibo += "RIGHT 400\r\n";
	            recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalNC()) + "\r\n";
	            y += 30;
	        }
	        
	        if (rcol.getTotalDesc() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " -Descuento:\r\n";
	            recibo += "RIGHT 400\r\n";
	            recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalDesc()) + "\r\n";
	            y += 30;
	        }
	        
	        if (rcol.getTotalRetenido() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " -Retención:\r\n";
	            recibo += "RIGHT 400\r\n";
	            recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalRetenido()) + "\r\n";        
	            y += 30;        
	        }
	        
	        if (rcol.getTotalOtrasDed() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " -Otros:\r\n";
	            recibo += "RIGHT 400\r\n";
	            recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalOtrasDed()) + "\r\n";        
	            y += 30;
	        }
	                
	        if (rcol.getTotalImpuestoExonerado() > 0) {
	            recibo += "LEFT\r\n";
	            recibo += "T 7 0 0 " + y + " -Impuesto Exento:\r\n";
	            recibo += "RIGHT 400\r\n";
	            recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalImpuestoExonerado()) + "\r\n";        
	            y += 30;
	        }
	        
	        recibo += "LEFT\r\n";        
	        recibo += "LINE 0 " + y + " 575 " + y + " 1\r\n";
	        y += 5;
	        
	        recibo += "T 7 0 0 " + y + " Neto a Pagar:\r\n";
	        recibo += "RIGHT 400\r\n";
	        recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(rcol.getTotalRecibo()) + "\r\n";        
	        recibo += "LEFT\r\n";
	
	        y += 40;
	        
	        // Saldo Pendiente de Documentos Abonados        
	        if (hayDocAbonados) {
	            recibo += "T 7 0 0 " + y + " Saldo Pendiente de Documentos Abonados\r\n";
	            y += 30;
	            recibo += "LINE 0 " + y + " 576 " + y + " 1\r\n";
	            y += 5;
	            
	            if (rcol.getFacturasRecibo().size() != 0) 
	            {
	                ReciboDetFactura[] ff =new  ReciboDetFactura[rcol.getFacturasRecibo().size()];
	                ff =rcol.getFacturasRecibo().toArray(ff);
	                for(int i=0; i<ff.length; i++) {
	                    ReciboDetFactura f = ff[i];
	                    if (!f.isEsAbono()) continue;
	                    
	                    recibo += "T 7 0 0 " + y + " FA\r\n";
	                    recibo += "T 7 0 50 " + y + " " + f.getNumero() + "\r\n";
	                    
	                    recibo += "RIGHT 400\r\n";
	                    recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(StringUtil.round(f.getSaldoTotal() - f.getMonto(), 2)) + "\r\n";
	                    recibo += "LEFT\r\n";
	                    y += 30;
	                }
	            }
	            
	            if (rcol.getNotasDebitoRecibo().size() != 0 ) {
	                ReciboDetND[] dd =new ReciboDetND[rcol.getNotasDebitoRecibo().size()];
	                dd=rcol.getNotasDebitoRecibo().toArray(dd);
	                for(int i=0; i<dd.length; i++) {
	                    ReciboDetND d = dd[i];
	                    if (!d.isEsAbono()) continue;
	                    
	                    recibo += "T 7 0 0 " + y + " ND\r\n";
	                    recibo += "T 7 0 50 " + y + " " + d.getNumero() + "\r\n";
	                    
	                    recibo += "RIGHT 400\r\n";
	                    recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(StringUtil.round(d.getSaldoTotal() - d.getMontoPagar(), 2)) + "\r\n";
	                    recibo += "LEFT\r\n";
	                    y += 30; 
	                }
	            }
	                
	            recibo += "LINE 0 " + y + " 575 " + y + " 1\r\n";
	            y += 40;
	        } //if (hayDocAbonados)
	        
	                
	        //Tasas de cambio
	        if (hashTasas.size() > 0) {
	            String tc = "";  
	            
	            for (Enumeration e = hashTasas.keys() ; e.hasMoreElements() ;) {
	                String cod = (String)e.nextElement();
	                Float valor = (Float)hashTasas.get(cod);
	                tc = tc + " " + cod + " -> " + StringUtil.formatReal(valor.floatValue()) + ",";        
	            }
	            tc = tc.trim();
	            tc = tc.substring(0, tc.length() - 1);
	            
	            tc = "Tasas de cambio: " + tc;
	            recibo += "T 0 0 0 " + y + " " + tc + "\r\n";
	            y += 20;
	        }
	                
	        //Notas
	        String notas = rcol.getNotas();
	        if (notas == null) notas = "";
	        linea1 = notas;
	        linea2 = "";
	        linea3 = ""; 
	        if (notas.length() > 60) {
	            int idxLastSpace = notas.substring(0, 60).lastIndexOf(' ');
	            linea1 = notas.substring(0, idxLastSpace);
	            linea2 = notas.substring(idxLastSpace, notas.length());
	            notas = linea2;           
	            if (notas.length() > 60) {
	                idxLastSpace = notas.substring(0, 60).lastIndexOf(' ');
	                linea2 = notas.substring(0, idxLastSpace);
	                linea3 = notas.substring(idxLastSpace, notas.length());
	            }
	        } 
	        
	        if (linea1.length() > 0) {
	            recibo += "T 0 0 0 " + y + " Notas:\r\n";
	            recibo += "T 0 0 80 " + y + " " + linea1.trim() + "\r\n";      
	            if (linea2.length() > 0) {
	                y += 20;
	                recibo += "T 0 0 80 " + y + " " + linea2.trim() + "\r\n";
	            }
	            if (linea3.length() > 0) {
	                y += 20;
	                recibo += "T 0 0 80 " + y + " " + linea3.trim() + "\r\n";
	            }
	             y += 30;
	        }     
	                
	        //Nombre del colector                
	        recibo += "T 7 0 0 " + y + " Colector: " + SessionManager.getLoginUser().getNombre() + "\r\n";
	        
	        y += 40;
	        recibo += "T 7 0 50 " + y + " Este recibo no requiere firma y sello\r\n";
	        y += 30;        
	        recibo += "T 7 0 80 " + y + " ******Gracias por su pago******\r\n";
	        
	        if (reimpresion) {
	            y += 30;        
	            recibo += "T 7 0 210 " + y + " Reimpresion\r\n";
	        }
	        
	        recibo += "FORM\r\n";
	        recibo += "PRINT\r\n";
	        
	        y += 60;
	        
	        String header = "! 0 200 200 " + y + " 1\r\n";
	        header += "LABEL\r\n";
	        header += "CONTRAST 0\r\n";
	        header += "TONE 0\r\n";
	        header += "SPEED 3\r\n";
	        header += "PAGE-WIDTH 600\r\n";
	        header += "BAR-SENSE\r\n";
	                                
	        recibo = header + recibo;        
	        if(recibo.length() > 0) 
				new BluetoothConnection(recibo); 
		} catch (Exception e) 
		 { 
			 NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
			Log.d(TAG,"ERROR al tratar de envia el recibo", e);
		}
    }
	
}
