package com.panzyma.nm.CBridgeM;


import java.lang.reflect.Type;
import java.util.ArrayList;   

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import static com.panzyma.nm.controller.ControllerProtocol.*;
import com.comunicator.Parameters;
import com.google.gson.Gson;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
/*import com.panzyma.nm.auxiliar.Parameters;* Comentado por Jrostrn*/
import com.panzyma.nm.auxiliar.Processor; 
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.model.ModelCliente;  
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Cliente;   
import com.panzyma.nm.viewmodel.vmCliente;

import android.annotation.SuppressLint; 
import android.content.ContentResolver;
import android.content.Context;
import android.os.Message;
import android.util.Log;
 
@SuppressLint("ParserError") 
public final class BClienteM extends BBaseM
{ 
    ArrayList<vmCliente> a_vaC;
    ArrayList<Cliente> obj=new ArrayList<Cliente>();
    CCCliente objccc=new CCCliente();    
	String TAG = BClienteM.class.getSimpleName();
	JSONArray ja_clientes=new JSONArray();	
	
	@Override
	public boolean handleMessage(Message msg) 
	{
		Boolean val=false;
		switch (msg.what) 
		{  
			case LOAD_DATA_FROM_LOCALHOST: 
					onLoadALLData_From_LocalHost();
					val=true;	
					break;
			case LOAD_DATA_FROM_SERVER:  
				    onLoadALLData_From_Server(); 
				    val=true;
				    break;
			case UPDATE_ITEM_FROM_SERVER:  
					onUpdateItem_From_Server(Long.parseLong(msg.obj.toString())); 
					val=true;
					break;
			case LOAD_FICHACLIENTE_FROM_SERVER: 
				    onLoadFichaCliente_From_Server(Long.parseLong(msg.obj.toString())); 
				    val=true;
				    break;
			case LOAD_FACTURASCLIENTE_FROM_SERVER: 
				    onLoadFacturasCliente_From_Server(msg); 
				    val=true;	    
					break;
		}
		return val;
	}
	
	private void onLoadALLData_From_LocalHost()
	{		
		try 
		{   
			getPool().execute
			(
				new Runnable()
				{
	
					@Override
					public void run() 
					{ 						
						try
						{ 													
							Processor.send_ViewCustomerToView((ModelCliente.getArrayCustomerFromLocalHost(getResolver())), getController());							
						}
						catch (Exception e) 
						{
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno en la sincronización con la BDD",e.toString(),"\n Causa: "+e.getCause()));
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
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno en la sincronización con la BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}  
		} 
		
	}
	 
	private void onLoadALLData_From_Server()
	{ 
		try
		{ 
				getPool().execute(  new Runnable()
	            {
	            	@Override
					public void run() 
					{
						try 
						{
							
							final String credentials=SessionManager.getCredenciales();			  
							if(credentials.trim()=="")
							   return;
							if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController() ) )
						    {
								Integer page=1;
								String userName = ModelConfiguracion.getVMConfiguration(getContext()).getNameUser();
							    while(true)
								{ 					    	
							       JSONArray modelcliente=ModelCliente.getArrayCustomerFromServer2(
							    		   credentials,
							    		   userName,
							    		   page,
							    		   50); 
								   if(modelcliente.length()!=0)
								   {    
									   ModelCliente.saveClientes(modelcliente,getContext(),page);
									   ja_clientes.toJSONObject(modelcliente);
									   Processor.notifyToView(getController(),C_UPDATE_IN_PROGRESS, 0, 0,"Sincronizando Clientes \npágina:"+page.toString()+" ...");
								   	   page++;
								   } 
								   else break;			  
								} 
							    
							     Processor.notifyToView(getController(),C_UPDATE_FINISHED,0,1,"Los clientes han sido sincronizados exitosamente");
								 					
							}
		  						 				 
						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
							} catch (Exception e1) { 
								e1.printStackTrace();
							} 
						} 
					}
	            }); 
				Processor.notifyToView(getController(),C_UPDATE_STARTED, 0, 1,"Sincronizando Clientes"); 
		}     
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static Cliente getClienteBySucursalID(ContentResolver content,long objSucursalID)throws Exception{
		 return ModelCliente.getClienteBySucursalID(content,objSucursalID,0);
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

	
	private void onUpdateItem_From_Server(final Long sucursalID) 
	{ 
		obj.clear();
		try 
		{		  
			
			this.getPool().execute
			(  
				new Runnable()
	            { 
					@Override
					public void run() 
					{
						try 
						{      	 
							final String credentials=SessionManager.getCredenciales();			  
							if(credentials.trim()=="")
							   return;
							JSONObject modelcliente = null;
							if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController()))
						    {
								modelcliente =ModelCliente.actualizarCliente(credentials,sucursalID);
								if(modelcliente!=null)  
								{
									ModelCliente.actualizarClienteLocalmente(modelcliente,getContext());	
									Processor.notifyToView(getController(),C_UPDATE_ITEM_FINISHED,0,0,"sincronización exitosa");
								}
							}
							
						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error en la sincronización con el servidor",e.toString(),"\nCausa: "+e.getCause()));
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
			
			getPool().execute
			(  
				new Runnable()
	            { 
					@Override
					public void run() 
					{
						try 
						{   
							final String credentials=SessionManager.getCredentials();			  
							if(credentials.trim()=="")
							   return;				
							if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController()))								
								Processor.send_ViewFichaCustomerToView(ModelCliente.GetFichaCustomerFromServer(credentials,sucursalID),getController());

						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error en la sincronización con el servidor",e.toString(),"\nCausa: "+e.getCause()));
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
 
	private void onLoadFacturasCliente_From_Server(final Message msg) 
	{  
		try 
		{		 
			
			getPool().execute
			(  
				new Runnable()
	            { 
					@Override
					public void run() 
					{
						try 
						{   
							final String credentials=SessionManager.getCredentials();			  
							if(credentials.trim()=="")
							   return;
							if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController()))
							{
								Parameters params = new Parameters(
										 (new String[]{"Credentials","idSucursal","fechaInic","fechaFin","mostrarTodasSucursales","soloConSaldo","codEstado"}),
										 (new Object[]{credentials,
												       msg.getData().getLong("idSucursal"),
												       msg.getData().getLong("fechaInic"),
												       msg.getData().getLong("fechaFin"),
												       msg.getData().getBoolean("mostrarTodasSucursales"),
												       msg.getData().getBoolean("soloConSaldo"),
												       msg.getData().getBoolean("codEstado")}),
										 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.BOOLEAN_CLASS,
												 PropertyInfo.BOOLEAN_CLASS,PropertyInfo.STRING_CLASS})); 
									
									Processor.send_FacturasToViewCuentasPorCobrar(ModelCliente.getFacturasClienteFromServer(params),getController());
							} 
						}  
						catch (Exception e) 
				        { 
							e.printStackTrace();
							try {
								Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error en la sincronización con el servidor",e.toString(),"\nCausa: "+e.getCause()));
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
