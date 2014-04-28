package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.*;  

import java.util.ArrayList;   

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.auxiliar.Processor; 
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelCliente;  
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Cliente;   
import com.panzyma.nm.view.vCliente;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogCuentasPorCobrar;
import com.panzyma.nm.viewmodel.vmCliente;

import android.annotation.SuppressLint; 
import android.content.ContentResolver;
import android.os.Message;
import android.util.Log;      
 
@SuppressLint("ParserError")@SuppressWarnings({"rawtypes","unused"})
public final class BClienteM 
{ 
    ArrayList<vmCliente> a_vaC; 
	Controller controller;
	
	vCliente view;
	DialogCuentasPorCobrar viewcc;
    ArrayList<Cliente> obj=new ArrayList<Cliente>();
    CCCliente objccc=new CCCliente(); 
    DialogCliente dlogCliente;
    ThreadPool pool;
	String TAG=BClienteM.class.getSimpleName();   
	int view_activated;
	
	public BClienteM(){}
	
	public BClienteM(vCliente view)
	{
		this.view= view;
    	this.controller=((NMApp)view.getApplication()).getController();      	
    	this.pool =((NMApp)view.getApplication()).getThreadPool();
    	view_activated=1;
    }
	
	 
	public BClienteM(DialogCuentasPorCobrar view)
	{
    	this.controller=((NMApp)view.getContext().getApplicationContext()).getController();;  
    	this.viewcc=view;
    	this.pool = ((NMApp)view.getContext().getApplicationContext()).getThreadPool();
    	view_activated=2;
    }
	public BClienteM(DialogCliente view)
	{
    	this.controller=((NMApp)view.getContext().getApplicationContext()).getController();  
    	this.dlogCliente=view;
    	this.pool = ((NMApp)view.getContext().getApplicationContext()).getThreadPool();
    	view_activated=3;
    }
	public boolean handleMessage(Message msg) 
	{
		switch (msg.what) 
		{  
			case LOAD_DATA_FROM_LOCALHOST: 
					onLoadALLData_From_LocalHost();
					return true;		
			case LOAD_DATA_FROM_SERVER:  
				    onLoadALLData_From_Server(); 
					return true; 						
			case UPDATE_ITEM_FROM_SERVER:  
					onUpdateItem_From_Server(); 
					return true;
			case LOAD_FICHACLIENTE_FROM_SERVER: 
				    onLoadFichaCliente_From_Server(); 
				    return true; 
			case LOAD_FACTURASCLIENTE_FROM_SERVER: 
				    onLoadFacturasCliente_From_Server(); 
			        return true;	    
					
		}
		return false;
	}
	
	
	private void onSave_From_LocalHost(final ArrayList<Cliente> objL, int page)
	{		
		try
		{	
			this.pool.execute
			(
			  new Runnable() 
			  {				  
				@Override
				public void run() 
				{						
					try
					{      
						ModelCliente.RegisterCustomer_From_LocalHost(objL, view.getApplicationContext()); 						
					} 
			    	catch(Exception e)
			    	{
			    	    e.printStackTrace();  
			    	    try {
			    	    	Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error interno salva guardando datos en la BDD",e.toString(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) { 
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
	
	private void onLoadALLData_From_LocalHost()
	{		
		try 
		{   
			pool.execute
			(
				new Runnable()
				{
	
					@Override
					public void run() 
					{ 
						
						try
						{ 
							if(view_activated==1)	{								
								Processor.send_ViewCustomerToView((ModelCliente.getArrayCustomerFromLocalHost(view.getContentResolver())), controller);
							}
							else if(view_activated==3)
								Processor.send_ViewCustomerToView((ModelCliente.getArrayCustomerFromLocalHost(dlogCliente.getContext().getContentResolver())), controller);
						}
						catch (Exception e) 
						{
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error interno en la sincronización con la BDD",e.toString(),"\n Causa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							}  
						}
					}
				}
			);
			
		}  
		catch (Exception e) 
        { 
			e.printStackTrace();
		} 
		
	}
	 
	public static Cliente getClienteBySucursalID(ContentResolver content,long objSucursalID)throws Exception{
		 return ModelCliente.getClienteBySucursalID(content,objSucursalID);
	}
	
	private void onLoadALLData_From_Server() 
	{ 
		try 
		{	 
			obj.clear();
			this.pool.execute(  new Runnable()
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
							       ArrayList<Cliente> modelcliente=ModelCliente.getArrayCustomerFromServer("sa||nordis09||dp","kpineda",page,30); 
								   if(modelcliente.size()!=0)
								   {   
									   obj.addAll(modelcliente);
									   Processor.builAndsend_ViewCustomerToView(modelcliente,controller);
								   	   page++;
								   }
								   else  
									   break; 							
								  
								} 	
							    Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,null);
							    onSave_From_LocalHost(obj,0); 
					    } 
						  						 				 
					}  
					catch (Exception e) 
			        { 
						e.printStackTrace();
						try {
							Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));							 
						} catch (Exception e1) { 
							e1.printStackTrace();
						} 
					} 
				}
            }); 
			Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, null); 		
			 
		}   
		catch (Exception e) 
        { 
			e.printStackTrace();
		}
		
	}
	 
	private void onUpdateItem_From_Server() 
	{ 
		obj.clear();
		try 
		{		  
			this.pool.execute
			(  
				new Runnable()
	            { 
					@Override
					public void run() 
					{
						try 
						{      	 
							
							Cliente modelcliente = null;
							if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
						    {
									modelcliente =ModelCliente.getCustomerFromServer("sa||nordis09||dp",view.get_SucursalID());
									if(modelcliente!=null)
									{
										obj.add(modelcliente);
										ModelCliente.UpdateCustomer_From_LocalHost(obj, view.getApplicationContext());
										Processor.notifyToView(controller,C_UPDATE_ITEM_FINISHED,0,0,"sincronización exitosa");
									} 
							}
							 
							
							
						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronización con el servidor",e.toString(),"\nCausa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							} 
						}  
					}
	            }
			);  	
			 
		}  
		catch (Exception e) 
        { 
			e.printStackTrace();
		}
		
	} 
    
    private void onLoadFichaCliente_From_Server() 
	{  
		try 
		{		  
			this.pool.execute
			(  
				new Runnable()
	            { 
					@Override
					public void run() 
					{
						try 
						{    
							if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller)) 
									Processor.send_ViewFichaCustomerToView(ModelCliente.GetFichaCustomerFromServer("sa||nordis09||dp",view.get_SucursalID()),controller);
							 
						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronización con el servidor",e.toString(),"\nCausa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							}  
						}  
					}
	            }
			);  	
			 
		}  
		catch (Exception e) 
        { 
			e.printStackTrace();
		}
		
	}
 
	private void onLoadFacturasCliente_From_Server() 
	{  
		try 
		{		  
			this.pool.execute
			(  
				new Runnable()
	            { 
					@Override
					public void run() 
					{
						try 
						{    
							if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
							{
									Parameters params=viewcc.get_FacturaParameters();
									Processor.send_FacturasToViewCuentasPorCobrar(ModelCliente.getFacturasClienteFromServer(params),controller);
							} 
						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronización con el servidor",e.toString(),"\nCausa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							}  
						}  
					}
	            }
			);  	
			 
		}  
		catch (Exception e) 
        { 
			e.printStackTrace();
		}
		
	}
    
}
