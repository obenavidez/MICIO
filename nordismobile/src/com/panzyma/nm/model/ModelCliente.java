package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
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
		return  AppNMComunication.InvokeService(NMConfig.URL2+NMConfig.MethodName.GetCliente+"/"+Credentials+"/"+idSucursal); 
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
	
	 
	public synchronized static Cliente getClienteBySucursalID(ContentResolver content,long objSucursalID)throws Exception
	{
		Cliente cliente=new Cliente(); 
		Uri uri=Uri.parse(DatabaseProvider.CONTENT_URI_CLIENTE+"/"+String.valueOf(objSucursalID)); 
		Cursor cur = content.query(uri,
		        null, //Columnas a devolver
		        null,       //Condición de la query
		        null,       //Argumentos variables de la query
		        null); 
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
            
            cliente.setFacturasPendientes(getDocumentosPendientes(content,cliente.getIdSucursal())); 
    		cliente.setNotasCreditoPendientes(getCCNotasDeCredito(content,cliente.getIdSucursal()));
    		cliente.setNotasDebitoPendientes(getCCNotasDeDebito(content,cliente.getIdSucursal()));
    		cliente.setDescuentosProveedor(getDescuentosProveedor(content,cliente.getIdSucursal()));
		 }   
		return cliente;
	}
	
	
	private static Factura[] getDocumentosPendientes(ContentResolver content,long objSucursalID)
	{
		int cont=0;
		Cursor cur_fact=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_FACTURA+"/"+String.valueOf(objSucursalID)),null, null,null, null); 
	    Factura[] afact=new Factura[cur_fact.getCount()]; 
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
        		
        		fact.setDetallePromocionCobro(getPromocionesCobro(content,fact.getId()));
        		fact.setDetalleMontoProveedor(getMontosProveedor(content,fact.getId()));
        	
            	afact[cont]=fact; 
            	cont++;
	            	
	         }while (cur_fact.moveToNext());
	      }
		return afact.length==0?null:afact;
	}
	
	private static PromocionCobro[] getPromocionesCobro(ContentResolver content,long objFacturaID)
	{
		int cont=0;
		Cursor cur_pc=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_PROMOCIONCOBRO+"/"+String.valueOf(objFacturaID)),null, null,null, null);
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
	
	private static MontoProveedor[] getMontosProveedor(ContentResolver content,long objFacturaID)
	{
		int cont=0;
		
		Cursor cur_mp=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_MONTOPROVEEDOR+"/"+String.valueOf(objFacturaID)),null, null,null, null);
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
	
	private static CCNotaCredito[] getCCNotasDeCredito(ContentResolver content,long objSucursalID)
	{
		int cont=0;
		Cursor cur_nc=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_CCNOTACREDITO+"/"+String.valueOf(objSucursalID)),null, null,null, null); 
		CCNotaCredito[] anc=new CCNotaCredito[cur_nc.getCount()]; 
 	    if (cur_nc.moveToFirst()) 
		{   	    	   			 
            do
            {
        	    CCNotaCredito nc=new CCNotaCredito();        	    
        	    nc.setId(Long.parseLong(cur_nc.getString(cur_nc.getColumnIndex(NMConfig.Cliente.CCNotaCredito.Id))));        	    
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
        	    anc[cont]=nc;
        	    cont++;

            }while (cur_nc.moveToNext());
		}
 	    return anc.length==0?null:anc;
	}
	
	private static CCNotaDebito[] getCCNotasDeDebito(ContentResolver content,long objSucursalID)
	{
		int cont=0;
		Cursor cur_nd=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_CCNOTADEBITO+"/"+String.valueOf(objSucursalID)),null, null,null, null); 
		CCNotaDebito[] and=new CCNotaDebito[cur_nd.getCount()]; 
 	    if (cur_nd.moveToFirst()) 
		{   	    	   			 
            do
            {
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
        	    
        	    and[cont]=nd;
        	    cont++;

            }while (cur_nd.moveToNext());
		}
 	    return  and.length==0?null:and;
	}
	
	private static DescuentoProveedor[] getDescuentosProveedor(ContentResolver content,long objSucursalID)
	{
		int cont=0;
		Cursor cur_dp=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_DESCUENTOPROVEEDOR+"/"+String.valueOf(objSucursalID)),null, null,null, null); 
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
		Cursor cur_fact=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_FACTURA+"/"+String.valueOf(objSucursalID)),null, null,null, null); 
		ArrayList<Factura> afact= new ArrayList<Factura>(); 
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
        		
        		fact.setDetallePromocionCobro(getPromocionesCobro(content,fact.getId()));
        		fact.setDetalleMontoProveedor(getMontosProveedor(content,fact.getId()));
        	
            	afact.add(fact); 
            	cont++;
	            	
	         }while (cur_fact.moveToNext());
	      } 	   
		return afact.size()==0?null:afact;
	}
	
}
