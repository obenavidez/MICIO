package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;

import android.os.Message;
import android.util.Log;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.fragments.ConsultaVentasFragment;
import com.panzyma.nm.model.ModelVenta;
import com.panzyma.nm.serviceproxy.CDetalleFactura;

public class BVentaM extends BBaseM{
 
	private String TAG = BVentaM.class.getSimpleName();
	boolean OK = false;
	private ConsultaVentasFragment fragment = null;

	public enum Petition {

		VENTAS_DEL_DIA(0), VENTAS_DEL_SEMANA(1), VENTAS_DEL_MES(2) , OBTENER_DETALLE_FACTURA(3);

		int result;

		Petition(int result) {
			this.result = result;
		}

		public int getActionCode() {
			return result;
		}

		public static Petition toInt(int x) {
			
			return Petition.values()[x];
		}

	}

	public BVentaM() {
	}
 
	@Override
	public boolean handleMessage(Message msg) throws Exception 
	{
		Petition request = Petition.toInt(msg.what);
		switch (request) {
		case VENTAS_DEL_DIA:
			loadVentas(true, false, false, 0, 0, Petition.VENTAS_DEL_DIA);
			break;
		case VENTAS_DEL_SEMANA:
			loadVentas(false, true, false, 0, 0, Petition.VENTAS_DEL_SEMANA);
			break;
		case VENTAS_DEL_MES:
			loadVentas(false, false, true, 0, 0, Petition.VENTAS_DEL_MES);
			break;
		case OBTENER_DETALLE_FACTURA :
			getDetallefactura( Long.parseLong(msg.obj.toString()), Petition.OBTENER_DETALLE_FACTURA);
			break;
		}
		return false;
	}

	private void loadVentas(final boolean delDia, final boolean deSemana,
			final boolean delMes, final int fechaInic, final int fechaFin, final Petition peticion) {
		try
		{
			final String credentials = SessionManager.getCredentials(); 

			if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
			{
				getPool().execute(new Runnable() {

					@Override
					public void run() {

						try {
							Processor.notifyToView(
									getController(),
									peticion.getActionCode(),
									0,
									0,
									ModelVenta.getConsultaVentas(
											credentials,
											delDia,
											deSemana,
											delMes,
											fechaInic,
											fechaFin
											));

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
			else
				NMApp.getController()._notifyOutboxHandlers(0,0,0,0);
				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void  getDetallefactura( final long idFactura , final Petition peticion) throws Exception {
		
		
		try 
		{
			final String credentials = SessionManager.getCredentials(); 

			if (!credentials.trim().equals("") && NMNetWork.CheckConnection(getController()) ) 
			{
				getPool().execute(new Runnable() {
					@Override
					public void run() {
						try {
							ArrayList<CDetalleFactura> detalle = ModelVenta.getDetalleFactura(credentials,idFactura);
							Processor.notifyToView( getController(),peticion.getActionCode(),0,0,detalle);
						} 
						catch (Exception e) {
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
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
