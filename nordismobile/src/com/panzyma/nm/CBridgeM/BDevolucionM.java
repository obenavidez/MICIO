package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
 


















import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol; 
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelDevolucion;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelPedido; 
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.viewmodel.vmDevolucion;
import com.panzyma.nm.viewmodel.vmDevolucionEdit;

import android.os.Message;
import android.util.Log;

@SuppressWarnings("unchecked")
public class BDevolucionM extends BBaseM
{
	private static String TAG=BDevolucionM.class.getSimpleName();
	
	@Override
	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) 
		{
			case ControllerProtocol.OBTENERVALORCATALOGO:
				ObtenerValorCatalogo((String[]) msg.obj);
				break;
			case ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO:
				BuscarDevolucionDePedido(msg.obj);
				break;
			case ControllerProtocol.LOAD_PEDIDOS_FROM_LOCALHOST:
					ObtenerPedidosLocalmente( Long.valueOf(msg.getData().get("objSucursalID").toString()));
				break;			
			case LOAD_DATA_FROM_LOCALHOST:
				onLoadALLData_From_LocalHost();
				break;
			case ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST:
				loadItem(Long.valueOf((msg.obj==null)?""+0l:msg.obj.toString()));
				break;
			case ControllerProtocol.SAVE_DATA_FROM_LOCALHOST:
				Devolucion dev=(Devolucion) msg.obj;
				RegistrarDevolucion(dev);
				break;
			case ControllerProtocol.ENVIARDEVOLUCION:
				Devolucion $dev=(Devolucion) msg.obj;
				EnviarDevolucion($dev);
				break;
			default:
				break;
		}
		return true;
	}

	private void ObtenerValorCatalogo(String...valores)
	{
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.OBTENERVALORCATALOGO,
					0,
					0,
					ModelLogic.getValorCatalogo(valores)
					);
		} catch (Exception e) 
		{ 
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		} 
	}
	
	private void ObtenerValorCatalogo(String codigo,String valores)
	{
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.OBTENERVALORCATALOGO,
					0,
					0,
					ModelLogic.getValorByCatalogo(codigo,valores)
					);
		} catch (Exception e) 
		{ 
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		} 
	}
	
	
	private void ObtenerPedidosLocalmente(long objSucursalID)
	{
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.ID_REQUEST_OBTENERPEDIDOS,
					0,
					0,
					ModelPedido.obtenerPedidosFacturados(new long[]{objSucursalID})
					);
		} catch (Exception e) 
		{ 
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		} 
	}
    
    private void BuscarDevolucionDePedido(Object obj) 
    {
		HashMap<String, Long> parametros = (HashMap<String, Long>) obj;
		long idsucursal, nopedido, nofactura;
		idsucursal = parametros.get("idsucursal");
		nopedido = parametros.get("nopedido");
		nofactura = parametros.get("nofactura");

		String credenciales = "";
		Devolucion dev;
		Pedido pedido;
		credenciales = SessionManager.getCredentials();
		if (credenciales == "")
			return;
		try 
		{
			dev = ModelDevolucion.BuscarDevolucionDePedido(credenciales,
					idsucursal, nopedido, nofactura);
			pedido = obtenerPedido(dev.getObjPedidoDevueltoID());
			if (pedido != null && pedido.getId()!=0)
				dev.setObjPedido(pedido);
			else
				Processor.notifyToView(getController(),
						ControllerProtocol.NOTIFICATION, 0, 0,"No se encontro pedido alguno...");
			Processor.notifyToView(getController(),
					ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO, 0, 0, dev);

		} catch (Exception e) {
			Log.e(TAG, "Error interno trayendo datos desde el servidor", e);
			try {
				Processor
						.notifyToView(
								getController(),
								ERROR,
								0,
								0,
								new ErrorMessage(
										"Error interno trayendo datos desde el servidor",
										""
												+ ((e != null && e.toString() != null) ? e
														.toString() : ""),
										"\n Causa: "
												+ ""
												+ ((e != null && e.getCause() != null) ? e
														.getCause() : "")));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}
 
    public static long CalcMontoPromocionDevolucion(Devolucion dev)
    {
    	String credenciales="";
    	long value=0;
    	try 
    	{ 
    		credenciales = SessionManager.getCredentials();
    		if (credenciales == "")
    			return 0;    		
			value=ModelDevolucion.CalcMontoPromocionDevolucion(credenciales, dev);    		
    		
		} catch (Exception e) 
		{ 
		}
    	return value;
    }
    
    public static String EnviarDevolucion(Devolucion dev)
    {
    	String credenciales="";
    	String value="";
    	try 
    	{ 
    		credenciales = SessionManager.getCredentials();
    		if (credenciales == "")
    			return "";    		
    		guardarDevolucion(dev);
    		if(dev.getReferencia()!=0)
			value=ModelDevolucion.enviarDevolucion(credenciales, dev); 
		} catch (Exception e) 
		{ 
			try {
				Processor.notifyToView(NMApp.getController(),ERROR,0,0,new ErrorMessage("Error interno en la sincronización con la BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}  
		}
    	return value;
    }
    
	private Pedido obtenerPedido(final long idPedido) throws Exception {
		return ModelPedido.getPedido(SessionManager.getCredentials(),
				idPedido);
	}

    private void onLoadALLData_From_LocalHost()
	{		
		try 
		{ 
			getPool().execute(new Runnable(){
			
					@Override
					public void run() {
						try 
						{ 
							Processor.send_ViewDevolucionesToView(ModelDevolucion.obtenerDevolucionesFromLocalHost(getResolver())/*lista*/, getController());	
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
			});
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
    
    public synchronized static void getDevolucionbyID(long devolucionid)
    {
    	try 
		{ 
			Processor.send_ViewObjectToView(ModelDevolucion.getDevolucionbyID(devolucionid));	
		}
		catch (Exception e) 
		{
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(NMApp.getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		}
    }

    public synchronized static void loadItem(long devolucionid)
    {
    	try 
		{ 
    		Devolucion dev=ModelDevolucion.getDevolucionbyID(devolucionid);
    		List<Catalogo> cats=ModelLogic.getValorCatalogo(new String[] { "MotivoDevolucionNoVencidos" });
    		Cliente cliente =ModelCliente.getClienteBySucursalID(NMApp.getContext().getContentResolver(),dev.getObjSucursalID(),0);
    		Pedido pedido=ModelPedido.obtenerPedidoByID(dev.getObjPedidoDevueltoID(),NMApp.getContext().getContentResolver());
    		dev.setObjPedido(pedido);
			Processor.send_ViewObjectToView(ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST,new vmDevolucionEdit(dev,cats,cliente));	
		}
		catch (Exception e) 
		{
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(NMApp.getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		}
    }
    
    public static Devolucion guardarDevolucion(Devolucion dev) throws Exception{
    	Integer prefijo=Ventas.getPrefijoIds(NMApp.getContext());
    	Integer devolucionmax=dev.isDeVencido()?Ventas.getMaxDevolucionVId(NMApp.getContext()):Ventas.getMaxDevolucionNVId(NMApp.getContext());
    	
    	  //Generar Id del devolución
        if (dev.getReferencia() == 0) 
        {                     
            if (devolucionmax == null) 
            	devolucionmax = Integer.valueOf(1);
            else
            	devolucionmax =devolucionmax+1; 
            String strIdMovil = prefijo.intValue() + "" + devolucionmax.intValue();
            int idMovil = Integer.parseInt(strIdMovil);
            dev.setId(0);
            dev.setReferencia(idMovil);
            dev.setCodEstado("REGISTRADA"); 
            dev.setNumeroCentral(0); 
        }    	
		 DatabaseProvider.RegistrarDevolucion(dev, NMApp.getContext());
		 ModelConfiguracion.ActualizarSecuenciaDevolucion(devolucionmax, dev.isDeVencido());
		 if(dev.getObjPedido()!=null && dev.getObjPedido().getId()!=0)
			 ModelPedido.RegistrarPedido(dev.getObjPedido(), NMApp.getContext());
		 
		 return dev;
    }
    
    private  void RegistrarDevolucion(final Devolucion dev) throws Exception
    {
			try 
			{
				getPool().execute(new Runnable() 
				{
					@Override
					public void run() 
					{
						
						try 
						{
							
					    	Integer prefijo=Ventas.getPrefijoIds(NMApp.getContext());
					    	Integer devolucionmax=dev.isDeVencido()?Ventas.getMaxDevolucionVId(NMApp.getContext()):Ventas.getMaxDevolucionNVId(NMApp.getContext());
					    	
					    	  //Generar Id del devolución
					        if (dev.getReferencia() == 0) 
					        {                     
					            if (devolucionmax == null) 
					            	devolucionmax = Integer.valueOf(1);
					            else
					            	devolucionmax =devolucionmax+1; 
					            String strIdMovil = prefijo.intValue() + "" + devolucionmax.intValue();
					            int idMovil = Integer.parseInt(strIdMovil);
					            dev.setId(0);
					            dev.setReferencia(idMovil);
					            dev.setCodEstado("REGISTRADA"); 
					            dev.setNumeroCentral(0); 
					        }    	
							 DatabaseProvider.RegistrarDevolucion(dev, NMApp.getContext());
							 ModelConfiguracion.ActualizarSecuenciaDevolucion(devolucionmax, dev.isDeVencido());
							 if(dev.getObjPedido()!=null && dev.getObjPedido().getId()!=0)
								 ModelPedido.RegistrarPedido(dev.getObjPedido(), NMApp.getContext());
							  			        
					        Processor.notifyToView(
					        		NMApp.getController(),
									ControllerProtocol.NOTIFICATION,
									ControllerProtocol.AFTERSAVE_DATA_FROM_LOCALHOST,
									0,
									dev);
							
						} catch (Exception e) {
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor
										.notifyToView(
												NMApp.getController(),
												ERROR,
												0,
												0,
												ErrorMessage.newInstance(
														"Error interno",
														e.getMessage(), "\n Causa: "
																+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						
						
					}
				});
				
				 Processor.notifyToView(
						 NMApp.getController(),
							ControllerProtocol.NOTIFICATION_DIALOG2,
							0,
							0,new String("Guardando devolución localmente."));
				
			} catch (Exception e) 
			{ 
				e.printStackTrace();
			} 
		   
    }
    
 }

