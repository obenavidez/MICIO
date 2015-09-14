package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
 





import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol; 
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.model.ModelDevolucion;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelPedido; 
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.viewmodel.vmDevolucion;

import android.os.Message;
import android.util.Log;

@SuppressWarnings("unchecked")
public class BDevolucionM extends BBaseM
{
	private String TAG=BDevolucionM.class.getSimpleName();
	
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
			case ControllerProtocol.SAVE_DATA_FROM_LOCALHOST:
				Devolucion dev=(Devolucion) msg.obj;
				RegistrarDevolucion(dev);
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
							vmDevolucion item = new vmDevolucion();
							item.setCliente("Fci. Dayanara/Angelica Quezada");
							item.setEstado("Registrada");
							
							Calendar c = Calendar.getInstance(); 
							c.set(2014, 02, 14);
							item.setFecha(c.getTime());
							item.setNumeroCentral(7487799);
							item.setTotal(new Float(123.45));
							item.setCliente_id(1002548);
							ArrayList<vmDevolucion> lista = new  ArrayList<vmDevolucion>();
							lista.add(item);
							
							item = new vmDevolucion();
							item.setCliente("Fci. Tamara Garcia/Angelica Palema.");
							item.setEstado("Registrada");
							item.setCliente_id(1002548);
							c = Calendar.getInstance(); 
							c.set(2014, 02, 14);
							item.setFecha(c.getTime());
							item.setNumeroCentral(8887702);
							item.setTotal(new Float(155670.43));
							item.setOffLine(true);
							lista.add(item);
							
							item = new vmDevolucion();
							item.setCliente("Empresa Medica de Carazo/Rolando Niño.");
							item.setEstado("Enviada");
							item.setCliente_id(1002548);
							c = Calendar.getInstance(); 
							c.set(2014, 02, 14);
							item.setFecha(c.getTime());
							item.setNumeroCentral(8887702);
							item.setTotal(new Float(155670.43));
							item.setOffLine(false);
							lista.add(item);
							
							/*
							lista.add(new vmDevolucion(
     							   Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))),
     							   Integer.parseInt(cur.getString(cur.getColumnIndex(projection[1]))), 
     							   Date.valueOf(cur.getString(cur.getColumnIndex(projection[2]))), 
     							   cur.getString(cur.getColumnIndex(projection[3])), 
     							   Float.valueOf(cur.getString(cur.getColumnIndex(projection[4]))),
     							   cur.getString(cur.getColumnIndex(projection[5])),
     							   Long.parseLong(cur.getString(cur.getColumnIndex(projection[6]))))
     	);*/
							
							Processor.send_ViewDevolucionesToView(/*ModelDevolucion.obtenerDevolucionesFromLocalHost(getResolver())*/lista, getController());	
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

    private void RegistrarDevolucion(Devolucion dev){
    	try {
			DatabaseProvider.RegistrarDevolucion(dev, NMApp.getContext());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
 }

