package com.panzyma.nm;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Session;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ProductoView;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.view.ViewPedido;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.vCliente;
import com.panzyma.nordismobile.R;

@SuppressLint("ShowToast")
@SuppressWarnings({ "unused" })
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
	private static CustomDialog dlg;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		NMApp.getController().setView(this);
		
		setContentView(R.layout.home);
		
		setHeader(getString(R.string.HomeActivityTitle), false, true);
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		context = this;
		if ((savedInstanceState != null) ? savedInstanceState
				.getBoolean("dl_visible") : false)

		NMApp.modulo = NMApp.Modulo.HOME;
	}


	@Override
	protected void onDestroy() {

		if (SessionManager.dl != null)
			SessionManager.dl.dismiss();

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

		if (SessionManager.dl != null) {
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

		NMApp.modulo = NMApp.Modulo.HOME;
		buttonActive = v.getId();
		Session session=UserSessionManager.getSession();
		if (session!=null && session.isLoged()) 
		{
			switch (v.getId()) {

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
				NMApp.modulo = NMApp.Modulo.CONFIGURACION;
				intent = new Intent(context,ViewConfiguracion.class);
				intent.putExtra("isEditActive",true);
				startActivity(intent);
				break;
			default:
				break;
			}
		} else 	FINISH_ACTIVITY(); 
	}
	
	public void dialogLogin(){
		try 
		{
			NMApp.getThreadPool().execute(new Runnable() 
			{
				@Override
				public void run() 
				{
					try 
					{
						NMApp.modulo = NMApp.Modulo.HOME;
						if(!SessionManager.SignIn(false, false))
							if(!SessionManager.hasError){
								NMApp.killApp(true);
							}
					} catch (Exception e) {
						NMApp.modulo = NMApp.Modulo.HOME;
						e.printStackTrace();
						dialog("", SessionManager.getErrorAuntentication(),
								ALERT_DIALOG);
					}

				}

			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("ParserError")
	public void callDialogLogin() {
		try {
			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						Usuario user = SessionManager.getLoginUser();
						//if (user != null && SessionManager.SignInForce()) {
						if (user != null && SessionManager.isLogged()) {
							NMApp.modulo = NMApp.Modulo.CONFIGURACION;
							intent = new Intent(context,
									ViewConfiguracion.class);
							intent.putExtra("isEditActive",
									(user != null) ? SessionManager.isAdmin()
											: true);
							startActivity(intent);
							FINISH_COMPONENT();
						} else {
							Log.d(TAG, "Error in login");
							NMApp.modulo = NMApp.Modulo.HOME;
						}
					} catch (Exception e) {
						NMApp.modulo = NMApp.Modulo.HOME;
						e.printStackTrace();
						dialog("", SessionManager.getErrorAuntentication(),
								ALERT_DIALOG);
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

		ocultarDialogos();
		switch (msg.what) {

		case ControllerProtocol.NOTIFICATION:
			showStatus(msg.obj.toString(), true);
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			showStatus(msg.obj.toString());
			break;
		case ERROR:
			AppDialog.showMessage(context,
					((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			break;
		}
		return false;
	}

	public void showStatus(final String mensaje, boolean... confirmacion) {
		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(context, "", mensaje,
							AppDialog.DialogType.DIALOGO_ALERTA,
							new AppDialog.OnButtonClickListener() {
								@Override
								public void onButtonClick(AlertDialog _dialog,
										int actionId) {

									if (AppDialog.OK_BUTTOM == actionId) {
										_dialog.dismiss();
									}
								}
							});
				}
			});
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dlg = new CustomDialog(context, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}

	public void ocultarDialogos() {
		if (cd != null && cd.isShowing())
			cd.dismiss();
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

	@Override
	protected void onResume() {
		NMApp.modulo = NMApp.Modulo.HOME; 
		ocultarDialogos();		
		if(NMApp.ciclo==NMApp.lifecycle.ONPAUSE || NMApp.ciclo==NMApp.lifecycle.ONRESTART)
		{
			NMApp.getController().setView(this);
			SessionManager.setContext(this);
			UserSessionManager._context = this;
		}
		NMApp.ciclo=NMApp.lifecycle.ONRESUME;
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		NMApp.ciclo=NMApp.lifecycle.ONPAUSE;
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		NMApp.ciclo=NMApp.lifecycle.ONSTOP;
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		NMApp.ciclo=NMApp.lifecycle.ONRESTART;
		super.onRestart();
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

	private void FINISH_ACTIVITY() { 
		if (cd != null && cd.isShowing())
			cd.dismiss(); 
		Log.d(TAG, "Activity quitting");
		SessionManager.clean();
		UserSessionManager.logoutUser();		
		finish();
	}

}