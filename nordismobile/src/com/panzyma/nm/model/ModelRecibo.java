package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.comunicator.Parameters;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.datastore.DatabaseProvider.Helper;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.serviceproxy.RespuestaEnviarRecibo;
import com.panzyma.nm.viewmodel.vmRecibo;

public class ModelRecibo {

	private final static String logger = ModelRecibo.class.getSimpleName();

	public ModelRecibo() {
		super();
	}

	public synchronized static Object aplicarDescuentoOcacional(
			String credenciales, ReciboColector recibo) throws Exception {
		Parameters params = new Parameters((new String[] { "Credentials",
				"fecha", "idCliente", "idSucursal", "referencia" }),
				(new Object[] { credenciales,
						(int) DateUtil.dt2i(Calendar.getInstance().getTime()),
						recibo.getObjClienteID(), recibo.getObjSucursalID(),
						recibo.getReferencia() }), (new Type[] {
						PropertyInfo.STRING_CLASS, PropertyInfo.INTEGER_CLASS,
						PropertyInfo.LONG_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS }));
		Object rs = NMComunicacion.InvokeMethod(params.getParameters(),
				NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.VerificarAutorizacionDescuento);
		return rs;
	}

	public synchronized static RespuestaEnviarRecibo enviarRecibo(
			String credenciales, ReciboColector recibo) throws Exception {
		Parameters params = new Parameters(
				(new String[] { "Credentials", "r" }), (new Object[] {
						credenciales, recibo }), (new Type[] {
						PropertyInfo.STRING_CLASS, recibo.getClass() }));

		Object rs = NMComunicacion.InvokeMethod(params.getParameters(),
				NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.EnviarRecibo, ReciboColector.class);
		return NMTranslate.ToObject(rs, new RespuestaEnviarRecibo());
	}

	public synchronized static long solicitarDescuentoOcacional(
			String credenciales, ReciboColector recibo, String notas)
			throws Exception {
		Parameters params = new Parameters(
				(new String[] { "Credentials", "idCliente", "idSucursal",
						"referencia", "notas" }),
				(new Object[] { credenciales, recibo.getObjClienteID(),
						recibo.getObjSucursalID(), recibo.getReferencia(),
						notas }),
				(new Type[] { PropertyInfo.STRING_CLASS,
						PropertyInfo.LONG_CLASS, PropertyInfo.LONG_CLASS,
						PropertyInfo.INTEGER_CLASS, PropertyInfo.STRING_CLASS }));
		Object data = NMComunicacion.InvokeMethod(params.getParameters(),
				NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.SolicitarDescuento);
		SoapPrimitive rs = (SoapPrimitive) ((data != null) ? data : 0);
		return Long.parseLong(rs.toString());
	}

	public static Factura[] getFacturasByReciboDetalleFacturasList(
			List<ReciboDetFactura> list) {
		Factura[] resultList = new Factura[list.size()];
		int c = 0;

		SQLiteDatabase db = null;
		try {
			db = Helper.getDatabase(NMApp.ctx);

			for (ReciboDetFactura rdf : list) {
				Factura f = ModelDocumento.getFacturaByID(db,
						rdf.getObjFacturaID());
				resultList[c++] = f;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				if (db.isOpen())
					db.close();
				db = null;
			}
		}
		return resultList;
	}

	public static CCNotaCredito[] getNotasCreditoByReciboDetalleNCSList(
			List<ReciboDetNC> list) {
		CCNotaCredito[] resultList = new CCNotaCredito[list.size()];
		int c = 0;
		for (ReciboDetNC rdf : list) {
			CCNotaCredito f = ModelDocumento.getNotaCreditoByID(NMApp
					.getContext().getContentResolver(), rdf
					.getObjNotaCreditoID());
			resultList[c++] = f;
		}
		return resultList;
	}

	public static CCNotaDebito[] getNotasDebitoByReciboDetalleNDSList(
			List<ReciboDetND> list) {
		CCNotaDebito[] resultList = new CCNotaDebito[list.size()];
		int c = 0;
		for (ReciboDetND rdf : list) {
			CCNotaDebito f = ModelDocumento.getNotasDebitoByID(NMApp
					.getContext().getContentResolver(), rdf
					.getObjNotaDebitoID());
			resultList[c++] = f;
		}
		return resultList;
	}

	public static void updateFacturas(List<ReciboDetFactura> facturas,
			ContentResolver content, Context context) {
		SQLiteDatabase db = null;
		try {
			db = Helper.getDatabase(context);
			for (ReciboDetFactura factura : facturas) {

				Factura _factura = ModelDocumento.getFacturaByID(db,
						factura.getObjFacturaID());
				// Filtro de factura
				String mWhereClause = NMConfig.Cliente.Factura.Id + " = "
						+ factura.getObjFacturaID();
				// Columnas a actualizar
				ContentValues mUpdateValues = new ContentValues();
				float abonado = _factura.getAbonado() - factura.getMonto();
				String estado = "";
				String codEstado = "";
				if (abonado == 0.00) {
					estado = "Facturada";
					codEstado = "EMITIDA";
				}
				/*
				 * mUpdateValues.put(NMConfig.Cliente.Factura.Abonado, abonado);
				 * mUpdateValues.put(NMConfig.Cliente.Factura.Descontado,
				 * _factura.getDescontado() - factura.getDescuento());
				 * mUpdateValues.put(NMConfig.Cliente.Factura.Retenido,
				 * _factura.getRetenido() - factura.getRetencion());
				 * mUpdateValues.put(NMConfig.Cliente.Factura.Saldo,
				 * _factura.getTotalFacturado() - abonado);
				 * mUpdateValues.put(NMConfig.Cliente.Factura.Estado, estado);
				 * mUpdateValues.put(NMConfig.Cliente.Factura.CodEstado,
				 * codEstado); String uri = DatabaseProvider.CONTENT_URI_FACTURA
				 * +"/"+String.valueOf(factura.getObjFacturaID());
				 */

				StringBuilder query = new StringBuilder();
				query.append(" UPDATE Factura ");
				query.append(" SET Abonado    = %f   , ");
				query.append(" 	   Descontado = %f   , ");
				query.append(" 	   Retenido   = %f   , ");
				query.append(" 	   saldo      = %f   , ");
				query.append(" 	   Estado     = '%s' , ");
				query.append(" 	   CodEstado  = '%s' ");
				query.append(" WHERE id = ( ");
				query.append(" 				SELECT distinct f.id");
				query.append(" 				FROM   Factura f");
				query.append("        				INNER JOIN ReciboDetalleFactura rdf");
				query.append("        				ON f.Id = rdf.objFacturaID");
				query.append(" 				WHERE rdf.objReciboID = %d and f.id = %d ");
				query.append(" ) ");

				Object[] params = { abonado,
						(_factura.getDescontado() - factura.getDescuento()),
						(_factura.getDescontado() - factura.getDescuento()),
						(_factura.getTotalFacturado() - abonado), estado,
						codEstado, factura.getObjReciboID(),
						factura.getObjFacturaID() };

				db.execSQL(String.format(query.toString(), params));
				// Actualizar Factura
				/*
				 * db.update(DatabaseProvider.TABLA_FACTURA, mUpdateValues,
				 * mWhereClause, null);
				 */

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				if (db.isOpen())
					db.close();
				db = null;
			}
		}

	}

	public static boolean factEnRecibo(ReciboDetFactura factura,
			ArrayList<ReciboDetFactura> recfaturas) {
		boolean continuar = false;

		for (ReciboDetFactura recfact : recfaturas) {
			if (factura.getObjFacturaID() == recfact.getObjFacturaID()) {
				continuar = true;
				break;
			}

		}
		return continuar;
	}

	public static void updateFacturas(long reciboID,
			List<ReciboDetFactura> facturas, ContentResolver content,
			Context context) {
		SQLiteDatabase db = null;
		try {

			db = DatabaseProvider.Helper.getDatabase(context);
			ArrayList<ReciboDetFactura> recfaturas = getFacturasDelRecibo(db,
					reciboID);
			boolean continuar = false;

			for (ReciboDetFactura factura : facturas) {

				if (!factEnRecibo(factura, recfaturas))
					continue;

				Factura _factura = ModelDocumento.getFacturaByID(db,
						factura.getObjFacturaID());
				// Filtro de factura
				String mWhereClause = NMConfig.Cliente.Factura.Id + " = "
						+ factura.getObjFacturaID();
				// Columnas a actualizar
				ContentValues mUpdateValues = new ContentValues();
				float abonado = _factura.getAbonado() - factura.getMonto();
				String estado = "";
				String codEstado = "";
				if (abonado == 0.00) {
					estado = "Facturada";
					codEstado = "EMITIDA";
				}
				mUpdateValues.put(NMConfig.Cliente.Factura.Abonado, abonado);
				mUpdateValues.put(NMConfig.Cliente.Factura.Descontado,
						_factura.getDescontado() - factura.getDescuento());
				mUpdateValues.put(NMConfig.Cliente.Factura.Retenido,
						_factura.getRetenido() - factura.getRetencion());
				mUpdateValues.put(NMConfig.Cliente.Factura.Saldo,
						_factura.getTotalFacturado() - abonado);
				mUpdateValues.put(NMConfig.Cliente.Factura.Estado, estado);
				mUpdateValues
						.put(NMConfig.Cliente.Factura.CodEstado, codEstado);

				// String uri = DatabaseProvider.CONTENT_URI_FACTURA
				// +"/"+String.valueOf(factura.getObjFacturaID());

				// Actualizar Factura
				db.update(DatabaseProvider.TABLA_FACTURA, mUpdateValues,
						mWhereClause, null);
				continuar = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				if (db.isOpen())
					db.close();
				db = null;
			}
		}

	}

	public static void updateNotasCredito(List<ReciboDetNC> notasCredito,
			ContentResolver content, Context context) {
		for (ReciboDetNC notaCredito : notasCredito) {

			CCNotaCredito _notaCredito = ModelDocumento.getNotaCreditoByID(
					content, notaCredito.getObjNotaCreditoID());
			// Filtro de factura
			String mWhereClause = NMConfig.Cliente.CCNotaCredito.Id + " = "
					+ notaCredito.getObjNotaCreditoID();
			// Columnas a actualizar
			ContentValues mUpdateValues = new ContentValues();
			float abonado = _notaCredito.getMonto() - notaCredito.getMonto();
			String estado = "";
			String codEstado = "";
			if (abonado == 0.00) {
				estado = "Autorizada";
				codEstado = "AUTORIZADA";
			}
			// mUpdateValues.put(NMConfig.Cliente.CCNotaCredito.Monto, abonado);
			mUpdateValues.put(NMConfig.Cliente.CCNotaCredito.Estado, estado);
			mUpdateValues.put(NMConfig.Cliente.CCNotaCredito.CodEstado,
					codEstado);

			String uri = DatabaseProvider.CONTENT_URI_CCNOTACREDITO + "/"
					+ String.valueOf(notaCredito.getObjNotaCreditoID());
			SQLiteDatabase db = DatabaseProvider.Helper.getDatabase(context);

			// Actualizar Factura
			db.update(DatabaseProvider.TABLA_CCNOTACREDITO, mUpdateValues,
					mWhereClause, null);
			
			db.close();

		}
	}

	public static void updateNotasDebito(List<ReciboDetND> notasDebito,
			ContentResolver content, Context context) {
		for (ReciboDetND notaDebito : notasDebito) {

			CCNotaDebito _notaDebito = ModelDocumento.getNotasDebitoByID(
					content, notaDebito.getObjNotaDebitoID());
			// Filtro de factura
			String mWhereClause = NMConfig.Cliente.CCNotaDebito.Id + " = "
					+ notaDebito.getObjNotaDebitoID();
			// Columnas a actualizar
			ContentValues mUpdateValues = new ContentValues();
			float abonado = _notaDebito.getMontoAbonado()
					- notaDebito.getMontoPagar();
			float saldo = _notaDebito.getMonto() - abonado;
			mUpdateValues.put(NMConfig.Cliente.CCNotaDebito.MontoAbonado,
					abonado);
			mUpdateValues.put(NMConfig.Cliente.CCNotaDebito.Saldo, saldo);

			String uri = DatabaseProvider.CONTENT_URI_CCNOTADEBITO + "/"
					+ String.valueOf(notaDebito.getObjNotaDebitoID());
			SQLiteDatabase db = DatabaseProvider.Helper.getDatabase(context);
			// Actualizar Factura
			db.update(DatabaseProvider.TABLA_CCNOTADEBITO, mUpdateValues,
					mWhereClause, null);
			db.close();

		}
	}

	public synchronized static void updateRecibo(ReciboColector recibo,
			Context cnt) {
		SQLiteDatabase bdd = null;
		try {
			// OBTENER LA REFERENCIA A LA BASE DE DATOS
			bdd = Helper.getDatabase(cnt);
			// ABRIENDO LA CONEXION
			bdd.beginTransaction();
			// DEFINIENDO LA CONSULTA
			StringBuilder sQuery = new StringBuilder();
			sQuery.append("UPDATE Recibo ");
			sQuery.append(String.format("	SET totalRecibo     = %s, ",
					recibo.getTotalRecibo()));
			sQuery.append(String.format("	    totalFacturas   = %s, ",
					recibo.getTotalFacturas()));
			sQuery.append(String.format("	    totalND         =  %s , ",
					recibo.getTotalND()));
			sQuery.append(String.format("	    totalInteres    =  %s , ",
					recibo.getTotalInteres()));
			sQuery.append(String.format("	    subTotal        =  %s , ",
					recibo.getSubTotal()));
			sQuery.append(String.format("	    totalDesc       =  %s , ",
					recibo.getTotalDesc()));
			sQuery.append(String.format("	    totalRetenido   =  %s , ",
					recibo.getTotalRetenido()));
			sQuery.append(String.format("	    totalOtrasDed   =  %s , ",
					recibo.getTotalOtrasDed()));
			sQuery.append(String.format("	    totalNC   =  %s , ",
					recibo.getTotalNC()));
			sQuery.append(String.format("	    aplicaDescOca   =  %s , ",
					recibo.isAplicaDescOca() ? 255 : 0));
			sQuery.append(String.format("	    porcDescOcaColector   =  %s , ",
					recibo.getPorcDescOcaColector()));
			sQuery.append(String.format("	    totalDescOca   =  %s , ",
					recibo.getTotalDescOca()));
			sQuery.append(String.format("	    totalDescPromo   =  %s , ",
					recibo.getTotalDescPromo()));
			sQuery.append(String.format("	    totalDescPP   =  %s , ",
					recibo.getTotalDescPP()));
			sQuery.append(String.format(
					"	    totalImpuestoProporcional   =  %s , ",
					recibo.getTotalImpuestoProporcional()));
			sQuery.append(String.format(
					"	    totalImpuestoExonerado   =  %s , ",
					recibo.getTotalImpuestoExonerado()));
			sQuery.append(String.format("	    exento   =  %s  ",
					recibo.isExento() ? 255 : 0));
			sQuery.append(String.format("	WHERE Id       =  %d  ",
					recibo.getId()));
			// EJECUTAR LA CONSULTA
			bdd.execSQL(sQuery.toString());
			// CONFIRMAR LA TRANSACCION
			bdd.setTransactionSuccessful();
			if (bdd != null || (bdd.isOpen())) {
				// CERRAR LA CONEXION
				bdd.endTransaction();
				bdd.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public synchronized static void deleteDocument(int type, long l,
			long reciboId, Context context) {
		String[] projection = new String[] {};
		String tabla = "";
		String where = "id = " + l + " and  objReciboID= " + reciboId;
		switch (type) {
		case 10:
			tabla = DatabaseProvider.TABLA_RECIBO_DETALLE_FACTURA;
			break;
		case 20:
			tabla = DatabaseProvider.TABLA_RECIBO_DETALLE_NOTA_DEBITO;
			break;
		case 30:
			tabla = DatabaseProvider.TABLA_RECIBO_DETALLE_NOTA_CREDITO;
			break;
		}
		SQLiteDatabase bdd = null;
		try {
			bdd = Helper.getDatabase(context);
			bdd.beginTransaction();
			// BORRAR LOS DETALLES DE LAS FACTURAS DEL RECIBO
			bdd.delete(tabla, where, null);
			bdd.delete(
					DatabaseProvider.TABLA_RECIBO_DETALLE_FORMA_PAGO,
					NMConfig.Recibo.DetalleFactura.RECIBO_ID + " = " + reciboId,
					null);
			bdd.setTransactionSuccessful();
			if (bdd != null || (bdd.isOpen())) {
				bdd.endTransaction();
				bdd.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static int borraReciboByID(ContentResolver content,
			int reciboID, Context context) {
		String[] projection = new String[] {};

		int result = 0;
		ReciboColector recibo = getReciboByRef(content, reciboID);
		// SI EL RECIBO AUN NO HA SIDO ENVIADO
		if (recibo.getNumero() == 0) {
			// ACTUALIZAR LOS DOCUMENTOS PENDIENTES DEL CLIENTE QUE ESTAN LOCAL
			updateFacturas(recibo.getFacturasRecibo(), content, context);
			updateNotasCredito(recibo.getNotasCreditoRecibo(), content, context);
			updateNotasDebito(recibo.getNotasDebitoRecibo(), content, context);
		}
		String url = DatabaseProvider.CONTENT_URI_RECIBODETALLEFACTURA + "/"
				+ String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		url = DatabaseProvider.CONTENT_URI_RECIBODETALLENOTADEBITO + "/"
				+ String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		url = DatabaseProvider.CONTENT_URI_RECIBODETALLENOTACREDITO + "/"
				+ String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		url = DatabaseProvider.CONTENT_URI_RECIBODETALLEFORMAPAGO + "/"
				+ String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		url = DatabaseProvider.CONTENT_URI_RECIBO + "/"
				+ String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		result = 1;
		return result;
	}

	public synchronized static ReciboColector getReciboByRef(
			ContentResolver content, Integer ref) {
		String[] projection = new String[] { NMConfig.Recibo.ID,
				NMConfig.Recibo.NUMERO, NMConfig.Recibo.FECHA,
				NMConfig.Recibo.NOTAS, NMConfig.Recibo.TOTAL_RECIBO,
				NMConfig.Recibo.TOTAL_FACTURAS,
				NMConfig.Recibo.TOTAL_NOTAS_DEBITO,
				NMConfig.Recibo.TOTAL_INTERES, NMConfig.Recibo.SUBTOTAL,
				NMConfig.Recibo.TOTAL_DESCUENTO,
				NMConfig.Recibo.TOTAL_RETENIDO,
				NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES,
				NMConfig.Recibo.TOTAL_NOTAS_CREDITO,
				NMConfig.Recibo.REFERENCIA, NMConfig.Recibo.CLIENTE_ID,
				NMConfig.Recibo.SUCURSAL_ID, NMConfig.Recibo.NOMBRE_CLIENTE,
				NMConfig.Recibo.COLECTOR_ID,
				NMConfig.Recibo.APLICA_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL,
				NMConfig.Recibo.PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR,
				NMConfig.Recibo.ESTADO_ID, NMConfig.Recibo.CODIGO_ESTADO,
				NMConfig.Recibo.DESCRICION_ESTADO,
				NMConfig.Recibo.TOTAL_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.TOTAL_DESCUENTO_PROMOCION,
				NMConfig.Recibo.TOTAL_DESCUENTO_PRONTO_PAGO,
				NMConfig.Recibo.TOTAL_IMPUESTO_PROPORCIONAL,
				NMConfig.Recibo.TOTAL_IMPUESTO_EXONERADO,
				NMConfig.Recibo.EXENTO, NMConfig.Recibo.AUTORIZA_DGI };
		
		String projection1 =NMConfig.Recibo.ID+","+
				NMConfig.Recibo.NUMERO+","+ NMConfig.Recibo.FECHA+","+
				NMConfig.Recibo.NOTAS+","+ NMConfig.Recibo.TOTAL_RECIBO+","+
				NMConfig.Recibo.TOTAL_FACTURAS+","+
				NMConfig.Recibo.TOTAL_NOTAS_DEBITO+","+
				NMConfig.Recibo.TOTAL_INTERES+","+ NMConfig.Recibo.SUBTOTAL+","+
				NMConfig.Recibo.TOTAL_DESCUENTO+","+
				NMConfig.Recibo.TOTAL_RETENIDO+","+
				NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES+","+
				NMConfig.Recibo.TOTAL_NOTAS_CREDITO+","+
				NMConfig.Recibo.REFERENCIA+","+ NMConfig.Recibo.CLIENTE_ID+","+
				NMConfig.Recibo.SUCURSAL_ID+","+ NMConfig.Recibo.NOMBRE_CLIENTE+","+
				NMConfig.Recibo.COLECTOR_ID+","+
				NMConfig.Recibo.APLICA_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL+","+
				NMConfig.Recibo.PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR+","+
				NMConfig.Recibo.ESTADO_ID+","+ NMConfig.Recibo.CODIGO_ESTADO+","+
				NMConfig.Recibo.DESCRICION_ESTADO+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_PROMOCION+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_PRONTO_PAGO+","+
				NMConfig.Recibo.TOTAL_IMPUESTO_PROPORCIONAL+","+
				NMConfig.Recibo.TOTAL_IMPUESTO_EXONERADO+","+
				NMConfig.Recibo.EXENTO+","+ NMConfig.Recibo.AUTORIZA_DGI;
		
		ReciboColector recibo = null;
		SQLiteDatabase db = null;
		try {

			// OBTENIENDO LAS FACTURAS
			db = Helper.getDatabase(NMApp.ctx);
			StringBuilder sQuery = new StringBuilder();

			sQuery.append(" SELECT "+ projection1.toString());
			sQuery.append(" FROM Recibo AS r ");
			sQuery.append(" WHERE referencia= " + String.valueOf(ref));

			Cursor cur = DatabaseProvider.query(db, sQuery.toString());

			if (cur.moveToFirst()) {
				recibo = new ReciboColector();
				do {
					recibo.setId(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					recibo.setNumero(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[1]))));
					recibo.setFecha(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[2]))));
					recibo.setNotas(cur.getString(cur
							.getColumnIndex(projection[3])));
					recibo.setTotalRecibo(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[4]))));
					recibo.setTotalFacturas(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[5]))));
					recibo.setTotalND(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[6]))));
					recibo.setTotalInteres(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[7]))));
					recibo.setSubTotal(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[8]))));
					recibo.setTotalDesc(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[9]))));
					recibo.setTotalRetenido(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[10]))));
					recibo.setTotalOtrasDed(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[11]))));
					recibo.setTotalNC(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[12]))));
					recibo.setReferencia(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[13]))));
					recibo.setObjClienteID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[14]))));
					recibo.setObjSucursalID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[15]))));
					recibo.setNombreCliente(cur.getString(cur
							.getColumnIndex(projection[16])));
					recibo.setObjColectorID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[17]))));
					boolean aplicaDescuento = !(Integer.parseInt(cur
							.getString(cur.getColumnIndex(projection[18]))) == 0);
					recibo.setAplicaDescOca(aplicaDescuento);
					recibo.setClaveAutorizaDescOca(cur.getString(cur
							.getColumnIndex(projection[19])));
					recibo.setPorcDescOcaColector(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[20]))));
					recibo.setObjEstadoID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[21]))));
					recibo.setCodEstado(cur.getString(cur
							.getColumnIndex(projection[22])));
					recibo.setDescEstado(cur.getString(cur
							.getColumnIndex(projection[23])));
					recibo.setTotalDescOca(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[24]))));
					recibo.setTotalDescPromo(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[25]))));
					recibo.setTotalDescPP(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[26]))));
					recibo.setTotalImpuestoProporcional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[27]))));
					recibo.setTotalImpuestoExonerado(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[28]))));
					boolean exento = !(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[29]))) == 0);
					recibo.setExento(exento);
					recibo.setAutorizacionDGI(cur.getString(cur
							.getColumnIndex(projection[30])));

				} while (cur.moveToNext());

				// OBTENER EL CLIENTE DEL RECIBO
				Cliente cliente = ModelCliente.getClienteBySucursalID(db,
						recibo.getObjSucursalID(), recibo.getId());
				recibo.setCliente(cliente);

			}
			recibo.setFacturasRecibo(getFacturasDelRecibo(db, recibo.getId()));
			recibo.setNotasDebitoRecibo(getNotasDebitoDelRecibo(db,
					recibo.getId()));
			recibo.setNotasCreditoRecibo(getNotasCreitoDelRecibo(db,
					recibo.getId()));
			recibo.setFormasPagoRecibo(getFormasPagoDelRecibo(db,
					recibo.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				if (db.isOpen())
					db.close();
				db = null;
			}
		}

		return recibo;
	}

	public synchronized static ReciboDetFactura getFacturaRecibo(
			ContentResolver content, long facturaID) {

		String[] projection = new String[] {
				NMConfig.Recibo.DetalleFactura.ID,
				NMConfig.Recibo.DetalleFactura.FACTURA_ID,
				NMConfig.Recibo.DetalleFactura.RECIBO_ID,
				NMConfig.Recibo.DetalleFactura.MONTO,
				NMConfig.Recibo.DetalleFactura.ESABONO,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_ESPECIFICO,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.DetalleFactura.MONTO_RETENCION,
				NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO,
				NMConfig.Recibo.DetalleFactura.MONTO_INTERES,
				NMConfig.Recibo.DetalleFactura.MONTO_NETO,
				NMConfig.Recibo.DetalleFactura.MONTO_OTRAS_DEDUCCIONES,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_PROMOCION,
				NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_PROMOCION,
				NMConfig.Recibo.DetalleFactura.NUMERO,
				NMConfig.Recibo.DetalleFactura.FECHA,
				NMConfig.Recibo.DetalleFactura.FECHA_VENCE,
				NMConfig.Recibo.DetalleFactura.FECHA_APLICA_DESCUENTO_PRONTO_PAGO,
				NMConfig.Recibo.DetalleFactura.SUB_TOTAL,
				NMConfig.Recibo.DetalleFactura.IMPUESTO,
				NMConfig.Recibo.DetalleFactura.TOTAL_FACTURA,
				NMConfig.Recibo.DetalleFactura.SALDO_FACTURA,
				NMConfig.Recibo.DetalleFactura.INTERES_MORATORIO,
				NMConfig.Recibo.DetalleFactura.SALDO_TOTAL,
				NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO_EXONERADO,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_ESPECIFICO_CALCULADO };
		ReciboDetFactura detalleFactura = null;
		try {
			String uriString = DatabaseProvider.CONTENT_URI_RECIBODETALLEFACTURA
					.toString();
			Cursor cur = content.query(Uri.parse(uriString), projection, // Columnas
																			// a
																			// devolver
					"id = " + String.valueOf(facturaID), // Condición de la
															// query
					null, // Argumentos variables de la query
					null);
			if (cur.moveToFirst()) {
				do {
					detalleFactura = new ReciboDetFactura();
					detalleFactura.setId(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					detalleFactura.setObjFacturaID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[1]))));
					detalleFactura.setObjReciboID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[2]))));
					detalleFactura.setMonto(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[3]))));
					boolean esAbono = (Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[4]))) == 255);
					detalleFactura.setEsAbono(esAbono);
					detalleFactura.setMontoDescEspecifico(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[5]))));
					detalleFactura.setMontoDescOcasional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[6]))));
					detalleFactura.setMontoRetencion(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[7]))));
					detalleFactura.setMontoImpuesto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[8]))));
					detalleFactura.setMontoInteres(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[9]))));
					detalleFactura.setMontoNeto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[10]))));
					detalleFactura.setMontoOtrasDeducciones(Float
							.parseFloat(cur.getString(cur
									.getColumnIndex(projection[11]))));
					detalleFactura.setMontoDescPromocion(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[12]))));
					detalleFactura.setPorcDescOcasional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[13]))));
					detalleFactura.setPorcDescPromo(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[14]))));
					detalleFactura.setNumero(cur.getString(cur
							.getColumnIndex(projection[15])));
					detalleFactura.setFecha(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[16]))));
					detalleFactura.setFechaVence(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[17]))));
					detalleFactura.setFechaAplicaDescPP(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[18]))));
					detalleFactura.setSubTotal(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[19]))));
					detalleFactura.setImpuesto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[20]))));
					detalleFactura.setTotalFactura(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[21]))));
					detalleFactura.setSaldoFactura(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[22]))));
					detalleFactura.setInteresMoratorio(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[23]))));
					detalleFactura.setSaldoTotal(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[24]))));
					detalleFactura.setMontoImpuestoExento(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[25]))));
					// detalleFactura.setMontoImpuestoExento(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[26]))));

				} while (cur.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return detalleFactura;
	}

	public synchronized static ArrayList<ReciboDetFactura> getFacturasDelRecibo(
			SQLiteDatabase db, long reciboID) {
		ArrayList<ReciboDetFactura> facturas = new ArrayList<ReciboDetFactura>();
		String[] projection = new String[] {
				NMConfig.Recibo.DetalleFactura.ID,
				NMConfig.Recibo.DetalleFactura.FACTURA_ID,
				NMConfig.Recibo.DetalleFactura.RECIBO_ID,
				NMConfig.Recibo.DetalleFactura.MONTO,
				NMConfig.Recibo.DetalleFactura.ESABONO,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_ESPECIFICO,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.DetalleFactura.MONTO_RETENCION,
				NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO,
				NMConfig.Recibo.DetalleFactura.MONTO_INTERES,
				NMConfig.Recibo.DetalleFactura.MONTO_NETO,
				NMConfig.Recibo.DetalleFactura.MONTO_OTRAS_DEDUCCIONES,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_PROMOCION,
				NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_PROMOCION,
				NMConfig.Recibo.DetalleFactura.NUMERO,
				NMConfig.Recibo.DetalleFactura.FECHA,
				NMConfig.Recibo.DetalleFactura.FECHA_VENCE,
				NMConfig.Recibo.DetalleFactura.FECHA_APLICA_DESCUENTO_PRONTO_PAGO,
				NMConfig.Recibo.DetalleFactura.SUB_TOTAL,
				NMConfig.Recibo.DetalleFactura.IMPUESTO,
				NMConfig.Recibo.DetalleFactura.TOTAL_FACTURA,
				NMConfig.Recibo.DetalleFactura.SALDO_FACTURA,
				NMConfig.Recibo.DetalleFactura.INTERES_MORATORIO,
				NMConfig.Recibo.DetalleFactura.SALDO_TOTAL,
				NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO_EXONERADO,
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_ESPECIFICO_CALCULADO };
		
		
		String projection1 =
				NMConfig.Recibo.DetalleFactura.ID+","+
				NMConfig.Recibo.DetalleFactura.FACTURA_ID+","+
				NMConfig.Recibo.DetalleFactura.RECIBO_ID+","+
				NMConfig.Recibo.DetalleFactura.MONTO+","+
				NMConfig.Recibo.DetalleFactura.ESABONO+","+
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_ESPECIFICO+","+
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.DetalleFactura.MONTO_RETENCION+","+
				NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO+","+
				NMConfig.Recibo.DetalleFactura.MONTO_INTERES+","+
				NMConfig.Recibo.DetalleFactura.MONTO_NETO+","+
				NMConfig.Recibo.DetalleFactura.MONTO_OTRAS_DEDUCCIONES+","+
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_PROMOCION+","+
				NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_PROMOCION+","+
				NMConfig.Recibo.DetalleFactura.NUMERO+","+
				NMConfig.Recibo.DetalleFactura.FECHA+","+
				NMConfig.Recibo.DetalleFactura.FECHA_VENCE+","+
				NMConfig.Recibo.DetalleFactura.FECHA_APLICA_DESCUENTO_PRONTO_PAGO+","+
				NMConfig.Recibo.DetalleFactura.SUB_TOTAL+","+
				NMConfig.Recibo.DetalleFactura.IMPUESTO+","+
				NMConfig.Recibo.DetalleFactura.TOTAL_FACTURA+","+
				NMConfig.Recibo.DetalleFactura.SALDO_FACTURA+","+
				NMConfig.Recibo.DetalleFactura.INTERES_MORATORIO+","+
				NMConfig.Recibo.DetalleFactura.SALDO_TOTAL+","+
				NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO_EXONERADO+","+
				NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_ESPECIFICO_CALCULADO;
		
		ReciboDetFactura detalleFactura = null;
		try {

			StringBuilder sQuery = new StringBuilder();
			sQuery.append(" SELECT "+projection1.toString());
			sQuery.append(" FROM ReciboDetalleFactura AS df ");
			sQuery.append(" WHERE df.objReciboID= " + String.valueOf(reciboID));

			Cursor cur = DatabaseProvider.query(db, sQuery.toString());
			if (cur.moveToFirst()) {
				do {
					detalleFactura = new ReciboDetFactura();
					detalleFactura.setId(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					detalleFactura.setObjFacturaID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[1]))));
					detalleFactura.setObjReciboID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[2]))));
					detalleFactura.setMonto(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[3]))));
					boolean esAbono = (Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[4]))) == 255);
					detalleFactura.setEsAbono(esAbono);
					detalleFactura.setMontoDescEspecifico(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[5]))));
					detalleFactura.setMontoDescOcasional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[6]))));
					detalleFactura.setMontoRetencion(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[7]))));
					detalleFactura.setMontoImpuesto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[8]))));
					detalleFactura.setMontoInteres(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[9]))));
					detalleFactura.setMontoNeto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[10]))));
					detalleFactura.setMontoOtrasDeducciones(Float
							.parseFloat(cur.getString(cur
									.getColumnIndex(projection[11]))));
					detalleFactura.setMontoDescPromocion(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[12]))));
					detalleFactura.setPorcDescOcasional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[13]))));
					detalleFactura.setPorcDescPromo(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[14]))));
					detalleFactura.setNumero(cur.getString(cur
							.getColumnIndex(projection[15])));
					detalleFactura.setFecha(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[16]))));
					detalleFactura.setFechaVence(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[17]))));
					detalleFactura.setFechaAplicaDescPP(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[18]))));
					detalleFactura.setSubTotal(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[19]))));
					detalleFactura.setImpuesto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[20]))));
					detalleFactura.setTotalFactura(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[21]))));
					detalleFactura.setSaldoFactura(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[22]))));
					detalleFactura.setInteresMoratorio(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[23]))));
					detalleFactura.setSaldoTotal(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[24]))));
					detalleFactura.setMontoImpuestoExento(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[25]))));
					// detalleFactura.setMontoImpuestoExento(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[26]))));
					facturas.add(detalleFactura);
				} while (cur.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return facturas;
	}

	public synchronized static ArrayList<ReciboDetND> getNotasDebitoDelRecibo(
			SQLiteDatabase db, long reciboID) {

		ArrayList<ReciboDetND> notasdebito = new ArrayList<ReciboDetND>();
		String[] projection = new String[] {
				NMConfig.Recibo.DetalleNotaDebito.ID,
				NMConfig.Recibo.DetalleNotaDebito.NOTADEBITO_ID,
				NMConfig.Recibo.DetalleNotaDebito.RECIBO_ID,
				NMConfig.Recibo.DetalleNotaDebito.MONTO_INTERES,
				NMConfig.Recibo.DetalleNotaDebito.ESABONO,
				NMConfig.Recibo.DetalleNotaDebito.MONTO_PAGAR,
				NMConfig.Recibo.DetalleNotaDebito.NUMERO,
				NMConfig.Recibo.DetalleNotaDebito.FECHA,
				NMConfig.Recibo.DetalleNotaDebito.FECHA_VENCE,
				NMConfig.Recibo.DetalleNotaDebito.MONTO_ND,
				NMConfig.Recibo.DetalleNotaDebito.SALDO_ND,
				NMConfig.Recibo.DetalleNotaDebito.INTERES_MORATORIO,
				NMConfig.Recibo.DetalleNotaDebito.SALDO_TOTAL,
				NMConfig.Recibo.DetalleNotaDebito.MONTO_NETO };
		
		String  projection1=
				NMConfig.Recibo.DetalleNotaDebito.ID+","+
				NMConfig.Recibo.DetalleNotaDebito.NOTADEBITO_ID+","+
				NMConfig.Recibo.DetalleNotaDebito.RECIBO_ID+","+
				NMConfig.Recibo.DetalleNotaDebito.MONTO_INTERES+","+
				NMConfig.Recibo.DetalleNotaDebito.ESABONO+","+
				NMConfig.Recibo.DetalleNotaDebito.MONTO_PAGAR+","+
				NMConfig.Recibo.DetalleNotaDebito.NUMERO+","+
				NMConfig.Recibo.DetalleNotaDebito.FECHA+","+
				NMConfig.Recibo.DetalleNotaDebito.FECHA_VENCE+","+
				NMConfig.Recibo.DetalleNotaDebito.MONTO_ND+","+
				NMConfig.Recibo.DetalleNotaDebito.SALDO_ND+","+
				NMConfig.Recibo.DetalleNotaDebito.INTERES_MORATORIO+","+
				NMConfig.Recibo.DetalleNotaDebito.SALDO_TOTAL+","+
				NMConfig.Recibo.DetalleNotaDebito.MONTO_NETO ;
		
		ReciboDetND notaDebitoDetalle = null;
		try {
			
			StringBuilder sQuery = new StringBuilder();
			sQuery.append(" SELECT "+projection1.toString());
			sQuery.append(" FROM ReciboDetalleNotaDebito AS dnd ");
			sQuery.append(" WHERE dnd.objReciboID= " + String.valueOf(reciboID));
 
			Cursor cur = DatabaseProvider.query(db, sQuery.toString()); 

			if (cur.moveToFirst()) {
				do {
					notaDebitoDetalle = new ReciboDetND();
					notaDebitoDetalle.setId(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					notaDebitoDetalle.setObjNotaDebitoID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[1]))));
					notaDebitoDetalle.setObjReciboID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[2]))));
					notaDebitoDetalle.setInteresMoratorio(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[3]))));
					boolean esAbono = (Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[4]))) == 0);
					notaDebitoDetalle.setEsAbono(esAbono);
					notaDebitoDetalle.setMontoPagar(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[5]))));
					notaDebitoDetalle.setNumero(cur.getString(cur
							.getColumnIndex(projection[6])));
					notaDebitoDetalle.setFecha(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[7]))));
					notaDebitoDetalle.setFechaVence(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[8]))));
					notaDebitoDetalle.setMontoND(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[9]))));
					notaDebitoDetalle.setSaldoND(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[10]))));
					notaDebitoDetalle.setInteresMoratorio(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[11]))));
					notaDebitoDetalle.setSaldoTotal(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[12]))));
					notaDebitoDetalle.setMontoNeto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[13]))));
					notasdebito.add(notaDebitoDetalle);
				} while (cur.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return notasdebito;
	}

	public synchronized static ArrayList<ReciboDetNC> getNotasCreitoDelRecibo(
			SQLiteDatabase db, long reciboID) {
		ArrayList<ReciboDetNC> notascredito = new ArrayList<ReciboDetNC>();
		String[] projection = new String[] {
				NMConfig.Recibo.DetalleNotaCredito.ID,
				NMConfig.Recibo.DetalleNotaCredito.NOTACREDITO_ID,
				NMConfig.Recibo.DetalleNotaCredito.RECIBO_ID,
				NMConfig.Recibo.DetalleNotaCredito.MONTO,
				NMConfig.Recibo.DetalleNotaCredito.NUMERO,
				NMConfig.Recibo.DetalleNotaCredito.FECHA,
				NMConfig.Recibo.DetalleNotaCredito.FECHA_VENCE };
		
		String  projection1 = 
				NMConfig.Recibo.DetalleNotaCredito.ID+","+
				NMConfig.Recibo.DetalleNotaCredito.NOTACREDITO_ID+","+
				NMConfig.Recibo.DetalleNotaCredito.RECIBO_ID+","+
				NMConfig.Recibo.DetalleNotaCredito.MONTO+","+
				NMConfig.Recibo.DetalleNotaCredito.NUMERO+","+
				NMConfig.Recibo.DetalleNotaCredito.FECHA+","+
				NMConfig.Recibo.DetalleNotaCredito.FECHA_VENCE;
		
		ReciboDetNC notacreditoDetalle = null;
		try {
			
			
			StringBuilder sQuery = new StringBuilder();
			sQuery.append(" SELECT "+projection1.toString());
			sQuery.append(" FROM ReciboDetalleNotaCredito AS dnc ");
			sQuery.append(" WHERE dnc.objReciboID= " + String.valueOf(reciboID)); 
			 
			Cursor cur = DatabaseProvider.query(db, sQuery.toString()); 
			if (cur.moveToFirst()) {
				do {
					notacreditoDetalle = new ReciboDetNC();
					notacreditoDetalle.setId(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					notacreditoDetalle.setObjNotaCreditoID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[1]))));
					notacreditoDetalle.setObjReciboID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[2]))));
					notacreditoDetalle.setMonto(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[3]))));
					notacreditoDetalle.setNumero(cur.getString(cur
							.getColumnIndex(projection[4])));
					notacreditoDetalle.setFecha(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[5]))));
					notacreditoDetalle.setFechaVence(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[6]))));
					notascredito.add(notacreditoDetalle);
				} while (cur.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return notascredito;
	}

	public synchronized static ArrayList<ReciboDetFormaPago> getFormasPagoDelRecibo(
			SQLiteDatabase db , long reciboID) {

		ArrayList<ReciboDetFormaPago> formasPago = new ArrayList<ReciboDetFormaPago>();
		String[] projection = new String[] {
				NMConfig.Recibo.DetalleFormaPago.RECIBO_ID,
				NMConfig.Recibo.DetalleFormaPago.FORMA_PAGO_ID,
				NMConfig.Recibo.DetalleFormaPago.COD_FORMA_PAGO,
				NMConfig.Recibo.DetalleFormaPago.DESC_FORMA_PAGO,
				NMConfig.Recibo.DetalleFormaPago.NUMERO,
				NMConfig.Recibo.DetalleFormaPago.MONEDA_ID,
				NMConfig.Recibo.DetalleFormaPago.COD_MONEDA,
				NMConfig.Recibo.DetalleFormaPago.DESC_MONEDA,
				NMConfig.Recibo.DetalleFormaPago.MONTO,
				NMConfig.Recibo.DetalleFormaPago.MONTO_NACIONAL,
				NMConfig.Recibo.DetalleFormaPago.ENTIDAD_ID,
				NMConfig.Recibo.DetalleFormaPago.COD_ENTIDAD,
				NMConfig.Recibo.DetalleFormaPago.DESC_ENTIDAD,
				NMConfig.Recibo.DetalleFormaPago.FECHA,
				NMConfig.Recibo.DetalleFormaPago.SERIE_BILLETES,
				NMConfig.Recibo.DetalleFormaPago.TASA_CAMBIO };
		
		String  projection1 =  
				NMConfig.Recibo.DetalleFormaPago.RECIBO_ID+","+
				NMConfig.Recibo.DetalleFormaPago.FORMA_PAGO_ID+","+
				NMConfig.Recibo.DetalleFormaPago.COD_FORMA_PAGO+","+
				NMConfig.Recibo.DetalleFormaPago.DESC_FORMA_PAGO+","+
				NMConfig.Recibo.DetalleFormaPago.NUMERO+","+
				NMConfig.Recibo.DetalleFormaPago.MONEDA_ID+","+
				NMConfig.Recibo.DetalleFormaPago.COD_MONEDA+","+
				NMConfig.Recibo.DetalleFormaPago.DESC_MONEDA+","+
				NMConfig.Recibo.DetalleFormaPago.MONTO+","+
				NMConfig.Recibo.DetalleFormaPago.MONTO_NACIONAL+","+
				NMConfig.Recibo.DetalleFormaPago.ENTIDAD_ID+","+
				NMConfig.Recibo.DetalleFormaPago.COD_ENTIDAD+","+
				NMConfig.Recibo.DetalleFormaPago.DESC_ENTIDAD+","+
				NMConfig.Recibo.DetalleFormaPago.FECHA+","+
				NMConfig.Recibo.DetalleFormaPago.SERIE_BILLETES+","+
				NMConfig.Recibo.DetalleFormaPago.TASA_CAMBIO ;

		ReciboDetFormaPago formaPago = null; 
		try 
		{
 
			StringBuilder sQuery = new StringBuilder();

			sQuery.append(" SELECT "+ projection1.toString());
			sQuery.append(" FROM ReciboDetalleFormaPago AS fp ");
			sQuery.append(" WHERE fp.objReciboID= " + String.valueOf(reciboID));

			Cursor cur = DatabaseProvider.query(db, sQuery.toString()); 

			if (cur.moveToFirst()) {
				do {
					formaPago = new ReciboDetFormaPago();
					formaPago.setObjReciboID(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					formaPago.setObjFormaPagoID(Long.parseLong(cur
							.getString(cur.getColumnIndex(projection[1]))));
					formaPago.setCodFormaPago(cur.getString(cur
							.getColumnIndex(projection[2])));
					formaPago.setDescFormaPago(cur.getString(cur
							.getColumnIndex(projection[3])));
					formaPago.setNumero(cur.getString(cur
							.getColumnIndex(projection[4])));
					formaPago.setObjMonedaID(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[5]))));
					formaPago.setCodMoneda(cur.getString(cur
							.getColumnIndex(projection[6])));
					formaPago.setDescMoneda(cur.getString(cur
							.getColumnIndex(projection[7])));
					formaPago.setMonto(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[8]))));
					formaPago.setMontoNacional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[9]))));
					formaPago.setObjEntidadID(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[10]))));
					formaPago.setCodEntidad(cur.getString(cur
							.getColumnIndex(projection[11])));
					formaPago.setDescEntidad(cur.getString(cur
							.getColumnIndex(projection[12])));
					formaPago.setFecha(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[13]))));
					formaPago.setSerieBilletes(cur.getString(cur
							.getColumnIndex(projection[14])));
					formaPago.setTasaCambio(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[15]))));

					formasPago.add(formaPago);
				} while (cur.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return formasPago;
	}

	public synchronized static ArrayList<vmRecibo> getArrayCustomerFromLocalHost(
			ContentResolver content) throws Exception {

		String[] projection = new String[] { NMConfig.Recibo.ID,
				NMConfig.Recibo.REFERENCIA, NMConfig.Recibo.FECHA,
				NMConfig.Recibo.TOTAL_RECIBO, NMConfig.Recibo.NOMBRE_CLIENTE,
				NMConfig.Recibo.DESCRICION_ESTADO,
				NMConfig.Recibo.CODIGO_ESTADO, NMConfig.Recibo.SUCURSAL_ID };

		int count = 0;
		ArrayList<vmRecibo> a_vmprod = new ArrayList<vmRecibo>();
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_RECIBO,
				projection, // Columnas a devolver
				null, // Condición de la query
				null, // Argumentos variables de la query
				NMConfig.Recibo.ID + " DESC");
		if (cur.moveToFirst()) {
			do {

				a_vmprod.add(new vmRecibo(Integer.parseInt(cur.getString(cur
						.getColumnIndex(projection[0]))), Integer.parseInt(cur
						.getString(cur.getColumnIndex(projection[1]))), Long
						.parseLong(cur.getString(cur
								.getColumnIndex(projection[2]))), Float
						.parseFloat(cur.getString(cur
								.getColumnIndex(projection[3]))), cur
						.getString(cur.getColumnIndex(projection[4])), cur
						.getString(cur.getColumnIndex(projection[5])), cur
						.getString(cur.getColumnIndex(projection[6])), cur
						.getLong(cur.getColumnIndex(projection[7]))));
			} while (cur.moveToNext());
		}

		return a_vmprod;
	}

	public synchronized static ArrayList<ReciboColector> getRecibosEnEstadoRegistrado(
			ContentResolver content) {
		ArrayList<ReciboColector> recibos = new ArrayList<ReciboColector>();

		String[] projection = new String[] { NMConfig.Recibo.ID,
				NMConfig.Recibo.NUMERO, NMConfig.Recibo.FECHA,
				NMConfig.Recibo.NOTAS, NMConfig.Recibo.TOTAL_RECIBO,
				NMConfig.Recibo.TOTAL_FACTURAS,
				NMConfig.Recibo.TOTAL_NOTAS_DEBITO,
				NMConfig.Recibo.TOTAL_INTERES, NMConfig.Recibo.SUBTOTAL,
				NMConfig.Recibo.TOTAL_DESCUENTO,
				NMConfig.Recibo.TOTAL_RETENIDO,
				NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES,
				NMConfig.Recibo.TOTAL_NOTAS_CREDITO,
				NMConfig.Recibo.REFERENCIA, NMConfig.Recibo.CLIENTE_ID,
				NMConfig.Recibo.SUCURSAL_ID, NMConfig.Recibo.NOMBRE_CLIENTE,
				NMConfig.Recibo.COLECTOR_ID,
				NMConfig.Recibo.APLICA_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL,
				NMConfig.Recibo.PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR,
				NMConfig.Recibo.ESTADO_ID, NMConfig.Recibo.CODIGO_ESTADO,
				NMConfig.Recibo.DESCRICION_ESTADO,
				NMConfig.Recibo.TOTAL_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.TOTAL_DESCUENTO_PROMOCION,
				NMConfig.Recibo.TOTAL_DESCUENTO_PRONTO_PAGO,
				NMConfig.Recibo.TOTAL_IMPUESTO_PROPORCIONAL,
				NMConfig.Recibo.TOTAL_IMPUESTO_EXONERADO,
				NMConfig.Recibo.EXENTO, NMConfig.Recibo.AUTORIZA_DGI };
		
		String projection1 =  NMConfig.Recibo.ID+","+
				NMConfig.Recibo.NUMERO+","+ NMConfig.Recibo.FECHA+","+
				NMConfig.Recibo.NOTAS+","+ NMConfig.Recibo.TOTAL_RECIBO+","+
				NMConfig.Recibo.TOTAL_FACTURAS+","+
				NMConfig.Recibo.TOTAL_NOTAS_DEBITO+","+
				NMConfig.Recibo.TOTAL_INTERES+","+ NMConfig.Recibo.SUBTOTAL+","+
				NMConfig.Recibo.TOTAL_DESCUENTO+","+
				NMConfig.Recibo.TOTAL_RETENIDO+","+
				NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES+","+
				NMConfig.Recibo.TOTAL_NOTAS_CREDITO+","+
				NMConfig.Recibo.REFERENCIA+","+ NMConfig.Recibo.CLIENTE_ID+","+
				NMConfig.Recibo.SUCURSAL_ID+","+ NMConfig.Recibo.NOMBRE_CLIENTE+","+
				NMConfig.Recibo.COLECTOR_ID+","+
				NMConfig.Recibo.APLICA_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL+","+
				NMConfig.Recibo.PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR+","+
				NMConfig.Recibo.ESTADO_ID+","+ NMConfig.Recibo.CODIGO_ESTADO+","+
				NMConfig.Recibo.DESCRICION_ESTADO+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_PROMOCION+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_PRONTO_PAGO+","+
				NMConfig.Recibo.TOTAL_IMPUESTO_PROPORCIONAL+","+
				NMConfig.Recibo.TOTAL_IMPUESTO_EXONERADO+","+
				NMConfig.Recibo.EXENTO+","+ NMConfig.Recibo.AUTORIZA_DGI;
		SQLiteDatabase db = null;
		try 
		{

			// OBTENIENDO LAS FACTURAS
			db = Helper.getDatabase(NMApp.ctx);
			StringBuilder sQuery = new StringBuilder();

			sQuery.append(" SELECT "+projection1.toString());
			sQuery.append(" FROM Recibo AS r ");
			sQuery.append(" WHERE codEstado= " + "'REGISTRADO'");

			Cursor cur = DatabaseProvider.query(db, sQuery.toString());
			ReciboColector recibo = null;
			if (cur.moveToFirst()) {
				do {
					recibo = new ReciboColector();
					recibo.setId(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					recibo.setNumero(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[1]))));
					recibo.setFecha(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[2]))));
					recibo.setNotas(cur.getString(cur
							.getColumnIndex(projection[3])));
					recibo.setTotalRecibo(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[4]))));
					recibo.setTotalFacturas(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[5]))));
					recibo.setTotalND(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[6]))));
					recibo.setTotalInteres(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[7]))));
					recibo.setSubTotal(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[8]))));
					recibo.setTotalDesc(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[9]))));
					recibo.setTotalRetenido(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[10]))));
					recibo.setTotalOtrasDed(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[11]))));
					recibo.setTotalNC(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[12]))));
					recibo.setReferencia(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[13]))));
					recibo.setObjClienteID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[14]))));
					recibo.setObjSucursalID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[15]))));
					recibo.setNombreCliente(cur.getString(cur
							.getColumnIndex(projection[16])));
					recibo.setObjColectorID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[17]))));
					boolean aplicaDescuento = !(Integer.parseInt(cur
							.getString(cur.getColumnIndex(projection[18]))) == 0);
					recibo.setAplicaDescOca(aplicaDescuento);
					recibo.setClaveAutorizaDescOca(cur.getString(cur
							.getColumnIndex(projection[19])));
					recibo.setPorcDescOcaColector(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[20]))));
					recibo.setObjEstadoID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[21]))));
					recibo.setCodEstado(cur.getString(cur
							.getColumnIndex(projection[22])));
					recibo.setDescEstado(cur.getString(cur
							.getColumnIndex(projection[23])));
					recibo.setTotalDescOca(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[24]))));
					recibo.setTotalDescPromo(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[25]))));
					recibo.setTotalDescPP(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[26]))));
					recibo.setTotalImpuestoProporcional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[27]))));
					recibo.setTotalImpuestoExonerado(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[28]))));
					boolean exento = !(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[29]))) == 0);
					recibo.setExento(exento);
					recibo.setAutorizacionDGI(cur.getString(cur
							.getColumnIndex(projection[30])));
					// Get Customer reference
					// Cliente cliente =
					// ModelCliente.getClienteBySucursalID(content,
					// recibo.getObjSucursalID(), recibo.getId());
					// recibo.setCliente(cliente);
					// Get Detail
					recibo.setFacturasRecibo(getFacturasDelRecibo(db,
							recibo.getId()));
					recibo.setNotasDebitoRecibo(getNotasDebitoDelRecibo(
							db, recibo.getId()));
					recibo.setNotasCreditoRecibo(getNotasCreitoDelRecibo(
							db, recibo.getId()));
					recibos.add(recibo);
				} while (cur.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				if (db.isOpen())
					db.close();
				db = null;
			}
		}
		return recibos;
	}

	public synchronized static ArrayList<ReciboColector> getArrayRecibosFromLocalHost(
			ContentResolver content) {
		ArrayList<ReciboColector> recibos = new ArrayList<ReciboColector>();

		String[] projection = new String[] { NMConfig.Recibo.ID,
				NMConfig.Recibo.NUMERO, NMConfig.Recibo.FECHA,
				NMConfig.Recibo.NOTAS, NMConfig.Recibo.TOTAL_RECIBO,
				NMConfig.Recibo.TOTAL_FACTURAS,
				NMConfig.Recibo.TOTAL_NOTAS_DEBITO,
				NMConfig.Recibo.TOTAL_INTERES, NMConfig.Recibo.SUBTOTAL,
				NMConfig.Recibo.TOTAL_DESCUENTO,
				NMConfig.Recibo.TOTAL_RETENIDO,
				NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES,
				NMConfig.Recibo.TOTAL_NOTAS_CREDITO,
				NMConfig.Recibo.REFERENCIA, NMConfig.Recibo.CLIENTE_ID,
				NMConfig.Recibo.SUCURSAL_ID, NMConfig.Recibo.NOMBRE_CLIENTE,
				NMConfig.Recibo.COLECTOR_ID,
				NMConfig.Recibo.APLICA_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL,
				NMConfig.Recibo.PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR,
				NMConfig.Recibo.ESTADO_ID, NMConfig.Recibo.CODIGO_ESTADO,
				NMConfig.Recibo.DESCRICION_ESTADO,
				NMConfig.Recibo.TOTAL_DESCUENTO_OCASIONAL,
				NMConfig.Recibo.TOTAL_DESCUENTO_PROMOCION,
				NMConfig.Recibo.TOTAL_DESCUENTO_PRONTO_PAGO,
				NMConfig.Recibo.TOTAL_IMPUESTO_PROPORCIONAL,
				NMConfig.Recibo.TOTAL_IMPUESTO_EXONERADO,
				NMConfig.Recibo.EXENTO, NMConfig.Recibo.AUTORIZA_DGI };
		
		
		String projection1 = NMConfig.Recibo.ID+","+
				NMConfig.Recibo.NUMERO+","+ NMConfig.Recibo.FECHA+","+
				NMConfig.Recibo.NOTAS+","+ NMConfig.Recibo.TOTAL_RECIBO+","+
				NMConfig.Recibo.TOTAL_FACTURAS+","+
				NMConfig.Recibo.TOTAL_NOTAS_DEBITO+","+
				NMConfig.Recibo.TOTAL_INTERES+","+ NMConfig.Recibo.SUBTOTAL+","+
				NMConfig.Recibo.TOTAL_DESCUENTO+","+
				NMConfig.Recibo.TOTAL_RETENIDO+","+
				NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES+","+
				NMConfig.Recibo.TOTAL_NOTAS_CREDITO+","+
				NMConfig.Recibo.REFERENCIA+","+ NMConfig.Recibo.CLIENTE_ID+","+
				NMConfig.Recibo.SUCURSAL_ID+","+ NMConfig.Recibo.NOMBRE_CLIENTE+","+
				NMConfig.Recibo.COLECTOR_ID+","+
				NMConfig.Recibo.APLICA_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL+","+
				NMConfig.Recibo.PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR+","+
				NMConfig.Recibo.ESTADO_ID+","+ NMConfig.Recibo.CODIGO_ESTADO+","+
				NMConfig.Recibo.DESCRICION_ESTADO+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_OCASIONAL+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_PROMOCION+","+
				NMConfig.Recibo.TOTAL_DESCUENTO_PRONTO_PAGO+","+
				NMConfig.Recibo.TOTAL_IMPUESTO_PROPORCIONAL+","+
				NMConfig.Recibo.TOTAL_IMPUESTO_EXONERADO+","+
				NMConfig.Recibo.EXENTO+","+ NMConfig.Recibo.AUTORIZA_DGI;
		
		SQLiteDatabase db = null;
		try 
		{

			// OBTENIENDO LAS FACTURAS
			db = Helper.getDatabase(NMApp.ctx);
			StringBuilder sQuery = new StringBuilder();

			sQuery.append(" SELECT "+projection1.toString());
			sQuery.append(" FROM Recibo AS r ");
			sQuery.append(" WHERE codEstado= " + "'REGISTRADO'");

			Cursor cur = DatabaseProvider.query(db, sQuery.toString());
			
			ReciboColector recibo = null;
			if (cur.moveToFirst()) {
				do {
					recibo = new ReciboColector();
					recibo.setId(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[0]))));
					recibo.setNumero(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[1]))));
					recibo.setFecha(Long.parseLong(cur.getString(cur
							.getColumnIndex(projection[2]))));
					recibo.setNotas(cur.getString(cur
							.getColumnIndex(projection[3])));
					recibo.setTotalRecibo(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[4]))));
					recibo.setTotalFacturas(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[5]))));
					recibo.setTotalND(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[6]))));
					recibo.setTotalInteres(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[7]))));
					recibo.setSubTotal(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[8]))));
					recibo.setTotalDesc(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[9]))));
					recibo.setTotalRetenido(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[10]))));
					recibo.setTotalOtrasDed(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[11]))));
					recibo.setTotalNC(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[12]))));
					recibo.setReferencia(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[13]))));
					recibo.setObjClienteID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[14]))));
					recibo.setObjSucursalID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[15]))));
					recibo.setNombreCliente(cur.getString(cur
							.getColumnIndex(projection[16])));
					recibo.setObjColectorID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[17]))));
					boolean aplicaDescuento = !(Integer.parseInt(cur
							.getString(cur.getColumnIndex(projection[18]))) == 0);
					recibo.setAplicaDescOca(aplicaDescuento);
					recibo.setClaveAutorizaDescOca(cur.getString(cur
							.getColumnIndex(projection[19])));
					recibo.setPorcDescOcaColector(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[20]))));
					recibo.setObjEstadoID(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[21]))));
					recibo.setCodEstado(cur.getString(cur
							.getColumnIndex(projection[22])));
					recibo.setDescEstado(cur.getString(cur
							.getColumnIndex(projection[23])));
					recibo.setTotalDescOca(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[24]))));
					recibo.setTotalDescPromo(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[25]))));
					recibo.setTotalDescPP(Float.parseFloat(cur.getString(cur
							.getColumnIndex(projection[26]))));
					recibo.setTotalImpuestoProporcional(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[27]))));
					recibo.setTotalImpuestoExonerado(Float.parseFloat(cur
							.getString(cur.getColumnIndex(projection[28]))));
					boolean exento = !(Integer.parseInt(cur.getString(cur
							.getColumnIndex(projection[29]))) == 0);
					recibo.setExento(exento);
					recibo.setAutorizacionDGI(cur.getString(cur
							.getColumnIndex(projection[30])));
					// Get Customer reference
					Cliente cliente = ModelCliente.getClienteBySucursalID(
							content, recibo.getObjSucursalID(), recibo.getId());
					recibo.setCliente(cliente);
					// Get Detail
					recibo.setFacturasRecibo(getFacturasDelRecibo(db,
							recibo.getId()));
					recibo.setNotasDebitoRecibo(getNotasDebitoDelRecibo(
							db, recibo.getId()));
					recibo.setNotasCreditoRecibo(getNotasCreitoDelRecibo(
							db, recibo.getId()));
					recibos.add(recibo);
				} while (cur.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				if (db.isOpen())
					db.close();
				db = null;
			}
		}
		return recibos;
	}
}
