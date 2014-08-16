package com.panzyma.nm.model;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.ValorCatalogo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ModelValorCatalogo {	
	
	public synchronized static Catalogo getCatalogByName (Context cnt, String catalogName){
		
		List<ValorCatalogo> valoresCatalogo = null ;
		Catalogo catalogo = new Catalogo(catalogName);
		StringBuilder query = new StringBuilder();
		query.append(" SELECT id , ");
		query.append("        codigo , ");
		query.append("        descripcion ");
		query.append(" FROM ValorCatalogo vc ");
		query.append(" WHERE vc.objCatalogoID = (  ");
		query.append(String.format(" SELECT Id FROM CATALOGO c WHERE c.NombreCatalogo = '%s' ", catalogName));
		query.append(" )   ");
		
		SQLiteDatabase db = DatabaseProvider.Helper.getDatabase(cnt);
		try {
			Cursor c = DatabaseProvider.query( db, query.toString());
			valoresCatalogo = new ArrayList<ValorCatalogo> ();
			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do {
					valoresCatalogo.add(
							new ValorCatalogo(
								c.getInt(0),
								c.getString(1),
								c.getString(2))
							);
				} while (c.moveToNext());
			}
			catalogo.setValoresCatalogo(valoresCatalogo);
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
		return catalogo;
	}

}
