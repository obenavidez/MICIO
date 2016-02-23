package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class PProductoViewHolder 
{
	
	@InvokeView(viewId = R.id.gpa_nombreprod)
	public TextView nomprod; 
	@InvokeView(viewId = R.id.gpa_cantidad)
	public TextView cantidad; 	
	@InvokeView(viewId = R.id.gpa_bonificacion)
	public TextView bonificacion; 
	@InvokeView(viewId = R.id.gpa_promocion)
	public TextView promocion; 
	@InvokeView(viewId = R.id.gpa_productoprecio)
	public TextView precio; 
	@InvokeView(viewId = R.id.gpa_iva)
	public TextView iva; 
	@InvokeView(viewId = R.id.gpa_total)
	public TextView total; 
	public void mappingData(Object entity)
	{	
		DetallePedido prod=(DetallePedido) entity; 
		nomprod.setText(""+prod.getNombreProducto());		
		cantidad.setText(""+prod.getCantidadOrdenada());  
		bonificacion.setText(""+prod.getCantidadBonificadaEditada());
		promocion.setText(""+prod.getCantidadPromocion());
		precio.setText(""+prod.getMontoPrecioEditado());
		iva.setText(String.valueOf(prod.getImpuesto()));
		total.setText(String.valueOf(prod.getTotal())); 
	}
}
 