package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class FormaPagoViewHolder {
	
	@InvokeView(viewId = R.id.cxctextv_detalle_forma_pago)
	public TextView formaPago; 
	@InvokeView(viewId = R.id.cxctextv_detalle_moneda)
	public TextView moneda;
	@InvokeView(viewId = R.id.cxctextv_detalle_monto)
	public TextView monto;
	@InvokeView(viewId = R.id.cxctextv_detalle_monto_nacional)
	public TextView montoNacional;
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	@InvokeView(viewId = R.id.cxctext_detalle_tasa)
	public TextView tasa;
	
	@SuppressWarnings("unused")
	public void mappingData(Object entity)
	{
		ReciboDetFormaPago rdfp = (ReciboDetFormaPago)entity;
		String str = "%s - %s";
		formaPago.setText(String.format(str, rdfp.getCodFormaPago(), rdfp.getDescFormaPago()));
		moneda.setText(String.format(str, rdfp.getCodMoneda(), rdfp.getDescMoneda()));
		monto.setText(StringUtil.formatReal(rdfp.getMonto()));
		montoNacional.setText(StringUtil.formatReal(rdfp.getMontoNacional()));
		fecha.setText(DateUtil.idateToStrYY(rdfp.getFecha()));
		tasa.setText(StringUtil.formatReal(rdfp.getTasaCambio()));
	}

}
