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
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ProductoView;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.view.ViewPedido;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.vCliente;
import com.panzyma.nordismobile.R;

@SuppressLint("ShowToast")
@SuppressWarnings({ "rawtypes", "unused" })
public class Main extends DashBoardActivity implements Handler.Callback {

	/* este es un Comentario */
	private Display display;
	private Context context;
	private CustomDialog cd;
	public static String TAG = Main.class.getSimpleName();
	private ThreadPool pool;
	private boolean onRestart;
	private boolean onPause;
	public int buttonActive;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SessionManager.setContext(this);
		setContentView(R.layout.home);
		setHeader(getString(R.string.HomeActivityTitle), false, true);
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		context = this;
		NMApp.getController().addOutboxHandler(new Handler(this));
		if ((savedInstanceState != null) ? savedInstanceState
				.getBoolean("dl_visible") : false)
			callDialogLogin();
		NMApp.modulo = NMApp.Modulo.HOME;
	}

	@Override
	protected void onDestroy() {
		
		if (SessionManager.dl != null)
			SessionManager.dl.dismiss();
		
		//NMApp.killApp(true);
		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FINISH_ACTIVITY();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		

		if (SessionManager.dl != null)
		{
			if (SessionManager.dl.isShowing())
				bundle.putBoolean("dl_visible", true);
			else
				bundle.putBoolean("dl_visible", false);
		}
		super.onSaveInstanceState(bundle);
	}

	/**
	 * Button click handler on Main activity
	 * 
	 * @param v
	 */
	public void onButtonClicker(View v) {
		final Intent intent;
		NMApp.modulo = NMApp.Modulo.HOME;
		buttonActive = v.getId();
		Usuario user = SessionManager.getLoginUser();
		if(user!=null)
		{
				switch (v.getId())  
				{
		
					case R.id.hbtnpedido:
						NMApp.modulo = NMApp.Modulo.PEDIDO;
						intent = new Intent(this, ViewPedido.class);
						startActivity(intent);
						break;
					case R.id.hbtnrecibocollector:
						NMApp.modulo = NMApp.Modulo.RECIBO;
						intent = new Intent(this, ViewRecibo.class);
						startActivity(intent);
						break;
					case R.id.hbtndevolucion:
						NMApp.modulo = NMApp.Modulo.DEVOLUCION;
						break;
					case R.id.hbtncliente: 
						NMApp.modulo = NMApp.Modulo.CLIENTE;
						intent = new Intent(this, vCliente.class);
						startActivity(intent);
						break;
					case R.id.hbtnproducto:
						NMApp.modulo = NMApp.Modulo.PRODUCTO;
						intent = new Intent(this, ProductoView.class);
						startActivity(intent);
						break;
			
					case R.id.hbtnconfiguracion:
						callDialogLogin();
						break;
					default:
						break;
				}
		}
		else
			callDialogLogin();
	}

	@SuppressLint("ParserError")
	public void callDialogLogin()
    {
    	try 
    	{ 
			NMApp.getThreadPool().execute(new Runnable()
			{ 
				@Override
				public void run()
			    {
					try
			        { 						 
						Usuario user = SessionManager.getLoginUser(); 
						if(user==null || (user!=null && SessionManager.SignIn(false)))
						{  
							NMApp.modulo = NMApp.Modulo.CONFIGURACION;
							Intent intent = new Intent(context, ViewConfiguracion.class);
							intent.putExtra("isEditActive",(user!=null)?SessionManager.isAdmin():true);
							startActivity(intent);
							FINISH_COMPONENT(); 
						}
						else 
						{
							Log.d(TAG, "Error in login");
							NMApp.modulo = NMApp.Modulo.HOME;
						}
					}
			        catch (Exception e)
			        {
			        	NMApp.modulo = NMApp.Modulo.HOME;
			            e.printStackTrace();
			            dialog("",SessionManager.getErrorAuntentication(),ALERT_DIALOG);
			        } 

			     }

			});
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
    } 

	public Toast ToastMessage(String msg, int duration) {
		Toast toast = Toast.makeText(this, msg, duration);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		return toast;
	}

	public CustomDialog dialog(final String tittle, final String msg,
			final int type) {
		return new CustomDialog(context, tittle, msg, false, type);
	}

	public CustomDialog dialog(final String msg, final int type) {
		return new CustomDialog(context, msg, false, type);
	}

	@Override
	public boolean handleMessage(Message msg) {

		if (cd != null && cd.isShowing())
			cd.dismiss();
		switch (msg.what) {
		case NOTIFICATION:
			NotificationMessage message = ((NotificationMessage) msg.obj);
			(cd = dialog(message.getMessage() + message.getCause(),
					NOTIFICATION_DIALOG)).show();
			return true;
		case ERROR:
			ErrorMessage error = ((ErrorMessage) msg.obj);
			(cd = dialog(error.getTittle(),
					error.getMessage() + error.getCause(), ALERT_DIALOG))
					.show();
			return true;

		}
		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		onPause = true;
		onRestart = false;
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		onRestart = false;
		onPause = false;
		super.onStop();
	}

	@Override
	protected void onRestart() {
		initController();
		onRestart = true;
		onPause = false;
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (onPause && !onRestart)
			initController();
		SessionManager.setContext(Main.this);
		onRestart = false;
		onPause = false;
		super.onResume();
	}

	private void initController() {
		Handler handler = (Handler) NMApp.getController().getoutboxHandlers()
				.get(TAG);
		if (handler == null)
			NMApp.getController().addOutboxHandler(new Handler(this));
	}

	private void FINISH_COMPONENT() {
		NMApp.getController().removeOutboxHandler(TAG);
		if (cd != null && cd.isShowing())
			cd.dismiss();
		Log.d(TAG, "Activity quitting");
	}

	private void FINISH_ACTIVITY() 
	{
		NMApp.getController().clearOutboxHandler();
		if (cd != null && cd.isShowing())
			cd.dismiss();
		NMApp.getController().dispose();
		Log.d(TAG, "Activity quitting");
		SessionManager.clean();
		NMApp.killApp(false);
		finish();
	}

}