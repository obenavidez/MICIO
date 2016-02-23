package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class PromocionesViewHolder {

	@InvokeView(viewId = R.id.ap_codigo)
	public TextView codigo;
	@InvokeView(viewId = R.id.ap_descripcion)
	public TextView descripcion;
	
	public void mappingData(Object entity)
	{
		com.panzyma.nm.serviceproxy.Promocion prom=(com.panzyma.nm.serviceproxy.Promocion)entity;
		codigo.setText(prom.getCodigo());
		String desc = prom.getDescripcionPromocion();
        desc = StringUtil.replace(desc, ".LF.", "\n");        
		descripcion.setText(desc);		
	}
}
