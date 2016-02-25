package com.panzyma.nm.auxiliar; 
 

import android.annotation.SuppressLint;
import android.app.Activity;   
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;   
import android.view.View;  

import com.panzyma.nm.Main; 
import com.panzyma.nm.NMApp;
import com.panzyma.nm.NMApp.Modulo;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener; 
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.serviceproxy.LoginUserResult;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.viewdialog.DialogLogin; 
import com.panzyma.nm.viewdialog.DialogLogin.OnButtonClickListener;  

import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG2;

@SuppressLint("ParserError") 
public class SessionManager
{
	private static  String empresa = "dp";
	private static  String nameuser; 
	private static  String password; 
	private static  boolean islogged;
	public static boolean usuarioDeSincronizacion = false;
	public static boolean validPrefix = false;
	
	public static boolean isValidPrefix() {
		return validPrefix;
	}

	public static void setValidPrefix(boolean validPrefix) {
		SessionManager.validPrefix = validPrefix;
	}
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
	private static Usuario user;
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
		if(impresora==null || (impresora!=null && Impresora.obtenerMac().trim()==""))
			impresora=Impresora.get(NMApp.getContext());
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
	
	public static void setLoginUserToNull(){
		userinfo = null;
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
		/*nameuser="kpineda";
		password="123";
		islogged=true;
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
        	return nameuser + "-" + password + "-" + empresa; */
		return UserSessionManager.getCredenciales();
	} 
	public static String getCredentials()
	{		
		/*nameuser="kpineda";
		password="123";
		empresa="dp";
		islogged=true;
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
        	return nameuser + "||" + password + "||" + empresa;*/
		return UserSessionManager.getCredentials();
	}
	
	public static String getCredentials(Context context)
	{		 
		return UserSessionManager.getCredentials(context);
	}
	
	
	
	public static void setErrorAuntentication(String _error){
		errormessage=_error;
	}

	public static String getErrorAuntentication(){
		return errormessage;	
	}
	
	public synchronized static boolean SignInForce()
    {
		isOK=true; 
		if(SessionManager.getLoginUser()!=null || NMApp.modulo == NMApp.Modulo.CONFIGURACION)
		{
			while( isOK || ( !SessionManager.isAdmin() && isOK && !(NMApp.getController().getView() instanceof Main)) )
			{
				if(hasError)
					break;
				isOK=false;
				SessionManager.bloque1(false);
				SessionManager.bloque2(false);  			
			}
		}
		else
			sendErrorMessage(ErrorMessage.newInstance("Error en inicio de Session","El no hay usuario configurado para este dispositivo, favor asigne un usuario en el modulo de Configuración",""));
		SessionManager.hasError=false;
        return SessionManager.isLogged();
    } 
	
	public synchronized static boolean SignIn(final boolean admin, final boolean... logged)
    {
		isOK=true; 
		boolean logueado = false;
		if(SessionManager.getLoginUser()!=null || NMApp.modulo == NMApp.Modulo.CONFIGURACION)
		{
			logueado = (logged.length == 0) ? SessionManager.isLogged() : logged[0];
			//(!SessionManager.isLogged())
			while( ((!logueado) && isOK) || (admin && !SessionManager.isAdmin() && isOK && !(NMApp.getController().getView() instanceof Main)) )
			{
				if(hasError)
					break;
				isOK=false;
				SessionManager.bloque1(admin);
				SessionManager.bloque2(admin); 
				logueado = (logged.length == 0) ? SessionManager.isLogged() : logged[0];
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
	
	public static Boolean isPhoneConnected2()
	{
		return NMNetWork.isPhoneConnected();		
	}
	
	public static Boolean isPhoneConnected3()
	{
		return NMNetWork.isPhoneConnected3();		
	}
	
	public  static boolean login(final boolean admin, String... credentials)
	{
		final String empresa= ( ( dl == null || dl.getEmpresa() == null ) ? SessionManager.empresa : dl.getEmpresa() );
		errormessage="";
		nombreusuario = ( ( dl == null || dl.getNameUser() == null ) ? credentials[0] : dl.getNameUser() );
		final String password = ( ( dl == null || dl.getPassword() == null ) ? credentials[1] : dl.getPassword() );  
		SessionManager.setLogged(false); 
		SessionManager.setErrorAuntentication("");		
		final String url= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer():NMConfig.URL; 
		final String url2= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionManager.getContext()).getTBoxUrlServer2():NMConfig.URL2; 
		hasError=false;
		user = SessionManager.getLoginUser();
		try 
		{ 							
			NMApp.getThreadPool().execute(new Runnable()
			{
				@Override
				public void run() 
				{ 
					
																				
					if( NMApp.modulo == Modulo.HOME && user != null && user.getPassword().trim().length() > 0  ) {
						NMApp.tipoAutenticacion = AutenticationType.LOCAL;
					}
										
					if((NMApp.tipoAutenticacion == AutenticationType.REMOTE) || (NMNetWork.isPhoneConnected() && NMNetWork.CheckConnection(url)))			
					{
						try 
						{
							NMApp.getThreadPool().execute(new Runnable()
								{
									@Override
									public void run()
									{ 										
										LoginUserResult res;
										

										Usuario user = SessionManager.getLoginUser();
																				
										if( NMApp.modulo == Modulo.HOME && user != null && user.getPassword().trim().length() > 0 && !admin ) {
											NMApp.tipoAutenticacion = AutenticationType.LOCAL;
										}

										
										try 
										{
											if( NMApp.tipoAutenticacion == AutenticationType.REMOTE) {
												// AUTENTICACION REMOTA
												res =ModelConfiguracion.verifyLogin(url2,(nombreusuario+"-"+password+"-"+empresa),"ADMIN");
												
												if (res.getAuntenticateRS() == AUT_EXITOSA )
												{				
													if(!(NMApp.getContext() instanceof Main) && NMApp.modulo != NMApp.Modulo.CONFIGURACION &&  res.IsAdmin())
													{
														sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario no es valido para realizar esta operación",""));
														return;
													}
													else
													{
														user = SessionManager.getLoginUser();
														if (user!=null && (!user.getLogin().trim().toString().equals(nombreusuario.trim().toString())) && (NMApp.modulo != NMApp.Modulo.CONFIGURACION) && !(NMApp.getContext() instanceof Main))
														{
															sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario "+nombreusuario+" no esta configurado localmente, asigne este usuario en el modulo de Configuración",""));
															return;
														}
														else
														{ 
															if(user==null && NMApp.modulo== NMApp.Modulo.CONFIGURACION)
															{
																BConfiguracionM.GET_DATACONFIGURATION(url,url2,empresa, 
																										nombreusuario+"-"+password+"-"+empresa,
																										((ViewConfiguracion)SessionManager.getContext()).getTBoxUserName(),
																										NMNetWork.getDeviceId(context),getImpresora());
															} else if(user != null 
																	&& ( user.getPassword() == null 
																		|| ( user.getPassword() != null && user.getPassword().trim().length() == 0 ) ) ){
																
																BConfiguracionM.GET_DATACONFIGURATION(url,url2,empresa, 
																		nombreusuario+"-"+password+"-"+empresa,
																		nombreusuario,
																		NMNetWork.getDeviceId(NMApp.getContext()),getImpresora());
															}	
															
															_esAdmin=res.IsAdmin();															
															SessionManager.setEmpresa(empresa);
															SessionManager.setNameUser(nombreusuario);
															SessionManager.setPassword(password);															
															SessionManager.setLogged(true);															
															if( ( user != null ) &&
																( user.getPassword() == null ||
																  ( user.getPassword() != null &&
																    user.getPassword().trim().length() == 0) 
																  )
															   ) {
																usuarioDeSincronizacion = false;
																//SessionManager.setLogged(true);
																user.setPassword(password);
																user.setIsAdmin(admin);
																Usuario.guardarInfoUsuario(NMApp.getContext(), user);
																NMApp.tipoAutenticacion = AutenticationType.LOCAL;
															}else {
																usuarioDeSincronizacion = true;
															}														
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
												
											} else {
												// NOS AUTENTICAREMOS DE FORMA LOCAL
												user=SessionManager.getLoginUser();
												if(!dl.getPassword().equals(user.getPassword())) {
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login:Contraseña inválida.",""));
												} else {
													SessionManager.setLogged(true);
												}
												unlock();
											}								
											
										}catch (Exception e) {
											sendErrorMessage(new ErrorMessage("Error en la Autenticación","Login: Fallo la comunicación con el servidor de aplicaciones.\r\n",e.toString()));
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
		NMApp.getController()._notifyOutboxHandlers(0,0,0,0);
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
		errormessage=error.getTittle()+"\n"+error.getMessage()+"\n"+error.getCause();
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
            try 
            {
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
