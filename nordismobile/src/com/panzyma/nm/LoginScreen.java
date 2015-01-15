package com.panzyma.nm;

import com.panzyma.nm.auxiliar.Session;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.viewdialog.DialogLogin;
import com.panzyma.nm.viewdialog.DialogLogin.OnButtonClickListener;
import com.panzyma.nordismobile.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LoginScreen extends DashBoardActivity implements Handler.Callback {

	private EditText txtenterprise;
	private EditText txtusername;
	private EditText txtpassword;
	private Button signin;
	private Button cancel;
	private Controller controller;
	private OnButtonClickListener mButtonClickListener;
	private String TAG = DialogLogin.class.getSimpleName();
	private Context mycontext;
	boolean admin;
	private Intent intent;

	public interface OnButtonClickListener {
		public abstract void onButtonClick(boolean btn);
	}

	public void setOnDialogLoginButtonClickListener(
			OnButtonClickListener listener) {
		mButtonClickListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (UserSessionManager.getLoginUser() == null) {
			NMApp.modulo = NMApp.Modulo.CONFIGURACION;
			intent = new Intent(this, ViewConfiguracion.class);
			intent.putExtra("isEditActive", true);
			startActivity(intent);
		} else if (UserSessionManager.isUserLoggedIn()) {
			NMApp.modulo = NMApp.Modulo.HOME;
			// user is not logged in redirect him to Login Activity
			intent = new Intent(this, Main.class);

			// Closing all the Activities from stack
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			startActivity(intent);
		} else {
			setContentView(R.layout.screen_login);
			NMApp.getController().setView(this);
			initComponents();
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
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

	

	public void initComponents()
	{	      
	    View layout=(View)findViewById(R.id.loggin);
	    signin=((Button)layout.findViewById(R.id.btnsignin)); 
	    cancel=((Button)layout.findViewById(R.id.btncancel));  
	    txtusername=((EditText)layout.findViewById(R.id.etusername)); 
	    txtpassword=((EditText)layout.findViewById(R.id.etpassword));   	 
	    
	    signin.setOnClickListener(new View.OnClickListener() 
		{ 
    	    @Override
			public void onClick(View v) 
			{   
	    		if(isValidInformation())
    	    	{     	    	 
	    			UserSessionManager.guardarSession(new Session(UserSessionManager.getLoginUser(),true));
	    			
				} 
			} 
		}
	    );
	    cancel.setOnClickListener(new View.OnClickListener() 
		{				

			@Override
			public void onClick(View v) {
				mButtonClickListener.onButtonClick(false);
				FINISH_ACTIVITY();
			}
		});

	}

	/*public boolean isValidInformation() {
		String msg = "";
		if (txtusername.getText().toString().trim().length() == 0) {
			msg = "Ingrese un usuario válido.";
			txtusername.setError(msg);
			txtusername.requestFocus();
			return false;
		} else if (txtpassword.getText().toString().trim().length() == 0) {
			msg = "La contraseña invalida";
			txtpassword.setError(msg);
			txtpassword.requestFocus();
			return false;
		}

	   
  
	} */
	
	public boolean isValidInformation()
	{
		String msg = ""; 
		if (txtusername.getText().toString().trim().length()==0){
                msg = "Ingrese un usuario válido.";
                txtusername.setError(msg);
                txtusername.requestFocus();
                return false;
	    }
        else if (txtpassword.getText().toString().trim().length()==0){
                msg = "La contraseña invalida";     
                txtpassword.setError(msg);
                txtpassword.requestFocus();
                return false;
        }  
        else if(!(UserSessionManager.checkLogin(txtusername.getText().toString().trim(), txtpassword.getText().toString().trim())))
        	return false;
		return true;
	} 


	private void FINISH_ACTIVITY() {
		Log.d(TAG, "Activity quitting");
	}	

}
