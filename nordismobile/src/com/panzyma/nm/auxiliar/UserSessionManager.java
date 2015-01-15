package com.panzyma.nm.auxiliar;

import java.util.HashMap;

import com.panzyma.nm.Main;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.serviceproxy.Usuario;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
public class UserSessionManager {
     
     // SHARED PREF REFERENCES
	
     
    // EDITO REFERENCE FOR SHARED PREF 
	static Editor editor;
     
    // CONTEXT
    Context _context;
     
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
    
    static SharedPreferences pref=NMApp.getContext().getSharedPreferences(PREFER_NAME, PRIVATE_MODE); 
    
    private static Usuario userinfo;
    
    // CREATE LONGIN SESSION
    public static Session guardarSession(Session session)
	{ 
		Usuario user=session.getUsuario();
		session.setStarted_session(System.currentTimeMillis());
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
        	if(userinfo.getLogin()==username && userinfo.getPassword()==password)
        		return true;
        }
        return false;
    }
     
     
     
//    /**
//     * Get stored session data
//     * */
//    public HashMap<String, String> getUserDetails(){
//         
//        //Use hashmap to store user credentials
//        HashMap<String, String> user = new HashMap<String, String>();
//         
//        // user name
//        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
//         
//        // user email id
//        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
//         
//        // return user
//        return user;
//    }
     
//    /**
//     * Clear session details
//     * */
//    public void logoutUser(){
//         
//        // Clearing all user data from Shared Preferences
//        editor.clear();
//        editor.commit();
//         
//        // After logout redirect user to Login Activity
//        Intent i = new Intent(_context, LoginActivity.class);
//         
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//         
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//         
//        // Staring Login Activity
//        _context.startActivity(i);
//    }
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
