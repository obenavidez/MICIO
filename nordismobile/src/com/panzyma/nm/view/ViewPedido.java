package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG; 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Handler.Callback;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.telephony.TelephonyManager;
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
import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.auxiliar.AppDialog.OnButtonClickListener;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.ConsultaVentasFragment;
import com.panzyma.nm.fragments.CuentasPorCobrarFragment;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PedidoPromocion;
import com.panzyma.nm.serviceproxy.Promociones;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.ViewRecibo.FragmentActive;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.viewmodel.vmEntity;
import com.panzyma.nm.viewmodel.vmRecibo;
import com.panzyma.nordismobile.R;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings({ "rawtypes", "unchecked" })
@InvokeBridge(bridgeName = "BPedidoM")
public class ViewPedido extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) 
	{
		super.onActivityResult(requestcode, resultcode, data);
		try 
		{
			SessionManager.setContext(this);
			com.panzyma.nm.NMApp.getController().setView(this);
			request_code = requestcode;
			if ((NUEVO_PEDIDO == request_code || EDITAR_PEDIDO == request_code)
					&& data != null)
				establecer(data.getParcelableExtra("pedido"));

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

	@Override
	protected void onResume() {
		try {
			SessionManager.setContext(this);
			com.panzyma.nm.NMApp.getController().setView(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResume();

	}

	public enum FragmentActive {
		LIST, ITEM, CUENTAS_POR_COBRAR, CONSULTA_VENTAS
	};

	private FragmentActive fragmentActive = null;
	CuentasPorCobrarFragment cuentasPorCobrar;
	ConsultaVentasFragment consultasVentas;
	FragmentTransaction transaction;
	private static final String TAG = ViewPedido.class.getSimpleName();
	CustomArrayAdapter<vmEntity> customArrayAdapter;
	private SearchView searchView;
	int listFragmentId;
	public static int positioncache = -1;
	private Context context;
	private static final int NUEVO_PEDIDO = 0;
	private static final int EDITAR_PEDIDO = 1;
	private static final int ENVIAR_PEDIDO = 2;
	private static final int BORRAR_PEDIDO = 3;
	private static final int ANULAR_PEDIDO = 4;
	private static final int CUENTAS_POR_COBRAR = 5;
	protected static final int CONSULTA_VENTAS = 6;
	protected static final int CERRAR = 7;
	private static int request_code;
	private String[] opcionesMenu;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	Intent intent;
	Bundle b;
	private CharSequence tituloSeccion;
	private CharSequence tituloApp;
	TextView gridheader;
	TextView footerView;
	private static CustomDialog dlg;
	private List<vmEntity> pedidos = new ArrayList<vmEntity>();
	vmEntity pedido_selected = null;
	ListaFragment<vmEntity> firstFragment;
	private ViewPedido vp;
	private BPedidoM bpm;
	Handler handler;
	private Pedido pedido;
	private Cliente cliente;

	@SuppressLint("CutPasteId")
	private void initComponent() {
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		gridheader.setText("Listado de Pedidos (0) ");
		footerView = (TextView) findViewById(R.id.ctxtview_enty);
		footerView.setVisibility(View.VISIBLE);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SessionManager.setContext(this);
		context = getApplicationContext();

		setContentView(R.layout.layout_client_fragment);

		initComponent();

		fragmentActive = FragmentActive.LIST;

		vp = this;
		transaction = getSupportFragmentManager().beginTransaction();
		gridheader.setVisibility(View.VISIBLE);
		opcionesMenu = new String[] { "Nuevo Pedido", "Abrir Pedido",
				"Enviar Pedido", "Borrar Pedido", "Anular Pedido",
				"Consultar Cuentas X Cobrar", "Consultas de Ventas", "Cerrar" };
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {

				int pos = 0;
				String state = "";
				// SELECCIONAR LA POSICION DEL PEDIDO SELECCIONADO
				// ACTUALMENTE
				positioncache = customArrayAdapter.getSelectedPosition();
				// CERRAR EL MENU DEL DRAWER
				drawerLayout.closeDrawers();
				
				switch (position) {
				case NUEVO_PEDIDO: 
					intent = new Intent(ViewPedido.this, ViewPedidoEdit.class);
					intent.putExtra("requestcode", NUEVO_PEDIDO);
					startActivityForResult(intent, NUEVO_PEDIDO);// Activity is

					break;
				case EDITAR_PEDIDO:
					try 
					{
						if(pedido_selected!=null)
						{
							drawerLayout.closeDrawers();
							positioncache = customArrayAdapter.getSelectedPosition();
							Pedido p = null;
							p = Ventas.obtenerPedidoByID(pedido_selected.getId(), vp);
							intent = new Intent(ViewPedido.this,
									ViewPedidoEdit.class);
							b = new Bundle();
							b.putParcelable("pedido", p);
							intent.putExtras(b);
							intent.putExtra("requestcode", EDITAR_PEDIDO);
							startActivityForResult(intent, EDITAR_PEDIDO);// Activity
						}												// is

					} catch (Exception e) {
						e.printStackTrace();
						AppDialog.showMessage(vp, "Información",
								e.getMessage(), DialogType.DIALOGO_ALERTA);
					}
					// with requestCode 2
					break;

				case ENVIAR_PEDIDO: 
					enviarPedido();
					break;

				case BORRAR_PEDIDO:
					
					// OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
					pedido_selected = customArrayAdapter.getItem(positioncache);
					if (pedido_selected != null) 
					{
						// OBTENER EL ESTADO DEL REGISTRO
						state = pedido_selected.getDescEstado();
						if ("PORVALIDAR".equals(state)
								|| "APROBADO".equals(state)) {
							AppDialog
									.showMessage(
											vp,
											"Información",
											"No puede borrar pedidos por validar o aprobados.",
											DialogType.DIALOGO_ALERTA);
							return;
						}
						AllowRemove("Confirmación",
								"¿Está seguro que desea eliminar el pedido?",
								DialogType.DIALOGO_CONFIRMACION);
					} else {
						ShowNoRecords();
					}
					// OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
					if(customArrayAdapter.getCount()!=0)
						pedido_selected = customArrayAdapter.getItem(0);
					else 
						pedido_selected=null;
					
					break;

				case CUENTAS_POR_COBRAR: 					
					pedido_selected = customArrayAdapter.getItem(positioncache);
					if (pedido_selected != null) 
					{
						fragmentActive = FragmentActive.CUENTAS_POR_COBRAR;
						if (findViewById(R.id.fragment_container) != null) 
						{
							Pedido p1 = null;
							try {
								p1 = Ventas.obtenerPedidoByID(
										pedido_selected.getId(), vp);
							} catch (Exception e) {
								e.printStackTrace();
							}
							transaction = getSupportFragmentManager()
									.beginTransaction();
							cuentasPorCobrar = new CuentasPorCobrarFragment();
							Bundle msg = new Bundle();
							msg.putInt(CuentasPorCobrarFragment.ARG_POSITION,
									positioncache);
							msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID,
									p1.getObjSucursalID());
							cuentasPorCobrar.setArguments(msg);
							transaction.replace(R.id.fragment_container,
									cuentasPorCobrar);
							transaction.addToBackStack(null);
							transaction.commit();
						}
					}
					break;
				case CONSULTA_VENTAS:
					fragmentActive = FragmentActive.CONSULTA_VENTAS; 
					// OCULTAR LA BARRA DE ACCION
					pedido_selected = customArrayAdapter.getItem(positioncache);
					if (pedido_selected != null) 
					{
						getSupportActionBar().hide();
						if (findViewById(R.id.fragment_container) != null) 
						{
							transaction = getSupportFragmentManager()
									.beginTransaction();
							consultasVentas = new ConsultaVentasFragment(); 
							Bundle msg = new Bundle();
							msg.putInt(CuentasPorCobrarFragment.ARG_POSITION,
									positioncache);
							consultasVentas.setArguments(msg);
							transaction.replace(R.id.fragment_container,
									consultasVentas);
							transaction.addToBackStack(null);
							transaction.commit();
						}
					}
					break;
				case ANULAR_PEDIDO:
					// SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO
					// ACTUALMENTE
					pos = customArrayAdapter.getSelectedPosition();
					// OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
					pedido_selected = customArrayAdapter.getItem(pos);
					if (pedido_selected != null) {
						// OBTENER EL ESTADO DEL REGISTRO
						state = pedido_selected.getCodEstado();
						// VALIDAR QUE EL PEDIDO ESTÉ EN ESTADO NO ES APROBADO
						// if ("APROBADO".compareTo(state) != 0)
						// {
						// AppDialog.showMessage(vp,"Información","Solo se pueden anular pedidos en estado de APROBADO.",DialogType.DIALOGO_ALERTA);
						// //CERRAR EL MENU DEL DRAWER
						// drawerLayout.closeDrawers();
						// return;
						// }
						// SI SE ESTÁ FUERA DE LA COBERTURA
						if (!NMNetWork.isPhoneConnected(com.panzyma.nm.NMApp.getContext())
								&& !NMNetWork
										.CheckConnection(com.panzyma.nm.NMApp
												.getController())) {
							// Toast.makeText(getApplicationContext(),"La operación no puede ser realizada ya que está fuera de cobertura.",
							// Toast.LENGTH_SHORT).show();
							AppDialog
									.showMessage(
											vp,
											"Información",
											"La operación no puede ser realizada ya que está fuera de cobertura.",
											DialogType.DIALOGO_ALERTA);
							return;
						}
						try {
							// SOLICITAMOS QUE SE ANULE EL PEDIDO
							Message msg = new Message();
							msg.obj = pedido_selected.getId();
							msg.what = ControllerProtocol.ANULAR_PEDIDO;
							/*
							 * NMApp.controller.removeBridgeByName(ViewPedido.class
							 * .toString());
							 * com.panzyma.nm.NMApp.getController()
							 * .setEntities(this, bpm = new BPedidoM());
							 * com.panzyma
							 * .nm.NMApp.getController().addOutboxHandler(new
							 * Handler(vp));
							 */
							com.panzyma.nm.NMApp.getController()
									.getInboxHandler().sendMessage(msg);
							// CERRAR EL MENU DEL DRAWER
							drawerLayout.closeDrawers();
						} catch (Exception ex) {
							ex.printStackTrace();
							drawerLayout.closeDrawers();
						}
					} else {
						// CERRAR EL MENU DEL DRAWER
						drawerLayout.closeDrawers();
						ShowNoRecords();
					}
					break;
				case CERRAR:
					drawerLayout.closeDrawers();
					FINISH_ACTIVITY();
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
				ActivityCompat.invalidateOptionsMenu(ViewPedido.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewPedido.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);		
		
		getDeviceId(this);
		
		// Create an instance of ExampleFragment
		firstFragment = new ListaFragment<vmEntity>();
		firstFragment.setRetainInstance(true);
		// However, if we're being restored from a previous state,
		// then we don't need to do anything and should return or else
		// we could end up with overlapping fragments.
		if ( savedInstanceState != null ) {
			Parcelable[] objects = savedInstanceState.getParcelableArray("pedidos");	
			pedidos = new ArrayList<vmEntity>((Collection<? extends vmEntity>) Arrays.asList(objects));
			//recibos = vmRecibo.arrayParcelToArrayRecibo(objects);			
		} else {
			pedidos = null;
		} 
		
		if(pedidos == null){
			com.panzyma.nm.NMApp.getController().setView(this);
			com.panzyma.nm.NMApp.getController().getInboxHandler().sendEmptyMessage(ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);
		}
		
		if (savedInstanceState != null) {
			return;
		}	
		 
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

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  Parcelable [] objects = new Parcelable[pedidos.size()];
	  pedidos.toArray(objects);
	  savedInstanceState.putParcelableArray("pedidos", objects);
	  savedInstanceState.putInt("positioncache", positioncache);
	  savedInstanceState.putParcelable("fragment", firstFragment);
	   // etc.
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  Parcelable [] objects = savedInstanceState.getParcelableArray("pedidos");
	  pedidos = new ArrayList<vmEntity>( (Collection<? extends vmEntity>) Arrays.asList(objects) ); 
	  positioncache = savedInstanceState.getInt("positioncache");	  
	  firstFragment = (ListaFragment<vmEntity>) savedInstanceState.getParcelable("fragment");
	  gridheader.setText(String.format("Listado de Pedidos (%s)",pedidos.size()));
	  //setList();
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		if (fragmentActive == FragmentActive.LIST) {
			if (findViewById(R.id.fragment_container) != null) {
				customArrayAdapter = (CustomArrayAdapter<vmEntity>) ((Filterable) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container))
						.getAdapter();

			} else {
				customArrayAdapter = (CustomArrayAdapter<vmEntity>) ((Filterable) getSupportFragmentManager()
						.findFragmentById(R.id.item_client_fragment))
						.getAdapter();
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
		}
		return true;
	}
	
	public static String  getDeviceId(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    	
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
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

	public void ocultarDialogos() {
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

	public void showStatus(final String mensaje, boolean... confirmacion) {

		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(vp, "", mensaje,
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
					dlg = new CustomDialog(vp, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		Boolean val = false;
		final Object item = msg.obj;
		ocultarDialogos();
		switch (msg.what) 
		{
		case C_DATA:
			establecer(msg);
			val = true;
			break;
		case ControllerProtocol.NOTIFICATION:
			showStatus(msg.obj.toString(), true);
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			showStatus(msg.obj.toString());
			break;
		case ControllerProtocol.ID_REQUEST_ENVIARPEDIDO:
			request_code = EDITAR_PEDIDO;
			resultadoEnvioPedido(msg.obj);
			break;
		case DELETE_ITEM_FINISHED:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (Integer.parseInt(item.toString()) == 1) {
						Boolean removed = pedidos.remove(pedido_selected);
						if (removed) {
							firstFragment.getAdapter().notifyDataSetChanged();
							gridheader.setText(String.format(
									"Listado de Pedidos (%s)", pedidos.size()));
							customArrayAdapter.remove(pedido_selected);

							if (pedidos.size() > 0) {
								firstFragment.getAdapter().setSelectedPosition(
										0);
								pedido_selected = pedidos.get(0);
							}
							;
							AppDialog.showMessage(vp, "Exíto",
									"Se ha Elimando Correctamente el pedido.",
									DialogType.DIALOGO_ALERTA);
						}
					}
				}
			});
			val = true;
			break;
		case ERROR:
			ErrorMessage error = ((ErrorMessage) msg.obj);
			Toast.makeText(getApplicationContext(), error.getTittle(),
					Toast.LENGTH_SHORT).show();
			AppDialog.showMessage(vp, error.getTittle(), error.getMessage()
					+ error.getCause(), DialogType.DIALOGO_ALERTA,
					new OnButtonClickListener() {
						@Override
						public void onButtonClick(AlertDialog alert,
								int actionId) {
						}
					});
			val = true;
			break;
		case ControllerProtocol.ID_REQUEST_ANULARPEDIDO: 
			AppDialog.showMessage(vp, "Información",
					"El pedido ha sido anulado.", DialogType.DIALOGO_ALERTA);
			break;
		}
		return val;
	}

	public void resultadoEnvioPedido(Object obj) {
		Object pedd = ((ArrayList<Object>) obj).get(0);
		Object clte = ((ArrayList<Object>) obj).get(1);

		if (pedd != null)
			pedido = (Pedido) pedd;

		if (clte != null)
			cliente = (Cliente) clte;

		establecer(pedd);

		final String sms = (pedido.getCodEstado().compareTo("FACTURADO") == 0) ? "El pedido ha sido enviado y facturado \n¿Desea imprimir el recibo?"
				: "El pedido ha sido enviado.Estado: " + pedido.getDescEstado()
						+ "\r\nCausa: " + pedido.getDescCausaEstado();
		// Informar al usuario
		AppDialog.showMessage(this, "", sms,
				AppDialog.DialogType.DIALOGO_CONFIRMACION,
				new AppDialog.OnButtonClickListener() {
					@Override
					public void onButtonClick(AlertDialog _dialog, int actionId) {

						if (pedido.getCodEstado().compareTo("FACTURADO") == 0) {
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putParcelable("pedido", pedido);
							b.putParcelable("cliente", cliente);
							msg.setData(b);
							msg.what = ControllerProtocol.IMPRIMIR;
							com.panzyma.nm.NMApp.getController()
									.getInboxHandler().sendMessage(msg);

						}
						_dialog.dismiss();
					}
				});

	}

	@Override
	public void onItemSelected(Object obj, int position) {
		pedido_selected = firstFragment.getAdapter().getItem(position);
		positioncache = position;
	}

	private void establecer(Object _obj) {
		if (_obj == null)
			return;

		if (_obj instanceof Message) {
			Message msg = (Message) _obj;
			pedidos = (ArrayList<vmEntity>) ((msg.obj == null) ? new ArrayList<vmEntity>()
					: msg.obj);
			positioncache = 0;
		} else if (_obj instanceof Pedido) {
			Pedido p = (Pedido) _obj;
			if (EDITAR_PEDIDO == request_code) {
				pedidos.set(
						positioncache,
						new vmEntity(p.getId(), p.getNumeroMovil(), p
								.getFecha(), p.getTotal(),
								p.getNombreCliente(), p.getDescEstado(), p
										.getCodEstado()));

			} else if (NUEVO_PEDIDO == request_code) {
				pedidos.add(new vmEntity(p.getId(), p.getNumeroMovil(), p
						.getFecha(), p.getTotal(), p.getNombreCliente(), p
						.getDescEstado(), p.getCodEstado()));
				positioncache = pedidos.size() - 1;
			}
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gridheader.setVisibility(View.VISIBLE);
				gridheader.setText(String.format("Listado de Pedidos (%s)",
						pedidos.size()));
				if (pedidos.size() == 0) {
					TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
					txtenty.setVisibility(View.VISIBLE);
				} else {
					firstFragment.setItems(pedidos);
					firstFragment.getAdapter().setSelectedPosition(
							positioncache);
					pedido_selected = firstFragment.getAdapter().getItem(
							positioncache);
				}
			}
		});

	}

	public vmEntity getPedidoSelected() {
		return pedido_selected;
	}

	public BPedidoM getBridge() {
		return bpm;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			Fragment fragment = getSupportFragmentManager().findFragmentById(
					R.id.fragment_container);
			if (fragment instanceof CuentasPorCobrarFragment || fragment instanceof ConsultaVentasFragment) 
			{ 
				fragmentActive = FragmentActive.LIST;
				gridheader.setVisibility(View.VISIBLE);
				transaction = getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_container, firstFragment);				
				transaction.commit();
			} else
				FINISH_ACTIVITY();

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			switch (fragmentActive) {
			case CUENTAS_POR_COBRAR:
				cuentasPorCobrar.mostrarMenu();
				break;
			case CONSULTA_VENTAS:
				consultasVentas.mostrarMenu();
				break;
			default:
				drawerLayout.openDrawer(Gravity.LEFT);
				break;
			}

		}
		return super.onKeyUp(keyCode, event);
	}

	private void FINISH_ACTIVITY() 
	{
		NMApp.getController().setView((Callback) getParent());  
		Log.d(TAG, "Activity quitting");
		finish();
	}

	public void AllowRemove(final String title, final String msg,
			final DialogType type) {
		AppDialog.showMessage(vp, title, msg, type,
				new OnButtonClickListener() {
					@Override
					public void onButtonClick(AlertDialog _dialog, int actionId) {
						if (actionId == AppDialog.OK_BUTTOM) {
							Message ms = new Message();
							ms.what = ControllerProtocol.DELETE_DATA_FROM_LOCALHOST;
							ms.obj = pedido_selected.getId();
							com.panzyma.nm.NMApp.getController()
									.getInboxHandler().sendMessage(ms);
						}
					}
				});
	}

	private void ShowNoRecords() {
		if (pedidos.size() > 0 && pedido_selected != null) {
			AppDialog.showMessage(vp, "", "Seleccione un registro.",
					DialogType.DIALOGO_ALERTA);
		} else {
			AppDialog.showMessage(vp, "", "No existen pedidos registrados.",
					DialogType.DIALOGO_ALERTA);
		}
	}

	private void enviarPedido() {
		try {
			if (pedido_selected == null)
				return;
			pedido = Ventas.obtenerPedidoByID(pedido_selected.getId(), vp);
			//PORVALIDAR
			if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido.getCodEstado().compareTo("PORVALIDAR")==0) || (pedido
					.getCodEstado().compareTo("APROBADO") == 0)))
				return;

			if (pedido.getCodEstado().compareTo("REGISTRADO") == 0) {
				if (pedido.getNumeroCentral() > 0) {
					AppDialog.showMessage(this, "El pedido ya fue enviado.",
							DialogType.DIALOGO_ALERTA);
					return;
				}
			}
			// Si se está fuera de covertura, salir
			if (!SessionManager.isPhoneConnected())
				return;
			if (!isDataValid(pedido))
				return;

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putParcelable("pedido", pedido);
			msg.setData(b);
			msg.what = ControllerProtocol.SEND_DATA_FROM_SERVER;
			com.panzyma.nm.NMApp.getController().getInboxHandler().sendMessage(msg);

		} catch (Exception e) {
			AppDialog.showMessage(this,
					e.getMessage() + "\nCausa: " + e.getCause(),
					DialogType.DIALOGO_ALERTA);
		}

	}

	public boolean isDataValid(Pedido pedido) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter.setCalendar(Calendar.getInstance());
		Date d = formatter.parse(DateUtil.idateToStrYY(DateUtil.dt2i(Calendar
				.getInstance().getTime())));
		Date d2 = formatter.parse(DateUtil.idateToStrYY(pedido.getFecha()));
		if (DateUtil.d2i(d2) > DateUtil.d2i(d)) {
			AppDialog.showMessage(this,
					"La fecha del pedido no debe ser mayor a la fecha actual.",
					DialogType.DIALOGO_ALERTA);
			return false;
		}
		if ((pedido.getDetalles() == null)
				|| (pedido.getDetalles() != null && pedido.getDetalles().length == 0))
			AppDialog.showMessage(this,
					"El pedido no tiene detalle de productos.",
					DialogType.DIALOGO_ALERTA);

		return comprobarPromociones(pedido);
	}

	@SuppressLint("DefaultLocale")
	private boolean comprobarPromociones(Pedido pedido) throws Exception {
		// Si al pedido no se le ha aplicado promociones
		// y la aplicación es obligatoria
		// y si hay al menos una promoción aplicable
		// no permitir enviar pedido
		PedidoPromocion[] pproms = pedido.getPromocionesAplicadas();
		if ((pproms == null) || (pproms.length == 0)) {

			String apo = this
					.getApplicationContext()
					.getSharedPreferences("SystemParams",
							android.content.Context.MODE_PRIVATE)
					.getString("AplicacionPromocionesOpcional", "FALSE");

			if (apo.toUpperCase().compareTo("FALSE") == 0) {
				if (Promociones.getPromocionesAplican(pedido,
						this.getContentResolver()).size() > 0) {
					AppDialog.showMessage(this,
							"Debe aplicarse al menos una promoción al pedido.",
							DialogType.DIALOGO_ALERTA);
					return false;
				}
			}
		}

		return true;
	}
}
