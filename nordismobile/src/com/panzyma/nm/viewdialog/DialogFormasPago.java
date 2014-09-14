package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nm.view.viewholder.FormaPagoViewHolder;
import com.panzyma.nm.viewdialog.DialogDocumentos.OnDocumentoButtonClickListener;
import com.panzyma.nordismobile.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("unused")
public class DialogFormasPago extends Dialog {

	private static final String TAG = DialogFormasPago.class.getSimpleName();
	private List<ReciboDetFormaPago> lista = null;
	private Display display = null;
	private ProgressDialog pd = null;
	private ListView lvfacturas = null;
	private TextView gridheader = null;
	private GenericAdapter<ReciboDetFormaPago, FormaPagoViewHolder> adapter; 
	private ViewReciboEdit parent = null;
	private int positioncache = 0;
	private ReciboDetFormaPago formaPagoSelected = null;
	private Context mcontext; 
	private OnFormaPagoButtonClickListener mButtonClickListener;
	private NMApp nmapp;

	public DialogFormasPago(ViewReciboEdit me, int theme,List<ReciboDetFormaPago> lista) {
		super(me.getContext(), theme);
		setContentView(R.layout.mainfactura); 
		this.lista = lista;
		parent = me;
		nmapp = (NMApp)me.getApplication();
		mcontext = this.getContext(); 
		WindowManager wm = (WindowManager) me.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		pd = ProgressDialog.show(me, "Espere por favor", "Trayendo Info...",
				true, false);
		initComponents();
		loadFormasPago((ArrayList<ReciboDetFormaPago>) lista);
	}

	private void initComponents() {

		LinearLayout.LayoutParams layoutParams;
		LinearLayout viewroot = ((LinearLayout) findViewById(R.id.lmainfactura));
		LinearLayout llheader = ((LinearLayout) findViewById(R.id.llheader));
		LinearLayout llbody = ((LinearLayout) findViewById(R.id.llbody));
		viewroot.setBackgroundResource(R.drawable.bgdialog2);

		layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(2, 2, 1, 0);
		llheader.setLayoutParams(layoutParams);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(2, 0, 1, 1);
		llbody.setLayoutParams(layoutParams);

		lvfacturas = (ListView) findViewById(R.id.lvfacturas);		
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
		((ViewGroup) lvfacturas.getParent()).removeView(stub);

		EditText filterEditText = (EditText) findViewById(R.id.EditText_Client);
		filterEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				adapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	public interface OnFormaPagoButtonClickListener {
		public abstract void onButtonClick(ReciboDetFormaPago formaPago);
	}
	
	public void setOnDialogButtonClickListener(OnFormaPagoButtonClickListener listener) {
		mButtonClickListener = listener;
	}
	
	public void loadFormasPago(ArrayList<ReciboDetFormaPago> formasPago) {
		try {						
			if(formasPago.size() > 0){
				//SE OBTIENEN LAS FORMAS DE PAGO				
				if( formasPago.size() == 0 ){
					pd.dismiss();
					//FINISH_ACTIVITY();
					Util.Message.buildToastMessage(parent, "No existen formas de pago", Toast.LENGTH_SHORT).show();
					return;
				}
				FINISH_ACTIVITY();
				gridheader.setText("Listado de Formas de Pago (" + formasPago.size() + ")");				
				adapter = new GenericAdapter<ReciboDetFormaPago, FormaPagoViewHolder>(parent.getContext(),FormaPagoViewHolder.class,formasPago,R.layout.detalle_forma_pago);				 
				lvfacturas.setAdapter(adapter);
				lvfacturas.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if((parent.getChildAt(positioncache)) != null)						            							            		
		            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
		            	positioncache=position;				            	
		            	formaPagoSelected = (ReciboDetFormaPago) adapter.getItem(position);			            	
		            	adapter.setSelectedPosition(position); 
		            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));					            	 
		            	mButtonClickListener.onButtonClick(formaPagoSelected);
		            	//FINISH_ACTIVITY();						
					}					
				});
				adapter.notifyDataSetChanged();
			}
			
		} catch (Exception e) {
			//buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			e.printStackTrace();
		}
		if(pd != null)
			pd.dismiss();	
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
	
	@SuppressWarnings({ "static-access", "unchecked" })
	private void FINISH_ACTIVITY()
	{
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().removebridge(nmapp.getController().getBridge());
		nmapp.getController().disposeEntities();
		try {
			nmapp.getController().setEntities(((ViewReciboEdit)parent),((ViewReciboEdit)parent).getBridge());
		} catch (Exception e) {		
			e.printStackTrace();
		}
		if(pd != null)
			pd.dismiss();	
		Log.d(TAG, "Activity quitting"); 
		pd = null;	
		this.dismiss();
	}

}