package com.panzyma.nm.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.TasaCambio;

public class ModelTasaCambio {
	
	public synchronized static List<TasaCambio> getTasaCambio(Context cnt, int fechaTasaCambio) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT Id , ");
		query.append("        CodMoneda , ");
		query.append("        Tasa ");
		query.append(" FROM TasaCambio tc ");
		query.append(String.format(" WHERE tc.Fecha = %d  ", fechaTasaCambio));
		List<TasaCambio> paridaCambiaria = null;
		SQLiteDatabase db = DatabaseProvider.Helper.getDatabase(cnt);
		try {
			Cursor c = DatabaseProvider.query( db, query.toString());
			paridaCambiaria = new ArrayList<TasaCambio>();
			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do {
					paridaCambiaria.add(new TasaCambio(c.getString(1),
							fechaTasaCambio, c.getFloat(2)));
					
				} while (c.moveToNext());
			}			
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if( db != null  )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		}
		
//		TasaCambio t = new TasaCambio();
//		t.setCodMoneda("CO");
//		t.setFecha(1);
//		t.setTasa(new Float(26.89));
//		paridaCambiaria.add(t);
		return paridaCambiaria;
		
	}
	
	public synchronized static List<TasaCambio> getTasasDeCambios(Context cnt) {
		StringBuilder query = new StringBuilder();
		List<TasaCambio> paridaCambiaria = null;
		SQLiteDatabase db = DatabaseProvider.Helper.getDatabase(cnt);
		
		try {
			query.append(" SELECT Id , ");
			query.append(" Fecha , ");
			query.append(" CodMoneda , ");
			query.append(" Tasa ");
			query.append(" FROM TasaCambio tc ");
			
			Cursor c = DatabaseProvider.query( db, query.toString());
			
			paridaCambiaria = new ArrayList<TasaCambio>();
			
			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				do {
					paridaCambiaria.add(new TasaCambio(c.getString(1),c.getInt(0), c.getFloat(2)));
								
				} while (c.moveToNext());
			}			
		}
		catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		}
		finally {
			if( db != null  )
			{	
				if(db.isOpen())				
					db.close();
				db = null;
			}
		}

//		float tasa = (float) 26.89;
//		
//		for (int i = 0; i < 15; i++) {
//			TasaCambio t = new TasaCambio();
//			t.setCodMoneda("CO");
//			t.setFecha(20150122);
//			t.setTasa(tasa*(i+1));
//			paridaCambiaria.add(t);
//		}
		
		
		return paridaCambiaria;
	}
	
	
}
