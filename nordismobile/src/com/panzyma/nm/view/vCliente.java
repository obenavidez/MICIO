package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.KeyEvent;
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
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.auxiliar.CustomDialog.OnDismissDialogListener;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CuentasPorCobrarFragment;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.view.ViewRecibo.FragmentActive;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nordismobile.R;

@InvokeBridge(bridgeName = "BClienteM")
public class vCliente extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {

	// VARIABLES
	CustomArrayAdapter customArrayAdapter;
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
	vCliente vc;
	TextView gridheader;
	TextView footerView;
	ListaFragment<vmCliente> firstFragment;
	List<vmCliente> clientes = new ArrayList<vmCliente>();
	vmCliente cliente_selected;
	private static CustomDialog dlg;
	CuentasPorCobrarFragment cuentasPorCobrar;
	public enum FragmentActive {
		LIST, FICHACLIENTE, CONSULTAR_CUENTA_COBRAR
	};

	private FragmentActive fragmentActive = null;
	// Menu Variables
	int listFragmentId;
	int positioncache = 0;
	long idsucursal;
	private Cliente cliente;
	static final String TAG = vCliente.class.getSimpleName();
	private static final int NUEVO_PEDIDO = 0;
	private static final int NUEVO_RECIBO = 1;
	private static final int NUEVO_DEVOLUCION = 2;
	private static final int FICHA_CLIENTE = 3;
	private static final int CONSULTAR_CUENTA_COBRAR = 4;
	private static final int SINCRONIZAR_ITEM = 5;
	private static final int SINCRONIZAR_TODOS = 6;
	private static final int CERRAR = 7;
	// RECIBO
	public static final String PEDIDO_ID = "pedido_id";
	public static final String RECIBO_ID = "recibo_id";
	public static final String CLIENTE = "cliente";
	
	private static final int MOSTRAR_FACTURAS = 0;
	private static final int MOSTRAR_NOTAS_DEBITO = 1;
	private static final int MOSTRAR_NOTAS_CREDITO = 2;
	private static final int MOSTRAR_PEDIDOS = 3;
	private static final int MOSTRAR_RECIBOS = 4;	

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		setContentView(R.layout.layout_client_fragment);
		NMApp.getController().setView(this);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		fragmentActive = FragmentActive.LIST;
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		footerView = (TextView) findViewById(R.id.ctextv_gridheader);
		vc = this;
		CreateMenu();
		
		/*
		try {
			clientes = (savedInstanceState != null) ? clientes = savedInstanceState
					.getParcelableArrayList("vmCliente") : null;
			if (clientes == null)
				Load_Data(LOAD_DATA_FROM_LOCALHOST);
			else {
				SetList(clientes);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		fragmentActive = FragmentActive.LIST;		

		// Create an instance of ExampleFragment
		firstFragment = new ListaFragment<vmCliente>();
//		firstFragment.setRetainInstance(true);
		// In case this activity was started with special instructions from
		// an Intent,
		// pass the Intent's extras to the fragment as arguments
		firstFragment.setArguments(getIntent().getExtras());
		
			Load_Data(LOAD_DATA_FROM_LOCALHOST);
		// if device is a mobile
		if (findViewById(R.id.fragment_container) != null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.fragment_container, firstFragment, "lista")
//					.addToBackStack("list").commit();
			// Display the fragment as the main content.
			getSupportFragmentManager().beginTransaction()
	                .replace(R.id.fragment_container, firstFragment)
	                .commit();
			
		} else {

		}
	}
 
	public void ocultarDialogos() {
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
	}

	public void showStatus(final String mensaje, boolean... confirmacion) {

		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(vc, "", mensaje,
							AppDialog.DialogType.DIALOGO_ALERTA,
							new AppDialog.OnButtonClickListener() {
								@Override
								public void onButtonClick(AlertDialog _dialog,
										int actionId) {

									if (AppDialog.OK_BUTTOM == actionId) {
										_dialog.dismiss();
									}
								}
							});
				}
			});
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dlg = new CustomDialog(vc, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		/*
		 * if(savedInstanceState != null) {
		 */
		Parcelable[] objects = new Parcelable[clientes.size()];
		clientes.toArray(objects);
		savedInstanceState.putParcelableArray("vmCliente", objects);
		savedInstanceState.putInt("positioncache", positioncache);
		savedInstanceState.putParcelable("fragment", firstFragment);
		/*
		 * clientes=new ArrayList<vmCliente>();
		 * clientes=customArrayAdapter.getItems();
		 * bundle.putParcelableArrayList("vmCliente",(ArrayList<? extends
		 * Parcelable>) clientes);
		 */
		// }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  Parcelable [] objects = savedInstanceState.getParcelableArray("vmCliente");
	  clientes = new ArrayList<vmCliente>( (Collection<? extends vmCliente>) Arrays.asList(objects) ); 
	  positioncache = savedInstanceState.getInt("positioncache");	  
	  firstFragment = (ListaFragment<vmCliente>) savedInstanceState.getParcelable("fragment");
	  gridheader.setText(String.format("LISTA CLIENTES (%s)", clientes.size()));
	  //setList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		boolean result = false;
		ArrayList<vmCliente> list = null;
		ocultarDialogos();
		switch (msg.what) {
		case ControllerProtocol.NOTIFICATION:
			if(msg.obj instanceof ErrorMessage){
				showStatus(((ErrorMessage)msg.obj).getMessage() ,true);  
			}
			else {
				showStatus(msg.obj.toString(), true);
			}
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			if(msg.obj instanceof ErrorMessage){
				showStatus(((ErrorMessage)msg.obj).getMessage() ,true);  
			}
			else {
				showStatus(msg.obj.toString(), true);
			}
			break;

		case C_DATA:
			list = (ArrayList<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>()
					: msg.obj);
			SetList(list);
			result = true;
			break;
		case C_SETTING_DATA:
			list = (ArrayList<vmCliente>) ((msg.obj == null) ? new ArrayList<vmCliente>()
					: msg.obj);
			SetData(list, C_SETTING_DATA);
			result = true;
			break;
		case C_UPDATE_ITEM_FINISHED:
			showStatus(msg.obj.toString(), true);
			result = true;
			break;
		case C_UPDATE_FINISHED:
			final String finishMessage = msg.obj.toString();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					AppDialog.showMessage(vc, "", finishMessage,
							AppDialog.DialogType.DIALOGO_ALERTA,
							new AppDialog.OnButtonClickListener() {
								@Override
								public void onButtonClick(AlertDialog _dialog,
										int actionId) {

									if (AppDialog.OK_BUTTOM == actionId) {
										Load_Data(LOAD_DATA_FROM_LOCALHOST);
										_dialog.dismiss();
									}
								}
							});

				}
			});
			result = true;
			break;
		case C_UPDATE_IN_PROGRESS:
			showStatus(msg.obj.toString());
			result = true;
			break;
		case ERROR:
			AppDialog.showMessage(vc, ((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			final ErrorMessage error = ((ErrorMessage) msg.obj);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// pDialog.hide();
					dlg = new CustomDialog(vc, error.getMessage()
							+ error.getCause(), false, NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
			result = true;
			break;
		case ControllerProtocol.UPDATE_LISTVIEW_HEADER:
			updateListViewHeader();
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
	protected void onResume() 
	{
		ocultarDialogos();
		if(NMApp.ciclo==NMApp.lifecycle.ONPAUSE || NMApp.ciclo==NMApp.lifecycle.ONRESTART)
		{
			NMApp.getController().setView(this);
			SessionManager.setContext(this);
			UserSessionManager.setContext(this);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		NMApp.ciclo=NMApp.lifecycle.ONPAUSE;
		ocultarDialogos();
		if(customArrayAdapter!=null)
			customArrayAdapter.notifyDataSetChanged();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		NMApp.ciclo=NMApp.lifecycle.ONSTOP;
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		NMApp.ciclo=NMApp.lifecycle.ONRESTART;
		super.onRestart();
	}
	

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.mcliente, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		if (searchItem != null) {
			searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
			//if (fragmentActive == FragmentActive.LIST) {
				
				
														
					searchView.setOnQueryTextListener(new OnQueryTextListener() {
						@Override
						public boolean onQueryTextChange(String s) {
							
							if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof Filterable){
							customArrayAdapter = ((Filterable) getSupportFragmentManager()
									.findFragmentById(R.id.fragment_container))
									.getAdapter();
							
							customArrayAdapter.getFilter().filter(s);
							}
							
							return false;
						}

						@Override
						public boolean onQueryTextSubmit(String s) {
							customArrayAdapter.getFilter().filter(s);
							return false;
						}
					});
				}
				
			
		//}

		return true;
	}

	public void updateListViewHeader()
	{ 
		runOnUiThread(new Runnable() 
		{				
			@Override
			public void run() { 
				if(firstFragment!=null && gridheader!=null)
					gridheader.setText("LISTA CLIENTES("+firstFragment.getAdapter().getCount()+")");
				
			}
		});		 
		
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
			item.getItemId();
			return true;
		}
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		
//		drawerToggle.onConfigurationChanged(newConfig);
//		SetList(clientes);
//	}

	private void CreateMenu() {
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(R.array.customeroptions);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		// A�adimos Funciones al men� laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				drawerList.setItemChecked(position, true);
				drawerLayout.closeDrawers();
				tituloSeccion = opcionesMenu[position];
				// Ponemos el titulo del Men�
				getSupportActionBar().setTitle(tituloSeccion);
				// SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
				int pos = customArrayAdapter.getSelectedPosition();
				// OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
				cliente_selected = (vmCliente) customArrayAdapter.getItem(pos);
				if(fragmentActive == FragmentActive.LIST) {
					switch (position) {
					case NUEVO_PEDIDO:
						if (cliente_selected == null) {
							drawerLayout.closeDrawers();
							AppDialog.showMessage(vc, "Informaci�n",
									"Seleccione un registro.",
									DialogType.DIALOGO_ALERTA);
							return;
						}
						intent = new Intent(vCliente.this, ViewPedidoEdit.class);
						intent.putExtra(PEDIDO_ID, 0);
						intent.putExtra(CLIENTE, cliente_selected.IdSucursal);
						startActivity(intent);
						break;
					case NUEVO_RECIBO:
						if (cliente_selected == null) {
							AppDialog.showMessage(vc, "Informaci�n",
									"Seleccione un registro.",
									DialogType.DIALOGO_ALERTA);
							return;
						}
						intent = new Intent(vCliente.this, ViewReciboEdit.class);
						intent.putExtra(RECIBO_ID, 0);
						intent.putExtra(CLIENTE, cliente_selected.IdSucursal);
						startActivity(intent);
						break;
					case NUEVO_DEVOLUCION:					
						if (cliente_selected == null) {
							AppDialog.showMessage(vc, "Informaci�n",
									"Seleccione un registro.",
									DialogType.DIALOGO_ALERTA);
							return;
						}
						intent = new Intent(vCliente.this, ViewDevolucionEdit.class);
						intent.putExtra(CLIENTE, cliente_selected.IdSucursal);
						startActivity(intent);
						
						break;
					case FICHA_CLIENTE:
						if (cliente_selected == null) {
	
							AppDialog.showMessage(vc, "Informaci�n",
									"Seleccione un registro.",
									DialogType.DIALOGO_ALERTA);
							return;
						}
						if(NMNetWork.isPhoneConnected(NMApp.getContext()) && NMNetWork.CheckConnection(NMApp.getController()))
			            {
							setDrawerState(false);
							fragmentActive = FragmentActive.FICHACLIENTE;
							LOAD_FICHACLIENTE_FROMSERVER();
			            }
						
						break;
					case CONSULTAR_CUENTA_COBRAR:
						if (cliente_selected == null) {
							AppDialog.showMessage(vc, "Informaci�n",
									"Seleccione un registro.",
									DialogType.DIALOGO_ALERTA);
							return;
						}
						if(NMNetWork.isPhoneConnected(NMApp.getContext()) && NMNetWork.CheckConnection(NMApp.getController()))
			            {
							fragmentActive = FragmentActive.CONSULTAR_CUENTA_COBRAR;
							LOAD_CUENTASXPAGAR();
			            }
						
						break;
					case SINCRONIZAR_ITEM:
						if (cliente_selected == null) {
							AppDialog.showMessage(vc, "Informaci�n",
									"Seleccione un registro.",
									DialogType.DIALOGO_ALERTA);
							return;
						}
						if(NMNetWork.isPhoneConnected(NMApp.getContext()) && NMNetWork.CheckConnection(NMApp.getController()))
			            {
							UPDATE_SELECTEDITEM_FROMSERVER();
			            }
						
						break;
					case SINCRONIZAR_TODOS:
						if(NMNetWork.isPhoneConnected(NMApp.getContext()) && NMNetWork.CheckConnection(NMApp.getController()))
			            {
							Load_Data(LOAD_DATA_FROM_SERVER);
			            }
						
						break;
					case CERRAR:
						FINISH_ACTIVITY();
						break;
					}
				}
				if(fragmentActive == FragmentActive.CONSULTAR_CUENTA_COBRAR) {
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
				ActivityCompat.invalidateOptionsMenu(vCliente.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(vCliente.this);

			}
		};
		// establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	@SuppressWarnings("unchecked")
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
		NMApp.getThreadPool().stopRequestAllWorkers();
		ocultarDialogos();
		Log.d(TAG, "Activity quitting");
		if (pDialog != null)
			pDialog.dismiss();
		finish();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void SetList(List<vmCliente> list) {

		// clientes = (List<vmCliente>) ((msg.obj == null) ? new
		// ArrayList<vmCliente>(): msg.obj);

		clientes = list;
		gridheader.setText(String.format("LISTA CLIENTES (%s)",
				clientes.size()));

		if (clientes.size() == 0) {

			ShowEmptyMessage(true);
		} else {
			ShowEmptyMessage(false);
		}

		firstFragment.setItems(clientes);
	}

	private void ShowEmptyMessage(boolean show) {
		TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);

		if (show) {
			txtenty.setVisibility(View.VISIBLE);
		} else {
			txtenty.setVisibility(View.INVISIBLE);
		}
	}

	private void SetData(final ArrayList<vmCliente> data, final int what) {
		try {
			if (data.size() != 0) {

				NMApp.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									if (what == C_SETTING_DATA
											&& customArrayAdapter != null
											&& customArrayAdapter.getCount() >= 0) {
										customArrayAdapter
												.AddAllToListViewDataSource(data);
										firstFragment.setItems(data);
										gridheader
												.setText("LISTA CLIENTES("
														+ customArrayAdapter
																.getCount()
														+ ")");
										footerView.setVisibility(View.VISIBLE);
										firstFragment.getAdapter()
												.notifyDataSetChanged();
										ShowEmptyMessage(false);
									} else {
										if (what == C_SETTING_DATA)
											footerView
													.setVisibility(View.VISIBLE);

										gridheader
												.setText("LISTA CLIENTES("
														+ data.size() + ")");
										firstFragment.setItems(data);
										customArrayAdapter
												.setSelectedPosition(0);
										positioncache = 0;
										firstFragment.getAdapter()
												.notifyDataSetChanged();
										/*
										 * product_selected =
										 * customArrayAdapter.getItem(0);
										 */
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ShowCustomerDetails() {
		Bundle args = new Bundle();
		args.putInt(FichaClienteFragment.ARG_POSITION, positioncache);
		args.putLong(FichaClienteFragment.ARG_SUCURSAL, idsucursal);

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
				//transaction.remove(fragment);
				transaction.replace(R.id.fragment_container, ficha, "ficha");
				gridheader.setVisibility(View.INVISIBLE);
			}
		}
		// Commit the transaction transaction.commit();
		transaction.commit();
	}

	private void LOAD_FICHACLIENTE_FROMSERVER() {

		idsucursal = get_SucursalID();
		if (idsucursal != 0 && idsucursal != 1) {
			ShowCustomerDetails();
		} else {
			if (idsucursal == 1)
				buildCustomDialog(
						"No hay cliente que consultar",
						"Debe sincronizar con el servidor primero...\nDesea Sincronizar ahora?",
						CONFIRMATION_DIALOG).show();
			else
				buildCustomDialog("No hay cliente que consultar",
						"Seleccione cliente primero", ALERT_DIALOG).show();
		}

	}

	public long get_SucursalID() {
		if (positioncache != -1) {
			cliente_selected = (vmCliente) customArrayAdapter
					.getItem(positioncache);
			idsucursal = cliente_selected.getIdSucursal();
		} else {
			if (customArrayAdapter != null) {
				if (customArrayAdapter.getCount() != 0) {
					idsucursal = cliente_selected.getIdSucursal();
				}
			}
			// idsucursal= ()?((customArrayAdapter.getCount()!=0)?( (
			// (cliente_selected!=null)?cliente_selected.getIdSucursal():0 )
			// ):1):1;
		}
		return idsucursal;
	}

	public Dialog buildCustomDialog(String tittle, String msg, int type) {
		final CustomDialog dialog = new CustomDialog(this);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setMessageType(type);
		dialog.setTitulo(tittle);
		dialog.setMensaje(msg);
		dialog.setOnActionDialogButtonClickListener(new OnActionButtonClickListener() {
			@SuppressWarnings("static-access")
			@Override
			public void onButtonClick(View _dialog, int actionId) {
				if (actionId == CustomDialog.OK_BUTTOM && idsucursal == 1)
					NMApp.getController().getInboxHandler()
							.sendEmptyMessage(LOAD_DATA_FROM_SERVER);
				else if (actionId == CustomDialog.OK_BUTTOM)
					dialog.dismiss();
			}
		});
		dialog.setOnDismissDialogListener(new OnDismissDialogListener() {
			@Override
			public void onDismiss() {

			}
		});

		return dialog;
	}

	@SuppressLint("ShowToast")
	public Toast buildToastMessage(String msg, int duration) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
		toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
		return toast;
	}

	private void UPDATE_SELECTEDITEM_FROMSERVER() {
		Message ms = new Message();
		ms.what = UPDATE_ITEM_FROM_SERVER;
		ms.obj = get_SucursalID();

		NMApp.getController().getInboxHandler().sendMessage(ms);
		// Toast.makeText(this, "sincronizando cliente...",Toast.LENGTH_LONG);
		pDialog = new ProgressDialog(vCliente.this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("sincronizando cliente...");
		pDialog.setCancelable(true);
		pDialog.show();
	}

		@Override
	public void onBackPressed() {
		Fragment fragment = getSupportFragmentManager().findFragmentById(
				R.id.fragment_container);
		if (fragment instanceof FichaClienteFragment || fragment instanceof CuentasPorCobrarFragment) {
			gridheader.setVisibility(View.VISIBLE);
			if(fragmentActive== FragmentActive.FICHACLIENTE){
				setDrawerState(true);
			}
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, firstFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			
			//customArrayAdapter.getFilter().filter("");
			fragmentActive = FragmentActive.LIST;
			getSupportActionBar().show();
			EstablecerMenu(fragmentActive);
			//invalidateOptionsMenu();
			CreateMenu();
		} else {
			FINISH_ACTIVITY();
		}
	}

	public void LOAD_CUENTASXPAGAR() {
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		//CuentasPorCobrarFragment cuentasPorCobrar = CuentasPorCobrarFragment.Instancia();
		
//		android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
//		if (prev != null){
//			transaction.remove(prev);
//		}
//		
//		Bundle msg = new Bundle();
//		msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, positioncache);
//		msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID, cliente_selected.getIdSucursal());
//		
//		
//		transaction.addToBackStack(null);
//		cuentasPorCobrar.setArguments(msg); 
//	    cuentasPorCobrar.show(transaction, "dialog");
		
		/*
		CuentasPorCobrarFragment cuentasPorCobrar = new CuentasPorCobrarFragment();
		Bundle msg = new Bundle();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, positioncache);
		msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID,
				cliente_selected.getIdSucursal());
		cuentasPorCobrar.setArguments(msg);
		transaction.replace(R.id.fragment_container, cuentasPorCobrar);
		transaction.addToBackStack(null);
		transaction.commit();
		*/
		
		fragmentActive = FragmentActive.CONSULTAR_CUENTA_COBRAR;
		if (findViewById(R.id.fragment_container) != null) 
		{	
			cuentasPorCobrar = new CuentasPorCobrarFragment();						
			Bundle msg = new Bundle();
			msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, positioncache);
			msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID, cliente_selected.getIdSucursal());
			cuentasPorCobrar.setArguments(msg);	
			transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container,cuentasPorCobrar);
			transaction.addToBackStack(null);
			transaction.commit();	
			EstablecerMenu(fragmentActive);
			//CreateMenu();
		}
		
	}
	
	public void EstablecerMenu(FragmentActive fragmentActive){
		if(fragmentActive == FragmentActive.LIST)
			opcionesMenu = getResources().getStringArray(R.array.customeroptions);
		
		if(fragmentActive ==FragmentActive.CONSULTAR_CUENTA_COBRAR)
			opcionesMenu = new String[] { "Mostrar Facturas", "Mostrar Notas D�bito", "Mostrar Notas de Cr�dito", "Mostrar Pedido" ,"Mostrar Recibos" };

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
}