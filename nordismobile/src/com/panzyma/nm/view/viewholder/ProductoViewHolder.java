package com.panzyma.nm.view.viewholder;
 
import android.widget.TextView;

import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class ProductoViewHolder {
	@InvokeView(viewId = R.id.gp_productonom)
	public TextView nomprod; 
	@InvokeView(viewId = R.id.gp_productocod)
	public TextView codigo;
	@InvokeView(viewId = R.id.gp_productodisp)
	public TextView disponibilidad;
	
	public void mappingData(Object entity)
	{	
		Producto prod=(Producto) entity;
		codigo.setText(""+prod.getCodigo()); 
		nomprod.setText(""+prod.getNombre());
		disponibilidad.setText(""+prod.getDisponible()); 
		if(prod.getDisponible()==0)
			disponibilidad.setTextColor(android.graphics.Color.RED); 
		else if(prod.getDisponible()>0 && prod.getDisponible()<20)
			disponibilidad.setTextColor(android.graphics.Color.rgb(255, 140, 60)); 
		else
			disponibilidad.setTextColor(android.graphics.Color.BLUE);
	}
	 
}
