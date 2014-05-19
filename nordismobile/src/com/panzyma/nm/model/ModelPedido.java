package com.panzyma.nm.model;

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;

import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.Parameters;

public class ModelPedido {

	
	public synchronized static Object onUpdateInventory_From_Server(java.lang.String credentials, java.lang.String usuarioVendedor, boolean todos)throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","UsuarioVendedor","todos"}),
				 (new Object[]{credentials,usuarioVendedor,todos}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS,PropertyInfo.BOOLEAN_CLASS}));
		return NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.TraerDisponibilidadProductos);
	}   
}
