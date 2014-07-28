package com.panzyma.nm.viewdialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nordismobile.R;

@SuppressLint("ValidFragment")
public class GenericCustomDialog  extends DialogFragment implements OnDismissListener{

	private Builder mBuilder;
	public static final int OK_BUTTOM=11;
	public static final int NO_BUTTOM=22;
	//VARIABLES
	private String Message=null;
	private String Tittle=null; 
	private int Dialogtype;  
	
	// UX Design
	View vDialog ;
	Button btn_aceptar;
	Button btn_cancelar;
	TextView tvtittle;
	TextView tvmessage;
	private OnActionButtonClickListener mButtonClickListener;
	private OnDismissDialogListener mDismissListener;
	//CONSTRUCTORS
	public GenericCustomDialog(){}
	@SuppressLint("ValidFragment")
	public GenericCustomDialog(String title , String message)
	{
		this.Tittle = title;
		this.Message = message;
	}
	public GenericCustomDialog(String title , String message, DialogType dialogtype)
	{
		this.Tittle = title;
		this.Message = message;
		this.Dialogtype =dialogtype.getActionCode();
	}
	
	
	public static GenericCustomDialog newInstance(String title , String message,DialogType  dialogtype) {
		GenericCustomDialog fragment = new GenericCustomDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putInt("dialogtype", dialogtype.getActionCode());
		fragment.setArguments(args);
		//fragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        return fragment;
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		Dialogtype = getArguments().getInt("dialogtype");
		DialogType tipo = DialogType.toInt(Dialogtype);
		Tittle=getArguments().get("title").toString();
		Message=getArguments().get("message").toString();
		switch(tipo)
		{
			case DIALOGO_ALERTA :
				vDialog =inflater.inflate(R.layout.alert_dialog, null);
				tvtittle = (TextView)vDialog.findViewById(R.id.tittle_dialog_alert);
				tvtittle.setText(Tittle.toString());
				tvmessage =(TextView)vDialog.findViewById(R.id.bodymessage_dialog_alert);
				tvmessage.setText(Message.toString());
				btn_aceptar = (Button)vDialog.findViewById(R.id.btnaceptar_dialog_alert);
				btn_aceptar.setOnClickListener(new Button.OnClickListener()
			        	{
				            @Override
							public void onClick(View v) 
				            {
				            	mButtonClickListener.onButtonClick(vDialog,OK_BUTTOM); 
				            	dismiss();
				            }
			        	}); 
				mBuilder.setView(vDialog);
				mBuilder.setCancelable(false);
				break;
			case DIALOGO_CONFIRMACION :
				vDialog =inflater.inflate(R.layout.confirm_dialog, null);
				tvtittle = (TextView)vDialog.findViewById(R.id.tittle_dialog_confirm);
				tvtittle.setText(Tittle.toString());
				tvmessage =(TextView)vDialog.findViewById(R.id.bodymessage_dialog_confirm);
				tvmessage.setText(Message.toString());
				btn_aceptar = (Button)vDialog.findViewById(R.id.btnaceptar_dialog_confirm);
				btn_aceptar.setOnClickListener( 	new Button.OnClickListener()
	        	{
		            @Override
					public void onClick(View v) 
		            {
		            	mButtonClickListener.onButtonClick(vDialog,OK_BUTTOM); 
		            	dismiss();
		            }
	        	});
				btn_cancelar = (Button) vDialog.findViewById(R.id.btncancelar_dialog_confirm);
				btn_cancelar.setOnClickListener( new Button.OnClickListener()
			    {
					@Override
					public void onClick(View v) {
						mButtonClickListener.onButtonClick(vDialog,NO_BUTTOM);
						dismiss();
					}
			    });
				
				mBuilder.setView(vDialog);
				mBuilder.setCancelable(false);
				break;	
			case DIALOGO_DINAMICO:
				//vDialog =inflater.inflate(R.layout.information_dialog, null);
				mBuilder.setView(vDialog);
				mBuilder.setIcon(R.drawable.clientes);
				mBuilder.setTitle("Texto Informativo");
				mBuilder.setMessage("Este es un texto Informativo!");
				mBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog, int id) {
				    	mButtonClickListener.onButtonClick(vDialog,NO_BUTTOM);
						dismiss();
				    }
				});
				mBuilder.setCancelable(false);
				break;
		
		}
		return mBuilder.create();
	}
	
	@Override
	public void onDestroyView() {
	  if (getDialog() != null && getRetainInstance())
	    getDialog().setOnDismissListener(null);
	  super.onDestroyView();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		mDismissListener.onDismiss();
	}	
	//INTERFACES
	public interface OnActionButtonClickListener {
		public abstract void onButtonClick(View _dialog,int actionId);
	}
	
    public void setOnActionDialogButtonClickListener(OnActionButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
	
    public interface OnDismissDialogListener {
		public abstract void onDismiss();
	}
    
	public void setOnDismissDialogListener(OnDismissDialogListener listener) {  
		mDismissListener = listener;
	}
	
}
