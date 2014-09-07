package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.TasaCambio;
import com.panzyma.nm.serviceproxy.ValorCatalogo;

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
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
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
	
	/***
	 * Obtiene la informacion de catalogos y tasas de cambio para forma de pago
	 * @param cnt             Contexto de ejecucion
	 * @param fechaTasaCambio Fecha de recuperacion de tasas de cambio
	 * @param catalogsNames   Nombre de catalogos basicos a recuperar
	 * @return Map<String,List<Object>>
	 */
	public synchronized static Map<String,List<Object>> getDataFormaPago(Context cnt,int fechaTasaCambio, String... catalogsNames) {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT id , ");
		query.append("        codigo , ");
		query.append("        descripcion ");
		query.append(" FROM ValorCatalogo vc ");
		query.append(" WHERE vc.objCatalogoID = (  ");
		query.append("	 SELECT Id FROM CATALOGO c WHERE c.NombreCatalogo = '%s' ");
		query.append(" )   ");
		Map<String,List<Object>> objectResult = new HashMap<String, List<Object>>();
		
		StringBuilder query2 = new StringBuilder();
		query2.append(" SELECT Id , ");
		query2.append("        CodMoneda , ");
		query2.append("        Tasa ");
		query2.append(" FROM TasaCambio tc ");
		query2.append(String.format(" WHERE tc.Fecha = %d  ", fechaTasaCambio));
		List<Object> paridaCambiaria = null;
		SQLiteDatabase db = null;
		try {
			db = DatabaseProvider.Helper.getDatabase(cnt);
			Cursor c = null;
			List<Object> catalogos = null;
			for (String catalogName : catalogsNames) {
				Catalogo catalogo = new Catalogo(catalogName);
				List<ValorCatalogo> valoresCatalogo = new ArrayList<ValorCatalogo>();
				c = DatabaseProvider.query(db,
						String.format(query.toString(), catalogName));
				if (c.moveToFirst()) {
					// Recorremos el cursor hasta que no haya más registros
					do {
						valoresCatalogo.add(new ValorCatalogo(c.getInt(0), c
								.getString(1), c.getString(2)));
					} while (c.moveToNext());
					catalogo.setValoresCatalogo(valoresCatalogo);
					if (catalogos == null)
						catalogos = new ArrayList<Object>();
					catalogos.add(catalogo);
				}
			}
			
			c = DatabaseProvider.query( db, query2.toString());
			paridaCambiaria = new ArrayList<Object>();
			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do {
					paridaCambiaria.add(new TasaCambio(c.getString(1),
							fechaTasaCambio, c.getFloat(2)));
					
				} while (c.moveToNext());
			}	
			objectResult.put("basic", catalogos);
			objectResult.put("tasaCambio", paridaCambiaria);
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if (db != null) {
				if (db.isOpen())
					db.close();
			}
		}
		return objectResult;
	}

}
