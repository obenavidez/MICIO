package com.panzyma.nm.serviceproxy;

public class TasaCambio {
    protected java.lang.String CodMoneda;
    protected int Fecha;
    protected float Tasa;
    
    public TasaCambio() {
    }
    
    public TasaCambio(java.lang.String codMoneda, int fecha, float tasa) {
        this.CodMoneda = codMoneda;
        this.Fecha = fecha;
        this.Tasa = tasa;
    }
    
    public java.lang.String getCodMoneda() {
        return CodMoneda;
    }
    
    public void setCodMoneda(java.lang.String codMoneda) {
        this.CodMoneda = codMoneda;
    }
    
    public int getFecha() {
        return Fecha;
    }
    
    public void setFecha(int fecha) {
        this.Fecha = fecha;
    }
    
    public float getTasa() {
        return Tasa;
    }
    
    public void setTasa(float tasa) {
        this.Tasa = tasa;
    }
}
