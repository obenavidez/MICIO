package com.panzyma.nm.viewdialog;
 
import java.util.List; 
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.SolicitudDescuentoViewHolder;
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle; 
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View; 
import android.widget.ListView; 

public class ProductoDevolucion  extends DialogFragment 
{
	DevolverDocumento parent;
	public static ProductoDevolucion pd;
	List<DevolucionProducto> dev_prod;
	
	private ListView lvdevproducto;
	

	private GenericAdapter adapter;
	
	public static ProductoDevolucion newInstance(DevolverDocumento _parent,List<DevolucionProducto> dev_prod) 
	{
		if(pd==null)
			pd = new ProductoDevolucion(_parent,dev_prod);  		
	    return pd;
	}
	
	public ProductoDevolucion(){}; 
	
	public ProductoDevolucion(DevolverDocumento _parent,List<DevolucionProducto> _dev_prod)
	{
		parent=_parent;
		dev_prod=_dev_prod;
	}; 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		int visible=View.VISIBLE;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.productodevolucion, null);
		lvdevproducto=(ListView) view.findViewById(R.id.pl_lvproductolote); 		
		builder.setView(view);   
		return builder.create();
	}
	
	public void initComponent()
	{
		if(adapter==null)
		{
	    	adapter = new GenericAdapter(parent.getActivity(), SolicitudDescuentoViewHolder.class,dev_prod,  R.layout.detalle_productolote);
	    	lvdevproducto.setAdapter(adapter);
	    }
	    else
	    	adapter.notifyDataSetChanged();
		
	}
	
}
