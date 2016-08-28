package com.panzyma.nm.auxiliar;
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AppDialog  extends DialogFragment  implements OnDismissListener{
	public static final int OK_BUTTOM=11;
	public static final int NO_BUTTOM=22;
	static Object lock=new Object();
	static String Message=null;
	static String Tittle=null; 
	private static android.support.v4.app.FragmentManager manager;
	public static enum DialogType 
	{
		 DIALOGO_NOTIFICACION (0),
		 DIALOGO_ALERTA (1),
		 DIALOGO_CONFIRMACION (2),
		 DIALOGO_SELECCION (3),
		 DIALOGO_DINAMICO(4),
		 DIALOGO_INPUT(5),
		 DIALOGO_OCADISCOUNT(6),
		 DIALOGO_NOTIFICACION2 (10);
		 
		 int result;
		 DialogType(int result) {
		 this.result = result;
		 }
		 public int getActionCode() {
		 return result;
		 }
		 public static DialogType toInt(int x) {
		 return DialogType.values()[x];
		 }
	}
	static AlertDialog alert = null;
	static View vDialog;
	static Builder mybuilder;
	static LayoutInflater inflater;
	static TextView tvtittle;
	static TextView tvmessage;
	static Button btn_aceptar;
	static Button btn_cancelar;
	static EditText txtpayamount;
	static EditText tbox_discoutnkey;
	static EditText tbox_percentcollector;
	public OnButtonClickListener mButtonClickListener;
	private OnDismissDialogListener mDismissListener;
	
	public interface OnButtonClickListener {
		public abstract void onButtonClick(AlertDialog alert,int actionId);
	}
	
	public interface OnDismissDialogListener {
		public abstract void onDismiss();
	}
	
	public void setOnDialogButtonClickListener(OnButtonClickListener listener){
		mButtonClickListener = listener;
	}

	public void setOnDismissDialogListener(OnDismissDialogListener listener) {  
		mDismissListener = listener;
	}
	
	public static void showMessage (Context mContext,String msg, DialogType type)
	{
		mybuilder = new AlertDialog.Builder(mContext);
		//inflater = mContext.getLayoutInflater();
		inflater = LayoutInflater.from(mContext);
		Message = msg;
		switch(type)
		{
			case DIALOGO_ALERTA :
				CreateDialog(null);
			break;
		case DIALOGO_CONFIRMACION :
				CreateConfirmDialog(null);
			break;
		case  DIALOGO_INPUT:
			CreatePayAmountDialog(null);
		case  DIALOGO_OCADISCOUNT:CreateOcationalDiscountDialog(null);
		break;
		default:
			break;
			
		}
		alert = mybuilder.create();
		alert.setCancelable(false);
		int margen = -2;
		alert.setView(vDialog, margen, margen, margen, margen);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.show();
	}
	
	public static void showMessage (Context mContext,String title,String msg, DialogType type)
	{
		mybuilder = new AlertDialog.Builder(mContext);
			
		//inflater = mContext.getLayoutInflater();
		inflater = LayoutInflater.from(mContext);
		Tittle = title;
		Message = msg;
		switch(type)
		{
			case DIALOGO_ALERTA :
				CreateDialog(null);
			break;
		case DIALOGO_CONFIRMACION :
				CreateConfirmDialog(null);
			break;
		case  DIALOGO_INPUT:
			CreatePayAmountDialog(null);
		case  DIALOGO_OCADISCOUNT:CreateOcationalDiscountDialog(null);
		break;
		default:
			break;
			
		}
		alert = mybuilder.create();
		if(type == DialogType.DIALOGO_ALERTA)
			alert.setCancelable(true);
		else 
			alert.setCancelable(false);		
		int margen = -2;
		alert.setView(vDialog, margen, margen, margen, margen);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.show();
	}
		
	public static AlertDialog _showMessage(Context mContext,String title,String msg, DialogType type ,final OnButtonClickListener mylistener)
	{
		mybuilder = new AlertDialog.Builder(mContext);
		//inflater = mContext.getLayoutInflater();
		inflater = LayoutInflater.from(mContext);
		Tittle = title;
		Message = msg;
		switch(type)
		{
			case DIALOGO_ALERTA :
				CreateDialog(mylistener);
			break;
			case DIALOGO_CONFIRMACION :
				CreateConfirmDialog(mylistener);
			break;
			case DIALOGO_INPUT :
				CreatePayAmountDialog(mylistener);
				break;
			case  DIALOGO_OCADISCOUNT:CreateOcationalDiscountDialog(mylistener);
			default:
			break;
			
		}
		//mybuilder.setView(vDialog);
		alert = mybuilder.create();
		alert.setCancelable(false);
		int margen = -2;
		alert.setView(vDialog, margen, margen, margen, margen);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return alert;
	}
	
	public static void showMessage (Context mContext,String title,String msg, DialogType type ,final OnButtonClickListener mylistener)
	{
		mybuilder = new AlertDialog.Builder(mContext);
		//inflater = mContext.getLayoutInflater();
		inflater = LayoutInflater.from(mContext);		
		Tittle = title;
		Message = msg;
		switch(type)
		{
			case DIALOGO_ALERTA :
				CreateDialog(mylistener);
			break;
			case DIALOGO_CONFIRMACION :
				CreateConfirmDialog(mylistener);
			break;
			case DIALOGO_INPUT :
				CreatePayAmountDialog(mylistener);
				break;
			case  DIALOGO_OCADISCOUNT:CreateOcationalDiscountDialog(mylistener);
			default:
			break;
			
		}
		//mybuilder.setView(vDialog);
		alert = mybuilder.create();
		alert.setCancelable(false);
		int margen = -2;
		alert.setView(vDialog, margen, margen, margen, margen);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.show();
	}
	
	private static void CreateConfirmDialog(final OnButtonClickListener mylistener)
	{
		vDialog =inflater.inflate(R.layout.confirm_dialog2, null,false);
		tvmessage =(TextView)vDialog.findViewById(R.id.message_dialog);
		tvmessage.setText(Message.toString());
		btn_aceptar = (Button)vDialog.findViewById(R.id.btnaceptar_dialog_confirm);
		btn_aceptar.setOnClickListener(new Button.OnClickListener()
    	{
			@Override
			public void onClick(View v) 
			{
				if(mylistener!=null)
					mylistener.onButtonClick(alert, OK_BUTTOM); 
				alert.dismiss();
			}	
    	});
		btn_cancelar = (Button) vDialog.findViewById(R.id.btncancelar_dialog_confirm);
		btn_cancelar.setOnClickListener( new Button.OnClickListener()
	    {
			@Override
			public void onClick(View v) {
				mylistener.onButtonClick(alert, NO_BUTTOM); 
				alert.dismiss();
			}
	    });
		
	}
	
	private static void CreateDialog(final OnButtonClickListener mylistener)
	{
		try {
			vDialog =inflater.inflate(R.layout.alert_dialog2, null, false);
			tvmessage =(TextView)vDialog.findViewById(R.id.bodymessage_dialog_alert);
			tvmessage.setText(Message.toString());
			btn_aceptar = (Button)vDialog.findViewById(R.id.btnaceptar_dialog_alert);
			btn_aceptar.setVisibility(View.INVISIBLE);
			((Builder) mybuilder).setNeutralButton("Aceptar", new DialogInterface.OnClickListener() { // define the 'Cancel' button
			    public void onClick(DialogInterface dialog, int which) {
			        //Either of the following two lines should work.
			    	if(mylistener!=null){
						mylistener.onButtonClick(alert, OK_BUTTOM);
					} 
			        dialog.cancel();
			        //dialog.dismiss();
			    } 
			});
			/*btn_aceptar.setOnClickListener(new Button.OnClickListener()
	    	{
				@Override
				public void onClick(View v) {
					if(mylistener!=null){
						mylistener.onButtonClick(alert, OK_BUTTOM);
					} 
					if(alert != null && alert.isShowing())
						alert.dismiss();				
				}	
	    	});*/
			/*mybuilder.setView(vDialog);
			alert = mybuilder.create();*/
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
		
	private static void CreateOcationalDiscountDialog(final OnButtonClickListener mylistener){
		vDialog =inflater.inflate(R.layout.oca_discount_dialog, null,false);
		tvtittle = (TextView)vDialog.findViewById(R.id.title_ocadiscount);
		tvtittle.setText(Tittle.toString());
		tbox_discoutnkey =(EditText) vDialog.findViewById(R.id.editkey);
		btn_aceptar = (Button)vDialog.findViewById(R.id.btnaceptar_dialog_confirm);
		btn_aceptar.setOnClickListener(new Button.OnClickListener()
    	{
			@Override
			public void onClick(View v) {
				if(tbox_discoutnkey.getText().length()>0)
				{
					if(tbox_discoutnkey.getText().toString().compareTo("0")==1)
					{
						if(mylistener!=null)
							mylistener.onButtonClick(alert, OK_BUTTOM); 
						alert.dismiss();
					}
				}
			}	
    	});
		btn_cancelar = (Button) vDialog.findViewById(R.id.btncancelar_dialog_confirm);
		btn_cancelar.setOnClickListener( new Button.OnClickListener()
	    {
			@Override
			public void onClick(View v) {
				if(mylistener!=null)
					mylistener.onButtonClick(alert, NO_BUTTOM); 
				alert.dismiss();
			}
	    });
		
	}
	
	private static void CreatePayAmountDialog(final OnButtonClickListener mylistener){
		vDialog =inflater.inflate(R.layout.dialog_pay_amount, null,false);
		tvtittle = (TextView)vDialog.findViewById(R.id.title_pay_amount);
		tvtittle.setText(Tittle.toString());
		btn_aceptar = (Button)vDialog.findViewById(R.id.btnaceptar_dialog_confirm);
		btn_aceptar.setOnClickListener(new Button.OnClickListener()
    	{
			@Override
			public void onClick(View v) {
				if(txtpayamount.getText().length()>0){
					if(txtpayamount.getText().length()>0){
						if(mylistener!=null)
							mylistener.onButtonClick(alert, OK_BUTTOM); 
						alert.dismiss();
					}
				}
			}	
    	});
		btn_cancelar = (Button) vDialog.findViewById(R.id.btncancelar_dialog_confirm);
		btn_cancelar.setOnClickListener( new Button.OnClickListener()
	    {
			@Override
			public void onClick(View v) {
				if(mylistener!=null)
					mylistener.onButtonClick(alert, NO_BUTTOM); 
				alert.dismiss();
			}
	    });
		txtpayamount =(EditText) vDialog.findViewById(R.id.txtpayamount);
	}
		
	@Override	
	public void onDestroyView() {
	  if (getDialog() != null && getRetainInstance())
	    getDialog().setOnDismissListener(null);
	  super.onDestroyView();
	}
	
	@Override
	public void onResume()
	{
	  super.onResume();       
	  getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	  setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		mDismissListener.onDismiss();
	}	
	
	

    
}
