package com.panzyma.nm.auxiliar;

import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG2;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import com.panzyma.nm.LoginScreen; 
import com.panzyma.nm.Main;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.NMApp.Modulo;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.LoginUserResult;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
 




import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SyncResult;
 
public class UserSessionManager 
{
	
	private static final int AUT_EXPIRES=60;
        
	private static final int AUT_FALLIDA = 0; 
	
    private static final int AUT_EXITOSA = 1; 
    
    private static final int AUT_USER_NO_EXIST = 2; 
    
    private static final int AUT_PWD_INVALIDO = 3;  
	
	//EMPRESA
	private static  String empresa = "dp";
	
	//NOMBRE DE USUARIO DEL SISTEMA
	private static  String nameuser; 
	
	//PASSWORD DEL USUARIO DEL SISTEMA
	private static  String password;  
	
	 // SESSION STATUS
    private static final String IS_LOGGED_IN= "IS_LOGGED_IN"; 
    
     
    // CONTEXT
    public static Context _context = NMApp.getContext();
     
     // SHARED PREF MODE
    static int PRIVATE_MODE = 0;
    
    public static boolean isValidCredentials = false;
    
    
    // SHARED PREF FILE NAME
    private static final String PREFER_NAME = "SIMFAC_SESSION";
    
    // KEY SESSION
    private static final String USER_ID = "USER_ID";
    
    static Object lock=new Object();
    
    // TIEMPO INICIO DE SESSION
    private static final String START_SESSION_AT = "START_SESSION_AT";
    
    // SHARED PREF REFERENCES
    static SharedPreferences pref=NMApp.getContext().getSharedPreferences(PREFER_NAME, PRIVATE_MODE); 
    
 // EDITO REFERENCE FOR SHARED PREF 
 	static Editor editor=pref.edit();
    
    private static Usuario userinfo;
    
    
    public static String getNameUser()
	{
		return nameuser;
	}
	
	public static void setNameUser(String _nameuser)
	{
		nameuser=_nameuser;
	}
	
	public static String getPassword()
	{
		return password;
	}
	
	public static void setPassword(String _password)
	{
		password=_password;
	}
	
	public static String getEmpresa()
	{
		return empresa;
	}    
    
    // CREATE LONGIN SESSION
    public static Session guardarSession(Session session)
	{ 
		Usuario user=session.getUsuario();  
		session.setStarted_session(Long.valueOf(new SimpleDateFormat("hhmmss").format(Calendar.getInstance().getTime())));
		editor.putLong("USER_ID", user.getId());
		editor.putBoolean("IS_LOGGED_IN",session.isLoged());
		editor.putLong("START_SESSION_AT",session.getStarted_session()); 
		editor.commit();
		return session;
	}
     
    public static Session getSession(){ 
    	
    	if(pref.getLong("USER_ID",0)==0)
    		return null;
    	Session session=new Session(); 
    	session.setLoged(pref.getBoolean("IS_LOGGED_IN",false));
    	session.setStarted_session(pref.getLong("START_SESSION_AT",0)); 
		return session;
    }
    
    public static String getCredenciales(){ 
		Usuario user = UserSessionManager.getLoginUser(); 
		nameuser = user.getLogin();
		password = user.getPassword();
		empresa = SessionManager.getEmpresa();
		if(nameuser==null || password==null || empresa==null ||(nameuser!=null && nameuser.trim()=="") 
        		|| (password!=null && password.trim()=="") || (empresa!=null && empresa.trim()==""))
        	return "";
        else
        	return nameuser + "-" + password + "-" + empresa; 
	} 
	public static String getCredentials()
	{		
		Usuario user = UserSessionManager.getLoginUser(); 
		nameuser = user.getLogin();
		password = user.getPassword();
		empresa = SessionManager.getEmpresa();
        if(nameuser==null || password==null || empresa==null ||(nameuser!=null && nameuser.trim()=="") 
        		|| (password!=null && password.trim()=="") || (empresa!=null && empresa.trim()==""))
        	return "";
        else
        	return nameuser + "||" + password + "||" + empresa;
	}

    
    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public static  boolean checkLogin(final String username,final String _password){
        // Check login status
    	UserSessionManager.isValidCredentials=false;
        if(userinfo!=null || getLoginUser()!=null)
        {        	
        	if(userinfo.getLogin().equals(username) && userinfo.getPassword().equals(_password))
        		return UserSessionManager.isValidCredentials=true;
        } else 
        {

			while(!UserSessionManager.isValidCredentials)
			{
	    		((Activity) _context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						UserSessionManager.login(false, new String[]{username,_password});
					}
				});
				synchronized(lock)
	            {
	                try {
	                	lock.wait();
	    			} catch (InterruptedException e) { 
	    				e.printStackTrace();
	    			}
	            }  
			}        	
			      	
        }
        return UserSessionManager.isValidCredentials;
    } 
    
    public synchronized static boolean login(final boolean admin, String... credentials)
	{
		final String empresa=getEmpresa();
		String errormessage="";
		final String nombreUsuario = credentials[0] ;
		final String password = credentials[1];   
		UserSessionManager.isValidCredentials = false;
		final String url= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer():NMConfig.URL; 
		final String url2= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer2():NMConfig.URL2; 
		
		boolean hasError=false; 
		try 
		{ 							
			NMApp.getThreadPool().execute(new Runnable()
			{
				@Override
				public void run() 
				{ 
								
					if((NMNetWork.isPhoneConnected() && NMNetWork.CheckConnection(url)))			
					{
						try 
						{
							NMApp.getThreadPool().execute(new Runnable()
								{
									@Override
									public void run()
									{ 										
										LoginUserResult res; 
										
										try 
										{
											if( NMApp.tipoAutenticacion == AutenticationType.REMOTE) 
											{
												// AUTENTICACION REMOTA
												res =ModelConfiguracion.verifyLogin(url2,(nombreUsuario+"-"+password+"-"+empresa),"ADMIN");
												
												if (res.getAuntenticateRS() == AUT_EXITOSA )
												{				
													BConfiguracionM.GET_DATACONFIGURATION(url,url2,empresa, 
															nombreUsuario+"-"+password+"-"+empresa,
															nombreUsuario,
															NMNetWork.getDeviceId(NMApp.getContext()),
															SessionManager.getImpresora());
															
															boolean _esAdmin=res.IsAdmin();															
															
															UserSessionManager.setNameUser(nombreUsuario);
															UserSessionManager.setPassword(password);															
															UserSessionManager.isValidCredentials = true;	
															
															Usuario user = SessionManager.getLoginUser();
															if( ( user != null ) &&
																( user.getPassword() == null ||
																  ( user.getPassword() != null &&
																    user.getPassword().trim().length() == 0) 
																  )
															   ) {
																
																//SessionManager.setLogged(true);
																user.setPassword(password);
																user.setIsAdmin(admin);
																Usuario.guardarInfoUsuario(NMApp.getContext(), user);
																NMApp.tipoAutenticacion = AutenticationType.LOCAL;
															
															}														
														}
													}
											unlock();
											
										}catch (Exception e) {
											
											e.printStackTrace();
										}
									}
									
								});
								
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}  
						NMApp.getController()._notifyOutboxHandlers(NOTIFICATION_DIALOG2, 0, 0, "Validando Credenciales."); 
							
					}
					
				 }
			});	  
			NMApp.getController()._notifyOutboxHandlers(NOTIFICATION_DIALOG2, 0, 0,"Probando Conexión.");   
		} catch (Exception e) {  
			e.printStackTrace();			
		} 
		return SessionManager.isLogged();

 	} 
    
    public static void unlock()
	{
		NMApp.getController()._notifyOutboxHandlers(0,0,0,0);
		((Activity) _context).runOnUiThread(new Runnable()
	     {
	            @Override
				public void run()
	            {
	            	synchronized(lock)
	            	{                             
	            			lock.notify();
                    }
	            }
	      }); 
	} 
     
    /**
     * Clear session details
     * */
    public static void logoutUser(){
         
        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginScreen.class);
         
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
//     
    public static Usuario getLoginUser()
	{
		if(userinfo==null)
			userinfo=Usuario.get();
		return userinfo;
	}
    // Check for login
    public static boolean isUserLoggedIn()
    {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }
    
    // Check for login
    @SuppressLint("SimpleDateFormat") 
    public static boolean isSessionExpires()
    {
    	long initTime=pref.getLong(START_SESSION_AT, 0);
    	long endTime=Long.valueOf(new SimpleDateFormat("hhmmss").format(Calendar.getInstance().getTime())); 
    	if(initTime-endTime>AUT_EXPIRES)
    		return true;

    	return false;
    }
    
    
    
    
}
