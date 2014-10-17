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

	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) {
		case LOAD_DATA_FROM_SERVER:
			onLoadClienteDataFromServer(
					msg.getData().getLong("sucursalId")
					);
			return true;
		case ControllerProtocol.LOAD_FACTURASCLIENTE_FROM_SERVER:
			onLoadFacturasClienteFromServer(
					msg.getData().getLong("sucursalId"),
					msg.getData().getInt("fechaInic"),
					msg.getData().getInt("fechaFin"),
					msg.getData().getBoolean("soloConSaldo"),
					msg.getData().getString("estadoFac")
					);
			break;
		case ControllerProtocol.LOAD_NOTAS_DEBITO_FROM_SERVER:
			onLoadNotasDebitoClienteFromServer(
					msg.getData().getLong("sucursalId"),
					msg.getData().getInt("fechaInic"),
					msg.getData().getInt("fechaFin"),					
					msg.getData().getString("estadoND")
					);
			break;
		case ControllerProtocol.LOAD_NOTAS_CREDITO_FROM_SERVER:
			onLoadNotasCreditoClienteFromServer(
					msg.getData().getLong("sucursalId"),
					msg.getData().getInt("fechaInic"),
					msg.getData().getInt("fechaFin"),					
					msg.getData().getString("estadoNC")
					);
			break;
		case ControllerProtocol.LOAD_PEDIDOS_FROM_SERVER:
			onLoadPedidosClienteFromServer(
					msg.getData().getLong("sucursalId"),
					msg.getData().getInt("fechaInic"),
					msg.getData().getInt("fechaFin"),					
					msg.getData().getString("estadoPedidos")
					);
			break;
		case ControllerProtocol.LOAD_RECIBOS_FROM_SERVER:
			onLoadRecibosClienteFromServer(
					msg.getData().getLong("sucursalId"),
					msg.getData().getInt("fechaInic"),
					msg.getData().getInt("fechaFin"),					
					msg.getData().getString("estadoRecibos")
					);
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

	private void onLoadRecibosClienteFromServer(final long sucursalId, 
			final int fechaIni,
			final int fechaFin,			
			final String estado) {
		 final String credentials = SessionManager.getCredentials();

		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) {
			try {
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(getController(),
									Result.RECIBOS_COLECTOR.getResult(),
									0, 
									0,
									ModelLogic.getRecibosColector(credentials,
											sucursalId,
											fechaIni,
											fechaFin,
											estado));
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

	}

	private void onLoadPedidosClienteFromServer(final long sucursalId, 
			final int fechaIni,
			final int fechaFin,			
			final String estado) {
		final String credentials = SessionManager.getCredentials();
		
		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) {
			try {
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(getController(),
									Result.PEDIDOS.getResult(),
									0,
									0,
									ModelLogic.getPedidosCliente(credentials,
											sucursalId,
											fechaIni,
											fechaFin,
											estado));
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
	}

	private void onLoadNotasCreditoClienteFromServer(final long sucursalId, 
			final int fechaIni,
			final int fechaFin,			
			final String estado) {
		final String credentials = SessionManager.getCredentials();
		
		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) {
			try {
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(getController(),
									Result.NOTAS_CREDITO.getResult(),
									0,
									0,
									ModelLogic.getNotasCreditoCliente(
											credentials,
											sucursalId,
											fechaIni,
											fechaFin,
											estado));
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
	}

	private void onLoadNotasDebitoClienteFromServer(final long sucursalId, 
			final int fechaIni,
			final int fechaFin,			
			final String estado) {
		final String credentials = SessionManager.getCredentials();
		
		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) {
			try {
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(getController(),
									Result.NOTAS_DEBITO.getResult(),
									0,
									0,
									ModelLogic.getNotasDebitoCliente(
											credentials,
											sucursalId,
											fechaIni,
											fechaFin,
											estado));
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

	}

	private void onLoadFacturasClienteFromServer(final long sucursalId, 
			final int fechaIni,
			final int fechaFin,
			final boolean soloConSaldo,
			final String estado
			) {

		final String credentials = SessionManager.getCredentials();
		
		if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) {
			try {
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(getController(),
									Result.FACTURAS_CLIENTE.getResult(),
									0,
									0,
									ModelLogic.getFacturasCliente(credentials,
											sucursalId,
											fechaIni,
											fechaFin,
											soloConSaldo,
											estado));
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

	}

	private void onLoadClienteDataFromServer(final long sucursalId) {
		try {
		   final String credentials = SessionManager.getCredentials();

			if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) {
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(getController(),
									Result.CLIENTE.getResult(),
									0, 
									0, 
									ModelLogic.getCuentasPorCobrarDelCliente(
											credentials,
											sucursalId));

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
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
