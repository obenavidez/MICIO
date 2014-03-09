package com.panzyma.nm.view.viewholder;

import android.widget.TextView;
 
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class FacturaViewHolder{
 
	@InvokeView(viewId = R.id.cxctextv_detalle_sucursal)
	public TextView sucursal; 
	@InvokeView(viewId = R.id.cxctextv_detalle_nofactura)
	public TextView nofactura;
	@InvokeView(viewId = R.id.cxctextv_detalle_tipo)
	public TextView tipo;
	@InvokeView(viewId = R.id.cxctextv_detalle_nopedido)
	public TextView nopedido;
	@InvokeView(viewId = R.id.cxctextv_detalle_estado)
	public TextView estado;
	@InvokeView(viewId = R.id.cxctextv_detalle_fecha)
	public TextView fecha;
	@InvokeView(viewId = R.id.cxctextv_detalle_fechavence)
	public TextView fechavence;
	@InvokeView(viewId = R.id.cxctextv_detallefechadescpp)
	public TextView fechadescpp;
	@InvokeView(viewId = R.id.cxctext_detalle_dias)
	public TextView dias;
	@InvokeView(viewId = R.id.cxctext_detalle_totalfact)
	public TextView totalfact;
	@InvokeView(viewId = R.id.cxctext_detalle_abonado)
	public TextView abonado;
	@InvokeView(viewId = R.id.cxctext_detalle_descontado)
	public TextView descontado;
	@InvokeView(viewId = R.id.cxctext_detalle_retenido)
	public TextView retenido;
	@InvokeView(viewId = R.id.cxctext_detalle_otro)
	public TextView otro;
	@InvokeView(viewId = R.id.cxctext_detalle_saldo)
	public TextView saldo; 
 
	
	public void mappingData(Object entity)
	{	
		Factura fact=(Factura) entity;
		sucursal.setText(""+fact.getNombreSucursal()); 
		nofactura.setText(""+fact.getNoFactura());
	    tipo.setText(""+fact.getTipo());
		nopedido.setText(""+fact.getNoPedido()); 
		estado.setText(""+fact.getEstado()); 
		fecha.setText(""+DateUtil.idateToStr(fact.getFecha()));
		fechavence.setText(""+DateUtil.idateToStr(fact.getFechaVencimiento()));
		fechadescpp.setText(""+DateUtil.idateToStr(fact.getFechaAppDescPP()));
		dias.setText(""+fact.getDias());  
		totalfact.setText(""+StringUtil.formatReal(fact.getTotalFacturado())); 
		abonado.setText(""+StringUtil.formatReal(fact.getAbonado()));
		descontado.setText(""+StringUtil.formatReal(fact.getDescontado()));
		retenido.setText(""+StringUtil.formatReal(fact.getRetenido()));
		otro.setText(""+StringUtil.formatReal(fact.getOtro())); 
		saldo.setText(""+StringUtil.formatReal(fact.getSaldo()));
 
		//Estado 
        int color = R.color.White;
        if (fact.getCodEstado().toUpperCase().compareTo("PAGADA") == 0)
        	color = R.color.Blue;
        if ((fact.getCodEstado().toUpperCase().compareTo("EMITIDA") == 0) || (fact.getCodEstado().toUpperCase().compareTo("REIMPRESA") == 0)) 
        {
            if (fact.getDias() > 0) 
                color = R.color.Red;
            else
                color = R.color.Green;
        }      
        estado.setTextColor(color); 
		
	}

}
