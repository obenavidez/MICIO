package com.panzyma.nm.fragments;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.panzyma.nm.controller.ControllerProtocol.*;

import com.panzyma.nm.NMApp; 
import com.panzyma.nm.CBridgeM.BLogicM.Result;
import com.panzyma.nm.auxiliar.AppDialog; 
import com.panzyma.nm.auxiliar.CustomDialog; 
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
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
import com.panzyma.nm.serviceproxy.DetallePedido;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView; 
@InvokeBridge(bridgeName = "BLogicM") 
public class CuentasPorCobrarFragment extends Fragment implements
		Handler.Callback {

	public enum TypeDetail {
		FACTURA, NOTAS_DEBITO, NOTAS_CREDITO
	}
	
	private static CustomDialog dlg;
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
	private String title = "LISTA FACTURAS (%s)";
	
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
	 View me ;
	protected int positioncache;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fcontext = activity;
		nmapp = (NMApp) activity.getApplicationContext();		
	} 	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putInt(ARG_POSITION, mCurrentPosition);
	  savedInstanceState.putLong(SUCURSAL_ID, objSucursalID);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
//	  Parcelable [] objects = new Parcelable[documentos.size()];
//	  if(objects!=null && documentos!=null){
//		  documentos.toArray(objects);
//		  savedInstanceState.putParcelableArray("documentos", objects);
//	  }
	   // etc.
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    if (savedInstanceState != null) {	       
	      Parcelable [] objects = savedInstanceState.getParcelableArray("documentos");
	  	  documentos = new ArrayList<GenericDocument>( (Collection<? extends GenericDocument>) Arrays.asList(objects) ); 
	  	  mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
	    }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SessionManager.setContext(this.getActivity()); 
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			objSucursalID = savedInstanceState.getLong(SUCURSAL_ID);
		}
		me = inflater.inflate(R.layout.cuentas_x_cobrar, container, false);
		return me;
	}	
	
	@Override
	public void onStart() {
		super.onStart();
		SessionManager.setContext(getActivity());
		NMApp.getController().setView(this);
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
	public boolean handleMessage(final Message msg) 
	{
		
		ocultarDialogos();	
		if(msg.what == ControllerProtocol.ERROR)
		{ 
			
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(getActivity(),((ErrorMessage) msg.obj).getTittle(),
							((ErrorMessage) msg.obj).getMessage(),
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
		switch (msg.what) {
			case ControllerProtocol.NOTIFICATION:
				showStatus(msg.obj.toString(), true);
				break;
			case ControllerProtocol.NOTIFICATION_DIALOG2:
				showStatus(msg.obj.toString());
				break;
		
			case ControllerProtocol.ERROR:
					AppDialog.showMessage(getActivity(), ((ErrorMessage) msg.obj).getTittle(),
							((ErrorMessage) msg.obj).getMessage(),
							DialogType.DIALOGO_ALERTA);
					break;
		}   
		return false;
	}
	
	public void ocultarDialogos() {
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
		if(waiting!=null && waiting.isShowing())
			waiting.dismiss();
	}
	
	public void showStatus(final String mensaje, boolean... confirmacion) 
	{

		ocultarDialogos();		 
		if (confirmacion.length != 0 && confirmacion[0]) 
		{
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(getActivity(), "", mensaje,
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
		} else 
		{
			getActivity().runOnUiThread(new Runnable() 
			{
				@Override
				public void run() {
					dlg = new CustomDialog(getActivity(), mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}
	

	@SuppressWarnings("unused")
	private void cargarEncabezadoCliente() {
 
		try 
		{ 
			Message msg = new Message();
			Bundle params = new Bundle();
			params.putLong("sucursalId", getSucursalId());
			msg.setData(params);
			NMApp.getController().getInboxHandler().sendMessage(msg);
			msg.what = ControllerProtocol.LOAD_DATA_FROM_SERVER; 
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
	public void cargarFacturasCliente() 
	{
		try 
		{ 
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putLong("SucursalId", getSucursalId());
			b.putInt("FechaInicFac", getFechaInicFac());
			b.putInt("FechaFinFac", getFechaFinFac());
			b.putBoolean("SoloFacturasConSaldo", isSoloFacturasConSaldo());
			b.putString("EstadoFac", getEstadoFac()); 
			msg.setData(b);
			msg.what = ControllerProtocol.LOAD_FACTURASCLIENTE_FROM_SERVER;
			
			NMApp.getController().getInboxHandler().sendMessage(msg); 
			
			if(waiting!=null) waiting.hide();	 
			
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
		try 
		{
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Notas D�bito...", true, false); 
			
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putLong("SucursalId", getSucursalId());
			b.putInt("FechaInicND", getFechaInicND());
			b.putInt("FechaFinND", getFechaFinND());
			b.putString("EstadoND", getEstadoND());
			msg.setData(b);
			msg.what = ControllerProtocol.LOAD_NOTAS_DEBITO_FROM_SERVER;
			
			NMApp.getController().getInboxHandler().sendMessage(msg);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LAS NOTAS DE CREDITO DEL SERVIDOR PANZYMA
	public void cargarNotasCredito() {
		try 
		{
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Notas Cr�dito...", true, false); 
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putLong("SucursalId", getSucursalId());
			b.putInt("FechaInicNC", getFechaInicNC());
			b.putInt("FechaFinNC", getFechaFinNC());
			b.putString("EstadoNC", getEstadoNC());
			msg.setData(b);
			msg.what = ControllerProtocol.LOAD_NOTAS_CREDITO_FROM_SERVER;
			
			NMApp.getController().getInboxHandler().sendMessage(msg); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LOS PEDIDOS DEL SERVIDOR PANZYMA
	public void cargarPedidos() {
		try 
		{
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Pedidos...", true, false); 
			
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putLong("SucursalId", getSucursalId());
			b.putInt("FechaInicPedidos", getFechaInicPedidos());
			b.putInt("FechaFinPedidos", getFechaFinPedidos());
			b.putString("EstadoPedidos", getEstadoPedidos());
			msg.setData(b);
			msg.what = ControllerProtocol.LOAD_PEDIDOS_FROM_SERVER;
			
			NMApp.getController().getInboxHandler().sendMessage(msg); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LOS RECIBOS DEL SERVIDOR PANZYMA
	public void cargarRecibosColector() {
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo Recibos...", true, false); 
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putLong("SucursalId", getSucursalId());
			b.putInt("FechaInicRCol", getFechaInicRCol());
			b.putInt("FechaFinRCol", getFechaFinRCol());
			b.putString("EstadoRCol", getEstadoRCol());
			msg.setData(b);
			msg.what = ControllerProtocol.LOAD_RECIBOS_FROM_SERVER;
			
			NMApp.getController().getInboxHandler().sendMessage(msg); 
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
		title = "LISTA FACTURAS (%s)";
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
			listaGenerica.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					Factura f  =(Factura) adapter.getItem(position);
					
					FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
					detalleFacturaFragment dialog = detalleFacturaFragment.newInstance(f.getId());
					dialog.show(transaction, "dialog");
					
					//TasaCambioFragment dialog = TasaCambioFragment.newInstance();
					//dialog.show(transaction, "dialog");
					
					return false;
				}
				
			});		
			listaGenerica.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) 
				{
					if ((parent.getChildAt(positioncache)) != null)
						(parent.getChildAt(positioncache))
								.setBackgroundResource(android.R.color.transparent);
					positioncache = position;
					adapter.setSelectedPosition(position);
					view.setBackgroundDrawable(parent.getResources().getDrawable(
							R.drawable.action_item_selected));
					
				}

			});
		} else 
		{
			headerGrid.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			if( adapter != null ) {
				adapter.clearItems();
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private void mostrarNotasDebito(ArrayList<CCNotaDebito> notasDebito) {
		title = "LISTA NOTAS DEBITO (%s)";
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
			mostrarDetalleConsulta("notas d�bito", true, fechaInicND, fechaFinND,
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
		title = "LISTA NOTAS CREDITO (%s)";
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
			mostrarDetalleConsulta("notas cr�dito", true, fechaInicNC, fechaFinNC,
					estadoNC);
			
			listaGenerica.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) 
				{
					if ((parent.getChildAt(positioncache)) != null)
						(parent.getChildAt(positioncache))
								.setBackgroundResource(android.R.color.transparent);
					positioncache = position;
					adapter.setSelectedPosition(position);
					view.setBackgroundDrawable(parent.getResources().getDrawable(
							R.drawable.action_item_selected));
					
				}

			});
			
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
		title = "LISTA PEDIDOS (%s)";
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
			listaGenerica.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) 
				{
					if ((parent.getChildAt(positioncache)) != null)
						(parent.getChildAt(positioncache))
								.setBackgroundResource(android.R.color.transparent);
					positioncache = position;
					adapter.setSelectedPosition(position);
					view.setBackgroundDrawable(parent.getResources().getDrawable(
							R.drawable.action_item_selected));
					
				}

			});
			
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
		title = "LISTA RECIBO (%s)";
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
			listaGenerica.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) 
				{
					if ((parent.getChildAt(positioncache)) != null)
						(parent.getChildAt(positioncache))
								.setBackgroundResource(android.R.color.transparent);
					positioncache = position;
					adapter.setSelectedPosition(position);
					view.setBackgroundDrawable(parent.getResources().getDrawable(
							R.drawable.action_item_selected));
					
				}

			});
			listaGenerica.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					if ((parent.getChildAt(positioncache)) != null)
						(parent.getChildAt(positioncache))
								.setBackgroundResource(android.R.color.transparent);
					positioncache = position;
					adapter.setSelectedPosition(position);
					view.setBackgroundDrawable(parent.getResources().getDrawable(
							R.drawable.action_item_selected));
					return true;
				}
			});
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
				"Mostrar Notas D�bito"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_NOTAS_CREDITO,
				"Mostrar Notas Cr�dito"));
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
									listaGenerica.setOnItemLongClickListener(null);
									cargarFacturasCliente();
									break;
								case MOSTRAR_NOTAS_DEBITO:
									listaGenerica.setOnItemLongClickListener(null);
									cargarNotasDebito();
									break;
								case MOSTRAR_NOTAS_CREDITO:
									listaGenerica.setOnItemLongClickListener(null);
									cargarNotasCredito();
									break;
								case MOSTRAR_RECIBOS:
									listaGenerica.setOnItemLongClickListener(null);
									cargarRecibosColector();
									break;
								case MOSTRAR_PEDIDOS:
									listaGenerica.setOnItemLongClickListener(null);
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
		NMApp.getController().setView((Callback)getActivity()); 
		 //savedInstanceState
		Log.d(TAG, "OnDetach"); 
		super.onDetach();
	}
	
	@Override
    public void onStop() {
        super.onStop(); 
        ocultarDialogos();
        NMApp.getController().setView((Callback)getActivity());  
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
		protected void onPostExecute(cuentaporcobrar objectResult) 
		{
			
			cuentaporcobrar value = objectResult; 
			if(value!=null && value.clienteseleccionado!=null && value.facturaspendientes!=null)
			{ 
			if(value.clienteseleccionado != null){ 
				txtViewCliente.setText(value.clienteseleccionado.getNombreCliente());
				txtViewLimiteCredito.setText(StringUtil.formatReal(value.clienteseleccionado.getLimiteCredito()));
				txtViewSaldo.setText(StringUtil.formatReal(value.clienteseleccionado.getSaldoActual()));
				txtViewDisponible.setText(StringUtil.formatReal(value.clienteseleccionado.getDisponible()));
				mostrarFacturas(value.facturaspendientes); 
			}
			ocultarDialogos(); 
			} else {
				if(waiting!=null) waiting.hide();
			} 
		}
		 
	 }
	 
	 
	 public class cuentaporcobrar
	 {
		 public CCCliente clienteseleccionado;
		 public ArrayList<Factura>  facturaspendientes;

	 }
	 
}