package com.panzyma.nm.model;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.datastore.DatabaseProvider;
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
