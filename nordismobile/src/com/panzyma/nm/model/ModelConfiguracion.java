package com.panzyma.nm.model;

import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import android.content.Context;
import android.content.SharedPreferences; 
import android.content.SharedPreferences.Editor;

import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
/*import com.panzyma.nm.auxiliar.Parameters;*/  
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.DataConfigurationResult;
import com.panzyma.nm.serviceproxy.LoginUserResult;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.viewmodel.vmConfiguracion;

public class ModelConfiguracion {

	static SharedPreferences pref;
	static SharedPreferences.Editor edit;
	 
	public ModelConfiguracion(){}
	
	public  static Usuario getDatosUsuario(String Credentials,String LoginUsuario) throws Exception
	{
		Parameters params=new Parameters((new String[]{"Credentials","LoginUsuario"}),
				 (new Object[]{Credentials,LoginUsuario}),
				 (new Type[]{PropertyInfo.STRING_CLASS,PropertyInfo.STRING_CLASS}));
		return  (NMTranslate.ToObject(( AppNMComunication.InvokeMethod(params.getParameters(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.GetDatosUsuario)),new Usuario()));		
    }
	
	public  static int get_DEVISE_PREFIX(String Credentials,String PIN) throws Exception
	{
		return (AppNMComunication.InvokeService(NMConfig.URL2+NMConfig.MethodName.GetDevicePrefix+"/"+Credentials+"/"+PIN)).getInt("getDevicePrefixResult");
    }
	
	public static vmConfiguracion getVMConfiguration(Context cnt)
	{ 
		pref=cnt.getSharedPreferences("VConfiguracion",Context.MODE_PRIVATE);  
		
		return vmConfiguracion.setConfiguration(
										 pref.getString("url_server",NMConfig.URL_SERVER), 
										 pref.getString("device_id",""),
										 pref.getString("enterprise","dp"), 
										 pref.getString("name_user",""), 
										 pref.getInt("max_idpedido",0),
										 pref.getInt("max_idrecibo",0));
	}
	
	public static int getMaxReciboID(Context cnt)
	{
		pref=cnt.getSharedPreferences("VConfiguracion",Context.MODE_PRIVATE);  		
		if(pref==null)
			return 0;
		return pref.getInt("max_idrecibo",0); 
	}
	
	public static int getDeviceID(Context cnt)
	{
		pref=cnt.getSharedPreferences("VConfiguracion",Context.MODE_PRIVATE);  		
		if(pref==null)
			return 0;
		return Integer.parseInt(pref.getString("device_id","0")); 
	}
	
	public static void setMaxReciboId(Context cnt, int value) 
	{
		Editor e=cnt.getSharedPreferences("VConfiguracion",Context.MODE_PRIVATE).edit();  
		e.putInt("max_idrecibo", value);
		e.commit();
	}
	
	public static LoginUserResult verifyLogin(String Credentials,String Roll) throws Exception
	{
		return NMTranslate.ToObject(AppNMComunication.InvokeService(NMConfig.URL2+NMConfig.MethodName.LoginUser+"/"+Credentials+"/"+Roll),new LoginUserResult());		 
	}
	
	public static DataConfigurationResult getDataConfiguration(String Credentials,String LoginUsuario,String PIN) throws Exception
	{
		PIN="21C5D535";
		return NMTranslate.ToObject(AppNMComunication.InvokeService(NMConfig.URL2+NMConfig.MethodName.getDataConfiguration+"/"+Credentials+"/"+LoginUsuario+"/"+PIN),new DataConfigurationResult());
	}
	
	public static void saveConfiguration(Context view,vmConfiguracion setting)throws Exception
	{
		pref=view.getSharedPreferences("VConfiguracion",Context.MODE_PRIVATE); 
		edit=pref.edit();		
		edit.putString("url_server",setting.getAppServerURL());
		edit.putString("device_id",setting.getDeviceId());
		edit.putString("enterprise",setting.getEnterprise());
		edit.putString("name_user",setting.getNameUser()); 
		edit.putInt("max_idpedido",setting.getMax_IdPedido());
		edit.putInt("max_idrecibo",setting.getMax_Idrecibo());
		edit.commit();		
	}
	
	public static int ActualizarSecuenciaPedido(Context cnt, int idpedido){
		pref=cnt.getSharedPreferences("VConfiguracion",Context.MODE_PRIVATE); 
		edit=pref.edit();		 
		edit.putInt("max_idpedido",idpedido); 
		edit.commit();
		return 1;
	}
	
	
	public static Usuario getUser(Context view)
	{ 
		pref=view.getSharedPreferences("LoginUser",Context.MODE_PRIVATE);  
		if(pref.getLong("id",0)==0)
			return null;
		else
			return new Usuario(
			pref.getLong("id",0), 
			pref.getString("login",""),
			pref.getString("nombre",""),
			pref.getString("sexo",""),
			pref.getBoolean("isAccedeModuloPedidos",false),
			pref.getBoolean("isIsAdmin",false),
			pref.getBoolean("isPuedeConsultarPedido",false),
			pref.getBoolean("isPuedeCrearPedido",false),
			pref.getBoolean("isPuedeEditarBonifAbajo",false),
			pref.getBoolean("isPuedeEditarBonifArriba",false),
			pref.getBoolean("isPuedeEditarDescPP",false),
			pref.getBoolean("isPuedeEditarPrecioAbajo",false),
			pref.getString("codigo",""),
			pref.getBoolean("isPuedeEditarPrecioArriba",false)
		);
	}
	
	
	public static void saveUser(Context view,Usuario user)throws Exception
	{
		pref=view.getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
		edit=pref.edit();		
		edit.putString("codigo",user.getCodigo());
		edit.putString("login",user.getLogin());
		edit.putString("nombre",user.getNombre());
		edit.putString("sexo",user.getSexo());
		edit.putLong("id",user.getId());
		edit.putBoolean("isAccedeModuloPedidos",user.isAccedeModuloPedidos());
		edit.putBoolean("isIsAdmin",user.isIsAdmin());
		edit.putBoolean("isPuedeConsultarPedido",user.isPuedeConsultarPedido());
		edit.putBoolean("isPuedeCrearPedido",user.isPuedeCrearPedido());
		edit.putBoolean("isPuedeEditarBonifAbajo",user.isPuedeEditarBonifAbajo());
		edit.putBoolean("isPuedeEditarBonifArriba",user.isPuedeEditarBonifArriba());
		edit.putBoolean("isPuedeEditarDescPP",user.isPuedeEditarDescPP());
		edit.putBoolean("isPuedeEditarPrecioAbajo",user.isPuedeEditarPrecioAbajo());
		edit.putBoolean("isPuedeEditarPrecioArriba",user.isPuedeEditarPrecioArriba());
		
		edit.commit();		
	}
  
	public static JSONArray getSystemPerams(String Credentials) throws Exception{		
		return AppNMComunication.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetParams+"/"+Credentials);
	}
	
 
	public static JSONArray getValoresCatalogo(String Credentials,String NombresCatalogo) throws Exception{		
		return AppNMComunication.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetValoresCatalogo+"/"+Credentials+"/"+NombresCatalogo);
	}
	
	public static JSONArray getTasasDeCambio(String Credentials)throws Exception{
		return AppNMComunication.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetTasasDeCambio+"/"+Credentials);
	}
	
 
	
	public static JSONArray getPromocionesPaged(String Credentials,String LoginUsuario,int Pages,int RowPages)throws Exception
	{ 
		return  AppNMComunication.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetPromocionesPaged+"/"+Credentials+"/"+LoginUsuario+"/"+Pages+"/"+RowPages);
    }
	
	public static void saveSystemParam(Context view,JSONArray params)throws Exception
	{
		pref=view.getSharedPreferences("SystemParams",Context.MODE_PRIVATE);
		edit=pref.edit();
		for(int i=0; i<params.length();i++)
		{
			JSONObject obj=params.getJSONObject(i);
			edit.putString(obj.getString("Nombre"),obj.getString("Valor"));		    
		}
		edit.commit();	
		
	} 
	
	public synchronized static void saveValorCatalogoSystem(Context view,JSONArray objL)throws Exception
	{
		DatabaseProvider.RegistrarCatalogos(objL,view);		
	}
	
	public synchronized static void savePromociones(Context view,JSONArray objL,int page)throws Exception
	{
		DatabaseProvider.RegistrarPromociones(objL,view,page);		
	}
	
	public synchronized static void saveTasasDeCambio(Context view,JSONArray objL)throws Exception
	{
		DatabaseProvider.RegistrarTasaCambios(objL,view);		
	}
}
