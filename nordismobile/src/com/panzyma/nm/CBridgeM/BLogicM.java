package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CuentasPorCobrarFragment;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelProducto;

public class BLogicM {

	private Controller controller = null;
	private ThreadPool pool = null;
	private String TAG = BLogicM.class.getSimpleName();
	boolean OK = false;
	private CuentasPorCobrarFragment cuentasPorCobrarFragment = null;

	public BLogicM() {
	}

	public BLogicM(CuentasPorCobrarFragment cuentasPorCobrarFragment) {
		this.cuentasPorCobrarFragment = cuentasPorCobrarFragment;
		this.controller = ((NMApp) cuentasPorCobrarFragment.getActivity()
				.getApplication()).getController();
		this.pool = ((NMApp) cuentasPorCobrarFragment.getActivity()
				.getApplication()).getThreadPool();
	}

	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) {
		case LOAD_DATA_FROM_SERVER:
			onLoadALLDataFromServer();
			return true;

		}
		return false;
	}

	private void onLoadALLDataFromServer() {
		try {
			final String credentials = SessionManager.getCredenciales();

			if (!credentials.trim().equals("")) {
				pool.execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(controller,
									ControllerProtocol.C_DATA, 0, 0, ModelLogic
											.getCuentasPorCobrarDelCliente(
													credentials,
													cuentasPorCobrarFragment
															.getSucursalId()));

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
