package com.panzyma.nm.view.viewholder;

import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.serviceproxy.CCobro;
import com.panzyma.nm.serviceproxy.CVenta;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

import android.widget.TextView;

public class CobroViewHolder {

	@InvokeView(viewId = R.id.cxctextv_numero_central)
	public TextView NumeroCentral;
	
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	
	@InvokeView(viewId = R.id.cxctext_cobro_cliente)
	public TextView Cliente;
	
	@InvokeView(viewId = R.id.cxctext_detalle_total)
	public TextView Total;
	
	
	public void mappingData(Object entity) {
		CCobro cobro = (CCobro) entity;
		NumeroCentral.setText("" + cobro.getNumeroCentral() );
		fecha.setText("" + cobro.getFecha() );
		Cliente.setText("" + ( cobro.getNombreCliente().trim().length() < 50 ? cobro.getNombreCliente() : cobro.getNombreCliente().trim().substring(0, 50)+".." ) );
		Total.setText(StringUtil.formatReal(cobro.getTotalRecibo()));
		
	}
	
}
