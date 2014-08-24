package com.panzyma.nm.auxiliar;

import java.util.ArrayList; 

import org.json.JSONArray;
import org.json.JSONObject;

import static com.panzyma.nm.controller.ControllerProtocol.C_SETTING_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA; 
import static com.panzyma.nm.controller.ControllerProtocol.C_FICHACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.C_FACTURACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.ID_REQUEST_SALVARPEDIDO;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_ITEM_FINISHED;

import com.google.gson.Gson;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura; 
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nm.viewmodel.vmFicha;
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nm.viewmodel.vmRecibo;

 @SuppressWarnings({ "unchecked", "rawtypes" })
public class Processor {
	
    static Object lock=new Object();
    static String TAG=Processor.class.getSimpleName();
	public static void builAndsend_ViewCustomerToView(ArrayList<Cliente> objL,Controller controller)throws Exception
    {   
    	ArrayList<vmCliente> a_vaC=new ArrayList<vmCliente>();   
    	synchronized(lock)
    	{ 
			for(Cliente c:objL) 
				a_vaC.add(new vmCliente(c.getIdCliente(),c.getIdSucursal(),c.getNombreCliente()
						,c.getCodigo(),c.getUbicacion()));			  	
			controller.notifyOutboxHandlers(C_SETTING_DATA, 0, 0, (a_vaC.size()!=0)?a_vaC:new ArrayList<vmCliente>() );  
    	}  
        
    } 
	public static void send_ViewCustomerToView(ArrayList<vmCliente> objL,Controller controller)throws Exception
	{ 
		 synchronized(lock)
	     { 
			 controller.notifyOutboxHandlers(C_DATA, 0, 0, (objL.size()!=0)?objL:new ArrayList<vmCliente>() );
			// Thread.sleep(500); 
	     }  
	}
	public static void send_ViewProductoToView(ArrayList<vmProducto> objL,Controller controller)throws Exception
	{ 
		 synchronized(lock)
	     { 
			 controller.notifyOutboxHandlers(C_DATA, 0, 0, (objL.size()!=0)?objL:new ArrayList<vmProducto>() );
			// Thread.sleep(500); 
	     }  
	} 
	public static void send_ViewProductosToView(ArrayList<Producto> objL,Controller controller)throws Exception
	{ 
		 synchronized(lock)
	     { 
			 controller.notifyOutboxHandlers(C_DATA, 0, 0, (objL.size()!=0)?objL:new ArrayList<vmProducto>() );
			// Thread.sleep(500); 
	     }  
	} 
	public static void send_ViewFichaCustomerToView(CCCliente objL,Controller controller)throws Exception
	{
		synchronized(lock)
	     { 
			 controller._notifyOutboxHandlers(C_FICHACLIENTE, 0, 0,objL);
			 Thread.sleep(500); 
	     }  
	}
	
	public static void send_ViewFichaCustomerToView(vmFicha objL,Controller controller)throws Exception
	{
		synchronized(lock)
	     { 
			 controller._notifyOutboxHandlers(C_FICHACLIENTE, 0, 0,objL);
			 Thread.sleep(500); 
	     }  
	}
	
	public static void send_FacturasToViewCuentasPorCobrar(ArrayList<Factura> objL,Controller controller)throws Exception
	{
		synchronized(lock)
	     { 
			 controller._notifyOutboxHandlers(C_FACTURACLIENTE, 0, 0,objL);
			 Thread.sleep(500); 
	     }  
	}
	
	public static void notifyToView(Controller controller,int what,int arg1,int arg2,Object obj)throws Exception
	{
		synchronized(lock)
	     {
			controller._notifyOutboxHandlers(what,arg1, arg2,obj); 
	     } 
    }
	
	public static void send_DataSourceToView(Object obj,Controller controller)throws Exception
	{
		synchronized(lock)
	     {
			controller._notifyOutboxHandlers(C_DATA,0, 0,obj); 
	     } 
    }
	/**
	 * 
	 * */
	
	public static void builAndsend_ViewProductoToView(ArrayList<Producto> objL,Controller controller)throws Exception
    {
	  synchronized(lock)
	    {
			ArrayList<vmProducto> a_vmP=new ArrayList<vmProducto>();  
	        for(Producto p:objL) 
	               a_vmP.add(new vmProducto(p.getId(),p.getCodigo(),p.getNombre(),p.getDisponible())); 
 	 	
			controller.notifyOutboxHandlers(C_SETTING_DATA, 0, 0, (a_vmP.size()!=0)?a_vmP:new ArrayList<vmProducto>() ); 
 
		} 
    }
	public static void builAndsend_ViewProductoToView(JSONArray objL,Controller controller)throws Exception
    {
	  synchronized(lock)
	    {
		    Producto producto=new Producto();
		    ArrayList<vmProducto> a_vmP=new ArrayList<vmProducto>();
			for(int i = 0;i<objL.length();i++)
			{
				JSONObject json=(JSONObject) objL.get(i);
				producto=(new Gson().fromJson(json.toString(), Producto.class));
				a_vmP.add(new vmProducto(producto.getId(),producto.getCodigo(),producto.getNombre(),producto.getDisponible()));
			}
		  
			controller.notifyOutboxHandlers(C_SETTING_DATA, 0, 0, (a_vmP.size()!=0)?a_vmP:new ArrayList<vmProducto>() ); 
 
		} 
    }
	
	public static void send_ViewReciboToView(
			ArrayList<vmRecibo> objL,
			Controller controller) {
		synchronized (lock) {
			controller.notifyOutboxHandlers(C_DATA, 0, 0, (objL.size() != 0) ? objL : new ArrayList<vmProducto>());
			// Thread.sleep(500);
		}
	}

	public static void send_ViewPedidoToView(int ID,Controller controller,Object obj) 
	{
		synchronized (lock) {
			controller.notifyOutboxHandlers(ID, 0, 0, obj); 
		}
	}

	public static void send_ViewPedidoToView(int rs,Controller controller) 
	{
		synchronized (lock) {
			controller._notifyOutboxHandlers(ID_REQUEST_SALVARPEDIDO, 0, 0, rs); 
		}
	}

	public static void send_ViewDeleteReciboToView(
			Integer result,
			Controller controller) {
		synchronized (lock) {
			controller._notifyOutboxHandlers(DELETE_ITEM_FINISHED, 0, 0, result);
			// Thread.sleep(500);

		}
	}
	public static void send_ViewDeletePedidoToView(Integer result,Controller controller) {
		synchronized (lock) {
			controller._notifyOutboxHandlers(DELETE_ITEM_FINISHED, 0, 0, result);
			// Thread.sleep(500);
		}
	}
	public static void send_ViewReciboEditToView(
			ReciboColector result,
			Controller controller) {
		synchronized (lock) {
			controller._notifyOutboxHandlers(C_DATA, 0, 0, result);
			// Thread.sleep(500);

		}
	}
	
	public static void send_ViewReciboEditToView(
			Cliente cliente,
			Controller controller) {
		synchronized (lock) {
			controller._notifyOutboxHandlers(C_DATA, 0, 0, cliente );
			// Thread.sleep(500);
		}

	}
	
	public static void sendViewReciboEditToView(){
		
	}
	
	
}