package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.datastore.DatabaseProvider.Helper;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DescuentoProveedor;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.MontoProveedor;
import com.panzyma.nm.serviceproxy.PromocionCobro;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nm.viewmodel.vmFicha;

public class ModelCliente
{  
	static String TAG=ModelCliente.class.getSimpleName();
	
	/*Method del modulo de Cliente pertenecientes a la pantalla principal del modulo*/
	public ModelCliente() {} 
	 
	public synchronized static ArrayList<Cliente> getArrayCustomerFromServer(String Credencials,String UsuarioVendedor,Integer page,Integer rowpage)throws Exception
	 { 
		ArrayList<Cliente> modelcliente =new ArrayList<Cliente>();  
		ArrayList<Parameters> arrayparams=new ArrayList<Parameters>();
		String[] paramname=new String[]{"Credentials","UsuarioVendedor","page","rowsPerPage"}; 
		Object[] values=new Object[]{Credencials,UsuarioVendedor,page,rowpage}; 
		Type[] type=new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.INTEGER_CLASS};
		 
		for(int i=0;i<4;i++)
		{
			Parameters params=new Parameters();
			params.setName(paramname[i]);
			params.setValue(values[i]);
			params.setType(type[i]);  
			arrayparams.add(params); 
		} 
		ArrayList<Cliente> modelclienteL =  NMTranslate.ToCollection( ( AppNMComunication.InvokeMethod (arrayparams,NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetClientesPaged)),Cliente.class);
		if(modelclienteL.size()!=0)
			modelcliente.addAll(modelclienteL);  
		 return modelcliente;
	 }
	
	
	public synchronized static JSONArray getArrayCustomerFromServer2(String Credentials,String UsuarioVendedor,Integer page,Integer rowpage)throws Exception
	{
		//UsuarioVendedor = "ebuitrago"; // <<-- COMENTAR LUEGO
		return AppNMComunication.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetClientesPaged+"/"+Credentials+"/"+UsuarioVendedor+"/"+page+"/"+rowpage);
		//return NMComunicacion.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetClientesPaged+"/"+Credentials+"/"+UsuarioVendedor+"/"+page+"/"+rowpage);
	}
	
	
//	public synchronized static JSONObject actualizarCliente(String Credentials,long idSucursal) throws Exception
//	{
//	    ArrayList<Parameters> arrayparams=new ArrayList<Parameters>();
//		String[] paramname=new String[]{"Credentials","idSucursal"}; 
//		Object[] values=new Object[]{Credentials,idSucursal}; 
//		Type[] type=new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS};		 
//		for(int i=0;i<2;i++)
//		{
//			Parameters params=new Parameters();
//			params.setName(paramname[i]);
//			params.setValue(values[i]);
//			params.setType(type[i]);  
//			arrayparams.add(params); 
//		}  			 
//		//return  new JSONObject(NMComunicacion.InvokeMethod(arrayparams,NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCliente).toString());
//		return  new JSONObject(AppNMComunication.InvokeMethod(arrayparams,NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCliente).toString());
//	 }
	
	public synchronized static JSONObject actualizarCliente(String Credentials,long idSucursal) throws Exception
	{ 
		return  NMComunicacion.InvokeService(NMConfig.URL2+NMConfig.MethodName.GetCliente+"/"+Credentials+"/"+idSucursal); 
	}
	
	public synchronized static ArrayList<vmCliente> getArrayCustomerFromLocalHost(ContentResolver content)throws Exception
	{  		 
		 String[] projection = new String[] {NMConfig.Cliente.IdCliente,NMConfig.Cliente.IdSucursal,
				 							 NMConfig.Cliente.NombreCliente,NMConfig.Cliente.Codigo,
				 							 NMConfig.Cliente.Ubicacion};
 
		 ArrayList<vmCliente> arrayclient=new ArrayList<vmCliente>(); 
		 Cursor cur = content.query(DatabaseProvider.CONTENT_URI_CLIENTE,
							        projection, //Columnas a devolver
							        null,       //Condición de la query
							        null,       //Argumentos variables de la query
							        null);  
		 if (cur.moveToFirst()) 
		 {  
				
	            do{
	            	
	               
	            	arrayclient.add(
	            			         new vmCliente(
							                		Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))),
							                		Long.parseLong(cur.getString(cur.getColumnIndex(projection[1]))),
							                		cur.getString(cur.getColumnIndex(projection[2])),
							                		cur.getString(cur.getColumnIndex(projection[3])),
							                		cur.getString(cur.getColumnIndex(projection[4]))
	                		                       )
	            	                );
	            }while (cur.moveToNext());
		 }   
		 
		 return arrayclient;
	}
	
	public synchronized static void actualizarClienteLocalmente(JSONObject objL,Context ctn)throws Exception
	{   
		DatabaseProvider.ActualizarCliente(objL,ctn);
	}
	
	
	public synchronized static Cliente getClienteBySucursalID(long objSucursalID, long reciboID)throws Exception
	{
		Cliente cliente=new Cliente(); 
//		Uri uri=Uri.parse(DatabaseProvider.CONTENT_URI_CLIENTE+"/"+String.valueOf(objSucursalID)); 
		SQLiteDatabase db = null;
		try 
		{
			
			 //OBTENIENDO LAS FACTURAS
		    db = Helper.getDatabase(NMApp.ctx);
			StringBuilder sQuery = new StringBuilder();
		    sQuery.append(" SELECT * " );		
 			sQuery.append(" FROM Cliente AS c "); 			
 			sQuery.append(" WHERE c.IdSucursal= "+ String.valueOf(objSucursalID)); 
			
			Cursor cur = DatabaseProvider.query(db, sQuery.toString()); 
			if(cur.getCount()!=0)
				cliente=new Cliente();
			
			if (cur.moveToFirst()) 
			 {  
				 
	            do{
	            	
	            	int value; 
	            	cliente.setIdCliente(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.IdCliente))));	            	
				    cliente.setNombreCliente(cur.getString(cur.getColumnIndex(NMConfig.Cliente.NombreCliente)));
				    cliente.setIdSucursal(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.IdSucursal))));
	        		cliente.setCodigo(cur.getString(cur.getColumnIndex(NMConfig.Cliente.Codigo)));
	        		cliente.setCodTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Cliente.CodTipoPrecio)));
	        		cliente.setDesTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Cliente.DesTipoPrecio)));
	        		cliente.setObjPrecioVentaID((Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objPrecioVentaID)))));
	        		cliente.setObjCategoriaClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objCategoriaClienteID))));
	        		cliente.setObjTipoClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objTipoClienteID))));            		
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.AplicaBonificacion));            		
	        		cliente.setAplicaBonificacion(value==1?true:false);
	                value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermiteBonifEspecial));
	        		cliente.setPermiteBonifEspecial(value==1?true:false);
	                value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermitePrecioEspecial));
	        		cliente.setPermitePrecioEspecial(value==1?true:false);            		
	        		cliente.setUG(cur.getString(cur.getColumnIndex(NMConfig.Cliente.UG)));
	        		cliente.setUbicacion(cur.getString(cur.getColumnIndex(NMConfig.Cliente.Ubicacion)));
	        		cliente.setNombreLegalCliente(cur.getString(cur.getColumnIndex(NMConfig.Cliente.NombreLegalCliente)));
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.AplicaOtrasDeducciones));            		
	        		cliente.setAplicaOtrasDeducciones(value==1?true:false);
	        		cliente.setMontoMinimoAbono(cur.getFloat(cur.getColumnIndex(NMConfig.Cliente.MontoMinimoAbono)));
	        		cliente.setPlazoDescuento(cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PlazoDescuento)));
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermiteDevolucion));
	        		cliente.setPermiteDevolucion(value==1?true:false);
	        	    
	        		
	        		
	        	 }while (cur.moveToNext());
	            if (cur!=null)cur.close();
	            cliente.setFacturasPendientes(getFacturasPendientes(cliente.getIdSucursal(),reciboID) ); 
	    		cliente.setNotasCreditoPendientes(getCCNotasDeCredito(db,cliente.getIdSucursal(),reciboID) );
	    		cliente.setNotasDebitoPendientes(getCCNotasDeDebitoPendiente(cliente.getIdSucursal(),reciboID));
	    		cliente.setDescuentosProveedor(getDescuentosProveedor(db,cliente.getIdSucursal()));
			 }  
		} catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		}  
		return cliente;
	}	
	 
	
	public synchronized static Cliente getClienteBySucursalID(SQLiteDatabase db,long objSucursalID, long reciboID)throws Exception
	{
		Cliente cliente=null;
		try
		{		 		
			StringBuilder sQuery = new StringBuilder();
		    sQuery.append(" SELECT * " );		
 			sQuery.append(" FROM Cliente AS c "); 			
 			sQuery.append(" WHERE c.IdSucursal= "+ String.valueOf(objSucursalID)); 
 			if(!db.isOpen())
				db = Helper.getDatabase(NMApp.ctx);
			Cursor cur = DatabaseProvider.query(db, sQuery.toString()); 
			if(cur.getCount()!=0)
				cliente=new Cliente(); 
			if (cur.moveToFirst()) 
			 {  
				 
	            do{
	            	
	            	int value; 
	            	cliente.setIdCliente(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.IdCliente))));	            	
				    cliente.setNombreCliente(cur.getString(cur.getColumnIndex(NMConfig.Cliente.NombreCliente)));
				    cliente.setIdSucursal(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.IdSucursal))));
	        		cliente.setCodigo(cur.getString(cur.getColumnIndex(NMConfig.Cliente.Codigo)));
	        		cliente.setCodTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Cliente.CodTipoPrecio)));
	        		cliente.setDesTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Cliente.DesTipoPrecio)));
	        		cliente.setObjPrecioVentaID((Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objPrecioVentaID)))));
	        		cliente.setObjCategoriaClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objCategoriaClienteID))));
	        		cliente.setObjTipoClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objTipoClienteID))));            		
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.AplicaBonificacion));            		
	        		cliente.setAplicaBonificacion(value==1?true:false);
	                value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermiteBonifEspecial));
	        		cliente.setPermiteBonifEspecial(value==1?true:false);
	                value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermitePrecioEspecial));
	        		cliente.setPermitePrecioEspecial(value==1?true:false);            		
	        		cliente.setUG(cur.getString(cur.getColumnIndex(NMConfig.Cliente.UG)));
	        		cliente.setUbicacion(cur.getString(cur.getColumnIndex(NMConfig.Cliente.Ubicacion)));
	        		cliente.setNombreLegalCliente(cur.getString(cur.getColumnIndex(NMConfig.Cliente.NombreLegalCliente)));
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.AplicaOtrasDeducciones));            		
	        		cliente.setAplicaOtrasDeducciones(value==1?true:false);
	        		cliente.setMontoMinimoAbono(cur.getFloat(cur.getColumnIndex(NMConfig.Cliente.MontoMinimoAbono)));
	        		cliente.setPlazoDescuento(cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PlazoDescuento)));
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermiteDevolucion));
	        		cliente.setPermiteDevolucion(value==1?true:false);
	        	    
	        		
	        		
	        	 }while (cur.moveToNext());
	            
	            cliente.setFacturasPendientes(getDocumentosPendientes(db,cliente.getIdSucursal(),reciboID) ); 
	    		cliente.setNotasCreditoPendientes(getCCNotasDeCredito(db,cliente.getIdSucursal(),reciboID) );
	    		cliente.setNotasDebitoPendientes(getCCNotasDeDebito(db,cliente.getIdSucursal(),reciboID));
	    		cliente.setDescuentosProveedor(getDescuentosProveedor(db,cliente.getIdSucursal()));
			 }  
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return cliente;
	}
	
	
	public synchronized static Cliente getClienteBySucursalID(ContentResolver content,long objSucursalID, long reciboID)throws Exception
	{
		Cliente cliente=new Cliente(); 
		//Uri uri=Uri.parse(DatabaseProvider.CONTENT_URI_CLIENTE+"/"+String.valueOf(objSucursalID)); 
		SQLiteDatabase db = null;
		try 
		{
			
			 //OBTENIENDO LAS FACTURAS
		    db = Helper.getDatabase(NMApp.ctx);		

			StringBuilder sQuery = new StringBuilder();
		    sQuery.append(" SELECT * " );		
 			sQuery.append(" FROM Cliente AS c "); 			
 			sQuery.append(" WHERE c.IdSucursal= "+ String.valueOf(objSucursalID)); 
			
			Cursor cur = DatabaseProvider.query(db, sQuery.toString()); 
			if(cur.getCount()!=0)
				cliente=new Cliente(); 
			
			if (cur.moveToFirst()) 
			 {  
				 
	            do{
	            	
	            	int value; 
	            	cliente.setIdCliente(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.IdCliente))));	            	
				    cliente.setNombreCliente(cur.getString(cur.getColumnIndex(NMConfig.Cliente.NombreCliente)));
				    cliente.setIdSucursal(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.IdSucursal))));
	        		cliente.setCodigo(cur.getString(cur.getColumnIndex(NMConfig.Cliente.Codigo)));
	        		cliente.setCodTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Cliente.CodTipoPrecio)));
	        		cliente.setDesTipoPrecio(cur.getString(cur.getColumnIndex(NMConfig.Cliente.DesTipoPrecio)));
	        		cliente.setObjPrecioVentaID((Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objPrecioVentaID)))));
	        		cliente.setObjCategoriaClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objCategoriaClienteID))));
	        		cliente.setObjTipoClienteID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Cliente.objTipoClienteID))));            		
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.AplicaBonificacion));            		
	        		cliente.setAplicaBonificacion(value==1?true:false);
	                value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermiteBonifEspecial));
	        		cliente.setPermiteBonifEspecial(value==1?true:false);
	                value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermitePrecioEspecial));
	        		cliente.setPermitePrecioEspecial(value==1?true:false);            		
	        		cliente.setUG(cur.getString(cur.getColumnIndex(NMConfig.Cliente.UG)));
	        		cliente.setUbicacion(cur.getString(cur.getColumnIndex(NMConfig.Cliente.Ubicacion)));
	        		cliente.setNombreLegalCliente(cur.getString(cur.getColumnIndex(NMConfig.Cliente.NombreLegalCliente)));
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.AplicaOtrasDeducciones));            		
	        		cliente.setAplicaOtrasDeducciones(value==1?true:false);
	        		cliente.setMontoMinimoAbono(cur.getFloat(cur.getColumnIndex(NMConfig.Cliente.MontoMinimoAbono)));
	        		cliente.setPlazoDescuento(cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PlazoDescuento)));
	        	    value=cur.getInt(cur.getColumnIndex(NMConfig.Cliente.PermiteDevolucion));
	        		cliente.setPermiteDevolucion(value==1?true:false);
	        	    
	        		
	        		
	        	 }while (cur.moveToNext());
	            
	            cliente.setFacturasPendientes(getDocumentosPendientes(db,cliente.getIdSucursal(),reciboID) ); 
	    		cliente.setNotasCreditoPendientes(getCCNotasDeCredito(db,cliente.getIdSucursal(),reciboID) );
	    		cliente.setNotasDebitoPendientes(getCCNotasDeDebito(db,cliente.getIdSucursal(),reciboID));
	    		cliente.setDescuentosProveedor(getDescuentosProveedor(db,cliente.getIdSucursal()));
			 }  
		} catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		}  
		return cliente;
	}
	
	
	private static Factura[] getFacturasPendientes(long objSucursalID,long reciboID)
	{
		int cont=0;
		ContentResolver content=NMApp.getContext().getContentResolver(); 
	    SQLiteDatabase db = null;
	    Factura[] afact = null; 
	    try
	    {
	    	
		    //OBTENIENDO LAS FACTURAS
		    db = Helper.getDatabase(NMApp.ctx);
		    StringBuilder sQuery = new StringBuilder();
		    
		    
		    sQuery.append(" SELECT * " );		
 			sQuery.append(" FROM Factura AS f "); 			
 			sQuery.append(" WHERE f.id in ("); 			
 			
		    sQuery.append(" SELECT f.Id ");
			sQuery.append(" FROM Factura AS f "); 
			sQuery.append(" WHERE f.objSucursalID = " + objSucursalID); 
			sQuery.append("       AND f.codEstado = 'EMITIDA' " );
		    
			sQuery.append(" EXCEPT " );
		     
			sQuery.append(" SELECT f.Id " );		
			sQuery.append(" FROM Factura AS f ");
			sQuery.append("      INNER JOIN ReciboDetalleFactura AS rdf ON f.id=rdf.objFacturaID  " ); 
			sQuery.append("      INNER JOIN Recibo r  ON rdf.objReciboID = r.id");			
			sQuery.append(" WHERE r.codEstado = 'REGISTRADO' AND r.id= "+ reciboID);			
			sQuery.append(" )" ); 
			
			Cursor cur_fact = DatabaseProvider.query(db, sQuery.toString());
			afact=new Factura[cur_fact.getCount()];
			if (cur_fact.moveToFirst()) 
			{
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
				do 
				{	
					Factura fact=new Factura();	
					int value=0;
	            	fact.setId(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Id))));    	            	
	            	fact.setNombreSucursal(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NombreSucursal)));
	            	fact.setNoFactura(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoFactura)));
	            	fact.setTipo(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Tipo)));
	            	fact.setNoPedido(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoPedido)));
	            	fact.setCodEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.CodEstado)));
	            	fact.setEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Estado)));        	            	
	            	fact.setFecha(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Fecha))));
	            	fact.setFechaVencimiento(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaVencimiento))));
	            	fact.setFechaAppDescPP(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaAppDescPP))));
	            	fact.setDias(cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Dias)));
	            	fact.setTotalFacturado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.TotalFacturado)));       	            	
	            	fact.setAbonado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Abonado)));
	            	fact.setDescontado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Descontado)));
	            	fact.setRetenido(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Retenido)));
	            	fact.setOtro(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Otro)));
	            	fact.setSaldo(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Saldo)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Exenta));       	            	
	            	fact.setExenta(value==1?true:false);       	            	
	            	fact.setSubtotalFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.SubtotalFactura)));
	            	fact.setDescuentoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.DescuentoFactura)));
	            	fact.setImpuestoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.ImpuestoFactura)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.PuedeAplicarDescPP));       	            	
	        		fact.setPuedeAplicarDescPP(value==1?true:false);       	            	
	        		
	        		fact.setDetallePromocionCobro(getPromocionesCobro(db,fact.getId()));
	        		fact.setDetalleMontoProveedor(getMontosProveedor(db,fact.getId()));
	        	 
        			afact[cont]=fact; 
                	cont++;       		

				} while (cur_fact.moveToNext());
			}
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		}  
 	    
		return afact.length==0?null:afact;
	}
	
	
	public static Factura[] getFacturasRecibo(SQLiteDatabase db,long reciboID)
	{
		int cont=0;  
	    Factura[] afact = null; 
	    try
	    { 
		    StringBuilder sQuery = new StringBuilder();
		     
		     
			sQuery.append(" SELECT *" );		
			sQuery.append(" FROM Recibo r Factura AS f ");
			sQuery.append("      INNER JOIN ReciboDetalleFactura AS rdf  ON  r.id=rdf.objReciboID " );
			sQuery.append("      INNER JOIN Factura AS f ON rdf.objFacturaID=f.id  " ); 
			sQuery.append("      INNER JOIN Recibo r  ON rdf.objReciboID = r.id");			
			sQuery.append(" WHERE r.id= "+ reciboID);			
			sQuery.append(" )" ); 
			if(!db.isOpen())
				db = Helper.getDatabase(NMApp.ctx);
			Cursor cur_fact = DatabaseProvider.query(db, sQuery.toString());
			afact=new Factura[cur_fact.getCount()];
			if (cur_fact.moveToFirst()) 
			{
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
				do 
				{	
					Factura fact=new Factura();	
					int value=0;
	            	fact.setId(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Id))));    	            	
	            	fact.setNombreSucursal(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NombreSucursal)));
	            	fact.setNoFactura(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoFactura)));
	            	fact.setTipo(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Tipo)));
	            	fact.setNoPedido(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoPedido)));
	            	fact.setCodEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.CodEstado)));
	            	fact.setEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Estado)));        	            	
	            	fact.setFecha(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Fecha))));
	            	fact.setFechaVencimiento(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaVencimiento))));
	            	fact.setFechaAppDescPP(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaAppDescPP))));
	            	fact.setDias(cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Dias)));
	            	fact.setTotalFacturado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.TotalFacturado)));       	            	
	            	fact.setAbonado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Abonado)));
	            	fact.setDescontado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Descontado)));
	            	fact.setRetenido(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Retenido)));
	            	fact.setOtro(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Otro)));
	            	fact.setSaldo(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Saldo)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Exenta));       	            	
	            	fact.setExenta(value==1?true:false);       	            	
	            	fact.setSubtotalFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.SubtotalFactura)));
	            	fact.setDescuentoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.DescuentoFactura)));
	            	fact.setImpuestoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.ImpuestoFactura)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.PuedeAplicarDescPP));       	            	
	        		fact.setPuedeAplicarDescPP(value==1?true:false);       	            	
	        		
	        		fact.setDetallePromocionCobro(getPromocionesCobro(db,fact.getId()));
	        		fact.setDetalleMontoProveedor(getMontosProveedor(db,fact.getId()));
	        	 
        			afact[cont]=fact; 
                	cont++;       		

				} while (cur_fact.moveToNext());
			}
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		}  
 	    
		return afact.length==0?null:afact;
	}
	
	public static Factura[] getFacturasRecibo(long reciboID)
	{
		int cont=0;  
	    Factura[] afact = null; 
	    SQLiteDatabase db = null;
		try 
		{
			
			 //OBTENIENDO LAS FACTURAS
		    db = Helper.getDatabase(NMApp.ctx); 
		    StringBuilder sQuery = new StringBuilder();
		     
		     
			sQuery.append(" SELECT *" );		
			sQuery.append(" FROM Recibo r Factura AS f ");
			sQuery.append("      INNER JOIN ReciboDetalleFactura AS rdf  ON  r.id=rdf.objReciboID " );
			sQuery.append("      INNER JOIN Factura AS f ON rdf.objFacturaID=f.id  " ); 
			sQuery.append("      INNER JOIN Recibo r  ON rdf.objReciboID = r.id");			
			sQuery.append(" WHERE r.id= "+ reciboID);			
			sQuery.append(" )" ); 
			
			Cursor cur_fact = DatabaseProvider.query(db, sQuery.toString());
			afact=new Factura[cur_fact.getCount()];
			if (cur_fact.moveToFirst()) 
			{
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
				do 
				{	
					Factura fact=new Factura();	
					int value=0;
	            	fact.setId(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Id))));    	            	
	            	fact.setNombreSucursal(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NombreSucursal)));
	            	fact.setNoFactura(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoFactura)));
	            	fact.setTipo(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Tipo)));
	            	fact.setNoPedido(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoPedido)));
	            	fact.setCodEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.CodEstado)));
	            	fact.setEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Estado)));        	            	
	            	fact.setFecha(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Fecha))));
	            	fact.setFechaVencimiento(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaVencimiento))));
	            	fact.setFechaAppDescPP(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaAppDescPP))));
	            	fact.setDias(cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Dias)));
	            	fact.setTotalFacturado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.TotalFacturado)));       	            	
	            	fact.setAbonado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Abonado)));
	            	fact.setDescontado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Descontado)));
	            	fact.setRetenido(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Retenido)));
	            	fact.setOtro(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Otro)));
	            	fact.setSaldo(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Saldo)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Exenta));       	            	
	            	fact.setExenta(value==1?true:false);       	            	
	            	fact.setSubtotalFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.SubtotalFactura)));
	            	fact.setDescuentoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.DescuentoFactura)));
	            	fact.setImpuestoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.ImpuestoFactura)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.PuedeAplicarDescPP));       	            	
	        		fact.setPuedeAplicarDescPP(value==1?true:false);       	            	
	        		
	        		fact.setDetallePromocionCobro(getPromocionesCobro(db,fact.getId()));
	        		fact.setDetalleMontoProveedor(getMontosProveedor(db,fact.getId()));
	        	 
        			afact[cont]=fact; 
                	cont++;       		

				} while (cur_fact.moveToNext());
			}
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		}  
 	    
		return afact.length==0?null:afact;
	}
	
	
	private static Factura[] getDocumentosPendientes(SQLiteDatabase db,long objSucursalID, long reciboID)
	{
		int cont=0;  
		Factura[] afact=null;
	    List<Long> facturasGravadas = new ArrayList<Long>();
	    try
	    {
	    	 
		    StringBuilder sQuery = new StringBuilder();
		    
		    sQuery.append(" SELECT Id ");
			sQuery.append(" FROM Factura AS f ");
			sQuery.append(" WHERE f.objSucursalID = " + objSucursalID);
			sQuery.append(" EXCEPT ");
			sQuery.append(" SELECT rdf.objFacturaID " );		
			sQuery.append(" FROM ReciboDetalleFactura AS rdf ");
			sQuery.append("      INNER JOIN Recibo r ");
			sQuery.append("      ON  r.id = rdf.objReciboID ");
			sQuery.append(" WHERE r.objSucursalID = " + objSucursalID);			
			sQuery.append("       AND r.codEstado = 'REGISTRADO' " );
			sQuery.append("       AND r.id <> " + reciboID );
			/*sQuery.append("SELECT rdf.objFacturaID ");
			sQuery.append(" FROM ReciboDetalleFactura AS rdf ");
			sQuery.append("      INNER JOIN Recibo r ");
			sQuery.append("      ON  r.id = rdf.objReciboID ");
			sQuery.append(" WHERE r.objSucursalID = " + objSucursalID);		
			sQuery.append("       AND r.codEstado <> 'ANULADO' ");*/
			if(!db.isOpen())
				db = Helper.getDatabase(NMApp.ctx);
			Cursor c = DatabaseProvider.query(db, sQuery.toString());
			
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
				do {				
					facturasGravadas.add(c.getLong(0));
				} while (c.moveToNext());
			}
			
			
			
			
		    //Factura[] afact=new Factura[cur_fact.getCount() - facturasGravadas.size()];
		       //Recorremos el cursor
		    sQuery=new StringBuilder();
		    sQuery.append(" SELECT * ");
			sQuery.append(" FROM Factura AS f ");
			sQuery.append(" WHERE f.objSucursalID = " + objSucursalID);
			Cursor cur_fact = DatabaseProvider.query(db, sQuery.toString());
			int size =cur_fact.getCount(); //facturasGravadas.size(); 
		    afact=new Factura[size];
	 	    if (cur_fact.moveToFirst()) 
			{   	    	   			 
	            do
	            {
	        	    int value;
	        	    boolean agregar = true;
	            	Factura fact=new Factura();		       	            	
	            	fact.setId(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Id))));   
	            	
	            	/*for(Long facturaID : facturasGravadas) {
	            		if(fact.getId() == facturaID)
	            			agregar = false;
	            	}*/
	            	
	            	fact.setNombreSucursal(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NombreSucursal)));
	            	fact.setNoFactura(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoFactura)));
	            	fact.setTipo(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Tipo)));
	            	fact.setNoPedido(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoPedido)));
	            	fact.setCodEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.CodEstado)));
	            	fact.setEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Estado)));        	            	
	            	fact.setFecha(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Fecha))));
	            	fact.setFechaVencimiento(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaVencimiento))));
	            	fact.setFechaAppDescPP(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaAppDescPP))));
	            	fact.setDias(cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Dias)));
	            	fact.setTotalFacturado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.TotalFacturado)));       	            	
	            	fact.setAbonado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Abonado)));
	            	fact.setDescontado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Descontado)));
	            	fact.setRetenido(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Retenido)));
	            	fact.setOtro(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Otro)));
	            	fact.setSaldo(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Saldo)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Exenta));       	            	
	            	fact.setExenta(value==1?true:false);       	            	
	            	fact.setSubtotalFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.SubtotalFactura)));
	            	fact.setDescuentoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.DescuentoFactura)));
	            	fact.setImpuestoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.ImpuestoFactura)));       	            	
	                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.PuedeAplicarDescPP));       	            	
	        		fact.setPuedeAplicarDescPP(value==1?true:false);       	            	
	        		
	        		fact.setDetallePromocionCobro(getPromocionesCobro(db,fact.getId()));
	        		fact.setDetalleMontoProveedor(getMontosProveedor(db,fact.getId()));
	        	
	        		if(agregar) {
	        			afact[cont]=fact; 
	                	cont++;
	        		}        		
	            	
		            	
		         }while (cur_fact.moveToNext());
		      } 	    
	 	    
			
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} 
	    
		return afact.length==0?null:afact;
	}
	
	private static PromocionCobro[] getPromocionesCobro(SQLiteDatabase db,long objFacturaID)
	{
		int cont=0;
		StringBuilder sQuery = new StringBuilder();
	    sQuery.append(" SELECT * ");
		sQuery.append(" FROM PromocionCobro AS pc ");
		sQuery.append(" WHERE pc.objFacturaID = " + objFacturaID);
		if(!db.isOpen())
			db = Helper.getDatabase(NMApp.ctx);
		Cursor cur_pc = DatabaseProvider.query(db, sQuery.toString()); 
		PromocionCobro[] apc=new PromocionCobro[cur_pc.getCount()];
		if (cur_pc.moveToFirst()) 
		{         	
			do
		    {
				PromocionCobro pc=new PromocionCobro();
				pc.setFacturasAplicacion(cur_pc.getString(cur_pc.getColumnIndex(NMConfig.Cliente.Factura.PromocionCobro.FacturasAplicacion)));
				pc.setTipoDescuento(cur_pc.getString(cur_pc.getColumnIndex(NMConfig.Cliente.Factura.PromocionCobro.TipoDescuento)));
				pc.setDescuento(cur_pc.getFloat(cur_pc.getColumnIndex(NMConfig.Cliente.Factura.PromocionCobro.FacturasAplicacion)));
				apc[cont]=pc; 
		    	cont++;
		     }while (cur_pc.moveToNext()); 
		} 
		return apc.length==0?null:apc;
		
	}
	
	private static MontoProveedor[] getMontosProveedor(SQLiteDatabase db,long objFacturaID)
	{
		int cont=0;
		StringBuilder sQuery = new StringBuilder();
	    sQuery.append(" SELECT * ");
		sQuery.append(" FROM MontoProveedor AS mp ");
		sQuery.append(" WHERE mp.objFacturaID = " + objFacturaID);
		if(!db.isOpen())
			db = Helper.getDatabase(NMApp.ctx);
		Cursor cur_mp = DatabaseProvider.query(db, sQuery.toString()); 
		MontoProveedor[] amp=new MontoProveedor[cur_mp.getCount()];
		if (cur_mp.moveToFirst()) 
		{ 
			do
		    {
		    	MontoProveedor mp=new MontoProveedor();
		    	mp.setObjProveedorID(Long.parseLong(cur_mp.getString(cur_mp.getColumnIndex(NMConfig.Cliente.Factura.MontoProveedor.ObjProveedorID))));
		    	mp.setMonto(cur_mp.getFloat(cur_mp.getColumnIndex(NMConfig.Cliente.Factura.MontoProveedor.Monto)));
		    	mp.setCodProveedor(cur_mp.getString(cur_mp.getColumnIndex(NMConfig.Cliente.Factura.MontoProveedor.CodProveedor)));
		    	amp[cont]=mp; 
		    	cont++;
		     }while (cur_mp.moveToNext()); 
		}
		return amp.length==0?null:amp;
		
	}
	
	private static CCNotaCredito[] getCCNotasDeCredito(SQLiteDatabase db,long objSucursalID,long reciboID)
	{
		int cont=0; 
	    List<Long> notasCreditoGravadas = new ArrayList<Long>();
	    CCNotaCredito[] anc=null;
	    try{
	    	 
		    StringBuilder sQuery = new StringBuilder();
			
		    sQuery.append(" SELECT Id ");
			sQuery.append(" FROM CCNotaCredito AS nd ");
			sQuery.append(" WHERE nd.objSucursalID = " + objSucursalID);
			sQuery.append(" EXCEPT ");
			sQuery.append(" SELECT rdf.objNotaCreditoID " );		
			sQuery.append(" FROM ReciboDetalleNotaCredito AS rdf ");
			sQuery.append("      INNER JOIN Recibo r ");
			sQuery.append("      ON  r.id = rdf.objReciboID ");
			sQuery.append(" WHERE r.objSucursalID = " + objSucursalID);			
			sQuery.append("       AND r.codEstado = 'REGISTRADO' " );
			sQuery.append("       AND r.id <> " + reciboID );
			if(!db.isOpen())
				db = Helper.getDatabase(NMApp.ctx);
			Cursor c = DatabaseProvider.query(db, sQuery.toString());
			
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
				do {				
					notasCreditoGravadas.add(c.getLong(0));
				} while (c.moveToNext());
			}
			
			
			
			
			
			sQuery=new StringBuilder();
		    sQuery.append(" SELECT * ");
			sQuery.append(" FROM CCNotaCredito AS nc ");
			sQuery.append(" WHERE nc.objSucursalID = " + objSucursalID);
			Cursor cur_nc = DatabaseProvider.query(db, sQuery.toString());
			 //int size = notasCreditoGravadas.size();
		    int size = cur_nc.getCount();
			anc=new CCNotaCredito[size]; 
			
	 	    if (cur_nc.moveToFirst()) 
			{   	    	   			 
	            do
	            {
	            	boolean agregar = true;
	        	    CCNotaCredito nc=new CCNotaCredito();        	    
	        	    nc.setId(Long.parseLong(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Id))));

	        	    /*for(Long id : notasCreditoGravadas) {
	        	    	if(id == nc.getId() && reciboID != 0) {
	        	    		agregar = true;
	        	    	}
	        	    }*/
	        	    
	        	    nc.setNombreSucursal(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.NombreSucursal)));
	        	    nc.setEstado(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Estado)));
	        	    nc.setNumero(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Numero)));        	    
	        	    nc.setFecha(Long.parseLong(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Fecha))));
	        	    nc.setFechaVence(Long.parseLong(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.FechaVence))));
	        	    nc.setConcepto(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Concepto)));
	        	    nc.setMonto(cur_nc.getFloat(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Monto)));
	        	    nc.setNumRColAplic(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.NumRColAplic)));
	        	    nc.setCodEstado(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.CodEstado)));
	        	    nc.setDescripcion(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Descripcion)));
	        	    nc.setReferencia(cur_nc.getInt(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Referencia)));
	        	    nc.setCodConcepto(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.CodConcepto)));
	        	    
	        	    if(agregar) {
	        	    	anc[cont]=nc;
	            	    cont++;
	        	    }        	    

	            }while (cur_nc.moveToNext());
			}
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} 
	   
 	    return anc.length==0?null:anc;
	}
		
	private static CCNotaDebito[] getCCNotasDeDebitoPendiente(long objSucursalID,long reciboID)
	{
		int cont=0; 
		
		CCNotaDebito[] array_nd =null; SQLiteDatabase db = null; 
	    try
	    {
	    	
		    //OBTENIENDO ND
		    db = Helper.getDatabase(NMApp.ctx);
		    StringBuilder sQuery = new StringBuilder(); 	    
 			
 			sQuery.append(" SELECT * " );		
 			sQuery.append(" FROM CCNotaDebito AS nd "); 			
 			sQuery.append(" WHERE nd.id in (");
 			
 			
		    sQuery.append(" SELECT Id ");
			sQuery.append(" FROM CCNotaDebito AS nd "); 
			sQuery.append(" WHERE nd.objSucursalID = " + objSucursalID); 
			sQuery.append("       AND nd.saldo <> 0 " );
		    
			sQuery.append(" EXCEPT " );
			
		    sQuery.append(" SELECT nd.Id ");
			sQuery.append(" FROM CCNotaDebito AS nd ");
			sQuery.append(" 	 INNER JOIN ReciboDetalleNotaDebito AS rdnd  ON  nd.id = rdnd.objNotaDebitoID ");
			sQuery.append("      INNER JOIN Recibo r  ON  rdnd.objReciboID = r.id   ");
			sQuery.append(" WHERE nd.objSucursalID = " + objSucursalID );
			sQuery.append(" 	  AND  r.id <> " + reciboID );
			sQuery.append("       )"); 
			
			Cursor cur_nd = DatabaseProvider.query(db, sQuery.toString());
			array_nd=new CCNotaDebito[cur_nd.getCount()];
			if (cur_nd.moveToFirst()) 
			{
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
				do {		

					boolean agregar = true;
	            	CCNotaDebito nd=new CCNotaDebito();        	    
	        	    nd.setId(Long.parseLong(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Id))));           	    
	        	    nd.setNombreSucursal(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.NombreSucursal)));
	        	    nd.setEstado(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Estado)));
	        	    nd.setNumero(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Numero)));        	    
	        	    nd.setFecha(Long.parseLong(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Fecha))));
	        	    nd.setFechaVence(Long.parseLong(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.FechaVence))));
	        	    nd.setDias(cur_nd.getInt(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Dias)));
	        	    nd.setConcepto(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Concepto)));
	        	    nd.setMonto(cur_nd.getFloat(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Monto)));
	        	    nd.setMontoAbonado(cur_nd.getFloat(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.MontoAbonado)));
	        	    nd.setSaldo(cur_nd.getFloat(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Saldo)));
	        	    nd.setCodEstado(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.CodEstado)));
	        	    nd.setDescripcion(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Descripcion)));
	        	     
        	    	array_nd[cont]=nd;
            	    cont++; 
					
				} while (cur_nd.moveToNext());
			}
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		}   
 	    return  array_nd.length==0?null:array_nd;
	}
	
	private static CCNotaDebito[] getCCNotasDeDebito(SQLiteDatabase db,long objSucursalID, long reciboID)
	{
		int cont=0;
		  
	    List<Long> notasDebitoGravadas = new ArrayList<Long>();CCNotaDebito[] array_nd=null;
	    try{
	    	 
		    StringBuilder sQuery = new StringBuilder();
		    
		    sQuery.append(" SELECT nd.Id ");
			sQuery.append(" FROM CCNotaDebito AS nd ");
			sQuery.append(" WHERE nd.objSucursalID = " + objSucursalID);
			sQuery.append(" EXCEPT ");
			sQuery.append(" SELECT rdf.objNotaDebitoID " );		
			sQuery.append(" FROM ReciboDetalleNotaDebito AS rdf ");
			sQuery.append("      INNER JOIN Recibo r ");
			sQuery.append("      ON  r.id = rdf.objReciboID ");
			sQuery.append(" WHERE r.objSucursalID = " + objSucursalID);			
			sQuery.append("       AND r.codEstado = 'REGISTRADO' " );	
			sQuery.append("       AND r.id <> " + reciboID );
			if(!db.isOpen())
				db = Helper.getDatabase(NMApp.ctx);
			
			Cursor c = DatabaseProvider.query(db, sQuery.toString());
			
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registro(_db[0]==null)s
				do {				
					notasDebitoGravadas.add(c.getLong(0));
				} while (c.moveToNext());
			}
			
			sQuery = new StringBuilder();
			sQuery.append(" SELECT * ");
			sQuery.append(" FROM CCNotaDebito AS nd ");
			sQuery.append(" WHERE nd.objSucursalID = " + objSucursalID);
			Cursor cur_nd= DatabaseProvider.query(db, sQuery.toString());
		    int  size=cur_nd.getCount();
			array_nd = new CCNotaDebito[size]; 
	 	    if (cur_nd.moveToFirst()) 
			{   	    	   			 
	            do
	            {
	            	boolean agregar = true;
	            	CCNotaDebito nd=new CCNotaDebito();        	    
	        	    nd.setId(Long.parseLong(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Id))));   
	        	    
	        	    /*for(Long id : notasDebitoGravadas) {
	        	    	if(id == nd.getId() && reciboID != 0) {
	        	    		agregar = true;
	        	    	}
	        	    }*/
	        	    
	        	    nd.setNombreSucursal(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.NombreSucursal)));
	        	    nd.setEstado(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Estado)));
	        	    nd.setNumero(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Numero)));        	    
	        	    nd.setFecha(Long.parseLong(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Fecha))));
	        	    nd.setFechaVence(Long.parseLong(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.FechaVence))));
	        	    nd.setDias(cur_nd.getInt(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Dias)));
	        	    nd.setConcepto(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Concepto)));
	        	    nd.setMonto(cur_nd.getFloat(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Monto)));
	        	    nd.setMontoAbonado(cur_nd.getFloat(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.MontoAbonado)));
	        	    nd.setSaldo(cur_nd.getFloat(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Saldo)));
	        	    nd.setCodEstado(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.CodEstado)));
	        	    nd.setDescripcion(cur_nd.getString(cur_nd.getColumnIndex(NMConfig.Cliente.CCNotaDebito.Descripcion)));
	        	    
	        	    if(agregar) {
	        	    	array_nd[cont]=nd;
	            	    cont++;
	        	    }
	        	    

	            }while (cur_nd.moveToNext());
			}
			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		}  
	   
 	    return  array_nd.length==0?null:array_nd;
	}
	
	private static DescuentoProveedor[] getDescuentosProveedor(SQLiteDatabase db,long objSucursalID)
	{
		int cont=0;
		StringBuilder sQuery = new StringBuilder();
	    
	    sQuery.append(" SELECT * ");
		sQuery.append(" FROM DescuentoProveedor AS dp ");
		sQuery.append(" WHERE dp.objSucursalID = " + objSucursalID); 		
		if(!db.isOpen())
			db = Helper.getDatabase(NMApp.ctx);
		Cursor  cur_dp= DatabaseProvider.query(db, sQuery.toString());
		DescuentoProveedor[] adp=new DescuentoProveedor[cur_dp.getCount()]; 
 	    if (cur_dp.moveToFirst()) 
		{   	    	   			 
            do
            {
            	DescuentoProveedor dp=new DescuentoProveedor();        	    
        	    dp.setObjProveedorID(Long.parseLong(cur_dp.getString(cur_dp.getColumnIndex(NMConfig.Cliente.DescuentoProveedor.ObjProveedorID))));
        	    dp.setPrcDescuento(cur_dp.getFloat(cur_dp.getColumnIndex(NMConfig.Cliente.DescuentoProveedor.PrcDescuento)));
        	    adp[cont]=dp;
        	    cont++;

            }while (cur_dp.moveToNext());
		}
 	    return adp.length==0?null:adp;
	}
	
	public synchronized static void saveClientes(JSONArray objL,Context cnt,int page) throws Exception
	{
		DatabaseProvider.RegistrarClientes(objL, cnt, page);
	}
	
	public synchronized static void UpdateCustomer_From_LocalHost(final ArrayList<Cliente> objL,Context ctn) throws Exception
	{  
		//DatabaseProvider.UpdateCustomer(objL,ctn); 
	}  
	
	/*Method del modulo de Cliente pertenecientes a la pantalla Ficha del Cliente*/ 	
	public synchronized static CCCliente getFichaCustomerFromServer(String Credentials,long idSucursal) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","idSucursal"}),
										 (new Object[]{Credentials,idSucursal}),
										 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS}));
		return  NMTranslate.ToObject(AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCCCliente),new CCCliente());		 
	} 
	
	public synchronized static vmFicha GetFichaCustomerFromServer(String Credentials,long idSucursal) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","idSucursal"}),
										 (new Object[]{Credentials,idSucursal}),
										 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS}));
		
		return  NMTranslate.ToObject(AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCCCliente),new vmFicha());		 
	} 

	public synchronized static ArrayList<Factura> getFacturasClienteFromServer(Parameters params) throws Exception
	{		
		return  NMTranslate.ToCollection(AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.TraerFacturasCliente),Factura.class);	 
	}	
	
	public static ArrayList<Factura> getFacturasPendientesBySucursal(ContentResolver content,long objSucursalID)
	{
		int cont=0;
//		Cursor cur_fact=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_FACTURA+"/"+String.valueOf(objSucursalID)),null, null,null, null); 
		ArrayList<Factura> afact= new ArrayList<Factura>(); 
		
		SQLiteDatabase db = null; 
	    try
	    { 
		    db = Helper.getDatabase(NMApp.ctx);
		    StringBuilder sQuery = new StringBuilder(); 	    
 			
 			sQuery.append(" SELECT * " );		
 			sQuery.append(" FROM Factura AS f "); 			
 			sQuery.append(" WHERE f.objSucursalID="+ objSucursalID);
 			Cursor  cur_fact= DatabaseProvider.query(db, sQuery.toString());
	       //Recorremos el cursor
 	    if (cur_fact.moveToFirst()) 
		{   	    	   			 
            do
            {
        	    int value;
            	Factura fact=new Factura();		       	            	
            	fact.setId(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Id))));       	            	
            	fact.setNombreSucursal(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NombreSucursal)));
            	fact.setNoFactura(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoFactura)));
            	fact.setTipo(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Tipo)));
            	fact.setNoPedido(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.NoPedido)));
            	fact.setCodEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.CodEstado)));
            	fact.setEstado(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Estado)));        	            	
            	fact.setFecha(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Fecha))));
            	fact.setFechaVencimiento(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaVencimiento))));
            	fact.setFechaAppDescPP(Long.parseLong(cur_fact.getString(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.FechaAppDescPP))));
            	fact.setDias(cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Dias)));
            	fact.setTotalFacturado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.TotalFacturado)));       	            	
            	fact.setAbonado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Abonado)));
            	fact.setDescontado(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Descontado)));
            	fact.setRetenido(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Retenido)));
            	fact.setOtro(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Otro)));
            	fact.setSaldo(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Saldo)));       	            	
                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.Exenta));       	            	
            	fact.setExenta(value==1?true:false);       	            	
            	fact.setSubtotalFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.SubtotalFactura)));
            	fact.setDescuentoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.DescuentoFactura)));
            	fact.setImpuestoFactura(cur_fact.getFloat(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.ImpuestoFactura)));       	            	
                value=cur_fact.getInt(cur_fact.getColumnIndex(NMConfig.Cliente.Factura.PuedeAplicarDescPP));       	            	
        		fact.setPuedeAplicarDescPP(value==1?true:false);       	            	
        		
        		fact.setDetallePromocionCobro(getPromocionesCobro(db,fact.getId()));
        		fact.setDetalleMontoProveedor(getMontosProveedor(db,fact.getId()));
        	
            	afact.add(fact); 
            	cont++;
	            	
	         }while (cur_fact.moveToNext());
	      }
	    } catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if( db != null )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		} 
		return afact.size()==0?null:afact;
	}
	
}
