package com.panzyma.nm.auxiliar;


import static com.panzyma.nm.controller.ControllerProtocol.ERROR; 

import java.util.ArrayList;

import org.ksoap2.serialization.SoapPrimitive; 

import com.comunicator.Parameters;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.view.ViewConfiguracion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
     * @param parent
     * @return
     * @throws Exception 
     */
    
    public static boolean isPhoneConnected3()
    {
		try 
		{	
	        ConnectivityManager cm = (ConnectivityManager) NMApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo(); 
			 if(info== null) 
				 return false; 
	        else if(!info.isConnected())  
        		return false;  
		}catch (Exception e) { 
			e.printStackTrace();
			return false;
		}        
        return true;
    }  
    
    /**
     * Verificar si hay alguna red activa, ya sea WIFI o MOBILE.
     * @param parent
     * @return
     * @throws Exception 
     */
    
    public static boolean isPhoneConnected()
    {
    	try 
		{	
    		error=null;
	        ConnectivityManager cm = (ConnectivityManager) NMApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo(); 
			 if(info== null)
			 {
				 error=new ErrorMessage("Error de conexión"," Dispositivo Fuera de linea","\n Causa: No hay ninguna conexion activa");
			 }
	        else if(!info.isConnected()) 
        	{
        		error=new ErrorMessage("Error de conexión"," Dispositivo Fuera de linea","\n Causa: El dispositivo no esta conectado a ninguna conexión activa");      	 
        	}
	        if(error!=null)
	        {
				try 
				{ 
						UserSessionManager.HAS_ERROR=true;
						SessionManager.hasError=true;
						SessionManager.setErrorAuntentication(error.getTittle()+"\n\t\t"+error.getMessage());
						Thread.sleep(1000);
						NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,error); 
						return false;
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
    
    public static boolean isPhoneConnected(Context context){
		try 
		{	
    		error=null;
	        ConnectivityManager cm = (ConnectivityManager) NMApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo(); 
			 if(info== null)
			 {
				 error=new ErrorMessage("Error de conexión"," Dispositivo Fuera de linea","\n Causa: No hay ninguna conexion activa");
			 }
	        else if(!info.isConnected()) 
        	{
        		error=new ErrorMessage("Error de conexión"," Dispositivo Fuera de linea","\n Causa: El dispositivo no esta conectado a ninguna conexión activa");      	 
        	}
	        if(error!=null)
	        {
				try 
				{ 
						UserSessionManager.HAS_ERROR=true;
						SessionManager.hasError=true;
						SessionManager.setErrorAuntentication(error.getTittle()+"\n\t\t"+error.getMessage());
						Thread.sleep(1000);
						NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,error); 
						return false;
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
    
    /**
     * Verificar si hay alguna red activa, ya sea WIFI o MOBILE.
     * @param context
     * @return
     * @throws Exception 
     */
//    public static boolean isPhoneConnected(Context context){
//		try 
//		{	
//    		error=null;
//	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//	        NetworkInfo info = cm.getActiveNetworkInfo(); 
//			return (info != null && info.isConnected());
//		}catch (Exception e) { 
//			e.printStackTrace();
//		}
//       
//        
//        return false;
//    }    
    
    static boolean response = false;
    static Object lock = new Object();
	
	public static synchronized void consultaConeccion() {
		try {
			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						response = Boolean.parseBoolean(((SoapPrimitive)NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.CheckConnection)).toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					synchronized (lock) {
						lock.notify();
					}					
				}
			});		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	static class ConsultaConexionTask extends AsyncTask<Void, Void, Boolean> {

	    private Exception exception;
	    
	    @Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	    @Override
	    protected Boolean doInBackground(Void... params) {
	        try {
	        	return Boolean.parseBoolean(((SoapPrimitive)NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),NMConfig.URL,NMConfig.NAME_SPACE,NMConfig.MethodName.CheckConnection)).toString());
	        } catch (Exception e) {
	            this.exception = e;
	            return null;
	        }
	    }

	    @Override
	    protected void onPostExecute(Boolean r) {
	        response = r;
	        lock.notify();
	    	super.onPostExecute(r);
	    }		
	}
	
    //Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static synchronized boolean CheckConnection(Controller controller) 
    {
    	error=null;  
        try 
        {        
			Thread t = new Thread(new Runnable() 
			{
				@Override
				public void run() {
					try 
					{
						response = Boolean.parseBoolean(((SoapPrimitive) NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),
										NMConfig.URL, NMConfig.NAME_SPACE,
										NMConfig.MethodName.CheckConnection))
								.toString());
					} catch (Exception ex) {
						ex.printStackTrace();    	 
		            	UserSessionManager.HAS_ERROR=true;
		            	try 
		    			{				
		    				NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,new ErrorMessage("","error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""),"error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""))); 
		    			} catch (Exception e) { 
		    				e.printStackTrace();
		    			}	  
					}
				}
			});
        	t.start(); // spawn thread
        	try {
        		t.join();  // wait for thread to finish
            } catch (InterruptedException ex) {
            	ex.printStackTrace();    	 
            	UserSessionManager.HAS_ERROR=true;
            	try 
    			{				
    				NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,new ErrorMessage("","error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""),"error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""))); 
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}	  
                return false; 
            }        	
			/*NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					consultaConeccion();					
				}
			});	
			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});*/	
        } 
        catch(Exception ex) 
        {         	 
        	ex.printStackTrace();    	 
        	UserSessionManager.HAS_ERROR=true;
        	try 
			{				
				NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,new ErrorMessage("","error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""),"error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""))); 
			} catch (Exception e) { 
				e.printStackTrace();
			}	  
            return false;
        }
		return response;  
    }    
    
  //Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static synchronized void CheckConnection(final int what) 
    {
    	error=null;  
        try 
        {        
        	NMApp.getThreadPool().execute(new Runnable() 
			{
				@Override
				public void run() {
					try 
					{
						response = Boolean.parseBoolean(((SoapPrimitive) NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),
										NMConfig.URL, NMConfig.NAME_SPACE,
										NMConfig.MethodName.CheckConnection)).toString());
						
						NMApp.getController()._notifyOutboxHandlers(what, 0, 0,response);
						
					} catch (Exception ex) {
						ex.printStackTrace();    	 
		            	UserSessionManager.HAS_ERROR=true;
		            	try 
		    			{				
		            		NMApp.getController()._notifyOutboxHandlers(what, 0, 0,false); 
		    			} catch (Exception e) { 
		    				e.printStackTrace();
		    			}	  
					}
				}
			}); 				
			 Processor.notifyToView(
					 NMApp.getController(),
						ControllerProtocol.NOTIFICATION_DIALOG2,
						0,
						0,new String("Probando conexión."));	 
        } 
        catch(Exception ex) 
        {         	 
        	ex.printStackTrace();    	 
        	UserSessionManager.HAS_ERROR=true;
        	NMApp.getController()._notifyOutboxHandlers(what, 0, 0,false);    
        } 
    }    
    
  //Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static synchronized boolean CheckConnection() 
    {
    	error=null; 
    	response=false;
    	UserSessionManager.HAS_ERROR=false;
        try 
        {        
			Thread t = new Thread(new Runnable() 
			{
				@Override
				public void run() {
					try 
					{
						response = Boolean.parseBoolean(((SoapPrimitive) NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),
										NMConfig.URL, NMConfig.NAME_SPACE,
										NMConfig.MethodName.CheckConnection))
								.toString());
					} catch (Exception ex) {
						ex.printStackTrace();    	 
		            	UserSessionManager.HAS_ERROR=true;
		            	try 
		    			{				
		    				NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,new ErrorMessage("","error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""),"error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""))); 
		    			} catch (Exception e) { 
		    				e.printStackTrace();
		    			}	  
		            	
					}
				}
			});
        	t.start(); // spawn thread
        	try {
        		t.join();  // wait for thread to finish
            } catch (InterruptedException ex) 
            {
            	ex.printStackTrace();    	 
            	UserSessionManager.HAS_ERROR=true;
            	try 
    			{				
    				NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,new ErrorMessage("","error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""),"error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""))); 
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}	  
                return false; 
            }        	
			/*NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					consultaConeccion();					
				}
			});	
			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});*/	
        } 
        catch(Exception ex) 
        {         	 
        	ex.printStackTrace();    	 
        	UserSessionManager.HAS_ERROR=true;
        	try 
			{				
				NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,new ErrorMessage("","error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""),"error en la comunicación con el servidor de aplicaciones.\n"+((ex!=null)?ex.toString():""))); 
			} catch (Exception e) { 
				e.printStackTrace();
			}	  
            return false;
        }
		return response;  
    }    
    
    
    
    
    
    //Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static synchronized boolean CheckConnectionV2() 
    {
    	error=null; 
    	response=false;
    	UserSessionManager.HAS_ERROR=false;
        try 
        {        
			Thread t = new Thread(new Runnable() 
			{
				@Override
				public void run() {
					try 
					{
						response = Boolean.parseBoolean(((SoapPrimitive) NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),
										NMConfig.URL, NMConfig.NAME_SPACE,
										NMConfig.MethodName.CheckConnection))
								.toString());
					} catch (Exception ex) {
						ex.printStackTrace();    	 
		            	UserSessionManager.HAS_ERROR=true; 
					}
				}
			});
        	t.start(); // spawn thread
        	try {
        		t.join();  // wait for thread to finish
            } catch (InterruptedException ex) 
            {
            	ex.printStackTrace();    	 
            	UserSessionManager.HAS_ERROR=true;
            	
                return false; 
            }        	 
        } 
        catch(Exception ex) 
        {         	 
        	ex.printStackTrace();    	 
        	UserSessionManager.HAS_ERROR=true;
         
            return false;
        }
		return response;  
    }    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  //Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static boolean CheckConnection(String url) 
    {
    	UserSessionManager.HAS_ERROR=false;
    	boolean rs=false;
    	error=null;    	
        try 
        { 
        		rs= Boolean.parseBoolean(((SoapPrimitive)NMComunicacion.InvokeMethod(new ArrayList<Parameters>(),url,NMConfig.NAME_SPACE,NMConfig.MethodName.CheckConnection)).toString());
        		        
        } 
        catch(Exception ex) 
        {         
        	rs=false;
        	UserSessionManager.HAS_ERROR=true;
        	if(UserSessionManager.getLoginUser()==null && !(UserSessionManager._context instanceof ViewConfiguracion))
        		NMApp.getController()._notifyOutboxHandlers(ControllerProtocol.SETTING_REDIREC, 0, 0,0);
        	else
        		NMApp.getController()._notifyOutboxHandlers(ERROR, 0, 0,new ErrorMessage("","error en la comunicación con el servidor de aplicaciones.\n"+ex.toString(),"error en la comunicación con el servidor de aplicaciones.\n"+ex.toString()));
        }
        return rs;
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