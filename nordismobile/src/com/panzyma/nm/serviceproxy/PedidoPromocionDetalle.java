package com.panzyma.nm.serviceproxy;
 
import android.annotation.SuppressLint;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 

public final class PedidoPromocionDetalle implements KvmSerializable {
    
	private long objProductoID;
    private java.lang.String NombreProducto;
    private int CantidadEntregada;
    private float Descuento;

    public PedidoPromocionDetalle() {
 
    }
    public void setObjProductoID(long objProductoID) {
        this.objProductoID = objProductoID;
    }

    public long getObjProductoID() {
        return this.objProductoID;
    }

    public void setNombreProducto(java.lang.String nombreProducto) {
        this.NombreProducto = nombreProducto;
    }

    public java.lang.String getNombreProducto() {
        return this.NombreProducto;
    }

    public void setCantidadEntregada(int cantidadEntregada) {
        this.CantidadEntregada = cantidadEntregada;
    }

    public int getCantidadEntregada() {
        return this.CantidadEntregada;
    }

    public void setDescuento(float descuento) {
        this.Descuento = descuento;
    }

    public float getDescuento() {
        return this.Descuento;
    }

    @Override
	public int getPropertyCount() {
        return 4;
    }

    @Override
	@SuppressLint("UseValueOf")
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return new Long(objProductoID);
        case 1: return NombreProducto;
        case 2: return new Integer(CantidadEntregada);
        case 3: return new Float(Descuento);
        }
        return null;
    }

    @Override
	public void setProperty(int _index, Object _obj) {
        switch(_index)  {
        case 0: objProductoID = Long.parseLong(_obj.toString()); break;
        case 1: NombreProducto = (java.lang.String) _obj; break;
        case 2: CantidadEntregada = Integer.parseInt(_obj.toString()); break;
        case 3: Descuento = Float.parseFloat(_obj.toString()); break;
        }
    }

    @Override
	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "objProductoID";
            _info.type = Long.class; break;
        case 1:
            _info.name = "NombreProducto";
            _info.type = java.lang.String.class; break;
        case 2:
            _info.name = "CantidadEntregada";
            _info.type = Integer.class; break;
        case 3:
            _info.name = "Descuento";
            _info.type = Float.class; break;
        }
    }

}
