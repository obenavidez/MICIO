package com.panzyma.nm.viewdialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.auxiliar.ValorCatalogoUtil;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.auxiliar.AppDialog.OnButtonClickListener;
import com.panzyma.nm.custom.model.SpinnerModel;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.TasaCambio;
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.view.ViewDevolucionEdit;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nm.view.adapter.ExpandListChild;
import com.panzyma.nm.view.adapter.ExpandListGroup;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class EditDevolucionProducto extends DialogFragment {

	private static final int TIME_TO_MESSAGE = 3000;
	private static final String TAG = EditDevolucionProducto.class
			.getSimpleName();
	private View view = null;

	private EditText productName;
	private Spinner cmbLote;
	private EditText numeroLote;
	private Spinner cmbMesVencimiento;
	private EditText anioVencimiento;
	private EditText cantidad;
	private TableRow tblRowVencimiento;
	private TableRow tblRowNumeroLote;

	LayoutParams lyrow1;
	LayoutParams lyrow2;
	LayoutParams lyrow3;
	LayoutParams lyrow4;
	LayoutParams lyrow5;
	LayoutParams lyrow6;

	private List<TasaCambio> _tasaDeCambio = new ArrayList<TasaCambio>();
	private static Object lock = new Object();
	int iCurrentSelection = 0;
	float _montoPago = 0.00F;

	private ReciboColector _recibo = null;
	private ReciboDetFormaPago pagoRecibo = null;
	private boolean editFormaPago = false;
	private List<ValorCatalogo> monedas = null;
	private List<ValorCatalogo> bancos = null;
	private List<Lote> lotes = null;
	private CustomAdapter lotesAdapter = null;
	private CustomAdapter monedaAdapter = null;
	private CustomAdapter bancoAdapter = null;
	private float montoPorPagar = 0.00F;
	private Activity activityMain = null;
	private Dialog dialog = null;
	private NMApp nmApp;
	protected static Producto product_selected;
	protected DevolucionProductoLote devProLote;
	private static ViewDevolucionEdit me;
	private DevolucionProducto devolucionProducto;
	private ExpandListGroup groupselected;

	public static final String SELECTED_PRODUCT = "OBJ_PRODUCT";
	public static final String PRODUCT_LOTE = "OBJ_PRODUCT_LOTE";
	private Cliente cliente;
	private List<DevolucionProductoLote> lotesInDevolution = new ArrayList<DevolucionProductoLote>();;

	enum Type {
		ADD, EDIT
	}

	private Type tipo;

	public class LoadDataToUI extends AsyncTask<Void, Void, List<Lote>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected List<Lote> doInBackground(Void... params) {
			return ModelLogic.getLotesByProductoID(getNMApp().getBaseContext(),
					product_selected.getId());
		}

		@Override
		protected void onPostExecute(List<Lote> objectResult) {
			establecer(objectResult);
			super.onPostExecute(objectResult);
		}

	}

	public EditDevolucionProducto(ExpandListGroup groupselected,
			ViewDevolucionEdit vde) 
	{
		this.setDevolucionProducto((DevolucionProducto) groupselected.getObject());

		DevolucionProducto dp = this.getDevolucionProducto();

		if (dp.getProductoLotes() == null
				|| (dp.getProductoLotes() != null && dp.getProductoLotes().length == 0)) {
			lotesInDevolution = new ArrayList<DevolucionProductoLote>();
		} else {
			lotesInDevolution = new ArrayList<DevolucionProductoLote>(
					Arrays.asList(dp.getProductoLotes()));
		}

		this.groupselected = groupselected;
		cliente = vde.getCliente();
		try {
			product_selected = ModelProducto.getProductoByID(NMApp.getContext().getContentResolver(), devolucionProducto.getObjProductoID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tipo = Type.EDIT;
		me = vde;
		Bundle parametros = new Bundle();
		parametros.putParcelable(SELECTED_PRODUCT, product_selected);
		this.setArguments(parametros);
	}

	public EditDevolucionProducto(Producto ps, ViewDevolucionEdit vde) {
		product_selected = ps;
		me = vde;
		cliente = vde.getCliente();
		tipo = Type.ADD;
		this.setDevolucionProducto(null);
		Bundle parametros = new Bundle();
		parametros.putParcelable(SELECTED_PRODUCT, product_selected);
		this.setArguments(parametros);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.layout_devolver_producto, null);
		builder.setView(view);
		builder.setPositiveButton("AGREGAR",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.setNegativeButton("CANCELAR",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface d, int which) {
						Fragment prev = getFragmentManager().findFragmentByTag(
								"frgDialogFrmPago");
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
	public void onStart() {
		super.onStart();
		AlertDialog d = (AlertDialog) getDialog();
		if (d != null) {
			Button positiveButton = d
					.getButton(DialogInterface.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (validarDatos()) {
							accept();
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

		productName = (EditText) view.findViewById(R.id.txtProducto);
		cmbLote = (Spinner) view.findViewById(R.id.cmb_lote);
		numeroLote = (EditText) view.findViewById(R.id.txtNumeroLote);
		cmbMesVencimiento = (Spinner) view.findViewById(R.id.cmb_mes);
		anioVencimiento = (EditText) view.findViewById(R.id.txtAnio);
		cantidad = (EditText) view.findViewById(R.id.txtCantidad);

		lyrow1 = (LayoutParams) productName.getLayoutParams();
		lyrow2 = (LayoutParams) cmbLote.getLayoutParams();
		lyrow3 = (LayoutParams) numeroLote.getLayoutParams();
		lyrow4 = (LayoutParams) cmbMesVencimiento.getLayoutParams();
		lyrow5 = (LayoutParams) anioVencimiento.getLayoutParams();
		lyrow6 = (LayoutParams) cantidad.getLayoutParams();

		productName.setLayoutParams(new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

		cmbLote.setLayoutParams(new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

		cantidad.setLayoutParams(new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

		tblRowVencimiento = (TableRow) view
				.findViewById(R.id.tblRowVencimiento);
		tblRowNumeroLote = (TableRow) view.findViewById(R.id.tblRowNumero);

		nmApp = (NMApp) this.getActivity().getApplicationContext();

		LoadDataToUI loadData = new LoadDataToUI();
		loadData.execute();

	}

	private void setValuesToView() {
		// INICIANDO MIEMBROS INTERNOS
		if (getArguments() != null) 
		{
			devProLote = getArguments().getParcelable(PRODUCT_LOTE);
			product_selected = getArguments().getParcelable(SELECTED_PRODUCT);

		}
		tblRowNumeroLote.setVisibility(View.GONE);
		tblRowVencimiento.setVisibility(View.GONE);

		productName.setText(product_selected.getNombre());

		cmbLote.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (position == (lotes.size() - 1)) {
					Lote _formaPago = (Lote) lotesAdapter.getItem(position)
							.getObj();
					tblRowNumeroLote.setVisibility(View.VISIBLE);
					tblRowVencimiento.setVisibility(View.VISIBLE);

					productName.setLayoutParams(lyrow1);
					cmbLote.setLayoutParams(lyrow2);
					numeroLote.setLayoutParams(lyrow3);
					cmbMesVencimiento.setLayoutParams(lyrow4);
					anioVencimiento.setLayoutParams(lyrow5);
					cantidad.setLayoutParams(lyrow6);

				} else {
					productName.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT));
					cmbLote.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT));
					/*
					 * cantidad.setLayoutParams( new TableRow.LayoutParams(
					 * LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
					 */
					cantidad.setLayoutParams(lyrow6);
					tblRowNumeroLote.setVisibility(View.GONE);
					tblRowVencimiento.setVisibility(View.GONE);
				}

				// CambiaFormaPago(_formaPago.getCodigo());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
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

	private boolean hasTasaCambio(String codMoneda) {
		boolean encontrado = false;
		for (TasaCambio tasaCambio : _tasaDeCambio) {
			if (tasaCambio.getCodMoneda().equals(codMoneda)) {
				encontrado = true;
				break;
			}
		}
		return encontrado;
	}

	private float getTasaCambioByCodigoMoneda(String codMoneda) {
		float _tasaCambio = 0.00F;
		for (TasaCambio tasaCambio : _tasaDeCambio) {
			if (tasaCambio.getCodMoneda().equals(codMoneda)) {
				_tasaCambio = tasaCambio.getTasa();
				break;
			}
		}
		return _tasaCambio;
	}

	private boolean existInDevolucion(Lote lote) {
		boolean exist = false;
		for (DevolucionProductoLote l : lotesInDevolution) {
			if ( l.getNumeroLote().equals(lote.getNumeroLote()) ) {
				exist = true;
			}
		}
		return exist;
	}

	private List<Lote> lotesSinRepetir(List<Lote> lotes) {
		List<Lote> lts = new ArrayList();
		if ( this.devolucionProducto != null ) {
			if( lotesInDevolution.size() > 0 ) {
				for (Lote lote : lotes) {
					if (!existInDevolucion(lote)) {
						lts.add(lote);
					}
				}
			}			
		} else {
			lts = lotes;
		}
		return lts;
	}

	private void establecer(List<Lote> ltes) {
		lotes = lotesSinRepetir(ltes);
		// lotes = ltes;
		lotes.add(lotes.size(), new Lote(-1, "OTRO", 0));
		lotesAdapter = new CustomAdapter(getActivity(), R.layout.spinner_rows,
				setListData(lotes));
		cmbLote.setAdapter(lotesAdapter);
		setValuesToView();
	}

	private ArrayList<SpinnerModel> setListData(List<Lote> lotes) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
		// Now i have taken static values by loop.
		// For further inhancement we can take data by webservice / json / xml;

		for (Lote valor : lotes) {

			final SpinnerModel sched = new SpinnerModel();

			/******* Firstly take data in model object ******/
			sched.setId(valor.getId());
			sched.setCodigo(valor.getNumeroLote());
			sched.setDescripcion(String.valueOf(valor.getFechaVencimiento())
					.equals("0") ? "" : String.valueOf(valor
					.getFechaVencimiento()));
			sched.setObj(valor);

			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}
		return CustomListViewValuesArr;
	}

	public void accept() 
	{
		String dia = "";
		String mes = "";
		String anio = "";
		String fechaVencimiento = "";
		long fechaVencimientoL;
/**************************************************************************************************************************************************************/
		DevolucionProductoLote dvl = new DevolucionProductoLote();
		dvl.setCantidadDespachada(0);
		if ((lotes.size() - 1) > cmbLote.getSelectedItemPosition()) 
		{
			Lote lote = (Lote) lotesAdapter.getItem(cmbLote.getSelectedItemPosition()).getObj();
			numeroLote.setText(lote.getNumeroLote());
			dvl.setFechaVencimiento(lote.getFechaVencimiento());
			dvl.setObjLoteID(lote.getId());
		} else 
		{
			anio = anioVencimiento.getText().toString();
			mes = String.valueOf(cmbMesVencimiento.getSelectedItemPosition() + 1);
			if (mes.length() == 1)
				mes = "0" + mes;
			fechaVencimiento = anio + mes + "01";
			long date = DateUtil.getLastDayOfMonth(Long.valueOf(fechaVencimiento));
			dvl.setFechaVencimiento((int) date);
		}
		dvl.setCantidadDevuelta(Integer.valueOf(cantidad.getText().toString()));
		dvl.setNumeroLote(numeroLote.getText().toString());
/**************************************************************************************************************************************************************/
		
		if (me.getDev_prod() == null) 
		{
			me.setDev_prod(new ArrayList<DevolucionProducto>());
		}
		DevolucionProductoInfo obj=obtenerProductoDevolucion(); 
		
		DevolucionProducto dp =obj.getItem();
		dp.setCantidadDevolver(dp.getCantidadDevolver()+ dvl.getCantidadDevuelta());
		List<DevolucionProductoLote> l = null;
		if (dp.getProductoLotes() == null || (dp.getProductoLotes() != null && dp.getProductoLotes().length == 0)) 
		{
			l = new ArrayList<DevolucionProductoLote>();
		} else 
		{
			l = new ArrayList<DevolucionProductoLote>(Arrays.asList(dp.getProductoLotes()));
		}
		addLote(l, dvl);
		dp.setProductoLotes(new DevolucionProductoLote[l.size()]);
		l.toArray(dp.getProductoLotes());
		
		if(dp.getObjProductoID()==0)
		{
			dp.setObjProductoID(product_selected.getId());
			dp.setNombreProducto(product_selected.getNombre());
			dp.setObjProveedorID(product_selected.getObjProveedorID());
			me.getDev_prod().add(dp);		
		}else
		{
			me.getDev_prod().set(obj.getIndex(), dp);
		}
		
        me.initExpandableListView(true);
		
	}

	private List<DevolucionProductoLote> addLote(List<DevolucionProductoLote> lotes,DevolucionProductoLote _lote) 
	{   
		int index=0;
		for (DevolucionProductoLote lote : lotes) 
		{
			if (lote.getObjLoteID()!=_lote.getObjLoteID())  
				index++; 
		} 		
		if(index==lotes.size())
			lotes.add(_lote);
		else
			lotes.set(index,_lote);
		return lotes;
	}
	public DevolucionProductoInfo obtenerProductoDevolucion()
	{ 
		List<DevolucionProducto> _ldp=me.getDev_prod();
		DevolucionProducto _dp = null;
		int cont=0;
		for(DevolucionProducto item:_ldp)
		{			 
			if(item.getObjProductoID()==product_selected.getId())
			{
				_dp=item;
				break;
			}
			cont++;
		} 
		return new DevolucionProductoInfo(cont==_ldp.size()?null:cont, _dp==null?new DevolucionProducto():_dp);	 
	}
	
	private void removeFormaPagoFromRecibo(ReciboDetFormaPago fp) {
		int index = -1;
		for (int i = 0; i < _recibo.getFormasPagoRecibo().size(); i++) {
			if (fp.equals(_recibo.getFormasPagoRecibo().get(i))) {
				index = i;
			}
		}
		_recibo.getFormasPagoRecibo().remove(index);
	}

	private boolean validarDatos() throws InterruptedException {
		// SI ESTAMOS EN OTRO LOTE
		if ((lotes.size() - 1) == cmbLote.getSelectedItemPosition()) {
			// VERIFICAR SI SE DIGITO UN NUMERO DE LOTE Y FECHA DE VENCIMIENTO
			if (numeroLote.getText().toString().trim().length() == 0) {
				numeroLote.setError("Debe indicar el número de lote");
				return false;
			}
			if (anioVencimiento.getText().toString().trim().length() == 0) {
				anioVencimiento.setError("Debe indicar el año de vencimiento");
				return false;
			}
		}
		if (cantidad.getText().toString().trim().length() == 0) {
			cantidad.setError("Debe indicar la cantidad a devolver");
			return false;
		}
		return true;
	}

	public int getFecha() {
		return pagoRecibo.getFecha();
	}

	public String getCodigoMoneda() {
		return pagoRecibo.getCodMoneda();
	}

	public void showStatusOnUI(Object msg) throws InterruptedException {

		final String titulo = "" + ((ErrorMessage) msg).getTittle();
		final String mensaje = "" + ((ErrorMessage) msg).getMessage();

		NMApp.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {

				try {

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AppDialog.showMessage(getActivity(), titulo,
									mensaje,
									AppDialog.DialogType.DIALOGO_CONFIRMACION,
									new AppDialog.OnButtonClickListener() {
										@Override
										public void onButtonClick(
												AlertDialog _dialog,
												int actionId) {
											synchronized (lock) {
												lock.notify();
											}
										}
									});
						}
					});

					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public Activity getContext() {
		return activityMain;
	}

	public NMApp getNMApp() {
		return nmApp;
	}

	public void SetMontoPagado() {
		EditDialogListener activity = (EditDialogListener) getActivity();
		float MontoFormasPago = 0;
		ArrayList<ReciboDetFormaPago> MONTOS = _recibo.getFormasPagoRecibo();
		for (ReciboDetFormaPago reciboDetFormaPago : MONTOS) {
			MontoFormasPago += reciboDetFormaPago.getMonto();
		}
		activity.updateResult(StringUtil.formatReal(MontoFormasPago));
	}

	public interface EditDialogListener {
		void updateResult(String inputText);
	}

	public DevolucionProducto getDevolucionProducto() 
	{
		return (devolucionProducto == null ? new DevolucionProducto()
				: devolucionProducto);
	}

	public void setDevolucionProducto(DevolucionProducto devolucionProducto) {
		this.devolucionProducto = devolucionProducto;
	}
	
	class DevolucionProductoInfo{
		/**
		 * @param index
		 * @param item
		 */
		public DevolucionProductoInfo(Integer index, DevolucionProducto item) {
			super();
			this.index = index;
			this.item = item;
		}
		Integer index;
		DevolucionProducto item;
		/**
		 * @return the index
		 */
		public Integer getIndex() {
			return index;
		}
		/**
		 * @param index the index to set
		 */
		public void setIndex(Integer index) {
			this.index = index;
		}
		/**
		 * @return the item
		 */
		public DevolucionProducto getItem() {
			return item;
		}
		/**
		 * @param item the item to set
		 */
		public void setItem(DevolucionProducto item) {
			this.item = item;
		}
		
	}
}
