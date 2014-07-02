package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class NotaDebitoViewHolder {

	@InvokeView(viewId = R.id.cxctextv_nond)
	public TextView numero;
	@InvokeView(viewId = R.id.cxctextv_detalle_estado)
	public TextView estado;
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	@InvokeView(viewId = R.id.cxctextv_detalle_fechavence)
	public TextView fechaVence;
	@InvokeView(viewId = R.id.cxctext_dias)
	public TextView dias;
	@InvokeView(viewId = R.id.cxctext_concepto)
	public TextView concepto;
	@InvokeView(viewId = R.id.cxctext_monto)
	public TextView monto;
	@InvokeView(viewId = R.id.cxctext_detalle_abonado)
	public TextView abonado;
	@InvokeView(viewId = R.id.cxctext_detalle_saldo)
	public TextView saldo;

	public void mappingData(Object entity) {
		// PREGUNTAR SI SE TIENE UNA INSTANCIA ADECUADA
		if (entity instanceof CCNotaDebito) {
			CCNotaDebito cnota = (CCNotaDebito) entity;
			numero.setText("" + cnota.getNumero());
			estado.setText("" + cnota.getCodEstado());
			fecha.setText("" + DateUtil.idateToStrYY( cnota.getFecha() ) );
			fechaVence.setText("" + DateUtil.idateToStrYY( cnota.getFechaVence() ) );
			dias.setText("" + cnota.getDias());
			concepto.setText("" + cnota.getConcepto());
			monto.setText("" +StringUtil.formatReal( cnota.getMonto() ) );
			abonado.setText("" + StringUtil.formatReal( cnota.getMontoAbonado() ));
			saldo.setText("" + StringUtil.formatReal( cnota.getSaldo() ) );
		}

	}

}
