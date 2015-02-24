package com.panzyma.nm.model;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.datastore.DatabaseProvider.Helper;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;

public class ModelSolicitudDescuento 
{
	static String TAG=ModelSolicitudDescuento.class.getSimpleName();
	
	
	/**
	 * OBTENER DEL RECIBO LAS SOLICITUDES DE DESCUENTO
	 * @param idrecibo 
	 */
	public synchronized static List<SolicitudDescuento> obtenerSolicitudes(long idrecibo)
	{
		SQLiteDatabase bd = Helper.getDatabase(NMApp.getContext());  
		Cursor cur;
		try 
		{
			
			StringBuilder sQuery = new StringBuilder(); 			
			sQuery.append(" SELECT sd.* ");
			sQuery.append(" FROM SolicitudDescuento AS sd  ");
			sQuery.append(" WHERE sd.objReciboID = " + idrecibo); 	
			cur = DatabaseProvider.query(bd, sQuery.toString());
			ArrayList<SolicitudDescuento> solicitudes=new ArrayList<SolicitudDescuento>();
			if (cur.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do 
				{	 					
				   SolicitudDescuento solicitud=new SolicitudDescuento(); 
				   solicitud.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.SolicitudDescuento.ID))));
				   solicitud.setReciboId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.SolicitudDescuento.OBJ_RECIBO_ID))));
				   solicitud.setFacturaId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.SolicitudDescuento.OBJ_FACTURA_ID))));
				   solicitud.setPorcentaje(Float.parseFloat(cur.getString(cur.getColumnIndex(NMConfig.SolicitudDescuento.PORCENTAJE))));
				   solicitud.setJustificacion(cur.getString(cur.getColumnIndex(NMConfig.SolicitudDescuento.JUSTIFICACION)));
				   solicitud.setFecha((Integer.parseInt(cur.getString(cur.getColumnIndex(NMConfig.SolicitudDescuento.FECHA)))));
					
				} while (cur.moveToNext());
			}			
			
		} catch (Exception e) {
			// TODO: handle exception
		}		
			
		
		return null;
	}

	public synchronized static List<SolicitudDescuento> RegistrarSolicitudes(List<SolicitudDescuento> solicitudes) throws Exception
	{
		return DatabaseProvider.registrarSolicitudesDescuento(solicitudes, NMApp.getContext());
	}
}
