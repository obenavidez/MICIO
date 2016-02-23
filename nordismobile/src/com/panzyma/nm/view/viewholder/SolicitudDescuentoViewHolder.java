package com.panzyma.nm.view.viewholder;
 
import android.widget.EditText;
import android.widget.TextView;
 
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nm.viewdialog.DialogSolicitudDescuento; 
import com.panzyma.nordismobile.R;

public class SolicitudDescuentoViewHolder {

	@InvokeView(viewId = R.id.factura)
	public TextView title;

	@InvokeView(viewId = R.id.descuento)
	public EditText descuento;

	@InvokeView(viewId = R.id.justificacion)
	public EditText justificacion;

	public void mappingData(Object entity) 
	{
		SolicitudDescuento sd = (SolicitudDescuento)entity;
		title.setText("#"+sd.getFactura().getNoFactura()); 
		descuento.setText(""); 
		justificacion.setText("");
		if(sd.getPorcentaje()>0)
			descuento.setText(""+sd.getPorcentaje()); 
		if(!sd.getJustificacion().trim().equals(""))
			justificacion.setText(sd.getJustificacion());
		
		if(!DialogSolicitudDescuento.DOC_STATUS_REGISTRADO.equals(sd.getStatus()))
		{
			descuento.setEnabled(false);
			justificacion.setEnabled(false);
		}
			
		
	}
}
