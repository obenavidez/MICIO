package com.panzyma.nm.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil; 
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class SolicitudDescuentoViewHolder {

	@InvokeView(viewId = R.id.menu_name)
	public TextView title;

	@InvokeView(viewId = R.id.description)
	public TextView title2;

	@InvokeView(viewId = R.id.price)
	public TextView justificacion;

	public void mappingData(Object entity) {
		SolicitudDescuento sd = (SolicitudDescuento)entity;
		title.setText("#"+sd.getFactura().getNoFactura());
		title2.setText(" FSD: " + DateUtil.idateToStrYY(sd.getFecha())+" % Descuento: "+sd.getPorcentaje());
		justificacion.setVisibility(View.INVISIBLE);
		title2.setText(" Justificación: "+sd.getJustificacion());
	}
}
