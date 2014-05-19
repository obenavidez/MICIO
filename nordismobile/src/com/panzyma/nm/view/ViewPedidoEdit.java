package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.C_INVETORY_UPDATED;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.UPDATE_INVENTORY_FROM_SERVER;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.panzyma.nm.DashBoardActivity;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.auxiliar.BluetoothComunication;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol; 
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PedidoPromocion;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Promocion;
import com.panzyma.nm.serviceproxy.Promociones;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.PProductoViewHolder;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DetalleProducto.OnButtonClickHandler;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener;
import com.panzyma.nm.viewdialog.DetalleProducto;
import com.panzyma.nm.viewdialog.DialogCondicionesNotas;
import com.panzyma.nm.viewdialog.DialogProducto;
import com.panzyma.nm.viewdialog.DialogPromociones;
import com.panzyma.nm.viewmodel.vmPProducto; 
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nordismobile.R;
 

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.panzyma.nordismobile.R;

@SuppressLint("NewApi")
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class ViewPedidoEdit extends Activity implements Handler.Callback {
	public EditText tbxFecha;
	public EditText tbxNumReferencia;
	public EditText tbxNumRecibo;
	public EditText tbxNombreDelCliente;
	public TextView tbxPrecio;
	public Spinner tbxTipoVenta;
	public TextView tbxTotalFact;

	public View gridDetallePedido;
	private ListView grid_dp;
	TextView gridheader;
	private GenericAdapter adapter;
	private ProgressDialog pd;
	private Button Menu;
	private QuickAction quickAction;
	private QuickAction quickAction2;
	private int positioncache = -1;
	private Display display;
	private static final String TAG = ViewPedidoEdit.class.getSimpleName();
	private ViewPedidoEdit me;
	private Pedido pedido;
	private Cliente cliente;
	ArrayList<DetallePedido> Lvmpproducto;
	ArrayList<Producto> aprodselected;
    private DetallePedido dpselected;
	// Totales del pedido
	private float subTotal = 0;
	private float descuento = 0;
	private float impuesto = 0;
	private float total = 0;

	private NMApp nmapp;
	private boolean invActualizado;
	private static final int ID_SELECCIONAR_CLIENTE = 1;
	private static final int ID_CONSULTAR_CUENTAS_X_COBRAR = 2;
	private static final int ID_AGREGAR_PRODUCTOS = 3;
	private static final int ID_EDITAR_PRODUCTO = 4;
	private static final int ID_ELIMINAR_PRODUCTO = 5;
	private static final int ID_CONSULTAR_BONIFICACIONES = 6;
	private static final int ID_CONSULTAR_LISTA_PRECIOS = 7;
	private static final int ID_CONDICIONES_Y_NOTAS = 8;
	private static final int ID_APLICAR_PROMOCIONES = 9;
	private static final int ID_DESAPLICAR_PROMOCIONES = 10;
	private static final int ID_EXONERAR_IMPUESTO = 11;
	private static final int ID_GUARDAR = 12;
	private static final int ID_ENVIAR = 13;
	private static final int ID_IMPRIMIR_COMPROBANTE = 14;
	private static final int ID_CERRAR = 15;
	BPedidoM bpm;
		
	private final Handler handler=new Handler();
	private boolean salvado;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedido_edit); 

		try {
			
			AssetManager assetManager = getAssets();

		    InputStream istr = assetManager.open("imglogopanzyma.jpg");
		    Bitmap bitmap = BitmapFactory.decodeStream(istr);
			
			Lvmpproducto = new ArrayList<DetallePedido>();
			aprodselected=new ArrayList<Producto>();
			me = this;
			nmapp = (NMApp) this.getApplicationContext();
			nmapp.getController().setEntities(this, bpm = new BPedidoM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);

			WindowManager wm = (WindowManager) this.getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			initComponent();

		} catch (Exception e) {
			e.printStackTrace();
			buildCustomDialog("Error Message",
					e.getMessage() + "\n Cause:" + e.getCause(), ALERT_DIALOG)
					.show();
		}

	}

	public void initComponent() {
		gridDetallePedido = findViewById(R.id.pddgrilla);
		grid_dp=(ListView)(findViewById(R.id.pddgrilla)).findViewById(R.id.data_items);
		 
		// LinearLayout grilla=(LinearLayout) findViewById(R.id.pddgrilla);
		gridheader = (TextView) gridDetallePedido.findViewById(R.id.header);
		gridheader.setText("Productos a Facturar(0)");
		tbxFecha = (EditText) findViewById(R.id.pddetextv_detalle_fecha);
		tbxNumReferencia = (EditText) findViewById(R.id.pdddetextv_detalle_numref);
		tbxNumRecibo = (EditText) findViewById(R.id.pdddetextv_detalle_num);
		tbxNombreDelCliente = (EditText) findViewById(R.id.pddtextv_detallecliente);
		tbxPrecio = (TextView) findViewById(R.id.pddtextv_detalleprecio);
		tbxTotalFact = (TextView) findViewById(R.id.pddtextv_detalletotales);
		tbxTipoVenta = (Spinner) findViewById(R.id.pddcombox_detalletipo);
		
		grid_dp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				if((parent.getChildAt(positioncache))!=null)						            							            		
            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
            	positioncache=position;				            	
            	dpselected=(DetallePedido) adapter.getItem(position);	 
            	adapter.setSelectedPosition(position);  
            	view.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.action_item_selected));	 
            	 
			}

			 
		});
		
		grid_dp.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				if((parent.getChildAt(positioncache))!=null)						            							            		
            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
            	positioncache=position;				            	
            	dpselected=(DetallePedido) adapter.getItem(position);	 
            	adapter.setSelectedPosition(position);  
            	view.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.action_item_selected));	
            	showMenu(view);
            	
				return true;
			}
		});
		
		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
				R.array.tipopedido, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tbxTipoVenta.setAdapter(adapter2);
		tbxTipoVenta.setSelection(1);
		tbxTipoVenta.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentview,
					View selectedItemView, int pos, long id) {
				pedido.setTipo(pos == 0 ? "CO" : "CR");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		if (pedido == null) {
			pedido = new Pedido();
			cliente = null;
			pedido.setCodEstado("REGISTRADO");
			pedido.setId(0);
			pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
			pedido.setNumeroCentral(0);
			pedido.setNumeroMovil(0);
			// pedido.setObjVendedorID(DataStore.getUsuario().getId());
			pedido.setTipo("CR"); // Crédito
			pedido.setExento(false);
			pedido.setAutorizacionDGI("");
		}

		tbxFecha.setText(Calendar.getInstance().getTime().toString());
		// Fecha del Pedido
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		if (pedido.getFecha() == 0)
			pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));

		if (pedido.getNumeroMovil() > 0)
			tbxNumReferencia.setText(getNumeroPedido(pedido.getNumeroMovil()));

		if (pedido.getNumeroCentral() > 0)
			tbxNumRecibo.setText(getNumeroPedido(pedido.getNumeroCentral()));

		if (pedido.getNombreCliente() != null)
			tbxNombreDelCliente.setText(pedido.getNombreSucursal() + "\\"
					+ pedido.getNombreCliente());

		if (pedido.getCodTipoPrecio() != null)
			tbxPrecio.setText(pedido.getDescTipoPrecio());

		if (pedido.getTipo().compareTo("CO") == 0)
			tbxTipoVenta.setSelection(0);
		else
			tbxTipoVenta.setSelection(1);

		setTotales(false);
		
		gridheader.setText("Productos a Facturar(0)"); 
		 
		adapter = new GenericAdapter(this, PProductoViewHolder.class,Lvmpproducto, R.layout.gridproductosavender);	 
		grid_dp.setAdapter(adapter); 
		adapter.setSelectedPosition(0);
		gridheader.setText("Productos a Facturar(" + adapter.getCount() + ")");  
		 
		initMenu();

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
					quickAction2.addActionItem(new ActionItem(ID_EDITAR_PRODUCTO,
							"Editar Producto"));
					quickAction2.addActionItem(new ActionItem(ID_ELIMINAR_PRODUCTO,
							"Eliminar Productos"));
					quickAction2.setOnActionItemClickListener
					(new QuickAction.OnActionItemClickListener() 
						{
				
							@Override
							public void onItemClick(QuickAction source, final int pos,final int actionId) 
							{ 
										ActionItem actionItem = quickAction2
												.getActionItem(pos);
										
										if(actionId==ID_EDITAR_PRODUCTO)
											editarProducto();
										else if(actionId==ID_ELIMINAR_PRODUCTO)
											eliminarProducto();							
									 
							}
						 }
					 ); 
					quickAction2.show(view,display,false);
				   }
				}
		    );
	}
	
    public void initMenu() {

		quickAction = new QuickAction(this, QuickAction.VERTICAL, 1);
		quickAction.addActionItem(new ActionItem(ID_SELECCIONAR_CLIENTE,
				"Seleccionar Cliente"));
		quickAction.addActionItem(new ActionItem(ID_AGREGAR_PRODUCTOS,
				"Agregar Productos"));
		quickAction.addActionItem(new ActionItem(ID_CONDICIONES_Y_NOTAS,
				"Agregar Condición Y Nota"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_EDITAR_PRODUCTO,
				"Editar Producto"));
		quickAction.addActionItem(new ActionItem(ID_ELIMINAR_PRODUCTO,
				"Eliminar Productos"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_CONSULTAR_CUENTAS_X_COBRAR,
				"Consultar Cuentas X Cobrar"));
		quickAction.addActionItem(new ActionItem(ID_CONSULTAR_BONIFICACIONES,
				"Consultar Bonificaciones"));
		quickAction.addActionItem(new ActionItem(ID_CONSULTAR_LISTA_PRECIOS,
				"Consultar Lista de Precios"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_APLICAR_PROMOCIONES,
				"Aplicar Promociones"));
		quickAction.addActionItem(new ActionItem(ID_DESAPLICAR_PROMOCIONES,
				"Des Aplicar Promociones"));
		quickAction.addActionItem(new ActionItem(ID_EXONERAR_IMPUESTO,
				"Aplicar Exoneración de Impuesto"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_GUARDAR, "Guardar"));
		quickAction.addActionItem(new ActionItem(ID_ENVIAR, "Enviar"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_IMPRIMIR_COMPROBANTE,
				"Imprimir Comprobante"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));

		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, final int pos,
							final int actionId) {

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ActionItem actionItem = quickAction
										.getActionItem(pos);
								
								if (actionId == ID_SELECCIONAR_CLIENTE) 
								{
									DialogCliente dc = new DialogCliente(
											me,
											android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
									dc.setOnDialogClientButtonClickListener(new OnButtonClickListener() {

										@Override
										public void onButtonClick(
												Cliente _cliente) {
											if (pedido.getCodEstado()
													.compareTo("REGISTRADO") != 0)
												return;

											// if (cliente != null &&
											// Lvmpproducto !=
											// null) {
											// if (Lvmpproducto.size() > 0)
											// if (Dialog.ask(Dialog.D_YES_NO,
											// "Si cambia el cliente se eliminarán los detalles del pedido.\n\n¿Desea continuar?")
											// != Dialog.YES) return;
											// }
											cliente = _cliente;
											tbxNombreDelCliente.setText(cliente
													.getNombreCliente());
											tbxPrecio.setText(cliente
													.getDesTipoPrecio());
											pedido.setObjClienteID(cliente
													.getIdCliente());
											pedido.setObjSucursalID(cliente
													.getIdSucursal());

											String[] nomClie = StringUtil.split(
													cliente.getNombreCliente(),
													"/");
											pedido.setNombreCliente(nomClie[1]);
											pedido.setNombreSucursal(nomClie[0]);
											pedido.setObjTipoPrecioVentaID(cliente
													.getObjPrecioVentaID());
											pedido.setCodTipoPrecio(cliente
													.getCodTipoPrecio());
											pedido.setDescTipoPrecio(cliente
													.getDesTipoPrecio());
										}
									});
									Window window = dc.getWindow();
									window.setGravity(Gravity.CENTER);
									window.setLayout(display.getWidth() - 40,
											display.getHeight() - 110);
									dc.show();
								}
								else if (actionId == ID_AGREGAR_PRODUCTOS) {
									if (!((pedido.getCodEstado().compareTo(
											"REGISTRADO") == 0) || (pedido
											.getCodEstado().compareTo(
													"APROBADO") == 0)))
										return;

									if (cliente == null) {
										// Dialog.alert("Seleccione primero el cliente del pedido.");
										return;
									}

									invActualizado = true;
									DialogProducto dp = new DialogProducto(me,
											pedido.getCodTipoPrecio(), aprodselected,
											pedido.getId(),
											cliente.getObjCategoriaClienteID(),
											pedido.getObjTipoPrecioVentaID(),
											cliente.getObjTipoClienteID(),
											pedido.isExento());

									dp.setOnDialogProductButtonClickListener(new DialogProducto.OnButtonClickListener() {

										@Override
										public void onButtonClick(
												DetallePedido det_p,Producto prod) {
											// TODO Auto-generated method stub
											det_p.setId(pedido.getId());
											aprodselected.add(prod);
											Lvmpproducto.add(det_p);  											 
											CalculaTotales();
											setTotales(true); 		 
											adapter.setSelectedPosition(Lvmpproducto.size()-1);
											adapter.notifyDataSetChanged(); 
											
										}

									});
									Window window = dp.getWindow();
									window.setGravity(Gravity.CENTER);
									window.setLayout(display.getWidth() - 10,
											display.getHeight() - 50);
									dp.show();

									// Actualizar dispobibilidad de inventario
									// if (!invActualizado)
									// nmapp.getController().getInboxHandler().sendEmptyMessage(UPDATE_INVENTORY_FROM_SERVER);
									// seleccionarProducto();
									// Ventas.ActualizarDisponibilidadProducto();

								}
								else if (actionId == ID_CONDICIONES_Y_NOTAS) 
								{
									DialogCondicionesNotas cn = new DialogCondicionesNotas(
											me,
											pedido,
											cliente,
											android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
									cn.setOnDialogCNButtonClickListener(new com.panzyma.nm.viewdialog.DialogCondicionesNotas.OnButtonClickListener() {
										@Override
										public void onButtonClick(Pedido _pedido) {
											pedido = _pedido;
										}
									});

									Window window = cn.getWindow();
									window.setGravity(Gravity.CENTER);
									window.setLayout(display.getWidth() - 40,
											display.getHeight() - 110);
									cn.show();
								}
								else if(actionId==ID_EDITAR_PRODUCTO)
									editarProducto();
								else if(actionId==ID_ELIMINAR_PRODUCTO)
									eliminarProducto();	
								else if (actionId == ID_APLICAR_PROMOCIONES) {
									DialogPromociones dprom = new DialogPromociones(
											ViewPedidoEdit.this,
											pedido,
											android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
									
									dprom.setOnDialogPromocionesButtonClickListener(new com.panzyma.nm.viewdialog.DialogPromociones.OnButtonClickHandler() {
										
										@Override
										public void onButtonClick(Promocion promocion) 
										{
											// TODO Auto-generated method stub
											if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido.getCodEstado().compareTo("APROBADO") == 0))) 
												return;
									            
								            if (cliente == null) {
								                //Dialog.alert("Seleccione primero el cliente del pedido.");
								                return;
								            }
								            if ((Lvmpproducto == null) || (Lvmpproducto.size() == 0)) {
								               // Dialog.alert("El pedido no tiene detalle de productos.");
								                return;
								            }
								            
								            
								            if (promocion != null) 
								            {
								                //Validar que no se haya alcanzado el máximo de promociones a aplicar
								                PedidoPromocion[] pproms = pedido.getPromocionesAplicadas();
								                if (pproms != null) {
								                    int maxPromos = Integer.parseInt(me
								            				.getApplicationContext()
								            				.getSharedPreferences("SystemParams",
								            						android.content.Context.MODE_PRIVATE)
								            				.getString("CantMaximaPromocionesAplicar", "0")); 
								                    if (pproms.length >= maxPromos) {
								                       // Dialog.alert("La promoción no puede aplicarse.\n\rSe ha alcanzado el máximo\n\rde promociones aplicables.");
								                        return;
								                    }
								                }
								                
								                
								                Promociones.aplicarPromocion(pedido, promocion,me.getContentResolver());                
								                Promociones.ActualizaPedidoDePromociones(pedido);
								                                
								                //Salvar la promoción aplicada
								                try {
								                	
								                	//save();
								                
								                }
								                catch (Exception ioEx) { 
								                    //Dialog.alert("Error: " + ioEx.toString()); 
								                    return;
								                }
								                
								                DetallePedido[] detPed = pedido.getDetalles();
								                Lvmpproducto=new ArrayList<DetallePedido>(); 
								                for(int i = 0; i < detPed.length; i++) {
								                	Lvmpproducto.add(detPed[i]);
								                }
								                
								                CalculaTotales(); 
								                setTotales(true);
								                adapter.notifyDataSetChanged();
								              Toast.makeText(me,"La promoción ha sido aplicada.",Toast.LENGTH_LONG).show();
								            }
								            
								            
										}
									});
									
									Window window = dprom.getWindow();
									window.setGravity(Gravity.CENTER);
									window.setLayout(display.getWidth() -5,display.getHeight() - 10);
									dprom.show();
								} 
								else if (actionId == ID_IMPRIMIR_COMPROBANTE)
									ImprimirComprobante();
								else if (actionId == ID_CERRAR)
									FINISH_ACTIVITY();
								else {
									Toast.makeText(
											getApplicationContext(),
											actionItem.getTitle() + " selected",
											Toast.LENGTH_SHORT).show();
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
    
    public void setTotales(boolean needaThread){
    	
    	String descTotales = "ST: " + StringUtil.formatReal(subTotal)
				+ " | Desc: " + StringUtil.formatReal(descuento) + " | IVA: "
				+ StringUtil.formatReal(impuesto) + "  |  Total: "
				+ StringUtil.formatReal(total);

		final SpannableString sb = new SpannableString(descTotales);
		final ForegroundColorSpan color = new ForegroundColorSpan(Color.rgb(
				255, 0, 0));

		// Span to set text color to some RGB value
		final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

		// Span to make text bold
		int start = 0, start2 = 0, end = 0, end2;

		String st = "ST:";
		String desc = "Desc:";
		String iva = "IVA:";
		String total = "Total:";

		start = descTotales.indexOf(st);
		end = descTotales.indexOf(st) + st.length();
		start2 = descTotales.indexOf(desc) - 2;
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				start2, 0);

		start = descTotales.indexOf(desc);
		end = descTotales.indexOf(desc) + desc.length();
		start2 = descTotales.indexOf(iva) - 2;
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				start2, 0);

		start = descTotales.indexOf(iva);
		end = descTotales.indexOf(iva) + iva.length();
		start2 = descTotales.indexOf(total) - 2;
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				start2, 0);

		start = descTotales.indexOf(total);
		end = descTotales.indexOf(total) + total.length();
		sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, 0);
		sb.setSpan(new ForegroundColorSpan(Color.rgb(163, 46, 84)), end,
				descTotales.length(), 0);

		if(needaThread)
		{
		
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tbxTotalFact.setText(sb);
					gridheader.setText("Productos a Facturar(" + adapter.getCount() + ")");
				}
			});
			
		}else
			tbxTotalFact.setText(sb);
    	
    }

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Menu = (Button) gridDetallePedido.findViewById(R.id.btnmenu);
			quickAction.show(Menu, display, true);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean handleMessage(Message msg) {
		pd.dismiss();
		switch (msg.what) {
		case C_DATA:

			setData((ArrayList<Pedido>) ((msg.obj == null) ? new ArrayList<Pedido>()
					: msg.obj), C_DATA);
			return true;
		case C_INVETORY_UPDATED:
			seleccionarProducto();
			return true;

		case ERROR:
			pd.dismiss();
			ErrorMessage error = ((ErrorMessage) msg.obj);
			buildCustomDialog(error.getTittle(),
					error.getMessage() + error.getCause(), ALERT_DIALOG).show();
			return true;
		}

		return false;
	}

	public void setData(ArrayList<Pedido> Lpedido, int what) {
	}
 
	public Dialog buildCustomDialog(String tittle, String msg, int type) {
		return new CustomDialog(this.getApplicationContext(), tittle, msg,
				false, type);
	}

	private void FINISH_ACTIVITY() {
		nmapp.getController().removeOutboxHandler(TAG);
		Log.d(TAG, "Activity quitting");
		finish();
	}

	@SuppressLint("NewApi")
	private void CalculaTotales() {
		subTotal = 0;
		descuento = 0;
		impuesto = 0;
		total = 0;
		DetallePedido[] detsPedido = new DetallePedido[Lvmpproducto.size()];
		for (int i = 0; i < Lvmpproducto.size(); i++) {
			DetallePedido dp = (DetallePedido) Lvmpproducto.get(i);
			detsPedido[i] = dp;
			subTotal += StringUtil.round(dp.getSubtotal(), 2);
			descuento += StringUtil.round(dp.getDescuento(), 2);
			impuesto += StringUtil.round(dp.getImpuesto(), 2);
		}

		pedido.setDetalles((detsPedido));
		pedido.setSubtotal(StringUtil.round(subTotal, 2));
		pedido.setDescuento(StringUtil.round(descuento, 2));
		pedido.setImpuesto(StringUtil.round(impuesto, 2));
		total = StringUtil.round(subTotal - descuento + impuesto, 2);
		pedido.setTotal(total);

		// Salvando el tipo de pedido (crédito contado)
		if (tbxTipoVenta != null) {
			pedido.setTipo("CR");
			if (((tbxTipoVenta.getSelectedItemPosition() == 0) ? "CO" : "CR") == "CO")
				pedido.setTipo("CO");
		}

		if (tbxFecha != null) {
			pedido.setFecha(DateUtil.d2i(new Date()));
		}
		
		

	}

	private void seleccionarProducto() {

		invActualizado = true;
		DialogProducto dp = new DialogProducto(me, pedido.getCodTipoPrecio(),
				aprodselected, pedido.getId(), cliente.getObjCategoriaClienteID(),
				pedido.getObjTipoPrecioVentaID(),
				cliente.getObjTipoClienteID(), pedido.isExento());

		dp.setOnDialogProductButtonClickListener(new DialogProducto.OnButtonClickListener() {

			@Override
			public void onButtonClick(DetallePedido det_p,Producto prod) {
				// TODO Auto-generated method stub
				det_p.setId(pedido.getId());
				Lvmpproducto.add(det_p);
				aprodselected.add(prod);
			//	addProductToOrder(det_p);
				CalculaTotales();
				initComponent();
			}

		});
		Window window = dp.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(display.getWidth() - 40, display.getHeight() - 110);
		dp.show();

	}

	public String getNumeroPedido(int numero) {
		int cr = Integer.parseInt(me
				.getApplicationContext()
				.getSharedPreferences("SystemParams",
						android.content.Context.MODE_PRIVATE)
				.getString("CerosRellenoNumRefPedido", "0"));

		char[] num = new char[cr];
		for (int i = 0; i < cr; i++)
			num[i] = '0';

		String strNum = new String(num);
		strNum = strNum + numero;
		return strNum.substring(strNum.length() - cr, strNum.length());
	}

	public void editarProducto()
	{
		Producto producto;
		try 
		{
			 
			 if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido.getCodEstado().compareTo("APROBADO") == 0))) return;
	        
	        int idx = grid_dp.getCheckedItemPosition();;
	        if (idx == -1) return;
	        
	        DetallePedido det = (DetallePedido)Lvmpproducto.get(idx);    
	        if (det.getCantidadOrdenada() == 0) return; //No editar: es producto dado en promoción
	        
			producto = (Producto) nmapp.getController().getBridge().getClass().getMethod("getProductoByID",ContentResolver.class,long.class).invoke(null,me.getApplicationContext().getContentResolver(),dpselected.getObjProductoID());
			DetalleProducto dp=new DetalleProducto(me,producto,cliente.getObjCategoriaClienteID(),
					pedido.getObjTipoPrecioVentaID(),
					cliente.getObjTipoClienteID(),
					pedido.isExento());
			
			dp.setOnDialogDetalleProductButtonClickListener(new OnButtonClickHandler(){

				@Override
				public void onButtonClick(DetallePedido det_p,boolean btn) {

					det_p.setId(pedido.getId());
					Lvmpproducto.set(positioncache, det_p);  
					
					PedidoPromocion[] pproms = pedido.getPromocionesAplicadas();
		            if ((pproms != null) && (pproms.length > 0)) {
		                Promociones.DesaplicarPromociones(pedido);            
		                Promociones.ActualizaPedidoDePromociones(pedido);
		                Toast.makeText(me, "Las promociones han sido desaplicadas.",Toast.LENGTH_LONG).show();
		            }
					
					
					CalculaTotales();
					setTotales(true);  
					adapter.setSelectedPosition(positioncache);
					adapter.notifyDataSetChanged(); 
				} 
				
			}); 
			dp.getWindow().setGravity(Gravity.CENTER); 
			dp.getWindow().setGravity(Gravity.CENTER); 
			dp.getWindow().setLayout(display.getWidth()-30,display.getHeight()-50);  
			dp.show();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	  
	public void eliminarProducto()
	{
		if (!((pedido.getCodEstado().compareTo("REGISTRADO") == 0) || (pedido.getCodEstado().compareTo("APROBADO") == 0))) return;
        int idx = grid_dp.getCheckedItemPosition();
        
        if (idx == -1) return;
        
        //if (Dialog.ask(Dialog.D_YES_NO, "¿Confirma que desea eliminar el detalle?", Dialog.YES) == Dialog.NO) return;
        Lvmpproducto.remove(idx); 
        
        //Si habían promociones aplicadas, desaplicarlas
        PedidoPromocion[] pproms = pedido.getPromocionesAplicadas();
        if ((pproms != null) && (pproms.length > 0)) {
            Promociones.DesaplicarPromociones(pedido);            
            Promociones.ActualizaPedidoDePromociones(pedido);
           // Dialog.alert("Las promociones han sido desaplicadas.");
        }
         
             
        CalculaTotales();
		setTotales(true); 
		   
        
        if (Lvmpproducto.size() > 0) {
            if (idx == 0)
            { 
                positioncache=0;
                dpselected=(DetallePedido) adapter.getItem(0);	
                adapter.setSelectedPosition(0); 
            }
            else {
                if (idx == Lvmpproducto.size())
                {  
                    positioncache=idx - 1;
                    dpselected=(DetallePedido) adapter.getItem(idx - 1);	
                    adapter.setSelectedPosition(idx - 1);
                }
                else
                {
                     positioncache=idx;
                     dpselected=(DetallePedido) adapter.getItem(idx);	
                     adapter.setSelectedPosition(idx);                     
                }
            }            
        }
        adapter.notifyDataSetChanged();
	}
	
	public BPedidoM getBridge() {
		return bpm;
	}
	
    private void ImprimirComprobante() {
       // if (!isDataValid()) return; 
                
        //Salvar pedido si aún no tiene un número de referencia asignado
        if (pedido.getNumeroMovil() == 0) {            
            try { 
            	//save(); 
            	}
            catch(Exception ioEx) { 
               // Dialog.alert("Error: " + ioEx.toString()); 
                return;
            }
        }
        
        String recibo = "";        
        recibo += "T 7 1 123 5 Distribuidora Panzyma - DISPAN\r\n";        
        recibo += "T 7 0 189 54 Orden de Pedido\r\n";
        recibo += "LINE 0 80 576 80 1\r\n";
        recibo += "T 0 0 0 90 Fecha:\r\n";
        recibo += "T 0 0 90 90 " + DateUtil.idateToStr(pedido.getFecha()) + "\r\n";
        recibo += "T 0 0 400 90 Referencia:\r\n";
        recibo += "RIGHT 576\r\n"; 
        recibo += "T 0 0 490 90 " + Ventas.getNumeroPedido(pedido.getNumeroMovil(),me.getApplicationContext()) + "\r\n";
        recibo += "LEFT\r\n";
        recibo += "T 0 0 0 118 Cliente:\r\n";
        recibo += "T 0 0 90 118 " + cliente.getNombreLegalCliente() + "\r\n";
        recibo += "T 0 0 0 144 Vendedor:\r\n";
      //  recibo += "T 0 0 90 144 " + DataStore.getUsuario().getCodigo() + " / " + DataStore.getUsuario().getNombre() + "\r\n";
        recibo += "LINE 0 170 576 170 1\r\n";
        recibo += "T 0 0 0 180 Producto\r\n";
        recibo += "RIGHT 382\r\n"; 
        recibo += "T 0 0 0 180 Cant\r\n";
        recibo += "RIGHT 435\r\n"; 
        recibo += "T 0 0 0 180 Bonif\r\n";
        recibo += "RIGHT 482\r\n"; 
        recibo += "T 0 0 0 180 Prom\r\n";
        recibo += "RIGHT 576\r\n";        
        recibo += "T 0 0 0 180 Precio\r\n";
        recibo += "LEFT\r\n";
        recibo += "LINE 17 196 591 196 1\r\n";
        
        int y = 206;        
        for (int curRecord = 0;  curRecord < Lvmpproducto.size();  curRecord++) {
            DetallePedido det = (DetallePedido)Lvmpproducto.get(curRecord);
            
            String nombreProd = det.getNombreProducto();
            if (nombreProd.length() > 40)
                nombreProd = nombreProd.substring(0, 40) + "...";
            recibo += "T 0 0 0 " + y + " " + nombreProd + "\r\n";
            recibo += "RIGHT 382\r\n"; 
            recibo += "T 0 0 0 " + y + " " + StringUtil.formatInt(det.getCantidadOrdenada()) + "\r\n";
            recibo += "RIGHT 435\r\n"; 
            recibo += "T 0 0 0 " + y + " " + StringUtil.formatInt(det.getCantidadBonificadaEditada()) + "\r\n";
            recibo += "RIGHT 482\r\n"; 
            recibo += "T 0 0 0 " + y + " " + "0" + "\r\n"; //Poner promoción
            recibo += "RIGHT 576\r\n"; 
            recibo += "T 0 0 482 " + y + " " + StringUtil.formatReal(det.getPrecio()) + "\r\n";            
            recibo += "LEFT\r\n";
            y += 26;
        }
        recibo += "LINE 0 " + y + " 576 " + y + " 1\r\n";
        y += 10;        
        recibo += "T 0 0 379 " + y + " Subtotal:\r\n";
        recibo += "RIGHT 576\r\n"; 
        recibo += "T 0 0 0 " + y + " " + StringUtil.formatReal(pedido.getSubtotal()) + "\r\n";
        recibo += "LEFT\r\n";
        
        y += 26;        
        recibo += "T 0 0 379 " + y + " Descuento:\r\n";        
        recibo += "RIGHT 576\r\n"; 
        recibo += "T 0 0 0 " + y + " " + StringUtil.formatReal(pedido.getDescuento()) + "\r\n";
        recibo += "LEFT\r\n";
        
        
        
        y += 26;        
        recibo += "T 0 0 379 " + y + " " + me.getApplicationContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("NombreImpuesto", "--") + ":\r\n";        
        recibo += "RIGHT 576\r\n"; 
        recibo += "T 0 0 0 " + y + " " + StringUtil.formatReal(pedido.getImpuesto()) + "\r\n";
        recibo += "LEFT\r\n";
                
        y += 26;        
        recibo += "T 0 0 379 " + y + " Total " +me.getApplicationContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("MonedaNacional", "--")+ ":\r\n";        
        recibo += "RIGHT 576\r\n"; 
        recibo += "T 0 0 0 " + y + " " + StringUtil.formatReal(pedido.getTotal()) + "\r\n";
        recibo += "LEFT\r\n";
        
        y += 15;
        recibo += "LINE 0 " + y + " 576 " + y + " 1\r\n";
        y += 10;
        recibo += "T 7 0 169 " + y + " Gracias por su pedido\r\n";
        y += 30;
        recibo += "T 7 0 119 " + y + " Panzyma. Al cuidado de la salud\r\n";
        recibo += "FORM\r\n";
        recibo += "PRINT\r\n";
        y += 50;
        
        String header = "! 0 200 200 " + y + " 1\r\n";
        header += "LABEL\r\n";
        header += "CONTRAST 0\r\n";
        header += "TONE 0\r\n";
        header += "SPEED 3\r\n";
        header += "PAGE-WIDTH 600\r\n";
        header += "BAR-SENSE\r\n";
        header += ";// PAGE 0000000006000460\r\n";
                        
        recibo = header + recibo;        
        try
        {
        	BluetoothComunication b=new BluetoothComunication();
        	b.sendData(recibo);
        	//b.closeBT();
//            ZebraPrint zp = new ZebraPrint();
//            zp.Print(recibo);
//            zp = null;
            //Dialog.alert("El comprobante fue enviado a la impresora.");
        }
        catch(Exception ioex)
        {
          //  Status.show("Error: " + ioex.getMessage());
        }
    }
	
    @SuppressWarnings("deprecation")
    public void save() { 
        try 
        {
        	//Salvando el tipo de pedido (crédito contado)
            pedido.setTipo("CR"); 
        	if (((tbxTipoVenta.getSelectedItemPosition() == 0) ? "CO" : "CR") == "CO")
				pedido.setTipo("CO");
           
			Date d = new Date(tbxFecha.getText().toString());
            pedido.setFecha(DateUtil.d2i(d));
            
            //Generar Id del pedido
            if (pedido.getNumeroMovil() == 0) 
            {            
                Integer intId = Ventas.getLastOrderId(me.getApplicationContext());
                if (intId == null) 
                    intId = new Integer(1);
                else
                    intId = new Integer(intId.intValue() + 1);
               // DataStore.setLastOrderId(intId);
                Integer prefix = Ventas.getPrefijoIds(me.getApplicationContext());
                String strIdMovil = prefix.intValue() + "" + intId.intValue();
                int idMovil = Integer.parseInt(strIdMovil);
                pedido.setNumeroMovil(idMovil);
                pedido.setObjEstadoID(0);
                pedido.setObjCausaEstadoID(0);
                pedido.setCodEstado("REGISTRADO");
                pedido.setDescEstado("Elaboración");
                pedido.setCodCausaEstado("REGISTRADO");
                pedido.setDescCausaEstado("Registrado");
            }
                       
//            if (indexPedido == -1) {
//                pedidos.addElement(pedido);
//                indexPedido = pedidos.size() - 1;
//            }
//            else {
//                pedidos.setElementAt(pedido, indexPedido);
//            }
        //    DataStore.setCachedOrders(pedidos); 
            tbxNumReferencia.setText(Ventas.getNumeroPedido(pedido.getNumeroMovil(),me.getApplicationContext()));
            salvado = true;         
        }
        catch(Exception ex) {
           // throw new IOException(ex.toString());
        }        
    }
	
}
