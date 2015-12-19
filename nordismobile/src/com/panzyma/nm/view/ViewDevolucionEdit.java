package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BDevolucionM;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.custom.model.SpinnerModel;
import com.panzyma.nm.interfaces.Editable;
import com.panzyma.nm.interfaces.IFilterabble;
import com.panzyma.nm.interfaces.Predicate;
import com.panzyma.nm.logic.DevolucionBL;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PedidoPromocion;
import com.panzyma.nm.serviceproxy.PedidoPromocionDetalle;
import com.panzyma.nm.serviceproxy.PrecioProducto;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nm.view.adapter.ExpandListAdapter;
import com.panzyma.nm.view.adapter.ExpandListChild;
import com.panzyma.nm.view.adapter.ExpandListGroup;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.adapter.SetViewHolderWLayout;
import com.panzyma.nm.view.viewholder.ProductoLoteDetalleViewHolder;
import com.panzyma.nm.view.viewholder.ProductoLoteViewHolder;
import com.panzyma.nm.viewdialog.DevolucionProductoBonificacion;
import com.panzyma.nm.viewdialog.DevolucionProductoBonificacion.escucharModificacionProducto;
import com.panzyma.nm.viewdialog.DevolucionProductoCantidad;
import com.panzyma.nm.viewdialog.DevolverDocumento;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogCosteoDevolucion;
import com.panzyma.nm.viewdialog.DialogNotaDevolucion;
import com.panzyma.nm.viewdialog.DialogNotaDevolucion.RespuestaNotaDevolucion;
import com.panzyma.nm.viewdialog.DialogNotaRecibo;
import com.panzyma.nm.viewdialog.DialogProducto;
import com.panzyma.nm.viewdialog.EditDevolucionProducto;
import com.panzyma.nm.viewdialog.ProductoDevolucion;
import com.panzyma.nm.viewdialog.DevolucionProductoCantidad.escucharModificacionProductoLote;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewdialog.DialogNotaRecibo.RespuestaNotaRecibo;
import com.panzyma.nordismobile.R;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation", "unused" })
@InvokeBridge(bridgeName = "BDevolucionM")
public class ViewDevolucionEdit extends ActionBarActivity implements
		Handler.Callback, Editable {
	private static final int ID_SELECCIONAR_CLIENTE = 0;
	private static final int ID_DEVOLVER_PEDIDO = 1;
	private static final int ID_AGREGAR_PRODUCTO = 2;
	private static final int ID_AGREGAR_LOTE = 3;
	private static final int ID_EDITAR_NOTA = 4;
	private static final int ID_VER_COSTEO = 5;
	private static final int ID_GUARDAR = 6;
	private static final int ID_ENVIAR = 7;
	private static final int ID_CERRAR = 8;

	private static BigDecimal costeoMontoSubTotal = BigDecimal.ZERO;
	private static BigDecimal costeoMontoBonificacion = BigDecimal.ZERO;
	private static BigDecimal costeoMontoImpuesto = BigDecimal.ZERO;
	private static BigDecimal costeoMontoPromocion = BigDecimal.ZERO;
	private static BigDecimal costeoMontoCargoAdministrativo = BigDecimal.ZERO;
	private static BigDecimal costeoMontoTotal = BigDecimal.ZERO;

	private static BigDecimal costeoMontoSubTotalVen = BigDecimal.ZERO;
	private static BigDecimal costeoMontoBonificacionVen = BigDecimal.ZERO;
	private static BigDecimal costeoMontoImpuestoVen = BigDecimal.ZERO;
	private static BigDecimal costeoMontoPromocionVen = BigDecimal.ZERO;
	private static BigDecimal costeoMontoCargoAdministrativoVen = BigDecimal.ZERO;
	private static BigDecimal costeoMontoCargoVen = BigDecimal.ZERO;
	private static BigDecimal costeoMontoVinieta = BigDecimal.ZERO;
	private static BigDecimal costeoMontoTotalVen = BigDecimal.ZERO;

	// TIPO TRAMITE
	private static final int REPOSICION = 1;
	private static final int NOTADECREDITO = 2;
	// TIPO DEVOLUCIÓN
	private static final int TOTAL = 1;
	private static final int PARCIAL = 2;

	private TextView tbxNombreDelCliente;
	private CheckBox ckboxvencidodev;
	private Spinner cboxmotivodev;
	private Spinner cboxtramitedev;
	private Spinner cboxtipodev;
	private CheckBox ckboxncinmeditata;
	private EditText tbxFecha;
	private EditText tbxtotaldev;
	private EditText tbxNota;

	CustomAdapter adapter_motdev;
	CustomAdapter adapter_tramite;
	CustomAdapter adapter_tipodev;
	ArrayList<Catalogo> catalogos;

	private CustomDialog dlg;
	public static ProductoDevolucion pd;
	List<DevolucionProducto> dev_prod;
	public static final String CLIENTE = "cliente";

	public List<DevolucionProducto> getDev_prod() {
		return dev_prod;
	}

	private ExpandableListView lvdevproducto;
	private ExpandListAdapter adapter;
	protected int[] positioncache = new int[2];
	protected ExpandListGroup dvselected;
	private ExpandListChild childselected;

	private ViewDevolucionEdit me;
	ArrayList<Producto> aprodselected;

	private ExpandListGroup groupselected;

	DrawerLayout drawerLayout;
	ListView drawerList;
	ActionBarDrawerToggle drawerToggle;
	String[] opcionesMenu;
	CharSequence tituloSeccion;
	CharSequence tituloApp;
	View _view;

	private static final Map<String, String> hmmotivodev = null;
	private static final Map<String, String> hmtramite;
	private static final Map<String, String> hmtipodev;
	private static final String REGISTRADA = "REGISTRADA";
	private static final String RECEPCIONADA = "RECEPCIONADA";

	Cliente cliente;

	public Cliente getCliente() {
		return cliente;
	}

	Pedido pedido;
	Factura factura;
	Lote lote;
	Devolucion devolucion;

	Context context;
	private ProgressDialog pdialog;

	public static final Display display;

	List<ExpandListGroup> lgroups = new LinkedList<ExpandListGroup>();

	static {
		int contador = 0;
		Map<String, String> aMap = null;

		aMap = new LinkedHashMap<String, String>();
		aMap.put("-1", "");
		aMap.put("RE", "Reposición");
		aMap.put("NC", "Nota de Crédito");
		hmtramite = Collections.unmodifiableMap(aMap);

		aMap = new LinkedHashMap<String, String>();
		aMap.put("-1", "");
		aMap.put("TT", "Total");
		aMap.put("PC", "Parcial");
		hmtipodev = Collections.unmodifiableMap(aMap);

		WindowManager wm = (WindowManager) NMApp.getContext()
				.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		me = this;
		setContentView(R.layout.devolucion_edit);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		com.panzyma.nm.NMApp.getController().setView(this);
		Message m = new Message();
		aprodselected = new ArrayList<Producto>();
		m.what = ControllerProtocol.OBTENERVALORCATALOGO;
		m.obj = new String[] { "MotivoDevolucionNoVencidos" };
		NMApp.getController().getInboxHandler().sendMessage(m);
		initComponent();
		if (getIntent().hasExtra("cliente")) {
			long IdCliente = getIntent().getLongExtra("cliente", 0);
			try {
				cliente = Ventas.getClienteBySucursalID(IdCliente,
						me.getContentResolver());
				setInformacionCliente();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void initComponent() {
		ckboxvencidodev = (CheckBox) findViewById(R.id.devchk_typodevolucion);
		ckboxncinmeditata = (CheckBox) findViewById(R.id.devchk_ncinmediata);
		cboxmotivodev = (Spinner) findViewById(R.id.devcombox_motivo);
		cboxtramitedev = (Spinner) findViewById(R.id.devcombox_tramite);
		cboxtipodev = (Spinner) findViewById(R.id.devcombox_tipo);
		tbxNombreDelCliente = (TextView) findViewById(R.id.devtextv_detallecliente);
		tbxNota = (EditText) findViewById(R.id.devtextv_detalle_notas);
		View include = findViewById(R.id.pdevgrilla);
		lvdevproducto = (ExpandableListView) include.findViewById(R.id.ExpList);
		tbxFecha = (EditText) findViewById(R.id.devetextv_detalle_fecha);
		tbxtotaldev = (EditText) findViewById(R.id.devtextv_total);
		adapter_tramite = new CustomAdapter(getContext(),
				R.layout.spinner_rows, setListData(hmtramite));
		cboxtramitedev.setAdapter(adapter_tramite);

		adapter_tipodev = new CustomAdapter(getContext(),
				R.layout.spinner_rows, setListData(hmtipodev));
		cboxtipodev.setAdapter(adapter_tipodev);

		cboxmotivodev.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || ckboxvencidodev.isChecked())
					return;

				if (!"DAÑADO".equals(((ValorCatalogo) adapter_motdev.getItem(
						position).getObj()).getCodigo())) {
					cboxtramitedev.setSelection(2);
					adapter_tramite.setSelectedPosition(2);
					cboxtramitedev.setEnabled(false);
				} else
					cboxtramitedev.setEnabled(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		cboxtramitedev.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0)
					return;
				if (adapter == null
						|| (adapter != null && adapter.getGroupCount() == 0))
					return;

				if ("RE".equals(((SpinnerModel) cboxtramitedev
						.getSelectedItem()).getCodigo())
						&& ckboxncinmeditata.isChecked())
					ckboxncinmeditata.setChecked(false);
				if ("RE".equals(((Map.Entry<String, String>) adapter_tramite
						.getItem(position).getObj()).getKey()))
					ValidarTipoTramite();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		ckboxncinmeditata
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (adapter == null
								|| (adapter != null && adapter.getGroupCount() == 0))
							return;
						if ("RE".equals(((SpinnerModel) cboxtramitedev
								.getSelectedItem()).getCodigo())
								&& ckboxncinmeditata.isChecked())
							ckboxncinmeditata.setChecked(false);
						if ("TT".equals(((SpinnerModel) cboxtipodev
								.getSelectedItem()).getCodigo())
								&& pedido != null)
							ckboxncinmeditata.setChecked(false);
						EstimarCostosDev(true);
					}
				});

		cboxtipodev.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					cboxtipodev.setSelection(PARCIAL);
					return;
				}
				if (adapter == null
						|| (adapter != null && adapter.getGroupCount() == 0))
					return;

				if (pedido != null) {
					establecerCantidadDev();
					EstimarCostosDev(true);
					CalTotalDevolucion();
					adapter.notifyDataSetChanged();
				}

				ValidarTipoTramite();
				if ("TT".equals(((Map.Entry<String, String>) adapter_tipodev
						.getItem(position).getObj()).getKey())
						&& pedido != null)
					ckboxncinmeditata.setChecked(false);

				adapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		if (devolucion == null) {
			devolucion = new Devolucion();
			devolucion.setCodEstado(REGISTRADA);
			devolucion.setId(0);
			devolucion.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
			devolucion.setNumeroCentral(0);
			devolucion.setReferencia(0);
			cboxtramitedev.setSelection(NOTADECREDITO);
			cboxtipodev.setSelection(PARCIAL);
		}
		if (devolucion.getFecha() == 0)
			devolucion.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
		tbxFecha.setText("" + DateUtil.idateToStrYY(devolucion.getFecha()));
		CreateMenu();

	}

	public void initExpandableListView() {
		if (adapter == null) {
			ArrayList<SetViewHolderWLayout> layouts = new ArrayList<SetViewHolderWLayout>();
			layouts.add(new SetViewHolderWLayout(R.layout.detalle_productolote,
					ProductoLoteViewHolder.class, true));
			layouts.add(new SetViewHolderWLayout(R.layout.detalle_loteproducto,
					ProductoLoteDetalleViewHolder.class, false));
			try {
				adapter = new ExpandListAdapter(context,
						lgroups = SetStandardGroups(), layouts);
				lvdevproducto.setAdapter(adapter);

				for (int g = 0; g < adapter.getGroupCount(); g++) {
					lvdevproducto.expandGroup(g);
				}

				lvdevproducto
						.setOnChildClickListener(new OnChildClickListener() {

							@Override
							public boolean onChildClick(
									ExpandableListView _parent, View v,
									int groupPosition, int childPosition,
									long id) {
								int flatpost;
								int ajustPos;
								if (_parent == null)
									return false;

								if (positioncache != null
										&& positioncache.length != 0) {
									long value = ExpandableListView
											.getPackedPositionForChild(
													positioncache[0],
													positioncache[1]);
									flatpost = _parent
											.getFlatListPosition(value);
									ajustPos = flatpost
											- _parent.getFirstVisiblePosition();
									View oldview = _parent.getChildAt(ajustPos);
									if (oldview != null
											&& oldview.getTag() != null
											&& oldview.getTag() instanceof ProductoLoteDetalleViewHolder) {
										oldview.setBackgroundDrawable(context
												.getResources().getDrawable(
														R.color.Terracota));
										oldview.setSelected(false);
									}

								}
								v.setSelected(true);
								v.setBackgroundDrawable(context.getResources()
										.getDrawable(R.color.LighBlueMarine));
								positioncache[0] = groupPosition;
								positioncache[1] = childPosition;

								childselected = (ExpandListChild) adapter
										.getChild(positioncache[0],
												positioncache[1]);

								return true;
							}
						});

				lvdevproducto
						.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(
									AdapterView<?> _parent, View view,
									int position, long id) {
								ExpandableListView elv;
								int flatpost;
								int ajustPos;
								long value = 0;
								elv = (ExpandableListView) _parent;
								if (ExpandableListView
										.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

									if (view.getTag() instanceof ProductoLoteDetalleViewHolder) {
										ProductoLoteDetalleViewHolder pld = (ProductoLoteDetalleViewHolder) view
												.getTag();
										if (elv == null)
											return false;

										if (positioncache != null
												&& positioncache.length != 0) {
											if (positioncache[1] != -1) {
												value = ExpandableListView
														.getPackedPositionForChild(
																positioncache[0],
																positioncache[1]);
												flatpost = elv
														.getFlatListPosition(value);
												ajustPos = flatpost
														- elv.getFirstVisiblePosition();
												View oldview = elv
														.getChildAt(ajustPos);
												if (oldview != null
														&& oldview.getTag() != null
														&& oldview.getTag() instanceof ProductoLoteDetalleViewHolder) {
													oldview.setBackgroundDrawable(context
															.getResources()
															.getDrawable(
																	R.color.Terracota));
													oldview.setSelected(false);
												}

											}

										}
										view.setSelected(true);
										view.setBackgroundDrawable(context
												.getResources().getDrawable(
														R.color.LighBlueMarine));

										positioncache[0] = ExpandableListView
												.getPackedPositionGroup(id);
										positioncache[1] = ExpandableListView
												.getPackedPositionChild(id);

										childselected = (ExpandListChild) adapter
												.getChild(positioncache[0],
														positioncache[1]);
										groupselected = (ExpandListGroup) adapter
												.getGroup(positioncache[0]);
										EditarProductoLote(
												groupselected.getName(),
												childselected);

									}

									return true;
								} else {

									if (view.getTag() instanceof ProductoLoteViewHolder) {
										if (elv == null)
											return false;

										value = ExpandableListView
												.getPackedPositionForGroup(positioncache[0]);
										flatpost = elv
												.getFlatListPosition(value);
										ajustPos = flatpost
												- elv.getFirstVisiblePosition();
										View oldview = elv.getChildAt(ajustPos);
										if (oldview != null
												&& oldview.getTag() != null
												&& oldview.getTag() instanceof ProductoLoteDetalleViewHolder) {
											oldview.setBackgroundDrawable(context
													.getResources()
													.getDrawable(
															R.color.Terracota));
											oldview.setSelected(false);
										}

										positioncache[0] = ExpandableListView
												.getPackedPositionGroup(id);
										positioncache[1] = -1;

										childselected = null;
										groupselected = (ExpandListGroup) adapter
												.getGroup(positioncache[0]);

										EditarProductoBonificacion(
												groupselected.getName(),
												groupselected);
									}

								}
								return false;
							}
						});

				lvdevproducto
						.setOnChildClickListener(new OnChildClickListener() {
							@Override
							public boolean onChildClick(
									ExpandableListView parent, View v,
									int groupPosition, int childPosition,
									long id) {
								Object obj = adapter.getChild(groupPosition,
										childPosition);
								return false;
							}
						});

				lvdevproducto
						.setOnGroupClickListener(new OnGroupClickListener() {

							public boolean onGroupClick(
									ExpandableListView parent, View v,
									int groupPosition, long id) {
								groupselected = (ExpandListGroup) adapter
										.getGroup(groupPosition);
								return false;
							}
						});

				EstimarCostosDev(true);
				CalTotalDevolucion();
				CalMontoCargoVendedor();

			} catch (Exception e) {
			}
		} else {
			adapter.notifyDataSetChanged();
			((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
		}

	}

	private void EditarProductoBonificacion(String productname,
			ExpandListGroup _groupselected) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag("DevolucionProductoBonificacion");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DevolucionProductoBonificacion newFragment = DevolucionProductoBonificacion
				.newInstance(productname, ((DevolucionProducto) _groupselected
						.getObject()).getCantidadDevolver());
		newFragment
				.obtenerProductoModificado(new escucharModificacionProducto() {

					@Override
					public void onButtonClick(int cantidadbonificacion) {
						actualizarProductoBonificacion(cantidadbonificacion);
						EstimarCostosDev(false);
						CalTotalDevolucion();
						CalMontoCargoVendedor();
						adapter.notifyDataSetChanged();
					}
				});
		newFragment.show(ft, "DevolucionProductoBonificacion");
	}

	private void EditarProductoLote(String productname,
			ExpandListChild _childselected) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag("dialogDevolucionProductoCantidad");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DevolucionProductoCantidad newFragment = DevolucionProductoCantidad
				.newInstance(productname,
						(DevolucionProductoLote) _childselected.getObject());
		newFragment
				.obtenerProductoLoteModificado(new escucharModificacionProductoLote() {

					@Override
					public void onButtonClick(DevolucionProductoLote plote) {
						actualizarProductoLote(plote);
						EstimarCostosDev(true);
						CalTotalDevolucion();
						CalMontoCargoVendedor();
						adapter.notifyDataSetChanged();
					}
				});
		newFragment.show(ft, "dialogDevolucionProductoCantidad");
	}

	public List<ExpandListGroup> SetStandardGroups() {
		LinkedList<ExpandListGroup> _lgroups = new LinkedList<ExpandListGroup>();

		LinkedList<ExpandListChild> groupchild;
		for (DevolucionProducto dp : dev_prod) {
			ExpandListGroup group = new ExpandListGroup();
			groupchild = new LinkedList<ExpandListChild>();
			group.setName(dp.getNombreProducto());
			group.setObject(dp);
			for (DevolucionProductoLote dpl : dp.getProductoLotes()) {
				ExpandListChild ch = new ExpandListChild();
				ch.setName(dpl.getNumeroLote());
				ch.setObject(dpl);
				groupchild.add(ch);
			}
			group.setItems(groupchild);
			_lgroups.add(group);
		}
		return _lgroups;
	}

	// SetcantidadDevolver
	public void establecerCantidadDev() {
		int contador = 0;
		LinkedList<ExpandListGroup> _lgroups = new LinkedList<ExpandListGroup>();
		LinkedList<ExpandListChild> groupchild;
		Iterator<ExpandListGroup> llgroups = lgroups.iterator();
		while (llgroups.hasNext()) {
			ExpandListGroup _group = (ExpandListGroup) llgroups.next();
			DevolucionProducto dp = (DevolucionProducto) _group.getObject();
			int cantidaddevueltadp = 0;
			ExpandListGroup group = new ExpandListGroup();
			groupchild = new LinkedList<ExpandListChild>();

			for (DevolucionProductoLote dpl : dp.getProductoLotes()) {
				ExpandListChild ch = new ExpandListChild();
				if ("TT".equals(((SpinnerModel) cboxtipodev.getSelectedItem())
						.getCodigo()))
					dpl.setCantidadDevuelta(dpl.getCantidadDespachada());
				else
					dpl.setCantidadDevuelta(0);
				ch.setName(dpl.getNumeroLote());
				ch.setObject(dpl);
				groupchild.add(ch);
				cantidaddevueltadp = cantidaddevueltadp
						+ dpl.getCantidadDevuelta();
			}
			dp.setCantidadDevolver(cantidaddevueltadp);
			group.setName(dp.getNombreProducto());
			group.setObject(dp);
			group.setItems(groupchild);
			lgroups.set(contador, group);
			contador++;
		}

	}

	public void updateObject() {
		List<ExpandListGroup> _lgroups = lgroups;
		ExpandListGroup group = null;
		DevolucionProducto[] adp = new DevolucionProducto[lgroups.size()];

		for (int a = 0; a < lgroups.size(); a++) {
			group = (ExpandListGroup) lgroups.get(a);
			DevolucionProducto dp = (DevolucionProducto) group.getObject();

			LinkedList<ExpandListChild> Items = group.getItems();
			DevolucionProductoLote[] adpl = new DevolucionProductoLote[Items
					.size()];
			for (int i = 0; i < Items.size(); i++) {
				adpl[i] = (DevolucionProductoLote) Items.get(i).getObject();
			}
			dp.setProductoLotes(adpl);
			adp[a] = dp;
		}
		devolucion.setProductosDevueltos(adp);
	}

	public void actualizarProductoBonificacion(int cantidadbonificacion) {
		DevolucionProducto _dp = (DevolucionProducto) groupselected.getObject();
		_dp.setBonificacionVen(cantidadbonificacion);
		groupselected.setObject(_dp);
		lgroups.set(positioncache[0], groupselected);
		adapter.notifyDataSetChanged();
	}

	private void actualizarProductoLote(DevolucionProductoLote dpl) {
		DevolucionProducto _dp = (DevolucionProducto) groupselected.getObject();
		if ("".equals(dpl.getNumeroLote()) || dpl.getNumeroLote() == null) {
			com.panzyma.nm.NMApp
					.getController()
					.notifyOutboxHandlers(
							ControllerProtocol.ERROR,
							0,
							0,
							new ErrorMessage(
									String.format(
											"Para cada detalle del producto '{0}' se debe ingresar el número de lote",
											_dp.getNombreProducto()),
									String.format(
											"Para cada detalle del producto '{0}' se debe ingresar el número de lote",
											_dp.getNombreProducto()), ""));
			return;
		}

		if (dpl.getFechaVencimiento() <= 0) {
			com.panzyma.nm.NMApp
					.getController()
					.notifyOutboxHandlers(
							ControllerProtocol.ERROR,
							0,
							0,
							new ErrorMessage(
									String.format(
											"Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",
											_dp.getNombreProducto()),
									String.format(
											"Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",
											_dp.getNombreProducto()), ""));
			return;
		}
		if (dpl.getCantidadDevuelta() <= 0) {
			com.panzyma.nm.NMApp
					.getController()
					.notifyOutboxHandlers(
							ControllerProtocol.ERROR,
							0,
							0,
							new ErrorMessage(
									String.format(
											"La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.",
											_dp.getNombreProducto()),
									String.format(
											"La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.",
											_dp.getNombreProducto()), ""));
			return;
		}

		int cantidadTotalDevolver = 0;
		ExpandListChild ch = new ExpandListChild();
		ch.setName(dpl.getNumeroLote());
		ch.setObject(dpl);
		groupselected.getItems().set(positioncache[1], ch);
		// sumar todas las cantidades a devolver de sus lote del producto
		// seleccionado.
		for (ExpandListChild _ch : groupselected.getItems())
			cantidadTotalDevolver += ((DevolucionProductoLote) _ch.getObject())
					.getCantidadDevuelta();

		_dp.setCantidadDevolver(cantidadTotalDevolver);
		groupselected.setObject(_dp);
		lgroups.set(positioncache[0], groupselected);

	}

	public void updateGrid(DevolucionProductoLote dpl) {
		int cantidadDevolver = 0, cantidadTotalDevolver = 0, cantidadBonificada = 0, cantidadOrdenada = 0, cantidadFacturada;
		float proporcion = 0.0f;
		double preciounitario = 0;
		double montobonificacion = 0;
		double subTotal;
		double impuesto = 0;

		ExpandListGroup lg = new ExpandListGroup();
		ExpandListChild ch = new ExpandListChild();
		ch.setName(dpl.getNumeroLote());
		ch.setObject(dpl);
		groupselected.getItems().set(positioncache[1], ch);

		// sumar todas las cantidades a devolver de sus lote del producto
		// seleccionado.
		for (ExpandListChild _ch : groupselected.getItems())
			cantidadTotalDevolver += ((DevolucionProductoLote) _ch.getObject())
					.getCantidadDevuelta();
		DevolucionProducto dp = (DevolucionProducto) groupselected.getObject();
		dp.setCantidadDevolver(cantidadTotalDevolver);
		groupselected.setObject(dp);
		lgroups.set(positioncache[0], groupselected);

		// Estimar costo devolucion
		int cantmindevbonif = Integer.parseInt(this.getSharedPreferences(
				"SystemParams", android.content.Context.MODE_PRIVATE)
				.getString("CantMinDevolvBonif", "0"));

		DevolucionProducto _dp = (DevolucionProducto) groupselected.getObject();
		cantidadDevolver = _dp.getCantidadDevolver();
		// bonificacion se calcula si la cantidad a devolver es mayor que la
		// cantidad min
		if (cantidadDevolver >= cantmindevbonif || pedido != null) {
			if (pedido != null) {
				cantidadBonificada = dp.getCantidadBonificada();
				cantidadOrdenada = dpl.getCantidadDespachada();
				cantidadFacturada = cantidadOrdenada
						+ dp.getCantidadBonificada();
				preciounitario = dp.getPrecio() / 100.00;
				if (cantidadDevolver == cantidadFacturada)
					cantidadBonificada = dp.getCantidadBonificada();
				else {
					if (cantidadDevolver < cantidadFacturada) {
						// calcular Bonificacion
						proporcion = ((float) dp.getCantidadBonificada() / (float) cantidadOrdenada) + 1;
						cantidadBonificada = (int) Math
								.ceil((cantidadDevolver - (cantidadDevolver / proporcion)));
					}
				}

			}

		} else
			cantidadBonificada = 0;
		// calcular montobonificacion
		montobonificacion = cantidadBonificada * preciounitario;
		subTotal = cantidadDevolver * preciounitario;
		// Impuesto --pendiente, tenemos q traer el impuesto del pedido.
		// impuesto = porcentajadeImpuestoPedido * ( subtotal -
		// montobonificacion);
		// Total
		dp.setBonificacion(cantidadBonificada);
		dp.setBonificacionVen(cantidadBonificada);
		dp.setMontoBonif((long) ((double) (montobonificacion * 100)));
		dp.setMontoBonifVen((long) ((double) (montobonificacion * 100)));

		dp.setSubtotal((long) ((double) (subTotal * 100.00)));
		dp.setTotal((long) ((double) ((subTotal - montobonificacion + impuesto)) * 100));
		groupselected.setObject(dp);
		lgroups.set(positioncache[0], groupselected);
		// fin estimacion de costo
		initExpandableListView();
	}

	public void CreateMenu() {
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(
				R.array.devolucioneditoptions);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		// Añadimos Funciones al menú laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				drawerList.setItemChecked(position, true);
				drawerLayout.closeDrawers();
				tituloSeccion = opcionesMenu[position];
				// Ponemos el titulo del Menú
				getSupportActionBar().setTitle(tituloSeccion);
				Controller controller = com.panzyma.nm.NMApp.getController();
				switch (position) {

				case ID_SELECCIONAR_CLIENTE:
					seleccionarCliente();
					break;
				case ID_DEVOLVER_PEDIDO:
					devolverdocumento();
					break;
				case ID_AGREGAR_PRODUCTO:
					agregarProducto();
					break;
				case ID_AGREGAR_LOTE:
					if (groupselected != null) {
						EditDevolucionProducto dialogDevolucion = new EditDevolucionProducto(
								groupselected, me);
						FragmentManager fragmentManager = getSupportFragmentManager();
						dialogDevolucion.show(fragmentManager, "");
					}
					break;
				case ID_EDITAR_NOTA:
					EditarNotaDevolucion();
					break;
				case ID_VER_COSTEO:
					VerCosteoDevolucion();
					break;
				case ID_GUARDAR:
					salvarDevolucion();
					break;
				case ID_ENVIAR:
					enviarPedido();
					break;
				// case ID_IMPRIMIR_COMPROBANTE:
				// try {
				// ImprimirComprobante();
				// break;
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				case ID_CERRAR:
					// FINISH_ACTIVITY();
					break;

				}
			}
		});

		tituloSeccion = getTitle();
		tituloApp = getTitle();

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloSeccion);
				ActivityCompat.invalidateOptionsMenu(ViewDevolucionEdit.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewDevolucionEdit.this);

			}
		};

		// establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	protected void salvarDevolucion() {
		updateObject();
		if (!validarDevolucion())
			return;
		Message msg = new Message();
		msg.obj = devolucion;
		msg.what = SAVE_DATA_FROM_LOCALHOST;
		com.panzyma.nm.NMApp.getController().getInboxHandler().sendMessage(msg);

	}

	private void enviarPedido() {
		if (!validarDevolucion()) {
			return;
		}
		Message msg = new Message();
		msg.obj = devolucion;
		msg.what = ControllerProtocol.ENVIARDEVOLUCION;
		com.panzyma.nm.NMApp.getController().getInboxHandler().sendMessage(msg);
	}

	public boolean validarDevolucion() {

		devolucion.setFecha(DateUtil.d2i(new Date()));
		if (cliente == null) {
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage(
							"Seleccione primero el cliente de la Devolución.",
							"Seleccione primero el cliente de la Devolución.",
							""));
			return false;
		}
		if ((cboxmotivodev == null || (cboxmotivodev != null && cboxmotivodev
				.getSelectedItem() == null)) && !ckboxvencidodev.isChecked()) {
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage(
							"Debe seleccionar la causa de la devolución",
							"Debe seleccionar la causa de la devolución", ""));
			return false;
		}

		if (devolucion.getProductosDevueltos() == null
				|| (devolucion.getProductosDevueltos() != null && devolucion
						.getProductosDevueltos().length == 0)) {
			com.panzyma.nm.NMApp
					.getController()
					.notifyOutboxHandlers(
							ControllerProtocol.ERROR,
							0,
							0,
							new ErrorMessage(
									"Al menos un producto debe ser incluido en la devolución.",
									"Al menos un producto debe ser incluido en la devolución.",
									""));
			return false;
		}

		for (DevolucionProducto _dp : devolucion.getProductosDevueltos()) {
			if (_dp.getProductoLotes() == null
					|| (_dp.getProductoLotes() != null && _dp
							.getProductoLotes().length == 0)) {
				com.panzyma.nm.NMApp
						.getController()
						.notifyOutboxHandlers(
								ControllerProtocol.ERROR,
								0,
								0,
								new ErrorMessage(
										"Al menos un lote debe ser incluido en el detalle.",
										"Al menos un lote debe ser incluido en el detalle.",
										""));
				return false;
			}
			for (DevolucionProductoLote _dpl : _dp.getProductoLotes()) {
				if ("".equals(_dpl.getNumeroLote())
						|| _dpl.getNumeroLote() == null) {
					com.panzyma.nm.NMApp
							.getController()
							.notifyOutboxHandlers(
									ControllerProtocol.ERROR,
									0,
									0,
									new ErrorMessage(
											String.format(
													"Para cada detalle del producto '{0}' se debe ingresar el número de lote",
													_dp.getNombreProducto()),
											String.format(
													"Para cada detalle del producto '{0}' se debe ingresar el número de lote",
													_dp.getNombreProducto()),
											""));
					return false;
				}

				if (_dpl.getFechaVencimiento() <= 0) {
					com.panzyma.nm.NMApp
							.getController()
							.notifyOutboxHandlers(
									ControllerProtocol.ERROR,
									0,
									0,
									new ErrorMessage(
											String.format(
													"Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",
													_dp.getNombreProducto()),
											String.format(
													"Para cada detalle del producto '{0}' se debe ingresar la fecha de vencimiento del lote",
													_dp.getNombreProducto()),
											""));
					return false;
				}
				if (_dpl.getCantidadDevuelta() <= 0) {
					com.panzyma.nm.NMApp
							.getController()
							.notifyOutboxHandlers(
									ControllerProtocol.ERROR,
									0,
									0,
									new ErrorMessage(
											String.format(
													"La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.",
													_dp.getNombreProducto()),
											String.format(
													"La cantidad a devolver de cada lote del producto '{0}' debe ser mayor que cero.",
													_dp.getNombreProducto()),
											""));
					return false;
				}
			}
		}

		if ("NC".equals(((SpinnerModel) cboxtramitedev.getSelectedItem())
				.getCodigo())) {
			devolucion.setTipoTramite("NC");
		} else {
			devolucion.setTipoTramite("RE");
		}

		devolucion.setParcial(("TT".equals(((SpinnerModel) cboxtramitedev
				.getSelectedItem()).getCodigo())) ? false : true);
		devolucion.setDeVencido(ckboxvencidodev.isChecked());
		if (this.pedido == null || this.pedido.getId() == 0L) {
			devolucion.setObjPedidoDevueltoID(0);
		} else {
			devolucion.setObjPedidoDevueltoID(pedido.getId());
			devolucion.setObjVendedorID(pedido.getObjVendedorID());
		}

		devolucion.setObjClienteID(cliente.getIdCliente());
		devolucion.setObjSucursalID(cliente.getIdSucursal());
		devolucion.setEspecial(!"".equals(devolucion.getObservacion()));
		if (!ckboxncinmeditata.isChecked()
				&& "REGISTRADA".equals(devolucion.getCodEstado())) {
			EstimarCostosDev(false);
			CalMontoPromocion();
			double d = DevolucionBL.CalMontoPromocion(cliente, pedido, Arrays
					.asList(devolucion.getProductosDevueltos()), ("TT"
					.equals(((SpinnerModel) cboxtramitedev.getSelectedItem())
							.getCodigo())) ? false : true);
			CalTotalDevolucion();
		}
		return true;
	}

	public void EstimarCostosDev(boolean calBonif) {
		// Estimar costo devolucion
		int cantmindevbonif = Integer.parseInt(this.getSharedPreferences(
				"SystemParams", android.content.Context.MODE_PRIVATE)
				.getString("CantMinDevolvBonif", "0"));
		// sumar todas las cantidades a devolver de sus lote del producto
		// seleccionado.
		int contador = 0;
		Iterator<ExpandListGroup> llgroups = lgroups.iterator();
		while (llgroups.hasNext()) {
			ExpandListGroup group = (ExpandListGroup) llgroups.next();
			DevolucionProducto _dp = (DevolucionProducto) group.getObject();
			Producto prod = null;
			try {
				prod = ModelProducto.getProductoByID(getContext()
						.getContentResolver(), _dp.getObjProductoID());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int cantidaddevolver = _dp.getCantidadDevolver();
			if (cantidaddevolver >= cantmindevbonif || this.pedido != null) {
				if (this.pedido != null) {
					int cantidadordenada = _dp.getCantidadOrdenada();
					int cantidadbonificadaeditada = _dp.getCantidadBonificada();
					if (_dp.getCantidadDevolver() == (cantidadordenada + cantidadbonificadaeditada)) {
						_dp.setBonificacion(cantidadbonificadaeditada);
						_dp.setBonificacionVen(cantidadbonificadaeditada);
					} else {
						if (calBonif) {
							if (_dp.getCantidadDevolver() < (cantidadordenada + cantidadbonificadaeditada)) {
								int rs = DevolucionBL.CalcularBonificacion(
										_dp.getCantidadDevolver(),
										_dp.getCantidadOrdenada(),
										_dp.getCantidadBonificada());
								_dp.setBonificacion(rs);
								_dp.setBonificacionVen(rs);
							}
						}
					}
				} else {
					if (calBonif) {
						int qtybonif = 0;
						Bonificacion bonif = DevolucionBL.getBonificacion(prod,
								cliente.getObjCategoriaClienteID(),
								_dp.getCantidadDevolver());
						if (bonif != null && bonif.getCantBonificacion() != 0) {
							qtybonif = bonif.getCantBonificacion();
						}
						_dp.setBonificacion(qtybonif);
						_dp.setBonificacionVen(qtybonif);
					}
				}
			} else {
				_dp.setBonificacion(0);
				_dp.setBonificacionVen(0);
			}

			if (this.pedido != null) {
				if (!ckboxvencidodev.isChecked()) {
					// Calcular precio IdTipoClienteMayorista
					long idTP = pedido.getObjTipoPrecioVentaID();
					long idTipoCliente = cliente.getObjTipoClienteID();
					long IdTipoClienteMayorista = Long.parseLong(this
							.getSharedPreferences("SystemParams",
									android.content.Context.MODE_PRIVATE)
							.getString("IdTipoClienteMayorista", "0"));
					if (IdTipoClienteMayorista == idTipoCliente)
						idTP = Long.parseLong(this.getSharedPreferences(
								"SystemParams",
								android.content.Context.MODE_PRIVATE)
								.getString("IdTipoPrecioGeneral", "0"));

					_dp.setPrecio((long) (DevolucionBL.getPrecioProducto(prod,
							idTP, _dp.getCantidadDevolver()) * 100));
				} else {
					List<Catalogo> catalogo = ModelLogic
							.getValorCatalogo(new String("VEN"));
					long idTP = pedido.getObjTipoPrecioVentaID();
					if (catalogo != null)
						idTP = catalogo.get(0).getId();
					_dp.setPrecio((long) (DevolucionBL.getPrecioProducto(prod,
							idTP, _dp.getCantidadDevolver()) * 100));
				}

				_dp.setSubtotal((long) ((double) (_dp.getCantidadDevolver() * (_dp
						.getPrecio() / 100.00)) * 100.00));
				long boniL = (long) ((double) (_dp.getBonificacion() * (_dp
						.getPrecio() / 100.00)) * 100.00);

				_dp.setMontoBonif(boniL);
				boniL = (long) ((double) (_dp.getBonificacionVen() * (_dp
						.getPrecio() / 100.00)) * 100.00);
				_dp.setMontoBonifVen(boniL);

				if (_dp.isGravable()) {
					if (this.pedido != null) {
						_dp.setPorcImpuesto((long) DevolucionBL
								.getPorcImpuesto(pedido, prod.getId()));

					} else {
						int porcimp = Integer.parseInt(this
								.getSharedPreferences("SystemParams",
										android.content.Context.MODE_PRIVATE)
								.getString("PorcentajeImpuesto", "0"));
						_dp.setPorcImpuesto((long) porcimp);
					}

				}
				long impuestoL = (long) (_dp.getPorcImpuesto() * (_dp
						.getSubtotal() - _dp.getMontoBonif() / 100.00));
				_dp.setImpuesto(impuestoL);
				_dp.setImpuestoVen((long) (_dp.getPorcImpuesto() * (_dp
						.getSubtotal() - _dp.getMontoBonifVen() / 100.00)));
				_dp.setTotal(_dp.getSubtotal() - _dp.getMontoBonif()
						+ impuestoL);
				_dp.setTotalVen(_dp.getSubtotal() - _dp.getMontoBonifVen()
						+ _dp.getImpuestoVen());

				List<PedidoPromocion> pap = Arrays.asList(pedido
						.getPromocionesAplicadas());

				if (pap != null && pap.size() != 0) {
					PedidoPromocionDetalle ppd = null;
					for (PedidoPromocion pp : pap) {
						ppd = Predicate.find(Arrays.asList(pp.getDetalles()),
								_dp.getObjProductoID(),
								new IFilterabble<PedidoPromocionDetalle>() {

									@Override
									public boolean search(
											PedidoPromocionDetalle ppdet,
											long ID) {
										return (ppdet.getObjProductoID() == ID);
									}

								});
						if (ppd != null) {
							_dp.setCantidadPromocionada(ppd
									.getCantidadEntregada());
							break;
						}

					}

				}
			}
			group.setObject(_dp);
			lgroups.set(contador, group);
		}// fin del ciclo

		costeoMontoCargoAdministrativo = BigDecimal.ZERO;
		costeoMontoCargoAdministrativoVen = BigDecimal.ZERO;

		double porcgastoAdm = 0d;
		porcgastoAdm = (!ckboxvencidodev.isChecked()) ? Double.valueOf(this
				.getSharedPreferences("SystemParams",
						android.content.Context.MODE_PRIVATE).getString(
						"PorcentajeGastosAdm", "0")) : Double.valueOf(this
				.getSharedPreferences("SystemParams",
						android.content.Context.MODE_PRIVATE).getString(
						"CargoAdmDevBueno", "0"));
		double sumatotal = 0.d;
		double sumatotalv = 0.d;
		for (int b = 0; b < lgroups.size(); b++) {
			ExpandListGroup lgroup = (ExpandListGroup) lgroups.get(b);
			DevolucionProducto item = (DevolucionProducto) lgroup.getObject();
			sumatotal = sumatotal + item.getTotal();
			sumatotalv = sumatotalv + item.getTotalVen();
		}
		double total = sumatotalv / 100.00;
		costeoMontoCargoAdministrativo = new BigDecimal(
				(total * porcgastoAdm) / 100);
		costeoMontoCargoAdministrativoVen = costeoMontoCargoAdministrativo;
		BDevolucionM.CalcMontoPromocionDevolucion(devolucion);

	}

	public void CalTotalDevolucion() {
		if (lgroups.isEmpty())
			return;
		double sumatotal = 0.d;
		double sumatotalv = 0.d;
		double sumasubtotal = 0.d;
		double sumabonf = 0.d;
		double sumabonfv = 0.d;
		double sumaimp = 0.d;
		double sumaimpv = 0.d;
		for (int a = 0; a < lgroups.size(); a++) {
			ExpandListGroup group = (ExpandListGroup) lgroups.get(a);
			DevolucionProducto item = (DevolucionProducto) group.getObject();
			// SUB TOTAL
			sumasubtotal = sumasubtotal + item.getSubtotal();
			/* devolucion.setSubtotal((long)sumasubtotal); */
			// BONIFICACION
			sumabonf = sumabonf + item.getMontoBonif();
			sumabonfv = sumabonfv + item.getMontoBonifVen();
			/*
			 * devolucion.setMontoBonif((long)sumabonf);
			 * devolucion.setMontoBonifVen((long)sumabonfv);
			 */
			// IMPUESTO
			sumaimp = sumaimp + item.getMontoBonif();
			sumaimpv = sumaimpv + item.getMontoBonifVen();
			/*
			 * devolucion.setImpuesto((long)sumaimp);
			 * devolucion.setImpuestoVen((long)sumaimpv);
			 */

			// TOTAL
			sumatotal = sumatotal + item.getTotal();
			sumatotalv = sumatotalv + item.getTotalVen();
			/*
			 * devolucion.setTotal((long)sumatotal);
			 * devolucion.setTotalVen((long)sumatotalv);
			 */

		}
		// SUB TOTAL
		costeoMontoSubTotal = new BigDecimal(sumasubtotal);
		costeoMontoSubTotalVen = costeoMontoSubTotal;
		// BONIFICACION
		costeoMontoBonificacion = new BigDecimal(sumabonf);
		costeoMontoBonificacionVen = new BigDecimal(sumabonfv);
		// IMPUESTO
		costeoMontoImpuesto = new BigDecimal(sumaimp);
		costeoMontoImpuestoVen = new BigDecimal(sumaimpv);
		// VIÑETA
		BigDecimal vinietas = costeoMontoVinieta == null ? new BigDecimal(0)
				: costeoMontoVinieta;
		// TOTAL DEVOLUCION
		BigDecimal total = new BigDecimal(sumatotal);
		costeoMontoTotal = total.subtract(costeoMontoPromocion).subtract(
				costeoMontoCargoAdministrativo);
		total = new BigDecimal(sumatotalv);
		costeoMontoTotalVen = total.subtract(costeoMontoPromocionVen)
				.subtract(costeoMontoCargoAdministrativoVen).subtract(vinietas);
		tbxtotaldev.setText(""
				+ costeoMontoTotal.divide(new BigDecimal(100.00)).setScale(2,
						RoundingMode.UNNECESSARY));
	}

	public void CalMontoCargoVendedor() {
		if (RECEPCIONADA.equals(devolucion.getCodEstado()))
			return;
		if (costeoMontoTotalVen.longValue() > costeoMontoTotal.longValue()) {
			BigDecimal cargoven = costeoMontoTotalVen
					.subtract(costeoMontoTotal);
			if (cargoven.longValue() > 0) {
				cargoven = cargoven.subtract(costeoMontoBonificacion
						.subtract(costeoMontoBonificacionVen));
				costeoMontoCargoVen = cargoven;
			}
		} else
			costeoMontoCargoVen = BigDecimal.ZERO;

	}

	public List<DevolucionProducto> obtenerDevProductos() {
		List<DevolucionProducto> ldp = new ArrayList<DevolucionProducto>();
		for (int a = 0; a < lgroups.size(); a++) {
			ExpandListGroup group = (ExpandListGroup) lgroups.get(a);
			DevolucionProducto _dp = (DevolucionProducto) group.getObject();
			ldp.add(_dp);
		}
		return ldp;
	}

	public void CalMontoPromocion() {
		costeoMontoPromocion = BigDecimal.ZERO;
		costeoMontoPromocionVen = BigDecimal.ZERO;
		if (pedido != null) {
			BigDecimal val = BigDecimal.ZERO;
			val = new BigDecimal(DevolucionBL.CalMontoPromocion(cliente,
					pedido, Arrays.asList(devolucion.getProductosDevueltos()),
					!devolucion.isParcial()));
			costeoMontoPromocion = (val != null && val
					.compareTo(BigDecimal.ZERO) != 0) ? val
					.multiply(new BigDecimal(100.00)) : BigDecimal.ZERO;
			costeoMontoPromocionVen = costeoMontoPromocion;
		}
	}

	private void agregarProducto() {

		if (cliente == null) {
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage(
							"Seleccione primero el cliente de la Devolución.",
							"Seleccione primero el cliente de la Devolución.",
							""));
			return;
		}

		DialogProducto dp = new DialogProducto(me, aprodselected,
				cliente.getObjCategoriaClienteID(),
				cliente.getObjTipoClienteID());

		dp.setOnDialogProductButtonClickListener(new DialogProducto.OnButtonClickListener() {

			@Override
			public void onButtonClick(DetallePedido det_p, Producto prod) {
				com.panzyma.nm.NMApp.getController().setView(me);
				det_p.setId(pedido.getId());
				aprodselected.add(prod);
			}

		});
		Window window = dp.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 10, display.getHeight() - 50);
		dp.show();

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		_view = super.onCreateView(name, context, attrs);
		return _view;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.action_search).setVisible(true);
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			item.getItemId();
			return true;
		}
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU)
			drawerLayout.openDrawer(Gravity.LEFT);
		return super.onKeyUp(keyCode, event);
	}

	private void seleccionarCliente() {
		if (!((devolucion.getCodEstado().compareTo("REGISTRADA") == 0) || (devolucion
				.getCodEstado().compareTo("APROBADA") == 0))) {
			com.panzyma.nm.NMApp.getController().notifyOutboxHandlers(
					ControllerProtocol.ERROR,
					0,
					0,
					new ErrorMessage(
							"No puede modificar la Devolución en estado "
									+ devolucion.getCodEstado(),
							"No puede modificar la Devolución en estado "
									+ devolucion.getCodEstado(), ""));
			return;
		}

		showClientDialog();
	}

	private void devolverdocumento() {
		if (cliente == null) {
			AppDialog.showMessage(this, "Alerta",
					"Por favor seleccione un cliente primero.",
					DialogType.DIALOGO_ALERTA);
			return;
		}

		boolean isoffline = false;
		pdialog = ProgressDialog.show(this, "Probando conexión", "");
		if ((!NMNetWork.CheckConnectionV2()) && UserSessionManager.HAS_ERROR)
			isoffline = true;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag("DialogDevolverDocumento");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DevolverDocumento newFragment = DevolverDocumento.newInstance(this,
				cliente.getIdSucursal(), devolucion, isoffline);
		newFragment
				.setOnDialogClickListener(new DevolverDocumento.DialogListener() {
					@Override
					public void onDialogPositiveClick(Devolucion _dev) {
						if (_dev != null
								&& _dev.getProductosDevueltos() != null
								&& _dev.getProductosDevueltos().length != 0) {
							devolucion = _dev;
							devolucion.setCodEstado(devolucion.getDescEstado()
									.equals("") ? REGISTRADA : devolucion
									.getDescEstado());
							pedido = devolucion.getObjPedido();
							dev_prod = Arrays.asList(devolucion
									.getProductosDevueltos());
							devolucion.setOlddata(_dev);
							setInformacionCliente();
							initExpandableListView();
						}

					}
				});
		newFragment.show(ft, "dialogDocumentoDevolucion");
		if (isoffline)
			NMApp.getController()._notifyOutboxHandlers(0, 0, 0,
					"Dispositivo fuera de cobertura");
	}

	public void showClientDialog(final AlertDialog... _dialog) {
		DialogCliente dc = new DialogCliente(this,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dc.setOnDialogClientButtonClickListener(new OnButtonClickListener() {

			@Override
			public void onButtonClick(Cliente _cliente) {
				com.panzyma.nm.NMApp.getController()
						.setView((Callback) context);
				if (devolucion.getCodEstado().compareTo("REGISTRADA") != 0)
					return;
				cliente = _cliente;
				setInformacionCliente();
				if (_dialog != null && _dialog.length != 0)
					_dialog[0].dismiss();
			}
		});
		Window window = dc.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dc.show();
	}

	private void setInformacionCliente() {
		tbxNombreDelCliente.setText(cliente.getNombreCliente());
		devolucion.setObjClienteID(cliente.getIdCliente());
		devolucion.setObjSucursalID(cliente.getIdSucursal());
		devolucion.setNombreCliente(cliente.getNombreCliente());
	}

	private ArrayList<SpinnerModel> setListData(ArrayList<Catalogo> valores) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
		// Now i have taken static values by loop.
		// For further inhancement we can take data by webservice / json / xml;
		valores.get(0).getValoresCatalogo()
				.add(0, new ValorCatalogo(0, "-1", ""));
		for (ValorCatalogo valor : valores.get(0).getValoresCatalogo()) {

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

	private ArrayList<SpinnerModel> setListData(Map<String, String> valores) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>();
		// Now i have taken static values by loop.
		// For further inhancement we can take data by webservice / json / xml;
		int contador = 0;
		for (Map.Entry<String, String> entry : valores.entrySet()) {
			final SpinnerModel sched = new SpinnerModel();
			/******* Firstly take data in model object ******/
			sched.setId(contador);
			sched.setCodigo((String) entry.getKey());
			sched.setDescripcion((String) entry.getValue());
			sched.setObj(entry);
			contador++;
			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}
		return CustomListViewValuesArr;
	}

	@Override
	public Object getBridge() {
		return null;
	}

	@Override
	public Context getContext() {
		return this;
	}

	public void dismiss() {
		if (pdialog != null)
			pdialog.dismiss();
		if (dlg != null)
			dlg.dismiss();
	}

	public void hideProgress() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (pdialog != null)
					pdialog.dismiss();
				if (dlg != null)
					dlg.dismiss();
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		dismiss();
		switch (msg.what) {
		case ControllerProtocol.OBTENERVALORCATALOGO:
			adapter_motdev = new CustomAdapter(getContext(),
					R.layout.spinner_rows,
					setListData(catalogos = (ArrayList<Catalogo>) msg.obj));
			cboxmotivodev.setAdapter(adapter_motdev);
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG:
			if (msg.obj instanceof String)
				showStatus(msg.obj.toString(), true);
			break;
		case ControllerProtocol.ERROR:
			/*
			 * AppDialog.showMessage(me, ((ErrorMessage) msg.obj).getTittle(),
			 * ((ErrorMessage) msg.obj).getMessage(),
			 * DialogType.DIALOGO_ALERTA);
			 */
			if (msg.obj instanceof ErrorMessage)
				showStatus(((ErrorMessage) msg.obj).getMessage(), true);
			else if (msg.obj instanceof String)
				showStatus(((String) msg.obj), true);

			break;
		}
		return false;
	}

	public void showStatus(final String mensaje, boolean... confirmacion) {
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

	public void setDev_prod(ArrayList<DevolucionProducto> arrayList) {
		this.dev_prod = arrayList;
	}

	public void ValidarTipoTramite() {
		if (ckboxvencidodev.isChecked()
				&& "REGISTRADA".equals(devolucion.getCodEstado())) {
			if (!PermiteSeleccionarTipoTramite())
				cboxtramitedev.setSelection(NOTADECREDITO);
		}
	}

	public boolean PermiteSeleccionarTipoTramite() {
		double porc = Double.parseDouble(this.getSharedPreferences(
				"SystemParams", android.content.Context.MODE_PRIVATE)
				.getString("PorcMaxDevolucReposic", "0.0"));
		double porcdev = 0d;
		if (pedido != null) {
			for (int a = 0; a < lgroups.size(); a++) {
				ExpandListGroup group = (ExpandListGroup) lgroups.get(a);
				DevolucionProducto _dp = (DevolucionProducto) group.getObject();
				if (_dp.getCantidadOrdenada() == 0)
					continue;

				porcdev = ((double) _dp.getCantidadDevolver())
						/ ((double) _dp.getCantidadOrdenada());
				if (porcdev > porc)
					return false;
			}
		}
		return true;
	}

	private void Setfieldsdevolucion() {

		devolucion.setDeVencido(ckboxvencidodev.isChecked());
		devolucion.setTipoTramite(cboxtramitedev.getSelectedItem().toString());
		devolucion.setDescMotivo(cboxmotivodev.getSelectedItem().toString());
		devolucion.setCodMotivo(cboxmotivodev.getSelectedItem().toString());
		devolucion.setObjMotivoID(cboxmotivodev.getSelectedItemId());

	}

	private void EditarNotaDevolucion() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag(DialogNotaDevolucion.FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DialogNotaDevolucion newFragment = DialogNotaDevolucion.newInstance(
				devolucion.getNota(), this);
		newFragment
				.establecerRespuestaNotaDevolucion(new RespuestaNotaDevolucion() {
					@Override
					public void onButtonClick(String nota) {
						if ("".equals(nota))
							devolucion.setNota(nota);
						tbxNota.setText(nota);
					}
				});
		newFragment.show(ft, DialogNotaDevolucion.FRAGMENT_TAG);
	}

	private void VerCosteoDevolucion() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getSupportFragmentManager()
				.findFragmentByTag(DialogCosteoDevolucion.FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DialogCosteoDevolucion newFragment = DialogCosteoDevolucion
				.newInstance(this);
		newFragment.show(ft, DialogNotaDevolucion.FRAGMENT_TAG);
	}

}
