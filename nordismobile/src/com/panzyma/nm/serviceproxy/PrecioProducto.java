package com.panzyma.nm.serviceproxy;



public class PrecioProducto {
    protected long objTipoPrecioID;
    protected int minimo;
    protected int maximo;
    protected float precio;
    protected String descTipoPrecio;
    
    public PrecioProducto() {
    }
    
    public PrecioProducto(long objTipoPrecioID, int minimo, int maximo, float precio) {
        this.objTipoPrecioID = objTipoPrecioID;
        this.minimo = minimo;
        this.maximo = maximo;
        this.precio = precio;
    }
    
    public String getDescTipoPrecio() {
        return descTipoPrecio;
    }
    
    public void setDescTipoPrecio(String valor) {
        this.descTipoPrecio = valor;
    }
    
    public long getObjTipoPrecioID() {
        return objTipoPrecioID;
    }
    
    public void setObjTipoPrecioID(long objTipoPrecioID) {
        this.objTipoPrecioID = objTipoPrecioID;
    }
    
    public int getMinimo() {
        return minimo;
    }
    
    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }
    
    public int getMaximo() {
        return maximo;
    }
    
    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }
    
    public float getPrecio() {
        return precio;
    }
    
    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
