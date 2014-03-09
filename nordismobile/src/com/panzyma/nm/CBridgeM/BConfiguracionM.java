package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_SETTING;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION;
import static com.panzyma.nm.controller.ControllerProtocol.C_FINISH;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_IN_PROGRESS;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SALVAR_CONFIGURACION;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS2;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PARAMETROS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CLIENTES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PRODUCTOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PROMOCIONES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_TODOS;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
  
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.Controller; 
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DataConfigurationResult;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.viewmodel.vmConfiguracion;


@SuppressWarnings({"rawtypes","unused"})
public class BConfiguracionM {

	String TAG=BConfiguracionM.class.getSimpleName();
	ViewConfiguracion view; 
	Controller controller;
	int view_activated;
	ArrayList<Cliente> LCliente; 
	JSONArray productos;
	JSONArray promociones;
	Object lock=new Object();
	public int ON_ID_Handler;
	public BConfiguracionM(){}
	
	public BConfiguracionM(ViewConfiguracion view)
	{
		
    	this.controller=((NMApp)view.getApplication()).getController();  
    	this.view=view;
    	view_activated=1; 
    }
	
	public boolean handleMessage(Message msg) 
	{
		ON_ID_Handler=msg.what;
		Bundle b = msg.getData();
		switch (msg.what) 
		{  
		
			case LOAD_DATA: 
													LOAD_DATA();
													return true; 
			case LOAD_SETTING:  
													GET_DATACONFIGURATION(b.get("Credentials").toString(),b.get("LoginUsuario").toString(),b.get("PIN").toString());
													return true; 
			case ID_SALVAR_CONFIGURACION:  
													return true; 
			case ID_SINCRONIZE_PARAMETROS:  
													SINCRONIZE_PARAMETROS(); 
													return true;    
			case ID_SINCRONIZE_CATALOGOSBASICOS: 
													SINCRONIZE_CATALOGOSBASICOS();
													return true;  
			case ID_SINCRONIZE_CLIENTES: 
													SINCRONIZE_CLIENTES();
													return true;   
			case ID_SINCRONIZE_PRODUCTOS: 
													SINCRONIZE_PRODUCTOS();
													return true;   
			case ID_SINCRONIZE_PROMOCIONES: 
													SINCRONIZE_PROMOCIONES();
													return true;   
			case ID_SINCRONIZE_TODOS: 
													SINCRONIZE_TODOS();
													return true;   
		}
		return false;
	}
	
	private void LOAD_DATA()
	{
		try
		{			 
			Processor.send_DataSourceToView(ModelConfiguracion.getVMConfiguration(view), controller);			 			 
		}
		catch(Exception e)
		{
			try {
				Processor.notifyToView(controller,ERROR,0,0,"CheckConnection :"+"\n error en la comunicación con el servidor de aplicaciones \n Causa:"+e.toString());
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		}		
		
	}
	
	public  void GET_DATACONFIGURATION(final String Credentials,final String LoginUsuario,final String PIN)
	{		
		try 
		{
				((NMApp)view.getApplication()).getThreadPool().execute(  new Runnable()
				{
					@Override
					public void run() 
					{	
						try {
								DataConfigurationResult res=ModelConfiguracion.getDataConfiguration(Credentials, LoginUsuario, PIN);
								if(res.get_error()==null){
									Processor.notifyToView(controller,NOTIFICATION, 0, 0, new NotificationMessage("","Salvando configuración.",""));
									vmConfiguracion setting=vmConfiguracion.setConfiguration(view.getUrlServer(),
																				String.valueOf(res.get_devicePrefix()),
																				view.getEnterprise(),
																				res.get_userInfo().getLogin(),
																				view.getPasswd(),
																				res.get_maxIdPedido(),
																				res.get_maxIdRecibo());
									ModelConfiguracion.saveConfiguration(view, setting);
									SessionManager.setLoguedUser(res.userInfo);
									
									Processor.notifyToView(controller,C_FINISH, 0, 1, new NotificationMessage("","Configuración registrada con exito.","")); 
								}
								else
									Processor.notifyToView(controller,ERROR, 0, 1,new ErrorMessage("error en la comunicacion con el servidor",res.get_error()+"\r\n",""));
								
								
							} catch (Exception e) { 
								e.printStackTrace();
							}	 
					}
				}
				);
				Processor.notifyToView(controller, NOTIFICATION, 0,0, new NotificationMessage("","Validando información con el servidor.",""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 			
	}
	
	public static Usuario GET_DATAUSER(String Credentials,String LoginUsuario) throws Exception
	{
		return ModelConfiguracion.getDatosUsuario(Credentials, LoginUsuario);
	}
	
	public static int GET_DEVISE_PREFIX(String Credentials,String PIN) throws Exception
	{
		return ModelConfiguracion.get_DEVISE_PREFIX(Credentials, PIN);
	} 
	
	private void SINCRONIZE_PARAMETROS()
	{
		try
		{
			final String credentials=SessionManager.getCredenciales();
			if(credentials.trim()!="")
			{	
				((NMApp)view.getApplicationContext()).getThreadPool().execute( new Runnable()
	            {
	            	@Override
					public void run() 
					{
						try 
						{							
							if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
						    {						
								onSave_From_LocalHost(ModelConfiguracion.getSystemPerams(credentials),ID_SINCRONIZE_PARAMETROS,0);
						    }
						}
						catch(Exception e)
						{
							e.printStackTrace();
							try {
								Processor.notifyToView(controller,ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							} 
						}
					}
	            });
				Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, "Sincronizando parametros del sistema");
			}
						
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	private void SINCRONIZE_CATALOGOSBASICOS()
	{
		try
		{
			final String credentials=SessionManager.getCredenciales();
			if(credentials.trim()!="")
			{	
				((NMApp)view.getApplicationContext()).getThreadPool().execute( new Runnable()
	            {
	            	@Override
					public void run() 
					{
						try 
						{ 
							onSave_From_LocalHost(ModelConfiguracion.getValoresCatalogo(credentials, "FormaPago-Moneda-EntidadBancaria"),ID_SINCRONIZE_CATALOGOSBASICOS,0);
							Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, "Sincronizando Tasas de Cambio");
						    onSave_From_LocalHost(ModelConfiguracion.getTasasDeCambio(credentials),ID_SINCRONIZE_CATALOGOSBASICOS2,0);			 
						}
						catch(Exception e)
						{
							e.printStackTrace();
							try {
								Processor.notifyToView(controller,ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							} 
						}
					}
	            });
				
				Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, "Sincronizando FormaPago-Moneda-EntidadBancaria"); 
			}
			
						
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	private void SINCRONIZE_CLIENTES()
	{ 
		try
		{ 
		    final String credentials=SessionManager.getCredenciales();
			  
			if(credentials.trim()!="")
			{	
				
				((NMApp)view.getApplicationContext()).getThreadPool().execute(  new Runnable()
	            {
	            	@Override
					public void run() 
					{
						try 
						{
							if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
						    {
								Integer page=1;
							    while(true)
								{ 					    	 
							       JSONArray modelcliente=ModelCliente.getArrayCustomerFromServer2(credentials,view.getLoginUsuario(),page,50); 
								   if(modelcliente.length()!=0)
								   {    
									   onSave_From_LocalHost(modelcliente,ID_SINCRONIZE_CLIENTES,page);
									   Processor.notifyToView(controller,C_UPDATE_IN_PROGRESS, 0, 0,"Sincronizando Clientes \npágina:"+page.toString()+" ...");
								   	   page++;
								   }
								   else{
									   synchronized(lock){ 
								           lock.notify(); 
								       }		
									   break;
								   }
									    					  
								} 	
							    //onSave_From_LocalHost(LCliente,ID_SINCRONIZE_CLIENTES);
							    if(ON_ID_Handler!=ID_SINCRONIZE_TODOS)
							    Processor.notifyToView(controller,C_UPDATE_FINISHED,0,1,"Los clientes han sido sincronizados exitosamente");
								 					
							}
		  						 				 
						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(controller,ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							} 
						} 
					}
	            }); 
				Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 1,"Sincronizando Clientes"); 		
		    }
		}     
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void SINCRONIZE_PRODUCTOS()
	{
		try 
		{ 

			final String Credentials=SessionManager.getCredenciales();
			if(Credentials.trim()!="")
			{	
				((NMApp)view.getApplicationContext()).getThreadPool().execute
				    (  new Runnable()
						{
							@Override
							public void run() 
							{
								try 
								{								
									Integer page=1; 
								    while(true)
									{ 			
								       JSONArray modelproducto=ModelProducto.getArrayProductoFromServer(Credentials,view.getLoginUsuario(),page,50);
								       if(modelproducto.length()!=0)							      
									   {  
								    	   onSave_From_LocalHost(modelproducto,ID_SINCRONIZE_PRODUCTOS,page);
										   Log.d(TAG,"C_UPDATE_IN_PROGRESS on SINCRONIZE_PRODUCTOS");
										   Processor.notifyToView(controller,C_UPDATE_IN_PROGRESS, 0, 0,"Sincronizando Productos \npágina: "+page.toString()+" ...");
									       page++;
									   }
								       else{
										   synchronized(lock){ 
									           lock.notify(); 
									       }		
										   break;
									   }	 
									} 
								    if(ON_ID_Handler!=ID_SINCRONIZE_TODOS)
								    	Processor.notifyToView(controller,C_UPDATE_FINISHED,0,1,"La información del Catalogo de Productos fueron sincronizados y salvados con exito...");
								}
								catch (Exception e) 
						        { 
									e.printStackTrace();
									try {
										Processor.notifyToView(controller,ERROR,0,1,new ErrorMessage("Error en la sincronización de produstos con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
									} catch (Exception e1) { 
										e1.printStackTrace();
									} 
							    } 
							}
							
							
						}
				    );
				Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, "Sincronizando Productos");
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
	
	private void SINCRONIZE_PROMOCIONES()
	{
		try
		{
			
			final String credentials=SessionManager.getCredenciales();
			if(credentials.trim()!="")
			{	
				((NMApp)view.getApplicationContext()).getThreadPool().execute( new Runnable()
	            {
	            	@Override
					public void run() 
					{
						try 
						{ 							
							Integer page=1; 
						    while(true)
							{ 	 
						       JSONArray lpromocion=ModelConfiguracion.getPromocionesPaged(credentials,view.getLoginUsuario(),page,30);
						       if(lpromocion.length()!=0)							      
							   {	  
						    	   Log.d(TAG,"C_UPDATE_IN_PROGRESS on SINCRONIZE_PRODUCTOS");
						    	   onSave_From_LocalHost(lpromocion,ID_SINCRONIZE_PROMOCIONES,page); 
								   Processor.notifyToView(controller,C_UPDATE_IN_PROGRESS, 0, 0,"Sincronizando Promociones \npágina: #"+page.toString());
							       page++;
							   } 
							   else{
								   synchronized(lock){ 
							           lock.notify(); 
							       }		
								   break;
							   }	
						       if(ON_ID_Handler!=ID_SINCRONIZE_TODOS)
						    	   Processor.notifyToView(controller,C_UPDATE_FINISHED,0,1,"Promociones fueron sincronizadas con exito.");
								  
							} 
							
						}
						catch(Exception e)
						{
							Log.d(TAG,e.getMessage());
						}
					}
	            });
				
				Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, "Sincronizando Promociones"); 
			}
			
			
		}
		catch(Exception e)
		{
			
		}
		
	}
	 
	private void SINCRONIZE_TODOS()
	{
		try
		{
			((NMApp)view.getApplicationContext()).getThreadPool().execute(new Runnable()
	        {			        
				@Override
				public void run() {

					SINCRONIZE_PARAMETROS();
					synchronized(lock)
			        {
			            try {
			            	lock.wait();
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}
			        }
					SINCRONIZE_CATALOGOSBASICOS();
					synchronized(lock)
			        {
			            try {
			            	lock.wait();
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}
			        }
					SINCRONIZE_CLIENTES();
					synchronized(lock)
			        {
			            try {
			            	lock.wait();
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}
			        }
					SINCRONIZE_PRODUCTOS();
					synchronized(lock)
			        {
			            try {
			            	lock.wait();
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}
			        }
					SINCRONIZE_PROMOCIONES();
					synchronized(lock)
			        {
			            try {
			            	lock.wait();
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}
			        }
					try {
						Processor.notifyToView(controller,C_UPDATE_FINISHED, 0, 1,"Catalogos sincronizados exitosamente");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 			
					
					
			}); 
			
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	private synchronized void onSave_From_LocalHost(final JSONArray objL, final int ID,final int page){
	
		try
		{ 
			
			((NMApp)view.getApplicationContext()).getThreadPool().execute
			(
			  new Runnable() 
			  {				   
				@Override
				public void run() 
				{						
					try
					{      
						switch (ID) 
						{  
							case ID_SINCRONIZE_PARAMETROS: 			ModelConfiguracion.saveSystemParam(view,objL);
																	if(ON_ID_Handler!=ID_SINCRONIZE_TODOS)
																		Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,"Los Parametros del sistema fueron sincronizados con exito.");
																	synchronized(lock)
															        {
															            try {
															            	lock.notify();
																		} catch (Exception e) { 
																			e.printStackTrace();
																		}
															        }
																	break;							
							case ID_SINCRONIZE_CATALOGOSBASICOS:    ModelConfiguracion.saveValorCatalogoSystem(view,objL); 
																	break;
																	
							case ID_SINCRONIZE_CATALOGOSBASICOS2: 	ModelConfiguracion.saveTasasDeCambio(view,objL);
																	if(ON_ID_Handler!=ID_SINCRONIZE_TODOS)
																		Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,"Los Catalogos Básicos del sistema fueron sincronizados con exito.");
																	synchronized(lock)
															        {
															            try {
															            	lock.notify();
																		} catch (Exception e) { 
																			e.printStackTrace();
																		}
															        }
																	break;
							
							case ID_SINCRONIZE_CLIENTES: 			ModelCliente.saveClientes(objL, view,page);																									
																	break;
																								
							case ID_SINCRONIZE_PRODUCTOS:   		
								                                    ModelProducto.saveProductos( objL, view,page); 
								                                    break;
																	
							case ID_SINCRONIZE_PROMOCIONES: 		ModelConfiguracion.savePromociones(view, objL,page);												 
																	break;
							
						/*	case ID_SINCRONIZE_TODOS:    			break;*/
						}						 						
					} 
			    	catch(Exception e)
			    	{
			    	    e.printStackTrace();  
			    	    try {
			    	    	Processor.notifyToView(controller,ERROR,0,1,new ErrorMessage("Error interno salva guardando datos en la BDD",e.toString(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) 
						{ 
							e1.printStackTrace();
						} 
			    	}  
				}
			  }
		    );
		} 
		catch(Exception e)
    	{
    	    e.printStackTrace();  
    	} 
	} 
	
}
