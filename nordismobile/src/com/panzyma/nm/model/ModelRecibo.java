package com.panzyma.nm.model;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Recibo;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.viewmodel.vmRecibo;

public class ModelRecibo {

	private final static String logger = ModelRecibo.class.getSimpleName();

	public ModelRecibo() {
		super();
	}
	
	public synchronized static int borraReciboByID (ContentResolver content,int reciboID){
		String[] projection = new String[] {};
		int result = 0;
		try {
			String url = DatabaseProvider.CONTENT_URI_RECIBO +"/"+String.valueOf(reciboID);
			content.delete(Uri.parse(url), "", projection);
			result = 1;
		} catch (Exception e) {
			
		}
		return result;		
	}
	
	public synchronized static Recibo getReciboByID(ContentResolver content,int reciboID){
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
		Recibo recibo = null;
		try {
			String uriString = DatabaseProvider.CONTENT_URI_RECIBO +"/"+String.valueOf(reciboID);
			Cursor cur = content.query(Uri.parse(uriString),
					projection, // Columnas a devolver
					null, // Condición de la query
					null, // Argumentos variables de la query
					null);
			if (cur.moveToFirst()) {
				recibo = new Recibo();
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		recibo.setFacturasRecibo(getFacturasDelRecibo(content, reciboID));
		recibo.setNotasDebitoRecibo(getNotasDebitoDelRecibo(reciboID));
		recibo.setNotasCreditoRecibo(getNotasCreitoDelRecibo(reciboID));
		return recibo;
	}
	
	public synchronized static ArrayList<ReciboDetFactura> getFacturasDelRecibo(ContentResolver content,int reciboID){
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
			// TODO: handle exception
		}
		
		return facturas;
	}
	
	public synchronized static ArrayList<ReciboDetND> getNotasDebitoDelRecibo(int reiboID){
		ArrayList<ReciboDetND> notasdebito = new ArrayList<ReciboDetND>();
		return notasdebito;
	}
	
	public synchronized static ArrayList<ReciboDetNC> getNotasCreitoDelRecibo(int reiboID){
		ArrayList<ReciboDetNC> notascredito = new ArrayList<ReciboDetNC>();
		return notascredito;
	}

	public synchronized static ArrayList<vmRecibo> getArrayCustomerFromLocalHost(
			ContentResolver content) throws Exception {

		String[] projection = new String[] { NMConfig.Recibo.ID,
				NMConfig.Recibo.NUMERO, 
				NMConfig.Recibo.FECHA,
				NMConfig.Recibo.TOTAL_RECIBO,
				NMConfig.Recibo.NOMBRE_CLIENTE,				
				NMConfig.Recibo.DESCRICION_ESTADO };	
		
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
						.getString(cur.getColumnIndex(projection[5]))));
			} while (cur.moveToNext());
		}

		return a_vmprod;
	}

}
