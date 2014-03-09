package com.panzyma.nm.CBridgeM; 

import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;

import java.util.ArrayList;

import android.os.Message;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;   
import com.panzyma.nm.serviceproxy.Pedido;
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
}
