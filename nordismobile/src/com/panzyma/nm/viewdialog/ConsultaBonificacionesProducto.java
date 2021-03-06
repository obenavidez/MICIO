package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;

import java.util.ArrayList; 

import com.panzyma.nm.NMApp; 
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.ProductoView;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.DetallePromocionesViewHolder;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle; 
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings({ "unchecked", "rawtypes" })
@SuppressLint("ValidFragment")
@InvokeBridge(bridgeName = "BProductoM")
public class ConsultaBonificacionesProducto extends DialogFragment implements Handler.Callback{
	 
	private View view;	
	private EditText nombre_prod;
	private ListView lvpromociones;
	private long idProducto;
	private long idCatCliente;
	private GenericAdapter adapter;
	ArrayList<Bonificacion> lBonificacion;	
	private int positioncache; 
	public static String TAG= ConsultaBonificacionesProducto.class.getSimpleName();
	private ProgressDialog pd;private static CustomDialog dlg;
	private ViewPedidoEdit parent;
	private ProductoView parent2;
	
	public static ConsultaBonificacionesProducto newInstance(long idProducto,long _idCatCliente) {
		ConsultaBonificacionesProducto frag = new ConsultaBonificacionesProducto();
		Bundle args = new Bundle(); 
	    args.putLong("_idProducto", idProducto);
	    args.putLong("_idCatCliente", _idCatCliente);
	    frag.setArguments(args);
	    return frag;
    } 
	 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder=null;
		LayoutInflater inflater =null;
		NMApp.getController().setView(this); 
		if(this.getActivity() instanceof ViewPedidoEdit){
			parent=(ViewPedidoEdit) this.getActivity();
			builder = new AlertDialog.Builder(parent); 
			inflater = parent.getLayoutInflater();
		}
		
		if(this.getActivity() instanceof ProductoView){
			parent2=(ProductoView) this.getActivity();
			builder = new AlertDialog.Builder(parent2); 
			inflater = parent2.getLayoutInflater();
		}
		
		//parent=(ViewPedidoEdit) this.getActivity();
    	//AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
    	//LayoutInflater inflater = parent.getLayoutInflater();
		view = inflater.inflate(R.layout.layout_consultabonificacionesprod, null);
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		}); 
		builder.setOnKeyListener(keyListener); 
		mandar_A_TraerDatos(); 
		Dialog dialog = builder.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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
	
    
   	@Override
	public void onDismiss(DialogInterface dialog) 
   	{   		
   		FINISH_ACTIVITY();
		super.onDismiss(dialog);
	}
   	
    @Override
	public boolean handleMessage(Message msg) {
    	ocultarDialogos();
    	switch (msg.what) 
		{
			case ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST:
					establecerBonificacionesProducto((msg.obj != null) ?(Producto)msg.obj:new Producto());
					return true;
					
			case ControllerProtocol.NOTIFICATION:
				showStatus(msg.obj.toString(), true);
				break;
			case ControllerProtocol.NOTIFICATION_DIALOG2:
				showStatus(msg.obj.toString());
				break;
			case ControllerProtocol.ERROR:
				AppDialog.showMessage(getActivity(), ((ErrorMessage) msg.obj).getTittle(),
						((ErrorMessage) msg.obj).getMessage(),
						DialogType.DIALOGO_ALERTA);
				break;
		}
		return false;
	}
    
    public void ocultarDialogos() {
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
		if (pd != null && pd.isShowing())
			pd.dismiss();
	}
    
    public void showStatus(final String mensaje, boolean... confirmacion) {

		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(getActivity(), "", mensaje,
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
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dlg = new CustomDialog(getActivity(), mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}
    
    public void  mandar_A_TraerDatos() 
    {
    	idProducto = getArguments().getLong("_idProducto"); 
        idCatCliente = getArguments().getLong("_idCatCliente"); 
        Message msg = new Message();
		Bundle b = new Bundle();
		b.putLong("idProducto",idProducto); 
		msg.setData(b);
		msg.what=ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
    	NMApp.controller.getInboxHandler().sendMessage(msg);   
		pd = ProgressDialog.show(parent!=null ? parent : parent2  , "Espere por favor", "Cargando Información", true, false);

    }

    public void establecerBonificacionesProducto(Producto producto)
	{
    	lvpromociones=(ListView) view.findViewById(R.id.bnflv_bonificacion);
		nombre_prod=(EditText) view.findViewById(R.id.etProducto);
    	nombre_prod.setText(producto.getCodigo()+"-"+producto.getNombre());
		lBonificacion= new ArrayList<Bonificacion>();
		lBonificacion.addAll(Ventas.parseListaBonificacion(producto, idCatCliente));				
		adapter = new GenericAdapter(getActivity(),DetallePromocionesViewHolder.class,lBonificacion, R.layout.detalle_bonificacion);  
		lvpromociones.setAdapter(adapter);         
        lvpromociones.setOnItemClickListener(new OnItemClickListener() 
        {
			@SuppressWarnings("deprecation")
			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
            { 		  
            	if((parent.getChildAt(positioncache))!=null)						            							            		
            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
            	positioncache=position;				            	 			
            	adapter.setSelectedPosition(position); 
            	view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_item_selected));					            	 
            	
            }
        }); 	
        pd.dismiss();
	}

	private void FINISH_ACTIVITY()
	{
		com.panzyma.nm.NMApp.getController().setView((parent==null)?parent2:parent);
		if(pd!=null)
			pd.dismiss();	
		Log.d(TAG, "Quitting"+ TAG); 
	}
	
	public ViewPedidoEdit getParent(){
		return parent;
	}
	public ProductoView getListParent(){
		return parent2;
	}
	
}
