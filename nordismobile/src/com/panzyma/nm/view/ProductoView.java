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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.fragments.FichaProductoFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nordismobile.R;

public class ProductoView extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {

	CustomArrayAdapter<vmProducto> customArrayAdapter;
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

	private List<vmProducto> productos = new ArrayList<vmProducto>();
	vmProducto product_selected;
	ListaFragment<vmProducto> firstFragment;

	@SuppressLint("CutPasteId")
	private void initComponent() {
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		footerView = (TextView) findViewById(R.id.ctextv_gridheader);
	}

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.layout_client_fragment);

		initComponent();

		opcionesMenu = new String[] { "Ficha Detalle", "Bonificaciones",
				"Lista de Precios", "Sincronizar Productos", "Cerrar" };
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

				drawerList.setItemChecked(position, true);

				tituloSeccion = opcionesMenu[position];
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

		nmapp = (NMApp) this.getApplicationContext();
		try {
			nmapp.getController().setEntities(this, new BProductoM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(ControllerProtocol.LOAD_DATA_FROM_SERVER);

		} catch (Exception e) {
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
			firstFragment = new ListaFragment<vmProducto>();

			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout

			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		customArrayAdapter = (CustomArrayAdapter<vmProducto>) ((Filterable) getSupportFragmentManager()
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

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
			;
			break;
		case R.id.action_search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
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
		else
			menu.findItem(R.id.action_search).setVisible(true);

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

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case C_DATA:
			establecer(msg);
			// pDialog.hide();
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
			setData((ArrayList<vmProducto>) ((msg.obj == null) ? new ArrayList<vmProducto>()
					: msg.obj), C_SETTING_DATA);
			return true;
		case ERROR:

			return true;

		}
		return false;

	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void establecer(Message msg) {
		productos = (List<vmProducto>) ((msg.obj == null) ? new ArrayList<vmProducto>()
				: msg.obj);

		gridheader.setText(String.format("Listado de Productos (%s)",
				productos.size()));
		if (productos.size() == 0) {
			TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
			txtenty.setVisibility(View.VISIBLE);
		}
		firstFragment.setItems(productos);
	}

	private void setData(final ArrayList<vmProducto> data, final int what) {
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
								gridheader.setText("Listado de Clientes("
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
								// buildToastMessage("sincronización exitosa",Toast.LENGTH_SHORT).show();
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
	public void onItemSelected(int position) {
		// The user selected the headline of an article from the
		// HeadlinesFragment

		// Capture the article fragment from the activity layout
		// R.id.article_fragment
		FichaProductoFragment productFrag = (FichaProductoFragment) getSupportFragmentManager()
				.findFragmentById(R.id.ficha_client_fragment);

		if (productFrag != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			productFrag.updateArticleView(position);

		} else {
			// If the frag is not available, we're in the one-pane layout and
			// must swap frags...

			// Create fragment and give it an argument for the selected article
			/*
			 * FichaClienteFragment newFragment = new FichaClienteFragment();
			 * Bundle args = new Bundle();
			 * args.putInt(FichaClienteFragment.ARG_POSITION, position);
			 * newFragment.setArguments(args); FragmentTransaction transaction =
			 * getSupportFragmentManager() .beginTransaction();
			 * 
			 * // Replace whatever is in the fragment_container view with this
			 * // fragment, // and add the transaction to the back stack so the
			 * user can // navigate back
			 * 
			 * transaction.replace(R.id.fragment_container, newFragment);
			 * transaction.addToBackStack(null);
			 * 
			 * // Commit the transaction transaction.commit();
			 */

		}
	}

	@Override
	public void onItemSelected(Object obj, int position) {
		// The user selected the headline of an article from the
		// HeadlinesFragment

		// Capture the article fragment from the activity layout
		// R.id.article_fragment
		FichaProductoFragment productFrag = (FichaProductoFragment) getSupportFragmentManager()
				.findFragmentById(R.id.ficha_client_fragment);

		if (productFrag != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			productFrag.updateArticleView((vmProducto) obj, position);

		} else {
			// If the frag is not available, we're in the one-pane layout and
			// must swap frags...

			// Create fragment and give it an argument for the selected article

			FichaProductoFragment newFragment = new FichaProductoFragment();
			Bundle args = new Bundle();
			args.putInt(FichaProductoFragment.ARG_POSITION, position);
			args.putParcelable(FichaProductoFragment.OBJECT, (vmProducto) obj);
			newFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment, // and add the transaction to the back stack so the
			// user can navigate back

			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction transaction.commit();
			transaction.commit();
		}

	}

}
