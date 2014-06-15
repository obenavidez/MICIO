package com.panzyma.nm.model;

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.serviceproxy.CCCliente;

public class ModelLogic {
	
	/**
	 * Obtiene las cuentas por cobrar del cliente
	 * @param credenciales: credenciales del colector 
	 * @param sucursalID  : sucursal del cliente
	 * @return CCCliente
	 */	
	public synchronized static CCCliente getCuentasPorCobrarDelCliente(
			String credenciales, long sucursalID) {
		
		Parameters params = new Parameters((new String[] { "Credentials",
				"idSucursal" }), (new Object[] { credenciales, sucursalID }),
				(new Type[] { PropertyInfo.STRING_CLASS,
						PropertyInfo.STRING_CLASS }));

		try {
			return (NMTranslate.ToObject((NMComunicacion.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.GetCCCliente)), new CCCliente()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
