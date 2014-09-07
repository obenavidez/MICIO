package com.panzyma.nm.auxiliar; 
 

import android.annotation.SuppressLint;
import android.app.Activity;  
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;   
import android.view.View;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.serviceproxy.LoginUserResult;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.viewdialog.DialogLogin; 
import com.panzyma.nm.viewdialog.DialogLogin.OnButtonClickListener;  

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
    private static NMApp nmapp;
    static Object lock=new Object();
    static Object lock2=new Object(); 
    
    public static DialogLogin  dl;
	private static ThreadPool pool;
	private static CustomDialog dlg; 
    private static Usuario userinfo;
    public static boolean hasError=false;
    
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

	public static void setLoguedUser(Usuario user){
		userinfo=user;
	}
	
	public static void setContext(Activity _context)
	{
		context=_context;
		nmapp=((NMApp)_context.getApplicationContext());
		if(pool==null)
			pool =nmapp.getThreadPool();
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
					
			}else if (SessionManager.SignIn(true))
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
					
			}else if (SessionManager.SignIn(true))
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
		while( ((!SessionManager.isLogged()) && isOK) || (admin && !SessionManager.isAdmin() && isOK) )
		{
			if(hasError)break;
			isOK=false;
			SessionManager.bloque1(admin);
			SessionManager.bloque2(admin);  			
		}
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
				public void onButtonClick(boolean btn) { 
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
	}
	
	public static Boolean isPhoneConnected()
	{
		return NMNetWork.isPhoneConnected(context,NMApp.getController());		
	}
	
	public  static boolean  login(final boolean admin)
	{
		final Controller controller=NMApp.getController();
		final String empresa=dl.getEmpresa();
		final String nombreusuario=dl.getNameUser();
		final String password=dl.getPassword();  
		SessionManager.setLogged(false); 
		SessionManager.setErrorAuntentication("");		
		hasError=false;
		try 
		{ 							
			pool.execute(new Runnable()
			{
				@Override
				public void run() 
				{ 
					if(NMNetWork.isPhoneConnected(context,controller) && NMNetWork.CheckConnection(controller))					
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
											res =ModelConfiguracion.verifyLogin((nombreusuario+"-"+password+"-"+empresa),"ADMIN");
											
											if (res.getAuntenticateRS() == AUT_EXITOSA )
											{				
												if(NMApp.modulo != NMApp.Modulo.CONFIGURACION && res.IsAdmin())
												     sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario no es valido para realizar esta operación",""));
												if(NMApp.modulo == NMApp.Modulo.CONFIGURACION && admin && (res.IsAdmin()!=admin))
													sendErrorMessage(new ErrorMessage("Error en la Autenticación","El Usuario "+nombreusuario+" no es Administrador",""));
												else
												{
													 _esAdmin=res.IsAdmin();
													SessionManager.setEmpresa(empresa);
													SessionManager.setNameUser(nombreusuario);
													SessionManager.setPassword(password);
													SessionManager.setLogged(true);
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
						 //showStatus(new NotificationMessage("","Validando Credenciales.",""));
						controller._notifyOutboxHandlers(NOTIFICATION_DIALOG2, 0, 0, new NotificationMessage("","Validando Credenciales.","")); 
							
					}
					else
						unlock();
				 }
			});	 
			//showStatus(new NotificationMessage("","Probando Conexión.",""));
			controller._notifyOutboxHandlers(NOTIFICATION_DIALOG2, 0, 0, new NotificationMessage("","Probando Conexión.",""));   

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
	            	synchronized(lock){                             
	            			lock.notify();
                    }
	            }
	      });
	} 
	
	public static void sendErrorMessage(final ErrorMessage error)
	{
		hasError=true;
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
						NMApp.getController()._notifyOutboxHandlers(0,0,0,0); 
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
         return NMNetWork.CheckConnection(nmapp.getController());
    }
     
	
}
