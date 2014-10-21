package com.panzyma.nm.viewdialog;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST; 

import java.util.ArrayList; 

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.ProductoViewHolder;
import com.panzyma.nm.viewdialog.DetalleProducto.OnButtonClickHandler;
import com.panzyma.nordismobile.R;
@InvokeBridge(bridgeName = "BProductoM")
public class DialogProducto extends Dialog  implements Handler.Callback{

	private Display display;
	private ProgressDialog pd;
	private GenericAdapter adapter; 
	private Button Menu; 
	private QuickAction quickAction;  
	private static final String TAG = DialogProducto.class.getSimpleName(); 
	ListView lvproducto;	
	TextView gridheader;
	private int positioncache=-1;  
	private OnButtonClickListener mButtonClickListener; 
	
	public Producto producto;
	protected Producto product_selected;  

	public interface OnButtonClickListener {
		public abstract void onButtonClick(DetallePedido det_p,Producto prod);
	}
	
    public void setOnDialogProductButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
	
    
    private String codTipoPrecio;
    
    private String filtro = "";  
    private int[] dataIndex; 
    private long _idCategCliente;
    private long _idTipoPrecio;
    private long _idPedido;
    private long _idTipoCliente;
    private String[][] data = null;
    private boolean _exento;
	private ArrayList<Producto> _idsProdsExcluir; 
	private ArrayList<Producto> Lproducto;
	private com.panzyma.nm.interfaces.Editable parent;
	
	public DialogProducto(com.panzyma.nm.interfaces.Editable vpe,String codTP, ArrayList<Producto> ProdsExclir, long idPedido, long idCategCliente, long idTipoPrecio, long idTipoCliente, boolean exento) 
    {    
    	super(vpe.getContext(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);     
        
        try 
        {   
			setContentView(R.layout.mainproducto);   
			parent=vpe;      	 
	        NMApp.getController().setView(this);
	        pd = ProgressDialog.show(vpe.getContext(), "Espere por favor", "Trayendo Info...", true, false); 
			WindowManager wm = (WindowManager) vpe.getContext().getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay(); 
			NMApp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);
			initComponents();
			codTipoPrecio = codTP;
	        _idsProdsExcluir = (ProdsExclir==null)?new ArrayList<Producto>():ProdsExclir;
	        _idCategCliente = idCategCliente;
	        _idTipoPrecio = idTipoPrecio;
	        _idPedido = idPedido;       
	        _idTipoCliente = idTipoCliente; 
	        _exento = exento; 
	        Lproducto=new ArrayList<Producto>();
	        
        }catch (Exception e) { 
			e.printStackTrace();
			//buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();			  
		}	 
    } 
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    { 
    	if(pd!=null)
    		pd.dismiss();
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
				
				filterData((ArrayList<Producto>)((msg.obj==null)?new ArrayList<Producto>():msg.obj));
				LoadData();
				return true;
			case ERROR: 
				ErrorMessage error=((ErrorMessage)msg.obj);			 
				return true;		
		}
		return false;
		
	}
 
	public void filterData(ArrayList<Producto> lproductos)
	{
		  
		if(_idsProdsExcluir!=null && _idsProdsExcluir.size()!=0)
		{  		
			if(lproductos.size()!=0)
			{								
				for(Producto item:_idsProdsExcluir) 	
				{		
					if(lproductos.contains(item))
						lproductos.remove(item);
				}
				
			}
			
		} 
		if(lproductos!=null && lproductos.size()!=0)
			Lproducto=lproductos;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void LoadData() {		
		
		try 
		{			  
			if(Lproducto.size()!=0 )
			{ 
					gridheader.setText("Listado de Productos("+Lproducto.size()+")");
					adapter=new GenericAdapter(NMApp.getContext(),ProductoViewHolder.class,Lproducto,R.layout.gridproducto);				 
					lvproducto.setAdapter(adapter);
					lvproducto.setOnItemClickListener(new OnItemClickListener() 
			        {
			            @SuppressLint("NewApi")
						@Override
			            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			            { 		  
			            	if((parent.getChildAt(positioncache))!=null)						            							            		
			            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
			            	positioncache=position;				            	
			            	product_selected=(Producto) adapter.getItem(position);	
			            	adapter.setSelectedPosition(position); 
			            	view.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.action_item_selected));					            	 
			            	//mButtonClickListener.onButtonClick(producto); 
			            }
			        }); 								
					lvproducto.setOnItemLongClickListener(new OnItemLongClickListener()
				    {
						@SuppressWarnings("deprecation")
						@SuppressLint("NewApi")
						@Override
						public boolean onItemLongClick(AdapterView<?> _parent, View view,int position, long id) 
						{  											 
							if((_parent.getChildAt(positioncache))!=null)						            							            		
			            		(_parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
			            	positioncache=position;				            	
			            	product_selected=(Producto) adapter.getItem(position);	
			            	adapter.setSelectedPosition(position); 
			            	view.setBackgroundDrawable(_parent.getResources().getDrawable(R.drawable.action_item_selected));				
							
			            	//EditDetPedido editForm = new EditDetPedido(prod, _idCategCliente, _idTipoPrecio, _idTipoCliente, _exento);
							DetalleProducto dp=new DetalleProducto((ViewPedidoEdit)parent,product_selected, _idCategCliente, _idTipoPrecio, _idTipoCliente, _exento);
							
							dp.setOnDialogDetalleProductButtonClickListener(new OnButtonClickHandler(){

								@Override
								public void onButtonClick(DetallePedido det_p,boolean btn) {
									if(btn)
									{ 
										mButtonClickListener.onButtonClick(det_p,product_selected);
										Lproducto.remove(positioncache);
										adapter.notifyDataSetChanged(); 
									}
																		
									
								} 
								
							}); 
      						dp.getWindow().setGravity(Gravity.CENTER); 
							dp.getWindow().setGravity(Gravity.CENTER); 
							dp.getWindow().setLayout(display.getWidth()-40,display.getHeight()-110);  
							dp.show(); 
							return true;
						}
 
				        	
				    }); 
				}  
		} catch (Exception e) {
//			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
			e.printStackTrace();
		}
		pd.dismiss();	

	} 
	
	private void FINISH_ACTIVITY()
	{ 
		com.panzyma.nm.NMApp.getController().setView((ViewPedidoEdit)parent);
		if(pd!=null)
			pd.dismiss();
		Log.d(TAG, "Exit from DialogProducto"); 
	}  
 

}
