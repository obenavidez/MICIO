package com.panzyma.nm.viewdialog;
      
import java.util.ArrayList;
import java.util.HashMap;  
import java.util.List;
import java.util.Map;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BDevolucionM;
import com.panzyma.nm.auxiliar.NMNetWork; 
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.custom.model.SpinnerModel; 
import com.panzyma.nm.model.ModelPedido;
import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Pedido; 
import com.panzyma.nm.serviceproxy.ValorCatalogo;
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R; 

import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;  
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
	private boolean offline;
	private static Devolucion devolucion;
	
	public interface DialogListener 
	{ 
		public void onDialogPositiveClick(Devolucion dev,long nopedido,Pedido _pedido);
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
		if(NMNetWork.CheckConnection() && !UserSessionManager.HAS_ERROR) {
			offline = true;
			visible=View.GONE; 
		}			
		btnagregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{ 
				long nopedido = 0; 	
				devolucion.setOffLine(offline);
				if( offline ) {					
					nopedido = (long)((tboxPedido.getText()!=null && (!tboxPedido.getText().equals("")))?Long.valueOf(tboxPedido.getText().toString()):0);					
				} else {
					HashMap<String,Long> parametros = new HashMap<String,Long>();				
					Message m=new Message();
					m.what=ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO;
					parametros.put("idsucursal",objSucursalID);
					parametros.put("nopedido",(long)((tboxPedido.getText()!=null && (!tboxPedido.getText().equals("")))?Long.valueOf(tboxPedido.getText().toString()):0));
					parametros.put("nofactura",(long)((tboxFactura.getText()!=null && (!tboxFactura.getText().toString().equals("")))?Long.valueOf(tboxFactura.getText().toString()):0));
					m.obj=parametros;
					NMApp.getController().getInboxHandler().sendMessage(m);
				}
				listener.onDialogPositiveClick(devolucion, nopedido, null);
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
		
		/*com.panzyma.nm.NMApp.getController().setView(this);
		Message m=new Message();
		m.what=ControllerProtocol.LOAD_PEDIDOS_FROM_LOCALHOST; 
		Bundle data = new Bundle();
		data.putLong("objSucursalID", objSucursalID);
		m.setData(data);
		NMApp.getController().getInboxHandler().sendMessage(m);*/
		
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
   	
   	public void initComponent()
   	{   		 
		com.panzyma.nm.NMApp.getController().setView(this);
   	} 
   	
	@Override
	public boolean handleMessage(Message msg) { 
		switch (msg.what) 
		{
			case ControllerProtocol.ID_REQUEST_OBTENERPEDIDOS:				
				adapter_pedidos = new CustomAdapter(this.getActivity(),
						R.layout.spinner_rows, setListData((ArrayList<Factura>) msg.obj));
				cboxreciborec.setAdapter(adapter_pedidos);  
				break;
			case ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO:
				Devolucion dev=(msg.obj instanceof Devolucion)?(Devolucion)msg.obj:new Devolucion();
				
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
		Log.d(TAG, "Quitting"+ TAG); 
	}
	
	public class LoadDataToUI extends AsyncTask<Void, Void, List<Factura> > {
		
		@Override
		protected List<Factura> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return ModelPedido.obtenerPedidosFacturados(objSucursalID);
		}
		
		@Override
		protected void onPostExecute(List<Factura> facturas) {
			facturas.add(0, new Factura(-1,"",""));
			adapter_pedidos = new CustomAdapter($this.getActivity(),
					R.layout.spinner_rows, setListData(facturas));
			cboxreciborec.setAdapter(adapter_pedidos);  
		}
		
	}
	
	
	
}
