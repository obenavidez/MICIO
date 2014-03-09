package com.panzyma.nm.serviceproxy;
 

import android.annotation.SuppressLint;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 

public final class DetallePedido implements KvmSerializable{
    private long Id;
    private long objPedidoID;
    private long objProductoID;
    private java.lang.String CodProducto;
    private java.lang.String NombreProducto;
    private int CantidadOrdenada;
    private int CantidadBonificada;
    private long objBonificacionID;
    private boolean BonifEditada;
    private int CantidadBonificadaEditada;
    private float Precio;
    private float MontoPrecioEditado;
    private boolean PrecioEditado;
    private float Subtotal;
    private float Descuento;
    private float PorcImpuesto;
    private float Impuesto;
    private float Total;
    private int CantidadDespachada;
    private int CantidadADespachar;
    private int CantidadPromocion;

    public DetallePedido() {
         
    }
    public void setId(long id) {
        this.Id = id;
    }

    public long getId() {
        return this.Id;
    }

    public void setObjPedidoID(long objPedidoID) {
        this.objPedidoID = objPedidoID;
    }

    public long getObjPedidoID() {
        return this.objPedidoID;
    }

    public void setObjProductoID(long objProductoID) {
        this.objProductoID = objProductoID;
    }

    public long getObjProductoID() {
        return this.objProductoID;
    }

    public void setCodProducto(java.lang.String codProducto) {
        this.CodProducto = codProducto;
    }

    public java.lang.String getCodProducto() {
        return this.CodProducto;
    }

    public void setNombreProducto(java.lang.String nombreProducto) {
        this.NombreProducto = nombreProducto;
    }

    public java.lang.String getNombreProducto() {
        return this.NombreProducto;
    }

    public void setCantidadOrdenada(int cantidadOrdenada) {
        this.CantidadOrdenada = cantidadOrdenada;
    }

    public int getCantidadOrdenada() {
        return this.CantidadOrdenada;
    }

    public void setCantidadBonificada(int cantidadBonificada) {
        this.CantidadBonificada = cantidadBonificada;
    }

    public int getCantidadBonificada() {
        return this.CantidadBonificada;
    }

    public void setObjBonificacionID(long objBonificacionID) {
        this.objBonificacionID = objBonificacionID;
    }

    public long getObjBonificacionID() {
        return this.objBonificacionID;
    }

    public void setBonifEditada(boolean bonifEditada) {
        this.BonifEditada = bonifEditada;
    }

    public boolean getBonifEditada() {
        return this.BonifEditada;
    }

    public void setCantidadBonificadaEditada(int cantidadBonificadaEditada) {
        this.CantidadBonificadaEditada = cantidadBonificadaEditada;
    }

    public int getCantidadBonificadaEditada() {
        return this.CantidadBonificadaEditada;
    }

    public void setPrecio(float precio) {
        this.Precio = precio;
    }

    public float getPrecio(float precio) {
        return this.Precio;
    }

    public void setMontoPrecioEditado(float montoPrecioEditado) {
        this.MontoPrecioEditado = montoPrecioEditado;
    }

    public float getMontoPrecioEditado() {
        return this.MontoPrecioEditado;
    }

    public void setPrecioEditado(boolean precioEditado) {
        this.PrecioEditado = precioEditado;
    }

    public boolean getPrecioEditado() {
        return this.PrecioEditado;
    }

    public void setSubtotal(float subtotal) {
        this.Subtotal = subtotal;
    }

    public float getSubtotal() {
        return this.Subtotal;
    }

    public void setDescuento(float descuento) {
        this.Descuento = descuento;
    }

    public float getDescuento() {
        return this.Descuento;
    }

    public void setPorcImpuesto(float porcImpuesto) {
        this.PorcImpuesto = porcImpuesto;
    }

    public float getPorcImpuesto() {
        return this.PorcImpuesto;
    }

    public void setImpuesto(float impuesto) {
        this.Impuesto = impuesto;
    }

    public float getImpuesto() {
        return this.Impuesto;
    }

    public void setTotal(float total) {
        this.Total = total;
    }

    public float getTotal() {
        return this.Total;
    }

    public void setCantidadDespachada(int cantidadDespachada) {
        this.CantidadDespachada = cantidadDespachada;
    }

    public int getCantidadDespachada() {
        return this.CantidadDespachada;
    }

    public void setCantidadADespachar(int cantidadADespachar) {
        this.CantidadADespachar = cantidadADespachar;
    }

    public int getCantidadADespachar() {
        return this.CantidadADespachar;
    }

    public void setCantidadPromocion(int cantidadPromocion) {
        this.CantidadPromocion = cantidadPromocion;
    }

    public int getCantidadPromocion() {
        return this.CantidadPromocion;
    }

    @Override
	public int getPropertyCount() {
        return 21;
    }

    @Override
	@SuppressLint("UseValueOf")
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return new Long(Id);
        case 1: return new Long(objPedidoID);
        case 2: return new Long(objProductoID);
        case 3: return CodProducto;
        case 4: return NombreProducto;
        case 5: return new Integer(CantidadOrdenada);
        case 6: return new Integer(CantidadBonificada);
        case 7: return new Long(objBonificacionID);
        case 8: return new Boolean(BonifEditada);
        case 9: return new Integer(CantidadBonificadaEditada);
        case 10: return new Float(Precio);
        case 11: return new Float(MontoPrecioEditado);
        case 12: return new Boolean(PrecioEditado);
        case 13: return new Float(Subtotal);
        case 14: return new Float(Descuento);
        case 15: return new Float(PorcImpuesto);
        case 16: return new Float(Impuesto);
        case 17: return new Float(Total);
        case 18: return new Integer(CantidadDespachada);
        case 19: return new Integer(CantidadADespachar);
        case 20: return new Integer(CantidadPromocion);
        }
        return null;
    }

    @Override
	public void setProperty(int _index, Object _obj) {
        switch(_index)  {
        case 0:  Id = Long.parseLong(_obj.toString()); break;
        case 1:  objPedidoID = Long.parseLong(_obj.toString()); break;
        case 2:  objProductoID = Long.parseLong(_obj.toString()); break;
        case 3:  CodProducto = (java.lang.String) _obj; break;
        case 4:  NombreProducto = (java.lang.String) _obj; break;
        case 5:  CantidadOrdenada = Integer.parseInt(_obj.toString()); break;
        case 6:  CantidadBonificada = Integer.parseInt(_obj.toString()); break;
        case 7:  objBonificacionID = Long.parseLong(_obj.toString()); break;
        case 8:  BonifEditada = "true".equals(_obj.toString()); break;
        case 9:  CantidadBonificadaEditada = Integer.parseInt(_obj.toString()); break;
        case 10: Precio = Float.parseFloat(_obj.toString()); break;
        case 11: MontoPrecioEditado = Float.parseFloat(_obj.toString()); break;
        case 12: PrecioEditado = "true".equals(_obj.toString()); break;
        case 13: Subtotal = Float.parseFloat(_obj.toString()); break;
        case 14: Descuento = Float.parseFloat(_obj.toString()); break;
        case 15: PorcImpuesto = Float.parseFloat(_obj.toString()); break;
        case 16: Impuesto = Float.parseFloat(_obj.toString()); break;
        case 17: Total = Float.parseFloat(_obj.toString()); break;
        case 18: CantidadDespachada = Integer.parseInt(_obj.toString()); break;
        case 19: CantidadADespachar = Integer.parseInt(_obj.toString()); break;
        case 20: CantidadPromocion = Integer.parseInt(_obj.toString()); break;
        }
    }

    @Override
	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "Id";
            _info.type = Long.class; break;
        case 1:
            _info.name = "objPedidoID";
            _info.type = Long.class; break;
        case 2:
            _info.name = "objProductoID";
            _info.type = Long.class; break;
        case 3:
            _info.name = "CodProducto";
            _info.type = java.lang.String.class; break;
        case 4:
            _info.name = "NombreProducto";
            _info.type = java.lang.String.class; break;
        case 5:
            _info.name = "CantidadOrdenada";
            _info.type = Integer.class; break;
        case 6:
            _info.name = "CantidadBonificada";
            _info.type = Integer.class; break;
        case 7:
            _info.name = "objBonificacionID";
            _info.type = Long.class; break;
        case 8:
            _info.name = "BonifEditada";
            _info.type = Boolean.class; break;
        case 9:
            _info.name = "CantidadBonificadaEditada";
            _info.type = Integer.class; break;
        case 10:
            _info.name = "Precio";
            _info.type = Float.class; break;
        case 11:
            _info.name = "MontoPrecioEditado";
            _info.type = Float.class; break;
        case 12:
            _info.name = "PrecioEditado";
            _info.type = Boolean.class; break;
        case 13:
            _info.name = "Subtotal";
            _info.type = Float.class; break;
        case 14:
            _info.name = "Descuento";
            _info.type = Float.class; break;
        case 15:
            _info.name = "PorcImpuesto";
            _info.type = Float.class; break;
        case 16:
            _info.name = "Impuesto";
            _info.type = Float.class; break;
        case 17:
            _info.name = "Total";
            _info.type = Float.class; break;
        case 18:
            _info.name = "CantidadDespachada";
            _info.type = Integer.class; break;
        case 19:
            _info.name = "CantidadADespachar";
            _info.type = Integer.class; break;
        case 20:
            _info.name = "CantidadPromocion";
            _info.type = Integer.class; break;
        }
    }

}
