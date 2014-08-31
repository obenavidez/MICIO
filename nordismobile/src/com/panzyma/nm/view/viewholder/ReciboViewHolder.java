package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.CCReciboColector;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class ReciboViewHolder{
 
	@InvokeView(viewId = R.id.cxctextv_detalle_sucursal)
	public TextView sucursal; 
	@InvokeView(viewId = R.id.cxctextv_no_recibo)
	public TextView noRecibo;
	@InvokeView(viewId = R.id.cxctext_totalnd)
	public TextView totalNotaDebito;
	@InvokeView(viewId = R.id.cxctext_intereses)
	public TextView intereses;
	@InvokeView(viewId = R.id.cxctextv_detalle_estado)
	public TextView estado;
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	@InvokeView(viewId = R.id.cxctext_totalnc)
	public TextView totalNotaCredito;
	@InvokeView(viewId = R.id.cxctextv_totaldscpp)
	public TextView totalDescPP;
	@InvokeView(viewId = R.id.cxctextv_totaldscoca)
	public TextView totalDescuentoOcasional;
	@InvokeView(viewId = R.id.cxctext_totalpromocion)
	public TextView totalPromocion;
	@InvokeView(viewId = R.id.cxctext_otros)
	public TextView totalOtros;
	@InvokeView(viewId = R.id.cxclbl_neto)
	public TextView neto;
	@InvokeView(viewId = R.id.cxctext_detalle_retenido)
	public TextView retenido; 
	
	public void mappingData(Object entity)
	{	
		CCReciboColector recibo = (CCReciboColector) entity;
		sucursal.setText(""+recibo.getNombreSucursal().substring(0,10)+".."); 
		noRecibo.setText(""+recibo.getNetoRecibo());	    
		totalNotaDebito.setText(""+StringUtil.formatReal(recibo.getTotalND())); 
		totalNotaCredito.setText(""+StringUtil.formatReal(recibo.getTotalNC()));
		estado.setText(""+recibo.getEstado()); 
		fecha.setText(""+DateUtil.idateToStr(recibo.getFecha()));
		intereses.setText(""+StringUtil.formatReal(recibo.getTotalIntereses()));
		totalDescPP.setText(""+StringUtil.formatReal(recibo.getTotalDescPP()));
		totalDescuentoOcasional.setText(""+StringUtil.formatReal(recibo.getTotalDescOca()));  
		totalPromocion.setText(""+StringUtil.formatReal(recibo.getTotalDescProm())); 
		totalOtros.setText(""+StringUtil.formatReal(recibo.getTotalOtro()));
		neto.setText(""+StringUtil.formatReal(recibo.getNetoRecibo()));
		retenido.setText(""+StringUtil.formatReal(recibo.getTotalRetenido()));
				
	}

}
