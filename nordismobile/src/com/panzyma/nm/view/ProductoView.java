package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_FICHACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.C_SETTING_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaProductoFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.viewdialog.ConsultaBonificacionesProducto;
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nordismobile.R;


public class ProductoView extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {


	private static final String TAG = ProductoView.class.getSimpleName();
	CustomArrayAdapter<Producto> customArrayAdapter;
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
	ProductoView pv;
	BProductoM bpm;
	private List<Producto> productos = new ArrayList<Producto>();
	Producto product_selected;
	ListaFragment<Producto> firstFragment;
	private static final int FICHA_DETALLE=0;
	private static final int BONIFICACIONES=1;
	private static final int CERRAR=4;


	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.layout_client_fragment);

		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		footerView = (TextView) findViewById(R.id.ctxtview_enty);
		
		CreateMenu();
		SessionManager.setContext(this); 
		
		gridheader.setVisibility(View.VISIBLE);
		nmapp = (NMApp) this.getApplicationContext();
		
		try {
			
			productos=(savedInstanceState!=null)?productos=savedInstanceState.getParcelableArrayList("vmProducto"):null;
			if(productos==null){
				Load_Data(ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);
			}
			else{
				establecer(productos);
			}
			/*			
			if(productos==null){
				NMApp.getController().setEntities(this, new BProductoM());
				NMApp.getController().addOutboxHandler(new Handler(this));
				NMApp.getController().getInboxHandler()
						.sendEmptyMessage(ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);
				
				pDialog = new ProgressDialog(this);
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setMessage("Procesando...");
				pDialog.setCancelable(true);
				pDialog.show();
			}
			else{
				establecer(productos);
			}
 		*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		// However, if we're being restored from a previous state,
		// then we don't need to do anything and should return or else
		// we could end up with overlapping fragments.
		if (savedInstanceState != null) {
			return;
		}

		// Create an instance of ExampleFragment
		firstFragment = new ListaFragment<Producto>();

		// In case this activity was started with special instructions from
		// an Intent,
		// pass the Intent's extras to the fragment as arguments
		firstFragment.setArguments(getIntent().getExtras());

		// Add the fragment to the 'fragment_container' FrameLayout
		if (findViewById(R.id.fragment_container) != null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
			firstFragment.setRetainInstance(true);
		} else {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.item_client_fragment, firstFragment).commit();
		}
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		if (findViewById(R.id.fragment_container) != null) {
			customArrayAdapter = (CustomArrayAdapter<Producto>) ((Filterable) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container)).getAdapter();

		} else {
			customArrayAdapter = (CustomArrayAdapter<Producto>) ((Filterable) getSupportFragmentManager()
					.findFragmentById(R.id.item_client_fragment)).getAdapter();
		}

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

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return false;
		}
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.action_search).setVisible(true);
		super.onPrepareOptionsMenu(menu);
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

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case C_DATA:
			List<Producto> list=(List<Producto>) ((msg.obj == null) ? new ArrayList<Producto>() : msg.obj);
			establecer(list);
			pDialog.dismiss();
			return true;

		case C_FICHACLIENTE:

			return true;

		case C_UPDATE_STARTED:

			return true;
		case C_UPDATE_ITEM_FINISHED:

			return true;
		case C_UPDATE_FINISHED:
			// pDialog.hide();
			return true;
		case C_SETTING_DATA:
			setData((ArrayList<Producto>) ((msg.obj == null) ? new ArrayList<vmProducto>()
					: msg.obj), C_SETTING_DATA);
			return true;
		case ERROR:

			return true;

		}
		return false;

	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void establecer(List<Producto> list) {
		/*productos = (List<Producto>) ((msg.obj == null) ? new ArrayList<Producto>()
				: msg.obj);*/
		productos = list;
		gridheader.setVisibility(View.VISIBLE);
		gridheader.setText(String.format("Listado de Productos (%s)",
				productos.size()));
		if (productos.size() == 0) {
			TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
			txtenty.setVisibility(View.VISIBLE);
		}
		firstFragment.setItems(productos); 
		firstFragment.getAdapter().setSelectedPosition(0);
		positioncache = 0;
		product_selected = firstFragment.getAdapter().getItem(0); 
	}

	private void setData(final ArrayList<Producto> data, final int what) {
		try {
			if (data.size() != 0) {
				this.runOnUiThread(new Runnable() {

					@SuppressWarnings("unchecked")
					@Override
					public void run() {

						try {

							if (what == C_SETTING_DATA
									&& customArrayAdapter != null
									&& customArrayAdapter.getCount() >= 0) {
								firstFragment.setItems(data);
								gridheader.setText("Listado de Productos ("
										+ customArrayAdapter.getCount() + ")");
								footerView.setVisibility(View.VISIBLE);
							} else {
								if (what == C_SETTING_DATA)
									footerView.setVisibility(View.VISIBLE);
								gridheader.setText("Listado de Productos ("
										+ data.size() + ")");
								firstFragment.setItems(data);
								customArrayAdapter.setSelectedPosition(0);
								positioncache = 0;
								product_selected = customArrayAdapter
										.getItem(0);
								/*
								 * lvcliente.setAdapter(adapter);
								 * lvcliente.setOnItemClickListener(new
								 * OnItemClickListener() {
								 * 
								 * @Override public void
								 * onItemClick(AdapterView<?> parent, View view,
								 * int position, long id) {
								 * if((parent.getChildAt(positioncache))!=null)
								 * (parent.getChildAt(positioncache)).
								 * setBackgroundResource
								 * (android.R.color.transparent);
								 * positioncache=position;
								 * cliente_selected=(vmCliente)
								 * customArrayAdapter.getItem(position);
								 * customArrayAdapter
								 * .setSelectedPosition(position);
								 * view.setBackgroundDrawable
								 * (getResources().getDrawable
								 * (R.drawable.action_item_selected));
								 * 
								 * } });
								 * lvcliente.setOnItemLongClickListener(new
								 * OnItemLongClickListener() {
								 * 
								 * @Override public boolean
								 * onItemLongClick(AdapterView<?> parent, View
								 * view,int position, long id) {
								 * if((parent.getChildAt(positioncache))!=null)
								 * (parent.getChildAt(positioncache)).
								 * setBackgroundResource
								 * (android.R.color.transparent);
								 * positioncache=position;
								 * cliente_selected=(vmCliente)
								 * customArrayAdapter.getItem(position);
								 * customArrayAdapter
								 * .setSelectedPosition(position);
								 * view.setBackgroundDrawable
								 * (getResources().getDrawable
								 * (R.drawable.action_item_selected));
								 * //quickAction.show(view,display,false);
								 * return true; }
								 * 
								 * });
								 */
								// buildToastMessage("sincronizaciÃ³n exitosa",Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							// buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
						}

					}
				});

			}
			/*
			 * else limpiarGrilla();
			 */
		} catch (Exception e) {
			// Log.d(TAG,"Error=>"+e.getMessage()+"---"+e.getCause());
			e.printStackTrace();
			// buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}

	}

	@Override 
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {        	
          Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
      	  if (fragment instanceof FichaProductoFragment) {
      		  gridheader.setVisibility(View.VISIBLE);
      		  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      		  transaction.detach(fragment);
      		  transaction.replace(R.id.fragment_container, firstFragment);
      		  transaction.addToBackStack(null);
      		  transaction.commit();
      		  getSupportActionBar().show();
      	  }
      	  else{
      		  FINISH_ACTIVITY();
      	   }
           return true;
	    }
        return super.onKeyUp(keyCode, event); 
    } 
	
	private void FINISH_ACTIVITY()
	{ 	 		
		NMApp.getController().removeOutboxHandler(TAG);
		NMApp.getController().disposeEntities();
		Log.d(TAG, "Activity quitting");
		finish();		
	}
	
	@Override 
	public void onItemSelected(Object obj, int position) 
	{

		product_selected=(Producto) obj;
		positioncache=position;
		
//		FichaProductoFragment productFrag;
//		Bundle args = new Bundle();
//		args.putInt(FichaProductoFragment.ARG_POSITION, position);
//		args.putParcelable(FichaProductoFragment.OBJECT, (Producto) obj);
//
//		FragmentTransaction transaction = getSupportFragmentManager()
//				.beginTransaction();		
//
//		if (findViewById(R.id.dynamic_fragment) != null) {
//
//			productFrag = (FichaProductoFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.dynamic_fragment);
//			if (productFrag != null) {				
//				productFrag.updateArticleView((Producto) obj, position);
//			} else {
//				productFrag = new FichaProductoFragment();
//				productFrag.setArguments(args);
//				transaction.add(R.id.dynamic_fragment, productFrag);
//				transaction.addToBackStack(null);
//			}
//
//		} else {
//
//
//			Fragment fragment = getSupportFragmentManager().findFragmentById(
//					R.id.fragment_container);
//			
//			gridheader.setVisibility(View.INVISIBLE);
//
//			if (fragment instanceof ListaFragment) {
//				productFrag = new FichaProductoFragment();
//				productFrag.setArguments(args);
//				transaction.replace(R.id.fragment_container, productFrag);
//				transaction.addToBackStack(null);			
//			}
//		}
//		// Commit the transaction transaction.commit();
//		transaction.commit();

	}
	@Override
	protected void onSaveInstanceState(Bundle bundle) 
	{ 
		super.onSaveInstanceState(bundle);
		if(customArrayAdapter!=null && customArrayAdapter.getItems().size()!=0) 
		{  
			productos=new ArrayList<Producto>();
			productos=customArrayAdapter.getItems();
			bundle.putParcelableArrayList("vmProducto",(ArrayList<? extends Parcelable>) productos);  
		}
	}
	
	private void CreateMenu(){

	opcionesMenu = getResources().getStringArray(R.array.productoptions);
	drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	drawerList = (ListView) findViewById(R.id.left_drawer);

	drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
		.getThemedContext(), android.R.layout.simple_list_item_1,
		opcionesMenu));

	drawerList.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			drawerList.setItemChecked(position, true);
			tituloSeccion = opcionesMenu[position];
			getSupportActionBar().setTitle(tituloSeccion);
			
				//SELECCIONAR LA POSICION DEL PRODUCTO SELECCIONADO ACTUALMENTE
			positioncache = customArrayAdapter.getSelectedPosition();
				//OBTENER EL PRODUCTO DE LA LISTA
			product_selected =customArrayAdapter.getItem(positioncache);
			switch (position) {
				case FICHA_DETALLE :
				if(product_selected== null){
					AppDialog.showMessage(pv,"Información","Seleccione un registro.",DialogType.DIALOGO_ALERTA);
					return;
				}
				Bundle args = new Bundle();
				args.putInt(FichaProductoFragment.ARG_POSITION, position);
				args.putParcelable(FichaProductoFragment.OBJECT, product_selected);
				FichaProductoFragment productFrag;
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();	
				if (findViewById(R.id.dynamic_fragment) != null) {
					productFrag = (FichaProductoFragment) getSupportFragmentManager().findFragmentById(R.id.dynamic_fragment);
					if (productFrag != null) {				
						productFrag.updateArticleView(product_selected, position);
					} 
					else {
						productFrag = new FichaProductoFragment();
						productFrag.setArguments(args);
						transaction.add(R.id.dynamic_fragment, productFrag);
						transaction.addToBackStack(null);
					}

				} 
				else {
					Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
					gridheader.setVisibility(View.INVISIBLE);
					if (fragment instanceof ListaFragment) {
						productFrag = new FichaProductoFragment();
						productFrag.setArguments(args);
						transaction.replace(R.id.fragment_container, productFrag);
						transaction.addToBackStack(null);			
					}
				}
				 //Commit the transaction transaction.commit();
				transaction.commit();
				drawerLayout.closeDrawers();
				break; 
				case BONIFICACIONES:
					if(product_selected== null){
						AppDialog.showMessage(pv,"Información","Seleccione un registro.",DialogType.DIALOGO_ALERTA);
						return;
					}
					
					FragmentTransaction Fragtransaction = getSupportFragmentManager().beginTransaction();
					android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
					if (prev != null){
						Fragtransaction.remove(prev);
					}
					Fragtransaction.addToBackStack(null);
					ConsultaBonificacionesProducto bonificaciones = ConsultaBonificacionesProducto.newInstance( product_selected.getId(), 0);
					bonificaciones.show(Fragtransaction, "dialog");
					drawerLayout.closeDrawers();
					break;
				case CERRAR : 
				drawerLayout.closeDrawers();
				FINISH_ACTIVITY();
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
			ActivityCompat.invalidateOptionsMenu(ProductoView.this);
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			getSupportActionBar().setTitle(tituloApp);
			ActivityCompat.invalidateOptionsMenu(ProductoView.this);
		}
	};

	drawerLayout.setDrawerListener(drawerToggle);

	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	getSupportActionBar().setHomeButtonEnabled(true);
}

	private void Load_Data(int what)
	{
		try {
				NMApp.getController().setEntities(this, bpm= new BProductoM());
				NMApp.getController().addOutboxHandler(new Handler(this));
				NMApp.getController().getInboxHandler().sendEmptyMessage(what);
				
				pDialog = new ProgressDialog(this);
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setMessage("Procesando...");
				pDialog.setCancelable(true);
				pDialog.show();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public BProductoM getBridge() {
		return bpm;
	}
}
