package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BValorCatalogoM;
import com.panzyma.nm.CBridgeM.BVentaM;
import com.panzyma.nm.CBridgeM.BValorCatalogoM.Petition;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EditFormaPago extends DialogFragment implements Handler.Callback {

	private static final int TIME_TO_MESSAGE = 3000;	
	private View view;
	private EditText numero;
	private EditText fecha;
	private EditText tasa;
	private Spinner cmbFormaPago;
	private Spinner cmbBanco;
	private Spinner cmbMoneda;
	private EditText montoPago;
	private EditText montoNacional;
	private EditText numeroSerie;
	private TableRow tblRowFormaPago;
	private TableRow tblRowNumero;
	private TableRow tblRowFecha;
	private TableRow tblRowBanco;
	private TableRow tblRowMoneda;
	private TableRow tblRowTasa;
	private TableRow tblRowMontoPago;
	private TableRow tblRowMontoPagoNacional;
	private TableRow tblRowNumeroSerie;
	
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = getArguments();
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
		return builder.create();
	}

	@SuppressWarnings("unused")
	private void cargarCatalogoFormasPago() {
		try {
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(
					BValorCatalogoM.class.toString());
			nmapp.getController().setEntities(this, new BValorCatalogoM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(Petition.FORMAS_PAGOS.getActionCode());

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
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(Petition.MONEDAS.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
		// OBTENIENDO LAS REFERENCIAS DE LAS VIEWS
		numero = (EditText) view.findViewById(R.id.txtNumero);
		fecha = (EditText) view.findViewById(R.id.txtFecha);
		tasa = (EditText) view.findViewById(R.id.txtTasa);
		montoPago = (EditText) view.findViewById(R.id.txtMontoPago);
		montoNacional = (EditText) view.findViewById(R.id.txtMontoNacional);
		cmbFormaPago = (Spinner) view.findViewById(R.id.cmb_forma_pago);
		cmbBanco = (Spinner) view.findViewById(R.id.cmb_banco);
		cmbMoneda = (Spinner) view.findViewById(R.id.cmb_moneda);
		numeroSerie = (EditText) view.findViewById(R.id.txtNumeroSerie);
		tblRowFormaPago = (TableRow) view.findViewById(R.id.tblRowFormaPago);
		tblRowNumero = (TableRow) view.findViewById(R.id.tblRowNumero);
		tblRowFecha = (TableRow) view.findViewById(R.id.tblRowFecha);
		tblRowBanco = (TableRow) view.findViewById(R.id.tblRowBanco);
		tblRowMoneda = (TableRow) view.findViewById(R.id.tblRowMoneda);
		tblRowTasa = (TableRow) view.findViewById(R.id.tblRowTasa);
		tblRowMontoPago = (TableRow) view.findViewById(R.id.tblRowMontoPago);
		tblRowMontoPagoNacional = (TableRow) view
				.findViewById(R.id.tblRowMontoNacional);
		tblRowNumeroSerie = (TableRow) view.findViewById(R.id.tblRowNumero);
	}

	private void setValuesToView() {
		// INICIANDO MIEMBROS INTERNOS
		if (savedInstanceState != null) {
			editFormaPago = savedInstanceState.getBoolean(ViewReciboEdit.FORMA_PAGO_IN_EDITION);
			_recibo = savedInstanceState.getParcelable(ViewReciboEdit.OBJECT_TO_EDIT);
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
			boolean pagoEnEfectivo = pagoRecibo.getCodFormaPago().compareTo("EFEC") == 0;
			boolean pagoEnCordoba = pagoRecibo.getCodMoneda().compareTo("COR") == 0;

			//ESTABLECIENDO VISIBILIDAD PARA LAS FILAS DEL LAYOUT 
			if( pagoEnEfectivo )
			{								
				tblRowNumero.setVisibility(View.GONE);			
				tblRowFecha.setVisibility(View.GONE);
				tblRowBanco.setVisibility(View.GONE);
				tblRowTasa.setVisibility(View.GONE);
			}
			if( pagoEnCordoba ) {
				tblRowNumeroSerie.setVisibility(View.GONE);
			}
			
			numero.setText(pagoRecibo.getNumero());
			tasa.setText(StringUtil.formatReal(pagoRecibo.getTasaCambio()));
			montoPago.setText(StringUtil.formatReal(pagoRecibo.getMonto()));
			montoNacional.setText(StringUtil.formatReal(pagoRecibo
					.getMontoNacional()));
			numeroSerie.setText(pagoRecibo.getSerieBilletes());
					
			numero.setEnabled( !pagoEnEfectivo );
			fecha.setEnabled( !pagoEnEfectivo );
			cmbBanco.setEnabled( !pagoEnEfectivo );
			numeroSerie.setEnabled( !pagoEnCordoba );

			if (pagoRecibo.getFecha() == 0) {
				fecha.setText(DateUtil.idateToStr(DateUtil.getNow()).toString());
			} else {
				fecha.setText(DateUtil.idateToStr(pagoRecibo.getFecha())
						.toString());
			}	
			
			for(int i=0; ( formasPago != null && i < formasPago.size() ); i++) {
	            if(formasPago.get(i).getCodigo().compareTo(pagoRecibo.getCodFormaPago()) == 0) {
	                cmbFormaPago.setSelection(i);
	                break;
	            }
	        }
						 
			for (int i = 0; ( bancos != null && i < bancos.size() ); i++) {
				if (bancos.get(i).getCodigo()
						.compareTo(pagoRecibo.getCodEntidad()) == 0) {
					cmbBanco.setSelection(i);
					break;
				}
			}
			
			for(int i=0; ( monedas != null && i < monedas.size() ); i++) {
	            if(monedas.get(i).getCodigo().compareTo(pagoRecibo.getCodMoneda()) == 0) {
	                cmbMoneda.setSelection(i);
	                break;
	            }
	        }
			
			cmbFormaPago.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					if( position == 0 ) return;
					ValorCatalogo _formaPago = (ValorCatalogo) ((SpinnerModel)formaPagoAdapter.getItem(position)).getObj();
					CambiaFormaPago(_formaPago.getCodigo());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parentView) {
					// your code here
				}

			});
			
			cmbMoneda.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					if( position == 0 ) return;
					ValorCatalogo _moneda = (ValorCatalogo) ((SpinnerModel)monedaAdapter.getItem(position)).getObj();
					CambioMoneda(_moneda.getCodigo());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parentView) {
					// your code here
				}

			});

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
			default:
				establecer((Catalogo) msg.obj);
				break;
		}
		return false;
	}

	private void establecer(Catalogo obj) {
		if ("FormaPago".equals(obj.getNombreCatalogo().trim())) {
			formasPago = obj.getValoresCatalogo();
			formasPago.add(0, new ValorCatalogo(-1,"",""));
			formaPagoAdapter = new CustomAdapter(this.getActivity(),
					R.layout.spinner_rows, setListData(formasPago));
			cmbFormaPago.setAdapter(formaPagoAdapter);
			cargarCatalogoMonedas();
		} else if ("Moneda".equals(obj.getNombreCatalogo().trim())) {
			monedas = obj.getValoresCatalogo();
			monedas.add(0, new ValorCatalogo(-1,"",""));
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
			sched.setObj(valor);

			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}
		return CustomListViewValuesArr;
	}

	private void CambiaFormaPago(String codNuevaFormaPago) {
        if (numero == null) return;
        boolean pagoEnEfectivo = codNuevaFormaPago.compareTo("EFEC") == 0;

		//ESTABLECIENDO VISIBILIDAD PARA LAS FILAS DEL LAYOUT
		tblRowNumero.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);
		tblRowFecha.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);
		tblRowBanco.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);
		tblRowTasa.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);

		numero.setEnabled( !pagoEnEfectivo );
		fecha.setEnabled( !pagoEnEfectivo );
		cmbBanco.setEnabled( !pagoEnEfectivo );  
    }
	
	private void CambioMoneda(String nuevoCodMoneda) {
        if (tasa == null) return;
        boolean pagoEnCordoba = nuevoCodMoneda.compareTo("COR") == 0;
        
        tasa.setEnabled( !pagoEnCordoba );
        tblRowTasa.setVisibility(pagoEnCordoba ? View.GONE : View.VISIBLE);
        
        ValorCatalogo vc = (ValorCatalogo)((SpinnerModel)formaPagoAdapter.getItem(cmbFormaPago.getSelectedItemPosition())).getObj();
        String codFormaPago = vc.getCodigo();
        
        if( pagoEnCordoba && ( codFormaPago.compareTo("EFEC") == 0 ) ) {
			tblRowNumeroSerie.setVisibility(View.GONE);
		}
                 
        numeroSerie.setEnabled((codFormaPago.compareTo("EFEC") == 0) && (nuevoCodMoneda.compareTo("COR") != 0));
       
        float montoPorPagar = StringUtil.round(_recibo.getTotalRecibo() - Cobro.getTotalPagoRecibo(_recibo), 2);
        float mtoNac = montoPorPagar;
        float mto = mtoNac;
        float tasa = 1;
        if (nuevoCodMoneda.compareTo("COR") != 0) {
            //Buscar tasa de cambio asociada a la moneda seleccionada para la fecha actual
            tasa = Cobro.getTasaCambioHoy(nuevoCodMoneda);
            if (tasa == 0) {
                mto = 0;
                Util.Message.buildToastMessage(this.getActivity(), "No hay tasa de cambio registrada.", TIME_TO_MESSAGE);               
            } else {
                pagoRecibo.setTasaCambio(tasa);                
                mto = StringUtil.round(montoPorPagar / tasa, 2);                
            }
        }
        pagoRecibo.setTasaCambio(tasa);      
        montoPago.setText(mto + "");
        this.tasa.setText(tasa + "");            
        montoNacional.setText(StringUtil.formatReal(mtoNac));
    }
}
