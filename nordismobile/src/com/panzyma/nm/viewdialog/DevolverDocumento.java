package com.panzyma.nm.viewdialog;
      
import java.util.ArrayList;
import java.util.HashMap;  

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMNetWork; 
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.custom.model.SpinnerModel; 
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.Pedido; 
import com.panzyma.nm.view.adapter.CustomAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R; 

import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;  
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
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
	
	public interface DialogListener 
	{ 
		public void onDialogPositiveClick(Devolucion dev,long nopedido,Pedido _pedido);
	} 
 
	public static DevolverDocumento newInstance(Handler.Callback _parent,long _objSucursalID) 
	{
		if(dd==null)
			dd = new DevolverDocumento(); 
		parent=_parent;
		objSucursalID=_objSucursalID;
		
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
		if(NMNetWork.CheckConnection()) 
			visible=View.GONE; 
		btnagregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{ 
				HashMap<String,Long> parametros = new HashMap<String,Long>();				
				Message m=new Message();
				m.what=ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO;
				parametros.put("idsucursal",objSucursalID);
				parametros.put("nopedido",(long)((tboxPedido.getText()!=null && (!tboxPedido.getText().equals("")))?Long.valueOf(tboxPedido.getText().toString()):0));
				parametros.put("nofactura",(long)((tboxFactura.getText()!=null && (!tboxFactura.getText().toString().equals("")))?Long.valueOf(tboxFactura.getText().toString()):0));
				m.obj=parametros;
				NMApp.getController().getInboxHandler().sendMessage(m);
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
		 
		com.panzyma.nm.NMApp.getController().setView(this);
		Message m=new Message();
		m.what=ControllerProtocol.LOAD_PEDIDOS_FROM_LOCALHOST; 
		NMApp.getController().getInboxHandler().sendMessage(m);
		
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
						R.layout.spinner_rows, setListData(pedidos=(ArrayList<Pedido>) msg.obj));
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
	
	private ArrayList<SpinnerModel> setListData(ArrayList<Pedido> pedidos) {
		ArrayList<SpinnerModel> CustomListViewValuesArr = new ArrayList<SpinnerModel>(); 
		for (Pedido p : pedidos) {

			final SpinnerModel sched = new SpinnerModel();

			/******* Firstly take data in model object ******/
			sched.setId(p.getId());
			sched.setCodigo(""+p.getNumeroCentral());
			sched.setDescripcion(p.getNombreSucursal()); 
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
}
