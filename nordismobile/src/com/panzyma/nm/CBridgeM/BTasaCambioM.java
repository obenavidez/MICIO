package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BValorCatalogoM.Petition;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelTasaCambio;
import com.panzyma.nm.model.ModelValorCatalogo;
import com.panzyma.nm.viewdialog.EditFormaPago;

public class BTasaCambioM {

	private static final String TAG = BValorCatalogoM.class.getSimpleName();
	private Controller controller;
	private ThreadPool pool;
	private EditFormaPago view; 

	public enum Petition {

		TASA_CAMBIO(0);

		int result;

		Petition(int result) {
			this.result = result;
		}

		public int getActionCode() {
			return result;
		}

		public static Petition toInt(int x) {
			if (values().length > x)
				return Petition.values()[x];
			else
				return Petition.values()[0];
		}

	}

	public BTasaCambioM(EditFormaPago view) {
		this.controller = ((NMApp) view.getActivity().getApplicationContext())
				.getController();
		this.view = view;
		this.pool = ((NMApp) view.getActivity().getApplicationContext())
				.getThreadPool();
	}

	public boolean handleMessage(Message msg) throws Exception {
		Petition request = Petition.toInt(msg.what);
		switch (request) {
		case TASA_CAMBIO:
			onLoadTasaCambioListener(request);
			return true;
		}
		return false;
	}

	private void onLoadTasaCambioListener(final Petition request) {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {
						Processor.notifyToView(
								controller,
								request.getActionCode(),
								0,
								0,
								ModelTasaCambio.getTasaCambio(
										view.getActivity(),										
										view.getCodigoMoneda(),
										view.getFecha())
										);

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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
