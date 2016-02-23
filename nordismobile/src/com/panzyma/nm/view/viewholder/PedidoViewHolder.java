package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CCPedido;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class PedidoViewHolder {

	@InvokeView(viewId = R.id.cxctextv_nond)
	public TextView numero;
	@InvokeView(viewId = R.id.cxctextv_detalle_estado)
	public TextView estado;
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	@InvokeView(viewId = R.id.cxctextv_referencia)
	public TextView referencia;
	@InvokeView(viewId = R.id.cxctext_tipo)
	public TextView tipo;
	@InvokeView(viewId = R.id.cxctext_concepto)
	public TextView concepto;
	@InvokeView(viewId = R.id.cxctext_total)
	public TextView total;
	@InvokeView(viewId = R.id.cxctext_tipo_precio)
	public TextView tipoPrecio;	

	public void mappingData(Object entity) {
		// PREGUNTAR SI SE TIENE UNA INSTANCIA ADECUADA
		if (entity instanceof CCPedido) {
			CCPedido cnota = (CCPedido) entity;
			numero.setText("" + cnota.getNumero());
			estado.setText("" + cnota.getCodEstado());
			fecha.setText("" + DateUtil.idateToStrYY(cnota.getFecha()) );
			referencia.setText("" + cnota.getReferencia());
			tipo.setText("" + cnota.getTipo());
			concepto.setText("");
			total.setText("" + StringUtil.formatReal(cnota.getTotal()) );
			tipoPrecio.setText("" + cnota.getTipoPrecio());			
		}

	}

}
