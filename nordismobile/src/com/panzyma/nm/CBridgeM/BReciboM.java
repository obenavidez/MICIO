package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.C_FACTURACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.viewdialog.DialogDocumentos;

@SuppressWarnings("rawtypes")
public final class BReciboM {

	private static final String logger = BReciboM.class.getSimpleName();

	Controller controller;
	ThreadPool pool;
	private ViewRecibo view;
	private DialogDocumentos view1;
	private ViewReciboEdit reciboEdit;

	boolean OK = false;
	ArrayList<Producto> obj = new ArrayList<Producto>();
	JSONArray jsonA = new JSONArray();

	public BReciboM() {
	}

	public BReciboM(ViewRecibo view) {
		this.controller = ((NMApp) view.getApplicationContext())
				.getController();
		this.view = view;
		this.pool = ((NMApp) view.getApplicationContext()).getThreadPool();
	}

	public BReciboM(ViewReciboEdit view) {
		//view.getApplicationContext()
		this.controller = ((NMApp) view.getApplicationContext())
				.getController();
		this.reciboEdit = view;
		this.pool = ((NMApp) view.getApplicationContext()).getThreadPool();
	}
	
	public BReciboM(DialogDocumentos view1) {
		this.controller = ((NMApp) view1.getContext().getApplicationContext())
				.getController();
		this.view1 = view1;
		this.pool = ((NMApp) view1.getContext().getApplicationContext()).getThreadPool();
	}


	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) {
		case LOAD_DATA_FROM_LOCALHOST:
			onLoadALLData_From_LocalHost();
			return true;
		case LOAD_ITEM_FROM_LOCALHOST:
			onLoadItemFromLocalHost();
			return true;
		case DELETE_DATA_FROM_LOCALHOST:
			onDeleteData_From_LocalHost();
			break;
		case C_FACTURACLIENTE:
			onLoadFacturasCliente_From_Localhost();
			break; 
		case LOAD_DATA_FROM_SERVER:
			// onLoadALLData_From_LocalHost();
			return true;
		case UPDATE_ITEM_FROM_SERVER:
			// onUpdateItem_From_Server();
			return true;

		}
		return false;
	}

	private void onLoadItemFromLocalHost() {
		try {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					Processor.send_ViewReciboEditToView(
							ModelRecibo.getReciboByID(
									reciboEdit.getContentResolver(),
									reciboEdit.getReciboID()), controller);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void onDeleteData_From_LocalHost() {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {

						Processor.send_ViewDeleteReciboToView(
								ModelRecibo
										.borraReciboByID((view != null) ? view
												.getContentResolver() : view1
												.getContext()
												.getContentResolver(), view.getReciboSelected().getId() ),
								controller);
					} catch (Exception e) {
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

	private void onUpdateItem_From_Server() {
		// TODO Auto-generated method stub

	}

	private void onLoadALLData_From_LocalHost() {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {

						Processor.send_ViewReciboToView(
								ModelRecibo
										.getArrayCustomerFromLocalHost((view != null) ? view
												.getContentResolver() : view1
												.getContext()
												.getContentResolver()),
								controller);
					} catch (Exception e) {
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
			e.printStackTrace();
		}

	}

	private void onLoadFacturasCliente_From_Localhost() {
		try {
			this.pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// Parameters params = viewcc.get_FacturaParameters();
						Processor.send_ViewReciboEditToView(
								ModelCliente.getClienteBySucursalID(view1.getContext()
										.getContentResolver(), view1
										.getObjSucursalId()), controller);

					} catch (Exception e) {
						e.printStackTrace();
						try {
							Processor
									.notifyToView(
											controller,
											ERROR,
											0,
											0,
											new ErrorMessage(
													"Error en la sincronización con el servidor",
													e.toString(), "\nCausa: "
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

}
