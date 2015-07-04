package com.panzyma.nm.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections; 
import java.util.LinkedHashMap;  
import java.util.Map;  

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat; 
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity; 
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView; 
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner; 
import android.widget.TextView;

import com.panzyma.nm.NMApp; 
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.SessionManager; 
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.custom.model.SpinnerModel; 
import com.panzyma.nm.interfaces.Editable; 
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente; 
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.viewdialog.DevolverDocumento;
import com.panzyma.nm.viewdialog.DialogCliente; 
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener; 
import com.panzyma.nordismobile.R;

@InvokeBridge(bridgeName = "BDevolucionM")
public class ViewDevolucionEdit extends ActionBarActivity implements
Handler.Callback, Editable
{	
	private static final int ID_SELECCIONAR_CLIENTE = 0; 
	private static final int ID_DEVOLVER_PEDIDO = 1; 
	private static final int ID_AGREGAR_PRODUCTO = 2;
	private static final int ID_AGREGAR_LOTE = 3;
	private static final int ID_EDITAR_NOTA = 4;
	private static final int ID_VER_COSTEO = 5;
	private static final int ID_GUARDAR = 6;
	private static final int ID_ENVIAR = 7;
	private static final int ID_CERRAR = 8;
	
	//TIPO TRAMITE
	private static final int REPOSICION = 1;
	private static final int NOTADECREDITO = 2;
	//TIPO DEVOLUCI�N
	private static final int TOTAL = 1;	
	private static final int PARCIAL = 2;
	
	private TextView tbxNombreDelCliente;
	private CheckBox ckboxnovencidodev;
	private Spinner cboxmotivodev;
	private Spinner cboxtramitedev;
	private Spinner cboxtipodev;
	private CheckBox ckboxncinmeditata;
	
	CustomAdapter adapter_motdev;
	CustomAdapter adapter_tramite;
	CustomAdapter adapter_tipodev;
	ArrayList<Catalogo> catalogos;
	
	
	DrawerLayout drawerLayout;
	ListView drawerList;
	ActionBarDrawerToggle drawerToggle;
	String[] opcionesMenu;
	CharSequence tituloSeccion;
	CharSequence tituloApp;
	View _view;
	
	private static final Map<String, String> hmmotivodev=null;
	private static final Map<String, String> hmtramite;
	private static final Map<String, String> hmtipodev;
	
	Cliente cliente;
	Pedido pedido;
	Factura factura;
	Lote lote;
	Devolucion devolucion;
	
	Context context;
	
	public static final Display display;
	
    static 
    { 
    	int contador=0;
    	Map<String, String> aMap = null;  
    	 
    	aMap = new LinkedHashMap<String, String>(); 
    	aMap.put("-1",""); 
    	aMap.put("RE","Reposici�n");
    	aMap.put("NC","Nota de Cr�dito");   
    	hmtramite = Collections.unmodifiableMap(aMap);
    	 
    	aMap = new LinkedHashMap<String, String>(); 
    	aMap.put("-1","");
    	aMap.put("TT","Total");
    	aMap.put("PC","Parcial");  
    	hmtipodev = Collections.unmodifiableMap(aMap); 
    	
    	WindowManager wm = (WindowManager) NMApp.getContext().getApplicationContext()
    			.getSystemService(Context.WINDOW_SERVICE);
    	 display= wm.getDefaultDisplay();
    	
    }
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		context=this;
		setContentView(R.layout.devolucion_edit);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		com.panzyma.nm.NMApp.getController().setView(this);
		Message m=new Message();
		m.what=ControllerProtocol.OBTENERVALORCATALOGO;
		m.obj=new String[]{"MotivoDevolucionNoVencidos"};
		NMApp.getController().getInboxHandler().sendMessage(m);
		initComponent();
	}
	
	public void initComponent() 
	{
		ckboxnovencidodev=(CheckBox) findViewById(R.id.devchk_typodevolucion);
		ckboxncinmeditata=(CheckBox) findViewById(R.id.devchk_ncinmediata);
		cboxmotivodev=(Spinner) findViewById(R.id.devcombox_motivo);
		cboxtramitedev=(Spinner) findViewById(R.id.devcombox_tramite);
		cboxtipodev=(Spinner) findViewById(R.id.devcombox_tipo);
		tbxNombreDelCliente=(TextView) findViewById(R.id.devtextv_detallecliente); 
		
		adapter_tramite = new CustomAdapter(getContext(),
				R.layout.spinner_rows, setListData(hmtramite));
		cboxtramitedev.setAdapter(adapter_tramite); 
		
		adapter_tipodev = new CustomAdapter(getContext(),
				R.layout.spinner_rows, setListData(hmtipodev));
		cboxtipodev.setAdapter(adapter_tipodev);
		
		
		cboxmotivodev.setOnItemSelectedListener(new OnItemSelectedListener() {
 
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id){  
				if (position == 0)
					return;
				
				if(!"DA�ADO".equals(((ValorCatalogo) adapter_motdev.getItem(position).getObj()).getCodigo()))
				{
					cboxtramitedev.setSelection(2);
					adapter_tramite.setSelectedPosition(2);
					cboxtramitedev.setEnabled(false); 
				}
				else
					cboxtramitedev.setEnabled(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		cboxtramitedev.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) 
			{
				if (position == 0)
					return;
				if("RE".equals(((Map.Entry<String, String>) adapter_tramite
						.getItem(position).getObj()).getKey()))
				{
					ckboxncinmeditata.setChecked(false);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			} 
 
		});
		ckboxncinmeditata.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				 
				
				if("RE".equals(((SpinnerModel)cboxtramitedev.getSelectedItem()).getCodigo()))
				{
					ckboxncinmeditata.setChecked(false);
				}
			}
		});
		
		if(devolucion==null)
		{
			devolucion=new Devolucion();
			devolucion.setCodEstado("REGISTRADA");
			devolucion.setId(0);
			devolucion.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
			devolucion.setNumeroCentral(0);
			devolucion.setReferencia(0); 
			
			cboxtramitedev.setSelection(NOTADECREDITO);
			cboxtipodev.setSelection(PARCIAL);
		}
		
		CreateMenu();
		
	}
	
	public void CreateMenu() {
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(
				R.array.devolucioneditoptions);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		// A�adimos Funciones al men� laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				drawerList.setItemChecked(position, true);
				drawerLayout.closeDrawers();
				tituloSeccion = opcionesMenu[position];
				// Ponemos el titulo del Men�
				getSupportActionBar().setTitle(tituloSeccion);
				Controller controller = com.panzyma.nm.NMApp.getController();
				switch (position) 
				{ 
				
				case ID_SELECCIONAR_CLIENTE:
						seleccionarCliente();
						break;
				case ID_DEVOLVER_PEDIDO:
						devolverdocumento();
						break;
//				case ID_CONDICIONES_Y_NOTAS:
//						agregarCondicionesYNotas();
//						break;
//				case ID_EDITAR_PRODUCTO:
//						editarProducto();
//						break;
//				case ID_ELIMINAR_PRODUCTO:
//						eliminarProducto();
//						break;
//				case ID_CONSULTAR_BONIFICACIONES:
//						consultarBonificaciones();
//						break;
//				case ID_CONSULTAR_LISTA_PRECIOS:
//						consultarPrecioProducto();
//						break;
//				case ID_APLICAR_PROMOCIONES:
//						aplicarPromociones(true); 
//						break;
//				case ID_DESAPLICAR_PROMOCIONES:
//						desaplicarPromociones();
//						break;
//				case ID_EXONERAR_IMPUESTO:
//						exonerarDeImpuesto();
//						break;
//				case ID_GUARDAR:
//						salvarPedido();
//						break;
//				case ID_ENVIAR:
//						enviarPedido();
//						break;
//				case ID_IMPRIMIR_COMPROBANTE:
//					try {
//						ImprimirComprobante();
//						break;
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				case ID_CERRAR:
						//FINISH_ACTIVITY();
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
				ActivityCompat.invalidateOptionsMenu(ViewDevolucionEdit.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewDevolucionEdit.this);

			}
		};

		// establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		_view = super.onCreateView(name, context, attrs);
		return _view;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.action_search).setVisible(true);
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
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_MENU) 
			drawerLayout.openDrawer(Gravity.LEFT);
		return super.onKeyUp(keyCode, event);
	}
	
	private void seleccionarCliente() 
	{
		if (!((devolucion.getCodEstado().compareTo("REGISTRADA") == 0) || (devolucion
				.getCodEstado().compareTo("APROBADA") == 0))){
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"No puede modificar la Devoluci�n en estado "+devolucion
					.getCodEstado(),
					"No puede modificar la Devoluci�n en estado "+devolucion
					.getCodEstado(),"")); 
			return;
		}
		
		showClientDialog();
	}
	
	private void devolverdocumento() 
	{
		
		if (cliente == null) {
			AppDialog.showMessage(this, "Alerta",
					"Por favor seleccione un cliente primero.",
					DialogType.DIALOGO_ALERTA);
			return;
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialogNotaRecibo");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DevolverDocumento newFragment =DevolverDocumento.newInstance(this,cliente.getIdSucursal(), devolucion); 
		newFragment.setOnDialogClickListener(new DevolverDocumento.DialogListener() 
		{
			@Override
			public void onDialogPositiveClick(Devolucion dev, long nopedido,
					Pedido _pedido) { 
				
				
			}
		});
		newFragment.show(ft, "dialogDocumentoDevolucion");
		
	}
	
	public void showClientDialog(final AlertDialog... _dialog)
	{
		DialogCliente dc = new DialogCliente(this,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dc.setOnDialogClientButtonClickListener(new OnButtonClickListener() 
		{

			@Override
			public void onButtonClick(Cliente _cliente) 
			{
				com.panzyma.nm.NMApp.getController().setView((Callback) context);
				if (devolucion.getCodEstado().compareTo("REGISTRADA") != 0)
					return;
				cliente = _cliente;
				setInformacionCliente();
				if(_dialog!=null && _dialog.length!=0)
					_dialog[0].dismiss();
			}
		});
		Window window = dc.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dc.show();
	}
	
	private void setInformacionCliente() {
		tbxNombreDelCliente.setText(cliente.getNombreCliente());
		devolucion.setObjClienteID(cliente.getIdCliente());
		devolucion.setObjSucursalID(cliente.getIdSucursal());
		devolucion.setNombreCliente(cliente.getNombreCliente());  
	}
	
	private ArrayList<SpinnerModel> setListData(ArrayList<Catalogo> valores) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
		// Now i have taken static values by loop.
		// For further inhancement we can take data by webservice / json / xml;
		valores.get(0).getValoresCatalogo().add(0,new ValorCatalogo(0, "-1", ""));
		for (ValorCatalogo valor : valores.get(0).getValoresCatalogo()) {

			final SpinnerModel sched = new SpinnerModel();

			/******* Firstly take data in model object ******/
			sched.setId(valor.getId());
			sched.setCodigo(valor.getCodigo());
			sched.setDescripcion(valor.getDescripcion()); 
			sched.setObj(valor);

			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}
		return CustomListViewValuesArr;
	}

	private ArrayList<SpinnerModel> setListData(Map<String, String> valores) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
		// Now i have taken static values by loop.
		// For further inhancement we can take data by webservice / json / xml;
		int contador=0; 
		for (Map.Entry<String, String> entry : valores.entrySet()) 		  
	    { 
			final SpinnerModel sched = new SpinnerModel(); 
			/******* Firstly take data in model object ******/
			sched.setId(contador);
			sched.setCodigo((String)entry.getKey());
			sched.setDescripcion((String) entry.getValue()); 
			sched.setObj(entry);
			contador++;
			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}
		return CustomListViewValuesArr;
	}
	
	@Override
	public Object getBridge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean handleMessage(Message msg) 
	{ 		
		switch (msg.what) 
		{
			case ControllerProtocol.OBTENERVALORCATALOGO:				
				adapter_motdev = new CustomAdapter(getContext(),
						R.layout.spinner_rows, setListData(catalogos=(ArrayList<Catalogo>) msg.obj));
				cboxmotivodev.setAdapter(adapter_motdev);  
				break;
		}
		return false;
	}
	

}
