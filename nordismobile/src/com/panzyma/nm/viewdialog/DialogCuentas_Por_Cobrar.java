package com.panzyma.nm.viewdialog;

import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nordismobile.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class DialogCuentas_Por_Cobrar extends DialogFragment  implements  Handler.Callback {

	
	//Variables
	private static DialogCuentas_Por_Cobrar cuentasporcobrar ;
	private static AlertDialog dialog =null;
	private static final int MOSTRAR_FACTURAS = 0;
	private QuickAction quickAction;
	private View view;
	private Button btnMenu;
	private Display display;
	
	public enum TypeDetail { FACTURA, NOTAS_DEBITO, NOTAS_CREDITO }
	
	public static DialogCuentas_Por_Cobrar  Instancia (){
		if(cuentasporcobrar == null) cuentasporcobrar = new DialogCuentas_Por_Cobrar();
		return cuentasporcobrar;
	}

	
	
	
	@Override
	public void onStart() {
		btnMenu = (Button) getActivity().findViewById(R.id.btnMenu);
		if (dialog != null){
			 dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		initMenu();
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		super.onStart();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.cuentas_x_cobrar, null);
		builder.setView(view);
		builder.setOnKeyListener(keyListener);
		dialog = builder.create();
		
		return dialog;
	}
	
	
	
//	public void onResume(){
//        super.onResume();
//        Window window = getDialog().getWindow();
//        window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);       
//        window.setGravity(Gravity.CENTER);
//    }
	
	
	OnKeyListener keyListener = new OnKeyListener() 
	{ 
		  @Override
		  public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) 
		  {
			if (keyCode == KeyEvent.KEYCODE_BACK) 
			{        	
				Toast.makeText(getActivity(), "BACK", 1000);
			  	dismiss();
			    return true;
			}
			if (keyCode == KeyEvent.KEYCODE_MENU) {	
				quickAction.show(btnMenu, display, true);	
			}
			
			return false;	 
		  } 
	};



	
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	} 
	
	
	private void initMenu() {
		quickAction = new QuickAction(this.getActivity(), QuickAction.VERTICAL, 1);
		quickAction.addActionItem(new ActionItem(MOSTRAR_FACTURAS, "Mostrar Facturas"));
		
	}
	
}
