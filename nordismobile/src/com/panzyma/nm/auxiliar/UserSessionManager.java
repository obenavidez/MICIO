package com.panzyma.nm.auxiliar;

import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG2;

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
 
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class UserSessionManager {
        
    // EDITO REFERENCE FOR SHARED PREF 
	static Editor editor;
     
    // CONTEXT
    Context _context=NMApp.getContext();
     
     // SHARED PREF MODE
    static int PRIVATE_MODE = 0;
    
    
    // SHARED PREF FILE NAME
    private static final String PREFER_NAME = "SIMFAC_SESSION";
    
    // KEY SESSION
    private static final String USER_ID = "USER_ID";
    
    // SESSION STATUS
    private static final String IS_LOGGED_IN= "IS_LOGGED_IN"; 
    
    // TIEMPO INICIO DE SESSION
    private static final String START_SESSION_AT = "START_SESSION_AT";
    
    // SHARED PREF REFERENCES
    static SharedPreferences pref=NMApp.getContext().getSharedPreferences(PREFER_NAME, PRIVATE_MODE); 
    
    private static Usuario userinfo;
    
    // CREATE LONGIN SESSION
    public static Session guardarSession(Session session)
	{ 
		Usuario user=session.getUsuario();
		session.setStarted_session(System.currentTimeMillis());
		editor = pref.edit();
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

    
    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public static  boolean checkLogin(String username,String password){
        // Check login status
        if(userinfo!=null || getLoginUser()!=null)
        {        	
        	if(userinfo.getLogin().equals(username) && userinfo.getPassword().equals(password))
        		return true;
        } else {
        	return UserSessionManager.login(false, new String[]{username,password});
        }
        return false;
    } 
    
    public  static boolean login(final boolean admin, String... credentials)
	{
		final String empresa= SessionManager.getEmpresa();
		String errormessage="";
		final String nombreUsuario = credentials[0] ;
		final int AUT_EXITOSA = 1;
		final String password = credentials[1];  
		SessionManager.setLogged(false); 
		SessionManager.setErrorAuntentication("");		
		final String url= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer():NMConfig.URL; 
		final String url2= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer2():NMConfig.URL2; 
		boolean hasError=false;
		final Usuario user = SessionManager.getLoginUser();
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
										

										
																				
										if( NMApp.modulo == Modulo.HOME && user != null && user.getPassword().trim().length() > 0 && !admin ) {
											NMApp.tipoAutenticacion = AutenticationType.LOCAL;
										}

										
										try 
										{
											if( NMApp.tipoAutenticacion == AutenticationType.REMOTE) {
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
															SessionManager.setEmpresa(empresa);
															SessionManager.setNameUser(nombreUsuario);
															SessionManager.setPassword(password);															
															SessionManager.setLogged(true);	
															
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

		}
	    catch (Exception e) {  
			e.printStackTrace();			
		} 
		return SessionManager.isLogged();

 	} 
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
         
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
}
