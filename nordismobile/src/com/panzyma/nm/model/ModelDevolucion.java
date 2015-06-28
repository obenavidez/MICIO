package com.panzyma.nm.model;

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;
 

import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.serviceproxy.Devolucion; 
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
}
