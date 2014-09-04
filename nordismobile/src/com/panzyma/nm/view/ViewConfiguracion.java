package com.panzyma.nm.view;
 
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_FINISH;
import static com.panzyma.nm.controller.ControllerProtocol.C_SAVING;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_IN_PROGRESS;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.ID_CERRAR;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SALVAR_CONFIGURACION;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CLIENTES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PARAMETROS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PRODUCTOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PROMOCIONES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_TODOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SETTING_BLUETOOTHDEVICE;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_SETTING;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG2;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.NumberUtil;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.DataConfigurationResult;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.viewmodel.vmConfiguracion;
import com.panzyma.nordismobile.R;

@SuppressLint("ShowToast")
@SuppressWarnings({"unchecked","rawtypes","unused","static-access"})  
public class ViewConfiguracion extends FragmentActivity implements Handler.Callback 
{
	
	String TAG=ViewConfiguracion.class.getSimpleName();
	QuickAction quickAction;  
	Display display;
	ProgressDialog pd;
	Context context;  
	boolean isEditActive;
	private EditText txtURL;
	private EditText txtEmpresa;
	private EditText txtUsuario; 
	private EditText txtDispositivoID; 
	private String user_name;
	private String passwd;
	private String enterprise;
	private String url_server;
	private String deviceid;
	private NMApp nmapp;
	private boolean onRestart;
	private boolean onPause;
	private DataConfigurationResult settingdata;
	private CustomDialog dlg;
	static Object lockC=new Object();
	private Intent intento;
	private BConfiguracionM bcm;
	private EditText txtImpresora;
	public static int RESULTADO_IMPRESORA=1;
	private Impresora impresora;
	private vmConfiguracion oldata;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_configuracion);
		//setHeader(getString(R.string.ConfiguracionActivityTitle),true, false); 
		isEditActive=getIntent().getExtras().getBoolean("isEditActive");
		 try 
		    { 		
			    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			 	context=this;  
			 	nmapp=(NMApp) this.getApplicationContext(); 
		        nmapp.getController().setEntities(this,bcm=new BConfiguracionM());
		        nmapp.getController().addOutboxHandler(new Handler(this));
				nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA); 
				
				WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	            display = wm.getDefaultDisplay(); 
				pd = ProgressDialog.show(this, "Espere por favor", "Trayendo Info...", true, false);  		       
		        initComponents(); 		        
			}catch (Exception e) { 
				e.printStackTrace();
				dlg=(CustomDialog) dialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG);dlg.show();
			}	
	} 	 
	
	public NMApp getAplication()
	{
		return nmapp;		
	}
	
	public void setUserName(String user){
		this.user_name=user;
	}
	
	public String getUserName(){
		return this.user_name;
	}
	
	public void setPasswd(String passwd){
		this.passwd=passwd;
	}
	
	public String getPasswd(){
		return this.passwd;
	}
	
	public String getEnterprise(){
		return this.enterprise;
	}
	
	public void setEnterprise(String enterprise){
		this.enterprise=enterprise;
	}
	
	public String getUrlServer(){
		return this.url_server;
	}
	
	public void setUrlServer(String url_server){
		this.url_server=url_server;
	}
	
	public void setDeviceId(String deviceid){
		this.deviceid=deviceid;
	}
	
	public String getDeviceId(){
		return this.deviceid;
	}
	
	public void setImpresora(Impresora _impresora){
		this.impresora=_impresora;
	}
	
	public Impresora getImpresora(){
		return this.impresora;
	}
	
	@Override
	public boolean handleMessage(final Message msg) {
		Log.d(TAG, "Received message: " + msg);
		if(dlg!=null)
			dlg.dismiss();
		if(pd!=null)
			pd.dismiss();
		switch (msg.what) 
		{		 
			case NOTIFICATION:				
										NotificationMessage message=((NotificationMessage)msg.obj);										
										dialog(message.getMessage()+message.getCause(),NOTIFICATION_DIALOG,msg.arg2);
										return true;
						
			case C_DATA: 				setData((vmConfiguracion)msg.obj);
										return true;
			
			case C_UPDATE_STARTED:
				
		    case C_UPDATE_IN_PROGRESS: 
									
										pd = ProgressDialog.show(context,"",msg.obj.toString(), true, false);  	
										return true; 
										
		    case C_SAVING:  			pd = ProgressDialog.show(context,"",msg.obj.toString(), true, false);  	
										return true; 
				
			case C_UPDATE_FINISHED: 	if(msg.arg2==1)	
										{									
											dialog(msg.obj.toString(),NOTIFICATION_DIALOG2,msg.arg2);
										    dlg.show();	 
									    }
										else
										{
										    buildToastMessage(msg.obj.toString(), Toast.LENGTH_LONG).show();	
									        Log.d(TAG, "C_UPDATE_FINISHED: " + msg.obj.toString());
									    }
										return true;
			case ERROR: 
										ErrorMessage error=((ErrorMessage)msg.obj);
										dialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
										return true;	
			case C_FINISH: 	
										NotificationMessage nmsg=((NotificationMessage)msg.obj);
									    dialog(nmsg.getMessage()+nmsg.getCause(),NOTIFICATION_DIALOG2,msg.arg2);dlg.show();				 
										return true;	
									
		}
		return false;
	}
	
	private void setData(vmConfiguracion setting)
	{
		oldata=vmConfiguracion.setConfiguration(setting);
		txtURL.setText(setting.getAppServerURL());
		txtEmpresa.setText(setting.getEnterprise());
		txtUsuario.setText(setting.getNameUser());
		txtDispositivoID.setText(setting.getDeviceId());
		
		this.setUrlServer(setting.getAppServerURL()); 
		this.setDeviceId(setting.getEnterprise());
		this.setUserName(setting.getNameUser());

	}
		 
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_MENU) 
        {        	
        	Button Menu = (Button)findViewById(R.id.btn_menu);
        	quickAction.show(Menu,display,true);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {        	
	    	  	FINISH_ACTIVITY();
	            return true;
	    }
        return super.onKeyUp(keyCode, event); 
    } 
	
	private void initComponents()
	{   
		initMenu();
	    txtURL=(EditText)findViewById(R.id.cfgtextv_detalleurlws);
		txtEmpresa=(EditText)findViewById(R.id.cfgtextv_detallecodempresa);
	    txtUsuario=(EditText)findViewById(R.id.cfgtextv_detalleuser); 
	    txtDispositivoID=(EditText)findViewById(R.id.cfgtextv_detalledeviceid);
	    txtImpresora=(EditText)findViewById(R.id.cf_bluetoothprinter);
	    txtURL.setEnabled(isEditActive);
	    txtEmpresa.setEnabled(isEditActive);
	    txtUsuario.setEnabled(isEditActive); 
	    
	    pd.dismiss();
	}
	
	public void initMenu()
    {  
        quickAction = new QuickAction(this, QuickAction.VERTICAL,1);
		//if(isEditActive)
			quickAction.addActionItem((new ActionItem(ID_SALVAR_CONFIGURACION, "Salvar Configuración")));
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PARAMETROS, "Sincronizar Parametros")));
		quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_CATALOGOSBASICOS, "Sincronizar Catalogos Básicos")));
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_CLIENTES, "Sincronizar Clientes")));         
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PRODUCTOS, "Sincronizar Productos")));
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PROMOCIONES, "Sincronizar Promociones")));
        quickAction.addActionItem(null);
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_TODOS, "Sincronizar Todo")));
        quickAction.addActionItem(null);
        quickAction.addActionItem((new ActionItem(ID_SETTING_BLUETOOTHDEVICE, "Configurar Impresora")));
        quickAction.addActionItem(null);
        quickAction.addActionItem((new ActionItem(ID_CERRAR, "Cerrar")));
        
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{			
			

			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) 
			{				
				final Controller controller=nmapp.getController();
				ActionItem actionItem = quickAction.getActionItem(pos);    
				if (actionId == ID_SALVAR_CONFIGURACION) 
						salvarConfiguracion(); 
				else if (actionId == ID_SINCRONIZE_PARAMETROS)
			    {  
					try {
						
						nmapp.getThreadPool().execute(new Runnable()
						{ 
							@Override
							public void run()
						    {
								try		
								{
									if(SessionManager.getCredenciales().trim()!="") 
										controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_PARAMETROS);
								}
								catch (Exception e)
						        {
						            e.printStackTrace(); 
						        } 
								
						     }
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
				else if (actionId == ID_SINCRONIZE_CATALOGOSBASICOS) 
				{
					
					try {
						nmapp.getThreadPool().execute(new Runnable()
						{ 
							@Override
							public void run()
						    {
								try		
								{
									if(SessionManager.getCredenciales().trim()!="") 
										controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_CATALOGOSBASICOS);
								}
								catch (Exception e)
						        {
						            e.printStackTrace(); 
						        } 
								
						     }
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (actionId == ID_SINCRONIZE_CLIENTES) 
				{
					try {
						nmapp.getThreadPool().execute(new Runnable()
						{ 
							@Override
							public void run()
						    {
								try		
								{
									if(SessionManager.getCredenciales().trim()!="") 
										controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_CLIENTES);
								}
								catch (Exception e)
						        {
						            e.printStackTrace(); 
						        } 
								
						     }
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (actionId == ID_SINCRONIZE_PRODUCTOS) 
				{
					try {
						nmapp.getThreadPool().execute(new Runnable()
						{ 
							@Override
							public void run()
						    {
								try		
								{
									if(SessionManager.getCredenciales().trim()!="") 
										controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_PRODUCTOS);
								}
								catch (Exception e)
						        {
						            e.printStackTrace(); 
						        } 
								
						     }
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if (actionId == ID_SINCRONIZE_PROMOCIONES) 
				{				
					try {
						nmapp.getThreadPool().execute(new Runnable()
						{ 
							@Override
							public void run()
						    {
								try		
								{
									if(isValidInformation()) 
										if(SessionManager.getCredenciales().trim()!="") 
										{
											 Message msg = new Message();
										     Bundle b = new Bundle(); 
										     b.putString("LoginUsuario",txtUsuario.getText().toString()); 
										     msg.setData(b);
										     msg.what=ID_SINCRONIZE_PROMOCIONES; 
										     controller.getInboxHandler().sendMessage(msg);
									    } 
								}
								catch (Exception e)
						        {
						            e.printStackTrace(); 
						        } 
								
						     }
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				} 
				else if (actionId == ID_SINCRONIZE_TODOS) 
				{
					if(SessionManager.getLoginUser()==null)
					   salvarConfiguracion();
					
					try {
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
						nmapp.getThreadPool().execute(new Runnable()
						{ 
							@Override
							public void run()
						    {
								try		
								{
									setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
									if(SessionManager.getCredenciales().trim()!="") 
										controller.getInboxHandler().sendEmptyMessage(ID_SINCRONIZE_TODOS);
								}
								catch (Exception e)
						        {
						            e.printStackTrace(); 
						        } 
								
						     }
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
						
				} 
				else if (actionId == ID_SETTING_BLUETOOTHDEVICE)
				{ 
					intento = new Intent(ViewConfiguracion.this, ConfigurarDispositivosBluetooth.class); 
					startActivityForResult(intento, 0);  
				}
				else if (actionId == ID_CERRAR) 
					FINISH_ACTIVITY();
				else  
				    Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				 
			}
		});
		
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() 
		{			
			@Override
			public void onDismiss() { 
			}
		});
    	
        
        
     }
	
	private boolean salvarConfiguracion()
	{
		if(isValidInformation())
		{			
			try 
			{	 
				nmapp.getThreadPool().execute(new Runnable()
				{ 
					@Override
					public void run()
				    {
						try
				        {  							
							Controller c=nmapp.getController();  
							if(/*SessionManager.SignIn*/(true))
							{			
						         setEnterprise(txtEmpresa.getText().toString());
						         setUserName(txtUsuario.getText().toString());
						         setUrlServer(txtURL.getText().toString()); 
						         setDeviceId(txtDispositivoID.getText().toString());
						         
								 Message msg = new Message();
							     Bundle b = new Bundle();
//							     b.putString("Credentials",SessionManager.getCredenciales());
							     b.putString("Credentials","kpineda-123-dp");
							     b.putString("LoginUsuario",txtUsuario.getText().toString());
							     b.putParcelable("impresora", getImpresora());
							     b.putString("PIN",NMNetWork.getDeviceId(context));
							     msg.setData(b);
							     msg.what=LOAD_SETTING;
							     c.getInboxHandler().sendMessage(msg); 
						    } 
						 		
						}
				        catch (Exception e)
				        {
				            e.printStackTrace(); 
				        } 

				     }

				});								
				
			} catch (Exception e) { 
				e.printStackTrace();
			} 
			
		}
		return false;
	}
	 
	public boolean isValidInformation()
	{ 
		
        if (txtURL.getText().toString().trim().length()==0){ 
        		txtURL.setError( "Ingrese el nombre o IP del servidor SIMFAC.");
        		txtURL.requestFocus();
        		return false;
        }
        else if (txtEmpresa.getText().toString().trim().length()==0){ 
                txtEmpresa.setError("Ingrese el código de la empresa.");
                txtEmpresa.requestFocus();
                return false;
	    }
        else if (txtImpresora.getText().toString().trim().length()==0){ 
	        	txtImpresora.setError("Configure Impresora.");
	        	txtImpresora.requestFocus();
                return false;
        }  
        else if (txtUsuario.getText().toString().trim().length()==0){ 
	            txtUsuario.setError("Ingrese el nombre del usuario.");
	            txtUsuario.requestFocus();
	            return false;
        } 
        else if (getImpresora()==null || (getImpresora()!=null && getImpresora().obtenerNombre().trim()=="")){ 
            txtUsuario.setError("No se ha configurado impresora de trabajo."); 
            return false;
    } 
		return true;
	}
  
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	onPause=true;
		super.onPause();
	} 

	@Override
	protected void onRestart() {
		initController();
	    onRestart=true;
	    onPause=false;
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(onPause && !onRestart)
			initController();
		onRestart=false;
		SessionManager.setContext(ViewConfiguracion.this); 
		onPause=false;
		super.onResume();
	}
	
	private void initController()
	{
		 nmapp=(NMApp) this.getApplicationContext(); 		
		 nmapp.getController().addOutboxHandler(new Handler(this));
	}
	
	private void FINISH_ACTIVITY()
	{ 
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().disposeEntities();
		Log.d(TAG, "Activity quitting");
		finish();	
	}   
	
	@SuppressLint("ShowToast")
	public Toast buildToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(context,msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}	
	
	public  Dialog dialog(String tittle,String msg,int type)
	{
		return new CustomDialog(this,tittle,msg,false,type);	    
	}
	
	public void dialog(final String msg,final int type, final int handlerOwnActionButton)
	{
		dlg= new CustomDialog(context,msg,true,type);	     
		if(handlerOwnActionButton==1)
		{
			dlg.setOnActionDialogButtonClickListener(new OnActionButtonClickListener()
			{
				@Override
				public void onButtonClick(View _dialog, int actionId) 
				{ 
					
				}
			});
		}
		dlg.show();	 		
	}	 
	
	public String getLoginUsuario(){
		return txtUsuario.getText().toString();
	}
	 
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestcode, resultcode, data);
		try 
		{ 
			if (RESULTADO_IMPRESORA == resultcode 	&& data != null)
				actualizarImpresoraEnPantalla((Impresora)data.getParcelableExtra("impresora"));
			nmapp.getController().setEntities(this,(bcm!=null)?getBridge():new BConfiguracionM()); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityFromFragment(fragment, intent, requestCode);
	}
	
	private BConfiguracionM getBridge()
	{
		return bcm;
	}
	
	public void actualizarImpresoraEnPantalla(final Impresora dispositivo) 
	{
		setImpresora(dispositivo);
		runOnUiThread(new Runnable() 
		{
			
			@Override
			public void run() {
				txtImpresora.setText(dispositivo.obtenerNombre());
			}
		});
	}
}
