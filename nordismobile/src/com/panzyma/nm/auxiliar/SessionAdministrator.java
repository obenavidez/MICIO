package com.panzyma.nm.auxiliar;

import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG2;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.panzyma.nm.Main;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.serviceproxy.LoginUserResult;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.viewdialog.DialogLogin;
import com.panzyma.nm.viewdialog.DialogLogin.OnButtonClickListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.view.View;

@SuppressLint("SimpleDateFormat") 
public class SessionAdministrator 
{ 
	 
	private static Activity context;
	
	private static int PRIVATE_MODE=0;
	
	private static final String PREF_NAME="SimfacSession";
	
	private static final String IS_LOGIN="IsLoginIn";
	
	public static final String KEY_NAME="name";
	
	public static final String KEY_ENTERPRISE="enterprise";
	
	public static final String KEY_PASSWORD="password";
	
	public static final String KEY_ISADMIN="isAdmin";
	
	public static final String KEY_SESSIONTIME="start_sessiontime";
	 
	private static SharedPreferences pref=getContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
	
	private static Editor editor;
	
    private static final int AUT_FALLIDA = 0; 
    
    private static final int AUT_EXITOSA = 1; 
    
    private static final int AUT_USER_NO_EXIST = 2; 
    
    private static final int AUT_PWD_INVALIDO = 3; 
    
    private static boolean isOK;
    
    static Object lock=new Object();
    
    static Object lock2=new Object(); 
    
    public static DialogLogin  dl;
	
    private static String errormessage=""; 
    
    private static CustomDialog dlg; 
    
    public static boolean hasError=false;
    
    static String nombreusuario="";
    
    static Usuario usuariodispositivo;

	private static Impresora impresora;
    
	public static Context getContext()
	{
		return (context==null)?NMApp.getContext():context;
	}
	
	public void setContext(Activity cnt)
	{
		context=cnt;
	}
	
	public static void initPreferences()
	{
		pref=getContext().getSharedPreferences(KEY_NAME, PRIVATE_MODE);
		editor=pref.edit();
	}
	  
	public static void createSession(String enterprise,Usuario usuario)
	{
		if(pref!=null)
			initPreferences();
		
		/**ALAMACENANDO VARIABLES DE SESSION**/ 
		editor.putBoolean(IS_LOGIN,true);
		
		editor.putString(KEY_ENTERPRISE, enterprise);
		
		editor.putString(KEY_NAME, usuario.getLogin());
		
		editor.putString(KEY_PASSWORD,usuario.getPassword());
		
		editor.putBoolean(KEY_ISADMIN,usuario.isIsAdmin());
		
		editor.putString(KEY_SESSIONTIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		editor.commit();
	}
	 
	public static boolean isAppConfigured()
	{
		if(getNameLogin()!=null && getPassword()!=null && getEnterprise()!=null)
			return true;
		else
			return false;
	}
	
	public static boolean islogged()
	{
		return pref.getBoolean(IS_LOGIN,false);
	}
	
	public static String getNameLogin()
	{
		return pref.getString(KEY_NAME,null);
	}
	
	public static String getEnterprise()
	{
		return pref.getString(KEY_ENTERPRISE,null);
	}
	
	public static String getPassword()
	{
		return pref.getString(KEY_PASSWORD,null);
	}
	
	public static boolean isAdmin()
	{
		return pref.getBoolean(KEY_ISADMIN,false);
	}
	
	@SuppressWarnings("static-access")
	public static Impresora getImpresora(){
		if(impresora==null || (impresora!=null && Impresora.obtenerMac().trim()==""))
			impresora=Impresora.get(context);
		return impresora;
	}
	
	public static void setImpresora(Impresora dispositivo)
	{
		impresora=dispositivo;
	}
	
	public static Usuario getUsuarioDispositivo()
	{
		if(usuariodispositivo==null)
			usuariodispositivo=Usuario.get(context);
		return usuariodispositivo;
	}
	
	public static String getCredenciales()
	{
		if( !isAppConfigured() && NMApp.modulo==NMApp.Modulo.CONFIGURACION)
		{
			if(SignIn(true))
				return  getNameLogin() + "-" + getPassword() + "-" + getEnterprise(); 
		}
		else if(isAppConfigured() && NMApp.modulo==NMApp.Modulo.HOME){
			if(SignIn(false))
				return  getNameLogin() + "-" + getPassword() + "-" + getEnterprise(); 
		}
		else if((isAppConfigured() && islogged() && NMApp.modulo==NMApp.Modulo.CONFIGURACION) || 
		  (isAppConfigured() && islogged() && NMApp.modulo!=NMApp.Modulo.CONFIGURACION && NMApp.modulo!=NMApp.Modulo.HOME  &&  !SessionAdministrator.isAdmin()))
			return  getNameLogin() + "-" + getPassword() + "-" + getEnterprise(); 
		 
		
        return ""; 
	} 
	
	public synchronized static boolean SignIn(final boolean admin)
    { 
		SessionManager.bloque1(admin);
		SessionManager.bloque2(admin);  
		return SessionAdministrator.islogged();
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
	
	public  static boolean  login(final boolean admin)
	{
		final String empresa=dl.getEmpresa();
		errormessage="";
		nombreusuario=dl.getNameUser();
		final String password=dl.getPassword();   
		
		final String url= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionAdministrator.getContext()).getTBoxUrlServer():NMConfig.URL; 
		final String url2= (NMApp.modulo== NMApp.Modulo.CONFIGURACION)?((ViewConfiguracion)SessionAdministrator.getContext()).getTBoxUrlServer2():NMConfig.URL2; 
		hasError=false;
		try 
		{ 							
			NMApp.getThreadPool().execute(new Runnable()
			{
				@Override
				public void run() 
				{ 
					if(NMNetWork.isPhoneConnected() && NMNetWork.CheckConnection(url))			
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
											res =ModelConfiguracion.verifyLogin(url2,(nombreusuario+"-"+password+"-"+empresa),"ADMIN");
											
											if (res.getAuntenticateRS() == AUT_EXITOSA )
											{				
												if((!(NMApp.getContext() instanceof Main)) && NMApp.modulo != NMApp.Modulo.CONFIGURACION &&  res.IsAdmin())
												{
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario no es valido para realizar esta operación",""));
													return;
												}
												else
												{
													Usuario ud=SessionAdministrator.getUsuarioDispositivo();
													if (ud!=null && !(ud.getLogin().trim().toString().equals(nombreusuario.trim().toString())) && NMApp.modulo!= NMApp.Modulo.CONFIGURACION)
													{
														sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario "+nombreusuario+" no esta configurado localmente, asigne este usuario en el modulo de Configuración",""));
														return;
													}
													else
													{ 
														if(ud==null || NMApp.modulo== NMApp.Modulo.CONFIGURACION && (ud.getLogin().trim().toString().equals(nombreusuario.trim().toString())))
														{
														     BConfiguracionM.GET_DATACONFIGURATION(url,url2,empresa, nombreusuario+"-"+password+"-"+empresa, 
														    		 ((ViewConfiguracion)SessionManager.getContext()).getTBoxUserName(), NMNetWork.getDeviceId(context),getImpresora());
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
	
}
