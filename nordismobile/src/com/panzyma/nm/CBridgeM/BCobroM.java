package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;

import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.model.ModelCobro;
import com.panzyma.nm.serviceproxy.CCobro;

import android.os.Message;

public class BCobroM extends BBaseM{

	
	public BCobroM(){}
	
	public enum Accion {
		COBROS_DEL_DIA(0), COBROS_DEL_SEMANA(1), COBROS_DEL_MES(2) , UPDATE_LISTVIEW_HEADER(888);
		int result;

		Accion(int result) {
			this.result = result;
		}

		public int getActionCode() {
			return result;
		}

		public static Accion toInt(int x) {
			
			return Accion.values()[x];
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) throws Exception {
		Accion request = Accion.toInt(msg.what);
		switch (request) {
		case COBROS_DEL_DIA:
			LoadCobros(true, false, false, 0, 0, Accion.COBROS_DEL_DIA);
			break;
		case COBROS_DEL_SEMANA:
			LoadCobros(false, true, false, 0, 0, Accion.COBROS_DEL_SEMANA);
			break;
		case COBROS_DEL_MES:
			LoadCobros(false, false, true, 0, 0, Accion.COBROS_DEL_MES);
			break;
		
		}
		return false;
	}
	
	
	public void LoadCobros(final boolean delDia, final boolean deSemana, final boolean delMes, final int fechaInic, final int fechaFin, final Accion accion )
	{
		try
		{
			final String credentials = SessionManager.getCredentials(); 

			if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
			{
				getPool().execute(new Runnable() {
					@Override
					public void run() {
						try {
							
							ArrayList<CCobro> cobros = ModelCobro.getConsultaCobro(credentials,delDia,deSemana,delMes,fechaInic,fechaFin);
							Processor.notifyToView( getController(),accion.getActionCode(),0,0,cobros);
							
						} catch (Exception e) {
							try {
								Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno en la sincronización con la BDD",e.toString(),"\n Causa: "+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			}
		}
		 catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

}
