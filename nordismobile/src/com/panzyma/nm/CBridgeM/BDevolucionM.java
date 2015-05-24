package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.HashMap;

import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelDevolucion;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelSolicitudDescuento;

import android.os.Message;
import android.util.Log;
@SuppressWarnings("unchecked")
public class BDevolucionM extends BBaseM{

	private String TAG=BDevolucionM.class.getSimpleName();

	@Override
	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) 
		{
			case ControllerProtocol.OBTENERVALORCATALOGO:
				OtenerValorCatalogo((String[]) msg.obj);
				break;
			case ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO:
				BuscarDevolucionDePedido(msg.obj);
				break;
			default:
				break;
		}
		return false;
	}

	private void OtenerValorCatalogo(String...valores)
	{
		try 
		{
			Processor.notifyToView(getController(),
					ControllerProtocol.OBTENERVALORCATALOGO,
					0,
					0,
					ModelLogic.getValorCatalogo(valores)
					);
		} catch (Exception e) 
		{ 
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(getController(),ERROR,0,0,new ErrorMessage("Error interno trayendo datos desde BDD",e.toString(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		} 
	}

    private void BuscarDevolucionDePedido(Object obj){
    	
    	
		HashMap<String,Long> parametros = (HashMap<String, Long>) obj;
    	long idsucursal,nopedido,nofactura; 
    	idsucursal=parametros.get("idsucursal");
    	nopedido=parametros.get("nopedido");
    	nofactura=parametros.get("nofactura");
    	
    	String credenciales="";
		
		credenciales = SessionManager.getCredentials(); 
		if(credenciales=="")
			return;
		try 
		{
			ModelDevolucion.BuscarDevolucionDePedido(credenciales, idsucursal, nopedido, nofactura);
			
		} catch (Exception e) 
		{ 
			e.printStackTrace();
		}
		  
    }
}