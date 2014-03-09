package com.panzyma.nm.serviceproxy;
 

import android.annotation.SuppressLint;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 

public final class PedidoPromocion implements KvmSerializable {
  
	private long objPromocionID;
    private float Descuento;
    private PedidoPromocionDetalle[] Detalles;
    private java.lang.String CodigoPromocion;
    private java.lang.String NombrePromocion;

    public PedidoPromocion() { 
    }
    public void setObjPromocionID(long objPromocionID) {
        this.objPromocionID = objPromocionID;
    }

    public long getObjPromocionID() {
        return this.objPromocionID;
    }

    public void setDescuento(float descuento) {
        this.Descuento = descuento;
    }

    public float getDescuento() {
        return this.Descuento;
    }

    public void setDetalles(PedidoPromocionDetalle[] detalles) {
        this.Detalles = detalles;
    }

    public PedidoPromocionDetalle[] getDetalles() {
        return this.Detalles;
    }

    public void setCodigoPromocion(java.lang.String codigoPromocion) {
        this.CodigoPromocion = codigoPromocion;
    }

    public java.lang.String getCodigoPromocion() {
        return this.CodigoPromocion;
    }

    public void setNombrePromocion(java.lang.String nombrePromocion) {
        this.NombrePromocion = nombrePromocion;
    }

    public java.lang.String getNombrePromocion() {
        return this.NombrePromocion;
    }

    @Override
	public int getPropertyCount() {
        return 5;
    }

    @Override
	@SuppressLint("UseValueOf")
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return new Long(objPromocionID);
        case 1: return new Float(Descuento);
        case 2: return Detalles;
        case 3: return CodigoPromocion;
        case 4: return NombrePromocion;
        }
        return null;
    }

    @Override
	public void setProperty(int _index, Object _obj) {
        switch(_index)  {
        case 0: objPromocionID = Long.parseLong(_obj.toString()); break;
        case 1: Descuento = Float.parseFloat(_obj.toString()); break;
        case 3: CodigoPromocion = (java.lang.String) _obj; break;
        case 4: NombrePromocion = (java.lang.String) _obj; break;
        }
    }

    @Override
	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "objPromocionID";
            _info.type = Long.class; break;
        case 1:
            _info.name = "Descuento";
            _info.type = Float.class; break;
        case 2:
            _info.name = "Detalles";
            _info.type=PedidoPromocionDetalle[].class; break;
        case 3:
            _info.name = "CodigoPromocion";
            _info.type = java.lang.String.class; break;
        case 4:
            _info.name = "NombrePromocion";
            _info.type = java.lang.String.class; break;
        }
    }

}
