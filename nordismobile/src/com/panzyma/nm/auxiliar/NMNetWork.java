package com.panzyma.nm.auxiliar;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR; 
import java.util.ArrayList;

import org.ksoap2.serialization.SoapPrimitive; 
import com.panzyma.nm.controller.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

@SuppressWarnings("rawtypes")
public class NMNetWork {
    /*
     * Estas constantes no estan disponibles en mi API level (7), 
     *          pero se necesita manejar estos casos por si la aplicacion 
     *          se extiende o actualiza a nuevas versiones
     */
	static ErrorMessage error;
	
    public static final int NETWORK_TYPE_EHRPD=14; // Level 11
    public static final int NETWORK_TYPE_EVDO_B=12; // Level 9
    public static final int NETWORK_TYPE_HSPAP=15; // Level 13
    public static final int NETWORK_TYPE_IDEN=11; // Level 8
    public static final int NETWORK_TYPE_LTE=13; // Level 11

    public static ErrorMessage getError()
    {
    	return error;
    }
    /**
     * Verificar si hay alguna red activa, ya sea WIFI o MOBILE.
     * @param context
     * @return
     * @throws Exception 
     */
    public static boolean isPhoneConnected(Context context,Controller controller){
		try 
		{	
    		error=null;
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo(); 
			 if(info== null)
				 error=new ErrorMessage("Error de conexión"," Dispositivo Fuera de linea","\n Causa: No hay ninguna conexion activa");
	        else if(!info.isConnected()) 
	        	error=new ErrorMessage("Error de conexión"," Dispositivo Fuera de linea","\n Causa: El dispositivo no esta conectado a ninguna conexión activa");      	 
	        if(error!=null && controller!=null)
	        {
				try { 
						Processor.notifyToView(controller,ERROR,0,0,error);
				} catch (Exception e) { 
					e.printStackTrace();
				}
	        }
	        return (info != null && info.isConnected());
		}catch (Exception e) { 
			e.printStackTrace();
		}
       
        
        return false;
    }    
    
    //Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static boolean CheckConnection(Controller controller) 
    {
    	error=null;    	
        try { 
        		return Boolean.parseBoolean(((SoapPrimitive)NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.CheckConnection)).toString());              
        } 
        catch(Exception ex) 
        {         	 
        	error=new ErrorMessage("Error de conexión","error en la comunicación con el servidor de aplicaciones.\n",ex.toString());
        	try {
        		if(controller!=null)
        			Processor.notifyToView(controller,ERROR,0,0,error);				 
			} catch (Exception e) { 
				e.printStackTrace();
			}
        	
            return false;
        }  
    }    
    
    /**
     * Verificar la velocidad de la Red Activa
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && NMNetWork.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Verificar la velocidad de la Red Activa
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            System.out.println("CONNECTED VIA WIFI");
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            // NOT AVAILABLE YET IN API LEVEL 7
            case NMNetWork.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case NMNetWork.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case NMNetWork.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case NMNetWork.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps 
            case NMNetWork.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps 
            // Unknown
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return false;
            }
        }else{
            return false;
        }
    }    
    
    /**
     * Obtener el Tipo de Conexion
     * @param type
     * @param subType
     * @return
     */
    public String getNetWorkType(int networkType)
    {
    	switch (networkType) 
    	{
    		case TelephonyManager.NETWORK_TYPE_1xRTT:break;
    		case TelephonyManager.NETWORK_TYPE_CDMA:break;
    		case TelephonyManager.NETWORK_TYPE_EDGE:break;
    		case TelephonyManager.NETWORK_TYPE_EVDO_0:break;
    		case TelephonyManager.NETWORK_TYPE_EVDO_A:break;
    		case TelephonyManager.NETWORK_TYPE_EVDO_B:break;
    		case TelephonyManager.NETWORK_TYPE_GPRS:break;
    		case TelephonyManager.NETWORK_TYPE_HSDPA:break;
    		case TelephonyManager.NETWORK_TYPE_HSPA:break;
    		case TelephonyManager.NETWORK_TYPE_HSUPA:break;
    		case TelephonyManager.NETWORK_TYPE_IDEN:break;
    		case TelephonyManager.NETWORK_TYPE_UMTS:break; 
    	}
    	
    	return new String();
    }
    
    public static String  getDeviceId(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    	
    }
}