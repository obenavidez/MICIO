package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA; 
import static com.panzyma.nm.controller.ControllerProtocol.ERROR; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.panzyma.nm.DashBoardActivity;
import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage; 
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction; 
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.view.adapter.GenericAdapter; 
import com.panzyma.nm.view.viewholder.PProductoViewHolder;
import com.panzyma.nm.viewdialog.DialogCliente;
import com.panzyma.nm.viewdialog.DialogCliente.OnButtonClickListener; 
import com.panzyma.nm.viewdialog.DialogProducto;
import com.panzyma.nm.viewmodel.vmPProducto;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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
@SuppressWarnings({"unchecked","rawtypes","unused"})
public class ViewPedidoEdit extends Activity implements Handler.Callback
{ 
	public EditText tbxFecha;
	public EditText tbxNumReferencia;
	public EditText tbxNumRecibo;
	public EditText tbxNombreDelCliente;
	public TextView tbxPrecio;
	public Spinner tbxTipoVenta;
	public TextView tbxTotalFact;
	
	public View gridDetallePedido;	
	TextView gridheader;
	private Controller controller;
	private GenericAdapter adapter;   
	private ProgressDialog pd;
	private Button Menu; 
	private QuickAction quickAction ; 
	private int positioncache=-1;
	private Display display;
	private static final String TAG = ViewCliente.class.getSimpleName();
	private ViewPedidoEdit me;
	private Pedido pedido;
    private Cliente cliente;
    ArrayList<DetallePedido> Lvmpproducto;
    
    //Totales del pedido
    private float subTotal = 0;
    private float descuento = 0;
    private float impuesto = 0;
    private float total = 0;
    
    private NMApp nmapp;
    
	private static final int ID_SELECCIONAR_CLIENTE  = 1;
	private static final int ID_CONSULTAR_CUENTAS_X_COBRAR = 2;
	private static final int ID_AGREGAR_PRODUCTOS= 3;
	private static final int ID_EDITAR_PRODUCTO= 4;
	private static final int ID_ELIMINAR_PRODUCTO = 5;	
	private static final int ID_CONSULTAR_BONIFICACIONES  = 6;
	private static final int ID_CONSULTAR_LISTA_PRECIOS = 7;
	private static final int ID_CONDICIONES_Y_NOTAS= 8;
	private static final int ID_APLICAR_PROMOCIONES= 9; 
	private static final int ID_DESAPLICAR_PROMOCIONES= 10;	
	private static final int ID_EXONERAR_IMPUESTO = 11;
	private static final int ID_GUARDAR= 12;
	private static final int ID_ENVIAR= 13; 
	private static final int ID_IMPRIMIR_COMPROBANTE= 14;
	private static final int ID_CERRAR = 15; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.pedido_edit); 
		//setHeader(getString(R.string.PedidoActivityTitle),true, false); 
		
		try 
	    {
	    	me=this; 
	    	nmapp=(NMApp) this.getApplication(); 
	        nmapp.getController().setEntities(this,new BPedidoM());
	        nmapp.getController().addOutboxHandler(new Handler(this));   
	       // controller.getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);  	   
	        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
	        initComponent();
	        
		}catch (Exception e) { 
			e.printStackTrace();
			buildCustomDialog("Error Message",e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}	
		
	}

	public void initComponent()
	{ 	
		gridDetallePedido=findViewById(R.id.pddgrilla);
		Lvmpproducto=new ArrayList<DetallePedido>();
		//LinearLayout  grilla=(LinearLayout) findViewById(R.id.pddgrilla);
	    gridheader=(TextView)gridDetallePedido.findViewById(R.id.header);
	    gridheader.setText("Productos a Facturar(0)");
	    tbxFecha=(EditText) findViewById(R.id.pddetextv_detalle_fecha); 
	    tbxNumReferencia=(EditText) findViewById(R.id.pdddetextv_detalle_numref);
	    tbxNumRecibo=(EditText) findViewById(R.id.pdddetextv_detalle_num);
		tbxNombreDelCliente=(EditText) findViewById(R.id.pddtextv_detallecliente);
		tbxPrecio=(TextView) findViewById(R.id.pddtextv_detalleprecio); 
		tbxTotalFact=(TextView) findViewById(R.id.pddtextv_detalletotales); 
		tbxTipoVenta=(Spinner) findViewById(R.id.pddcombox_detalletipo);
		ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.tipopedido,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tbxTipoVenta.setAdapter(adapter);
		
		tbxTipoVenta.setSelection(1);
		tbxTipoVenta.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parentview, View selectedItemView,int pos, long id) { 
				pedido.setTipo(pos==0?"CO":"CR");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		if(pedido==null)
		{
			pedido = new Pedido();
            cliente = null;
            pedido.setCodEstado("REGISTRADO");
            pedido.setId(0);          
            pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));
            pedido.setNumeroCentral(0);
            pedido.setNumeroMovil(0);
          //  pedido.setObjVendedorID(DataStore.getUsuario().getId());
            pedido.setTipo("CR"); //Crédito
            pedido.setExento(false);
            pedido.setAutorizacionDGI("");
		} 
		
		//Fecha del Pedido
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        if (pedido.getFecha() == 0)   
            pedido.setFecha(DateUtil.d2i(Calendar.getInstance().getTime()));   
        
        if (pedido.getNumeroMovil() > 0)
        	tbxNumReferencia.setText(getNumeroPedido(pedido.getNumeroMovil()));
        
        if (pedido.getNumeroCentral() > 0)
        	tbxNumRecibo.setText(getNumeroPedido(pedido.getNumeroCentral()));
        
        if (pedido.getNombreCliente() != null)
        	tbxNombreDelCliente.setText(pedido.getNombreSucursal() + "\\" + pedido.getNombreCliente());
        
        if (pedido.getCodTipoPrecio() != null)
        	tbxPrecio.setText(pedido.getDescTipoPrecio());
        
        if (pedido.getTipo().compareTo("CO") == 0)
        	tbxTipoVenta.setSelection(0);
        else 
        	tbxTipoVenta.setSelection(1);
        
        String descTotales = "ST: " + StringUtil.formatReal(subTotal) + " | Desc: " + StringUtil.formatReal(descuento) + " | IVA: " + StringUtil.formatReal(impuesto) +
        "  |  Total: " + StringUtil.formatReal(total);
        
        
        SpannableString sb = new SpannableString(descTotales);
        final ForegroundColorSpan color = new ForegroundColorSpan(Color.rgb(255, 0, 0)); 

        // Span to set text color to some RGB value
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); 

        // Span to make text bold
        int start=0,start2=0,end=0,end2;        
        
        String st="ST:";     String desc="Desc:";      String iva="IVA:";        String total="Total:";
        
        start=descTotales.indexOf(st);        end=descTotales.indexOf(st)+ st.length();           start2=descTotales.indexOf(desc)-2;   
        sb.setSpan(new ForegroundColorSpan(Color.BLUE),start,end,0); 
        sb.setSpan(new ForegroundColorSpan(Color.rgb(163,46,84)),end,start2,0); 
        
        start=descTotales.indexOf(desc);      end=descTotales.indexOf(desc)+desc.length();        start2=descTotales.indexOf(iva)-2;
        sb.setSpan(new ForegroundColorSpan(Color.BLUE),start,end,0); 
        sb.setSpan(new ForegroundColorSpan(Color.rgb(163,46,84)),end,start2,0);
        
        start=descTotales.indexOf(iva);       end=descTotales.indexOf(iva)+ iva.length();         start2=descTotales.indexOf(total)-2;
        sb.setSpan(new ForegroundColorSpan(Color.BLUE),start,end,0); 
        sb.setSpan(new ForegroundColorSpan(Color.rgb(163,46,84)),end,start2,0);           
        
        start=descTotales.indexOf(total);     end=descTotales.indexOf(total)+total.length();   
        sb.setSpan(new ForegroundColorSpan(Color.BLUE),start,end,0);          
        sb.setSpan(new ForegroundColorSpan(Color.rgb(163,46,84)),end,descTotales.length(),0); 

        tbxTotalFact.setText(sb); 
		initMenu();		
		
	}
	
	public void initMenu()
    { 
       
		quickAction = new QuickAction(this, QuickAction.VERTICAL,1);	
	    quickAction.addActionItem(new ActionItem(ID_SELECCIONAR_CLIENTE, "Seleccionar Cliente"));
		quickAction.addActionItem(new ActionItem(ID_AGREGAR_PRODUCTOS, "Agregar Productos"));
		quickAction.addActionItem(new ActionItem(ID_CONDICIONES_Y_NOTAS, "Agregar Condición Y Nota"));		
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(ID_EDITAR_PRODUCTO, "Editar Producto"));
		quickAction.addActionItem(new ActionItem(ID_ELIMINAR_PRODUCTO, "Eliminar Productos"));		
        quickAction.addActionItem(null);		
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_CUENTAS_X_COBRAR, "Consultar Cuentas X Cobrar"));
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_BONIFICACIONES, "Consultar Bonificaciones"));
        quickAction.addActionItem(new ActionItem(ID_CONSULTAR_LISTA_PRECIOS, "Consultar Lista de Precios"));        
        quickAction.addActionItem(null);        
        quickAction.addActionItem(new ActionItem(ID_APLICAR_PROMOCIONES, "Aplicar Promociones"));
        quickAction.addActionItem(new ActionItem(ID_DESAPLICAR_PROMOCIONES, "Des Aplicar Promociones"));
        quickAction.addActionItem(new ActionItem(ID_EXONERAR_IMPUESTO, "Aplicar Exoneración de Impuesto"));        
        quickAction.addActionItem(null);        
        quickAction.addActionItem(new ActionItem(ID_GUARDAR, "Guardar"));
        quickAction.addActionItem(new ActionItem(ID_ENVIAR, "Enviar"));        
        quickAction.addActionItem(null);        
        quickAction.addActionItem(new ActionItem(ID_IMPRIMIR_COMPROBANTE, "Imprimir Comprobante")); 
        quickAction.addActionItem(null);
        quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));
         
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) 
			{				
				ActionItem actionItem = quickAction.getActionItem(pos);    
				if (actionId ==ID_SELECCIONAR_CLIENTE)
				{ 
					DialogCliente dc=new DialogCliente(me,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
					dc.setOnDialogClientButtonClickListener
					(new OnButtonClickListener()
						{

							@Override
							public void onButtonClick(Cliente _cliente) 
							{  
								cliente=_cliente;
								tbxNombreDelCliente.setText(cliente.getNombreCliente()); 
					            tbxPrecio.setText(cliente.getDesTipoPrecio());
					            pedido.setObjClienteID(cliente.getIdCliente());
					            pedido.setObjSucursalID(cliente.getIdSucursal());
					            
					            String[] nomClie = StringUtil.split(cliente.getNombreCliente(), "/");
					            pedido.setNombreCliente(nomClie[1]);
					            pedido.setNombreSucursal(nomClie[0]);
					            pedido.setObjTipoPrecioVentaID(cliente.getObjPrecioVentaID());
					            pedido.setCodTipoPrecio(cliente.getCodTipoPrecio());
					            pedido.setDescTipoPrecio(cliente.getDesTipoPrecio()); 
							}
						}
					); 
					Window window = dc.getWindow(); 
				    window.setGravity(Gravity.CENTER); 
				    window.setLayout(display.getWidth()-40,display.getHeight()-110);  
					dc.show();
				}	
				else if (actionId == ID_AGREGAR_PRODUCTOS)
				{					
					DialogProducto dp = new DialogProducto(me,pedido.getCodTipoPrecio(), null, pedido.getId(), cliente.getObjCategoriaClienteID(), pedido.getObjTipoPrecioVentaID(), cliente.getObjTipoClienteID(), pedido.isExento());
					
					//DialogProducto dp=new DialogProducto(me,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
					dp.setOnDialogProductButtonClickListener
					(  
					    new DialogProducto.OnButtonClickListener() 
					    {

							@Override
							public void onButtonClick(DetallePedido det_p) {
								// TODO Auto-generated method stub
								det_p.setId(pedido.getId());
								Lvmpproducto.add(det_p);
								addProductToOrder();
								CalculaTotales();
								initComponent();
							}						
							 
					    }
					);
					Window window = dp.getWindow(); 
				    window.setGravity(Gravity.CENTER); 
				    window.setLayout(display.getWidth()-40,display.getHeight()-110);  
					dp.show();
					
				}				
				else if (actionId == ID_AGREGAR_PRODUCTOS)
				{
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
				else if (actionId == ID_CERRAR) 
					FINISH_ACTIVITY();
				else  
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				 
			}
		});
		
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() 
		{			
			@Override
			public void onDismiss() { 
				quickAction.dismiss();
			}
		});
    	
    	
    }    
	
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_MENU) 
        {        	
        	Menu = (Button)gridDetallePedido.findViewById(R.id.btnmenu);
        	quickAction.show(Menu,display,true);
            return true;
        }
        return super.onKeyUp(keyCode, event); 
    } 
	
	@Override
	public boolean handleMessage(Message msg) {


		switch (msg.what) 
		{		 
			case C_DATA:   
				
					setData((ArrayList<Pedido>)((msg.obj==null)?new ArrayList<Pedido>():msg.obj),C_DATA);
					return true;
 		
			case ERROR:		
				
					pd.dismiss();
					ErrorMessage error=((ErrorMessage)msg.obj);
					buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();				 
					return true;						
		}
		
		
		return false;
	}
	
	public void setData(ArrayList<Pedido> Lpedido, int what)
	{
		
	}
	
	public void addProductToOrder(){
		 
		//gridheader.setText("Listado de Productos a Vender");
		if(adapter==null)
		{
			adapter=new GenericAdapter(this,PProductoViewHolder.class,Lvmpproducto,R.layout.gridproductosavender);				 
			((ListView)gridDetallePedido.findViewById(R.id.data_items)).setAdapter(adapter);
			gridheader.setText("Productos a Facturar("+adapter.getCount()+")");
		}
		else
		{ 
			adapter.notifyDataSetChanged();
			gridheader.setText("Productos a Facturar("+adapter.getCount()+")");
		}
	}
 
	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		return new CustomDialog(this.getApplicationContext(),tittle,msg,false,type);		 		 
	}
	
	private void FINISH_ACTIVITY()
	{
		nmapp.getController().removeOutboxHandler(TAG);
		Log.d(TAG, "Activity quitting");
		finish();		
	}  

	 @SuppressLint("NewApi")
	private void CalculaTotales() 
	 {
	        subTotal = 0;
	        descuento = 0;
	        impuesto = 0;
	        total = 0;
	        DetallePedido[] detsPedido = new DetallePedido[Lvmpproducto.size()];
	        for(int i=0; i < Lvmpproducto.size(); i++) {
	            DetallePedido dp = Lvmpproducto.get(i);
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
	        
	        //Salvando el tipo de pedido (crédito contado)
	        if (tbxTipoVenta != null) {
	            pedido.setTipo("CR");
	            if (((tbxTipoVenta.getSelectedItemPosition()==0)?"CO":"CR") == "CO")
	                pedido.setTipo("CO");
	        }
	        
	        if (tbxFecha != null) {   
	            pedido.setFecha(DateUtil.d2i(new Date()));  
	        } 
	        
	    }
	 public  String getNumeroPedido(int numero) {  
	        int cr = Integer.parseInt(me.getApplicationContext().getSharedPreferences("SystemParams", android.content.Context.MODE_PRIVATE).getString("CerosRellenoNumRefPedido","0"));
	        
	        char[] num = new char[cr];
	        for(int i=0; i < cr; i++) num[i] = '0';
	        
	        String strNum = new String(num);
	        strNum = strNum + numero;
	        return strNum.substring(strNum.length() - cr, strNum.length());
	    }
}
