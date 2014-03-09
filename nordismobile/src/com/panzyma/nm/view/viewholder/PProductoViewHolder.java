package com.panzyma.nm.view.viewholder;

import android.widget.TextView;

import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nm.viewmodel.vmPProducto;
import com.panzyma.nordismobile.R;

public class PProductoViewHolder {
	
	@InvokeView(viewId = R.id.gpa_nombreprod)
	public TextView nomprod; 
	@InvokeView(viewId = R.id.gpa_cantidad)
	public TextView cantidad; 
	
	public void mappingData(Object entity)
	{	
		vmPProducto prod=(vmPProducto) entity; 
		nomprod.setText(""+prod.getNomproducto());
		cantidad.setText(""+prod.getCantidad());  
		cantidad.setTextColor(android.graphics.Color.BLUE);
	}
}
