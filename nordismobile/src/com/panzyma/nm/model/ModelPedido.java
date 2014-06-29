package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PedidoPromocion;
import com.panzyma.nm.serviceproxy.PedidoPromocionDetalle;
import com.panzyma.nm.viewmodel.vmEntity;

public class ModelPedido {

	
	public synchronized static Object onUpdateInventory_From_Server(java.lang.String credentials, java.lang.String usuarioVendedor, boolean todos)throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","UsuarioVendedor","todos"}),
				 (new Object[]{credentials,usuarioVendedor,todos}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS,PropertyInfo.BOOLEAN_CLASS}));
		return com.comunicator.AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.TraerDisponibilidadProductos);
	}   
	
	public  static long RegistrarPedido(Pedido pedido,Context cnt)throws Exception
	{
 		return DatabaseProvider.RegistrarPedido(pedido, cnt);	
	}
 
	public  static JSONObject enviarPedido(String credenciales,Pedido pedido)throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","Pedido"}),
				 (new Object[]{credenciales,pedido}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.OBJECT_CLASS}));
		return new JSONObject(com.comunicator.AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.EnviarPedido).toString());
	
	}
	
	public static List<vmEntity> obtenerPedidosLocalmente(ContentResolver content)
	{
		List<vmEntity> le=new ArrayList<vmEntity>();
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDO,
		        null, //Columnas a devolver
		        null,       //Condición de la query
		        null,       //Argumentos variables de la query
		        null); 
		
		if (cur.moveToFirst()) 
		 {  			 
           do{
        	   vmEntity e=new vmEntity();
        	   e.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Id)))); 
        	   e.setNumero(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.NumeroMovil))); 
        	   e.setNombre(cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreCliente))+"/"+cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
        	   e.setDescEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DescEstado)));
        	   e.setTotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Total)));
        	   le.add(e);
           }while (cur.moveToNext());
		 }		
		return le;
	}
	
	
	public static Pedido obtenerPedidoByID(long idpedido,ContentResolver content)throws Exception
	{  
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDO,
		        null, //Columnas a devolver
		        "Id=?",       //Condición de la query
		        new String[]{String.valueOf(idpedido)},       //Argumentos variables de la query
		        null);  
		Pedido p=new Pedido();
		if (cur.moveToFirst()) 
		 {    
        	   int value;
        	   p.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Id))));
        	   p.setNumeroMovil(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.NumeroMovil)));
        	   p.setNumeroCentral(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.NumeroCentral)));
        	   p.setTipo(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Tipo)));
        	   p.setFecha(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Fecha)));
        	   p.setObjClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objClienteID))));
        	   p.setNombreCliente(cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreCliente)));
        	   p.setObjSucursalID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objSucursalID))));
        	   p.setNombreSucursal(cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
        	   p.setObjTipoPrecioVentaID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objTipoPrecioVentaID))));
        	   p.setCodTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Pedido.CodTipoPrecio)));
        	   p.setDescTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DescTipoPrecio)));
        	   p.setObjVendedorID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objVendedorID))));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.BonificacionEspecial)); 
        	   p.setBonificacionEspecial(value==1?true:false);
        	   p.setBonificacionSolicitada(cur.getString(cur.getColumnIndex(NMConfig.Pedido.BonificacionSolicitada)));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.PrecioEspecial));
        	   p.setPrecioEspecial(value==1?true:false);
        	   p.setPrecioSolicitado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.PrecioSolicitado)));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.PedidoCondicionado));
        	   p.setPedidoCondicionado(value==1?true:false);
        	   p.setCondicion(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Condicion)));
        	   p.setSubtotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Subtotal)));
        	   p.setDescuento(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Descuento)));
        	   p.setImpuesto(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Impuesto)));
        	   p.setTotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Total)));
        	   p.setObjEstadoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objEstadoID))));
        	   p.setCodEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.CodEstado)));
        	   p.setDescEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DescEstado)));
        	   p.setObjCausaEstadoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objCausaEstadoID))));
        	   p.setCodCausaEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.CodCausaEstado)));
        	   p.setDescCausaEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DescCausaEstado)));
        	   p.setNombreVendedor(cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreVendedor)));
        	   p.setNota(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Nota)));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Exento));        	   
        	   p.setExento(value==1?true:false);
        	   p.setAutorizacionDGI(cur.getString(cur.getColumnIndex(NMConfig.Pedido.AutorizacionDGI)));
        	   
        	   p.setDetalles(obtenerDetallePedido(content,p.getId()));
        	   p.setPromocionesAplicadas(obtenerPedidoPromocion(content,p.getId()));
        	    
          }
		return p;
	}
	
	public static Pedido obtenerPedidosLocales(ContentResolver content)
	{ 
		List<Pedido> lp=new ArrayList<Pedido>();
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDO,
		        null, //Columnas a devolver
		        null,       //Condición de la query
		        null,       //Argumentos variables de la query
		        null); 
		
		if (cur.moveToFirst()) 
		 {  
			 
           do{
        	   Pedido p=new Pedido();
        	   int value;
        	   p.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Id))));
        	   p.setNumeroMovil(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.NumeroMovil)));
        	   p.setNumeroCentral(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.NumeroCentral)));
        	   p.setTipo(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Tipo)));
        	   p.setFecha(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Fecha)));
        	   p.setObjClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objClienteID))));
        	   p.setNombreCliente(cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreCliente)));
        	   p.setObjSucursalID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objSucursalID))));
        	   p.setNombreSucursal(cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreSucursal)));
        	   p.setObjTipoPrecioVentaID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objTipoPrecioVentaID))));
        	   p.setCodTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Pedido.CodTipoPrecio)));
        	   p.setDescTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DescTipoPrecio)));
        	   p.setObjVendedorID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objVendedorID))));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.BonificacionEspecial)); 
        	   p.setBonificacionEspecial(value==1?true:false);
        	   p.setBonificacionSolicitada(cur.getString(cur.getColumnIndex(NMConfig.Pedido.BonificacionSolicitada)));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.PrecioEspecial));
        	   p.setPrecioEspecial(value==1?true:false);
        	   p.setPrecioSolicitado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.PrecioSolicitado)));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.PedidoCondicionado));
        	   p.setPedidoCondicionado(value==1?true:false);
        	   p.setCondicion(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Condicion)));
        	   p.setSubtotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Subtotal)));
        	   p.setDescuento(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Descuento)));
        	   p.setImpuesto(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Impuesto)));
        	   p.setTotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.Total)));
        	   p.setObjEstadoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objEstadoID))));
        	   p.setCodEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.CodEstado)));
        	   p.setDescEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DescEstado)));
        	   p.setObjCausaEstadoID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Pedido.objCausaEstadoID))));
        	   p.setCodCausaEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.CodCausaEstado)));
        	   p.setDescCausaEstado(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DescCausaEstado)));
        	   p.setNombreVendedor(cur.getString(cur.getColumnIndex(NMConfig.Pedido.NombreVendedor)));
        	   p.setNota(cur.getString(cur.getColumnIndex(NMConfig.Pedido.Nota)));
        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.Exento));        	   
        	   p.setExento(value==1?true:false);
        	   p.setAutorizacionDGI(cur.getString(cur.getColumnIndex(NMConfig.Pedido.AutorizacionDGI)));
        	   
        	   p.setDetalles(obtenerDetallePedido(content,p.getId()));
        	   p.setPromocionesAplicadas(obtenerPedidoPromocion(content,p.getId()));
        	   
           }while (cur.moveToNext()); 
	           
          }
		return null;
	}
	
	public static DetallePedido[] obtenerDetallePedido(ContentResolver content,long ObjPedidoID)
	{
	
		int cont=0;int value;  
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDODETALLE,
		        null, //Columnas a devolver
		       NMConfig.Pedido.DetallePedido.objPedidoID+"="+String.valueOf(ObjPedidoID),       //Condición de la query
		     null, // new String[]{ String.valueOf(ObjPedidoID) } ,       //Argumentos variables de la query
   	        null); 
		DetallePedido[] adp=new DetallePedido[cur.getCount()];
		
		if (cur.moveToFirst()) 
		 {  
			 
	           do{ 	 
	        	   
	        	   DetallePedido dp=new DetallePedido();
	        	   dp.setId(cur.getLong(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.Id)));
	        	   dp.setObjPedidoID(cur.getLong(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.objPedidoID)));
	        	   dp.setObjProductoID(cur.getLong(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.objProductoID)));
	        	   dp.setCodProducto(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.codProducto)));
	        	   dp.setNombreProducto(cur.getString(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.nombreProducto)));
	        	   dp.setCantidadOrdenada(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadOrdenada)));
	        	   dp.setCantidadBonificada(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadBonificada)));
	        	   dp.setObjBonificacionID(cur.getLong(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.objBonificacionID)));	        	  
	        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.bonifEditada));
	        	   dp.setBonifEditada(value==1?true:false); 
	        	   dp.setCantidadBonificadaEditada(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadBonificadaEditada)));
	        	   dp.setPrecio(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.precio)));
	        	   dp.setMontoPrecioEditado(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.montoPrecioEditado)));
	        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.precioEditado));
	        	   dp.setPrecioEditado(value==1?true:false); 
	        	   dp.setSubtotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.subtotal)));
	        	   dp.setDescuento(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.descuento)));
	        	   dp.setPorcImpuesto(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.porcImpuesto)));
	        	   dp.setImpuesto(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.impuesto)));
	        	   dp.setTotal(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.total)));
	        	   dp.setCantidadDespachada(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadDespachada)));
	        	   dp.setCantidadADespachar(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadADespachar)));
	        	   dp.setCantidadPromocion(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.DetallePedido.cantidadPromocion)));
 
	        	   adp[cont]=dp; 
				   cont++;
	           }while (cur.moveToNext());
	     }   
	   return adp.length==0?null:adp; 
	}

	public static PedidoPromocion[] obtenerPedidoPromocion(ContentResolver content,long ObjPedidoID)
	{
		int cont=0;int value;  
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDOPROMOCION,
		        null, //Columnas a devolver
		        "objPedidoID=?",       //Condición de la query
		        new String[]{ String.valueOf(ObjPedidoID) }  ,       //Argumentos variables de la query
   	        null); 
		PedidoPromocion[] app=new PedidoPromocion[cur.getCount()];
		
		if (cur.moveToFirst()) 
		 {  
			 
	           do{ 	 
	        	   
	        	   PedidoPromocion pp=new PedidoPromocion();
	         	   pp.setObjPromocionID(cur.getLong(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.objPromocionID)));
	         	   pp.setDescuento(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.descuento)));
	         	   pp.setDetalles(obtenerPedidoPromocionDetalle(content,pp.getObjPromocionID()));
	        	   pp.setCodigoPromocion(cur.getString(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.codigoPromocion)));
	        	   pp.setNombrePromocion(cur.getString(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.nombrePromocion)));
	        	   
	        	   app[cont]=pp; 
				   cont++;
	           }while (cur.moveToNext());  
		
	     }
		return  app.length==0?null:app;
	}
	
	public static PedidoPromocionDetalle[] obtenerPedidoPromocionDetalle(ContentResolver content,long objPromocionID)
	{
		int cont=0;int value;  
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PEDIDOPROMOCIONDETALLE,
		        null, //Columnas a devolver
		        "objPromocionID=?",       //Condición de la query
		        new String[]{String.valueOf(objPromocionID)} ,       //Argumentos variables de la query
   	        null); 
		PedidoPromocionDetalle[] appd=new PedidoPromocionDetalle[cur.getCount()];
		
		if (cur.moveToFirst()) 
		{  
			 
           do{ 	 	        	   
        	   PedidoPromocionDetalle ppd=new PedidoPromocionDetalle();
         	   ppd.setObjProductoID(cur.getLong(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.objProductoID)));
         	   ppd.setDescuento(cur.getFloat(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.descuento)));
         	   ppd.setNombreProducto(cur.getString(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.nombreProducto)));
        	   ppd.setCantidadEntregada(cur.getInt(cur.getColumnIndex(NMConfig.Pedido.PedidoPromocion.PedidoPromocionDetalle.cantidadEntregada)));
        	   appd[cont]=ppd; 
			   cont++;
           }while (cur.moveToNext());  
		
	    }		
		return  appd.length==0?null:appd;
	}
	
}
