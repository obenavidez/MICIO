package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.*;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.auxiliar.CustomDialog.OnDismissDialogListener;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nordismobile.R;

public class vCliente extends ActionBarActivity implements 
	   ListaFragment.OnItemSelectedListener, Handler.Callback 
{

	// VARIABLES
	CustomArrayAdapter customArrayAdapter;
	NMApp nmapp;
	ProgressDialog pDialog;
	
	SearchView searchView;
	Context context;
	String[] opcionesMenu;
	DrawerLayout drawerLayout;
	ListView drawerList;
	ActionBarDrawerToggle drawerToggle;
	Intent intent;
	CharSequence tituloSeccion;
	CharSequence tituloApp;

	TextView gridheader;
	TextView footerView;	
	ListaFragment<vmCliente> firstFragment;
	List<vmCliente> clientes = new ArrayList<vmCliente>();
	vmCliente cliente_selected;
	

	//Menu Variables
	int listFragmentId;
	int positioncache = 0;
	long idsucursal;
	private Cliente cliente;
	static final String TAG = vCliente.class.getSimpleName();
	private static final int NUEVO_PEDIDO = 0;
	private static final int NUEVO_NOTA_CREDITO=1;
	private static final int NUEVO_RECIBO = 2;
	private static final int NUEVO_DEVOLUCION=3;
	//RECIBO
	public static final String RECIBO_ID = "recibo_id";
	public static final String CLIENTE = "cliente";
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = getApplicationContext();
		setContentView(R.layout.layout_client_fragment);
		
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		footerView = (TextView) findViewById(R.id.ctextv_gridheader);
		
		CreateMenu();
		
		nmapp = (NMApp) this.getApplicationContext();
		try 
		{
			clientes=(savedInstanceState!=null)?clientes=savedInstanceState.getParcelableArrayList("vmCliente"):null;
			if(clientes==null)
				Load_Data(LOAD_DATA_FROM_LOCALHOST);
			else {
				SetList(clientes);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// However, if we're being restored from a previous state,
		// then we don't need to do anything and should return or else
		// we could end up with overlapping fragments.
		if (savedInstanceState != null) {
			return;
		}
		
		// Create an instance of ExampleFragment
		firstFragment = new ListaFragment<vmCliente>();
		firstFragment.setRetainInstance(true);
		// In case this activity was started with special instructions from
		//an Intent,
		// pass the Intent's extras to the fragment as arguments
		firstFragment.setArguments(getIntent().getExtras());
		
		//if device is a mobile 
		if (findViewById(R.id.fragment_container) != null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, firstFragment,"lista")
			.addToBackStack("list")
			.commit();
		}
		else {
			
		}
	}
	


	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		boolean result = false;
		ArrayList<vmCliente> list = null;
		switch (msg.what) {
			case C_DATA:
				 list= (ArrayList<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>() : msg.obj);
				SetList(list);
				pDialog.hide();
				result=true;
					
				break;
			case C_SETTING_DATA:
				 list = (ArrayList<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>() : msg.obj);
				SetData(list, C_SETTING_DATA);
				result=true;
				
				break;
		   case C_UPDATE_ITEM_FINISHED:
			   buildToastMessage(msg.obj.toString(), Toast.LENGTH_SHORT).show();
			   result=true;
				break;
				
		   case C_UPDATE_FINISHED:

			    pDialog.hide();
				result=true;
				break;
				
			case ERROR:
				ErrorMessage error=((ErrorMessage)msg.obj);
				buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
				result=true;
				break;
		}
		return result;
	}

	@Override
	public void onItemSelected(Object obj, int position) {
		// TODO Auto-generated method stub
		cliente_selected = (vmCliente) obj;
		positioncache = position;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub 
		if(pDialog!=null)
    		pDialog.dismiss();
		customArrayAdapter.notifyDataSetChanged();
		super.onPause();
	} 
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		getMenuInflater().inflate(R.menu.mcliente, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		
		if(searchItem!=null) {
			searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	
			customArrayAdapter = ((Filterable) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container)).getAdapter();
	
			searchView.setOnQueryTextListener(new OnQueryTextListener() {
				@Override
				public boolean onQueryTextChange(String s) {
					customArrayAdapter.getFilter().filter(s);
					return false;
				}
	
				@Override
				public boolean onQueryTextSubmit(String s) {
					customArrayAdapter.getFilter().filter(s);
					return false;
				}
			});
		}
		
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean menuAbierto = drawerLayout.isDrawerOpen(drawerList);

		if (menuAbierto)
			menu.findItem(R.id.action_search).setVisible(false);
		/*else
			menu.findItem(R.id.action_search).setVisible(true);*/

		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			item.getItemId();
			return true;
		}
		switch (item.getItemId()) 
		{
			case R.id.sincronizar_all:
				Load_Data(LOAD_DATA_FROM_SERVER);
			break;
			
			case R.id.consultar_fc:
			case R.id.consultar_cxc:
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				drawerLayout.closeDrawers();
				LOAD_FICHACLIENTE_FROMSERVER();
		
			break;
			case R.id.sincronizar_selected:
				UPDATE_SELECTEDITEM_FROMSERVER();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	private void CreateMenu()
	{
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(R.array.customeroptions);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));
		
		//A�adimos Funciones al men� laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				drawerList.setItemChecked(position, true);

				tituloSeccion = opcionesMenu[position];
				//Ponemos el titulo del Men�
				getSupportActionBar().setTitle(tituloSeccion);	
				//SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
				int pos = customArrayAdapter.getSelectedPosition();
				//OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
				cliente_selected =(vmCliente) customArrayAdapter.getItem(pos);
				
				switch (position) {
					case NUEVO_RECIBO : 
						intent = new Intent(vCliente.this,ViewReciboEdit.class);
						//ENVIAR UN RECIBO VACIO EN CASO DE AGREGAR UNO
						try {
							cliente=(Cliente) nmapp.getController().getBridge().getClass().getMethod("getClienteBySucursalID",ContentResolver.class,long.class).invoke(null,vCliente.this.getContentResolver(),cliente_selected.getIdSucursal());
						} 
						catch (Exception e) {
							e.printStackTrace();
						}
						
						intent.putExtra(RECIBO_ID, 0);
						intent.putExtra(CLIENTE,cliente.getIdCliente());
						/*Bundle args = new Bundle();
						args.putInt(RECIBO_ID, 0);
						args.putParcelable(CLIENTE, cliente);
						intent.putExtras(args);*/
						
						startActivity(intent);	
						break;
				
				
				
				}
			}
		});
		
		tituloSeccion = getTitle();
		tituloApp = getTitle();
		
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			
			@Override
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloSeccion);
				ActivityCompat.invalidateOptionsMenu(vCliente.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(vCliente.this);

			}
		};
		//establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
	}
	
	@SuppressWarnings("unchecked")
	private void Load_Data(int what)
	{
		/*controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_SERVER);*/
		try {
			nmapp.getController().setEntities(this, new BClienteM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(what);

			pDialog = new ProgressDialog(vCliente.this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Procesando...");
			pDialog.setCancelable(true);
			pDialog.show();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    { 
        if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {  
        	if (getSupportFragmentManager().getBackStackEntryCount() == 0)
       	    {
        		FINISH_ACTIVITY();
        		return false;
       	    }
        	else
            {
                getSupportFragmentManager().popBackStack();
                return false;
            }
	    }
        return super.onKeyUp(keyCode, event); 
    } 
	*/
    
    
    private void FINISH_ACTIVITY()
	{ 	 		
    	if(pDialog!=null)
    		pDialog.dismiss();
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().disposeEntities();
		Log.d(TAG, "Activity quitting");
		finish();		
	}  
	
	@SuppressWarnings({ "unused", "unchecked" })
	private void SetList(List<vmCliente> list) {
		
		//clientes = (List<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>(): msg.obj);

		clientes = list;
		gridheader.setText(String.format("Listado de Clientes (%s)",clientes.size()));
		
		if (clientes.size() == 0) {
			
			ShowEmptyMessage(true);
		}
		else {
			ShowEmptyMessage(false);
		}
		
		firstFragment.setItems(clientes);
	}
	
	private void ShowEmptyMessage(boolean show)
	{
		TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
		
		if(show)
		{
			txtenty.setVisibility(View.VISIBLE);
		}
		else{
			txtenty.setVisibility(View.INVISIBLE);
		}
	}
	
	private void SetData(final ArrayList<vmCliente> data, final int what) {
		try {
			if (data.size() != 0) {
				
				this.runOnUiThread(new Runnable() {

					@Override
					public void run() 
					{
						try 
						{
							if (what == C_SETTING_DATA && customArrayAdapter != null && customArrayAdapter.getCount() >= 0) {
								customArrayAdapter.AddAllToListViewDataSource(data);
								firstFragment.setItems(data);
								gridheader.setText("Listado de Clientes("+ customArrayAdapter.getCount() + ")");
								footerView.setVisibility(View.VISIBLE);
								ShowEmptyMessage(false);
							}
							else {
								if (what == C_SETTING_DATA)
									footerView.setVisibility(View.VISIBLE);
								
								gridheader.setText("Listado de Clientes("+ data.size() + ")");
								firstFragment.setItems(data);
								customArrayAdapter.setSelectedPosition(0);
								positioncache = 0;
								/*product_selected = customArrayAdapter.getItem(0);*/
							}
							
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void ShowCustomerDetails ()
	{
		Bundle args = new Bundle();
		args.putInt(FichaClienteFragment.ARG_POSITION, positioncache);
		args.putLong(FichaClienteFragment.ARG_SUCURSAL, idsucursal);
		
		//establecemos el titulo
		getSupportActionBar().setTitle(R.string.FichaClienteDialogTitle);
		
		FichaClienteFragment ficha;	
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		if (findViewById(R.id.dynamic_fragment) != null) {
		}
		else
		{
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
			if (fragment instanceof ListaFragment) {
				ficha = new FichaClienteFragment();
				ficha.setArguments(args);
				transaction.addToBackStack(null);
				transaction.replace(R.id.fragment_container, ficha);
				gridheader.setVisibility(View.INVISIBLE);
			}
		}
		// Commit the transaction transaction.commit();
		transaction.commit();
	}
	
	private void LOAD_FICHACLIENTE_FROMSERVER()
	{
		
		idsucursal=get_SucursalID();
		if(idsucursal != 0 && idsucursal != 1)
		{			
			ShowCustomerDetails();		
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
	
	public long get_SucursalID()
	{ 
		if(positioncache!=-1)
		{
			cliente_selected = (vmCliente)customArrayAdapter.getItem(positioncache);
			idsucursal= cliente_selected.getIdSucursal();
		}
		else {
			if(customArrayAdapter!=null){
				if(customArrayAdapter.getCount()!=0)
				{
					idsucursal= cliente_selected.getIdSucursal();
				}
			}
			//idsucursal= ()?((customArrayAdapter.getCount()!=0)?(   (  (cliente_selected!=null)?cliente_selected.getIdSucursal():0  )  ):1):1;
		}
		return idsucursal;
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

	@SuppressLint("ShowToast")
	public Toast buildToastMessage(String msg,int duration)
	{
		Toast toast= Toast.makeText(getApplicationContext(),msg,duration);  
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); 
		return toast;
	}	

	private void UPDATE_SELECTEDITEM_FROMSERVER()
	{
		nmapp.getController().getInboxHandler().sendEmptyMessage(UPDATE_ITEM_FROM_SERVER);
	    Toast.makeText(this, "sincronizando cliente...",Toast.LENGTH_LONG);  
	} 	

	@Override
	protected void onSaveInstanceState(Bundle bundle) 
	{ 
		super.onSaveInstanceState(bundle);
		if(customArrayAdapter!=null && customArrayAdapter.getItems().size()!=0) 
		{  
			clientes=new ArrayList<vmCliente>();
			clientes=customArrayAdapter.getItems();
			bundle.putParcelableArrayList("vmCliente",(ArrayList<? extends Parcelable>) clientes);  
		}
	} 

	@Override
	public void onBackPressed() {
	  Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
	  if (fragment instanceof FichaClienteFragment) {
		  gridheader.setVisibility(View.VISIBLE);
		  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		  transaction.replace(R.id.fragment_container, firstFragment);
		  transaction.addToBackStack(null);
		  transaction.commit();
	  }else{
		  FINISH_ACTIVITY();
	   }
	}
}
