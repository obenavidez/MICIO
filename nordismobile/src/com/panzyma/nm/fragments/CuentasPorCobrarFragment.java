package com.panzyma.nm.fragments;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BLogicM;
import com.panzyma.nm.CBridgeM.BLogicM.Result;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.interfaces.GenericDocument;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.serviceproxy.CCPedido;
import com.panzyma.nm.serviceproxy.CCReciboColector;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nm.view.viewholder.NotaCreditoViewHolder;
import com.panzyma.nm.view.viewholder.NotaDebitoViewHolder;
import com.panzyma.nm.view.viewholder.PedidoViewHolder;
import com.panzyma.nm.view.viewholder.ReciboViewHolder;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
@InvokeBridge(bridgeName ="BLogicM")
public class CuentasPorCobrarFragment extends Fragment implements
		Handler.Callback {

	public enum TypeDetail {
		FACTURA, NOTAS_DEBITO, NOTAS_CREDITO
	}
	private static final String TAG = CuentasPorCobrarFragment.class.getSimpleName();
	private TypeDetail typeDetail = TypeDetail.FACTURA;
	private int mCurrentPosition = -1;
	private long objSucursalID ;
	private Context fcontext = null;
	private GenericAdapter adapter = null;
	private NMApp nmapp;
	private TextView txtViewCliente;
	private TextView txtViewLimiteCredito;
	private TextView txtViewSaldo;
	private TextView txtViewDisponible;
	private TextView gridheader;
	private TextView headerGrid;
	private TextView txtenty;
	private ListView listaGenerica;
	static ProgressDialog waiting;
	private EditText search;
	private QuickAction quickAction;
	private List<GenericDocument> filterDocs = new ArrayList<GenericDocument>();	
	private List<GenericDocument> documentos = new ArrayList<GenericDocument>();
	private Display display;
	private Button btnMenu;
	private String title = "Listado de Facturas (%s)";
	
	private int fechaFinFac = 0;
	private int fechaInicFac = 0;
	private String estadoFac = "TODOS";
	private boolean soloFacturasConSaldo = true;

	private int fechaFinPedidos = 0;
	private int fechaInicPedidos = 0;
	private String estadoPedidos = "TODOS";

	private int fechaFinRCol = 0;
	private int fechaInicRCol = 0;
	private String estadoRCol = "TODOS";

	private int fechaFinNC = 0;
	private int fechaInicNC = 0;
	private String estadoNC = "AUTORIZADA";

	private int fechaFinND = 0;
	private int fechaInicND = 0;
	private String estadoND = "AUTORIZADA";
	
	private static final int MOSTRAR_FACTURAS = 0;
	private static final int MOSTRAR_NOTAS_DEBITO = 1;
	private static final int MOSTRAR_NOTAS_CREDITO = 2;
	private static final int MOSTRAR_PEDIDOS = 3;
	private static final int MOSTRAR_RECIBOS = 4;

	public final static String ARG_POSITION = "position";
	public final static String SUCURSAL_ID = "sucursalID";
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		nmapp = (NMApp) activity.getApplicationContext();		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putLong(SUCURSAL_ID, objSucursalID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SessionManager.setContext(this.getActivity()); 
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			objSucursalID = savedInstanceState.getLong(SUCURSAL_ID);
		}
		return inflater.inflate(R.layout.cuentas_x_cobrar, container, false);
	}	
	
	@Override
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		initComponents();
		if (args != null) {
			objSucursalID = args.getLong(SUCURSAL_ID);
			mCurrentPosition = args.getInt(ARG_POSITION);
			//cargarEncabezadoCliente();
		} 
		/*else if (mCurrentPosition != -1) {
			cargarEncabezadoCliente();
		}*/
		LoadDataToUI sync = new LoadDataToUI();
		sync.execute();
	}	 
	
	public long getSucursalId() {
		return objSucursalID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {

		if(msg.what < 6) {
			
			Result resultado = Result.toInt(msg.what);

			switch (resultado) {
			case CLIENTE:
				establecerDatosGenerales((CCCliente) msg.obj);
				break;
			case FACTURAS_CLIENTE:
				mostrarFacturas(((ArrayList<Factura>) msg.obj));
				break;
			case NOTAS_CREDITO:
				mostrarNotasCredito(((ArrayList<CCNotaCredito>) msg.obj));
				break;
			case NOTAS_DEBITO:
				mostrarNotasDebito(((ArrayList<CCNotaDebito>) msg.obj));
				break;
			case PEDIDOS:
				mostrarPedidos(((ArrayList<CCPedido>) msg.obj));
				break;
			case RECIBOS_COLECTOR:
				mostrarRecibosColector(((ArrayList<CCReciboColector>) msg.obj));
				break;
			default:
				break;
			}			
		}		
		return false;
	}

	@SuppressWarnings("unused")
	private void cargarEncabezadoCliente() {
		try {

			nmapp = (NMApp) this.getActivity().getApplication();
			
			Message msg = new Message();
			Bundle params = new Bundle();
			params.putLong("sucursalId", getSucursalId());
			msg.setData(params);
			msg.what = ControllerProtocol.LOAD_DATA_FROM_SERVER;
			
			NMApp.getController().setView(this);
			NMApp.getController().
				getInboxHandler().
				sendMessage(msg);
			
			waiting = new ProgressDialog(getActivity());
			waiting.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waiting.setMessage("Buscando Info del Cliente...");
			waiting.setCancelable(false);
			waiting.show();		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LAS FACTURAS DEL SERVIDOR PANZYMA
	public void cargarFacturasCliente() {
		try {
			
			if(waiting!=null) waiting.hide();			
			
			nmapp = (NMApp) this.getActivity().getApplication();
		
			Message msg = new Message();
			
			Bundle params = new Bundle();
			params.putLong("sucursalId", getSucursalId());
			params.putInt("fechaInic", getFechaInicFac());
			params.putInt("fechaFin", getFechaFinFac());
			params.putBoolean("soloConSaldo", isSoloFacturasConSaldo());
			params.putString("estadoFac", getEstadoFac());
			
			msg.setData(params);
			msg.what = ControllerProtocol.LOAD_FACTURASCLIENTE_FROM_SERVER;
			
			NMApp.getController().setView(this);
			NMApp.getController().
				getInboxHandler().
				sendMessage(msg);			
			
			waiting = new ProgressDialog(getActivity());
			waiting.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waiting.setMessage("Trayendo Facturas...");
			waiting.setCancelable(false);
			waiting.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LAS NOTAS DE DEBITO DEL SERVIDOR PANZYMA
	public void cargarNotasDebito() {
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Notas Débito...", true, false);
			nmapp = (NMApp) this.getActivity().getApplication();
			
			Message msg = new Message();
			
			Bundle params = new Bundle();
			params.putLong("sucursalId", getSucursalId());
			params.putInt("fechaInic", getFechaInicND());
			params.putInt("fechaFin", getFechaFinND());			
			params.putString("estadoND", getEstadoND());
			
			msg.setData(params);			
			msg.what = ControllerProtocol.LOAD_NOTAS_DEBITO_FROM_SERVER;
			
			NMApp.getController().setView(this);
			NMApp.getController()
					.getInboxHandler()
					.sendMessage(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LAS NOTAS DE CREDITO DEL SERVIDOR PANZYMA
	public void cargarNotasCredito() {
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Notas Crédito...", true, false);
			nmapp = (NMApp) this.getActivity().getApplication();
			
			Message msg = new Message();
			
			Bundle params = new Bundle();
			params.putLong("sucursalId", getSucursalId());
			params.putInt("fechaInic", getFechaInicNC());
			params.putInt("fechaFin", getFechaFinNC());			
			params.putString("estadoNC", getEstadoNC());
			
			msg.setData(params);			
			msg.what = ControllerProtocol.LOAD_NOTAS_CREDITO_FROM_SERVER;
			
			NMApp.getController().setView(this);
			NMApp.getController()
					.getInboxHandler()
					.sendMessage(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LOS PEDIDOS DEL SERVIDOR PANZYMA
	public void cargarPedidos() {
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Pedidos...", true, false);
			nmapp = (NMApp) this.getActivity().getApplication();
			
			Message msg = new Message();
			
			Bundle params = new Bundle();
			params.putLong("sucursalId", getSucursalId());
			params.putInt("fechaInic", getFechaInicPedidos());
			params.putInt("fechaFin", getFechaFinPedidos());			
			params.putString("estadoPedidos", getEstadoPedidos());
			
			msg.setData(params);			
			msg.what = ControllerProtocol.LOAD_PEDIDOS_FROM_SERVER;
			
			NMApp.getController().setView(this);
			NMApp.getController()
					.getInboxHandler()
					.sendMessage(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LOS RECIBOS DEL SERVIDOR PANZYMA
	public void cargarRecibosColector() {
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Recibos...", true, false);
			nmapp = (NMApp) this.getActivity().getApplication();
			
			Message msg = new Message();
			
			Bundle params = new Bundle();
			params.putLong("sucursalId", getSucursalId());
			params.putInt("fechaInic", getFechaInicRCol());
			params.putInt("fechaFin", getFechaFinRCol());			
			params.putString("estadoRecibos", getEstadoRCol());
			
			msg.setData(params);			
			msg.what = ControllerProtocol.LOAD_RECIBOS_FROM_SERVER;
			
			NMApp.getController().setView(this);
			NMApp.getController()
					.getInboxHandler()
					.sendMessage(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void initComponents() {
		Activity actividad = getActivity();
		// INICIALIZAR VARIABLES
		fechaFinPedidos = DateUtil.getToday();
		String s = String.valueOf(fechaFinPedidos);
		fechaInicPedidos = Integer.parseInt(s.substring(0, 6) + "01");		
		fechaInicPedidos = DateUtil.d2i(Date.valueOf("2014-01-01")) ;		
		
		fechaFinRCol = fechaFinPedidos;
		fechaInicRCol = fechaInicPedidos;
		fechaInicND = fechaInicPedidos;
		fechaFinND = fechaFinPedidos;
		fechaInicNC = fechaInicND;
		fechaFinNC = fechaFinND;
		// OBTENER LAS REFERENCIAS DE LAS VISTAS
		txtViewCliente = (TextView) actividad
				.findViewById(R.id.cctextv_detallecliente);
		txtViewLimiteCredito = (TextView) actividad
				.findViewById(R.id.cctextv_detallelimitecredito);
		txtViewSaldo = (TextView) actividad
				.findViewById(R.id.cctextv_detallesaldo);
		txtViewDisponible = (TextView) actividad
				.findViewById(R.id.cctextv_detalledisponible);
		gridheader = (TextView) actividad.findViewById(R.id.ctextv_gridheader);
		gridheader.setVisibility(View.INVISIBLE);
		gridheader.setHeight(0);
		txtenty = (TextView) getActivity().findViewById(R.id.ctxtview_enty);
		headerGrid = (TextView) actividad.findViewById(R.id.cxctextv_header2);
		listaGenerica = (ListView) actividad.findViewById(R.id.cxclvgeneric);	
		
		
		search = (EditText) actividad.findViewById(R.id.cxctextv_detalle_generico);
		
		search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				List<GenericDocument> docs = documentos;	
				//LIMPIAR LOS DOCUMENTOS FILTRADOS
				filterDocs.clear();
				if (docs.size() > 0){
					String docNumerToFound = s.toString();
					for(GenericDocument doc : docs) {
						if( doc.getDocumentNumber().contains(docNumerToFound)){
							filterDocs.add(doc);
						}
					}
					headerGrid.setText(String.format(title, filterDocs.size()));
					adapter.setItems(filterDocs);
					adapter.notifyDataSetChanged();
				}				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		WindowManager wm = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		
		btnMenu = (Button) getActivity().findViewById(R.id.btnMenu);
		
		initMenu();
	}
	
	public void mostrarMenu() {		
		quickAction.show(btnMenu, display, true);
	} 

	private void establecerDatosGenerales(CCCliente cliente) {
		if (cliente != null) {

			txtViewCliente.setText(
					cliente.getNombreCliente()
					);
			txtViewLimiteCredito.setText(
					StringUtil.formatReal(cliente.getLimiteCredito())
					);
			txtViewSaldo.setText(
					StringUtil.formatReal(cliente.getSaldoActual())
					);
			txtViewDisponible.setText(
					StringUtil.formatReal(cliente.getDisponible())
					);

			switch (typeDetail) {
			case FACTURA:
				cargarFacturasCliente();
				break;
			case NOTAS_CREDITO:
				break;
			case NOTAS_DEBITO:
				cargarNotasDebito();
				break;
			}

		}
	}	
	
	private void addDocuments(Object[] objects){
		documentos.clear();
		for(Object doc: objects)
			documentos.add((GenericDocument) doc);
	}
	
	private void mostrarDetalleConsulta(String tipoDocumento, boolean conSaldoPendiente, int fechaInic, int fechaFinal, String estado ) {
		String s = String.format("Mostrando %s: ", tipoDocumento);
        if (conSaldoPendiente) s = s + "con saldo pendiente.";
        if (fechaInic > 0)
            s = s + " desde " + DateUtil.idateToStr(fechaInic);
        if (fechaFinal > 0)
            s = s + " hasta " + DateUtil.idateToStr(fechaFinal);
        if (fechaInic + fechaFinal > 0) s = s + ".";
        if (estado.compareTo("TODOS") != 0)
            s = s + " con estado " + estado + ".";
        Util.Message.buildToastMessage(getActivity(), s, 3000).show();
        
	}

	private void mostrarFacturas(ArrayList<Factura> facturas) {	
		title = "Listado de Facturas (%s)";
		if (facturas != null && facturas.size() > 0) {
			addDocuments(facturas.toArray());
			adapter = new GenericAdapter<Factura, FacturaViewHolder>(
					this.getActivity().getApplicationContext(),
					FacturaViewHolder.class,
					facturas,
					R.layout.detalle_factura);			
			txtenty.setVisibility(View.INVISIBLE);
			headerGrid.setText(String.format(title, facturas.size()));
			listaGenerica.setAdapter(adapter);
			mostrarDetalleConsulta("facturas", true, fechaInicFac, fechaFinFac,
					estadoFac);
		} else {
			headerGrid.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			if( adapter != null ) {
				adapter.clearItems();
				adapter.notifyDataSetChanged();
			}
		}
		//waiting.dismiss();
		waiting.hide();
	}
	
	private void mostrarNotasDebito(ArrayList<CCNotaDebito> notasDebito) {
		title = "Listado de Notas Débito (%s)";
		if (notasDebito != null && notasDebito.size() > 0) {
			addDocuments(notasDebito.toArray());
			adapter = new GenericAdapter<CCNotaDebito, NotaDebitoViewHolder>(
					this.getActivity().getApplicationContext(),
					NotaDebitoViewHolder.class,
					notasDebito,
					R.layout.detalle_nota_debito);
			txtenty.setVisibility(View.INVISIBLE);
			headerGrid.setText(String.format(title, notasDebito.size()));
			listaGenerica.setAdapter(adapter);
			mostrarDetalleConsulta("notas débito", true, fechaInicND, fechaFinND,
					estadoND);
		} else {
			headerGrid.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			if( adapter != null ) {
				adapter.clearItems();
				adapter.notifyDataSetChanged();
			}			
		}
		waiting.dismiss();
	}
	
	private void mostrarNotasCredito(ArrayList<CCNotaCredito> notasCredito) {
		title = "Listado de Notas Crédito (%s)";
		if (notasCredito != null && notasCredito.size() > 0) {
			addDocuments(notasCredito.toArray());
			adapter = new GenericAdapter<CCNotaCredito, NotaCreditoViewHolder>(
					this.getActivity().getApplicationContext(),
					NotaCreditoViewHolder.class,
					notasCredito,
					R.layout.detalle_nota_credito);
			txtenty.setVisibility(View.INVISIBLE);
			headerGrid.setText(String.format(title, notasCredito.size()));
			listaGenerica.setAdapter(adapter);
			mostrarDetalleConsulta("notas crédito", true, fechaInicNC, fechaFinNC,
					estadoNC);
		} else {
			headerGrid.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			if( adapter != null ) {
				adapter.clearItems();
				adapter.notifyDataSetChanged();
			}
		}
		waiting.dismiss();
	}
	
	private void mostrarPedidos(ArrayList<CCPedido> pedidos) {
		title = "Listado de Pedidos (%s)";
		if (pedidos != null && pedidos.size() > 0) {
			addDocuments(pedidos.toArray());
			adapter = new GenericAdapter<CCPedido, PedidoViewHolder>(
					this.getActivity().getApplicationContext(),
					PedidoViewHolder.class,
					pedidos,
					R.layout.detalle_pedido);
			txtenty.setVisibility(View.INVISIBLE);
			headerGrid.setText(String.format(title, pedidos.size()));
			listaGenerica.setAdapter(adapter);
			mostrarDetalleConsulta("pedidos", true, fechaInicPedidos, fechaFinPedidos,
					estadoPedidos);
		} else {
			headerGrid.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			if( adapter != null ) {
				adapter.clearItems();
				adapter.notifyDataSetChanged();
			}
		}
		waiting.dismiss();
	}
	
	private void mostrarRecibosColector(ArrayList<CCReciboColector> recibos) {
		title = "Listado de Recibos (%s)";
		if (recibos != null && recibos.size() > 0) {
			addDocuments(recibos.toArray());
			adapter = new GenericAdapter<CCReciboColector, ReciboViewHolder>(
					this.getActivity().getApplicationContext(),
					ReciboViewHolder.class,
					recibos,
					R.layout.detalle_recibo_colector);
			txtenty.setVisibility(View.INVISIBLE);
			headerGrid.setText(String.format(title, recibos.size()));
			listaGenerica.setAdapter(adapter);
			mostrarDetalleConsulta("recibos", true, fechaInicRCol, fechaFinRCol,
					estadoRCol);
		} else {
			headerGrid.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			if( adapter != null ) {
				adapter.clearItems();
				adapter.notifyDataSetChanged();
			}
		}
		waiting.dismiss();
	}
	
	private void initMenu() {
		quickAction = new QuickAction(this.getActivity(), QuickAction.VERTICAL, 1);
		quickAction.addActionItem(new ActionItem(MOSTRAR_FACTURAS,
				"Mostrar Facturas"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_NOTAS_DEBITO,
				"Mostrar Notas Débito"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_NOTAS_CREDITO,
				"Mostrar Notas Crédito"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(MOSTRAR_PEDIDOS,
				"Mostrar Pedidos"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_RECIBOS,
				"Mostrar Recibos"));		

		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, final int pos,
							int actionId) {

						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ActionItem actionItem = quickAction.getActionItem(pos);
								
								switch (actionItem.getActionId()) {
								case MOSTRAR_FACTURAS:
									cargarFacturasCliente();
									break;
								case MOSTRAR_NOTAS_DEBITO:
									cargarNotasDebito();
									break;
								case MOSTRAR_NOTAS_CREDITO:
									cargarNotasCredito();
									break;
								case MOSTRAR_RECIBOS:
									cargarRecibosColector();
									break;
								case MOSTRAR_PEDIDOS:
									cargarPedidos();
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
		
	public int getFechaFinFac() {
		return fechaFinFac;
	}

	public void setFechaFinFac(int fechaFinFac) {
		this.fechaFinFac = fechaFinFac;
	}

	public int getFechaInicFac() {
		return fechaInicFac;
	}

	public void setFechaInicFac(int fechaInicFac) {
		this.fechaInicFac = fechaInicFac;
	}

	public String getEstadoFac() {
		return estadoFac;
	}

	public void setEstadoFac(String estadoFac) {
		this.estadoFac = estadoFac;
	}

	public boolean isSoloFacturasConSaldo() {
		return soloFacturasConSaldo;
	}

	public void setSoloFacturasConSaldo(boolean soloFacturasConSaldo) {
		this.soloFacturasConSaldo = soloFacturasConSaldo;
	}

	public int getFechaFinPedidos() {
		return fechaFinPedidos;
	}

	public void setFechaFinPedidos(int fechaFinPedidos) {
		this.fechaFinPedidos = fechaFinPedidos;
	}

	public int getFechaInicPedidos() {
		return fechaInicPedidos;
	}

	public void setFechaInicPedidos(int fechaInicPedidos) {
		this.fechaInicPedidos = fechaInicPedidos;
	}

	public String getEstadoPedidos() {
		return estadoPedidos;
	}

	public void setEstadoPedidos(String estadoPedidos) {
		this.estadoPedidos = estadoPedidos;
	}

	public int getFechaFinRCol() {
		return fechaFinRCol;
	}

	public void setFechaFinRCol(int fechaFinRCol) {
		this.fechaFinRCol = fechaFinRCol;
	}

	public int getFechaInicRCol() {
		return fechaInicRCol;
	}

	public void setFechaInicRCol(int fechaInicRCol) {
		this.fechaInicRCol = fechaInicRCol;
	}

	public String getEstadoRCol() {
		return estadoRCol;
	}

	public void setEstadoRCol(String estadoRCol) {
		this.estadoRCol = estadoRCol;
	}

	public int getFechaFinNC() {
		return fechaFinNC;
	}

	public void setFechaFinNC(int fechaFinNC) {
		this.fechaFinNC = fechaFinNC;
	}

	public int getFechaInicNC() {
		return fechaInicNC;
	}

	public void setFechaInicNC(int fechaInicNC) {
		this.fechaInicNC = fechaInicNC;
	}

	public String getEstadoNC() {
		return estadoNC;
	}

	public void setEstadoNC(String estadoNC) {
		this.estadoNC = estadoNC;
	}

	public int getFechaFinND() {
		return fechaFinND;
	}

	public void setFechaFinND(int fechaFinND) {
		this.fechaFinND = fechaFinND;
	}

	public int getFechaInicND() {
		return fechaInicND;
	}

	public void setFechaInicND(int fechaInicND) {
		this.fechaInicND = fechaInicND;
	}

	public String getEstadoND() {
		return estadoND;
	}

	public void setEstadoND(String estadoND) {
		this.estadoND = estadoND;
	}

	@Override
	public void onDetach ()
	{
		Log.d(TAG, "OnDetach");
		super.onDetach();
	}
	
	@Override
    public void onStop() {
        super.onStop();
        if(waiting!=null)waiting.dismiss(); // try this
        Log.d(TAG, "onStop");
    }
	
	 private class LoadDataToUI extends AsyncTask<Void, Void, cuentaporcobrar > {
		 
		@Override
		protected void onPreExecute() {
			waiting = new ProgressDialog(getActivity());
			waiting.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waiting.setMessage("Buscando Info del Cliente...");
			waiting.setCancelable(false);
			waiting.show();
			super.onPreExecute();
		} 
		 
		 
		@Override
		protected cuentaporcobrar doInBackground(Void... params) {
			
			cuentaporcobrar resultado = new cuentaporcobrar();
			final String credentials = SessionManager.getCredentials();
			if (!credentials.trim().equals("") && NMNetWork.CheckConnection(NMApp.controller) ) {
				CCCliente cliente = new CCCliente();
				cliente= ModelLogic.getCuentasPorCobrarDelCliente(credentials,getSucursalId());
				 ArrayList<Factura> facturas=ModelLogic.getFacturasCliente(credentials,
																		    getSucursalId(),
																			getFechaInicFac(),
																			getFechaFinFac(),
																			isSoloFacturasConSaldo(),
																			getEstadoFac());
				
				 resultado.clienteseleccionado = cliente;
				 resultado.facturaspendientes = facturas;

			}
			return resultado;
		}
		
		@Override
		protected void onPostExecute(cuentaporcobrar objectResult) {
			cuentaporcobrar value = objectResult;
			txtViewCliente.setText(value.clienteseleccionado.getNombreCliente());
			txtViewLimiteCredito.setText(StringUtil.formatReal(value.clienteseleccionado.getLimiteCredito()));
			txtViewSaldo.setText(StringUtil.formatReal(value.clienteseleccionado.getSaldoActual()));
			txtViewDisponible.setText(StringUtil.formatReal(value.clienteseleccionado.getDisponible()));
			mostrarFacturas(value.facturaspendientes);

		}
		 
	 }
	 
	 
	 public class cuentaporcobrar
	 {
		 public CCCliente clienteseleccionado;
		 public ArrayList<Factura>  facturaspendientes;

	 }
	
}
