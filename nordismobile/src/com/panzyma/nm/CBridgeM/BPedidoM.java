package com.panzyma.nm.CBridgeM; 

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_INVENTORY_FROM_SERVER;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;   
import com.panzyma.nm.model.ModelPedido;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewPedidoEdit; 
import com.panzyma.nm.viewdialog.DialogProducto;
@SuppressWarnings("rawtypes")
public class BPedidoM {
 
	Controller controller; 
    ThreadPool pool;
    ViewPedidoEdit view;
    DialogProducto view1;
	String TAG=BClienteM.class.getSimpleName();
	boolean OK=false; 
	ArrayList<Pedido> obj=new ArrayList<Pedido>();
	
	public BPedidoM(){}
	
	
	public BPedidoM(ViewPedidoEdit view)
	{
    	this.controller=((NMApp)view.getApplication()).getController();  
    	this.view=view;
    	this.pool =((NMApp)view.getApplicationContext()).getThreadPool();
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
			case UPDATE_INVENTORY_FROM_SERVER:
					onUpdateInventory_From_Server();
					return true;
					
		}
		return false;
	}
	
	private void onLoadALLData_From_LocalHost()
	{
		
	}
	
	private void onLoadALLData_From_Server()
	{
		
		 
	}
	
	private void onUpdateItem_From_Server()
	{
		
	}
	
	private void onUpdateInventory_From_Server()
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
							SoapObject DisponibilidadProducto=(SoapObject)ModelPedido.onUpdateInventory_From_Server("sa||nordis09||dp","areyes",true);
							if(DisponibilidadProducto!=null) 
								ModelProducto.UpdateProducto(view.getContentResolver(),DisponibilidadProducto); 						
							
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
 
	public static Producto getProductoByID(ContentResolver content,long idproducto)throws Exception{
		 return ModelProducto.getProductoByID(content,idproducto);
	}


}
