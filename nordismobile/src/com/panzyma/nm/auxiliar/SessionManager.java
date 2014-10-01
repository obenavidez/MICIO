package com.panzyma.nm.auxiliar; 
 

import android.annotation.SuppressLint;
import android.app.Activity;   
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;   
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewConfiguration;

import com.panzyma.nm.Main;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.DataConfigurationResult;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.serviceproxy.LoginUserResult;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.viewdialog.DialogLogin; 
import com.panzyma.nm.viewdialog.DialogLogin.OnButtonClickListener;  

import static com.panzyma.nm.controller.ControllerProtocol.LOAD_SETTING;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG2;

@SuppressLint("ParserError")@SuppressWarnings("rawtypes")
public class SessionManager
{
	private static  String empresa;
	private static  String nameuser; 
	private static  String password; 
	private static  boolean islogged;
	
	private static  Activity context;
	
    private static final int AUT_FALLIDA = 0; 
    private static final int AUT_EXITOSA = 1; 
    private static final int AUT_USER_NO_EXIST = 2; 
    private static final int AUT_PWD_INVALIDO = 3;  
    
    private static String errormessage=""; 
    private static boolean isOK;
    private static boolean _esAdmin; 
    static Object lock=new Object();
    static Object lock2=new Object(); 
    
    public static DialogLogin  dl;
	private static ThreadPool pool;
	private static CustomDialog dlg; 
    private static Usuario userinfo;
    public static boolean hasError=false;
    static String nombreusuario="";
	public SessionManager(){};
	
	private static Impresora impresora;
	
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
	
	public static Impresora obtenerImpresora()
	{
		return ModelConfiguracion.obtenerImpresora(context);
	}
	
	public static void setEmpresa(String _empresa)
	{
		empresa=_empresa;
	}
	
	public static boolean isAdmin() 
	{
	       return _esAdmin;
    }	 
	
	public static Context getContext(){
		return context;
	}
	
	@SuppressWarnings("static-access")
	public static Impresora getImpresora(){
		if(impresora==null || (impresora!=null && impresora.obtenerMac().trim()==""))
			impresora=Impresora.get(context);
		return impresora;
	}
	
	public static void setImpresora(Impresora dispositivo){
		impresora=dispositivo;
	}
	public static Usuario getLoginUser()
	{
		if(userinfo==null)
			userinfo=Usuario.get(context);
		return userinfo;
	}
	
	public static String[] getSession()
	{
		if(getEmpresa()==null || getNameUser()==null || (getEmpresa()!=null && getEmpresa()=="") || (getNameUser()!=null && getNameUser()==""))
			return ModelConfiguracion.getVariablesSession(context);
		else		
			return new String[]{getEmpresa(),getNameUser()};
			
	}

	public static void setLoguedUser(Usuario user){
		userinfo=user;
	}
	
	public static void setContext(Activity _context)
	{
		context=_context; 
		if(pool==null)
			pool =NMApp.getThreadPool();
	} 
	
	public  static void setLogged(boolean value){
		islogged=value;
	}
	
	public  static boolean isLogged(){
		return islogged;
	}
	
	public static String getCredenciales(){
		
		if (!islogged)
		{			
			if (NMApp.modulo != NMApp.Modulo.CONFIGURACION) 
			{
				if(!SessionManager.SignIn(false))
					return "";  
					
			}else if (!SessionManager.SignIn(true))
				return "";
		} 
		else
		{		
			if(SessionManager.isAdmin() && NMApp.modulo != NMApp.Modulo.CONFIGURACION)  
				if(!SessionManager.SignIn(false))
					return "";			
		}        
		if(nameuser==null || password==null || empresa==null ||(nameuser!=null && nameuser.trim()=="") 
        		|| (password!=null && password.trim()=="") || (empresa!=null && empresa.trim()==""))
        	return "";
        else
        	return nameuser + "-" + password + "-" + empresa; 
	} 
	public static String getCredentials()
	{		
		if (!islogged)
		{			
			if (NMApp.modulo != NMApp.Modulo.CONFIGURACION) 
			{
				if(!SessionManager.SignIn(false))
					return "";  
					
			}else if (!SessionManager.SignIn(true))
				return "";
		} 
		else
		{		
			if(SessionManager.isAdmin() && NMApp.modulo != NMApp.Modulo.CONFIGURACION)  
				if(!SessionManager.SignIn(false))
					return "";			
		}          
        if(nameuser==null || password==null || empresa==null ||(nameuser!=null && nameuser.trim()=="") 
        		|| (password!=null && password.trim()=="") || (empresa!=null && empresa.trim()==""))
        	return "";
        else
        	return nameuser + "||" + password + "||" + empresa;
	}
	
	public static void setErrorAuntentication(String _error){
		errormessage=_error;
	}

	public static String getErrorAuntentication(){
		return errormessage;	
	}
	
	public synchronized static boolean SignIn(final boolean admin)
    {
		isOK=true; 
		if(SessionManager.getLoginUser()!=null || NMApp.modulo == NMApp.Modulo.CONFIGURACION)
		{
			while( ((!SessionManager.isLogged()) && isOK) || (admin && !SessionManager.isAdmin() && isOK) )
			{
				if(hasError)
					break;
				isOK=false;
				SessionManager.bloque1(admin);
				SessionManager.bloque2(admin);  			
			}
		}
		else
			sendErrorMessage(ErrorMessage.newInstance("Error en inicio de Session","El no hay usuario configurado para este dispositivo, favor asigne un usuario en el modulo de Configuración",""));
		SessionManager.hasError=false;
        return SessionManager.isLogged();
    } 
	
	public static void bloque1(final boolean admin)
	{
        context.runOnUiThread(new Runnable()
        {
            @Override
			public void run()
            { 
        		dl=new DialogLogin(context,admin);
        		dl.setOnDialogLoginButtonClickListener(new OnButtonClickListener(){
				@Override
				public void onButtonClick(boolean btn) 
				{ 
					isOK=btn;  
					dl.dismiss();   
				}}); 
        		dl.setOnDismissListener(new OnDismissListener()
                {
                    @Override
					public void onDismiss(DialogInterface dialog)
                    {
                        synchronized(lock){                            
                        	lock.notify();
                        }
                    }
                });    
            	dl.show(); 
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
	
	public static void bloque2(final boolean admin)
	{ 
        if(isOK)
        {
        	context.runOnUiThread(new Runnable()
	        {
	            @Override
				public void run()
	            {		         
	            	SessionManager.login(admin);   
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
        else
        {
        	SessionManager.setLogged(false);
        	NMApp.getController()._notifyOutboxHandlers(0,0,0,0);
        }
	}
	
	public static Boolean isPhoneConnected()
	{
		return NMNetWork.isPhoneConnected(context);		
	}
	
	public  static boolean  login(final boolean admin)
	{
		final String empresa=dl.getEmpresa();
		errormessage="";
		nombreusuario=dl.getNameUser();
		final String password=dl.getPassword();  
		SessionManager.setLogged(false); 
		SessionManager.setErrorAuntentication("");		
		final String url= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer():NMConfig.URL; 
		final String url2= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer2():NMConfig.URL2; 
		hasError=false;
		try 
		{ 							
			pool.execute(new Runnable()
			{
				@Override
				public void run() 
				{ 
					if(NMNetWork.isPhoneConnected() && NMNetWork.CheckConnection(url))			
					{
						try 
						{
								pool.execute(new Runnable()
								{
									@Override
									public void run()
									{ 										
										LoginUserResult res;
										
										try 
										{
											res =ModelConfiguracion.verifyLogin(url2,(nombreusuario+"-"+password+"-"+empresa),"ADMIN");
											
											if (res.getAuntenticateRS() == AUT_EXITOSA )
											{				
												if(!(NMApp.getContext() instanceof Main) && NMApp.modulo != NMApp.Modulo.CONFIGURACION &&  res.IsAdmin())
												{
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario no es valido para realizar esta operación",""));
													return;
												}
												
												if(NMApp.modulo == NMApp.Modulo.CONFIGURACION &&  (res.IsAdmin()!=admin))
												{
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario "+nombreusuario+" no es Administrador",""));
													return;
												}
												else
												{
													Usuario user=SessionManager.getLoginUser();
													if (user!=null && (!user.getLogin().trim().toString().equals(nombreusuario.trim().toString())) && (NMApp.modulo != NMApp.Modulo.CONFIGURACION) && !(NMApp.getContext() instanceof Main))
													{
														sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario "+nombreusuario+" no esta configurado localmente, asigne este usuario en el modulo de Configuración",""));
														return;
													}
													else
													{ 
														if(user==null && NMApp.modulo== NMApp.Modulo.CONFIGURACION)
														{
															BConfiguracionM.GET_DATACONFIGURATION(url,url2,empresa, nombreusuario+"-"+password+"-"+empresa, ((ViewConfiguracion)SessionManager.getContext()).getTBoxUserName(), NMNetWork.getDeviceId(context),getImpresora());
														}														
														_esAdmin=res.IsAdmin();
														SessionManager.setEmpresa(empresa);
														SessionManager.setNameUser(nombreusuario);
														SessionManager.setPassword(password);
														SessionManager.setLogged(true);
													}
												}
												unlock();
											}
											else
											{
												if(res.getAuntenticateRS() == AUT_USER_NO_EXIST)
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Usuario desconocido.","")); 
												else if (res.getAuntenticateRS() == AUT_PWD_INVALIDO) 
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login:Contraseña inválida.","")); 
												else if (res.getAuntenticateRS() == AUT_FALLIDA)
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login:Fallo de autenticación.","")); 
												else
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Fallo en la conexión con el servidor de aplicaciones.\r\n",""));
												unlock();											
											}
											
										}catch (Exception e) {
											sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Fallo en la conexión con el servidor de aplicaciones.\r\n",e.toString()));
											e.printStackTrace();
										}
									}
									
								});
								
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}  
						NMApp.getController()._notifyOutboxHandlers(NOTIFICATION_DIALOG2, 0, 0, "Validando Credenciales."); 
							
					}
					else
						unlock();
				 }
			});	  
			NMApp.getController()._notifyOutboxHandlers(NOTIFICATION_DIALOG2, 0, 0,"Probando Conexión.");   

		}
	    catch (Exception e) {  
			e.printStackTrace();
			sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Fallo en la conexión con el servidor de aplicaciones.\r\n",e.toString()));
		} 
		return SessionManager.isLogged();

 	} 
	 
	public static void unlock()
	{
		context.runOnUiThread(new Runnable()
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
	
	public static void sendErrorMessage(final ErrorMessage error)
	{
		hasError=true;
		errormessage=error.getTittle()+"\n\t\t"+error.getMessage();
		NMApp.getController()._notifyOutboxHandlers(0,0,0,0);
		context.runOnUiThread(new Runnable()
        {
            @Override
			public void run()
            { 
				dlg= new CustomDialog(context,error.getTittle()+"\n\t\t"+error.getMessage(),true,NOTIFICATION_DIALOG2);						 
				dlg.setOnActionDialogButtonClickListener(new OnActionButtonClickListener()
				{
					@Override
					public void onButtonClick(View _dialog, int actionId) 
					{ 
						synchronized(lock2){                            
                        	lock2.notify();
                        }
						 
					}
				}); 
				dlg.show();		
            }
        });
	    synchronized(lock2)
        {
            try {
            	lock2.wait();
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
        }
	    unlock();
		SessionManager.setLogged(false);
	}
     
	public static void clean(){
		SessionManager.setEmpresa("");
		SessionManager.setNameUser("");
		SessionManager.setPassword(""); 
		SessionManager.setLogged(false);
		userinfo=null;
	}
	
	//Chequea el estado de la conexión con el servidor de aplicaciones de Nordis
    public static boolean CheckConnection() 
    {
         return NMNetWork.CheckConnection(NMApp.getController());
    }
     
	
}
