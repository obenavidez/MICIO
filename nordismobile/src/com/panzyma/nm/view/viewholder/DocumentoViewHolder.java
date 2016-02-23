package com.panzyma.nm.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.serviceproxy.Documento;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class DocumentoViewHolder {

	@InvokeView(viewId = R.id.menu_name)
	public TextView title;

	@InvokeView(viewId = R.id.description)
	public TextView subtitle;

	@InvokeView(viewId = R.id.price)
	public TextView codigo;

	public void mappingData(Object entity) {
		Documento document = (Documento)entity;
		title.setText(" "+document.getTipo()+" #: " + document.getNumero());
		subtitle.setText(" Fecha: "+DateUtil.idateToStrYY(document.getFechaDocumento())+", Monto: " + document.getMonto());
		codigo.setVisibility(View.INVISIBLE);
	}

}
