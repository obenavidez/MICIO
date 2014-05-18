package com.panzyma.nm.serviceproxy;

public class ReciboDetND {

	protected long id;
	protected long objNotaDebitoID;
	protected float montoInteres;
	protected boolean esAbono;
	protected float montoPagar;
	protected java.lang.String numero;
	protected long fecha;
	protected long fechaVence;
	protected float montoND;
	protected float saldoND;
	protected float interesMoratorio;
	protected float saldoTotal;
	protected float montoNeto;	

	public ReciboDetND() {
		super();
	}

	public ReciboDetND(long id, long objNotaDebitoID, float montoInteres,
			boolean esAbono, float montoPagar, String numero, long fecha,
			long fechaVence, float montoND, float saldoND,
			float interesMoratorio, float saldoTotal, float montoNeto) {
		super();
		this.id = id;
		this.objNotaDebitoID = objNotaDebitoID;
		this.montoInteres = montoInteres;
		this.esAbono = esAbono;
		this.montoPagar = montoPagar;
		this.numero = numero;
		this.fecha = fecha;
		this.fechaVence = fechaVence;
		this.montoND = montoND;
		this.saldoND = saldoND;
		this.interesMoratorio = interesMoratorio;
		this.saldoTotal = saldoTotal;
		this.montoNeto = montoNeto;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getObjNotaDebitoID() {
		return objNotaDebitoID;
	}

	public void setObjNotaDebitoID(long objNotaDebitoID) {
		this.objNotaDebitoID = objNotaDebitoID;
	}

	public float getMontoInteres() {
		return montoInteres;
	}

	public void setMontoInteres(float montoInteres) {
		this.montoInteres = montoInteres;
	}

	public boolean isEsAbono() {
		return esAbono;
	}

	public void setEsAbono(boolean esAbono) {
		this.esAbono = esAbono;
	}

	public float getMontoPagar() {
		return montoPagar;
	}

	public void setMontoPagar(float montoPagar) {
		this.montoPagar = montoPagar;
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

	public float getMontoND() {
		return montoND;
	}

	public void setMontoND(float montoND) {
		this.montoND = montoND;
	}

	public float getSaldoND() {
		return saldoND;
	}

	public void setSaldoND(float saldoND) {
		this.saldoND = saldoND;
	}

	public float getInteresMoratorio() {
		return interesMoratorio;
	}

	public void setInteresMoratorio(float interesMoratorio) {
		this.interesMoratorio = interesMoratorio;
	}

	public float getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(float saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public float getMontoNeto() {
		return montoNeto;
	}

	public void setMontoNeto(float montoNeto) {
		this.montoNeto = montoNeto;
	}

}
