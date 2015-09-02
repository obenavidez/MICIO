package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections; 
import java.util.LinkedHashMap;  
import java.util.LinkedList;
import java.util.List;
import java.util.Map;  

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat; 
import android.support.v4.app.FragmentManager;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Spinner; 
import android.widget.TextView;

import com.panzyma.nm.NMApp; 
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager; 
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.custom.model.SpinnerModel; 
import com.panzyma.nm.interfaces.Editable; 
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente; 
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nm.view.adapter.ExpandListAdapter;
import com.panzyma.nm.view.adapter.ExpandListChild;
import com.panzyma.nm.view.adapter.ExpandListGroup;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.adapter.SetViewHolderWLayout;
import com.panzyma.nm.view.viewholder.ProductoLoteDetalleViewHolder;
import com.panzyma.nm.view.viewholder.ProductoLoteViewHolder;
import com.panzyma.nm.viewdialog.DevolucionProductoCantidad;
import com.panzyma.nm.viewdialog.DevolverDocumento;
import com.panzyma.nm.viewdialog.DialogCliente; 
import com.panzyma.nm.viewdialog.DialogProducto;
import com.panzyma.nm.viewdialog.EditDevolucionProducto;
import com.panzyma.nm.viewdialog.ProductoDevolucion;
import com.panzyma.nm.viewdialog.DevolucionProductoCantidad.escucharModificacionProductoLote;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener; 
import com.panzyma.nordismobile.R;

@SuppressWarnings({ "unchecked", "rawtypes","deprecation","unused" })
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
	//TIPO DEVOLUCIÓN
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
	
	private CustomDialog dlg;
	public static ProductoDevolucion pd;
	List<DevolucionProducto> dev_prod;
	
	public List<DevolucionProducto> getDev_prod() {
		return dev_prod;
	}	

	private Devolucion dev;
	private ExpandableListView lvdevproducto; 
	private ExpandListAdapter adapter;
	protected int[] positioncache = new int[2];
	protected ExpandListGroup dvselected;
	private ExpandListChild childselected;

	private ViewDevolucionEdit me;
	ArrayList<Producto> aprodselected;

	private ExpandListGroup groupselected;
	
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
	public Cliente getCliente() {
		return cliente;
	}

	Pedido pedido;
	Factura factura;
	Lote lote;
	Devolucion devolucion;
	
	Context context;
	private ProgressDialog pdialog;
	
	public static final Display display;
	
	List<ExpandListGroup> lgroups = new LinkedList<ExpandListGroup>();
	
    static 
    { 
    	int contador=0;
    	Map<String, String> aMap = null;  
    	
    	aMap = new LinkedHashMap<String, String>(); 
    	aMap.put("-1",""); 
    	aMap.put("RE","Reposición");
    	aMap.put("NC","Nota de Crédito");   
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
		me = this;
		setContentView(R.layout.devolucion_edit);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		com.panzyma.nm.NMApp.getController().setView(this);
		Message m=new Message();
		aprodselected = new ArrayList<Producto>();
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
		View include=findViewById(R.id.pdevgrilla);
		lvdevproducto = (ExpandableListView)include.findViewById(R.id.ExpList); 
		
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
				
				if(!"DAÑADO".equals(((ValorCatalogo) adapter_motdev.getItem(position).getObj()).getCodigo()))
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
				
			}
		});
		
		cboxtramitedev.setOnItemSelectedListener(new OnItemSelectedListener() {

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
	
	public void initExpandableListView()
	{		
		if (adapter == null) 
		{
			ArrayList<SetViewHolderWLayout> layouts = new ArrayList<SetViewHolderWLayout>();
			layouts.add(new SetViewHolderWLayout(R.layout.detalle_productolote,
					ProductoLoteViewHolder.class, true));
			layouts.add(new SetViewHolderWLayout(R.layout.detalle_loteproducto,
					ProductoLoteDetalleViewHolder.class, false));
			try 
			{
				adapter = new ExpandListAdapter(context,lgroups=SetStandardGroups(), layouts);
				lvdevproducto.setAdapter(adapter);
				
				for(int g=0; g < adapter.getGroupCount(); g++){
					lvdevproducto.expandGroup(g);
				}
				
				lvdevproducto.setOnChildClickListener(new OnChildClickListener() {

							
							@Override
							public boolean onChildClick(
									ExpandableListView _parent, View v,
									int groupPosition, int childPosition,
									long id) {
								int flatpost;
								int ajustPos;
								if (_parent == null)
									return false;

								if (positioncache != null
										&& positioncache.length != 0) 
								{
									long value = ExpandableListView
											.getPackedPositionForChild(
													positioncache[0],
													positioncache[1]);
									flatpost = _parent
											.getFlatListPosition(value);
									ajustPos = flatpost
											- _parent.getFirstVisiblePosition();
									View oldview = _parent.getChildAt(ajustPos);
									if (oldview != null
											&& oldview.getTag() != null
											&& oldview.getTag() instanceof ProductoLoteDetalleViewHolder)
									{
										oldview.setBackgroundDrawable(context
												.getResources().getDrawable(
														R.color.Terracota));
										oldview.setSelected(false);
									}
										
								}
								v.setSelected(true);
								v.setBackgroundDrawable(context
										.getResources().getDrawable(
												R.color.LighBlueMarine));
								positioncache[0] = groupPosition;
								positioncache[1] = childPosition;
								
								childselected=(ExpandListChild) adapter.getChild(positioncache[0], positioncache[1]);
								
								return true;
							}
						});

				lvdevproducto.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(
									AdapterView<?> _parent, View view,
									int position, long id) 
							{
								ExpandableListView elv;
								if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
								{

									if (view.getTag() instanceof ProductoLoteDetalleViewHolder) 
									{

										elv = (ExpandableListView) _parent;
										ProductoLoteDetalleViewHolder pld = (ProductoLoteDetalleViewHolder) view.getTag();

										int flatpost;
										int ajustPos;
										if (elv == null)
											return false;

										if (positioncache != null && positioncache.length != 0) 
										{
											long value = ExpandableListView
													.getPackedPositionForChild(
															positioncache[0],
															positioncache[1]);
											flatpost = elv
													.getFlatListPosition(value);
											ajustPos = flatpost - elv.getFirstVisiblePosition();
											View oldview = elv.getChildAt(ajustPos);
											if (oldview != null
													&& oldview.getTag() != null
													&& oldview.getTag() instanceof ProductoLoteDetalleViewHolder)
											{
												oldview.setBackgroundDrawable(context
														.getResources()
														.getDrawable(
																R.color.Terracota));
												oldview.setSelected(false);
											}
												

										}
										view.setSelected(true);
										view.setBackgroundDrawable(context
												.getResources().getDrawable(
														R.color.LighBlueMarine));

										positioncache[0] = ExpandableListView.getPackedPositionGroup(id);
										positioncache[1] = ExpandableListView.getPackedPositionChild(id);
										 
										childselected=(ExpandListChild) adapter.getChild(positioncache[0], positioncache[1]);
										groupselected=(ExpandListGroup) adapter.getGroup(positioncache[0]);
										EditarProductoLote(groupselected.getName(),childselected);
										
									}

									return true;
								}
								return false;
							}
						});

				lvdevproducto.setOnChildClickListener(new OnChildClickListener() {					
					@Override
					public boolean onChildClick(ExpandableListView parent, View v,
							int groupPosition, int childPosition, long id) {
						Object obj = adapter.getChild(groupPosition, childPosition);
						return false;
					}
				});
				
				lvdevproducto.setOnGroupClickListener(new OnGroupClickListener() {
					
					public boolean onGroupClick(ExpandableListView parent, View v,
							int groupPosition, long id) {
						groupselected = (ExpandListGroup) adapter.getGroup(groupPosition);						
						return false;
					}
				});
				
			} catch (Exception e) {
			}
		} else {
			adapter.notifyDataSetChanged();	
			((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
		}
				
		
	}	
	
	private void EditarProductoLote(String productname,ExpandListChild _childselected) 
	{ 
		FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialogNotaRecibo");
		if (prev != null) 
		{
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DevolucionProductoCantidad newFragment = DevolucionProductoCantidad.newInstance(productname,(DevolucionProductoLote) _childselected.getObject());
		newFragment.obtenerProductoLoteModificado(new escucharModificacionProductoLote() {
			
			@Override
			public void onButtonClick(DevolucionProductoLote plote) 
			{ 
				updateGrid(plote);
			}
		});
		newFragment.show(ft, "dialogDevolucionProductoCantidad");
	}
	
	public List<ExpandListGroup> SetStandardGroups() 
	{
		LinkedList<ExpandListGroup> _lgroups = new LinkedList<ExpandListGroup>();
		
		LinkedList<ExpandListChild> groupchild;

		for (DevolucionProducto dp : dev_prod) 
		{
			ExpandListGroup group = new ExpandListGroup();
			groupchild = new LinkedList<ExpandListChild>();
			group.setName(dp.getNombreProducto());
			group.setObject(dp);
			for (DevolucionProductoLote dpl : dp.getProductoLotes()) 
			{
				ExpandListChild ch = new ExpandListChild();
				ch.setName(dpl.getNumeroLote());
				ch.setObject(dpl);
				groupchild.add(ch);
			}
			group.setItems(groupchild);
			_lgroups.add(group);
		} 
		return _lgroups;
	}
	
	public void updateGrid(DevolucionProductoLote dpl)
	{  
		int cantidadDevolver =0, cantidadTotalDevolver=0,cantidadBonificada=0,cantidadOrdenada=0,cantidadFacturada;
		float proporcion=0.0f;
		double preciounitario=0;
		double montobonificacion=0;
		double subTotal;
		double impuesto=0;	
		
		ExpandListGroup lg=new ExpandListGroup();		
		ExpandListChild ch = new ExpandListChild();
		ch.setName(dpl.getNumeroLote());
		ch.setObject(dpl); 		
		groupselected.getItems().set(positioncache[1],ch);			
		
		//sumar todas las cantidades a devolver de sus lote del producto seleccionado.
		for(ExpandListChild _ch:groupselected.getItems()) 
			cantidadTotalDevolver+=((DevolucionProductoLote)_ch.getObject()).getCantidadDevuelta(); 
		DevolucionProducto dp=(DevolucionProducto) groupselected.getObject();
		dp.setCantidadDevolver(cantidadTotalDevolver); 
		groupselected.setObject(dp); 
		lgroups.set(positioncache[0], groupselected);	 
		 
		
		//Estimar costo devolucion
		int cantmindevbonif=Integer.parseInt(this.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("CantMinDevolvBonif","0"));
		 		
		DevolucionProducto _dp=(DevolucionProducto) groupselected.getObject();
		cantidadDevolver=_dp.getCantidadDevolver();
		//bonificacion se calcula si la cantidad a devolver es mayor que la cantidad min
		if(cantidadDevolver>=cantmindevbonif || pedido!=null)
		{
			if(pedido!=null)
			{ 			
				cantidadBonificada=dp.getCantidadBonificada();
				cantidadOrdenada=dpl.getCantidadDespachada();
				cantidadFacturada=cantidadOrdenada+dp.getCantidadBonificada();	
				preciounitario=dp.getPrecio()/100.00;					
				if(cantidadDevolver==cantidadFacturada)
					cantidadBonificada=dp.getCantidadBonificada();
				else
				{
					if(cantidadDevolver < cantidadFacturada)
					{
						// calcular Bonificacion
						proporcion  = ((float)dp.getCantidadBonificada() / (float)cantidadOrdenada ) + 1;
						cantidadBonificada = (int)Math.ceil((cantidadDevolver - (cantidadDevolver / proporcion)));
					}
				}
				
			}
			
		}
		else
			cantidadBonificada=0; 
		// calcular montobonificacion
		montobonificacion = cantidadBonificada * preciounitario;
		subTotal=cantidadDevolver*preciounitario;
		// Impuesto --pendiente, tenemos q traer el impuesto del pedido.
		//impuesto = porcentajadeImpuestoPedido * ( subtotal - montobonificacion); 
		//Total	
		dp.setBonificacion(cantidadBonificada);
		dp.setBonificacionVen(cantidadBonificada);
		dp.setMontoBonif((long) ((double)(montobonificacion*100)));
		dp.setMontoBonifVen((long) ((double)(montobonificacion*100)));
		
		dp.setSubtotal((long) ((double)(subTotal*100.00))); 
		dp.setTotal((long) ((double)((subTotal - montobonificacion + impuesto))*100));
		groupselected.setObject(dp); 
		lgroups.set(positioncache[0], groupselected);		
		//fin estimacion de costo		
		initExpandableListView();
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

		// Añadimos Funciones al menú laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				drawerList.setItemChecked(position, true);
				drawerLayout.closeDrawers();
				tituloSeccion = opcionesMenu[position];
				// Ponemos el titulo del Menú
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
				case ID_AGREGAR_PRODUCTO:
						agregarProducto();
						break;
				case ID_AGREGAR_LOTE:						
						if ( groupselected != null ){
							EditDevolucionProducto dialogDevolucion = new EditDevolucionProducto(groupselected, me);
		            		FragmentManager fragmentManager =  getSupportFragmentManager();
							dialogDevolucion.show(fragmentManager, "");
						}					
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
	
	private void agregarProducto() 
	{
	
		if (cliente == null) 
		{
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"Seleccione primero el cliente del pedido.",
					"Seleccione primero el cliente del pedido.","")); 
			return;
		}

		DialogProducto dp = new DialogProducto(me, aprodselected, cliente.getObjCategoriaClienteID(), cliente.getObjTipoClienteID());
		

		dp.setOnDialogProductButtonClickListener(new DialogProducto.OnButtonClickListener() {

			@Override
			public void onButtonClick(DetallePedido det_p, Producto prod) 
			{
				com.panzyma.nm.NMApp.getController().setView(me);
				det_p.setId(pedido.getId());
				aprodselected.add(prod);				
			}

		});
		Window window = dp.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 10, display.getHeight() - 50);
		dp.show();

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
					"No puede modificar la Devolución en estado "+devolucion
					.getCodEstado(),
					"No puede modificar la Devolución en estado "+devolucion
					.getCodEstado(),"")); 
			return;
		}
		
		showClientDialog();
	}
	
	private void devolverdocumento() 
	{		
		if (cliente == null) 
		{
			AppDialog.showMessage(this, "Alerta",
					"Por favor seleccione un cliente primero.",
					DialogType.DIALOGO_ALERTA);
			return;
		}		
		
		boolean isoffline=false;
		pdialog=ProgressDialog.show(this, "Probando conexión","");
		if( (!NMNetWork.CheckConnection()) && !UserSessionManager.HAS_ERROR)  
			isoffline=true; 
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogDevolverDocumento");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DevolverDocumento newFragment =DevolverDocumento.newInstance(this,cliente.getIdSucursal(), devolucion,isoffline); 
		newFragment.setOnDialogClickListener(new DevolverDocumento.DialogListener() 
		{ 
			@Override
			public void onDialogPositiveClick(Devolucion  _dev) 
			{ 
				if(_dev!=null && _dev.getProductosDevueltos()!=null && _dev.getProductosDevueltos().length!=0)
				{
					dev=_dev;
					pedido=dev.getObjPedido();
					dev_prod=Arrays.asList(dev.getProductosDevueltos());
					initExpandableListView();
				}
				
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
		return null;
	}

	@Override
	public Context getContext() 
	{
		return this;
	}
	
	public void hideProgress(){
		runOnUiThread(new Runnable() 
		{
			
			@Override
			public void run() {
				if(pdialog!=null)
					pdialog.dismiss();				
			}
		});
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
			case ControllerProtocol.NOTIFICATION_DIALOG:
				showStatus(msg.obj.toString(),true);
				break;
			case ControllerProtocol.ERROR:
				AppDialog.showMessage(me, ((ErrorMessage) msg.obj).getTittle(),
						((ErrorMessage) msg.obj).getMessage(),
						DialogType.DIALOGO_ALERTA);
				break;
		}
		return false;
	}

	public void showStatus(final String mensaje, boolean... confirmacion) 
	{ 
		if (confirmacion.length != 0 && confirmacion[0]) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(me, "", mensaje,
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
					dlg = new CustomDialog(me, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}


	public void setDev_prod(ArrayList<DevolucionProducto> arrayList) {
		this.dev_prod = arrayList;		
	}
	

}
