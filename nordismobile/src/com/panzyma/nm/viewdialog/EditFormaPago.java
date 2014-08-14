package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BValorCatalogoM;
import com.panzyma.nm.CBridgeM.BVentaM;
import com.panzyma.nm.CBridgeM.BValorCatalogoM.Petition;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.ValorCatalogoUtil;
import com.panzyma.nm.custom.model.SpinnerModel;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Recibo;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditFormaPago extends DialogFragment implements Handler.Callback {

	private View view;
	private TextView numero;
	private TextView fecha;
	private TextView tasa;
	private Spinner cmbFormaPago;
	private Spinner cmbBanco;
	private Spinner cmbMoneda;
	private EditText montoPago;
	private TextView montoNacional;
	private Recibo _recibo;
	private ReciboDetFormaPago pagoRecibo;
	private boolean editFormaPago = false;
	private List<ValorCatalogo> monedas;
	private List<ValorCatalogo> bancos;
	private List<ValorCatalogo> formasPago;
	private boolean catalogosReady = false;
	private NMApp nmapp;
	private CustomAdapter formaPagoAdapter;
	private CustomAdapter monedaAdapter;
	private String catalogNameToFound = "";
	private Bundle savedInstanceState = null;

	public EditFormaPago() {
		super();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.layout_seleccion_forma_pago, null);
		builder.setView(view);
		builder.setPositiveButton("AGREGAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.setNegativeButton("CANCELAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		initComponents();
		cargarCatalogoFormasPago();
		this.savedInstanceState = savedInstanceState;
		return builder.create();
	}

	@SuppressWarnings("unused")
	private void cargarCatalogoFormasPago() {
		try {
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(
					BValorCatalogoM.class.toString());
			catalogNameToFound = "FormaPago";
			nmapp.getController().setEntities(this, new BValorCatalogoM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							Petition.GET_VALORES_CATALOGO_BY_NAME
									.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void cargarCatalogoMonedas() {
		try {
			catalogosReady = false;
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(
					BValorCatalogoM.class.toString());
			catalogNameToFound = "Moneda";
			nmapp.getController().setEntities(this, new BValorCatalogoM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							Petition.GET_VALORES_CATALOGO_BY_NAME
									.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
		// OBTENIENDO LAS REFERENCIAS DE LAS VIEWS
		numero = (TextView) view.findViewById(R.id.txtNumero);
		fecha = (TextView) view.findViewById(R.id.txtFecha);
		tasa = (TextView) view.findViewById(R.id.txtTasa);
		montoPago = (EditText) view.findViewById(R.id.txtMontoPago);
		montoNacional = (TextView) view.findViewById(R.id.txtMontoNacional);
		cmbFormaPago = (Spinner) view.findViewById(R.id.cmb_forma_pago);
		cmbBanco = (Spinner) view.findViewById(R.id.cmb_banco);
		cmbMoneda = (Spinner) view.findViewById(R.id.cmb_moneda);
	}

	private void setValuesToView() {
		// INICIANDO MIEMBROS INTERNOS
		if (savedInstanceState != null) {
			editFormaPago = savedInstanceState
					.getBoolean(ViewReciboEdit.FORMA_PAGO_IN_EDITION);
			_recibo = savedInstanceState
					.getParcelable(ViewReciboEdit.OBJECT_TO_EDIT);
			if (!editFormaPago) {
				// AGREGANDO UNA FORMA DE PAGO
				pagoRecibo = new ReciboDetFormaPago();
				pagoRecibo.setCodEntidad("");
				pagoRecibo.setCodFormaPago("EFEC");
				pagoRecibo.setCodMoneda("COR");
				pagoRecibo.setDescEntidad("");
				pagoRecibo.setDescFormaPago("Efectivo");
				pagoRecibo.setDescMoneda("Córdoba");
				pagoRecibo.setFecha(0);
				pagoRecibo.setId(0);

				float montoPorPagar = StringUtil.round(_recibo.getTotalRecibo()
						- Cobro.getTotalPagoRecibo(_recibo), 2);
				pagoRecibo.setMonto(montoPorPagar);
				pagoRecibo.setMontoNacional(montoPorPagar);
				pagoRecibo.setNumero(""); // No es CHK
				pagoRecibo.setObjEntidadID(0);
				pagoRecibo.setObjFormaPagoID(ValorCatalogoUtil
						.getValorCatalogoID(formasPago, "EFEC"));
				pagoRecibo.setObjMonedaID(ValorCatalogoUtil.getValorCatalogoID(
						formasPago, "COR"));
				pagoRecibo.setSerieBilletes("");
				pagoRecibo.setTasaCambio(1.0F);
			} else {
				// EDITANDO UNA FORMA DE PAGO
			}

			numero.setText(pagoRecibo.getNumero());
			fecha.setText(DateUtil.idateToStr(pagoRecibo.getFecha()).toString());
			tasa.setText(StringUtil.formatReal(pagoRecibo.getTasaCambio()));
			montoPago.setText(StringUtil.formatReal(pagoRecibo.getMonto()));
			montoNacional.setText(StringUtil.formatReal(pagoRecibo
					.getMontoNacional()));

		}
	}

	public String getCatalogNameToFound() {
		return catalogNameToFound;
	}

	public ReciboDetFormaPago getPagoRecibo() {
		return pagoRecibo;
	}

	public void setPagoRecibo(ReciboDetFormaPago _pagoRecibo) {
		this.pagoRecibo = _pagoRecibo;
	}

	@Override
	public boolean handleMessage(Message msg) {
		Petition response = Petition.toInt(msg.what);
		switch (response) {
		case GET_VALORES_CATALOGO_BY_NAME:
			establecer((Catalogo) msg.obj);
			break;
		}
		return false;
	}

	private void establecer(Catalogo obj) {
		if ("FormaPago".equals(obj.getNombreCatalogo().trim())) {
			formasPago = obj.getValoresCatalogo();
			formaPagoAdapter = new CustomAdapter(this.getActivity(),
					R.layout.spinner_rows, setListData(formasPago));
			cmbFormaPago.setAdapter(formaPagoAdapter);
			cargarCatalogoMonedas();
		} else if ("Moneda".equals(obj.getNombreCatalogo().trim())) {
			monedas = obj.getValoresCatalogo();
			monedaAdapter = new CustomAdapter(this.getActivity(),
					R.layout.spinner_rows, setListData(monedas));
			cmbMoneda.setAdapter(monedaAdapter);
			setValuesToView();
		} else if ("banco".equals(obj.getNombreCatalogo().trim())) {
			bancos = obj.getValoresCatalogo();
		}
	}

	public ArrayList<SpinnerModel> setListData(List<ValorCatalogo> valores) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
		// Now i have taken static values by loop.
		// For further inhancement we can take data by webservice / json / xml;

		for (ValorCatalogo valor : valores) {

			final SpinnerModel sched = new SpinnerModel();

			/******* Firstly take data in model object ******/
			sched.setId(valor.getId());
			sched.setCodigo(valor.getCodigo());
			sched.setDescripcion(valor.getDescripcion());

			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}
		return CustomListViewValuesArr;
	}

}
