package com.panzyma.nm.view.viewholder;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.serviceproxy.TasaCambio;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;
public class TasaCambioViewHolder {

 
	@InvokeView(viewId = R.id.bnftextv_detalle_tasa_moneda)
	public TextView moneda; 
	@InvokeView(viewId = R.id.bnftextv_detalle_tasa_dia)
	public TextView fecha; 
	@InvokeView(viewId = R.id.bnftextv_detalle_tasa_tasa)
	public TextView tasatxt;
	
	public void mappingData(Object entity)
	{
		TasaCambio item = (TasaCambio)entity;
		moneda.setText(item.getCodMoneda());
		tasatxt.setText(""+item.getTasa());
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Calendar cal = DateUtil.getCalendar(item.getFecha());
		java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
		fecha.setText(""+  sdf.format(date) );
	}
}
