package com.panzyma.nm.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Factura;

public class ModelDocumento {
	
	/**
	 * Obtiene un Documento del tipo factura
	 * @param content Objeto que resuelve la consulta a la base
	 * @param objFacturaID Id de la factura a buscar
	 * @return
	 */
	public static Factura getFacturaByID(ContentResolver content, Long objFacturaID) {
		String[] proyection = new String[] {};
		
		String uri = DatabaseProvider.CONTENT_URI_FACTURA_BY_ID +"/"+String.valueOf(objFacturaID);
		Factura fact = null;
		
		Cursor cur_fact = content.query(Uri.parse(uri), proyection, null, null, null);
		
		if (cur_fact.moveToFirst()) 
		{   	    	   			 
            do
            {
        	    int value;
        	    fact = new Factura();		       	            	
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
        			
	            	
	         }while (cur_fact.moveToNext());
	      } 	    
 	    
		
		return fact;
	}
    
	/**
	 * Obtiene un Documento del tipo nota credito
	 * @param content
	 * @param objNotaCreditoID
	 * @return
	 */
	public static CCNotaCredito getNotaCreditoByID(ContentResolver content, Long objNotaCreditoID){
		Cursor cur_nc=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_CCNOTACREDITO_BY_ID+"/"+String.valueOf(objNotaCreditoID)),null, null,null, null);
		CCNotaCredito nc = null;
		if (cur_nc.moveToFirst()) 
		{   	    	   			 
            do
            {
            	boolean agregar = true;
        	    nc = new CCNotaCredito();        	    
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
            }while (cur_nc.moveToNext());
		}
 	    return nc;
	}
	
	/**
	 * Obtiene un Documento del tipo nota debito
	 * @param content
	 * @param objNotaDebitoID
	 * @return
	 */
	public static CCNotaDebito getNotasDebitoByID(ContentResolver content, Long objNotaDebitoID) {
		Cursor cur_nd=content.query(Uri.parse(DatabaseProvider.CONTENT_URI_CCNOTADEBITO_BY_ID+"/"+String.valueOf(objNotaDebitoID)),null, null,null, null);
		CCNotaDebito nd = null;
		if (cur_nd.moveToFirst()) 
		{   	    	   			 
            do
            {	nd = new CCNotaDebito();        	    
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

            }while (cur_nd.moveToNext());
		}
		return nd;
	}
	
}
