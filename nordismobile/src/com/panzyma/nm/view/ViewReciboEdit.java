package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.auxiliar.VentasUtil;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.interfaces.Editable;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Recibo;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nm.view.viewholder.PProductoViewHolder;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogoConfirmacion;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewdialog.DialogFactura;
import com.panzyma.nm.viewdialog.DialogFactura.OnFacturaButtonClickListener;
import com.panzyma.nm.viewdialog.DialogoConfirmacion.Pagable;
import com.panzyma.nordismobile.R;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewReciboEdit extends FragmentActivity implements Handler.Callback, Editable {

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
	private TextView gridheader;
	private Controller controller;
	private GenericAdapter adapter;
	private ProgressDialog pd;
	private Button Menu;
	private QuickAction quickAction;
	private int positioncache = -1;
	private Display display;
	private static final String TAG = ViewCliente.class.getSimpleName();
	private static final int ID_SELECCIONAR_CLIENTE = 0;
	private static final int ID_AGREGAR_DOCUMENTOS = 1;
	private static final int ID_AGREGAR_PAGOS = 2;
	private static final int ID_EDITAR_NOTAS = 3;
	private static final int ID_PAGAR_TODO = 4;
	private static final int ID_PAGAR_MONTO = 5;
	private static final int ID_EDITAR_DESCUENTO = 6;
	private static final int ID_SALVAR_RECIBO = 7;
	private static final int ID_ENVIAR_RECIBO = 8;
	private static final int ID_SOLICITAR_DESCUENTO_OCASIONAL = 9;
	private static final int ID_CERRAR = 10;
	private ViewReciboEdit me;
	private Cliente cliente;
	private Recibo recibo;
	private Context contexto;
	private BReciboM brm;

	private NMApp nmapp;
	private List<Factura> facturasRecibo = new ArrayList<Factura> ();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recibo_edit);

		try {
			me = this;
			nmapp = (NMApp) this.getApplicationContext();
			
			nmapp.getController().setEntities(this, brm =  new BReciboM());
			nmapp.getController().addOutboxHandler(new Handler(this));

			contexto = this.getApplicationContext();

			// controller.getInboxHandler().sendEmptyMessage(ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);
			WindowManager wm = (WindowManager) contexto
					.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			initComponent();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initComponent() {

		gridDetalleRecibo = findViewById(R.id.pddgrilla);

		gridheader = (TextView) gridDetalleRecibo.findViewById(R.id.header);
		gridheader.setText("Documentos a Pagar (0)");
		tbxFecha = (EditText) findViewById(R.id.pddetextv_detalle_fecha);
		tbxNumReferencia = (EditText) findViewById(R.id.pdddetextv_detalle_numref);
		tbxNumRecibo = (EditText) findViewById(R.id.pddtextv_detallenumero);
		tbxNombreDelCliente = (EditText) findViewById(R.id.pddtextv_detallecliente);
		tbxNotas = (EditText) findViewById(R.id.pddtextv_detalle_notas);
		//Totales Documentos
		txtTotalAbonadoFacturas = (TextView) findViewById(R.id.txtTotalFacturas);
		txtTotalAbonadoND = (TextView) findViewById(R.id.txtTotalNotasDebito);
		txtTotalAbonadoNC = (TextView) findViewById(R.id.txtTotalNotaCredito);
		txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);
		txtTotal = (TextView) findViewById(R.id.txtTotal);

		if (recibo == null) {
			recibo = new Recibo();
			recibo.setId(0);
			cliente = null;
			recibo.setCodEstado("REGISTRADO");
			recibo.setReferencia(0);
			recibo.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
			recibo.setExento(false);
			recibo.setAutorizacionDGI("");
		}

		// Fecha del Pedido
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		formato.setCalendar(Calendar.getInstance());
		long date = DateUtil.dt2i(Calendar.getInstance().getTime());

		if (recibo.getFecha() == 0)
			recibo.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));

		if (recibo.getReferencia() == 0)
			tbxNumReferencia.setText(VentasUtil.getNumeroPedido(me,
					recibo.getReferencia()));

		if (recibo.getNombreCliente() != null)
			tbxNombreDelCliente.setText(recibo.getNombreCliente());

		tbxFecha.setText("" + DateUtil.idateToStrYY(date));

		initMenu();
	}

	private void initMenu() {
		quickAction = new QuickAction(this, QuickAction.VERTICAL, 1);
		quickAction.addActionItem(new ActionItem(ID_SELECCIONAR_CLIENTE,
				"Seleccionar Cliente"));
		quickAction.addActionItem(new ActionItem(ID_AGREGAR_DOCUMENTOS,
				"Agregar Documentos"));
		quickAction.addActionItem(new ActionItem(ID_AGREGAR_PAGOS,
				"Agregar Pagos"));
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
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));

		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
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
								case ID_SALVAR_RECIBO:
									guardarRecibo();
									break;
								case ID_CERRAR:
									finalizarActividad();
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
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	private void seleccionarCliente() {
		DialogCliente dc = new DialogCliente(me,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dc.setOnDialogClientButtonClickListener(new OnButtonClickListener() {
			@Override
			public void onButtonClick(Cliente _cliente) {
				cliente = _cliente;
				tbxNombreDelCliente.setText(cliente.getNombreCliente());

				String[] nomClie = StringUtil.split(cliente.getNombreCliente(),
						"/");
				// establecer valores de recibo
				recibo.setObjClienteID(cliente.getIdCliente());
				recibo.setObjSucursalID(cliente.getIdSucursal());
				recibo.setNombreCliente(nomClie[1]);
			}
		});
		Window window = dc.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dc.show();
	}

	private void guardarRecibo() {

		recibo.setNumero(Integer.parseInt((tbxNumRecibo.getText().toString()
				.trim().equals("") ? "0" : tbxNumRecibo.getText().toString())));

		recibo.setReferencia(Integer.parseInt((tbxNumReferencia.getText()
				.toString().trim().equals("")) ? "0" : tbxNumReferencia
				.getText().toString()));

		recibo.setNotas((tbxNotas.getText().toString().trim().equals("") ? "0"
				: tbxNotas.getText().toString()));

		if (valido()) {

			this.subTotal = (this.totalFacturas + this.totalNotasDebito + this.totalInteres)
					- this.totalNotasCredito;

			this.totalDescuento = this.totalDescuentoOcasional
					+ this.totalDescuentoPromocion
					+ this.totalDescuentoProntoPago;

			this.totalRecibo = this.subTotal
					+ (this.totalImpuestoProporcional + this.totalImpuestoExonerado)
					- (this.totalDescuento - this.totalOtrasDeducciones);

			// totales
			recibo.setTotalFacturas(totalFacturas);
			recibo.setTotalNC(totalNotasCredito);
			recibo.setTotalND(totalNotasDebito);
			recibo.setTotalInteres(totalInteres);
			recibo.setSubTotal(subTotal);
			recibo.setTotalDescOca(totalDescuentoOcasional);
			recibo.setTotalDescPromo(totalDescuentoPromocion);
			recibo.setTotalDescPP(totalDescuentoProntoPago);
			recibo.setTotalDesc(totalDescuento);
			recibo.setTotalImpuestoProporcional(totalImpuestoProporcional);
			recibo.setTotalImpuestoExonerado(totalImpuestoExonerado);
			recibo.setTotalOtrasDed(totalOtrasDeducciones);
			recibo.setTotalRecibo(totalRecibo);
			// colector
			recibo.setObjColectorID(10);
			recibo.setAplicaDescOca(false);
			recibo.setClaveAutorizaDescOca("");
			recibo.setPorcDescOcaColector(this.porcentajeDescuentoOcasional);
			// estado
			recibo.setObjEstadoID(100);
			recibo.setCodEstado("REGISTRADO");
			
			recibo.setId(Ventas.getMaxReciboId(this.contexto) + 1);

			try {
				DatabaseProvider.RegistrarRecibo(recibo, contexto);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void actualizaTotales() {
		// ENCONTRANDO EL SUBTOTAL DEL RECIBO
		txtTotalAbonadoFacturas.setText(String.valueOf(recibo
				.getTotalFacturas()));
		txtTotalAbonadoND.setText(String.valueOf(recibo.getTotalND()));
		recibo.setSubTotal(recibo.getTotalFacturas() + recibo.getTotalND()
				+ recibo.getTotalInteres());
		txtSubTotal.setText(String.valueOf(recibo.getSubTotal()));
		// OBTENIENDO EL TOTAL DE DESCUENTO
		recibo.setTotalDesc(recibo.getTotalDescOca() + recibo.getTotalDescPP()
				+ recibo.getTotalDescPromo());
		// TOTAL NOTAS DE CREDITO
		txtTotalAbonadoNC.setText(String.valueOf(recibo.getTotalNC()));
		
		recibo.setTotalRecibo(recibo.getSubTotal() - recibo.getTotalNC()
				- recibo.getTotalDesc() - recibo.getTotalRetenido()
				- recibo.getTotalOtrasDed()
				- recibo.getTotalImpuestoExonerado());
		txtTotal.setText(String.valueOf(recibo.getTotalRecibo()));
	}

	private void agregarDocumentosPendientesCliente() {
		
		if(cliente == null)	{
			Toast.makeText(getApplicationContext(), "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show();
			return;
		}			
				
		DialogFactura dialog= new DialogFactura(me,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen, recibo.getObjSucursalID());
		dialog.setOnDialogFacturaButtonClickListener(new OnFacturaButtonClickListener() {
			@Override
			public void onButtonClick(final Factura factura) {
				
				final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(factura);
				dialogConfirmacion.setActionPago(new Pagable() {					
					@Override
					public void onPagarFactura(Float montoAbonado) {
						factura.setAbonado(factura.getAbonado() + montoAbonado);
						if(factura.Saldo < montoAbonado )							
							factura.setEstado("ABONADA");
						else if (factura.Saldo == montoAbonado)
							factura.setEstado("CANCELADA");
						else { 
							//ERROR
						}
						factura.setSaldo(factura.getSaldo() - factura.getAbonado());
						recibo.setTotalFacturas(montoAbonado);
						facturasRecibo.add(factura);
						agregarDocumentosAlDetalleDeRecibo();
						actualizaTotales();
					}
				});
				FragmentManager fragmentManager = getSupportFragmentManager();
				
				dialogConfirmacion.show(fragmentManager, "");				
				
				@SuppressWarnings("unused")
				Object obj = factura;			
			}
		});			
		Window window = dialog.getWindow(); 
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dialog.show();

	}

	private boolean valido() {
		boolean valido = true;

		if (recibo.getObjClienteID() == 0) {
			valido = false;
			Util.Message.buildToastMessage(contexto,
					"DEBE seleccionar un cliente", 1000).show();
		}

		if (recibo.getNumero() == 0) {
			valido = false;
			Util.Message.buildToastMessage(contexto,
					"ESPECIFIQUE número de recibo", 1000).show();
		}

		if (recibo.getReferencia() == 0) {
			valido = false;
			Util.Message.buildToastMessage(contexto,
					"ESPECIFIQUE número de referencia", 1000).show();
		}

		return valido;
	}

	private void finalizarActividad() {
		nmapp.getController().removeOutboxHandler(TAG);
		Log.d(TAG, "Activity quitting");
		finish();
	}

	public Long getObjectSucursalID() {
		if (recibo != null)
			return recibo.getObjSucursalID();
		else
			return Long.valueOf("0");
	}
	
	public void agregarDocumentosAlDetalleDeRecibo(){
		 
		//gridheader.setText("Listado de Productos a Vender");
		if(adapter==null)
		{
			adapter=new GenericAdapter(this,FacturaViewHolder.class,facturasRecibo,R.layout.detalle_factura);				 
			((ListView)gridDetalleRecibo.findViewById(R.id.data_items)).setAdapter(adapter);
			gridheader.setText("Facturas a Pagar ("+adapter.getCount()+")");
		}
		else
		{ 
			adapter.notifyDataSetChanged();
			gridheader.setText("Facturas a Pagar ("+adapter.getCount()+")");
		}
	}

	@Override
	public BReciboM getBridge() {
		// TODO Auto-generated method stub
		return brm;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this.me;
	}

}
