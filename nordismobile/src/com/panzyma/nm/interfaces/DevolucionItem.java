package com.panzyma.nm.interfaces;

public interface DevolucionItem extends BaseHolder {

	String getItemNumero();

	String getItemfecha();

	String getItemCliente();

	String getItemTotal();

	String getItemEstado();
	
	boolean getItemOffline(); 
	
}
