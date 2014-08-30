package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.Cliente;
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
	
	public synchronized static Object aplicarDescuentoOcacional(String credenciales,ReciboColector recibo) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","fecha","idCliente","idSucursal","referencia"}),
				 (new Object[]{credenciales,(int)DateUtil.dt2i(Calendar.getInstance().getTime()),recibo.getObjClienteID(),recibo.getObjSucursalID(),recibo.getReferencia()}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.INTEGER_CLASS})); 
		Object rs= NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.VerificarAutorizacionDescuento);
		return rs;
	}
	public  synchronized static RespuestaEnviarRecibo enviarRecibo(String credenciales,ReciboColector recibo) throws Exception
	{		
		Parameters params=new Parameters((new String[]{"Credentials","r"}),
				 (new Object[]{credenciales,recibo}),
				 (new Type[]{PropertyInfo.STRING_CLASS,recibo.getClass()}));
		
		Object rs= NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.EnviarRecibo,ReciboColector.class);
		return NMTranslate.ToObject(rs,new RespuestaEnviarRecibo()); 
	}
	
	public  synchronized static long solicitarDescuentoOcacional(String credenciales,ReciboColector recibo,String notas) throws Exception
	{		
		Parameters params=new Parameters((new String[]{"Credentials","idCliente","idSucursal","referencia","notas"}),
				 (new Object[]{credenciales,recibo.getObjClienteID(),recibo.getObjSucursalID(),recibo.getReferencia(),notas}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.INTEGER_CLASS,PropertyInfo.STRING_CLASS}));
		
		@SuppressWarnings("unused")
		SoapObject rs=(SoapObject) NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.SolicitarDescuento);
		return (Long) rs.getProperty(0); 
	}
	
	public synchronized static int borraReciboByID (ContentResolver content,int reciboID){
		String[] projection = new String[] {};
		int result = 0; 
		String url = DatabaseProvider.CONTENT_URI_RECIBODETALLEFACTURA +"/"+String.valueOf(reciboID);			
		content.delete(Uri.parse(url), "", projection);
		url = DatabaseProvider.CONTENT_URI_RECIBODETALLENOTADEBITO +"/"+String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		url = DatabaseProvider.CONTENT_URI_RECIBODETALLENOTACREDITO +"/"+String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		url = DatabaseProvider.CONTENT_URI_RECIBO +"/"+String.valueOf(reciboID);
		content.delete(Uri.parse(url), "", projection);
		result = 1; 
		return result;		
	}
	
	public synchronized static ReciboColector getReciboByID(ContentResolver content,Integer integer){
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
		ReciboColector recibo = null;
		try {
			String uriString = DatabaseProvider.CONTENT_URI_RECIBO +"/"+String.valueOf(integer);
			Cursor cur = content.query(Uri.parse(uriString),
					projection, // Columnas a devolver
					null, // Condición de la query
					null, // Argumentos variables de la query
					null);
			if (cur.moveToFirst()) {
				recibo = new ReciboColector();
				do {
					recibo.setId(Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))));
					recibo.setNumero(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[1]))));
					recibo.setFecha(Long.parseLong(cur.getString(cur.getColumnIndex(projection[2]))));
					recibo.setNotas(cur.getString(cur.getColumnIndex(projection[3])));
					recibo.setTotalRecibo(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[4]))));
					recibo.setTotalFacturas(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[5]))));
					recibo.setTotalND(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[6]))));
					recibo.setTotalInteres(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[7]))));
					recibo.setSubTotal(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[8]))));
					recibo.setTotalDesc(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[9]))));
					recibo.setTotalRetenido(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[10]))));
					recibo.setTotalOtrasDed(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[11]))));
					recibo.setTotalNC(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[12]))));
					recibo.setReferencia(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[13]))));
					recibo.setObjClienteID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[14]))));
					recibo.setObjSucursalID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[15]))));
					recibo.setNombreCliente(cur.getString(cur.getColumnIndex(projection[16])));
					recibo.setObjColectorID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[17]))));
					boolean aplicaDescuento = !(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[18]))) == 0);
					recibo.setAplicaDescOca(aplicaDescuento);
					recibo.setClaveAutorizaDescOca(cur.getString(cur.getColumnIndex(projection[19])));
					recibo.setPorcDescOcaColector(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[20]))));					
					recibo.setObjEstadoID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[21]))));
					recibo.setCodEstado(cur.getString(cur.getColumnIndex(projection[22])));
					recibo.setDescEstado(cur.getString(cur.getColumnIndex(projection[23])));					
					recibo.setTotalDescOca(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[24]))));
					recibo.setTotalDescPromo(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[25]))));					
					recibo.setTotalDescPP(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[26]))));
					recibo.setTotalImpuestoProporcional(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[27]))));
					recibo.setTotalImpuestoExonerado(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[28]))));
					boolean exento = !( Integer.parseInt(cur.getString(cur.getColumnIndex(projection[29]))) == 0);
					recibo.setExento(exento);
					recibo.setAutorizacionDGI(cur.getString(cur.getColumnIndex(projection[30])));
					
					
				} while (cur.moveToNext());
				
				//OBTENER EL CLIENTE DEL RECIBO
				Cliente cliente = ModelCliente.getClienteBySucursalID(content, recibo.getObjSucursalID());
				recibo.setCliente(cliente);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		recibo.setFacturasRecibo(getFacturasDelRecibo(content, integer));
		recibo.setNotasDebitoRecibo(getNotasDebitoDelRecibo(content, integer));
		recibo.setNotasCreditoRecibo(getNotasCreitoDelRecibo(content, integer));
		recibo.setFormasPagoRecibo(getFormasPagoDelRecibo(content, integer));
		return recibo;
	}
	
	public synchronized static ArrayList<ReciboDetFactura> getFacturasDelRecibo(ContentResolver content,long reciboID){
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
		ReciboDetFactura detalleFactura = null;
		try {	
			String uriString = DatabaseProvider.CONTENT_URI_RECIBODETALLEFACTURA +"/"+String.valueOf(reciboID);
			Cursor cur = content.query(Uri.parse(uriString),
					projection, // Columnas a devolver
					null, // Condición de la query
					null, // Argumentos variables de la query
					null);
			if (cur.moveToFirst()) {				
				do {
					detalleFactura = new ReciboDetFactura();
					detalleFactura.setId(Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))));
					detalleFactura.setObjFacturaID(Long.parseLong(cur.getString(cur.getColumnIndex(projection[1]))));
					detalleFactura.setObjReciboID(Long.parseLong(cur.getString(cur.getColumnIndex(projection[2]))));
					detalleFactura.setMonto(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[3]))));
					boolean esAbono = ( Integer.parseInt(cur.getString(cur.getColumnIndex(projection[4]))) == 0 );
					detalleFactura.setEsAbono(esAbono);
					detalleFactura.setMontoDescEspecifico(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[5]))));
					detalleFactura.setMontoDescOcasional(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[6]))));
					detalleFactura.setMontoRetencion(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[7]))));
					detalleFactura.setMontoImpuesto(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[8]))));
					detalleFactura.setMontoInteres(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[9]))));
					detalleFactura.setMontoNeto(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[10]))));
					detalleFactura.setMontoOtrasDeducciones(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[11]))));
					detalleFactura.setMontoDescPromocion(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[12]))));
					detalleFactura.setPorcDescOcasional(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[13]))));
					detalleFactura.setPorcDescPromo(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[14]))));
					detalleFactura.setNumero(cur.getString(cur.getColumnIndex(projection[15])));
					detalleFactura.setFecha(Long.parseLong(cur.getString(cur.getColumnIndex(projection[16]))));
					detalleFactura.setFechaVence(Long.parseLong(cur.getString(cur.getColumnIndex(projection[17]))));
					detalleFactura.setFechaAplicaDescPP(Long.parseLong(cur.getString(cur.getColumnIndex(projection[18]))));
					detalleFactura.setSubTotal(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[19]))));
					detalleFactura.setImpuesto(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[20]))));
					detalleFactura.setTotalFactura(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[21]))));
					detalleFactura.setSaldoFactura(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[22]))));
					detalleFactura.setInteresMoratorio(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[23]))));
					detalleFactura.setSaldoTotal(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[24]))));
					detalleFactura.setMontoImpuestoExento(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[25]))));
					//detalleFactura.setMontoImpuestoExento(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[26]))));					
					facturas.add(detalleFactura);
				} while (cur.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return facturas;
	}
	
	public synchronized static ArrayList<ReciboDetND> getNotasDebitoDelRecibo(
			ContentResolver content, long reciboID) {
		
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
		ReciboDetND notadebitoDetalle = null;
		try {
			String uriString = DatabaseProvider.CONTENT_URI_RECIBODETALLENOTADEBITO	+ "/" + String.valueOf(reciboID);
			Cursor cur = content.query(Uri.parse(uriString), 
					projection, // Columnas a devolver
					null, // Condición de la query
					null, // Argumentos variables de la query
					null);
			
			if (cur.moveToFirst()) {				
				do {
					notadebitoDetalle = new ReciboDetND();
					notadebitoDetalle.setId(Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))));
					notadebitoDetalle.setObjNotaDebitoID(Long.parseLong(cur.getString(cur.getColumnIndex(projection[1]))));
					notadebitoDetalle.setObjReciboID(Long.parseLong(cur.getString(cur.getColumnIndex(projection[2]))));
					boolean esAbono = ( Integer.parseInt(cur.getString(cur.getColumnIndex(projection[3]))) == 0);
					notadebitoDetalle.setEsAbono(esAbono);
					notadebitoDetalle.setMontoPagar(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[4]))));
					notadebitoDetalle.setNumero(cur.getString(cur.getColumnIndex(projection[5])));
					notadebitoDetalle.setFecha(Long.parseLong(cur.getString(cur.getColumnIndex(projection[6]))));
					notadebitoDetalle.setFechaVence(Long.parseLong(cur.getString(cur.getColumnIndex(projection[7]))));
					notadebitoDetalle.setMontoND(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[8]))));
					notadebitoDetalle.setSaldoND(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[9]))));
					notadebitoDetalle.setInteresMoratorio(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[10]))));
					notadebitoDetalle.setSaldoTotal(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[11]))));
					notadebitoDetalle.setMontoNeto(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[12]))));
					notasdebito.add(notadebitoDetalle);
				} while(cur.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return notasdebito;
	}
	
	public synchronized static ArrayList<ReciboDetNC> getNotasCreitoDelRecibo(ContentResolver content, long reciboID){
		ArrayList<ReciboDetNC> notascredito = new ArrayList<ReciboDetNC>();
		String[] projection = new String[] {
				NMConfig.Recibo.DetalleNotaCredito.ID,
				NMConfig.Recibo.DetalleNotaCredito.NOTACREDITO_ID,
				NMConfig.Recibo.DetalleNotaCredito.RECIBO_ID,
				NMConfig.Recibo.DetalleNotaCredito.MONTO,
				NMConfig.Recibo.DetalleNotaCredito.NUMERO,
				NMConfig.Recibo.DetalleNotaCredito.FECHA,
				NMConfig.Recibo.DetalleNotaCredito.FECHA_VENCE };
		ReciboDetNC notacreditoDetalle = null;
		try {
			String uriString = DatabaseProvider.CONTENT_URI_RECIBODETALLENOTACREDITO + "/" + String.valueOf(reciboID);
			Cursor cur = content.query(Uri.parse(uriString), 
					projection, // Columnas a devolver
					null, // Condición de la query
					null, // Argumentos variables de la query
					null);
			
			if (cur.moveToFirst()) {				
				do {
					notacreditoDetalle = new ReciboDetNC();
					notacreditoDetalle.setId(Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))));
					notacreditoDetalle.setObjNotaCreditoID(Long.parseLong(cur.getString(cur.getColumnIndex(projection[1]))));
					notacreditoDetalle.setObjReciboID(Long.parseLong(cur.getString(cur.getColumnIndex(projection[2]))));					
					notacreditoDetalle.setMonto(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[3]))));
					notacreditoDetalle.setNumero(cur.getString(cur.getColumnIndex(projection[4])));
					notacreditoDetalle.setFecha(Long.parseLong(cur.getString(cur.getColumnIndex(projection[5]))));
					notacreditoDetalle.setFechaVence(Long.parseLong(cur.getString(cur.getColumnIndex(projection[6]))));			
					notascredito.add(notacreditoDetalle);
				} while(cur.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return notascredito;
	}
	
	public synchronized static ArrayList<ReciboDetFormaPago> getFormasPagoDelRecibo(
			ContentResolver content, long reciboID) {
		
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
		
		ReciboDetFormaPago formaPago = null;
		
		try {
			String uriString = DatabaseProvider.CONTENT_URI_RECIBODETALLEFORMAPAGO	+ "/" + String.valueOf(reciboID);
			Cursor cur = content.query(Uri.parse(uriString), 
					projection, // Columnas a devolver
					null, // Condición de la query
					null, // Argumentos variables de la query
					null);
			
			if (cur.moveToFirst()) {				
				do {
					formaPago = new ReciboDetFormaPago();
					formaPago.setObjReciboID( Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))) );
					formaPago.setObjFormaPagoID( Long.parseLong(cur.getString(cur.getColumnIndex(projection[1]))) );
					formaPago.setCodFormaPago( cur.getString(cur.getColumnIndex(projection[2])) );
					formaPago.setDescFormaPago( cur.getString(cur.getColumnIndex(projection[3])) );
					formaPago.setNumero( cur.getString(cur.getColumnIndex(projection[4])) );
					formaPago.setObjMonedaID( Long.parseLong(cur.getString(cur.getColumnIndex(projection[5]))) );
					formaPago.setCodMoneda( cur.getString(cur.getColumnIndex(projection[6])) );
					formaPago.setDescMoneda( cur.getString(cur.getColumnIndex(projection[7])) );
					formaPago.setMonto( Float.parseFloat(cur.getString(cur.getColumnIndex(projection[8]))) );
					formaPago.setMontoNacional( Float.parseFloat(cur.getString(cur.getColumnIndex(projection[9]))) );
					formaPago.setObjEntidadID( Long.parseLong(cur.getString(cur.getColumnIndex(projection[10]))) );
					formaPago.setCodEntidad( cur.getString(cur.getColumnIndex(projection[11])) );
					formaPago.setDescEntidad( cur.getString(cur.getColumnIndex(projection[12])) );
					formaPago.setFecha( Integer.parseInt(cur.getString(cur.getColumnIndex(projection[13]))) );
					formaPago.setSerieBilletes( cur.getString(cur.getColumnIndex(projection[14])) );
					formaPago.setTasaCambio( Float.parseFloat(cur.getString(cur.getColumnIndex(projection[15]))) );
					
					formasPago.add(formaPago);
				} while(cur.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return formasPago;
	}
	
	public synchronized static ArrayList<vmRecibo> getArrayCustomerFromLocalHost(
			ContentResolver content) throws Exception {

		String[] projection = new String[] { NMConfig.Recibo.ID,
				NMConfig.Recibo.NUMERO, NMConfig.Recibo.FECHA,
				NMConfig.Recibo.TOTAL_RECIBO, NMConfig.Recibo.NOMBRE_CLIENTE,
				NMConfig.Recibo.DESCRICION_ESTADO,NMConfig.Recibo.CODIGO_ESTADO, NMConfig.Recibo.SUCURSAL_ID };

		int count = 0;
		ArrayList<vmRecibo> a_vmprod = new ArrayList<vmRecibo>();
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_RECIBO,
				projection, // Columnas a devolver
				null, // Condición de la query
				null, // Argumentos variables de la query
				null);
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

	public synchronized static ArrayList<ReciboColector> getArrayRecibosFromLocalHost(ContentResolver content)
	{
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
		try {
				Cursor cur = content.query(DatabaseProvider.CONTENT_URI_RECIBO,
							projection, // Columnas a devolver
							null, // Condición de la query
							null, // Argumentos variables de la query
							null);
			    ReciboColector recibo = null;
			    if(cur.moveToFirst()){
					do {
						recibo = new ReciboColector();
						recibo.setId(Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))));
						recibo.setNumero(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[1]))));
						recibo.setFecha(Long.parseLong(cur.getString(cur.getColumnIndex(projection[2]))));
						recibo.setNotas(cur.getString(cur.getColumnIndex(projection[3])));
						recibo.setTotalRecibo(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[4]))));
						recibo.setTotalFacturas(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[5]))));
						recibo.setTotalND(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[6]))));
						recibo.setTotalInteres(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[7]))));
						recibo.setSubTotal(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[8]))));
						recibo.setTotalDesc(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[9]))));
						recibo.setTotalRetenido(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[10]))));
						recibo.setTotalOtrasDed(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[11]))));
						recibo.setTotalNC(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[12]))));
						recibo.setReferencia(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[13]))));
						recibo.setObjClienteID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[14]))));
						recibo.setObjSucursalID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[15]))));
						recibo.setNombreCliente(cur.getString(cur.getColumnIndex(projection[16])));
						recibo.setObjColectorID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[17]))));
						boolean aplicaDescuento = !(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[18]))) == 0);
						recibo.setAplicaDescOca(aplicaDescuento);
						recibo.setClaveAutorizaDescOca(cur.getString(cur.getColumnIndex(projection[19])));
						recibo.setPorcDescOcaColector(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[20]))));					
						recibo.setObjEstadoID(Integer.parseInt(cur.getString(cur.getColumnIndex(projection[21]))));
						recibo.setCodEstado(cur.getString(cur.getColumnIndex(projection[22])));
						recibo.setDescEstado(cur.getString(cur.getColumnIndex(projection[23])));					
						recibo.setTotalDescOca(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[24]))));
						recibo.setTotalDescPromo(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[25]))));					
						recibo.setTotalDescPP(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[26]))));
						recibo.setTotalImpuestoProporcional(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[27]))));
						recibo.setTotalImpuestoExonerado(Float.parseFloat(cur.getString(cur.getColumnIndex(projection[28]))));
						boolean exento = !( Integer.parseInt(cur.getString(cur.getColumnIndex(projection[29]))) == 0);
						recibo.setExento(exento);
						recibo.setAutorizacionDGI(cur.getString(cur.getColumnIndex(projection[30])));
						//Get Customer reference
						Cliente cliente = ModelCliente.getClienteBySucursalID(content, recibo.getObjSucursalID());
						recibo.setCliente(cliente);
						//Get Detail
						recibo.setFacturasRecibo(getFacturasDelRecibo(content, recibo.getId()));
						recibo.setNotasDebitoRecibo(getNotasDebitoDelRecibo(content, recibo.getId()));
						recibo.setNotasCreditoRecibo(getNotasCreitoDelRecibo(content, recibo.getId()));
						recibos.add(recibo);
					}
					while (cur.moveToNext());
			    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return recibos;
	}
}
