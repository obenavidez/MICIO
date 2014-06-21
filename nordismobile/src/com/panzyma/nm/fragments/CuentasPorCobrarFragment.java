package com.panzyma.nm.fragments;

import java.util.ArrayList;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BLogicM;
import com.panzyma.nm.CBridgeM.BLogicM.Result;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nm.viewmodel.vmFicha;
import com.panzyma.nm.viewmodel.vmRecibo;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CuentasPorCobrarFragment extends Fragment implements
		Handler.Callback {

	public enum TypeDetail {
		FACTURA, NOTAS_DEBITO, NOTAS_CREDITO
	}

	private TypeDetail typeDetail = TypeDetail.FACTURA;
	private int mCurrentPosition = -1;
	private vmRecibo cliente = null;
	private Context fcontext = null;
	private GenericAdapter adapter = null;
	private NMApp nmapp;
	private TextView txtViewCliente;
	private TextView txtViewLimiteCredito;
	private TextView txtViewSaldo;
	private TextView txtViewDisponible;
	private TextView gridheader;
	private TextView headerGrid;
	private ListView listaGenerica;
	private ProgressBar progressBar;

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

	public final static String ARG_POSITION = "position";
	public final static String OBJECT = "cliente";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			cliente = (vmRecibo) savedInstanceState.getParcelable(OBJECT);
		}
		return inflater.inflate(R.layout.cuentas_x_cobrar, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		initComponents();
		if (args != null) {
			cliente = (vmRecibo) args.getParcelable(OBJECT);
			mCurrentPosition = args.getInt(ARG_POSITION);
			cargarEncabezadoCliente();
		} else if (mCurrentPosition != -1) {
			cargarEncabezadoCliente();
		}
	}

	public long getSucursalId() {
		return cliente.getObjSucursalID();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {

		Result resultado = Result.toInt(msg.what);

		switch (resultado) {
		case CLIENTE:
			establecerDatosGenerales((CCCliente) msg.obj);
			break;
		case FACTURAS_CLIENTE:
			mostrarFacturas(((ArrayList<Factura>) msg.obj));
			break;
		case NOTAS_CREDITO:
			break;
		case NOTAS_DEBITO:
			break;
		case PEDIDOS:
			break;
		case RECIBOS_COLECTOR:
			break;
		default:
			break;
		}
		return false;
	}

	private void cargarEncabezadoCliente() {
		try {

			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(BLogicM.class.toString());
			nmapp.getController().setEntities(this, new BLogicM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(ControllerProtocol.LOAD_DATA_FROM_SERVER);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cargarFacturasCliente() {
		try {
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(BLogicM.class.toString());
			nmapp.getController().setEntities(this, new BLogicM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							ControllerProtocol.LOAD_FACTURASCLIENTE_FROM_SERVER);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
		Activity actividad = getActivity();
		// INICIALIZAR VARIABLES
		fechaInicPedidos = DateUtil.getToday();
		String s = String.valueOf(fechaInicPedidos);
		fechaInicPedidos = Integer.parseInt(s.substring(0, 6) + "01");
		fechaFinRCol = fechaFinPedidos;
		fechaInicRCol = fechaInicPedidos;
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

		headerGrid = (TextView) actividad.findViewById(R.id.cxctextv_header2);
		listaGenerica = (ListView) actividad.findViewById(R.id.cxclvgeneric);
	}

	private void establecerDatosGenerales(CCCliente cliente) {
		if (cliente != null) {

			txtViewCliente.setText(cliente.getNombreSucursal() + " - "
					+ cliente.getNombreSucursal());
			txtViewLimiteCredito.setText(StringUtil.formatReal(cliente
					.getLimiteCredito()));
			txtViewSaldo
					.setText(StringUtil.formatReal(cliente.getSaldoActual()));
			txtViewDisponible.setText(StringUtil.formatReal(cliente
					.getDisponible()));

			switch (typeDetail) {
			case FACTURA:
				cargarFacturasCliente();
				break;
			case NOTAS_CREDITO:
				break;
			case NOTAS_DEBITO:
				break;
			}

		}
	}

	private void mostrarFacturas(ArrayList<Factura> facturas) {
		if (facturas != null && facturas.size() > 0) {
			adapter = new GenericAdapter<Factura, FacturaViewHolder>(this
					.getActivity().getApplicationContext(),
					FacturaViewHolder.class, facturas, R.layout.detalle_factura);
			headerGrid.setText("Listado de Facturas (" + facturas.size() + ")");

			listaGenerica.setAdapter(adapter);
		}
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

}
