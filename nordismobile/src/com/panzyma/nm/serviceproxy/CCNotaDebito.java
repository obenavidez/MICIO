package com.panzyma.nm.serviceproxy;

import android.annotation.SuppressLint;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 
public final class CCNotaDebito implements KvmSerializable {
 
	private long Id;
    private java.lang.String NombreSucursal; 
    private java.lang.String Estado; 
    private java.lang.String Numero; 
    private long Fecha; 
    private long FechaVence; 
    private int Dias; 
    private java.lang.String Concepto; 
    private float Monto; 
    private float MontoAbonado; 
    private float Saldo; 
    private java.lang.String CodEstado;  
    private java.lang.String Descripcion;

    public CCNotaDebito() { 
    }
    public void setNombreSucursal(java.lang.String nombreSucursal) {
        this.NombreSucursal = nombreSucursal;
    }

    public java.lang.String getNombreSucursal() {
        return this.NombreSucursal;
    }

    public void setEstado(java.lang.String estado) {
        this.Estado = estado;
    }

    public java.lang.String getEstado() {
        return this.Estado;
    }

    public void setNumero(java.lang.String numero) {
        this.Numero = numero;
    }

    public java.lang.String getNumero() {
        return this.Numero;
    }

    public void setFecha(long fecha) {
        this.Fecha = fecha;
    }

    public long getFecha() {
        return this.Fecha;
    }

    public void setFechaVence(long fechaVence) {
        this.FechaVence = fechaVence;
    }

    public long getFechaVence() {
        return this.FechaVence;
    }

    public void setDias(int dias) {
        this.Dias = dias;
    }

    public int getDias() {
        return this.Dias;
    }

    public void setConcepto(java.lang.String concepto) {
        this.Concepto = concepto;
    }

    public java.lang.String getConcepto() {
        return this.Concepto;
    }

    public void setMonto(float monto) {
        this.Monto = monto;
    }

    public float getMonto() {
        return this.Monto;
    }

    public void setMontoAbonado(float montoAbonado) {
        this.MontoAbonado = montoAbonado;
    }

    public float getMontoAbonado() {
        return this.MontoAbonado;
    }

    public void setSaldo(float saldo) {
        this.Saldo = saldo;
    }

    public float getSaldo() {
        return this.Saldo;
    }

    public void setCodEstado(java.lang.String codEstado) {
        this.CodEstado = codEstado;
    }

    public java.lang.String getCodEstado() {
        return this.CodEstado;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public long getId() {
        return this.Id;
    }

    public void setDescripcion(java.lang.String descripcion) {
        this.Descripcion = descripcion;
    }

    public java.lang.String getDescripcion() {
        return this.Descripcion;
    }
    @Override
	public int getPropertyCount() {
        return 13;
    }

    
	@SuppressLint("UseValueOf")
	@Override
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return NombreSucursal;
        case 1: return Estado;
        case 2: return Numero;
        case 3: return new Long(Fecha);
        case 4: return new Long(FechaVence);
        case 5: return new Integer(Dias);
        case 6: return Concepto;
        case 7: return new Float(Monto);
        case 8: return new Float(MontoAbonado);
        case 9: return new Float(Saldo);
        case 10: return CodEstado;
        case 11: return new Long(Id);
        case 12: return Descripcion;
        }
        return null;
    }

	@Override
	public void setProperty(int _index, Object _obj) {
        switch(_index)  {
        case 0: NombreSucursal = (java.lang.String) _obj; break;
        case 1: Estado = (java.lang.String) _obj; break;
        case 2: Numero = (java.lang.String) _obj; break;
        case 3: Fecha = Long.parseLong(_obj.toString()); break;
        case 4: FechaVence = Long.parseLong(_obj.toString()); break;
        case 5: Dias = Integer.parseInt(_obj.toString()); break;
        case 6: Concepto = (java.lang.String) _obj; break;
        case 7: Monto = Float.parseFloat(_obj.toString()); break;
        case 8: MontoAbonado = Float.parseFloat(_obj.toString()); break;
        case 9: Saldo = Float.parseFloat(_obj.toString()); break;
        case 10: CodEstado = (java.lang.String) _obj; break;
        case 11: Id = Long.parseLong(_obj.toString()); break;
        case 12: Descripcion = (java.lang.String) _obj; break;
        }
    }

    
	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "NombreSucursal";
            _info.type = java.lang.String.class; break;
        case 1:
            _info.name = "Estado";
            _info.type = java.lang.String.class; break;
        case 2:
            _info.name = "Numero";
            _info.type = java.lang.String.class; break;
        case 3:
            _info.name = "Fecha";
            _info.type = Long.class; break;
        case 4:
            _info.name = "FechaVence";
            _info.type = Long.class; break;
        case 5:
            _info.name = "Dias";
            _info.type = Integer.class; break;
        case 6:
            _info.name = "Concepto";
            _info.type = java.lang.String.class; break;
        case 7:
            _info.name = "Monto";
            _info.type = Float.class; break;
        case 8:
            _info.name = "MontoAbonado";
            _info.type = Float.class; break;
        case 9:
            _info.name = "Saldo";
            _info.type = Float.class; break;
        case 10:
            _info.name = "CodEstado";
            _info.type = java.lang.String.class; break;
        case 11:
            _info.name = "Id";
            _info.type = Long.class; break;
        case 12:
            _info.name = "Descripcion";
            _info.type = java.lang.String.class; break;
        }
    }

}
