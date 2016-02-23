package com.panzyma.nm.serviceproxy;

public class Bonificacion {
    protected long objCategoriaClienteID;
    protected long objBonificacionID;
    protected int cantidad;
    protected int cantBonificacion;
    protected String categoriaCliente;
    
    public Bonificacion() {
    }
    
    public Bonificacion(long objCategoriaClienteID, long objBonificacionID, int cantidad, int cantBonificacion, String categoriaCliente) {
        this.objCategoriaClienteID = objCategoriaClienteID;
        this.objBonificacionID = objBonificacionID;
        this.cantidad = cantidad;
        this.cantBonificacion = cantBonificacion;
        this.categoriaCliente = categoriaCliente;
    }
    
    public String getCategoriaCliente() {
        return categoriaCliente;
    }
    
    public void setCategoriaCliente(String valor) {
        this.categoriaCliente = valor;
    }
    
    public long getObjCategoriaClienteID() {
        return objCategoriaClienteID;
    }
    
    public void setObjCategoriaClienteID(long objCategoriaClienteID) {
        this.objCategoriaClienteID = objCategoriaClienteID;
    }
    
    public long getObjBonificacionID() {
        return objBonificacionID;
    }
    
    public void setObjBonificacionID(long objBonificacionID) {
        this.objBonificacionID = objBonificacionID;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public int getCantBonificacion() {
        return cantBonificacion;
    }
    
    public void setCantBonificacion(int cantBonificacion) {
        this.cantBonificacion = cantBonificacion;
    }
}
