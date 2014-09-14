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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CuentasPorCobrarFragment;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.fragments.FichaProductoFragment;
import com.panzyma.nm.fragments.FichaReciboFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.model.ModelPedido;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.viewmodel.vmRecibo;
import com.panzyma.nordismobile.R;

@SuppressWarnings("rawtypes")
public class ViewRecibo extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestcode, resultcode, data);
		try 
		{ 
			nmapp.getController().setEntities(this,this.getBridge());
			request_code = requestcode;
			if ((NUEVO_RECIBO == request_code || EDITAR_RECIBO == request_code)	&& data != null)
				establecer(data.getParcelableExtra("recibo"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(drawerLayout!=null)drawerLayout.closeDrawers();
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityFromFragment(fragment, intent, requestCode);
	}
	
	@SuppressWarnings("unchecked")
	private void establecer(Object _obj) {
		if (_obj == null)
			return;

		if (_obj instanceof Message) 
		{
			Message msg = (Message) _obj;
			recibos = (ArrayList<vmRecibo>) ((msg.obj == null) ? new ArrayList<vmRecibo>(): msg.obj); 
		}
		if (_obj instanceof ReciboColector) {
			ReciboColector p = (ReciboColector) _obj;
			if (EDITAR_RECIBO == request_code) {
				vmRecibo recibe = recibos.get(positioncache);
				recibe.setRecibo(Integer.parseInt(String.valueOf(p.getId())),
						p.getNumero(),
						p.getFecha(),
						p.getTotalRecibo(),
						p.getNombreCliente(),
						p.getDescEstado(),p.getCodEstado(),
						p.getObjSucursalID());				
				
			}else if (NUEVO_RECIBO == request_code) {
				recibos.add(
						new vmRecibo(Integer.parseInt(String.valueOf(p.getId())),
								p.getNumero(),
								p.getFecha(),
								p.getTotalRecibo(),
								p.getNombreCliente(),
								p.getDescEstado(),p.getCodEstado(),
								p.getObjSucursalID())	
				);
				positioncache = recibos.size() - 1;
			}
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gridheader.setVisibility(View.VISIBLE);
				gridheader.setText(String.format("Listado de Recibos (%s)",recibos.size()));
				if (recibos.size() == 0) {
					TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
					txtenty.setVisibility(View.VISIBLE);
					
				} 
				else
				{
					positioncache = 0;
					firstFragment.setItems(recibos);
					firstFragment.getAdapter().setSelectedPosition(positioncache);
					recibo_selected = firstFragment.getAdapter().getItem(positioncache);
				}
			}
		});

	}

	private BReciboM getBridge() {
		return bpm;
	}

	public enum FragmentActive {
		LIST,
		ITEM,
		CUENTAS_POR_COBRAR,
		FICHA_CLIENTE
	};

	private static final String TAG = ViewRecibo.class.getSimpleName();
	private static int request_code;
	private static final int NUEVO_RECIBO = 0;
	private static final int EDITAR_RECIBO = 1;
	private static final int BORRAR_RECIBO = 2;
	protected static final int ENVIAR_RECIBO = 3;
	private static final int FICHA_CLIENTE = 5;
	private static final int CUENTAS_POR_COBRAR = 6;
	public static final String RECIBO_ID = "recibo_id";
	private FragmentActive fragmentActive = null;
	private BReciboM bpm;
	private ViewRecibo vr;
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
	private QuickAction quickAction;
	private Button btnMenu;
	private Display display;
	Bundle args = new Bundle();
	ProgressDialog pDialog;
	TextView gridheader;
	TextView footerView;
	Intent intento;
	private List<vmRecibo> recibos = new ArrayList<vmRecibo>();
	vmRecibo recibo_selected;
	ListaFragment<vmRecibo> firstFragment;
	FragmentTransaction transaction;
	CuentasPorCobrarFragment cuentasPorCobrar;
	Object lock=new Object();
	
	private static final int MOSTRAR_FACTURAS = 0;
	private static final int MOSTRAR_NOTAS_DEBITO = 1;
	private static final int MOSTRAR_NOTAS_CREDITO = 2;
	private static final int MOSTRAR_PEDIDOS = 3;
	private static final int MOSTRAR_RECIBOS = 4;	
	
	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		SessionManager.setContext(this);
		setContentView(R.layout.layout_client_fragment);
		
		transaction = getSupportFragmentManager()
				.beginTransaction();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		
		initComponent();

		gridheader.setVisibility(View.VISIBLE);

		opcionesMenu = new String[] { "Nuevo Recibo", "Editar Recibo",
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
				//CERRAR EL MENU DEL DRAWER
				drawerLayout.closeDrawers();
				//SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
				int pos = customArrayAdapter.getSelectedPosition();
				//OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
				recibo_selected = customArrayAdapter.getItem(pos);
				
				switch (position) {
				case NUEVO_RECIBO:
					intento = new Intent(ViewRecibo.this, ViewReciboEdit.class);
					//ENVIAR UN RECIBO VACIO EN CASO DE AGREGAR UNO
					intento.putExtra(RECIBO_ID, 0);
					//startActivity(intento);
					startActivityForResult(intento, NUEVO_RECIBO);
					break;
				case EDITAR_RECIBO:
					intento = new Intent(ViewRecibo.this, ViewReciboEdit.class);
					if(recibo_selected != null) {
						//ENVIAR EL RECIBO SELECCIONADO EN CASO DE VER DEL DETALLE
						intento.putExtra(RECIBO_ID, recibo_selected.getId());
						//startActivity(intento);	
						startActivityForResult(intento, EDITAR_RECIBO);
					} else {
						Toast.makeText(getApplicationContext(), "No existen recibos para editar", Toast.LENGTH_SHORT).show();
					}					
					break;
				case BORRAR_RECIBO:
					//NO PERMITIR ELIMINAR RECIBOS DONDE EL ESTADO SEA DISTINTO A REGISTRADO 
					if(recibo_selected==null || (customArrayAdapter!=null && customArrayAdapter.getCount()==0)) return;
					if ("REGISTRADO".equals(recibo_selected.getCodEstado())) 
					{
						nmapp.getController()
								.getInboxHandler()
								.sendEmptyMessage(
										ControllerProtocol.DELETE_DATA_FROM_LOCALHOST);
					} else {
						Toast.makeText(getApplicationContext(), String.format("Los recibos con estado '%s'.\n No se pueden eliminar.", recibo_selected.getDescEstado()), Toast.LENGTH_SHORT).show();
						return;
					} 
					break;	
				case ENVIAR_RECIBO:  
					break;
				case FICHA_CLIENTE :			
					
					if(recibo_selected== null)
					{
						AppDialog.showMessage(vr,"Información","Seleccione un registro.",DialogType.DIALOGO_ALERTA);
						return;
					}
					//SI SE ESTÁ FUERA DE LA COBERTURA
		            if(!NMNetWork.isPhoneConnected(context,nmapp.getController()) && !NMNetWork.CheckConnection(nmapp.getController()))
		            {
		            	AppDialog.showMessage(vr,"Información","La operación no puede ser realizada ya que está fuera de cobertura.",DialogType.DIALOGO_ALERTA);
		            	return;
		            }
		            long sucursal=recibo_selected.getObjSucursalID();
		            
		            args = new Bundle();
					args.putInt(FichaClienteFragment.ARG_POSITION, positioncache);
					args.putLong(FichaClienteFragment.ARG_SUCURSAL, sucursal);
		            
					FichaClienteFragment ficha;	
		            FragmentTransaction mytransaction = getSupportFragmentManager().beginTransaction();
		            
					fragmentActive = FragmentActive.FICHA_CLIENTE;
					if (findViewById(R.id.dynamic_fragment) != null) {
					}
					else{
						ficha = new FichaClienteFragment();
						ficha.setArguments(args);
						mytransaction.addToBackStack(null);
						mytransaction.replace(R.id.fragment_container,ficha);
						mytransaction.commit();	
					}
					/*
					if (findViewById(R.id.fragment_container) != null) 
					{
						mytransaction.replace(R.id.fragment_container,ficha);
					}
					mytransaction.addToBackStack(null);
					mytransaction.commit();*/	
					gridheader.setVisibility(View.INVISIBLE);
					
					//OCULTAR LA BARRA DE ACCION
					//getSupportActionBar().hide();
					break;
				case CUENTAS_POR_COBRAR:
					fragmentActive = FragmentActive.CUENTAS_POR_COBRAR;
					if (findViewById(R.id.fragment_container) != null) 
					{	
						cuentasPorCobrar = new CuentasPorCobrarFragment();						
						Bundle msg = new Bundle();
						msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, pos);
						msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID, recibo_selected.getObjSucursalID());
						cuentasPorCobrar.setArguments(msg);
						transaction.replace(R.id.fragment_container,cuentasPorCobrar);
						transaction.addToBackStack(null);
						transaction.commit();						
					}
					//CERRAR EL MENU DEL DRAWER
					drawerLayout.closeDrawers();
					//OCULTAR LA BARRA DE ACCION
					getSupportActionBar().hide();
					break;
				}

				drawerList.setItemChecked(position, true);
				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);

			}
		});

		tituloSeccion = getTitle();
		tituloApp = getTitle();
		vr=this;

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
			nmapp.getController().removeBridgeByName(BReciboM.class.toString());
			nmapp.getController().setEntities(this, bpm = new BReciboM());
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
		
		fragmentActive = FragmentActive.LIST;

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
		
		if ( fragmentActive == FragmentActive.LIST) 
		{
			
			if (findViewById(R.id.fragment_container) != null) {
				
				Object obj = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
				
				if ( !(obj instanceof CuentasPorCobrarFragment) ){
					customArrayAdapter = (CustomArrayAdapter<vmRecibo>) ((Filterable) getSupportFragmentManager()
							.findFragmentById(R.id.fragment_container)).getAdapter();					
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
		//initMenu();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case C_DATA:
			establecer(msg);
			return true;
		case DELETE_ITEM_FINISHED:	
			Toast.makeText(vr,msg.obj.equals(1)?"Eliminado Satisfactoriamente":"Error al eliminar", Toast.LENGTH_LONG); 
			runOnUiThread(new Runnable() {
				@Override
				public void run() 
				{
					gridheader.setVisibility(View.VISIBLE);
					gridheader.setText(String.format("Listado de Recibos (%s)",recibos.size()));
					if(recibos!=null && recibos.size()!=0)
						recibos.remove(recibo_selected);
					if (recibos.size() == 0) 
					{
						TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
						txtenty.setVisibility(View.VISIBLE);
						firstFragment.getAdapter().clearItems();
						firstFragment.getAdapter().notifyDataSetChanged();
						
					} 
					else
					{
						positioncache = 0;
						firstFragment.setItems(recibos);
						firstFragment.getAdapter().setSelectedPosition(positioncache);
						recibo_selected = firstFragment.getAdapter().getItem(positioncache);
						firstFragment.getAdapter().notifyDataSetChanged();
					}
				}
			});
			//CERRAR EL MENU DEL DRAWER
			drawerLayout.closeDrawers();
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

public void showStatusOnUI(Object msg) throws InterruptedException{
		
		final String titulo=""+((ErrorMessage)msg).getTittle();
		final String mensaje=""+((ErrorMessage)msg).getMessage();
		
		
		nmapp.getThreadPool().execute(new Runnable()
		{ 
			@Override
			public void run()
		    {
				 
				try 
				{
					
					runOnUiThread(new Runnable() 
			        {
						@Override
						public void run() 
						{ 
							 AppDialog.showMessage(vr,titulo,mensaje,AppDialog.DialogType.DIALOGO_CONFIRMACION,new AppDialog.OnButtonClickListener() 
							 {						 
									@Override
					    			public void onButtonClick(AlertDialog _dialog, int actionId) 
					    			{ 
					    				synchronized(lock)
					    				{
					    					lock.notify();
					    				}
					    			}
							  }); 
				          }
					});
					
			        synchronized(lock)
			        {
			            try {
			            	lock.wait();
						} catch (InterruptedException e) { 
							e.printStackTrace();
						}
			        }
					
				} catch (Exception e) 
				{ 
				}
		    }
		}); 
		
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

	/*@SuppressWarnings("unchecked")
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
	}*/

	@Override
	public void onItemSelected(Object obj, int position) {

		/*FichaReciboFragment reciboFragment;
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
		*/
	}
		
//	private void initMenu() {
//		quickAction = new QuickAction(this, QuickAction.VERTICAL, 1);
//		quickAction.addActionItem(new ActionItem(MOSTRAR_FACTURAS,
//				"Mostrar Facturas"));
//		quickAction.addActionItem(new ActionItem(MOSTRAR_NOTAS_DEBITO,
//				"Mostrar Notas Débito"));
//		quickAction.addActionItem(new ActionItem(MOSTRAR_NOTAS_CREDITO,
//				"Mostrar Notas Crédito"));
//		quickAction.addActionItem(null);
//		quickAction.addActionItem(new ActionItem(MOSTRAR_PEDIDOS,
//				"Mostrar Pedidos"));
//		quickAction.addActionItem(new ActionItem(MOSTRAR_RECIBOS,
//				"Mostrar Recibos"));		
//
//		quickAction
//				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
//					@Override
//					public void onItemClick(QuickAction source, final int pos,
//							int actionId) {
//
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								ActionItem actionItem = quickAction.getActionItem(pos);
//								
//								switch (actionItem.getActionId()) {
//								case MOSTRAR_FACTURAS:
//									cuentasPorCobrar.cargarFacturasCliente();
//									break;
//								case MOSTRAR_NOTAS_DEBITO:
//									cuentasPorCobrar.cargarNotasDebito();
//									break;
//								case MOSTRAR_NOTAS_CREDITO:
//									cuentasPorCobrar.cargarNotasCredito();
//									break;
//								case MOSTRAR_RECIBOS:
//									cuentasPorCobrar.cargarRecibosColector();
//									break;
//								case MOSTRAR_PEDIDOS:
//									cuentasPorCobrar.cargarPedidos();
//									break;								
//								}
//							}
//						});
//
//					}
//
//				});
//		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				quickAction.dismiss();
//			}
//		});
//
//	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && fragmentActive == FragmentActive.CUENTAS_POR_COBRAR) {			
			cuentasPorCobrar.mostrarMenu();
			return true;
		} 
		else if (keyCode == KeyEvent.KEYCODE_BACK && fragmentActive == FragmentActive.LIST) {        	
		  	FINISH_ACTIVITY();	
		  	finish();
		} else if (keyCode == KeyEvent.KEYCODE_BACK && fragmentActive == FragmentActive.CUENTAS_POR_COBRAR) {
			getSupportActionBar().show();
			gridheader.setVisibility(View.VISIBLE);
		}
		
		return super.onKeyUp(keyCode, event);
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
	@Override
	public void onBackPressed() {
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (fragment instanceof FichaClienteFragment) {
			fragmentActive = FragmentActive.LIST;
			gridheader.setVisibility(View.VISIBLE);
			getSupportActionBar().show();
			FragmentTransaction mytransaction = getSupportFragmentManager().beginTransaction();
			mytransaction.replace(R.id.fragment_container, firstFragment);
			mytransaction.commit();
		}
	}

	public Context getContext()
	{
		return this.context;
	}
}

