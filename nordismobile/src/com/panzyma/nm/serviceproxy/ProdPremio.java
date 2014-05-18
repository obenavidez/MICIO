package com.panzyma.nm.serviceproxy;

public class ProdPremio {
    private long objProductoID;
    private int cantidad;
    private float monto;
    private String nombreProducto;
    
    public ProdPremio() {    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String valor) {
        nombreProducto = valor;
    }
     
    public float getMonto() {
        return monto;
    }
    
    public void setMonto(float valor) {
        monto = valor;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int valor) {
        cantidad = valor;
    }
    
    public long getObjProductoID() {
        return objProductoID;
    }
    
    public void setObjProductoID(long valor) {
        objProductoID = valor;
    }
} 
