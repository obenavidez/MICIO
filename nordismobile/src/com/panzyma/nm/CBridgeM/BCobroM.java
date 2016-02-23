package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.bluetooth.BluetoothConnection;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelCobro;
import com.panzyma.nm.serviceproxy.CCobro;
import com.panzyma.nm.serviceproxy.CFormaPago;
import com.panzyma.nm.serviceproxy.CobroDetalle;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;

public class BCobroM extends BBaseM{

	protected String TAG= BCobroM.class.toString();
	public BCobroM(){}
	
	public enum Accion {
		COBROS_DEL_DIA(0), COBROS_DEL_SEMANA(1), COBROS_DEL_MES(2) , PAGOS_DEL_DIA(3), PAGOS_DE_SEMANA(4), PAGOS_DEL_MES(5) , IMPRIMIR(6);
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
		case  PAGOS_DEL_DIA : 
			LoadPagos(false, false, true, 0, 0, Accion.PAGOS_DEL_DIA);
			break;
		case  PAGOS_DE_SEMANA : 
			LoadPagos(false, false, true, 0, 0, Accion.PAGOS_DE_SEMANA);
			break;
		case  PAGOS_DEL_MES : 
			LoadPagos(false, false, true, 0, 0, Accion.PAGOS_DEL_MES);
			break;
		case IMPRIMIR: 
				ImprimirCobro((CobroDetalle)msg.obj);
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
	
	public void LoadPagos(final boolean delDia, final boolean deSemana, final boolean delMes, final int fechaInic, final int fechaFin, final Accion accion )
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
							
							ArrayList<CFormaPago> pagos = ModelCobro.getConsultaPagos(credentials,delDia,deSemana,delMes,fechaInic,fechaFin);
							Processor.notifyToView( getController(),accion.getActionCode(),0,0,pagos);
							
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

	
	public  void ImprimirCobro(CobroDetalle detalle ) 
	{   
		try
		{
			 String recibo = "";              
	         String msg = "";
	         
	         
	         //Encabezado del reporte
	         recibo += "T 7 0 0 2 Distribuidora Panzyma - DISPAN\r\n";        
	         recibo += "T 7 0 0 36 " + msg + "\r\n";  
	         recibo += "LINE 0 70 576 70 1 \r\n";
	         recibo += "T 7 0 0 80 Fecha:\r\n";
	         recibo += "T 7 0 115 80 " + DateUtil.idateToStr(DateUtil.getToday()) + "\r\n";
	         recibo += "T 7 0 0 110 Colector:\r\n";
	         
	         SharedPreferences pref = getContext().getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
	 		 String name = pref.getString("nombre", "");
	         recibo += "T 7 0 115 110 " + name + "\r\n";

	         recibo += "T 7 0 0 150 Recibo\r\n";
	         recibo += "T 7 0 110 150 Fecha\r\n";
	         recibo += "T 7 0 220 150 Cliente\r\n";
	         recibo += "RIGHT 576\r\n"; 
	         recibo += "T 7 0 0 149 Total\r\n";
	         recibo += "LEFT\r\n"; 
	         recibo += "LINE 1 175 576 175 1\r\n";
	         
	         int y = 180;
	         double total = 0;
	         
	         for (CCobro c : detalle.getCobros()) {
				total+= c.getTotalRecibo();
				//Número del recibo
                String numero = c.getReferencia();
                if (c.getReferencia().compareTo("") == 0) numero = c.getNumeroCentral();
                
                recibo += "T 7 0 0 " + y + " " + numero + "\r\n";
                recibo += "T 7 0 110 " + y + " " + c.getFecha() + "\r\n";
                
                String nombreCliente = c.getNombreCliente();
                if (nombreCliente.length() > 18) 
                    nombreCliente = nombreCliente.substring(0, 18);
                    
                recibo += "T 7 0 220 " + y + " " + nombreCliente + "\r\n";
                
                recibo += "RIGHT 576\r\n"; 
                recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(c.getTotalRecibo()) + "\r\n";
                recibo += "LEFT\r\n"; 
                
                y += 30;
			}
	         
	         recibo += "LINE 0 " + y + " 576 " + y + " 1\r\n";
	         y += 5;
	         recibo += "T 7 0 0 " + y + " " + detalle.getCobros().size() + " recibos pagados.\r\n";

	         recibo += "RIGHT 576\r\n"; 
	         total = StringUtil.round(total, 2);
	         recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(total) + "\r\n";
	         recibo += "LEFT\r\n"; 

	         y += 50;
	         
	         //Encabezado de detalles de pago
	         recibo += "T 7 0 0 " + y + " Forma de Pago\r\n";                        
	         recibo += "T 7 0 180 " + y + " Moneda\r\n";
	         recibo += "RIGHT 400\r\n"; 
	         recibo += "T 7 0 0 " + y + " Monto\r\n";            
	         recibo += "RIGHT 576\r\n"; 
	         recibo += "T 7 0 0 " + y + " Monto Nacional\r\n";
	         recibo += "LEFT\r\n"; 
	         y += 30;
	         recibo += "LINE 1 " + y + " 576 " + y + " 1\r\n";
	         y += 5;

	         //Detalle de pagos
	         total = 0;
	         for (CFormaPago p : detalle.getFormapagos()) {
	        	 total += p.getMontoNacional();
	                
	                recibo += "T 7 0 0 " + y + " " + p.getDescFormaPago() + "\r\n";            
	                recibo += "T 7 0 180 " + y + " " + p.getDescMoneda() + "\r\n";     
	                       
	                recibo += "RIGHT 400\r\n"; 
	                recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(p.getMonto()) + "\r\n";
	            
	                recibo += "RIGHT 576\r\n"; 
	                recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(p.getMontoNacional()) + "\r\n";
	                recibo += "LEFT\r\n"; 
	                y += 30;
			}
	         recibo += "LINE 1 " + y + " 576 " + y + " 1\r\n";
	         recibo += "RIGHT 576\r\n"; 
	         total = StringUtil.round(total, 2);
	         recibo += "T 7 0 0 " + y + " " + StringUtil.formatReal(total) + "\r\n";
	         recibo += "LEFT\r\n"; 

	         recibo += "FORM\r\n";
	         recibo += "PRINT\r\n";

	         y += 50; 

	         String header = "! 0 200 200 " + y + " 1\r\n";
	         header += "LABEL\r\n";
	         header += "CONTRAST 0\r\n";
	         header += "TONE 0\r\n";
	         header += "SPEED 3\r\n";
	         header += "PAGE-WIDTH 600\r\n";
	         header += "BAR-SENSE\r\n";

	         recibo = header + recibo;    
	         new BluetoothConnection(recibo); 
		}
		catch (Exception e) 
		 { 
			 NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
			Log.d(TAG,"ERROR al tratar de envia el recibo", e);
		}
	}
}
