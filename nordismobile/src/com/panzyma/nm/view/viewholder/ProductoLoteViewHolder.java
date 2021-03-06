package com.panzyma.nm.view.viewholder;
 
import android.widget.TextView;

import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.view.adapter.ExpandListGroup; 
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class ProductoLoteViewHolder {

	@InvokeView(viewId = R.id.dpl_tboxproducto)
	public TextView tboxproducto; 
	@InvokeView(viewId = R.id.dpltextv_cantord)
	public TextView tboxcantord;
	@InvokeView(viewId = R.id.dpltextv_cantbonif)
	public TextView tboxcantbonif;
	@InvokeView(viewId = R.id.dpltextv_cantprom)
	public TextView tboxcantprom;
	@InvokeView(viewId = R.id.dpltextv_descuento)
	public TextView tboxdescuento;
	@InvokeView(viewId = R.id.dpltextv_totalprod) 
	public TextView tboxtotalprod;	
	@InvokeView(viewId = R.id.dpltext_cantdev)
	public TextView tboxcantdev;
	@InvokeView(viewId = R.id.dpltext_bonifdev)
	public TextView tboxdevbonif;	
	@InvokeView(viewId = R.id.dpltextv_preciounitario)
	public TextView tboxpreciounit;
	@InvokeView(viewId = R.id.dpltextv_subtotal)
	public TextView tboxsubtotal;
	@InvokeView(viewId = R.id.dpltext_montobonif)
	public TextView tboxmontobonif;
	@InvokeView(viewId = R.id.dpltext_impuesto)
	public TextView tboximpuesto;
	@InvokeView(viewId = R.id.dpltext_totaldev)
	public TextView tboxtotaldev;	   
	
	public void mappingData(Object entity)
	{
		ExpandListGroup grp=(ExpandListGroup)entity;
		
		DevolucionProducto dp=(DevolucionProducto) grp.getObject();
		
		tboxproducto.setText(""+dp.getNombreProducto());
		tboxcantord.setText(""+dp.getCantidadOrdenada());
		tboxcantbonif.setText(""+dp.getCantidadBonificada());
		tboxcantprom.setText(""+dp.getCantidadPromocionada());
		tboxdescuento.setText(""+dp.getDescuento()/100.00);
		tboxtotalprod.setText(""+dp.getTotalProducto());
		
		tboxcantdev.setText(""+dp.getCantidadDevolver());
		tboxdevbonif.setText(""+dp.getBonificacionVen());
		tboxpreciounit.setText(""+dp.getPrecio()/100.00);
		tboxsubtotal.setText(""+dp.getSubtotal()/100.00);
		tboxmontobonif.setText(""+dp.getMontoBonifVen()/100.00);
		tboximpuesto.setText(""+dp.getImpuesto()/100.00);
		tboxtotaldev.setText(""+dp.getTotal()/100.00);
		 
	} 
	 
}
