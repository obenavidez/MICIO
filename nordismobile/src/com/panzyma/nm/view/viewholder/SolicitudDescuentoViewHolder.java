package com.panzyma.nm.view.viewholder;
 
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
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
	
	private int index;
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	private SolicitudDescuento sd;
	
	/*public SolicitudDescuentoViewHolder(){
		sd = new SolicitudDescuento();
	}

	public void mappingData(Object entity, int index) 
	{ 
		sd = (SolicitudDescuento)entity;
		
		title.setText("#"+sd.getFactura().getNoFactura()); 
		/*descuento.setText(""); 
		
		justificacion.setText("");*/
		/*if(sd.getPorcentaje()>0)
			descuento.setText(""+sd.getPorcentaje()); 
		if(!sd.getJustificacion().trim().equals(""))
			justificacion.setText(sd.getJustificacion());
		Log.d("getNoFactura =>", String.valueOf(sd.getFactura().getNoFactura().toString()));
		
		
		
		if(!DialogSolicitudDescuento.DOC_STATUS_REGISTRADO.equals(sd.getStatus()))
		{
			descuento.setEnabled(false);
			justificacion.setEnabled(false);
		}
	}*/
	
}
