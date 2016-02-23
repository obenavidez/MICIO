package com.panzyma.nm.serviceproxy;

public class CDetalleFactura {
	
	protected String nombreProducto;
    protected int cantidad;
    protected int bonificado;
    protected int promocion;
    protected float precio;
    protected float subtotal;
    protected float descuento;
    protected float impuesto;
    protected float total;
    
    public CDetalleFactura() {
    }
    
    public CDetalleFactura(java.lang.String nombreProducto, int cantidad, int bonificado, int promocion, float precio, float subtotal, float descuento, float impuesto, float total) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.bonificado = bonificado;
        this.promocion = promocion;
        this.precio = precio;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.impuesto = impuesto;
        this.total = total;
    }
    
    public java.lang.String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(java.lang.String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public int getBonificado() {
        return bonificado;
    }
    
    public void setBonificado(int bonificado) {
        this.bonificado = bonificado;
    }
    
    public int getPromocion() {
        return promocion;
    }
    
    public void setPromocion(int promocion) {
        this.promocion = promocion;
    }
    
    public float getPrecio() {
        return precio;
    }
    
    public void setPrecio(float precio) {
        this.precio = precio;
    }
    
    public float getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }
    
    public float getDescuento() {
        return descuento;
    }
    
    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }
    
    public float getImpuesto() {
        return impuesto;
    }
    
    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }
    
    public float getTotal() {
        return total;
    }
    
    public void setTotal(float total) {
        this.total = total;
    }
}
