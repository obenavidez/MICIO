package com.panzyma.nm.serviceproxy;
 

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

@SuppressWarnings({"rawtypes"})
public final class Pedido extends SoapObject implements Serializable {
    private long Id;
    private int NumeroMovil;
    private int NumeroCentral;
    private java.lang.String Tipo; 
    private int Fecha; 
    private long objClienteID; 
    private java.lang.String NombreCliente;
    private long objSucursalID;
    private java.lang.String NombreSucursal;
    private long objTipoPrecioVentaID;
    private java.lang.String CodTipoPrecio;
    private java.lang.String DescTipoPrecio;
    private long objVendedorID;
    private boolean BonificacionEspecial;
    private java.lang.String BonificacionSolicitada;
    private boolean PrecioEspecial;
    private java.lang.String PrecioSolicitado;
    private boolean PedidoCondicionado;
    private java.lang.String Condicion;
    private float Subtotal;
    private float Descuento;
    private float Impuesto;
    private float Total;
    private long objEstadoID;
    private java.lang.String CodEstado;
    private java.lang.String DescEstado;
    private long objCausaEstadoID;
    private java.lang.String CodCausaEstado;
    private java.lang.String DescCausaEstado;
    private java.lang.String NombreVendedor;
    private DetallePedido[] Detalles;
    private PedidoPromocion[] PromocionesAplicadas;
    private java.lang.String Nota;
    private boolean Exento;
    private java.lang.String AutorizacionDGI;
    

    public Pedido() {
        super("", "");
    }
    public void setId(long id) {
        this.Id = id;
    }

    public long getId() {
        return this.Id;
    }

    public void setNumeroMovil(int numeroMovil) {
        this.NumeroMovil = numeroMovil;
    }

    public int getNumeroMovil() {
        return this.NumeroMovil;
    }

    public void setNumeroCentral(int numeroCentral) {
        this.NumeroCentral = numeroCentral;
    }

    public int getNumeroCentral() {
        return this.NumeroCentral;
    }

    public void setTipo(java.lang.String tipo) {
        this.Tipo = tipo;
    }

    public java.lang.String getTipo() {
        return this.Tipo;
    }

    public void setFecha(int fecha) {
        this.Fecha = fecha;
    }

    public int getFecha() {
        return this.Fecha;
    }

    public void setObjClienteID(long objClienteID) {
        this.objClienteID = objClienteID;
    }

    public long getObjClienteID() {
        return this.objClienteID;
    }

    public void setNombreCliente(java.lang.String nombreCliente) {
        this.NombreCliente = nombreCliente;
    }

    public java.lang.String getNombreCliente() {
        return this.NombreCliente;
    }

    public void setObjSucursalID(long objSucursalID) {
        this.objSucursalID = objSucursalID;
    }

    public long getObjSucursalID() {
        return this.objSucursalID;
    }

    public void setNombreSucursal(java.lang.String nombreSucursal) {
        this.NombreSucursal = nombreSucursal;
    }

    public java.lang.String getNombreSucursal() {
        return this.NombreSucursal;
    }

    public void setObjTipoPrecioVentaID(long objTipoPrecioVentaID) {
        this.objTipoPrecioVentaID = objTipoPrecioVentaID;
    }

    public long getObjTipoPrecioVentaID() {
        return this.objTipoPrecioVentaID;
    }

    public void setCodTipoPrecio(java.lang.String codTipoPrecio) {
        this.CodTipoPrecio = codTipoPrecio;
    }

    public java.lang.String getCodTipoPrecio() {
        return this.CodTipoPrecio;
    }

    public void setDescTipoPrecio(java.lang.String descTipoPrecio) {
        this.DescTipoPrecio = descTipoPrecio;
    }

    public java.lang.String getDescTipoPrecio() {
        return this.DescTipoPrecio;
    }

    public void setObjVendedorID(long objVendedorID) {
        this.objVendedorID = objVendedorID;
    }

    public long getObjVendedorID() {
        return this.objVendedorID;
    }

    public void setBonificacionEspecial(boolean bonificacionEspecial) {
        this.BonificacionEspecial = bonificacionEspecial;
    }

    public boolean getBonificacionEspecial() {
        return this.BonificacionEspecial;
    }

    public void setBonificacionSolicitada(java.lang.String bonificacionSolicitada) {
        this.BonificacionSolicitada = bonificacionSolicitada;
    }

    public java.lang.String getBonificacionSolicitada() {
        return this.BonificacionSolicitada;
    }

    public void setPrecioEspecial(boolean precioEspecial) {
        this.PrecioEspecial = precioEspecial;
    }

    public boolean getPrecioEspecial() {
        return this.PrecioEspecial;
    }

    public void setPrecioSolicitado(java.lang.String precioSolicitado) {
        this.PrecioSolicitado = precioSolicitado;
    }

    public java.lang.String getPrecioSolicitado() {
        return this.PrecioSolicitado;
    }

    public void setPedidoCondicionado(boolean pedidoCondicionado) {
        this.PedidoCondicionado = pedidoCondicionado;
    }

    public boolean getPedidoCondicionado() {
        return this.PedidoCondicionado;
    }

    public void setCondicion(java.lang.String condicion) {
        this.Condicion = condicion;
    }

    public java.lang.String getCondicion() {
        return this.Condicion;
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

    public void setObjEstadoID(long objEstadoID) {
        this.objEstadoID = objEstadoID;
    }

    public long getObjEstadoID() {
        return this.objEstadoID;
    }

    public void setCodEstado(java.lang.String codEstado) {
        this.CodEstado = codEstado;
    }

    public java.lang.String getCodEstado() {
        return this.CodEstado;
    }

    public void setDescEstado(java.lang.String descEstado) {
        this.DescEstado = descEstado;
    }

    public java.lang.String getDescEstado() {
        return this.DescEstado;
    }

    public void setObjCausaEstadoID(long objCausaEstadoID) {
        this.objCausaEstadoID = objCausaEstadoID;
    }

    public long getObjCausaEstadoID() {
        return this.objCausaEstadoID;
    }

    public void setCodCausaEstado(java.lang.String codCausaEstado) {
        this.CodCausaEstado = codCausaEstado;
    }

    public java.lang.String getCodCausaEstado() {
        return this.CodCausaEstado;
    }

    public void setDescCausaEstado(java.lang.String descCausaEstado) {
        this.DescCausaEstado = descCausaEstado;
    }

    public java.lang.String getDescCausaEstado() {
        return this.DescCausaEstado;
    }

    public void setNombreVendedor(java.lang.String nombreVendedor) {
        this.NombreVendedor = nombreVendedor;
    }

    public java.lang.String getNombreVendedor() {
        return this.NombreVendedor;
    }

    public void setDetalles(DetallePedido[] detalles) {
        this.Detalles = detalles;
    }

    public DetallePedido[] getDetalles() {
        return this.Detalles;
    }

    public void setPromocionesAplicadas(PedidoPromocion[] promocionesAplicadas) {
        this.PromocionesAplicadas = promocionesAplicadas;
    }

    public PedidoPromocion[] getPromocionesAplicadas() {
        return this.PromocionesAplicadas;
    }

    public void setNota(java.lang.String nota) {
        this.Nota = nota;
    }

    public java.lang.String getNota() {
        return this.Nota;
    }

    public void setExento(boolean exento) {
        this.Exento = exento;
    }

    public boolean isExento() {
        return this.Exento;
    }

    public void setAutorizacionDGI(java.lang.String autorizacionDGI) {
        this.AutorizacionDGI = autorizacionDGI;
    }

    public java.lang.String getAutorizacionDGI() {
        return this.AutorizacionDGI;
    }

    @Override
	public int getPropertyCount() {
        return 35;
    }

    @Override
	@SuppressLint("UseValueOf")
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return new Long(Id);
        case 1: return new Integer(NumeroMovil);
        case 2: return new Integer(NumeroCentral);
        case 3: return Tipo;
        case 4: return new Integer(Fecha);
        case 5: return new Long(objClienteID);
        case 6: return NombreCliente;
        case 7: return new Long(objSucursalID);
        case 8: return NombreSucursal;
        case 9: return new Long(objTipoPrecioVentaID);
        case 10: return CodTipoPrecio;
        case 11: return DescTipoPrecio;
        case 12: return new Long(objVendedorID);
        case 13: return new Boolean(BonificacionEspecial);
        case 14: return BonificacionSolicitada;
        case 15: return new Boolean(PrecioEspecial);
        case 16: return PrecioSolicitado;
        case 17: return new Boolean(PedidoCondicionado);
        case 18: return Condicion;
        case 19: return new Float(Subtotal);
        case 20: return new Float(Descuento);
        case 21: return new Float(Impuesto);
        case 22: return new Float(Total);
        case 23: return new Long(objEstadoID);
        case 24: return CodEstado;
        case 25: return DescEstado;
        case 26: return new Long(objCausaEstadoID);
        case 27: return CodCausaEstado;
        case 28: return DescCausaEstado;
        case 29: return NombreVendedor;
        case 32: return Nota;
        case 33: return new Boolean(Exento);
        case 34: return AutorizacionDGI;
        }
        return null;
    }

    @Override
	public void setProperty(int _index, Object _) {
        switch(_index)  {
        case 0: Id = Long.parseLong(_.toString()); break;
        case 1: NumeroMovil = Integer.parseInt(_.toString()); break;
        case 2: NumeroCentral = Integer.parseInt(_.toString()); break;
        case 3: Tipo = (java.lang.String) _; break;
        case 4: Fecha = Integer.parseInt(_.toString()); break;
        case 5: objClienteID = Long.parseLong(_.toString()); break;
        case 6: NombreCliente = (java.lang.String) _; break;
        case 7: objSucursalID = Long.parseLong(_.toString()); break;
        case 8: NombreSucursal = (java.lang.String) _; break;
        case 9: objTipoPrecioVentaID = Long.parseLong(_.toString()); break;
        case 10: CodTipoPrecio = (java.lang.String) _; break;
        case 11: DescTipoPrecio = (java.lang.String) _; break;
        case 12: objVendedorID = Long.parseLong(_.toString()); break;
        case 13: BonificacionEspecial = "true".equals(_.toString()); break;
        case 14: BonificacionSolicitada = (java.lang.String) _; break;
        case 15: PrecioEspecial = "true".equals(_.toString()); break;
        case 16: PrecioSolicitado = (java.lang.String) _; break;
        case 17: PedidoCondicionado = "true".equals(_.toString()); break;
        case 18: Condicion = (java.lang.String) _; break;
        case 19: Subtotal = Float.parseFloat(_.toString()); break;
        case 20: Descuento = Float.parseFloat(_.toString()); break;
        case 21: Impuesto = Float.parseFloat(_.toString()); break;
        case 22: Total = Float.parseFloat(_.toString()); break;
        case 23: objEstadoID = Long.parseLong(_.toString()); break;
        case 24: CodEstado = (java.lang.String) _; break;
        case 25: DescEstado = (java.lang.String) _; break;
        case 26: objCausaEstadoID = Long.parseLong(_.toString()); break;
        case 27: CodCausaEstado = (java.lang.String) _; break;
        case 28: DescCausaEstado = (java.lang.String) _; break;
        case 29: NombreVendedor = (java.lang.String) _; break;
        case 32: Nota = (java.lang.String) _; break;
        case 33: Exento = "true".equals(_.toString()); break;
        case 34: AutorizacionDGI = (java.lang.String) _; break;
        }
    }

    @Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "Id";
            _info.type = Long.class; break;
        case 1:
            _info.name = "NumeroMovil";
            _info.type = Integer.class; break;
        case 2:
            _info.name = "NumeroCentral";
            _info.type = Integer.class; break;
        case 3:
            _info.name = "Tipo";
            _info.type = java.lang.String.class; break;
        case 4:
            _info.name = "Fecha";
            _info.type = Integer.class; break;
        case 5:
            _info.name = "objClienteID";
            _info.type = Long.class; break;
        case 6:
            _info.name = "NombreCliente";
            _info.type = java.lang.String.class; break;
        case 7:
            _info.name = "objSucursalID";
            _info.type = Long.class; break;
        case 8:
            _info.name = "NombreSucursal";
            _info.type = java.lang.String.class; break;
        case 9:
            _info.name = "objTipoPrecioVentaID";
            _info.type = Long.class; break;
        case 10:
            _info.name = "CodTipoPrecio";
            _info.type = java.lang.String.class; break;
        case 11:
            _info.name = "DescTipoPrecio";
            _info.type = java.lang.String.class; break;
        case 12:
            _info.name = "objVendedorID";
            _info.type = Long.class; break;
        case 13:
            _info.name = "BonificacionEspecial";
            _info.type = Boolean.class; break;
        case 14:
            _info.name = "BonificacionSolicitada";
            _info.type = java.lang.String.class; break;
        case 15:
            _info.name = "PrecioEspecial";
            _info.type = Boolean.class; break;
        case 16:
            _info.name = "PrecioSolicitado";
            _info.type = java.lang.String.class; break;
        case 17:
            _info.name = "PedidoCondicionado";
            _info.type = Boolean.class; break;
        case 18:
            _info.name = "Condicion";
            _info.type = java.lang.String.class; break;
        case 19:
            _info.name = "Subtotal";
            _info.type = Float.class; break;
        case 20:
            _info.name = "Descuento";
            _info.type = Float.class; break;
        case 21:
            _info.name = "Impuesto";
            _info.type = Float.class; break;
        case 22:
            _info.name = "Total";
            _info.type = Float.class; break;
        case 23:
            _info.name = "objEstadoID";
            _info.type = Long.class; break;
        case 24:
            _info.name = "CodEstado";
            _info.type = java.lang.String.class; break;
        case 25:
            _info.name = "DescEstado";
            _info.type = java.lang.String.class; break;
        case 26:
            _info.name = "objCausaEstadoID";
            _info.type = Long.class; break;
        case 27:
            _info.name = "CodCausaEstado";
            _info.type = java.lang.String.class; break;
        case 28:
            _info.name = "DescCausaEstado";
            _info.type = java.lang.String.class; break;
        case 29:
            _info.name = "NombreVendedor";
            _info.type = java.lang.String.class; break;
        case 30:
            _info.name = "Detalles";
        case 31:
            _info.name = "PromocionesAplicadas";
        case 32:
            _info.name = "Nota";
            _info.type = java.lang.String.class; break;
        case 33:
            _info.name = "Exento";
            _info.type = Boolean.class; break;
        case 34:
            _info.name = "AutorizacionDGI";
            _info.type = java.lang.String.class; break;
        }
    }

}
