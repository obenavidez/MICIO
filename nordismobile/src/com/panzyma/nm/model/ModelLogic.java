package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.ksoap2.serialization.PropertyInfo;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.CCPedido;
import com.panzyma.nm.serviceproxy.CCReciboColector;
import com.panzyma.nm.serviceproxy.Factura;

public class ModelLogic {
	
	/**
	 * OBTENER CLIENTE POR SU IDENTIFICADOR DE SUCURSAL
	 * @param credenciales
	 * @param sucursalID
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

	/**
	 * OBTENER LAS FACTURAS DEL CLIENTE
	 * @param credenciales
	 * @param sucursalID
	 * @param fechaInic
	 * @param fechaFin
	 * @param soloConSaldo
	 * @param codEstado
	 * @return ArrayList<Factura>
	 */
	public synchronized static ArrayList<Factura> getFacturasCliente(
			String credenciales, long sucursalID, int fechaInic, int fechaFin,
			boolean soloConSaldo, String codEstado) {

		Parameters params = new Parameters((new String[] { "Credentials",
				"idSucursal", "fechaInic", "fechaFin",
				"mostrarTodasSucursales", "soloConSaldo", "codEstado" }),
				(new Object[] { credenciales, sucursalID, fechaInic, fechaFin,
						false, soloConSaldo, codEstado }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS, PropertyInfo.INTEGER_CLASS,
						PropertyInfo.BOOLEAN_CLASS, PropertyInfo.BOOLEAN_CLASS,
						PropertyInfo.STRING_CLASS }));

		try {
			
			Object facturas =  NMComunicacion.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.TraerFacturasCliente);			
					
			return NMTranslate.ToCollection(facturas, Factura.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * OBTENER LOS PEDIDOS DEL CLIENTE
	 * @param credenciales
	 * @param sucursalID
	 * @param fechaInic
	 * @param fechaFin
	 * @param soloConSaldo
	 * @param codEstado
	 * @return ArrayList<CCPedido>
	 */
	public synchronized static ArrayList<CCPedido> getPedidosCliente(
			String credenciales, long sucursalID, int fechaInic, int fechaFin,
			String codEstado) {

		Parameters params = new Parameters((new String[] { "Credentials",
				"idSucursal", "fechaInic", "fechaFin",
				"mostrarTodasSucursales", "codEstado" }),
				(new Object[] { credenciales, sucursalID, fechaInic, fechaFin,
						false, codEstado }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS, PropertyInfo.INTEGER_CLASS,
						PropertyInfo.BOOLEAN_CLASS, PropertyInfo.STRING_CLASS }));

		try {
			
			Object pedidos =  NMComunicacion.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.TraerPedidosCliente);			
					
			return NMTranslate.ToCollection(pedidos, CCPedido.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * OBTENER LOS RECIBOS DEL CLIENTE
	 * @param credenciales
	 * @param sucursalID
	 * @param fechaInic
	 * @param fechaFin
	 * @param soloConSaldo
	 * @param codEstado
	 * @return ArrayList<CCReciboColector>
	 */
	public synchronized static ArrayList<CCReciboColector> getRecibosColector(
			String credenciales, long sucursalID, int fechaInic, int fechaFin,
			String codEstado) {

		Parameters params = new Parameters((new String[] { "Credentials",
				"idSucursal", "fechaInic", "fechaFin",
				"mostrarTodasSucursales", "codEstado" }),
				(new Object[] { credenciales, sucursalID, fechaInic, fechaFin,
						false, codEstado }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS, PropertyInfo.INTEGER_CLASS,
						PropertyInfo.BOOLEAN_CLASS, PropertyInfo.STRING_CLASS }));

		try {
			
			Object recibos =  NMComunicacion.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.TraerRColCliente);			
					
			return NMTranslate.ToCollection(recibos, CCReciboColector.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * OBTENER NOTAS CREDITO DEL CLIENTE
	 * @param credenciales
	 * @param sucursalID
	 * @param fechaInic
	 * @param fechaFin
	 * @param codEstado
	 * @return ArrayList<CCNotaCredito>
	 */
	public synchronized static ArrayList<CCNotaCredito> getNotasCreditoCliente(
			String credenciales, long sucursalID, int fechaInic, int fechaFin,
			String codEstado) {

		Parameters params = new Parameters((new String[] { "Credentials",
				"idSucursal", "fechaInic", "fechaFin",
				"mostrarTodasSucursales", "codEstado" }),
				(new Object[] { credenciales, sucursalID, fechaInic, fechaFin,
						false, codEstado }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS, PropertyInfo.INTEGER_CLASS,
						PropertyInfo.BOOLEAN_CLASS, PropertyInfo.STRING_CLASS }));

		try {
			
			Object notasCredito =  NMComunicacion.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.TraerNotasDebitoCliente);			
					
			return NMTranslate.ToCollection(notasCredito, CCNotaCredito.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * OBTENER NOTAS DE DEBITO DEL CLIENTE
	 * @param credenciales
	 * @param sucursalID
	 * @param fechaInic
	 * @param fechaFin
	 * @param codEstado
	 * @return ArrayList<CCNotaDebito>
	 */
	public synchronized static ArrayList<CCNotaDebito> getNotasDebitoCliente (
			String credenciales, long sucursalID, int fechaInic, int fechaFin,
			String codEstado) {

		Parameters params = new Parameters((new String[] { "Credentials",
				"idSucursal", "fechaInic", "fechaFin",
				"mostrarTodasSucursales",  "codEstado" }),
				(new Object[] { credenciales, sucursalID, fechaInic, fechaFin,
						false, codEstado }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS, PropertyInfo.INTEGER_CLASS,
						PropertyInfo.BOOLEAN_CLASS, 
						PropertyInfo.STRING_CLASS }));

		try {
			
			Object notasDebito =  NMComunicacion.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.TraerNotasCreditoCliente);			
					
			return NMTranslate.ToCollection(notasDebito, CCNotaDebito.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
