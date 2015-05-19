package com.panzyma.nm.viewdialog;
     
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.Ammount;
import com.panzyma.nm.auxiliar.AmmountType;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nordismobile.R;
 




import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;  
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;

public class DevolverDocumento extends DialogFragment implements Handler.Callback
{	
	private static final String TAG = DevolverDocumento.class.getSimpleName();
	private DialogListener listener;
	private Button btnagregar;
	private Button btncancelar;
	private static Handler.Callback parent;
	private static DevolverDocumento dd;
	protected EditText tboxPedido;
	protected EditText tboxFactura;
	private static long objSucursalID;
	public interface DialogListener 
	{
		public void onDialogPositiveClick(Devolucion dev); 
	} 
 
	public static DevolverDocumento newInstance(Handler.Callback _parent,long _objSucursalID) 
	{
		if(dd==null)
			dd = new DevolverDocumento(); 
		parent=_parent;
		objSucursalID=_objSucursalID;
	    return dd;
	}

	public void setOnDialogClickListener(DialogListener _listener)
	{
		listener = _listener;
	} 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.devolver_documento, null);
		tboxPedido=(EditText) view.findViewById(R.id.dev_tboxPedido);
		tboxFactura=(EditText) view.findViewById(R.id.dev_tboxFactura);
		TableRow row=(TableRow) view.findViewById(R.id.devlayoutbutton);
		builder.setView(view); 
		btnagregar=(Button) row.findViewById(R.id.btnOK);
		btncancelar=(Button) row.findViewById(R.id.btnCancel);
		
		btnagregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{ 
				HashMap<String,Long> parametros = new HashMap<String,Long>();				
				Message m=new Message();
				m.what=ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO;
				parametros.put("idsucursal",objSucursalID);
				parametros.put("nopedido",(long)((tboxPedido.getText()!=null && (!tboxPedido.getText().equals("")))?Long.valueOf(tboxPedido.getText().toString()):0));
				parametros.put("nofactura",(long)((tboxFactura.getText()!=null && (!tboxFactura.getText().toString().equals("")))?Long.valueOf(tboxFactura.getText().toString()):0));
				m.obj=parametros;
				NMApp.getController().getInboxHandler().sendMessage(m);
			}
		});
		
		btncancelar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{ 
				dismiss();
			}
		});		
		
		builder.setOnKeyListener(keyListener); 
		Dialog mydialog=builder.create(); 
		return mydialog;
	}
	
    OnKeyListener keyListener = new OnKeyListener() 
   	{ 
   		  @Override
   		  public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) 
   		  {
   			if (keyCode == KeyEvent.KEYCODE_BACK) 
   			{        	
   			  	dismiss();
   			    return true;
   			}		  
   			return false;	 
   		  } 
   	};
   	
   	public void initComponent()
   	{   		 
		com.panzyma.nm.NMApp.getController().setView(this);
   	} 
   	
	@Override
	public boolean handleMessage(Message msg) { 
		return false;
	} 

	@Override
	public void onDismiss(DialogInterface dialog) 
   	{   		
   		FINISH_ACTIVITY();
		super.onDismiss(dialog);
	}
	
	private void FINISH_ACTIVITY()
	{
		com.panzyma.nm.NMApp.getController().setView(parent);		 
		Log.d(TAG, "Quitting"+ TAG); 
	}
}
