package com.panzyma.nm.view.viewholder;

import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.view.adapter.ExpandListChild;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

import android.widget.TextView;

public class ProductoLoteDetalleViewHolder 
{

	@InvokeView(viewId = R.id.dpltext_lote)
	public TextView tboxlote;
	@InvokeView(viewId = R.id.dpltext_fechavence)
	public TextView tboxlotefechaVence; 	
	@InvokeView(viewId = R.id.dpltext_cantdespachada)
	public TextView tboxcantdespachada;
	@InvokeView(viewId = R.id.dpltext_cantdevolver)
	public TextView tboxcantdevolver; 
	
	public void mappingData(Object entity)
	{
		ExpandListChild grp=(ExpandListChild)entity;
		if(grp.getObject()==null)
			return;
		DevolucionProductoLote dpl=(DevolucionProductoLote) grp.getObject();  
		tboxlote.setText(dpl.getNumeroLote());
		tboxlotefechaVence.setText(""+dpl.getFechaVencimiento());
		tboxcantdespachada.setText(""+dpl.getCantidadDespachada());
		tboxcantdevolver.setText(""+dpl.getCantidadDevuelta()); 
	}
	
	public ProductoLoteDetalleViewHolder(){}
}
