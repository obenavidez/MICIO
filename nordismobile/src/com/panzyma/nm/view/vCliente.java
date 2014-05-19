package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.CONFIRMATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_FICHACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.C_SETTING_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_FICHACLIENTE_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.*;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
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
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nm.viewmodel.vmFicha;
import com.panzyma.nordismobile.R;
import com.panzyma.nm.viewmodel.*;

public class vCliente extends ActionBarActivity implements 
	   ListaFragment.OnItemSelectedListener, Handler.Callback {

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

	CharSequence tituloSeccion;
	CharSequence tituloApp;

	TextView gridheader;
	TextView footerView;	
	ListaFragment<vmCliente> firstFragment;
	List<vmCliente> clientes = new ArrayList<vmCliente>();
	vmCliente cliente_selected;
	

	//Menu Variables
	int listFragmentId;
	int positioncache = -1;
	long idsucursal;
	static final String TAG = vCliente.class.getSimpleName();
	
	
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
			Load_Data(LOAD_DATA_FROM_LOCALHOST);
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
		
		// In case this activity was started with special instructions from
		//an Intent,
		// pass the Intent's extras to the fragment as arguments
		firstFragment.setArguments(getIntent().getExtras());
		
		//if device is a mobile 
		if (findViewById(R.id.fragment_container) != null) {
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
		}
		else {
			
		}
		
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		boolean result = false;
		
		switch (msg.what) {
			case C_DATA:
				
				SetList(msg);
				pDialog.hide();
				result=true;
					
				break;
			case C_SETTING_DATA:
				
				ArrayList<vmCliente> list = (ArrayList<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>() : msg.obj);
				SetData(list, C_SETTING_DATA);
				
				result=true;
				
				break;
			case C_FICHACLIENTE:
				vmFicha DetailCustomerSelected = ((vmFicha)((msg.obj==null)?new vmFicha():msg.obj));
				ShowCustomerDetails(DetailCustomerSelected);
				
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
			return true;
		}
		switch (item.getItemId()) 
		{
			case R.id.sincronizar_all:
				Load_Data(LOAD_DATA_FROM_SERVER);
			break;
			
			case R.id.consultar_fc:
			case R.id.consultar_cxc:
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
		
		//Añadimos Funciones al menú laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				drawerList.setItemChecked(position, true);

				tituloSeccion = opcionesMenu[position];
				//Ponemos el titulo del Menú
				getSupportActionBar().setTitle(tituloSeccion);
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
	
	@SuppressWarnings({ "unused", "unchecked" })
	private void SetList(Message msg) {
		
		clientes = (List<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>(): msg.obj);

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
					public void run() {
						try {
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
	
	private void ShowCustomerDetails (vmFicha detailselected)
	{
		Bundle args = new Bundle();
		args.putInt(FichaClienteFragment.ARG_POSITION, positioncache);
		args.putParcelable(FichaClienteFragment.OBJECT, detailselected);

		
		//establecemos el titulo
		getSupportActionBar().setTitle(R.string.FichaClienteDialogTitle);
		
		FichaClienteFragment ficha;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		if (findViewById(R.id.dynamic_fragment) != null) {
			
		}
		else {

			@SuppressWarnings("unused")
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
			if (fragment instanceof ListaFragment) {
				ficha = new FichaClienteFragment();
				ficha.setArguments(args);
				transaction.replace(R.id.fragment_container, ficha);
				transaction.addToBackStack(null);	
				/*gridheader.setVisibility(View.INVISIBLE);*/
			}
		}
		// Commit the transaction transaction.commit();
		transaction.commit();
	}
	
	private void LOAD_FICHACLIENTE_FROMSERVER()
	{
		get_SucursalID();
		
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
	
	public long get_SucursalID()
	{ 
		return (customArrayAdapter!=null)?((customArrayAdapter.getCount()!=0)?(   (  (cliente_selected!=null)?cliente_selected.getIdSucursal():0  )  ):1):1;
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

}
