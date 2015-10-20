package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections; 
import java.util.Date;
import java.util.LinkedHashMap;  
import java.util.LinkedList;
import java.util.List;
import java.util.Map;  

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
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
import android.widget.EditText;
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
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.custom.model.SpinnerModel; 
import com.panzyma.nm.interfaces.Editable; 
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente; 
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PrecioProducto;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.serviceproxy.Ventas;
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
	private CheckBox ckboxvencidodev;
	private Spinner cboxmotivodev;
	private Spinner cboxtramitedev;
	private Spinner cboxtipodev;
	private CheckBox ckboxncinmeditata;
	private EditText tbxFecha;
	
	CustomAdapter adapter_motdev;
	CustomAdapter adapter_tramite;
	CustomAdapter adapter_tipodev;
	ArrayList<Catalogo> catalogos;
	
	private CustomDialog dlg;
	public static ProductoDevolucion pd;
	List<DevolucionProducto> dev_prod;
	public static final String CLIENTE = "cliente";
	
	public List<DevolucionProducto> getDev_prod() {
		return dev_prod;
	}	
 
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
		if (getIntent().hasExtra("cliente")) {
			long IdCliente = getIntent().getLongExtra("cliente", 0);
			try {
				cliente = Ventas.getClienteBySucursalID(IdCliente,me.getContentResolver());
				setInformacionCliente();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void initComponent() 
	{
		ckboxvencidodev=(CheckBox) findViewById(R.id.devchk_typodevolucion);
		ckboxncinmeditata=(CheckBox) findViewById(R.id.devchk_ncinmediata);
		cboxmotivodev=(Spinner) findViewById(R.id.devcombox_motivo);
		cboxtramitedev=(Spinner) findViewById(R.id.devcombox_tramite);
		cboxtipodev=(Spinner) findViewById(R.id.devcombox_tipo);
		tbxNombreDelCliente=(TextView) findViewById(R.id.devtextv_detallecliente); 
		View include=findViewById(R.id.pdevgrilla);
		lvdevproducto = (ExpandableListView)include.findViewById(R.id.ExpList); 
		tbxFecha=(EditText)findViewById(R.id.devetextv_detalle_fecha);
		
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
		if (devolucion.getFecha() == 0)
			devolucion.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
		tbxFecha.setText("" + DateUtil.idateToStrYY(devolucion.getFecha()));
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
				actualizarProductoLote(plote);
				validarDevolucion(false);
				//updateGrid(plote);
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
	
	public void updateObject()
	{		
		List<ExpandListGroup>  _lgroups = lgroups;
		ExpandListGroup group = null;
		DevolucionProducto[] adp=new DevolucionProducto[lgroups.size()];
		
		for(int a=0;a<lgroups.size();a++)
		{
			group =  (ExpandListGroup) lgroups.get(a);
			DevolucionProducto dp=(DevolucionProducto) group.getObject();
			
			LinkedList<ExpandListChild> Items=group.getItems();
			DevolucionProductoLote[] adpl=new DevolucionProductoLote[Items.size()];
			for(int i=0;i<Items.size();i++)
			{
				adpl[i]=(DevolucionProductoLote) Items.get(i).getObject();
			}
			dp.setProductoLotes(adpl);
			adp[a]=dp; 			
		}
		devolucion.setProductosDevueltos(adp);
	}
	
	private void actualizarProductoLote(DevolucionProductoLote dpl)
	{
		int cantidadTotalDevolver = 0;
		ExpandListGroup lg=new ExpandListGroup();		
		ExpandListChild ch = new ExpandListChild();
		ch.setName(dpl.getNumeroLote());
		ch.setObject(dpl); 		
		groupselected.getItems().set(positioncache[1],ch);			
		//sumar todas las cantidades a devolver de sus lote del producto seleccionado.
		for(ExpandListChild _ch:groupselected.getItems()) 
			cantidadTotalDevolver+=((DevolucionProductoLote)_ch.getObject()).getCantidadDevuelta(); 
		DevolucionProducto _dp=(DevolucionProducto) groupselected.getObject();
		
		if(_dp.getProductoLotes()==null || (_dp.getProductoLotes()!=null &&_dp.getProductoLotes().length==0))
		{
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
					"Al menos un lote debe ser incluido en el detalle.",
					"Al menos un lote debe ser incluido en el detalle.","")); 
			return ;
		}
		for(DevolucionProductoLote _dpl:_dp.getProductoLotes())
		{
			if("".equals(_dpl.getNumeroLote()) || _dpl.getNumeroLote()==null)
			{
				com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
						String.format("Para cada detalle del producto '{0}' se debe ingresar el número de lote",_dp.getNombreProducto()),
						String.format("Para cada detalle del producto '{0}' se debe ingresar el número de lote",_dp.getNombreProducto()),"")); 
				return ;
			}
			
			
			if(_dpl.getFechaVencimiento()<=0)
			{
				com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
						String.format("Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",_dp.getNombreProducto()),
						String.format("Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",_dp.getNombreProducto()),"")); 
				return ;
			}
			if(_dpl.getCantidadDevuelta()<=0)
			{
				com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
						String.format("La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.", _dp.getNombreProducto()),
						String.format("La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.", _dp.getNombreProducto()),"")); 
				return ;
			}
			 
		}		
		_dp.setCantidadDevolver(cantidadTotalDevolver); 
		groupselected.setObject(_dp); 
		lgroups.set(positioncache[0], groupselected);	 	
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
				 
				case ID_GUARDAR:
						salvarDevolucion();
						break;
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
	
	protected void salvarDevolucion() 
	{
		if(!validarDevolucion())
			return;
		Message msg = new Message(); 
		msg.obj=devolucion; 
		msg.what = SAVE_DATA_FROM_LOCALHOST;
		com.panzyma.nm.NMApp.getController().getInboxHandler().sendMessage(msg);
		
	}
	
	public boolean validarDevolucion(boolean...todo)
	{ 
		boolean result;
		pedido.setFecha(DateUtil.d2i(new Date()));
		if (cliente == null) 
		{
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"Seleccione primero el cliente de la Devolución.",
					"Seleccione primero el cliente de la Devolución.","")); 
			return false;
		}
		if (( cboxmotivodev==null || (cboxmotivodev!=null && cboxmotivodev.getSelectedItem()==null)) && !ckboxvencidodev.isChecked())
		{
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					 "Debe seleccionar la causa de la devolución",
					 "Debe seleccionar la causa de la devolución","")); 
			return false;
		}
		
		if(devolucion.getProductosDevueltos()==null || (devolucion.getProductosDevueltos()!=null && devolucion.getProductosDevueltos().length==0))
		{
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
					"Al menos un producto debe ser incluido en la devolución.",
					"Al menos un producto debe ser incluido en la devolución.","")); 
			return false;
		}
		
		if(devolucion.getProductosDevueltos()==null || (devolucion.getProductosDevueltos()!=null && devolucion.getProductosDevueltos().length==0))
		{
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
					"Al menos un producto debe ser incluido en la devolución.",
					"Al menos un producto debe ser incluido en la devolución.","")); 
			return false;
		}
		
		if((todo!=null && todo.length!=0 && todo[0]) || (todo==null || (todo!=null && todo.length==0 )))
		for(DevolucionProducto _dp:devolucion.getProductosDevueltos())
		{
			if(_dp.getProductoLotes()==null || (_dp.getProductoLotes()!=null &&_dp.getProductoLotes().length==0))
			{
				com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
						"Al menos un lote debe ser incluido en el detalle.",
						"Al menos un lote debe ser incluido en el detalle.","")); 
				return false;
			}
			for(DevolucionProductoLote _dpl:_dp.getProductoLotes())
			{
				if("".equals(_dpl.getNumeroLote()) || _dpl.getNumeroLote()==null)
				{
					com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
							String.format("Para cada detalle del producto '{0}' se debe ingresar el número de lote",_dp.getNombreProducto()),
							String.format("Para cada detalle del producto '{0}' se debe ingresar el número de lote",_dp.getNombreProducto()),"")); 
					return false;
				}				
				
				if(_dpl.getFechaVencimiento()<=0)
				{
					com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
							String.format("Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",_dp.getNombreProducto()),
							String.format("Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",_dp.getNombreProducto()),"")); 
					return false;
				}
				if(_dpl.getCantidadDevuelta()<=0)
				{
					com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage( 
							String.format("La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.", _dp.getNombreProducto()),
							String.format("La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.", _dp.getNombreProducto()),"")); 
					return false;
				}				 
			}
		}
		
		/*ckboxvencidodev=(CheckBox) findViewById(R.id.devchk_typodevolucion);
		ckboxncinmeditata=(CheckBox) findViewById(R.id.devchk_ncinmediata);
		cboxmotivodev=(Spinner) findViewById(R.id.devcombox_motivo);
		cboxtramitedev=(Spinner) findViewById(R.id.devcombox_tramite);
		cboxtipodev=(Spinner) findViewById(R.id.devcombox_tipo);
		tbxNombreDelCliente=(TextView) findViewById(R.id.devtextv_detallecliente); 
		View include=findViewById(R.id.pdevgrilla);
		lvdevproducto = (ExpandableListView)include.findViewById(R.id.ExpList); 
		tbxFecha=(EditText)findViewById(R.id.devetextv_detalle_fecha);*/
		 
		if("NC".equals(((SpinnerModel)cboxtramitedev.getSelectedItem()).getCodigo())) 
		{
			devolucion.setTipoTramite("NC");
		}
		else
		{
			devolucion.setTipoTramite("RE"); 
		}		
		
		if("TT".equals(((SpinnerModel)cboxtramitedev.getSelectedItem()).getCodigo())) 
		
			
		devolucion.setParcial(("TT".equals(((SpinnerModel)cboxtramitedev.getSelectedItem()).getCodigo()))?false:true);
		devolucion.setDeVencido(ckboxvencidodev.isChecked());
		if (this.pedido == null || this.pedido.getId() == 0L)
		{
			devolucion.setObjPedidoDevueltoID(0);
		}
		else
		{
			devolucion.setObjPedidoDevueltoID(pedido.getId());
		}
		
		devolucion.setObjClienteID(cliente.getIdCliente());
		devolucion.setObjSucursalID(cliente.getIdSucursal());
		if (!ckboxncinmeditata.isChecked() && "REGISTRADA".equals(devolucion.getCodEstado()))
		{
			EstimarCostosDev(false);
//			this.CalcMontoPromocion();
//			this.CalcularTotalDev(); 
		} 
		return true;
	}

	public void EstimarCostosDev(boolean calBonif)
	{
		//Estimar costo devolucion
		int cantmindevbonif=Integer.parseInt(this.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("CantMinDevolvBonif","0"));
		//sumar todas las cantidades a devolver de sus lote del producto seleccionado.
		for(int a=0;a<lgroups.size();a++)
		{
			ExpandListGroup group =  (ExpandListGroup) lgroups.get(a);
			DevolucionProducto _dp=(DevolucionProducto) group.getObject();
			Producto prod = null;
			try {
				prod = ModelProducto.getProductoByID(getContext().getContentResolver(),_dp.getObjProductoID());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int cantidaddevolver=_dp.getCantidadDevolver();
			if (cantidaddevolver >= cantmindevbonif || this.pedido != null)
			{
				if (this.pedido != null)
				{
					int cantidadordenada=_dp.getCantidadOrdenada();
					int cantidadbonificadaeditada=_dp.getCantidadBonificada();
					if(_dp.getCantidadDevolver()==(cantidadordenada+cantidadbonificadaeditada))
					{
						_dp.setBonificacion(cantidadbonificadaeditada);
						_dp.setBonificacionVen(cantidadbonificadaeditada);
					}else
					{
						if (calBonif)
						{
							if(_dp.getCantidadDevolver()<(cantidadordenada+cantidadbonificadaeditada))
							{								
								int rs=CalcularBonificacion(_dp.getCantidadDevolver(),_dp.getCantidadOrdenada(),_dp.getCantidadBonificada());
								_dp.setBonificacion(rs);
								_dp.setBonificacionVen(rs);
							}
						}
					}
					
					
				}
				else
				{
					if (calBonif)
					{
						int qtybonif = 0; 
						 
						Bonificacion bonif=getBonificacion(prod, cliente.getObjCategoriaClienteID(), _dp.getCantidadDevolver());
						if (bonif!=null  && bonif.getCantBonificacion()!=0)
						{
							qtybonif = bonif.getCantBonificacion();
						}
						_dp.setBonificacion(qtybonif);
						_dp.setBonificacionVen(qtybonif); 
					}
				}
			}
			else
			{
				_dp.setBonificacion(0);
				_dp.setBonificacionVen(0); 
			}
			
			if (this.pedido != null)
			{  

				if (!devolucion.isDeVencido())
				{
					// Calcular precio IdTipoClienteMayorista
					long idTP = pedido.getObjTipoPrecioVentaID();
					long idTipoCliente=cliente.getObjTipoClienteID();
					long IdTipoClienteMayorista=Long.parseLong(this.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("IdTipoClienteMayorista","0"));
					if (IdTipoClienteMayorista==idTipoCliente)
						idTP = Long.parseLong(this.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("IdTipoPrecioGeneral","0"));

					_dp.setPrecio((long)(getPrecioProducto(prod, idTP,  _dp.getCantidadDevolver())*100));
			    }
				else
				{
					List<Catalogo> catalogo=ModelLogic.getValorCatalogo(new String("VEN"));
					long idTP =pedido.getObjTipoPrecioVentaID();
					if(catalogo!=null)
						idTP=catalogo.get(0).getId();
					Long.parseLong(this.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("VEN","0"));
					_dp.setPrecio((long)(getPrecioProducto(prod, idTP,  _dp.getCantidadDevolver())*100)); 
				} 
				
				_dp.setSubtotal((long)((double)(_dp.getCantidadDevolver()*(_dp.getPrecio()/100.00))*100.00)); 
				long boniL=(long)((double)(_dp.getBonificacion()*(_dp.getPrecio()/100.00))*100.00);
				_dp.setMontoBonif(boniL); 
				_dp.setMontoBonifVen(boniL);
				
				if (_dp.isGravable() )
				{
					if (this.pedido != null)
					{ 
						_dp.setPorcImpuesto((long) ViewDevolucionEdit.getPorcImpuesto(pedido,prod.getId()));
						 
					}
					else
					{
						int porcimp=Integer.parseInt(this.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("PorcentajeImpuesto","0"));
						_dp.setPorcImpuesto((long) porcimp); 
					}
					
					 
				}
				long impuestoL=(long)(_dp.getPorcImpuesto() * (_dp.getSubtotal() - _dp.getBonificacion()/100.00));
				_dp.setImpuesto(impuestoL);
				_dp.setImpuestoVen((long)(_dp.getPorcImpuesto() * (_dp.getSubtotal() - _dp.getBonificacionVen()/100.00)));
				_dp.setTotal(_dp.getSubtotal() - _dp.getBonificacion()+impuestoL);
				_dp.setTotalVen(_dp.getSubtotal() - _dp.getBonificacion()+impuestoL);
			}
		}
		
	}
	
	// BusinessLogic.DevolucionesBL
	public static int CalcularBonificacion(int CantidadDevolver, int CantidadOrdenada, int CantidadBonificada)
	{
		Double proporcion = Double.valueOf((double)CantidadBonificada) / Double.valueOf((double)CantidadOrdenada);
		Double CantBonif = CantidadDevolver * proporcion;
		int result;
		if (CantBonif == 0d)
		{
			result = 0;
		}
		else
		{
			Double decimalBonif = Double.valueOf("0." + CantBonif.toString().substring(CantBonif.toString().indexOf(".") + 1));
			String DecimalRedondeoBonif =NMApp.getContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("DecimalRedondeoBonif","0");
			if (decimalBonif >= Double.valueOf(DecimalRedondeoBonif))
			{
				CantBonif++;
			}
			result = Integer.valueOf(CantBonif.toString());
		}
		return result;
	}
	
	private void agregarProducto() 
	{
	
		if (cliente == null) 
		{
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"Seleccione primero el cliente de la Devolución.",
					"Seleccione primero el cliente de la Devolución.","")); 
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
		if( (!NMNetWork.CheckConnectionV2()) && UserSessionManager.HAS_ERROR)  
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
					devolucion=_dev;
					pedido=devolucion.getObjPedido();
					dev_prod=Arrays.asList(devolucion.getProductosDevueltos());
					initExpandableListView();
				}
				
			}
		});
		newFragment.show(ft, "dialogDocumentoDevolucion"); 
		if(isoffline=true)
			NMApp.getController()._notifyOutboxHandlers(0, 0, 0,"Dispositivo fuera de cobertura");
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
	
	public void dismiss(){
		if(pdialog!=null)
			pdialog.dismiss();				
		if(dlg!=null)
			dlg.dismiss();
	}
	
	
	public void hideProgress(){
		runOnUiThread(new Runnable() 
		{
			
			@Override
			public void run() {
				if(pdialog!=null)
					pdialog.dismiss();				
				if(dlg!=null)
					dlg.dismiss();
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) 
	{ 		
		dismiss();
		switch (msg.what) 
		{
			case ControllerProtocol.OBTENERVALORCATALOGO:				
				adapter_motdev = new CustomAdapter(getContext(),
						R.layout.spinner_rows, setListData(catalogos=(ArrayList<Catalogo>) msg.obj));
				cboxmotivodev.setAdapter(adapter_motdev);  
				break;
			case ControllerProtocol.NOTIFICATION_DIALOG:
				if (msg.obj instanceof String)
					showStatus(msg.obj.toString(),true);
				break;
			case ControllerProtocol.ERROR:
				/*AppDialog.showMessage(me, ((ErrorMessage) msg.obj).getTittle(),
						((ErrorMessage) msg.obj).getMessage(),
						DialogType.DIALOGO_ALERTA);*/
				if(msg.obj instanceof ErrorMessage)
					showStatus(((ErrorMessage) msg.obj).getMessage(), true);
				else if (msg.obj instanceof String)
					showStatus(((String) msg.obj), true);
				
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
	
	public static ArrayList<Bonificacion> parseListaBonificaciones(
			Producto prod, long idCatCliente) 
			{
		if (prod.getListaBonificaciones() == null)
			return null;
		if (prod.getListaBonificaciones() == "")
			return null;

		ArrayList<Bonificacion> abon = new ArrayList<Bonificacion>();
		String catCLiente = idCatCliente + "";
		String[] listaBonif = StringUtil.split(prod.getListaBonificaciones(),
				"|");
		for (int i = 0; i < listaBonif.length; i++)
		{
			if(!listaBonif[0].equals(""))
			{	
				String[] bonifProd = StringUtil.split(listaBonif[i], ",");
		
				if(bonifProd!=null && bonifProd.length!=0 && bonifProd[0]!="")
				if ((idCatCliente == 0) || (catCLiente.compareTo(bonifProd[1]) == 0)) {
					Bonificacion b = new Bonificacion();
					b.setObjBonificacionID(Long.parseLong(bonifProd[0]));
					b.setObjCategoriaClienteID(idCatCliente);
					b.setCantidad(Integer.parseInt(bonifProd[2]));
					b.setCantBonificacion(Integer.parseInt(bonifProd[3]));
					b.setCategoriaCliente(bonifProd[4]);
					abon.add(b);
				}
			}
			else
				break;
		}

		return abon;
	}

	/*
	 * Devuelve la bonificación de un producto dada la cantidad que se está
	 * ordenando y la categoría de cliente
	 */
	public static Bonificacion getBonificacion(Producto prod,
			long idCatCliente, int cantidad) {
		ArrayList<Bonificacion> bonificaciones = parseListaBonificaciones(prod,
				idCatCliente);
		if (bonificaciones == null)
			return null;

		Bonificacion bb = null;
		for (int i = bonificaciones.size() - 1; i >= 0; i--) {
			Bonificacion b = bonificaciones.get(i);
			if (cantidad >= b.getCantidad()) {
				bb = b; // Encontrada
				break; // Salir del ciclo
			}
		}

		// Si se encontró bonificación aplicada
		// Recalcular cantidad real a aplicar para la cantidad ordenada
		// Recordar traer valor de parámetro DecimalRedondeoBonif (por el
		// momento se usa 0.8)
		if (bb != null) {
			float decimalRedondeoBonif = 0.8F;
			float cb = bb.getCantBonificacion();
			float c = bb.getCantidad();
			float factor = cb / c;
			float cant = factor * cantidad;
			String sCant = String.valueOf(cant);
			String[] arrCant = StringUtil.split(sCant, ".");
			String sInt = arrCant[0];
			String sDec = "0.0";

			int cantReal = Integer.parseInt(sInt);
			if (arrCant.length > 1) {
				sDec = "0." + arrCant[1];
				float decimalPart = Float.parseFloat(sDec);
				if (decimalPart >= decimalRedondeoBonif)
					cantReal = cantReal + 1;
			}
			bb.setCantBonificacion(cantReal);
		}

		return bb;
	} 

	/*
	 * Devuelve el precio de un producto dada la cantidad que se está ordenando
	 */
	public static float getPrecioProducto(Producto prod, long idTipoPrecio,
			int cantidad) 
	{
		ArrayList<PrecioProducto> precios = parseListaPrecios(prod,idTipoPrecio); 
		PrecioProducto p =(precios!=null && precios.size()!=0)? precios.get(0):null ;
		if (precios.size() > 1) 
		{
			for (int i = 0; i < precios.size(); i++) 
			{
				p = precios.get(i);
				if ((cantidad >= p.getMinimo()) && (cantidad <= p.getMaximo()))
					break; // Salir del ciclo
			}
		}

		return (p!=null)?p.getPrecio():null;
	}
	
	public static ArrayList<PrecioProducto> parseListaPrecios(Producto prod,
			long idTipoPrecio) {
		ArrayList<PrecioProducto> pp = new ArrayList<PrecioProducto>();
		String tipoPrecio = idTipoPrecio + "";
		String[] listaPrecios = StringUtil.split(prod.getListaPrecios(), "|");
		for (int i = 0; i < listaPrecios.length; i++) {
			String[] precioProd = StringUtil.split(listaPrecios[i], ",");

			if ((idTipoPrecio == 0)
					|| (tipoPrecio.compareTo(precioProd[0]) == 0)) {
				PrecioProducto p = new PrecioProducto();
				p.setObjTipoPrecioID(idTipoPrecio);
				p.setMinimo(Integer.parseInt(precioProd[1]));
				p.setMaximo(Integer.parseInt(precioProd[2]));
				p.setPrecio(Float.parseFloat(precioProd[3]));
				p.setDescTipoPrecio(precioProd[4]);
				pp.add(p);
			}
		}

		return pp;
	}

	public static float getPorcImpuesto(Pedido pedido,long idproducto)
	{
		for(DetallePedido dtp:pedido.getDetalles())
			if(dtp.getObjProductoID()==idproducto)
				return dtp.getPorcImpuesto();
		return 0.f;
	}
	
}
