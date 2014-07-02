package com.panzyma.nm.view.viewholder;

import android.widget.TextView;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CVenta;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class VentaViewHolder {

	@InvokeView(viewId = R.id.cxctextv_nopedido)
	public TextView nopedido;
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	@InvokeView(viewId = R.id.cxctext_detalle_total)
	public TextView total;
	@InvokeView(viewId = R.id.cxctext_cliente)
	public TextView cliente;

	public void mappingData(Object entity) {
		CVenta venta = (CVenta) entity;
		nopedido.setText("" + venta.getNumeroCentral());
		fecha.setText("" + venta.getFecha());
		total.setText("" + StringUtil.formatReal(venta.getTotal()));
		cliente.setText("" + venta.getNombreCliente());

	}

}
