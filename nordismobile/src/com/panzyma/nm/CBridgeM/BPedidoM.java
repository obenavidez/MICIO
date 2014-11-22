package com.panzyma.nm.CBridgeM;
import android.util.Log; 
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_INVENTORY_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SALVAR;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_DATA_FROM_LOCALHOST;

import java.util.ArrayList;
import java.util.Arrays;

import org.ksoap2.serialization.SoapObject;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle; 
import android.os.Message; 

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NumberUtil;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.bluetooth.BluetoothConnection;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelPedido;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.Producto; 
import com.panzyma.nm.serviceproxy.Ventas;

@SuppressLint("SimpleDateFormat") @SuppressWarnings({"rawtypes"})

public class BPedidoM extends BBaseM {

	String credenciales; 
	String TAG = BClienteM.class.getSimpleName();
	boolean OK = false;
	ArrayList<Pedido> obj = new ArrayList<Pedido>();
	private static final String logger = BPedidoM.class.getSimpleName();
	BluetoothConnection bc;
	Object lock=new Object();
	 
	@Override
	public boolean handleMessage(Message msg) {
		Bundle b = msg.getData();
		Boolean val= false;
		try 
		{
			
			switch (msg.what) 
			{
				case LOAD_DATA_FROM_LOCALHOST:
					onLoadALLData_From_LocalHost();
					val= true;
					break;
				case LOAD_DATA_FROM_SERVER:
					onLoadALLData_From_Server();
					val= true;
					break;
				case UPDATE_ITEM_FROM_SERVER:					
					onUpdateItem_From_Server(b.getLong("refpedido"));
					val= true;
					break;
				case UPDATE_INVENTORY_FROM_SERVER:
					onUpdateInventory_From_Server();
					val= true;
					break;
				case ID_SALVAR:
					val= true;
					break;
				case ControllerProtocol.SAVE_DATA_FROM_LOCALHOST:				
					guardarPedido((Pedido)b.getParcelable("pedido"));
					break;  
				
				case ControllerProtocol.SALVARPEDIDOANTESDEPROMOCIONES:
				case ControllerProtocol.APLICARPEDIDOPROMOCIONES:
				case ControllerProtocol.DESAPLICARPEDIDOPROMOCIONES: 
					
						String mensaje="";
						int arg1=msg.what; 
						mensaje=(ControllerProtocol.SALVARPEDIDOANTESDEPROMOCIONES==msg.what)?"Aplicando promociones...":
							(ControllerProtocol.APLICARPEDIDOPROMOCIONES==msg.what)?"Las promociones fueron aplicadas exitosamente...":
							(ControllerProtocol.DESAPLICARPEDIDOPROMOCIONES==msg.what)?"Las promociones fueron desaplicadas exitosamente...":"";
						
						guardar_Pedido((Pedido)b.getParcelable("pedido"));
						Processor.notifyToView(
									NMApp.getController(),
									(msg.what==ControllerProtocol.SALVARPEDIDOANTESDEPROMOCIONES)?ControllerProtocol.SALVARPEDIDOANTESDEPROMOCIONES:
									ControllerProtocol.NOTIFICATION,
									arg1,
									0,
									mensaje); 
						
					break;
					
				case DELETE_DATA_FROM_LOCALHOST :
					onDeleteDataFromLocalHost(Long.parseLong(msg.obj.toString()));
					val= true;
					break;
				case ControllerProtocol.SEND_DATA_FROM_SERVER:				
					enviarPedido((Pedido)b.getParcelable("pedido"));
					break;
				case ControllerProtocol.IMPRIMIR:
					imprimirPedido((Pedido)b.getParcelable("pedido"),(Cliente)b.getParcelable("cliente"));
					break;
				case ControllerProtocol.ANULAR_PEDIDO : 
						anularPedido(Long.parseLong(msg.obj.toString())); 
			}
			
			
		} catch (Exception e) {
			try {
				Processor
				.notifyToView(
						NMApp.getController(),
						ERROR,
						0,
						0,
						ErrorMessage.newInstance("",
								e.toString(), "\n Causa: "
										+ e.getCause()));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return val;
	}

	private void onDeleteDataFromLocalHost(final long pedidoID) {
		try 
		{
			getPool().execute(new Runnable() {

				@Override
				public void run() {
				  try {
						 Processor.send_ViewDeletePedidoToView(ModelPedido.borraPedidoByID(getResolver(), pedidoID),getController());
				  	} 
				catch (Exception e) {
				Log.e(logger, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											NMApp.getController(),
											ERROR,
											0,
											0,
											ErrorMessage.newInstance(
													"Error interno en la sincronizaci�n con la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	private void onLoadALLData_From_LocalHost() {
		try 
		{
			getPool().execute(new Runnable() {

				@Override
				public void run() {

					try 
					{
						
						Processor.send_ViewPedidoToView(C_DATA, NMApp.getController(), ModelPedido.obtenerPedidosLocalmente(getResolver()));
					 
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
													"Error interno en la sincronizaci�n con la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onLoadALLData_From_Server() {

	}

	private void onUpdateItem_From_Server(final long refPedido) {

		try 
		{
			credenciales="";
			credenciales=SessionManager.getCredentials(); 
			if(credenciales=="")
				return;
			getPool().execute(new Runnable() {

				@Override
				public void run() {
 
					try 
					{ 
						NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ID_REQUEST_UPDATEITEM_FROMSERVER, 0, 0,guardar_Pedido(ModelPedido.refrescarPedido(credenciales, refPedido)));
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
													"Error en el envio del pedido",
													e.getMessage(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
					}
			});
		}catch(Exception e){
			
		}
	}

	private void onUpdateInventory_From_Server() {
		try 
		{
			getPool().execute(new Runnable() {

				@Override
				public void run() {

					try {
						SoapObject DisponibilidadProducto = (SoapObject) ModelPedido
								.onUpdateInventory_From_Server(
										"sa||nordis09||dp", "areyes", true);
						if (DisponibilidadProducto != null)
							ModelProducto.UpdateProducto(
									getResolver(),
									DisponibilidadProducto);

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
													"Error interno en la sincronizaci�n con la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Producto getProductoByID(ContentResolver content,
			long idproducto) throws Exception {
		return ModelProducto.getProductoByID(content, idproducto);
	}
	
	public void guardarPedido(final Pedido pedido)
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
						guardar_Pedido(pedido); 			        
				        Processor.notifyToView(
				        		NMApp.getController(),
								ControllerProtocol.NOTIFICATION,
								ControllerProtocol.SAVE_DATA_FROM_LOCALHOST,
								0,
								pedido);
						
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
						0,"Guardando pedido localmente");
			
		} catch (Exception e) 
		{ 
			e.printStackTrace();
		}
	}

	public Pedido RegistrarPedido(Pedido pedido, Context cnt) throws Exception {
		return ModelPedido.RegistrarPedido(pedido, cnt);
	}
 
	public Pedido guardar_Pedido(Pedido pedido)throws Exception
	{		
		//Salvando el tipo de pedido (cr�dito contado)		 
        Integer prefijo=Ventas.getPrefijoIds(NMApp.getContext());
        Integer pedidomax=Ventas.getLastOrderId(NMApp.getContext());
        //Generar Id del pedido
        if (pedido.getNumeroMovil() == 0) 
        {                     
            if (pedidomax == null) 
            	pedidomax = Integer.valueOf(1);
            else
            	pedidomax =pedidomax+1; 
            String strIdMovil = prefijo.intValue() + "" + pedidomax.intValue();
            int idMovil = Integer.parseInt(strIdMovil);
            
            pedido.setId(0);
            pedido.setNumeroMovil(idMovil);
            pedido.setObjEstadoID(0);
            pedido.setObjCausaEstadoID(0);
            pedido.setCodEstado("REGISTRADO");
            pedido.setDescEstado("Elaboraci�n");
            pedido.setCodCausaEstado("REGISTRADO");
            pedido.setDescCausaEstado("Registrado");
        }  
        RegistrarPedido(pedido,NMApp.getContext());         
        ModelConfiguracion.ActualizarSecuenciaPedido(NMApp.getContext(),(pedidomax));
        return pedido;
	}
	
	public void enviarPedido(final Pedido pedido)  
	{
		try 
		{
			
			
			getPool().execute(new Runnable()
			{ 
				
				@SuppressWarnings("unchecked")
				@Override
				public void run()
			    {					 
					try 
					{ 	
						credenciales="";
						credenciales=SessionManager.getCredentials(); 
						if(credenciales=="")
							return;
						
						Processor.notifyToView(getController(),ControllerProtocol.NOTIFICATION_DIALOG2,
								0,0,"Salvando primero la informaci�n en el dispositivo");
						//guardar primero el pedido localmente 
						guardar_Pedido(pedido);
						
						Processor.notifyToView(getController(),ControllerProtocol.NOTIFICATION_DIALOG2,
								0,0,"Enviando pedido al servidor central");
						//enviado pedido al servidor central
						Pedido obj=ModelPedido.enviarPedido(credenciales, pedido);; 
			    
						if (obj == null) return; 
						
					    //guardando de nuevo localmente el pedido ya actualizado  
						guardar_Pedido(obj);
						 
						Processor.notifyToView(NMApp.getController(),ControllerProtocol.NOTIFICATION_DIALOG2,
								0,0,"Actualizando el estado de cuentas del Cliente");
			            //Volver a traer al cliente del servidor y actualizarlo en la memoria del dispositivo            
			            Cliente cliente= BClienteM.actualizarCliente(NMApp.getContext(),SessionManager.getCredenciales(),obj.getObjSucursalID()); 
			           
			            //Notificar al Usuario el resultado del envio del Pedido.
			            NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ID_REQUEST_ENVIARPEDIDO, 0, 0, new ArrayList<Object>(Arrays.asList(obj,cliente)));
 
					}catch (Exception e) 
					{ 
						Log.e(TAG, "Error in the update thread", e);
						try {
							Processor.notifyToView(
											NMApp.getController(),
											ERROR,
											0,
											0,
											ErrorMessage.newInstance(
													"Error en el envio del pedido",
													e.getMessage(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
			    }
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	public static Pedido obtenerPedidoByID(long idpedido,ContentResolver content)throws Exception
	{
		return ModelPedido.obtenerPedidoByID(idpedido, content);
	}
	
	public void anularPedido(final long pedidoid) throws Exception
	{
		try 
		{
			credenciales="";
			credenciales=SessionManager.getCredentials(); 
			if(credenciales=="")
				return;
		
			getPool().execute(new Runnable()
			{  
				@Override
				public void run()
			    {					 
					try 
					{
						Processor.notifyToView(NMApp.getController(),ControllerProtocol.NOTIFICATION_DIALOG2,0,0,"Conectando con el servidor central.");
						
						Pedido pedido= ModelPedido.anularPedido(credenciales, pedidoid);
						
						//Notificar al Usuario que se Anulado.
			            NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ID_REQUEST_ANULARPEDIDO, 0, 0, pedido);
						
					}
					catch (Exception e) {
						Log.e(TAG, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											NMApp.getController(),
											ERROR,
											0,
											0,
											ErrorMessage.newInstance(
													"Error en Anular el pedido",
													e.getMessage(), "\n Causa: "
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
		//return ModelPedido.anularPedido("sa-nordis09-dp", pedidoid);
	}
     
	public void imprimirPedido(final Pedido pedido, final Cliente cliente)
    { 
		try 
		{
			 // Salvar pedido si a�n no tiene un n�mero de referencia asignado
    		if (pedido.getNumeroMovil() == 0) 
					guardarPedido(pedido); 
    		String _pedido = "";
//    		_pedido+="PCX 20 0\r\n";
//    		_pedido+= getImage();
    		_pedido += "T 7 1 123 5 Distribuidora Panzyma - DISPAN\r\n";
    		_pedido += "T 7 0 189 54 Orden de Pedido\r\n";
    		_pedido += "LINE 0 80 576 80 1\r\n";
    		_pedido += "T 0 0 0 90 Fecha:\r\n";
    		_pedido += "T 0 0 90 90 " + DateUtil.idateToStr(pedido.getFecha())
    				+ "\r\n";
    		_pedido += "T 0 0 400 90 Referencia:\r\n";
    		_pedido += "RIGHT 576\r\n";
    		_pedido += "T 0 0 490 90 "
    				+ NumberUtil.getFormatoNumero(pedido.getNumeroMovil(),NMApp.getContext()) + "\r\n";
    		_pedido += "LEFT\r\n";
    		_pedido += "T 0 0 0 118 Cliente:\r\n";
    		_pedido += "T 0 0 90 118 " + cliente.getNombreLegalCliente() + "\r\n";
    		_pedido += "T 0 0 0 144 Vendedor:\r\n";
    		_pedido += "T 0 0 90 144 " +SessionManager.getLoginUser().getCodigo() +
    	    " / " + SessionManager.getLoginUser().getNombre() + "\r\n";
    		_pedido += "LINE 0 170 576 170 1\r\n";
    		_pedido += "T 0 0 0 180 Producto\r\n";
    		_pedido += "RIGHT 382\r\n";
    		_pedido += "T 0 0 0 180 Cant\r\n";
    		_pedido += "RIGHT 435\r\n";
    		_pedido += "T 0 0 0 180 Bonif\r\n";
    		_pedido += "RIGHT 482\r\n";
    		_pedido += "T 0 0 0 180 Prom\r\n";
    		_pedido += "RIGHT 576\r\n";
    		_pedido += "T 0 0 0 180 Precio\r\n";
    		_pedido += "LEFT\r\n";
    		_pedido += "LINE 17 196 591 196 1\r\n";

    		int y = 206;
    		int tamanio=pedido.getDetalles().length;
    		for (int curRecord = 0; curRecord < tamanio; curRecord++) 
    		{
    			DetallePedido det = pedido.getDetalles()[curRecord];

    			String nombreProd = det.getNombreProducto();
    			if (nombreProd.length() > 40)
    				nombreProd = nombreProd.substring(0, 40) + "...";
    			_pedido += "T 0 0 0 " + y + " " + nombreProd + "\r\n";
    			_pedido += "RIGHT 382\r\n";
    			_pedido += "T 0 0 0 " + y + " "
    					+ StringUtil.formatInt(det.getCantidadOrdenada()) + "\r\n";
    			_pedido += "RIGHT 435\r\n";
    			_pedido += "T 0 0 0 " + y + " "
    					+ StringUtil.formatInt(det.getCantidadBonificadaEditada())
    					+ "\r\n";
    			_pedido += "RIGHT 482\r\n";
    			_pedido += "T 0 0 0 " + y + " " + "0" + "\r\n"; // Poner promoci�n
    			_pedido += "RIGHT 576\r\n";
    			_pedido += "T 0 0 482 " + y + " "
    					+ StringUtil.formatReal(det.getPrecio()) + "\r\n";
    			_pedido += "LEFT\r\n";
    			y += 26;
    		}
    		_pedido += "LINE 0 " + y + " 576 " + y + " 1\r\n";
    		y += 10;
    		_pedido += "T 0 0 379 " + y + " Subtotal:\r\n";
    		_pedido += "RIGHT 576\r\n";
    		_pedido += "T 0 0 0 " + y + " "
    				+ StringUtil.formatReal(pedido.getSubtotal()) + "\r\n";
    		_pedido += "LEFT\r\n";

    		y += 26;
    		_pedido += "T 0 0 379 " + y + " Descuento:\r\n";
    		_pedido += "RIGHT 576\r\n";
    		_pedido += "T 0 0 0 " + y + " "
    				+ StringUtil.formatReal(pedido.getDescuento()) + "\r\n";
    		_pedido += "LEFT\r\n";

    		y += 26;
    		_pedido += "T 0 0 379 "
    				+ y
    				+ " "
    				+ NMApp.getContext().getSharedPreferences("SystemParams",
    								android.content.Context.MODE_PRIVATE)
    						.getString("NombreImpuesto", "--") + ":\r\n";
    		_pedido += "RIGHT 576\r\n";
    		_pedido += "T 0 0 0 " + y + " "
    				+ StringUtil.formatReal(pedido.getImpuesto()) + "\r\n";
    		_pedido += "LEFT\r\n";

    		y += 26;
    		_pedido += "T 0 0 379 "
    				+ y
    				+ " Total "
    				+ NMApp.getContext().getSharedPreferences("SystemParams",
    								android.content.Context.MODE_PRIVATE)
    						.getString("MonedaNacional", "--") + ":\r\n";
    		_pedido += "RIGHT 576\r\n";
    		_pedido += "T 0 0 0 " + y + " "
    				+ StringUtil.formatReal(pedido.getTotal()) + "\r\n";
    		_pedido += "LEFT\r\n";

    		y += 15;
    		_pedido += "LINE 0 " + y + " 576 " + y + " 1\r\n";
    		y += 10;
    		_pedido += "T 7 0 169 " + y + " Gracias por su pedido\r\n";
    		y += 30;
    		_pedido += "T 7 0 119 " + y + " Panzyma. Al cuidado de la salud\r\n";
    		_pedido += "FORM\r\n";
    		_pedido += "PRINT\r\n";
    		y += 50;

    		String header = "! 0 200 200 " + y + " 1\r\n";
    		header += "LABEL\r\n";
    		header += "CONTRAST 0\r\n";
    		header += "TONE 0\r\n";
    		header += "SPEED 3\r\n";
    		header += "PAGE-WIDTH 600\r\n";
    		header += "BAR-SENSE\r\n";
    		header += ";// PAGE 0000000006000460\r\n";

    		_pedido = header + _pedido; 
    		
    		if(_pedido.length() > 0) 
    			new BluetoothConnection(_pedido); 
    		
		 } catch (Exception e) 
		 { 
			 NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
			Log.d(TAG,"ERROR al tratar de envia el pedido", e);
		}
    }
	

	 
}
