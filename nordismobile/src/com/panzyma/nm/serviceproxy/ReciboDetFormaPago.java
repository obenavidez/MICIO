package com.panzyma.nm.serviceproxy;

public class ReciboDetFormaPago {

	protected long id;
	protected long objFormaPagoID;
	protected java.lang.String codFormaPago;
	protected java.lang.String descFormaPago;
	protected java.lang.String numero;
	protected long objMonedaID;
	protected java.lang.String codMoneda;
	protected java.lang.String descMoneda;
	protected float monto;
	protected float montoNacional;
	protected long objEntidadID;
	protected java.lang.String codEntidad;
	protected java.lang.String descEntidad;
	protected int fecha;
	protected java.lang.String serieBilletes;
	protected float tasaCambio;

	public ReciboDetFormaPago() {
	}

	public ReciboDetFormaPago(long id, long objFormaPagoID,
			java.lang.String codFormaPago, java.lang.String descFormaPago,
			java.lang.String numero, long objMonedaID,
			java.lang.String codMoneda, java.lang.String descMoneda,
			float monto, float montoNacional, long objEntidadID,
			java.lang.String codEntidad, java.lang.String descEntidad,
			int fecha, java.lang.String serieBilletes, float tasaCambio) {
		this.id = id;
		this.objFormaPagoID = objFormaPagoID;
		this.codFormaPago = codFormaPago;
		this.descFormaPago = descFormaPago;
		this.numero = numero;
		this.objMonedaID = objMonedaID;
		this.codMoneda = codMoneda;
		this.descMoneda = descMoneda;
		this.monto = monto;
		this.montoNacional = montoNacional;
		this.objEntidadID = objEntidadID;
		this.codEntidad = codEntidad;
		this.descEntidad = descEntidad;
		this.fecha = fecha;
		this.serieBilletes = serieBilletes;
		this.tasaCambio = tasaCambio;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getObjFormaPagoID() {
		return objFormaPagoID;
	}

	public void setObjFormaPagoID(long objFormaPagoID) {
		this.objFormaPagoID = objFormaPagoID;
	}

	public java.lang.String getCodFormaPago() {
		return codFormaPago;
	}

	public void setCodFormaPago(java.lang.String codFormaPago) {
		this.codFormaPago = codFormaPago;
	}

	public java.lang.String getDescFormaPago() {
		return descFormaPago;
	}

	public void setDescFormaPago(java.lang.String descFormaPago) {
		this.descFormaPago = descFormaPago;
	}

	public java.lang.String getNumero() {
		return numero;
	}

	public void setNumero(java.lang.String numero) {
		this.numero = numero;
	}

	public long getObjMonedaID() {
		return objMonedaID;
	}

	public void setObjMonedaID(long objMonedaID) {
		this.objMonedaID = objMonedaID;
	}

	public java.lang.String getCodMoneda() {
		return codMoneda;
	}

	public void setCodMoneda(java.lang.String codMoneda) {
		this.codMoneda = codMoneda;
	}

	public java.lang.String getDescMoneda() {
		return descMoneda;
	}

	public void setDescMoneda(java.lang.String descMoneda) {
		this.descMoneda = descMoneda;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public float getMontoNacional() {
		return montoNacional;
	}

	public void setMontoNacional(float montoNacional) {
		this.montoNacional = montoNacional;
	}

	public long getObjEntidadID() {
		return objEntidadID;
	}

	public void setObjEntidadID(long objEntidadID) {
		this.objEntidadID = objEntidadID;
	}

	public java.lang.String getCodEntidad() {
		return codEntidad;
	}

	public void setCodEntidad(java.lang.String codEntidad) {
		this.codEntidad = codEntidad;
	}

	public java.lang.String getDescEntidad() {
		return descEntidad;
	}

	public void setDescEntidad(java.lang.String descEntidad) {
		this.descEntidad = descEntidad;
	}

	public int getFecha() {
		return fecha;
	}

	public void setFecha(int fecha) {
		this.fecha = fecha;
	}

	public java.lang.String getSerieBilletes() {
		return serieBilletes;
	}

	public void setSerieBilletes(java.lang.String serieBilletes) {
		this.serieBilletes = serieBilletes;
	}

	public float getTasaCambio() {
		return tasaCambio;
	}

	public void setTasaCambio(float tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
}
