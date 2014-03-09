package com.panzyma.nm.view.viewholder;
 
import android.widget.TextView;

import com.panzyma.nm.view.adapter.InvokeView; 
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nordismobile.R;
 
public class ProductoViewHolder {
	@InvokeView(viewId = R.id.gp_productonom)
	public TextView nomprod; 
	@InvokeView(viewId = R.id.gp_productocod)
	public TextView codigo;
	@InvokeView(viewId = R.id.gp_productodisp)
	public TextView disponibilidad;
	
	public void mappingData(Object entity)
	{	
		vmProducto prod=(vmProducto) entity;
		codigo.setText(""+prod.getCodigo()); 
		nomprod.setText(""+prod.getNombre());
		disponibilidad.setText(""+prod.getDisponibilidad()); 
		if(prod.getDisponibilidad()==0)
			disponibilidad.setTextColor(android.graphics.Color.RED); 
		else if(prod.getDisponibilidad()>0 && prod.getDisponibilidad()<20)
			disponibilidad.setTextColor(android.graphics.Color.rgb(255, 140, 60)); 
		else
			disponibilidad.setTextColor(android.graphics.Color.BLUE);
	}
	/*public void setListItemEventHandler(vmProducto prod)
	{
		checkBox.setTag(prod); 
		checkBox.setChecked(prod.isChecked());
		checkBox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				
				((vmProducto) ((CheckBox) v).getTag()).setChecked(((CheckBox) v).isChecked());
			}
		});
	}*/
}
