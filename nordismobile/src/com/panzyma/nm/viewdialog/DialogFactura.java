package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_FACTURACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class DialogFactura  extends Dialog  implements Handler.Callback  {
	
	private Context mcontext;  
	private GenericAdapter<Factura, FacturaViewHolder> adapter; 
	public ProgressDialog pd;
	private Button Menu; 
	private QuickAction quickAction; 
	private Display display;
	private static final String TAG = DialogCliente.class.getSimpleName(); 
	ListView lvfacturas;	
	TextView gridheader;
	private int positioncache=-1; 
	private OnFacturaButtonClickListener mButtonClickListener;
	private NMApp nmapp;
	public Factura factura_selected;
	private static  Activity parent;
	private long objSucursalId;	
	
	public long getObjSucursalId() {
		return objSucursalId;
	}
	
	private void setObjSucursalId(long objSucursalId) {
		this.objSucursalId = objSucursalId;
	}

	public interface OnFacturaButtonClickListener {
		public abstract void onButtonClick(Factura factura);
	}
	
	public void setOnDialogFacturaButtonClickListener(OnFacturaButtonClickListener listener) {
		mButtonClickListener = listener;
	} 

	public DialogFactura(ViewReciboEdit recibo, int theme, long objSucursalID) {
		super(recibo, theme);
		try {
			setContentView(R.layout.mainfactura);  
        	mcontext=this.getContext();  
        	parent = recibo;  
        	WindowManager wm = (WindowManager) parent.getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
        	setObjSucursalId(objSucursalID);
        	nmapp=(NMApp) recibo.getApplication();
        	nmapp.getController().removebridgeByName(new BReciboM());
	        nmapp.getController().setEntities(this,new BReciboM()); 
	        nmapp.getController().addOutboxHandler(new Handler(this));
	        nmapp.getController().getInboxHandler().sendEmptyMessage(C_FACTURACLIENTE); 			
            pd = ProgressDialog.show(parent, "Espere por favor", "Trayendo Info...", true, false);
	        initComponents();
		} catch (Exception e) {
			e.printStackTrace();
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}
	}

	private void initComponents() {
		
		LinearLayout.LayoutParams layoutParams;
	    LinearLayout viewroot=((LinearLayout)findViewById(R.id.lmainfactura)); 
	    LinearLayout llheader=((LinearLayout)findViewById(R.id.llheader));
	    LinearLayout llbody=((LinearLayout)findViewById(R.id.llbody));
	    viewroot.setBackgroundResource(R.drawable.bgdialog2); 

	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 2, 1,0); 
	    llheader.setLayoutParams(layoutParams);
	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 0, 1,1); 
	    llbody.setLayoutParams(layoutParams);
	    
	    
	    lvfacturas = (ListView) findViewById(R.id.lvfacturas);	
	    gridheader=(TextView) findViewById(R.id.ctextv_gridheader); 
	    ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
	    ((ViewGroup) lvfacturas.getParent()).removeView(stub);
	    
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
	        public void afterTextChanged(Editable s) {}
	    }); 
				
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what){
		case C_DATA:
			LoadData((ArrayList<Factura>)((msg.obj==null)?new ArrayList<Factura>():msg.obj),C_DATA);
			break;
		}
		return false;
	}
	
	private void LoadData(ArrayList<Factura> facturas, int cData) {
		try {
			
			if(facturas.size() > 0){
				gridheader.setText("Listado de Facturas Pendientes ("+facturas.size()+")");				
				adapter = new GenericAdapter<Factura, FacturaViewHolder>(mcontext,FacturaViewHolder.class,facturas,R.layout.detalle_factura);				 
				lvfacturas.setAdapter(adapter);
				lvfacturas.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if((parent.getChildAt(positioncache)) != null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position;				            	
		            	factura_selected = (Factura) adapter.getItem(position);	
		            	try {
		            		//Object d= nmapp.getController().getBridge().getClass().getMethods();
		            		//factura_selected =(Cliente) nmapp.getController().getBridge().getClass().getMethod("getClienteBySucursalID",ContentResolver.class,long.class).invoke(null,DialogCliente.this.getContext().getContentResolver(),cliente_selected.getIdSucursal());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	adapter.setSelectedPosition(position); 
		            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));					            	 
		            	mButtonClickListener.onButtonClick(factura_selected);
		            	FINISH_ACTIVITY();						
					}					
				});
			}
			
		} catch (Exception e) {
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			e.printStackTrace();
		}
		if(pd != null)
			pd.dismiss();	
	}

	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		return new CustomDialog(getContext(),tittle,msg,false,type);	    
	}
	
	private void FINISH_ACTIVITY()
	{
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().removebridge(nmapp.getController().getBridge());
		nmapp.getController().disposeEntities();
		Log.d(TAG, "Activity quitting"); 
		this.dismiss();
	}  
	

}
