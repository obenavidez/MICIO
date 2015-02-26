package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.SOLICITAR_DESCUENTO;
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SEND_DATA_FROM_SERVER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.ActionType;
import com.panzyma.nm.auxiliar.Ammount;
import com.panzyma.nm.auxiliar.AmmountType;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.NumberUtil;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.auxiliar.VentasUtil;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.interfaces.Editable;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.model.FacND;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.DocumentoViewHolder;
import com.panzyma.nm.view.viewholder.PProductoViewHolder;
import com.panzyma.nm.viewdialog.AplicarDescuentoOcasional;
import com.panzyma.nm.viewdialog.AplicarDescuentoOcasional.RespuestaAlAplicarDescOca;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogFormasPago;
import com.panzyma.nm.viewdialog.DialogSolicitudDescuento;
import com.panzyma.nm.viewdialog.DialogFormasPago.OnFormaPagoButtonClickListener;
import com.panzyma.nm.viewdialog.DialogSeleccionTipoDocumento;
import com.panzyma.nm.viewdialog.DialogSeleccionTipoDocumento.Seleccionable;
import com.panzyma.nm.viewdialog.DialogoConfirmacion;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewdialog.DialogDocumentos;
import com.panzyma.nm.viewdialog.DialogDocumentos.OnDocumentoButtonClickListener;
import com.panzyma.nm.viewdialog.DialogSeleccionTipoDocumento.Documento;
import com.panzyma.nm.viewdialog.DialogoConfirmacion.Pagable;
import com.panzyma.nm.viewdialog.EditFormaPago;
import com.panzyma.nm.viewdialog.EditFormaPago.EditDialogListener;
import com.panzyma.nordismobile.R;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@InvokeBridge(bridgeName = "BReciboM")
@SuppressLint("ShowToast")
@SuppressWarnings({ "unused", "rawtypes", "deprecation", "unchecked" })
public class ViewReciboEdit extends ActionBarActivity implements Handler.Callback, Editable ,EditDialogListener
{
	private static CustomDialog dlg;
	private EditText tbxFecha;
	private EditText tbxNumReferencia;
	private EditText tbxNumRecibo;
	private EditText tbxNombreDelCliente;
	private TextView tbxNotas;
	private TextView txtTotalAbonadoFacturas;
	private TextView txtTotalAbonadoND;
	private TextView txtTotalAbonadoNC;
	private TextView txtSubTotal;
	private TextView txtTotal;
	private TextView txtTotalRetencion;
	private TextView txtTotalDescuento;
	private TextView txtmonto;
	private float totalRecibo = 0.00f;
	private float totalFacturas = 0.00f;
	private float totalNotasCredito = 0.00f;
	private float totalInteres = 0.00f;
	private float subTotal = 0.00f;
	private float totalDescuento = 0.00f;
	private float totalRetenido = 0.00f;
	private float totalOtrasDeducciones = 0.00f;
	private float totalNotasDebito = 0.00f;
	private float totalDescuentoOcasional = 0.00f;
	private float totalDescuentoPromocion = 0.00f;
	private float totalDescuentoProntoPago = 0.00f;
	private float totalImpuestoProporcional = 0.00f;
	private float totalImpuestoExonerado = 0.00f;
	private float totalExento = 0.00f;
	private float totalAutoriazadoDGI = 0.00f;

	private float porcentajeDescuentoOcasional = 0.00f;

	private View gridDetalleRecibo;
	private ListView item_document;
	private TextView gridheader;

	private Controller controller;
	private GenericAdapter adapter;
	private ProgressDialog pd;
	private Button Menu;
	private QuickAction quickAction;
	private QuickAction quickAction2;
	private int positioncache = -1;
	private Display display;
	private static final String TAG = ViewReciboEdit.class.getSimpleName();
	private static final int ID_SELECCIONAR_CLIENTE = 0;
	private static final int ID_AGREGAR_DOCUMENTOS = 1;
	private static final int ID_AGREGAR_PAGOS = 2;
	private static final int ID_EDITAR_PAGOS = 3;
	private static final int ID_EDITAR_NOTAS = 4;
	private static final int ID_PAGAR_TODO = 5;
	private static final int ID_PAGAR_MONTO = 6;
	private static final int ID_EDITAR_DESCUENTO = 7;
	private static final int ID_SALVAR_RECIBO = 8;
	private static final int ID_ENVIAR_RECIBO = 9;
	private static final int ID_SOLICITAR_DESCUENTO_OCASIONAL = 10;
	private static final int ID_APLICAR_DESCUENTO_OCASIONAL = 11;
	private static final int ID_CERRAR = 13;
	private static final int ID_IMPRIMIR_COMPROBANTE = 12;
	
	private static final int TIME_TO_VIEW_MESSAGE = 3000;
	public static final String FORMA_PAGO_IN_EDITION = "edit";
	public static final String OBJECT_RECIBO = "recibo";
	public static final String OBJECT_TO_EDIT = "formaPago";
	private boolean salvado = false;
	//
	private static final int ID_EDITAR_DOCUMENTO = 0;
	private static final int ID_ELIMINAR_DOCUMENTO = 1;
	private static final int VER_DETALLE_DOCUMENTO = 2;
	
	
	private ViewReciboEdit me;
	private Cliente cliente;
	private ReciboColector recibo = null;
	private Context contexto;
	private BReciboM brm;
	private Handler handler;
	private Integer reciboId;
	private com.panzyma.nm.serviceproxy.Documento documento_selected;
	private boolean onEdit = false;
	private boolean onNew;
	private static Object lock = new Object();
	private boolean isReimpresion = false;
	private List<Factura> facturasRecibo;
	List<CCNotaDebito> notasDebitoRecibo;
	List<CCNotaCredito> notasCreditoRecibo;
	private List<com.panzyma.nm.serviceproxy.Documento> documents;
	DrawerLayout drawerLayout;
	ListView drawerList;
	ActionBarDrawerToggle drawerToggle;
	String[] opcionesMenu;
	CharSequence tituloSeccion;
	CharSequence tituloApp;
	View _view;

	boolean imprimir = false;
	boolean pagarOnLine = false;

	public List<Factura> getFacturasRecibo() 
	{
		return facturasRecibo;
	}

	public List<CCNotaDebito> getNotasDebitoRecibo() {
		return notasDebitoRecibo;
	}

	public List<CCNotaCredito> getNotasCreditoRecibo() {
		return notasCreditoRecibo;
	}

	public Integer getReciboID() 
	{
		return reciboId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recibo_edit);

		try 
		{

			Bundle bundle = getIntent().getExtras();
			// OBTENER EL ID DEL RECIBO
			reciboId = (Integer) bundle.get(ViewRecibo.RECIBO_ID);
			SessionManager.setContext(this);UserSessionManager.setContext(this);
			me = this;

			if (getIntent().hasExtra("cliente")) {
				long IdCliente = getIntent().getLongExtra("cliente", 0);
				cliente = Ventas.getClienteBySucursalID(IdCliente,me.getContentResolver());
			}
			
			if(facturasRecibo==null)
				facturasRecibo = new ArrayList<Factura>();
			if(notasDebitoRecibo==null)
				notasDebitoRecibo = new ArrayList<CCNotaDebito>();
			if(notasCreditoRecibo==null)
				notasCreditoRecibo = new ArrayList<CCNotaCredito>();
			
			if (savedInstanceState != null) {
				Parcelable[] docs = savedInstanceState.getParcelableArray("documentos");
				documents = new ArrayList<com.panzyma.nm.serviceproxy.Documento>((Collection<? extends com.panzyma.nm.serviceproxy.Documento>) Arrays.asList(docs));
				recibo = savedInstanceState.getParcelable("recibo");
				recibo.setOldData(recibo);
			} 
			else {
				documents = new ArrayList<com.panzyma.nm.serviceproxy.Documento>();
			}

			if (reciboId != 0) {
				onEdit = true;
				// OBTENER EL RECIBO DESDE LOCALHOST
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putInt("idrecibo", reciboId);
				msg.setData(b);
				msg.what = ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
				NMApp.getController().setView(this);
				NMApp.getController().getInboxHandler().sendMessage(msg);
			}

			contexto = this.getApplicationContext();

			WindowManager wm = (WindowManager) contexto
					.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			initComponent();

		} 
		catch (Exception e) {
			NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage("Error cargando Recibo", e.getMessage(), e
							.getMessage()));
		}

	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		_view = super.onCreateView(name, context, attrs);
		return _view;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		Parcelable[] objects = new Parcelable[documents.size()];
		documents.toArray(objects);
		savedInstanceState.putParcelableArray("documentos", objects);
		savedInstanceState.putParcelable("recibo", recibo);
		savedInstanceState.putParcelable("cliente", cliente);
		// etc.
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		Parcelable[] objects = savedInstanceState.getParcelableArray("documentos");
		documents = new ArrayList<com.panzyma.nm.serviceproxy.Documento>((Collection<? extends com.panzyma.nm.serviceproxy.Documento>) Arrays.asList(objects));
		cliente = savedInstanceState.getParcelable("cliente");
		gridheader.setText(String.format("DOCUMENTOS A PAGAR (%s)",documents.size()));
		recibo = savedInstanceState.getParcelable("recibo");
		agregarDocumentosAlDetalleDeRecibo();
		// setList();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		drawerToggle.onConfigurationChanged(newConfig);
//	}
	
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
	
	public void CreateMenu() 
	{
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(
				R.array.reciboeditoptions);
		String[] copy=new String[opcionesMenu.length];
		if (recibo != null && recibo.getCodEstado().equals("PAGADO")) 
			
		{	if(opcionesMenu.length!=0 && opcionesMenu.length>2)
				copy[0]=opcionesMenu[opcionesMenu.length-2];
				copy[1]=opcionesMenu[opcionesMenu.length-1];
		}
		else
			copy=opcionesMenu.clone();
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				copy));
		
		 
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
				Controller controller = NMApp.getController();
				switch (position) 
				{   
				case ID_SELECCIONAR_CLIENTE:
					seleccionarCliente();
					break;
				case ID_AGREGAR_DOCUMENTOS:
					agregarDocumentosPendientesCliente();
					break;
				case ID_AGREGAR_PAGOS:
					agregarPago();
					break;
				case ID_EDITAR_PAGOS:
					editarPagos();
					break;
				case ID_EDITAR_DESCUENTO:
					editarDescuento();
					break;
				case ID_PAGAR_TODO:
					if (cliente == null) {
						AppDialog
								.showMessage(
										me,
										"Información",
										"Por favor seleccione un cliente.",
										DialogType.DIALOGO_ALERTA);
						return;
					}
					PagarTodo();
					break;
				case ID_PAGAR_MONTO:
					if (cliente == null) {
						AppDialog
								.showMessage(
										me,
										"Información",
										"Por favor seleccione un cliente.",
										DialogType.DIALOGO_ALERTA);
						return;
					}
					PagarMonto();

					break;
				case ID_SOLICITAR_DESCUENTO_OCASIONAL:
					solicitardescuento();
					break;
				case ID_APLICAR_DESCUENTO_OCASIONAL:
					aplicardescuento();
					break;
				case ID_SALVAR_RECIBO:
					guardarRecibo();
					salvado = true;
					break;
				case ID_ENVIAR_RECIBO:
					enviarRecibo();
					break;
				case ID_IMPRIMIR_COMPROBANTE:
					enviarImprimirRecibo(recibo);
					break;
				case ID_CERRAR:
						finish();
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
				ActivityCompat.invalidateOptionsMenu(ViewReciboEdit.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewReciboEdit.this);

			}
		};

		// establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	protected void onResume() {
		NMApp.getController().setView(this);
		super.onResume();
	}

	private void initComponent() 
	{ 
		gridDetalleRecibo = findViewById(R.id.pddgrilla);
		item_document = (ListView) (gridDetalleRecibo)
				.findViewById(R.id.data_items);
		gridheader = (TextView) gridDetalleRecibo.findViewById(R.id.header);
		gridheader.setText("DOCUMENTOS A PAGAR (0)");
		tbxFecha = (EditText) findViewById(R.id.pddetextv_detalle_fecha);
		tbxNumReferencia = (EditText) findViewById(R.id.pdddetextv_detalle_numref);
		tbxNumRecibo = (EditText) findViewById(R.id.pddtextv_detallenumero);
		tbxNombreDelCliente = (EditText) findViewById(R.id.pddtextv_detallecliente);
		tbxNotas = (EditText) findViewById(R.id.pddtextv_detalle_notas);
		// Totales Documentos
		txtTotalAbonadoFacturas = (TextView) findViewById(R.id.txtTotalFacturas);
		txtTotalAbonadoND = (TextView) findViewById(R.id.txtTotalNotasDebito);
		txtTotalAbonadoNC = (TextView) findViewById(R.id.txtTotalNotaCredito);
		txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);
		txtTotalRetencion = (TextView) findViewById(R.id.txtTotalRetencion);
		txtTotalDescuento = (TextView) findViewById(R.id.txtTotalDescuento);
		txtTotal = (TextView) findViewById(R.id.txtTotal);
		txtmonto = (TextView)findViewById(R.id.txtmontopagado);
		if (recibo != null && !recibo.getCodEstado().equals("REGISTRADO") && !recibo.getCodEstado().equals("APROBADO")) 
		{
			item_document.setEnabled(false);
			tbxFecha.setEnabled(false);
			tbxNumReferencia.setEnabled(false);
			tbxNumRecibo.setEnabled(false); 
			tbxNombreDelCliente.setEnabled(false);
			tbxNotas.setEnabled(false);
			txtTotalAbonadoFacturas.setEnabled(false);
			txtTotalAbonadoND.setEnabled(false);	
			txtTotalAbonadoNC.setEnabled(false);	
			txtSubTotal.setEnabled(false);	
			txtTotalDescuento.setEnabled(false);	
			txtTotal.setEnabled(false);				
		} 
		
		item_document.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if ((parent.getChildAt(positioncache)) != null)
					(parent.getChildAt(positioncache))
							.setBackgroundResource(android.R.color.transparent);
				positioncache = position;
				documento_selected = (com.panzyma.nm.serviceproxy.Documento) adapter
						.getItem(position);
				adapter.setSelectedPosition(position);
				view.setBackgroundDrawable(parent.getResources().getDrawable(
						R.drawable.action_item_selected));

			}

		});
		item_document.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				if ((parent.getChildAt(positioncache)) != null)
					(parent.getChildAt(positioncache))
							.setBackgroundResource(android.R.color.transparent);
				positioncache = position;
				documento_selected = (com.panzyma.nm.serviceproxy.Documento) adapter
						.getItem(position);
				adapter.setSelectedPosition(position);
				view.setBackgroundDrawable(parent.getResources().getDrawable(
						R.drawable.action_item_selected));
				showMenu(view);

				return true;
			}
		});

		loadData();
		initMenu();
		CreateMenu();
				
		if (getIntent().hasExtra("cliente")) {
			SetCliente();
		}
	}

	private void addWithoutRepeating(Object obj) {
		if (documents == null)
			return;
		boolean repetido = false;
		for (com.panzyma.nm.serviceproxy.Documento doc : documents) {
			if (obj instanceof ReciboDetFactura
					&& doc.getObject() instanceof ReciboDetFactura) {
				ReciboDetFactura fac = (ReciboDetFactura) obj;
				ReciboDetFactura fac2 = (ReciboDetFactura) doc.getObject();
				if (fac2.getObjFacturaID() == fac.getObjFacturaID()) {
					repetido = true;
					break;
				}
			} else if (obj instanceof ReciboDetND
					&& doc.getObject() instanceof ReciboDetND) {
				ReciboDetND nd = (ReciboDetND) obj;
				ReciboDetND nd2 = (ReciboDetND) doc.getObject();
				if (nd.getObjNotaDebitoID() == nd2.getObjNotaDebitoID()) {
					repetido = true;
					break;
				}
			} else if (obj instanceof ReciboDetNC
					&& doc.getObject() instanceof ReciboDetNC) {
				ReciboDetNC nc = (ReciboDetNC) obj;
				ReciboDetNC nc2 = (ReciboDetNC) doc.getObject();
				if (nc.getObjNotaCreditoID() == nc2.getObjNotaCreditoID()) {
					repetido = true;
					break;
				}
			}
		}
		if (!repetido) {
			documents.add((com.panzyma.nm.serviceproxy.Documento) obj);
		}

	}

	private void loadData(boolean... _onEdit) {

		long date = DateUtil.dt2i(Calendar.getInstance().getTime());
		salvado = false;
	
		
		if (_onEdit != null && _onEdit.length != 0)
			onEdit = true;
		if (recibo == null || !onEdit) 
		{
			
			if (recibo == null)
			{
			// NUEVO RECIBO
				recibo = new ReciboColector();
				recibo.setId(0);
				
				if (!getIntent().hasExtra("cliente")) {
					cliente = null;	
					recibo.setNombreCliente("");
				}
				recibo.setCodEstado("REGISTRADO");
				recibo.setReferencia(0);
				recibo.setFecha(date);
				recibo.setExento(false);
				recibo.setAutorizacionDGI("");
				recibo.setNotas("");
				recibo.setTotalFacturas(0.00f);
				recibo.setTotalND(0.00f);
				recibo.setTotalNC(0.00f);
				recibo.setSubTotal(0.00f);   
				recibo.setTotalDesc(0.00F);
				recibo.setTotalImpuestoExonerado(0.00F);
				recibo.setTotalRetenido(0.00F);
				recibo.setTotalOtrasDed(0.00F);
				recibo.setTotalInteres(0.00F);
				recibo.setTotalRecibo(0.00F);
				tbxNumRecibo.setText("");
				
				notasDebitoRecibo.clear();
				notasCreditoRecibo.clear();
				facturasRecibo.clear();
				documents.clear();
				recibo.getFormasPagoRecibo().clear(); 
				
				recibo.getFacturasRecibo().clear();
				recibo.getNotasDebitoRecibo().clear();
				recibo.getNotasCreditoRecibo().clear();
			}
		} else {

			cliente = recibo.getCliente();

			// EDICION DE RECIBO
			if ("REGISTRADO".equals(recibo.getDescEstado())) {
				recibo.setFecha(date);
			}
			// AGREGAR LAS FACTURAS DEL RECIBO A LA GRILLA
			for (ReciboDetFactura factura : recibo.getFacturasRecibo()) {
				if (cliente.getFacturasPendientes() != null)
					for (Factura fac : cliente.getFacturasPendientes()) {
						if (fac.getId() == factura.getObjFacturaID()) {
							// fac.setAbonado(factura.getMonto());
							factura.setTotalFactura(fac.getTotalFacturado());
							getFacturasRecibo().add(fac);
						}
					}
				addWithoutRepeating(factura);
			}
			// AGREGAR LAS NOTAS DE DEBITO DEL RECIBO A LA GRILLA
			for (ReciboDetND nd : recibo.getNotasDebitoRecibo()) {
				if (cliente.getNotasDebitoPendientes() != null)
					for (CCNotaDebito ndp : cliente.getNotasDebitoPendientes()) {
						if (nd.getObjNotaDebitoID() == ndp.getId()) {
							getNotasDebitoRecibo().add(ndp);
						}
					}
				addWithoutRepeating(nd);
			}
			// AGREGAR LAS NOTAS DE CREDITO DEL RECIBO A LA GRILLA
			if (recibo.getNotasCreditoRecibo() != null
					&& recibo.getNotasCreditoRecibo().size() != 0)
				for (ReciboDetNC nc : recibo.getNotasCreditoRecibo()) {
					if (cliente.getNotasCreditoPendientes() != null)
						for (CCNotaCredito ncp : cliente
								.getNotasCreditoPendientes()) {
							if (nc.getObjNotaCreditoID() == ncp.getId()) {
								getNotasCreditoRecibo().add(ncp);
							}
						}
					addWithoutRepeating(nc);
				}
			adapter = null;
			agregarDocumentosAlDetalleDeRecibo();
		}
		// ESTABLECER LOS VALORES EN LA VISTA DE EDICION DE RECIBO
		tbxNumRecibo.setText("" + recibo.getNumero());
		tbxNotas.setText("" + recibo.getNotas());
		tbxNumReferencia.setText(""
				+ VentasUtil.getNumeroPedido(me, recibo.getReferencia()));
		tbxNombreDelCliente.setText("" + recibo.getNombreCliente());
		tbxFecha.setText("" + DateUtil.idateToStrYY(recibo.getFecha()));
		// ESTABLECER LOS TOTALES
		txtTotalAbonadoFacturas.setText("" + recibo.getTotalFacturas());
		txtTotalAbonadoND.setText("" + recibo.getTotalND());
		txtTotalAbonadoNC.setText("" + recibo.getTotalNC());
		txtSubTotal.setText("" + recibo.getSubTotal());
		txtTotalRetencion.setText(""+ recibo.getTotalRetenido());
		txtTotalDescuento.setText("" + recibo.getTotalDesc());
		txtTotal.setText("" + recibo.getTotalRecibo());
		txtmonto.setText("" +recibo.getFormasPagoMonto());
		CalculaTotales();
		actualizaTotales();
	}

	private void initMenu() 
	{
		
		if (recibo != null && !recibo.getCodEstado().equals("REGISTRADO") && !recibo.getCodEstado().equals("APROBADO")) 
		{
			quickAction.addActionItem(new ActionItem(ID_IMPRIMIR_COMPROBANTE,
					"Imprimir Comprobante"));
			quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));
		} 
		else 
		{ 
			quickAction = new QuickAction(this, QuickAction.VERTICAL, 1);
			quickAction.addActionItem(new ActionItem(ID_SELECCIONAR_CLIENTE,
					"Seleccionar Cliente"));
			quickAction.addActionItem(new ActionItem(ID_AGREGAR_DOCUMENTOS,
					"Agregar Documentos"));
			quickAction.addActionItem(new ActionItem(ID_AGREGAR_PAGOS,
					"Agregar Pagos"));
			quickAction.addActionItem(new ActionItem(ID_EDITAR_PAGOS,
					"Editar Pagos"));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_EDITAR_NOTAS,
					"Editar Notas"));
			quickAction.addActionItem(new ActionItem(ID_PAGAR_TODO,
					"Pagar Todo (Facturas + ND)"));
			quickAction.addActionItem(null);
			quickAction
					.addActionItem(new ActionItem(ID_PAGAR_MONTO, "Pagar Monto"));
			quickAction.addActionItem(new ActionItem(ID_EDITAR_DESCUENTO,
					"Editar Descuento"));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_SALVAR_RECIBO,
					"Guardar Recibo"));
			quickAction.addActionItem(new ActionItem(ID_ENVIAR_RECIBO,
					"Enviar Recibo"));
			quickAction.addActionItem(new ActionItem(
					ID_SOLICITAR_DESCUENTO_OCASIONAL,
					"Solicitar Descuento Ocasional"));
			quickAction.addActionItem(new ActionItem(
					ID_APLICAR_DESCUENTO_OCASIONAL, "Aplicar Descuento Ocasional"));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));
		}
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
				{
					@Override
					public void onItemClick(QuickAction source, final int pos,
							int actionId) {

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ActionItem actionItem = quickAction
										.getActionItem(pos);
								switch (actionItem.getActionId()) {
								case ID_SELECCIONAR_CLIENTE:
									seleccionarCliente();
									break;
								case ID_AGREGAR_DOCUMENTOS:
									agregarDocumentosPendientesCliente();
									break;
								case ID_AGREGAR_PAGOS:
									agregarPago();
									break;
								case ID_EDITAR_PAGOS:
									editarPagos();
									break;
								case ID_EDITAR_DESCUENTO:
									editarDescuento();
									break;
								case ID_PAGAR_TODO:
									if (cliente == null) {
										AppDialog
												.showMessage(
														me,
														"Información",
														"Por favor seleccione un cliente.",
														DialogType.DIALOGO_ALERTA);
										return;
									}
									PagarTodo();
									break;
								case ID_PAGAR_MONTO:
									if (cliente == null) {
										AppDialog
												.showMessage(
														me,
														"Información",
														"Por favor seleccione un cliente.",
														DialogType.DIALOGO_ALERTA);
										return;
									}
									PagarMonto();

									break;
								case ID_SOLICITAR_DESCUENTO_OCASIONAL:
									solicitardescuento();
									break;
								case ID_APLICAR_DESCUENTO_OCASIONAL:
									aplicardescuento();
									break;
								case ID_SALVAR_RECIBO:
									guardarRecibo();
									salvado = true;
									break;
								case ID_ENVIAR_RECIBO:
									enviarRecibo();
									break;
								case ID_CERRAR:
									// finalizarvidad();
									break;
								}
							}
						});
					}

				});
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
				quickAction.dismiss();
			}
		});

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Menu = (Button) gridDetalleRecibo.findViewById(R.id.btnmenu);
			quickAction.show(Menu, display, true);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			FINISH_ACTIVITY();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (dlg != null)
			dlg.dismiss();
		switch (msg.what) {
		case C_DATA:
			recibo = (ReciboColector) msg.obj;
			recibo.setOldData(recibo);
			loadData();
			break;
		case ControllerProtocol.ID_REQUEST_ENVIARPEDIDO:
			if (msg.obj != null) {
				recibo = ((ReciboColector) msg.obj);
				recibo.setOldData(recibo);
				cliente = recibo.getCliente();
				actualizarOnUINumRef(recibo);
				if (msg.arg1 == 1)
					if(recibo != null  && "PAGADO".equals(recibo.getCodEstado()) )
						enviarImprimirRecibo(recibo);
			}
			break;
		case ControllerProtocol.NOTIFICATION:
			if (ControllerProtocol.SAVE_DATA_FROM_LOCALHOST == msg.arg1) {
				recibo = (ReciboColector) msg.obj;
				recibo.setOldData(recibo);
				recibo.getOldData();
				actualizarOnUINumRef(recibo);
				salvado = true;
				showStatus("El Recibo fue registrado con exito", true);
			} else if (msg.obj instanceof String)
				showStatus(msg.obj.toString(), true);
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			if (msg.obj instanceof String)
				showStatus(msg.obj.toString());
			break;
		case ControllerProtocol.ERROR:
			AppDialog.showMessage(me, ((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			break;

		case ControllerProtocol.SALVARRECIBOANTESDESALIR:

			int requescode = 0;
			if (pd != null)
				pd.dismiss();
			Log.d(TAG, "Activity quitting");

			Intent intent = null;

			if (recibo != null
					&& (recibo.getFacturasRecibo().size() > 0
							|| recibo.getNotasCreditoRecibo().size() > 0 || recibo
							.getNotasDebitoRecibo().size() > 0)
					&& recibo.getReferencia() != 0) {
				intent = new Intent();
				Bundle b = new Bundle();
				b.putParcelable("recibo", recibo);
				intent.putExtras(b);
			}
			if (onEdit)
				requescode = getIntent().getIntExtra("requestcode", 0);
			setResult(requescode, intent);
			finish();

			if (dlg != null)
				dlg.dismiss();

			Log.d(TAG, "Activity quitting");

			break;
		}
		return false;
	}

	private  void enviarImprimirRecibo(final ReciboColector recibo) 
	{
		
		if (recibo != null && !recibo.getCodEstado().equals("PAGADO")) 
		{
			showStatus("No se puede imprimir recibos en estado "+ recibo.getCodEstado(), true);
			return;
		}
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AppDialog.showMessage(me, "", "Desea Imprimir el Recibo?",
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

	public void actualizarOnUINumRef(final ReciboColector r) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				tbxNumReferencia.setText(NumberUtil.getFormatoNumero(
						r.getReferencia(), me));
				tbxNumRecibo.setText(NumberUtil.getFormatoNumero(r.getNumero(),
						me));
				salvado = true;
			}
		});
	}

	private void seleccionarCliente() 
	{ 		
		  if (cliente != null && documents != null && documents.size()!=0) 
		  {
				AppDialog.showMessage(me, "", "Si cambia el cliente se eliminarán los detalles del recibo.\n\n¿Desea continuar?",
						AppDialog.DialogType.DIALOGO_CONFIRMACION,
						new AppDialog.OnButtonClickListener() 
				{
							@Override
							public void onButtonClick(AlertDialog _dialog,
									int actionId) 
							{
	
								if (AppDialog.OK_BUTTOM == actionId) 
								{
									recibo=null;  
									initComponent();
									showClientDialog(_dialog);
								}
								else
									_dialog.dismiss();
							}
				});
	
	      }	
		  else 
			  showClientDialog();
		
		
		
	}
	
	private void SetCliente ()
	{
		String nombreSucursal = "";		
		if (cliente.getNombreCliente().indexOf("/") != -1) {
			String[] nomClie = StringUtil
					.split(cliente.getNombreCliente(), "/");
			tbxNombreDelCliente.setText(nomClie[0]);
			recibo.setNombreCliente(nomClie[0]);
		} else {
			recibo.setNombreCliente(cliente.getNombreCliente());
			tbxNombreDelCliente.setText(cliente.getNombreCliente());
		}
		// establecer valores de recibo
		recibo.setObjClienteID(cliente.getIdCliente());
		recibo.setObjSucursalID(cliente.getIdSucursal());
		
	}
	
	private void aplicardescuento() {

		boolean aplicandoDescOca = ((recibo.getClaveAutorizaDescOca() != null) && (recibo
				.getClaveAutorizaDescOca().length() > 0))
				|| (recibo.getPorcDescOcaColector() > 0);
		if (aplicandoDescOca)
			DesaplicarDescuentoOcasional();
		else
			aplicarDescuentoOcasional();

	}

	private void aplicarDescuentoOcasional() {
		if (cliente == null)
			return;
		if (!Cobro.validaAplicDescOca(me.getContext(), recibo)) {
			AppDialog
					.showMessage(
							me,
							"Alerta",
							"Debe cancelar al menos una factura vencida para aplicar descuento ocasional.",
							DialogType.DIALOGO_ALERTA);
			return;
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		AplicarDescuentoOcasional newFragment = AplicarDescuentoOcasional
				.newInstance(recibo);
		newFragment
				.escucharRespuestaAplicarDescOca(new RespuestaAlAplicarDescOca() {

					@Override
					public void onButtonClick(Float percentcollector,
							String clave) {
						recibo.setClaveAutorizaDescOca(clave);
						recibo.setPorcDescOcaColector(percentcollector);
						for(ReciboDetFactura rec: recibo.getFacturasRecibo()){
							if(!Cobro.validaAplicDescOca(NMApp.getContext(), recibo, rec.getId())){
								//SI LA FACTURA NO APLICA PARA DESCUENTO OCASIONAL
								rec.setPorcDescOcasional(0.00F);
							}							
						}
						Cobro.calcularDetFacturasRecibo(NMApp.getContext(), recibo, cliente, true);
						CalculaTotales();
						actualizaTotales();
						recibo.getFormasPagoRecibo().clear();						
					}
				});

		newFragment.show(ft, "dialog");
	}

	private void DesaplicarDescuentoOcasional() {

	}

	private void solicitardescuento() {
		
		// Si se está fuera de covertura, salir
//		if (!SessionManager.isPhoneConnected()) {
//			//showStatus("La operación no puede ser realizada ya que está fuera de cobertura.", true);			
//			return;
//		}
		if (!Cobro.validaAplicDescOca(me.getContext(), recibo)) {
			AppDialog
					.showMessage(
							me,
							"Alerta",
							"Debe cancelar al menos una factura vencida para aplicar descuento ocasional.",
							DialogType.DIALOGO_ALERTA);
			return;
		}
		if (cliente == null) {
			AppDialog.showMessage(me, "Alerta",
					"Por favor seleccione un cliente.",
					DialogType.DIALOGO_ALERTA);
			return;
		}
		if (recibo.getReferencia() == 0) {
			AppDialog.showMessage(me, "",
					"Debe guardar primero el recibo localmente.",
					DialogType.DIALOGO_ALERTA);
			return;
		}
		 
			
		DialogSolicitudDescuento sd=new DialogSolicitudDescuento(this, getFacturasRecibo(), recibo);
		sd.setOnDialogSDButtonClickListener(new DialogSolicitudDescuento.OnButtonClickListener() {
			
			@Override
			public void onButtonClick(String notasolicituddescuento) 
			{ 
 
				if (notasolicituddescuento == "")
					return; 
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putParcelable("recibo", recibo);
				b.putString("notas", notasolicituddescuento);
				msg.setData(b);
				msg.what = SOLICITAR_DESCUENTO;
				NMApp.getController().getInboxHandler()
						.sendMessage(msg);
			}
		});
		Window window =sd.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 10, display.getHeight() - 50);
		sd.show();
	}
	
//	private void solicitardescuento() {
//		// Si se está fuera de covertura, salir
//		if (!SessionManager.isPhoneConnected()) {
//			//showStatus("La operación no puede ser realizada ya que está fuera de cobertura.", true);			
//			return;
//		}
//		if (!Cobro.validaAplicDescOca(me.getContext(), recibo)) {
//			AppDialog
//					.showMessage(
//							me,
//							"Alerta",
//							"Debe cancelar al menos una factura vencida para aplicar descuento ocasional.",
//							DialogType.DIALOGO_ALERTA);
//			return;
//		}
//		if (cliente == null) {
//			AppDialog.showMessage(me, "Alerta",
//					"Por favor seleccione un cliente.",
//					DialogType.DIALOGO_ALERTA);
//			return;
//		}
//		if (recibo.getReferencia() == 0) {
//			AppDialog.showMessage(me, "",
//					"Debe guardar primero el recibo localmente.",
//					DialogType.DIALOGO_ALERTA);
//			return;
//		}
//		AppDialog.showMessage(me, "Enviar solicitud de descuento ocasional",
//				"", DialogType.DIALOGO_INPUT,
//				new AppDialog.OnButtonClickListener() {
//					@Override
//					public void onButtonClick(AlertDialog alert, int actionId) {
//						if (actionId == AppDialog.OK_BUTTOM) {
//							try {
//								String nota = "";
//								nota = ((TextView) alert
//										.findViewById(R.id.txtpayamount))
//										.getText().toString();
//								if (nota == "")
//									return;
//								// NMApp.getController().setEntities(this,getBridge()==null?new
//								// BReciboM():getBridge());
//								// NMApp.getController().addOutboxHandler((getHandler()==null)?new
//								// Handler(me):getHandler());
//								Message msg = new Message();
//								Bundle b = new Bundle();
//								b.putParcelable("recibo", recibo);
//								b.putString("notas", nota);
//								msg.setData(b);
//								msg.what = SOLICITAR_DESCUENTO;
//								NMApp.getController().getInboxHandler()
//										.sendMessage(msg);
//							} catch (Exception e) {
//								NMApp.getController()
//										.notifyOutboxHandlers(
//												ControllerProtocol.ERROR,
//												0,
//												0,
//												new ErrorMessage(
//														"Error al solicitar descuento",
//														e.getMessage(), e
//																.getMessage()));
//							}
//						}
//					}
//				});
//	}

	private void guardarRecibo(int... arg) {

		// <<<<<<< HEAD
		// //
		// recibo.setNumero(Integer.parseInt((tbxNumRecibo.getText().toString()
		// // .trim().equals("") ? "0" : tbxNumRecibo.getText().toString())));
		// //
		// // if(recibo.getId() != 0)
		// // recibo.setReferencia(Integer.parseInt((tbxNumReferencia.getText()
		// // .toString().trim().equals("")) ? "0" : tbxNumReferencia
		// // .getText().toString()));
		// =======
		// recibo.setNumero(Integer.parseInt((tbxNumRecibo.getText().toString()
		// .trim().equals("") ? "0" : tbxNumRecibo.getText().toString())));
		//
		//
		// recibo.setReferencia(Integer.valueOf(recibo.getId()+""));
		//
		// if(recibo.getId() != 0)
		// recibo.setReferencia(Integer.parseInt((tbxNumReferencia.getText()
		// .toString().trim().equals("")) ? "0" : tbxNumReferencia
		// .getText().toString()));
		// >>>>>>> origin/master

		// if(bs != null && bs.length == 0)
		// recibo.setReferencia(Integer.parseInt((tbxNumReferencia.getText()
		// .toString().trim().equals("")) ? "0" : tbxNumReferencia
		// .getText().toString()));

		recibo.setNotas((tbxNotas.getText().toString().trim().equals("") ? "0"
				: tbxNotas.getText().toString()));
		//
		if (valido()) {

			CalculaTotales();
			// colector
			recibo.setObjColectorID(10);
			recibo.setAplicaDescOca(false);
			recibo.setClaveAutorizaDescOca("");
			recibo.setPorcDescOcaColector(this.porcentajeDescuentoOcasional);
			// estado
			recibo.setObjEstadoID(100);
			recibo.setCodEstado("REGISTRADO");
			recibo.setDescEstado("Registrado");

			// LIMPIAR LOS DOCUMENTOS DEL RECIBO
			recibo.getFacturasRecibo().clear();
			recibo.getNotasDebitoRecibo().clear();
			recibo.getNotasCreditoRecibo().clear();

			// AGREGAR LOS DOCUMENTOS DE LA GRILLA AL RECIBO
			for (com.panzyma.nm.serviceproxy.Documento doc : documents) {

				if (doc.getTipo().equals("Factura")) {
					ReciboDetFactura detalleFactura = (ReciboDetFactura) doc
							.getObject();
					// Agregar la factura al detalle del recibo
					recibo.getFacturasRecibo().add(detalleFactura);
				} else if (doc.getTipo().equals("Nota Débito")) {
					ReciboDetND notaDebito = (ReciboDetND) doc.getObject();
					// Agregar la nota débito al detalle del recibo
					recibo.getNotasDebitoRecibo().add(notaDebito);
				} else {
					ReciboDetNC notaCredito = (ReciboDetNC) doc.getObject();
					// Agregar la nota débito al detalle del recibo
					recibo.getNotasCreditoRecibo().add(notaCredito);
				}

			}

			try

			{
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putParcelable("recibo", recibo);
				b.putParcelableArray("facturasToUpdate", getArrayOfFacturas());
				b.putParcelableArray("notasDebitoToUpdate",
						getArrayOfNotasDebito());
				b.putParcelableArray("notasCreditoToUpdate",
						getArrayOfNotasCredito());
				msg.setData(b);
				msg.what = arg.length != 0 ? arg[0] : SAVE_DATA_FROM_LOCALHOST;
				NMApp.getController().getInboxHandler().sendMessage(msg);
			} catch (Exception e) {
				NMApp.getController().notifyOutboxHandlers(
						ControllerProtocol.ERROR,
						0,
						0,
						new ErrorMessage("Error al guardar el recibo", e
								.getMessage(), e.getMessage()));
			}
		}

	}

	private Factura[] getArrayOfFacturas() {
		Factura[] facturas = new Factura[facturasRecibo.size()];
		for (int i = 0; (facturasRecibo != null && i < facturasRecibo.size()); i++) {
			facturas[i] = facturasRecibo.get(i);
		}
		return facturas;
	}

	private CCNotaDebito[] getArrayOfNotasDebito() {
		CCNotaDebito[] notasDebito = new CCNotaDebito[notasDebitoRecibo.size()];
		for (int i = 0; (notasDebitoRecibo != null && i < notasDebitoRecibo
				.size()); i++) {
			notasDebito[i] = notasDebitoRecibo.get(i);
		}
		return notasDebito;
	}

	private CCNotaCredito[] getArrayOfNotasCredito() {
		CCNotaCredito[] notasCredito = new CCNotaCredito[notasCreditoRecibo
				.size()];
		for (int i = 0; (notasCreditoRecibo != null && i < notasCreditoRecibo
				.size()); i++) {
			notasCredito[i] = notasCreditoRecibo.get(i);
		}
		return notasCredito;
	}

	private void editarDescuento() {
		if (!"REGISTRADO".equals(recibo.getCodEstado()))
			return;
		int posicion = positioncache;
		if (posicion == -1)
			return;

		final com.panzyma.nm.serviceproxy.Documento documentToEdit;

		documentToEdit = (com.panzyma.nm.serviceproxy.Documento) adapter
				.getItem(posicion);

		if (documentToEdit instanceof ReciboDetFactura
				|| documentToEdit instanceof ReciboDetND) 
		{

			FragmentManager fragmentManager = getSupportFragmentManager();

			if (documentToEdit instanceof ReciboDetFactura) {

				final ReciboDetFactura facturaDetalle = (ReciboDetFactura) documentToEdit
						.getObject();
				final Factura factura = getFacturaByID(facturaDetalle
						.getObjFacturaID());

				final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(
						facturaDetalle, recibo, ActionType.EDIT, true);
				dialogConfirmacion.setActionPago(new Pagable() {
					@Override
					public void onPagarEvent(List<Ammount> montos) {
						procesaFactura(facturaDetalle, factura, montos, false);
					}
				});

				dialogConfirmacion.show(fragmentManager, "");

			} else {

				final ReciboDetND notaDebitoDetalle = (ReciboDetND) documentToEdit
						.getObject();
				final CCNotaDebito nd = getNotaDebitoByID(notaDebitoDetalle
						.getObjNotaDebitoID());

				final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(
						notaDebitoDetalle, recibo, ActionType.EDIT, true);
				dialogConfirmacion.setActionPago(new Pagable() {
					@Override
					public void onPagarEvent(List<Ammount> montos) {
						procesaNotaDebito(notaDebitoDetalle, nd, montos, false);
					}
				});

				dialogConfirmacion.show(fragmentManager, "");

			}

		} else {
			Util.Message.buildToastMessage(this.getContext(),
					"No es posible editar el descuento", TIME_TO_VIEW_MESSAGE);
		}

	}

	private void editarPagos() {
		if (!"REGISTRADO".equals(recibo.getCodEstado()))
			return;
		final FragmentManager fragmentManager = getSupportFragmentManager();
		/*
		 * runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() {
		 */
		final DialogFormasPago dialog = new DialogFormasPago(me,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen,
				recibo.getFormasPagoRecibo());
		dialog.setOnDialogButtonClickListener(new OnFormaPagoButtonClickListener() {
			@Override
			public void onButtonClick(ReciboDetFormaPago formaPago) {
				EditFormaPago editarPago = EditFormaPago.newInstance(recibo,
						formaPago, true);
				editarPago.show(fragmentManager, "");
				dialog.loadFormasPago(recibo.getFormasPagoRecibo());
			}
		});
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dialog.show();
		/*
		 * } });
		 */

	}

	private void enviarRecibo() {

		if (!valido())
			return;

		if (recibo.getCodEstado().compareTo("PAGADO") == 0) {
			showStatus("No se puede enviar un recibo que tiene estado PAGADO",
					true);
			return;
		}

		if (recibo.getNumero() > 0) {
			showStatus("El recibo ya fue enviado anteriormente", true);
			return;
		}
		showStatus("Enviando recibo a la central");
		try {
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putParcelable("recibo", recibo);
			b.putParcelableArray("facturasToUpdate", getArrayOfFacturas());
			b.putParcelableArray("notasDebitoToUpdate", getArrayOfNotasDebito());
			b.putParcelableArray("notasCreditoToUpdate",
					getArrayOfNotasCredito());
			msg.setData(b);
			msg.what = SEND_DATA_FROM_SERVER;
			NMApp.getController().getInboxHandler().sendMessage(msg);

		} catch (Exception e) {
			NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage("Error al enviar el recibo", e
							.getMessage(), e.getMessage()));
		}

	}

	public boolean valido() {

		try {

			// Validar fecha del pedido
			long d = (DateUtil.strTimeToLong(tbxFecha.getText().toString()));
			if (d > DateUtil.d2i(Calendar.getInstance().getTime())) {
				NMApp.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Fecha invalida",
										""
												+ "La fecha del recibo no debe ser mayor a la fecha actual.",
										""));

				return false;
			}

			if (recibo.getNombreCliente().trim() == "") {
				NMApp.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Cliente invalido",
										""
												+ "El cliente del recibo no ha sido ingresado.",
										""));
				return false;
			}

			// Se incluya al menos una factura y/o la cantidad de facturas
			// marcadas para ser incluidas
			int cantFac = Cobro.cantFacturas(recibo);
			int cantND = Cobro.cantNDs(recibo);
			if (cantFac == 0) {
				if (cantND == 0) {

					NMApp.getController()
							.notifyOutboxHandlers(
									ControllerProtocol.ERROR,
									0,
									0,
									new ErrorMessage(
											"Error en Facturas.",
											""
													+ "Debe incluir al menos una factura o nota de débito.",
											""));

					return false;
				}
			}

			// Validar que la cantidad de facturas incluidas no sea mayor que el
			// valor del parámetro CantMaxFacturasEnRecibo.
			int max = Integer.parseInt(Cobro.getParametro(me,
					"CantMaxFacturasEnRecibo") + "");
			if (cantFac > max) {
				NMApp.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Error en detalle del Recibo",
										""
												+ "La cantidad de facturas no debe ser mayor que "
												+ max + " .", ""));

				return false;
			}

			// La cantidad de notas de débito marcadas para ser incluidas en el
			// recibo
			// no debe ser mayor que el valor del párametro
			// CantMaxNotasDebitoEnRecibo
			max = Integer.parseInt(Cobro.getParametro(me,
					"CantMaxNotasDebitoEnRecibo") + "");
			if (cantND > max) {

				NMApp.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Error en detalle del Recibo",
										""
												+ "La cantidad de notas de débito no debe ser mayor que "
												+ max + " .", ""));
				return false;
			}

			// La cantidad de notas de crédito incluidas a pagar no debe ser
			// mayor
			// que el valor del parámetro CantMaxNotasCreditoEnRecibo
			max = Integer.parseInt(Cobro.getParametro(me,
					"CantMaxNotasCreditoEnRecibo") + "");
			if (Cobro.cantNCs(recibo) > max) {
				NMApp.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Error en detalle del Recibo",
										""
												+ "La cantidad de notas de crédito no debe ser mayor que "
												+ max + " .", ""));
				// Dialog.alert("La cantidad de notas de crédito no debe ser mayor que "
				// + max + ".");
				return false;
			}

			// Validar que se haya ingresado al menos un pago
			if (Cobro.cantFPs(recibo) == 0) {
				if( !Cobro.sinNecesidadFormaPago(recibo) ) {
					NMApp.getController().notifyOutboxHandlers(
							ControllerProtocol.ERROR,
							0,
							0,
							new ErrorMessage("Error validando Recibo", ""
									+ "No se ha agregado ningun pago.", ""));

					// Dialog.alert("Detalle de pagos no ingresado.");
					return false;
				}
				
			}

			// Validar que la sumatoria de los montos de las NC seleccionadas no
			// sea mayor ni igual que la sumatoria
			// de los montos a pagar de las facturas incluidas en el recibo, a
			// excepción de que solamente se estén
			// pagando facturas vencidas en cuyo caso SÍ se permite un monto
			// igual
			if (recibo.getTotalNC() > 0) { // Si hay NC aplicadas

				// Ver si todas las facturas aplicadas son vencidas
				boolean todasVencidas = true;
				int diasAplicaMora = Integer.parseInt(Cobro.getParametro(me,
						"DiasDespuesVenceCalculaMora") + "");
				long fechaHoy = DateUtil.getTime(DateUtil.getToday());
				if (recibo.getFacturasRecibo().size() != 0) {
					ReciboDetFactura[] ff = new ReciboDetFactura[recibo
							.getFacturasRecibo().size()];
					recibo.getFacturasRecibo().toArray(ff);
					if (ff != null) {
						for (int i = 0; i < ff.length; i++) {
							ReciboDetFactura f = ff[i];
							String s = f.getFechaVence() + "";
							int fechaVence = Integer
									.parseInt(s.substring(0, 8));
							long fechaCaeEnMora = DateUtil.addDays(
									DateUtil.getTime(fechaVence),
									diasAplicaMora);
							if (fechaCaeEnMora > fechaHoy) {
								todasVencidas = false;
								break;
							}
						}
					}
				} // Ver si todas las facturas aplicadas son vencidas

				if (todasVencidas
						&& (recibo.getTotalNC() > recibo.getTotalFacturas())) {

					NMApp.getController()
							.notifyOutboxHandlers(
									ControllerProtocol.ERROR,
									0,
									0,
									new ErrorMessage(
											"Error en detalle del Recibo",
											""
													+ "El total de notas de crédito a aplicar debe ser menor o igual al total a pagar en facturas."
													+ max + " .", ""));
					// Dialog.alert("El total de notas de crédito a aplicar debe ser menor o igual al total a pagar en facturas.");
					return false;
				}

				if (todasVencidas
						&& (recibo.getTotalNC() >= recibo.getTotalFacturas())) {
					NMApp.getController()
							.notifyOutboxHandlers(
									ControllerProtocol.ERROR,
									0,
									0,
									new ErrorMessage(
											"Error en detalle del Recibo",
											""
													+ "El total de notas de crédito a aplicar debe ser menor al total a pagar en facturas.",
											""));

					// Dialog.alert("El total de notas de crédito a aplicar debe ser menor al total a pagar en facturas.");
					return false;
				}

			} // Si hay NC aplicadas

			// Monto Mínimo Recibo: Para aplicar descuento específico a cada
			// factura que se va cancelar,
			// el total del recibo deber mayor o igual al valor del parámetro
			// 'MontoMinReciboAplicaDpp'
			boolean ValidarMontoAplicaDpp = false;

			// Determinando si hay descPP que validar
			if (recibo.getFacturasRecibo().size() != 0) {
				ArrayList<ReciboDetFactura> ff = recibo.getFacturasRecibo();
				if (ff != null) {
					for (int i = 0; i < ff.size(); i++) {
						ReciboDetFactura f = ff.get(i);
						if (f.getMontoDescEspecifico() != 0) {
							ValidarMontoAplicaDpp = true;
							break;
						}
					}
				}
			} // Determinando si hay descPP que validar

			// Validando el monto mínimo del recibo
			float montoMinimoRecibo = Float.parseFloat(Cobro.getParametro(me,
					"MontoMinReciboAplicaDpp") + "");
			if ((recibo.getTotalRecibo() < montoMinimoRecibo)
					&& ValidarMontoAplicaDpp) {
				// Recalcular detalles del recibo sin aplicar DescPP
				Cobro.calcularDetFacturasRecibo(me, recibo,
						recibo.getCliente(), false);
				actualizaTotales();
				NMApp.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Error validando Recibo",
										""
												+ "Para aplicar descuento pronto pago \rel monto del recibo no debe ser menor que "
												+ StringUtil
														.formatReal(montoMinimoRecibo)
												+ ".", ""));

				return false;
			}
			
			if (Cobro.getTotalPagoRecibo(recibo) != StringUtil.round(recibo.getTotalRecibo(),2)){
				NMApp.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Problema con el Monto Total del Recibo",
										""
												+ "El monto pagado no cuadra con el total del recibo.",
										""));

				// Dialog.alert("El monto pagado no cuadra con el total del recibo.");
				return false;
			}
			return true;

		} catch (Exception e) {
			NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage("Error al Validar el Recibo", e
							.getMessage(), e.getMessage()));

		}
		return false;
	}

	private float getMontoInteresFacturas() {
		float interes = 0.0f;
		for (ReciboDetFactura f : recibo.getFacturasRecibo()) {
			interes += f.getMontoInteres();
		}
		return interes;
	}

	private float getMontoInteresNotaDebito() {
		float interes = 0.0f;
		for (ReciboDetND nd : recibo.getNotasDebitoRecibo()) {
			interes += nd.getMontoInteres();
		}
		return interes;
	}

	private float getMontoDescuentoFacturas() {
		float monto = 0.0f;
		for (ReciboDetFactura f : recibo.getFacturasRecibo()) {
			monto += f.getMontoDescOcasional() + f.getMontoDescPromocion()
					+ f.getMontoDescEspecifico();
		}
		return monto;
	}

	private float getMontoRetencionFacturas() {
		float monto = 0.0f;
		for (ReciboDetFactura f : recibo.getFacturasRecibo()) {
			monto += f.getMontoRetencion();
		}
		return monto;
	}

	private float getMontoOtrasDeduccionesFacturas() {
		float monto = 0.0f;
		for (ReciboDetFactura f : recibo.getFacturasRecibo()) {
			monto += f.getMontoOtrasDeducciones();
		}
		return monto;
	}

	private float getMontoImpuestoExentoFacturas() {
		float monto = 0.0f;
		for (ReciboDetFactura f : recibo.getFacturasRecibo()) {
			monto += f.getMontoImpuestoExento();
		}
		return monto;
	}

	private void actualizaTotales() {

		/*------------- CALCULANDO VALORES ---------------- */
		// CALCULARA EL INTERES
		float interesMora = getMontoInteresFacturas()
				+ getMontoInteresNotaDebito();
		// CALCULANDO EL SUBTOTAL
		float totalFacturas = recibo.getTotalFacturas()
				- getMontoInteresFacturas();
		float totalNotasDebito = recibo.getTotalND()
				- getMontoInteresNotaDebito();
		float totalNotaCredito = recibo.getTotalNC();
		float subTotal = totalFacturas + totalNotasDebito;
		// CALCULAR EL TOTAL DE DESCUENTO
		float totalDesc = getMontoDescuentoFacturas();
		// CALCULAR EL TOTAL RETENIDO
		float totalRetencion = getMontoRetencionFacturas();
		// CALCULAR EL TOTAL DE OTRAS DEDUCCIONES
		float totalOtrasDed = getMontoOtrasDeduccionesFacturas();
		// CALCULAR EL TOTAL DE IMPUESTO EXENTO
		float totalImpuestoExento = getMontoImpuestoExentoFacturas();
		float netoRecibo = subTotal
				- (totalNotaCredito + totalDesc + totalRetencion
						+ totalOtrasDed + totalImpuestoExento);
		/*------------- ESTABLECIENDO VALORES A RECIBO ----------------*/
		recibo.setTotalFacturas(totalFacturas);
		recibo.setTotalND(totalNotasDebito);
		recibo.setSubTotal(subTotal);
		recibo.setTotalDesc(totalDesc);
		recibo.setTotalImpuestoExonerado(totalImpuestoExento);
		recibo.setTotalRetenido(totalRetencion);
		recibo.setTotalOtrasDed(totalOtrasDed);
		recibo.setTotalInteres(interesMora);
		recibo.setTotalRecibo(netoRecibo);
		/*------------- ESTABLECIENDO VALORES A LA VISTA ----------------*/
		txtTotalAbonadoFacturas.setText(StringUtil.formatReal(recibo
				.getTotalFacturas()));
		txtTotalAbonadoND.setText(StringUtil.formatReal(recibo.getTotalND()));
		txtTotalAbonadoNC.setText(StringUtil.formatReal(recibo.getTotalNC()));
		txtSubTotal.setText(StringUtil.formatReal(recibo.getSubTotal()));
		txtTotalDescuento.setText(StringUtil.formatReal(recibo.getTotalDesc()));
		txtTotal.setText(StringUtil.formatReal(recibo.getTotalRecibo()));
		txtTotalRetencion.setText(StringUtil.formatReal(recibo.getTotalRetenido()));
		txtmonto.setText(StringUtil.formatReal(recibo.getFormasPagoMonto()));
	}

	private void procesaFactura(ReciboDetFactura facturaDetalle,
			Factura factura, List<Ammount> montos, boolean agregar) {
		for (Ammount ammount : montos) {
			switch (ammount.getAmmountType()) {
			case ABONADO_OTROS_RECIBOS:
				if (ammount.isEvaluar()) {
					factura.setAbonado(ammount.getValue());
					factura.setSaldo(factura.getTotalFacturado()
							- factura.getAbonado());
				}
				break;

			case ABONADO:
				if (ammount.isEvaluar()) {

					float montoAbonado = 0.00F, saldo = 0.00F;

					montoAbonado = ammount.getValue();
					factura.setAbonado(factura.getAbonado() + montoAbonado);
					saldo = factura.getTotalFacturado() - factura.getAbonado();
					if (saldo > 0) {
						factura.setCodEstado("ABONADA");
						factura.setEstado("Abonada");
						facturaDetalle.setEsAbono(true);
					} else {
						factura.setCodEstado("CANCELADA");
						factura.setEstado("Cancelada");
						facturaDetalle.setEsAbono(false);
					}
					factura.setSaldo(saldo);
					facturaDetalle.setMonto(ammount.getValue());
					facturaDetalle.setSaldoFactura(factura.getSaldo());
					facturaDetalle.setSaldoTotal(factura.getSaldo());
					facturaDetalle.setMontoNeto(facturaDetalle.getMonto());
					Cobro.ActualizaTotalFacturas(recibo);
				}
				break;
			case RETENIDO:
				if (ammount.isEvaluar()) {
					float montoRetencion = 0.00F;
					montoRetencion = ammount.getValue();
					factura.setRetenido(montoRetencion);
					facturaDetalle.setMontoRetencion(montoRetencion);
				}
				break;
			case DESCONTADO:
				if (ammount.isEvaluar()) 
				{
					float montoDescuento = 0.00F;
					montoDescuento = ammount.getValue();					
					if( !(recibo.getPorcDescOcaColector() == 100) ) {
						if (montoDescuento > facturaDetalle.getMontoDescEspecificoCalc()) 
						{
							NMApp.getController()
									.notifyOutboxHandlers(
											ControllerProtocol.ERROR,
											0,
											0,
											new ErrorMessage(
													"Error al editar descuento",
													"El nuevo descuento no debe ser mayor que "
															+ StringUtil
																	.formatReal(facturaDetalle
																			.getMontoDescEspecificoCalc())
															+ ".", ""));

							return;
						}
					}					
					// Recalcular monto neto
					facturaDetalle.setPorcDescOcasional(montoDescuento);
					Cobro.calcularDetFacturasRecibo(contexto, recibo,
							recibo.getCliente(), true);
					factura.setDescontado(facturaDetalle.getMontoDescOcasional());
					factura.setDescuentoFactura(factura.getDescuentoFactura()+ facturaDetalle.getMontoDescOcasional());
					Cobro.ActualizaMtoNetoFacturasrecibo(recibo);					
					
					//facturaDetalle.setMontoDescEspecificoCalc(montoDescuento);
				}
			default:
				break;
			}
		}
		if (agregar) {
			facturaDetalle.setFechaAplicaDescPP(factura.getFechaAppDescPP());
			facturasRecibo.add(factura);
			recibo.getFacturasRecibo().add(facturaDetalle);
			recibo.setCliente(cliente);
			Cobro.calcularDetFacturasRecibo(contexto, recibo,
					recibo.getCliente(), true);
			documents.add(facturaDetalle);

		}		
		agregarDocumentosAlDetalleDeRecibo();
		Cobro.ActualizaTotalFacturas(recibo);
		actualizaTotales();
	}

	@SuppressWarnings("incomplete-switch")
	private void procesaNotaCredito(ReciboDetNC notaCreditoDetalle,
			CCNotaCredito notaCredito, List<Ammount> montos, boolean agregar) {
		for (Ammount ammount : montos) {
			switch (ammount.getAmmountType()) {
			case ABONADO:
				if (ammount.isEvaluar()) {
					float montoAbonado = 0.00F, saldo = 0.00F;
					montoAbonado = ammount.getValue();

					saldo = notaCredito.getMonto() - notaCredito.getMonto();
					if (saldo > 0) {
						notaCredito.setCodEstado("ABONADA");
						notaCredito.setEstado("Abonada");
					} else {
						notaCredito.setCodEstado("CANCELADA");
						notaCredito.setEstado("Cancelada");
					}

				}
				break;
			}
		}
		if (agregar) {
			notasCreditoRecibo.add(notaCredito);
			recibo.getNotasCreditoRecibo().add(notaCreditoDetalle);
			documents.add(notaCreditoDetalle);
		}
		recibo.setCliente(cliente);
		Cobro.calcularDetFacturasRecibo(contexto, recibo, recibo.getCliente(),
				true);

		agregarDocumentosAlDetalleDeRecibo();
		Cobro.ActualizaTotalNotasCredito(recibo);
		actualizaTotales();

	}

	@SuppressWarnings("incomplete-switch")
	private void procesaNotaDebito(ReciboDetND notaDebitoDetalle,
			CCNotaDebito notaDebito, List<Ammount> montos, boolean agregar) {
		for (Ammount ammount : montos) {
			switch (ammount.getAmmountType()) {
			case ABONADO_OTROS_RECIBOS:
				if (ammount.isEvaluar()) {
					float montoAbonado = 0.00F;
					montoAbonado = ammount.getValue();
					notaDebito.setMontoAbonado(montoAbonado);
				}
				break;
			case ABONADO:
				if (ammount.isEvaluar()) {
					float montoAbonado = 0.00F, saldo = 0.00F;
					montoAbonado = ammount.getValue();
					notaDebito.setMontoAbonado(notaDebito.getMontoAbonado()
							+ montoAbonado);
					saldo = notaDebito.getMonto()
							- notaDebito.getMontoAbonado();
					/*
					 * if ( saldo > 0 ) { notaDebito.setCodEstado("ABONADA");
					 * notaDebito.setEstado("Abonada"); } else {
					 * notaDebito.setCodEstado("CANCELADA");
					 * notaDebito.setEstado("Cancelada"); }
					 */
					notaDebito.setSaldo(saldo);
					notaDebitoDetalle.setMontoPagar(montoAbonado);
					notaDebitoDetalle.setMontoNeto(notaDebitoDetalle
							.getMontoPagar()
							- notaDebitoDetalle.getMontoInteres());
					if (notaDebitoDetalle.getMontoPagar() < notaDebitoDetalle
							.getSaldoTotal())
						notaDebitoDetalle.setEsAbono(true);
				}
				break;
			}
		}
		if (agregar) {
			notasDebitoRecibo.add(notaDebito);
			recibo.getNotasDebitoRecibo().add(notaDebitoDetalle);
			documents.add(notaDebitoDetalle);
		}
		agregarDocumentosAlDetalleDeRecibo();
		Cobro.ActualizaTotalNotasDebito(recibo);
		actualizaTotales();

	}

	private void agregarDocumentosPendientesCliente() {

		if (cliente == null) {
			Toast.makeText(getApplicationContext(),
					"Debe seleccionar un cliente", Toast.LENGTH_SHORT).show();
			return;
		}

		FragmentManager fragmentManager = getSupportFragmentManager();

		DialogSeleccionTipoDocumento dtp = new DialogSeleccionTipoDocumento();
		dtp.setEventSeleccionable(new Seleccionable() {
			@Override
			public void onSeleccionarDocumento(Documento document) 
			{

				final DialogDocumentos dialog = new DialogDocumentos(me,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen,cliente, document);
				dialog.setOnDialogDocumentoButtonClickListener(new OnDocumentoButtonClickListener() {
					@Override
					public void onButtonClick(Object documento) 
					{
						if (documento instanceof Factura) {
							// SI EL DOCUMENTO ES UNA FACTURA
							final Factura factura = (Factura) documento;
							// CREAR UN OBJETO DETALLE DE FACURA
							final ReciboDetFactura facturaDetalle = new ReciboDetFactura();
							facturaDetalle.setObjFacturaID(factura.getId());
							facturaDetalle.setFecha(factura.getFecha());
							facturaDetalle.setFechaVence(factura.getFechaVencimiento());
							facturaDetalle.setFechaAplicaDescPP(factura.getFechaAppDescPP());
							facturaDetalle.setNumero(factura.getNoFactura());
							facturaDetalle.setMontoRetencion(0.00f);
							facturaDetalle.setEsAbono(false);
							facturaDetalle.setImpuesto(factura.getImpuestoFactura());
							facturaDetalle.setMontoImpuesto(0.00F); 
							// Calcular el interés moratorio de la factura si
							// está en mora
							float porcentajeIntMora = Float.parseFloat((String) Cobro.getParametro(contexto, "PorcInteresMoratorio"));
							facturaDetalle.setInteresMoratorio(porcentajeIntMora);
							float interesMoratorio = Cobro.getInteresMoratorio(contexto, factura.getFechaVencimiento(),factura.getSaldo());
							facturaDetalle.setMontoInteres(interesMoratorio);
							facturaDetalle.setMontoDescEspecifico(0.0F);
							facturaDetalle.setPorcDescPromo(0.0F);
							facturaDetalle.setMontoDescPromocion(0.0F);
							facturaDetalle.setPorcDescOcasional(0.0F);
							facturaDetalle.setMontoDescOcasional(0.0F);
							facturaDetalle.setMontoNeto(0.0F);
							facturaDetalle.setMontoOtrasDeducciones(0.0F);
							facturaDetalle.setMontoRetencion(0.0F);
							facturaDetalle.setSaldoFactura(factura.getSaldo());
							facturaDetalle.setSaldoTotal(factura.getSaldo()+ interesMoratorio);
							facturaDetalle.setMonto(facturaDetalle.getSaldoTotal());
							facturaDetalle.setSubTotal(factura.getSubtotalFactura()- factura.getDescuentoFactura());
							facturaDetalle.setTotalFactura(factura.getTotalFacturado());

							final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(facturaDetalle, recibo, ActionType.ADD);
							dialogConfirmacion.setActionPago(new Pagable() {
								@Override
								public void onPagarEvent(List<Ammount> montos) {
									procesaFactura(facturaDetalle, factura,montos, true);
									dialog.loadFacturas(cliente.getFacturasPendientes(), 0);
								}
							});
							FragmentManager fragmentManager = getSupportFragmentManager();

							dialogConfirmacion.show(fragmentManager, "");

						} 
						else if (documento instanceof CCNotaDebito) {
							// SI EL DOCUMENTO ES UNA NOTA DE DEBITO
							final CCNotaDebito notaDebito = (CCNotaDebito) documento;
							// CREAR UN OBJETO DETALLE DE NOTA DEBITO
							final ReciboDetND notaDebitoDetalle = new ReciboDetND();
							notaDebitoDetalle.setId(0);
							notaDebitoDetalle.setObjNotaDebitoID(notaDebito.getId());
							notaDebitoDetalle.setNumero(notaDebito.getNumero());
							notaDebitoDetalle.setFecha(notaDebito.getFecha());
							notaDebitoDetalle.setFechaVence(notaDebito.getFechaVence());
							notaDebitoDetalle.setEsAbono(false);

							float porcentajeIntMora = Float.parseFloat((String) Cobro.getParametro(contexto, "PorcInteresMoratorio"));
							float interesMoratorio = Cobro.getInteresMoratorio(contexto, notaDebito.getFechaVence(),notaDebito.getSaldo());
							notaDebitoDetalle.setMontoInteres(interesMoratorio);

							notaDebitoDetalle.setInteresMoratorio(porcentajeIntMora);
							notaDebitoDetalle.setMontoND(notaDebito.getMonto());
							notaDebitoDetalle.setSaldoND(notaDebito.getSaldo());
							notaDebitoDetalle.setMontoNeto(0.0F);
							notaDebitoDetalle.setSaldoTotal(notaDebitoDetalle.getSaldoND()+ notaDebitoDetalle.getMontoInteres());
							notaDebitoDetalle.setMontoPagar(notaDebitoDetalle.getSaldoTotal());

							final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(notaDebitoDetalle, recibo, ActionType.ADD);
							dialogConfirmacion.setActionPago(new Pagable() {
								@Override
								public void onPagarEvent(List<Ammount> montos) {
									procesaNotaDebito(notaDebitoDetalle,notaDebito, montos, true);
									dialog.loadNotasDebito(cliente.getNotasDebitoPendientes(),0);
								}
							});
							FragmentManager fragmentManager = getSupportFragmentManager();

							dialogConfirmacion.show(fragmentManager, "");

						} 
						else if (documento instanceof CCNotaCredito) {
							// SI EL DOCUMENTO ES UNA NOTA DE CREDITO
							final CCNotaCredito notaCredito = (CCNotaCredito) documento;
							// CREAR UN OBJETO DETALLE DE NOTA CREDITO
							final ReciboDetNC notaCreditoDetalle = new ReciboDetNC();
							notaCreditoDetalle.setId(0);
							notaCreditoDetalle.setFecha(notaCredito.getFecha());
							notaCreditoDetalle.setFechaVence(notaCredito.getFechaVence());
							notaCreditoDetalle.setMonto(notaCredito.getMonto());
							notaCreditoDetalle.setNumero(notaCredito.getNumero());
							notaCreditoDetalle.setObjNotaCreditoID(notaCredito.getId());

							final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(notaCreditoDetalle, recibo, ActionType.ADD);
							dialogConfirmacion.setActionPago(new Pagable() {
								@Override
								public void onPagarEvent(List<Ammount> montos) {
									procesaNotaCredito(notaCreditoDetalle,notaCredito, montos, true);
									dialog.loadNotasCredito(cliente.getNotasCreditoPendientes(),0);
								}
							});
							FragmentManager fragmentManager = getSupportFragmentManager();
							dialogConfirmacion.show(fragmentManager, "");
						}
					}
				});
				Window window = dialog.getWindow();
				window.setGravity(Gravity.CENTER);
				window.setLayout(display.getWidth() - 40,display.getHeight() - 110);
				dialog.show();
			}
		});
		dtp.show(fragmentManager, "");

	}

	private void agregarPago() {
		if (recibo != null && recibo.getTotalRecibo() == 0) {
			NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage("Error al agregar pago",
							"No es posible agregar pago, monto pendiente igual a cero."
									+ ".", ""));

			return;
		}

		if ("registrado".compareTo(recibo.getCodEstado().toLowerCase()) != 0) {
			NMApp.getController()
					.notifyOutboxHandlers(
							ControllerProtocol.ERROR,
							0,
							0,
							new ErrorMessage(
									"Error al agregar pago",
									String.format(
											"El estado %s del recibo, no permite agregar pagos",
											recibo.getDescEstado())
											+ ".", ""));
			return;
		}

		// Validar que haya pendiente por pagar
		float montoPorPagar = StringUtil.round(
				recibo.getTotalRecibo() - Cobro.getTotalPagoRecibo(recibo), 2);
		if (montoPorPagar <= 0) {
			NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage("Error al agregar pago",
							"No hay monto pendiente de pago.", ""));
			return;
		}

		final FragmentManager fragmentManager = getSupportFragmentManager();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EditFormaPago editarPago = EditFormaPago.newInstance(recibo,
						null, false);
				editarPago.show(fragmentManager, "");
			}
		});

	}

	public Long getObjectSucursalID() {
		if (recibo != null)
			return recibo.getObjSucursalID();
		else
			return Long.valueOf("0");
	}

	public void agregarDocumentosAlDetalleDeRecibo() {

		adapter = new GenericAdapter(this, DocumentoViewHolder.class,documents,  R.layout.list_row);
		item_document.setAdapter(adapter);
		adapter.setSelectedPosition(0);
		
		if (documents.size() > 0)
			documento_selected = documents.get(0);
		gridheader.setText("DOCUMENTOS A PAGAR(" + adapter.getCount() + ")");
	}

	public void showMenu(final View view) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				quickAction2 = new QuickAction(me, QuickAction.VERTICAL, 1);
				quickAction2.addActionItem(new ActionItem(ID_EDITAR_DOCUMENTO,
						"Editar Documento"));
				quickAction2.addActionItem(new ActionItem(
						VER_DETALLE_DOCUMENTO, "Ver Detalle Documento"));
				quickAction2.addActionItem(new ActionItem(
						ID_ELIMINAR_DOCUMENTO, "Eliminar Documento"));
				quickAction2
						.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
							@Override
							public void onItemClick(QuickAction source,
									final int pos, final int actionId) {
								ActionItem actionItem = quickAction2
										.getActionItem(pos);

								switch (actionId) {
								case ID_EDITAR_DOCUMENTO:
									editarDocumento();
									break;
								case ID_EDITAR_DESCUENTO:
									editarDescuento();
									break;
								case ID_ELIMINAR_DOCUMENTO:
									eliminarDocumento();
									break;
								}

							}

						});
				quickAction2.show(view, display, false);
			}
		});
	}

	private void removeDocument(
			com.panzyma.nm.serviceproxy.Documento documentRemoved) {
		int positionDocument = -1, count = 0;
		if (documentRemoved instanceof ReciboDetFactura) {
			// SI EL DOCUMENTO SE TRATA DE UNA FACTURA
			ReciboDetFactura facturaToRemoved = ((ReciboDetFactura) documentRemoved
					.getObject());
			for (Factura fac : getFacturasRecibo()) {
				if (fac.getId() == facturaToRemoved.getObjFacturaID()) {
					positionDocument = count;
					break;
				}
				++count;
			}
			// ACTUALIZAR LA INFORMACION DE LA FACTURA
			ReciboDetFactura factDetalle = recibo.getFacturasRecibo().get(
					positionDocument);

			if (recibo.getId() != 0) {
				List<ReciboDetFactura> factToUpd = new ArrayList<ReciboDetFactura>();
				factToUpd.add(factDetalle);
				ContentResolver cntr = (ContentResolver) NMApp.getContext()
						.getContentResolver();
				Context cntx = (Context) NMApp.getContext();
				ModelRecibo.updateFacturas(factToUpd, cntr, cntx);
				ModelRecibo.deleteDocument(10, factDetalle.getId(),
						recibo.getId(), cntx);
			}

			facturasRecibo.remove(positionDocument);
			recibo.getFacturasRecibo().remove(positionDocument);
			recibo.setTotalFacturas(recibo.getTotalFacturas()
					- facturaToRemoved.getMonto());
			// CALCULA NUEVAMENTE LOS DESCUENTOS
			Cobro.calcularDetFacturasRecibo(contexto, recibo,
					recibo.getCliente(), true);

		} else if (documentRemoved instanceof ReciboDetND) {
			// SI EL DOCUMENTO SE TRATA DE UNA NOTA DE DEBITO
			ReciboDetND notaDebitoToRemoved = ((ReciboDetND) documentRemoved
					.getObject());
			for (CCNotaDebito notaDebito : getNotasDebitoRecibo()) {
				if (notaDebito.getId() == notaDebitoToRemoved
						.getObjNotaDebitoID()) {
					positionDocument = count;
					break;
				}
				++count;
			}

			// ACTUALIZAR LA INFORMACION DE LA NOTA DE DEBITO
			ReciboDetND ndDetalle = recibo.getNotasDebitoRecibo().get(
					positionDocument);

			if (recibo.getId() != 0) {
				List<ReciboDetND> ndToUpd = new ArrayList<ReciboDetND>();
				ndToUpd.add(ndDetalle);
				ContentResolver cntr = (ContentResolver) NMApp.getContext()
						.getContentResolver();
				Context cntx = (Context) NMApp.getContext();
				ModelRecibo.updateNotasDebito(ndToUpd, cntr, cntx);
				ModelRecibo.deleteDocument(20, ndDetalle.getId(),
						recibo.getId(), cntx);
			}

			notasDebitoRecibo.remove(positionDocument);
			recibo.getNotasDebitoRecibo().remove(positionDocument);
			recibo.setTotalND(recibo.getTotalND()
					- notaDebitoToRemoved.getMonto());
		} else if (documentRemoved instanceof ReciboDetNC) {
			// SI EL DOCUMENTO SE TRATA DE UNA NOTA DE CREDITO
			ReciboDetNC notaCreditoToRemoved = ((ReciboDetNC) documentRemoved
					.getObject());
			for (CCNotaCredito notaCredito : getNotasCreditoRecibo()) {
				if (notaCredito.getId() == notaCreditoToRemoved
						.getObjNotaCreditoID()) {
					positionDocument = count;
					break;
				}
				++count;
			}

			// ACTUALIZAR LA INFORMACION DE LA NOTA DE DEBITO
			ReciboDetNC ncDetalle = recibo.getNotasCreditoRecibo().get(
					positionDocument);

			if (recibo.getId() != 0) {
				List<ReciboDetNC> ncToUpd = new ArrayList<ReciboDetNC>();
				ncToUpd.add(ncDetalle);
				ContentResolver cntr = (ContentResolver) NMApp.getContext()
						.getContentResolver();
				Context cntx = (Context) NMApp.getContext();
				ModelRecibo.updateNotasCredito(ncToUpd, cntr, cntx);
				ModelRecibo.deleteDocument(30, ncDetalle.getId(),
						recibo.getId(), cntx);
			}

			notasCreditoRecibo.remove(positionDocument);
			recibo.getNotasCreditoRecibo().remove(positionDocument);
			recibo.setTotalNC(recibo.getTotalNC()
					- notaCreditoToRemoved.getMonto());
			// CALCULA NUEVAMENTE LOS DESCUENTOS
			Cobro.calcularDetFacturasRecibo(contexto, recibo,
					recibo.getCliente(), true);
		}

	}

	private void eliminarDocumento() {
		if (!"REGISTRADO".equals(recibo.getCodEstado()))
			return;
		final int posicion = positioncache;
		if (posicion == -1)
			return;

		final String sms = "Confirme si desea eliminar el documento";

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Informar al usuario
				AppDialog.showMessage(me, "", sms,
						AppDialog.DialogType.DIALOGO_CONFIRMACION,
						new AppDialog.OnButtonClickListener() {
							@Override
							public void onButtonClick(AlertDialog _dialog,
									int actionId) {
								if (actionId == AppDialog.OK_BUTTOM) {
									com.panzyma.nm.serviceproxy.Documento documentRemoved;

									// ELIMINAR DE LA LISTA DE DOCUMENTOS
									documentRemoved = documents
											.remove(posicion);

									// ELIMINAR EL DOCUMENTO DEL RECIBO Y
									// ACTUALIZAR EL TOTAL
									removeDocument(documentRemoved);

									// ELIMINAR LOS PAGOS, PARA QUE SEAN
									// REALIZADOS NUEVAMENTE
									for (int i = 0; i < recibo
											.getFormasPagoRecibo().size(); i++) {
										recibo.getFormasPagoRecibo().remove(i);
									}

									// ACTUALIZA EL TOTAL EN LA PANTALLA Y
									// ACTUALIZA EL SUBTOTAL Y TOTAL DEL RECIBO
									actualizaTotales();

									// ACTUALIZA LOS MONTOS DEL RECIBO
									ModelRecibo.updateRecibo(recibo,
											NMApp.getContext());

									if (documents.size() > 0) {
										if (posicion == 0) {
											positioncache = 0;
											documento_selected = (com.panzyma.nm.serviceproxy.Documento) adapter
													.getItem(0);
											adapter.setSelectedPosition(0);
										} else {
											if (posicion == documents.size()) {
												positioncache = posicion - 1;
												documento_selected = (com.panzyma.nm.serviceproxy.Documento) adapter
														.getItem(posicion - 1);
												adapter.setSelectedPosition(posicion - 1);
											} else {
												positioncache = posicion;
												documento_selected = (com.panzyma.nm.serviceproxy.Documento) adapter
														.getItem(posicion);
												adapter.setSelectedPosition(posicion);
											}
										}
									}
									adapter.notifyDataSetChanged();
									gridheader.setText("DOCUMENTOS A PAGAR ("
											+ adapter.getCount() + ")");
									// Josue, cambie el parametro de boolean a
									// int de guardarRecibo, creo q no t afecte,
									// ya q el condicional
									// lo puse dentro d la funcion, yo hic esto
									// debido a q a veces c necsita guardar el
									// recibo pero no por la misma via.
									// if(recibo.getId() != 0)
									// guardarRecibo(false);
									guardarRecibo();									
								}
								_dialog.dismiss();
							}
						});
			}
		});

	}

	private Factura getFacturaByID(long id) {
		Factura facturaToFound = null;
		for (Factura factura : facturasRecibo) {
			if (factura.getId() == id) {
				facturaToFound = factura;
				break;
			}
		}
		return facturaToFound;
	}

	private CCNotaDebito getNotaDebitoByID(long id) {
		CCNotaDebito notaDebitoToFound = null;
		for (CCNotaDebito notaDebito : notasDebitoRecibo) {
			if (notaDebito.getId() == id) {
				notaDebitoToFound = notaDebito;
				break;
			}
		}
		return notaDebitoToFound;
	}

	private CCNotaCredito getNotaCreditoByID(long id) {
		CCNotaCredito notaCreditoToFound = null;
		for (CCNotaCredito notaCredito : notasCreditoRecibo) {
			if (notaCredito.getId() == id) {
				notaCreditoToFound = notaCredito;
				break;
			}
		}
		return notaCreditoToFound;
	}

	private void editarDocumento() {
		if (!"REGISTRADO".equals(recibo.getCodEstado()))
			return;
		int posicion = positioncache;
		if (posicion == -1)
			return;

		final com.panzyma.nm.serviceproxy.Documento documentToEdit;

		documentToEdit = (com.panzyma.nm.serviceproxy.Documento) adapter.getItem(posicion);

		final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(
				documentToEdit, recibo, ActionType.EDIT);
		dialogConfirmacion.setActionPago(new Pagable() {
			@Override
			public void onPagarEvent(List<Ammount> montos) {

				if (documentToEdit instanceof ReciboDetFactura) {

					ReciboDetFactura facturaDetalle = (ReciboDetFactura) documentToEdit;
					Factura factura = getFacturaByID(facturaDetalle
							.getObjFacturaID());
					procesaFactura(facturaDetalle, factura, montos, false);

				} else if (documentToEdit instanceof ReciboDetND) {

					ReciboDetND notaDebitoDetalle = (ReciboDetND) documentToEdit;
					CCNotaDebito notaDebito = getNotaDebitoByID(notaDebitoDetalle
							.getObjNotaDebitoID());
					procesaNotaDebito(notaDebitoDetalle, notaDebito, montos,
							false);

				} else if (documentToEdit instanceof ReciboDetNC) {

					ReciboDetNC notaCreditoDetalle = (ReciboDetNC) documentToEdit;
					CCNotaCredito notaCredito = getNotaCreditoByID(notaCreditoDetalle
							.getObjNotaCreditoID());
					procesaNotaCredito(notaCreditoDetalle, notaCredito, montos,
							false);

				}
				recibo.setTotalFacturas(0.00f);
				recibo.setTotalND(0.00f);
				recibo.setTotalNC(0.00F);
				for (com.panzyma.nm.serviceproxy.Documento doc : (List<com.panzyma.nm.serviceproxy.Documento>) adapter
						.getData()) {
					if (doc instanceof ReciboDetFactura)
						recibo.setTotalFacturas(recibo.getTotalFacturas()
								+ doc.getMonto());
					else if (doc instanceof ReciboDetND)
						recibo.setTotalND(recibo.getTotalND() + doc.getMonto());
					else
						recibo.setTotalNC(recibo.getTotalNC() + doc.getMonto());
				}

				agregarDocumentosAlDetalleDeRecibo();
				actualizaTotales();
			}
		});
		FragmentManager fragmentManager = getSupportFragmentManager();

		dialogConfirmacion.show(fragmentManager, "");

	}

	@Override
	public BReciboM getBridge() {
		return brm;
	}

	public Handler getHandler() {
		return handler;
	}

	@Override
	public Context getContext() {
		return this.me;
	}

	public ReciboColector getRecibo() {
		return recibo;
	}

	public ReciboColector setRecibo(ReciboColector r) {
		return recibo = r;
	}

	public boolean getEstadoConexionPago() {
		return pagarOnLine;
	}

	public void showStatusOnUI(Object msg) throws InterruptedException {

		final String titulo = "" + ((ErrorMessage) msg).getTittle();
		final String mensaje = "" + ((ErrorMessage) msg).getMessage();

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AppDialog.showMessage(me, titulo, mensaje,
						AppDialog.DialogType.DIALOGO_ALERTA,
						new AppDialog.OnButtonClickListener() {
							@Override
							public void onButtonClick(AlertDialog _dialog,
									int actionId) {
								_dialog.dismiss();
							}
						});
			}
		});

	}

	private void FINISH_ACTIVITY() {
		recibo.setNotas(tbxNotas.getText().toString());
		if (recibo.hasModified(recibo.getOldData())) {

			AppDialog.showMessage(getContext(), "",
					"¿Desea guardar la información antes de salir?",
					AppDialog.DialogType.DIALOGO_CONFIRMACION,
					new AppDialog.OnButtonClickListener() {
						@Override
						public void onButtonClick(AlertDialog _dialog,
								int actionId) {

							if (AppDialog.OK_BUTTOM == actionId) {
								try {
									guardarRecibo(ControllerProtocol.SALVARRECIBOANTESDESALIR);

								} catch (Exception e) {
									NMApp.getController().notifyOutboxHandlers(
											ControllerProtocol.ERROR,
											0,
											0,
											new ErrorMessage(
													"Error guardando recibo", e
															.getMessage(), ""
															+ e.getCause()));
								}

							} else {
								NMApp.getController()
										._notifyOutboxHandlers(
												ControllerProtocol.SALVARRECIBOANTESDESALIR,
												0, 0, recibo.getOldData());

							}
							_dialog.dismiss();

						}

					});

		} else {
			if (!onEdit)
				NMApp.getController()._notifyOutboxHandlers(
						ControllerProtocol.SALVARRECIBOANTESDESALIR, 0, 0,
						recibo);
			else
				finish();
		}

	}
	
	public void showClientDialog(final AlertDialog... _dialog)
	{
		DialogCliente dc = new DialogCliente(me,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dc.setOnDialogClientButtonClickListener(new OnButtonClickListener() 
		{

			@Override
			public void onButtonClick(Cliente _cliente) 
			{
				com.panzyma.nm.NMApp.getController().setView(me);
				if (recibo.getCodEstado().compareTo("REGISTRADO") != 0)
					return;
				cliente = _cliente;
				SetCliente();
				if(_dialog!=null && _dialog.length!=0)
					_dialog[0].dismiss();
			}
		});
		Window window = dc.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dc.show();
	}

	private void PagarTodo() {
		List<CCNotaDebito> ndr = new ArrayList<CCNotaDebito>(notasDebitoRecibo);
		List<Factura> fr = new ArrayList<Factura>(facturasRecibo);
		List<com.panzyma.nm.serviceproxy.Documento> d = new ArrayList<com.panzyma.nm.serviceproxy.Documento>(
				documents);
		ArrayList<ReciboDetFactura> rdf = new ArrayList<ReciboDetFactura>(
				recibo.getFacturasRecibo());
		ArrayList<ReciboDetND> rdnd = new ArrayList<ReciboDetND>(
				recibo.getNotasDebitoRecibo());
		ArrayList<ReciboDetNC> rdnc = new ArrayList<ReciboDetNC>(
				recibo.getNotasCreditoRecibo());
		try {

			ArrayList<ReciboDetFactura> _facSeleccionadas = new ArrayList<ReciboDetFactura>();
			ArrayList<ReciboDetND> _ndsSeleccionadas = new ArrayList<ReciboDetND>();

			cliente = (Cliente) NMApp
					.getController()
					.getBridge()
					.getClass()
					.getMethod("getClienteBySucursalID", ContentResolver.class,
							long.class)
					.invoke(null, getContentResolver(), cliente.getIdSucursal());
			ArrayList<Factura> facturas = new ArrayList<Factura>();
			// Traer las facturas del cliente
			Factura[] facturaspendientes = cliente.getFacturasPendientes();
			if (facturaspendientes != null) {
				if (facturaspendientes.length > 0) {
					for (int i = 0; i < facturaspendientes.length; i++) {
						Factura fac = facturaspendientes[i];
						// Si la factura no está en otro recibo
						if (Cobro.FacturaEstaEnOtroRecibo(
								getApplicationContext().getContentResolver(),
								fac.getId(), true) == 0) {
							facturas.add(fac);
						}
					}
				}
			}
			// Traer las notas de débito del cliente
			ArrayList<CCNotaDebito> notas = new ArrayList<CCNotaDebito>();
			CCNotaDebito[] NotasDebitoPendientes = cliente
					.getNotasDebitoPendientes();
			if (NotasDebitoPendientes != null) {
				if (NotasDebitoPendientes.length > 0) {
					for (int i = 0; i < NotasDebitoPendientes.length; i++) {
						if (Cobro.NDEstaEnOtroRecibo(getApplicationContext()
								.getContentResolver(), NotasDebitoPendientes[i]
								.getId(), true) == 0)
							notas.add(NotasDebitoPendientes[i]);
					}
				}
			}

			if (((facturas == null) || (facturas.size() == 0))
					&& ((notas == null) || (notas.size() == 0)))
				return;

			String interes = getSharedPreferences("SystemParams",
					android.content.Context.MODE_PRIVATE).getString(
					"PorcInteresMoratorio", "0");
			// Facturas

			notasDebitoRecibo.clear();
			notasCreditoRecibo.clear();
			facturasRecibo.clear();
			documents.clear();
			recibo.getFormasPagoRecibo().clear(); 
			
			recibo.getFacturasRecibo().clear();
			recibo.getNotasDebitoRecibo().clear();
			recibo.getNotasCreditoRecibo().clear();

			recibo.setTotalFacturas(0.00F);
			recibo.setTotalNC(0.00F);
			recibo.setTotalND(0.00F);
			recibo.setSubTotal(0.00F);
			recibo.setTotalDesc(0.00F);
			recibo.setTotalImpuestoExonerado(0.00F);
			recibo.setTotalRetenido(0.00F);
			recibo.setTotalOtrasDed(0.00F);
			recibo.setTotalInteres(0.00F);
			recibo.setTotalRecibo(0.00F);

			if ((facturas != null) && (facturas.size() > 0)) {
				for (int i = 0; i < facturas.size(); i++) {
					Factura _fac = facturas.get(i);
					// CREAR UN OBJETO DETALLE DE FACURA
					final ReciboDetFactura facturaDetalle = new ReciboDetFactura();
					facturaDetalle.setObjFacturaID(_fac.getId());
					facturaDetalle.setFecha(_fac.getFecha());
					facturaDetalle.setFechaVence(_fac.getFechaVencimiento());
					facturaDetalle.setFechaAplicaDescPP(_fac
							.getFechaAppDescPP());
					facturaDetalle.setNumero(_fac.getNoFactura());
					facturaDetalle.setMontoRetencion(0.00f);
					facturaDetalle.setEsAbono(false);
					facturaDetalle.setImpuesto(_fac.getImpuestoFactura());
					facturaDetalle.setMontoImpuesto(0.00F);
					// Calcular el interés moratorio de la factura si está en
					// mora
					float porcentajeIntMora = Float.parseFloat((String) Cobro
							.getParametro(contexto, "PorcInteresMoratorio"));
					facturaDetalle.setInteresMoratorio(porcentajeIntMora);
					float interesMoratorio = Cobro.getInteresMoratorio(
							contexto, _fac.getFechaVencimiento(),
							_fac.getSaldo());
					facturaDetalle.setMontoInteres(interesMoratorio);
					facturaDetalle.setMontoDescEspecifico(0.0F);
					facturaDetalle.setPorcDescPromo(0.0F);
					facturaDetalle.setMontoDescPromocion(0.0F);
					facturaDetalle.setPorcDescOcasional(0.0F);
					facturaDetalle.setMontoDescOcasional(0.0F);
					facturaDetalle.setMontoNeto(0.0F);
					facturaDetalle.setMontoOtrasDeducciones(0.0F);
					facturaDetalle.setMontoRetencion(0.0F);
					facturaDetalle.setSaldoFactura(_fac.getSaldo());
					facturaDetalle.setSaldoTotal(_fac.getSaldo()
							+ interesMoratorio);
					facturaDetalle.setMonto(facturaDetalle.getSaldoTotal());
					facturaDetalle.setSubTotal(_fac.getSubtotalFactura()
							- _fac.getDescuentoFactura());
					facturaDetalle.setTotalFactura(_fac.getTotalFacturado());

					// Agregarla a facturas seleccionadas
					_facSeleccionadas.add(facturaDetalle);
					List<Ammount> montos = new ArrayList<Ammount>();
					montos.add(new Ammount(AmmountType.ABONADO, facturaDetalle
							.getSaldoTotal(), true));
					procesaFactura(facturaDetalle, _fac, montos, true);

				}
			}// Facturas
			if ((notas != null) && (notas.size() > 0)) {
				for (int i = 0; i < notas.size(); i++) {
					CCNotaDebito _nd = notas.get(i);
					ReciboDetND _ndRecibo = new ReciboDetND();
					_ndRecibo.setId(0);
					_ndRecibo.setObjNotaDebitoID(_nd.getId());
					_ndRecibo.setNumero(_nd.getNumero());
					_ndRecibo.setFecha(_nd.getFecha());
					_ndRecibo.setFechaVence(_nd.getFechaVence());
					_ndRecibo.setEsAbono(false);
					_ndRecibo.setMontoInteres(Cobro.getInteresMoratorio(this,
							_nd.getFechaVence(), _nd.getSaldo()));
					_ndRecibo.setInteresMoratorio(Float.parseFloat(interes));
					_ndRecibo.setMontoND(_nd.getMonto());
					_ndRecibo.setSaldoND(_nd.getSaldo());
					_ndRecibo.setMontoNeto(0.0F);
					_ndRecibo.setSaldoTotal(_ndRecibo.getSaldoND()
							+ _ndRecibo.getMontoInteres());
					_ndRecibo.setMontoPagar(_ndRecibo.getSaldoTotal());
					List<Ammount> montos = new ArrayList<Ammount>();
					montos.add(new Ammount(AmmountType.ABONADO, _ndRecibo
							.getSaldoTotal(), true));
					procesaNotaDebito(_ndRecibo, _nd, montos, true);
					_ndsSeleccionadas.add(_ndRecibo);
				}
			}

			if (_facSeleccionadas.size() > 0) {
				// Insertar nuevas facturas en el detalle de facturas del recibo
				ArrayList<ReciboDetFactura> fff = new ArrayList<ReciboDetFactura>();
				// Copiar facturas seleccionadas
				for (int i = 0; i < _facSeleccionadas.size(); i++) {
					fff.add(_facSeleccionadas.get(i));
//					float totalfactura = recibo.getTotalFacturas();
//					recibo.setTotalFacturas(totalfactura
//							+ _facSeleccionadas.get(i).getTotalfactura());
				}
				// Actualizar detalle de facturas
				recibo.setFacturasRecibo(fff);

				// documents.addAll(fff);
			}
			if (_ndsSeleccionadas.size() > 0) {
				// Insertar nuevas ncs en el detalle de ncs del recibo
				ArrayList<ReciboDetND> ccc = new ArrayList<ReciboDetND>();

				// Copiar notas de crédito seleccionadas
				for (int i = 0; i < _ndsSeleccionadas.size(); i++)
					ccc.add(_ndsSeleccionadas.get(i));

				// Actualizar detalle de facturas
				recibo.setNotasDebitoRecibo(ccc);
				// documents.addAll(ccc);
			}
			// loadData(true);
			// CalculaTotales();
			// actualizaTotales();
			// adapter = null;
			// agregarDocumentosAlDetalleDeRecibo();
			// actualizaTotales();
		} catch (Exception e) {
			notasDebitoRecibo = ndr;
			facturasRecibo = fr;
			documents = d;

			notasDebitoRecibo.clear();
			notasCreditoRecibo.clear();
			facturasRecibo.clear();
			documents.clear();
			recibo.setFacturasRecibo(rdf);
			recibo.setNotasDebitoRecibo(rdnd);
			recibo.setNotasCreditoRecibo(rdnc);
			adapter = null;

			Cobro.calcularDetFacturasRecibo(contexto, recibo,
					recibo.getCliente(), true);
			agregarDocumentosAlDetalleDeRecibo();
			Cobro.ActualizaTotalFacturas(recibo);
			Cobro.ActualizaTotalNotasCredito(recibo);
			Cobro.ActualizaTotalNotasDebito(recibo);
			actualizaTotales();
		}

	}

	private void PagarMonto() {
		AppDialog.showMessage(me, "Ingrese el monto", "",
				DialogType.DIALOGO_INPUT,
				new AppDialog.OnButtonClickListener() {
					@Override
					public void onButtonClick(AlertDialog alert, int actionId) {
						if (actionId == AppDialog.OK_BUTTOM) {
							float amount = 0;
							amount = Float.parseFloat(((TextView) alert
									.findViewById(R.id.txtpayamount)).getText()
									.toString());
							payamount(amount);

						}
					}
				});
	}

	private void payamount(float mto) {
		// Declaracion de variables ;
		float mtoOrig = mto;
		boolean seguir = true;
		boolean primeraPasada = true;
		ArrayList<ReciboDetFactura> _facSeleccionadas = new ArrayList<ReciboDetFactura>();
		ArrayList<ReciboDetND> _ndsSeleccionadas = new ArrayList<ReciboDetND>();
		ArrayList<Factura> facturas = new ArrayList<Factura>();
		ArrayList<CCNotaDebito> notas = new ArrayList<CCNotaDebito>();

		List<CCNotaDebito> ndr = new ArrayList<CCNotaDebito>(notasDebitoRecibo);
		List<Factura> fr = new ArrayList<Factura>(facturasRecibo);
		List<com.panzyma.nm.serviceproxy.Documento> d = new ArrayList<com.panzyma.nm.serviceproxy.Documento>(
				documents);
		ArrayList<ReciboDetFactura> rdf = new ArrayList<ReciboDetFactura>(
				recibo.getFacturasRecibo());
		ArrayList<ReciboDetND> rdnd = new ArrayList<ReciboDetND>(
				recibo.getNotasDebitoRecibo());
		ArrayList<ReciboDetNC> rdnc = new ArrayList<ReciboDetNC>(
				recibo.getNotasCreditoRecibo());

		try {

			while (seguir) {
				// Declaración de las listas principales
				cliente = (Cliente) NMApp
						.getController()
						.getBridge()
						.getClass()
						.getMethod("getClienteBySucursalID",
								ContentResolver.class, long.class)
						.invoke(null, getContentResolver(),
								cliente.getIdSucursal());

				Factura[] facturaspendientes = cliente.getFacturasPendientes();
				if ((facturaspendientes != null)
						&& (facturaspendientes.length > 0)) {
					for (int i = 0; i < facturaspendientes.length; i++) {
						if (Cobro.FacturaEstaEnOtroRecibo(
								me.getContentResolver(),
								facturaspendientes[i].Id, true) == 0) {
							facturas.add(facturaspendientes[i]);
						}
					}
				}

				CCNotaDebito[] notaspendientes = cliente
						.getNotasDebitoPendientes();
				if ((notaspendientes != null) && (notaspendientes.length > 0)) {
					for (int i = 0; i < notaspendientes.length; i++) {
						if (Cobro.NDEstaEnOtroRecibo(me.getContentResolver(),
								notaspendientes[i].getId(), true) == 0) {
							notas.add(notaspendientes[i]);
						}
					}
				}

				if (((facturas == null) || (facturas.size() == 0))
						&& ((notas == null) || (notas.size() == 0)))
					return;
				// Añadimos a la lista

				notasDebitoRecibo.clear();
				notasCreditoRecibo.clear();
				facturasRecibo.clear();
				documents.clear();

				recibo.getFacturasRecibo().clear();
				recibo.getNotasDebitoRecibo().clear();
				recibo.getNotasCreditoRecibo().clear();
				recibo.getFormasPagoRecibo().clear(); 
				recibo.setTotalFacturas(0.00F);
				recibo.setTotalND(0.00F);
				recibo.setSubTotal(0.00F);
				recibo.setTotalDesc(0.00F);
				recibo.setTotalImpuestoExonerado(0.00F);
				recibo.setTotalRetenido(0.00F);
				recibo.setTotalOtrasDed(0.00F);
				recibo.setTotalInteres(0.00F);
				recibo.setTotalRecibo(0.00F);

				ArrayList<FacND> list = new ArrayList<FacND>();
				for (Factura factura : facturas) {
					FacND fnd = new FacND();
					fnd.fac = factura;
					fnd.fechaVence = factura.getFechaVencimiento();
					fnd.tipo = "FAC";
					list.add(fnd);
				}
				// Añadimos la Lista
				for (CCNotaDebito nota : notas) {
					FacND fnd = new FacND();
					fnd.nd = nota;
					fnd.fechaVence = nota.getFechaVence();
					fnd.tipo = "ND";
					list.add(fnd);
				}
				// ordenando vector por fecha de vencimiento (descendente)
				Collections.sort(list, new Comparator<FacND>() {
					@Override
					public int compare(FacND item1, FacND item2) {
						return String.valueOf(item1.fechaVence).compareTo(
								String.valueOf(item2.fechaVence));
					}
				});
				String interes = getSharedPreferences("SystemParams",
						android.content.Context.MODE_PRIVATE).getString(
						"PorcInteresMoratorio", "0");
				// Procesando documentos ordenados
				for (FacND fnd : list) {
					if (fnd.tipo == "FAC") {
						Factura _fac = fnd.fac;
						float mtoInteres = Cobro.getInteresMoratorio(me,
								_fac.getFechaVencimiento(), _fac.getSaldo());
						float saldoTotalFac = _fac.getSaldo() + mtoInteres;
						float mtoFac = (saldoTotalFac <= mto) ? saldoTotalFac
								: mto;
						mto -= mtoFac;

						ReciboDetFactura _facRecibo = new ReciboDetFactura();
						_facRecibo.setId(0);
						_facRecibo.setObjFacturaID(_fac.getId());
						_facRecibo.setNumero(_fac.getNoFactura());
						_facRecibo.setFecha(_fac.getFecha());
						_facRecibo.setFechaVence(_fac.getFechaVencimiento());
						_facRecibo.setFechaAplicaDescPP(_fac
								.getFechaAppDescPP());
						_facRecibo.setImpuesto(_fac.getImpuestoFactura());
						_facRecibo.setMontoImpuesto(0.0F); // Este es el
															// impuesto
															// proporcional
						// Calcular el interés moratorio de la factura si está
						// en mora
						_facRecibo.setInteresMoratorio(Float
								.parseFloat(interes));
						_facRecibo.setMontoInteres(mtoInteres);
						_facRecibo.setMontoDescEspecifico(0.0F);
						_facRecibo.setPorcDescPromo(0.0F);
						_facRecibo.setMontoDescPromocion(0.0F);
						_facRecibo.setPorcDescOcasional(0.0F);
						_facRecibo.setMontoDescOcasional(0.0F);
						_facRecibo.setMontoNeto(0.0F);
						_facRecibo.setMontoOtrasDeducciones(0.0F);
						_facRecibo.setMontoRetencion(0.0F);
						_facRecibo.setSaldoFactura(_fac.getSaldo());
						_facRecibo.setSaldoTotal(saldoTotalFac);
						_facRecibo.setMonto(mtoFac);
						_facRecibo.setSubTotal(_fac.getSubtotalFactura()
								- _fac.getDescuentoFactura());
						_facRecibo.setTotalFactura(_fac.getTotalFacturado());
						_facRecibo
								.setEsAbono(_facRecibo.getMonto() < _facRecibo
										.getSaldoTotal());
						// Agregarla a facturas seleccionadasha
						_facSeleccionadas.add(_facRecibo);
						List<Ammount> montos = new ArrayList<Ammount>();
						montos.add(new Ammount(AmmountType.ABONADO, mtoFac,
								true));
						procesaFactura(_facRecibo, fnd.fac, montos, true);

						if (mto <= 0)
							break;

						continue;
					} // FAC
					if (fnd.tipo == "ND") {
						CCNotaDebito _nd = fnd.nd;
						float mtoInteres = Cobro.getInteresMoratorio(me,
								_nd.getFechaVence(), _nd.getSaldo());
						float saldoTotalND = _nd.getSaldo() + mtoInteres;
						float mtoND = (saldoTotalND <= mto) ? saldoTotalND
								: mto;
						mto -= mtoND;

						ReciboDetND _ndRecibo = new ReciboDetND();
						_ndRecibo.setId(0);
						_ndRecibo.setObjNotaDebitoID(_nd.getId());
						_ndRecibo.setNumero(_nd.getNumero());
						_ndRecibo.setFecha(_nd.getFecha());
						_ndRecibo.setFechaVence(_nd.getFechaVence());
						_ndRecibo.setMontoInteres(mtoInteres);
						_ndRecibo
								.setInteresMoratorio(Float.parseFloat(interes));
						_ndRecibo.setMontoND(_nd.getMonto());
						_ndRecibo.setSaldoND(_nd.getSaldo());
						_ndRecibo.setSaldoTotal(saldoTotalND);
						_ndRecibo.setMontoPagar(mtoND);
						_ndRecibo.setMontoNeto(_ndRecibo.getMontoPagar()
								- _ndRecibo.getMontoInteres());
						_ndRecibo
								.setEsAbono(_ndRecibo.getMontoPagar() < _ndRecibo
										.getSaldoTotal());
						List<Ammount> montos = new ArrayList<Ammount>();
						montos.add(new Ammount(AmmountType.ABONADO, mtoND, true));
						procesaNotaDebito(_ndRecibo, fnd.nd, montos, true);
						_ndsSeleccionadas.add(_ndRecibo);
						if (mto <= 0)
							break;

					}// FIn ND
				}
				// if (_facSeleccionadas.size() > 0) {
				// //Insertar nuevas facturas en el detalle de facturas del
				// recibo
				// ArrayList<ReciboDetFactura> fff = new
				// ArrayList<ReciboDetFactura>();
				// for (ReciboDetFactura facturaseleccionada :
				// _facSeleccionadas) {
				// fff.add(facturaseleccionada);
				// }
				// //Actualizar detalle de facturas
				// recibo.setFacturasRecibo(fff);
				// //documents.addAll(fff);
				//
				// }
				// if (_ndsSeleccionadas.size() > 0) {
				// //Insertar nuevas ncs en el detalle de ncs del recibo
				// ArrayList<ReciboDetND> ccc = new ArrayList<ReciboDetND>();
				// for (ReciboDetND ndseleccionada : _ndsSeleccionadas) {
				// ccc.add(ndseleccionada);
				// }
				// recibo.setNotasDebitoRecibo(ccc);
				// //documents.addAll(ccc);
				// }

				// Cobro.calcularDetFacturasRecibo(me,recibo, cliente, true);
				// CalculaTotales();

				// Ver si hay monto de descuento que quede de remanente
				seguir = false;
				if (primeraPasada && (recibo.getTotalDesc() > 0)) {
					mto = mtoOrig + recibo.getTotalDesc();
					seguir = true;
					primeraPasada = false;
				}

			}

			// notasDebitoRecibo.addAll(notas);
			// facturasRecibo.addAll(facturas);
			// documents.addAll(recibo.getNotasDebitoRecibo());
			// documents.addAll(recibo.getFacturasRecibo());
			// adapter = null;
			// agregarDocumentosAlDetalleDeRecibo();
			// actualizaTotales();

		} catch (Exception e) {

			notasDebitoRecibo = ndr;
			facturasRecibo = fr;
			documents = d;

			notasDebitoRecibo.clear();
			notasCreditoRecibo.clear();
			facturasRecibo.clear();
			documents.clear();
			recibo.setFacturasRecibo(rdf);
			recibo.setNotasDebitoRecibo(rdnd);
			recibo.setNotasCreditoRecibo(rdnc);
			adapter = null;
			agregarDocumentosAlDetalleDeRecibo();
			Cobro.ActualizaTotalFacturas(recibo);
			Cobro.ActualizaTotalNotasCredito(recibo);
			Cobro.ActualizaTotalNotasDebito(recibo);
			actualizaTotales();

		}

	}

	private void CalculaTotales() {
		double totalInteresFac = 0;
		double totalFacturas = 0;
		double totalND = 0;
		double totalNC = 0;
		double totalDescOca = 0;
		double totalDescPromo = 0;
		double totalDescPP = 0;
		double totalRetencion = 0;
		double totalOtro = 0;
		double totalInteresND = 0;
		double totalImpProp = 0;
		double totalImpExe = 0;

		if (recibo.getFacturasRecibo() != null) {
			ArrayList<ReciboDetFactura> _ff = recibo.getFacturasRecibo();
			if (_ff != null) {
				for (ReciboDetFactura reciboDetFactura : _ff) {
					totalInteresFac += reciboDetFactura.getMontoInteres();
					totalFacturas += reciboDetFactura.getMonto();
					totalDescOca += reciboDetFactura.getMontoDescOcasional();
					totalDescPromo += reciboDetFactura.getMontoDescPromocion();
					totalDescPP += reciboDetFactura.getMontoDescEspecifico();
					totalRetencion += reciboDetFactura.getMontoRetencion();
					totalOtro += reciboDetFactura.getMontoOtrasDeducciones();
					totalImpProp += reciboDetFactura.getMontoImpuesto();
					totalImpExe += reciboDetFactura.getMontoImpuestoExento();
				}
			}
		}

		if (recibo.getNotasDebitoRecibo() != null) {
			ArrayList<ReciboDetND> _dd = recibo.getNotasDebitoRecibo();
			if (_dd != null) {
				for (ReciboDetND reciboDetND : _dd) {
					totalND += reciboDetND.getMontoPagar();
					totalInteresND += reciboDetND.getMontoInteres();
				}
			}
		}

		if (recibo.getNotasCreditoRecibo() != null) {
			ArrayList<ReciboDetNC> _cc = recibo.getNotasCreditoRecibo();
			if (_cc != null) {
				for (ReciboDetNC reciboDetNC : _cc) {
					totalNC += reciboDetNC.getMonto();
				}
			}
		}

		totalInteresFac = StringUtil.round(totalInteresFac, 2);
		totalFacturas = StringUtil.round(totalFacturas, 2);
		totalDescOca = StringUtil.round(totalDescOca, 2);
		totalDescPromo = StringUtil.round(totalDescPromo, 2);
		totalDescPP = StringUtil.round(totalDescPP, 2);
		totalRetencion = StringUtil.round(totalRetencion, 2);
		totalOtro = StringUtil.round(totalOtro, 2);
		totalImpProp = StringUtil.round(totalImpProp, 2);
		totalImpExe = StringUtil.round(totalImpExe, 2);
		totalND = StringUtil.round(totalND, 2);
		totalInteresND = StringUtil.round(totalInteresND, 2);
		totalNC = StringUtil.round(totalNC, 2);

		recibo.setTotalFacturas((float) StringUtil.round(totalFacturas
				- totalInteresFac, 2));
		recibo.setTotalND((float) StringUtil.round(totalND - totalInteresND, 2));
		recibo.setTotalInteres((float) StringUtil.round(totalInteresFac
				+ totalInteresND, 2));
		recibo.setSubTotal((float) StringUtil.round(totalFacturas + totalND, 2));
		recibo.setTotalNC((float) StringUtil.round(totalNC, 2));
		recibo.setTotalDesc((float) StringUtil.round(totalDescOca
				+ totalDescPromo + totalDescPP, 2));
		recibo.setTotalRetenido((float) StringUtil.round(totalRetencion, 2));
		recibo.setTotalOtrasDed((float) StringUtil.round(totalOtro, 2));
		recibo.setTotalDescOca((float) StringUtil.round(totalDescOca, 2));
		recibo.setTotalDescPP((float) StringUtil.round(totalDescPP, 2));
		recibo.setTotalDescPromo((float) StringUtil.round(totalDescPromo, 2));
		recibo.setTotalImpuestoProporcional((float) StringUtil.round(
				totalImpProp, 2));
		recibo.setTotalImpuestoExonerado((float) StringUtil.round(totalImpExe,
				2));

		double netoPago = totalFacturas + totalND;
		netoPago = netoPago - totalNC;
		netoPago = netoPago - totalDescOca;
		netoPago = netoPago - totalDescPromo;
		netoPago = netoPago - totalDescPP;
		netoPago = netoPago - totalRetencion;
		netoPago = netoPago - totalOtro;
		netoPago = netoPago - totalImpExe;

		netoPago = StringUtil.round(netoPago, 2);

		// recibo.setTotalRecibo((float)netoPago);

		String snp = netoPago + "";
		float np = Float.parseFloat(snp);
		recibo.setTotalRecibo(np);
	} // CalculaTotales

	public void showStatus(final NotificationMessage notificacion) {
		if (dlg != null)
			dlg.dismiss();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dlg = new CustomDialog(me, notificacion.getMessage()
						+ notificacion.getCause(), false, NOTIFICATION_DIALOG);
				dlg.show();
			}
		});
	}

	public  void showStatus(final String mensaje, boolean... confirmacion) {

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
										_dialog.dismiss(); 
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

	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        //drawerToggle.onConfigurationChanged(newConfig);
	        LayoutInflater inflater = LayoutInflater.from(this);
	        populateViewForOrientation(inflater, (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
	 }
	 
	 private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
	        viewGroup.removeAllViewsInLayout();
	        _view= inflater.inflate(R.layout.recibo_edit, viewGroup);
	        initComponent();
	        agregarDocumentosAlDetalleDeRecibo();
	}
	 
	 public void updateResult(String montopagado) {
		 
		 txtmonto.setText(montopagado);
		  
	}
	
}
