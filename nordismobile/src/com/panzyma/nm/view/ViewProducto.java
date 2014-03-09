package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;  
import static com.panzyma.nm.controller.ControllerProtocol.C_SETTING_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;  

import java.util.ArrayList;

import com.panzyma.nm.DashBoardActivity;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.auxiliar.CustomDialog.OnDismissDialogListener;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.view.adapter.GenericAdapter;  
import com.panzyma.nm.view.viewholder.ProductoViewHolder; 
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View; 
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button; 
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressWarnings({"unchecked","rawtypes","unused"})
public class ViewProducto extends DashBoardActivity implements Handler.Callback
{
	private GenericAdapter adapter; 
	private Button Menu; 
	private QuickAction quickAction ; 
	private int positioncache=-1;
	private Display display;
	private static final String TAG = ViewProducto.class.getSimpleName();
	private Context mcontext; 
	private vmProducto producto_selected; 
	
	private NMApp nmapp;
	
	private static final int ID_CONSULTAR_FICHA_PRODUCTO  = 1;
	private static final int ID_CONSULTAR_BONIFICACIONES = 2;
	private static final int ID_CONSULTAR_CONSULTARLISTAPRECIOS= 3;
	private static final int ID_SINCRONIZE_ALL_PRODUCTO= 4; 
	private static final int ID_CERRAR = 5; 
	ListView lvproducto;	
	TextView gridheader;
	View footerView ;
	private ProgressDialog pd ;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{ 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.mainproducto); 
		setHeader(getString(R.string.ProductoActivityTitle),true, false); 
		
		try 
	    {
	    	mcontext=this;
	    	nmapp=(NMApp) this.getApplication(); 
	        nmapp.getController().setEntities(this,new BProductoM());
	        nmapp.getController().addOutboxHandler(new Handler(this)); 
			WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay(); 
            //pd = ProgressDialog.show(this, "Espere por favor", "Trayendo Info...", true, false);
			nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);  
			initComponents();
	        
		}catch (Exception e) { 
			e.printStackTrace();
			buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}	
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	    }
		
	}
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_MENU) 
        {        	
        	Menu = (Button)findViewById(R.id.p_btnmenu);
        	quickAction.show(Menu,display,true);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {        	
    	  	FINISH_ACTIVITY();
            return true;
	    }
        return super.onKeyUp(keyCode, event); 
    } 
	public void initComponents()
    {
	    lvproducto = (ListView) findViewById(R.id.p_lvproducto);	
	    gridheader=(TextView) findViewById(R.id.p_textv_gridheader);
	    footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
		footerView.setVisibility(View.GONE);
		lvproducto.addFooterView(footerView);
		
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
    	InitMenu();
    }
	public void InitMenu()
    {       
		quickAction = new QuickAction(this, QuickAction.VERTICAL,1);		 
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_FICHA_PRODUCTO, "Consultar ficha del Producto",null));
		quickAction.addActionItem(new ActionItem(ID_CONSULTAR_BONIFICACIONES, "Consultar Bonificaciones", null));
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_CONSULTARLISTAPRECIOS, "Consultar Lista de Precios",null));
        quickAction.addActionItem(null);
        quickAction.addActionItem(new ActionItem(ID_SINCRONIZE_ALL_PRODUCTO, "Sincronizar todos los Productos", null));
        quickAction.addActionItem(null);
        quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar", null)); 
         
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) 
			{				
				ActionItem actionItem = quickAction.getActionItem(pos);   
				if (actionId == ID_SINCRONIZE_ALL_PRODUCTO)
					nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_SERVER); 
				else if (actionId == ID_CERRAR) 
					FINISH_ACTIVITY();
				 				 
			}
		});
		
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() 
		{			
			@Override
			public void onDismiss() { 
			}
		});
    	
    	
    } 
	@Override
	public boolean handleMessage(Message msg) 
	{
		if(pd!=null)
			pd.dismiss();
		switch (msg.what) 
		{		
		    case C_UPDATE_STARTED:
		    						limpiarGrilla(); 
		    						pd=ProgressDialog.show(mcontext,"","sincronizando info....", true, false); 
									return true; 
			case C_DATA: 									
									setData((ArrayList<vmProducto>)((msg.obj==null)?new ArrayList<vmProducto>():msg.obj),C_DATA);
									return true;
			 
			case C_SETTING_DATA: 
									setData((ArrayList<vmProducto>)((msg.obj==null)?new ArrayList<vmProducto>():msg.obj),C_SETTING_DATA);
									return true;		
									
			case C_UPDATE_FINISHED: footerView.setVisibility(View.INVISIBLE);
									return true;	
		}		
		return false;
	} 
	private void setData(final ArrayList<vmProducto> objL, final int what)
	{
		
		try
		{ 
				if(objL.size()!=0)
				{	 
					this.runOnUiThread
					(  new Runnable() 
				       {						
						 
							@Override
							public void run() 
							{					
								
								try
								{  				
								    if(what==C_SETTING_DATA && adapter!=null && adapter.getData().size()>=0)
									{
										adapter.AddAllToListViewDataSource(objL);
										gridheader.setText("Listado de Produtos("+adapter.getCount()+")");
										footerView.setVisibility(View.VISIBLE); 
									}
									else
									{
										if(what==C_SETTING_DATA)
											footerView.setVisibility(View.VISIBLE); 										
										gridheader.setText("Listado de Productos("+objL.size()+")");
										adapter=new GenericAdapter(mcontext,ProductoViewHolder.class,objL,R.layout.gridproducto);								 
										positioncache=0;
										adapter.setSelectedPosition(0);										
										producto_selected=(vmProducto) adapter.getItem(0);
										lvproducto.setAdapter(adapter); 
										lvproducto.setOnItemClickListener(new OnItemClickListener() 
								        {
								            @Override
								            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
								            { 		  
								            	if((parent.getChildAt(positioncache))!=null)						            							            		
								            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
								            	positioncache=position;
								            	adapter.setSelectedPosition(position);
								            	producto_selected=(vmProducto) adapter.getItem(position);
								            	producto_selected.toggleChecked();
								            	view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_item_selected));     	 
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
								            	producto_selected=(vmProducto) adapter.getItem(position);				
								            	adapter.setSelectedPosition(position); 
								            	view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_item_selected));
								            	quickAction.show(view,display,false);
												return true;
											}
								        	
								        });								        
										buildToastMessage("sincronización exitosa",Toast.LENGTH_SHORT).show();
									}
									
								}
								catch(Exception e)
						    	{ 
						    	    e.printStackTrace();  
						    	    buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show(); 
						    	}
							}
				   });
				}
				else 
					limpiarGrilla(); 
				
				 
				
				
		}
		catch(Exception e)
		{
			Log.d(TAG,"Error=>"+e.getMessage()+"---"+e.getCause());
			e.printStackTrace();
			buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show(); 
		}
		
	} 
	@SuppressLint("ShowToast")
	public Toast buildToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(getApplicationContext(),msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}	
	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		final CustomDialog dialog=new CustomDialog(this);
		dialog.setCancelable(true);
	    dialog.setCanceledOnTouchOutside(true);
	    dialog.setMessageType(type);
	    dialog.setTitulo(tittle);
	    dialog.setMensaje(msg);    
	    dialog.setOnActionDialogButtonClickListener
	    (
    		 new OnActionButtonClickListener()
    		 { 
				@Override
				public void onButtonClick(View _dialog,int actionId) {									 
					/*if(actionId==dialog.OK_BUTTOM && idsucursal==1)
					    controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_SERVER);	
					else if(actionId==dialog.OK_BUTTOM)
						dialog.dismiss();*/
				}
			 } 
	    );
	    dialog.setOnDismissDialogListener
	    (
    		 new OnDismissDialogListener()
    		 {
				@Override
				public void onDismiss() {
					
				}
    		 }
	    );		
	    
	    return dialog;
	}
    private void FINISH_ACTIVITY()
	{
    	nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().disposeEntities();
		Log.d(TAG, "Activity quitting"); 
		finish();	
	}
    @SuppressWarnings("static-access")
	private void limpiarGrilla()
    {    	
    	gridheader.setText("Listado de Productos(0)"); 
		TextView txtenty=(TextView) findViewById(R.id.p_txtview_enty); 
        txtenty.setVisibility(View.VISIBLE);
        lvproducto.setEmptyView(txtenty); 
        footerView.setVisibility(View.INVISIBLE);
        if(adapter!=null){
        	adapter.clearItems();
        	adapter.notifyDataSetChanged();
    	}
        
		
    }
}

