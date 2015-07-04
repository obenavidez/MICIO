package com.panzyma.nm.model;

import java.lang.reflect.Type;
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
	
	public static List<Devolucion> obtenerDevolucionesLocalmente(ContentResolver content)
	{
		 List<Devolucion> lista = new  ArrayList<Devolucion>();
		
		
		 Cursor cur = content.query(DatabaseProvider.CONTENT_URI_DEVOLUCION,
			        null, //Columnas a devolver
			        null,       //Condición de la query
			        null,       //Argumentos variables de la query
			        null); 
		 
		 
		 
		
		
		return lista;
	}
	
}
