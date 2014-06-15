package com.panzyma.nm.serviceproxy;

public interface Documento {

	public long id();

	public String getNumero();

	public float getMonto();

	public long getFechaDocumento();

	public String getTipo();
	
	public float getSaldo();
	
	public float getRetencion();
	
	public Object getObject();

}
