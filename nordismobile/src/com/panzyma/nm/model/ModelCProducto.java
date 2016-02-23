package com.panzyma.nm.model;
import java.lang.reflect.Type;

import org.ksoap2.serialization.PropertyInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.CNota;
import com.panzyma.nm.serviceproxy.CProducto;

public class ModelCProducto {


	public synchronized static void saveCProducto(final CProducto cproducto,Context context)throws Exception
	{
		DatabaseProvider.RegistrarDetalleProducto(cproducto,context); 
	}

	public synchronized static CProducto getFichaProductoFromServer(String Credentials , long idproducto) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","idProducto"}),
				(new Object[]{Credentials,idproducto}),
				(new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS}));

		return  NMTranslate.ToObject(AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCProducto),new CProducto());

	}
	public synchronized static CProducto getFichaProductoFromLocalHost(ContentResolver content,long idproducto)throws Exception
	{

		//String uriString = DatabaseProvider.CONTENT_URI_CPRODUCTO.toString(); //+"/"+String.valueOf(idproducto);
		//String query = NMConfig.CProducto.ID+ "=?";
		
//		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_CPRODUCTO,
//		        null, //Columnas a devolver
//		        "Id=?",       //Condición de la query
//		        new String[]{String.valueOf(idproducto)},       //Argumentos variables de la query
//		        null);  
		
//		Uri uri = Uri.parse(DatabaseProvider.CONTENT_URI_CPRODUCTO.toString()); 
//		Cursor cur = content.query(uri,
//		        null, //Columnas a devolver
//		        "Id=?",       //Condición de la query
//		        new String[]{ String.valueOf(idproducto) },       //Argumentos variables de la query
//		        null);
		
		Uri uri=Uri.parse(DatabaseProvider.CONTENT_URI_CPRODUCTO+"/"+String.valueOf(idproducto)); 
		Cursor cur = content.query(uri,
		        null, //Columnas a devolver
		        null,       //Condición de la query
		        null,       //Argumentos variables de la query
		        null); 

		CProducto detalle = null;

		if (cur.moveToFirst()) 
		{  
			detalle = new CProducto();
			detalle.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.CProducto.ID))));
			detalle.setNombre(cur.getString(cur.getColumnIndex(NMConfig.CProducto.NOMBRE)));
			detalle.setNombreComercial(cur.getString(cur.getColumnIndex(NMConfig.CProducto.NOMBRE_COMERCIAL)));
			detalle.setNombreGenerico(cur.getString(cur.getColumnIndex(NMConfig.CProducto.NOMBRE_GENERICO)));
			detalle.setProveedor(cur.getString(cur.getColumnIndex(NMConfig.CProducto.PROVEEDOR)));
			detalle.setAccionFarmacologica(cur.getString(cur.getColumnIndex(NMConfig.CProducto.ACCION_FARMACOLOGICA)));
			detalle.setFormaFarmaceutica(cur.getString(cur.getColumnIndex(NMConfig.CProducto.FORMA_FARMACEUTICA)));
			detalle.setCategoria(cur.getString(cur.getColumnIndex(NMConfig.CProducto.CATEGORIA)));
			detalle.setCodigo(cur.getString(cur.getColumnIndex(NMConfig.CProducto.CODIGO)));
			detalle.setEspecialidades(cur.getString(cur.getColumnIndex(NMConfig.CProducto.ESPECIALIDADES)));
			detalle.setRegistro(cur.getString(cur.getColumnIndex(NMConfig.CProducto.REGISTRO)));
			detalle.setTipoProducto(cur.getString(cur.getColumnIndex(NMConfig.CProducto.TIPO_PRODUCTO)));
			CNota[] notas = getCNotasByProductoID(content,idproducto);
			detalle.setNotas(notas);
		}
		return detalle;
	}
	
	public synchronized static CNota[] getCNotasByProductoID(ContentResolver content,long ObjProductoID)throws Exception{
		int cont=0;
		
		String uriString = DatabaseProvider.CONTENT_URI_CNOTA+"/"+String.valueOf(ObjProductoID);
		Cursor cur = content.query(Uri.parse(uriString),
				null, // Columnas a devolver
				null, // Condición de la query
				null, // Argumentos variables de la query
				null);

		CNota[] notas=new CNota[cur.getCount()];
		if (cur.moveToFirst()) 
		{ 
			  do{ 
				  notas[cont]= new CNota(cur.getString(cur.getColumnIndex(NMConfig.CProducto.CNota.FECHA))
						  				,cur.getString(cur.getColumnIndex(NMConfig.CProducto.CNota.ELABORADO_POR)) 
						  				,cur.getString(cur.getColumnIndex(NMConfig.CProducto.CNota.TEXTONOTA))
						  				,cur.getString(cur.getColumnIndex(NMConfig.CProducto.CNota.CONCEPTO))
						  				,cur.getLong(cur.getColumnIndex(NMConfig.CProducto.CNota.PRODUCTOID)));
	        	  
		        	 cont++;
		           }while (cur.moveToNext());

		}
		return notas.length==0?null:notas;
	}


	//
	//	@Override
	//	public boolean handleMessage(Message msg){
	//		Boolean val=false;
	//		Bundle b = msg.getData();
	//		switch (msg.what) 
	//		{
	//			case LOAD_FICHAPRODUCTO_FROM_SERVER: 
	//				getFichaProductoByID(b.getLong("idProducto"));
	//			break;
	//		}
	//		return val;
	//	}
	//
	//
	//	public synchronized static void saveCProducto(final CProducto cproducto,Context context)throws Exception
	//	{
	//		DatabaseProvider.RegistrarDetalleProducto(cproducto,context); 
	//	}
	//
	//	public void getFichaProductoByID(final long idProducto)
	//	{
	//		try 
	//		{
	//			getPool().execute(  new Runnable()
	//			{
	//				@Override
	//				public void run() {
	//					try 
	//					{
	//						CProducto fichaproducto= null;
	//						final String credentials=SessionManager.getCredentials();			  
	//						if(credentials.trim()=="")
	//							return;	
	//						if(NMNetWork.isPhoneConnected(getContext()) && NMNetWork.CheckConnection(getController())){
	//							fichaproducto =getFichaProducto(credentials, idProducto);
	//							Processor.notifyToView(getController(),ID_SINCRONIZE_PRODUCTO,0,1,fichaproducto);
	//						}
	//
	//					}
	//					catch (Exception e) 
	//					{ 
	//						e.printStackTrace();
	//						try {
	//							Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
	//						} catch (Exception e1) { 
	//							e1.printStackTrace();
	//						} 
	//					}
	//				}
	//			});
	//		}
	//		catch(Exception e)
	//		{
	//			try {
	//				Processor.notifyToView(getController(),ERROR,0,1,new ErrorMessage("Error en la sincronización de clientes con el servidor",e.getMessage(),"\n Causa: "+e.getCause()));
	//			} catch (Exception e1) {
	//				e1.printStackTrace();
	//			}
	//		}
	//	}
	//
	//	public synchronized static CProducto getFichaProducto(String Credentials , long idproducto) throws Exception
	//	{
	//		Parameters params=new Parameters((new String[]{"Credentials","idProducto"}),
	//				(new Object[]{Credentials,idproducto}),
	//				(new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.LONG_CLASS}));
	//
	//		return  NMTranslate.ToObject(AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetCProducto),new CProducto());
	//	}
	//

}
