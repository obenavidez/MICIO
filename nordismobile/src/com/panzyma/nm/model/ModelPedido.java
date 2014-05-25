package com.panzyma.nm.model;

import java.lang.reflect.Type;

import org.json.JSONArray;
import org.ksoap2.serialization.PropertyInfo;

import android.content.Context;

import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.Parameters;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.Pedido;

public class ModelPedido {

	
	public synchronized static Object onUpdateInventory_From_Server(java.lang.String credentials, java.lang.String usuarioVendedor, boolean todos)throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","UsuarioVendedor","todos"}),
				 (new Object[]{credentials,usuarioVendedor,todos}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS,PropertyInfo.BOOLEAN_CLASS}));
		return NMComunicacion.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.TraerDisponibilidadProductos);
	}   
	
	public  static long RegistrarPedido(Pedido pedido,Context cnt)throws Exception
	{
 		return DatabaseProvider.RegistrarPedido(pedido, cnt);	
	}
 
}
