package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;

import com.panzyma.nm.CBridgeM.BVentaM.Petition;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.model.ModelCobro;
import com.panzyma.nm.model.ModelVenta;
import com.panzyma.nm.serviceproxy.CCobro;

import android.os.Message;

public class BCobro extends BBaseM{

	public enum Accion {
		VENTAS_DEL_DIA(0), VENTAS_DEL_SEMANA(1), VENTAS_DEL_MES(2);
		int result;

		Accion(int result) {
			this.result = result;
		}

		public int getActionCode() {
			return result;
		}

		public static Petition toInt(int x) {
			
			return Petition.values()[x];
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) throws Exception {
		Petition request = Petition.toInt(msg.what);
		switch (request) {
		case VENTAS_DEL_DIA:
			LoadCobros(true, false, false, 0, 0, Accion.VENTAS_DEL_DIA);
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
