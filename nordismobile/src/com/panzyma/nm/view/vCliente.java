package com.panzyma.nm.view;

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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.auxiliar.CustomDialog.OnDismissDialogListener;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nordismobile.R;
import com.panzyma.nm.viewmodel.*;

public class vCliente extends ActionBarActivity implements ListaFragment.OnItemSelectedListener, Handler.Callback {

	CustomArrayAdapter customArrayAdapter;
	private SearchView searchView;
	int listFragmentId;
	int positioncache = -1;
	private Context context;

	private String[] opcionesMenu;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	private CharSequence tituloSeccion;
	private CharSequence tituloApp;
	private NMApp nmapp;
	ProgressDialog pDialog;
	TextView gridheader;
	TextView footerView;

	private List<vmCliente> clientes = new ArrayList<vmCliente>();
	vmCliente cliente_selected;
	ListaFragment<vmCliente> firstFragment;
	//Menu Variables
	private long idsucursal;
	private static final String TAG = vCliente.class.getSimpleName();

	

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.layout_client_fragment);
		
		initComponent();

		opcionesMenu = getResources().getStringArray(R.array.customeroptions);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// drawerLayout.openDrawer(Gravity.END);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				 * Fragment fragment = null;
				 * 
				 * switch (position) { case 0: fragment = new Fragment1();
				 * break; case 1: fragment = new Fragment2(); break; case 2:
				 * fragment = new Fragment3(); break; }
				 * 
				 * FragmentManager fragmentManager =
				 * getSupportFragmentManager();
				 * 
				 * fragmentManager.beginTransaction()
				 * .replace(R.id.fragment_container, fragment) .commit();
				 */

				drawerList.setItemChecked(position, true);

				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);

				// drawerLayout.closeDrawer(drawerList);
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

		
		drawerLayout.setDrawerListener(drawerToggle);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		nmapp = (NMApp) this.getApplicationContext();
		try {
			nmapp.getController().setEntities(this, new BClienteM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);

			pDialog = new ProgressDialog(vCliente.this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Procesando...");
			pDialog.setCancelable(true);
			pDialog.show();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Check whether the activity is using the layout version with
		// the fragment_container FrameLayout. If so, we must add the first
		// fragment
		if (findViewById(R.id.fragment_container) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of ExampleFragment
			firstFragment = new ListaFragment<vmCliente>();
			//firstFragment.setItems(clientes);

			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout

			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
	}

	@Override
	public void onItemSelected(int position) {
		// The user selected the headline of an article from the
		// HeadlinesFragment

		// Capture the article fragment from the activity layout
		// R.id.article_fragment
		FichaClienteFragment articleFrag = (FichaClienteFragment) getSupportFragmentManager()
				.findFragmentById(R.id.ficha_client_fragment);

		if (articleFrag != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			articleFrag.updateArticleView(position);

		} else {
			// If the frag is not available, we're in the one-pane layout and
			// must swap frags...

			// Create fragment and give it an argument for the selected article
			/*FichaClienteFragment newFragment = new FichaClienteFragment();
			Bundle args = new Bundle();
			args.putInt(FichaClienteFragment.ARG_POSITION, position);
			newFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack so the user can
			// navigate back

			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();*/
		}
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
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "prueba", Toast.LENGTH_SHORT).show();
			;
			break;
		case R.id.action_search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			break;
		case R.id.sincronizar_all: 
			LOAD_FROMSERVER();
			break;
		case R.id.consultar_fc:
		case R.id.consultar_cxc:
			LOAD_FICHACLIENTE_FROMSERVER();
			break;
		case R.id.sincronizar_selected:
			UPDATE_SELECTEDITEM_FROMSERVER();
			break;
		case R.id.salir:
			FINISH_ACTIVITY();
			break;
		default:
			return super.onOptionsItemSelected(item);
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
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
		case C_DATA:
			establecer(msg);
			pDialog.hide();
			return true;

		case C_FICHACLIENTE:

			return true;

		case C_UPDATE_STARTED:

			return true;
		case C_UPDATE_ITEM_FINISHED:

			return true;
		case C_UPDATE_FINISHED:
			pDialog.hide();
			return true;
		case C_SETTING_DATA:
			setData((ArrayList<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>()
					: msg.obj), C_SETTING_DATA);
			return true;
		case ERROR:

			return true;

		}
		return false;

	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void establecer(Message msg) {
		clientes = (List<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>()
				: msg.obj);

		gridheader.setText(String.format("Listado de Clientes (%s)",
				clientes.size()));
		if (clientes.size() == 0) {
			TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
			txtenty.setVisibility(View.VISIBLE);
		}
		firstFragment.setItems(clientes);
	}

	private void setData(final ArrayList<vmCliente> data, final int what) {
		try {
			if (data.size() != 0) {
				this.runOnUiThread(new Runnable() {

					@SuppressWarnings("unchecked")
					@Override
					public void run() {

						try {

							if (what == C_SETTING_DATA && customArrayAdapter != null && customArrayAdapter.getCount() >= 0) {
								firstFragment.setItems(data);
								gridheader.setText("Listado de Clientes("+ customArrayAdapter.getCount() + ")");
								footerView.setVisibility(View.VISIBLE);
							} 
							else {
								if (what == C_SETTING_DATA)
									footerView.setVisibility(View.VISIBLE);
							
								gridheader.setText("Listado de Clientes(" + data.size() + ")");
								firstFragment.setItems(data);
								customArrayAdapter.setSelectedPosition(0);
								positioncache = 0;
								cliente_selected = (vmCliente) customArrayAdapter.getItem(0);
								
								// buildToastMessage("sincronización exitosa",Toast.LENGTH_SHORT).show();
							}
						} 
						catch (Exception e) {
							e.printStackTrace();
							// buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
						}

					}
				});

			}
			/*
			 * else limpiarGrilla();
			 */
		} 
		catch (Exception e) {
			// Log.d(TAG,"Error=>"+e.getMessage()+"---"+e.getCause());
			e.printStackTrace();
			// buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}

	}

	private void initComponent() {
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		footerView = (TextView) findViewById(R.id.ctextv_gridheader);
		
		
	}
	
	/* Opciones del Menú */
	private void LOAD_FROMSERVER()
	{
		/*controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_SERVER);*/
		try {
			nmapp.getController().setEntities(this, new BClienteM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);

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
    
	private void UPDATE_SELECTEDITEM_FROMSERVER()
	{
		nmapp.getController().getInboxHandler().sendEmptyMessage(UPDATE_ITEM_FROM_SERVER);
	    Toast.makeText(this, "sincronizando cliente...",Toast.LENGTH_LONG);  
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
}
