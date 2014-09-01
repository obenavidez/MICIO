package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.ksoap2.serialization.PropertyInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.datastore.DatabaseProvider.Helper;
/*import com.panzyma.nm.auxiliar.Parameters; by jrostran*/
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
			return (NMTranslate.ToObject((AppNMComunication.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.GetCCCliente)), new CCCliente()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/***
	 * OBTENER EL VALOR DE MONTO ABONADO PARA LA FACTURA EN OTROS RECIBOS
	 * @param view
	 * @param objFacturaId
	 * @return
	 */
	public synchronized static float getAbonosFacturaEnOtrosRecibos(Context view, long objFacturaId, long objReciboId){
		SQLiteDatabase bd = Helper.getDatabase(view);
		float montoAbonado = 0.00F;
		try {			
			StringBuilder sQuery = new StringBuilder();
			sQuery.append("SELECT SUM(monto) AS montoAbonado ");
			sQuery.append(" FROM ReciboDetalleFactura AS rdf ");
			sQuery.append("      INNER JOIN Recibo r ");
			sQuery.append("      ON  r.id = rdf.objReciboID ");
			sQuery.append(" WHERE rdf.objFacturaID = " + objFacturaId);
			sQuery.append("       AND rdf.objReciboID <> " + objReciboId);
			sQuery.append("       AND r.codEstado <> 'ANULADO' ");
			Cursor c = DatabaseProvider.query(bd, sQuery.toString());
			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya m�s registro(_db[0]==null)s
				do {				
					montoAbonado = (float)c.getInt(0);
				} while (c.moveToNext());
			}			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if( bd != null )
			{	
				if(bd.isOpen())				
					bd.close();
				bd = null;
			}
		} 
		return montoAbonado;
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
			
			Object facturas =  AppNMComunication.InvokeMethod(
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
			
			Object pedidos =  AppNMComunication.InvokeMethod(
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
			
			Object recibos =  AppNMComunication.InvokeMethod(
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
			
			Object notasCredito =  AppNMComunication.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.TraerNotasCreditoCliente);			
					
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
						true, codEstado }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS, PropertyInfo.INTEGER_CLASS,
						PropertyInfo.BOOLEAN_CLASS, 
						PropertyInfo.STRING_CLASS }));

		try {
			
			Object notasDebito =  AppNMComunication.InvokeMethod(
					params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
					NMConfig.MethodName.TraerNotasDebitoCliente);			
					
			return NMTranslate.ToCollection(notasDebito, CCNotaDebito.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
