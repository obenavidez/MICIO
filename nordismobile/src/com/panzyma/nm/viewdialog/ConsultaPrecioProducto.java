package com.panzyma.nm.viewdialog;

import java.util.ArrayList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.PrecioProducto;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.view.ProductoView;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.DetallePrecioProductoViewHolder;
import com.panzyma.nordismobile.R;

@SuppressWarnings({ "unchecked", "rawtypes" })
@SuppressLint("ValidFragment")
public class ConsultaPrecioProducto extends DialogFragment implements Handler.Callback{
	
	private View view;	
	private EditText nombre_prod;
	private ListView lvprecios;
	private long idProducto;
	private long idTipoPrecio;
	private GenericAdapter adapter;
	ArrayList<PrecioProducto> lprecioproducto;	
	private int positioncache;
	private NMApp nmapp;
	public static String TAG=ConsultaPrecioProducto.class.getSimpleName();
	ProgressDialog pd;
	private ViewPedidoEdit parent;
	private ProductoView parent2;
	private static ConsultaPrecioProducto cpp=new ConsultaPrecioProducto();
	public static ConsultaPrecioProducto newInstance(long idProducto,long idTipoPrecio) 
	{
		if(cpp==null)
			cpp = new ConsultaPrecioProducto(); 
	    Bundle args = new Bundle();
	    args.putLong("_idProducto", idProducto);
	    args.putLong("_idTipoPrecio", idTipoPrecio);
	    cpp.setArguments(args);
	    return cpp;
	}	 
    
    @SuppressLint("InflateParams") 
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	AlertDialog.Builder builder=null;
		LayoutInflater inflater =null;
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
		view = inflater.inflate(R.layout.layout_consultapreciosprod, null);
		lvprecios=(ListView) view.findViewById(R.id.bnflv_detalleprecios);
		nombre_prod=(EditText) view.findViewById(R.id.etProducto);
		builder.setTitle("Consulta Precios Producto");
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(pd!=null)
					pd.dismiss();
				dismiss();
			}
		});
		builder.setOnKeyListener(keyListener);
		mandar_A_TraerDatos();
        return builder.create();
    	/*
    	parent=(ViewPedidoEdit) this.getActivity();
    	AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
    	LayoutInflater inflater = parent.getLayoutInflater();
		view = inflater.inflate(R.layout.layout_consultapreciosprod, null);
		lvprecios=(ListView) view.findViewById(R.id.bnflv_detalleprecios);
		nombre_prod=(EditText) view.findViewById(R.id.etProducto);
		builder.setTitle("Consulta Precios Producto");
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});
		
		
		builder.setOnKeyListener(keyListener);
		mandar_A_TraerDatos();
        return builder.create();   	*/
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
	public boolean handleMessage(Message msg) 
	{ 
		switch (msg.what) 
		{
			case ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST:
					establecerListaDePrecios((msg.obj != null) ?(Producto)msg.obj:new Producto());
					return true;
		}
		return false;
	}
	
	public void  mandar_A_TraerDatos() 
    {
		try 
		{
			idProducto = getArguments().getLong("_idProducto"); 
	    	idTipoPrecio = getArguments().getLong("_idTipoPrecio");
 
			NMApp.getController().setEntities(this,new BProductoM());
			NMApp.getController().addOutboxHandler(new Handler(this));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putLong("_idProducto",idProducto); 
			msg.setData(b);
			msg.what=ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
			NMApp.getController().getInboxHandler().sendMessage(msg);    
			//pd = ProgressDialog.show(this.getActivity(), "Espere por favor", "Cargando Información", true, false);			

			/*Message msg = new Message();
	    	Bundle b = new Bundle();
	    	
	    	b.putLong("_idProducto",idProducto); 
			msg.setData(b);
			msg.what=ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;
			
			NMApp.controller.removeBridgeByName(BProductoM.class.toString());
			NMApp.controller.setEntities(this,new BProductoM());
			NMApp.controller.addOutboxHandler(new Handler(this));
			NMApp.controller.getInboxHandler().sendMessage(msg); 
			
	    	
	    	nmapp=(NMApp)this.getActivity().getApplicationContext();
			NMApp.getController().setEntities(this,new BProductoM());
			NMApp.getController().addOutboxHandler(new Handler(this));
			
			
			
			NMApp.getController().getInboxHandler().sendMessage(msg);
			pd = ProgressDialog.show(this.getActivity(), "Espere por favor", "Cargando Información", true, false);*/
			
			//pd = ProgressDialog.show(parent!=null ? parent : parent2  , "Espere por favor", "Cargando Información", true, false);

		} catch (Exception e) {
			// TODO: handle exception
		}
		
    }
	
	public void establecerListaDePrecios(Producto producto)
	{		
		nombre_prod.setText(producto.getCodigo()+"-"+producto.getNombre());
		lprecioproducto= new ArrayList<PrecioProducto>();		
		lprecioproducto.addAll(Ventas.parseListaPrecio(producto, idTipoPrecio));				
		adapter = new GenericAdapter(getActivity(),DetallePrecioProductoViewHolder.class,lprecioproducto, R.layout.detalle_precio);  
		lvprecios.setAdapter(adapter);         
		lvprecios.setOnItemClickListener(new OnItemClickListener() 
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
		//pd.dismiss();
	} 

	private void FINISH_ACTIVITY()
	{
		if(pd!=null)
			pd.dismiss();
		try 
		{
			NMApp.getController().removeOutboxHandler(TAG);
		    NMApp.getController().removebridge(NMApp.getController().getBridge());
		    NMApp.getController().disposeEntities();		

		    NMApp.getController().setEntities((parent),parent.getBridge());

		    //NMApp.getController().setEntities((parent),parent.getBridge());
		    if(this.getActivity() instanceof ViewPedidoEdit){
		    	NMApp.getController().setEntities((parent),parent.getBridge());	
		    }
		    if(this.getActivity() instanceof ProductoView){
		    	NMApp.getController().setEntities((parent2),parent2.getBridge());	
		    }
		    
		} catch (Exception e) 
		{ 
			e.printStackTrace();
		}
			
		Log.d(TAG, "Activity quitting");  
		dismiss();
	}

	public ViewPedidoEdit getParent(){
		return parent;
	}
	
	public ProductoView getListParent(){
		return parent2;
	}
}
