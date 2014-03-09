package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import java.util.ArrayList;

import com.panzyma.nm.NMApp; 
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.ProductoViewHolder;
import com.panzyma.nm.viewmodel.vmPProducto;
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nordismobile.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DialogProducto extends Dialog  implements Handler.Callback{

	
	private Context mcontext;
	private ViewPedidoEdit parent;
	private NMApp nmapp;
	private Display display;
	private ProgressDialog pd;
	private GenericAdapter adapter; 
	private Button Menu; 
	private QuickAction quickAction;  
	private static final String TAG = DialogCliente.class.getSimpleName(); 
	ListView lvproducto;	
	TextView gridheader;
	private int positioncache=-1; 
	private OnButtonClickListener mButtonClickListener; 
	public Producto producto;
	protected vmProducto product_selected;  
	public interface OnButtonClickListener {
		public abstract void onButtonClick(vmPProducto vmpproducto);
	}
	
    public void setOnDialogProductButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
	

	public DialogProducto(ViewPedidoEdit vpe, int theme) {
		super(vpe, theme);

		try 
        {   
			setContentView(R.layout.mainproducto);  
        	mcontext=this.getContext();  
        	parent=vpe;       	
        	nmapp=(NMApp) vpe.getApplication(); 
	        nmapp.getController().setEntities(this,new BProductoM()); 
	        nmapp.getController().addOutboxHandler(new Handler(this));
			WindowManager wm = (WindowManager) vpe.getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
			pd = ProgressDialog.show(vpe, "Espere por favor", "Trayendo Info...", true, false); 
			nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST); 
	        initComponents();
	        
        }catch (Exception e) { 
			e.printStackTrace();
			//buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();			  
		}	 
		
	}
	
	
	public void initComponents()
	{
		LinearLayout.LayoutParams layoutParams;
	    LinearLayout viewroot=((LinearLayout)findViewById(R.id.p_mainproducto)); 
	    LinearLayout llheader=((LinearLayout)findViewById(R.id.p_llheader));
	    LinearLayout llbody=((LinearLayout)findViewById(R.id.p_llbody));
	    viewroot.setBackgroundResource(R.drawable.bgdialog2); 

	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 2, 1,0); 
	    llheader.setLayoutParams(layoutParams);
	    layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    layoutParams.setMargins(2, 0, 1,1); 
	    llbody.setLayoutParams(layoutParams);
	    
	    
	    lvproducto = (ListView) findViewById(R.id.p_lvproducto);	
	    gridheader=(TextView) findViewById(R.id.p_textv_gridheader); 
	    ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
	    ((ViewGroup) lvproducto.getParent()).removeView(stub);
	    
		EditText filterEditText = (EditText) findViewById(R.id.p_editextfilter_prod); 
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

		switch (msg.what) 
		{		 
			case C_DATA:   
				
				LoadData((ArrayList<vmProducto>)((msg.obj==null)?new ArrayList<vmProducto>():msg.obj),C_DATA);
				return true;
			case ERROR:
				pd.dismiss();
				ErrorMessage error=((ErrorMessage)msg.obj);
				buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
				return true;		
		}
		return false;
		
	}

	private Dialog buildCustomDialog(String tittle, String string,
			int alertDialog) {
		// TODO Auto-generated method stub
		return null;
	}

	private void LoadData(ArrayList<vmProducto> Lproducto, int cData) {
		
		
		try 
		{			 
			if(Lproducto.size()!=0)
			{
					gridheader.setText("Listado de Productos("+Lproducto.size()+")");
					adapter=new GenericAdapter(mcontext,ProductoViewHolder.class,Lproducto,R.layout.gridproducto);				 
					lvproducto.setAdapter(adapter);
					lvproducto.setOnItemClickListener(new OnItemClickListener() 
			        {
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			            { 		  
			            	if((parent.getChildAt(positioncache))!=null)						            							            		
			            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
			            	positioncache=position;				            	
			            	product_selected=(vmProducto) adapter.getItem(position);	
			            	try {
			            		Object d=nmapp.getController().getBridge().getClass().getMethods();
			            		producto=(Producto) nmapp.getController().getBridge().getClass().getMethod("getProductoByID",ContentResolver.class,long.class).invoke(null,DialogProducto.this.getContext().getContentResolver(),product_selected.getId());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	adapter.setSelectedPosition(position); 
			            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));					            	 
			            	//mButtonClickListener.onButtonClick(producto);
			            	FINISH_ACTIVITY();
			            }
			        }); 								
					lvproducto.setOnItemLongClickListener(new OnItemLongClickListener()
				    {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) 
						{  											 
							if((parent.getChildAt(positioncache))!=null)						            							            		
			            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
			            	positioncache=position;				            	
			            	product_selected=(vmProducto) adapter.getItem(position); 
			            	Toast.makeText(mcontext, "prueba long click", Toast.LENGTH_LONG).show();
			            	try {
								nmapp.getController().getBridge().getClass().getMethod("getProductoByID",long.class).invoke(producto,producto.getId());
							} catch (Exception e) { 
								e.printStackTrace();
							}
			            	adapter.setSelectedPosition(position); 
			            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));
			            	mButtonClickListener.onButtonClick(new vmPProducto(product_selected.getId(),product_selected.getNombre(), 23));
			            	FINISH_ACTIVITY();
			            	//quickAction.show(view,display,false);
							return true;
						}
 
				        	
				    });
		           // buildToastMessage("sincronización exitosa",Toast.LENGTH_SHORT).show();
				
		 	} 
		} catch (Exception e) {
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			e.printStackTrace();
		}
		pd.dismiss();	

		
		
	}

	protected void FINISH_ACTIVITY() {
		// TODO Auto-generated method stub
		
	}


	private Dialog buildToastMessage(String string, int lengthShort) {
		// TODO Auto-generated method stub
		return null;
	}

}
