package com.panzyma.nm.serviceproxy;

public class CCReciboColector {
    protected java.lang.String nombreSucursal;
    protected java.lang.String estado;
    protected java.lang.String numero;
    protected int fecha;
    protected float totalFacturas;
    protected float totalND;
    protected float totalIntereses;
    protected float totalNC;
    protected float totalDescPP;
    protected float totalDescOca;
    protected float totalDescProm;
    protected float totalRetenido;
    protected float totalOtro;
    protected float netoRecibo;
    protected long id;
    
    public CCReciboColector() {
    }
    
    public CCReciboColector(java.lang.String nombreSucursal, java.lang.String estado, java.lang.String numero, int fecha, float totalFacturas, float totalND, float totalIntereses, float totalNC, float totalDescPP, float totalDescOca, float totalDescProm, float totalRetenido, float totalOtro, float netoRecibo, long id) {
        this.nombreSucursal = nombreSucursal;
        this.estado = estado;
        this.numero = numero;
        this.fecha = fecha;
        this.totalFacturas = totalFacturas;
        this.totalND = totalND;
        this.totalIntereses = totalIntereses;
        this.totalNC = totalNC;
        this.totalDescPP = totalDescPP;
        this.totalDescOca = totalDescOca;
        this.totalDescProm = totalDescProm;
        this.totalRetenido = totalRetenido;
        this.totalOtro = totalOtro;
        this.netoRecibo = netoRecibo;
        this.id = id;
    }
    
    public java.lang.String getNombreSucursal() {
        return nombreSucursal;
    }
    
    public void setNombreSucursal(java.lang.String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }
    
    public java.lang.String getEstado() {
        return estado;
    }
    
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }
    
    public java.lang.String getNumero() {
        return numero;
    }
    
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }
    
    public int getFecha() {
        return fecha;
    }
    
    public void setFecha(int fecha) {
        this.fecha = fecha;
    }
    
    public float getTotalFacturas() {
        return totalFacturas;
    }
    
    public void setTotalFacturas(float totalFacturas) {
        this.totalFacturas = totalFacturas;
    }
    
    public float getTotalND() {
        return totalND;
    }
    
    public void setTotalND(float totalND) {
        this.totalND = totalND;
    }
    
    public float getTotalIntereses() {
        return totalIntereses;
    }
    
    public void setTotalIntereses(float totalIntereses) {
        this.totalIntereses = totalIntereses;
    }
    
    public float getTotalNC() {
        return totalNC;
    }
    
    public void setTotalNC(float totalNC) {
        this.totalNC = totalNC;
    }
    
    public float getTotalDescPP() {
        return totalDescPP;
    }
    
    public void setTotalDescPP(float totalDescPP) {
        this.totalDescPP = totalDescPP;
    }
    
    public float getTotalDescOca() {
        return totalDescOca;
    }
    
    public void setTotalDescOca(float totalDescOca) {
        this.totalDescOca = totalDescOca;
    }
    
    public float getTotalDescProm() {
        return totalDescProm;
    }
    
    public void setTotalDescProm(float totalDescProm) {
        this.totalDescProm = totalDescProm;
    }
    
    public float getTotalRetenido() {
        return totalRetenido;
    }
    
    public void setTotalRetenido(float totalRetenido) {
        this.totalRetenido = totalRetenido;
    }
    
    public float getTotalOtro() {
        return totalOtro;
    }
    
    public void setTotalOtro(float totalOtro) {
        this.totalOtro = totalOtro;
    }
    
    public float getNetoRecibo() {
        return netoRecibo;
    }
    
    public void setNetoRecibo(float netoRecibo) {
        this.netoRecibo = netoRecibo;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
}
