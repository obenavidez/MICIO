package com.panzyma.nm.interfaces;


public interface Item extends BaseHolder {

	String getItemName();

	String getItemDescription();

	String getItemCode();
	
	String getItemCodeStado();
	//Item parseJSONToItem();
}
