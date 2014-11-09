package com.panzyma.nm.model;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PRODUCTO;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_FICHAPRODUCTO_FROM_SERVER;

import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.CBridgeM.BBaseM;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.CProducto;

public class ModelCProducto extends BBaseM {

	@Override
	public boolean handleMessage(Message msg){
		Boolean val=false;
		Bundle b = msg.getData();
		switch (msg.what) 
		{
			case LOAD_FICHAPRODUCTO_FROM_SERVER: 
				getFichaProductoByID(b.getLong("idProducto"));
			break;
		}
		return val;
	}


	public synchronized static void saveCProducto(final CProducto cproducto,Context context)throws Exception
	{
		DatabaseProvider.RegistrarDetalleProducto(cproducto,context); 
	}

	public void getFichaProductoByID(final long idProducto)
	{
		try 
		{
			getPool().execute(  new Runnable()
			{
				@Override
				public void run() {
					try 
					{
						CProducto fichaproducto= null;
						final String credentials=SessionManager.getCredentials();			  
						if(credentials.trim()=="")
							return;	
						if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController())){
							fichaproducto =getFichaProducto(credentials, idProducto);
							Processor.notifyToView(getController(),ID_SINCRONIZE_PRODUCTO,0,1,fichaproducto);
						}

					}
					catch (Exception e) 
					{ 
						e.printStackTrace();
						try {
							Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
						} catch (Exception e1) { 
							e1.printStackTrace();
						} 
					}
				}
			});
		}
		catch(Exception e)
		{
			try {
				Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public synchronized static CProducto getFichaProducto(String Credentials , long idproducto) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","idProducto"}),
				(new Object[]{Credentials,idproducto}),
				(new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS}));

		return  NMTranslate.ToObject(AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCProducto),new CProducto());
	}


}
