package com.panzyma.nm.view.viewholder;

import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

import android.widget.EditText;
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
	public EditText tboxcantdevolver; 
	
	public ProductoLoteDetalleViewHolder(Object entity, int childposition)
	{
		DevolucionProducto dp=(DevolucionProducto) entity;
		if(dp!=null)
			return;
			DevolucionProductoLote dpl=dp.getProductoLotes()[childposition];
		if(dpl!=null)
			return;
			tboxlote.setText(dpl.getNumeroLote());
			tboxlotefechaVence.setText(""+dpl.getFechaVencimiento());
			tboxcantdespachada.setText(""+dpl.getCantidadDespachada());
			tboxcantdevolver.setText(""+dpl.getCantidadDevuelta()); 
		 
	}
	
	public ProductoLoteDetalleViewHolder(){}
}
