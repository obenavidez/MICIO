package com.panzyma.nm.serviceproxy;

public class CCPedido {
    protected long id;
    protected java.lang.String numero;
    protected java.lang.String referencia;
    protected java.lang.String tipo;
    protected int fecha;
    protected java.lang.String tipoPrecio;
    protected float total;
    protected java.lang.String estado;
    protected java.lang.String causaEstado;
    protected java.lang.String codEstado;
    
    public CCPedido() {
    }
    
    public CCPedido(long id, java.lang.String numero, java.lang.String referencia, java.lang.String tipo, int fecha, java.lang.String tipoPrecio, float total, java.lang.String estado, java.lang.String causaEstado, java.lang.String codEstado) {
        this.id = id;
        this.numero = numero;
        this.referencia = referencia;
        this.tipo = tipo;
        this.fecha = fecha;
        this.tipoPrecio = tipoPrecio;
        this.total = total;
        this.estado = estado;
        this.causaEstado = causaEstado;
        this.codEstado = codEstado;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public java.lang.String getNumero() {
        return numero;
    }
    
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }
    
    public java.lang.String getReferencia() {
        return referencia;
    }
    
    public void setReferencia(java.lang.String referencia) {
        this.referencia = referencia;
    }
    
    public java.lang.String getTipo() {
        return tipo;
    }
    
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }
    
    public int getFecha() {
        return fecha;
    }
    
    public void setFecha(int fecha) {
        this.fecha = fecha;
    }
    
    public java.lang.String getTipoPrecio() {
        return tipoPrecio;
    }
    
    public void setTipoPrecio(java.lang.String tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }
    
    public float getTotal() {
        return total;
    }
    
    public void setTotal(float total) {
        this.total = total;
    }
    
    public java.lang.String getEstado() {
        return estado;
    }
    
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }
    
    public java.lang.String getCausaEstado() {
        return causaEstado;
    }
    
    public void setCausaEstado(java.lang.String causaEstado) {
        this.causaEstado = causaEstado;
    }
    
    public java.lang.String getCodEstado() {
        return codEstado;
    }
    
    public void setCodEstado(java.lang.String codEstado) {
        this.codEstado = codEstado;
    }
}
