package com.panzyma.nm.model;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.datastore.DatabaseProvider.Helper;
import com.panzyma.nm.serviceproxy.EncabezadoSolicitud;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;

public class ModelSolicitudDescuento 
{
	static String TAG=ModelSolicitudDescuento.class.getSimpleName();
	
	
	/**
	 * OBTENER EL DETALLE DEL ENCABEZADO DE LAS SOLICITUDES DE DESCUENTO
	 * @param idrecibo
	 */
	public synchronized static List<SolicitudDescuento> obtenerDetalleSolicitud(long objEncabezadoSolicitudID)
	{
		SQLiteDatabase bd = Helper.getDatabase(NMApp.getContext());  
		List<SolicitudDescuento> detallesolicitud = null;
		Cursor cur;
		try 
		{
			
			StringBuilder sQuery = new StringBuilder(); 			
			sQuery.append(" SELECT sd.* ");
			sQuery.append(" FROM SolicitudDescuento AS sd  ");
		    sQuery.append(" WHERE sd.objEncabezadoSolicitudID = " + objEncabezadoSolicitudID); 	
			cur = DatabaseProvider.query(bd, sQuery.toString());
			detallesolicitud=new ArrayList<SolicitudDescuento>();
			if (cur.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do 
				{	 					
				   SolicitudDescuento solicitud=new SolicitudDescuento(); 
				   solicitud.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.SolicitudDescuento.ID))));
				   solicitud.setEncabezadoSolicitudId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.SolicitudDescuento.OBJ_ENCABEZADO_SOLICITUD_ID))));
				   solicitud.setFacturaId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.SolicitudDescuento.OBJ_FACTURA_ID))));
				   solicitud.setPorcentaje(Float.parseFloat(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.SolicitudDescuento.PORCENTAJE))));
				   solicitud.setJustificacion(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.SolicitudDescuento.JUSTIFICACION)));
				   solicitud.setFecha((Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.SolicitudDescuento.FECHA)))));
				   //solicitud.setFactura(ModelDocumento.getFacturaByID(NMApp.getContext().getContentResolver(),solicitud.getFacturaId()));
				   detallesolicitud.add(solicitud);
				   
				} while (cur.moveToNext());
			}			
			
		} catch (Exception e) {
			// TODO: handle exception
		}		
			
		
		return detallesolicitud;
	}

	/**
	 * OBTENER DEL RECIBO LAS SOLICITUDES DE DESCUENTO
	 * @param idrecibo
	 */
	public synchronized static EncabezadoSolicitud obtenerEncabezadoSolicitud(long idrecibo)
	{
		SQLiteDatabase bd = Helper.getDatabase(NMApp.getContext());  
		Cursor cur;
		ArrayList<SolicitudDescuento> solicitudes=null;
		EncabezadoSolicitud es=null;
		try 
		{			
			StringBuilder sQuery = new StringBuilder(); 			
			sQuery.append(" SELECT es.id,");
			sQuery.append("        es.objReciboID,");
			sQuery.append("        es.codigoEstado,");
			sQuery.append("        es.descripcionEstado,");
			sQuery.append("        es.fechaSolicitud ");
			sQuery.append(" FROM  EncabezadoSolicitud es  ");
			sQuery.append(" WHERE es.objReciboID = " + idrecibo); 
			cur = DatabaseProvider.query(bd, sQuery.toString());
			 
			if (cur.moveToFirst()) 
			{
				// Recorremos el cursor hasta que no haya más registros
				do 
				{	 				 
				   es=new EncabezadoSolicitud();
				   es.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.ID))));
				   es.setObjReciboID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.OBJ_RECIBO_ID))));
				   es.setCodigoEstado(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.CODIGO_ESTADO)));
				   es.setDescripcionEstado(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.DESCRIPCION_ESTADO)));
				   es.setFechaSolicitud(Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.EncabezadoSolicitud.FECHA_SOLICITUD))));
				   es.setDetalles(obtenerDetalleSolicitud(es.getId()));
				   
				} while (cur.moveToNext());
			}			
		
		} catch (Exception e) {
			e.printStackTrace();
		}				
		return es;
	}
 
	
	public synchronized static EncabezadoSolicitud RegistrarSolicituDescuento(EncabezadoSolicitud solicitud) throws Exception
	{
		return DatabaseProvider.registrarSolicitudesDescuento(solicitud, NMApp.getContext());
	}
}
