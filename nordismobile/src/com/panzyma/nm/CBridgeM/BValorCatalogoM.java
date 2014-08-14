package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BVentaM.Petition;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelValorCatalogo;
import com.panzyma.nm.model.ModelVenta;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.viewdialog.EditFormaPago;

public class BValorCatalogoM {
	
	private Controller controller;
	private ThreadPool pool;
	private EditFormaPago view;
	private String catalogName = "";
	private String valorCatalogName = "";
	
	private static final String TAG = BValorCatalogoM.class.getSimpleName();
	
	public enum Petition {

		GET_VALORES_CATALOGO_BY_NAME(0);

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
	
	public BValorCatalogoM(){}
	
	public BValorCatalogoM(EditFormaPago view)
	{		
    	this.controller=((NMApp)view.getActivity().getApplicationContext()).getController();  
    	this.view = view; 
    	this.pool=((NMApp)view.getActivity().getApplicationContext()).getThreadPool();    	  	
    }	
	
	public boolean handleMessage(Message msg) throws Exception 
	{
		Petition request = Petition.toInt(msg.what);
		switch (request) 
		{  
			case GET_VALORES_CATALOGO_BY_NAME: 
				onLoadValorCatalogoListener(request);
				return true;
		}
		return false;
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
										view.getCatalogNameToFound())
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
