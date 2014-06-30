package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import org.ksoap2.serialization.PropertyInfo;
import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.serviceproxy.CVenta;

public class ModelVenta {

	/**
	 * OBTENER LAS VENTAS DEL COLECTOR
	 * @param credenciales
	 * @param delDia
	 * @param deSemana
	 * @param deMes
	 * @param fechaInic
	 * @param fechaFin
	 * @return
	 */
	public synchronized static ArrayList<CVenta> getConsultaVentas(
			String credenciales, boolean delDia, boolean deSemana, boolean deMes, int fechaInic,
			int fechaFin) {
		Parameters params = new Parameters((new String[] { "Credentials",
				"delDia", "deSemana", "deMes", "fechaInic", "fechaFin"}),
				(new Object[] { credenciales, delDia, deSemana, deMes,
						fechaInic, fechaFin }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.BOOLEAN_CLASS,
						PropertyInfo.BOOLEAN_CLASS, PropertyInfo.BOOLEAN_CLASS,
						PropertyInfo.BOOLEAN_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.LONG_CLASS }));

		try {

			Object ventas = AppNMComunication.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.GetVentaVendedor);

			return NMTranslate.ToCollection(ventas, CVenta.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
