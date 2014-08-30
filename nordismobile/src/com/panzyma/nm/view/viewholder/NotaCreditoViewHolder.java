package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CCNotaCredito;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class NotaCreditoViewHolder {

	@InvokeView(viewId = R.id.cxctextv_nond)
	public TextView numero;
	@InvokeView(viewId = R.id.cxctextv_detalle_estado)
	public TextView estado;
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	@InvokeView(viewId = R.id.cxctextv_detalle_fechavence)
	public TextView fechaVence;
	@InvokeView(viewId = R.id.cxctext_no_recibo)
	public TextView noRecibo;
	@InvokeView(viewId = R.id.cxctext_concepto)
	public TextView concepto;
	@InvokeView(viewId = R.id.cxctext_monto)
	public TextView monto;

	public void mappingData(Object entity) {
		// PREGUNTAR SI SE TIENE UNA INSTANCIA ADECUADA
		if (entity instanceof CCNotaCredito) {
			int color = R.color.Black;		
			CCNotaCredito cnota = (CCNotaCredito) entity;
			if (cnota.getCodEstado().toUpperCase().compareTo("PAGADA") == 0) color = R.color.Blue;
            if ((cnota.getCodEstado().toUpperCase().compareTo("AUTORIZADA") == 0) || (cnota.getCodEstado().toUpperCase().compareTo("IMPRESA") == 0)) {
                if (cnota.getFechaVence() < DateUtil.d2i(DateUtil.getCalendar().getTime())) 
                    color = R.color.Red;
                else
                    color = R.color.Green;
            }
	        estado.setTextColor(color);
			numero.setText("" + cnota.getNumero());
			estado.setText("" + cnota.getCodEstado());
			fecha.setText("" + DateUtil.idateToStrYY(cnota.getFecha()) );
			fechaVence.setText("" + DateUtil.idateToStrYY(cnota.getFechaVence()) );
			noRecibo.setText("" +  cnota.getNumRColAplic() == null ? "" : cnota.getNumRColAplic()  );
			concepto.setText("" + cnota.getConcepto());
			monto.setText("" + StringUtil.formatReal(cnota.getMonto()) );
			
		}

	}

}
