package com.panzyma.nm.serviceproxy;

/*
 * ProdBase.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */
 

/**
 * 
 */
public class ProdBase {
    private long objProductoID;
    private int cantidadMinima;
    private float montoMinimo;
    private float montoMaximo;
    private boolean aplicaBonificacion;
    private String tipoDescuento;
    private float descuento;
    
    public ProdBase() {    }
    
    public float getMontoMinimo() {
        return montoMinimo;
    }
    
    public void setMontoMinimo(float valor) {
        montoMinimo = valor;
    }
    
    public float getMontoMaximo() {
        return montoMaximo;
    }
    
    public void setMontoMaximo(float valor) {
        montoMaximo = valor;
    }
    
    public boolean getAplicaBonificacion() {
        return aplicaBonificacion;
    }
    
    public void setAplicaBonificacion(boolean valor) {
        aplicaBonificacion = valor;
    }
    
    public String getTipoDescuento() {
        return tipoDescuento;
    }
    
    public void setTipoDescuento(String valor) {
        tipoDescuento = valor;
    }
    
    public float getDescuento() {
        return descuento;
    }
    
    public void setDescuento(float valor) {
        descuento = valor;
    }
    
    public long getObjProductoID() {
        return objProductoID;
    }
    
    public void setObjProductoID(long valor) {
        objProductoID = valor;
    }
    
    public int getCantidadMinima() {
        return cantidadMinima;
    }
    
    public void setCantidadMinima(int valor) {
        cantidadMinima = valor;
    }
} 
