package com.panzyma.nm.serviceproxy;

public class CFormaPago {
	
	    protected java.lang.String codFormaPago;
	    protected java.lang.String descFormaPago;
	    protected float montoNacional;
	    protected java.lang.String codMoneda;
	    protected java.lang.String descMoneda;
	    protected float monto;
	    
	    public CFormaPago() {
	    }
	    
	    public CFormaPago(java.lang.String codFormaPago, java.lang.String descFormaPago, float montoNacional, java.lang.String codMoneda, java.lang.String descMoneda, float monto) {
	        this.codFormaPago = codFormaPago;
	        this.descFormaPago = descFormaPago;
	        this.montoNacional = montoNacional;
	        this.codMoneda = codMoneda;
	        this.descMoneda = descMoneda;
	        this.monto = monto;
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
	    
	    public float getMontoNacional() {
	        return montoNacional;
	    }
	    
	    public void setMontoNacional(float montoNacional) {
	        this.montoNacional = montoNacional;
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
}
