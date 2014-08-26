package com.panzyma.nm.view;
 
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SOLICITAR_DESCUENTO;
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SEND_DATA_FROM_SERVER;
   
import java.util.ArrayList;
import java.util.Calendar; 
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.ActionType;
import com.panzyma.nm.auxiliar.Ammount;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil; 
import com.panzyma.nm.auxiliar.ErrorMessage;  
import com.panzyma.nm.auxiliar.NumberUtil; 
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.auxiliar.VentasUtil;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol; 
import com.panzyma.nm.interfaces.Editable;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction; 
import com.panzyma.nm.model.FacND;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.Cliente; 
import com.panzyma.nm.serviceproxy.Factura; 
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;  
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.DocumentoViewHolder; 
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogSeleccionTipoDocumento;
import com.panzyma.nm.viewdialog.DialogSeleccionTipoDocumento.Seleccionable;
import com.panzyma.nm.viewdialog.DialogoConfirmacion;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewdialog.DialogDocumentos;
import com.panzyma.nm.viewdialog.DialogDocumentos.OnDocumentoButtonClickListener;
import com.panzyma.nm.viewdialog.DialogSeleccionTipoDocumento.Documento;
import com.panzyma.nm.viewdialog.DialogoConfirmacion.Pagable;
import com.panzyma.nm.viewdialog.EditFormaPago; 
import com.panzyma.nordismobile.R;

import android.support.v4.app.FragmentActivity; 
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context; 
import android.content.Intent; 
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

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
	private static final int ID_EDITAR_NOTAS = 3;
	private static final int ID_PAGAR_TODO = 4;
	private static final int ID_PAGAR_MONTO = 5;
	private static final int ID_EDITAR_DESCUENTO = 6;
	private static final int ID_SALVAR_RECIBO = 7;
	private static final int ID_ENVIAR_RECIBO = 8;
	private static final int ID_SOLICITAR_DESCUENTO_OCASIONAL = 9;
	private static final int TIME_TO_VIEW_MESSAGE = 3000;
	public static final String FORMA_PAGO_IN_EDITION = "edit"; 
	public static final String OBJECT_TO_EDIT = "recibo"; 
	private boolean salvado=false;
	// 
	private static final int ID_EDITAR_DOCUMENTO = 0;
	private static final int ID_ELIMINAR_DOCUMENTO = 1;
	private static final int VER_DETALLE_DOCUMENTO = 2;
	private static final int ID_CERRAR = 10;
	private ViewReciboEdit me;
	private Cliente cliente;
	private ReciboColector recibo=null;
	private Context contexto;
	private BReciboM brm;
	private Handler handler;
	private Integer reciboId;
	private com.panzyma.nm.serviceproxy.Documento documento_selected;
	private boolean onEdit = false;
	private boolean onNew;	
	private static Object lock = new Object();
    private boolean isReimpresion=false;
	private NMApp nmapp;
	private List<Factura> facturasRecibo;
	private List<com.panzyma.nm.serviceproxy.Documento> documents;
	
	boolean imprimir = false;
    boolean pagarOnLine = false; 
    
	public List<Factura> getFacturasRecibo() {
		return facturasRecibo;
	}
	
	public Integer getReciboID (){
		return reciboId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recibo_edit);

		try {
			
			Bundle bundle =  getIntent().getExtras();
			//OBTENER EL ID DEL RECIBO 
			reciboId = (Integer)bundle.get(ViewRecibo.RECIBO_ID);
			SessionManager.setContext(this);
			me = this;
			nmapp = (NMApp) this.getApplicationContext();
			nmapp.getController().removebridgeByName(BReciboM.class.toString());
			nmapp.getController().setEntities(this, brm =  new BReciboM());
			nmapp.getController().addOutboxHandler(handler=new Handler(this));
			
			facturasRecibo = new ArrayList<Factura> ();
			documents = new ArrayList<com.panzyma.nm.serviceproxy.Documento>();
			 
			if(reciboId != 0)
			{
				onEdit = true;
				//OBTENER EL RECIBO DESDE LOCALHOST  	 
				Message msg = new Message();
			    Bundle b = new Bundle();
			    b.putInt("idrecibo", reciboId);  
			    msg.setData(b);
			    msg.what=ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
			    nmapp.getController().getInboxHandler().sendMessage(msg);  
			}  

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
		item_document = (ListView)(gridDetalleRecibo).findViewById(R.id.data_items);
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
		
		item_document.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				if((parent.getChildAt(positioncache))!=null)						            							            		
            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
            	positioncache=position;				            	
            	documento_selected=(com.panzyma.nm.serviceproxy.Documento) adapter.getItem(position);	 
            	adapter.setSelectedPosition(position);  
            	view.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.action_item_selected));	
            	showMenu(view);
            	
				return true;
			}
		});
		
		loadData();
		initMenu();
	}
	
	private void loadData() {

		long date = DateUtil.dt2i(Calendar.getInstance().getTime());
		salvado=false;
		if (recibo == null || !onEdit) {
			// NUEVO RECIBO
			recibo = new ReciboColector();
			recibo.setId(0);
			cliente = null;
			recibo.setCodEstado("REGISTRADO");
			recibo.setReferencia(0);
			recibo.setFecha(date);
			recibo.setExento(false);
			recibo.setAutorizacionDGI("");
			recibo.setNombreCliente("");
			recibo.setNotas("");
			recibo.setTotalFacturas(0.00f);
			recibo.setTotalND(0.00f);
			recibo.setTotalNC(0.00f);
			recibo.setSubTotal(0.00f);
			recibo.setTotalRecibo(0.00f);
			tbxNumRecibo.setText("");
		} else {
			
			cliente = recibo.getCliente();
			
			// EDICION DE RECIBO
			if ("REGISTRADO".equals(recibo.getDescEstado())) {
				recibo.setFecha(date);
			}
			// AGREGAGAR LAS FACTURAS DEL RECIBO A LA GRILLA
			for (ReciboDetFactura factura : recibo.getFacturasRecibo()) {				
				for (Factura fac : cliente.getFacturasPendientes()) {
					if (fac.getId() == factura.getObjFacturaID()) {
						fac.setAbonado(factura.getMonto());
						factura.setTotalFactura(fac.getTotalFacturado());
						getFacturasRecibo().add(fac);
					}
				}
				documents.add(factura);
			}
			// AGREGAR LAS NOTAS DE DEBITO DEL RECIBO A LA GRILLA
			for (ReciboDetND nd : recibo.getNotasDebitoRecibo()) {
				documents.add(nd);
			}
			// AGREGAR LAS NOTAS DE CREDITO DEL RECIBO A LA GRILLA
			for (ReciboDetNC nc : recibo.getNotasCreditoRecibo()) {
				documents.add(nc);
			}
			
			adapter = null;
			agregarDocumentosAlDetalleDeRecibo();
						
			try {
				nmapp.getController().setEntities(this,new BClienteM());
				nmapp.getController().addOutboxHandler(new Handler(this));
				nmapp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			        
	        
		}
		// ESTABLECER LOS VALORES EN LA VISTA DE EDICION DE RECIBO
		tbxNumRecibo.setText(""+recibo.getNumero());
		tbxNotas.setText(""+recibo.getNotas());
		tbxNumReferencia.setText(""+VentasUtil.getNumeroPedido(me,
				recibo.getReferencia()));
		tbxNombreDelCliente.setText(""+recibo.getNombreCliente());
		tbxFecha.setText("" + DateUtil.idateToStrYY(recibo.getFecha()));
		// ESTABLECER LOS TOTALES
		txtTotalAbonadoFacturas.setText("" + recibo.getTotalFacturas());
		txtTotalAbonadoND.setText("" + recibo.getTotalND());
		txtTotalAbonadoNC.setText("" + recibo.getTotalNC());
		txtSubTotal.setText("" + recibo.getSubTotal());
		txtTotal.setText("" + recibo.getTotalRecibo());
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
								case ID_AGREGAR_PAGOS:
									agregarPago();
									break;
								case ID_PAGAR_TODO :
									if(cliente==null){
										AppDialog.showMessage(me,"Información","Por favor seleccione un cliente.",DialogType.DIALOGO_ALERTA);
										return;
									}
									PagarTodo();
									break;
								case  ID_PAGAR_MONTO :
									if(cliente==null){
										AppDialog.showMessage(me,"Información","Por favor seleccione un cliente.",DialogType.DIALOGO_ALERTA);
										return;
									}
									PagarMonto();
									
								break;
								case ID_SOLICITAR_DESCUENTO_OCASIONAL:
									 solicitardescuento();
									break;
								case ID_SALVAR_RECIBO:
									guardarRecibo();
									salvado=true;
									break;
								case ID_ENVIAR_RECIBO:
									enviarRecibo();
									break;
								case ID_CERRAR:
									//finalizarvidad();
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
		else if (keyCode == KeyEvent.KEYCODE_BACK) {        	
		  	FINISH_ACTIVITY();
			finish();	       
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean handleMessage(Message msg) 
	{
		switch(msg.what)
		{
			case C_DATA:
				recibo = (ReciboColector)msg.obj;
				loadData();
				break;
			case ControllerProtocol.ID_REQUEST_SALVARPEDIDO:
				recibo = (ReciboColector)msg.obj;
				actualizarOnUINumRef(recibo);
				Util.Message.buildToastMessage(contexto,
						"Recibo Guardado!!", 1000).show();
				
				salvado=true;
				break;
			case ControllerProtocol.NOTIFICATION: 
				break;			
		}
		return false;
	}

	public void actualizarOnUINumRef(final ReciboColector r) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tbxNumReferencia.setText(NumberUtil.getFormatoNumero(
						r.getReferencia(), me.getApplicationContext()));
				salvado = true;
			}
		});
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

	private void solicitardescuento()
	{ 
		//Si se está fuera de covertura, salir
        if (SessionManager.isPhoneConnected()) {
            //Dialog.alert("La operación no puede ser realizada ya que está fuera de cobertura.");
            return;
        }
        
        if (!Cobro.validaAplicDescOca(me.getContext(),recibo))
        {            
        	AppDialog.showMessage(me,"Alerta","Debe cancelar al menos una factura vencida para aplicar descuento ocasional.",DialogType.DIALOGO_ALERTA);
            return;
        }  
        if(cliente==null){
			AppDialog.showMessage(me,"Alerta","Por favor seleccione un cliente.",DialogType.DIALOGO_ALERTA);
			return;
		} 
		AppDialog.showMessage(me,"Solicitar descuento Ocosional","",DialogType.DIALOGO_INPUT,new AppDialog.OnButtonClickListener()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onButtonClick(AlertDialog alert, int actionId) 
			{
				if(actionId == AppDialog.OK_BUTTOM)
				{ 					 
					try 
					{
						String nota =  ((TextView)alert.findViewById(R.id.txtpayamount)).getText().toString();
						nmapp.getController().setEntities(this,getBridge()==null?new BReciboM():getBridge());
						nmapp.getController().addOutboxHandler((getHandler()==null)?new Handler(me):getHandler()); 
						Message msg = new Message();
					    Bundle b = new Bundle();
					    b.putParcelable("recibo", recibo);
					    b.putString("notas",nota); 
					    msg.setData(b);
					    msg.what=SOLICITAR_DESCUENTO;
					    nmapp.getController().getInboxHandler().sendMessage(msg); 
					} catch (Exception e) 
					{ 
						e.printStackTrace();
					} 					
				}
			}
		}); 
	}
	
	@SuppressWarnings("unchecked")
	private void guardarRecibo() {

		recibo.setNumero(Integer.parseInt((tbxNumRecibo.getText().toString()
				.trim().equals("") ? "0" : tbxNumRecibo.getText().toString())));

		recibo.setReferencia(Integer.parseInt((tbxNumReferencia.getText()
				.toString().trim().equals("")) ? "0" : tbxNumReferencia
				.getText().toString()));

		recibo.setNotas((tbxNotas.getText().toString().trim().equals("") ? "0"
				: tbxNotas.getText().toString()));
		//
		if (valido()) 
		{
		//cambiar por el true
		//if (true) {

			this.subTotal = (this.totalFacturas + this.totalNotasDebito + this.totalInteres)
					- this.totalNotasCredito;

			this.totalDescuento = this.totalDescuentoOcasional
					+ this.totalDescuentoPromocion
					+ this.totalDescuentoProntoPago;

			this.totalRecibo = this.subTotal
					+ (this.totalImpuestoProporcional + this.totalImpuestoExonerado)
					- (this.totalDescuento - this.totalOtrasDeducciones);

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
			
			//AGREGAR LOS DOCUMENTOS DE LA GRILLA AL RECIBO
			for (com.panzyma.nm.serviceproxy.Documento doc : documents) {
				
				if (doc.getTipo().equals("Factura")) {
					ReciboDetFactura detalleFactura = (ReciboDetFactura) doc.getObject();
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
				nmapp.getController().setEntities(this,getBridge()==null?new BReciboM():getBridge());
				nmapp.getController().addOutboxHandler((getHandler()==null)?new Handler(this):getHandler());			
				Message msg = new Message();
			    Bundle b = new Bundle();
			    b.putParcelable("recibo", recibo); 
			    b.putParcelableArray("facturasToUpdate", getArrayOfFacturas() );
			    msg.setData(b);
			    msg.what=SAVE_DATA_FROM_LOCALHOST;
			    nmapp.getController().getInboxHandler().sendMessage(msg);  			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private Factura [] getArrayOfFacturas(){
		Factura [] facturas = new Factura [ facturasRecibo.size() ];
		for(int i = 0; (facturasRecibo != null && i < facturasRecibo.size() ) ; i++ ) {
			facturas[i] = facturasRecibo.get(i);
		}
		return facturas;
	}
	

	@SuppressLint("ShowToast") @SuppressWarnings({ "static-access", "unchecked" })
	private void enviarRecibo()
	{   
		
		if(!valido()) return; 
        pd.show(this, "Enviando recibo a la central", "Espere por favor", true); 
        try 
        {
			nmapp.getController().setEntities(this,getBridge()==null?new BReciboM():getBridge());
			nmapp.getController().addOutboxHandler((getHandler()==null)?new Handler(this):getHandler());
			Toast.makeText(this, "Enviando recibo a la central", Toast.LENGTH_LONG);  	 
			Message msg = new Message();
		    Bundle b = new Bundle();
		    b.putParcelable("recibo", recibo); 
		    b.putParcelableArray("facturasToUpdate", getArrayOfFacturas());
		    msg.setData(b);
		    msg.what=SEND_DATA_FROM_SERVER;
		    nmapp.getController().getInboxHandler().sendMessage(msg);  	 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		        
	}
	
	public boolean valido() 
    {        
    	
    	try 
    	{
    		
    		//Validar fecha del pedido
            long d =(DateUtil.strTimeToLong(tbxFecha.getText().toString()));            
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
        							          "Problemas con las Facturas.", "Debe incluir al menos una factura o nota de débito."));
                    
                    return false;
                }
            }
            
            //Validar que la cantidad de facturas incluidas no sea mayor que el valor del parámetro CantMaxFacturasEnRecibo.
            int max = Integer.parseInt(Cobro.getParametro(me,"CantMaxFacturasEnRecibo")+"");
            if (cantFac > max) {
            	showStatusOnUI(
    					new ErrorMessage(
    							          "Error en el proceso de enviar el recibo al servidor",
    							          "Problemas con los Documentos.", "La cantidad de facturas no debe ser mayor que "+max));             
     
                return false;
            }
                    
            //La cantidad de notas de débito marcadas para ser incluidas en el recibo 
            //no debe ser mayor que el valor del párametro CantMaxNotasDebitoEnRecibo        
            max = Integer.parseInt(Cobro.getParametro(me,"CantMaxNotasDebitoEnRecibo")+"");
            if (cantND > max) {
            	
            	showStatusOnUI(
    					new ErrorMessage(
    							          "Error en el proceso de enviar el recibo al servidor",
    							          "Problemas con los Documentos.", "La cantidad de notas de débito no debe ser mayor que "+max + "."));             
                return false;
            }
            
            //La cantidad de notas de crédito incluidas a pagar no debe ser mayor 
            //que el valor del parámetro CantMaxNotasCreditoEnRecibo        
            max = Integer.parseInt(Cobro.getParametro(me,"CantMaxNotasCreditoEnRecibo")+"");
            if (Cobro.cantNCs(recibo) > max) {
            	showStatusOnUI(
    					new ErrorMessage(
    							"Error al Validar el Recibo",
    							          "Problemas con los Documentos.", "La cantidad de notas de crédito no debe ser mayor que " + max + "."));
                //Dialog.alert("La cantidad de notas de crédito no debe ser mayor que " + max + ".");
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
            //de los montos a pagar de las facturas incluidas en el recibo, a excepción de que solamente se estén
            //pagando facturas vencidas en cuyo caso SÍ se permite un monto igual
            if (recibo.getTotalNC() > 0) { //Si hay NC aplicadas
            
                //Ver si todas las facturas aplicadas son vencidas
                boolean todasVencidas = true;
                int diasAplicaMora = Integer.parseInt(Cobro.getParametro(me, "DiasDespuesVenceCalculaMora")+"");
                long fechaHoy = DateUtil.getTime(DateUtil.getToday());
                if (recibo.getFacturasRecibo().size() != 0) {
                    ReciboDetFactura[] ff = (ReciboDetFactura[]) recibo.getFacturasRecibo().toArray();
                    if (ff != null) {
                        for(int i=0; i<ff.length; i++) {
                            ReciboDetFactura f = ff[i];
                            String s = f.getFechaVence() + "";
                            int fechaVence = Integer.parseInt(s.substring(0, 8));
                            long fechaCaeEnMora = DateUtil.addDays(DateUtil.getTime(fechaVence), diasAplicaMora);
                            if (fechaCaeEnMora > fechaHoy) {
                                todasVencidas = false;
                                break;
                            }
                        }
                    }
                } //Ver si todas las facturas aplicadas son vencidas
                
                if (todasVencidas && (recibo.getTotalNC() > recibo.getTotalFacturas()))  {
                	showStatusOnUI(
        					new ErrorMessage(
        							"Error al Validar el Recibo",
        							          "Problemas con los Documentos.", "El total de notas de crédito a aplicar debe ser menor o igual al total a pagar en facturas." + max + "."));
                   // Dialog.alert("El total de notas de crédito a aplicar debe ser menor o igual al total a pagar en facturas.");
                    return false;
                }
                            
                if (todasVencidas && (recibo.getTotalNC() >= recibo.getTotalFacturas()))  {
                	showStatusOnUI(
        					new ErrorMessage(
        							"Error al Validar el Recibo",
        							          "Problemas con los Documentos.", "El total de notas de crédito a aplicar debe ser menor al total a pagar en facturas."));

                   // Dialog.alert("El total de notas de crédito a aplicar debe ser menor al total a pagar en facturas.");
                    return false;
                }
                  
            } //Si hay NC aplicadas


            //Monto Mínimo Recibo: Para aplicar descuento específico a cada factura que se va cancelar,
            //el total del recibo deber mayor o igual al valor del parámetro 'MontoMinReciboAplicaDpp'
            boolean ValidarMontoAplicaDpp = false;
            
            //Determinando si hay descPP que validar
            if (recibo.getFacturasRecibo().size() != 0) {
                ArrayList<ReciboDetFactura> ff =recibo.getFacturasRecibo();
                if (ff != null) {
                    for(int i=0; i<ff.size(); i++) {
                        ReciboDetFactura f = ff.get(i);
                        if (f.getMontoDescEspecifico() != 0) {
                            ValidarMontoAplicaDpp = true;
                            break;
                        }
                    }
                }
            } //Determinando si hay descPP que validar
            
            //Validando el monto mínimo del recibo
            float montoMinimoRecibo = Float.parseFloat(Cobro.getParametro(me,"MontoMinReciboAplicaDpp")+"");
            if ((recibo.getTotalRecibo() < montoMinimoRecibo) && ValidarMontoAplicaDpp) {
                //Recalcular detalles del recibo sin aplicar DescPP
                Cobro.calcularDetFacturasRecibo(me,recibo, recibo.getCliente(), false);
                actualizaTotales();            
                showStatusOnUI(
    					new ErrorMessage(
    							"Error al Validar el Recibo",
    							          "Problemas el Descuento PP.","Para aplicar descuento pronto pago \r\nel monto del recibo no debe ser menor que " + StringUtil.formatReal(montoMinimoRecibo) + "."));
 
                return false;
            }              
            if (Cobro.getTotalPagoRecibo(recibo) != recibo.getTotalRecibo()) {
            	  showStatusOnUI(
      					new ErrorMessage(
      							          "Error al Validar el Recibo",
      							          "Problema con el Monto Total del Recibo","El monto pagado no cuadra con el total del recibo."));

               // Dialog.alert("El monto pagado no cuadra con el total del recibo.");
                return false;
            }
            return true;

			
		} catch (Exception e) 
		{
			
		}
    	return false;
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
	
	private void procesaFactura(ReciboDetFactura facturaDetalle,
			Factura factura, List<Ammount> montos, boolean agregar) 
	{
		for (Ammount ammount : montos) 
		{
			switch (ammount.getAmmountType()) 
			{
				case ABONADO:
					float montoAbonado = 0.00F, saldo = 0.00F;
					montoAbonado = factura.getAbonado() - ammount.getValue();					
					montoAbonado = ( montoAbonado <= 0 ) ? 0 : montoAbonado ;
					saldo = factura.getTotalFacturado() - montoAbonado;
					factura.setAbonado( montoAbonado + ammount.getValue() );
					if ( saldo < factura.getAbonado() ) {
						factura.setCodEstado("ABONADA");
						factura.setEstado("Abonada");
					} else if ( saldo == factura.getAbonado() ) {
						factura.setCodEstado("CANCELADA");
						factura.setEstado("Cancelada");
					}					
					facturaDetalle.setEsAbono(factura.getTotalFacturado() > factura
							.getAbonado());
					factura.setSaldo(factura.getTotalFacturado()- factura.getAbonado());
					facturaDetalle.setMonto(factura.getAbonado());
					facturaDetalle.setSaldoFactura(factura.getSaldo());
					Cobro.ActualizaTotalFacturas(recibo);
					break;
				case RETENIDO:
					float montoRetencion = 0.00F;
					montoRetencion = ammount.getValue();
					factura.setRetenido(montoRetencion);
					facturaDetalle.setMontoRetencion(montoRetencion);
					break;
				case DESCONTADO:
					float montoDescuento = 0.00F;
					montoDescuento = ammount.getValue();
					factura.setDescontado(montoDescuento);
					factura.setDescuentoFactura(factura.getDescuentoFactura() + montoDescuento);					
					if ( montoDescuento > facturaDetalle.getMontoDescEspecificoCalc() ) {
						try {
							showStatusOnUI(
									new ErrorMessage(
											          "Error al editar descuento",
											          "El nuevo descuento no debe ser mayor que " + StringUtil.formatReal(facturaDetalle.getMontoDescEspecificoCalc()) + ".", ""));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}	                          
			            return;
			        }
					//Recalcular monto neto
			        Cobro.ActualizaMtoNetoFacturasrecibo(recibo);
					facturaDetalle.setMontoDescEspecificoCalc(montoDescuento);
				default:
					break;
			}
		}
		if( agregar ) 
		{
			facturaDetalle.setFechaAplicaDescPP(factura.getFechaAppDescPP());
			facturasRecibo.add(factura);
			recibo.getFacturasRecibo().add(facturaDetalle);
			documents.add(facturaDetalle);
		}		
		agregarDocumentosAlDetalleDeRecibo();
		actualizaTotales();
	}

	private void agregarDocumentosPendientesCliente() {
		
		if(cliente == null)	{
			Toast.makeText(getApplicationContext(), "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show();
			return;
		}	
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		DialogSeleccionTipoDocumento dtp = new DialogSeleccionTipoDocumento();
		dtp.setEventSeleccionable(new Seleccionable() {			
			@Override
			public void onSeleccionarDocumento(Documento document) {
				
				DialogDocumentos dialog= new DialogDocumentos(me,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen, cliente, document);
				dialog.setOnDialogDocumentoButtonClickListener(new OnDocumentoButtonClickListener() {			
					@Override
					public void onButtonClick(Object documento) {
						//SI EL DOCUMENTO ES UNA FACTURA
						if (documento instanceof Factura)
						{
							final Factura factura = (Factura)documento;
							//CREAR UN OBJETO DETALLE DE FACURA
							final ReciboDetFactura facturaDetalle = new ReciboDetFactura();
							facturaDetalle.setObjFacturaID(factura.getId());
							facturaDetalle.setFecha(factura.getFecha());							
							facturaDetalle.setNumero(factura.getNoFactura());							
							facturaDetalle.setMontoRetencion(0.00f);
							facturaDetalle.setTotalFactura(factura.getTotalFacturado());
							
							final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(facturaDetalle, ActionType.ADD);
							dialogConfirmacion.setActionPago(new Pagable() {					
								@Override
								public void onPagarEvent(List<Ammount> montos) {
									procesaFactura(facturaDetalle, factura, montos, true);
								}
							});
							FragmentManager fragmentManager = getSupportFragmentManager();
							
							dialogConfirmacion.show(fragmentManager, "");
							
						}				
					}
				});			
				Window window = dialog.getWindow(); 
				window.setGravity(Gravity.CENTER);
				window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
				dialog.show();
			}
		});
		dtp.show(fragmentManager, "");

	}

	private void agregarPago() {
		if (recibo != null && recibo.getTotalRecibo() == 0) return;
        
        if ("REGISTRADO".compareTo(recibo.getCodEstado()) != 0) return;		
        
        //Validar que haya pendiente por pagar
        float montoPorPagar = StringUtil.round(recibo.getTotalRecibo() - Cobro.getTotalPagoRecibo(recibo), 2);
        if (montoPorPagar <= 0) {
            Util.Message.buildToastMessage(this.contexto, "No hay monto pendiente de pago.", TIME_TO_VIEW_MESSAGE);
            return;
        }
        
        FragmentManager fragmentManager = getSupportFragmentManager();        
        EditFormaPago editarPago = new EditFormaPago();        
        Bundle parameters = new Bundle();     
        parameters.putBoolean(FORMA_PAGO_IN_EDITION, false);
        parameters.putParcelable(OBJECT_TO_EDIT, recibo);
        editarPago.setArguments(parameters);        
        editarPago.show(fragmentManager, "");
	}
	
//	private boolean valido() {
//		boolean valido = true;
//
//		if (recibo.getObjClienteID() == 0) {
//			valido = false;
//			Util.Message.buildToastMessage(contexto,
//					"DEBE seleccionar un cliente", TIME_TO_VIEW_MESSAGE).show();
//		}
//
//		if (recibo.getNumero() == 0) {
//			valido = false;
//			Util.Message.buildToastMessage(contexto,
//					"ESPECIFIQUE número de recibo", TIME_TO_VIEW_MESSAGE).show();
//		}
//
//		if (recibo.getReferencia() == 0) {
//			valido = false;
//			Util.Message.buildToastMessage(contexto,
//					"ESPECIFIQUE número de referencia", TIME_TO_VIEW_MESSAGE).show();
//		}
//
//		return valido;
//	}
 

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
			//adapter=new GenericAdapter(this, FacturaViewHolder.class,facturasRecibo,R.layout.detalle_factura);
			adapter=new GenericAdapter(this, DocumentoViewHolder.class, documents,R.layout.list_row);
			((ListView)gridDetalleRecibo.findViewById(R.id.data_items)).setAdapter(adapter);
			gridheader.setText("Documentos a Pagar ("+adapter.getCount()+")");
		}
		else
		{ 			
			adapter.notifyDataSetChanged();
			adapter.setSelectedPosition(documents.size() - 1);
			gridheader.setText("Documentos a Pagar ("+adapter.getCount()+")");
		}
	}
	
	public void showMenu(final View view) 
	{

		runOnUiThread
		(new Runnable() 
			{
				@Override
				public void run() 
				{
					quickAction2 = new QuickAction(me, QuickAction.VERTICAL, 1); 
					quickAction2.addActionItem(new ActionItem(ID_EDITAR_DOCUMENTO,"Editar Documento"));
					quickAction2.addActionItem(new ActionItem(VER_DETALLE_DOCUMENTO,"Ver Detalle Documento"));
					quickAction2.addActionItem(new ActionItem(ID_ELIMINAR_DOCUMENTO, "Eliminar Documento"));
					quickAction2.setOnActionItemClickListener
					(new QuickAction.OnActionItemClickListener() 
						{
				
							@Override
							public void onItemClick(QuickAction source, final int pos,final int actionId) 
							{ 
										ActionItem actionItem = quickAction2
												.getActionItem(pos);
										
										if(actionId==ID_EDITAR_DOCUMENTO)
											editarDocumento();
										else if(actionId==ID_ELIMINAR_DOCUMENTO)
											eliminarDocumento();							
									 
							}

						 }
					 ); 
					quickAction2.show(view,display,false);
				   }
				}
		    );
	}
	
	private void removeDocument(com.panzyma.nm.serviceproxy.Documento documentRemoved){
		int positionDocument = -1,
				count = 0;
		if (documentRemoved instanceof ReciboDetFactura) {
			//SI EL DOCUMENTO SE TRATA DE UNA FACTURA
			ReciboDetFactura facturaToRemoved = ((ReciboDetFactura)documentRemoved.getObject());
			for(Factura fac : getFacturasRecibo()){
				if(fac.getId() == facturaToRemoved.getObjFacturaID() ){
					positionDocument = count;
				}
				++count;
			}
			recibo.setTotalFacturas(recibo.getTotalFacturas() - facturaToRemoved.getMonto());			
		} else if (documentRemoved instanceof ReciboDetND) {
			//SI EL DOCUMENTO SE TRATA DE UNA NOTA DE DEBITO

		} else if (documentRemoved instanceof ReciboDetNC) {
			//SI EL DOCUMENTO SE TRATA DE UNA NOTA DE CREDITO

		}
		
	}

	private void eliminarDocumento() {
		if (!"REGISTRADO".equals(recibo.getCodEstado())) return;
		int posicion = positioncache;
		if (posicion == -1) return;		
			
		com.panzyma.nm.serviceproxy.Documento documentRemoved;
		
		//ELIMINAR DE LA LISTA DE DOCUMENTOS
		documentRemoved = documents.remove(posicion);
		
		//ELIMINAR EL DOCUMENTO DEL RECIBO Y ACTUALIZAR EL TOTAL 
		removeDocument(documentRemoved);
		
		//ACTUALIZA EL TOTAL EN LA PANTALLA Y ACTUALIZA EL SUBTOTAL Y TOTAL DEL RECIBO
		actualizaTotales();
		
		if (documents.size() > 0) {
            if (posicion == 0)
            { 
                positioncache = 0;
                documento_selected =(com.panzyma.nm.serviceproxy.Documento) adapter.getItem(0);	
                adapter.setSelectedPosition(0); 
            }
            else {
                if (posicion == documents.size())
                {  
                    positioncache= posicion - 1;
                    documento_selected = (com.panzyma.nm.serviceproxy.Documento) adapter.getItem(posicion - 1);	
                    adapter.setSelectedPosition(posicion - 1);
                }
                else
                {
                     positioncache = posicion;
                     documento_selected=(com.panzyma.nm.serviceproxy.Documento) adapter.getItem(posicion);	
                     adapter.setSelectedPosition(posicion);                     
                }
            }            
        }		
        adapter.notifyDataSetChanged();
        gridheader.setText("Documentos a Pagar ("+adapter.getCount()+")");
	}
	
	private Factura getFacturaByID(long id){
		Factura facturaToFound = null;
		for(Factura factura : facturasRecibo){
			if( factura.getId() == id ) {
				facturaToFound = factura;
				break;
			}
		}
		return facturaToFound;
	}

	private void editarDocumento() {
		if(!"REGISTRADO".equals(recibo.getCodEstado()))  return;
		int posicion = positioncache;
		if (posicion == -1) return;		
			
		final com.panzyma.nm.serviceproxy.Documento documentToEdit;
		
		documentToEdit = (com.panzyma.nm.serviceproxy.Documento) adapter.getItem(posicion);
		
		final DialogoConfirmacion dialogConfirmacion = new DialogoConfirmacion(documentToEdit, ActionType.EDIT);
		dialogConfirmacion.setActionPago(new Pagable() {					
			@Override
			public void onPagarEvent(List<Ammount> montos) {
				
				if( documentToEdit instanceof ReciboDetFactura ){
					
					ReciboDetFactura facturaDetalle = (ReciboDetFactura)documentToEdit;
					Factura factura = getFacturaByID(facturaDetalle.getObjFacturaID());	
					procesaFactura(facturaDetalle, factura, montos, false);					
					
				} else if ( documentToEdit instanceof ReciboDetND ){
					
				}				
				recibo.setTotalFacturas(0.00f);
				for(com.panzyma.nm.serviceproxy.Documento doc : (List<com.panzyma.nm.serviceproxy.Documento>)adapter.getData()){
					recibo.setTotalFacturas(recibo.getTotalFacturas() +doc.getMonto());
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
		// TODO Auto-generated method stub
		return brm;
	}
    public Handler getHandler(){
    	return handler;
    }
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this.me;
	}

	public ReciboColector getRecibo(){
		return recibo;
	}
		
	public ReciboColector setRecibo(ReciboColector r){
		return recibo=r;
	}
	
	public boolean getEstadoConexionPago(){
		return pagarOnLine;
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
							 AppDialog.showMessage(me,titulo,mensaje,AppDialog.DialogType.DIALOGO_CONFIRMACION,new AppDialog.OnButtonClickListener() 
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
	private void FINISH_ACTIVITY()
	{
		int requescode=0;
		nmapp.getController().removeOutboxHandler(TAG);
		nmapp.getController().removebridge(nmapp.getController().getBridge());
		nmapp.getController().disposeEntities();
		if(pd!=null)
			pd.dismiss();	
		Log.d(TAG, "Activity quitting");
		
		Intent intent =null;
		if(salvado)
		{
			if( recibo !=null 
					&& ( recibo.getFacturasRecibo().size() > 0 
					     || recibo.getNotasCreditoRecibo().size() > 0
					     || recibo.getNotasDebitoRecibo().size() > 0 ) )
			{
				intent = new Intent();
				Bundle b = new Bundle();
				b.putParcelable("recibo", recibo);
				intent.putExtras(b);
			} 
			requescode = getIntent().getIntExtra("requestcode", (onEdit)?1:0);			
			setResult(requescode,intent);
		}
		onEdit=false;
	}
	
	//
	private void PagarTodo()
	{
		recibo.getFacturasRecibo().clear();
		recibo.getNotasDebitoRecibo().clear();
		ArrayList<ReciboDetFactura> _facSeleccionadas = new ArrayList<ReciboDetFactura>();
		ArrayList<ReciboDetND> _ndsSeleccionadas = new ArrayList<ReciboDetND>();
		
		ArrayList<Factura> facturas = new ArrayList<Factura>();
		 //Traer las facturas del cliente
		Factura[] facturaspendientes = cliente.getFacturasPendientes();
		if(facturaspendientes!=null){
			if (facturaspendientes.length > 0){   
				
				 for(int i=0; i< facturaspendientes.length; i++) {
					 Factura fac = facturaspendientes[i];
					//Si la factura no está en otro recibo
					 if(Cobro.FacturaEstaEnOtroRecibo(getApplicationContext().getContentResolver(),fac.getId(),true) ==0){
						 facturas.add(fac);
					 }
				 }
			}
		}
		//Traer las notas de débito del cliente
		ArrayList<CCNotaDebito> notas= new ArrayList<CCNotaDebito>();
		CCNotaDebito[] NotasDebitoPendientes= cliente.getNotasDebitoPendientes();
		if(NotasDebitoPendientes!=null){
			if(NotasDebitoPendientes.length>0){
				for(int i=0; i<NotasDebitoPendientes.length; i++) {
					if (Cobro.NDEstaEnOtroRecibo(getApplicationContext().getContentResolver(),NotasDebitoPendientes[i].getId(), true) == 0)
						notas.add(NotasDebitoPendientes[i]);
				}
			}
		}
		
		if (((facturas == null) || (facturas.size() == 0)) && ((notas == null) || (notas.size() == 0))) return;
		
		String interes= getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("PorcInteresMoratorio", "0");
		//Facturas
        if ((facturas != null) && (facturas.size() > 0)) {   
        	 for(int i = 0; i < facturas.size(); i++) {
        		 Factura _fac = facturas.get(i);
        		 ReciboDetFactura _facRecibo = new ReciboDetFactura();
        		 _facRecibo.setId(0);
                 _facRecibo.setObjFacturaID(_fac.getId());
                 _facRecibo.setNumero(_fac.getNoFactura());
                 _facRecibo.setFecha(_fac.getFecha());
                 _facRecibo.setFechaVence(_fac.getFechaVencimiento());
                 _facRecibo.setFechaAplicaDescPP(_fac.getFechaAppDescPP());
                 _facRecibo.setEsAbono(false);                
                 _facRecibo.setImpuesto(_fac.getImpuestoFactura()); 
                 _facRecibo.setMontoImpuesto(0.0F); //Este es el impuesto proporcional                
                 //Calcular el interés moratorio de la factura si está en mora
                 _facRecibo.setInteresMoratorio(Float.parseFloat(interes));
                 _facRecibo.setMontoInteres(Cobro.getInteresMoratorio(this, _fac.getFechaVencimiento(), _fac.getSaldo()));                
                 _facRecibo.setMontoDescEspecifico(0.0F);                
                 _facRecibo.setPorcDescPromo(0.0F);
                 _facRecibo.setMontoDescPromocion(0.0F);                
                 _facRecibo.setPorcDescOcasional(0.0F);
                 _facRecibo.setMontoDescOcasional(0.0F);                        
                 _facRecibo.setMontoNeto(0.0F);
                 _facRecibo.setMontoOtrasDeducciones(0.0F);
                 _facRecibo.setMontoRetencion(0.0F);                
                 _facRecibo.setSaldoFactura(_fac.getSaldo());
                 _facRecibo.setSaldoTotal(_fac.getSaldo() + _facRecibo.getMontoInteres());                
                 _facRecibo.setMonto(_facRecibo.getSaldoTotal()); //Se pagará el saldo total de la factura                
                 _facRecibo.setSubTotal(_fac.getSubtotalFactura() - _fac.getDescuentoFactura());
                 _facRecibo.setTotalFactura(_fac.getTotalFacturado());
                 
               //Agregarla a facturas seleccionadas
                _facSeleccionadas.add(_facRecibo);      
        		 
        	 }
        }//Facturas
        if ((notas != null) && (notas.size() >0)) {  
        	for(int i = 0; i < notas.size(); i++) {
        		CCNotaDebito _nd = notas.get(i);
        		ReciboDetND _ndRecibo = new ReciboDetND();
        		_ndRecibo.setId(0);
                _ndRecibo.setObjNotaDebitoID(_nd.getId());
                _ndRecibo.setNumero(_nd.getNumero());
                _ndRecibo.setFecha(_nd.getFecha());
                _ndRecibo.setFechaVence(_nd.getFechaVence());        
                _ndRecibo.setEsAbono(false);
                _ndRecibo.setMontoInteres(Cobro.getInteresMoratorio( this,_nd.getFechaVence(), _nd.getSaldo()));
                _ndRecibo.setInteresMoratorio(Float.parseFloat(interes));
                _ndRecibo.setMontoND(_nd.getMonto());
                _ndRecibo.setSaldoND(_nd.getSaldo());
                _ndRecibo.setMontoNeto(0.0F);
                _ndRecibo.setSaldoTotal(_ndRecibo.getSaldoND() + _ndRecibo.getMontoInteres()); 
                _ndRecibo.setMontoPagar(_ndRecibo.getSaldoTotal());  
                
                _ndsSeleccionadas.add(_ndRecibo);                 
        	}
        }
        
        if (_facSeleccionadas.size() > 0) {
        	//Insertar nuevas facturas en el detalle de facturas del recibo    
        	ArrayList<ReciboDetFactura> fff = new ArrayList<ReciboDetFactura>();
            //Copiar facturas seleccionadas
            for(int i = 0; i < _facSeleccionadas.size(); i++){
                fff.add(_facSeleccionadas.get(i));
                float totalfactura = recibo.getTotalFacturas();
                recibo.setTotalFacturas(totalfactura + _facSeleccionadas.get(i).getTotalfactura());
            }
            //Actualizar detalle de facturas
            recibo.setFacturasRecibo(fff);

            documents.addAll(fff);
        }
        if (_ndsSeleccionadas.size() > 0) {
            //Insertar nuevas ncs en el detalle de ncs del recibo
        	ArrayList<ReciboDetND> ccc = new ArrayList<ReciboDetND>();
        	
        	//Copiar notas de crédito seleccionadas
            for(int i = 0; i < _ndsSeleccionadas.size(); i++)
                ccc.add(_ndsSeleccionadas.get(i));
            
            //Actualizar detalle de facturas
            recibo.setNotasDebitoRecibo(ccc);
            documents.addAll(ccc);
        }
        adapter = null;
        agregarDocumentosAlDetalleDeRecibo();
        actualizaTotales();
        /*
        tbxNumRecibo.setText(""+recibo.getNumero());
		tbxNotas.setText(""+recibo.getNotas());
		tbxNumReferencia.setText(""+VentasUtil.getNumeroPedido(me,
				recibo.getReferencia()));
		tbxNombreDelCliente.setText(""+recibo.getNombreCliente());
		tbxFecha.setText("" + DateUtil.idateToStrYY(recibo.getFecha()));
		// ESTABLECER LOS TOTALES
		txtTotalAbonadoFacturas.setText("" + recibo.getTotalFacturas());
		txtTotalAbonadoND.setText("" + recibo.getTotalND());
		txtTotalAbonadoNC.setText("" + recibo.getTotalNC());
		txtSubTotal.setText("" + recibo.getSubTotal());
		txtTotal.setText("" + recibo.getTotalRecibo());
		*/
       // loadData();
	}

	private void PagarMonto() {
		AppDialog.showMessage(me,"Ingrese el monto","",DialogType.DIALOGO_INPUT,new AppDialog.OnButtonClickListener(){
			@Override
			public void onButtonClick(AlertDialog alert, int actionId) {
				if(actionId == AppDialog.OK_BUTTOM)
				{
					float amount=0;
					amount = Float.parseFloat(((TextView)alert.findViewById(R.id.txtpayamount)).getText().toString());
					payamount(amount);
					
				}
			}});
	}	

	
	private void payamount (float mto)
	{
		//Declaracion de variables ;
		 float mtoOrig = mto;   
	     boolean seguir = true;
         boolean primeraPasada = true;
         while (seguir) {
        	recibo.getFacturasRecibo().clear();
        	recibo.getNotasDebitoRecibo().clear();
        	// Declaración de las listas principales
        	ArrayList<ReciboDetFactura> _facSeleccionadas = new ArrayList<ReciboDetFactura>();
     		ArrayList<ReciboDetND> _ndsSeleccionadas = new ArrayList<ReciboDetND>();
     	    ArrayList<Factura> facturas = new ArrayList<Factura>();
     		 
     		Factura[] facturaspendientes = cliente.getFacturasPendientes();
     		if((facturaspendientes!=null) && (facturaspendientes.length>0)){
     			 for(int i=0; i<facturaspendientes.length; i++) {
     				if(Cobro.FacturaEstaEnOtroRecibo(me.getContentResolver(),facturaspendientes[i].Id, true)==0){
     					facturas.add(facturaspendientes[i]);
     				}
     			 }
     		}
     		
     		ArrayList<CCNotaDebito> notas= new ArrayList<CCNotaDebito>();
     		CCNotaDebito[] notaspendientes = cliente.getNotasDebitoPendientes(); 
     		if ((notaspendientes != null) && (notaspendientes.length > 0)) {
     			 for(int i=0; i<notaspendientes.length; i++) {
     				 if (Cobro.NDEstaEnOtroRecibo(me.getContentResolver(),notaspendientes[i].getId(), true) == 0){
     					notas.add(notaspendientes[i]);
     				 }
     			 }
     		}
        	 
     		if (((facturas == null) || (facturas.size() == 0)) && ((notas == null) || (notas.size() == 0))) return;
     		
     		ArrayList<FacND> fact = new ArrayList<FacND>();
     		 for (Factura factura : facturas) {
     			FacND fnd = new FacND();
   				fnd.fac = factura;
				fnd.fechaVence= factura.getFechaVencimiento();
				fnd.tipo="FAC";
				fact.add(fnd);
			}
     		

         }
	}
	
}
