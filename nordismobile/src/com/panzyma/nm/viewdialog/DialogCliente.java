package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.panzyma.nm.interfaces.Editable;

import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nordismobile.R;
@InvokeBridge(bridgeName = "BClienteM")
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class DialogCliente extends Dialog  implements Handler.Callback
{
	
	private GenericAdapter adapter; 
	 
	private vmCliente cliente_selected; 
	public ProgressDialog pd;
	private Button Menu; 
	private QuickAction quickAction; 
	private Display display;
	private static final String TAG = DialogCliente.class.getSimpleName(); 
	ListView lvcliente;	
	TextView gridheader;
	private int positioncache=-1; 
	private OnButtonClickListener mButtonClickListener; 
	public Cliente cliente;
	private Editable parent;
	
	public interface OnButtonClickListener {
		public abstract void onButtonClick(Cliente cliente);
	}
	
    public void setOnDialogClientButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
	
	public DialogCliente(Editable vpe,int theme) 
	{		
		super(vpe.getContext(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		try 
        {    
			parent=vpe;
			setContentView(R.layout.maincliente);          	 
	        NMApp.getController().setView(this); 
			WindowManager wm = (WindowManager) NMApp.getContext().getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
			pd = ProgressDialog.show((Context)vpe, "Espere por favor", "Trayendo Info...", true, false); 
			Toast.makeText(this.getContext(), "TestersterexdsafADFASDF", Toast.LENGTH_LONG);
			NMApp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST); 
	        initComponents();
	        
        }catch (Exception e) { 
			e.printStackTrace();
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();			  
		}	 
      
	}	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {        	
			dismiss();
	        return true;
	    }
	    return super.onKeyUp(keyCode, event); 
	}
	
	@Override
	public void dismiss() 
   	{   		
   		FINISH_ACTIVITY();
		super.dismiss();
	}
	
	public void initComponents()
	{
		LinearLayout.LayoutParams layoutParams;
	    LinearLayout viewroot=((LinearLayout)findViewById(R.id.lmaincliente)); 
	    LinearLayout llheader=((LinearLayout)findViewById(R.id.llheader));
	    LinearLayout llbody=((LinearLayout)findViewById(R.id.llbody));
	    viewroot.setBackgroundResource(R.drawable.bgdialog2); 

	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 2, 1,0); 
	    llheader.setLayoutParams(layoutParams);
	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 0, 1,1); 
	    llbody.setLayoutParams(layoutParams);
	    
	    
	    lvcliente = (ListView) findViewById(R.id.lvcliente);
	    lvcliente.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    gridheader=(TextView) findViewById(R.id.ctextv_gridheader); 
	    ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
	    ((ViewGroup) lvcliente.getParent()).removeView(stub);
	    
		EditText filterEditText = (EditText) findViewById(R.id.EditText_Client); 
	    filterEditText.addTextChangedListener(
	    new TextWatcher() 
	    {
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) 
	        {
	            adapter.getFilter().filter(s.toString());
	        }
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
	        @Override
	        public void afterTextChanged(android.text.Editable s) {}
	    });  
	    
	}
		
	@Override
	public boolean handleMessage(Message msg) 
	{ 
		switch (msg.what) 
		{		 
			case C_DATA:   
				
				LoadData((ArrayList<vmCliente>)((msg.obj==null)?new ArrayList<vmCliente>():msg.obj),C_DATA);
				return true;
			case ERROR:
				pd.dismiss();
				ErrorMessage error=((ErrorMessage)msg.obj);
				buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
				return true;		
		}
		return false;
	}
		 
	public void LoadData(final ArrayList<vmCliente> Lcliente, int what)
	{     	 
		try 
		{			 
			if(Lcliente.size()!=0)
			{
					gridheader.setText("LISTA CLIENTE("+Lcliente.size()+")");
					adapter=new GenericAdapter(NMApp.getContext(),ClienteViewHolder.class,Lcliente,R.layout.gridcliente);				 
					lvcliente.setAdapter(adapter);
					lvcliente.setOnItemClickListener(new OnItemClickListener() 
			        {
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			            { 		
			            	if(positioncache < 0 && adapter.getCount() > 0)
			            		positioncache = 0;
			            	if((parent.getChildAt(positioncache))!=null)						            							            		
			            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
			            	positioncache=position;				            	
			            	cliente_selected=(vmCliente) adapter.getItem(position);	
			            	try {
			            		Object d=NMApp.getController().getBridge().getClass().getMethods();
								cliente=(Cliente) NMApp.getController().getBridge().getClass().getMethod("getClienteBySucursalID",ContentResolver.class,long.class).invoke(null,DialogCliente.this.getContext().getContentResolver(),cliente_selected.getIdSucursal());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	adapter.setSelectedPosition(position); 
			            	view.setBackgroundDrawable(NMApp.getContext().getResources().getDrawable(R.drawable.action_item_selected));					            	 
			             
			            }
			        }); 								
				    lvcliente.setOnItemLongClickListener(new OnItemLongClickListener()
				    {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) 
						{  											 
							if((parent.getChildAt(positioncache))!=null)						            							            		
			            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
			            	positioncache=position;				            	
			            	cliente_selected=(vmCliente) adapter.getItem(position);	
			            	try { 
								cliente=(Cliente) NMApp.getController().getBridge().getClass().getMethod("getClienteBySucursalID",ContentResolver.class,long.class).invoke(null,DialogCliente.this.getContext().getContentResolver(),cliente_selected.getIdSucursal());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	adapter.setSelectedPosition(position); 
			            	view.setBackgroundDrawable(NMApp.getContext().getResources().getDrawable(R.drawable.action_item_selected));					            	 
			            	mButtonClickListener.onButtonClick(cliente);
			            	dismiss();
							return true;
						}
				        	
				    });
		            buildToastMessage("sincronización exitosa",Toast.LENGTH_SHORT).show();
				
		 	} 
		} catch (Exception e) {
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			e.printStackTrace();
		}
		pd.dismiss();	

	} 
	
	@SuppressLint("ShowToast")
	public Toast buildToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(getContext(),msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}
	
	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		return new CustomDialog(getContext(),tittle,msg,false,type);	    
	}
	
	private void FINISH_ACTIVITY()
	{
		
		if(pd!=null)
			pd.dismiss();	 
		com.panzyma.nm.NMApp.getController().setView((Callback)parent); 
		Log.d(TAG, "Activity quitting");  
	}  
}
