package com.panzyma.nm.serviceproxy;

public class ReciboDetFactura implements Documento {

	protected long id;
	protected long objFacturaID;
	protected long objReciboID;
	protected float monto;
	protected boolean esAbono;
	protected float montoDescEspecifico;
	protected float montoDescOcasional;
	protected float montoRetencion;
	protected float montoImpuesto;
	protected float montoInteres;
	protected float montoNeto;
	protected float montoOtrasDeducciones;
	protected float montoDescPromocion;
	protected float porcDescOcasional;
	protected float porcDescPromo;
	protected java.lang.String numero;
	protected long fecha;
	protected long fechaVence;
	protected long fechaAplicaDescPP;
	protected float subTotal;
	protected float impuesto;
	protected float totalfactura;
	protected float saldofactura;
	protected float interesMoratorio;
	protected float saldoTotal;
	protected float montoImpuestoExento;
	protected float montoDescEspecificoCalc;
	
	public ReciboDetFactura() {
		super();
	}

	public ReciboDetFactura(long id, long objFacturaID, float monto,
			boolean esAbono, float montoDescEspecifico,
			float montoDescOcasional, float montoRetencion,
			float montoImpuesto, float montoInteres, float montoNeto,
			float montoOtrasDeducciones, float montoDescPromocion,
			float porcDescOcasional, float porcDescPromo, String numero,
			long fecha, long fechaVence, long fechaAplicaDescPP,
			float subTotal, float impuesto, float totalfactura,
			float saldofactura, float interesMoratorio, float saldoTotal,
			float montoImpuestoExento, float montoDescEspecificoCalc) {
		super();
		this.id = id;
		this.objFacturaID = objFacturaID;
		this.monto = monto;
		this.esAbono = esAbono;
		this.montoDescEspecifico = montoDescEspecifico;
		this.montoDescOcasional = montoDescOcasional;
		this.montoRetencion = montoRetencion;
		this.montoImpuesto = montoImpuesto;
		this.montoInteres = montoInteres;
		this.montoNeto = montoNeto;
		this.montoOtrasDeducciones = montoOtrasDeducciones;
		this.montoDescPromocion = montoDescPromocion;
		this.porcDescOcasional = porcDescOcasional;
		this.porcDescPromo = porcDescPromo;
		this.numero = numero;
		this.fecha = fecha;
		this.fechaVence = fechaVence;
		this.fechaAplicaDescPP = fechaAplicaDescPP;
		this.subTotal = subTotal;
		this.impuesto = impuesto;
		this.totalfactura = totalfactura;
		this.saldofactura = saldofactura;
		this.interesMoratorio = interesMoratorio;
		this.saldoTotal = saldoTotal;
		this.montoImpuestoExento = montoImpuestoExento;
		this.montoDescEspecificoCalc = montoDescEspecificoCalc;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getObjFacturaID() {
		return objFacturaID;
	}

	public void setObjFacturaID(long objFacturaID) {
		this.objFacturaID = objFacturaID;
	}
	
	public long getObjReciboID() {
		return objReciboID;
	}

	public void setObjReciboID(long objReciboID) {
		this.objReciboID = objReciboID;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public boolean isEsAbono() {
		return esAbono;
	}

	public void setEsAbono(boolean esAbono) {
		this.esAbono = esAbono;
	}

	public float getMontoDescEspecifico() {
		return montoDescEspecifico;
	}

	public void setMontoDescEspecifico(float montoDescEspecifico) {
		this.montoDescEspecifico = montoDescEspecifico;
	}

	public float getMontoDescOcasional() {
		return montoDescOcasional;
	}

	public void setMontoDescOcasional(float montoDescOcasional) {
		this.montoDescOcasional = montoDescOcasional;
	}

	public float getMontoRetencion() {
		return montoRetencion;
	}

	public void setMontoRetencion(float montoRetencion) {
		this.montoRetencion = montoRetencion;
	}

	public float getMontoImpuesto() {
		return montoImpuesto;
	}

	public void setMontoImpuesto(float montoImpuesto) {
		this.montoImpuesto = montoImpuesto;
	}

	public float getMontoInteres() {
		return montoInteres;
	}

	public void setMontoInteres(float montoInteres) {
		this.montoInteres = montoInteres;
	}

	public float getMontoNeto() {
		return montoNeto;
	}

	public void setMontoNeto(float montoNeto) {
		this.montoNeto = montoNeto;
	}

	public float getMontoOtrasDeducciones() {
		return montoOtrasDeducciones;
	}

	public void setMontoOtrasDeducciones(float montoOtrasDeducciones) {
		this.montoOtrasDeducciones = montoOtrasDeducciones;
	}

	public float getMontoDescPromocion() {
		return montoDescPromocion;
	}

	public void setMontoDescPromocion(float montoDescPromocion) {
		this.montoDescPromocion = montoDescPromocion;
	}

	public float getPorcDescOcasional() {
		return porcDescOcasional;
	}

	public void setPorcDescOcasional(float porcDescOcasional) {
		this.porcDescOcasional = porcDescOcasional;
	}

	public float getPorcDescPromo() {
		return porcDescPromo;
	}

	public void setPorcDescPromo(float porcDescPromo) {
		this.porcDescPromo = porcDescPromo;
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

	public long getFechaAplicaDescPP() {
		return fechaAplicaDescPP;
	}

	public void setFechaAplicaDescPP(long fechaAplicaDescPP) {
		this.fechaAplicaDescPP = fechaAplicaDescPP;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public float getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(float impuesto) {
		this.impuesto = impuesto;
	}

	public float getTotalfactura() {
		return totalfactura;
	}

	public void setTotalFactura(float totalfactura) {
		this.totalfactura = totalfactura;
	}

	public float getSaldofactura() {
		return saldofactura;
	}

	public void setSaldoFactura(float saldofactura) {
		this.saldofactura = saldofactura;
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

	public float getMontoImpuestoExento() {
		return montoImpuestoExento;
	}

	public void setMontoImpuestoExento(float montoImpuestoExento) {
		this.montoImpuestoExento = montoImpuestoExento;
	}

	public float getMontoDescEspecificoCalc() {
		return montoDescEspecificoCalc;
	}

	public void setMontoDescEspecificoCalc(float montoDescEspecificoCalc) {
		this.montoDescEspecificoCalc = montoDescEspecificoCalc;
	}

	@Override
	public long id() {
		// TODO Auto-generated method stub
		return getId();
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return "Factura";
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public long getFechaDocumento() {
		// TODO Auto-generated method stub
		return getFecha();
	}

}
