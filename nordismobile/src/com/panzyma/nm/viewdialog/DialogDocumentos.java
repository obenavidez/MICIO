package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_FACTURACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nm.view.viewholder.NotaCreditoViewHolder;
import com.panzyma.nm.view.viewholder.NotaDebitoViewHolder;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewdialog.DialogSeleccionTipoDocumento.Documento;
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
import android.view.KeyEvent;
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
public class DialogDocumentos  extends Dialog  implements Handler.Callback  {
	
	private Context mcontext;  
	private GenericAdapter<Factura, FacturaViewHolder> adapter; 
	private GenericAdapter<CCNotaDebito, NotaDebitoViewHolder> adapter2;
	private GenericAdapter<CCNotaCredito, NotaCreditoViewHolder> adapter3;
	public ProgressDialog pd;
	private Button Menu; 
	private QuickAction quickAction; 
	private Display display;
	private static final String TAG = DialogDocumentos.class.getSimpleName(); 
	ListView lvfacturas;	
	ListView lvnotasc;
	ListView lvnotasd;
	TextView gridheader;
	private int positioncache=-1; 
	private OnDocumentoButtonClickListener mButtonClickListener;
	private NMApp nmapp;
	public Factura factura_selected;
	public CCNotaDebito nota_debito_selected;
	public CCNotaCredito nota_credito_selected;
	private static ViewReciboEdit parent;
	private long objSucursalId;
	private Cliente cliente;
	private Documento documento;
	
	public long getObjSucursalId() {
		return objSucursalId;
	}
	
	private void setObjSucursalId(long objSucursalId) {
		this.objSucursalId = objSucursalId;
	}

	public interface OnDocumentoButtonClickListener {
		public abstract void onButtonClick(Object documento);
	}
	
	public void setOnDialogDocumentoButtonClickListener(OnDocumentoButtonClickListener listener) {
		mButtonClickListener = listener;
	}
	
	public DialogDocumentos(ViewReciboEdit me, int theme, Cliente cliente, Documento document) 
	{
		super(me.getContext(), theme);
		try {
			setContentView(R.layout.mainfactura); 
			mcontext=this.getContext(); 
			this.documento = document;
			parent = me; 
			WindowManager wm = (WindowManager) me.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			setObjSucursalId(cliente.getIdSucursal());
			nmapp=(NMApp) me.getApplication();
			nmapp.getController().removeBridgeByName(BReciboM.class.toString());
			nmapp.getController().setEntities(this,new BReciboM()); 
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler().sendEmptyMessage(C_FACTURACLIENTE); 
			pd = ProgressDialog.show(me, "Espere por favor", "Trayendo Info...", true, false);
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
	    lvnotasd = (ListView) findViewById(R.id.lvnotasd);
	    lvnotasc = (ListView) findViewById(R.id.lvnotasc);
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
			switch(documento){
			case FACTURA:
				Factura [] facturas = ((Cliente)msg.obj).getFacturasPendientes();
				if(facturas != null && facturas.length > 0){
					loadFacturas( ( (msg.obj == null) ? new Factura[]{} : facturas ), C_DATA);
				}else{
					pd.dismiss();
					FINISH_ACTIVITY();
					Util.Message.buildToastMessage(parent, "No existen facturas pendientes", 1000).show();
				}					
				break;
			case NOTA_DEBITO:
				CCNotaDebito [] notasdebito = ((Cliente)msg.obj).getNotasDebitoPendientes();
				if(notasdebito != null && notasdebito.length > 0 ){
					loadNotasDebito((ArrayList<CCNotaDebito>)((msg.obj==null)?new ArrayList<CCNotaDebito>(): toList(notasdebito)),C_DATA);
				} else {
					pd.dismiss();
					FINISH_ACTIVITY();
					Util.Message.buildToastMessage(parent, "No existen notas de débito pendientes", 1000).show();
				}
				break;
			case NOTA_CREDITO:
				CCNotaCredito [] notasCredito = ((Cliente)msg.obj).getNotasCreditoPendientes();
				if(notasCredito != null && notasCredito.length > 0 ){
					loadNotasCredito((ArrayList<CCNotaCredito>)((msg.obj==null)?new ArrayList<CCNotaCredito>(): toList(notasCredito)),C_DATA);
				} else {
					pd.dismiss();
					FINISH_ACTIVITY();
					Util.Message.buildToastMessage(parent, "No existen notas de crédito pendientes", 1000).show();
				}
				break;
			}
			break;
		}
		return false;
	}
	
	private void loadNotasCredito(ArrayList<CCNotaCredito> notasCredito, int cData) {
try {
			
			if(notasCredito.size() > 0){
				gridheader.setText("Listado Notas Crédito Pendientes ("+notasCredito.size()+")");				
				adapter3 = new GenericAdapter<CCNotaCredito, NotaCreditoViewHolder>(mcontext,NotaCreditoViewHolder.class,notasCredito,R.layout.detalle_nota_credito);				 
				lvnotasd.setAdapter(adapter3);
				lvnotasd.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if((parent.getChildAt(positioncache)) != null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position;				            	
		            	nota_credito_selected = (CCNotaCredito) adapter3.getItem(position);	
		            	try {
		            		//Object d= nmapp.getController().getBridge().getClass().getMethods();
		            		//factura_selected =(Cliente) nmapp.getController().getBridge().getClass().getMethod("getClienteBySucursalID",ContentResolver.class,long.class).invoke(null,DialogCliente.this.getContext().getContentResolver(),cliente_selected.getIdSucursal());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	adapter3.setSelectedPosition(position); 
		            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));					            	 
		            	mButtonClickListener.onButtonClick(nota_credito_selected);
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

	private ArrayList<Object> toList(Object[] array) {
		ArrayList<Object> list = new ArrayList<Object>();
		switch(documento){
		case FACTURA:
			for(Object obj: array){
				list.add(obj);
				for(Factura factura: ((ViewReciboEdit)parent).getFacturasRecibo()){
					if( factura.getNoFactura().equals(((Factura)obj).getNoFactura()) )
						list.remove(obj);						
				}				
			}
			break;
		case NOTA_DEBITO:
			
			break;
		case NOTA_CREDITO:
			break;
		}
		
		
		return list;
	}
	
	private ArrayList removeObjectNull(ArrayList arrayList) {
		ArrayList arrayListResult = new ArrayList<Object>();
		for(Object obj : arrayList){
			if(obj != null) arrayListResult.add(obj);
		}
		return arrayListResult;
	}
	
	private ArrayList getArray(Object [] array, Object type){
		
		List<Factura> facturas = null;
		List<CCNotaDebito> notasDebito = null;
		List<CCNotaCredito> notasCredito = null;
		ArrayList arrayList = null;		
		
		if ( type instanceof Factura ) {
			
			Factura [] _facturas = new Factura[array.length];			
			toList(array).toArray(_facturas);
			arrayList = new ArrayList( Arrays.asList(_facturas) ) ;
			return removeObjectNull(arrayList);
			
		} else if ( type instanceof CCNotaDebito ) {
			
			CCNotaDebito [] _notasDebito = new CCNotaDebito[array.length];			
			toList(array).toArray(_notasDebito);
			arrayList = new ArrayList( Arrays.asList(_notasDebito) ) ;
			return removeObjectNull(arrayList);			
			
		} else if ( type instanceof CCNotaCredito ) {
			
			CCNotaCredito [] _notasCredito = new CCNotaCredito[array.length];			
			toList(array).toArray(_notasCredito);
			arrayList = new ArrayList( Arrays.asList(_notasCredito) ) ;
			return removeObjectNull(arrayList);			
			
		}
		return null;
	} 
	
	public void loadFacturas(Factura [] facturas, int cData) {
		try {
			/*Object [] objetos = new Object[facturas.size()];
			Factura [] _facturas = new Factura[facturas.size()];
			facturas.toArray(objetos);
			toList(objetos).toArray(_facturas);
			facturas = (ArrayList<Factura>) Arrays.asList(_facturas) ;*/			
			if(facturas.length > 0){
				ArrayList<Factura> _facturas = (ArrayList<Factura>) getArray(facturas, facturas[0]);
				gridheader.setText("Listado de Facturas Pendientes ("+_facturas.size()+")");				
				adapter = new GenericAdapter<Factura, FacturaViewHolder>(mcontext,FacturaViewHolder.class,_facturas,R.layout.detalle_factura);				 
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
		            	//FINISH_ACTIVITY();						
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

	private void loadNotasDebito(ArrayList<CCNotaDebito> notasDebito, int cData){
		try {
			
			if(notasDebito.size() > 0){
				gridheader.setText("Listado Notas Debito Pendientes ("+notasDebito.size()+")");				
				adapter2 = new GenericAdapter<CCNotaDebito, NotaDebitoViewHolder>(mcontext,NotaDebitoViewHolder.class,notasDebito,R.layout.detalle_nota_debito);				 
				lvnotasd.setAdapter(adapter2);
				lvnotasd.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if((parent.getChildAt(positioncache)) != null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position;				            	
		            	nota_debito_selected = (CCNotaDebito) adapter2.getItem(position);	
		            	try {
		            		//Object d= nmapp.getController().getBridge().getClass().getMethods();
		            		//factura_selected =(Cliente) nmapp.getController().getBridge().getClass().getMethod("getClienteBySucursalID",ContentResolver.class,long.class).invoke(null,DialogCliente.this.getContext().getContentResolver(),cliente_selected.getIdSucursal());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	adapter2.setSelectedPosition(position); 
		            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));					            	 
		            	mButtonClickListener.onButtonClick(nota_debito_selected);
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
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {        	
		  	FINISH_ACTIVITY();
	        return true;
	    }
	    return super.onKeyUp(keyCode, event); 
	}
	
	private void FINISH_ACTIVITY()
	{
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().removebridge(nmapp.getController().getBridge());
		nmapp.getController().disposeEntities();
		try {
		nmapp.getController().setEntities(((ViewReciboEdit)parent),((ViewReciboEdit)parent).getBridge());
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		if(pd!=null)
		pd.dismiss();	
		Log.d(TAG, "Activity quitting"); 
		pd = null;	
		this.dismiss();
	}
}
