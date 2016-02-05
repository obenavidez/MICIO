package com.panzyma.nm.logic;

import java.io.Serializable;

public class PojoDevolucion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8067582826972497595L;
	
	
	long id;
	int referencia;
	String fecha;
	String nombreCliente;
	float total;
	String codigoEstado;
	long clienteId;
	boolean offLine;
	long sucursalId;

	public PojoDevolucion() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getReferencia() {
		return referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getCodigoEstado() {
		return codigoEstado;
	}

	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	public long getClienteId() {
		return clienteId;
	}

	public void setClienteId(long clienteId) {
		this.clienteId = clienteId;
	}

	public boolean isOffLine() {
		return offLine;
	}

	public void setOffLine(boolean offLine) {
		this.offLine = offLine;
	}

	public long getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(long sucursalId) {
		this.sucursalId = sucursalId;
	}
}
