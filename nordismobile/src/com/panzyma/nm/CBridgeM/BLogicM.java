package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CuentasPorCobrarFragment;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.viewdialog.DialogoConfirmacion;

public class BLogicM extends BBaseM {
	private String TAG = BLogicM.class.getSimpleName();
	boolean OK = false;
	private CuentasPorCobrarFragment fragment = null;	
	private DialogoConfirmacion view;
	
	public enum Result 
	{		
		CLIENTE(0),
		FACTURAS_CLIENTE(1), 
		NOTAS_DEBITO(2), 
		NOTAS_CREDITO(3), 
		PEDIDOS(4), 
		RECIBOS_COLECTOR(5),
		ABONOS_FACTURAS_OTROS_RECIBOS(6);
		
		int result;
		
		Result(int result){
			this.result = result;
		}
		
		public int getResult(){
			return result;
		}		

	    public static Result toInt(int x) {
	    	 return Result.values()[x];
	    }
		
	}

	public BLogicM() {
	}
 

	public boolean handleMessage(Message msg) throws Exception 
	{
		Bundle b = msg.getData();
		switch (msg.what) 
		{
			case LOAD_DATA_FROM_SERVER:
				onLoadClienteDataFromServer();
				return true;
			case ControllerProtocol.LOAD_FACTURASCLIENTE_FROM_SERVER:
				onLoadFacturasClienteFromServer(b);
				break;
			case ControllerProtocol.LOAD_NOTAS_DEBITO_FROM_SERVER:
				onLoadNotasDebitoClienteFromServer(b);
				break;
			case ControllerProtocol.LOAD_NOTAS_CREDITO_FROM_SERVER:
				onLoadNotasCreditoClienteFromServer(b);
				break;
			case ControllerProtocol.LOAD_PEDIDOS_FROM_SERVER:
				onLoadPedidosClienteFromServer(b);
				break;
			case ControllerProtocol.LOAD_RECIBOS_FROM_SERVER:
				onLoadRecibosClienteFromServer(b);
				break;
			case ControllerProtocol.LOAD_ABONOS_FACTURA_EN_OTROS_RECIBOS:
				Bundle params = msg.getData();			
				onLoadAbonosOtrasOtrosRecibosFactura(
						params.getLong("objFacturaID"),
						params.getLong("objReciboID"));
				break;
		}
		return false;
	}

	private void onLoadAbonosOtrasOtrosRecibosFactura(final long facturaId, final long reciboId) {
		try {
			getPool().execute(new Runnable() {

				@Override
				public void run() {

					try {
						Processor.notifyToView(getController(),
								Result.ABONOS_FACTURAS_OTROS_RECIBOS.getResult(),
								0, 
								0,
								ModelLogic.getAbonosEnOtrosRecibos(
										getContext(),
										facturaId,
										reciboId, 0)
							);
					} catch (Exception e) {
						Log.e(TAG, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											getController(),
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
		} catch (InterruptedException e) {
			Log.e(TAG, "Error in the update thread", e);
			try {
				Processor
						.notifyToView(
								getController(),
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

	private void onLoadRecibosClienteFromServer(final Bundle b) {
		
			try 
			{
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try 
						{
							final String credentials = SessionManager.getCredentials();
							if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) {
								Processor.notifyToView(getController(),
										Result.RECIBOS_COLECTOR.getResult(),
										0, 
										0,
										ModelLogic.getRecibosColector(credentials,
												b.getLong("SucursalId"),
												b.getInt("FechaInicRCol"),
												b.getInt("FechaFinRCol"), 
												b.getString("EstadoRCol")));
							}else
								getController()._notifyOutboxHandlers(0, 0, 0, 0);
						} catch (Exception e) {
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor
										.notifyToView(
												getController(),
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
			} catch (InterruptedException e) {
				Log.e(TAG, "Error in the update thread", e);
				try {
					Processor
							.notifyToView(
									getController(),
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

	private void onLoadPedidosClienteFromServer(final Bundle b) {
		
			try 
			{
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try 
						{
							final String credentials = SessionManager.getCredentials();
							if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
							{
								Processor.notifyToView(getController(),
										Result.PEDIDOS.getResult(),
										0,
										0,
										ModelLogic.getPedidosCliente(credentials,
												b.getLong("SucursalId"),
												b.getInt("FechaInicPedidos"),
												b.getInt("FechaFinPedidos"), 
												b.getString("EstadoPedidos")));
							}else
								getController()._notifyOutboxHandlers(0, 0, 0, 0);
						} catch (Exception e) {
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor
										.notifyToView(
												getController(),
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
			} catch (InterruptedException e) {
				Log.e(TAG, "Error in the update thread", e);
				try {
					Processor
							.notifyToView(
									getController(),
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

	private void onLoadNotasCreditoClienteFromServer(final Bundle b) {
		
			try 
			{
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try 
						{
							final String credentials = SessionManager.getCredentials();
							if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
							{
							Processor.notifyToView(getController(),
									Result.NOTAS_CREDITO.getResult(),
									0,
									0,
									ModelLogic.getNotasCreditoCliente(
											credentials,
											b.getLong("SucursalId"),
											b.getInt("FechaInicNC"),
											b.getInt("FechaFinNC"), 
											b.getString("EstadoNC")));
							}else
								getController()._notifyOutboxHandlers(0, 0, 0, 0);
						} catch (Exception e) {
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor
										.notifyToView(
												getController(),
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
			} catch (InterruptedException e) {
				Log.e(TAG, "Error in the update thread", e);
				try {
					Processor
							.notifyToView(
									getController(),
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

	private void onLoadNotasDebitoClienteFromServer(final Bundle b) 
	{
		try {
			getPool().execute(new Runnable() {

				@Override
				public void run() {

					try 
					{
						
						final String credentials = SessionManager.getCredentials();
						if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
						{
							Processor.notifyToView(getController(),
									Result.NOTAS_DEBITO.getResult(),
									0,
									0,
									ModelLogic.getNotasDebitoCliente(
											credentials,
											b.getLong("SucursalId"),
											b.getInt("FechaInicND"),
											b.getInt("FechaFinND"), 
											b.getString("EstadoND")));
						}else 
							getController()._notifyOutboxHandlers(0, 0, 0, 0);
					} catch (Exception e) {
						Log.e(TAG, "Error in the update thread", e);
						try {
							Processor
									.notifyToView(
											getController(),
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
		} catch (InterruptedException e) {
			Log.e(TAG, "Error in the update thread", e);
			try {
				Processor
						.notifyToView(
								getController(),
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

	private void onLoadFacturasClienteFromServer(final Bundle b) {

		
			try 
			{
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try 
						{
							
							final String credentials = SessionManager.getCredentials();
							if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
							{
							
								Processor.notifyToView(getController(),
										Result.FACTURAS_CLIENTE.getResult(),
										0,
										0,
										ModelLogic.getFacturasCliente(credentials,
												b.getLong("SucursalId"),
												b.getInt("FechaInicFac"),
												b.getInt("FechaFinFac"), 
												b.getBoolean("SoloFacturasConSaldo"), 
												b.getString("EstadoFac")));
							}
							else
								getController()._notifyOutboxHandlers(0, 0, 0, 0);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			} catch (InterruptedException e) {
				Log.e(TAG, "Error in the update thread", e);
				try {
					Processor
							.notifyToView(
									getController(),
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

	private void onLoadClienteDataFromServer() {
		try {
		   
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try 
						{
							final String credentials = SessionManager.getCredentials();
							if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
							{
								Processor.notifyToView(getController(),
										Result.CLIENTE.getResult(),
										0, 
										0, 
										ModelLogic.getCuentasPorCobrarDelCliente(
												credentials,
												fragment.getSucursalId()));
							}
							else
								getController()._notifyOutboxHandlers(0, 0, 0, 0);
						} catch (Exception e) {
							Log.e(TAG, "Error in the update thread", e);
							try {
								Processor
										.notifyToView(
												getController(),
												ERROR,
												0,
												0,
												new ErrorMessage(
														"Error interno en la sincronización con la BDD",
														e.toString(),
														"\n Causa: "
																+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
