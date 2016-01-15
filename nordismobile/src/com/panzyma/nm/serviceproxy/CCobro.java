package com.panzyma.nm.serviceproxy;

public class CCobro {
	
		protected java.lang.String numeroCentral;
	    protected java.lang.String nombreCliente;
	    protected java.lang.String fecha;
	    protected float totalRecibo;
	    protected java.lang.String referencia;
	
	 	public CCobro() {
	    }
	    
	    public CCobro(java.lang.String numeroCentral, java.lang.String nombreCliente, java.lang.String fecha, float totalRecibo, java.lang.String referencia) {
	        this.numeroCentral = numeroCentral;
	        this.nombreCliente = nombreCliente;
	        this.fecha = fecha;
	        this.totalRecibo = totalRecibo;
	        this.referencia = referencia;
	    }
	    
	    public java.lang.String getNumeroCentral() {
	        return numeroCentral;
	    }
	    
	    public void setNumeroCentral(java.lang.String numeroCentral) {
	        this.numeroCentral = numeroCentral;
	    }
	    
	    public java.lang.String getNombreCliente() {
	        return nombreCliente;
	    }
	    
	    public void setNombreCliente(java.lang.String nombreCliente) {
	        this.nombreCliente = nombreCliente;
	    }
	    
	    public java.lang.String getFecha() {
	        return fecha;
	    }
	    
	    public void setFecha(java.lang.String fecha) {
	        this.fecha = fecha;
	    }
	    
	    public float getTotalRecibo() {
	        return totalRecibo;
	    }
	    
	    public void setTotalRecibo(float totalRecibo) {
	        this.totalRecibo = totalRecibo;
	    }
	    
	    public java.lang.String getReferencia() {
	        return referencia;
	    }
	    
	    public void setReferencia(java.lang.String referencia) {
	        this.referencia = referencia;
	    }
}
