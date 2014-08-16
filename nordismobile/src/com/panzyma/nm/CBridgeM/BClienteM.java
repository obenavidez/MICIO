package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.*;  

import java.util.ArrayList;   

import org.json.JSONArray;
import org.json.JSONObject;

import com.comunicator.Parameters;
import com.google.gson.Gson;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
/*import com.panzyma.nm.auxiliar.Parameters;* Comentado por Jrostrn*/
import com.panzyma.nm.auxiliar.Processor; 
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.model.ModelCliente;  
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Cliente;   
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewCliente;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.vCliente;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogCuentasPorCobrar;
import com.panzyma.nm.viewmodel.vmCliente;

import android.annotation.SuppressLint; 
import android.content.ContentResolver;
import android.content.Context;
import android.os.Message;
import android.util.Log;
 
@SuppressLint("ParserError")@SuppressWarnings({"rawtypes","unused"})
public final class BClienteM 
{ 
    ArrayList<vmCliente> a_vaC; 
	Controller controller;
	ViewCliente view2;
	ViewReciboEdit view3;
	ViewRecibo view4;
	vCliente view;
	FichaClienteFragment view5;
	DialogCuentasPorCobrar viewcc;
    ArrayList<Cliente> obj=new ArrayList<Cliente>();
    CCCliente objccc=new CCCliente(); 
    DialogCliente dlogCliente;
    ThreadPool pool;
	String TAG=BClienteM.class.getSimpleName();   
	int view_activated;	
	JSONArray ja_clientes=new JSONArray();
	
	public BClienteM(){}
	
	public BClienteM(vCliente view)
	{
		this.view = view;
    	this.controller=((NMApp)view.getApplication()).getController();      	
    	this.pool =((NMApp)view.getApplication()).getThreadPool();
    	view_activated=1;
    }
	
	public BClienteM(ViewReciboEdit view)
	{
		this.view3 = view;
    	this.controller=((NMApp)view3.getApplication()).getController();      	
    	this.pool =((NMApp)view3.getApplication()).getThreadPool();
    	view_activated = 4;
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

	public BClienteM(ViewRecibo view)
	{
		this.view4 = view;
    	this.controller=((NMApp)view.getApplication()).getController();      	
    	this.pool =((NMApp)view.getApplication()).getThreadPool();
    	view_activated=5;
    }
	public BClienteM(FichaClienteFragment view)
	{
		this.view5 = view;
		this.controller=this.controller=((NMApp)view.getActivity().getApplication()).getController();      	
    	this.pool =((NMApp)view.getActivity().getApplication()).getThreadPool();
		/*
    	this.controller=((NMApp)view.getActivity().getApplication()).getController();      	
    	this.pool =((NMApp)view.getActivity().getApplication()).getThreadPool();
    	*/
    	view_activated=6;
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
				    onLoadFichaCliente_From_Server(Long.parseLong(msg.obj.toString())); 
				    return true; 
			case LOAD_FACTURASCLIENTE_FROM_SERVER: 
				    onLoadFacturasCliente_From_Server(); 
			        return true;	    
					
		}
		return false;
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
							if(view_activated==1)	
							{								
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
	 
	private void onLoadALLData_From_Server()
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
							       JSONArray modelcliente=ModelCliente.getArrayCustomerFromServer2(credentials,"ebuitrago",page,50); 
								   if(modelcliente.length()!=0)
								   {    
									   ModelCliente.saveClientes(modelcliente,view,page);
									   ja_clientes.toJSONObject(modelcliente);
									   Processor.notifyToView(controller,C_UPDATE_IN_PROGRESS, 0, 0,"Sincronizando Clientes \npágina:"+page.toString()+" ...");
								   	   page++;
								   } 
								   else break;			  
								} 
							    
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
	
	public static Cliente getClienteBySucursalID(ContentResolver content,long objSucursalID)throws Exception{
		 return ModelCliente.getClienteBySucursalID(content,objSucursalID);
	}
	
	public static Cliente actualizarCliente(Context cnt,String credenciales, long objSucursalID) throws Exception
	{
		JSONObject modelcliente = null;
		Cliente cliente=null;
		if(NMNetWork.isPhoneConnected(cnt))
	    {
			modelcliente =ModelCliente.actualizarCliente(credenciales,objSucursalID); 
			cliente=(new Gson().fromJson(modelcliente.toString(), Cliente.class)); 
			if(modelcliente!=null)  
				ModelCliente.actualizarClienteLocalmente(modelcliente,cnt);	
			
			
	    } 
		return cliente;
	}

	
	private void onUpdateItem_From_Server() 
	{ 
		obj.clear();
		try 
		{		  
			final String credentials=SessionManager.getCredenciales();			  
			if(credentials.trim()!="")
			   return;
			this.pool.execute
			(  
				new Runnable()
	            { 
					@Override
					public void run() 
					{
						try 
						{      	 
							
							JSONObject modelcliente = null;
							if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
						    {
								modelcliente =ModelCliente.actualizarCliente(credentials,view.get_SucursalID());
								if(modelcliente!=null)  
								{
									ModelCliente.actualizarClienteLocalmente(modelcliente,view.getApplicationContext());	
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
    
    private void onLoadFichaCliente_From_Server(final Long sucursalID) 
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
							Boolean conected= false;
							switch (view_activated) {
							case 1:
								if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
									conected=true;
								break;
							case 6:
								if(NMNetWork.isPhoneConnected(view5.getActivity(),controller) && NMNetWork.CheckConnection(controller)) 
									conected=true;
								break;

							default:
								break;
							}
							if(conected)
								Processor.send_ViewFichaCustomerToView(ModelCliente.GetFichaCustomerFromServer("sa||nordis09||dp",sucursalID),controller);
							
							/*if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller)) 
									Processor.send_ViewFichaCustomerToView(ModelCliente.GetFichaCustomerFromServer("sa||nordis09||dp",sucursalID),controller);
									*/
							 
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
