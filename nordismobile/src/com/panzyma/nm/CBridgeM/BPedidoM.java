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

import org.ksoap2.serialization.SoapObject;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelPedido;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewPedido;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.viewmodel.vmEntity;

@SuppressWarnings("rawtypes")
public class BPedidoM {

	Controller controller;
	ThreadPool pool;
	ViewPedidoEdit view1;
	ViewPedido view;
	vmEntity pedido_selected;
	String TAG = BClienteM.class.getSimpleName();
	boolean OK = false;
	ArrayList<Pedido> obj = new ArrayList<Pedido>();
	private static final String logger = BPedidoM.class.getSimpleName();

	public BPedidoM() {
	}

	public BPedidoM(ViewPedido view) {
		this.controller = ((NMApp) view.getApplication()).getController();
		this.view = view;
		this.pool = ((NMApp) view.getApplicationContext()).getThreadPool();
	}

	public BPedidoM(ViewPedidoEdit view) {
		this.controller = ((NMApp) view.getApplication()).getController();
		this.view1 = view;
		this.pool = ((NMApp) view.getApplicationContext()).getThreadPool();
	}

	public boolean handleMessage(Message msg) {
		Bundle b = msg.getData();
		Boolean val= false;
		switch (msg.what) {
			case LOAD_DATA_FROM_LOCALHOST:
				onLoadALLData_From_LocalHost();
				val= true;
				break;
			case LOAD_DATA_FROM_SERVER:
				onLoadALLData_From_Server();
				val= true;
				break;
			case UPDATE_ITEM_FROM_SERVER:
				onUpdateItem_From_Server();
				val= true;
				break;
			case UPDATE_INVENTORY_FROM_SERVER:
				onUpdateInventory_From_Server();
				val= true;
				break;
			case ID_SALVAR:
				val= true;
				break;
			case DELETE_DATA_FROM_LOCALHOST :
				onDeleteDataFromLocalHost();
				val= true;
				break;
		}
		return val;
	}

	private void onDeleteDataFromLocalHost() {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {
				  try {
						ContentResolver resolver =(view != null) ? view.getContentResolver() : view1.getContext().getContentResolver();
						Processor.send_ViewDeletePedidoToView(ModelPedido.borraPedidoByID(resolver, view.getPedidoSelected().getId()),controller);
				  	} 
				catch (Exception e) {
				Log.e(logger, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											controller,
											ERROR,
											0,
											0,
											new ErrorMessage(
													"Error interno en la sincronización con la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void onLoadALLData_From_LocalHost() {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {
						
						Processor.send_ViewPedidoToView(C_DATA, controller, ModelPedido.obtenerPedidosLocalmente((view!=null)?view.getContentResolver():view1.getContentResolver()));
					 
					} catch (Exception e) {
						Log.e(TAG, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											controller,
											ERROR,
											0,
											0,
											new ErrorMessage(
													"Error interno en la sincronización con la BDD",
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

	private void onUpdateItem_From_Server() {

	}

	private void onUpdateInventory_From_Server() {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {
						SoapObject DisponibilidadProducto = (SoapObject) ModelPedido
								.onUpdateInventory_From_Server(
										"sa||nordis09||dp", "areyes", true);
						if (DisponibilidadProducto != null)
							ModelProducto.UpdateProducto(
									view.getContentResolver(),
									DisponibilidadProducto);

					} catch (Exception e) {
						Log.e(TAG, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											controller,
											ERROR,
											0,
											0,
											new ErrorMessage(
													"Error interno en la sincronización con la BDD",
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

	public long RegistrarPedido(Pedido pedido, Context cnt) throws Exception {
		return ModelPedido.RegistrarPedido(pedido, cnt);
	}

	public int ActualizarSecuenciaPedido(int idpedido, Context cnt)
			throws Exception {
		return ModelConfiguracion.ActualizarSecuenciaPedido(cnt, idpedido);
	}

	public static Pedido enviarPedido(String credenciales, Pedido pedido) {
		return null;// ModelPedido.enviarPedido(credenciales, pedido);
	}

	public static Pedido obtenerPedidoByID(long idpedido,ContentResolver content)throws Exception
	{
		return ModelPedido.obtenerPedidoByID(idpedido, content);
	}
	
	public static Pedido anularPedido(long pedidoid) throws Exception
	{
		//final String credentials=SessionManager.getCredenciales();
		return ModelPedido.anularPedido("sa-nordis09-dp", pedidoid);
	}
}
