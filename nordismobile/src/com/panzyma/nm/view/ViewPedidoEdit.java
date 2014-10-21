package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.C_INVETORY_UPDATED;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle.Control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NotificationMessage;
import com.panzyma.nm.auxiliar.NumberUtil;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.interfaces.Editable;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PedidoPromocion;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Promocion;
import com.panzyma.nm.serviceproxy.Promociones;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.PProductoViewHolder;
import com.panzyma.nm.viewdialog.ConsultaBonificacionesProducto;
import com.panzyma.nm.viewdialog.ConsultaPrecioProducto;
import com.panzyma.nm.viewdialog.DetalleProducto;
import com.panzyma.nm.viewdialog.DetalleProducto.OnButtonClickHandler;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewdialog.DialogCondicionesNotas;
import com.panzyma.nm.viewdialog.DialogProducto;
import com.panzyma.nm.viewdialog.DialogPromociones;
import com.panzyma.nm.viewdialog.ExonerarImpuesto;
import com.panzyma.nordismobile.R;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
@SuppressWarnings({ "unchecked", "rawtypes", "unused", "deprecation","static-access" })
@InvokeBridge(bridgeName = "BPedidoM")
public class ViewPedidoEdit extends FragmentActivity implements
		Handler.Callback, Editable {
	private static CustomDialog dlg;
	public EditText tbxFecha;
	public EditText tbxNumReferencia;
	public EditText tbxNumPedido;
	public EditText tbxNombreDelCliente;
	public Spinner tbxTipoVenta;
	public TextView tbxTotalFact;

	public View gridDetallePedido;
	private ListView grid_dp;
	TextView gridheader;

	private GenericAdapter adapter;
	private ProgressDialog pd;
	private Button Menu;
	private QuickAction quickAction;
	private QuickAction quickAction2;
	private int positioncache = -1;
	private Display display;
	private static final String TAG = ViewPedidoEdit.class.getSimpleName();
	private ViewPedidoEdit me;
	private Pedido pedido;
	private Cliente cliente;
	ArrayList<DetallePedido> Lvmpproducto;
	ArrayList<Producto> aprodselected;
	private DetallePedido dpselected;
	// Totales del pedido
	private float subTotal = 0.00f;
	private float descuento = 0.00f;
	private float impuesto = 0.00f;
	private float total = 0.00f;

	private NMApp NMApp;
	private boolean invActualizado;
	private static final int ID_SELECCIONAR_CLIENTE = 1;
	private static final int ID_CONSULTAR_CUENTAS_X_COBRAR = 2;
	private static final int ID_AGREGAR_PRODUCTOS = 3;
	private static final int ID_EDITAR_PRODUCTO = 4;
	private static final int ID_ELIMINAR_PRODUCTO = 5;
	private static final int ID_CONSULTAR_BONIFICACIONES = 6;
	private static final int ID_CONSULTAR_LISTA_PRECIOS = 7;
	private static final int ID_CONDICIONES_Y_NOTAS = 8;
	private static final int ID_APLICAR_PROMOCIONES = 9;
	private static final int ID_DESAPLICAR_PROMOCIONES = 10;
	private static final int ID_EXONERAR_IMPUESTO = 11;
	private static final int ID_GUARDAR = 12;
	private static final int ID_ENVIAR = 13;
	private static final int ID_IMPRIMIR_COMPROBANTE = 14;
	private static final int ID_CERRAR = 15;
	private static String TAG_IMPUESTO = "";
	BPedidoM bpm;
	private static Object lock = new Object();
	AlertDialog ad;
	private Handler handler = new Handler();
	private boolean salvado;
	private Bundle extras;
	private boolean onEdit = false;
	private boolean onNew;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedido_edit);

		try 
		{
			SessionManager.setContext(this);
			com.panzyma.nm.NMApp.getController().setView(this);
			aprodselected = new ArrayList<Producto>();
			me = this;
			
			pedido = (getIntent().getParcelableExtra("pedido") != null) ? (Pedido) getIntent()
					.getParcelableExtra("pedido") : null;
			Lvmpproducto = new ArrayList<DetallePedido>();
			if (pedido != null) {
				DetallePedido[] detPed = pedido.getDetalles();
				for (int i = 0; i < detPed.length; i++)
					Lvmpproducto.add(detPed[i]);
				onEdit = true;
				cliente = Ventas.getClienteBySucursalID(
						pedido.getObjSucursalID(), me.getContentResolver());
			}
			// BUscamos si
			if (getIntent().hasExtra("cliente")) {
				long IdCliente = getIntent().getLongExtra("cliente", 0);
				cliente = Ventas.getClienteBySucursalID(IdCliente,
						me.getContentResolver());
			}
			onNew = !onEdit;
			WindowManager wm = (WindowManager) this.getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			initComponent();

		} catch (Exception e) {
			showStatus(e.getMessage() + "\nCausa: " + e.getCause(), true);
		}

	}

	public void initComponent() 
	{
		salvado = false;
		gridDetallePedido = findViewById(R.id.pddgrilla);
		grid_dp = (ListView) (findViewById(R.id.pddgrilla))
				.findViewById(R.id.data_items);
		// LinearLayout grilla=(LinearLayout) findViewById(R.id.pddgrilla);
		gridheader = (TextView) gridDetallePedido.findViewById(R.id.header);
		gridheader.setText("Productos a Facturar(0)");
		tbxFecha = (EditText) findViewById(R.id.pddetextv_detalle_fecha);
		tbxNumReferencia = (EditText) findViewById(R.id.pdtv_detalle_numref);
		tbxNumPedido = (EditText) findViewById(R.id.pdddetextv_detalle_num);
		tbxNombreDelCliente = (EditText) findViewById(R.id.pddtextv_detallecliente);
		tbxTotalFact = (TextView) findViewById(R.id.pddtextv_detalletotales);
		tbxTipoVenta = (Spinner) findViewById(R.id.pddcombox_detalletipo);
		tbxFecha.setEnabled(false);
		grid_dp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if ((parent.getChildAt(positioncache)) != null)
					(parent.getChildAt(positioncache))
							.setBackgroundResource(android.R.color.transparent);
				positioncache = position;
				dpselected = (DetallePedido) adapter.getItem(position);
				adapter.setSelectedPosition(position);
				view.setBackgroundDrawable(parent.getResources().getDrawable(
						R.drawable.action_item_selected));
				
			}

		});

		grid_dp.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				if ((parent.getChildAt(positioncache)) != null)
					(parent.getChildAt(positioncache))
							.setBackgroundResource(android.R.color.transparent);
				positioncache = position;
				grid_dp.setItemChecked(position, true); 
				dpselected = (DetallePedido) adapter.getItem(position);
				adapter.setSelectedPosition(position);
				view.setBackgroundDrawable(parent.getResources().getDrawable(
						R.drawable.action_item_selected));
				showMenu(view);

				return true;
			}
		});

		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
				R.array.tipopedido, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tbxTipoVenta.setAdapter(adapter2);
		tbxTipoVenta.setSelection(1);
		tbxTipoVenta.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentview,
					View selectedItemView, int pos, long id) {
				pedido.setTipo(pos == 0 ? "CO" : "CR");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		if (pedido == null) {
			pedido = new Pedido();
			if (getIntent().hasExtra("cliente")) {
				setInformacionCliente();
			} else
				cliente = null;
			pedido.setCodEstado("REGISTRADO");
			pedido.setId(0);
			pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));

			pedido.setNumeroCentral(0);
			pedido.setNumeroMovil(0);
			// pedido.setObjVendedorID(SessionManager.getLoginUser().getId());
			pedido.setTipo("CR"); // Crédito
			pedido.setExento(false);
			pedido.setAutorizacionDGI("");
		}

		TAG_IMPUESTO = "Exonerar Impuesto";
		if (pedido.isExento())
			TAG_IMPUESTO = "Aplicar Impuesto";

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		formato.setCalendar(Calendar.getInstance());
		long date = DateUtil.dt2i(Calendar.getInstance().getTime());

		if (pedido.getFecha() == 0)
			pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));

		tbxNumReferencia.setText(NumberUtil.getFormatoNumero(
				(pedido.getNumeroMovil() > 0) ? pedido.getNumeroMovil() : 0,
				me.getContext()));

		tbxNumPedido
				.setText(NumberUtil.getFormatoNumero(
						(pedido.getNumeroCentral() > 0) ? pedido
								.getNumeroCentral() : 0, me.getContext()));

		if (pedido.getNombreCliente() != null)
			tbxNombreDelCliente.setText(pedido.getNombreCliente());

		if (pedido.getTipo().compareTo("CO") == 0)
			tbxTipoVenta.setSelection(0);
		else
			tbxTipoVenta.setSelection(1);

		tbxFecha.setText("" + DateUtil.idateToStrYY(pedido.getFecha()));
		CalculaTotales();
		setTotales(false);

		gridheader.setText("Productos a Facturar(0)");
		adapter = new GenericAdapter(this, PProductoViewHolder.class,
				Lvmpproducto, R.layout.gridproductosavender);
		grid_dp.setAdapter(adapter);
		adapter.setSelectedPosition(0);
		
		if (Lvmpproducto.size() > 0)
			dpselected = Lvmpproducto.get(0);
		gridheader.setText("Productos a Facturar(" + adapter.getCount() + ")");

		if (pedido != null && !pedido.getCodEstado().equals("REGISTRADO") && !pedido.getCodEstado().equals("APROBADO")) 
		{
			grid_dp.setEnabled(false);
			tbxFecha.setEnabled(false);
			tbxNumReferencia.setEnabled(false);
			tbxNumPedido.setEnabled(false); 
			tbxNombreDelCliente.setEnabled(false);
			tbxTipoVenta.setEnabled(false);
			tbxTotalFact.setEnabled(false);			
		}
		
		initMenu();
	}

	public void showMenu(final View view) 
	{

		runOnUiThread(new Runnable() 
		{
			@Override
			public void run() 
			{
				if (pedido != null && pedido.getCodEstado().equals("REGISTRADO")) 
				{
					quickAction2 = new QuickAction(me, QuickAction.VERTICAL, 1);
					quickAction2.addActionItem(new ActionItem(ID_EDITAR_PRODUCTO,"Editar Producto"));
					quickAction2.addActionItem(new ActionItem(ID_ELIMINAR_PRODUCTO,
							"Eliminar Productos"));
					quickAction2
							.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
	
								@Override
								public void onItemClick(QuickAction source,
										final int pos, final int actionId) {
									ActionItem actionItem = quickAction2
											.getActionItem(pos);
									if (actionId == ID_EDITAR_PRODUCTO)
										editarProducto();
									else if (actionId == ID_ELIMINAR_PRODUCTO)
										eliminarProducto();
	
								}
							});
					quickAction2.show(view, display, false);
				}
			}
		});
	}

	public void initMenu() 
	{
		quickAction = new QuickAction(me, QuickAction.VERTICAL, 1);
		if (pedido != null && !pedido.getCodEstado().equals("REGISTRADO") && !pedido.getCodEstado().equals("APROBADO")) 
		{
			quickAction.addActionItem(new ActionItem(ID_IMPRIMIR_COMPROBANTE,
					"Imprimir Comprobante"));
			quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));
		} 
		else 
		{
			quickAction.addActionItem(new ActionItem(ID_SELECCIONAR_CLIENTE,
					"Seleccionar Cliente"));
			quickAction.addActionItem(new ActionItem(ID_AGREGAR_PRODUCTOS,
					"Agregar Productos"));
			quickAction.addActionItem(new ActionItem(ID_CONDICIONES_Y_NOTAS,
					"Agregar Condición Y Nota"));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_EDITAR_PRODUCTO,
					"Editar Producto"));
			quickAction.addActionItem(new ActionItem(ID_ELIMINAR_PRODUCTO,
					"Eliminar Productos"));
			quickAction.addActionItem(null);
//			quickAction
//					.addActionItem(new ActionItem(
//							ID_CONSULTAR_CUENTAS_X_COBRAR,
//							"Consultar Cuentas X Cobrar"));
			quickAction.addActionItem(new ActionItem(
					ID_CONSULTAR_BONIFICACIONES, "Consultar Bonificaciones"));
			quickAction.addActionItem(new ActionItem(
					ID_CONSULTAR_LISTA_PRECIOS, "Consultar Lista de Precios"));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_APLICAR_PROMOCIONES,
					"Aplicar Promociones"));
			quickAction.addActionItem(new ActionItem(ID_DESAPLICAR_PROMOCIONES,
					"Des Aplicar Promociones"));
			quickAction.addActionItem(new ActionItem(ID_EXONERAR_IMPUESTO,
					TAG_IMPUESTO));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_GUARDAR, "Guardar"));
			quickAction.addActionItem(new ActionItem(ID_ENVIAR, "Enviar"));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_IMPRIMIR_COMPROBANTE,
					"Imprimir Comprobante"));
			quickAction.addActionItem(null);
			quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));
		}
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
				{
					@Override
					public void onItemClick(QuickAction source, final int pos,
							final int actionId) {

						runOnUiThread(new Runnable() 
						{
							@Override
							public void run() 
							{

								try 
								{

									ActionItem actionItem = quickAction
											.getActionItem(pos);
									if (actionId == ID_SELECCIONAR_CLIENTE)
										seleccionarCliente();
									else if (actionId == ID_AGREGAR_PRODUCTOS)
										agregarProducto();
									else if (actionId == ID_CONDICIONES_Y_NOTAS)
										agregarCondicionesYNotas();
									else if (actionId == ID_EDITAR_PRODUCTO)
										editarProducto();
									else if (actionId == ID_ELIMINAR_PRODUCTO)
										eliminarProducto();
									else if (actionId == ID_CONSULTAR_BONIFICACIONES)
										consultarBonificaciones();
									else if (actionId == ID_CONSULTAR_LISTA_PRECIOS)
										consultarPrecioProducto();
									else if (actionId == ID_APLICAR_PROMOCIONES)
										aplicarPromociones(true);
									else if (actionId == ID_DESAPLICAR_PROMOCIONES)
										desaplicarPromociones();
									else if (actionId == ID_EXONERAR_IMPUESTO)
										exonerarDeImpuesto();
									else if (actionId == ID_GUARDAR)
										salvarPedido();
									else if (actionId == ID_ENVIAR)
										enviarPedido();
									else if (actionId == ID_IMPRIMIR_COMPROBANTE)
										ImprimirComprobante();
									else if (actionId == ID_CERRAR)
										FINISH_ACTIVITY();
									else {
										Toast.makeText(
												getApplicationContext(),
												actionItem.getTitle()
														+ " selected",
												Toast.LENGTH_SHORT).show();
									}
								} catch (Exception e) {
									showStatus(
											e.getMessage() + "\nCausa: "
													+ e.getCause(), true);
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

	public void setTotales(boolean needaThread) {

		String descTotales = "ST: " + StringUtil.formatReal(subTotal)
				+ " | Desc: " + StringUtil.formatReal(descuento) + " | IVA: "
				+ StringUtil.formatReal(impuesto) + "  |  Total: "
				+ StringUtil.formatReal(total);

		final SpannableString sb = new SpannableString(descTotales);
		final ForegroundColorSpan color = new ForegroundColorSpan(Color.rgb(
				255, 0, 0));

		// Span to set text color to some RGB value
		// final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

		// Span to make text bold
		int start = 0, start2 = 0, end = 0, end2;

		String st = "ST:";
		String desc = "Desc:";
		String iva = "IVA:";
		String total = "Total:";

		start = descTotales.indexOf(st);
		end = descTotales.indexOf(st) + st.length();
		start2 = descTotales.indexOf(desc) - 2;
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				start2, 0);

		start = descTotales.indexOf(desc);
		end = descTotales.indexOf(desc) + desc.length();
		start2 = descTotales.indexOf(iva) - 2;
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				start2, 0);

		start = descTotales.indexOf(iva);
		end = descTotales.indexOf(iva) + iva.length();
		start2 = descTotales.indexOf(total) - 2;
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				start2, 0);

		start = descTotales.indexOf(total);
		end = descTotales.indexOf(total) + total.length();
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				descTotales.length(), 0);

		if (needaThread) {

			handler.post(new Runnable() {

				@Override
				public void run() {
					tbxTotalFact.setText(sb);
					gridheader.setText("Productos a Facturar("
							+ adapter.getCount() + ")");
				}
			});

		} else
			tbxTotalFact.setText(sb);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Menu = (Button) gridDetallePedido.findViewById(R.id.btnmenu);
			quickAction.show(Menu, display, true);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FINISH_ACTIVITY();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean handleMessage(Message msg) {

		ocultarDialogos();
		switch (msg.what) {
		case C_DATA:

			setData((ArrayList<Pedido>) ((msg.obj == null) ? new ArrayList<Pedido>()
					: msg.obj), C_DATA);
			return true;
		case C_INVETORY_UPDATED:
			return true;

		case ControllerProtocol.NOTIFICATION:
			if (ControllerProtocol.SAVE_DATA_FROM_LOCALHOST == msg.arg1) {
				actualizarOnUINumRef();
				salvado = true;
			}
			showStatus(msg.obj.toString(), true);
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			showStatus(msg.obj.toString());
			break;
		case ControllerProtocol.ERROR:
			AppDialog.showMessage(me, ((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			break;
		case ControllerProtocol.ID_REQUEST_ENVIARPEDIDO:

			resultadoEnvioPedido(msg.obj);

			salvado = true;
			break;
		case ControllerProtocol.ID_REQUEST_PROMOCIONES:
			actualizarDetallePedido();
			AppDialog.showMessage(me,
					msg.arg1 == 1 ? "Las promociones han sido aplicadas."
							: "Las Promociones fueron aplicadas",
					DialogType.DIALOGO_ALERTA);
			break;
		case ControllerProtocol.SALVARPEDIDOANTESDEPROMOCIONES:
			aplicarPromociones();
			break;
		case ControllerProtocol.APLICARPEDIDOPROMOCIONES:
			showStatus(msg.obj.toString(), true);
			actualizarDetallePedido();
			break;
		case ControllerProtocol.DESAPLICARPEDIDOPROMOCIONES:
			actualizarDetallePedido();
			showStatus(msg.obj.toString(), true);
			break;
		}

		return false;
	}

	public void setData(ArrayList<Pedido> Lpedido, int what) {
	}

	public void resultadoEnvioPedido(Object obj) {
		Object pedd = ((ArrayList<Object>) obj).get(0);
		Object clte = ((ArrayList<Object>) obj).get(1);

		if (pedd != null)
			pedido = (Pedido) pedd;

		if (clte != null)
			cliente = (Cliente) clte;

		actualizarOnUINumRef(pedido);

		final String sms = (pedido.getCodEstado().compareTo("FACTURADO") == 0) ? "El pedido ha sido enviado y facturado \n¿Desea imprimir el recibo?"
				: "El pedido ha sido enviado.Estado: " + pedido.getDescEstado()
						+ "\r\nCausa: " + pedido.getDescCausaEstado();
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

								if (pedido.getCodEstado().compareTo("FACTURADO") == 0) {
									Message msg = new Message();
									Bundle b = new Bundle();
									b.putParcelable("pedido", pedido);
									b.putParcelable("cliente", cliente);
									msg.setData(b);
									msg.what = ControllerProtocol.IMPRIMIR;
									NMApp.getController().getInboxHandler().sendMessage(msg);

								}
								_dialog.dismiss();
							}
						});
			}
		});

	}

	private void FINISH_ACTIVITY() 
	{
		int requescode = 0;
		ocultarDialogos();
		Log.d(TAG, "Activity quitting");
		Intent intent = null;
		if (pedido != null && pedido.getDetalles().length != 0
				&& pedido.getNumeroMovil() != 0 && salvado) {
			intent = new Intent();
			Bundle b = new Bundle();
			b.putParcelable("pedido", pedido);
			intent.putExtras(b);
		}
		if (onEdit)
			requescode = getIntent().getIntExtra("requestcode", 0);
		setResult(requescode, intent);
		finish();
	}

	@SuppressLint("NewApi")
	private void CalculaTotales() {
		subTotal = 0;
		descuento = 0;
		impuesto = 0;
		total = 0;
		DetallePedido[] detsPedido = new DetallePedido[Lvmpproducto.size()];
		for (int i = 0; i < Lvmpproducto.size(); i++) {
			DetallePedido dp = Lvmpproducto.get(i);
			detsPedido[i] = dp;
			subTotal += StringUtil.round(dp.getSubtotal(), 2);
			descuento += StringUtil.round(dp.getDescuento(), 2);
			impuesto += StringUtil.round(dp.getImpuesto(), 2);
		}

		pedido.setDetalles((detsPedido));
		pedido.setSubtotal(StringUtil.round(subTotal, 2));
		pedido.setDescuento(StringUtil.round(descuento, 2));
		pedido.setImpuesto(StringUtil.round(impuesto, 2));
		total = StringUtil.round(subTotal - descuento + impuesto, 2);
		pedido.setTotal(total);

		// Salvando el tipo de pedido (crédito contado)
		if (tbxTipoVenta != null) {
			pedido.setTipo("CR");
			if (((tbxTipoVenta.getSelectedItemPosition() == 0) ? "CO" : "CR") == "CO")
				pedido.setTipo("CO");
		}

		if (tbxFecha != null) {
			pedido.setFecha(DateUtil.d2i(new Date()));
		}

	}

	private void seleccionarCliente() {
		DialogCliente dc = new DialogCliente(me,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dc.setOnDialogClientButtonClickListener(new OnButtonClickListener() {

			@Override
			public void onButtonClick(Cliente _cliente) 
			{
				com.panzyma.nm.NMApp.getController().setView(me);
				if (pedido.getCodEstado().compareTo("REGISTRADO") != 0)
					return;
				cliente = _cliente;
				setInformacionCliente();
			}
		});
		Window window = dc.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dc.show();
	}

	private void agregarProducto() {
		invActualizado = true;
		if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido
				.getCodEstado().compareTo("APROBADO") == 0)))
			return;

		if (cliente == null) 
		{
			showStatus("Seleccione primero el cliente del pedido.", true);
			return;
		}

		invActualizado = true;
		DialogProducto dp = new DialogProducto(me, pedido.getCodTipoPrecio(),
				aprodselected, pedido.getId(),
				cliente.getObjCategoriaClienteID(),
				pedido.getObjTipoPrecioVentaID(),
				cliente.getObjTipoClienteID(), pedido.isExento());

		dp.setOnDialogProductButtonClickListener(new DialogProducto.OnButtonClickListener() {

			@Override
			public void onButtonClick(DetallePedido det_p, Producto prod) 
			{
				com.panzyma.nm.NMApp.getController().setView(me);
				dpselected = det_p;
				det_p.setId(pedido.getId());
				aprodselected.add(prod);
				Lvmpproducto.add(det_p);
				CalculaTotales();
				setTotales(true);
				adapter.setSelectedPosition(Lvmpproducto.size() - 1);
				adapter.notifyDataSetChanged();

			}

		});
		Window window = dp.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 10, display.getHeight() - 50);
		dp.show();

		// Actualizar dispobibilidad de inventario
		// if (!invActualizado)
		// NMApp.getController().getInboxHandler().sendEmptyMessage(UPDATE_INVENTORY_FROM_SERVER);
		// seleccionarProducto();
		// Ventas.ActualizarDisponibilidadProducto();

	}

	public void editarProducto() {
		Producto producto;
		try {

			if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido
					.getCodEstado().compareTo("APROBADO") == 0)))
				return;

			int idx = grid_dp.getCheckedItemPosition();
			
			if (idx == -1)
				return;

			DetallePedido det = Lvmpproducto.get(idx);
			if (det.getCantidadOrdenada() == 0)
				return; // No editar: es producto dado en promoción

			producto = Ventas.getProductoByID(dpselected.getObjProductoID(), me);
			DetalleProducto dp = new DetalleProducto(me, producto,det,
					cliente.getObjCategoriaClienteID(),
					pedido.getObjTipoPrecioVentaID(),
					cliente.getObjTipoClienteID(), pedido.isExento());

			dp.setOnDialogDetalleProductButtonClickListener(new OnButtonClickHandler() {

				@Override
				public void onButtonClick(DetallePedido det_p, boolean btn) 
				{
					com.panzyma.nm.NMApp.getController().setView(me);
					det_p.setId(pedido.getId());
					Lvmpproducto.set(positioncache, det_p);

					PedidoPromocion[] pproms = pedido.getPromocionesAplicadas();
					if ((pproms != null) && (pproms.length > 0)) 
					{
						Promociones.DesaplicarPromociones(pedido);
						Promociones.ActualizaPedidoDePromociones(pedido);
						Toast.makeText(me, "Las promociones han sido desaplicadas.",Toast.LENGTH_LONG).show();
					}

					CalculaTotales();
					setTotales(true);
					adapter.setSelectedPosition(positioncache);
					adapter.notifyDataSetChanged();
				}

			});
			dp.getWindow().setGravity(Gravity.CENTER);
			dp.getWindow().setGravity(Gravity.CENTER);
			dp.getWindow().setLayout(display.getWidth() - 30,display.getHeight() - 50);
			dp.show();
		} catch (Exception e) {
			showStatus(e.getMessage() + "\nCausa: " + e.getCause(), true);
			e.printStackTrace();
		}

	}

	public void eliminarProducto() {
		if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido
				.getCodEstado().compareTo("APROBADO") == 0)))
			return;
		final int idx = grid_dp.getCheckedItemPosition();

		if (idx == -1)
			return;

		// if (Dialog.ask(Dialog.D_YES_NO,
		// "¿Confirma que desea eliminar el detalle?", Dialog.YES) == Dialog.NO)

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AppDialog.showMessage(me, "",
						"¿Confirma que desea eliminar el detalle?",
						AppDialog.DialogType.DIALOGO_CONFIRMACION,
						new AppDialog.OnButtonClickListener() {
							@Override
							public void onButtonClick(AlertDialog _dialog,
									int actionId) {

								if (AppDialog.OK_BUTTOM == actionId) 
								{
									// return;
									Lvmpproducto.remove(idx);

									// Si habían promociones aplicadas,
									// desaplicarlas
									PedidoPromocion[] pproms = pedido.getPromocionesAplicadas();
									if ((pproms != null) && (pproms.length > 0)) 
									{
										Promociones.DesaplicarPromociones(pedido);
										Promociones.ActualizaPedidoDePromociones(pedido);
										showStatus("Las promociones han sido desaplicadas.",true);
									}

									CalculaTotales();
									setTotales(true);

									if (Lvmpproducto.size() > 0) {
										if (idx == 0) {
											positioncache = 0;
											dpselected = (DetallePedido) adapter
													.getItem(0);
											adapter.setSelectedPosition(0);
										} 
										else 
										{
											if (idx == Lvmpproducto.size()) {
												positioncache = idx - 1;
												dpselected = (DetallePedido) adapter
														.getItem(idx - 1);
												adapter.setSelectedPosition(idx - 1);
											} else {
												positioncache = idx;
												dpselected = (DetallePedido) adapter
														.getItem(idx);
												adapter.setSelectedPosition(idx);
											}
										}
									}
									adapter.notifyDataSetChanged();
								}
								_dialog.dismiss();
							}
						});
			}
		});
	}

	private void consultarBonificaciones() {

		if (dpselected == null || cliente == null)
			return;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag("dialog");
		if (prev != null)
			ft.remove(prev);
		ft.addToBackStack(null);
		ConsultaBonificacionesProducto cbp = ConsultaBonificacionesProducto
				.newInstance(dpselected.getObjProductoID(),
						cliente.getObjCategoriaClienteID());
		cbp.show(ft, "dialog");
	}

	private void consultarPrecioProducto() {
		if (dpselected == null || cliente == null)
			return;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		ConsultaPrecioProducto newFragment = ConsultaPrecioProducto
				.newInstance(dpselected.getObjProductoID(),
						pedido.getObjTipoPrecioVentaID());
		newFragment.show(ft, "dialog");
	}

	public void aplicarPromociones(boolean... salvar) {

		try {

			if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido
					.getCodEstado().compareTo("APROBADO") == 0)))
				return;

			if (cliente == null) {
				showStatus("Seleccione primero el cliente del pedido.", true);
				return;
			}
			if ((Lvmpproducto == null) || (Lvmpproducto.size() == 0)) {
				AppDialog.showMessage(me,
						"El pedido no tiene detalle de productos.",
						DialogType.DIALOGO_ALERTA);
				return;
			}

			if (salvar.length != 0 && salvar[0]) {
				salvarPedido(ControllerProtocol.SALVARPEDIDOANTESDEPROMOCIONES);
				showStatus("Guardando 1mero el pedido");
				return;
			}

			ArrayList<Promocion> promociones = Promociones
					.getPromocionesAplican(pedido, getContentResolver());

			if (promociones != null && promociones.size() != 0) {

				DialogPromociones dprom = new DialogPromociones(
						ViewPedidoEdit.this, pedido, promociones,
						android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

				dprom.setOnDialogPromocionesButtonClickListener(new com.panzyma.nm.viewdialog.DialogPromociones.OnButtonClickHandler() {

					@Override
					public void onButtonClick(Promocion promocion) {
						com.panzyma.nm.NMApp.getController().setView(me);	
						if (promocion != null) 
						{
							// Validar que no se haya alcanzado el máximo de
							// promociones
							// a aplicar
							PedidoPromocion[] pproms = pedido
									.getPromocionesAplicadas();
							if (pproms != null) {
								int maxPromos = Integer
										.parseInt(me
												.getApplicationContext()
												.getSharedPreferences(
														"SystemParams",
														android.content.Context.MODE_PRIVATE)
												.getString(
														"CantMaximaPromocionesAplicar",
														"0"));
								if (pproms.length >= maxPromos) {
									showStatus(
											"La promoción no puede aplicarse.\n\rSe ha alcanzado el máximo\n\rde promociones aplicables.",
											true);
									return;
								}
							}

							Promociones.aplicarPromocion(pedido, promocion,
									me.getContentResolver());
							Promociones.ActualizaPedidoDePromociones(pedido);
							if (pedido.getPromocionesAplicadas().length == 0)
								return;
							showStatus("aplicando promociones...");
							// Salvar la promoción aplicada
							salvarPedido(ControllerProtocol.APLICARPEDIDOPROMOCIONES);
						} else
							AppDialog
									.showMessage(
											me,
											"No hay Promomociones pendientes que aplicar...",
											DialogType.DIALOGO_ALERTA);

					}
				});

				Window window = dprom.getWindow();
				window.setGravity(Gravity.CENTER);
				window.setLayout(display.getWidth() - 5,
						display.getHeight() - 10);
				dprom.show();
			} else
				AppDialog.showMessage(me,
						"No hay Promomociones pendientes que aplicar...",
						DialogType.DIALOGO_ALERTA);

		} catch (Exception e) {
			AppDialog.showMessage(me,
					e.getMessage() + "\nCausa: " + e.getCause(),
					DialogType.DIALOGO_ALERTA);
		}

	}

	public void actualizarDetallePedido() {
		DetallePedido[] detPed = pedido.getDetalles();
		Lvmpproducto = new ArrayList<DetallePedido>();
		for (int i = 0; i < detPed.length; i++) {
			Lvmpproducto.add(detPed[i]);
		}

		CalculaTotales();
		setTotales(true);
		adapter.notifyDataSetChanged();

	}

	// }

	public void desaplicarPromociones() {

		if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido
				.getCodEstado().compareTo("APROBADO") == 0)))
			return;

		if (pedido.getPromocionesAplicadas() == null
				|| (pedido.getPromocionesAplicadas() == null && pedido
						.getPromocionesAplicadas().length == 0)) {

			AppDialog.showMessage(me,
					"El pedido no tiene promociones aplicadas.",
					DialogType.DIALOGO_ALERTA);
			return;
		}

		Promociones.DesaplicarPromociones(pedido);

		Promociones.ActualizaPedidoDePromociones(pedido);

		// Salvar la promoción aplicada
		showStatus("Desaplicando promociones...");
		// Salvar la promoción aplicada
		salvarPedido(ControllerProtocol.DESAPLICARPEDIDOPROMOCIONES);
	}

	public void agregarCondicionesYNotas() {
		if (pedido.getObjClienteID() == 0) {
			AppDialog.showMessage(me, "Debe agregar primero el cliente",
					DialogType.DIALOGO_ALERTA);
			return;
		}
		if (pedido.getDetalles() == null) {
			AppDialog.showMessage(me,
					"Debe agregar primero los productos a facturar",
					DialogType.DIALOGO_ALERTA);
			return;
		}
		DialogCondicionesNotas cn = new DialogCondicionesNotas(me, pedido,
				cliente,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		cn.setOnDialogCNButtonClickListener(new com.panzyma.nm.viewdialog.DialogCondicionesNotas.OnButtonClickListener() {
			@Override
			public void onButtonClick(Pedido _pedido) {	
				pedido = _pedido;
			}
		});

		Window window = cn.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		cn.show();
	}

	public void exonerarDeImpuesto() {
		try {
			if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido
					.getCodEstado().compareTo("APROBADO") == 0)))
				return;

			if (pedido.isExento()) {
				pedido.setExento(false);
				pedido.setAutorizacionDGI("");

				if (Lvmpproducto == null || Lvmpproducto.size() == 0)
					return;

				float prcImp = Float.parseFloat(me
						.getApplicationContext()
						.getSharedPreferences("SystemParams",
								android.content.Context.MODE_PRIVATE)
						.getString("PorcentajeImpuesto", "0.0"));

				for (int i = 0; i < Lvmpproducto.size(); i++) {
					DetallePedido dp = Lvmpproducto.get(i);
					Producto prod = Ventas.getProductoByID(
							dp.getObjProductoID(), me);
					dp.setPorcImpuesto(0);
					dp.setImpuesto(0);
					if (prod.isEsGravable()) {
						dp.setPorcImpuesto(prcImp);
						dp.setImpuesto(dp.getSubtotal() * dp.getPorcImpuesto()
								/ 100);
					}
					dp.setTotal(dp.getSubtotal() + dp.getImpuesto()
							- dp.getDescuento());
					Lvmpproducto.set(i, dp);
				}
				TAG_IMPUESTO = "Exonerar Impuesto";
				CalculaTotales();
				initMenu();
				return;
			}

			// Pedir autorización de la DGI
			ExonerarImpuesto ei = new ExonerarImpuesto(me);

			ei.setOnDialog_EDGI_ButtonClickListener(new ExonerarImpuesto.OnButtonClickListener() {

				@Override
				public void onButtonClick(String exoneracion) {

					pedido.setExento(true);
					pedido.setAutorizacionDGI(exoneracion);
					if (Lvmpproducto == null || Lvmpproducto.size() == 0)
						return;
					for (int i = 0; i < Lvmpproducto.size(); i++) {
						DetallePedido dp = Lvmpproducto.get(i);
						dp.setImpuesto(0);
						dp.setPorcImpuesto(0);
						dp.setTotal(dp.getSubtotal() + dp.getImpuesto()
								- dp.getDescuento());
						Lvmpproducto.set(i, dp);
					}
					TAG_IMPUESTO = "Aplicar Impuesto";
					CalculaTotales();
					setTotales(true);
					adapter.setSelectedPosition(positioncache);
					adapter.notifyDataSetChanged();
					initMenu();
				}
			});
			ei.getWindow().setGravity(Gravity.CENTER);
			ei.getWindow().setGravity(Gravity.CENTER);
			ei.getWindow().setLayout(display.getWidth() - 20,
					display.getHeight() - 280);
			ei.show();
		} catch (Exception e) {
			AppDialog.showMessage(me,
					e.getMessage() + "\nCausa: " + e.getCause(),
					DialogType.DIALOGO_ALERTA);
		}
	}

	@Override
	public BPedidoM getBridge() {
		return bpm;
	}

	public Handler getHandler() {
		return handler;
	}

	private void ImprimirComprobante() throws Exception {
		if (!isDataValid())
			return;
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putParcelable("pedido", pedido);
		b.putParcelable("cliente", cliente);
		msg.setData(b);
		msg.what = ControllerProtocol.IMPRIMIR;
		NMApp.getController().getInboxHandler().sendMessage(msg);
	}

	@Override
	public Context getContext() {

		return this.me;
	}

	public void actualizarOnUINumRef() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				tbxNumReferencia.setText(NumberUtil.getFormatoNumero(
						pedido.getNumeroMovil(), me.getApplicationContext()));
				tbxNumPedido.setText(NumberUtil.getFormatoNumero(
						pedido.getNumeroCentral(), me.getApplicationContext()));
				salvado = true;
			}
		});
	}

	public void actualizarOnUINumRef(final Pedido p) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				tbxNumReferencia.setText(NumberUtil.getFormatoNumero(
						p.getNumeroMovil(), me.getApplicationContext()));
				tbxNumPedido.setText(NumberUtil.getFormatoNumero(
						p.getNumeroCentral(), me.getApplicationContext()));
				salvado = true;
			}
		});
	}

	public String getTipoVenta() {
		return (tbxTipoVenta.getSelectedItemPosition() == 0) ? "CO" : "CR";
	}

	public String getFechaPedido() {
		return tbxFecha.getText().toString();
	}

	public boolean isDataValid() throws Exception {
		String msg = "";

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter.setCalendar(Calendar.getInstance());
		Date d = formatter.parse(DateUtil.idateToStrYY(DateUtil.dt2i(Calendar
				.getInstance().getTime())));
		Date d2 = formatter.parse(getFechaPedido());
		if (DateUtil.d2i(d2) > DateUtil.d2i(d)) {
			AppDialog.showMessage(me,
					"La fecha del pedido no debe ser mayor a la fecha actual.",
					DialogType.DIALOGO_ALERTA);
			return false;
		}

		if (tbxNombreDelCliente.getText().toString().trim() == "")
			msg = "El cliente del pedido no ha sido ingresado.";
		else {
			if ((Lvmpproducto == null) || (Lvmpproducto.size() == 0))
				msg = "El pedido no tiene detalle de productos.";
		}

		if (msg != "") {
			AppDialog.showMessage(me, msg, DialogType.DIALOGO_ALERTA);
			return false;
		}

		return comprobarPromociones();
	}

	@SuppressLint("DefaultLocale")
	private boolean comprobarPromociones() throws Exception {
		// Si al pedido no se le ha aplicado promociones
		// y la aplicación es obligatoria
		// y si hay al menos una promoción aplicable
		// no permitir enviar pedido
		PedidoPromocion[] pproms = pedido.getPromocionesAplicadas();
		if ((pproms == null) || (pproms.length == 0)) {

			String apo = me
					.getApplicationContext()
					.getSharedPreferences("SystemParams",
							android.content.Context.MODE_PRIVATE)
					.getString("AplicacionPromocionesOpcional", "FALSE");

			if (apo.toUpperCase().compareTo("FALSE") == 0) {
				if (Promociones.getPromocionesAplican(pedido,
						me.getContentResolver()).size() > 0) {
					AppDialog.showMessage(me,
							"Debe aplicarse al menos una promoción al pedido.",
							DialogType.DIALOGO_ALERTA);
					return false;
				}
			}
		}

		return true;
	}

	private void salvarPedido(int... arg) {

		try {
			if (!isDataValid())
				return;
			pedido.setTipo("CR");
			if (getTipoVenta() == "CO")
				pedido.setTipo("CO");
			String f = getFechaPedido().toString();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date d = formatter.parse(f);
			pedido.setFecha(DateUtil.d2i(d));

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putParcelable("pedido", pedido);
			msg.setData(b);
			msg.what = arg.length != 0 ? arg[0] : SAVE_DATA_FROM_LOCALHOST;
			NMApp.getController().getInboxHandler().sendMessage(msg);

		} catch (Exception e) {
			AppDialog.showMessage(me,
					e.getMessage() + "\nCausa: " + e.getCause(),
					DialogType.DIALOGO_ALERTA);
		}
	}

	@Override
	protected void onResume() {

		try 
		{
			SessionManager.setContext(me);
			com.panzyma.nm.NMApp.getController().setView(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.onResume();
	}

	private void enviarPedido() {

		try {

			if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido
					.getCodEstado().compareTo("APROBADO") == 0)))
				return;

			if (pedido.getCodEstado().compareTo("REGISTRADO") == 0) {
				if (pedido.getNumeroCentral() > 0) {
					AppDialog.showMessage(me, "El pedido ya fue enviado.",
							DialogType.DIALOGO_ALERTA);
					return;
				}
			}

			// Si se está fuera de covertura, salir
			if (!SessionManager.isPhoneConnected())
				return;
			if (!isDataValid())
				return;

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putParcelable("pedido", pedido);
			msg.setData(b);
			msg.what = ControllerProtocol.SEND_DATA_FROM_SERVER;
			NMApp.getController().getInboxHandler().sendMessage(msg);

		} catch (Exception e) {
			AppDialog.showMessage(me,
					e.getMessage() + "\nCausa: " + e.getCause(),
					DialogType.DIALOGO_ALERTA);
		}

	}

	private void setInformacionCliente() {
		tbxNombreDelCliente.setText(cliente.getNombreCliente());
		pedido.setObjClienteID(cliente.getIdCliente());
		pedido.setObjSucursalID(cliente.getIdSucursal());
		pedido.setNombreCliente(cliente.getNombreCliente());
		String[] nomClie = StringUtil.formatClienteSucursal(cliente
				.getNombreCliente());
		if (nomClie[1] != null)
			pedido.setNombreSucursal(nomClie[1]);
		pedido.setObjTipoPrecioVentaID(cliente.getObjPrecioVentaID());
		pedido.setCodTipoPrecio(cliente.getCodTipoPrecio());
		pedido.setDescTipoPrecio(cliente.getDesTipoPrecio());
	}

	public void ocultarDialogos() {
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
		if (pd != null && pd.isShowing())
			pd.dismiss();
	}

	public void showStatus(final String mensaje, boolean... confirmacion) {

		ocultarDialogos();
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

}
