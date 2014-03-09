package com.panzyma.nm.serviceproxy;

import android.annotation.SuppressLint;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
@SuppressWarnings({"rawtypes"})
public final class CCNotaCredito implements KvmSerializable {
 
	private long Id;
    private java.lang.String NombreSucursal;
    private java.lang.String Estado;
    private java.lang.String Numero;
    private long Fecha;
    private long FechaVence;
    private java.lang.String Concepto;
    private float Monto;
    private java.lang.String NumRColAplic;
    private java.lang.String CodEstado;  
    private java.lang.String Descripcion;
    private int Referencia;
    private java.lang.String CodConcepto;

    public CCNotaCredito() {
      
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

    public void setNumRColAplic(java.lang.String numRColAplic) {
        this.NumRColAplic = numRColAplic;
    }

    public java.lang.String getNumRColAplic( ) {
        return this.NumRColAplic;
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

    public void setReferencia(int referencia) {
        this.Referencia = referencia;
    }

    public int getReferencia() {
        return this.Referencia;
    }

    public void setCodConcepto(java.lang.String codConcepto) {
        this.CodConcepto = codConcepto;
    }

    public java.lang.String getCodConcepto() {
        return this.CodConcepto;
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
        case 5: return Concepto;
        case 6: return new Float(Monto);
        case 7: return NumRColAplic;
        case 8: return CodEstado;
        case 9: return new Long(Id);
        case 10: return Descripcion;
        case 11: return new Integer(Referencia);
        case 12: return CodConcepto;
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
        case 5: Concepto = (java.lang.String) _obj; break;
        case 6: Monto = Float.parseFloat(_obj.toString()); break;
        case 7: NumRColAplic = (java.lang.String) _obj; break;
        case 8: CodEstado = (java.lang.String) _obj; break;
        case 9: Id = Long.parseLong(_obj.toString()); break;
        case 10: Descripcion = (java.lang.String) _obj; break;
        case 11: Referencia = Integer.parseInt(_obj.toString()); break;
        case 12: CodConcepto = (java.lang.String) _obj; break;
        }
    }
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
            _info.name = "Concepto";
            _info.type = java.lang.String.class; break;
        case 6:
            _info.name = "Monto";
            _info.type = Float.class; break;
        case 7:
            _info.name = "NumRColAplic";
            _info.type = java.lang.String.class; break;
        case 8:
            _info.name = "CodEstado";
            _info.type = java.lang.String.class; break;
        case 9:
            _info.name = "Id";
            _info.type = Long.class; break;
        case 10:
            _info.name = "Descripcion";
            _info.type = java.lang.String.class; break;
        case 11:
            _info.name = "Referencia";
            _info.type = Integer.class; break;
        case 12:
            _info.name = "CodConcepto";
            _info.type = java.lang.String.class; break;
        }
    }

}
