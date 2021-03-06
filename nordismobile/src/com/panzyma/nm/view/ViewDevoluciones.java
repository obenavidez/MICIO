package com.panzyma.nm.view;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BDevolucionM;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CuentasPorCobrarFragment;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.logic.PojoDevolucion;
import com.panzyma.nm.model.ModelDevolucion;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.view.ViewRecibo.FragmentActive;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.viewmodel.vmDevolucion;
import com.panzyma.nordismobile.R;

import static com.panzyma.nm.controller.ControllerProtocol.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("rawtypes")
@InvokeBridge(bridgeName = "BDevolucionM")
public class ViewDevoluciones extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {

	// VARIABLES
	public enum FragmentActive {
		LIST, EDIT, FICHACLIENTE, CONSULTAR_CUENTA_COBRAR
	};

	public static final String SERIALIZE_DEVOLUCION = "devolucion";

	private static final int NUEVO_DEVOLUCION = 0;
	private static final int ABRIR_DEVOLUCION = 1;
	private static final int BORRAR_DEVOLUCION = 2;
	private static final int ENVIAR_DEVOLUCION = 3;
	private static final int IMPRIMIR_COMPROBANTE = 4;
	private static final int BORRAR_ENVIADAS = 5;
	private static final int FICHA_DEL_CLIENTE = 6;
	private static final int CUENTAS_POR_COBRAR = 7;
	private static final int CERRAR = 8;
	String[] opcionesMenu;
	CharSequence tituloSeccion;
	CharSequence tituloApp;
	ViewDevoluciones vd;
	private FragmentActive fragmentActive = null;

	// Controles
	DrawerLayout drawerLayout;
	ListView drawerList;
	TextView gridheader;
	TextView footerView;
	ListaFragment<vmDevolucion> firstFragment;
	ActionBarDrawerToggle drawerToggle;
	SearchView searchView;
	CustomArrayAdapter customArrayAdapter;
	vmDevolucion item_selected = null;
	ProgressDialog pDialog;
	List<vmDevolucion> devoluciones = new ArrayList<vmDevolucion>();
	int posicion;
	private static CustomDialog dlg;

	Intent intent;
	Bundle b;
	protected int positioncache;

	Devolucion devolucion;
	private int request_code;
	CuentasPorCobrarFragment cuentasPorCobrar;
	private static final int MOSTRAR_FACTURAS = 0;
	private static final int MOSTRAR_NOTAS_DEBITO = 1;
	private static final int MOSTRAR_NOTAS_CREDITO = 2;
	private static final int MOSTRAR_PEDIDOS = 3;
	private static final int MOSTRAR_RECIBOS = 4;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Establecemos nuestras variables de entorno
		NMApp.getController().setView(this);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		setContentView(R.layout.layout_client_fragment);
		vd = this;
		CreateMenu();

		Load_Data(LOAD_DATA_FROM_LOCALHOST);

		fragmentActive = FragmentActive.LIST;

		firstFragment = new ListaFragment<vmDevolucion>(true);

		// Si es Phone
		if (findViewById(R.id.fragment_container) != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, firstFragment).commit();
		}

		gridheader = footerView = (TextView) findViewById(R.id.ctextv_gridheader);

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		boolean result = false;
		ArrayList<vmDevolucion> list = null;

		HideDialogos();

		switch (msg.what) {

		case C_DATA:
			list = (ArrayList<vmDevolucion>) ((msg.obj == null) ? new ArrayList<vmDevolucion>()
					: msg.obj);
			SetList(list);
			if (msg.arg1 == msg.arg2 && msg.arg2 == -1) {
				AppDialog.showMessage((ViewDevoluciones) NMApp.getController()
						.getView(), "Informaci�n.",
						"Registro(s) borrado(s) exitosamente!",
						DialogType.DIALOGO_ALERTA);
			}
			break;

		case ERROR:
			AppDialog.showMessage((ViewDevoluciones) NMApp.getController()
					.getView(), ((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			final ErrorMessage error = ((ErrorMessage) msg.obj);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// pDialog.hide();
					dlg = new CustomDialog(vd, error.getMessage()
							+ error.getCause(), false, NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
			break;
		case AFTERGETOBSERVACIONDEV:
 			if (dlg != null)
				dlg.dismiss();
			
			if (msg.arg1 == ControllerProtocol.IMPRIMIR) {
				enviarImprimirDevolucion(msg.obj.toString(), devolucion);
			} else if (msg.arg1 == ControllerProtocol.ENVIARDEVOLUCION) {
				enviarImprimirDevolucion(msg.obj.toString(), devolucion,
						ControllerProtocol.ENVIARDEVOLUCION);
			}
			break;
		case ControllerProtocol.ID_REQUEST_ENVIAR:
			if (dlg != null)
				dlg.dismiss();
			if (msg.arg1 == ControllerProtocol.IMPRIMIR)
				enviarImprimirDevolucion(msg.obj.toString(), devolucion);
			else {
				enviarImprimirDevolucion(msg.obj.toString(), devolucion,
						msg.arg1);
			}
			break;
		case NOTIFICATION_DIALOG2:
			if(msg.obj instanceof ErrorMessage){
				showStatus(((ErrorMessage)msg.obj).getMessage() ,true);  
			}
			else {
				showStatus(msg.obj.toString()/*, true*/);
			}
			break;
		case UPDATOBJECT :
			// Si msg.arg1==1 entonces significa que cambia el estado del registro
			if(msg.obj instanceof Devolucion && msg.arg1==1 ){
				UpdateRegistro((Devolucion)msg.obj);
			}
			break;
		}
		return result;
	}

	@Override
	public void onItemSelected(Object obj, int position) {
		item_selected = (vmDevolucion) obj;
		if ((devolucion == null)
				|| (devolucion != null && devolucion.getReferencia() != item_selected.getReferencia()))
			devolucion = ModelDevolucion.getDevolucionbyID(item_selected.getId());
		posicion = position;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.mcliente, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		if (searchItem != null) {
			searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
			if (fragmentActive == FragmentActive.LIST) {
				customArrayAdapter = ((Filterable) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container))
						.getAdapter();

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
		}

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.action_search).setVisible(true);
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	private void CreateMenu() {
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(
				R.array.devoluciones_lista_options);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		// A�adimos Funciones al men� laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				drawerList.setItemChecked(position, true);
				drawerLayout.closeDrawers();
				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);

				// SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
				positioncache = customArrayAdapter.getSelectedPosition();
				// int pos = customArrayAdapter.getSelectedPosition();
				// OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
				item_selected = (vmDevolucion) customArrayAdapter
						.getItem(positioncache);
				if(fragmentActive== FragmentActive.LIST){
				
					switch (position) 
					{
						case NUEVO_DEVOLUCION:
							intent = new Intent(ViewDevoluciones.this,
									ViewDevolucionEdit.class);
							intent.putExtra("requestcode", NUEVO_DEVOLUCION);
							startActivityForResult(intent, NUEVO_DEVOLUCION);
							break;
						case ABRIR_DEVOLUCION:
							abrirDevolucion();
		
							break;
						case BORRAR_DEVOLUCION:
							if (item_selected == null) {
								drawerLayout.closeDrawers();
								AppDialog.showMessage(vd, "Informaci�n",
										"Seleccione un registro.",
										DialogType.DIALOGO_ALERTA);
								return;
							} 
							if (!item_selected.getEstado().equals("REGISTRADA")) {
								drawerLayout.closeDrawers();
								AppDialog.showMessage(vd, "Informaci�n",
										"El registro no se puede borrar en estado "+ item_selected.getItemEstado()+" .",
										DialogType.DIALOGO_ALERTA);
								return;
							}
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putInt("id", (int) item_selected.getId());
							msg.setData(b);
							msg.what = ControllerProtocol.DELETE_DATA_FROM_LOCALHOST;
							NMApp.getController().getInboxHandler().sendMessage(msg);
							break;
						case ENVIAR_DEVOLUCION:
							if (item_selected == null) {
								drawerLayout.closeDrawers();
								AppDialog.showMessage(vd, "Informaci�n",
										"Seleccione un registro.",
										DialogType.DIALOGO_ALERTA);
								return;
							}
							
							enviarDevolucion(ControllerProtocol.GETOBSERVACIONDEV);
							//BDevolucionM.beforeSend(item_selected.getId());
							break;
						case IMPRIMIR_COMPROBANTE:
							if (item_selected == null) {
								drawerLayout.closeDrawers();
								AppDialog.showMessage(vd, "Informaci�n",
										"Seleccione un registro.",
										DialogType.DIALOGO_ALERTA);
								return;
							}
							devolucion = ModelDevolucion.getDevolucionbyID(item_selected.getId());
							if (devolucion.getNumeroCentral() == 0)
								enviarDevolucion(ControllerProtocol.GETOBSERVACIONDEV);
							else {						
								enviarImprimirDevolucion(
										"Se mandara a imprimir el comprobante de la Devoluci�n",
										devolucion);
							}
								
							/*
							 * BDevolucionM.ImprimirDevolucion(item_selected.getId(),
							 * false);
							 */
							break;
						case BORRAR_ENVIADAS:
							if (item_selected == null) 
							{
								drawerLayout.closeDrawers();
								AppDialog.showMessage(vd, "Informaci�n",
										"Seleccione un registro.",
										DialogType.DIALOGO_ALERTA);
								return;
							}
		
							Message msg2 = new Message();
							Bundle b2 = new Bundle();
							b2.putInt("id", -1);
							msg2.setData(b2);
							msg2.what = ControllerProtocol.DELETE_DATA_FROM_LOCALHOST;
							NMApp.getController().getInboxHandler().sendMessage(msg2);
							break;
						case FICHA_DEL_CLIENTE:
							if (item_selected == null) {
								drawerLayout.closeDrawers();
								AppDialog.showMessage(vd, "Informaci�n",
										"Seleccione un registro.",
										DialogType.DIALOGO_ALERTA);
								return;
							}
		
							if (NMNetWork.isPhoneConnected(NMApp.getContext())
									&& NMNetWork.CheckConnection(NMApp.getController())) {
								fragmentActive = FragmentActive.FICHACLIENTE;
								ShowCustomerDetails();
							}
							break;
						case CUENTAS_POR_COBRAR:
							if (item_selected == null) {
								drawerLayout.closeDrawers();
								AppDialog.showMessage(vd, "Informaci�n",
										"Seleccione un registro.",
										DialogType.DIALOGO_ALERTA);
								return;
							}
							if (NMNetWork.isPhoneConnected(NMApp.getContext())
									&& NMNetWork.CheckConnection(NMApp.getController())) {
								fragmentActive = FragmentActive.CONSULTAR_CUENTA_COBRAR;
								LOAD_CUENTASXPAGAR();
							}
		
							break;
						case CERRAR:
							finish();
							break;
					}
				}
				if(fragmentActive == FragmentActive.CONSULTAR_CUENTA_COBRAR){
					
					switch (position) {
					case MOSTRAR_FACTURAS:
						cuentasPorCobrar.cargarFacturasCliente();
						break;
					case MOSTRAR_NOTAS_DEBITO: 
						cuentasPorCobrar.cargarNotasDebito(); 
					break;
					case MOSTRAR_NOTAS_CREDITO: 
						cuentasPorCobrar.cargarNotasCredito(); 
					break;
					case MOSTRAR_PEDIDOS: 
						cuentasPorCobrar.cargarPedidos();
					break;
					case MOSTRAR_RECIBOS: 
						cuentasPorCobrar.cargarRecibosColector(); 
					break;
					}
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
				ActivityCompat.invalidateOptionsMenu(ViewDevoluciones.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewDevoluciones.this);

			}
		};

		// establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	private void enviarDevolucion(int... arg) {
		//showStatus("Enviando devoluci�n....",true);  
		
		Message msg = new Message();
		msg.obj = devolucion;
		msg.what = arg.length != 0 ? arg[0]
				: ControllerProtocol.ENVIARDEVOLUCION;
		com.panzyma.nm.NMApp.getController().getInboxHandler().sendMessage(msg);
	} 
	
	private void Load_Data(int what) {
		try {

			NMApp.getController().getInboxHandler().sendEmptyMessage(what);
			pDialog = new ProgressDialog(this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Procesando...");
			pDialog.setCancelable(true);
			pDialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SetList(List<vmDevolucion> list) {
		devoluciones = list;
		firstFragment.setItems(devoluciones, true);
		gridheader.setText(String.format("LISTA DEVOLUCIONES (%s)",
				devoluciones.size()));
		/*if (list != null && list.size() > 0) {
			vmDevolucion dev = devoluciones.get(0);
			devolucion = ModelDevolucion.getDevolucionbyID(dev.getId());
		}*/
	}

	private void ShowCustomerDetails() {
		Bundle args = new Bundle();
		args.putInt(FichaClienteFragment.ARG_POSITION, this.posicion);
		args.putLong(FichaClienteFragment.ARG_SUCURSAL,
				item_selected.getIdSucursal());

		// establecemos el titulo
		getSupportActionBar().setTitle(R.string.FichaClienteDialogTitle);

		FichaClienteFragment ficha;
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		/*if (findViewById(R.id.dynamic_fragment) != null) {
		} else */
		{
			Fragment fragment = getSupportFragmentManager().findFragmentById(
					R.id.fragment_container);
			if (fragment instanceof ListaFragment) {
				ficha = new FichaClienteFragment();
				ficha.setArguments(args);
				ficha.setRetainInstance(true);
				transaction.addToBackStack(null);
				transaction.replace(R.id.fragment_container, ficha, "ficha");
				gridheader.setVisibility(View.INVISIBLE);
				setDrawerState(false);
			}
		}
		// Commit the transaction transaction.commit();
		transaction.commit();
	}

	public void LOAD_CUENTASXPAGAR() {
		// CuentasPorCobrarFragment cuentasPorCobrar = new
		// CuentasPorCobrarFragment();
		// Bundle msg = new Bundle();
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, this.posicion);
		// msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID,this.item_selected.getIdSucursal());
		// cuentasPorCobrar.setArguments(msg);
		// transaction.replace(R.id.fragment_container, cuentasPorCobrar);
		// transaction.addToBackStack(null);
		// transaction.commit();

//		CuentasPorCobrarFragment cuentasPorCobrar = CuentasPorCobrarFragment
//				.Instancia();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//		android.support.v4.app.Fragment prev = getSupportFragmentManager()
//				.findFragmentByTag("dialog");
//		if (prev != null) {
//			transaction.remove(prev);
//		}
//
//		Bundle msg = new Bundle();
//		msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, positioncache);
//		msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID,
//				item_selected.getIdSucursal());
//
//		transaction.addToBackStack(null);
//		cuentasPorCobrar.setArguments(msg);
//		cuentasPorCobrar.show(transaction, "dialog");
		
		
		fragmentActive = FragmentActive.CONSULTAR_CUENTA_COBRAR;
		if (findViewById(R.id.fragment_container) != null) 
		{	
			cuentasPorCobrar = new CuentasPorCobrarFragment();						
			Bundle msg = new Bundle();
			msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, positioncache);
			msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID, item_selected.getIdSucursal());
			cuentasPorCobrar.setArguments(msg);	
			transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container,cuentasPorCobrar);
			transaction.addToBackStack(null);
			transaction.commit();	
			EstablecerMenu(fragmentActive);
		}
		
	}

	@Override
	public void onBackPressed() {
		Fragment fragment = getSupportFragmentManager().findFragmentById(
				R.id.fragment_container);
		if (fragment instanceof FichaClienteFragment
				|| fragment instanceof CuentasPorCobrarFragment) {
			gridheader.setVisibility(View.VISIBLE);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			// transaction.detach(fragment);
			transaction.replace(R.id.fragment_container, firstFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			if(fragmentActive== FragmentActive.FICHACLIENTE){
				setDrawerState(true);
			}
			fragmentActive = FragmentActive.LIST;
			EstablecerMenu(fragmentActive);
			getSupportActionBar().show();
		} else
			FINISH_ACTIVITY();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Fragment fragment = getSupportFragmentManager().findFragmentById(
					R.id.fragment_container);
			if (fragment instanceof FichaClienteFragment) {
				return true;
			} else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		}
		if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
			return false;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void FINISH_ACTIVITY() {
		// ocultarDialogos();
		// Log.d(TAG, "Activity quitting");
		if (pDialog != null)
			pDialog.dismiss();
		NMApp.getThreadPool().stopRequestAllWorkers();
		finish();
	}

	public void HideDialogos() {
		if (dlg != null)
			dlg.dismiss();
		if (pDialog != null)
			pDialog.dismiss();
	}

	public void abrirDevolucion() {

		try {
			if (item_selected == null) {
				drawerLayout.closeDrawers();
				AppDialog.showMessage(this, "Informaci�n",
						"Seleccione un registro.", DialogType.DIALOGO_ALERTA);
				return;
			}

			positioncache = customArrayAdapter.getSelectedPosition();
			intent = new Intent(ViewDevoluciones.this, ViewDevolucionEdit.class);
			b = new Bundle();
			b.putLong("iddevolucion", item_selected.getId());
			intent.putExtras(b);
			intent.putExtra("requestcode", ABRIR_DEVOLUCION);
			startActivityForResult(intent, ABRIR_DEVOLUCION);

		} catch (Exception e) {
			e.printStackTrace();
			AppDialog.showMessage(this, "Informaci�n", e.getMessage(),
					DialogType.DIALOGO_ALERTA);
		}

	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		try {
			SessionManager.setContext(this);
			UserSessionManager.setContext(this);
			com.panzyma.nm.NMApp.getController().setView(this);
			request_code = requestcode;
			if ((NUEVO_DEVOLUCION == request_code || ABRIR_DEVOLUCION == request_code)
					&& data != null) {
				Bundle bundle = data.getExtras();
				bundle.setClassLoader(com.panzyma.nm.serviceproxy.Devolucion.class
						.getClassLoader());
				PojoDevolucion dev = (PojoDevolucion) bundle
						.getSerializable(SERIALIZE_DEVOLUCION);
				if (!(dev.getClienteId() == 0L 
						|| dev.getSucursalId() == 0L
						|| dev.getNombreCliente() == null 
						|| dev.getTotal() == 0.0f)) {
					establecer(dev, false);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (drawerLayout != null && drawerLayout.isShown())
			drawerLayout.closeDrawers();
		finishActivity(request_code);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		super.startActivityFromFragment(fragment, intent, requestCode);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private void establecer(final Object _obj, final boolean thread,
			final int... what) {
		if (_obj == null)
			return;
		if (_obj instanceof Message) {
			Message msg = (Message) _obj;
			devoluciones = ((ArrayList<vmDevolucion>) ((msg.obj == null) ? new ArrayList<vmDevolucion>()
					: msg.obj));
			positioncache = 0;
		} else if (_obj instanceof PojoDevolucion) {
			PojoDevolucion d = (PojoDevolucion) _obj;
			vmDevolucion objDev = new vmDevolucion(d.getId(),
					d.getReferencia(), d.getFecha(), d.getNombreCliente(),
					Float.valueOf(d.getTotal()), d.getCodigoEstado(),
					d.getClienteId(), d.isOffLine(), d.getSucursalId(),
					d.getReferencia());
			if (ABRIR_DEVOLUCION == request_code
					|| (what != null && what.length != 0 && ControllerProtocol.ID_REQUEST_UPDATEITEM_FROMSERVER == what[0])) {
				devoluciones.set(positioncache, objDev);

			} else if (NUEVO_DEVOLUCION == request_code) {
				devoluciones.add(objDev);
				positioncache = devoluciones.size() - 1;
				customArrayAdapter.setSelectedPosition(positioncache);
			}
			onItemSelected(objDev, positioncache);
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SetList(devoluciones);
			}
		});

	}

	private void enviarImprimirDevolucion(final String mensaje,
			final Devolucion devolucion, final int... what) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				AppDialog.showMessage(vd, "", "" + mensaje,
						AppDialog.DialogType.DIALOGO_CONFIRMACION,
						new AppDialog.OnButtonClickListener() {
							@Override
							public void onButtonClick(AlertDialog _dialog,
									int actionId) {
								if (actionId == AppDialog.OK_BUTTOM) {
									try {
										_dialog.dismiss();
										Message msg = new Message();
										Bundle b = new Bundle();
										msg.obj = devolucion;
										msg.obj = devolucion;
										msg.what = what.length != 0 ? what[0]
												: ControllerProtocol.IMPRIMIR;
										NMApp.getController().getInboxHandler()
												.sendMessage(msg);
									} catch (Exception e) {
										NMApp.getController()
												.notifyOutboxHandlers(
														ControllerProtocol.ERROR,
														0,
														0,
														new ErrorMessage(
																"Error al intentar imprimir el comprobante de la4 devoluci�n",
																e.getMessage(),
																e.getMessage()));
									}
								}
							}
						});
			}
		});

	}

	
	public void showStatus(final String mensaje, boolean... confirmacion) {		 
		if (confirmacion.length != 0 && confirmacion[0]) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(vd, "", mensaje,
							AppDialog.DialogType.DIALOGO_ALERTA,
							new AppDialog.OnButtonClickListener() {
								@Override
								public void onButtonClick(
										AlertDialog _dialog, int actionId) 
								{
									if (AppDialog.OK_BUTTOM == actionId) 
									{
										_dialog.dismiss();
									}
								}
							});
				}
			});
		} else 
		{
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() {
					dlg =  new CustomDialog(vd, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		} 
	}
	
	public void EstablecerMenu(FragmentActive fragmentActive){
		if(fragmentActive == FragmentActive.LIST)
			opcionesMenu = getResources().getStringArray(R.array.devoluciones_lista_options);
		
		if(fragmentActive == FragmentActive.CONSULTAR_CUENTA_COBRAR)
			opcionesMenu = new String[] { "Mostrar Facturas", "Mostrar Notas D�bito", "Mostrar Cr�dito", "Mostrar Pedido" ,"Mostrar Recibos" };
		
		
		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), android.R.layout.simple_list_item_1,opcionesMenu));
	}
	
	public void setDrawerState(boolean isEnabled) {
	    if ( isEnabled ) {
	        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	        drawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
	        drawerToggle.setDrawerIndicatorEnabled(true);
	        drawerToggle.syncState();

	    }
	    else {
	    	drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	        drawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	        drawerToggle.setDrawerIndicatorEnabled(false);
	        drawerToggle.syncState();
	    }
	}
	

	private void UpdateRegistro(final Devolucion dev){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < devoluciones.size() ; i++ ){
					if(devoluciones.get(i).getId() == dev.getId()){
						devoluciones.get(i).setEstado(dev.getCodEstado());
						firstFragment.setItems(devoluciones, true);
						firstFragment.getAdapter().notifyDataSetChanged();
					}
				}
			}
		});
	}
}