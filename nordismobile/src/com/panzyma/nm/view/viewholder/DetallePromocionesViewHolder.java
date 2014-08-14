package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class DetallePromocionesViewHolder {

	@InvokeView(viewId = R.id.bnftextv_detalle_catcliente)
	public TextView catcliente; 
	@InvokeView(viewId = R.id.bnftextv_detalle_cantidad)
	public TextView cantidad;
	@InvokeView(viewId = R.id.bnftextv_detalle_cantbonificacion)
	public TextView cant_bonificacion;
	
	
	public void mappingData(Object entity)
	{
		com.panzyma.nm.serviceproxy.Bonificacion bnf=(com.panzyma.nm.serviceproxy.Bonificacion)entity;
		catcliente.setText(bnf.getCategoriaCliente());
		cantidad.setText(""+bnf.getCantidad());
		cant_bonificacion.setText(""+bnf.getCantBonificacion());
	}
	
}
