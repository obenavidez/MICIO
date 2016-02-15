package com.panzyma.nm.auxiliar;
  
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.panzyma.nordismobile.R;

@SuppressLint("ParserError")
public class CustomDialog extends Dialog implements OnDismissListener
{	 
	private String message=null;
	private String tittle=null; 
	private int dialoID; 
	private boolean imple_interfc;
	private static final int DIALOGO_NOTIFICACION = 0;
	private static final int DIALOGO_NOTIFICACION2 = 10;
	private static final int DIALOGO_ALERTA = 1;
	private static final int DIALOGO_CONFIRMACION = 2;
	private static final int DIALOGO_SELECCION = 3; 
	private static final int DIALOGO_DINAMICO=4;
	
	public static final int OK_BUTTOM=11;
	public static final int NO_BUTTOM=22;
	
	int layout;
	View viewdialog;
	private Context cnt;
	
	public static CustomDialog _dialog;
	
	public static CustomDialog nuevaInstancia(Context context,String mensaje,boolean interfac,int... mensajetype){
		if(_dialog==null)
			_dialog=new CustomDialog(context,mensaje,interfac,mensajetype);
		else
		{
			_dialog.setContext(context);
			_dialog.setMensaje(mensaje);
			if(mensajetype!=null)
				_dialog.setMessageType(mensajetype[0]);	
			_dialog.setCancelable(true);
			_dialog.setCanceledOnTouchOutside(true);
			
			_dialog.setInterface(interfac);
			_dialog.initDialog();
		}
		return _dialog;
	}
	
	public static CustomDialog nuevaInstancia(Context context,String titulo,String mensaje,int... mensajetype){
		if(_dialog==null)
			_dialog=new CustomDialog(context,titulo,mensaje,mensajetype);
		else
		{ 
			_dialog.setContext(context);
			_dialog.setMensaje(mensaje);
			_dialog.setTitulo(titulo);
			if(mensajetype!=null)
				_dialog.setMessageType(mensajetype[0]);	
			_dialog.setCancelable(true);
			_dialog.setCanceledOnTouchOutside(true);
			_dialog.initDialog();
		}
		return _dialog;
	}
	
	public void setContext(Context context)
	{
		this.cnt = context;
	}
	
	public void setInterface(boolean interfac)
	{
		this.imple_interfc=interfac;
	}
	/**
	 * CustomDialog  Interfaces  Variables
	 */ 
	
	private OnActionButtonClickListener mButtonClickListener;
	
	private OnDismissDialogListener mDismissListener;
	
	/**
	 * CustomDialog Interfaces   
	 */ 
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
	
	public CustomDialog(Context context) 
	{
		super(context, android.R.style.Theme_Translucent);   
	}  
	
	public CustomDialog(Context context,String titulo,String mensaje,int... mensajetype) 
	{
		super(context, android.R.style.Theme_Translucent);		
		cnt = context;
		this.setTitulo(titulo);
		this.setMensaje(mensaje);
		if(mensajetype!=null)
		this.setMessageType(mensajetype[0]);	
		this.setCancelable(true);
	    this.setCanceledOnTouchOutside(true);
	}
	public CustomDialog(Context context,String mensaje,boolean interfac,int... mensajetype) 
	{		
		super(context, android.R.style.Theme_Translucent);	
		cnt = context;
		this.setMensaje(mensaje);
		if(mensajetype!=null)
			this.setMessageType(mensajetype[0]);	
		this.setCancelable(true);
	    this.setCanceledOnTouchOutside(true);
	    imple_interfc=interfac;
	}
	public CustomDialog(Context context,String titulo,String mensaje,int _layout,int mensajetype) 
	{
		super(context, android.R.style.Theme_Translucent);
		cnt = context;
		this.setTitulo(titulo);
		this.layout=_layout;
		this.setMensaje(mensaje); 
		this.setMessageType(mensajetype);	
		this.setCancelable(true);
	    this.setCanceledOnTouchOutside(true);
	} 
	public CustomDialog(Context context,String titulo,String mensaje,boolean interfac,int... mensajetype) 
	{
		super(context, android.R.style.Theme_Translucent);
		cnt = context;
		this.setTitulo(titulo);
		this.setMensaje(mensaje);
		if(mensajetype!=null)
		this.setMessageType(mensajetype[0]);	
		this.setCancelable(true);
	    this.setCanceledOnTouchOutside(true);
	    imple_interfc=interfac;
	} 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		//setWindow();		
	    initDialog();
	} 
	
	private void setWindow(){
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	}
	
	public void setTitulo(String titulo)
	{
		this.tittle=titulo;
		super.setTitle(titulo);
	}
	
	public void setMensaje(String msg)
	{
		this.message=msg;
	}
	
	public void setMessageType(int dialog)
	{
		this.dialoID=dialog;
	} 
	public void setDinamicLayout(int layout)
	{
		this.layout=layout;
	}
	
    public void initDialog()
	{
		switch(dialoID)
    	{
			case DIALOGO_NOTIFICACION:initNotificationDialog(false);break;
			case DIALOGO_NOTIFICACION2:initNotificationDialog(true);break;			
    		case DIALOGO_ALERTA:
    			initAlertDialog();
    			break;
    		case DIALOGO_CONFIRMACION:
    			initConfirmDialog();	
    			break;
    		case DIALOGO_SELECCION:
    			//dialogo = crearDialogoSeleccion();
    			break;
    		case DIALOGO_DINAMICO:
    			initDinamicDialog();
    			break;
    		default:
    			initAlertDialog();
    			break;
    	}
		
	}
	
    private void initDinamicDialog()
    {
    	setContentView(this.layout); 
    }
    
    public void initNotificationDialog(boolean addbutton)
    {
    	setContentView(R.layout.customdialog);    
    	 
		TextView tvmessage =(TextView)findViewById(R.id.message_dialog);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		
	    tvmessage.setText(message.toString());
	    if(addbutton)
	    {
	    	RelativeLayout rl=(RelativeLayout)this.findViewById(R.id.rldialog);
	    	ScrollView scroll=(ScrollView)this.findViewById(R.id.scroll_dialog);
	    	Button btn = new Button(this.getContext());
	        btn.setOnClickListener
	  	    ( 	new Button.OnClickListener()
	          	{
	  	            @Override
	  				public void onClick(View v) 
	  	            {
	  	            	if(imple_interfc)
	  	            	mButtonClickListener.onButtonClick(viewdialog,OK_BUTTOM);
	  	                dismiss();
	  	            }
	          	}
	  	    ); 
	        btn.setText("Aceptar");  
						 
			params.setMargins(5,5,8,8);
			params.addRule(RelativeLayout.BELOW,R.id.scroll_dialog); 
			btn.setBackgroundDrawable(this.getContext().getResources().getDrawable(R.drawable.btn));
			btn.setLayoutParams(params);
	        
			rl.addView(btn);
	        
			params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,50);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP); 
			params.setMargins(20,5,5,3);
			scroll.setLayoutParams(params);
	    }
	    
	    
    }
    
    public void initAlertDialog()
	{        
		setContentView(R.layout.alert_dialog);  
		viewdialog=findViewById(R.layout.alert_dialog);
		TextView tvtittle = (TextView)findViewById(R.id.tittle_dialog_alert);
		tvtittle.setText(tittle.toString());
		TextView tvmessage =(TextView)findViewById(R.id.bodymessage_dialog_alert);
	    tvmessage.setText(message.toString());
	    
	    Button btn_aceptar = (Button) findViewById(R.id.btnaceptar_dialog_alert);
	    btn_aceptar.setOnClickListener
	    ( 	new Button.OnClickListener()
        	{
	            @Override
				public void onClick(View v) 
	            {
	            	if(imple_interfc)
	            	mButtonClickListener.onButtonClick(viewdialog,OK_BUTTOM); 
	            	dismiss();
	            }
        	}
	    ); 
	    
	    
	}
	
    public void initConfirmDialog()
	{ 
		setContentView(R.layout.confirm_dialog);    
		viewdialog=findViewById(R.layout.confirm_dialog);
		TextView tvtittle = (TextView)findViewById(R.id.tittle_dialog_confirm);
		tvtittle.setText(tittle.toString());
		TextView tvmessage =(TextView)findViewById(R.id.bodymessage_dialog_confirm);
	    tvmessage.setText(message.toString());
	    
	    Button btn_aceptar = (Button) findViewById(R.id.btnaceptar_dialog_confirm);
	    btn_aceptar.setOnClickListener
	    ( 	new Button.OnClickListener()
        	{
	            @Override
				public void onClick(View v) 
	            {
	            	if(imple_interfc)
	            	mButtonClickListener.onButtonClick(viewdialog,OK_BUTTOM);
	                dismiss();
	            }
        	}
	    ); 
	    
	    Button btn_cancelar = (Button) findViewById(R.id.btncancelar_dialog_confirm);
	    btn_cancelar.setOnClickListener
	    ( 	new Button.OnClickListener()
        	{
	            @Override
				public void onClick(View v) 
	            {
	            	if(imple_interfc)
	            	mButtonClickListener.onButtonClick(viewdialog,NO_BUTTOM);
	                dismiss();
	            }
        	}
	    ); 
	}
	
    @Override
	public void onDismiss(DialogInterface dialog) {
    	if(imple_interfc)
		mDismissListener.onDismiss();
		
	}	 
    
     
}
