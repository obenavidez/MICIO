package com.panzyma.nm;
 
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.view.ProductoView;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.view.ViewPedido;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.vCliente;
import com.panzyma.nordismobile.R;

@SuppressLint("ShowToast")
@SuppressWarnings({"rawtypes","unused"})
public class Main extends DashBoardActivity implements Handler.Callback{
  
	/*este es un Comentario*/	
	private Display display;
	private Context context;  
	private CustomDialog cd;
	public static String TAG=Main.class.getSimpleName();
	private ThreadPool pool;
	private NMApp nmapp;
	private boolean onRestart;
	private boolean onPause;
	public int buttonActive;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setHeader(getString(R.string.HomeActivityTitle), false, true);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        context=this; 
        nmapp=(NMApp) this.getApplicationContext(); 	 
        nmapp.getController().addOutboxHandler(new Handler(this)); 
        if((savedInstanceState!=null)?savedInstanceState.getBoolean("dl_visible"):false)
        	callDialogLogin();        
        
        
    }
    
    @Override
	protected void onDestroy() {	 
		super.onDestroy();
		if(SessionManager.dl!=null) 
			SessionManager.dl.dismiss(); 
		NMApp.killApp(true);
	}
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
	      if (keyCode == KeyEvent.KEYCODE_BACK) 
	      {        	
	    	  	FINISH_ACTIVITY();
	            return true;
	      }
        return super.onKeyUp(keyCode, event); 
    } 
    
	@Override
	protected void onSaveInstanceState(Bundle bundle) 
	{ 
		super.onSaveInstanceState(bundle);
		
		if(SessionManager.dl!=null) 
			if(SessionManager.dl.isShowing())
				bundle.putBoolean("dl_visible",true); 
		else
			bundle.putBoolean("dl_visible",false);  
		
	}
    
    /**
     * Button click handler on Main activity
     * @param v
     */
    public void onButtonClicker(View v)
    {
    	final Intent intent;
    	buttonActive=v.getId();
    	switch (v.getId()) 
    	{
    	
	    	case R.id.hbtnpedido:
						    		intent = new Intent(this,ViewPedido.class); 
									startActivity(intent);
									break;
	    	case R.id.hbtnrecibocollector: 
	    		                    intent = new Intent(this, ViewRecibo.class);
	    		                    startActivity(intent);
									break;
	    	case R.id.hbtndevolucion: 
									break;
			case R.id.hbtncliente:
									//intent = new Intent(this, ViewCliente.class);
				                    intent = new Intent(this, vCliente.class);
									startActivity(intent);
									break;
			case R.id.hbtnproducto:
									intent = new Intent(this, ProductoView.class);
									startActivity(intent);
									break;
				
			case R.id.hbtnconfiguracion:
			 						callDialogLogin(); break;
			default:
									break;
		}
    }
  
    
    @SuppressLint("ParserError")
	public void callDialogLogin()
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
						
						if(SessionManager.SignIn(false) || SessionManager.isCheckedSettingSession())  
						{  
							Intent intent = new Intent(context, ViewConfiguracion.class);
							intent.putExtra("isEditActive",(SessionManager.isCheckedSettingSession()==false)? SessionManager.isAdmin():true);							
							SessionManager.setCheckedSettingSession(false);
							startActivity(intent);
							FINISH_COMPONENT();
						}
						else
							Log.d(TAG, "Error in login"); 
						
					}
			        catch (Exception e)
			        {
			            e.printStackTrace();
			            dialog("",SessionManager.getErrorAuntentication(),ALERT_DIALOG);
			        } 

			     }

			});
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
    } 
    
	public Toast ToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(this,msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}	  
	
	public  CustomDialog  dialog(final String tittle,final String msg,final int type)
	{		
       return new CustomDialog(context,tittle,msg,false,type); 
	}

	public  CustomDialog  dialog(final String msg,final int type)
	{		
       return new CustomDialog(context,msg,false,type); 
	}
	 
	
    @Override
	public boolean handleMessage(Message msg) 
	{	
		
		if(cd!=null && cd.isShowing())
					cd.dismiss();	
		switch (msg.what) 
		{	 
			case NOTIFICATION:				
				NotificationMessage message=((NotificationMessage)msg.obj);
				(cd=dialog(message.getMessage()+message.getCause(),NOTIFICATION_DIALOG)).show();
				return true;
			case ERROR: 
				ErrorMessage error=((ErrorMessage)msg.obj);
				(cd=dialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG)).show();
				return true;	
					
		}
		return false;
	}
    
    @Override
 		protected void onPause() {
 			// TODO Auto-generated method stub
 	    	onPause=true;
 	    	onRestart=false;
 			super.onPause();
 		}

 		@Override
 		protected void onStop() {
 			// TODO Auto-generated method stub
 			onRestart=false;
 			onPause=false;
 			super.onStop();
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
 			SessionManager.setContext(Main.this); 
 			onRestart=false;
 			onPause=false;
 			super.onResume();
 		}

	
	private void initController()
	{
		 nmapp=(NMApp) this.getApplicationContext();  
		 Handler handler=(Handler) nmapp.getController().getoutboxHandlers().get(TAG);
		 if(handler==null)
			nmapp.getController().addOutboxHandler(new Handler(this));
	}
	private void FINISH_COMPONENT()
	{
		nmapp.getController().removeOutboxHandler(TAG); 
		if(cd!=null && cd.isShowing())
			cd.dismiss();	
		Log.d(TAG, "Activity quitting");
	}
	private void FINISH_ACTIVITY()
	{ 
		/*	Iterator  it= nmapp.getController().getoutboxHandlers().entrySet().iterator();		
		while (it.hasNext())
		{
		        String handler= (String) ((Map.Entry)it.next()).getKey();
		        nmapp.getController().removeOutboxHandler(handler);
		}*/
	//	for (Handler handler :handlers)  
		//	nmapp.getController().removeOutboxHandler(handler);
		nmapp.getController().clearOutboxHandler();
		if(cd!=null && cd.isShowing())
			cd.dismiss();	
		nmapp.getController().dispose();
		Log.d(TAG, "Activity quitting");
		SessionManager.clean(); 
		finish();
	}  
	
    
}