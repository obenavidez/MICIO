package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;
 













import android.content.ContentResolver;
import android.database.Cursor;

import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.Devolucion; 
import com.panzyma.nm.viewmodel.vmDevolucion;
import com.panzyma.nm.viewmodel.vmEntity;
@SuppressWarnings("unused")
public class ModelDevolucion {

	
	static String TAG=ModelDevolucion.class.getSimpleName();
	 
	public ModelDevolucion() {} 
	
	public synchronized static Devolucion BuscarDevolucionDePedido(String Credentials,long idSucursal, long nopedido,long nofactura) throws Exception
	{ 
			Parameters params=new Parameters((new String[]{"Credentials","SucursalID","NumeroPedido","NumeroFactura"}),
											 (new Object[]{Credentials,idSucursal,nopedido,nofactura}),
											 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.LONG_CLASS,PropertyInfo.LONG_CLASS}));
			
			return  NMTranslate.ToObject(NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.BuscarDevolucionDePedido),new Devolucion()); 
  
	}
	
	public static List<vmDevolucion> obtenerDevolucionesFromLocalHost(ContentResolver content)
	{
		
		 String[] projection = new String[] {
				 	 NMConfig.Devolucion.id,
				 	 NMConfig.Devolucion.numeroCentral,
				 	 NMConfig.Devolucion.fecha,
					 NMConfig.Devolucion.nombreCliente,
					 NMConfig.Devolucion.total,
					 NMConfig.Devolucion.codEstado,
					 NMConfig.Devolucion.objClienteID
					 };
		
		
		 List<vmDevolucion> lista = new  ArrayList<vmDevolucion>();
		
		 Cursor cur = content.query(DatabaseProvider.CONTENT_URI_DEVOLUCION,
			        projection, //Columnas a devolver
			        null,       //Condición de la query
			        null,       //Argumentos variables de la query
			        null);  
		 if (cur.moveToFirst()) 
		 {  
				
	            do{
	            	lista.add(new vmDevolucion(
	            							   Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))),
	            							   Integer.parseInt(cur.getString(cur.getColumnIndex(projection[1]))), 
	            							   Date.valueOf(cur.getString(cur.getColumnIndex(projection[2]))), 
	            							   cur.getString(cur.getColumnIndex(projection[3])), 
	            							   Float.valueOf(cur.getString(cur.getColumnIndex(projection[4]))),
	            							   cur.getString(cur.getColumnIndex(projection[5])),
	            							   Long.parseLong(cur.getString(cur.getColumnIndex(projection[6]))))
	            	);
	            	
	            }while (cur.moveToNext());
		 }
		
		
		return lista;
	}
	
}
