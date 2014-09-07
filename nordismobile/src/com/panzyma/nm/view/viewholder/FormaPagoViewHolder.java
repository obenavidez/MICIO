package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class FormaPagoViewHolder {
	
	@InvokeView(viewId = R.id.cxctextv_detalle_sucursal)
	public TextView formaPago; 
	@InvokeView(viewId = R.id.cxctextv_detalle_sucursal)
	public TextView moneda;
	@InvokeView(viewId = R.id.cxctextv_detalle_sucursal)
	public TextView monto;
	@InvokeView(viewId = R.id.cxctextv_detalle_sucursal)
	public TextView montoNacional;
	
	@SuppressWarnings("unused")
	public void mappingData(Object entity)
	{
		ReciboDetFormaPago rdfp = (ReciboDetFormaPago)entity;
		String str = "%s - %s";
		formaPago.setText(String.format(str, rdfp.getCodFormaPago(), rdfp.getDescFormaPago()));
		moneda.setText(String.format(str, rdfp.getCodMoneda(), rdfp.getDescMoneda()));
		monto.setText(StringUtil.formatReal(rdfp.getMonto()));
		montoNacional.setText(StringUtil.formatReal(rdfp.getMontoNacional()));
	}

}
