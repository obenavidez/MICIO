package com.panzyma.nm.CBridgeM; 



import java.util.ArrayList;

import org.json.JSONArray;

import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import android.content.ContentResolver;
import android.os.Message;
import android.util.Log;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER; 
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;   
import com.panzyma.nm.model.ModelProducto; 
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ProductoView;
import com.panzyma.nm.view.ViewProducto;
import com.panzyma.nm.viewdialog.DialogProducto;

@SuppressWarnings("rawtypes")
public class BProductoM {
 
	Controller controller; 
    ThreadPool pool;
    ViewProducto view;
    ProductoView view2;
	String TAG=BClienteM.class.getSimpleName();
	boolean OK=false; 
	ArrayList<Producto> obj=new ArrayList<Producto>();
	JSONArray jsonA=new JSONArray();
	private DialogProducto view1; 
	public BProductoM(){}
	
	
	public BProductoM(ViewProducto view)
	{
    	this.controller=((NMApp)view.getApplicationContext()).getController();  
    	this.view=view; 
    	this.pool=((NMApp)view.getApplicationContext()).getThreadPool();
    }  
	
	
	public BProductoM(ProductoView view2)
	{
    	this.controller=((NMApp)view2.getApplicationContext()).getController();  
    	this.view2=view2; 
    	this.pool=((NMApp)view2.getApplicationContext()).getThreadPool();
    } 
	
	public BProductoM(DialogProducto view)
	{
    	this.controller=((NMApp)view.getContext().getApplicationContext()).getController();  
    	this.view1=view;
    	this.pool =((NMApp)view.getContext().getApplicationContext()).getThreadPool();
    }  
	
	public boolean handleMessage(Message msg) throws Exception 
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
							 
							Processor.send_ViewProductoToView(ModelProducto.getArrayCustomerFromLocalHost((view2!=null)?view2.getContentResolver():view1.getContext().getContentResolver()),controller);						
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

	private void onLoadALLData_From_Server() throws Exception
	{
		
		try {
			this.pool.execute(  new Runnable()
			{
				@Override
				public void run() 
				{
					try 
					{
						
						if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
						{
								/*Integer page=1;
							    while(true)
								{ 
							       ArrayList<Producto> modelproducto=ModelProducto.getArrayProductoFromServer("sa||nordis09||dp","areyes",page,50); 
								   if(modelproducto.size()!=0)							      
								   {	 
									   obj.addAll(modelproducto); 
									   Processor.builAndsend_ViewProductoToView(modelproducto, controller);
								       page++;
								   }
								   else  
									   break; 	 
								} 
							    onSave_From_LocalHost(obj);
							    Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,null);*/
							
							Integer page=1;
						    while(true)
							{ 
						       JSONArray  modelproducto=ModelProducto.getArrayProductoFromServer("sa-nordis09-dp","areyes",page,50); 
						       if(modelproducto.length()!=0)							      
							   {	 
						    	   jsonA.put(modelproducto);
						    	   ModelProducto.saveProductos(modelproducto, view,page);
								   //obj.addAll(modelproducto); 
								   Processor.builAndsend_ViewProductoToView(modelproducto, controller);
							       page++;
							   }
							   else  
								   break; 	 
							} 
						   // ModelProducto.saveProductos(jsonA, view); 
						   // onSave_From_LocalHost(jsonA);
						    Processor.notifyToView(controller,C_UPDATE_FINISHED,0,0,null);
							
						}
						
					}
					catch (Exception e) 
			        { 
						e.printStackTrace();
						try {
							Processor.notifyToView(controller,ERROR,0,0,new ErrorMessage("Error en la sincronización con el servidor",e.toString(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) { 
							e1.printStackTrace();
						} 
				    } 
				}
				
				
			});
			Processor.notifyToView(controller,C_UPDATE_STARTED, 0, 0, null);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		
	}

	private void onUpdateItem_From_Server()
	{
		
	}
	 
	
	private void onSave_From_LocalHost(final ArrayList<Producto> objL)
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
					//	ModelProducto.saveProductos(objL, view); 						
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
	
	private void onSave_From_LocalHost(final JSONArray objL)
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
					     ModelProducto.saveProductos(objL, view); 						
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
	
	public static Producto getProductoByID(ContentResolver content,long idproducto)throws Exception{
		 return ModelProducto.getProductoByID(content,idproducto);
	}


}
