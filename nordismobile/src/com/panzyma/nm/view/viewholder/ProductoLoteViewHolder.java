package com.panzyma.nm.view.viewholder;

import android.widget.ListView;
import android.widget.TextView;

import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.view.adapter.GenericAdapter;
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
		DevolucionProducto dp=(DevolucionProducto) entity;
		
		tboxproducto.setText(dp.getNombreProducto());
		tboxcantord.setText(""+dp.getCantidadOrdenada());
		tboxcantbonif.setText(""+dp.getBonificacion());
		tboxcantprom.setText(""+dp.getCantidadBonificada());
		tboxdescuento.setText(""+dp.getDescuento());
		tboxtotalprod.setText(""+dp.getTotalProducto());
		tboxdevbonif.setText(""+dp.getCantidadBonificada());
		tboxpreciounit.setText(""+dp.getPrecio());
		tboxsubtotal.setText(""+dp.getSubtotal());
		tboxmontobonif.setText(""+dp.getMontoBonif());
		tboximpuesto.setText(""+dp.getImpuesto());
		tboxtotaldev.setText(""+dp.getTotal());
		 
	} 
	 
}