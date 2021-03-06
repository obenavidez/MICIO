package com.panzyma.nm.datastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray; 
import org.json.JSONObject;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NumberUtil;
import com.panzyma.nm.model.ModelCProducto;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.CNota;
import com.panzyma.nm.serviceproxy.CProducto;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.serviceproxy.EncabezadoSolicitud;
import com.panzyma.nm.serviceproxy.Factura; 
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PedidoPromocion;
import com.panzyma.nm.serviceproxy.PedidoPromocionDetalle;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.serviceproxy.Ventas;

import android.annotation.SuppressLint;
import android.content.ContentProvider; 
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider 
{ 
	//Definici�n del CONTENT_URI
	public static final String CONTEXT_PREFIX="content://";
	public static final String AUTHORITY="com.panzyma.nm.datastore.databaseprovider";
	public static final String CONTENT_URI="content://com.panzyma.nm.datastore.databaseprovider"; 
	
	public static final Uri CONTENT_URI_CLIENTE = Uri.parse(CONTENT_URI+"/cliente");
	public static final Uri CONTENT_URI_FACTURA = Uri.parse(CONTENT_URI+"/factura");
	public static final Uri CONTENT_URI_FACTURA_BY_ID = Uri.parse(CONTENT_URI+"/facturabyid");
	public static final Uri CONTENT_URI_PROMOCIONCOBRO = Uri.parse(CONTENT_URI+"/promocioncobro");
	public static final Uri CONTENT_URI_MONTOPROVEEDOR = Uri.parse(CONTENT_URI+"/montoproveedor");
	public static final Uri CONTENT_URI_CCNOTACREDITO = Uri.parse(CONTENT_URI+"/ccnotacredito");
	public static final Uri CONTENT_URI_CCNOTACREDITO_BY_ID = Uri.parse(CONTENT_URI+"/ccnotacreditobyid");
	public static final Uri CONTENT_URI_CCNOTADEBITO = Uri.parse(CONTENT_URI+"/ccnotadebito");
	public static final Uri CONTENT_URI_CCNOTADEBITO_BY_ID = Uri.parse(CONTENT_URI+"/ccnotadebitobyid");
	public static final Uri CONTENT_URI_DESCUENTOPROVEEDOR = Uri.parse(CONTENT_URI+"/descuentoproveedor");
	public static final Uri CONTENT_URI_CATALOGO = Uri.parse(CONTENT_URI+"/catalogo");
	public static final Uri CONTENT_URI_VALORCATALOGO = Uri.parse(CONTENT_URI+"/valorcatalogo");
	public static final Uri CONTENT_URI_PRODUCTO = Uri.parse(CONTENT_URI+"/producto");	
	public static final Uri CONTENT_URI_LOTE = Uri.parse(CONTENT_URI+"/lote");
	public static final Uri CONTENT_URI_PROMOCION = Uri.parse(CONTENT_URI+"/promocion");
	public static final Uri CONTENT_URI_USUARIO = Uri.parse(CONTENT_URI+"/usuario");	
	public static final Uri CONTENT_URI_RECIBO = Uri.parse(CONTENT_URI+ "/recibo");
	public static final Uri CONTENT_URI_RECIBODETALLEFACTURA = Uri.parse(CONTENT_URI+ "/recibodetallefactura");
	public static final Uri CONTENT_URI_RECIBODETALLENOTACREDITO = Uri.parse(CONTENT_URI+ "/recibodetallenotacredito");
	public static final Uri CONTENT_URI_RECIBODETALLENOTADEBITO = Uri.parse(CONTENT_URI+ "/recibodetallenotadebito");
	public static final Uri CONTENT_URI_PEDIDO = Uri.parse(CONTENT_URI+ "/pedido");
	public static final Uri CONTENT_URI_PEDIDODETALLE = Uri.parse(CONTENT_URI+ "/pedidodetalle");
	public static final Uri CONTENT_URI_PEDIDOPROMOCION = Uri.parse(CONTENT_URI+ "/pedidopromocion");
	public static final Uri CONTENT_URI_PEDIDOPROMOCIONDETALLE = Uri.parse(CONTENT_URI+ "/pedidopromociondetalle");
	public static final Uri CONTENT_URI_RECIBODETALLEFORMAPAGO = Uri.parse(CONTENT_URI+ "/recibodetalleformapago");
	public static final Uri CONTENT_URI_CPRODUCTO = Uri.parse(CONTENT_URI+ "/cproducto");
	public static final Uri CONTENT_URI_CNOTA = Uri.parse(CONTENT_URI+ "/cnota");
	public static final Uri CONTENT_URI_SOLICITUD_DESCUENTO = Uri.parse(CONTENT_URI+ "/SolicitudDescuento");
	public static final Uri CONTENT_URI_ENCABEZADO_SOLICITUD = Uri.parse(CONTENT_URI+ "/EncabezadoSolicitud");
	public static final Uri CONTENT_URI_DEVOLUCION = Uri.parse(CONTENT_URI+ "/devolucion");
	public static final Uri CONTENT_URI_DEVOLUCIONPRODUCTO = Uri.parse(CONTENT_URI+ "/devolucionproducto");
	public static final Uri CONTENT_URI_DEVOLUCIONPRODUCTOLOTE = Uri.parse(CONTENT_URI+ "/devolucionproductolote");
		
	//Necesario para UriMatcher
	private static final int CLIENTE = 1;
	private static final int CLIENTE_ID = 2;
	private static final int FACTURA=3;
	private static final int FACTURA_ID=4;
	private static final int PROMOCIONCOBRO = 5;
	private static final int PROMOCIONCOBRO_ID = 6;
	private static final int MONTOPROVEEDOR=7;
	private static final int MONTOPROVEEDOR_ID=8;
	private static final int CCNOTACREDITO=9;
	private static final int CCNOTACREDITO_ID=10;
	private static final int CCNOTADEBITO = 11;
	private static final int CCNOTADEBITO_ID =12;
	private static final int DESCUENTOPROVEEDOR=13;
	private static final int DESCUENTOPROVEEDOR_ID=14;
	private static final int CONTENT_URI_LOCALID=15;
	
	private static final int PRODUCTO=16;
	private static final int PRODUCTO_ID=17;
	private static final int LOTE=18;
	private static final int LOTE_ID=19;
	private static final int CATALOGO=20;
	private static final int CATALOGO_ID=21;
	private static final int CATALOGO_BY_NAME=50;
	private static final int VALORCATALOGO=22;
	private static final int VALORCATALOGO_ID=23;
	private static final int PROMOCION=24;
	private static final int PROMOCION_ID=25;
	private static final int USUARIO=26;
	private static final int USUARIO_ID=27;
	private static final UriMatcher uriMatcher;
	private static final int RECIBO = 28;
	private static final int RECIBO_ID = 29;
	private static final int RECIBODETALLEFACTURA = 30;
	private static final int RECIBODETALLEFACTURA_ID = 31;
	private static final int RECIBODETALLENOTADEBITO = 32;
	private static final int RECIBODETALLENOTADEBITO_ID = 33;
	private static final int RECIBODETALLENOTACREDITO = 34;
	private static final int RECIBODETALLENOTACREDITO_ID = 35;
	private static final int RECIBODETALLEFORMAPAGO = 44;
	private static final int RECIBODETALLEFORMAPAGO_ID = 45;
	private static final int PEDIDO = 36;
	private static final int PEDIDO_ID = 37;
	
	private static final int PEDIDODETALLE = 38;
	private static final int PEDIDODETALLE_ID = 39;
	
	private static final int PEDIDOPROMOCION = 40;
	private static final int PEDIDOPROMOCION_ID = 41;
	
	private static final int PEDIDOPROMOCIONDETALLE = 42;
	private static final int PEDIDOPROMOCIONDETALLE_ID = 43;
	
	private static final int CPRODUTO  = 46;
	private static final int CPRODUTO_ID  = 47;
	
	private static final int CNOTA = 48;
	private static final int CNOTA_CPRODUCTOID = 49;
	
	private static final int SOLICITUDDESCUENTO = 50;
	private static final int SOLICITUDDESCUENTO_ID = 51;
	
	private static final int ENCABEZADOSOLICITUD = 52;
	private static final int ENCABEZADOSOLICITUD_ID = 53;
	
	private static final int DEVOLUCION  = 54;
	private static final int DEVOLUCION_ID  = 55;
	
	private static final int DEVOLUCIONPRODUCTO  = 56;
	private static final int DEVOLUCIONPRODUCTO_ID  = 57;
	
	private static final int DEVOLUCIONPRODUCTOLOTE  = 58;
	private static final int DEVOLUCIONPRODUCTOLOTE_ID  = 59;
//	
	
	//Base de datos
	private NM_SQLiteHelper dbhelper;
	private SQLiteDatabase db; 
	private static final String DATABASE_NAME = "SIMFAC";
	private static final int BD_VERSION = 19; 
	
	public static final String TABLA_CLIENTE = "Cliente";
	public static final String TABLA_FACTURA = "Factura";
	public static final String TABLA_PROMOCIONCOBRO = "PromocionCobro";
	public static final String TABLA_MONTOPROVEEDOR = "MontoProveedor";
	public static final String TABLA_CCNOTACREDITO = "CCNotaCredito";
	public static final String TABLA_CCNOTADEBITO = "CCNotaDebito";
	public static final String TABLA_DESCUENTOPROVEEDOR = "DescuentoProveedor";
	public static final String TABLA_CATALOGO = "Catalogo";
	public static final String TABLA_VALORCATALOGO = "ValorCatalogo"; 
	public static final String TABLA_TASACAMBIO = "TasaCambio";
	public static final String TABLA_PRODUCTO = "Producto";
	public static final String TABLA_LOTE = "Lote";
	public static final String TABLA_PROMOCION = "Promocion";
	public static final String TABLA_USUARIO = "Usuario";
	public static final String TABLA_RECIBO = "Recibo";
	public static final String TABLA_PEDIDO = "Pedido";
	public static final String TABLA_RECIBO_DETALLE_FACTURA = "ReciboDetalleFactura";
	public static final String TABLA_RECIBO_DETALLE_NOTA_DEBITO = "ReciboDetalleNotaDebito";
	public static final String TABLA_RECIBO_DETALLE_NOTA_CREDITO = "ReciboDetalleNotaCredito";	
	public static final String TABLA_RECIBO_DETALLE_FORMA_PAGO = "ReciboDetalleFormaPago";	
	public static final String TABLA_PEDIDODETALLE = "PedidoDetalle";
	public static final String TABLA_PEDIDOPROMOCION = "PedidoPromocion";
	public static final String TABLA_PEDIDOPROMOCIONDETALLE = "PedidoPromocionDetalle";	
	public static final String TABLA_CPRODUCTO = "CProducto";	
	public static final String TABLA_CNOTA = "CNota";
	public static final String TABLA_SOLICITUD_DESCUENTO = "SolicitudDescuento";
	public static final String TABLA_ENCABEZADO_SOLICITUD = "EncabezadoSolicitud";
	public static final String TABLA_DEVOLUCIONES = "Devolucion";
	public static final String TABLA_DEVOLUCIONPRODUCTO = "DevolucionProducto"; 
	public static final String TABLA_DEVOLUCIONPRODUCTOLOTE = "DevolucionProductoLote"; 
	
	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "cliente", CLIENTE);
		uriMatcher.addURI(AUTHORITY, "cliente/#", CLIENTE_ID);
		
		uriMatcher.addURI(AUTHORITY, "factura", FACTURA);
		uriMatcher.addURI(AUTHORITY, "factura/#", FACTURA_ID);
		uriMatcher.addURI(AUTHORITY, "facturabyid/#", FACTURA_ID);
		
		uriMatcher.addURI(AUTHORITY, "promocioncobro", PROMOCIONCOBRO );
		uriMatcher.addURI(AUTHORITY, "promocioncobro/#", PROMOCIONCOBRO_ID);
		
		uriMatcher.addURI(AUTHORITY, "montoproveedor", MONTOPROVEEDOR);
		uriMatcher.addURI(AUTHORITY, "montoproveedor/#", MONTOPROVEEDOR_ID);
		
		uriMatcher.addURI(AUTHORITY, "ccnotacredito", CCNOTACREDITO);
		uriMatcher.addURI(AUTHORITY, "ccnotacredito/#", CCNOTACREDITO_ID);
		uriMatcher.addURI(AUTHORITY, "ccnotacreditobyid/#", CCNOTACREDITO_ID);
		
		uriMatcher.addURI(AUTHORITY, "ccnotadebito", CCNOTADEBITO );
		uriMatcher.addURI(AUTHORITY, "ccnotadebito/#",CCNOTADEBITO_ID);
		uriMatcher.addURI(AUTHORITY, "ccnotadebitobyid/#", CCNOTADEBITO_ID);
		
		uriMatcher.addURI(AUTHORITY, "descuentoproveedor", DESCUENTOPROVEEDOR);
		uriMatcher.addURI(AUTHORITY, "descuentoproveedor/#", DESCUENTOPROVEEDOR_ID);
		
		uriMatcher.addURI(AUTHORITY, "catalogo", CATALOGO);
		uriMatcher.addURI(AUTHORITY, "catalogo/#", CATALOGO_ID);
		
		uriMatcher.addURI(AUTHORITY, "valorcatalogo", VALORCATALOGO);
		uriMatcher.addURI(AUTHORITY, "valorcatalogo/#", VALORCATALOGO_ID);
		
		uriMatcher.addURI(AUTHORITY, "producto", PRODUCTO);
		uriMatcher.addURI(AUTHORITY, "producto/#", PRODUCTO_ID);
		
		uriMatcher.addURI(AUTHORITY, "promocion", PROMOCION);
		uriMatcher.addURI(AUTHORITY, "promocion/#", PROMOCION_ID);
		
		uriMatcher.addURI(AUTHORITY, "lote", LOTE);
		uriMatcher.addURI(AUTHORITY, "lote/#", LOTE_ID);
		
		uriMatcher.addURI(AUTHORITY, "usuario", USUARIO);
		uriMatcher.addURI(AUTHORITY, "usuario/#", USUARIO_ID);
		
		uriMatcher.addURI(AUTHORITY, "recibo", RECIBO);
		uriMatcher.addURI(AUTHORITY, "recibo/#", RECIBO_ID);
		
		uriMatcher.addURI(AUTHORITY, "pedido", PEDIDO);
		uriMatcher.addURI(AUTHORITY, "pedido/#",PEDIDO_ID);
		
		uriMatcher.addURI(AUTHORITY, "pedidodetalle", PEDIDODETALLE);
		uriMatcher.addURI(AUTHORITY, "pedidodetalle/#", PEDIDODETALLE_ID);
		
		uriMatcher.addURI(AUTHORITY, "pedidopromocion", PEDIDOPROMOCION);
		uriMatcher.addURI(AUTHORITY, "pedidopromocion/#", PEDIDOPROMOCION_ID);
		
		uriMatcher.addURI(AUTHORITY, "pedidopromociondetalle", PEDIDOPROMOCIONDETALLE);
		uriMatcher.addURI(AUTHORITY, "pedidopromociondetalle/#", PEDIDOPROMOCIONDETALLE_ID);
		
		uriMatcher.addURI(AUTHORITY, "recibodetallefactura", RECIBODETALLEFACTURA);
		uriMatcher.addURI(AUTHORITY, "recibodetallefactura/#", RECIBODETALLEFACTURA_ID);
		
		uriMatcher.addURI(AUTHORITY, "recibodetallenotadebito", RECIBODETALLENOTADEBITO);
		uriMatcher.addURI(AUTHORITY, "recibodetallenotadebito/#", RECIBODETALLENOTADEBITO_ID);
		
		uriMatcher.addURI(AUTHORITY, "recibodetallenotacredito", RECIBODETALLENOTACREDITO);
		uriMatcher.addURI(AUTHORITY, "recibodetallenotacredito/#", RECIBODETALLENOTACREDITO_ID);
	
		uriMatcher.addURI(AUTHORITY, "recibodetalleformapago", RECIBODETALLEFORMAPAGO);
		uriMatcher.addURI(AUTHORITY, "recibodetalleformapago/#", RECIBODETALLEFORMAPAGO_ID);
		
		uriMatcher.addURI(AUTHORITY, "cproducto", CPRODUTO);
		uriMatcher.addURI(AUTHORITY, "cproducto/#", CPRODUTO_ID);
		
		uriMatcher.addURI(AUTHORITY, "cnota", CNOTA);
		uriMatcher.addURI(AUTHORITY, "cnota/#", CNOTA_CPRODUCTOID);
		
		uriMatcher.addURI(AUTHORITY, "SolicitudDescuento", SOLICITUDDESCUENTO);
		uriMatcher.addURI(AUTHORITY, "SolicitudDescuento/#", SOLICITUDDESCUENTO_ID);
		
		uriMatcher.addURI(AUTHORITY, "EncabezadoSolicitud", ENCABEZADOSOLICITUD);
		uriMatcher.addURI(AUTHORITY, "EncabezadoSolicitud/#", ENCABEZADOSOLICITUD_ID);
		
		uriMatcher.addURI(AUTHORITY, "devolucion", DEVOLUCION);
		uriMatcher.addURI(AUTHORITY, "devolucion/#", DEVOLUCION_ID);
		
		uriMatcher.addURI(AUTHORITY, "devolucionproducto", DEVOLUCIONPRODUCTO);
		uriMatcher.addURI(AUTHORITY, "devolucionproducto/#", DEVOLUCIONPRODUCTO_ID);
		
		uriMatcher.addURI(AUTHORITY, "devolucionproducto", DEVOLUCIONPRODUCTO);
		uriMatcher.addURI(AUTHORITY, "devolucionproducto/#", DEVOLUCIONPRODUCTO_ID);
		
		uriMatcher.addURI(AUTHORITY, "devolucionproductolote", DEVOLUCIONPRODUCTOLOTE);
		uriMatcher.addURI(AUTHORITY, "devolucionproductolote/#", DEVOLUCIONPRODUCTOLOTE_ID);
		
	}
	
	@Override
	public boolean onCreate() 
	{		
		dbhelper = new NM_SQLiteHelper(getContext(), DATABASE_NAME, null, BD_VERSION); 
		return true;
	}
	 
	private SQLiteDatabase getOrOpenDataBase()
	{
		SQLiteDatabase bdd=null;
		if(this.db!=null && db.isOpen())
			bdd=db; 
		else if(dbhelper!=null)
			bdd=dbhelper.getWritableDatabase();
		else
		{
		   NM_SQLiteHelper d = new NM_SQLiteHelper(getContext(), DATABASE_NAME, null, BD_VERSION);
		   bdd=d.getWritableDatabase();
		}
		return bdd;
	}	
	
	@SuppressWarnings({ "rawtypes"})
	@Override
	public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder)
	{ 		 
		Cursor c = null; 
		List<Map.Entry> coll=FromWhere(uri);
		String TABLE_NAME_L=(coll.size()>0)?coll.get(0).getValue().toString():null;
		
		String where =selection;
		if(Integer.parseInt(coll.get(1).getKey().toString())!=CONTENT_URI_LOCALID)
			where=coll.get(1).getValue().toString();  
		SQLiteDatabase db = getOrOpenDataBase();		
		c = db.query(TABLE_NAME_L, columns, where,selectionArgs, null, null, sortOrder); 
		return c;
	}
	
	public abstract static class Helper 
	{
		
		private static SQLiteDatabase db;

		public static SQLiteDatabase getDatabase(Context view) {
			// OBTENER LA RUTA DE LA BASE DE DATOS
			String dataBasePath = view.getDatabasePath(DATABASE_NAME).getPath();
			// SI NO EXISTE NINGUNA INSTANCIA DE LA BASE DE DATOS, CREAR UNA
			if (db == null) {
				synchronized (Helper.class) {
					if (db == null) {
						NM_SQLiteHelper d = new NM_SQLiteHelper(view,
								DATABASE_NAME, null, BD_VERSION);
						db = d.getWritableDatabase();
					}
				}
			}
			// SI LA BASE DE DATOS NO SE ENCUENTRA ABIERTA, ABRIRLA
			if (!db.isOpen()) {
				db = view.openOrCreateDatabase(dataBasePath,
						SQLiteDatabase.CREATE_IF_NECESSARY, null);
			}
			// REGRESAR UNA UNICA INSTANCIA DE LA BASE DE DATOS
			return db;
		}

	}
	
	@SuppressWarnings({ "rawtypes"})	
	public static synchronized Cursor query( SQLiteDatabase db, String strQuery)
	{			
		Cursor c = null;  
		c = db.rawQuery(strQuery, null);   		 
		return c;
	}

	public static void RegistrarTasaCambios(JSONArray objL,Context view) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(view, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction();
		BorrarRegistrosTabla(bdd,TABLA_TASACAMBIO);
		RegistrarTasasDeCambios(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{	
			bdd.endTransaction();
			bdd.close();
		}  
	}
			
	private static void RegistrarTasasDeCambios(JSONArray objL,SQLiteDatabase bdd)throws Exception
	{
		JSONObject tsa;
		for(int i=0;i<objL.length();i++)
		{			
			ContentValues values  = new ContentValues();
			tsa  = new JSONObject();
			tsa=objL.getJSONObject(i);
		    values.put(NMConfig.TasaCambio.CodMoneda,tsa.getString(NMConfig.TasaCambio.CodMoneda));
		    values.put(NMConfig.TasaCambio.Fecha,tsa.getInt(NMConfig.TasaCambio.Fecha));
		    values.put(NMConfig.TasaCambio.Tasa,tsa.getDouble(NMConfig.TasaCambio.Tasa));
			bdd.insert(TABLA_TASACAMBIO, null, values);
		}
	}
	
	public static void RegistrarCatalogos(JSONArray objL,Context view) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(view, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction();
		BorrarRegistrosTabla(bdd,TABLA_VALORCATALOGO);
		BorrarRegistrosTabla(bdd,TABLA_CATALOGO); 
	    RegistrarCatalogos(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		}  
	}  
	
	private static void RegistrarCatalogos(JSONArray objL,SQLiteDatabase bdd) throws Exception
	{		
		ContentValues values;  
		JSONObject json,json2;
		JSONArray ajson;
		for(int i=0;i<objL.length();i++)
		{												
		    values  = new ContentValues();
		    json=new JSONObject();
		    json=objL.getJSONObject(i);
		    
		    values.put(NMConfig.Catalogo.Id,json.getInt(NMConfig.Catalogo.Id));
		    values.put(NMConfig.Catalogo.NombreCatalogo,json.getString(NMConfig.Catalogo.NombreCatalogo));
		    
		    ajson=json.getJSONArray("ValoresCatalogo");
		    bdd.insert(TABLA_CATALOGO, null, values);
		    for(int e=0;e<ajson.length();e++)
		    {
		    	json2=new JSONObject();
		    	json2=ajson.getJSONObject(e);
		    	if (json2.getString(NMConfig.Catalogo.ValorCatalogo.Codigo).compareTo("NC") == 0) continue;
		    	values  = new ContentValues();
		    	values.put(NMConfig.Catalogo.ValorCatalogo.Id,json2.getInt(NMConfig.Catalogo.ValorCatalogo.Id));
		    	values.put(NMConfig.Catalogo.ValorCatalogo.Codigo,json2.getString(NMConfig.Catalogo.ValorCatalogo.Codigo));
			    values.put(NMConfig.Catalogo.ValorCatalogo.Descripcion,json2.getString(NMConfig.Catalogo.ValorCatalogo.Descripcion));
			    values.put(NMConfig.Catalogo.ValorCatalogo.objCatalogoID,json.getInt(NMConfig.Catalogo.ValorCatalogo.Id));			    
			    bdd.insert(TABLA_VALORCATALOGO, null, values);
		    }
		   
		} 
	} 
	
	public static void RegistrarProductos(JSONArray objL,Context cnt,int page) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction(); 	 
		if(page==1){
			BorrarRegistrosTabla(bdd,TABLA_PRODUCTO);
			BorrarRegistrosTabla(bdd,TABLA_LOTE); 
		}
		RegistrarProductos(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		}  
	} 
	
	public static void RegistrarProductos(JSONArray objL,Context cnt) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction(); 	 
		BorrarRegistrosTabla(bdd,TABLA_PRODUCTO);
		BorrarRegistrosTabla(bdd,TABLA_LOTE);  
		RegistrarProductos(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		}  
	} 
	
	private static void RegistrarProductos(JSONArray objL,SQLiteDatabase bdd) throws Exception
	{ 
		ContentValues values;   
		JSONArray Llote;
		JSONObject prod,lote; 
			for(int e=0;e<objL.length();e++)
			{
				values  = new ContentValues();  
			    prod=new JSONObject();
				prod = objL.getJSONObject(e);
				values.put(NMConfig.Producto.Id,prod.getLong(NMConfig.Producto.Id));
				values.put(NMConfig.Producto.Codigo,prod.getString(NMConfig.Producto.Codigo));
				values.put(NMConfig.Producto.Nombre,prod.getString(NMConfig.Producto.Nombre));
				values.put(NMConfig.Producto.EsGravable,prod.getBoolean(NMConfig.Producto.EsGravable));
				values.put(NMConfig.Producto.ListaPrecios,prod.getString(NMConfig.Producto.ListaPrecios));
				values.put(NMConfig.Producto.ListaBonificaciones,prod.getString(NMConfig.Producto.ListaBonificaciones));
				values.put(NMConfig.Producto.CatPrecios,prod.getString(NMConfig.Producto.CatPrecios));
				values.put(NMConfig.Producto.Disponible,prod.getInt(NMConfig.Producto.Disponible));
				values.put(NMConfig.Producto.PermiteDevolucion,prod.getBoolean(NMConfig.Producto.PermiteDevolucion));
				values.put(NMConfig.Producto.LoteRequerido,prod.getBoolean(NMConfig.Producto.LoteRequerido));
				values.put(NMConfig.Producto.ObjProveedorID,prod.getLong(NMConfig.Producto.ObjProveedorID));
				values.put(NMConfig.Producto.DiasAntesVen,prod.getInt(NMConfig.Producto.DiasAntesVen));
				values.put(NMConfig.Producto.DiasDespuesVen,prod.getInt(NMConfig.Producto.DiasDespuesVen)); 
				
				bdd.insert(TABLA_PRODUCTO, null, values);
				Llote=new JSONArray();
			    Llote=prod.getJSONArray("ListaLotes");
				for(int b=0;b<Llote.length();b++)
				{ 
					values  = new ContentValues();	
					lote=new JSONObject();
					lote = Llote.getJSONObject(b);
					values.put(NMConfig.Producto.Lote.Id,lote.getLong(NMConfig.Producto.Lote.Id));
					values.put(NMConfig.Producto.Lote.NumeroLote,lote.getString(NMConfig.Producto.Lote.NumeroLote));
					values.put(NMConfig.Producto.Lote.FechaVencimiento,lote.getInt(NMConfig.Producto.Lote.FechaVencimiento));					 
				    values.put(NMConfig.Producto.ObjProductoID,prod.getLong(NMConfig.Producto.Id));
				    bdd.insert(TABLA_LOTE, null, values);
				}
			}  
		
	}

	public static void RegistrarPromociones(JSONArray objL,Context cnt, int page) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction(); 
		if(page==1)
		BorrarRegistrosTabla(bdd,TABLA_PROMOCION); 
		RegistrarPromociones(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		}  
	} 	
	
	private static void RegistrarPromociones(JSONArray objL,SQLiteDatabase bdd) throws Exception
	{ 
		ContentValues values;  
		JSONObject prom;
 
		for(int e=0;e<objL.length();e++)
		{
			values  = new ContentValues();  
			prom=new JSONObject();
			prom = objL.getJSONObject(e);
			values.put(NMConfig.Promocion.Id,prom.getLong(NMConfig.Promocion.Id)); 
			values.put(NMConfig.Producto.Codigo,prom.getString(NMConfig.Promocion.Codigo)); 
			values.put(NMConfig.Promocion.Descripcion,prom.getString(NMConfig.Promocion.Descripcion));
			values.put(NMConfig.Promocion.AplicaCredito,(prom.getBoolean(NMConfig.Promocion.AplicaCredito)==true)?1:0);
			values.put(NMConfig.Promocion.FechaFin,prom.getInt(NMConfig.Promocion.FechaFin));
			values.put(NMConfig.Promocion.MomentoAplicacion,prom.getString(NMConfig.Promocion.MomentoAplicacion));
			values.put(NMConfig.Promocion.TipoPromo,prom.getString(NMConfig.Promocion.TipoPromo));
			values.put(NMConfig.Promocion.MontoMinimo,prom.getDouble(NMConfig.Promocion.MontoMinimo));
			values.put(NMConfig.Promocion.TipoDescuento,prom.getString(NMConfig.Promocion.TipoDescuento));
			values.put(NMConfig.Promocion.Descuento,prom.getDouble(NMConfig.Promocion.Descuento));
			values.put(NMConfig.Promocion.AplicacionMultiple,(prom.getBoolean(NMConfig.Promocion.AplicacionMultiple)==true)?1:0);
			values.put(NMConfig.Promocion.CantidadMinimaItems,prom.getInt(NMConfig.Promocion.CantidadMinimaItems));
			values.put(NMConfig.Promocion.CantidadMinimaBaseUnica,(prom.getBoolean(NMConfig.Promocion.CantidadMinimaBaseUnica)==true)?1:0); 
			values.put(NMConfig.Promocion.CantidadMinimaBase,prom.getInt(NMConfig.Promocion.CantidadMinimaBase));
			values.put(NMConfig.Promocion.CantidadPremioUnica,(prom.getBoolean(NMConfig.Promocion.CantidadPremioUnica)==true)?1:0);
			values.put(NMConfig.Promocion.CantidadPremio,prom.getInt(NMConfig.Promocion.CantidadPremio));
			values.put(NMConfig.Promocion.ProductosOtorgadosPor,prom.getString(NMConfig.Promocion.ProductosOtorgadosPor));
			values.put(NMConfig.Promocion.MontoEntregadoPor,prom.getString(NMConfig.Promocion.MontoEntregadoPor));
			values.put(NMConfig.Promocion.DescripcionPromocion,prom.getString(NMConfig.Promocion.DescripcionPromocion));
			values.put(NMConfig.Promocion.CatClientes,prom.getString(NMConfig.Promocion.CatClientes));
			values.put(NMConfig.Promocion.TiposCliente,prom.getString(NMConfig.Promocion.TiposCliente));
			values.put(NMConfig.Promocion.Sucursales,prom.getString(NMConfig.Promocion.Sucursales));
			values.put(NMConfig.Promocion.Ubicaciones,prom.getString(NMConfig.Promocion.Ubicaciones));
			values.put(NMConfig.Promocion.ProdsBase,prom.getString(NMConfig.Promocion.ProdsBase));
			values.put(NMConfig.Promocion.ProdsPremio,prom.getString(NMConfig.Promocion.ProdsPremio)); 
			values.put(NMConfig.Promocion.MontoBaseUnico,(prom.getBoolean(NMConfig.Promocion.MontoBaseUnico)==true)?1:0);
			values.put(NMConfig.Promocion.MontoBaseMinimo,prom.getDouble(NMConfig.Promocion.MontoBaseMinimo));
			values.put(NMConfig.Promocion.MontoBaseMaximo,prom.getDouble(NMConfig.Promocion.MontoBaseMaximo));
			values.put(NMConfig.Promocion.MontoPremioUnico,(prom.getBoolean(NMConfig.Promocion.MontoPremioUnico)==true)?1:0);
			values.put(NMConfig.Promocion.MontoPremio,prom.getDouble(NMConfig.Promocion.MontoPremio)); 
			
			bdd.insert(TABLA_PROMOCION, null, values);
			 
		}  
	}
	 
	public static void  ActualizarCliente(JSONObject cliente,Context cnt) throws Exception
	{	  
		SQLiteDatabase bdd = null;NM_SQLiteHelper d;
		try 
		{
			
		
			d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
			bdd=d.getWritableDatabase();
			bdd.beginTransaction(); 	
			ContentValues values;
			if(BorrarCliente(cliente,bdd)==1); 
			{
				
				values = new ContentValues();
				values.put(NMConfig.Cliente.IdCliente,cliente.getLong(NMConfig.Cliente.IdCliente));
				values.put(NMConfig.Cliente.NombreCliente,cliente.getString(NMConfig.Cliente.NombreCliente));
				values.put(NMConfig.Cliente.IdSucursal,cliente.getLong(NMConfig.Cliente.IdSucursal));
				values.put(NMConfig.Cliente.Codigo,cliente.getString(NMConfig.Cliente.Codigo));
				values.put(NMConfig.Cliente.CodTipoPrecio,cliente.getString(NMConfig.Cliente.CodTipoPrecio));
				values.put(NMConfig.Cliente.DesTipoPrecio,cliente.getString(NMConfig.Cliente.DesTipoPrecio));
				values.put(NMConfig.Cliente.objPrecioVentaID,cliente.getLong(NMConfig.Cliente.objPrecioVentaID));
				values.put(NMConfig.Cliente.objCategoriaClienteID,cliente.getLong(NMConfig.Cliente.objCategoriaClienteID));
				values.put(NMConfig.Cliente.objTipoClienteID,cliente.getLong(NMConfig.Cliente.objTipoClienteID));
				values.put(NMConfig.Cliente.AplicaBonificacion,cliente.getBoolean(NMConfig.Cliente.AplicaBonificacion));
				values.put(NMConfig.Cliente.PermiteBonifEspecial,cliente.getBoolean(NMConfig.Cliente.PermiteBonifEspecial));
				values.put(NMConfig.Cliente.PermitePrecioEspecial,cliente.getBoolean(NMConfig.Cliente.PermitePrecioEspecial));
				values.put(NMConfig.Cliente.UG,cliente.getString(NMConfig.Cliente.UG));
				values.put(NMConfig.Cliente.Ubicacion,cliente.getString(NMConfig.Cliente.Ubicacion));
				values.put(NMConfig.Cliente.NombreLegalCliente,cliente.getString(NMConfig.Cliente.NombreLegalCliente));
				values.put(NMConfig.Cliente.AplicaOtrasDeducciones,cliente.getBoolean(NMConfig.Cliente.AplicaBonificacion));
				values.put(NMConfig.Cliente.MontoMinimoAbono,cliente.getDouble(NMConfig.Cliente.MontoMinimoAbono));
				values.put(NMConfig.Cliente.PlazoDescuento,cliente.getInt(NMConfig.Cliente.PlazoDescuento));
				values.put(NMConfig.Cliente.PermiteDevolucion,cliente.getBoolean(NMConfig.Cliente.PermiteDevolucion));
				bdd.insert(TABLA_CLIENTE, null, values);	
				JSONArray af=new JSONArray();
				af=cliente.getJSONArray("FacturasPendientes");
				for(int i=0;i<af.length();i++)
				{
					values  = new ContentValues();
					JSONObject fact=new JSONObject();
					fact=af.getJSONObject(i);
					values.put(NMConfig.Cliente.Factura.Id,fact.getLong(NMConfig.Cliente.Factura.Id));
					values.put(NMConfig.Cliente.Factura.NombreSucursal,fact.getString(NMConfig.Cliente.Factura.NombreSucursal));
					values.put(NMConfig.Cliente.Factura.NoFactura,fact.getString(NMConfig.Cliente.Factura.NoFactura));
					values.put(NMConfig.Cliente.Factura.Tipo,fact.getString(NMConfig.Cliente.Factura.Tipo));
					values.put(NMConfig.Cliente.Factura.NoPedido,fact.getString(NMConfig.Cliente.Factura.NoPedido));
					values.put(NMConfig.Cliente.Factura.CodEstado,fact.getString(NMConfig.Cliente.Factura.CodEstado));
					values.put(NMConfig.Cliente.Factura.Estado,fact.getString(NMConfig.Cliente.Factura.Estado));
					values.put(NMConfig.Cliente.Factura.Fecha,fact.getLong(NMConfig.Cliente.Factura.Fecha));
					values.put(NMConfig.Cliente.Factura.FechaVencimiento,fact.getLong(NMConfig.Cliente.Factura.FechaVencimiento));
					values.put(NMConfig.Cliente.Factura.FechaAppDescPP,fact.getLong(NMConfig.Cliente.Factura.FechaAppDescPP));
					values.put(NMConfig.Cliente.Factura.Dias,fact.getInt(NMConfig.Cliente.Factura.Dias));
					values.put(NMConfig.Cliente.Factura.TotalFacturado,fact.getDouble(NMConfig.Cliente.Factura.TotalFacturado));
					values.put(NMConfig.Cliente.Factura.Abonado,fact.getDouble(NMConfig.Cliente.Factura.Abonado));
					values.put(NMConfig.Cliente.Factura.Descontado,fact.getDouble(NMConfig.Cliente.Factura.Descontado));
					values.put(NMConfig.Cliente.Factura.Retenido,fact.getDouble(NMConfig.Cliente.Factura.Retenido));
					values.put(NMConfig.Cliente.Factura.Otro,fact.getDouble(NMConfig.Cliente.Factura.Otro));
					values.put(NMConfig.Cliente.Factura.Saldo,fact.getDouble(NMConfig.Cliente.Factura.Saldo));
					values.put(NMConfig.Cliente.Factura.Exenta,fact.getBoolean(NMConfig.Cliente.Factura.Exenta));
					values.put(NMConfig.Cliente.Factura.SubtotalFactura,fact.getDouble(NMConfig.Cliente.Factura.SubtotalFactura));
					values.put(NMConfig.Cliente.Factura.DescuentoFactura,fact.getDouble(NMConfig.Cliente.Factura.DescuentoFactura));
					values.put(NMConfig.Cliente.Factura.ImpuestoFactura,fact.getDouble(NMConfig.Cliente.Factura.ImpuestoFactura));
					values.put(NMConfig.Cliente.Factura.PuedeAplicarDescPP,fact.getBoolean(NMConfig.Cliente.Factura.PuedeAplicarDescPP));
					values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
					bdd.insert(TABLA_FACTURA, null, values);
	
					JSONArray adpc=new JSONArray();
					adpc=fact.getJSONArray("DetallePromocionCobro");
					for(int o=0;o<adpc.length();o++)
					{
						values  = new ContentValues();
						JSONObject dpc=new JSONObject();
						dpc=adpc.getJSONObject(o);
						values.put(NMConfig.Cliente.Factura.PromocionCobro.FacturasAplicacion,dpc.getString(NMConfig.Cliente.Factura.PromocionCobro.FacturasAplicacion));
						values.put(NMConfig.Cliente.Factura.PromocionCobro.TipoDescuento,dpc.getString(NMConfig.Cliente.Factura.PromocionCobro.TipoDescuento));
						values.put(NMConfig.Cliente.Factura.PromocionCobro.Descuento,dpc.getDouble(NMConfig.Cliente.Factura.PromocionCobro.Descuento));
						values.put(NMConfig.Cliente.Factura.objFacturaID,fact.getLong(NMConfig.Cliente.Factura.Id));
						bdd.insert(TABLA_PROMOCIONCOBRO, null, values);
					}
					JSONArray admp=new JSONArray();
					admp=fact.getJSONArray("DetalleMontoProveedor");
					for(int u=0;u<admp.length();u++)
					{
						values  = new ContentValues();
						JSONObject dmp=new JSONObject();
						dmp=admp.getJSONObject(u);
						values.put(NMConfig.Cliente.Factura.MontoProveedor.ObjProveedorID,dmp.getLong(NMConfig.Cliente.Factura.MontoProveedor.ObjProveedorID));
						values.put(NMConfig.Cliente.Factura.MontoProveedor.CodProveedor,dmp.getString(NMConfig.Cliente.Factura.MontoProveedor.CodProveedor));
						values.put(NMConfig.Cliente.Factura.MontoProveedor.Monto,dmp.getDouble(NMConfig.Cliente.Factura.MontoProveedor.Monto));
						values.put(NMConfig.Cliente.Factura.objFacturaID,fact.getLong(NMConfig.Cliente.Factura.Id));
						bdd.insert(TABLA_MONTOPROVEEDOR, null, values);
					}
					
					
				}
				JSONArray anc=new JSONArray();
				anc=cliente.getJSONArray("NotasCreditoPendientes");			
				for(int b=0;b<anc.length();b++)
				{ 
					values  = new ContentValues();
					JSONObject nc=new JSONObject();
					nc=anc.getJSONObject(b);
					values.put(NMConfig.Cliente.CCNotaCredito.Id,nc.getLong(NMConfig.Cliente.CCNotaCredito.Id));
					values.put(NMConfig.Cliente.CCNotaCredito.NombreSucursal,nc.getString(NMConfig.Cliente.CCNotaCredito.NombreSucursal));
					values.put(NMConfig.Cliente.CCNotaCredito.Estado,nc.getString(NMConfig.Cliente.CCNotaCredito.Estado));
					values.put(NMConfig.Cliente.CCNotaCredito.Numero,nc.getString(NMConfig.Cliente.CCNotaCredito.Numero));
					values.put(NMConfig.Cliente.CCNotaCredito.Fecha,nc.getLong(NMConfig.Cliente.CCNotaCredito.Fecha));
					values.put(NMConfig.Cliente.CCNotaCredito.FechaVence,nc.getLong(NMConfig.Cliente.CCNotaCredito.FechaVence));
					values.put(NMConfig.Cliente.CCNotaCredito.Concepto,nc.getString(NMConfig.Cliente.CCNotaCredito.Concepto));
					values.put(NMConfig.Cliente.CCNotaCredito.Monto,nc.getDouble(NMConfig.Cliente.CCNotaCredito.Monto));
					values.put(NMConfig.Cliente.CCNotaCredito.NumRColAplic,nc.getString(NMConfig.Cliente.CCNotaCredito.NumRColAplic));
					values.put(NMConfig.Cliente.CCNotaCredito.CodEstado,nc.getString(NMConfig.Cliente.CCNotaCredito.CodEstado));
					values.put(NMConfig.Cliente.CCNotaCredito.Descripcion,nc.getString(NMConfig.Cliente.CCNotaCredito.Descripcion));
					values.put(NMConfig.Cliente.CCNotaCredito.Referencia,nc.getInt(NMConfig.Cliente.CCNotaCredito.Referencia));
					values.put(NMConfig.Cliente.CCNotaCredito.CodConcepto,nc.getString(NMConfig.Cliente.CCNotaCredito.CodConcepto));
					values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
					bdd.insert(TABLA_CCNOTACREDITO, null, values);
				}
				
				JSONArray and=new JSONArray();
				and=cliente.getJSONArray("NotasDebitoPendientes");			
				for(int a=0;a<and.length();a++)
				{ 
					values  = new ContentValues();
					JSONObject nd=new JSONObject();
					nd=and.getJSONObject(a);
					values.put(NMConfig.Cliente.CCNotaCredito.Id,nd.getLong(NMConfig.Cliente.CCNotaDebito.Id));
					values.put(NMConfig.Cliente.CCNotaDebito.NombreSucursal,nd.getString(NMConfig.Cliente.CCNotaDebito.NombreSucursal));
					values.put(NMConfig.Cliente.CCNotaDebito.Estado,nd.getString(NMConfig.Cliente.CCNotaDebito.Estado));
					values.put(NMConfig.Cliente.CCNotaDebito.Numero,nd.getString(NMConfig.Cliente.CCNotaDebito.Numero));
					values.put(NMConfig.Cliente.CCNotaDebito.Fecha,nd.getLong(NMConfig.Cliente.CCNotaDebito.Fecha));
					values.put(NMConfig.Cliente.CCNotaDebito.FechaVence,nd.getLong(NMConfig.Cliente.CCNotaDebito.FechaVence));
					values.put(NMConfig.Cliente.CCNotaDebito.Dias,nd.getInt(NMConfig.Cliente.CCNotaDebito.Dias));
					values.put(NMConfig.Cliente.CCNotaDebito.Concepto,nd.getString(NMConfig.Cliente.CCNotaDebito.Concepto));
					values.put(NMConfig.Cliente.CCNotaDebito.Monto,nd.getDouble(NMConfig.Cliente.CCNotaDebito.Monto));
					values.put(NMConfig.Cliente.CCNotaDebito.MontoAbonado,nd.getDouble(NMConfig.Cliente.CCNotaDebito.MontoAbonado));
					values.put(NMConfig.Cliente.CCNotaDebito.Saldo,nd.getDouble(NMConfig.Cliente.CCNotaDebito.Saldo));
					values.put(NMConfig.Cliente.CCNotaDebito.CodEstado,nd.getString(NMConfig.Cliente.CCNotaDebito.CodEstado));
					values.put(NMConfig.Cliente.CCNotaDebito.Descripcion,nd.getString(NMConfig.Cliente.CCNotaDebito.Descripcion));
					values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
					bdd.insert(TABLA_CCNOTADEBITO, null, values);
				}
				
				JSONArray adp=new JSONArray();
				adp=cliente.getJSONArray("DescuentosProveedor");		
				for(int c=0;c<adp.length();c++)
				{
					values  = new ContentValues();
					JSONObject dp=new JSONObject();
					dp=adp.getJSONObject(c);
					values.put(NMConfig.Cliente.DescuentoProveedor.ObjProveedorID,dp.getLong(NMConfig.Cliente.DescuentoProveedor.ObjProveedorID));
					values.put(NMConfig.Cliente.DescuentoProveedor.PrcDescuento,dp.getDouble(NMConfig.Cliente.DescuentoProveedor.PrcDescuento));
					values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
					bdd.insert(TABLA_DESCUENTOPROVEEDOR, null, values);
				}
				
			}
			
			bdd.setTransactionSuccessful();  
			
			if(bdd!=null || (bdd.isOpen()))
			{				
				bdd.endTransaction();
				bdd.close();
			}
		} catch (Exception e) 
		{ 
			if(bdd!=null || (bdd.isOpen()))
			{				
				bdd.endTransaction();
				bdd.close();
			}
			throw new Exception(e);
		}
	}
	
	public static void RegistrarClientes(JSONArray objL,Context cnt,int page) throws Exception
	{		 
		NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);
		SQLiteDatabase bdd=d.getWritableDatabase();
		bdd.beginTransaction();
		if(page==1)
			BorrarRegistrosTablaCliente(bdd);
	    RegistrarClientes(objL,bdd);
		bdd.setTransactionSuccessful(); 
	 
		if(bdd!=null || (bdd.isOpen()))
		{				
			bdd.endTransaction();
			bdd.close();
		}  
	} 
	
	private static void RegistrarClientes(JSONArray objL,SQLiteDatabase bdd) throws Exception
	{	
		ContentValues values;  
		JSONObject cliente,fact,nc,nd,dpc,dmp,dp;
		JSONArray af,anc,and,adpc,admp,adp;
 
		for(int e=0;e<objL.length();e++)
		{
			values  = new ContentValues();  
			cliente=new JSONObject();
			cliente = objL.getJSONObject(e);
			
			values.put(NMConfig.Cliente.IdCliente,cliente.getLong(NMConfig.Cliente.IdCliente));
			values.put(NMConfig.Cliente.NombreCliente,cliente.getString(NMConfig.Cliente.NombreCliente));
			values.put(NMConfig.Cliente.IdSucursal,cliente.getLong(NMConfig.Cliente.IdSucursal));
			values.put(NMConfig.Cliente.Codigo,cliente.getString(NMConfig.Cliente.Codigo));
			values.put(NMConfig.Cliente.CodTipoPrecio,cliente.getString(NMConfig.Cliente.CodTipoPrecio));
			values.put(NMConfig.Cliente.DesTipoPrecio,cliente.getString(NMConfig.Cliente.DesTipoPrecio));
			values.put(NMConfig.Cliente.objPrecioVentaID,cliente.getLong(NMConfig.Cliente.objPrecioVentaID));
			values.put(NMConfig.Cliente.objCategoriaClienteID,cliente.getLong(NMConfig.Cliente.objCategoriaClienteID));
			values.put(NMConfig.Cliente.objTipoClienteID,cliente.getLong(NMConfig.Cliente.objTipoClienteID));
			values.put(NMConfig.Cliente.AplicaBonificacion,cliente.getBoolean(NMConfig.Cliente.AplicaBonificacion));
			values.put(NMConfig.Cliente.PermiteBonifEspecial,cliente.getBoolean(NMConfig.Cliente.PermiteBonifEspecial));
			values.put(NMConfig.Cliente.PermitePrecioEspecial,cliente.getBoolean(NMConfig.Cliente.PermitePrecioEspecial));
			values.put(NMConfig.Cliente.UG,cliente.getString(NMConfig.Cliente.UG));
			values.put(NMConfig.Cliente.Ubicacion,cliente.getString(NMConfig.Cliente.Ubicacion));
			values.put(NMConfig.Cliente.NombreLegalCliente,cliente.getString(NMConfig.Cliente.NombreLegalCliente));
			values.put(NMConfig.Cliente.AplicaOtrasDeducciones,cliente.getBoolean(NMConfig.Cliente.AplicaBonificacion));
			values.put(NMConfig.Cliente.MontoMinimoAbono,cliente.getDouble(NMConfig.Cliente.MontoMinimoAbono));
			values.put(NMConfig.Cliente.PlazoDescuento,cliente.getInt(NMConfig.Cliente.PlazoDescuento));
			values.put(NMConfig.Cliente.PermiteDevolucion,cliente.getBoolean(NMConfig.Cliente.PermiteDevolucion));
			bdd.insert(TABLA_CLIENTE, null, values);	
			af=new JSONArray();
			af=cliente.getJSONArray("FacturasPendientes");
			for(int i=0;i<af.length();i++)
			{
				values  = new ContentValues();
				fact=new JSONObject();
				fact=af.getJSONObject(i);
				values.put(NMConfig.Cliente.Factura.Id,fact.getLong(NMConfig.Cliente.Factura.Id));
				values.put(NMConfig.Cliente.Factura.NombreSucursal,fact.getString(NMConfig.Cliente.Factura.NombreSucursal));
				values.put(NMConfig.Cliente.Factura.NoFactura,fact.getString(NMConfig.Cliente.Factura.NoFactura));
				values.put(NMConfig.Cliente.Factura.Tipo,fact.getString(NMConfig.Cliente.Factura.Tipo));
				values.put(NMConfig.Cliente.Factura.NoPedido,fact.getString(NMConfig.Cliente.Factura.NoPedido));
				values.put(NMConfig.Cliente.Factura.CodEstado,fact.getString(NMConfig.Cliente.Factura.CodEstado));
				values.put(NMConfig.Cliente.Factura.Estado,fact.getString(NMConfig.Cliente.Factura.Estado));
				values.put(NMConfig.Cliente.Factura.Fecha,fact.getLong(NMConfig.Cliente.Factura.Fecha));
				values.put(NMConfig.Cliente.Factura.FechaVencimiento,fact.getLong(NMConfig.Cliente.Factura.FechaVencimiento));
				values.put(NMConfig.Cliente.Factura.FechaAppDescPP,fact.getLong(NMConfig.Cliente.Factura.FechaAppDescPP));
				values.put(NMConfig.Cliente.Factura.Dias,fact.getInt(NMConfig.Cliente.Factura.Dias));
				values.put(NMConfig.Cliente.Factura.TotalFacturado,fact.getDouble(NMConfig.Cliente.Factura.TotalFacturado));
				values.put(NMConfig.Cliente.Factura.Abonado,fact.getDouble(NMConfig.Cliente.Factura.Abonado));
				values.put(NMConfig.Cliente.Factura.Descontado,fact.getDouble(NMConfig.Cliente.Factura.Descontado));
				values.put(NMConfig.Cliente.Factura.Retenido,fact.getDouble(NMConfig.Cliente.Factura.Retenido));
				values.put(NMConfig.Cliente.Factura.Otro,fact.getDouble(NMConfig.Cliente.Factura.Otro));
				values.put(NMConfig.Cliente.Factura.Saldo,fact.getDouble(NMConfig.Cliente.Factura.Saldo));
				values.put(NMConfig.Cliente.Factura.Exenta,fact.getBoolean(NMConfig.Cliente.Factura.Exenta));
				values.put(NMConfig.Cliente.Factura.SubtotalFactura,fact.getDouble(NMConfig.Cliente.Factura.SubtotalFactura));
				values.put(NMConfig.Cliente.Factura.DescuentoFactura,fact.getDouble(NMConfig.Cliente.Factura.DescuentoFactura));
				values.put(NMConfig.Cliente.Factura.ImpuestoFactura,fact.getDouble(NMConfig.Cliente.Factura.ImpuestoFactura));
				values.put(NMConfig.Cliente.Factura.PuedeAplicarDescPP,fact.getBoolean(NMConfig.Cliente.Factura.PuedeAplicarDescPP));
				values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
				bdd.insert(TABLA_FACTURA, null, values);

				adpc=new JSONArray();
				adpc=fact.getJSONArray("DetallePromocionCobro");
				for(int o=0;o<adpc.length();o++)
				{
					values  = new ContentValues();
					dpc=new JSONObject();
					dpc=adpc.getJSONObject(o);
					values.put(NMConfig.Cliente.Factura.PromocionCobro.FacturasAplicacion,dpc.getString(NMConfig.Cliente.Factura.PromocionCobro.FacturasAplicacion));
					values.put(NMConfig.Cliente.Factura.PromocionCobro.TipoDescuento,dpc.getString(NMConfig.Cliente.Factura.PromocionCobro.TipoDescuento));
					values.put(NMConfig.Cliente.Factura.PromocionCobro.Descuento,dpc.getDouble(NMConfig.Cliente.Factura.PromocionCobro.Descuento));
					values.put(NMConfig.Cliente.Factura.objFacturaID,fact.getLong(NMConfig.Cliente.Factura.Id));
					bdd.insert(TABLA_PROMOCIONCOBRO, null, values);
				}
				admp=new JSONArray();
				admp=fact.getJSONArray("DetalleMontoProveedor");
				for(int u=0;u<admp.length();u++)
				{
					values  = new ContentValues();
					dmp=new JSONObject();
					dmp=admp.getJSONObject(u);
					values.put(NMConfig.Cliente.Factura.MontoProveedor.ObjProveedorID,dmp.getLong(NMConfig.Cliente.Factura.MontoProveedor.ObjProveedorID));
					values.put(NMConfig.Cliente.Factura.MontoProveedor.CodProveedor,dmp.getString(NMConfig.Cliente.Factura.MontoProveedor.CodProveedor));
					values.put(NMConfig.Cliente.Factura.MontoProveedor.Monto,dmp.getDouble(NMConfig.Cliente.Factura.MontoProveedor.Monto));
					values.put(NMConfig.Cliente.Factura.objFacturaID,fact.getLong(NMConfig.Cliente.Factura.Id));
					bdd.insert(TABLA_MONTOPROVEEDOR, null, values);
				}
				
				
			}
			anc=new JSONArray();
			anc=cliente.getJSONArray("NotasCreditoPendientes");			
			for(int b=0;b<anc.length();b++)
			{ 
				values  = new ContentValues();
				nc=new JSONObject();
				nc=anc.getJSONObject(b);
				values.put(NMConfig.Cliente.CCNotaCredito.Id,nc.getLong(NMConfig.Cliente.CCNotaCredito.Id));
				values.put(NMConfig.Cliente.CCNotaCredito.NombreSucursal,nc.getString(NMConfig.Cliente.CCNotaCredito.NombreSucursal));
				values.put(NMConfig.Cliente.CCNotaCredito.Estado,nc.getString(NMConfig.Cliente.CCNotaCredito.Estado));
				values.put(NMConfig.Cliente.CCNotaCredito.Numero,nc.getString(NMConfig.Cliente.CCNotaCredito.Numero));
				values.put(NMConfig.Cliente.CCNotaCredito.Fecha,nc.getLong(NMConfig.Cliente.CCNotaCredito.Fecha));
				values.put(NMConfig.Cliente.CCNotaCredito.FechaVence,nc.getLong(NMConfig.Cliente.CCNotaCredito.FechaVence));
				values.put(NMConfig.Cliente.CCNotaCredito.Concepto,nc.getString(NMConfig.Cliente.CCNotaCredito.Concepto));
				values.put(NMConfig.Cliente.CCNotaCredito.Monto,nc.getDouble(NMConfig.Cliente.CCNotaCredito.Monto));
				values.put(NMConfig.Cliente.CCNotaCredito.NumRColAplic,nc.getString(NMConfig.Cliente.CCNotaCredito.NumRColAplic));
				values.put(NMConfig.Cliente.CCNotaCredito.CodEstado,nc.getString(NMConfig.Cliente.CCNotaCredito.CodEstado));
				values.put(NMConfig.Cliente.CCNotaCredito.Descripcion,nc.getString(NMConfig.Cliente.CCNotaCredito.Descripcion));
				values.put(NMConfig.Cliente.CCNotaCredito.Referencia,nc.getInt(NMConfig.Cliente.CCNotaCredito.Referencia));
				values.put(NMConfig.Cliente.CCNotaCredito.CodConcepto,nc.getString(NMConfig.Cliente.CCNotaCredito.CodConcepto));
				values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
				bdd.insert(TABLA_CCNOTACREDITO, null, values);
			}
			
			and=new JSONArray();
			and=cliente.getJSONArray("NotasDebitoPendientes");			
			for(int a=0;a<and.length();a++)
			{ 
				values  = new ContentValues();
				nd=new JSONObject();
				nd=and.getJSONObject(a);
				values.put(NMConfig.Cliente.CCNotaCredito.Id,nd.getLong(NMConfig.Cliente.CCNotaDebito.Id));
				values.put(NMConfig.Cliente.CCNotaDebito.NombreSucursal,nd.getString(NMConfig.Cliente.CCNotaDebito.NombreSucursal));
				values.put(NMConfig.Cliente.CCNotaDebito.Estado,nd.getString(NMConfig.Cliente.CCNotaDebito.Estado));
				values.put(NMConfig.Cliente.CCNotaDebito.Numero,nd.getString(NMConfig.Cliente.CCNotaDebito.Numero));
				values.put(NMConfig.Cliente.CCNotaDebito.Fecha,nd.getLong(NMConfig.Cliente.CCNotaDebito.Fecha));
				values.put(NMConfig.Cliente.CCNotaDebito.FechaVence,nd.getLong(NMConfig.Cliente.CCNotaDebito.FechaVence));
				values.put(NMConfig.Cliente.CCNotaDebito.Dias,nd.getInt(NMConfig.Cliente.CCNotaDebito.Dias));
				values.put(NMConfig.Cliente.CCNotaDebito.Concepto,nd.getString(NMConfig.Cliente.CCNotaDebito.Concepto));
				values.put(NMConfig.Cliente.CCNotaDebito.Monto,nd.getDouble(NMConfig.Cliente.CCNotaDebito.Monto));
				values.put(NMConfig.Cliente.CCNotaDebito.MontoAbonado,nd.getDouble(NMConfig.Cliente.CCNotaDebito.MontoAbonado));
				values.put(NMConfig.Cliente.CCNotaDebito.Saldo,nd.getDouble(NMConfig.Cliente.CCNotaDebito.Saldo));
				values.put(NMConfig.Cliente.CCNotaDebito.CodEstado,nd.getString(NMConfig.Cliente.CCNotaDebito.CodEstado));
				values.put(NMConfig.Cliente.CCNotaDebito.Descripcion,nd.getString(NMConfig.Cliente.CCNotaDebito.Descripcion));
				values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
				bdd.insert(TABLA_CCNOTADEBITO, null, values);
			}
			
			adp=new JSONArray();
			adp=cliente.getJSONArray("DescuentosProveedor");		
			for(int c=0;c<adp.length();c++)
			{
				values  = new ContentValues();
				dp=new JSONObject();
				dp=adp.getJSONObject(c);
				values.put(NMConfig.Cliente.DescuentoProveedor.ObjProveedorID,dp.getLong(NMConfig.Cliente.DescuentoProveedor.ObjProveedorID));
				values.put(NMConfig.Cliente.DescuentoProveedor.PrcDescuento,dp.getDouble(NMConfig.Cliente.DescuentoProveedor.PrcDescuento));
				values.put(NMConfig.Cliente.objSucursalID,cliente.getLong(NMConfig.Cliente.IdSucursal));
				bdd.insert(TABLA_DESCUENTOPROVEEDOR, null, values);
			}
			
			
		}
		
	}
	
	public static int BorrarCliente(JSONObject cliente,SQLiteDatabase bdd) throws Exception
    { 		
		bdd.delete(TABLA_DESCUENTOPROVEEDOR, NMConfig.Cliente.objSucursalID+"="+String.valueOf(cliente.getLong(NMConfig.Cliente.IdSucursal)),null);
		bdd.delete(TABLA_CCNOTADEBITO, NMConfig.Cliente.objSucursalID+"="+String.valueOf(cliente.getLong(NMConfig.Cliente.IdSucursal)),null);
		bdd.delete(TABLA_CCNOTACREDITO, NMConfig.Cliente.objSucursalID+"="+String.valueOf(cliente.getLong(NMConfig.Cliente.IdSucursal)),null);
		
		JSONArray af=new JSONArray();
		af=cliente.getJSONArray("FacturasPendientes");
		for(int i=0;i<af.length();i++)
		{
			JSONObject fact = new JSONObject();
			fact=af.getJSONObject(i);
			bdd.delete(TABLA_MONTOPROVEEDOR, NMConfig.Cliente.Factura.objFacturaID+"="+String.valueOf(fact.getLong(NMConfig.Cliente.Factura.Id)),null);
			bdd.delete(TABLA_PROMOCIONCOBRO, NMConfig.Cliente.Factura.objFacturaID+"="+String.valueOf(fact.getLong(NMConfig.Cliente.Factura.Id)),null);
		}
		
		bdd.delete(TABLA_FACTURA, NMConfig.Cliente.objSucursalID+"="+String.valueOf(cliente.getLong(NMConfig.Cliente.IdSucursal)),null);
		bdd.delete(TABLA_CLIENTE, NMConfig.Cliente.IdSucursal+"="+String.valueOf(cliente.getLong(NMConfig.Cliente.IdSucursal)),null);			
		
		return 1;
    }
	
	public static void BorrarCliente(ArrayList<Cliente> objL,SQLiteDatabase bdd) throws Exception
    {  
		for(Cliente client:objL)
		{	
			
			bdd.delete(TABLA_DESCUENTOPROVEEDOR, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
			bdd.delete(TABLA_CCNOTADEBITO, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
			bdd.delete(TABLA_CCNOTACREDITO, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
			if(client.getFacturasPendientes()!=null)
			{
				for(Factura fact: client.getFacturasPendientes())
				{
					bdd.delete(TABLA_MONTOPROVEEDOR, NMConfig.Cliente.Factura.objFacturaID+"="+String.valueOf(fact.getId()),null);
					bdd.delete(TABLA_PROMOCIONCOBRO, NMConfig.Cliente.Factura.objFacturaID+"="+String.valueOf(fact.getId()),null);
				}
			}
			bdd.delete(TABLA_FACTURA, NMConfig.Cliente.objSucursalID+"="+String.valueOf(client.getIdSucursal()),null);
			bdd.delete(TABLA_CLIENTE, NMConfig.Cliente.IdSucursal+"="+String.valueOf(client.getIdSucursal()),null);			
		}	 
		 
    }
   
	public static void BorrarRegistrosTablaCliente(SQLiteDatabase bdd)
    {
    	bdd.delete(TABLA_DESCUENTOPROVEEDOR, null,null);
    	bdd.delete(TABLA_CCNOTADEBITO, null,null);
    	bdd.delete(TABLA_CCNOTACREDITO, null,null);
    	bdd.delete(TABLA_MONTOPROVEEDOR, null,null);
    	bdd.delete(TABLA_PROMOCIONCOBRO, null,null); 
		bdd.delete(TABLA_FACTURA, null,null);
		bdd.delete(TABLA_CLIENTE, null,null); 
    }
 
	public static void BorrarRegistrosTabla(SQLiteDatabase bdd, String TABLA){
		bdd.delete(TABLA, null,null); 
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int update(Uri uri, ContentValues values,String selection, String[] selectionArgs) 
	{
		
		int cont;
		//Si es una consulta a un ID concreto construimos el WHERE		 
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		db.beginTransaction();
		List<Map.Entry> coll=FromWhere(uri);
		String TablaName=coll.get(0).getValue().toString();
		String where =coll.get(1).getValue().toString(); 
		cont = db.update(TablaName, values, where, selectionArgs);
		db.endTransaction();
		return cont;
	}
	
	@SuppressLint("UseSparseArrays")
	@SuppressWarnings({ "rawtypes"}) 
	public List<Map.Entry> FromWhere(Uri uri)
	{
		
		LinkedHashMap <Integer,String> dictionary=new LinkedHashMap <Integer,String>();
		ArrayList<Map.Entry> e=new ArrayList<Map.Entry>(2); 
		 
		switch(uriMatcher.match(uri))
		{
			case CLIENTE:	   			dictionary.put(CLIENTE,TABLA_CLIENTE); 
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CLIENTE.toString());
							   			break;
							   		
			case CLIENTE_ID:   			dictionary.put(CLIENTE,TABLA_CLIENTE);
										dictionary.put(CLIENTE+1,"IdSucursal=" + uri.getLastPathSegment()); 
										break;
									
			case FACTURA:				dictionary.put(FACTURA,TABLA_FACTURA); 
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_FACTURA.toString());
										break;
									
			case FACTURA_ID:        	if( uri.toString().indexOf("byid") != -1){
											dictionary.put(FACTURA, TABLA_FACTURA);
											dictionary.put(FACTURA+1,"Id=" + uri.getLastPathSegment());
										} else {
											dictionary.put(FACTURA, TABLA_FACTURA);
											dictionary.put(FACTURA+1,"objSucursalID=" + uri.getLastPathSegment());
										}										  
										break; 
									
			case PROMOCIONCOBRO: 		dictionary.put(PROMOCIONCOBRO, TABLA_PROMOCIONCOBRO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PROMOCIONCOBRO.toString());
										break;     
									
			case PROMOCIONCOBRO_ID:		dictionary.put(PROMOCIONCOBRO,TABLA_PROMOCIONCOBRO);
										dictionary.put(PROMOCIONCOBRO+1,"objFacturaID=" + uri.getLastPathSegment()); 
										break;
									
			case MONTOPROVEEDOR:		dictionary.put(MONTOPROVEEDOR, TABLA_MONTOPROVEEDOR); 
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_MONTOPROVEEDOR.toString());
										break;
									
			case MONTOPROVEEDOR_ID: 	dictionary.put(MONTOPROVEEDOR, TABLA_MONTOPROVEEDOR); 
										dictionary.put(MONTOPROVEEDOR+1,"objFacturaID=" + uri.getLastPathSegment()); 
										break;
									
			case CCNOTACREDITO:			
										dictionary.put(CCNOTACREDITO, TABLA_CCNOTACREDITO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CCNOTACREDITO.toString());
																				
										break;
									
			case CCNOTACREDITO_ID:  	if( uri.toString().indexOf("byid") != -1){
											dictionary.put(CCNOTACREDITO, TABLA_CCNOTACREDITO);
											dictionary.put(CCNOTACREDITO+1,"Id=" + uri.getLastPathSegment());
										} else {
											dictionary.put(CCNOTACREDITO, TABLA_CCNOTACREDITO);
											dictionary.put(CCNOTACREDITO+1,"objSucursalID=" + uri.getLastPathSegment());
										}										  
										break;
									
			case CCNOTADEBITO:			dictionary.put(CCNOTADEBITO,TABLA_CCNOTADEBITO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CCNOTADEBITO.toString());
										break;
										
			case CCNOTADEBITO_ID:   	if( uri.toString().indexOf("byid") != -1){
											dictionary.put(CCNOTADEBITO, TABLA_CCNOTADEBITO);
											dictionary.put(CCNOTADEBITO+1,"Id=" + uri.getLastPathSegment());
										} else {					
											dictionary.put(CCNOTADEBITO,TABLA_CCNOTADEBITO);
											dictionary.put(CCNOTADEBITO+1,"objSucursalID=" + uri.getLastPathSegment());
										}
										break;
									
			case DESCUENTOPROVEEDOR:	dictionary.put(DESCUENTOPROVEEDOR, TABLA_DESCUENTOPROVEEDOR);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_DESCUENTOPROVEEDOR.toString());
										break;
			case DESCUENTOPROVEEDOR_ID: dictionary.put(DESCUENTOPROVEEDOR, TABLA_DESCUENTOPROVEEDOR);
										dictionary.put(DESCUENTOPROVEEDOR+1,"objSucursalID=" + uri.getLastPathSegment());
										break;  
										
			case PRODUCTO:				dictionary.put(PRODUCTO, TABLA_PRODUCTO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PRODUCTO.toString());
										break;
			case PRODUCTO_ID:           dictionary.put(PRODUCTO, TABLA_PRODUCTO);
										dictionary.put(PRODUCTO+1,"Id=" + uri.getLastPathSegment());
										break;
			case LOTE:				    dictionary.put(LOTE, TABLA_LOTE);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_LOTE.toString());
										break;
			case LOTE_ID: 			    dictionary.put(LOTE, TABLA_LOTE);
										dictionary.put(LOTE+1,"ObjProductoID=" + uri.getLastPathSegment());
										break;
			case CATALOGO:				dictionary.put(CATALOGO, TABLA_CATALOGO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CATALOGO.toString());
										break;
			
			case CATALOGO_ID: 			dictionary.put(CATALOGO, TABLA_CATALOGO);
										dictionary.put(CATALOGO+1,"Id=" + uri.getLastPathSegment());
										break;
										
			case CATALOGO_BY_NAME:      dictionary.put(CATALOGO, TABLA_CATALOGO);
										dictionary.put(CATALOGO_BY_NAME,"NombreCatalogo=" + uri.getLastPathSegment());
										break;
				
			case PROMOCION:				dictionary.put(PROMOCION, TABLA_PROMOCION);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PROMOCION.toString());
										break;

			case PROMOCION_ID: 			dictionary.put(PROMOCION, TABLA_PROMOCION);
										dictionary.put(PROMOCION+1,"Id=" + uri.getLastPathSegment());
										break;	
										
			case USUARIO:				dictionary.put(USUARIO, TABLA_USUARIO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_USUARIO.toString());
										break;

			case USUARIO_ID: 			dictionary.put(USUARIO, TABLA_USUARIO);
										dictionary.put(USUARIO+1,"Id=" + uri.getLastPathSegment());
										break;	 
										
			case PEDIDO:				dictionary.put(PEDIDO, TABLA_PEDIDO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PEDIDO.toString());
										break;

			
			case PEDIDO_ID: 			dictionary.put(PEDIDO, TABLA_PEDIDO);
										dictionary.put(PEDIDO+1,"Id=" + uri.getLastPathSegment());
										break;
										
			case PEDIDODETALLE:			dictionary.put(PEDIDODETALLE, TABLA_PEDIDODETALLE);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PEDIDODETALLE.toString());
										break;
										
			case PEDIDODETALLE_ID: 		dictionary.put(PEDIDODETALLE, TABLA_PEDIDODETALLE);
										dictionary.put(PEDIDODETALLE+1,"objPedidoID=" + uri.getLastPathSegment());
										break; 
										
										
			case PEDIDOPROMOCION:		dictionary.put(PEDIDOPROMOCION, TABLA_PEDIDOPROMOCION);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PEDIDOPROMOCION.toString());
										break;
			
			case PEDIDOPROMOCION_ID:    dictionary.put(PEDIDOPROMOCION, TABLA_PEDIDOPROMOCION);
										dictionary.put(PEDIDOPROMOCION+1,"objPedidoID=" + uri.getLastPathSegment());
										break; 
										
			case PEDIDOPROMOCIONDETALLE:dictionary.put(PEDIDOPROMOCIONDETALLE, TABLA_PEDIDOPROMOCIONDETALLE);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_PEDIDOPROMOCIONDETALLE.toString());
										break;

			case PEDIDOPROMOCIONDETALLE_ID:dictionary.put(PEDIDOPROMOCIONDETALLE, TABLA_PEDIDOPROMOCIONDETALLE);
										dictionary.put(PEDIDOPROMOCION+1,"objPedidoID=" + uri.getLastPathSegment());
										break;
													
			case RECIBO: 				dictionary.put(RECIBO, TABLA_RECIBO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_RECIBO.toString());
										break;	  							
			case RECIBO_ID: 			dictionary.put(RECIBO, TABLA_RECIBO);
										dictionary.put(RECIBO+1,"id=" + uri.getLastPathSegment());
										break; 
			case RECIBODETALLEFACTURA:  dictionary.put(RECIBODETALLEFACTURA, TABLA_RECIBO_DETALLE_FACTURA);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_RECIBODETALLEFACTURA.toString());
										break; 
			case RECIBODETALLEFACTURA_ID:  dictionary.put(RECIBODETALLEFACTURA, TABLA_RECIBO_DETALLE_FACTURA);			                            
										dictionary.put(RECIBODETALLEFACTURA+1,"objReciboID=" + uri.getLastPathSegment());				
										break; 
			case RECIBODETALLENOTADEBITO:  dictionary.put(RECIBODETALLENOTADEBITO, TABLA_RECIBO_DETALLE_NOTA_DEBITO);
										dictionary.put(CONTENT_URI_LOCALID, CONTENT_URI_RECIBODETALLENOTADEBITO.toString());
										break; 
			case RECIBODETALLENOTADEBITO_ID:  dictionary.put(RECIBODETALLENOTADEBITO, TABLA_RECIBO_DETALLE_NOTA_DEBITO);			                            
										dictionary.put(RECIBODETALLENOTADEBITO+1,"objReciboID=" + uri.getLastPathSegment());				
										break;
			case RECIBODETALLENOTACREDITO:  dictionary.put(RECIBODETALLENOTACREDITO, TABLA_RECIBO_DETALLE_NOTA_CREDITO);
										dictionary.put(CONTENT_URI_LOCALID, CONTENT_URI_RECIBODETALLENOTADEBITO.toString());
										break; 
			case RECIBODETALLENOTACREDITO_ID:  dictionary.put(RECIBODETALLENOTACREDITO, TABLA_RECIBO_DETALLE_NOTA_CREDITO);			                            
										dictionary.put(RECIBODETALLENOTACREDITO+1,"objReciboID=" + uri.getLastPathSegment());				
										break;
			case RECIBODETALLEFORMAPAGO : dictionary.put(RECIBODETALLEFORMAPAGO, TABLA_RECIBO_DETALLE_FORMA_PAGO);
										dictionary.put(CONTENT_URI_LOCALID, CONTENT_URI_RECIBODETALLEFORMAPAGO.toString());
										break;
			case RECIBODETALLEFORMAPAGO_ID : dictionary.put(RECIBODETALLEFORMAPAGO, TABLA_RECIBO_DETALLE_FORMA_PAGO);
										dictionary.put(RECIBODETALLEFORMAPAGO_ID,"objReciboID=" + uri.getLastPathSegment());
										break;
			case CPRODUTO :			    
										dictionary.put(CPRODUTO, TABLA_CPRODUCTO);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CPRODUCTO.toString());
										break;
										
			case CPRODUTO_ID :			dictionary.put(CPRODUTO, TABLA_CPRODUCTO);
										dictionary.put(CPRODUTO +1,"Id="+ uri.getLastPathSegment());
										break;
										
			case CNOTA_CPRODUCTOID : 	dictionary.put(CPRODUTO, TABLA_CNOTA);
									 	dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_CNOTA.toString());
									 	break;
			case DEVOLUCION:				
										dictionary.put(DEVOLUCION, TABLA_DEVOLUCIONES);
										dictionary.put(CONTENT_URI_LOCALID,CONTENT_URI_DEVOLUCION.toString());
										
										break;
			case DEVOLUCIONPRODUCTO:				
										dictionary.put(DEVOLUCIONPRODUCTO, TABLA_DEVOLUCIONPRODUCTO);
										dictionary.put(DEVOLUCIONPRODUCTO_ID,CONTENT_URI_DEVOLUCIONPRODUCTO.toString());
										break;
			case DEVOLUCIONPRODUCTOLOTE:				
										dictionary.put(DEVOLUCIONPRODUCTOLOTE, TABLA_DEVOLUCIONPRODUCTOLOTE);
										dictionary.put(DEVOLUCIONPRODUCTOLOTE_ID,CONTENT_URI_DEVOLUCIONPRODUCTOLOTE.toString());
										break;
		} 
		Iterator it = dictionary.entrySet().iterator();
		while (it.hasNext()) 
			e.add((Map.Entry)it.next()); 
		
		return e;
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		int cont;
		//Si es una consulta a un ID concreto construimos el WHERE 
		List<Map.Entry> coll=FromWhere(uri);
		String TablaName=coll.get(0).getValue().toString();
		String where = selection + coll.get(1).getValue().toString();		
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		
		cont = db.delete(TablaName, where, selectionArgs);
		
		return cont;
	}
	 	 
	@Override
	public String getType(Uri uri) 
	{
	 
		switch(uriMatcher.match(uri))
		{		 
			case CLIENTE:
			case FACTURA:
			case PROMOCIONCOBRO:
			case MONTOPROVEEDOR:
			case CCNOTACREDITO:
			case CCNOTADEBITO:
			case DESCUENTOPROVEEDOR:
			case PRODUCTO:
			case LOTE:
			case CATALOGO:
			case PROMOCION:
			case USUARIO:
			case PEDIDO:
			case PEDIDODETALLE: 
			case PEDIDOPROMOCION:
			case PEDIDOPROMOCIONDETALLE: 
			case CPRODUTO :
			case CNOTA: 
			case DEVOLUCION : 
			case DEVOLUCIONPRODUCTOLOTE : 
			case DEVOLUCIONPRODUCTO :
			return "vnd.android.cursor.dir/vnd"+AUTHORITY;
			case CLIENTE_ID:
			case FACTURA_ID:
			case PROMOCIONCOBRO_ID:
			case MONTOPROVEEDOR_ID:
			case CCNOTACREDITO_ID:
			case CCNOTADEBITO_ID:
			case DESCUENTOPROVEEDOR_ID:
			case PRODUCTO_ID:
			case LOTE_ID:
			case CATALOGO_ID:
			case PROMOCION_ID:
			case USUARIO_ID:
			case PEDIDO_ID:
			case PEDIDODETALLE_ID:
			case PEDIDOPROMOCION_ID:
			case PEDIDOPROMOCIONDETALLE_ID:
			case CPRODUTO_ID: 
			case CNOTA_CPRODUCTOID:
			case DEVOLUCION_ID:
			case DEVOLUCIONPRODUCTO_ID:
			case DEVOLUCIONPRODUCTOLOTE_ID:
				 return "vnd.android.cursor.item/vnd"+AUTHORITY; 									
		    default:throw new IllegalArgumentException("Invalid Uri: "+ uri);
		}  
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) { 
		return null;
	}
	
	private  static EncabezadoSolicitud registrarEncabezadoSolicitud(EncabezadoSolicitud solicitud, Context cnt) throws Exception {
		SQLiteDatabase bdd = null;
		try 
		{
			ContentValues values;
			bdd = Helper.getDatabase(cnt);
			bdd.beginTransaction();
			values = new ContentValues();
			
			values.put(NMConfig.EncabezadoSolicitud.OBJ_RECIBO_ID, solicitud.getObjReciboID() );
			values.put(NMConfig.EncabezadoSolicitud.CODIGO_ESTADO, solicitud.getCodigoEstado() );
			values.put(NMConfig.EncabezadoSolicitud.DESCRIPCION_ESTADO, solicitud.getDescripcionEstado() );
			values.put(NMConfig.EncabezadoSolicitud.FECHA_SOLICITUD, solicitud.getFechaSolicitud() );
		
			if(solicitud != null && solicitud.getId() == 0) {
				solicitud.setId(bdd.insert(TABLA_ENCABEZADO_SOLICITUD, null, values));
			} else {
				bdd.update(TABLA_ENCABEZADO_SOLICITUD, values, null, null);
			}
			
			for(SolicitudDescuento detalle : solicitud.getDetalles()) {
				detalle.setEncabezadoSolicitudId(solicitud.getId());
				registrarSolicitudDescuento(detalle, NMApp.getContext(),bdd); 		
			}
				
			
			bdd.setTransactionSuccessful();

			if (bdd != null || (bdd.isOpen())) {
				bdd.endTransaction();
				bdd.close();
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if ( bdd != null && bdd.isOpen() ) {
				bdd.endTransaction();
				bdd.close();
			}
		}
		return solicitud;		
	}
	
	private static SolicitudDescuento registrarSolicitudDescuento(SolicitudDescuento solicitud, Context cnt,SQLiteDatabase bdd) throws Exception {
		
		try 
		{
			ContentValues values;
			values = new ContentValues();
			
			values.put(NMConfig.EncabezadoSolicitud.SolicitudDescuento.OBJ_ENCABEZADO_SOLICITUD_ID, solicitud.getEncabezadoSolicitudId() );
			values.put(NMConfig.EncabezadoSolicitud.SolicitudDescuento.OBJ_FACTURA_ID, solicitud.getFacturaId() );
			values.put(NMConfig.EncabezadoSolicitud.SolicitudDescuento.PORCENTAJE, solicitud.getPorcentaje() );
			values.put(NMConfig.EncabezadoSolicitud.SolicitudDescuento.JUSTIFICACION, solicitud.getJustificacion() );
			values.put(NMConfig.EncabezadoSolicitud.SolicitudDescuento.FECHA, solicitud.getFecha() );
			
			String where = NMConfig.EncabezadoSolicitud.SolicitudDescuento.OBJ_ENCABEZADO_SOLICITUD_ID + " = " + String.valueOf(solicitud.getEncabezadoSolicitudId());
			where += " AND  " + NMConfig.EncabezadoSolicitud.SolicitudDescuento.OBJ_FACTURA_ID + " = " + String.valueOf(solicitud.getFacturaId());
			
			bdd.delete(TABLA_SOLICITUD_DESCUENTO, where ,null);
			solicitud.setId(bdd.insert(TABLA_SOLICITUD_DESCUENTO, null, values));
			
		} catch (Exception e) {
			throw new Exception(e);
		} 
		return solicitud;		
	}
	
	public static EncabezadoSolicitud registrarSolicitudesDescuento(EncabezadoSolicitud solicitud, Context cnt) throws Exception {
			return	registrarEncabezadoSolicitud(solicitud,cnt); 
	}
	 
	@SuppressWarnings("null")
	public static ReciboColector registrarRecibo(ReciboColector recibo, Context cnt, ArrayList<Factura> facturasToUpdate, ArrayList<CCNotaDebito> notasDebitoToUpdate, ArrayList<CCNotaCredito> notasCreditoToUpdate) throws Exception 
	{
		SQLiteDatabase bdd = null;
		Integer recibomax=0; 
		boolean newitem=false;
		try 
		{	
			bdd = Helper.getDatabase(cnt);
			
			ContentValues values;
			ContentValues factura;
			ContentValues notaDebito;
			ContentValues notaCredito;
			ContentValues formasPago;
			
			long idrecibo=0;
			
			bdd.beginTransaction();
			
			values = new ContentValues();
			
			values.put(NMConfig.Recibo.NUMERO, recibo.getNumero());
			values.put(NMConfig.Recibo.FECHA, recibo.getFecha());
			values.put(NMConfig.Recibo.NOTAS, recibo.getNotas());
			values.put(NMConfig.Recibo.TOTAL_RECIBO, recibo.getTotalRecibo());		
			values.put(NMConfig.Recibo.TOTAL_FACTURAS, recibo.getTotalFacturas());
			values.put(NMConfig.Recibo.TOTAL_NOTAS_DEBITO, recibo.getTotalND());
			values.put(NMConfig.Recibo.TOTAL_NOTAS_CREDITO, recibo.getTotalNC());
			values.put(NMConfig.Recibo.TOTAL_INTERES, recibo.getTotalInteres());
			values.put(NMConfig.Recibo.SUBTOTAL, recibo.getSubTotal());
			values.put(NMConfig.Recibo.TOTAL_DESCUENTO, recibo.getTotalDesc());
			values.put(NMConfig.Recibo.TOTAL_RETENIDO, recibo.getTotalRetenido());
			values.put(NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES, recibo.getTotalOtrasDed());
			values.put(NMConfig.Recibo.TOTAL_OTRAS_DEDUCCIONES, recibo.getTotalOtrasDed());
			values.put(NMConfig.Recibo.REFERENCIA, recibo.getReferencia());
			values.put(NMConfig.Recibo.CLIENTE_ID, recibo.getObjClienteID());
			values.put(NMConfig.Recibo.SUCURSAL_ID, recibo.getObjSucursalID());
			values.put(NMConfig.Recibo.NOMBRE_CLIENTE, recibo.getNombreCliente());
			values.put(NMConfig.Recibo.COLECTOR_ID, recibo.getObjColectorID());
			values.put(NMConfig.Recibo.APLICA_DESCUENTO_OCASIONAL, recibo.isAplicaDescOca());
			values.put(NMConfig.Recibo.CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL, recibo.getClaveAutorizaDescOca());
			values.put(NMConfig.Recibo.PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR, recibo.getPorcDescOcaColector());
			values.put(NMConfig.Recibo.ESTADO_ID, recibo.getObjEstadoID());
			values.put(NMConfig.Recibo.CODIGO_ESTADO, recibo.getCodEstado());
			values.put(NMConfig.Recibo.DESCRICION_ESTADO, recibo.getDescEstado());
			values.put(NMConfig.Recibo.TOTAL_DESCUENTO_OCASIONAL, recibo.getTotalDescOca());
			values.put(NMConfig.Recibo.TOTAL_DESCUENTO_PROMOCION, recibo.getTotalDescPromo());
			values.put(NMConfig.Recibo.TOTAL_DESCUENTO_PRONTO_PAGO, recibo.getTotalDescPP());
			values.put(NMConfig.Recibo.TOTAL_IMPUESTO_PROPORCIONAL, recibo.getTotalImpuestoProporcional());
			values.put(NMConfig.Recibo.TOTAL_IMPUESTO_EXONERADO, recibo.getTotalImpuestoExonerado());
			values.put(NMConfig.Recibo.EXENTO, recibo.isExento());
			values.put(NMConfig.Recibo.AUTORIZA_DGI, recibo.getAutorizacionDGI());
			
			Cursor _c=bdd.rawQuery("select id from Recibo where referencia="+recibo.getReferencia(), null);
			bdd = Helper.getDatabase(cnt);
			if(_c.moveToNext())
				idrecibo=_c.getInt(0);
		  
			if(recibo.getNumero()!=0)
				values.put(NMConfig.Recibo.ID, recibo.getId());	
					 //174
					 //1741756
			 Integer prefijo=Ventas.getPrefijoIds(NMApp.getContext());
	         recibomax=Ventas.getMaxReciboId(NMApp.getContext()); 
	          
			 //Generar Id del recibo
	       if (recibo.getReferencia() == 0) 
	       {            	 
	    	   newitem=true;
	    	   //Salvando el tipo de recibo 	    	   
               if (recibomax == null) 
               	recibomax = Integer.valueOf(1);
               else{ 
            	   recibomax =recibomax+1; 
               }
               	
               String strIdMovil = prefijo.intValue() + "" + recibomax.intValue();
               int idMovil = Integer.parseInt(strIdMovil);          
	           
	           recibo.setReferencia(idMovil); 
	           recibo.setCodEstado("REGISTRADO");
	           recibo.setDescEstado("Registrado"); 
	           values.put(NMConfig.Recibo.REFERENCIA, recibo.getReferencia());
	           values.put(NMConfig.Recibo.ESTADO_ID, recibo.getObjEstadoID());
	           values.put(NMConfig.Recibo.CODIGO_ESTADO, recibo.getCodEstado());
	   		   values.put(NMConfig.Recibo.DESCRICION_ESTADO, recibo.getDescEstado()); 	   		   
	   		   recibo.setId(bdd.insert(TABLA_RECIBO, null, values));
	        }else 
	        {
	        					
	        	newitem=false; 
	        	//BORRAR LOS DETALLES DE LAS FACTURAS DEL RECIBO
				bdd.delete(TABLA_RECIBO, NMConfig.Recibo.REFERENCIA+"="+String.valueOf(recibo.getReferencia()) ,null);
				// ACTUALIZANDO RECIBO 
				recibo.setId(bdd.insert(TABLA_RECIBO, null, values));
			}	
			 
	       
			String where = NMConfig.Recibo.DetalleFactura.RECIBO_ID+"="+String.valueOf(recibo.getId());
			
			//BORRAR LOS DETALLES DE LAS FACTURAS DEL RECIBO
			bdd.delete(TABLA_RECIBO_DETALLE_FACTURA, where ,null); 
			
			//BORRAR LOS DETALLES DE NOTAS DE DEBITO DEL RECIBO
			bdd.delete(TABLA_RECIBO_DETALLE_NOTA_DEBITO, where ,null);
			
			//BORRAR LOS DETALLES DE NOTAS DE CREDITO DEL RECIBO
			bdd.delete(TABLA_RECIBO_DETALLE_NOTA_CREDITO, where ,null);
			
			//BORRAR LOS DETALLES DE NOTAS DE CREDITO DEL RECIBO
			bdd.delete(TABLA_RECIBO_DETALLE_FORMA_PAGO, where ,null);
			
			//INSERTAR EL DETALLE DE FACTURAS DEL RECIBO
			for(ReciboDetFactura dt : recibo.getFacturasRecibo()){
				
				factura = new ContentValues();			
				factura.put(NMConfig.Recibo.DetalleFactura.RECIBO_ID, recibo.getId());
				factura.put(NMConfig.Recibo.DetalleFactura.FACTURA_ID, dt.getObjFacturaID());
				factura.put(NMConfig.Recibo.DetalleFactura.ESABONO, (dt.isEsAbono() ? 255 : 0 ));
				factura.put(NMConfig.Recibo.DetalleFactura.FECHA, dt.getFecha());
				factura.put(NMConfig.Recibo.DetalleFactura.FECHA_VENCE, dt.getFechaVence());
				factura.put(NMConfig.Recibo.DetalleFactura.FECHA_APLICA_DESCUENTO_PRONTO_PAGO, dt.getFechaAplicaDescPP());
				factura.put(NMConfig.Recibo.DetalleFactura.IMPUESTO, dt.getImpuesto());
				factura.put(NMConfig.Recibo.DetalleFactura.INTERES_MORATORIO, dt.getInteresMoratorio());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO, dt.getMonto());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_ESPECIFICO, dt.getMontoDescEspecifico());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_OCASIONAL, dt.getMontoDescOcasional());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_DESCUENTO_PROMOCION, dt.getMontoDescPromocion());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO, dt.getMontoImpuesto());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_IMPUESTO_EXONERADO, dt.getMontoImpuestoExento());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_INTERES, dt.getMontoInteres());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_NETO, dt.getMontoNeto());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_OTRAS_DEDUCCIONES, dt.getMontoOtrasDeducciones());
				factura.put(NMConfig.Recibo.DetalleFactura.MONTO_RETENCION, dt.getMontoRetencion());
				factura.put(NMConfig.Recibo.DetalleFactura.NUMERO, dt.getNumero());
				factura.put(NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_OCASIONAL, dt.getPorcDescOcasional());
				factura.put(NMConfig.Recibo.DetalleFactura.PORCENTAJE_DESCUENTO_PROMOCION, dt.getPorcDescPromo());
				factura.put(NMConfig.Recibo.DetalleFactura.SALDO_FACTURA, dt.getSaldofactura());
				factura.put(NMConfig.Recibo.DetalleFactura.SALDO_TOTAL, dt.getSaldoTotal());
				factura.put(NMConfig.Recibo.DetalleFactura.SUB_TOTAL, dt.getSubTotal());
				factura.put(NMConfig.Recibo.DetalleFactura.TOTAL_FACTURA, dt.getTotalfactura());
				factura.put(NMConfig.Recibo.DetalleFactura.TOTAL_FACTURA, dt.getTotalfactura());
				factura.put(NMConfig.Recibo.DetalleFactura.TOTAL_FACTURA_ORIGEN, dt.getTotalFacturaOrigen());
				bdd.insert(TABLA_RECIBO_DETALLE_FACTURA, null, factura);
			}
			
			for(ReciboDetND nd: recibo.getNotasDebitoRecibo()){
				
				notaDebito = new ContentValues();			
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.NOTADEBITO_ID, nd.getObjNotaDebitoID() );
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.RECIBO_ID, recibo.getId() );
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.MONTO_INTERES, nd.getMontoInteres() );
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.ESABONO, (nd.isEsAbono() ? 255 : 0 ) );
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.MONTO_PAGAR, nd.getMontoPagar());
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.NUMERO, nd.getNumero());
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.FECHA, nd.getFecha() );
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.FECHA_VENCE, nd.getFechaVence());
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.MONTO_ND, nd.getMontoND());
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.SALDO_ND, nd.getSaldoND());
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.INTERES_MORATORIO, nd.getInteresMoratorio() );
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.SALDO_TOTAL, nd.getSaldo() );
				notaDebito.put(NMConfig.Recibo.DetalleNotaDebito.MONTO_NETO, nd.getMontoNeto() );
				
				bdd.insert(TABLA_RECIBO_DETALLE_NOTA_DEBITO, null, notaDebito);
			}
			
			for(ReciboDetNC nd: recibo.getNotasCreditoRecibo()){
				
				notaCredito = new ContentValues();			
				notaCredito.put(NMConfig.Recibo.DetalleNotaCredito.NOTACREDITO_ID, nd.getObjNotaCreditoID() );
				notaCredito.put(NMConfig.Recibo.DetalleNotaCredito.RECIBO_ID, recibo.getId() );
				notaCredito.put(NMConfig.Recibo.DetalleNotaCredito.FECHA, nd.getFecha() );
				notaCredito.put(NMConfig.Recibo.DetalleNotaCredito.FECHA_VENCE, nd.getFechaVence() );
				notaCredito.put(NMConfig.Recibo.DetalleNotaCredito.MONTO, nd.getMonto() );
				notaCredito.put(NMConfig.Recibo.DetalleNotaCredito.NUMERO, nd.getNumero());
				
				bdd.insert(TABLA_RECIBO_DETALLE_NOTA_CREDITO, null, notaCredito);
			}
			
			for(ReciboDetFormaPago fp: recibo.getFormasPagoRecibo()){
				
				formasPago = new ContentValues();			
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.RECIBO_ID, recibo.getId());
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.FORMA_PAGO_ID, fp.getObjFormaPagoID() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.COD_FORMA_PAGO, fp.getCodFormaPago() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.DESC_FORMA_PAGO, fp.getDescFormaPago() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.NUMERO, fp.getNumero() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.MONEDA_ID, fp.getObjMonedaID() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.COD_MONEDA, fp.getCodMoneda() );			
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.DESC_MONEDA, fp.getDescMoneda() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.MONTO, fp.getMonto());
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.MONTO_NACIONAL, fp.getMontoNacional() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.ENTIDAD_ID, fp.getObjEntidadID() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.COD_ENTIDAD, fp.getCodEntidad() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.DESC_ENTIDAD, fp.getDescEntidad() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.FECHA, fp.getFecha() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.SERIE_BILLETES, fp.getSerieBilletes() );
				formasPago.put(NMConfig.Recibo.DetalleFormaPago.TASA_CAMBIO, fp.getTasaCambio() );
				
				bdd.insert(TABLA_RECIBO_DETALLE_FORMA_PAGO, null, formasPago);
			}
			
			for(int f = 0; ( facturasToUpdate != null && f < facturasToUpdate.size() ); f++ ){
				StringBuilder sQuery = new StringBuilder();
				sQuery.append("UPDATE Factura ");
				sQuery.append(String.format("	SET Estado     = '%s', ", facturasToUpdate.get(f).getEstado()));
				sQuery.append(String.format("	    CodEstado  = '%s', ", facturasToUpdate.get(f).getCodEstado()));
				sQuery.append(String.format("	    Abonado    =  %s , ", facturasToUpdate.get(f).getAbonado()));
				sQuery.append(String.format("	    Descontado =  %s , ", facturasToUpdate.get(f).getDescontado()));
				sQuery.append(String.format("	    Retenido   =  %s , ", facturasToUpdate.get(f).getRetenido()));
				sQuery.append(String.format("	    Otro       =  %s , ", facturasToUpdate.get(f).getOtro()));
				sQuery.append(String.format("	    Saldo      =  %s  ", facturasToUpdate.get(f).getSaldo()));
				sQuery.append(String.format("	WHERE Id       =  %d  ", facturasToUpdate.get(f).getId()));			
				bdd.execSQL(sQuery.toString());			
			}
			
			for(int f = 0; ( notasDebitoToUpdate != null && f < notasDebitoToUpdate.size() ); f++ ){
				StringBuilder sQuery = new StringBuilder();
				sQuery.append("UPDATE CCNotaDebito ");
				sQuery.append(String.format("	SET Estado     = '%s', ", notasDebitoToUpdate.get(f).getEstado()));
				sQuery.append(String.format("	    CodEstado  = '%s', ", notasDebitoToUpdate.get(f).getCodEstado()));
				sQuery.append(String.format("	    MontoAbonado    =  %s , ", notasDebitoToUpdate.get(f).getMontoAbonado()));				
				sQuery.append(String.format("	    Saldo      =  %s  ", notasDebitoToUpdate.get(f).getSaldo()));
				sQuery.append(String.format("	WHERE Id       =  %d  ", notasDebitoToUpdate.get(f).getId()));			
				bdd.execSQL(sQuery.toString());			
			}
			
			for(int f = 0; ( notasCreditoToUpdate != null && f < notasCreditoToUpdate.size() ); f++ ){
				StringBuilder sQuery = new StringBuilder();
				sQuery.append("UPDATE CCNotaCredito ");
				sQuery.append(String.format("	SET Estado     = '%s', ", notasCreditoToUpdate.get(f).getEstado()));
				sQuery.append(String.format("	    CodEstado  = '%s', ", notasCreditoToUpdate.get(f).getCodEstado()));
				sQuery.append(String.format("	    Monto    =  %s  ", notasCreditoToUpdate.get(f).getMonto()));
				sQuery.append(String.format("	WHERE Id       =  %d  ", notasCreditoToUpdate.get(f).getId()));			
				bdd.execSQL(sQuery.toString());			
			}
			
			bdd.setTransactionSuccessful();

			if (bdd != null || (bdd.isOpen())) {
				bdd.endTransaction();
				bdd.close();
			}
			//Actualizando el id m�ximo de recibo generado 				
			if(newitem)
				ModelConfiguracion.setMaxReciboId(NMApp.getContext(),recibomax);	
			
		} catch (Exception e) 
		{
			//Actualizando el id m�ximo de recibo generado 					
			//ModelConfiguracion.setMaxReciboId(NMApp.getContext(),recibomax2);	
			if (bdd != null || (bdd.isOpen())) 
			{
				bdd.endTransaction();
				bdd.close();
			}
			throw new Exception(e);
		}
 
	 			
		return recibo;
	}
		
	public static Pedido RegistrarPedido(Pedido pedido,Context cnt)throws Exception{
		
		SQLiteDatabase bdd = null;
		try 
		{
			
		
			NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);	
			long idpedido=-1;
			ContentValues values;		
			bdd = d.getWritableDatabase();		
			bdd.beginTransaction();
			
			values = new ContentValues();		 
			
			Cursor _c=bdd.rawQuery("select Id from Pedido where NumeroMovil="+pedido.getNumeroMovil(), null);
			if(_c.moveToNext())
				idpedido=_c.getInt(0);
		 
			if(idpedido!=0)
			{
				 
				bdd.delete(TABLA_PEDIDODETALLE,NMConfig.Pedido.DetallePedido.objPedidoID+ "="+String.valueOf(idpedido),null);
				bdd.delete(TABLA_PEDIDOPROMOCION,NMConfig.Pedido.DetallePedido.objPedidoID+ "="+String.valueOf(idpedido),null);
				bdd.delete(TABLA_PEDIDOPROMOCIONDETALLE,NMConfig.Pedido.DetallePedido.objPedidoID+ "="+String.valueOf(idpedido),null);
				bdd.delete(TABLA_PEDIDO, NMConfig.Pedido.Id+"="+String.valueOf(idpedido),null);
			}
			if(pedido.getNumeroCentral()!=0)
				values.put(NMConfig.Pedido.Id, pedido.getId());	
			
			values.put(NMConfig.Pedido.NumeroMovil, pedido.getNumeroMovil());		
			values.put(NMConfig.Pedido.NumeroCentral, pedido.getNumeroCentral());		
			values.put(NMConfig.Pedido.Tipo, pedido.getTipo());		
			values.put(NMConfig.Pedido.Fecha, pedido.getFecha());		
			values.put(NMConfig.Pedido.objClienteID, pedido.getObjClienteID());
			values.put(NMConfig.Pedido.NombreCliente, pedido.getNombreCliente());		
			values.put(NMConfig.Pedido.objSucursalID, pedido.getObjSucursalID());		
			values.put(NMConfig.Pedido.NombreSucursal, pedido.getNombreSucursal());
			values.put(NMConfig.Pedido.objTipoPrecioVentaID, pedido.getObjTipoPrecioVentaID());
			values.put(NMConfig.Pedido.CodTipoPrecio, pedido.getCodTipoPrecio());
			values.put(NMConfig.Pedido.DescTipoPrecio, pedido.getDescTipoPrecio());
			//values.put(NMConfig.Pedido.objVendedorID, pedido.getObjVendedorID());
			values.put(NMConfig.Pedido.objVendedorID, 0);
			values.put(NMConfig.Pedido.BonificacionEspecial, pedido.getBonificacionEspecial());
			values.put(NMConfig.Pedido.BonificacionSolicitada, pedido.getBonificacionSolicitada());
			values.put(NMConfig.Pedido.PrecioEspecial, pedido.getPrecioEspecial());
			values.put(NMConfig.Pedido.PrecioSolicitado, pedido.getPrecioSolicitado());
			values.put(NMConfig.Pedido.PedidoCondicionado, pedido.getPedidoCondicionado());
			values.put(NMConfig.Pedido.Condicion, pedido.getCondicion());
			values.put(NMConfig.Pedido.Subtotal, pedido.getSubtotal());
			values.put(NMConfig.Pedido.Descuento, pedido.getDescuento());
			values.put(NMConfig.Pedido.Impuesto, pedido.getImpuesto());
			values.put(NMConfig.Pedido.Total, pedido.getTotal());
			values.put(NMConfig.Pedido.objEstadoID, pedido.getObjEstadoID());
			values.put(NMConfig.Pedido.CodEstado, pedido.getCodEstado());
			values.put(NMConfig.Pedido.DescEstado, pedido.getDescEstado());
			values.put(NMConfig.Pedido.objCausaEstadoID, pedido.getObjCausaEstadoID());
			values.put(NMConfig.Pedido.CodCausaEstado, pedido.getCodCausaEstado());		
			values.put(NMConfig.Pedido.DescCausaEstado, pedido.getDescCausaEstado());
			values.put(NMConfig.Pedido.NombreVendedor, pedido.getNombreVendedor());  
			values.put(NMConfig.Pedido.Nota, pedido.getNota());		
			values.put(NMConfig.Pedido.Exento, pedido.isExento());
			values.put(NMConfig.Pedido.AutorizacionDGI, pedido.getAutorizacionDGI()); 
			
			idpedido=bdd.insert(TABLA_PEDIDO, null, values);	
			pedido.setId(idpedido);
			DetallePedido[] detp=pedido.getDetalles();
			if(detp!=null && detp.length!=0)
			for(DetallePedido dp:detp)
			{
				values = new ContentValues();		 
				values.put(NMConfig.Pedido.DetallePedido.objPedidoID,pedido.getId());
				values.put(NMConfig.Pedido.DetallePedido.objProductoID, dp.getObjProductoID());
				values.put(NMConfig.Pedido.DetallePedido.codProducto, dp.getCodProducto());
				values.put(NMConfig.Pedido.DetallePedido.nombreProducto, dp.getNombreProducto());
				values.put(NMConfig.Pedido.DetallePedido.cantidadOrdenada, dp.getCantidadOrdenada());
				values.put(NMConfig.Pedido.DetallePedido.cantidadBonificada, dp.getCantidadBonificada());
				values.put(NMConfig.Pedido.DetallePedido.objBonificacionID, dp.getObjBonificacionID());
				values.put(NMConfig.Pedido.DetallePedido.bonifEditada, dp.getBonifEditada());
				values.put(NMConfig.Pedido.DetallePedido.cantidadBonificadaEditada, dp.getCantidadBonificadaEditada());
				values.put(NMConfig.Pedido.DetallePedido.precio, dp.getPrecio());
				values.put(NMConfig.Pedido.DetallePedido.montoPrecioEditado, dp.getMontoPrecioEditado());
				values.put(NMConfig.Pedido.DetallePedido.precioEditado, dp.getPrecioEditado());
				values.put(NMConfig.Pedido.DetallePedido.subtotal, dp.getSubtotal());
				values.put(NMConfig.Pedido.DetallePedido.descuento, dp.getDescuento());
				values.put(NMConfig.Pedido.DetallePedido.porcImpuesto, dp.getPorcImpuesto());
				values.put(NMConfig.Pedido.DetallePedido.impuesto, dp.getImpuesto());
				values.put(NMConfig.Pedido.DetallePedido.total, dp.getTotal());
				values.put(NMConfig.Pedido.DetallePedido.cantidadDespachada, dp.getCantidadDespachada());
				values.put(NMConfig.Pedido.DetallePedido.cantidadADespachar, dp.getCantidadADespachar());
				values.put(NMConfig.Pedido.DetallePedido.cantidadPromocion, dp.getCantidadPromocion());
				
				bdd.insert(TABLA_PEDIDODETALLE, null, values);	
			}
			PedidoPromocion[] pedp=pedido.getPromocionesAplicadas();
			if(pedp!=null && pedp.length!=0)
			for(PedidoPromocion pp:pedp)
			{
				values = new ContentValues();		
				values.put(NMConfig.Pedido.PedidoPromocion.objPromocionID, pp.getObjPromocionID());
				values.put(NMConfig.Pedido.PedidoPromocion.objPedidoID,pedido.getId());
				values.put(NMConfig.Pedido.PedidoPromocion.descuento, pp.getDescuento());
				values.put(NMConfig.Pedido.PedidoPromocion.codigoPromocion,pp.getCodigoPromocion());
				values.put(NMConfig.Pedido.PedidoPromocion.nombrePromocion,pp.getNombrePromocion());			
				bdd.insert(TABLA_PEDIDOPROMOCION, null, values);
				PedidoPromocionDetalle[] ppromd=pp.getDetalles();
				if(ppromd!=null && ppromd.length!=0)
				for(PedidoPromocionDetalle ppd:ppromd)
				{
					values = new ContentValues();		
					values.put(NMConfig.Pedido.PedidoPromocion.objPromocionID, pp.getObjPromocionID());
					values.put(NMConfig.Pedido.PedidoPromocion.objPedidoID,pedido.getId());
					values.put(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.objProductoID, ppd.getObjProductoID());
					values.put(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.nombreProducto,ppd.getNombreProducto());
					values.put(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.cantidadEntregada,ppd.getCantidadEntregada());
					values.put(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.descuento,ppd.getDescuento());
					bdd.insert(TABLA_PEDIDOPROMOCIONDETALLE, null, values);
				}
			}
			
			
			bdd.setTransactionSuccessful();
			if (bdd != null || (bdd.isOpen())) {
				bdd.endTransaction();
				bdd.close();
			}		
		return pedido;
		} catch (Exception e) 
		{
			if (bdd != null || (bdd != null  && bdd.isOpen())) {
				bdd.endTransaction();
				bdd.close();
			}
			throw new Exception(e);
		}
		
	}
	
	public static void RegistrarDetalleProducto(CProducto detalle,Context cnt) throws Exception
	{
		SQLiteDatabase bdd = null;
		try 
		{
			boolean new_register = true;
			/*Buscamos el producto del LocalHOst*/
			CProducto local =  ModelCProducto.getFichaProductoFromLocalHost(cnt.getContentResolver(), detalle.getId());
			if(local!=null){
				new_register = false;
			}
			
			NM_SQLiteHelper helper = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);	
			bdd =helper.getWritableDatabase();		
			bdd.beginTransaction();
			ContentValues values= new ContentValues();
			//llenamos los valores
			values.put(NMConfig.CProducto.ID, detalle.getId());		
			values.put(NMConfig.CProducto.NOMBRE, detalle.getNombre());		
			values.put(NMConfig.CProducto.ACCION_FARMACOLOGICA, detalle.getAccionFarmacologica());		
			values.put(NMConfig.CProducto.CATEGORIA, detalle.getCategoria());	
			values.put(NMConfig.CProducto.CODIGO, detalle.getCodigo());	
			values.put(NMConfig.CProducto.ESPECIALIDADES, detalle.getEspecialidades());	
			values.put(NMConfig.CProducto.FORMA_FARMACEUTICA, detalle.getFormaFarmaceutica());	
			values.put(NMConfig.CProducto.NOMBRE_COMERCIAL, detalle.getNombreComercial());
			values.put(NMConfig.CProducto.NOMBRE_GENERICO, detalle.getNombreGenerico());
			values.put(NMConfig.CProducto.PROVEEDOR, detalle.getProveedor());
			values.put(NMConfig.CProducto.REGISTRO, detalle.getRegistro());
			values.put(NMConfig.CProducto.TIPO_PRODUCTO, detalle.getTipoProducto());
			
			if(new_register){
				bdd.insert(TABLA_CPRODUCTO, null, values);
			}
			else{
				bdd.update(TABLA_CPRODUCTO, values, NMConfig.CProducto.ID+"="+String.valueOf(detalle.getId()) , null);	
			}
			
			String where = NMConfig.CProducto.CNota.PRODUCTOID+"="+String.valueOf(detalle.getId());
			
			//BORRAR LAS NOTAS DEL PRODUCTO
			bdd.delete(TABLA_CNOTA, where ,null); 

			if(detalle.getNotas()!=null){
				
				CNota[] notas =detalle.getNotas();
				for(int i=0;i<notas.length;i++)
				{
					values = new ContentValues();
					values.put(NMConfig.CProducto.CNota.PRODUCTOID, detalle.getId());
					values.put(NMConfig.CProducto.CNota.CONCEPTO, notas[i].getConcepto());
					values.put(NMConfig.CProducto.CNota.ELABORADO_POR, notas[i].getElaboradaPor());
					values.put(NMConfig.CProducto.CNota.FECHA, notas[i].getFecha());
					values.put(NMConfig.CProducto.CNota.TEXTONOTA, notas[i].getTextoNota());
					bdd.insert(TABLA_CNOTA, null, values);
				}
			}
			bdd.setTransactionSuccessful();
			if (bdd != null || (bdd.isOpen())) {
				bdd.endTransaction();
				bdd.close();
			}	
		}
		catch (Exception e) 
		{
			if (bdd != null || (bdd != null  && bdd.isOpen())) {
				bdd.endTransaction();
				bdd.close();
			}
			throw new Exception(e);
		}
	}
	
	public static void RegistrarDevolucion(Devolucion devolucion,Context cnt)throws Exception
	{
		long devolucionid = 0;
		long devolucionproductoid = 0;
		SQLiteDatabase sdb = null;
		Integer devolucionmax=0;
		try 
		{
			NM_SQLiteHelper d = new NM_SQLiteHelper(cnt, DATABASE_NAME, null, BD_VERSION);	
			sdb = d.getWritableDatabase();	
			//sdb = Helper.getDatabase(cnt);
			ContentValues devolucion_value = new ContentValues();
			ContentValues productos_devueltos_value = new ContentValues();
			ContentValues productos_devueltoslote_value = new ContentValues(); 
			 	
			long iddevolucion=-1;  
			sdb.beginTransaction();
			 	 
			 Integer prefijo=Ventas.getPrefijoIds(NMApp.getContext());
	         devolucionmax=devolucion.isDeVencido()?Ventas.getMaxDevolucionVId(NMApp.getContext()):Ventas.getMaxDevolucionNVId(NMApp.getContext()); //Ventas.getMaxDevolucionVId(NMApp.getContext());
	         
	         if (devolucionmax == null)  devolucionmax = Integer.valueOf(1);
	         else
	         {
	            	 //Integer rmax=NumberUtil.getNumeroMaxRecibo(devolucionmax, prefijo);
	        	 devolucionmax = devolucionmax + 1; //devolucionmax =rmax+1; 
	         }	               	
	         
	         String strIdMovil = prefijo.intValue() + "" + devolucionmax.intValue();
	         int idMovil = Integer.parseInt(strIdMovil); 
	         
//			Cursor _c=sdb.rawQuery("select Id from Devolucion where referencia="+devolucion.getReferencia(), null);
//			if(_c.moveToNext())
//				iddevolucion=_c.getInt(0);
			if( devolucion.getId()!=0)
			{	
				
				sdb.delete(TABLA_DEVOLUCIONPRODUCTOLOTE,NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.devolucionID+ "="+String.valueOf(devolucion.getId()),null);
				sdb.delete(TABLA_DEVOLUCIONPRODUCTO,NMConfig.Devolucion.DevolucionProducto.devolucionID+ "="+String.valueOf(devolucion.getId()),null);
				sdb.delete(TABLA_DEVOLUCIONES,NMConfig.Devolucion.id+"="+String.valueOf(devolucion.getId()),null);
//				
//				String where = NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.devolucionID+"="+String.valueOf(iddevolucion);
//				
//				//BORRAR LOS DETALLES DE LAS FACTURAS DEL RECIBO
//				bdd.delete(TABLA_RECIBO_DETALLE_FACTURA, where ,null); 
			}
			
			if(devolucion.getId()==0){
				devolucion.setId(idMovil);				
			}
			else {
				devolucion_value.put(NMConfig.Devolucion.id, devolucion.getId());
			}
			if (devolucion.getReferencia() == 0) 
				devolucion.setReferencia(idMovil);
//			if(devolucion.getNumeroCentral()!=0) 
//				devolucion_value.put(NMConfig.Devolucion.id, devolucion.getId());
			
			devolucion_value.put(NMConfig.Devolucion.referencia, devolucion.getReferencia());
			devolucion_value.put(NMConfig.Devolucion.numeroCentral, devolucion.getNumeroCentral());
			devolucion_value.put(NMConfig.Devolucion.fecha, devolucion.getFecha());
			devolucion_value.put(NMConfig.Devolucion.objPedidoDevueltoID, devolucion.getObjPedidoDevueltoID());
			devolucion_value.put(NMConfig.Devolucion.numeroPedidoDevuelto, devolucion.getNumeroPedidoDevuelto());
			devolucion_value.put(NMConfig.Devolucion.numeroFacturaDevuelta, devolucion.getNumeroFacturaDevuelta());
	        devolucion_value.put(NMConfig.Devolucion.objClienteID , devolucion.getObjClienteID());
			devolucion_value.put(NMConfig.Devolucion.objSucursalID , devolucion.getObjSucursalID());
			devolucion_value.put(NMConfig.Devolucion.nombreCliente , devolucion.getNombreCliente());
			devolucion_value.put(NMConfig.Devolucion.objMotivoID , devolucion.getObjMotivoID());
			devolucion_value.put(NMConfig.Devolucion.codMotivo , devolucion.getCodMotivo());
			devolucion_value.put(NMConfig.Devolucion.descMotivo , devolucion.getDescMotivo());
			devolucion_value.put(NMConfig.Devolucion.tipoTramite , devolucion.getTipoTramite());
			devolucion_value.put(NMConfig.Devolucion.deVencido , devolucion.isDeVencido());
			devolucion_value.put(NMConfig.Devolucion.parcial , devolucion.isParcial());
			devolucion_value.put(NMConfig.Devolucion.aplicacionInmediata , devolucion.isAplicacionInmediata());
			devolucion_value.put(NMConfig.Devolucion.nota , devolucion.getNota());
			devolucion_value.put(NMConfig.Devolucion.observacion , devolucion.getObservacion());
			devolucion_value.put(NMConfig.Devolucion.subtotal , devolucion.getSubtotal());
			devolucion_value.put(NMConfig.Devolucion.impuesto , devolucion.getImpuesto());
			devolucion_value.put(NMConfig.Devolucion.montoPromocion , devolucion.getMontoPromocion());
			devolucion_value.put(NMConfig.Devolucion.montoPromocionVen , devolucion.getMontoPromocionVen());
			devolucion_value.put(NMConfig.Devolucion.montoCargoAdm , devolucion.getMontoCargoAdm());
			devolucion_value.put(NMConfig.Devolucion.montoCargoAdmVen , devolucion.getMontoCargoAdmVen());
			devolucion_value.put(NMConfig.Devolucion.montoVinieta , devolucion.getMontoVinieta());
			devolucion_value.put(NMConfig.Devolucion.total , devolucion.getTotal());
			devolucion_value.put(NMConfig.Devolucion.totalVen , devolucion.getTotalVen());
			devolucion_value.put(NMConfig.Devolucion.objEstadoID , devolucion.getObjEstadoID());
			devolucion_value.put(NMConfig.Devolucion.descEstado , devolucion.getDescEstado());
			devolucion_value.put(NMConfig.Devolucion.codEstado , devolucion.getCodEstado());
			devolucion_value.put(NMConfig.Devolucion.objCausaEstadoID , devolucion.getObjCausaEstadoID());
			devolucion_value.put(NMConfig.Devolucion.descCausaEstado , devolucion.getDescCausaEstado());
			devolucion_value.put(NMConfig.Devolucion.especial , devolucion.isEspecial());
			devolucion_value.put(NMConfig.Devolucion.montoCargoVendedor , devolucion.getMontoCargoVendedor());
			devolucion_value.put(NMConfig.Devolucion.montoBonif , devolucion.getMontoBonif());
			devolucion_value.put(NMConfig.Devolucion.montoBonifVen , devolucion.getMontoBonifVen());
			devolucion_value.put(NMConfig.Devolucion.impuestoVen , devolucion.getImpuestoVen());
			devolucion_value.put(NMConfig.Devolucion.claveAutorizaAplicacionInmediata , devolucion.getClaveAutorizaAplicacionInmediata());
			devolucion_value.put(NMConfig.Devolucion.fechaEnviada , devolucion.getFechaEnviada());
			devolucion_value.put(NMConfig.Devolucion.pedidoTienePromociones , devolucion.isPedidoTienePromociones());
			devolucion_value.put(NMConfig.Devolucion.pedidoYaDevuelto , devolucion.isPedidoYaDevuelto());
			devolucion_value.put(NMConfig.Devolucion.referenciaNC , devolucion.getReferenciaNC());
			devolucion_value.put(NMConfig.Devolucion.preRegistro , devolucion.isPreRegistro());
			devolucion_value.put(NMConfig.Devolucion.offLine , devolucion.isOffLine());
			devolucion_value.put(NMConfig.Devolucion.fechaFacturacion , devolucion.getFechaFacturacion());
			
			devolucionid = sdb.insert(TABLA_DEVOLUCIONES, null, devolucion_value); // Nueva Devolucion.	
			devolucion.setId(devolucionid);
			
			DevolucionProducto[] productos_devueltos  =  devolucion.getProductosDevueltos();
			
			for(DevolucionProducto dp:productos_devueltos)
			{
				 //productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.id , dp.getId());
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.devolucionID , devolucionid); 
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.objProductoID , dp.getObjProductoID() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.nombreProducto , dp.getNombreProducto() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.cantidadDevolver , dp.getCantidadDevolver() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.bonificacion , dp.getBonificacion() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.bonificacionVen , dp.getBonificacionVen() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.precio , dp.getPrecio() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.subtotal , dp.getSubtotal() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.porcImpuesto , dp.getPorcImpuesto() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.impuesto , dp.getImpuesto() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.total , dp.getTotal() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.totalVen , dp.getTotalVen() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.montoBonif , dp.getMontoBonif() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.montoBonifVen , dp.getMontoBonifVen() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.impuestoVen , dp.getImpuestoVen() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.cantidadOrdenada , dp.getCantidadOrdenada() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.cantidadBonificada , dp.getCantidadBonificada() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.cantidadPromocionada , dp.getCantidadPromocionada() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.descuento , dp.getDescuento() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.totalProducto , dp.getTotalProducto() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.gravable , dp.isGravable() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.deleted , dp.isDeleted() );
				 productos_devueltos_value.put(NMConfig.Devolucion.DevolucionProducto.objProveedorID , dp.getObjProveedorID() );
				 devolucionproductoid=sdb.insert(TABLA_DEVOLUCIONPRODUCTO, null, productos_devueltos_value); // Nueva Devolucion.	
				 
				 DevolucionProductoLote[] lote= dp.getProductoLotes();
				 
				 for(DevolucionProductoLote dpl:lote) {

					 productos_devueltoslote_value = new ContentValues();
					// productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.id , dpl.getId());
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.devolucionproductoID ,devolucionproductoid );
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.devolucionID ,devolucionid );
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.objLoteID , dpl.getObjLoteID());
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.numeroLote , dpl.getNumeroLote());
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.fechaVencimiento , dpl.getFechaVencimiento());
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.cantidadDevuelta , dpl.getCantidadDevuelta());
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.fueraPolitica , dpl.isFueraPolitica());
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.cantidadDespachada , dpl.getCantidadDespachada());
					 productos_devueltoslote_value.put(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.deleted , dpl.isDeleted());
					 sdb.insert(TABLA_DEVOLUCIONPRODUCTOLOTE, null, productos_devueltoslote_value); // Nueva Devolucion.	
					 
				 }
			}

			sdb.setTransactionSuccessful(); // fin transaccion
			
		}
		catch (Exception e) 
		{
			throw new Exception(e);
		}
		finally
		{
			sdb.endTransaction();
			sdb.close();
		}
		
	}
	
}
