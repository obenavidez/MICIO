package com.panzyma.nm.serviceproxy;

import android.annotation.SuppressLint;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 

public  class Factura implements KvmSerializable {
     
	public long Id;
	public java.lang.String NombreSucursal;
	public java.lang.String NoFactura;
	public java.lang.String Tipo;
    public java.lang.String NoPedido;
    public java.lang.String CodEstado;
    public java.lang.String Estado;
    public long Fecha;
    public long FechaVencimiento;
    public long FechaAppDescPP;
    public int Dias;
    public float TotalFacturado;
    public float Abonado;
    public float Descontado;
    public float Retenido;
    public float Otro;
    public float Saldo;
    public boolean Exenta;
    public float SubtotalFactura;
    public float DescuentoFactura;
    public float ImpuestoFactura;
    public boolean PuedeAplicarDescPP;    
	public PromocionCobro[] DetallePromocionCobro ;
    public MontoProveedor[] DetalleMontoProveedor ;  

    public Factura() {
         
    }
    public void setDetallePromocionCobro(PromocionCobro[] detallePromocionCobro) {
        this.DetallePromocionCobro = detallePromocionCobro;
    }

    public PromocionCobro[] getDetallePromocionCobro() {
        return this.DetallePromocionCobro;
    }

    public void setDetalleMontoProveedor(MontoProveedor[] detalleMontoProveedor) {
        this.DetalleMontoProveedor = detalleMontoProveedor;
    }

    public MontoProveedor[] getDetalleMontoProveedor() {
        return this.DetalleMontoProveedor;
    }

    public void setNoFactura(java.lang.String noFactura) {
        this.NoFactura = noFactura;
    }

    public java.lang.String getNoFactura() {
        return this.NoFactura;
    }

    public void setNombreSucursal(java.lang.String nombreSucursal) {
        this.NombreSucursal = nombreSucursal;
    }

    public java.lang.String getNombreSucursal() {
        return this.NombreSucursal;
    }

    public void setTipo(java.lang.String tipo) {
        this.Tipo = tipo;
    }

    public java.lang.String getTipo() {
        return this.Tipo;
    }

    public void setNoPedido(java.lang.String noPedido) {
        this.NoPedido = noPedido;
    }

    public java.lang.String getNoPedido() {
        return this.NoPedido;
    }

    public void setEstado(java.lang.String estado) {
        this.Estado = estado;
    }

    public java.lang.String getEstado() {
        return this.Estado;
    }

    public void setFecha(long fecha) {
        this.Fecha = fecha;
    }

    public long getFecha() {
        return this.Fecha;
    }

    public void setFechaVencimiento(long fechaVencimiento) {
        this.FechaVencimiento = fechaVencimiento;
    }

    public long getFechaVencimiento() {
        return this.FechaVencimiento;
    }

    public void setFechaAppDescPP(long fechaAppDescPP) {
        this.FechaAppDescPP = fechaAppDescPP;
    }

    public long getFechaAppDescPP() {
        return this.FechaAppDescPP;
    }

    public void setDias(int dias) {
        this.Dias = dias;
    }

    public int getDias() {
        return this.Dias;
    }

    public void setTotalFacturado(float totalFacturado) {
        this.TotalFacturado = totalFacturado;
    }

    public float getTotalFacturado() {
        return this.TotalFacturado;
    }

    public void setAbonado(float abonado) {
        this.Abonado = abonado;
    }

    public float getAbonado() {
        return this.Abonado;
    }

    public void setDescontado(float descontado) {
        this.Descontado = descontado;
    }

    public float getDescontado() {
        return this.Descontado;
    }

    public void setRetenido(float retenido) {
        this.Retenido = retenido;
    }

    public float getRetenido() {
        return this.Retenido;
    }

    public void setOtro(float otro) {
        this.Otro = otro;
    }

    public float getOtro() {
        return this.Otro;
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

    public void setSubtotalFactura(float subtotalFactura) {
        this.SubtotalFactura = subtotalFactura;
    }

    public float getSubtotalFactura() {
        return this.SubtotalFactura;
    }

    public void setImpuestoFactura(float impuestoFactura) {
        this.ImpuestoFactura = impuestoFactura;
    }

    public float getImpuestoFactura() {
        return this.ImpuestoFactura;
    }

    public void setDescuentoFactura(float descuentoFactura) {
        this.DescuentoFactura = descuentoFactura;
    }

    public float getDescuentoFactura() {
        return this.DescuentoFactura;
    }

    public void setPuedeAplicarDescPP(boolean puedeAplicarDescPP) {
        this.PuedeAplicarDescPP = puedeAplicarDescPP;
    }

    public boolean getPuedeAplicarDescPP() {
        return this.PuedeAplicarDescPP;
    }

    public void setExenta(boolean exenta) {
        this.Exenta = exenta;
    }

    public boolean getExenta() {
        return this.Exenta;
    }
    @Override
	public int getPropertyCount() {
        return 24;
    }
 
	@SuppressLint("UseValueOf")
	@Override
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return DetallePromocionCobro; 
        case 1: return DetalleMontoProveedor;
        case 2: return NoFactura;
        case 3: return NombreSucursal;
        case 4: return Tipo;
        case 5: return NoPedido;
        case 6: return Estado;
        case 7: return new Long(Fecha);
        case 8: return new Long(FechaVencimiento);
        case 9: return new Long(FechaAppDescPP);
        case 10: return new Integer(Dias);
        case 11: return new Float(TotalFacturado);
        case 12: return new Float(Abonado);
        case 13: return new Float(Descontado);
        case 14: return new Float(Retenido);
        case 15: return new Float(Otro);
        case 16: return new Float(Saldo);
        case 17: return CodEstado;
        case 18: return new Long(Id);
        case 19: return new Float(SubtotalFactura);
        case 20: return new Float(ImpuestoFactura);
        case 21: return new Float(DescuentoFactura);
        case 22: return new Boolean(PuedeAplicarDescPP);
        case 23: return new Boolean(Exenta);
        }
        return null;
    }
	@Override
	public void setProperty(int _index, Object _obj) {
        switch(_index)  {
        case 0: DetallePromocionCobro =(PromocionCobro[]) _obj; break;
        case 1: DetalleMontoProveedor =(MontoProveedor[]) _obj; break;
        case 2: NoFactura = (java.lang.String) _obj; break;
        case 3: NombreSucursal = (java.lang.String) _obj; break;
        case 4: Tipo = (java.lang.String) _obj; break;
        case 5: NoPedido = (java.lang.String) _obj; break;
        case 6: Estado = (java.lang.String) _obj; break;
        case 7: Fecha = Long.parseLong(_obj.toString()); break;
        case 8: FechaVencimiento = Long.parseLong(_obj.toString()); break;
        case 9: FechaAppDescPP = Long.parseLong(_obj.toString()); break;
        case 10: Dias = Integer.parseInt(_obj.toString()); break;
        case 11: TotalFacturado = Float.parseFloat(_obj.toString()); break;
        case 12: Abonado = Float.parseFloat(_obj.toString()); break;
        case 13: Descontado = Float.parseFloat(_obj.toString()); break;
        case 14: Retenido = Float.parseFloat(_obj.toString()); break;
        case 15: Otro = Float.parseFloat(_obj.toString()); break;
        case 16: Saldo = Float.parseFloat(_obj.toString()); break;
        case 17: CodEstado = (java.lang.String) _obj; break;
        case 18: Id = Long.parseLong(_obj.toString()); break;
        case 19: SubtotalFactura = Float.parseFloat(_obj.toString()); break;
        case 20: ImpuestoFactura = Float.parseFloat(_obj.toString()); break;
        case 21: DescuentoFactura = Float.parseFloat(_obj.toString()); break;
        case 22: PuedeAplicarDescPP = "true".equals(_obj.toString()); break;
        case 23: Exenta = "true".equals(_obj.toString()); break;
        }
    }

      
	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "DetallePromocionCobro";
            _info.type =  PromocionCobro[].class; break;
            
        case 1:
            _info.name = "DetalleMontoProveedor";
            _info.type =  MontoProveedor[].class; break;
        case 2:
            _info.name = "NoFactura";
            _info.type = java.lang.String.class; break;
        case 3:
            _info.name = "NombreSucursal";
            _info.type = java.lang.String.class; break;
        case 4:
            _info.name = "Tipo";
            _info.type = java.lang.String.class; break;
        case 5:
            _info.name = "NoPedido";
            _info.type = java.lang.String.class; break;
        case 6:
            _info.name = "Estado";
            _info.type = java.lang.String.class; break;
        case 7:
            _info.name = "Fecha";
            _info.type = Long.class; break;
        case 8:
            _info.name = "fechaVencimiento";
            _info.type = Long.class; break;
        case 9:
            _info.name = "FechaAppDescPP";
            _info.type = Long.class; break;
        case 10:
            _info.name = "Dias";
            _info.type = Integer.class; break;
        case 11:
            _info.name = "TotalFacturado";
            _info.type = Float.class; break;
        case 12:
            _info.name = "Abonado";
            _info.type = Float.class; break;
        case 13:
            _info.name = "Descontado";
            _info.type = Float.class; break;
        case 14:
            _info.name = "Retenido";
            _info.type = Float.class; break;
        case 15:
            _info.name = "Otro";
            _info.type = Float.class; break;
        case 16:
            _info.name = "Saldo";
            _info.type = Float.class; break;
        case 17:
            _info.name = "CodEstado";
            _info.type = java.lang.String.class; break;
        case 18:
            _info.name = "Id";
            _info.type = Long.class; break;
        case 19:
            _info.name = "SubtotalFactura";
            _info.type = Float.class; break;
        case 20:
            _info.name = "ImpuestoFactura";
            _info.type = Float.class; break;
        case 21:
            _info.name = "DescuentoFactura";
            _info.type = Float.class; break;
        case 22:
            _info.name = "PuedeAplicarDescPP";
            _info.type = Boolean.class; break;
        case 23:
            _info.name = "Exenta";
            _info.type = Boolean.class; break;
        }
    }

}
