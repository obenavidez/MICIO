package com.panzyma.nm.viewdialog;

import java.util.ArrayList; 

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings({ "unchecked", "rawtypes" })
@SuppressLint("ValidFragment")
public class ConsultaBonificacionesProducto extends DialogFragment implements Handler.Callback{
	 
	private View view;	
	private EditText nombre_prod;
	private ListView lvpromociones;
	private long idProducto;
	private long idCatCliente;
	private GenericAdapter adapter;
	ArrayList<Bonificacion> lBonificacion;	
	private int positioncache;
	private NMApp nmapp;
	public static String TAG= ConsultaBonificacionesProducto.class.getSimpleName();
	private ProgressDialog pd;
	private ViewPedidoEdit parent;
	
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

		parent=(ViewPedidoEdit) this.getActivity();
    	AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
    	LayoutInflater inflater = parent.getLayoutInflater();
		view = inflater.inflate(R.layout.layout_consultabonificacionesprod, null);
		builder.setTitle("Consulta de Bonificaciones");
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		}); 
		builder.setOnKeyListener(keyListener); 
		mandar_A_TraerDatos(); 
        return builder.create();
    }
    
    OnKeyListener keyListener = new OnKeyListener() 
   	{ 
   		  @Override
   		  public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) 
   		  {
   			if (keyCode == KeyEvent.KEYCODE_BACK) 
   			{        	
   			  	FINISH_ACTIVITY();
   			    return true;
   			}		  
   			return false;	 
   		  } 
   	};
	
    
   	@Override
	public void onDismiss(DialogInterface dialog) {
   		FINISH_ACTIVITY();
		super.onDismiss(dialog);
	}
   	
    @Override
	public boolean handleMessage(Message msg) {
    	switch (msg.what) 
		{
			case ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST:
					establecerBonificacionesProducto((msg.obj != null) ?(Producto)msg.obj:new Producto());
					return true;
		}
		return false;
	}
    
    public void  mandar_A_TraerDatos() 
    {
    	idProducto = getArguments().getLong("_idProducto"); 
        idCatCliente = getArguments().getLong("_idCatCliente"); 
        
    	nmapp=(NMApp)parent.getApplicationContext();
		try {
			nmapp.getController().setEntities(this,new BProductoM());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nmapp.getController().addOutboxHandler(new Handler(this));
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putLong("_idProducto",idProducto); 
		msg.setData(b);
		msg.what=ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
		nmapp.getController().getInboxHandler().sendMessage(msg);  
		pd = ProgressDialog.show(parent, "Espere por favor", "Cargando Información", true, false);
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
		try 
		{
			nmapp.getController().removeOutboxHandler(TAG);
		    nmapp.getController().removebridge(nmapp.getController().getBridge());
		    nmapp.getController().disposeEntities();		
		    nmapp.getController().setEntities(((ViewPedidoEdit)parent),((ViewPedidoEdit)parent).getBridge());
		    
		} catch (Exception e) 
		{ 
			e.printStackTrace();
		}
		if(pd!=null)
			pd.dismiss();	
		Log.d(TAG, "Activity quitting"); 
		dismiss();
	}
	
	public ViewPedidoEdit getParent(){
		return parent;
	}
}
