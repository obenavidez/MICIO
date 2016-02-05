package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NumberUtil;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.bluetooth.BluetoothConnection;
import com.panzyma.nm.controller.ControllerProtocol; 
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelDevolucion;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelPedido; 
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.serviceproxy.RespuestaEnviarRecibo;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.viewmodel.vmDevolucion;
import com.panzyma.nm.viewmodel.vmDevolucionEdit;
import android.annotation.SuppressLint;
import android.os.Bundle; 
import android.os.Message;
import android.util.Log;

@SuppressWarnings("unchecked")
public class BDevolucionM extends BBaseM
{
	private static String TAG=BDevolucionM.class.getSimpleName();
	
	@Override
	public boolean handleMessage(Message msg) throws Exception {
		Bundle b = msg.getData();
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
			case ControllerProtocol.SALVARRECIBOANTESDESALIR:
				Devolucion dev2 = (Devolucion) msg.obj;
				RegistrarDevolucion(dev2, ControllerProtocol.SALVARDEVOLUCIONANTESDESALIR);
				break;
			case ControllerProtocol.ENVIARDEVOLUCION:
				Devolucion $dev=(Devolucion) msg.obj;
				EnviarDevolucion($dev);
				break;
			case ControllerProtocol.DELETE_DATA_FROM_LOCALHOST:
				Integer id = msg.getData().getInt("id");	
				deleteDevolucion(id);
			case  ControllerProtocol.IMPRIMIR:
				if(msg.obj instanceof Devolucion)
					ImprimirDevolucion((Devolucion)msg.obj, true);
			default:
				break;
		}
		return true;
	}
	
	private void deleteDevolucion(Integer id)
	{
		try 
		{
			ModelDevolucion.borrarDevolucion(id);
			Processor.notifyToView(getController(),
					ControllerProtocol.C_DATA,
					-1,
					-1,
					ModelDevolucion.obtenerDevolucionesFromLocalHost(getResolver())
					);
		} catch (Exception e) 
		{ 
			Log.e(TAG, "Error interno al borrar devolución", e);
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno al borrar devolución",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				Log.e(TAG, "Error interno al borrar devolución", e);
			}
		} 
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
    
    public static void getObservacionesDevolucion(Devolucion dev) throws Exception
    {
    	 
    	
		try 
		{
			String credenciales="";
			credenciales=SessionManager.getCredentials();						
			if(credenciales!="")
			{
				 Processor.notifyToView(NMApp.getController(),ControllerProtocol.NOTIFICATION_DIALOG2,
							0,0,"Validando devolución con el servidor central.");
				if (!SessionManager.isPhoneConnected()) 
	            {
					Processor.notifyToView(NMApp.getController(),ControllerProtocol.AFTERGETOBSERVACIONDEV,1,0,"La devolución no será enviada por falta de cobertura\r\n, esta será impresa y quedará pendiente de enviarse."); 
	                return;
	            }
				String respuesta=ModelDevolucion.getObservacionesDevolucion(credenciales, dev); 
				Processor.notifyToView(NMApp.getController(),ControllerProtocol.AFTERGETOBSERVACIONDEV,1,0,"".equals(respuesta)?"Desea Imprimir el comprobante de devolución?":respuesta); 

				 
			}else
				Processor.notifyToView(NMApp.getController(),0,0,0,null);
			
		} catch (Exception e) {
			Processor.notifyToView(NMApp.getController(),ERROR,0,0,
					new ErrorMessage(
							          "Error en el Modulo Devolución.",
							          "Error en el proceso de obtener observaciones de la devolución", "\nCausa: "
									  + e.getMessage()
									 )
			      );
		}					
				
	}
    
    public static void EnviarDevolucion(Devolucion dev) throws Exception
    {
    	
    	boolean imprimir=false;
    	boolean pagarOnLine=false;
    	
		try 
		{  						
				String credenciales="";
				credenciales=SessionManager.getCredentials();						
				if(credenciales!="")
				{							 
			        imprimir = true;
			        pagarOnLine = true;
			        //Si se está fuera de covertura, salir        
			        if (dev.getCodEstado().compareTo("PAGADO_OFFLINE") == 0) 
			        {
			        	imprimir = false;
			            if (!SessionManager.isPhoneConnected()) 
			            {
			            	Processor.notifyToView(NMApp.getController(),ERROR,0,0,
									new ErrorMessage(
											          "Error en el Modulo Devolución.",
											          "Error en el proceso de envio de la devolución", "\nCausa: "
													  + "Falta de covertura."
													 )
							      );  
			                return ;
			            }
			        	 
			        } 
			        else 
			        {                
			            if (!SessionManager.isPhoneConnected()) 
			            {	
			            	pagarOnLine = false;
			            	Processor.notifyToView(NMApp.getController(),ControllerProtocol.NOTIFICATION_DIALOG2,0,0,"La devolución no será enviada por falta de cobertura\r\n, esta será impresa y quedará pendiente de enviarse.");
			            }
			        } 
			       
					//Guardando cambios en el Dispositivo   
			        
			        guardarDevolucion(dev);
			        
			        if (pagarOnLine) 
					{
						
						 Processor.notifyToView(NMApp.getController(),ControllerProtocol.NOTIFICATION_DIALOG2,
							0,0,"Enviando devolución al servidor central");
						 
							 
						String respuesta=ModelDevolucion.enviarDevolucion(credenciales, dev); 
						  
						if (respuesta == null) 
						{
							Processor.notifyToView(NMApp.getController(),ERROR,0,0,
									new ErrorMessage(
											          "Error en el Modulo Devolución.",
											          "Error en el proceso de envio de la devolución", "\nCausa: "
													  + respuesta
													 )
							      ); 
						}
						else if (!respuesta.contains("ID:")) 
		                {
						
							Processor.notifyToView(NMApp.getController(),ERROR,0,0,
									new ErrorMessage(
											          "Error en el Modulo Devolución.",
											          "La devolución fue rechazada por el servidor", "\nCausa: "
													  + respuesta
													 )
							      ); 
		                }
						
						String numerocentral=respuesta.split(":")[1];
						dev.setNumeroCentral(Integer.valueOf(""+numerocentral));
						//Guardando cambios en el Dispositivo   				        
				        guardarDevolucion(dev);
						 
		                //Salvar los cambios en el hilo pricipal
		                Processor.notifyToView(NMApp.getController(),ControllerProtocol.ID_REQUEST_ENVIAR,imprimir?1:0,0,dev);
					}
					else
					{								 
		               //Poner estado de recibo en PAGADO_OFFLINE                   
						dev.setCodEstado("PAGADO_OFFLINE");
						dev.setDescEstado("Registrado"); 	
						dev.setOffLine(true);
		               //Guardando cambios en el Dispositivo 
				        guardarDevolucion(dev); 
		                //enviar los cambios en el hilo pricipal
		                Processor.notifyToView(NMApp.getController(),ControllerProtocol.ID_REQUEST_ENVIAR,imprimir?1:0,0,dev);
					} 
					
				} 
				else
					Processor.notifyToView(NMApp.getController(),0,0,0,null);
		 
	 
	
		} catch (Exception e) 
		{ 
			Processor.notifyToView(NMApp.getController(),ERROR,0,0,
					new ErrorMessage(
							          "Error en el Modulo Devolución.",
							          "Error en el proceso de envio de la devolución", "\nCausa: "
									  + e.getMessage()
									 )
			      );
		} 
    }
    
    @SuppressLint("UseValueOf") 
	public  static void ImprimirDevolucion(long iddevolucion,boolean reimpresion) 
	{   
		try
		{
			Devolucion dev=ModelDevolucion.getDevolucionbyID(iddevolucion);  
			
    		String devolucion = ""; 
    		devolucion += "T 7 1 123 5 Distribuidora Panzyma - DISPAN\r\n";
    		devolucion += "T 7 0 123 54 Comprobante de Devoluci\u00f3n\r\n";
    		devolucion += "LINE 0 80 576 80 1\r\n";
    		
    		devolucion += "T 7 0 0 90 Tipo Devolucion:\r\n";    		
    		devolucion += "T 7 0 40 120 Vencido\r\n";
    		devolucion += "T 7 0 210 118 "+(dev.isDeVencido()?"X":"")+"\r\n";     		
    		devolucion += "LINE 205 140 225 140 1\r\n";    		
    		devolucion += "T 7 0 40 150 No Vencido\r\n";
    		devolucion += "T 7 0 210 148 "+(dev.isDeVencido()?"":"X")+"\r\n";    		 
    		devolucion += "LINE 205 170 225 170 1\r\n";
    		
    		devolucion += "T 7 0 350 90 Fecha:\n"; 
    		devolucion += "T 7 0 440 90 " + DateUtil.idateToStr(dev.getFecha()) + "\r\n";
    		
    		devolucion += "T 7 0 350 120 Numero:\n"; 
    		devolucion += "T 7 0 440 120 " + NumberUtil.getFormatoNumero(dev.getReferencia(),NMApp.getContext()) + "\r\n";
    		
    		devolucion += "T 7 0 350 150 Pedido:\n"; 
    		devolucion += "T 7 0 440 150 " + dev.getNumeroPedidoDevuelto() + "\r\n";    		
    		
    		devolucion += "T 7 0 0 180 Tipo Tramite:\r\n";    		
    		devolucion += "T 7 0 40 210 Reposicion\r\n";
    		devolucion += "T 7 0 210 208 "+("RE".equals(dev.getTipoTramite())?"X":"")+"\r\n";    		
    		devolucion += "LINE 205 230 225 230 1\r\n";    		
    		devolucion += "T 7 0 40 240 Nota Credito\r\n";
    		devolucion += "T 7 0 210 238 "+("NC".equals(dev.getTipoTramite())?"X":"")+"\r\n"; 
    		devolucion += "LINE 205 260 225 260 1\r\n";
    		
    		
    		
    		devolucion += "T 7 0 0 270 Motivo:\r\n";
    		devolucion += "T 7 0 170 270"+dev.getDescMotivo()+"\r\n";
    		

    		devolucion += "T 7 0 0 300 Cliente:\r\n";
    		devolucion += "T 7 0 170 300"+dev.getNombreCliente()+"\r\n";
    		
    		devolucion += "T 7 0 0 330 Vendedor:\r\n";
    		devolucion += "T 7 0 170 330 " +SessionManager.getLoginUser().getCodigo() +
    	    " / " + SessionManager.getLoginUser().getNombre() + "\r\n";
 
    		
      		devolucion += "LEFT\r\n";
      		
    		  
    		devolucion += "T 7 0 0 380 Producto\r\n";
    		devolucion += "RIGHT 450\r\n";
    		devolucion += "T 7 0 0 380 Lote\r\n";
    		devolucion += "RIGHT 560\r\n";
    		devolucion += "T 7 0 0 380 Cant\r\n";
    		devolucion += "LEFT\r\n";
    		devolucion += "LINE 17 415 591 415 1\r\n";
    		int y = 420;
    		for(DevolucionProducto det:dev.getProductosDevueltos())
    		{
    			DevolucionProductoLote[] detl=det.getProductoLotes();
    			for(DevolucionProductoLote lote:detl)
        		{
    				String nombreProd = det.getNombreProducto();
        			if (nombreProd.length() > 40)
        				nombreProd = nombreProd.substring(0, 40) + "...";
        			devolucion += "T 7 0 0 " + y + " " + nombreProd + "\r\n";
        			devolucion += "RIGHT 458\r\n";
        			devolucion += "T 7 0 0 " + y + " "
        					+ (lote.getNumeroLote()) + "\r\n";
        			devolucion += "RIGHT 445d\r\n";
        			devolucion += "T 7 0 0 " + y + " "
        					+ StringUtil.formatInt(lote.getCantidadDevuelta())
        					+ "\r\n";
        			devolucion += "LEFT\r\n";
        			y += 26;
        		}
    		}  
    		devolucion += "LINE 0 " + y + " 576 " + y + " 1\r\n";
    		y += 10;
    		devolucion += "LINE 0 " + y + " 576 " + y + " 1\r\n";
    		y += 10;
    		devolucion += "T 7 0 169 " + y + " Gracias por su pedido\r\n";
    		y += 30;
    		devolucion += "T 7 0 119 " + y + " Panzyma. Al cuidado de la salud\r\n";
    		devolucion += "FORM\r\n";
    		devolucion += "PRINT\r\n";
    		y += 50;

    		String header = "! 0 200 200 " + y + " 1\r\n";
    		header += "LABEL\r\n";
    		header += "CONTRAST 0\r\n";
    		header += "TONE 0\r\n";
    		header += "SPEED 3\r\n";
    		header += "PAGE-WIDTH 600\r\n";
    		header += "BAR-SENSE\r\n";
    		header += ";// PAGE 0000000006000460\r\n";

    		devolucion = header + devolucion; 
	        
	        
	        if(devolucion.length() > 0) 
				new BluetoothConnection(devolucion); 
		} catch (Exception e) 
		 { 
			 NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
			Log.d(TAG,"ERROR al tratar de envia el recibo", e);
		}
    }
    
    @SuppressLint("UseValueOf") 
	public  static void ImprimirDevolucion(Devolucion dev,boolean reimpresion) 
	{   
		try
		{          
    		String devolucion = ""; 
    		devolucion += "T 7 1 123 5 Distribuidora Panzyma - DISPAN\r\n";
    		devolucion += "T 7 0 123 54 Comprobante de Devoluci\u00f3n\r\n";
    		devolucion += "LINE 0 80 576 80 1\r\n";
    		
    		devolucion += "T 7 0 0 90 Tipo Devolucion:\r\n";    		
    		devolucion += "T 7 0 40 120 Vencido\r\n";
    		devolucion += "T 7 0 210 118 "+(dev.isDeVencido()?"X":"")+"\r\n";     		
    		devolucion += "LINE 205 140 225 140 1\r\n";    		
    		devolucion += "T 7 0 40 150 No Vencido\r\n";
    		devolucion += "T 7 0 210 148 "+(dev.isDeVencido()?"":"X")+"\r\n";    		 
    		devolucion += "LINE 205 170 225 170 1\r\n";
    		
    		devolucion += "T 7 0 350 90 Fecha:\n"; 
    		devolucion += "T 7 0 440 90 " + DateUtil.idateToStr(dev.getFecha()) + "\r\n";
    		
    		devolucion += "T 7 0 350 120 Numero:\n"; 
    		devolucion += "T 7 0 440 120 " + NumberUtil.getFormatoNumero(dev.getReferencia(),NMApp.getContext()) + "\r\n";
    		
    		devolucion += "T 7 0 350 150 Pedido:\n"; 
    		devolucion += "T 7 0 440 150 " + dev.getNumeroPedidoDevuelto() + "\r\n";    		
    		
    		devolucion += "T 7 0 0 180 Tipo Tramite:\r\n";    		
    		devolucion += "T 7 0 40 210 Reposicion\r\n";
    		devolucion += "T 7 0 210 208 "+("RE".equals(dev.getTipoTramite())?"X":"")+"\r\n";    		
    		devolucion += "LINE 205 230 225 230 1\r\n";    		
    		devolucion += "T 7 0 40 240 Nota Credito\r\n";
    		devolucion += "T 7 0 210 238 "+("NC".equals(dev.getTipoTramite())?"X":"")+"\r\n"; 
    		devolucion += "LINE 205 260 225 260 1\r\n";
    		
    		
    		
    		devolucion += "T 7 0 0 270 Motivo:\r\n";
    		devolucion += "T 7 0 170 270"+dev.getDescMotivo()+"\r\n";
    		

    		devolucion += "T 7 0 0 300 Cliente:\r\n";
    		devolucion += "T 7 0 170 300"+dev.getNombreCliente()+"\r\n";
    		
    		devolucion += "T 7 0 0 330 Vendedor:\r\n";
    		devolucion += "T 7 0 170 330 " +SessionManager.getLoginUser().getCodigo() +
    	    " / " + SessionManager.getLoginUser().getNombre() + "\r\n";
 
    		
      		devolucion += "LEFT\r\n";
      		
    		  
    		devolucion += "T 7 0 0 380 Producto\r\n";
    		devolucion += "RIGHT 450\r\n";
    		devolucion += "T 7 0 0 380 Lote\r\n";
    		devolucion += "RIGHT 560\r\n";
    		devolucion += "T 7 0 0 380 Cant\r\n";
    		devolucion += "LEFT\r\n";
    		devolucion += "LINE 17 415 591 415 1\r\n";
    		int y = 420;
    		for(DevolucionProducto det:dev.getProductosDevueltos())
    		{
    			DevolucionProductoLote[] detl=det.getProductoLotes();
    			for(DevolucionProductoLote lote:detl)
        		{
    				String nombreProd = det.getNombreProducto();
        			if (nombreProd.length() > 40)
        				nombreProd = nombreProd.substring(0, 40) + "...";
        			devolucion += "T 7 0 0 " + y + " " + nombreProd + "\r\n";
        			devolucion += "RIGHT 458\r\n";
        			devolucion += "T 7 0 0 " + y + " "
        					+ (lote.getNumeroLote()) + "\r\n";
        			devolucion += "RIGHT 445d\r\n";
        			devolucion += "T 7 0 0 " + y + " "
        					+ StringUtil.formatInt(lote.getCantidadDevuelta())
        					+ "\r\n";
        			devolucion += "LEFT\r\n";
        			y += 26;
        		}
    		}  
    		devolucion += "LINE 0 " + y + " 576 " + y + " 1\r\n";
    		y += 10;
    		devolucion += "LINE 0 " + y + " 576 " + y + " 1\r\n";
    		y += 10;
    		devolucion += "T 7 0 169 " + y + " Gracias por su pedido\r\n";
    		y += 30;
    		devolucion += "T 7 0 119 " + y + " Panzyma. Al cuidado de la salud\r\n";
    		devolucion += "FORM\r\n";
    		devolucion += "PRINT\r\n";
    		y += 50;

    		String header = "! 0 200 200 " + y + " 1\r\n";
    		header += "LABEL\r\n";
    		header += "CONTRAST 0\r\n";
    		header += "TONE 0\r\n";
    		header += "SPEED 3\r\n";
    		header += "PAGE-WIDTH 600\r\n";
    		header += "BAR-SENSE\r\n";
    		header += ";// PAGE 0000000006000460\r\n";

    		devolucion = header + devolucion; 
	        
	        
	        if(devolucion.length() > 0) 
				new BluetoothConnection(devolucion); 
		} catch (Exception e) 
		 { 
			 NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
			Log.d(TAG,"ERROR al tratar de envia el recibo", e);
		}
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
    
    private  void RegistrarDevolucion(final Devolucion dev,final int...args) throws Exception
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
					            if(dev.getId()==0)
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
									((args!=null && args.length!=0 && args[0]==ControllerProtocol.SALVARDEVOLUCIONANTESDEIMPRIMIR)?ControllerProtocol.SALVARDEVOLUCIONANTESDEIMPRIMIR:
										(args!=null && args.length!=0 && args[0]==ControllerProtocol.SALVARDEVOLUCIONANTESDESALIR?ControllerProtocol.SALVARDEVOLUCIONANTESDESALIR: ControllerProtocol.NOTIFICATION)
									),
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

