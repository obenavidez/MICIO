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
import static com.panzyma.nm.controller.ControllerProtocol.IMPRIMIR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.JSONArray;
 

















import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
/*import android.os.Handler;*/
import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NumberUtil;
/*import com.panzyma.nm.auxiliar.NMNetWork;*/
/*import com.panzyma.nm.auxiliar.Parameters; by jrostran */
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Recibo;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.serviceproxy.RespuestaEnviarRecibo;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.viewdialog.DialogDocumentos;

@SuppressWarnings("rawtypes")
public final class BReciboM {

	private static final String logger = BReciboM.class.getSimpleName();

	Controller controller;
	ThreadPool pool;
	private ViewRecibo view;
	private DialogDocumentos view1;
	private ViewReciboEdit reciboEdit;
	private Object lock=new Object();
	boolean OK = false;
	ArrayList<Producto> obj = new ArrayList<Producto>();
	JSONArray jsonA = new JSONArray();
	static boolean imprimir = false;
    static boolean pagarOnLine = false;
	public BReciboM() {
	}

	public BReciboM(ViewRecibo view) {
		this.controller = ((NMApp) view.getApplicationContext())
				.getController();
		this.view = view;
		this.pool = ((NMApp) view.getApplicationContext()).getThreadPool();
	}

	public BReciboM(ViewReciboEdit view) {
		//view.getApplicationContext()
		this.controller = ((NMApp) view.getApplicationContext())
				.getController();
		this.reciboEdit = view;
		this.pool = ((NMApp) view.getApplicationContext()).getThreadPool();
	}
	
	public BReciboM(DialogDocumentos view1) {
		this.controller = ((NMApp) view1.getContext().getApplicationContext())
				.getController();
		this.view1 = view1;
		this.pool = ((NMApp) view1.getContext().getApplicationContext()).getThreadPool();
	}


	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) {
		case SAVE_DATA_FROM_LOCALHOST:
			Bundle rec=msg.getData();
			onSaveDataToLocalHost((Recibo)rec.getParcelable("recibo"));
			break;
		case LOAD_DATA_FROM_LOCALHOST:
			onLoadALLDataFromLocalHost();
			return true;
		case LOAD_ITEM_FROM_LOCALHOST: 
			onLoadItemFromLocalHost();
			return true;
		case DELETE_DATA_FROM_LOCALHOST:
			onDeleteDataFromLocalHost();
			break;
		case C_FACTURACLIENTE:
			onLoadDocumentosClienteFromLocalhost();
			break; 
		case LOAD_DATA_FROM_SERVER:
			// onLoadALLData_From_LocalHost();
			return true;
		case UPDATE_ITEM_FROM_SERVER:
			// onUpdateItem_From_Server();
			return true;
		case SEND_DATA_FROM_SERVER: 
			enviarRecibo();
			break;

		}
		return false;
	}

	private void onSaveDataToLocalHost(final Recibo recibo) 
	{
		try {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					
					try 
					{
						
						//Guardando localmente el recibo
						Recibo reciboL=DatabaseProvider.registrarRecibo(recibo, reciboEdit.getContext()); 
						//Actualizando el id máximo de recibo generado
						Integer prefijo =Integer.parseInt(NumberUtil.setFormatPrefijo(ModelConfiguracion.getDeviceID(reciboEdit.getContext()), reciboL.getId()));						
						ModelConfiguracion.setMaxReciboId(reciboEdit.getContext(),(int) ((reciboL.getId())-prefijo));
						Integer idrec =ModelConfiguracion.getMaxReciboID(reciboEdit.getContext());
						Log.d("IDREC", idrec+"");
						Processor
						.notifyToView(
								controller,
								ControllerProtocol.ID_REQUEST_SALVARPEDIDO,
								0,
								0,
								recibo
								);
					} catch (Exception e) 
					{ 
						e.printStackTrace();
					}				
					
				}
			});
		}catch(Exception e){
			Log.e(logger, "Error in the update thread", e);
			try {
				Processor
						.notifyToView(
								controller,
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

	private void onLoadItemFromLocalHost() {
		try {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					Processor.send_ViewReciboEditToView(
							ModelRecibo.getReciboByID(
									reciboEdit.getContentResolver(),
									reciboEdit.getReciboID()), controller);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void onDeleteDataFromLocalHost() {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try 
					{						
						int idrecibo=view.getReciboSelected().getId();
						ModelRecibo.borraReciboByID(view.getContentResolver(), idrecibo);
						//Actualizando el id máximo de recibo generado
						Integer prefijo =Integer.parseInt(NumberUtil.setFormatPrefijo(ModelConfiguracion.getDeviceID(view.getContext()), idrecibo)); 
						Processor.send_ViewDeleteReciboToView(1,controller);
						
					} catch (Exception e) 
					{
						Log.e(logger, "Error in the update thread", e);
						try 
						{
							Processor.notifyToView(
											controller,
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
			// TODO: handle exception
		}
		
	}

	private void onUpdateItem_From_Server() {
		// TODO Auto-generated method stub

	}

	private void onLoadALLDataFromLocalHost() {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {

						Processor.send_ViewReciboToView(
								ModelRecibo
										.getArrayCustomerFromLocalHost((view != null) ? view
												.getContentResolver() : view1
												.getContext()
												.getContentResolver()),
								controller);
					} catch (Exception e) {
						Log.e(logger, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											controller,
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

	private void onLoadDocumentosClienteFromLocalhost() {
		try {
			this.pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// Parameters params = viewcc.get_FacturaParameters();
						Processor.send_ViewReciboEditToView(
								ModelCliente.getClienteBySucursalID(view1.getContext()
										.getContentResolver(), view1
										.getObjSucursalId()), controller);

					} catch (Exception e) {
						e.printStackTrace();
						try {
							Processor
									.notifyToView(
											controller,
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
	
	private void enviarRecibo()
	{
		try 
		{
			pool.execute(new Runnable() 
			{
				@Override
				public void run() 
				{
					try 
					{ 						
						String credenciales="";
						credenciales=SessionManager.getCredentials(); 
						Recibo recibo=reciboEdit.getRecibo();
						
						if(credenciales!="")
						{
							
							if (recibo.getCodEstado().compareTo("PAGADO") == 0) return;
					        
					        if (recibo.getNumero() > 0) 
					        {
					        	Processor.notifyToView(controller,ControllerProtocol.NOTIFICATION_DIALOG2,0,0,"El recibo ya fue enviado"
								      ); 
					        	//Util.Message.buildToastMessage(reciboEdit,"El recibo ya fue enviado.", TIME_TO_VIEW_MESSAGE).show(); 
					            return;
					        }
					        
					        imprimir = true;
					        pagarOnLine = true;        
					        //Si se está fuera de covertura, salir        
					        if (recibo.getCodEstado().compareTo("PAGADO_OFFLINE") == 0) 
					        {
					        	imprimir = false;
					            if (SessionManager.isPhoneConnected()) 
					            {
					            	Processor.notifyToView(controller,ERROR,0,0,
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
					            	Processor.notifyToView(controller,ERROR,0,0,
											new ErrorMessage(
													          "Error en el Modulo Recibo.",
													          "Error en el proceso de envio del recibo", "\nCausa: "
															  + "El recibo no será enviado por falta de cobertura\r\nEl recibo será impreso y quedará pendiente de enviarse."
															 )
									      );   
					            
					            }
					        } 

							//Guardando cambios en el Dispositivo
					        onSaveDataToLocalHost(recibo);
					        
					        if (pagarOnLine) 
							{
								
								
								RespuestaEnviarRecibo rs=ModelRecibo.enviarRecibo(credenciales,recibo);
								if (rs == null) 
								{
									Processor.notifyToView(controller,ERROR,0,0,
											new ErrorMessage(
													          "Error en el Modulo Recibo.",
													          "Error en el proceso de envio del recibo", "\nCausa: "
															  + "No se conocen las causas."
															 )
									      ); 
								}
								else if (rs.getNuevoEstado().getCodigo().compareTo("RECHAZADO_FECHA") == 0) 
				                {
								
									Processor.notifyToView(controller,ERROR,0,0,
											new ErrorMessage(
													          "Error en el Modulo Recibo.",
													          "El recibo fue rechazado por el servidor", "\nCausa: "
															  + "Fecha inválida."
															 )
									      ); 
				                }
								//Actualizar información del nuevo estaado del recibo
				                recibo.setObjEstadoID(rs.getNuevoEstado().getId());
				                recibo.setCodEstado(rs.getNuevoEstado().getCodigo());
				                recibo.setDescEstado(rs.getNuevoEstado().getDescripcion());
				                recibo.setNumero(rs.getNumeroCentral());
				                
				                //Guardando cambios en el Dispositivo
				                DatabaseProvider.registrarRecibo(recibo, reciboEdit.getContext());
				                //Trayendo información del Cliente actualizada desde el servidor y guadarla localmente automaticamente 
								Cliente cliente=BClienteM.actualizarCliente(reciboEdit.getContext(), credenciales,recibo.getObjSucursalID());
								//actualizando el cliente en el hilo principal
								recibo.setCliente(cliente);
								//Salvar los cambios en el hilo pricipal
				                reciboEdit.setRecibo(recibo);
				              //Guardando cambios en el Dispositivo
				                onSaveDataToLocalHost(recibo);
							}
							else
							{								 
				                //Poner estado de recibo en PAGADO_OFFLINE                   
								recibo.setCodEstado("PAGADO_OFFLINE");
								recibo.setDescEstado("Registrado"); 	
				                //Salvar los cambios en el hilo pricipal
				                reciboEdit.setRecibo(recibo);
				                //Guardando cambios en el Dispositivo
				                onSaveDataToLocalHost(recibo);
							}
							
					        if(imprimir)
					        	enviarImprimirRecibo(recibo);
							
						} 
						
					} catch (Exception e) 
					{ 
						
						try {
							Processor.notifyToView(controller,ERROR,0,0,
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
			// TODO: handle exception
		} 
	}
    
	
	private void enviarImprimirRecibo(final Recibo recibo)
	{		
		reciboEdit.runOnUiThread(new Runnable() 
        {
			@Override
			public void run() 
			{ 
				 AppDialog.showMessage(reciboEdit,"Confirme por favor.!!!","Desea Imprimir el Recibo?",AppDialog.DialogType.DIALOGO_CONFIRMACION,new AppDialog.OnButtonClickListener() 
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
	    					                onSaveDataToLocalHost(recibo);//GUARDANDO LOCALMENTE EL RECIBO QUE SE VA ENVIAR AL SERVIDOR CENTRAL
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
		
        synchronized(lock)
        {
            try {
            	lock.wait();
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
        }
		
	}
	
	 
	public  void ImprimirReciboColector(Recibo rcol,boolean reimpresion) {                
        String recibo = "";  
        String monedaNac = Cobro.getMoneda(reciboEdit);
          
        //Encabezado del recibo    
        recibo += "T 7 0 120 2 Distribuidora Panzyma - DISPAN\r\n";        
        recibo += "T 7 0 170 33 Recibo de Colector\r\n";
        recibo += "LINE 0 70 575 70 1\r\n";
        
        //Fecha y número
        recibo += "T 7 0 0 80 Fecha:\r\n";
        recibo += "T 7 0 155 80 " + DateUtil.idateToStr(rcol.getFecha()) + "\r\n";
        recibo += "T 7 0 0 110 Recibo:\r\n";
        recibo += "T 7 0 155 110 " + Cobro.getNumeroRecibo(reciboEdit,rcol.getReferencia()) + "\r\n"; 
        
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
        
        float totalPago = 0, totalDesc = 0, totalRet = 0, totalOtros = 0;
        boolean hayDocAbonados = false;
        //Imprimiendo detalle de facturas que se están pagando
        if ((rcol.getFacturasRecibo().size()!=0)) {
            ReciboDetFactura[] ff = (ReciboDetFactura[]) rcol.getFacturasRecibo().toArray();
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
        if (rcol.getNotasDebitoRecibo().size() != 0) {
            ReciboDetND[] dd = (ReciboDetND[]) rcol.getNotasDebitoRecibo().toArray();
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
            ReciboDetFormaPago[] pp = (ReciboDetFormaPago[]) rcol.getFormasPagoRecibo().toArray();
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
        if (rcol.getNotasCreditoRecibo().size() != 0) {
            ReciboDetNC[] cc = (ReciboDetNC[]) rcol.getNotasCreditoRecibo().toArray();
            for(int i=0; i<cc.length; i++) {
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
            
            if (rcol.getFacturasRecibo().size() != 0) {
                ReciboDetFactura[] ff = (ReciboDetFactura[]) rcol.getFacturasRecibo().toArray();
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
                ReciboDetND[] dd = (ReciboDetND[]) rcol.getNotasDebitoRecibo().toArray();
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
        try
        {
//            ZebraPrint zp = new ZebraPrint();
//            zp.Print(recibo);
//            zp = null;
            //Dialog.alert("El recibo fue enviado a la impresora.");
        }
        catch(Exception ioex)
        {
            //Status.show("Error: " + ioex.getMessage());
        }
    }
 
}
