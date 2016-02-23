package com.panzyma.nm.serviceproxy;

import com.panzyma.nm.interfaces.GenericDocument;

public class CCPedido implements GenericDocument {
	
    protected long Id;
    protected java.lang.String Numero;
    protected java.lang.String Referencia;
    protected java.lang.String Tipo;
    protected int Fecha;
    protected java.lang.String TipoPrecio;
    protected float Total;
    protected java.lang.String Estado;
    protected java.lang.String CausaEstado;
    protected java.lang.String CodEstado;
    
    public CCPedido() {
    }
    
    public CCPedido(long id, java.lang.String numero, java.lang.String referencia, java.lang.String tipo, int fecha, java.lang.String tipoPrecio, float total, java.lang.String estado, java.lang.String causaEstado, java.lang.String codEstado) {
        this.Id = id;
        this.Numero = numero;
        this.Referencia = referencia;
        this.Tipo = tipo;
        this.Fecha = fecha;
        this.TipoPrecio = tipoPrecio;
        this.Total = total;
        this.Estado = estado;
        this.CausaEstado = causaEstado;
        this.CodEstado = codEstado;
    }
    
    public long getId() {
        return Id;
    }
    
    public void setId(long id) {
        this.Id = id;
    }
    
    public java.lang.String getNumero() {
        return Numero;
    }
    
    public void setNumero(java.lang.String numero) {
        this.Numero = numero;
    }
    
    public java.lang.String getReferencia() {
        return Referencia;
    }
    
    public void setReferencia(java.lang.String referencia) {
        this.Referencia = referencia;
    }
    
    public java.lang.String getTipo() {
        return Tipo;
    }
    
    public void setTipo(java.lang.String tipo) {
        this.Tipo = tipo;
    }
    
    public int getFecha() {
        return Fecha;
    }
    
    public void setFecha(int fecha) {
        this.Fecha = fecha;
    }
    
    public java.lang.String getTipoPrecio() {
        return TipoPrecio;
    }
    
    public void setTipoPrecio(java.lang.String tipoPrecio) {
        this.TipoPrecio = tipoPrecio;
    }
    
    public float getTotal() {
        return Total;
    }
    
    public void setTotal(float total) {
        this.Total = total;
    }
    
    public java.lang.String getEstado() {
        return Estado;
    }
    
    public void setEstado(java.lang.String estado) {
        this.Estado = estado;
    }
    
    public java.lang.String getCausaEstado() {
        return CausaEstado;
    }
    
    public void setCausaEstado(java.lang.String causaEstado) {
        this.CausaEstado = causaEstado;
    }
    
    public java.lang.String getCodEstado() {
        return CodEstado;
    }
    
    public void setCodEstado(java.lang.String codEstado) {
        this.CodEstado = codEstado;
    }

	@Override
	public String getDocumentNumber() {		
		return getNumero();
	}

	@Override
	public Object getObject() {
		return this;
	}
}
