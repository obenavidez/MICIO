package com.panzyma.nm.viewdialog;
      
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.custom.model.SpinnerModel; 
import com.panzyma.nm.model.ModelPedido;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Pedido; 
import com.panzyma.nm.view.ViewDevolucionEdit;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R; 

import android.app.AlertDialog;
import android.app.Dialog; 
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;  
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView; 
import android.widget.AdapterView.OnItemSelectedListener; 
@InvokeBridge(bridgeName = "BDevolucionM")
public class DevolverDocumento extends DialogFragment implements Handler.Callback
{	
	private static final String TAG = DevolverDocumento.class.getSimpleName();
	private DialogListener listener;
	private Button btnagregar;
	private Button btncancelar;
	private static Handler.Callback parent;
	private static DevolverDocumento dd;
	protected EditText tboxPedido;
	private Spinner cboxreciborec;
	private TextView lblreciborec;
	protected EditText tboxFactura;
	private static long objSucursalID;
	private Pedido pedido;
	private ArrayList<Pedido> pedidos;	
	private DevolverDocumento $this;
	private static boolean offline=false;
	private static Devolucion devolucion;
	
	private ProgressDialog pdialog=null;
	
	private int numpedido;
	private int numfactura;
	
	public interface DialogListener 
	{ 
		public abstract void onDialogPositiveClick(Devolucion dev); 
	} 
 
	public static DevolverDocumento newInstance(Handler.Callback _parent,long _objSucursalID,Devolucion dev) 
	{
		if(dd==null)
			dd = new DevolverDocumento(); 
		parent=_parent;
		objSucursalID=_objSucursalID;
		devolucion = dev; 
	    return dd;
	}
	
	public static DevolverDocumento newInstance(Handler.Callback _parent,long _objSucursalID,Devolucion dev,boolean _offline ) 
	{
		if(dd==null)
			dd = new DevolverDocumento(); 
		parent=_parent;
		objSucursalID=_objSucursalID;
		devolucion = dev; 
		offline=_offline;
	    return dd;
	}

	public void setOnDialogClickListener(DialogListener _listener)
	{
		listener = _listener;
	} 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		int visible=View.VISIBLE;
		$this = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.devolver_documento, null);
		tboxPedido=(EditText) view.findViewById(R.id.dev_tboxPedido);
		tboxFactura=(EditText) view.findViewById(R.id.dev_tboxFactura);
		TableRow row=(TableRow) view.findViewById(R.id.devlayoutbutton);
		cboxreciborec=(Spinner) view.findViewById(R.id.devcombox_pedidorec);
		lblreciborec=(TextView) view.findViewById(R.id.dev_lblPedidoRec);
		
		builder.setView(view); 
		btnagregar=(Button) row.findViewById(R.id.btnOK);
		btncancelar=(Button) row.findViewById(R.id.btnCancel);
		visible=View.GONE; 
		
		((ViewDevolucionEdit)parent).dismiss();
		
		if(offline) 
		{
			offline = true;
			visible=View.VISIBLE;  
		}			
		btnagregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{ 	
				try {
					
					devolucion.setOffLine(offline);
					String numeroFactura = (tboxFactura.getText() != null ? tboxFactura
							.getText().toString() : "");
					if (!"".equals(numeroFactura.trim())) {
						if(numeroFactura.indexOf("-") != -1) 
						{
							String parts[] = numeroFactura.split("-"); 
							numeroFactura = parts[parts.length-1];
						} 
					} else {
						numeroFactura = "0";
					}				
					numfactura = Integer.valueOf(numeroFactura);
					numpedido = (tboxPedido.getText()!=null && (!tboxPedido.getText().toString().equals("")))?Integer.valueOf(tboxPedido.getText().toString()):0;
					
					if( offline ) {
						if(!validar())
							return;
						
						devolucion.setNumeroFacturaDevuelta(Integer.parseInt(""+numfactura));
						devolucion.setNumeroPedidoDevuelto(Integer.parseInt(""+numpedido));
						listener.onDialogPositiveClick(devolucion); 
						dismiss();
					} else 
					{
						if(!validar())
							return;
						Parametro  parametros = new Parametro(objSucursalID,numpedido,numfactura);				
						Message m=new Message();
						m.what=ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO; 
						m.obj=parametros;
						NMApp.getController().getInboxHandler().sendMessage(m); 
						pdialog=ProgressDialog.show(getActivity(), "Buscando productos lotes con pedido/factura "+ numpedido+"/"+numfactura,"buscando en el servidor central !!!");
					}
					
				} catch (Exception e) {
					AppDialog.showMessage($this.getActivity(), e.getCause().toString(),
							e.getMessage().toString(),
							DialogType.DIALOGO_ALERTA);
				}
				
			}
		});
		
		btncancelar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{ 
				dismiss();
			}
		});		
		
		builder.setOnKeyListener(keyListener); 
		Dialog mydialog=builder.create(); 
		cboxreciborec.setVisibility(visible);
		lblreciborec.setVisibility(visible);
		
		cboxreciborec.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (position == 0)
					return;
				Factura _factura = (Factura) adapter_pedidos.getItem(position).getObj();
				tboxPedido.setText(_factura.getNoPedido());
				tboxFactura.setText(_factura.getNoFactura());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}});
		
		initComponent();
		if(offline)
			new LoadDataToUI().execute();
		
		return mydialog;
	}
	
    OnKeyListener keyListener = new OnKeyListener() 
   	{ 
   		  @Override
   		  public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) 
   		  {
   			if (keyCode == KeyEvent.KEYCODE_BACK) 
   			{        	
   			  	dismiss();
   			    return true;
   			}		  
   			return false;	 
   		  } 
   	};
	private CustomAdapter adapter_pedidos;
	protected CustomDialog dlg;
   	
	public boolean validar() {

		if ((tboxPedido.getText().toString().trim().length() == 0
				|| "0".equals(tboxPedido.getText().toString())
				|| "".equals(tboxPedido.getText().toString()))
				&&
				(tboxFactura.getText().toString().trim().length() == 0
				|| "0".equals(tboxFactura.getText().toString())
				|| "".equals(tboxFactura.getText().toString()))) 
		{
			tboxPedido.setError("Debe ingresar el n�mero del pedido o factura.");
			tboxPedido.requestFocus();
			return false;
		}  
		return true;
	}
	
	public boolean validar(int nopedido,int nofactura) {

		if ((tboxPedido.getText().toString().trim().length() == 0
				|| "0".equals(tboxPedido.getText().toString()))
				&&
				(tboxFactura.getText().toString().trim().length() == 0
				|| "0".equals(tboxFactura.getText().toString()))) 
		{
			tboxPedido.setError("Debe ingresar el n�mero del pedido o factura.");
			tboxPedido.requestFocus();
			return false;
		}  
		return true;
	}
	
   	public void initComponent()
   	{   		 
		com.panzyma.nm.NMApp.getController().setView(this);
		SessionManager.setContext(this.getActivity());
		UserSessionManager.setContext(this.getActivity());
		com.panzyma.nm.NMApp.getController().setView(this);
   	} 
   	
	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) 
	{
		if(pdialog!=null)
			pdialog.dismiss();
		switch (msg.what) 
		{
			case ControllerProtocol.ID_REQUEST_OBTENERPEDIDOS:				
				adapter_pedidos = new CustomAdapter(this.getActivity(),
						R.layout.spinner_rows, setListData((ArrayList<Factura>) msg.obj));
				cboxreciborec.setAdapter(adapter_pedidos);  
				break;
			case ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO:
				Devolucion dev=(msg.obj instanceof Devolucion)?(Devolucion)msg.obj:new Devolucion();
				if(dev!=null && dev.getProductosDevueltos()!=null && dev.getProductosDevueltos().length!=0)
				{
					listener.onDialogPositiveClick(dev); 
					dismiss();
				}else
					showStatus("El pedido/factura "+ numpedido+"/"+numfactura+" no se encontro en el servidor central...",true);
				
				break;
			case ControllerProtocol.NOTIFICATION:
				
				String message = "";
				if (msg.obj != null && msg.obj instanceof Devolucion) {
					devolucion = (Devolucion) msg.obj;
				} else if (msg.obj != null && msg.obj instanceof String)
					message = msg.obj.toString();	 
				if (!message.equals(""))				
					showStatus(message, true);
				break;
			case ControllerProtocol.ERROR:
				AppDialog.showMessage(this.getActivity(), ((ErrorMessage) msg.obj).getTittle(),
						((ErrorMessage) msg.obj).getMessage(),
						DialogType.DIALOGO_ALERTA);
				break;
		}
		return false;
	} 

	@Override
	public void onDismiss(DialogInterface dialog) 
   	{   		
		
   		FINISH_ACTIVITY();
		super.onDismiss(dialog);
	}
	
	private ArrayList<SpinnerModel> setListData(List<Factura> pedidos) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>(); 
		for (Factura p : pedidos) {

			final SpinnerModel sched = new SpinnerModel();

			/******* Firstly take data in model object ******/
			sched.setId(p.getId());
			sched.setCodigo(""+p.getNoPedido());
			sched.setDescripcion(p.getNoFactura()); 
			sched.setObj(p);

			/******** Take Model Object in ArrayList **********/
			CustomListViewValuesArr.add(sched);
		}
		return CustomListViewValuesArr;
	}
	
	private void FINISH_ACTIVITY()
	{
		com.panzyma.nm.NMApp.getController().setView(parent);	 
		SessionManager.setContext(this.getActivity());
		UserSessionManager.setContext(this.getActivity()); 
		Log.d(TAG, "Quitting"+ TAG); 
	}
	
	public class LoadDataToUI extends AsyncTask<Void, Void, List<Factura> > 
	{
		
		@Override
		protected List<Factura> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return ModelPedido.obtenerPedidosFacturados(objSucursalID);
		}
		
		@Override
		protected void onPostExecute(List<Factura> facturas) 
		{
			if(adapter_pedidos != null)
				adapter_pedidos.getData().clear();			
			facturas.add(0, new Factura(-1,"",""));			
			adapter_pedidos = new CustomAdapter($this.getActivity(),
					R.layout.spinner_rows, setListData(facturas));
			cboxreciborec.setAdapter(adapter_pedidos);  
		}
		
	}
	
	@SuppressWarnings("unused")
	private void productoloteDevolucion(List<DevolucionProducto> dp) 
	{ 
		FragmentTransaction ft =((ViewDevolucionEdit) parent).getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = ((ViewDevolucionEdit) parent).getSupportFragmentManager().findFragmentByTag("dialogPL");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		ProductoDevolucion newFragment =ProductoDevolucion.newInstance(parent,dp);  
		newFragment.show(ft, "dialogPL");
		
	}
	
	public void showStatus(final String mensaje, boolean... confirmacion) {

		if (confirmacion.length != 0 && confirmacion[0]) {
			((ViewDevolucionEdit)parent).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(((ViewDevolucionEdit)parent), "", mensaje,
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
			((ViewDevolucionEdit)parent).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dlg = new CustomDialog(((ViewDevolucionEdit)parent), mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}
	
	public class Parametro
	{
		Parametro(){}
		/**
		 * @return the sucursalid
		 */
		public long getSucursalid() {
			return sucursalid;
		}
		/**
		 * @param sucursalid the sucursalid to set
		 */
		public void setSucursalid(long sucursalid) {
			this.sucursalid = sucursalid;
		}
		/**
		 * @return the pedidoid
		 */
		public int getPedidoid() {
			return pedidoid;
		}
		/**
		 * @param pedidoid the pedidoid to set
		 */
		public void setPedidoid(int pedidoid) {
			this.pedidoid = pedidoid;
		}
		/**
		 * @return the facturaid
		 */
		public int getFacturaid() {
			return facturaid;
		}
		/**
		 * @param facturaid the facturaid to set
		 */
		public void setFacturaid(int facturaid) {
			this.facturaid = facturaid;
		}
		public Parametro(long sucursalid, int pedidoid, int facturaid) 
		{ 
			this.sucursalid = sucursalid;
			this.pedidoid = pedidoid;
			this.facturaid = facturaid;
		}
		long sucursalid;
		int  pedidoid;
		int  facturaid;
	}
}