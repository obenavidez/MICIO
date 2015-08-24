package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.ksoap2.serialization.PropertyInfo;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.comunicator.Parameters;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.datastore.DatabaseProvider.Helper;
import com.panzyma.nm.serviceproxy.Devolucion; 
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.viewmodel.vmDevolucion;
import com.panzyma.nm.viewmodel.vmEntity;
@SuppressWarnings("unused")
public class ModelDevolucion {

	
	static String TAG=ModelDevolucion.class.getSimpleName();
	 
	public ModelDevolucion() {} 
	
	public synchronized static Devolucion BuscarDevolucionDePedido(String Credentials,long idSucursal, long nopedido,long nofactura) throws Exception
	{ 
			Parameters params=new Parameters((new String[]{"Credentials","SucursalID","NumeroPedido","NumeroFactura"}),
											 (new Object[]{Credentials,idSucursal,nopedido,nofactura}),
											 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.LONG_CLASS}));
			
			return  NMTranslate.ToObject(NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.BuscarDevolucionDePedido),new Devolucion()); 
  
	}
	
	public static ArrayList<vmDevolucion> obtenerDevolucionesFromLocalHost(ContentResolver content)
	{
		
		 String[] projection = new String[] {
				 	 NMConfig.Devolucion.id,
				 	 NMConfig.Devolucion.numeroCentral,
				 	 NMConfig.Devolucion.fecha,
					 NMConfig.Devolucion.nombreCliente,
					 NMConfig.Devolucion.total,
					 NMConfig.Devolucion.codEstado,
					 NMConfig.Devolucion.objClienteID,
					 NMConfig.Devolucion.offLine
					 };
		
		
		 ArrayList<vmDevolucion> lista = new  ArrayList<vmDevolucion>();
		
		 Cursor cur = content.query(DatabaseProvider.CONTENT_URI_DEVOLUCION,
				 projection, //Columnas a devolver
			        null,       //Condición de la query
			        null,       //Argumentos variables de la query
			        null);  
		 
		 if (cur.moveToFirst()) 
		 {  
				
	            do{
	            	lista.add(new vmDevolucion(
	            							   Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))),
	            							   Integer.parseInt(cur.getString(cur.getColumnIndex(projection[1]))), 
	            							   Date.valueOf(cur.getString(cur.getColumnIndex(projection[2]))), 
	            							   cur.getString(cur.getColumnIndex(projection[3])), 
	            							   Float.valueOf(cur.getString(cur.getColumnIndex(projection[4]))),
	            							   cur.getString(cur.getColumnIndex(projection[5])),
	            							   Long.parseLong(cur.getString(cur.getColumnIndex(projection[6]))),
	            							   Boolean.parseBoolean(cur.getString(cur.getColumnIndex(projection[7]))))
	            	);
	            	
	            }while (cur.moveToNext());
		 }
		
		
		return lista;
	}
	
	
	public synchronized static Devolucion getDevolucionbyID (long devolucionid)
	{
		Devolucion row = new Devolucion();
		StringBuilder query = new StringBuilder();
		
		SQLiteDatabase db = null;
		try 
		{
			 db = Helper.getDatabase(NMApp.ctx);
			 
			 query.append(" SELECT * FROM Devolucion AS d "); 			
			 query.append(" WHERE d.id= "+ String.valueOf(devolucionid)); 
			 
			 Cursor cur = DatabaseProvider.query(db, query.toString()); 
			 if(cur.getCount()> 0) {
				 if (cur.moveToFirst()) 
				 { 
					 do{
						 row.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.id))));
						 row.setNumeroCentral(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.numeroCentral))));
						 row.setFecha(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.fecha))));
						 row.setObjPedidoDevueltoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.objPedidoDevueltoID))));
						 row.setNumeroPedidoDevuelto(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.numeroPedidoDevuelto))));
						 row.setObjVendedorID(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.objVendedorID))));
						 row.setObjClienteID(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.objClienteID))));
						 row.setObjSucursalID(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.objSucursalID))));
						 row.setNombreCliente(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.nombreCliente)));
						 row.setObjMotivoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.objMotivoID))));
						 row.setCodMotivo(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.codMotivo)));
						 row.setDescMotivo(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.descMotivo)));
						 row.setTipoTramite(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.tipoTramite)));
						 row.setDeVencido(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.deVencido))==1 ? true:false  );
						 row.setParcial(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.parcial))==1 ? true:false  );
						 row.setAplicacionInmediata(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.aplicacionInmediata))==1 ? true:false  );
						 row.setNota(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.nota)));
						 row.setObservacion(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.observacion)));
						 row.setSubtotal(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.subtotal))));
						 row.setImpuesto(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.impuesto))));
						 row.setMontoPromocion(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoPromocion))));
						 row.setMontoPromocionVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoPromocionVen))));
						 row.setMontoCargoAdm(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoCargoAdm))));
						 row.setMontoCargoAdmVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoCargoAdmVen))));
						 row.setMontoVinieta(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoVinieta))));
						 row.setTotal(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.total))));
						 row.setTotalVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.totalVen))));
						 row.setObjEstadoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.objEstadoID))));
						 row.setDescEstado(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.descEstado)));
						 row.setCodEstado(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.codEstado)));
						 row.setObjCausaEstadoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.objCausaEstadoID))));
						 row.setDescCausaEstado(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.descCausaEstado)));
						 row.setEspecial(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.especial)) ==1 ? true:false  );
						 row.setMontoCargoVendedor(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoCargoVendedor))));
						 row.setMontoBonif(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoBonif))));
						 row.setMontoBonifVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.montoBonifVen))));
						 row.setImpuestoVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.impuestoVen))));
						 row.setClaveAutorizaAplicacionInmediata(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.claveAutorizaAplicacionInmediata)));
						 row.setFechaEnviada(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.fechaEnviada))));
						 row.setPedidoTienePromociones(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.pedidoTienePromociones))==1 ? true:false);
						 row.setPedidoYaDevuelto(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.pedidoYaDevuelto))==1 ? true:false);
						 row.setReferenciaNC(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.referenciaNC))); 
						 row.setPreRegistro(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.preRegistro)) ==1 ? true:false); 
						 row.setOffLine(cur.getInt(cur.getColumnIndex(NMConfig.Devolucion.offLine)) ==1 ? true:false); 
						 row.setFechaFacturacion(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.fechaFacturacion)))); 
						 // Get All Productos Devueltos...
						 row.setProductosDevueltos(getDevolucionProducto_byDevolucionID(row.getId()));
						 
					 }
					 while (cur.moveToNext());
				 }
				 
			 }
			 
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally 
		{
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				
				db = null;
			}
		}  
		
		
		return row;
	}
	
	public synchronized static DevolucionProducto[] getDevolucionProducto_byDevolucionID(long devolucionid)
	{
		DevolucionProducto[]  productos = null;
		StringBuilder query = new StringBuilder();
		int contador = 0;
		/*
			long id, long objProductoID,
			String nombreProducto, int cantidadDevolver, int bonificacion,
			int bonificacionVen, long precio, long subtotal, long porcImpuesto,
			long impuesto, long total, long totalVen, long montoBonif,
			long montoBonifVen, long impuestoVen, int cantidadOrdenada,
			int cantidadBonificada, int cantidadPromocionada, long descuento,
			int totalProducto, boolean gravable, boolean deleted,
			long objProveedorID
		*/
		SQLiteDatabase db = null;
		try 
		{
			db = Helper.getDatabase(NMApp.ctx);
			query.append(" Select * from DevolucionProducto where devolucionID = "+ String.valueOf(devolucionid));
			Cursor cur = DatabaseProvider.query(db, query.toString()); 
			productos = new DevolucionProducto[cur.getCount()];
			
			if (cur.moveToFirst()) 
			{ 
					 do{
						 DevolucionProducto r = new DevolucionProducto();
						 r.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.id))));
						 r.setNombreProducto(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.nombreProducto)));
						 r.setCantidadDevolver(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.cantidadDevolver))));
						 r.setBonificacion(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.bonificacion))));
						 r.setBonificacionVen(Integer.parseInt((cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.bonificacionVen)))));
						 r.setPrecio(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.precio))));
						 r.setSubtotal(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.subtotal))));
						 r.setPorcImpuesto(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.porcImpuesto))));
						 r.setImpuesto(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.impuesto))));
						 r.setTotal(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.total))));
						 r.setTotalVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.totalVen))));
						 r.setMontoBonif(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.montoBonif))));
						 r.setMontoBonifVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.montoBonifVen))));
						 r.setImpuestoVen(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.impuestoVen))));
						 r.setCantidadOrdenada(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.cantidadOrdenada))));
						 r.setCantidadBonificada(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.cantidadBonificada))));
						 r.setCantidadPromocionada(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.cantidadPromocionada))));
						 r.setDescuento(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.descuento))));
						 r.setTotalProducto(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.totalProducto))));
						 r.setGravable(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.gravable)))==1 ? true:false);
						 r.setDeleted(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.deleted)))==1 ? true:false);
						 r.setObjProveedorID(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.objProveedorID))));
						 //get All Lotes Asociados
						 r.setProductoLotes(getDevolucionProducto_byDevolucionProductoID(r.getId()));
						
						 productos[contador] = r;
						 contador++;
					 }
					 while (cur.moveToNext());
			 }
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally 
		{
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				
				db = null;
			}
		}  
		
		return productos;
	}
	
	public synchronized static DevolucionProductoLote[] getDevolucionProducto_byDevolucionProductoID(long devolucionproductoid)
	{
		DevolucionProductoLote  lote[] = null;
		
		StringBuilder query = new StringBuilder();
		int contador = 0;
		SQLiteDatabase db = null;
		try 
		{
			db = Helper.getDatabase(NMApp.ctx);
			query.append(" Select * from DevolucionProductoLote where devolucionproductoid = "+ String.valueOf(devolucionproductoid));
			Cursor cur = DatabaseProvider.query(db, query.toString()); 
			lote = new DevolucionProductoLote[cur.getCount()];
			
			if (cur.moveToFirst()) 
			{ 
					 do{
						 DevolucionProductoLote r = new DevolucionProductoLote();
						 r.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.id))));
						 r.setObjLoteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.objLoteID))));
						 r.setNumeroLote(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.numeroLote)));
						 r.setFechaVencimiento(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.fechaVencimiento))));
						 r.setCantidadDevuelta(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.cantidadDevuelta))));
						 r.setFueraPolitica(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.fueraPolitica)))==1 ? true:false);
						 r.setCantidadDespachada(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.cantidadDespachada))));
						 r.setDeleted(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.Devolucion.DevolucionProducto.DevolucionProductoLote.deleted)))==1 ? true:false);
						 lote[contador] = r;
						 
						 contador++;
					 }
					 while (cur.moveToNext());
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally 
		{
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				
				db = null;
			}
		}  
		return lote;
		
	}
	
	
	
}
