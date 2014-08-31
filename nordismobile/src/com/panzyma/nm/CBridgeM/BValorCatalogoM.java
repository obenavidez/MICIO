package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.HashMap;
import java.util.Map;

import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelValorCatalogo;
import com.panzyma.nm.viewdialog.EditFormaPago;

public class BValorCatalogoM {
	
	private Controller controller;
	private ThreadPool pool;
	private EditFormaPago view;
	private Map<Integer, String> catalogos = new HashMap<Integer, String>();
	
	private static final String TAG = BValorCatalogoM.class.getSimpleName();
	
	public enum Petition {
		
		FORMAS_PAGOS(0),
		MONEDAS(1),
		BANCOS(2);

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
	
	public BValorCatalogoM(){ }
	
	public BValorCatalogoM(EditFormaPago view)
	{		
    	this.controller=((NMApp)view.getActivity().getApplicationContext()).getController();  
    	this.view = view; 
    	this.pool=((NMApp)view.getActivity().getApplicationContext()).getThreadPool(); 
    	catalogos.clear();
    	catalogos.put(0, "FormaPago");
		catalogos.put(1, "Moneda");
		catalogos.put(2, "EntidadBancaria");
    }	
	
	public boolean handleMessage(Message msg) throws Exception 
	{
		Petition request = Petition.toInt(msg.what);
		switch (request) 
		{  
			default: 
				onLoadValorCatalogoListener(request);
				return true;
		}
	}

	private void onLoadValorCatalogoListener(final Petition peticion) {
		try {
			pool.execute(new Runnable() {

				@Override
				public void run() {

					try {
						Processor.notifyToView(
								controller,
								peticion.getActionCode(),
								0,
								0,
								ModelValorCatalogo.getCatalogByName(
										view.getActivity(),
										catalogos.get(peticion.getActionCode()))
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
