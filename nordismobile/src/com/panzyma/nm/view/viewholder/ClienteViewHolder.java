package com.panzyma.nm.view.viewholder;

import android.widget.TextView;
  
import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nm.viewmodel.vmCliente; 
import com.panzyma.nordismobile.R;

public class ClienteViewHolder{

	@InvokeView(viewId = R.id.gc_clientecod)
	public TextView codigo; 
	@InvokeView(viewId = R.id.gc_clientenom)
	public TextView nombreCliente;
	@InvokeView(viewId = R.id.gc_clienteubi)
	public TextView ubicacion;
	 
	public void mappingData(Object entity)
	{	
		vmCliente cliente=(vmCliente) entity;
		codigo.setText(""+cliente.getCodigo()); 
		nombreCliente.setText(""+cliente.getNombreCliente());
		ubicacion.setText(""+cliente.getUbicacion()); 
	}
}
