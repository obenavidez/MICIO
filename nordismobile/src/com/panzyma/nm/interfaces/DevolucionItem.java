package com.panzyma.nm.interfaces;

import com.panzyma.nm.serviceproxy.Devolucion;

public interface DevolucionItem extends BaseHolder {

	String getItemNumero();

	String getItemfecha();

	String getItemCliente();

	String getItemTotal();

	String getItemEstado();
	
	boolean getItemOffline(); 
	
    int getReferencia();
	
	long getItemsucursalid();
}
