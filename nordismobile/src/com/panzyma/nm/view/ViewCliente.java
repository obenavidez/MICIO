package com.panzyma.nm.view;
 
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.CONFIRMATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_FICHACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.C_SETTING_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_FICHACLIENTE_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_ITEM_FROM_SERVER;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panzyma.nm.DashBoardActivity;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.auxiliar.CustomDialog.OnDismissDialogListener;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nm.viewdialog.DialogCuentasPorCobrar;
import com.panzyma.nm.viewdialog.DialogFichaCliente;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nordismobile.R;

@SuppressLint("ParserError")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ViewCliente extends DashBoardActivity implements Handler.Callback
{   	 	 
	
	private GenericAdapter adapter; 
	private ArrayList<vmCliente> _vmCliente;
	private vmCliente cliente_selected;  
	private Button Menu; 
	private QuickAction quickAction;
	private long idsucursal;
	private int positioncache=-1;
	private Display display;
	private static final String TAG = ViewCliente.class.getSimpleName();
	private Context mcontext;
	
	ListView lvcliente;	
	TextView gridheader;
	View footerView ;
	private NMApp nmapp;
	
	private static final int ID_CONSULTAR_FICHA_CLIENT  = 1;
	private static final int ID_CONSULTAR_CUENTA_X_COBRAR = 2;
	private static final int ID_CONSULTAR_VENCIDOS= 3;
	private static final int ID_SINCRONIZE_ALL_CLIENT= 4;
	private static final int ID_SINCRONIZE_SELECTED_CLIENT = 5;	
	private static final int ID_CERRAR = 6; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maincliente);
		setHeader(getString(R.string.ClientActivityTitle),true, false);  	 
	    try 
	    { 
	    	mcontext=this; 
	    	
		 	nmapp=(NMApp) this.getApplicationContext(); 
	        nmapp.getController().setEntities(this,new BClienteM());
	        nmapp.getController().addOutboxHandler(new Handler(this));  			
			WindowManager wm = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay(); 
			_vmCliente=(savedInstanceState!=null)?_vmCliente=savedInstanceState.getParcelableArrayList("vmCliente"):null;
			
			initComponents(); 
			if(_vmCliente==null) 
				nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);  
			else
				setData(_vmCliente,C_DATA);  
	        
		}catch (Exception e) { 
			e.printStackTrace();
			buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}	    
	     
	}
	 
	@Override
	protected void onSaveInstanceState(Bundle bundle) 
	{ 
		super.onSaveInstanceState(bundle);
		if(adapter!=null && adapter.getData().size()!=0) 
		{  
			_vmCliente=new ArrayList<vmCliente>();
			_vmCliente=(ArrayList<vmCliente>) adapter.getData();
			bundle.putParcelableArrayList("vmCliente",_vmCliente);  
		}
	} 
	
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_MENU) 
        {        	
        	Menu = (Button)findViewById(R.id.btn_menu);
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

	    LinearLayout viewroot=((LinearLayout)findViewById(R.id.lmaincliente));
	    viewroot.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(),display.getHeight()));
	    
	    lvcliente = (ListView) findViewById(R.id.lvcliente);	
	    gridheader=(TextView) findViewById(R.id.ctextv_gridheader);
	    footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
		footerView.setVisibility(View.GONE);
		lvcliente.addFooterView(footerView);
    	
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
    	InitMenu();
    }
  
    public void InitMenu()
    {
    	quickAction = new QuickAction(this, QuickAction.VERTICAL,1);		 
        quickAction.addActionItem((new ActionItem(ID_CONSULTAR_FICHA_CLIENT, "Consultar ficha del Cliente")));
		quickAction.addActionItem((new ActionItem(ID_CONSULTAR_CUENTA_X_COBRAR, "Consultar Cuentas x Cobrar")));
        quickAction.addActionItem((new ActionItem(ID_CONSULTAR_VENCIDOS, "Consultar Vencidos")));
        quickAction.addActionItem(null);
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_ALL_CLIENT, "Sincronizar todos los Clientes")));
        quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_SELECTED_CLIENT, "Sincronizar item Seleccionado")));
        quickAction.addActionItem(null);
        quickAction.addActionItem((new ActionItem(ID_CERRAR, "Cerrar")));
         
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) 
			{				
				Controller controller=nmapp.getController();
				ActionItem actionItem = quickAction.getActionItem(pos);    
				
				if (actionId == ID_CONSULTAR_FICHA_CLIENT || actionId == ID_CONSULTAR_CUENTA_X_COBRAR) 
						LOAD_FICHACLIENTE_FROMSERVER();	 
				/*if (actionId == ID_CONSULTAR_VENCIDOS) 
					LOAD_FICHACLIENTE_FROMSERVER();	*/
				else if (actionId == ID_SINCRONIZE_ALL_CLIENT) 
						controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_SERVER);
				else if (actionId == ID_SINCRONIZE_SELECTED_CLIENT) 
						UPDATE_SELECTEDITEM_FROMSERVER(); 
				else if (actionId == ID_CERRAR) 
					FINISH_ACTIVITY();
				else  
				    Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				 
			}
		});
		
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() 
		{			
			@Override
			public void onDismiss() { 
			}
		});
    	
    	
    }    
	
    @SuppressLint({ "ParserError", "ParserError" })
	@Override
	public boolean handleMessage(Message msg) {
		Log.d(TAG, "Received message: " + msg);
		switch (msg.what) 
		{		 
			case C_DATA:   
				
					setData((ArrayList<vmCliente>)((msg.obj==null)?new ArrayList<vmCliente>():msg.obj),C_DATA);
					return true;
					
			case C_FICHACLIENTE: 
					if(quickAction.getItemSelected()==ID_CONSULTAR_FICHA_CLIENT)
							(new DialogFichaCliente(ViewCliente.this,android.R.style.Animation_Translucent,((CCCliente)((msg.obj==null)?new CCCliente():msg.obj)),get_SucursalID())).show();
					else if(quickAction.getItemSelected()==ID_CONSULTAR_CUENTA_X_COBRAR)
							(new DialogCuentasPorCobrar(
									ViewCliente.this,
									android.R.style.Animation_Translucent,
									((CCCliente)((msg.obj==null)?new CCCliente():msg.obj)),get_SucursalID())).show();
					return true; 
									
			case C_UPDATE_STARTED:
					limpiarGrilla(); 
					return true; 
			case C_UPDATE_ITEM_FINISHED: 
					buildToastMessage(msg.obj.toString(), Toast.LENGTH_SHORT).show();
					return true;
			case C_UPDATE_FINISHED: footerView.setVisibility(View.INVISIBLE);
									return true;
			
			case C_SETTING_DATA: 
				    setData((ArrayList<vmCliente>)((msg.obj==null)?new ArrayList<vmCliente>():msg.obj),C_SETTING_DATA);
				    return true;		
			case ERROR: 
					ErrorMessage error=((ErrorMessage)msg.obj);
					buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
					return true;	
					
		}
		return false;
	} 
    
	private void setData(final ArrayList<vmCliente> data,final int  what) 
	{
		try
		{  
			if(data.size()!=0)
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
									adapter.AddAllToListViewDataSource(data);
									gridheader.setText("Listado de Clientes("+adapter.getCount()+")");
									footerView.setVisibility(View.VISIBLE); 
								}
								else
								{
									if(what==C_SETTING_DATA)
										footerView.setVisibility(View.VISIBLE); 
									gridheader.setText("Listado de Clientes("+data.size()+")");
									adapter=new GenericAdapter(mcontext,ClienteViewHolder.class,data,R.layout.gridcliente);								 
									adapter.setSelectedPosition(0); 
									positioncache=0;
									cliente_selected=(vmCliente) adapter.getItem(0);
									lvcliente.setAdapter(adapter);
									lvcliente.setOnItemClickListener(new OnItemClickListener() 
							        {
							            @Override
							            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
							            { 		  
							            	if((parent.getChildAt(positioncache))!=null)						            							            		
							            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
							            	positioncache=position;				            	
							            	cliente_selected=(vmCliente) adapter.getItem(position);				
							            	adapter.setSelectedPosition(position); 
							            	view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_item_selected));					            	 
							            	
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
				  }
			   ); 
				
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
	
	private void UPDATE_SELECTEDITEM_FROMSERVER()
	{
		nmapp.getController().getInboxHandler().sendEmptyMessage(UPDATE_ITEM_FROM_SERVER);
	    Toast.makeText(this, "sincronizando cliente...",Toast.LENGTH_LONG);  
	} 	
	
	private void LOAD_FICHACLIENTE_FROMSERVER()
	{
		idsucursal=get_SucursalID();
		if(idsucursal != 0 && idsucursal != 1)
		{			
			nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_FICHACLIENTE_FROM_SERVER);
		    Toast.makeText(this, "Trayendo Ficha Cliente...",Toast.LENGTH_LONG); 			
    	}
		else 
		{ 
		    if(idsucursal==1) 					
		    	buildCustomDialog("No hay cliente que consultar",
		    			          "Debe sincronizar con el servidor primero...\nDesea Sincronizar ahora?",
		    			           CONFIRMATION_DIALOG).show(); 	 
			else 
				buildCustomDialog("No hay cliente que consultar","Seleccione cliente primero",ALERT_DIALOG).show();  
		}
	} 
	
	private void FINISH_ACTIVITY()
	{ 	 		
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().disposeEntities();
		Log.d(TAG, "Activity quitting");
		finish();		
	}  
	
    public long get_SucursalID()
	{ 
		return (adapter!=null)?((adapter.getCount()!=0)?(   (  (cliente_selected!=null)?cliente_selected.getIdSucursal():0  )  ):1):1;
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
				@SuppressWarnings("static-access")
				@Override
				public void onButtonClick(View _dialog,int actionId) {									 
					if(actionId==CustomDialog.OK_BUTTOM && idsucursal==1)
						nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_SERVER);	
					else if(actionId==CustomDialog.OK_BUTTOM)
						dialog.dismiss();
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

    @SuppressWarnings("static-access")
	private void limpiarGrilla()
    {    	
    	gridheader.setText("Listado de Clientes(0)"); 
		TextView txtenty=(TextView) findViewById(R.id.ctxtview_enty); 
        txtenty.setVisibility(View.VISIBLE);
        lvcliente.setEmptyView(txtenty); 
        footerView.setVisibility(View.INVISIBLE);
        if(adapter!=null)
        {
        	adapter.clearItems();
        	adapter.notifyDataSetChanged();
    	}
        

    }
	
	
	
}
