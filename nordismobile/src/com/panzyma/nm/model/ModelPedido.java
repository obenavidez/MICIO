package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PedidoPromocion;
import com.panzyma.nm.serviceproxy.PedidoPromocionDetalle;
import com.panzyma.nm.viewmodel.vmEntity;

public class ModelPedido {

	public synchronized static Object onUpdateInventory_From_Server(
			java.lang.String credentials, java.lang.String usuarioVendedor,
			boolean todos) throws Exception {
		Parameters params = new Parameters((new String[] { "Credentials",
				"UsuarioVendedor", "todos" }), (new Object[] { credentials,
				usuarioVendedor, todos }), (new Type[] {
				PropertyInfo.STRING_CLASS, PropertyInfo.STRING_CLASS,
				PropertyInfo.BOOLEAN_CLASS }));
		return com.comunicator.AppNMComunication.InvokeMethod(
				params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.TraerDisponibilidadProductos);
	}

	public static Pedido RegistrarPedido(Pedido pedido, Context cnt)
			throws Exception {
		return DatabaseProvider.RegistrarPedido(pedido, cnt);
	}

	public static Pedido enviarPedido(String credenciales, Pedido pedido)
			throws Exception {
		Parameters params = new Parameters((new String[] { "Credentials",
				"pedido" }), (new Object[] { credenciales, pedido }),
				(new Type[] { PropertyInfo.STRING_CLASS, pedido.getClass() }));

		// Object rs=
		// NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.EnviarPedido,Pedido.class);
		Object rs = NMComunicacion.InvokeMethod2(params.getParameters(),
				NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.EnviarPedido, Pedido.class);
		return NMTranslate.ToObject(rs, new Pedido());

	}

	public static Pedido refrescarPedido(String credenciales, long refPedido)
			throws Exception {

		Parameters params = new Parameters((new String[] { "Credentials",
				"refPedido" }), (new Object[] { credenciales, refPedido }),
				(new Type[] { PropertyInfo.STRING_CLASS,
						PropertyInfo.LONG_CLASS }));

		Object rs = NMComunicacion.InvokeMethod(params.getParameters(),
				NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.GetPedidoByRef, Pedido.class);
		return NMTranslate.ToObject(rs, new Pedido());

	}

	public static Pedido getPedido(String credenciales, long idPedido)
			throws Exception {

		Parameters params = new Parameters((new String[] { "Credentials",
				"idPedido" }), (new Object[] { credenciales, idPedido }),
				(new Type[] { PropertyInfo.STRING_CLASS,
						PropertyInfo.LONG_CLASS }));

		Object rs = NMComunicacion.InvokeMethod(params.getParameters(),
				NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.GetPedido, Pedido.class);
		return NMTranslate.ToObject(rs, new Pedido());

	}

	public static List<vmEntity> obtenerPedidosLocalmente(
			ContentResolver content) {
		List<vmEntity> le = new ArrayList<vmEntity>();
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDO, null, // Columnas
																				// a
																				// devolver
				null, // Condición de la query
				null, // Argumentos variables de la query
				null);

		if (cur.moveToFirst()) {
			do {
				vmEntity e = new vmEntity();
				e.setId(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.Id))));
				e.setNumero(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.NumeroMovil)));
				e.setNombre(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.NombreCliente))
						+ "/"
						+ cur.getString(cur
								.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
				e.setDescEstado(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.DescEstado)));
				e.setTotal(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.Total)));
				e.setCodEstado(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.CodEstado)));
				le.add(e);
			} while (cur.moveToNext());
		}
		return le;
	}

	public static Pedido obtenerPedidoByID(int numeromovil,
			ContentResolver content) throws Exception {
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDO, null, // Columnas
																				// a
																				// devolver
				"NumeroMovil=" + numeromovil, // Condición de la query
				null, // Argumentos variables de la query
				null);
		Pedido p = new Pedido();
		if (cur.moveToFirst()) {
			int value;
			p.setId(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.Id))));
			p.setNumeroMovil(cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.NumeroMovil)));
			p.setNumeroCentral(cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.NumeroCentral)));
			p.setTipo(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Tipo)));
			p.setFecha(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Fecha)));
			p.setObjClienteID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objClienteID))));
			p.setNombreCliente(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.NombreCliente)));
			p.setObjSucursalID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objSucursalID))));
			p.setNombreSucursal(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
			p.setObjTipoPrecioVentaID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objTipoPrecioVentaID))));
			p.setCodTipoPrecio(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.CodTipoPrecio)));
			p.setDescTipoPrecio(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.DescTipoPrecio)));
			p.setObjVendedorID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objVendedorID))));
			value = cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.BonificacionEspecial));
			p.setBonificacionEspecial(value == 1 ? true : false);
			p.setBonificacionSolicitada(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.BonificacionSolicitada)));
			value = cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.PrecioEspecial));
			p.setPrecioEspecial(value == 1 ? true : false);
			p.setPrecioSolicitado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.PrecioSolicitado)));
			value = cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.PedidoCondicionado));
			p.setPedidoCondicionado(value == 1 ? true : false);
			p.setCondicion(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.Condicion)));
			p.setSubtotal(cur.getFloat(cur
					.getColumnIndex(NMConfig.Pedido.Subtotal)));
			p.setDescuento(cur.getFloat(cur
					.getColumnIndex(NMConfig.Pedido.Descuento)));
			p.setImpuesto(cur.getFloat(cur
					.getColumnIndex(NMConfig.Pedido.Impuesto)));
			p.setTotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Total)));
			p.setObjEstadoID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objEstadoID))));
			p.setCodEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.CodEstado)));
			p.setDescEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.DescEstado)));
			p.setObjCausaEstadoID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objCausaEstadoID))));
			p.setCodCausaEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.CodCausaEstado)));
			p.setDescCausaEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.DescCausaEstado)));
			p.setNombreVendedor(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.NombreVendedor)));
			p.setNota(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Nota)));
			value = cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Exento));
			p.setExento(value == 1 ? true : false);
			p.setAutorizacionDGI(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.AutorizacionDGI)));

			p.setDetalles(obtenerDetallePedido(content, p.getId()));
			p.setPromocionesAplicadas(obtenerPedidoPromocion(content, p.getId()));

		}
		return p;
	}

	public static Pedido obtenerPedidoByID(long idpedido,
			ContentResolver content) throws Exception {
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDO, null, // Columnas
																				// a
																				// devolver
				"Id=?", // Condición de la query
				new String[] { String.valueOf(idpedido) }, // Argumentos
															// variables de la
															// query
				null);
		Pedido p = new Pedido();
		if (cur.moveToFirst()) {
			int value;
			p.setId(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.Id))));
			p.setNumeroMovil(cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.NumeroMovil)));
			p.setNumeroCentral(cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.NumeroCentral)));
			p.setTipo(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Tipo)));
			p.setFecha(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Fecha)));
			p.setObjClienteID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objClienteID))));
			p.setNombreCliente(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.NombreCliente)));
			p.setObjSucursalID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objSucursalID))));
			p.setNombreSucursal(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
			p.setObjTipoPrecioVentaID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objTipoPrecioVentaID))));
			p.setCodTipoPrecio(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.CodTipoPrecio)));
			p.setDescTipoPrecio(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.DescTipoPrecio)));
			p.setObjVendedorID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objVendedorID))));
			value = cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.BonificacionEspecial));
			p.setBonificacionEspecial(value == 1 ? true : false);
			p.setBonificacionSolicitada(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.BonificacionSolicitada)));
			value = cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.PrecioEspecial));
			p.setPrecioEspecial(value == 1 ? true : false);
			p.setPrecioSolicitado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.PrecioSolicitado)));
			value = cur.getInt(cur
					.getColumnIndex(NMConfig.Pedido.PedidoCondicionado));
			p.setPedidoCondicionado(value == 1 ? true : false);
			p.setCondicion(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.Condicion)));
			p.setSubtotal(cur.getFloat(cur
					.getColumnIndex(NMConfig.Pedido.Subtotal)));
			p.setDescuento(cur.getFloat(cur
					.getColumnIndex(NMConfig.Pedido.Descuento)));
			p.setImpuesto(cur.getFloat(cur
					.getColumnIndex(NMConfig.Pedido.Impuesto)));
			p.setTotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Total)));
			p.setObjEstadoID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objEstadoID))));
			p.setCodEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.CodEstado)));
			p.setDescEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.DescEstado)));
			p.setObjCausaEstadoID(Long.parseLong(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.objCausaEstadoID))));
			p.setCodCausaEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.CodCausaEstado)));
			p.setDescCausaEstado(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.DescCausaEstado)));
			p.setNombreVendedor(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.NombreVendedor)));
			p.setNota(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Nota)));
			value = cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Exento));
			p.setExento(value == 1 ? true : false);
			p.setAutorizacionDGI(cur.getString(cur
					.getColumnIndex(NMConfig.Pedido.AutorizacionDGI)));

			p.setDetalles(obtenerDetallePedido(content, p.getId()));
			p.setPromocionesAplicadas(obtenerPedidoPromocion(content, p.getId()));

		}
		return p;
	}

	public static List<Pedido> obtenerPedidosLocales() {
		List<Pedido> lp = new ArrayList<Pedido>();
		ContentResolver content = NMApp.getContext().getContentResolver();
		Cursor cur = NMApp.getContext().getContentResolver()
				.query(DatabaseProvider.CONTENT_URI_PEDIDO, null, // Columnas a
																	// devolver
						null, // Condición de la query
						null, // Argumentos variables de la query
						null);

		if (cur.moveToFirst()) {

			do {
				Pedido p = new Pedido();
				int value;
				p.setId(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.Id))));
				p.setNumeroMovil(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.NumeroMovil)));
				p.setNumeroCentral(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.NumeroCentral)));
				p.setTipo(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.Tipo)));
				p.setFecha(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Fecha)));
				p.setObjClienteID(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.objClienteID))));
				p.setNombreCliente(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.NombreCliente)));
				p.setObjSucursalID(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.objSucursalID))));
				p.setNombreSucursal(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
				p.setObjTipoPrecioVentaID(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.objTipoPrecioVentaID))));
				p.setCodTipoPrecio(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.CodTipoPrecio)));
				p.setDescTipoPrecio(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.DescTipoPrecio)));
				p.setObjVendedorID(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.objVendedorID))));
				value = cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.BonificacionEspecial));
				p.setBonificacionEspecial(value == 1 ? true : false);
				p.setBonificacionSolicitada(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.BonificacionSolicitada)));
				value = cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.PrecioEspecial));
				p.setPrecioEspecial(value == 1 ? true : false);
				p.setPrecioSolicitado(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.PrecioSolicitado)));
				value = cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.PedidoCondicionado));
				p.setPedidoCondicionado(value == 1 ? true : false);
				p.setCondicion(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.Condicion)));
				p.setSubtotal(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.Subtotal)));
				p.setDescuento(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.Descuento)));
				p.setImpuesto(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.Impuesto)));
				p.setTotal(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.Total)));
				p.setObjEstadoID(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.objEstadoID))));
				p.setCodEstado(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.CodEstado)));
				p.setDescEstado(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.DescEstado)));
				p.setObjCausaEstadoID(Long.parseLong(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.objCausaEstadoID))));
				p.setCodCausaEstado(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.CodCausaEstado)));
				p.setDescCausaEstado(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.DescCausaEstado)));
				p.setNombreVendedor(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.NombreVendedor)));
				p.setNota(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.Nota)));
				value = cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Exento));
				p.setExento(value == 1 ? true : false);
				p.setAutorizacionDGI(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.AutorizacionDGI)));

				p.setDetalles(obtenerDetallePedido(content, p.getId()));
				p.setPromocionesAplicadas(obtenerPedidoPromocion(content,
						p.getId()));
				lp.add(p);
			} while (cur.moveToNext());

		}
		return lp;
	}

	public static List<Factura> obtenerPedidosFacturados(long... objSucursalID) {
		long id = 0;
		if (objSucursalID != null && objSucursalID.length > 0) {
			id = objSucursalID[0];
		}
		List<Factura> lp = new ArrayList<Factura>();
		ContentResolver content = NMApp.getContext().getContentResolver();
		long objEstado = 100306;// Estado Facturado
		/*
		 * Cursor cur =
		 * NMApp.getContext().getContentResolver().query(DatabaseProvider
		 * .CONTENT_URI_PEDIDO, null, //Columnas a devolver null,//
		 * "objEstadoID="+ objEstado, //Condición de la query null, //Argumentos
		 * variables de la query null);
		 */
		String[] tableColumns = new String[] { "cast(Id as INT) as id",
				"NoFactura", "NoPedido" };
		String whereClause = "objSucursalID = " + String.valueOf(id);
		SQLiteDatabase sql = DatabaseProvider.Helper.getDatabase(NMApp
				.getContext());
		Cursor cur = sql.query("Factura", tableColumns, whereClause, null,
				null, null, null);

		if (cur.moveToFirst()) {

			do {
				/*
				 * Pedido p=new Pedido(); int value;
				 * p.setId(Long.parseLong(cur.getString
				 * (cur.getColumnIndex(NMConfig.Pedido.Id))));
				 * p.setNumeroMovil(cur
				 * .getInt(cur.getColumnIndex(NMConfig.Pedido.NumeroMovil)));
				 * p.setNumeroCentral
				 * (cur.getInt(cur.getColumnIndex(NMConfig.Pedido
				 * .NumeroCentral)));
				 * p.setTipo(cur.getString(cur.getColumnIndex(
				 * NMConfig.Pedido.Tipo)));
				 * p.setFecha(cur.getInt(cur.getColumnIndex
				 * (NMConfig.Pedido.Fecha)));
				 * p.setObjClienteID(Long.parseLong(cur
				 * .getString(cur.getColumnIndex
				 * (NMConfig.Pedido.objClienteID))));
				 * p.setNombreCliente(cur.getString
				 * (cur.getColumnIndex(NMConfig.Pedido.NombreCliente)));
				 * p.setObjSucursalID
				 * (Long.parseLong(cur.getString(cur.getColumnIndex
				 * (NMConfig.Pedido.objSucursalID))));
				 * p.setNombreSucursal(cur.getString
				 * (cur.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
				 * p.setObjTipoPrecioVentaID
				 * (Long.parseLong(cur.getString(cur.getColumnIndex
				 * (NMConfig.Pedido.objTipoPrecioVentaID))));
				 * p.setCodTipoPrecio(
				 * cur.getString(cur.getColumnIndex(NMConfig.Pedido
				 * .CodTipoPrecio)));
				 * p.setDescTipoPrecio(cur.getString(cur.getColumnIndex
				 * (NMConfig.Pedido.DescTipoPrecio)));
				 * p.setObjVendedorID(Long.parseLong
				 * (cur.getString(cur.getColumnIndex
				 * (NMConfig.Pedido.objVendedorID))));
				 * value=cur.getInt(cur.getColumnIndex
				 * (NMConfig.Pedido.BonificacionEspecial));
				 * p.setBonificacionEspecial(value==1?true:false);
				 * p.setBonificacionSolicitada
				 * (cur.getString(cur.getColumnIndex(NMConfig
				 * .Pedido.BonificacionSolicitada)));
				 * value=cur.getInt(cur.getColumnIndex
				 * (NMConfig.Pedido.PrecioEspecial));
				 * p.setPrecioEspecial(value==1?true:false);
				 * p.setPrecioSolicitado
				 * (cur.getString(cur.getColumnIndex(NMConfig
				 * .Pedido.PrecioSolicitado)));
				 * value=cur.getInt(cur.getColumnIndex
				 * (NMConfig.Pedido.PedidoCondicionado));
				 * p.setPedidoCondicionado(value==1?true:false);
				 * p.setCondicion(cur
				 * .getString(cur.getColumnIndex(NMConfig.Pedido.Condicion)));
				 * p.
				 * setSubtotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.
				 * Subtotal)));
				 * p.setDescuento(cur.getFloat(cur.getColumnIndex(NMConfig
				 * .Pedido.Descuento)));
				 * p.setImpuesto(cur.getFloat(cur.getColumnIndex
				 * (NMConfig.Pedido.Impuesto)));
				 * p.setTotal(cur.getFloat(cur.getColumnIndex
				 * (NMConfig.Pedido.Total)));
				 * p.setObjEstadoID(Long.parseLong(cur
				 * .getString(cur.getColumnIndex
				 * (NMConfig.Pedido.objEstadoID))));
				 * p.setCodEstado(cur.getString
				 * (cur.getColumnIndex(NMConfig.Pedido.CodEstado)));
				 * p.setDescEstado
				 * (cur.getString(cur.getColumnIndex(NMConfig.Pedido
				 * .DescEstado)));
				 * p.setObjCausaEstadoID(Long.parseLong(cur.getString
				 * (cur.getColumnIndex(NMConfig.Pedido.objCausaEstadoID))));
				 * p.setCodCausaEstado
				 * (cur.getString(cur.getColumnIndex(NMConfig.
				 * Pedido.CodCausaEstado)));
				 * p.setDescCausaEstado(cur.getString(cur
				 * .getColumnIndex(NMConfig.Pedido.DescCausaEstado)));
				 * p.setNombreVendedor
				 * (cur.getString(cur.getColumnIndex(NMConfig.
				 * Pedido.NombreVendedor)));
				 * p.setNota(cur.getString(cur.getColumnIndex
				 * (NMConfig.Pedido.Nota)));
				 * value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Exento));
				 * p.setExento(value==1?true:false);
				 * p.setAutorizacionDGI(cur.getString
				 * (cur.getColumnIndex(NMConfig.Pedido.AutorizacionDGI)));
				 * 
				 * p.setDetalles(obtenerDetallePedido(content,p.getId()));
				 * p.setPromocionesAplicadas
				 * (obtenerPedidoPromocion(content,p.getId()));
				 */
				Factura p = new Factura();
				p.Id = cur.getInt(cur.getColumnIndex("id"));
				p.NoFactura = cur.getString(cur.getColumnIndex("NoFactura"));
				p.NoPedido = cur.getString(cur.getColumnIndex("NoPedido"));
				lp.add(p);
			} while (cur.moveToNext());

		}
		return lp;
	}

	public static DetallePedido[] obtenerDetallePedido(ContentResolver content,
			long ObjPedidoID) {

		int cont = 0;
		int value;
		Cursor cur = content.query(
				DatabaseProvider.CONTENT_URI_PEDIDODETALLE,
				null, // Columnas a devolver
				NMConfig.Pedido.DetallePedido.objPedidoID + "="
						+ String.valueOf(ObjPedidoID), // Condición de la query
				null, // new String[]{ String.valueOf(ObjPedidoID) } ,
						// //Argumentos variables de la query
				null);
		DetallePedido[] adp = new DetallePedido[cur.getCount()];

		if (cur.moveToFirst()) {

			do {

				DetallePedido dp = new DetallePedido();
				dp.setId(cur.getLong(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.Id)));
				dp.setObjPedidoID(cur.getLong(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.objPedidoID)));
				dp.setObjProductoID(cur.getLong(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.objProductoID)));
				dp.setCodProducto(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.codProducto)));
				dp.setNombreProducto(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.nombreProducto)));
				dp.setCantidadOrdenada(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadOrdenada)));
				dp.setCantidadBonificada(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadBonificada)));
				dp.setObjBonificacionID(cur.getLong(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.objBonificacionID)));
				value = cur
						.getInt(cur
								.getColumnIndex(NMConfig.Pedido.DetallePedido.bonifEditada));
				dp.setBonifEditada(value == 1 ? true : false);
				dp.setCantidadBonificadaEditada(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadBonificadaEditada)));
				dp.setPrecio(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.precio)));
				dp.setMontoPrecioEditado(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.montoPrecioEditado)));
				value = cur
						.getInt(cur
								.getColumnIndex(NMConfig.Pedido.DetallePedido.precioEditado));
				dp.setPrecioEditado(value == 1 ? true : false);
				dp.setSubtotal(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.subtotal)));
				dp.setDescuento(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.descuento)));
				dp.setPorcImpuesto(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.porcImpuesto)));
				dp.setImpuesto(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.impuesto)));
				dp.setTotal(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.total)));
				dp.setCantidadDespachada(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadDespachada)));
				dp.setCantidadADespachar(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadADespachar)));
				dp.setCantidadPromocion(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadPromocion)));

				adp[cont] = dp;
				cont++;
			} while (cur.moveToNext());
		}
		return adp.length == 0 ? null : adp;
	}

	public static PedidoPromocion[] obtenerPedidoPromocion(
			ContentResolver content, long ObjPedidoID) {
		int cont = 0;
		int value;
		Cursor cur = content.query(
				DatabaseProvider.CONTENT_URI_PEDIDOPROMOCION,
				null, // Columnas a devolver
				NMConfig.Pedido.DetallePedido.objPedidoID + "="
						+ String.valueOf(ObjPedidoID), // Condición de la query,
														// //Condición de la
														// query
				null, // Argumentos variables de la query
				null);
		PedidoPromocion[] app = new PedidoPromocion[cur.getCount()];

		if (cur.moveToFirst()) {

			do {

				PedidoPromocion pp = new PedidoPromocion();
				pp.setObjPromocionID(cur.getLong(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.objPromocionID)));
				pp.setDescuento(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.descuento)));
				pp.setDetalles(obtenerPedidoPromocionDetalle(content,
						pp.getObjPromocionID()));
				pp.setCodigoPromocion(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.codigoPromocion)));
				pp.setNombrePromocion(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.nombrePromocion)));

				app[cont] = pp;
				cont++;
			} while (cur.moveToNext());

		}
		return app.length == 0 ? null : app;
	}

	public static PedidoPromocionDetalle[] obtenerPedidoPromocionDetalle(
			ContentResolver content, long objPromocionID) {
		int cont = 0;
		int value;
		Cursor cur = content.query(
				DatabaseProvider.CONTENT_URI_PEDIDOPROMOCIONDETALLE,
				null, // Columnas a devolver
				NMConfig.Pedido.PedidoPromocion.objPromocionID + "="
						+ String.valueOf(objPromocionID), null, // Argumentos
																// variables de
																// la query
				null);
		PedidoPromocionDetalle[] appd = new PedidoPromocionDetalle[cur
				.getCount()];

		if (cur.moveToFirst()) {

			do {
				PedidoPromocionDetalle ppd = new PedidoPromocionDetalle();
				ppd.setObjProductoID(cur.getLong(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.objProductoID)));
				ppd.setDescuento(cur.getFloat(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.descuento)));
				ppd.setNombreProducto(cur.getString(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.nombreProducto)));
				ppd.setCantidadEntregada(cur.getInt(cur
						.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.cantidadEntregada)));
				appd[cont] = ppd;
				cont++;
			} while (cur.moveToNext());

		}
		return appd.length == 0 ? null : appd;
	}

	public synchronized static int borraPedidoByID(ContentResolver content,
			long PedidoID) {
		String[] projection = new String[] {};
		int result = 0;
		String url = "";
		try {
			url = DatabaseProvider.CONTENT_URI_PEDIDOPROMOCIONDETALLE + "/"
					+ String.valueOf(PedidoID);
			content.delete(Uri.parse(url), "", projection);
			url = DatabaseProvider.CONTENT_URI_PEDIDODETALLE + "/"
					+ String.valueOf(PedidoID);
			content.delete(Uri.parse(url), "", projection);
			url = DatabaseProvider.CONTENT_URI_PEDIDOPROMOCION + "/"
					+ String.valueOf(PedidoID);
			content.delete(Uri.parse(url), "", projection);
			url = DatabaseProvider.CONTENT_URI_PEDIDO + "/"
					+ String.valueOf(PedidoID);
			result = content.delete(Uri.parse(url), "", projection);
			// result = 1;
		} catch (Exception e) {

		}
		return result;
	}

	public static Pedido anularPedido(String credenciales, long pedidoid)
			throws Exception {
		/*
		 * Parameters params=new Parameters((new
		 * String[]{"Credentials","idPedido"}), (new
		 * Object[]{credenciales,pedidoid}), (new
		 * Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS}));
		 */

		ArrayList<Parameters> arrayparams = new ArrayList<Parameters>();
		String[] paramname = new String[] { "Credentials", "idPedido" };
		Object[] values = new Object[] { credenciales, pedidoid };
		Type[] type = new Type[] { PropertyInfo.STRING_CLASS,
				PropertyInfo.LONG_CLASS };
		for (int i = 0; i < 2; i++) {
			Parameters params = new Parameters();
			params.setName(paramname[i]);
			params.setValue(values[i]);
			params.setType(type[i]);
			arrayparams.add(params);
		}
		// String result =
		// AppNMComunication.InvokeMethod(arrayparams,NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.AnularPedido).toString();

		return (NMTranslate.ToObject(AppNMComunication.InvokeMethod(
				arrayparams, NMConfig.URL, NMConfig.NAME_SPACE,
				NMConfig.MethodName.AnularPedido), new Pedido()));
		// return
		// JSONObject(com.comunicator.AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.AnularPedido).toString());
	}

}
