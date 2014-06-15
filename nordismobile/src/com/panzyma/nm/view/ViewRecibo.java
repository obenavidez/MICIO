package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_FICHACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.C_SETTING_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaProductoFragment;
import com.panzyma.nm.fragments.FichaReciboFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.serviceproxy.Recibo;
import com.panzyma.nm.viewmodel.vmRecibo;
import com.panzyma.nordismobile.R;

public class ViewRecibo extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {

	private static final String TAG = ViewRecibo.class.getSimpleName();
	private static final int NUEVO_RECIBO = 0;
	private static final int VER_DETALLE_RECIBO = 1;
	private static final int BORRAR_RECIBO = 2;
	public static final String RECIBO_ID = "recibo_id";

	CustomArrayAdapter<vmRecibo> customArrayAdapter;
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
	Intent intento;
	private List<vmRecibo> recibos = new ArrayList<vmRecibo>();
	vmRecibo recibo_selected;
	ListaFragment<vmRecibo> firstFragment;
	FragmentTransaction transaction;
	
	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.layout_client_fragment);
		
		transaction = getSupportFragmentManager()
				.beginTransaction();


		initComponent();

		gridheader.setVisibility(View.VISIBLE);

		opcionesMenu = new String[] { "Nuevo Recibo", "Ver Detalle",
				"Borrar Recibo", "Enviar Recibo", "Imprimir Recibo",
				"Ficha del Cliente", "Cuentas por Cobrar", "Cerrar" };

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
				
				//SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
				int pos = customArrayAdapter.getSelectedPosition();
				//OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
				recibo_selected = customArrayAdapter.getItem(pos);
				
				switch (position) {
				case NUEVO_RECIBO:
					intento = new Intent(ViewRecibo.this, ViewReciboEdit.class);
					//ENVIAR UN RECIBO VACIO EN CASO DE AGREGAR UNO
					intento.putExtra(RECIBO_ID, 0);
					startActivity(intento);  
					/*firstFragment.setAdapter(null);
					finish();*/
					break;
				case VER_DETALLE_RECIBO:
					intento = new Intent(ViewRecibo.this, ViewReciboEdit.class);
					//ENVIAR EL RECIBO SELECCIONADO EN CASO DE VER DEL DETALLE
					intento.putExtra(RECIBO_ID, recibo_selected.getId());
					startActivity(intento);
					/*firstFragment.setAdapter(null);
					finish();*/
					break;
				case BORRAR_RECIBO:
					//NO PERMITIR ELIMINAR RECIBOS DONDE EL ESTADO SEA DISTINTO A REGISTRADO 
					if ( "REGISTRADO".equals(recibo_selected.getDescEstado())) {
						nmapp.getController()
								.getInboxHandler()
								.sendEmptyMessage(
										ControllerProtocol.DELETE_DATA_FROM_LOCALHOST);
					} else {
						Toast.makeText(getApplicationContext(), String.format("Los recibos con estado '%s'.\n No se pueden eliminar.", recibo_selected.getDescEstado()), Toast.LENGTH_SHORT).show();
						return;
					}
					break;				
				}

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
				ActivityCompat.invalidateOptionsMenu(ViewRecibo.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewRecibo.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		nmapp = (NMApp) this.getApplicationContext();
		try {
			nmapp.getController().removebridgeByName(BReciboM.class.toString());
			nmapp.getController().setEntities(this, new BReciboM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);

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
		firstFragment = new ListaFragment<vmRecibo>();

		// In case this activity was started with special instructions from
		// an Intent,
		// pass the Intent's extras to the fragment as arguments
		firstFragment.setArguments(getIntent().getExtras());

		// Add the fragment to the 'fragment_container' FrameLayout
		if (findViewById(R.id.fragment_container) != null) {			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
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
			
			Object obj = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
			
			if ( !(obj instanceof ViewReciboEdit) ){
				customArrayAdapter = (CustomArrayAdapter<vmRecibo>) ((Filterable) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container)).getAdapter();
				//customArrayAdapter = firstFragment.getAdapter();
			}
			

		} else {
			customArrayAdapter = (CustomArrayAdapter<vmRecibo>) ((Filterable) getSupportFragmentManager()
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

	@SuppressLint("CutPasteId")
	private void initComponent() {
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		gridheader.setText("Listado de Recibos (0) ");
		footerView = (TextView) findViewById(R.id.ctxtview_enty);
		footerView.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case C_DATA:
			establecer(msg);
			return true;
		case DELETE_ITEM_FINISHED:			
			buildCustomDialog("Nordis Movile",msg.obj.equals(1)?"Eliminado Satisfactoriamente":"Error al eliminar",ALERT_DIALOG).show();
			customArrayAdapter.remove(recibo_selected);			
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
			setData((ArrayList<vmRecibo>) ((msg.obj == null) ? new ArrayList<vmRecibo>()
					: msg.obj), C_SETTING_DATA);
			return true;
		case ERROR:

			return true;

		}
		return false;
	}

	private void setData(final ArrayList<vmRecibo> data, final int what) {
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
								gridheader.setText("Listado de Recibos ("
										+ customArrayAdapter.getCount() + ")");
								footerView.setVisibility(View.VISIBLE);
							} else {
								if (what == C_SETTING_DATA)
									footerView.setVisibility(View.VISIBLE);
								gridheader.setText("Listado de Recibos ("
										+ data.size() + ")");
								firstFragment.setItems(data);
								customArrayAdapter.setSelectedPosition(0);
								positioncache = 0;
								recibo_selected = customArrayAdapter.getItem(0);
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

	@SuppressWarnings("unchecked")
	private void establecer(Message msg) {
		recibos = (List<vmRecibo>) ((msg.obj == null) ? new ArrayList<vmRecibo>()
				: msg.obj);
		gridheader.setVisibility(View.VISIBLE);
		gridheader.setText(String.format("Listado de Recibos (%s)",
				recibos.size()));
		if (recibos.size() == 0) {
			TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
			txtenty.setVisibility(View.VISIBLE);
		}
		firstFragment.setItems(recibos);		
		firstFragment.getAdapter().setSelectedPosition(0);		
		positioncache = 0;
		if(recibos.size() > 0)
			recibo_selected = firstFragment.getAdapter().getItem(0); //customArrayAdapter.getItem(0);
	}

	@Override
	public void onItemSelected(Object obj, int position) {

		FichaReciboFragment reciboFragment;
		Bundle args = new Bundle();
		args.putInt(FichaProductoFragment.ARG_POSITION, position);
		args.putParcelable(FichaReciboFragment.OBJECT, (vmRecibo) obj);

		

		if (findViewById(R.id.dynamic_fragment) != null) {

			reciboFragment = (FichaReciboFragment) getSupportFragmentManager()
					.findFragmentById(R.id.dynamic_fragment);
			if (reciboFragment != null) {
				reciboFragment.updateArticleView((vmRecibo) obj, position);
			} else {
				reciboFragment = new FichaReciboFragment();
				reciboFragment.setArguments(args);
				transaction.add(R.id.dynamic_fragment, reciboFragment);
				transaction.addToBackStack(null);
			}

		} else {

			@SuppressWarnings("unused")
			Fragment fragment = getSupportFragmentManager().findFragmentById(
					R.id.fragment_container);

			gridheader.setVisibility(View.INVISIBLE);

			if (fragment instanceof ListaFragment) {
				reciboFragment = new FichaReciboFragment();
				reciboFragment.setArguments(args);
				transaction.replace(R.id.fragment_container, reciboFragment);
				transaction.addToBackStack(null);
			}
		}
		// Commit the transaction transaction.commit();
		transaction.commit();

	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent e){
		super.onKeyUp(keyCode, e);
		if(keyCode == KeyEvent.KEYCODE_BACK){
			FINISH_ACTIVITY();
		}
		return true;		
	}
	
	private void FINISH_ACTIVITY()	{
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().removebridge(nmapp.getController().getBridge());			
		Log.d(TAG, "Activity quitting"); 
		finish();
	}

	public vmRecibo getReciboSelected() {
		return recibo_selected;
	}	
	
	public Dialog buildCustomDialog(String tittle, String msg, int type) {
		return new CustomDialog(this.getApplicationContext(), tittle, msg,
				false, type);
	}

}
