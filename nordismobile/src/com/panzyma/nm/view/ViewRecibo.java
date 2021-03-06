package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_SETTING_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_STARTED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.SEND_DATA_FROM_SERVER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
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
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
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
import com.panzyma.nm.auxiliar.AppDialog.OnButtonClickListener;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CuentasPorCobrarFragment;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.FichaClienteFragment;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.fragments.consultaCobroFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.view.ViewPedido.FragmentActive;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.viewdialog.TasaCambioFragment;
import com.panzyma.nm.viewmodel.vmRecibo;
import com.panzyma.nordismobile.R;
@InvokeBridge(bridgeName = "BReciboM")
@SuppressWarnings("rawtypes")
public class ViewRecibo extends ActionBarActivity implements
		ListaFragment.OnItemSelectedListener, Handler.Callback {	
 
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {		
		super.onActivityResult(requestcode, resultcode, data);
		try 
		{ 
			NMApp.getController().setView(this);
			UserSessionManager.setContext(this);
			request_code = requestcode;
			if ((NUEVO_RECIBO == request_code || ABRIR_RECIBO == request_code)	&& data != null)
				establecer(data.getParcelableExtra("recibo"));
		} catch (Exception e) 
		{			
			e.printStackTrace();
		}
		if(drawerLayout!=null && drawerLayout.isShown())drawerLayout.closeDrawers();
		finishActivity(request_code);
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
			if (ABRIR_RECIBO == request_code) {
				vmRecibo recibe = recibos.get(positioncache);
				recibe.setRecibo(Integer.parseInt(String.valueOf(p.getId())),
						p.getReferencia(),
						p.getFecha(),
						p.getTotalRecibo(),
						p.getNombreCliente(),
						p.getDescEstado(),p.getCodEstado(),
						p.getObjSucursalID());				
				
			}else if (NUEVO_RECIBO == request_code) {				
				recibos.add( 0, 
						new vmRecibo(Integer.parseInt(String.valueOf(p.getId())),
								p.getReferencia(),
								p.getFecha(),
								p.getTotalRecibo(),
								p.getNombreCliente(),
								p.getDescEstado(),p.getCodEstado(),
								p.getObjSucursalID())	
				);
				positioncache = 0;
				//positioncache = recibos.size() - 1;
			}
		}
		setList();
	}
	
	private void setList(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gridheader.setVisibility(View.VISIBLE);
				gridheader.setText(String.format("LISTA RECIBOS (%s)",recibos.size()));
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
		FICHA_CLIENTE,
		CONSULTAR_COBROS
	};

	private static final String TAG = ViewRecibo.class.getSimpleName();
	private static int request_code;
	private static final int NUEVO_RECIBO = 0;
	private static final int ABRIR_RECIBO = 1;
	private static final int BORRAR_RECIBO = 2;
	protected static final int ENVIAR_RECIBO = 3;
	private static final int IMPRIMIR_RECIBO = 4;
	private static final int FICHA_CLIENTE = 5;
	private static final int CUENTAS_POR_COBRAR = 6;
	private static final int CONSULTA_COBROS = 7;
	private static final int TASA_CAMBIO = 8;
	private static final int CERRAR = 9;
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
	CustomDialog dlg;
	private ReciboColector recibo;
	private Intent intent;
	private Bundle b;
	consultaCobroFragment cobros;
	
	private static final int MOSTRAR_FACTURAS = 0;
	private static final int MOSTRAR_NOTAS_DEBITO = 1;
	private static final int MOSTRAR_NOTAS_CREDITO = 2;
	private static final int MOSTRAR_PEDIDOS = 3;
	private static final int MOSTRAR_RECIBOS = 4;	
	
	private static final int MOSTRAR_COBROS_DEL_DIA = 0;
	private static final int MOSTRAR_COBROS_SEMANA = 1;
	private static final int MOSTRAR_COBROS_MES = 2;
	private static final int IMPRIMIR = 3;
	
	
	
	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ViewRecibo $this = this;
		
		context = ViewRecibo.this;
		UserSessionManager.setContext(this);
		SessionManager.setContext(this);
		setContentView(R.layout.layout_client_fragment);		

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		
		initComponent();

		gridheader.setVisibility(View.VISIBLE);

		opcionesMenu = new String[] { "Nuevo Recibo", "Abrir Recibo",
				"Borrar Recibo", "Enviar Recibo", "Imprimir Recibo",
				"Ficha del Cliente", "Cuentas por Cobrar","Consultar Cobros","Tasa Cambio" ,"Cerrar" };

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
				customArrayAdapter = (CustomArrayAdapter<vmRecibo>) ((customArrayAdapter == null) ? ((Filterable) firstFragment)
						.getAdapter() : customArrayAdapter);
				
				int pos =customArrayAdapter.getSelectedPosition();
				//OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
				recibo_selected = customArrayAdapter.getItem(pos);
				
				if(fragmentActive == FragmentActive.LIST)
				{
					switch (position) {
					case NUEVO_RECIBO:
						intento = new Intent(ViewRecibo.this, ViewReciboEdit.class);
						//ENVIAR UN RECIBO VACIO EN CASO DE AGREGAR UNO
						intento.putExtra(RECIBO_ID, 0);
						//startActivity(intento);
						startActivityForResult(intento, NUEVO_RECIBO); 
						break;
					case ABRIR_RECIBO:
						intento = new Intent(ViewRecibo.this, ViewReciboEdit.class);
						if(recibo_selected != null) {
							//ENVIAR EL RECIBO SELECCIONADO EN CASO DE VER DEL DETALLE
							intento.putExtra(RECIBO_ID, recibo_selected.getNumero());
							//startActivity(intento);	
							startActivityForResult(intento, ABRIR_RECIBO);
						} else {
							Toast.makeText(getApplicationContext(), "No existen recibos para editar", Toast.LENGTH_SHORT).show();
						}			
					    //abrirRecibo(true);
						break;
					case BORRAR_RECIBO:
						//NO PERMITIR ELIMINAR RECIBOS DONDE EL ESTADO SEA DISTINTO A REGISTRADO 
						if(recibo_selected==null || (customArrayAdapter!=null && customArrayAdapter.getCount()==0)) return;
						if ("registrado".compareTo(recibo_selected.getCodEstado().toLowerCase()) == 0) 
						{
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putInt("idrecibo", recibo_selected.getId());  
							msg.setData(b);
							msg.what=ControllerProtocol.DELETE_DATA_FROM_LOCALHOST;	
							
							NMApp.getController().removeBridgeByName(BReciboM.class.toString());
							NMApp.getController().addOutboxHandler(new Handler($this));
							try {
								NMApp.getController().setEntities($this, new BReciboM());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							NMApp.getController()
									.getInboxHandler()
									.sendMessage(msg);
							
						} else {
							Toast.makeText(getApplicationContext(), String.format("Los recibos con estado '%s'.\n No se pueden eliminar.", recibo_selected.getDescEstado()), Toast.LENGTH_SHORT).show();
							return;
						} 
						break;	
				case ENVIAR_RECIBO: 
					if(recibo_selected==null || (customArrayAdapter!=null && customArrayAdapter.getCount()==0)) return;
					
					if(NMNetWork.isPhoneConnected(NMApp.getContext()) /*&& NMNetWork.CheckConnection(NMApp.getController())*/)
		            {
						if ( !("PAGADO".compareTo(recibo_selected.getCodEstado()) == 0) ) {
							// ENVIAR SOLO SI EL ESTADO DEL RECIBO ES DISTINTO A PAGADO
							enviarRecibo(recibo_selected);
						}				
					}	
					break;
				case IMPRIMIR_RECIBO:
					if( recibo_selected != null ){
						ReciboColector recibo = ModelRecibo.getReciboByRef(NMApp.getContext().getContentResolver(), recibo_selected.getNumero());
						enviarImprimirRecibo(recibo);
					}					
					break;
				case FICHA_CLIENTE :			
					
					if(recibo_selected== null)
					{
						AppDialog.showMessage(vr,"Informaci�n","Seleccione un registro.",DialogType.DIALOGO_ALERTA);
						return;
					}
					//SI SE EST� FUERA DE LA COBERTURA
			        if(NMNetWork.isPhoneConnected(NMApp.getContext()) && NMNetWork.CheckConnection(NMApp.getController()))
			        {
	//		            	AppDialog.showMessage(vr,"Informaci�n","La operaci�n no puede ser realizada ya que est� fuera de cobertura.",DialogType.DIALOGO_ALERTA);
	//		            	return;
			            
				            long sucursal=recibo_selected.getObjSucursalID();
				            setDrawerState(false);
				            args = new Bundle();
							args.putInt(FichaClienteFragment.ARG_POSITION, positioncache);
							args.putLong(FichaClienteFragment.ARG_SUCURSAL, sucursal);
				            
							FichaClienteFragment ficha;	
				            FragmentTransaction mytransaction = getSupportFragmentManager().beginTransaction();
				            
							fragmentActive = FragmentActive.FICHA_CLIENTE;
							/*if (findViewById(R.id.dynamic_fragment) != null) {
							}
							else*/
							{
								ficha = new FichaClienteFragment();
								ficha.setArguments(args);
								mytransaction.addToBackStack(null);
								mytransaction.replace(R.id.fragment_container,ficha);
								mytransaction.commit();	
							}
							gridheader.setVisibility(View.INVISIBLE);
			            }
						/*
						if (findViewById(R.id.fragment_container) != null) 
						{
							mytransaction.replace(R.id.fragment_container,ficha);
						}
						mytransaction.addToBackStack(null);
						mytransaction.commit();*/	
						
						
						//OCULTAR LA BARRA DE ACCION
						//getSupportActionBar().hide();
						break;
					case CUENTAS_POR_COBRAR:
						if(recibo_selected== null)
						{
							AppDialog.showMessage(vr,"Informaci�n","Seleccione un registro.",DialogType.DIALOGO_ALERTA);
							return;
						}
						
						if(NMNetWork.isPhoneConnected(NMApp.getContext()) && NMNetWork.CheckConnection(NMApp.getController()))
			            {
							fragmentActive = FragmentActive.CUENTAS_POR_COBRAR;
							if (findViewById(R.id.fragment_container) != null) 
							{	
								transaction = getSupportFragmentManager().beginTransaction();
								
								fragmentActive =FragmentActive.CUENTAS_POR_COBRAR;
								//cuentasPorCobrar = CuentasPorCobrarFragment.Instancia();
								
//								android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
//								if (prev != null){
//									transaction.remove(prev);
//								}
								
//								Bundle msg = new Bundle();
//								msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, positioncache);
//								msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID, recibo_selected.getObjSucursalID());
//								
//								
//								transaction.addToBackStack(null);
//								cuentasPorCobrar.setArguments(msg);
//								transaction.addToBackStack(null);
//								transaction.replace(R.id.fragment_container,cuentasPorCobrar);
//								transaction.commit();
							    //cuentasPorCobrar.show(transaction, "dialog");
							    
								fragmentActive = FragmentActive.CUENTAS_POR_COBRAR;
								if (findViewById(R.id.fragment_container) != null) 
								{	
									cuentasPorCobrar = new CuentasPorCobrarFragment();						
									Bundle msg = new Bundle();
									msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, pos);
									msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID, recibo_selected.getObjSucursalID());
									cuentasPorCobrar.setArguments(msg);	
									transaction = getSupportFragmentManager().beginTransaction();
									transaction.replace(R.id.fragment_container,cuentasPorCobrar);
									transaction.addToBackStack(null);
									transaction.commit();	
									EstablecerMenu(fragmentActive);
								}
							    
								/*
								cuentasPorCobrar = new CuentasPorCobrarFragment();						
								Bundle msg = new Bundle();
								msg.putInt(CuentasPorCobrarFragment.ARG_POSITION, pos);
								msg.putLong(CuentasPorCobrarFragment.SUCURSAL_ID, recibo_selected.getObjSucursalID());
								cuentasPorCobrar.setArguments(msg);	
								transaction = getSupportFragmentManager().beginTransaction();
								transaction.replace(R.id.fragment_container,cuentasPorCobrar);
								transaction.addToBackStack(null);
								transaction.commit();
								*/						
							}
							//OCULTAR LA BARRA DE ACCION
							//getSupportActionBar().hide();
			            }
						break;
					case CONSULTA_COBROS :
						if(NMNetWork.isPhoneConnected(NMApp.getContext()) && NMNetWork.CheckConnection(NMApp.getController()))
			            {
							fragmentActive = FragmentActive.CONSULTAR_COBROS;
							/*if (findViewById(R.id.dynamic_fragment) != null) {}
							else*/
							{
								FragmentTransaction mytransaction = getSupportFragmentManager().beginTransaction();
								cobros = new consultaCobroFragment();
								mytransaction.replace(R.id.fragment_container,cobros);
								mytransaction.addToBackStack(null);
								mytransaction.commit();
								footerView.setVisibility(View.GONE);
								
								//if((Build.VERSION.SDK_INT <= 10 || (Build.VERSION.SDK_INT >= 14 &&   ViewConfiguration.get(vr).hasPermanentMenuKey()))==false) {
									EstablecerMenu(fragmentActive);
								//}
							}
							//CERRAR EL MENU DEL DRAWER
			            }
						drawerLayout.closeDrawers();
						break;
					case TASA_CAMBIO :
					
						transaction = getSupportFragmentManager().beginTransaction();
						TasaCambioFragment dialog = TasaCambioFragment.newInstance();
						dialog.show(transaction, "dialog");
						
							
						//CERRAR EL MENU DEL DRAWER
						drawerLayout.closeDrawers();
						break;
					case CERRAR:
						FINISH_ACTIVITY();
						break;
					}
				
				}
				if(fragmentActive == FragmentActive.CONSULTAR_COBROS) {
					
					switch (position) {
						case MOSTRAR_COBROS_DEL_DIA:
							cobros.CargarCobrosDia();
						break;
						case MOSTRAR_COBROS_SEMANA:
							cobros.CargarCobrosSemana();
						break;
						case MOSTRAR_COBROS_MES:
							cobros.CargarCobrosMes();
						break;
						case IMPRIMIR: 
							cobros.IMPRIMIR();
						break;
					}
				}
				if(fragmentActive == FragmentActive.CUENTAS_POR_COBRAR) {
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
				drawerList.setItemChecked(position, true);
				//tituloSeccion = opcionesMenu[position];
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
		
		fragmentActive = FragmentActive.LIST;

		// Create an instance of ExampleFragment
		firstFragment = new ListaFragment<vmRecibo>();
		firstFragment.setRetainInstance(true);
		// In case this activity was started with special instructions from
		// an Intent,
		// pass the Intent's extras to the fragment as arguments
		firstFragment.setArguments(getIntent().getExtras());
		
		if ( savedInstanceState != null ) {
			Parcelable[] objects = savedInstanceState.getParcelableArray("recibos");	
			recibos = new ArrayList<vmRecibo>((Collection<? extends vmRecibo>) Arrays.asList(objects));
			//Recipes = vmRecibo.arrayParcelToArrayRecibo(objects);			
		} else {
			recibos = null;
		} 
		
		if(recibos == null) 
		{
			cargarRecibos();
		}  
		
		// However, if we're being restored from a previous state,
		// then we don't need to do anything and should return or else
		// we could end up with overlapping fragments.
		if (savedInstanceState != null) {
			return;
		}		

		// Add the fragment to the 'fragment_container' FrameLayout
		if (findViewById(R.id.fragment_container) != null) {			
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		} 
		/*else {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.item_client_fragment, firstFragment).commit();
		}*/
	}
	
	public void mandarObtenerRecibo(int arg)
	{
		Message msg = new Message(); 
		msg.obj=recibo_selected.getNumero();
		msg.arg1=arg;
		msg.what =ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
		NMApp.getController().getInboxHandler().sendMessage(msg); 
	}
	
	public void abrirRecibo(boolean... obtener)
	{
		
		try 
		{ 
			if (recibo_selected == null) 
				return;
			if (obtener!=null && obtener.length != 0 && obtener[0]) 
			{ 
				mandarObtenerRecibo(ABRIR_RECIBO);		
				return;
			}
			
			if (recibo== null) 
			{
				AppDialog.showMessage(this, "Informaci�n",
						"Error al obtener el recibo localmente...", DialogType.DIALOGO_ALERTA);
				return;
			}
			
			positioncache = customArrayAdapter.getSelectedPosition(); 
			intent = new Intent(ViewRecibo.this,ViewReciboEdit.class);
			b = new Bundle();
			b.putParcelable("recibo", recibo);
			intent.putExtras(b);
			intent.putExtra("requestcode", ABRIR_RECIBO);
			startActivityForResult(intent, ABRIR_RECIBO); 

		} catch (Exception e) {
			e.printStackTrace();
			AppDialog.showMessage(this, "Informaci�n",
					e.getMessage(), DialogType.DIALOGO_ALERTA);
		} 
		
	}
	
	
	public boolean valido(ReciboColector recibo) 
	{        

		try 
		{
			
			//Validar fecha del pedido
			//long d = DateUtil.d2i(recibo.getFecha());  
			long d = DateUtil.strDateToLong(DateUtil.idateToStr(recibo.getFecha()));
			if (d > DateUtil.d2i(Calendar.getInstance().getTime())) 
			{
				showStatusOnUI(
						new ErrorMessage(
								"Error en el proceso de enviar el recibo al servidor",
								"Fecha invalida", "\nCausa: "+"La fecha del recibo no debe ser mayor a la fecha actual."));				

				return false;
			}

			if (recibo.getNombreCliente().trim() == "") 
			{            
				showStatusOnUI(
						new ErrorMessage(
								"Error en el proceso de enviar el recibo al servidor",
								"El cliente del recibo no ha sido ingresado.", "")); 
				return false;
			}

			//Se incluya al menos una factura y/o la cantidad de facturas marcadas para ser incluidas       
			int cantFac = Cobro.cantFacturas(recibo);
			int cantND = Cobro.cantNDs(recibo);
			if (cantFac == 0) {
				if(cantND == 0) {
					showStatusOnUI(
							new ErrorMessage(
									"Error en el proceso de enviar el recibo al servidor",
									"Problemas con las Facturas.", "Debe incluir al menos una factura o nota de d�bito."));

					return false;
				}
			}

			//Validar que la cantidad de facturas incluidas no sea mayor que el valor del par�metro CantMaxFacturasEnRecibo.
			int max = Integer.parseInt(Cobro.getParametro(this.getContext(),"CantMaxFacturasEnRecibo")+"");
			if (cantFac > max) {
				showStatusOnUI(
						new ErrorMessage(
								"Error en el proceso de enviar el recibo al servidor",
								"Problemas con los Documentos.", "La cantidad de facturas no debe ser mayor que "+max));             

				return false;
			}

			//La cantidad de notas de d�bito marcadas para ser incluidas en el recibo 
			//no debe ser mayor que el valor del p�rametro CantMaxNotasDebitoEnRecibo        
			max = Integer.parseInt(Cobro.getParametro(this.getContext(),"CantMaxNotasDebitoEnRecibo")+"");
			if (cantND > max) {

				showStatusOnUI(
						new ErrorMessage(
								"Error en el proceso de enviar el recibo al servidor",
								"Problemas con los Documentos.", "La cantidad de notas de d�bito no debe ser mayor que "+max + "."));             
				return false;
			}

			//La cantidad de notas de cr�dito incluidas a pagar no debe ser mayor 
			//que el valor del par�metro CantMaxNotasCreditoEnRecibo        
			max = Integer.parseInt(Cobro.getParametro(this.getContext(),"CantMaxNotasCreditoEnRecibo")+"");
			if (Cobro.cantNCs(recibo) > max) {
				showStatusOnUI(
						new ErrorMessage(
								"Error al Validar el Recibo",
								"Problemas con los Documentos.", "La cantidad de notas de cr�dito no debe ser mayor que " + max + "."));
				//Dialog.alert("La cantidad de notas de cr�dito no debe ser mayor que " + max + ".");
				return false;
			}

			//Validar que se haya ingresado al menos un pago
			if (Cobro.cantFPs(recibo) == 0) {
				showStatusOnUI(
						new ErrorMessage(
								"Error al Validar el Recibo",
								"No se ha agregado ningun pago.", ""));

				//Dialog.alert("Detalle de pagos no ingresado.");
				return false; 
			}

			//Validar que la sumatoria de los montos de las NC seleccionadas no sea mayor ni igual que la sumatoria
			//de los montos a pagar de las facturas incluidas en el recibo, a excepci�n de que solamente se est�n
			//pagando facturas vencidas en cuyo caso S� se permite un monto igual
			if (recibo.getTotalNC() > 0) 
			{ //Si hay NC aplicadas

				//Ver si todas las facturas aplicadas son vencidas
				boolean todasVencidas = true;
				int diasAplicaMora = Integer.parseInt(Cobro.getParametro(this.getContext(), "DiasDespuesVenceCalculaMora")+"");
				long fechaHoy = DateUtil.getTime(DateUtil.getToday());
				if (recibo.getFacturasRecibo().size() != 0) 
				{
					ReciboDetFactura[] ff = (ReciboDetFactura[]) recibo.getFacturasRecibo().toArray(); 
					if (ff != null) 
					{
						for(int i=0; i<ff.length; i++) 
						{
							ReciboDetFactura f = ff[i];
							String s = f.getFechaVence() + "";
							int fechaVence = Integer.parseInt(s.substring(0, 8));
							long fechaCaeEnMora = DateUtil.addDays(DateUtil.getTime(fechaVence), diasAplicaMora);
							if (fechaCaeEnMora > fechaHoy) 
							{
								todasVencidas = false;
								break;
							}
						}
					}
					if (Cobro.getMontoTotalSoloFacturasAcancelar(recibo)< Cobro.getTotalNC_RCol(recibo)) 
					{ 
						showStatusOnUI(
								new ErrorMessage(
										"Error al Validar el Recibo",
										"Problemas con los Documentos.", "No se puede procesasar Recibo ya que, la sumatoria de las facturas a cancelar es menor a la suma a todas las NC ..."));
						// Dialog.alert("El total de notas de cr�dito a aplicar debe ser menor o igual al total a pagar en facturas.");
						return false;

					}
					
				} //Ver si todas las facturas aplicadas son vencidas

				if (todasVencidas && (recibo.getTotalNC() > recibo.getTotalFacturas()))  
				{
					showStatusOnUI(
							new ErrorMessage(
									"Error al Validar el Recibo",
									"Problemas con los Documentos.", "El total de notas de cr�dito a aplicar debe ser menor o igual al total a pagar en facturas." + max + "."));
					// Dialog.alert("El total de notas de cr�dito a aplicar debe ser menor o igual al total a pagar en facturas.");
					return false;
				}

				if (todasVencidas && (recibo.getTotalNC() >= recibo.getTotalFacturas()))  
				{
					showStatusOnUI(
							new ErrorMessage(
									"Error al Validar el Recibo",
									"Problemas con los Documentos.", "El total de notas de cr�dito a aplicar debe ser menor al total a pagar en facturas."));

					// Dialog.alert("El total de notas de cr�dito a aplicar debe ser menor al total a pagar en facturas.");
					return false;
				}

			} //Si hay NC aplicadas


			//Monto M�nimo Recibo: Para aplicar descuento espec�fico a cada factura que se va cancelar,
			//el total del recibo deber mayor o igual al valor del par�metro 'MontoMinReciboAplicaDpp'
			boolean ValidarMontoAplicaDpp = false;

			//Determinando si hay descPP que validar
			if (recibo.getFacturasRecibo().size() != 0) {
				ArrayList<ReciboDetFactura> ff =recibo.getFacturasRecibo();
				if (ff != null) {
					for(int i=0; i<ff.size(); i++) 
					{
						ReciboDetFactura f = ff.get(i);
						if (f.getMontoDescEspecifico() != 0) 
						{
							ValidarMontoAplicaDpp = true;
							break;
						}
					}
				}
			} //Determinando si hay descPP que validar

			//Validando el monto m�nimo del recibo
			float montoMinimoRecibo = Float.parseFloat(Cobro.getParametro(this.getContext(),"MontoMinReciboAplicaDpp")+"");
			if ((recibo.getTotalRecibo() < montoMinimoRecibo) && ValidarMontoAplicaDpp) 
			{
				//Recalcular detalles del recibo sin aplicar DescPP
				Cobro.calcularDetFacturasRecibo(this.getContext(),recibo, recibo.getCliente(), false);
				//actualizaTotales();            
				showStatusOnUI(
						new ErrorMessage(
								"Error al Validar el Recibo",
								"Problemas el Descuento PP.","Para aplicar descuento pronto pago \r\nel monto del recibo no debe ser menor que " + StringUtil.formatReal(montoMinimoRecibo) + "."));

				return false;
			}              
			if (Cobro.getTotalPagoRecibo(recibo) != recibo.getTotalRecibo()) 
			{
				showStatusOnUI(
						new ErrorMessage(
								"Error al Validar el Recibo",
								"Problema con el Monto Total del Recibo","El monto pagado no cuadra con el total del recibo."));
				return false;
			}
			return true;


		} catch (Exception e) 
		{

		}
		return false;
	}
	
	public  void showStatus(final NotificationMessage notificacion)
	{
		if(dlg!=null)
			dlg.dismiss();
		runOnUiThread(new Runnable()
        {
            @Override
			public void run()
            { 
            	dlg= new CustomDialog(context,notificacion.getMessage()+notificacion.getCause(),false,NOTIFICATION_DIALOG); 
            	dlg.show();
            }
        });		
	}
	
	public void showStatus(final String mensaje, boolean... confirmacion) {		 
			if (confirmacion.length != 0 && confirmacion[0]) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AppDialog.showMessage(context, "", mensaje,
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
						dlg =  new CustomDialog(context, mensaje, false,
								NOTIFICATION_DIALOG);
						dlg.show();
					}
				});
			} 
	}
	
	private void enviarRecibo(vmRecibo recibe)
	{   
		ReciboColector recibo = ModelRecibo.getReciboByRef(this.getContentResolver(), recibe.getNumero());
		
		if (recibo== null) 
		{
			AppDialog.showMessage(this, "Informaci�n",
					"Error al obtener el recibo localmente...", DialogType.DIALOGO_ALERTA);
			return;
		}
		
		if(!valido(recibo)) return;  
		
		if (recibo.getCodEstado().compareTo("PAGADO") == 0) 
		{
			showStatus("No se puede enviar un recibo que tiene estado PAGADO",true);  
			return;
		}
        
        if (recibo.getNumero() > 0) 
        {
        	showStatus("El recibo ya fue enviado anteriormente",true);  
			return; 
        } 
		showStatus("Enviando recibo a la central");  
		try 
		{ 			
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putParcelable("recibo", recibo); 
			/*
			b.putParcelableArray("facturasToUpdate", (Parcelable[]) recibo.getFacturasRecibo().toArray() ); //getArrayOfFacturas()
			b.putParcelableArray("notasDebitoToUpdate", (Parcelable[]) recibo.getNotasDebitoRecibo().toArray() ); // getArrayOfNotasDebito()
			b.putParcelableArray("notasCreditoToUpdate",  (Parcelable[]) recibo.getNotasCreditoRecibo().toArray()); // getArrayOfNotasCredito()
			*/
			b.putParcelableArray("facturasToUpdate", ModelRecibo.getFacturasByReciboDetalleFacturasList(recibo.getFacturasRecibo()) ); //getArrayOfFacturas()
			b.putParcelableArray("notasDebitoToUpdate", ModelRecibo.getNotasDebitoByReciboDetalleNDSList(recibo.getNotasDebitoRecibo()) ); // getArrayOfNotasDebito()
			b.putParcelableArray("notasCreditoToUpdate",  ModelRecibo.getNotasCreditoByReciboDetalleNCSList(recibo.getNotasCreditoRecibo())); // getArrayOfNotasCredito()
			msg.setData(b);
			msg.what=SEND_DATA_FROM_SERVER;
			NMApp.getController().getInboxHandler().sendMessage(msg);  	 

		} catch (Exception e) {			
			e.printStackTrace();
		}

	}
		
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  Parcelable [] objects = new Parcelable[recibos.size()];
	  recibos.toArray(objects);
	  savedInstanceState.putParcelableArray("recibos", objects);
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
	  Parcelable [] objects = savedInstanceState.getParcelableArray("recibos");
	  recibos = new ArrayList<vmRecibo>( (Collection<? extends vmRecibo>) Arrays.asList(objects) ); 
	  positioncache = savedInstanceState.getInt("positioncache");	  
	  firstFragment = (ListaFragment<vmRecibo>) savedInstanceState.getParcelable("fragment");
	  gridheader.setText(String.format("LISTA RECIBOS (%s)",recibos.size()));
	  //setList();
	}
		
	@Override
	protected void onPause() {
		NMApp.ciclo=NMApp.lifecycle.ONPAUSE;
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
	
	@Override
	protected void onResume() 
	{
		if(fragmentActive == FragmentActive.LIST && ( NMApp.ciclo==NMApp.lifecycle.ONPAUSE || NMApp.ciclo==NMApp.lifecycle.ONRESTART))
		{
			NMApp.getController().setView(this);
			SessionManager.setContext(this);
			UserSessionManager.setContext(this);
		}
		super.onResume();
	}
	
	@SuppressWarnings("unchecked")
	private void cargarRecibos() {
		try {
			NMApp.getController().setView(this);
			/*NMApp.getController().removeBridgeByName(BReciboM.class.toString());
			NMApp.getController().setEntities(this, bpm = new BReciboM());
			NMApp.getController().addOutboxHandler(new Handler(this));*/
			NMApp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);

		} catch (Exception e) {
			e.printStackTrace();
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
				

			} else 
			/*{
				customArrayAdapter = (CustomArrayAdapter<vmRecibo>) ((Filterable) getSupportFragmentManager()
						.findFragmentById(R.id.item_client_fragment)).getAdapter();
			}*/

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
		/*
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
		}*/

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

	@SuppressLint("CutPasteId")
	private void initComponent() {
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		gridheader.setText("LISTA RECIBOS (0) ");
		footerView = (TextView) findViewById(R.id.ctxtview_enty);
		footerView.setVisibility(View.VISIBLE);
		//initMenu();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case C_DATA:
			if (pDialog != null)
				pDialog.dismiss();
			establecer(msg);
			break;
		case DELETE_ITEM_FINISHED:	
			Toast.makeText(vr,msg.obj.equals(1)?"Eliminado Satisfactoriamente":"Error al eliminar", Toast.LENGTH_LONG); 
			runOnUiThread(new Runnable() {
				@Override
				public void run() 
				{
					gridheader.setVisibility(View.VISIBLE);
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
					gridheader.setText(String.format("LISTA RECIBOS (%s)",recibos.size()));
				}
			});
			//CERRAR EL MENU DEL DRAWER
			drawerLayout.closeDrawers();
			break;
		case C_UPDATE_STARTED:

			break;
		case C_UPDATE_ITEM_FINISHED:

			break;
		case C_UPDATE_FINISHED:
			// pDialog.hide();
			break;
		case C_SETTING_DATA:
			setData((ArrayList<vmRecibo>) ((msg.obj == null) ? new ArrayList<vmRecibo>()
					: msg.obj), C_SETTING_DATA);
			break;
		case ERROR:
			AppDialog.showMessage(context, ((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			return true;
		case ControllerProtocol.ID_REQUEST_ENVIAR:
			if (dlg != null)
				dlg.dismiss();
			
			if (msg.obj != null) {
				
				final ReciboColector recibo = (ReciboColector)msg.obj;
				AppDialog.showMessage(context,"","Se ha enviado Correctamente.\n�Desea Imprimir el recibo?",DialogType.DIALOGO_CONFIRMACION, 
								new OnButtonClickListener() {
									@Override
									public void onButtonClick(AlertDialog alert, int actionId) {
										if(actionId == AppDialog.OK_BUTTOM){
											
											Message msg = new Message();
											Bundle b = new Bundle();
											b.putParcelable("recibo", recibo);
											msg.setData(b);
											msg.what = ControllerProtocol.IMPRIMIR;
											NMApp.getController().getInboxHandler().sendMessage(msg);
											alert.dismiss();
										}
										else {
											alert.dismiss();
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													pDialog = new ProgressDialog(vr);
													pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
													pDialog.setMessage("Procesando...");
													pDialog.setCancelable(true);
													pDialog.show();
													cargarRecibos();
												}
												
											});
										}
									}
							});
			}
			break;
			
		case ControllerProtocol.ID_REQUEST_LOADITEM_LOCALHOST:  
			if (msg.obj==null) 
	        {	 
				AppDialog.showMessage(this,
						"Error al obtener pedido localmente....",
						DialogType.DIALOGO_ALERTA); 
				return false;
			}
			
			if(msg.obj instanceof ReciboColector)
			{
				recibo=(ReciboColector) msg.obj;	 
				switch (msg.arg1) 
				{
					case ABRIR_RECIBO:
						abrirRecibo();
						break;
				 
				}
				
			} 
		break;
		case ControllerProtocol.NOTIFICATION:
			if (dlg!= null) dlg.dismiss();
			if(msg.obj instanceof ErrorMessage )
				showStatus(msg.obj.toString(), true);
			else 
				showStatus(msg.obj.toString());
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			if (dlg!= null) dlg.dismiss();
			if(msg.obj instanceof ErrorMessage )
				showStatus(((ErrorMessage)msg.obj).getMessage(), true);
			else 
				showStatus(msg.obj.toString());
			break;

			
		}
		return false;
	}

	public void showStatusOnUI(Object msg) throws InterruptedException{
		
		final String titulo=""+((ErrorMessage)msg).getTittle();
		final String mensaje=""+((ErrorMessage)msg).getMessage();
		
		
		NMApp.getThreadPool().execute(new Runnable()
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
								gridheader.setText("LISTA RECIBOS ("
										+ customArrayAdapter.getCount() + ")");
								footerView.setVisibility(View.VISIBLE);
							} else {
								if (what == C_SETTING_DATA)
									footerView.setVisibility(View.VISIBLE);
								gridheader.setText("LISTA RECIBOS ("
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
	
	@Override
	public void onItemSelected(Object obj, int position) {
		recibo_selected = firstFragment.getAdapter().getItem(position);
		positioncache = position;
		ListView lista=firstFragment.getListView();
		if(lista!=null)
			firstFragment.getListView().smoothScrollToPosition(positioncache);	
	}
		
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU  && fragmentActive != FragmentActive.CUENTAS_POR_COBRAR && fragmentActive != FragmentActive.CONSULTAR_COBROS) {
			 drawerLayout.openDrawer(Gravity.LEFT);
			 
		}
		if (keyCode == KeyEvent.KEYCODE_MENU && fragmentActive == FragmentActive.CUENTAS_POR_COBRAR) {			
			cuentasPorCobrar.mostrarMenu();
			return true;
		} 
		if (keyCode == KeyEvent.KEYCODE_MENU && fragmentActive == FragmentActive.CONSULTAR_COBROS) {			
			cobros.mostrarMenu();
			drawerLayout.closeDrawer(Gravity.LEFT);
			return true;
		} 
		if(keyCode == KeyEvent.KEYCODE_MENU &&fragmentActive == FragmentActive.FICHA_CLIENTE){
			drawerLayout.closeDrawer(Gravity.LEFT);
			return false;
		}
		
		else if (keyCode == KeyEvent.KEYCODE_BACK && fragmentActive == FragmentActive.LIST) {        	
		  	FINISH_ACTIVITY();	
		  	finish();
		} 
		else if (keyCode == KeyEvent.KEYCODE_BACK && ( fragmentActive == FragmentActive.CUENTAS_POR_COBRAR || fragmentActive == FragmentActive.CONSULTAR_COBROS)) {
			fragmentActive =FragmentActive.LIST;
			getSupportActionBar().show();
			gridheader.setVisibility(View.VISIBLE);
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);	
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.detach(fragment);
            transaction.replace(R.id.fragment_container, firstFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			getSupportActionBar().show();
			EstablecerMenu(fragmentActive);
			setList();
			cuentasPorCobrar = null;
			cobros=null;
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	private void FINISH_ACTIVITY()	{
		NMApp.getThreadPool().stopRequestAllWorkers();
		NMApp.getController().removeOutboxHandler(TAG);
		NMApp.getController().removebridge(NMApp.getController().getBridge());			
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
		if (fragment instanceof FichaClienteFragment || fragment instanceof CuentasPorCobrarFragment || fragmentActive == FragmentActive.CONSULTAR_COBROS) {
			if(fragmentActive== FragmentActive.FICHA_CLIENTE){
				setDrawerState(true);
			}
			fragmentActive = FragmentActive.LIST;
			gridheader.setText(String.format("LISTA RECIBOS (%s)",recibos.size()));
			gridheader.setVisibility(View.VISIBLE);
			getSupportActionBar().show();
			FragmentTransaction mytransaction = getSupportFragmentManager().beginTransaction();
			mytransaction.replace(R.id.fragment_container, firstFragment);
			mytransaction.commit();
			EstablecerMenu(fragmentActive);
			setList();
			
		} 
	}

	public Context getContext()
	{
		return this.context;
	}
	
	private  void enviarImprimirRecibo(final ReciboColector recibo) 
	{
		
//		if (recibo != null && !recibo.getCodEstado().equals("PAGADO")) 
//		{
//			showStatus("No se puede imprimir recibos en estado "+ recibo.getCodEstado(), true);
//			return;
//		}
		if (recibo.getCodEstado().compareTo("REGISTRADO") == 0) {
			showStatus("El recibo no ha sido enviado.",true);
            return;
        }
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AppDialog.showMessage(context, "", "Desea Imprimir el Recibo?",
						AppDialog.DialogType.DIALOGO_CONFIRMACION,
						new AppDialog.OnButtonClickListener() {
							@Override
							public void onButtonClick(AlertDialog _dialog,
									int actionId) {
								if (actionId == AppDialog.OK_BUTTOM) {
									try {
										Message msg = new Message();
										Bundle b = new Bundle();
										b.putParcelable("recibo", recibo);
										msg.setData(b);
										msg.what = ControllerProtocol.IMPRIMIR;
										NMApp.getController().getInboxHandler()
												.sendMessage(msg);
										_dialog.dismiss();

									} catch (Exception e) {
										NMApp.getController()
												.notifyOutboxHandlers(
														ControllerProtocol.ERROR,
														0,
														0,
														new ErrorMessage(
																"Error al intentar Imprimir el Recibo",
																e.getMessage(),
																e.getMessage()));
									}
								}
							}
						});
			}
		});

	}
	
	public void EstablecerMenu(FragmentActive fragmentActive){
		if(fragmentActive == FragmentActive.LIST)
		opcionesMenu = new String[] { "Nuevo Recibo", "Abrir Recibo",
				"Borrar Recibo", "Enviar Recibo", "Imprimir Recibo",
				"Ficha del Cliente", "Cuentas por Cobrar","Consultar Cobros","Tasa Cambio" ,"Cerrar" };
		
		if(fragmentActive == FragmentActive.CONSULTAR_COBROS)
			opcionesMenu = new String[] { "Mostrar Cobros del D�a", "Mostrar Cobros de la Semana", "Mostrar Cobros del Mes", "Imprimir" };
		
		if(fragmentActive ==FragmentActive.CUENTAS_POR_COBRAR)
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
}

