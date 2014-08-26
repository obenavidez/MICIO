package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;
import android.content.Context;
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
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.viewdialog.DialogoConfirmacion;

public class BLogicM {

	private Controller controller = null;
	private ThreadPool pool = null;
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

	public BLogicM(CuentasPorCobrarFragment cuentasPorCobrarFragment) {
		this.fragment = cuentasPorCobrarFragment;
		this.controller = ((NMApp) cuentasPorCobrarFragment.getActivity()
				.getApplication()).getController();
		this.pool = ((NMApp) cuentasPorCobrarFragment.getActivity()
				.getApplication()).getThreadPool();
	}
	
	public BLogicM(DialogoConfirmacion view) {
		this.view = view;
		this.controller = ((NMApp) this.view.getActivity()
				.getApplication()).getController();
		this.pool = ((NMApp) this.view.getActivity()
				.getApplication()).getThreadPool();
	}

	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) {
		case LOAD_DATA_FROM_SERVER:
			onLoadClienteDataFromServer();
			return true;
		case ControllerProtocol.LOAD_FACTURASCLIENTE_FROM_SERVER:
			onLoadFacturasClienteFromServer();
			break;
		case ControllerProtocol.LOAD_NOTAS_DEBITO_FROM_SERVER:
			onLoadNotasDebitoClienteFromServer();
			break;
		case ControllerProtocol.LOAD_NOTAS_CREDITO_FROM_SERVER:
			onLoadNotasCreditoClienteFromServer();
			break;
		case ControllerProtocol.LOAD_PEDIDOS_FROM_SERVER:
			onLoadPedidosClienteFromServer();
			break;
		case ControllerProtocol.LOAD_RECIBOS_FROM_SERVER:
			onLoadRecibosClienteFromServer();
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
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {
						Processor.notifyToView(controller,
								Result.ABONOS_FACTURAS_OTROS_RECIBOS.getResult(),
								0, 
								0,
								ModelLogic.getAbonosFacturaEnOtrosRecibos(
										view.getActivity(),
										facturaId,
										reciboId)
							);
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

	private void onLoadRecibosClienteFromServer() {
		 final String credentials = SessionManager.getCredentials();
		//final String credentials = "sa||nordis09||dp";

		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(controller) ) {
			try {
				pool.execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(controller,
									Result.RECIBOS_COLECTOR.getResult(),
									0, 
									0,
									ModelLogic.getRecibosColector(credentials,
											fragment.getSucursalId(),
											fragment.getFechaInicRCol(),
											fragment.getFechaFinRCol(),
											fragment.getEstadoRCol()));
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

	}

	private void onLoadPedidosClienteFromServer() {
		final String credentials = SessionManager.getCredentials();
		//final String credentials = "sa||nordis09||dp";

		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(controller) ) {
			try {
				pool.execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(controller,
									Result.PEDIDOS.getResult(),
									0,
									0,
									ModelLogic.getPedidosCliente(credentials,
											fragment.getSucursalId(),
											fragment.getFechaInicPedidos(),
											fragment.getFechaFinPedidos(),
											fragment.getEstadoPedidos()));
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
	}

	private void onLoadNotasCreditoClienteFromServer() {
		 final String credentials = SessionManager.getCredentials();
		//final String credentials = "sa||nordis09||dp";
		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(controller) ) {
			try {
				pool.execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(controller,
									Result.NOTAS_CREDITO.getResult(),
									0,
									0,
									ModelLogic.getNotasCreditoCliente(
											credentials,
											fragment.getSucursalId(),
											fragment.getFechaInicNC(),
											fragment.getFechaFinNC(),
											fragment.getEstadoNC()));
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
	}

	private void onLoadNotasDebitoClienteFromServer() {
		// final String credentials = SessionManager.getCredentials();
		final String credentials = "sa||nordis09||dp";

		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(controller) ) {
			try {
				pool.execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(controller,
									Result.NOTAS_DEBITO.getResult(),
									0,
									0,
									ModelLogic.getNotasDebitoCliente(
											credentials,
											fragment.getSucursalId(),
											fragment.getFechaInicND(),
											fragment.getFechaFinND(),
											fragment.getEstadoND()));
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

	}

	private void onLoadFacturasClienteFromServer() {

		final String credentials = SessionManager.getCredentials();
		//final String credentials = "sa||nordis09||dp";

		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(controller) ) {
			try {
				pool.execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(controller,
									Result.FACTURAS_CLIENTE.getResult(),
									0,
									0,
									ModelLogic.getFacturasCliente(credentials,
											fragment.getSucursalId(),
											fragment.getFechaInicFac(),
											fragment.getFechaFinFac(),
											fragment.isSoloFacturasConSaldo(),
											fragment.getEstadoFac()));
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

	}

	private void onLoadClienteDataFromServer() {
		try {
		   final String credentials = SessionManager.getCredentials();

			//final String credentials = "sa||nordis09||dp";

			if (!credentials.trim().equals("") && NMNetWork.CheckConnection(controller) ) {
				pool.execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(controller,
									Result.CLIENTE.getResult(),
									0, 
									0, 
									ModelLogic.getCuentasPorCobrarDelCliente(
											credentials,
											fragment.getSucursalId()));

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
														e.toString(),
														"\n Causa: "
																+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
