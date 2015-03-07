package com.panzyma.nm.viewdialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.auxiliar.ValorCatalogoUtil;
import com.panzyma.nm.auxiliar.AppDialog.OnButtonClickListener;
import com.panzyma.nm.custom.model.SpinnerModel;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.TasaCambio;
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;

public class EditFormaPago extends DialogFragment {
	
	private static final int TIME_TO_MESSAGE = 3000;
	private static final String TAG = EditFormaPago.class.getSimpleName();
	private View view = null;
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
	private List<TasaCambio> _tasaDeCambio = new ArrayList<TasaCambio>();
	private static Object lock = new Object();
	int iCurrentSelection = 0;
	float _montoPago = 0.00F;

	private ReciboColector _recibo = null;
	private ReciboDetFormaPago pagoRecibo = null;
	private boolean editFormaPago = false;
	private List<ValorCatalogo> monedas = null;
	private List<ValorCatalogo> bancos = null;
	private List<ValorCatalogo> formasPago = null;	
	private CustomAdapter formaPagoAdapter = null;
	private CustomAdapter monedaAdapter = null;
	private CustomAdapter bancoAdapter = null;	
	private float montoPorPagar = 0.00F;
	private Activity activityMain = null; 
	private Dialog dialog = null;		
	private NMApp nmApp;	

	
	
	public class LoadDataToUI extends AsyncTask<Void, Void, Map<String,List<Object>> > {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Map<String,List<Object>> doInBackground(Void... params) {			
			return ModelLogic.getDataFormaPago(getNMApp().getBaseContext(),
					DateUtil.getToday(),
					new String[]{"FormaPago","Moneda","EntidadBancaria"});
		}

		@Override
		protected void onPostExecute(Map<String,List<Object>> objectResult) {
			Map<String,List<Object>> result = objectResult;
			int cnt = result.get("basic").toArray().length;
			Catalogo [] catalogos = new Catalogo[cnt];
			result.get("basic").toArray(catalogos);
			for(Catalogo catalogo : catalogos){
				establecer(catalogo);
			}
			cnt = result.get("tasaCambio").toArray().length;
			TasaCambio [] tasasCambio = new TasaCambio [cnt];
			result.get("tasaCambio").toArray(tasasCambio);			
			estableceTasaCambio(Arrays.asList(tasasCambio));	
			setValuesToView();
			super.onPostExecute(result);
		}
		
	} 
	
	public static EditFormaPago newInstance(ReciboColector recibo, ReciboDetFormaPago reciboDetFormaPago, boolean edit) {
		EditFormaPago editFormaPago = new EditFormaPago();
		Bundle parametros = new Bundle();
		parametros.putBoolean(ViewReciboEdit.FORMA_PAGO_IN_EDITION, edit);
		parametros.putParcelable(ViewReciboEdit.OBJECT_RECIBO, recibo);
		parametros.putParcelable(ViewReciboEdit.OBJECT_TO_EDIT, reciboDetFormaPago);
		editFormaPago.setArguments(parametros);
		return editFormaPago;
	}

	@Override 
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();								 
		view = inflater.inflate(R.layout.layout_seleccion_forma_pago, null);	
		builder.setView(view);
		builder.setPositiveButton("AGREGAR", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
                  
            }
		});
		builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
			 @Override
             public void onClick(DialogInterface d, int which) {
				Fragment prev = getFragmentManager().findFragmentByTag("frgDialogFrmPago");
			    if (prev != null) {
			        DialogFragment df = (DialogFragment) prev;
			        df.dismiss();
			    }	
				
             }
		});	
		initComponents();		
		return (dialog = builder.create());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}	

	@Override
	public void onStart()
	{
	    super.onStart();    
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d != null)
	    {
	        Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
	        positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {					
					try {
						if( validarDatos() ){
							accept();
							SetMontoPagado();// Establecer la cantidad de Monto Pagado.
							dismiss();
						}						
					} catch (InterruptedException e) {						
						try {
							showStatusOnUI(new ErrorMessage(
									"Error de validación", e.getMessage(), ""));
						} catch (InterruptedException e1) {							
							e1.printStackTrace();
						}
					}					
                }
            });    
	        
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
		tblRowNumeroSerie = (TableRow) view.findViewById(R.id.tblRowNumeroSerie);
		
		nmApp =(NMApp) this.getActivity().getApplicationContext();		
		
		LoadDataToUI loadData = new LoadDataToUI();
		loadData.execute();	
				
	}

	private void setValuesToView() {		
		// INICIANDO MIEMBROS INTERNOS
		if (getArguments() != null ) {
			
			if( getArguments() != null) {
				editFormaPago = getArguments().getBoolean(ViewReciboEdit.FORMA_PAGO_IN_EDITION);
				_recibo = getArguments().getParcelable(ViewReciboEdit.OBJECT_RECIBO);
				
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

					montoPorPagar = StringUtil.round(_recibo.getTotalRecibo()
							- Cobro.getTotalPagoRecibo(_recibo), 2);
					pagoRecibo.setMonto(montoPorPagar);
					pagoRecibo.setMontoNacional(montoPorPagar);
					pagoRecibo.setNumero(""); // No es CHK
					pagoRecibo.setObjEntidadID(0);
					pagoRecibo.setObjFormaPagoID(ValorCatalogoUtil
							.getValorCatalogoID(formasPago, "EFEC"));
					pagoRecibo.setObjMonedaID(ValorCatalogoUtil.getValorCatalogoID(
							monedas, "COR"));
					pagoRecibo.setSerieBilletes("");
					pagoRecibo.setTasaCambio(1.0F);
				} else {
					// EDITANDO UNA FORMA DE PAGO
					pagoRecibo = getArguments().getParcelable(ViewReciboEdit.OBJECT_TO_EDIT);
				}
			} 
			
			boolean pagoEnEfectivo = pagoRecibo.getCodFormaPago().compareTo("EFEC") == 0;
			boolean pagoEnCordoba = pagoRecibo.getCodMoneda().compareTo("COR") == 0;

			// ESTABLECIENDO VISIBILIDAD PARA LAS FILAS DEL LAYOUT
			if (pagoEnEfectivo) {
				tblRowNumero.setVisibility(View.GONE);
				tblRowFecha.setVisibility(View.GONE);
				tblRowBanco.setVisibility(View.GONE);
				tblRowTasa.setVisibility(View.GONE);
			}
			if (pagoEnCordoba) {
				tblRowNumeroSerie.setVisibility(View.GONE);
			}

			numero.setText(pagoRecibo.getNumero().toString());
			tasa.setText(StringUtil.formatReal(pagoRecibo.getTasaCambio()));
			montoPago.setText(StringUtil.formatReal(pagoRecibo.getMonto()));
			_montoPago = pagoRecibo.getMonto();
			montoNacional.setText(StringUtil.formatReal(pagoRecibo.getMontoNacional()));
			numeroSerie.setText(pagoRecibo.getSerieBilletes());

			numero.setEnabled(!pagoEnEfectivo);
			fecha.setEnabled(!pagoEnEfectivo);
			cmbBanco.setEnabled(!pagoEnEfectivo);
			numeroSerie.setEnabled(!pagoEnCordoba);

			if (pagoRecibo.getFecha() == 0) {
				fecha.setText(DateUtil.idateToStr(DateUtil.getNow()).toString());
			} else {
				fecha.setText(DateUtil.idateToStr(pagoRecibo.getFecha())
						.toString());
			}

			for (int i = 0; (formasPago != null && i < formasPago.size()); i++) {
				if (formasPago.get(i).getCodigo()
						.compareTo(pagoRecibo.getCodFormaPago()) == 0) {
					cmbFormaPago.setSelection(i);
					break;
				}
			}

			for (int i = 0; (bancos != null && i < bancos.size()); i++) {
				if (bancos.get(i).getCodigo()
						.compareTo(pagoRecibo.getCodEntidad()) == 0) {
					cmbBanco.setSelection(i);
					break;
				}
			}

			for (int i = 0; (monedas != null && i < monedas.size()); i++) {
				if (monedas.get(i).getCodigo()
						.compareTo(pagoRecibo.getCodMoneda()) == 0) {
					cmbMoneda.setSelection(i);
					break;
				}
			}

			cmbFormaPago.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parentView,
						View selectedItemView, int position, long id) {
					if (position == 0)
						return;
					ValorCatalogo _formaPago = (ValorCatalogo) formaPagoAdapter
							.getItem(position).getObj();
					CambiaFormaPago(_formaPago.getCodigo());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parentView) {
					// your code here
				}

			});
			
			montoPago.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					RecalcularMontoNacional();
				}
			});			

			cmbMoneda.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					if (position == 0)	return;
					ValorCatalogo _moneda = (ValorCatalogo) monedaAdapter.getItem(position).getObj();
					CambioMoneda(_moneda.getCodigo());
					iCurrentSelection = position;
				}

				@Override
				public void onNothingSelected(AdapterView<?> parentView) {
					// your code here
				}

			});

		}		
		
	}

	public ReciboDetFormaPago getPagoRecibo() {
		return pagoRecibo;
	}

	public void setPagoRecibo(ReciboDetFormaPago _pagoRecibo) {
		this.pagoRecibo = _pagoRecibo;
	}	

	private void estableceTasaCambio(List<TasaCambio> obj) {		
		this._tasaDeCambio.clear();
		this._tasaDeCambio = obj;
	}

	private boolean hasTasaCambio(String codMoneda){
		boolean encontrado = false;
		for(TasaCambio tasaCambio : _tasaDeCambio) {
			if( tasaCambio.getCodMoneda().equals(codMoneda) )
			{
				encontrado = true;
				break;
			}
		}
		return encontrado;
	}
	
	private float getTasaCambioByCodigoMoneda(String codMoneda){
		float _tasaCambio = 0.00F;
		for(TasaCambio tasaCambio : _tasaDeCambio) {
			if( tasaCambio.getCodMoneda().equals(codMoneda) )
			{
				_tasaCambio = tasaCambio.getTasa();
				break;
			}
		}
		return _tasaCambio;
	}
	
	private void establecer(Catalogo obj) {
		if ("FormaPago".equals(obj.getNombreCatalogo().trim())) {
			formasPago = obj.getValoresCatalogo();
			formasPago.add(0, new ValorCatalogo(-1, "", ""));
			formaPagoAdapter = new CustomAdapter(getActivity(),
					R.layout.spinner_rows, setListData(formasPago));
			cmbFormaPago.setAdapter(formaPagoAdapter);
			//cargarCatalogoMonedas();
		} else if ("Moneda".equals(obj.getNombreCatalogo().trim())) {
			monedas = obj.getValoresCatalogo();
			monedas.add(0, new ValorCatalogo(-1, "", ""));
			monedaAdapter = new CustomAdapter(getActivity(),
					R.layout.spinner_rows, setListData(monedas));
			cmbMoneda.setAdapter(monedaAdapter);
			//cargarCatalogoBancos();			
		} else if ("EntidadBancaria".equals(obj.getNombreCatalogo().trim())) {
			bancos = obj.getValoresCatalogo();
			bancos.add(0, new ValorCatalogo(-1, "", ""));
			bancoAdapter = new CustomAdapter(getActivity(),
					R.layout.spinner_rows, setListData(bancos));
			cmbBanco.setAdapter(bancoAdapter);
			//cargarTasaCambio();
		}
	}

	private ArrayList<SpinnerModel> setListData(List<ValorCatalogo> valores) {
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

	public void accept() {
        //Salvando monto que se va a pagar
        ValorCatalogo vcFP = (ValorCatalogo)formaPagoAdapter.getItem(cmbFormaPago.getSelectedItemPosition()).getObj();
        pagoRecibo.setObjFormaPagoID(vcFP.getId());
        pagoRecibo.setCodFormaPago(vcFP.getCodigo());
        pagoRecibo.setDescFormaPago(vcFP.getDescripcion());
        
        pagoRecibo.setNumero("");
        pagoRecibo.setFecha(0);
        pagoRecibo.setObjEntidadID(0);
        pagoRecibo.setCodEntidad("");
        pagoRecibo.setDescEntidad("");
        if (vcFP.getCodigo().compareTo("EFEC") != 0) {
            pagoRecibo.setNumero(numero.getText().toString().trim());            
            pagoRecibo.setFecha(DateUtil.strToIntTime(fecha.getText().toString()));            
            ValorCatalogo vcBco = (ValorCatalogo)bancoAdapter.getItem(cmbBanco.getSelectedItemPosition()).getObj();
            pagoRecibo.setObjEntidadID(vcBco.getId());
            pagoRecibo.setCodEntidad(vcBco.getCodigo());
            pagoRecibo.setDescEntidad(vcBco.getDescripcion());
        }
        
        ValorCatalogo vcM = (ValorCatalogo)monedaAdapter.getItem(cmbMoneda.getSelectedItemPosition()).getObj();
        pagoRecibo.setObjMonedaID(vcM.getId());
        pagoRecibo.setCodMoneda(vcM.getCodigo());
        pagoRecibo.setDescMoneda(vcM.getDescripcion());
        
        if (vcM.getCodigo().compareTo("COR") == 0)
            pagoRecibo.setTasaCambio(1.0F);
        else
            pagoRecibo.setTasaCambio(Float.parseFloat(tasa.getText().toString()));
        
        pagoRecibo.setMonto(Float.parseFloat(montoPago.getText().toString().trim()));
        pagoRecibo.setMontoNacional(pagoRecibo.getMonto() * pagoRecibo.getTasaCambio());
        
        pagoRecibo.setSerieBilletes("");
        if ((vcFP.getCodigo().compareTo("EFEC") == 0) && (vcM.getCodigo().compareTo("COR") != 0)) 
            pagoRecibo.setSerieBilletes(numeroSerie.getText().toString().trim());
        
        if(editFormaPago) {
        	removeFormaPagoFromRecibo(pagoRecibo);
        	_recibo.getFormasPagoRecibo().add(pagoRecibo);
        } else {
        	_recibo.getFormasPagoRecibo().add(pagoRecibo);
        }            
    }
	
	private void removeFormaPagoFromRecibo(ReciboDetFormaPago fp) {
		int index = -1;
		for(int i = 0; i < _recibo.getFormasPagoRecibo().size() ; i++) {
			if( fp.equals(_recibo.getFormasPagoRecibo().get(i)) ) {
				index = i;
			}
		}
		_recibo.getFormasPagoRecibo().remove(index);
	}
	
	private void CambiaFormaPago(String codNuevaFormaPago) {
		if (numero == null)
			return;
		boolean pagoEnEfectivo = codNuevaFormaPago.compareTo("EFEC") == 0;

		// ESTABLECIENDO VISIBILIDAD PARA LAS FILAS DEL LAYOUT
		tblRowNumero.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);
		tblRowFecha.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);
		tblRowBanco.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);
		tblRowTasa.setVisibility(pagoEnEfectivo ? View.GONE : View.VISIBLE);

		numero.setEnabled(!pagoEnEfectivo);
		fecha.setEnabled(!pagoEnEfectivo);
		cmbBanco.setEnabled(!pagoEnEfectivo);
	}

	private void CambioMoneda(String nuevoCodMoneda) {
		if (tasa == null) return;
		boolean pagoEnCordoba = nuevoCodMoneda.compareTo("COR") == 0;
		
		tasa.setEnabled(!pagoEnCordoba);
		tblRowTasa.setVisibility(pagoEnCordoba ? View.GONE : View.VISIBLE);

		ValorCatalogo vc = (ValorCatalogo) formaPagoAdapter
				.getItem(cmbFormaPago.getSelectedItemPosition()).getObj();
		String codFormaPago = vc.getCodigo();

		if (pagoEnCordoba && (codFormaPago.compareTo("EFEC") == 0)) {
			tblRowNumeroSerie.setVisibility(View.GONE);
		} else {
			tblRowNumeroSerie.setVisibility(View.VISIBLE);
		}

		numeroSerie.setEnabled((codFormaPago.compareTo("EFEC") == 0)
				&& (nuevoCodMoneda.compareTo("COR") != 0));

		
		float montoPorPagar = 0.00F;
		if(!getArguments().getBoolean(ViewReciboEdit.FORMA_PAGO_IN_EDITION))
			montoPorPagar = StringUtil.round(_recibo.getTotalRecibo() - Cobro.getTotalPagoRecibo(_recibo), 2);
		else 
			montoPorPagar = Float.parseFloat(montoNacional.getText().toString().trim().replace(",", ""));
		float mtoNac = montoPorPagar;
		float mto = mtoNac;
		float tasa = 1;
		if (nuevoCodMoneda.compareTo("COR") != 0) {
			// Buscar tasa de cambio asociada a la moneda seleccionada para la
			// fecha actual
			if( !hasTasaCambio(nuevoCodMoneda) )
			{
				try {					
					showStatusOnUI(new ErrorMessage("Error en Tasa Cambio", 
							"No hay tasa de cambio registrada para el tipo de moneda " , ""));
					cmbMoneda.setSelection(iCurrentSelection);					
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}			
				
				return;
			}
			tasa = getTasaCambioByCodigoMoneda(nuevoCodMoneda);			
			if (tasa == 0) {
				mto = 0;
				Util.Message.buildToastMessage(this.getActivity(),
						"No hay tasa de cambio registrada.", TIME_TO_MESSAGE);
			} else {
				pagoRecibo.setTasaCambio(tasa);
				_montoPago = montoPorPagar / tasa;
				mto = StringUtil.round(_montoPago, 2);
			}
		}
		pagoRecibo.setTasaCambio(tasa);
		montoPago.setText(mto + "");
		this.tasa.setText(tasa + "");
		montoNacional.setText(StringUtil.formatReal(mtoNac));
	}

	private boolean validarDatos() throws InterruptedException { 
		
		// validar que se haya ingresado monto a pagar
		if (montoPago.getText().toString().trim() == "") {
			showStatusOnUI(new ErrorMessage("Error de validación",
					"Ingrese el monto a pagar.", ""));
			return false;
		}

		if (Float.parseFloat(montoPago.getText().toString().trim()) == 0) {
			showStatusOnUI(new ErrorMessage("Error de validación",
					"El monto a pagar debe ser mayor que cero.", ""));			
			return false;
		}

		ValorCatalogo vcM = (ValorCatalogo) monedaAdapter
				.getItem(cmbMoneda.getSelectedItemPosition()).getObj();
		
		if (!editFormaPago) {
			if (vcM.getCodigo().compareTo("COR") == 0)
				tasa.setText("1");

			// Validar que no sea mayor que el saldo total de la factura
			float mtoNac = StringUtil.round(
					Float.parseFloat(tasa.getText().toString())
							* _montoPago, 2);
			if (mtoNac > montoPorPagar) {
				showStatusOnUI(new ErrorMessage("Error de validación",
						"El monto a pagar no debe ser mayor al faltante de pago del recibo ("
								+ StringUtil.formatReal(montoPorPagar) + ").", ""));				
				return false;
			}
		}

		// Si el pago no es en efectivo validar que se haya ingresado el número
		// del documento
		// y que la fecha del cheque si es el caso sea válida
		ValorCatalogo vcFP = (ValorCatalogo) formaPagoAdapter
				.getItem(cmbFormaPago.getSelectedItemPosition()).getObj();
		;

		if (vcFP.getCodigo().compareTo("EFEC") != 0) {
			if (numero.getText().toString().trim().compareTo("") == 0) {
				showStatusOnUI(new ErrorMessage("Error de validación",
						"Ingrese el número de " + vcFP.getDescripcion() + ".", ""));				
				cmbFormaPago.setFocusable(true);
				return false;
			}

			// Validar las fechas si es cheque
			if (vcFP.getCodigo().compareTo("CK") == 0) {
				int minDiasFechaCheque = Integer.parseInt(Cobro.getParametro(
						this.getActivity(), "MinDiasFechaCheque").toString());
				int maxDiasFechaCheque = Integer.parseInt(Cobro.getParametro(
						this.getActivity(), "MaxDiasFechaCheque").toString());

				long hoy = DateUtil.getTime(DateUtil.getToday());
				/*int minFechaCheque = DateUtil.time2int(DateUtil.addDay(hoy,
						-minDiasFechaCheque));
				int maxFechaCheque = DateUtil.time2int(DateUtil.addDay(hoy,
						maxDiasFechaCheque));*/
				long minFechaCheque = DateUtil.addDay(DateUtil.getToday(), -minDiasFechaCheque );
				long maxFechaCheque = DateUtil.addDay(DateUtil.getToday(), maxDiasFechaCheque );
				
				long fechaCK = DateUtil.strDateToLong(fecha.getText()
						.toString());
				if (fechaCK < minFechaCheque) {
					Date d = new Date(minFechaCheque);
					showStatusOnUI(new ErrorMessage(
							"Error de validación",
							"La fecha del cheque no debe ser menor que "
									+ DateUtil.idateToStr(minFechaCheque) + ".",
							""));					
					return false;
				}

				if (fechaCK > maxFechaCheque) {
					Date d = new Date(maxFechaCheque);
					showStatusOnUI(new ErrorMessage(
							"Error de validación",
							"La fecha del cheque no debe ser mayor que "
									+ DateUtil.idateToStr(maxFechaCheque) + ".",
							""));					
					return false;
				}
			} else {
				// Si es otro tipo de documento validar que la fecha no sea
				// mayor que la actual
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date strDate = new Date();
				try {
					strDate = sdf.parse(fecha.getText().toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (strDate.after(new Date())) {
					showStatusOnUI(new ErrorMessage(
							"Error de validación",
							"La fecha del documento no debe ser mayor a la fecha actual.",
							""));					
					return false;
				}
//				long hoy = DateUtil.getTime(DateUtil.getToday());
//				long fechaDoc = DateUtil.strDateToLong(fecha.getText().toString());
//				
//				if (fechaDoc > hoy) {
//					showStatusOnUI(new ErrorMessage(
//							"Error de validación",
//							"La fecha del documento no debe ser mayor a la fecha actual.",
//							""));					
//					return false;
//				}
			}
		}

		// Si es cobro en efectivo y la moneda es diferente a la moneda
		// nacional, pedir números de serie
		if ((vcFP.getCodigo().compareTo("EFEC") == 0)
				&& (vcM.getCodigo().compareTo("COR") != 0)) {
			if (numeroSerie.getText().toString().trim().compareTo("") == 0) {
				showStatusOnUI(new ErrorMessage(
						"Error de validación",
						"Ingrese los números de serie de los billetes.",
						""));				
				numeroSerie.setFocusable(true);
				return false;
			}
		}

		// Si pago es en moneda extranjera, validar que tasa sea mayor que cero
		if (vcM.getCodigo().compareTo("COR") != 0) {
			if (tasa.getText().toString().trim().compareTo("") == 0) {
				showStatusOnUI(new ErrorMessage(
						"Error de validación",
						"La tasa de cambio no puede quedar vacía.",
						""));					
				tasa.setFocusable(true);
				return false;
			}

			if (Float.parseFloat(tasa.getText().toString().trim()) == 0) {
				showStatusOnUI(new ErrorMessage(
						"Error de validación",
						"La tasa de cambio no puede ser cero.",
						""));				
				tasa.setFocusable(true);
				return false;
			}
		} else {
			tasa.setText("1.0");
		}
		
		return true;
	}

	private void RecalcularMontoNacional() {
		float mto = 0, tasa = 0;
		if (this.tasa.getText().toString().trim().compareTo("") != 0)
			tasa = Float.parseFloat(this.tasa.getText().toString().trim());

		if (montoPago.getText().toString().trim().compareTo("") != 0) {
			mto = _montoPago;
		}
			//mto = Float.parseFloat(montoPago.getText().toString().trim());

		float montoNac = StringUtil.round(tasa * mto, 2);
		montoNacional.setText(StringUtil.formatReal(montoNac));
	}
	
	public int getFecha() {
		return pagoRecibo.getFecha();
	}
	
	public String getCodigoMoneda() {
		return pagoRecibo.getCodMoneda();
	}
	
	public void showStatusOnUI(Object msg) throws InterruptedException{
		
		final String titulo=""+((ErrorMessage)msg).getTittle();
		final String mensaje=""+((ErrorMessage)msg).getMessage();
		
		
		NMApp.getThreadPool().execute(new Runnable()
		{ 
			@Override
			public void run()
		    {
				 
				try 
				{
					
					getActivity().runOnUiThread(new Runnable() 
			        {
						@Override
						public void run() 
						{ 
							 AppDialog.showMessage(getActivity(),titulo,mensaje,AppDialog.DialogType.DIALOGO_CONFIRMACION,new AppDialog.OnButtonClickListener() 
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
					e.printStackTrace();
				}
		    }
		}); 
		
	}
	
	public Activity getContext(){
		return activityMain;
	}

	public NMApp getNMApp() {
		return nmApp;
	}
	public void SetMontoPagado (){
		EditDialogListener activity = (EditDialogListener) getActivity();
		float MontoFormasPago = 0;
		ArrayList<ReciboDetFormaPago> MONTOS = _recibo.getFormasPagoRecibo();
		for (ReciboDetFormaPago reciboDetFormaPago : MONTOS) {
			MontoFormasPago+= reciboDetFormaPago.getMonto();
		}
		activity.updateResult(StringUtil.formatReal(MontoFormasPago));
	}
	public interface EditDialogListener {
        void updateResult(String inputText);
    }
}
