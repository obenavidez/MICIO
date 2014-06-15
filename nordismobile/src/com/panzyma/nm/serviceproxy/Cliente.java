package com.panzyma.nm.serviceproxy;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.panzyma.nm.interfaces.Item;

public final class Cliente implements KvmSerializable, Item, Parcelable {

	private long IdCliente;

	private java.lang.String NombreCliente;

	private long IdSucursal;

	private java.lang.String Codigo;

	private java.lang.String CodTipoPrecio;

	private java.lang.String DesTipoPrecio;

	private long objPrecioVentaID;

	private long objCategoriaClienteID;

	private long objTipoClienteID;

	private boolean AplicaBonificacion;

	private boolean PermiteBonifEspecial;

	private boolean PermitePrecioEspecial;

	private java.lang.String UG;

	private java.lang.String Ubicacion;

	private java.lang.String NombreLegalCliente;

	private Factura[] FacturasPendientes;

	private CCNotaCredito[] NotasCreditoPendientes;

	private CCNotaDebito[] NotasDebitoPendientes;

	private DescuentoProveedor[] DescuentosProveedor;

	private boolean AplicaOtrasDeducciones;

	private float MontoMinimoAbono;

	private int PlazoDescuento;

	private boolean PermiteDevolucion;

	// private static final long serialVersionUID = 46543445;

	public Cliente() {

	}

	public Cliente(long idCliente, String nombreCliente, String codigo,
			String ubicacion) {
		super();
		IdCliente = idCliente;
		NombreCliente = nombreCliente;
		Codigo = codigo;
		Ubicacion = ubicacion;
	}

	public void setIdCliente(long idCliente) {
		this.IdCliente = idCliente;
	}

	public long getIdCliente() {
		return this.IdCliente;
	}

	public void setNombreCliente(java.lang.String nombreCliente) {
		this.NombreCliente = nombreCliente;
	}

	public java.lang.String getNombreCliente() {
		return this.NombreCliente;
	}

	public void setIdSucursal(long idSucursal) {
		this.IdSucursal = idSucursal;
	}

	public long getIdSucursal() {
		return this.IdSucursal;
	}

	public void setCodigo(java.lang.String codigo) {
		this.Codigo = codigo;
	}

	public java.lang.String getCodigo() {
		return this.Codigo;
	}

	public void setCodTipoPrecio(java.lang.String codTipoPrecio) {
		this.CodTipoPrecio = codTipoPrecio;
	}

	public java.lang.String getCodTipoPrecio() {
		return this.CodTipoPrecio;
	}

	public void setDesTipoPrecio(java.lang.String desTipoPrecio) {
		this.DesTipoPrecio = desTipoPrecio;
	}

	public java.lang.String getDesTipoPrecio() {
		return this.DesTipoPrecio;
	}

	public void setObjPrecioVentaID(long objprecioVentaID) {
		this.objPrecioVentaID = objprecioVentaID;
	}

	public long getObjPrecioVentaID() {
		return this.objPrecioVentaID;
	}

	public void setObjCategoriaClienteID(long objCategoriaClienteID) {
		this.objCategoriaClienteID = objCategoriaClienteID;
	}

	public long getObjCategoriaClienteID() {
		return this.objCategoriaClienteID;
	}

	public void setObjTipoClienteID(long objTipoClienteID) {
		this.objTipoClienteID = objTipoClienteID;
	}

	public long getObjTipoClienteID() {
		return this.objTipoClienteID;
	}

	public void setAplicaBonificacion(boolean aplicaBonificacion) {
		this.AplicaBonificacion = aplicaBonificacion;
	}

	public boolean getAplicaBonificacion() {
		return this.AplicaBonificacion;
	}

	public void setPermiteBonifEspecial(boolean permiteBonifEspecial) {
		this.PermiteBonifEspecial = permiteBonifEspecial;
	}

	public boolean getPermiteBonifEspecial() {
		return this.PermiteBonifEspecial;
	}

	public void setPermitePrecioEspecial(boolean permitePrecioEspecial) {
		this.PermitePrecioEspecial = permitePrecioEspecial;
	}

	public boolean getPermitePrecioEspecial() {
		return this.PermitePrecioEspecial;
	}

	public void setUG(java.lang.String UG) {
		this.UG = UG;
	}

	public java.lang.String getUG() {
		return this.UG;
	}

	public void setUbicacion(java.lang.String ubicacion) {
		this.Ubicacion = ubicacion;
	}

	public java.lang.String getUbicacion() {
		return this.Ubicacion;
	}

	public void setNombreLegalCliente(java.lang.String nombreLegalCliente) {
		this.NombreLegalCliente = nombreLegalCliente;
	}

	public java.lang.String getNombreLegalCliente() {
		return this.NombreLegalCliente;
	}

	public void setFacturasPendientes(Factura[] facturasPendientes) {
		this.FacturasPendientes = facturasPendientes;
	}

	public Factura[] getFacturasPendientes() {
		return this.FacturasPendientes;
	}

	public void setNotasCreditoPendientes(CCNotaCredito[] notasCreditoPendientes) {
		this.NotasCreditoPendientes = notasCreditoPendientes;
	}

	public CCNotaCredito[] getNotasCreditoPendientes() {
		return this.NotasCreditoPendientes;
	}

	public void setNotasDebitoPendientes(CCNotaDebito[] notasDebitoPendientes) {
		this.NotasDebitoPendientes = notasDebitoPendientes;
	}

	public CCNotaDebito[] getNotasDebitoPendientes() {
		return this.NotasDebitoPendientes;
	}

	public void setDescuentosProveedor(DescuentoProveedor[] descuentosProveedor) {
		this.DescuentosProveedor = descuentosProveedor;
	}

	public DescuentoProveedor[] getDescuentosProveedor() {
		return this.DescuentosProveedor;
	}

	public void setAplicaOtrasDeducciones(boolean aplicaOtrasDeducciones) {
		this.AplicaOtrasDeducciones = aplicaOtrasDeducciones;
	}

	public boolean getAplicaOtrasDeducciones() {
		return this.AplicaOtrasDeducciones;
	}

	public void setMontoMinimoAbono(float montoMinimoAbono) {
		this.MontoMinimoAbono = montoMinimoAbono;
	}

	public float getMontoMinimoAbono() {
		return this.MontoMinimoAbono;
	}

	public void setPlazoDescuento(int plazoDescuento) {
		this.PlazoDescuento = plazoDescuento;
	}

	public int getPlazoDescuento() {
		return this.PlazoDescuento;
	}

	public void setPermiteDevolucion(boolean permiteDevolucion) {
		this.PermiteDevolucion = permiteDevolucion;
	}

	public boolean getPermiteDevolucion() {
		return this.PermiteDevolucion;
	}

	@Override
	public int getPropertyCount() {
		return 23;
	}

	@SuppressLint({ "UseValueOf" })
	@Override
	public Object getProperty(int _index) {
		switch (_index) {
		case 0:
			return Long.valueOf(IdCliente);
		case 1:
			return NombreCliente;
		case 2:
			return Long.valueOf(IdSucursal);
		case 3:
			return Codigo;
		case 4:
			return CodTipoPrecio;
		case 5:
			return DesTipoPrecio;
		case 6:
			return Long.valueOf(objPrecioVentaID);
		case 7:
			return Long.valueOf(objCategoriaClienteID);
		case 8:
			return Long.valueOf(objTipoClienteID);
		case 9:
			return Boolean.valueOf(AplicaBonificacion);
		case 10:
			return Boolean.valueOf(PermiteBonifEspecial);
		case 11:
			return Boolean.valueOf(PermitePrecioEspecial);
		case 12:
			return UG;
		case 13:
			return Ubicacion;
		case 14:
			return NombreLegalCliente;
		case 19:
			return Boolean.valueOf(AplicaOtrasDeducciones);
		case 20:
			return Float.valueOf(MontoMinimoAbono);
		case 21:
			return Integer.valueOf(PlazoDescuento);
		case 22:
			return Boolean.valueOf(PermiteDevolucion);
		}
		return null;
	}

	@Override
	public void setProperty(int _index, Object _obj) {
		switch (_index) {
		case 0:
			IdCliente = Long.parseLong(_obj.toString());
			break;
		case 1:
			NombreCliente = (java.lang.String) _obj;
			break;
		case 2:
			IdSucursal = Long.parseLong(_obj.toString());
			break;
		case 3:
			Codigo = (java.lang.String) _obj;
			break;
		case 4:
			CodTipoPrecio = (java.lang.String) _obj;
			break;
		case 5:
			DesTipoPrecio = (java.lang.String) _obj;
			break;
		case 6:
			objPrecioVentaID = Long.parseLong(_obj.toString());
			break;
		case 7:
			objCategoriaClienteID = Long.parseLong(_obj.toString());
			break;
		case 8:
			objTipoClienteID = Long.parseLong(_obj.toString());
			break;
		case 9:
			AplicaBonificacion = "true".equals(_obj.toString());
			break;
		case 10:
			PermiteBonifEspecial = "true".equals(_obj.toString());
			break;
		case 11:
			PermitePrecioEspecial = "true".equals(_obj.toString());
			break;
		case 12:
			UG = (java.lang.String) _obj;
			break;
		case 13:
			Ubicacion = (java.lang.String) _obj;
			break;
		case 14:
			NombreLegalCliente = (java.lang.String) _obj;
			break;
		case 19:
			AplicaOtrasDeducciones = "true".equals(_obj.toString());
			break;
		case 20:
			MontoMinimoAbono = Float.parseFloat(_obj.toString());
			break;
		case 21:
			PlazoDescuento = Integer.parseInt(_obj.toString());
			break;
		case 22:
			PermiteDevolucion = "true".equals(_obj.toString());
			break;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
		switch (_index) {
		case 0:
			_info.name = "IdCliente";
			_info.type = Long.class;
			break;
		case 1:
			_info.name = "NombreCliente";
			_info.type = java.lang.String.class;
			break;
		case 2:
			_info.name = "IdSucursal";
			_info.type = Long.class;
			break;
		case 3:
			_info.name = "Codigo";
			_info.type = java.lang.String.class;
			break;
		case 4:
			_info.name = "CodTipoPrecio";
			_info.type = java.lang.String.class;
			break;
		case 5:
			_info.name = "DesTipoPrecio";
			_info.type = java.lang.String.class;
			break;
		case 6:
			_info.name = "objPrecioVentaID";
			_info.type = Long.class;
			break;
		case 7:
			_info.name = "objCategoriaClienteID";
			_info.type = Long.class;
			break;
		case 8:
			_info.name = "objTipoClienteID";
			_info.type = Long.class;
			break;
		case 9:
			_info.name = "AplicaBonificacion";
			_info.type = Boolean.class;
			break;
		case 10:
			_info.name = "PermiteBonifEspecial";
			_info.type = Boolean.class;
			break;
		case 11:
			_info.name = "PermitePrecioEspecial";
			_info.type = Boolean.class;
			break;
		case 12:
			_info.name = "UG";
			_info.type = java.lang.String.class;
			break;
		case 13:
			_info.name = "Ubicacion";
			_info.type = java.lang.String.class;
			break;
		case 14:
			_info.name = "NombreLegalCliente";
			_info.type = java.lang.String.class;
			break;
		case 15:
			_info.name = "FacturasPendientes";
			_info.type = Factura[].class;
		case 16:
			_info.name = "NotasCreditoPendientes";
			_info.type = CCNotaCredito[].class;
		case 17:
			_info.name = "NotasDebitoPendientes";
			_info.type = CCNotaDebito[].class;
		case 18:
			_info.name = "DescuentoProveedor";
			_info.type = DescuentoProveedor[].class;
		case 19:
			_info.name = "AplicaOtrasDeducciones";
			_info.type = Boolean.class;
			break;
		case 20:
			_info.name = "MontoMinimoAbono";
			_info.type = Float.class;
			break;
		case 21:
			_info.name = "PlazoDescuento";
			_info.type = Integer.class;
			break;
		case 22:
			_info.name = "PermiteDevolucion";
			_info.type = Boolean.class;
			break;
		}
	}

	@Override
	public Object isMatch(CharSequence constraint) {
		if (this.getNombreCliente().toLowerCase()
				.startsWith(constraint.toString()))
			return true;
		return false;
	}

	@Override
	public String getItemName() {
		return this.NombreCliente;
	}

	@Override
	public String getItemDescription() {		
		return this.Ubicacion;
	}

	@Override
	public String getItemCode() {
		return this.Codigo;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}	

}
