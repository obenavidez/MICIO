package com.panzyma.nm.CBridgeM;

import org.json.JSONArray;

import static com.panzyma.nm.controller.ControllerProtocol.*;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;

import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.CProducto;

@SuppressWarnings("rawtypes")
public class BProductoM extends BBaseM {
	
	String TAG = BProductoM.class.getSimpleName();
	JSONArray jsonA = new JSONArray();
	
	@Override
	public boolean handleMessage(Message msg)  
	{
		Bundle b = msg.getData();
		Boolean val=false;
		switch (msg.what) 
		{
			case LOAD_ITEM_FROM_LOCALHOST:				
				getProductoByID(b.getLong("idProducto"));
				return true;
			case LOAD_FICHAPRODUCTO_FROM_SERVER: 
				getFichaProductoByID(b.getLong("idProducto"));
				return true;
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
		return val;
	}
	
	private void onLoadALLData_From_LocalHost(){
		try 
		{   
			getPool().execute(new Runnable(){
				@Override
				public void run() {
					try
					{ 													
						Processor.send_ViewProductosToView((ModelProducto.getArrayProductoFromLocalHost(getContext())), getController());
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
				
			});
			
		}
		catch (Exception e) 
        { 
			
        }
	}
	
	private void onLoadALLData_From_Server(){
		try
		{ 
		    final String credentials=SessionManager.getCredenciales();
			if(credentials.trim()!="")
			{
				getPool().execute(  new Runnable(){
					@Override
					public void run() {
						try 
						{
							if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController() ) )
						    {
								Integer page=1;
								String userName = ModelConfiguracion.getVMConfiguration(getContext()).getNameUser();
							    while(true)
								{ 					    	
							    	JSONArray modelproducto = ModelProducto.getArrayProductoFromServer( credentials,userName,page,50); 
									if (modelproducto.length() != 0) {
										jsonA.put(modelproducto);
										ModelProducto.saveProductos( modelproducto,getContext(), page);
										Processor.notifyToView(
												getController(),
												ControllerProtocol.C_UPDATE_IN_PROGRESS,
												0,
												0,"Sincronizando Productos \npágina:"+ page.toString());
										page++;
									} 
									 else
										 break;		
								}
								Processor.notifyToView(getController(),C_UPDATE_FINISHED,0,1,"Los Productos han sido sincronizados exitosamente");
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
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void onUpdateItem_From_Server(){
		
	}
	
	
	public void getProductoByID(final long idProducto)
	{		 
		try 
		{
			Processor.notifyToView(
					getController(),
					ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST,
					0,
					0,
					ModelProducto.getProductoByID(getContext().getContentResolver(),idProducto));
		}
		catch(Exception e)
		{
			try {
				Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void getFichaProductoByID(final long idProducto)
	{
		try 
		{
			getPool().execute(  new Runnable()
            {
				@Override
				public void run() {
					try 
					{
						CProducto producto= null;
						final String credentials=SessionManager.getCredentials();			  
						if(credentials.trim()=="")
							   return;	
						if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController())){
							producto =ModelProducto.getCProducto(credentials, idProducto);
							Processor.notifyToView(getController(),ID_SINCRONIZE_PRODUCTO,0,1,producto);
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
		}
		catch(Exception e)
		{
			try {
				Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		
		
		/*try 
		{
			Processor.notifyToView(
					getController(),
					ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST,
					0,
					0,
					ModelProducto.getProductoByID(getContext().getContentResolver(),idProducto));
		}
		catch(Exception e)
		{
			try {
				Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		*/
	}

	@SuppressWarnings("unused")
	private void onSave_From_LocalHost(final JSONArray objL) {
		try {
			getPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						ModelProducto.saveProductos(objL, getContext());
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
													"Error interno salva guardando datos en la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//
//	Controller controller;
//	ThreadPool pool;
//	private ProductoView view;
//	private DialogProducto view1;
//	private ViewProducto view2;
//	private ConsultaPrecioProducto view3;
//	private ConsultaBonificacionesProducto view4;
//	int view_activated;	
//
//	String TAG = BClienteM.class.getSimpleName();
//	boolean OK = false;
//	ArrayList<Producto> obj = new ArrayList<Producto>();
//	JSONArray jsonA = new JSONArray();
//
//	public BProductoM() {
//	}
//
//	public BProductoM(ProductoView view) {
//		this.controller = NMApp
//				.getController();
//		this.view = view;
//		this.pool = NMApp.getThreadPool();
//		view_activated=1;
//	}
//
//	public BProductoM(DialogProducto view) {
//		this.controller = NMApp
//				.getController();
//		this.view1 = view;
//		this.pool = NMApp
//				.getThreadPool();
//		view_activated=2;
//	}
//
//	public BProductoM(ConsultaPrecioProducto view) {
//		this.controller = NMApp
//				.getController();
//		this.view3 = view;
//		this.pool = NMApp
//				.getThreadPool();
//		view_activated=3;
//	}
//	
//	public BProductoM(ConsultaBonificacionesProducto view) {
//		this.controller =NMApp.getController();   //((NMApp) view.getParent().getApplicationContext()).getController();
//		this.view4 = view;
//		this.pool = NMApp.getThreadPool();  //((NMApp) view.getParent().getApplicationContext()).getThreadPool();
//		view_activated=4;
//	}
//
//	public boolean handleMessage(Message msg) throws Exception {
//		switch (msg.what) {
//		case LOAD_ITEM_FROM_LOCALHOST:
//			Bundle b = msg.getData();
//			getProductoByID(b.getLong("_idProducto"));
//			return true;
//		case LOAD_DATA_FROM_LOCALHOST:
//			onLoadALLData_From_LocalHost();
//			return true;
//		case LOAD_DATA_FROM_SERVER:
//			onLoadALLData_From_Server();
//			return true;
//		case UPDATE_ITEM_FROM_SERVER:
//			onUpdateItem_From_Server();
//			return true;
//
//		}
//		return false;
//	}
//
//	private void onLoadALLData_From_LocalHost() {
//		try {
//			pool.execute(new Runnable() {
//
//				@Override
//				public void run() {
//
//					try {
//
//						Processor.send_ViewProductosToView(
//								ModelProducto
//										.getArrayProductoFromLocalHost((view != null) ? view
//												.getContentResolver() : view1
//												.getContext()
//												.getContentResolver()),
//								controller);
//					} catch (Exception e) {
//						Log.e(TAG, "Error in the update thread", e);
//						try {
//							Processor
//									.notifyToView(
//											controller,
//											ERROR,
//											0,
//											0,
//											new ErrorMessage(
//													"Error interno en la sincronización con la BDD",
//													e.toString(), "\n Causa: "
//															+ e.getCause()));
//						} catch (Exception e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void onLoadALLData_From_Server(){
//		try
//		{ 
//		    final String credentials=SessionManager.getCredenciales();
//			  
//			if(credentials.trim()!="")
//			{
//				NMApp.getThreadPool().execute(  new Runnable()
//	            {
//	            	@Override
//					public void run() 
//					{
//	            		try 
//						{
//							if(NMNetWork.isPhoneConnected(view,controller) && NMNetWork.CheckConnection(controller))
//						    {
//								Integer page=1;
//								while (true) 
//								{
//									JSONArray modelproducto = ModelProducto.getArrayProductoFromServer(credentials,"areyes",page, 50);
//									if (modelproducto.length() != 0) {
//										jsonA.put(modelproducto);
//										ModelProducto.saveProductos(modelproducto,view, page);
//										Processor.notifyToView(
//												controller,
//												ControllerProtocol.C_UPDATE_IN_PROGRESS,
//												0,
//												0,"Sincronizando Productos \npágina:"+ page.toString());
//										page++;
//									} 
//									 else break;		
//								}
//								Processor.notifyToView(controller,C_UPDATE_FINISHED,0,1,"Los Productos han sido sincronizados exitosamente");
//						    }
//						}
//	            		catch (Exception e) 
//				        { 
//							e.printStackTrace();
//							try {
//								Processor.notifyToView(controller,ERROR,0,1,new ErrorMessage("Error en la sincronización de productos con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
//							} catch (Exception e1) { 
//								e1.printStackTrace();
//							} 
//						}
//	            		
//					}
//	            });
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	
////	private void onLoadALLData_From_Server() throws Exception {
////
////		try {
////			this.pool.execute(new Runnable() {
////				@Override
////				public void run() {
////					try {
////
////						if (NMNetWork.isPhoneConnected(view, controller)
////								&& NMNetWork.CheckConnection(controller)) {
////							/*
////							 * Integer page=1; while(true) { ArrayList<Producto>
////							 * modelproducto
////							 * =ModelProducto.getArrayProductoFromServer
////							 * ("sa||nordis09||dp","areyes",page,50);
////							 * if(modelproducto.size()!=0) {
////							 * obj.addAll(modelproducto);
////							 * Processor.builAndsend_ViewProductoToView
////							 * (modelproducto, controller); page++; } else
////							 * break; } onSave_From_LocalHost(obj);
////							 * Processor.notifyToView
////							 * (controller,C_UPDATE_FINISHED,0,0,null);
////							 */
////
////							Integer page = 1;
////							while (true) {
////								JSONArray modelproducto = ModelProducto
////										.getArrayProductoFromServer(
////												"sa-nordis09-dp", "areyes",
////												page, 50);
////								if (modelproducto.length() != 0) {
////									jsonA.put(modelproducto);
////									ModelProducto.saveProductos(modelproducto,
////											view, page);
////									// obj.addAll(modelproducto);
////									Processor.builAndsend_ViewProductoToView(
////											modelproducto, controller);
////									page++;
////								} else
////									break;
////							}
////							// ModelProducto.saveProductos(jsonA, view);
////							// onSave_From_LocalHost(jsonA);
////							Processor.notifyToView(controller,
////									C_UPDATE_FINISHED, 0, 0, null);
////
////						}
////
////					} catch (Exception e) {
////						e.printStackTrace();
////						try {
////							Processor
////									.notifyToView(
////											controller,
////											ERROR,
////											0,
////											0,
////											new ErrorMessage(
////													"Error en la sincronización con el servidor",
////													e.toString(), "\n Causa: "
////															+ e.getCause()));
////						} catch (Exception e1) {
////							e1.printStackTrace();
////						}
////					}
////				}
////
////			});
////			Processor.notifyToView(controller, C_UPDATE_STARTED, 0, 0, null);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////
////	}
//
//	private void onUpdateItem_From_Server() {
//
//	}
//
//	@SuppressWarnings("unused")
//	private void onSave_From_LocalHost(final ArrayList<Producto> objL) {
//		try 
//		{
//			this.pool.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						// ModelProducto.saveProductos(objL, view);
//					} catch (Exception e) {
//						e.printStackTrace();
//						try {
//							Processor
//									.notifyToView(
//											controller,
//											ERROR,
//											0,
//											0,
//											new ErrorMessage(
//													"Error interno salva guardando datos en la BDD",
//													e.toString(), "\n Causa: "
//															+ e.getCause()));
//						} catch (Exception e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@SuppressWarnings("unused")
//	private void onSave_From_LocalHost(final JSONArray objL) {
//		try {
//			this.pool.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						ModelProducto.saveProductos(objL, view);
//					} catch (Exception e) {
//						e.printStackTrace();
//						try {
//							Processor
//									.notifyToView(
//											controller,
//											ERROR,
//											0,
//											0,
//											new ErrorMessage(
//													"Error interno salva guardando datos en la BDD",
//													e.toString(), "\n Causa: "
//															+ e.getCause()));
//						} catch (Exception e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void getProductoByID(final long idProducto) throws Exception 
//	{		 
//		try 
//		{  	
//			ContentResolver content=null;
//			switch (view_activated) {
//			case 1:
//				content=view.getParent().getContentResolver();
//				break;
//			case 3 :
//				content=view3.getParent().getContentResolver();
//				break;
//			case 4:
//				content=view4.getParent().getContentResolver();
//				break;
//			default:
//				break;
//
//			}
//			
//			Processor.notifyToView(
//					controller,
//					ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST,
//					0,
//					0,
//					ModelProducto.getProductoByID(content,idProducto));
//		} catch (Exception e) 
//		{ 
//			e.printStackTrace();
//		}
// 
//	}

}
