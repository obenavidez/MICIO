package com.panzyma.nm.viewdialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.panzyma.nm.Main;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nordismobile.R;

@SuppressWarnings({"rawtypes","unused"})
public class DialogLogin extends Dialog
{
	private EditText txtenterprise;
	private EditText txtusername;
	private EditText txtpassword;
	private Button signin; 
	private Button cancel;  
	private Controller controller;
	private OnButtonClickListener mButtonClickListener;
	private String TAG=DialogLogin.class.getSimpleName();
	private Context mycontext; 
	boolean admin;
	public interface OnButtonClickListener {
		public abstract void onButtonClick(boolean btn);
	}
	
    public void setOnDialogLoginButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
 
	public DialogLogin(Context context,boolean isadmin) 
	{
		super(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen); 
		try
		{ 
			mycontext=context;
			admin=isadmin;
		    setContentView(R.layout.login);
			initComponents(); 
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void clean()
	{
		txtenterprise.setText("");
		txtusername.setText("");
		txtpassword.setText("");  
	}

	public  String getNameUser()
	{ 
		return txtusername.getText().toString();
	} 
	
	public  String getPassword()
	{
		return txtpassword.getText().toString();
	} 
	
	public  String getEmpresa()
	{
		return txtenterprise.getText().toString();
	} 
	 
	public void setContentViewToDialog(int layout)
	{
		setContentView(layout); 
	 
	}
	 
	public void initComponents()
	{	      
		this.setCancelable(true); 
	    this.setCanceledOnTouchOutside(true); 
	    
	    signin=((Button)findViewById(R.id.btnsignin)); 
	    cancel=((Button)findViewById(R.id.btncancel)); 
	    txtenterprise=((EditText)findViewById(R.id.etenterprise));  
	    txtusername=((EditText)findViewById(R.id.etusername)); 
	    txtpassword=((EditText)findViewById(R.id.etpassword));  
	    if(admin)
	    {
	    	((TextView)findViewById(R.id.tvsign)).setText("Admin Login"); 
	    	TableRow buttonsrow=((TableRow)findViewById(R.id.layoutbutton));
	    	android.widget.RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW,R.id.etpassword);			  
			buttonsrow.setGravity(Gravity.CENTER_HORIZONTAL);
			buttonsrow.setLayoutParams(params);   
	    }	     
	    
	    String[] session=SessionManager.getSession();
		if(session!=null || (session!=null && session.length!=0)) 
	    { 			
			if(NMApp.getContext() instanceof Main)
			{
				txtenterprise.setText(session[0]);
		    	txtenterprise.setEnabled(true);  
		    	txtusername.setText(session[1]);
		    	txtusername.setEnabled(true); 
		    	txtpassword.requestFocus();
			}
			else
			{
		    	txtenterprise.setText(session[0]);
		    	txtenterprise.setEnabled(false);  
		    	txtusername.setText(session[1]);
		    	txtusername.setEnabled(false); 
		    	txtpassword.requestFocus();
	    	}
	    }
	    signin.setOnClickListener(new View.OnClickListener() 
		{ 
    	    @Override
			public void onClick(View v) 
			{   
	    		if(isValidInformation())
    	    	{     	    	 
			 	    try 
			 	    { 		 
			 	    	mButtonClickListener.onButtonClick(true);
			 	    } catch (Exception e) { 
						e.printStackTrace();
					}
				} 
			} 
		}
	    );
	    cancel.setOnClickListener(new View.OnClickListener() 
		{				
			@Override
			public void onClick(View v) 
			{ 
				mButtonClickListener.onButtonClick(false);
				FINISH_ACTIVITY();
			} 
		}
	    );
  
	} 
	
	public boolean isValidInformation()
	{
		String msg = "";
		
		if (txtenterprise.getText().toString().trim().length()==0){
        		msg = "Ingrese el nombre de la Empresa en la cual labora.";
        		txtenterprise.setError(msg);
        		txtenterprise.requestFocus();
        		return false;
        }
        else if (txtusername.getText().toString().trim().length()==0){
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
        
		return true;
	} 

	private void FINISH_ACTIVITY()
	{  
		Log.d(TAG, "Activity quitting"); 
		this.dismiss();
	}  
	
	
}
