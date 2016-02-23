package com.panzyma.nm.view.viewholder;

import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CDetalleFactura;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

import android.widget.TextView;

public class DetallesFacturaHolder {

	
	@InvokeView(viewId = R.id.cxctextv_producto)
	public TextView producto;
	
	@InvokeView(viewId = R.id.cxctextv_cantidad)
	public TextView cantidad;
	
	 @InvokeView(viewId = R.id.cxctext_total_detalle)
	public TextView total;
	
	@InvokeView(viewId = R.id.cxctext_iva)
	public TextView iva;
	
	
	@InvokeView(viewId = R.id.cxctext_precio_detalle)
	public TextView precio;
	
	@InvokeView(viewId = R.id.cxctext_promocion)
	public TextView promocion;
	
	
	@InvokeView(viewId = R.id.cxctext_bonificacion)
	public TextView bonificacion;
	
	
	public void mappingData(Object entity) 
	{
			CDetalleFactura fact = (CDetalleFactura) entity;
			producto.setText( "" + ( fact.getNombreProducto().trim().length() < 50 ? fact.getNombreProducto() : fact.getNombreProducto().trim().substring(0, 50)+".."));
			cantidad.setText(StringUtil.formatInt(fact.getCantidad()));
			iva.setText(StringUtil.formatReal(fact.getImpuesto()));
			precio.setText(StringUtil.formatReal(fact.getPrecio()));
			total.setText(StringUtil.formatReal(fact.getTotal()));
			promocion.setText(StringUtil.formatReal(fact.getPromocion()));
			bonificacion.setText(StringUtil.formatInt(fact.getBonificado()));
	}
	
	
}
