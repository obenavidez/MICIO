package com.panzyma.nm.view.viewholder; 
import com.panzyma.nm.serviceproxy.CNota;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;


import android.widget.TextView; 

public class CNotaViewHolder
{
	@InvokeView(viewId = R.id.cntextv_detallefecha)
	public TextView Fecha; 
	@InvokeView(viewId = R.id.cntextv_detalleautor)
	public TextView ElaboradaPor; 
	@InvokeView(viewId = R.id.cntextv_detallenota)
	public TextView TextoNota; 
	@InvokeView(viewId = R.id.cntextv_detalleconcepto)
	public TextView Concepto;  
	
	public void mappingData(Object entity)
	{
		CNota cnota=(CNota) entity; 
		Fecha.setText(""+cnota.getFecha()); 
		ElaboradaPor.setText(""+cnota.getElaboradaPor());
		Concepto.setText(""+cnota.getConcepto());
		TextoNota.setText(""+cnota.getTextoNota());		
	}
}
