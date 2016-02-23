package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class DetallePrecioProductoViewHolder {

	@InvokeView(viewId = R.id.bnftextv_detalle_tipoprecio)
	public TextView tipoprecio; 
	@InvokeView(viewId = R.id.bnftextv_detalle_preciomin)
	public TextView preciomin;
	@InvokeView(viewId = R.id.bnftextv_detalle_preciomax)
	public TextView preciomax;
	@InvokeView(viewId = R.id.bnftextv_detalle_precio)
	public TextView precio;
	
	
	public void mappingData(Object entity)
	{
		com.panzyma.nm.serviceproxy.PrecioProducto pp=(com.panzyma.nm.serviceproxy.PrecioProducto)entity;		
		tipoprecio.setText(pp.getDescTipoPrecio());
		preciomin.setText(""+pp.getMinimo());
		preciomax.setText(""+pp.getMaximo());
		precio.setText(""+pp.getPrecio());
	}
	
}
