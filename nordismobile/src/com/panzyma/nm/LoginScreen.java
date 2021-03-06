package com.panzyma.nm;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;

import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Session;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.viewdialog.DialogLogin;
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends ActionBarActivity implements Handler.Callback {

	private EditText txtenterprise;
	private EditText txtusername;
	private EditText txtpassword;
	private Button signin;
	private Button cancel;
	private String TAG = DialogLogin.class.getSimpleName();
	private Context mycontext;
	private CustomDialog cd;
	private Intent intent;
	private static CustomDialog dlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mycontext = this;
		NMApp.ciclo = NMApp.lifecycle.ONCREATE;
		validateSession();
	}

	public void validateSession() 
	{
		if (UserSessionManager.isUserLoggedIn())
			goHome();
		else 
		{
			setContentView(R.layout.screen_login);
			NMApp.getController().setView(this);
			UserSessionManager._context = this;
			initComponents();
		}
	}

	public void goHome() {
		NMApp.modulo = NMApp.Modulo.HOME;
		// user is not logged in redirect him to Login Activity
		intent = new Intent(this, Main.class); 
		// Staring Login Activity
		startActivity(intent);
		finish();
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
			AppDialog.showMessage(this, ((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			break;
		case ControllerProtocol.SETTING_REDIREC:
			goToConfiguration();
		}
		return false;
	}

	public void goToConfiguration() {
		NMApp.modulo = NMApp.Modulo.CONFIGURACION;
		Intent intent = new Intent(NMApp.getContext(), ViewConfiguracion.class);
		intent.putExtra("isEditActive", true);
		startActivity(intent);
	}

	public void ocultarDialogos() {
		if (cd != null && cd.isShowing())
			cd.dismiss();
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

	public void showStatus(final String mensaje, boolean... confirmacion) {
		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(mycontext, "", mensaje,
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
					dlg = new CustomDialog(mycontext, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}

	public void clean() {
		txtenterprise.setText("");
		txtusername.setText("");
		txtpassword.setText("");
	}

	public String getNameUser() {
		return txtusername.getText().toString();
	}

	public String getPassword() {
		return txtpassword.getText().toString();
	}

	public String getEmpresa() {
		return txtenterprise.getText().toString();
	}

	public void setContentViewToDialog(int layout) {
		setContentView(layout);
	}

	public void initComponents() {
		View layout = findViewById(R.id.loggin);
		signin = ((Button) layout.findViewById(R.id.btnsignin));
		cancel = ((Button) layout.findViewById(R.id.btncancel));
		txtusername = ((EditText) layout.findViewById(R.id.etusername));
		txtpassword = ((EditText) layout.findViewById(R.id.etpassword));

		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isValidInformation()) {
					try {
						NMApp.getThreadPool().execute(new Runnable() {
							@Override
							public void run() 
							{
								if (UserSessionManager.checkLogin(txtusername.getText().toString().trim(), txtpassword.getText().toString().trim())) {
									UserSessionManager.guardarSession(new Session(UserSessionManager.getLoginUser(),true));
									if (UserSessionManager.isUserLoggedIn())
										goHome();
								}
								//goHome();
							}
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FINISH_ACTIVITY();
			}
		});

	}

	/*
	 * public boolean isValidInformation() { String msg = ""; if
	 * (txtusername.getText().toString().trim().length() == 0) { msg =
	 * "Ingrese un usuario v�lido."; txtusername.setError(msg);
	 * txtusername.requestFocus(); return false; } else if
	 * (txtpassword.getText().toString().trim().length() == 0) { msg =
	 * "La contrase�a invalida"; txtpassword.setError(msg);
	 * txtpassword.requestFocus(); return false; }
	 * 
	 * 
	 * 
	 * }
	 */

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FINISH_ACTIVITY();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
  
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		ocultarDialogos();
		
		if(NMApp.ciclo==NMApp.lifecycle.ONPAUSE || NMApp.ciclo==NMApp.lifecycle.ONRESTART)
		{
			NMApp.getController().setView(this);
			SessionManager.setContext(this);
			UserSessionManager._context = this;
			validateSession(); 
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
	
	public boolean isValidInformation() {
		String msg = "";
		if (txtusername.getText().toString().trim().length() == 0) {
			msg = "Ingrese un usuario v�lido.";
			txtusername.setError(msg);
			txtusername.requestFocus();
			return false;
		} else if (txtpassword.getText().toString().trim().length() == 0) {
			msg = "La contrase�a invalida";
			txtpassword.setError(msg);
			txtpassword.requestFocus();
			return false;
		}

		return true;
	}

	private void FINISH_ACTIVITY() {
		Log.d(TAG, "Activity quitting");
		finish();
	}

}
