package com.panzyma.nm.view.viewholder;

import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CCobro;
import com.panzyma.nm.serviceproxy.CFormaPago;
import com.panzyma.nm.view.adapter.ExpandListChild;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

import android.widget.TextView;

public class PagoViewHolder {

	@InvokeView(viewId = R.id.cxctextv_forma_pago)
	public TextView FormaPago;
	
	@InvokeView(viewId = R.id.cxctextv_moneda)
	public TextView moneda;
	
	@InvokeView(viewId = R.id.cxctext_monto_nacional)
	public TextView monto_nacional;
	
	@InvokeView(viewId = R.id.cxctext_monto)
	public TextView monto;
	
	
	public void mappingData(Object entity) {
		ExpandListChild grp=(ExpandListChild)entity;
		if(grp.getObject()==null)
			return;
		
		if(grp.getObject().getClass()== CFormaPago.class){
			CFormaPago pago = (CFormaPago) grp.getObject() ;
			FormaPago.setText("" + pago.getDescFormaPago() );
			moneda.setText("" + pago.getCodMoneda() );
			monto_nacional.setText(StringUtil.formatReal(pago.getMontoNacional()) );
			monto.setText(StringUtil.formatReal(pago.getMonto()));
		}
	}
	
	
	
	
	
}
