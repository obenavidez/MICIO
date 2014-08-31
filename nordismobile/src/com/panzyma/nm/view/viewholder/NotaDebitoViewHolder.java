package com.panzyma.nm.view.viewholder;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CCNotaDebito;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

@SuppressLint("DefaultLocale")
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

	@SuppressLint("DefaultLocale")
	public void mappingData(Object entity) {
		// PREGUNTAR SI SE TIENE UNA INSTANCIA ADECUADA
		if (entity instanceof CCNotaDebito) {
			int color = R.color.Black;			
			CCNotaDebito cnota = (CCNotaDebito) entity;			
            if (cnota.getCodEstado().toUpperCase().compareTo("PAGADA") == 0) color = R.color.Blue;
            if (cnota.getCodEstado().toUpperCase().compareTo("AUTORIZADA") == 0) {
                if (cnota.getDias() > 0) 
                    color = R.color.Red;
                else
                    color = R.color.Green;
            }
            estado.setTextColor(color);
            color = R.color.Black;	
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
