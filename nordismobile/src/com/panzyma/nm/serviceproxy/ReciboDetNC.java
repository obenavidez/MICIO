package com.panzyma.nm.serviceproxy;

public class ReciboDetNC {

	protected long id;
	protected long objNotaCreditoID;
	protected float monto;
	protected java.lang.String numero;
	protected long fecha;
	protected long fechaVence;	

	public ReciboDetNC() {
		super();
	}	

	public ReciboDetNC(long id, long objNotaCreditoID, float monto,
			String numero, long fecha, long fechaVence) {
		super();
		this.id = id;
		this.objNotaCreditoID = objNotaCreditoID;
		this.monto = monto;
		this.numero = numero;
		this.fecha = fecha;
		this.fechaVence = fechaVence;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getObjNotaCreditoID() {
		return objNotaCreditoID;
	}

	public void setObjNotaCreditoID(long objNotaCreditoID) {
		this.objNotaCreditoID = objNotaCreditoID;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public java.lang.String getNumero() {
		return numero;
	}

	public void setNumero(java.lang.String numero) {
		this.numero = numero;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}

	public long getFechaVence() {
		return fechaVence;
	}

	public void setFechaVence(long fechaVence) {
		this.fechaVence = fechaVence;
	}

}
