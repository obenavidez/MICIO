package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.panzyma.nm.auxiliar.DateUtil;

import android.os.Parcel;
import android.os.Parcelable;


public class Devolucion implements KvmSerializable, Cloneable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -937340237731291658L;

	public Devolucion() {}
	
	public Devolucion(String a) {
		setNombreCliente("");
		setCodMotivo("");
		setDescMotivo("");
		setTipoTramite("");
		setNota("");
		setObservacion("");
		setDescEstado("");
		setCodEstado("");
		setDescCausaEstado("");
		setClaveAutorizaAplicacionInmediata("");
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return Id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.Id = id;
	}

	/**
	 * @return the referencia
	 */
	public int getReferencia() {
		return Referencia;
	}

	/**
	 * @param referencia
	 *            the referencia to set
	 */
	public void setReferencia(int referencia) {
		this.Referencia = referencia;
	}

	/**
	 * @return the numeroCentral
	 */
	public int getNumeroCentral() {
		return NumeroCentral;
	}

	/**
	 * @param numeroCentral
	 *            the numeroCentral to set
	 */
	public void setNumeroCentral(int numeroCentral) {
		this.NumeroCentral = numeroCentral;
	}

	/**
	 * @return the fecha
	 */
	public long getFecha() {
		return Fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(long fecha) {
		this.Fecha = fecha;
	}

	/**
	 * @return the objPedidoDevueltoID
	 */
	public long getObjPedidoDevueltoID() {
		return ObjPedidoDevueltoID;
	}

	/**
	 * @param objPedidoDevueltoID
	 *            the objPedidoDevueltoID to set
	 */
	public void setObjPedidoDevueltoID(long objPedidoDevueltoID) {
		this.ObjPedidoDevueltoID = objPedidoDevueltoID;
	}

	/**
	 * @return the numeroPedidoDevuelto
	 */
	public int getNumeroPedidoDevuelto() {
		return NumeroPedidoDevuelto;
	}

	/**
	 * @param numeroPedidoDevuelto
	 *            the numeroPedidoDevuelto to set
	 */
	public void setNumeroPedidoDevuelto(int numeroPedidoDevuelto) {
		this.NumeroPedidoDevuelto = numeroPedidoDevuelto;
	}

	/**
	 * @return the numeroFacturaDevuelta
	 */
	public int getNumeroFacturaDevuelta() {
		return NumeroFacturaDevuelta;
	}

	/**
	 * @param numeroFacturaDevuelta
	 *            the numeroFacturaDevuelta to set
	 */
	public void setNumeroFacturaDevuelta(int numeroFacturaDevuelta) {
		this.NumeroFacturaDevuelta = numeroFacturaDevuelta;
	}

	/**
	 * @return the objVendedorID
	 */
	public long getObjVendedorID() {
		return ObjVendedorID;
	}

	/**
	 * @param objVendedorID
	 *            the objVendedorID to set
	 */
	public void setObjVendedorID(long objVendedorID) {
		this.ObjVendedorID = objVendedorID;
	}

	/**
	 * @return the objClienteID
	 */
	public long getObjClienteID() {
		return ObjClienteID;
	}

	/**
	 * @param objClienteID
	 *            the objClienteID to set
	 */
	public void setObjClienteID(long objClienteID) {
		this.ObjClienteID = objClienteID;
	}

	/**
	 * @return the objSucursalID
	 */
	public long getObjSucursalID() {
		return ObjSucursalID;
	}

	/**
	 * @param objSucursalID
	 *            the objSucursalID to set
	 */
	public void setObjSucursalID(long objSucursalID) {
		this.ObjSucursalID = objSucursalID;
	}

	/**
	 * @return the nombreCliente
	 */
	public String getNombreCliente() {
		return NombreCliente;
	}

	/**
	 * @param nombreCliente
	 *            the nombreCliente to set
	 */
	public void setNombreCliente(String nombreCliente) {
		this.NombreCliente = nombreCliente;
	}

	/**
	 * @return the objMotivoID
	 */
	public long getObjMotivoID() {
		return ObjMotivoID;
	}

	/**
	 * @param objMotivoID
	 *            the objMotivoID to set
	 */
	public void setObjMotivoID(long objMotivoID) {
		this.ObjMotivoID = objMotivoID;
	}

	/**
	 * @return the codMotivo
	 */
	public String getCodMotivo() {
		return CodMotivo;
	}

	/**
	 * @param codMotivo
	 *            the codMotivo to set
	 */
	public void setCodMotivo(String codMotivo) {
		this.CodMotivo = codMotivo;
	}

	/**
	 * @return the descMotivo
	 */
	public String getDescMotivo() {
		return DescMotivo;
	}

	/**
	 * @param descMotivo
	 *            the descMotivo to set
	 */
	public void setDescMotivo(String descMotivo) {
		this.DescMotivo = descMotivo;
	}

	/**
	 * @return the tipoTramite
	 */
	public String getTipoTramite() {
		return TipoTramite;
	}

	/**
	 * @param tipoTramite
	 *            the tipoTramite to set
	 */
	public void setTipoTramite(String tipoTramite) {
		this.TipoTramite = tipoTramite;
	}

	/**
	 * @return the deVencido
	 */
	public boolean isDeVencido() {
		return DeVencido;
	}

	/**
	 * @param deVencido
	 *            the deVencido to set
	 */
	public void setDeVencido(boolean deVencido) {
		this.DeVencido = deVencido;
	}

	/**
	 * @return the parcial
	 */
	public boolean isParcial() {
		return Parcial;
	}

	/**
	 * @param parcial
	 *            the parcial to set
	 */
	public void setParcial(boolean parcial) {
		this.Parcial = parcial;
	}

	/**
	 * @return the aplicacionInmediata
	 */
	public boolean isAplicacionInmediata() {
		return AplicacionInmediata;
	}

	/**
	 * @param aplicacionInmediata
	 *            the aplicacionInmediata to set
	 */
	public void setAplicacionInmediata(boolean aplicacionInmediata) {
		this.AplicacionInmediata = aplicacionInmediata;
	}

	/**
	 * @return the nota
	 */
	public String getNota() {
		return Nota;
	}

	/**
	 * @param nota
	 *            the nota to set
	 */
	public void setNota(String nota) {
		this.Nota = nota;
	}

	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return Observacion;
	}

	/**
	 * @param observacion
	 *            the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.Observacion = observacion;
	}

	/**
	 * @return the subtotal
	 */
	public long getSubtotal() {
		return Subtotal;
	}

	/**
	 * @param subtotal
	 *            the subtotal to set
	 */
	public void setSubtotal(long subtotal) {
		this.Subtotal = subtotal;
	}

	/**
	 * @return the impuesto
	 */
	public long getImpuesto() {
		return Impuesto;
	}

	/**
	 * @param impuesto
	 *            the impuesto to set
	 */
	public void setImpuesto(long impuesto) {
		this.Impuesto = impuesto;
	}

	/**
	 * @return the montoPromocion
	 */
	public long getMontoPromocion() {
		return MontoPromocion;
	}

	/**
	 * @param montoPromocion
	 *            the montoPromocion to set
	 */
	public void setMontoPromocion(long montoPromocion) {
		this.MontoPromocion = montoPromocion;
	}

	/**
	 * @return the montoPromocionVen
	 */
	public long getMontoPromocionVen() {
		return MontoPromocionVen;
	}

	/**
	 * @param montoPromocionVen
	 *            the montoPromocionVen to set
	 */
	public void setMontoPromocionVen(long montoPromocionVen) {
		this.MontoPromocionVen = montoPromocionVen;
	}

	/**
	 * @return the montoCargoAdm
	 */
	public long getMontoCargoAdm() {
		return MontoCargoAdm;
	}

	/**
	 * @param montoCargoAdm
	 *            the montoCargoAdm to set
	 */
	public void setMontoCargoAdm(long montoCargoAdm) {
		this.MontoCargoAdm = montoCargoAdm;
	}

	/**
	 * @return the montoCargoAdmVen
	 */
	public long getMontoCargoAdmVen() {
		return MontoCargoAdmVen;
	}

	/**
	 * @param montoCargoAdmVen
	 *            the montoCargoAdmVen to set
	 */
	public void setMontoCargoAdmVen(long montoCargoAdmVen) {
		this.MontoCargoAdmVen = montoCargoAdmVen;
	}

	/**
	 * @return the montoVinieta
	 */
	public long getMontoVinieta() {
		return MontoVinieta;
	}

	/**
	 * @param montoVinieta
	 *            the montoVinieta to set
	 */
	public void setMontoVinieta(long montoVinieta) {
		this.MontoVinieta = montoVinieta;
	}

	/**
	 * @return the total
	 */
	public long getTotal() {
		return Total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(long total) {
		this.Total = total;
	}

	/**
	 * @return the totalVen
	 */
	public long getTotalVen() {
		return TotalVen;
	}

	/**
	 * @param totalVen
	 *            the totalVen to set
	 */
	public void setTotalVen(long totalVen) {
		this.TotalVen = totalVen;
	}

	/**
	 * @return the objEstadoID
	 */
	public long getObjEstadoID() {
		return ObjEstadoID;
	}

	/**
	 * @param objEstadoID
	 *            the objEstadoID to set
	 */
	public void setObjEstadoID(long objEstadoID) {
		this.ObjEstadoID = objEstadoID;
	}

	/**
	 * @return the descEstado
	 */
	public String getDescEstado() {
		return DescEstado;
	}

	/**
	 * @param descEstado
	 *            the descEstado to set
	 */
	public void setDescEstado(String descEstado) {
		this.DescEstado = descEstado;
	}

	/**
	 * @return the codEstado
	 */
	public String getCodEstado() {
		return CodEstado;
	}

	/**
	 * @param codEstado
	 *            the codEstado to set
	 */
	public void setCodEstado(String codEstado) {
		this.CodEstado = codEstado;
	}

	/**
	 * @return the objCausaEstadoID
	 */
	public long getObjCausaEstadoID() {
		return ObjCausaEstadoID;
	}

	/**
	 * @param objCausaEstadoID
	 *            the objCausaEstadoID to set
	 */
	public void setObjCausaEstadoID(long objCausaEstadoID) {
		this.ObjCausaEstadoID = objCausaEstadoID;
	}

	/**
	 * @return the descCausaEstado
	 */
	public String getDescCausaEstado() {
		return DescCausaEstado;
	}

	/**
	 * @param descCausaEstado
	 *            the descCausaEstado to set
	 */
	public void setDescCausaEstado(String descCausaEstado) {
		this.DescCausaEstado = descCausaEstado;
	}

	/**
	 * @return the especial
	 */
	public boolean isEspecial() {
		return Especial;
	}

	/**
	 * @param especial
	 *            the especial to set
	 */
	public void setEspecial(boolean especial) {
		this.Especial = especial;
	}

	/**
	 * @return the montoCargoVendedor
	 */
	public long getMontoCargoVendedor() {
		return MontoCargoVendedor;
	}

	/**
	 * @param montoCargoVendedor
	 *            the montoCargoVendedor to set
	 */
	public void setMontoCargoVendedor(long montoCargoVendedor) {
		this.MontoCargoVendedor = montoCargoVendedor;
	}

	/**
	 * @return the montoBonif
	 */
	public long getMontoBonif() {
		return MontoBonif;
	}

	/**
	 * @param montoBonif
	 *            the montoBonif to set
	 */
	public void setMontoBonif(long montoBonif) {
		this.MontoBonif = montoBonif;
	}

	/**
	 * @return the montoBonifVen
	 */
	public long getMontoBonifVen() {
		return MontoBonifVen;
	}

	/**
	 * @param montoBonifVen
	 *            the montoBonifVen to set
	 */
	public void setMontoBonifVen(long montoBonifVen) {
		this.MontoBonifVen = montoBonifVen;
	}

	/**
	 * @return the impuestoVen
	 */
	public long getImpuestoVen() {
		return ImpuestoVen;
	}

	/**
	 * @param impuestoVen
	 *            the impuestoVen to set
	 */
	public void setImpuestoVen(long impuestoVen) {
		this.ImpuestoVen = impuestoVen;
	}

	/**
	 * @return the claveAutorizaAplicacionInmediata
	 */
	public String getClaveAutorizaAplicacionInmediata() {
		return ClaveAutorizaAplicacionInmediata;
	}

	/**
	 * @param claveAutorizaAplicacionInmediata
	 *            the claveAutorizaAplicacionInmediata to set
	 */
	public void setClaveAutorizaAplicacionInmediata(
			String claveAutorizaAplicacionInmediata) {
		this.ClaveAutorizaAplicacionInmediata = claveAutorizaAplicacionInmediata;
	}

	/**
	 * @return the fechaEnviada
	 */
	public long getFechaEnviada() {
		return FechaEnviada;
	}

	/**
	 * @param fechaEnviada
	 *            the fechaEnviada to set
	 */
	public void setFechaEnviada(long fechaEnviada) {
		this.FechaEnviada = fechaEnviada;
	}

	/**
	 * @return the productosDevueltos
	 */
	public DevolucionProducto[] getProductosDevueltos() {
		return ProductosDevueltos;
	}

	/**
	 * @param productosDevueltos
	 *            the productosDevueltos to set
	 */
	public void setProductosDevueltos(DevolucionProducto[] productosDevueltos) {
		this.ProductosDevueltos = productosDevueltos;
	}

	/**
	 * @return the pedidoTienePromociones
	 */
	public boolean isPedidoTienePromociones() {
		return PedidoTienePromociones;
	}

	/**
	 * @param pedidoTienePromociones
	 *            the pedidoTienePromociones to set
	 */
	public void setPedidoTienePromociones(boolean pedidoTienePromociones) {
		this.PedidoTienePromociones = pedidoTienePromociones;
	}

	/**
	 * @return the pedidoYaDevuelto
	 */
	public boolean isPedidoYaDevuelto() {
		return PedidoYaDevuelto;
	}

	/**
	 * @param pedidoYaDevuelto
	 *            the pedidoYaDevuelto to set
	 */
	public void setPedidoYaDevuelto(boolean pedidoYaDevuelto) {
		this.PedidoYaDevuelto = pedidoYaDevuelto;
	}

	/**
	 * @return the referenciaNC
	 */
	public int getReferenciaNC() {
		return ReferenciaNC;
	}

	/**
	 * @param referenciaNC
	 *            the referenciaNC to set
	 */
	public void setReferenciaNC(int referenciaNC) {
		this.ReferenciaNC = referenciaNC;
	}

	/**
	 * @return the preRegistro
	 */
	public boolean isPreRegistro() {
		return PreRegistro;
	}

	/**
	 * @param preRegistro
	 *            the preRegistro to set
	 */
	public void setPreRegistro(boolean preRegistro) {
		this.PreRegistro = preRegistro;
	}

	/**
	 * @return the offLine
	 */
	public boolean isOffLine() {
		return OffLine;
	}

	/**
	 * @param offLine
	 *            the offLine to set
	 */
	public void setOffLine(boolean offLine) {
		this.OffLine = offLine;
	}

	/**
	 * @return the fechaFacturacion
	 */
	public long getFechaFacturacion() {
		return FechaFacturacion;
	}

	/**
	 * @param fechaFacturacion
	 *            the fechaFacturacion to set
	 */
	public void setFechaFacturacion(long fechaFacturacion) {
		this.FechaFacturacion = fechaFacturacion;
	}

	/**
	 * @return the objPedido
	 */
	public Pedido getObjPedido() {
		return objPedido;
	}

	/**
	 * @param objPedido
	 *            the objPedido to set
	 */
	public void setObjPedido(Pedido objPedido) {
		this.objPedido = objPedido;
	}

	/**
	 * @return the olddata
	 */
	public Devolucion getOlddata() {
		return olddata;
	}

	/**
	 * @param olddata
	 *            the olddata to set
	 */
	public void setOlddata(Devolucion olddata) {
		try {
			this.olddata = (Devolucion) olddata.clone();
		} catch (CloneNotSupportedException e) {			
			e.printStackTrace();
		}
	}

	/**
	 * @param id
	 * @param referencia
	 * @param numeroCentral
	 * @param fecha
	 * @param objPedidoDevueltoID
	 * @param numeroPedidoDevuelto
	 * @param numeroFacturaDevuelta
	 * @param objVendedorID
	 * @param objClienteID
	 * @param objSucursalID
	 * @param nombreCliente
	 * @param objMotivoID
	 * @param codMotivo
	 * @param descMotivo
	 * @param tipoTramite
	 * @param deVencido
	 * @param parcial
	 * @param aplicacionInmediata
	 * @param nota
	 * @param observacion
	 * @param subtotal
	 * @param impuesto
	 * @param montoPromocion
	 * @param montoPromocionVen
	 * @param montoCargoAdm
	 * @param montoCargoAdmVen
	 * @param montoVinieta
	 * @param total
	 * @param totalVen
	 * @param objEstadoID
	 * @param descEstado
	 * @param codEstado
	 * @param objCausaEstadoID
	 * @param descCausaEstado
	 * @param especial
	 * @param montoCargoVendedor
	 * @param montoBonif
	 * @param montoBonifVen
	 * @param impuestoVen
	 * @param claveAutorizaAplicacionInmediata
	 * @param fechaEnviada
	 * @param productosDevueltos
	 * @param pedidoTienePromociones
	 * @param pedidoYaDevuelto
	 * @param referenciaNC
	 * @param preRegistro
	 * @param offLine
	 * @param fechaFacturacion
	 */
	public Devolucion(long id, int referencia, int numeroCentral, long fecha,
			long objPedidoDevueltoID, int numeroPedidoDevuelto,
			int numeroFacturaDevuelta, long objVendedorID, long objClienteID,
			long objSucursalID, String nombreCliente, long objMotivoID,
			String codMotivo, String descMotivo, String tipoTramite,
			boolean deVencido, boolean parcial, boolean aplicacionInmediata,
			String nota, String observacion, long subtotal, long impuesto,
			long montoPromocion, long montoPromocionVen, long montoCargoAdm,
			long montoCargoAdmVen, long montoVinieta, long total,
			long totalVen, long objEstadoID, String descEstado,
			String codEstado, long objCausaEstadoID, String descCausaEstado,
			boolean especial, long montoCargoVendedor, long montoBonif,
			long montoBonifVen, long impuestoVen,
			String claveAutorizaAplicacionInmediata, long fechaEnviada,
			DevolucionProducto[] productosDevueltos,
			boolean pedidoTienePromociones, boolean pedidoYaDevuelto,
			int referenciaNC, boolean preRegistro, boolean offLine,
			long fechaFacturacion) {
		this.Id = id;
		this.Referencia = referencia;
		this.NumeroCentral = numeroCentral;
		this.Fecha = fecha;
		this.ObjPedidoDevueltoID = objPedidoDevueltoID;
		this.NumeroPedidoDevuelto = numeroPedidoDevuelto;
		this.NumeroFacturaDevuelta = numeroFacturaDevuelta;
		this.ObjVendedorID = objVendedorID;
		this.ObjClienteID = objClienteID;
		this.ObjSucursalID = objSucursalID;
		this.NombreCliente = nombreCliente;
		this.ObjMotivoID = objMotivoID;
		this.CodMotivo = codMotivo;
		this.DescMotivo = descMotivo;
		this.TipoTramite = tipoTramite;
		this.DeVencido = deVencido;
		this.Parcial = parcial;
		this.AplicacionInmediata = aplicacionInmediata;
		this.Nota = nota;
		this.Observacion = observacion;
		this.Subtotal = subtotal;
		this.Impuesto = impuesto;
		this.MontoPromocion = montoPromocion;
		this.MontoPromocionVen = montoPromocionVen;
		this.MontoCargoAdm = montoCargoAdm;
		this.MontoCargoAdmVen = montoCargoAdmVen;
		this.MontoVinieta = montoVinieta;
		this.Total = total;
		this.TotalVen = totalVen;
		this.ObjEstadoID = objEstadoID;
		this.DescEstado = descEstado;
		this.CodEstado = codEstado;
		this.ObjCausaEstadoID = objCausaEstadoID;
		this.DescCausaEstado = descCausaEstado;
		this.Especial = especial;
		this.MontoCargoVendedor = montoCargoVendedor;
		this.MontoBonif = montoBonif;
		this.MontoBonifVen = montoBonifVen;
		this.ImpuestoVen = impuestoVen;
		this.ClaveAutorizaAplicacionInmediata = claveAutorizaAplicacionInmediata;
		this.FechaEnviada = fechaEnviada;
		this.ProductosDevueltos = productosDevueltos;
		this.PedidoTienePromociones = pedidoTienePromociones;
		this.PedidoYaDevuelto = pedidoYaDevuelto;
		this.ReferenciaNC = referenciaNC;
		this.PreRegistro = preRegistro;
		this.OffLine = offLine;
		this.FechaFacturacion = fechaFacturacion;
	}

	public Devolucion(Devolucion _devolucion) {
		try {
			this.olddata = (Devolucion) _devolucion.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private long Id;
	private int Referencia;
	private int NumeroCentral;
	private long Fecha;
	private long ObjPedidoDevueltoID;
	private int NumeroPedidoDevuelto;
	private int NumeroFacturaDevuelta;
	private long ObjVendedorID;
	private long ObjClienteID;
	private long ObjSucursalID;
	private String NombreCliente;
	private long ObjMotivoID;
	private String CodMotivo;
	private String DescMotivo;
	private String TipoTramite;
	private boolean DeVencido;
	private boolean Parcial;
	private boolean AplicacionInmediata;
	private String Nota;
	private String Observacion;
	private long Subtotal;
	private long Impuesto;
	private long MontoPromocion;
	private long MontoPromocionVen;
	private long MontoCargoAdm;
	private long MontoCargoAdmVen;
	private long MontoVinieta;
	private long Total;
	private long TotalVen;
	private long ObjEstadoID;
	private String DescEstado;
	private String CodEstado;
	private long ObjCausaEstadoID;
	private String DescCausaEstado;
	private boolean Especial;
	private long MontoCargoVendedor;
	private long MontoBonif;
	private long MontoBonifVen;
	private long ImpuestoVen;
	private String ClaveAutorizaAplicacionInmediata;
	private long FechaEnviada;
	private DevolucionProducto[] ProductosDevueltos;
	private long FechaFacturacion;
	private boolean PedidoTienePromociones;
	private boolean PedidoYaDevuelto;
	private int ReferenciaNC;
	private boolean PreRegistro;
	private boolean OffLine;
	private Pedido objPedido;

	protected Devolucion olddata;

	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return (Id);
		case 1:
			return (Referencia);
		case 2:
			return (NumeroCentral);
		case 3:
			return (Fecha);
		case 4:
			return ObjPedidoDevueltoID;
		case 5:
			return NumeroPedidoDevuelto;
		case 6:
			return NumeroFacturaDevuelta;
		case 7:
			return ObjVendedorID;
		case 8:
			return ObjClienteID;
		case 9:
			return ObjSucursalID;
		case 10:
			return NombreCliente;
		case 11:
			return ObjMotivoID; 
		case 12:
			return DescMotivo;
		case 13:
			return TipoTramite;
		case 14:
			return DeVencido;
		case 15:
			return Parcial;
		case 16:
			return AplicacionInmediata;
		case 17:
			return Nota;
		case 18:
			return Observacion;
		case 19:
			return Subtotal;
		case 20:
			return Impuesto;
		case 21:
			return MontoPromocion;
		case 22:
			return MontoPromocionVen;
		case 23:
			return MontoCargoAdm;
		case 24:
			return MontoCargoAdmVen;
		
		case 25:
			return Total;
		case 26:
			return TotalVen;
		case 27:
			return ObjEstadoID;
		case 28:
			return DescEstado;
		
		case 29:
			return ObjCausaEstadoID;
		case 30:
			return DescCausaEstado;
		case 31:
			return Especial;
		case 32:
			return MontoCargoVendedor;
		case 33:
			return MontoBonif;
		case 34:
			return MontoBonifVen;
		case 35:
			return ImpuestoVen;
		case 36:
			return ClaveAutorizaAplicacionInmediata;
		case 37:
			return FechaEnviada;
		case 38:
			if (ProductosDevueltos != null && ProductosDevueltos.length > 0) {

				SoapObject _detalle = new SoapObject("", "");

				if (ProductosDevueltos != null) {
					for (DevolucionProducto dp : ProductosDevueltos) {
						SoapObject item = new SoapObject("",
								"DevolucionProducto");
						int cont = dp.getPropertyCount();
						for (int i = 0; i < cont; i++) {
							PropertyInfo info = new PropertyInfo();
							dp.getPropertyInfo(i, null, info);
							if ("ProductoLotes" == info.name)
								item.addSoapObject((SoapObject) dp
										.getProperty(i));
							else
								item.addProperty(info.name, dp.getProperty(i));
						}
						_detalle.addSoapObject(item);

					}
				}

				return _detalle;
			}

			break;
		case 39:
			return CodEstado;
		case 40:
			return CodMotivo;
		
		
		case 41:
			return FechaFacturacion;
		case 42:
			return PedidoTienePromociones;
		case 43:
			return PedidoYaDevuelto;
		case 44:
			return MontoVinieta;
		case 45:
			return ReferenciaNC;
		case 46:
			return PreRegistro;			
		case 47:
			return OffLine;

		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 48;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
		try {
			switch (_index) {
			case 0:
				_info.name = "Id";
				_info.type = java.lang.Long.class;
				break;
			case 1:
				_info.name = "Referencia";
				_info.type = java.lang.Integer.class;
				break;
			case 2:
				_info.name = "NumeroCentral";
				_info.type = java.lang.Integer.class;
				break;
			case 3:
				_info.name = "Fecha";
				_info.type = java.lang.Long.class;
				break;
			case 4:
				_info.name = "ObjPedidoDevueltoID";
				_info.type = java.lang.Long.class;
				break;
			case 5:
				_info.name = "NumeroPedidoDevuelto";
				_info.type = java.lang.Integer.class;
				break;
			case 6:
				_info.name = "NumeroFacturaDevuelta";
				_info.type = java.lang.Integer.class;
				break;
			case 7:
				_info.name = "ObjVendedorID";
				_info.type = java.lang.Long.class;
				break;
			case 8:
				_info.name = "ObjClienteID";
				_info.type = java.lang.Long.class;
				break;
			case 9:
				_info.name = "ObjSucursalID";
				_info.type = java.lang.Long.class;
				break;
			case 10:
				_info.name = "NombreCliente";
				_info.type = java.lang.String.class;
				break;
			case 11:
				_info.name = "ObjMotivoID";
				_info.type = java.lang.Long.class;
				break;
			case 12:
				_info.name = "DescMotivo";
				_info.type = java.lang.String.class;
				break; 
			case 13:
				_info.name = "TipoTramite";
				_info.type = java.lang.String.class;
				break;
			case 14:
				_info.name = "DeVencido";
				_info.type = java.lang.Boolean.class;
				break;
			case 15:
				_info.name = "Parcial";
				_info.type = java.lang.Boolean.class;
				break;
			case 16:
				_info.name = "AplicacionInmediata";
				_info.type = java.lang.Boolean.class;
				break;
			case 17:
				_info.name = "Nota";
				_info.type = java.lang.String.class;
				break;
			case 18:
				_info.name = "Observacion";
				_info.type = java.lang.String.class;
				break;
			case 19:
				_info.name = "Subtotal";
				_info.type = java.lang.Long.class;
				break;
			case 20:
				_info.name = "Impuesto";
				_info.type = java.lang.Long.class;
				break;
			case 21:
				_info.name = "MontoPromocion";
				_info.type = java.lang.Long.class;
				break;
			case 22:
				_info.name = "MontoPromocionVen";
				_info.type = java.lang.Long.class;
				break;
			case 23:
				_info.name = "MontoCargoAdm";
				_info.type = java.lang.Long.class;
				break;
			case 24:
				_info.name = "MontoCargoAdmVen";
				_info.type = java.lang.Long.class;
				break;
			
			case 25:
				_info.name = "Total";
				_info.type = java.lang.Long.class;
				break;
			case 26:
				_info.name = "TotalVen";
				_info.type = java.lang.Long.class;
				break;
			case 27:
				_info.name = "ObjEstadoID";
				_info.type = java.lang.Long.class;
				break;
			case 28:
				_info.name = "DescEstado";
				_info.type = java.lang.String.class;
				break;
			case 29:
				_info.name = "ObjCausaEstadoID";
				_info.type = java.lang.Long.class;
				break;
			
			
			case 30:
				_info.name = "DescCausaEstado";
				_info.type = java.lang.String.class;
				break;
				
			case 31:
				_info.name = "Especial";
				_info.type = java.lang.Boolean.class;
				break;
			case 32:
				_info.name = "MontoCargoVendedor";
				_info.type = java.lang.Long.class;
				break;
			case 33:
				_info.name = "MontoBonif";
				_info.type = java.lang.Long.class;
				break;
			case 34:
				_info.name = "MontoBonifVen";
				_info.type = java.lang.Long.class;
				break;
			case 35:
				_info.name = "ImpuestoVen";
				_info.type = java.lang.Long.class;
				break;

			case 36:
				_info.name = "ClaveAutorizaAplicacionInmediata";
				_info.type = java.lang.String.class;
				break;
			case 37:
				_info.name = "FechaEnviada";
				_info.type = java.lang.Long.class;
				break;

			case 38:
				if (ProductosDevueltos != null && ProductosDevueltos.length > 0) {
					_info.name = "ProductosDevueltos";
					_info.type = DevolucionProducto[].class;
					break;
				}
				break;
			case 39:
				_info.name = "CodEstado";
				_info.type = java.lang.String.class;
				break;
			case 40:
				_info.name = "CodMotivo";
				_info.type = java.lang.String.class;
				break;
				
			case 41:
				_info.name = "FechaFacturacion";
				_info.type = java.lang.Long.class;
				break;
			case 42:
				_info.name = "PedidoTienePromociones";
				_info.type = java.lang.Boolean.class;
				break;
			case 43:
				_info.name = "PedidoYaDevuelto";
				_info.type = java.lang.Boolean.class;
				break;
			case 44:
				_info.name = "MontoVinieta";
				_info.type = java.lang.Long.class;
				break;
			case 45:
				_info.name = "ReferenciaNC";
				_info.type = java.lang.Boolean.class;
				break;
			case 46:
				_info.name = "PreRegistro";
				_info.type = java.lang.Boolean.class;
				break;
			case 47:
				_info.name = "OffLine";
				_info.type = java.lang.Boolean.class;
				break; 
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setProperty(int _index, Object _) {
		switch (_index) {
		case 0:
			Id = java.lang.Long.parseLong(_.toString());
			break;
		case 1:
			Referencia = java.lang.Integer.parseInt(_.toString());
			break;
		case 2:
			NumeroCentral = java.lang.Integer.parseInt(_.toString());
			break;
		case 3:
			Fecha = java.lang.Long.parseLong(_.toString());
			break;
		case 4:
			ObjPedidoDevueltoID = java.lang.Long.parseLong(_.toString());
			break;
		case 5:
			NumeroPedidoDevuelto = java.lang.Integer.parseInt(_.toString());
			break;
		case 6:
			NumeroFacturaDevuelta = java.lang.Integer.parseInt(_.toString());
			break;
		case 7:
			ObjVendedorID = java.lang.Long.parseLong(_.toString());
			break;
		case 8:
			ObjClienteID = java.lang.Long.parseLong(_.toString());
			break;
		case 9:
			ObjSucursalID = java.lang.Long.parseLong(_.toString());
			break;
		case 10:
			NombreCliente = (java.lang.String) _;
			break;
		case 11:
			ObjMotivoID = java.lang.Long.parseLong(_.toString());
			break;
		
		case 12:
			DescMotivo = (java.lang.String) _;
			break;
		case 13:
			TipoTramite = (java.lang.String) _;
			break;
		case 14:
			DeVencido = "true".equals(_.toString());
			break;
		case 15:
			Parcial = "true".equals(_.toString());
			break;
		case 16:
			AplicacionInmediata = "true".equals(_.toString());
			break;
		case 17:
			Nota = (java.lang.String) _;
			break;
		case 18:
			Observacion = (java.lang.String) _;
			break;
		case 19:
			Subtotal = java.lang.Long.parseLong(_.toString());
			break;
		case 20:
			Impuesto = java.lang.Long.parseLong(_.toString());
			break;
		case 21:
			MontoPromocion = java.lang.Long.parseLong(_.toString());
			break;
		case 22:
			MontoPromocionVen = java.lang.Long.parseLong(_.toString());
			break;
		case 23:
			MontoCargoAdm = java.lang.Long.parseLong(_.toString());
			break;
		case 24:
			MontoCargoAdmVen = java.lang.Long.parseLong(_.toString());
			break;
		
		case 25:
			Total = java.lang.Long.parseLong(_.toString());
			break;
		case 26:
			TotalVen = java.lang.Long.parseLong(_.toString());
			break;
		case 27:
			ObjEstadoID = java.lang.Long.parseLong(_.toString());
			break;
		case 28:
			DescEstado = (java.lang.String) _;
			break;
		
		case 29:
			ObjCausaEstadoID = java.lang.Long.parseLong(_.toString());
			break;
		case 30:
			DescCausaEstado = (java.lang.String) _;
			break;
		case 31:
			Especial = "true".equals(_.toString());
			break;
		case 32:
			MontoCargoVendedor = java.lang.Long.parseLong(_.toString());
			break;
		case 33:
			MontoBonif = java.lang.Long.parseLong(_.toString());
			break;
		case 34:
			MontoBonifVen = java.lang.Long.parseLong(_.toString());
			break;
		case 35:
			ImpuestoVen = java.lang.Long.parseLong(_.toString());
			break;
		case 36:
			ClaveAutorizaAplicacionInmediata = (java.lang.String) _;
			break;
		case 37:
			FechaEnviada = java.lang.Long.parseLong(_.toString());
			break;
		case 38:
			ProductosDevueltos = (DevolucionProducto[]) (_);
			break;
			
		case 39:
			CodEstado = (java.lang.String) _;
			break;
		case 40:
			CodMotivo = (java.lang.String) _;
			break;
		case 41:
			FechaFacturacion = java.lang.Long.parseLong(_.toString());
			break;
		case 42:
			PedidoTienePromociones = "true".equals(_.toString());
			break;
		case 43:
			PedidoYaDevuelto = "true".equals(_.toString());
			break;
		case 44:
			MontoVinieta = java.lang.Long.parseLong(_.toString());
			break;
		case 45:
			ReferenciaNC = java.lang.Integer.parseInt(_.toString());
			break;
		case 46:
			PreRegistro = "true".equals(_.toString());
			break;
		case 47:
			OffLine = "true".equals(_.toString());
			break;
		
		
		
		}

	}

	public boolean hasModified() {
		return !equals(olddata);
	}

	@Override
	public boolean equals(Object obj) {		
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Devolucion other = (Devolucion) obj;
		if (AplicacionInmediata != other.AplicacionInmediata)
			return false;
		if (ClaveAutorizaAplicacionInmediata == null) {
			if (other.ClaveAutorizaAplicacionInmediata != null)
				return false;
		} else if (!ClaveAutorizaAplicacionInmediata
				.equals(other.ClaveAutorizaAplicacionInmediata))
			return false;
		if (CodEstado == null) {
			if (other.CodEstado != null)
				return false;
		} else if (!CodEstado.equals(other.CodEstado))
			return false;
		if (CodMotivo == null) {
			if (other.CodMotivo != null)
				return false;
		} else if (!CodMotivo.equals(other.CodMotivo))
			return false;
		if (DeVencido != other.DeVencido)
			return false;
		if (DescCausaEstado == null) {
			if (other.DescCausaEstado != null)
				return false;
		} else if (!DescCausaEstado.equals(other.DescCausaEstado))
			return false;
		if (DescEstado == null) {
			if (other.DescEstado != null)
				return false;
		} else if (!DescEstado.equals(other.DescEstado))
			return false;
		if (DescMotivo == null) {
			if (other.DescMotivo != null)
				return false;
		} else if (!DescMotivo.equals(other.DescMotivo))
			return false;
		if (Especial != other.Especial)
			return false;
		if (DateUtil.time2int(Fecha) != DateUtil.time2int(other.Fecha))
			return false;
		if (DateUtil.time2int(FechaEnviada) != DateUtil.time2int(other.FechaEnviada))
			return false;
		if (DateUtil.time2int(FechaFacturacion) != DateUtil.time2int(other.FechaFacturacion))
			return false;
		if (Id != other.Id)
			return false;		
		if (Nota == null) {
			if (other.Nota != null)
				return false;
		} else if (!Nota.equals(other.Nota))
			return false;
		if (NumeroCentral != other.NumeroCentral)
			return false;
		if (NumeroFacturaDevuelta != other.NumeroFacturaDevuelta)
			return false;
		if (NumeroPedidoDevuelto != other.NumeroPedidoDevuelto)
			return false;
		if (ObjCausaEstadoID != other.ObjCausaEstadoID)
			return false;
		if (ObjClienteID != other.ObjClienteID)
			return false;
		if (ObjEstadoID != other.ObjEstadoID)
			return false;
		if (ObjMotivoID != other.ObjMotivoID)
			return false;
		if (ObjSucursalID != other.ObjSucursalID)
			return false;
		if (ObjVendedorID != other.ObjVendedorID)
			return false;
		if (Observacion == null) {
			if (other.Observacion != null)
				return false;
		} else if (!Observacion.equals(other.Observacion))
			return false;
		if (OffLine != other.OffLine)
			return false;
		if (Parcial != other.Parcial)
			return false;
		if (PedidoTienePromociones != other.PedidoTienePromociones)
			return false;
		if (PedidoYaDevuelto != other.PedidoYaDevuelto)
			return false;
		if (PreRegistro != other.PreRegistro)
			return false;
		if (!Arrays.equals(ProductosDevueltos, other.ProductosDevueltos))
			return false;
		if (Referencia != other.Referencia)
			return false;
		if (ReferenciaNC != other.ReferenciaNC)
			return false;
		if (TipoTramite == null) {
			if (other.TipoTramite != null)
				return false;
		} else if (!TipoTramite.equals(other.TipoTramite))
			return false;		
		if (objPedido == null) {
			if (other.objPedido != null)
				return false;
		} else if (!objPedido.equals(other.objPedido))
			return false;		
		return true;
	}

	public void setOldData(Devolucion _devolucion) {
		this.olddata = new Devolucion(this);
	}

	public Devolucion getOldData() {
		return olddata;
	}

	public static final Parcelable.Creator<com.panzyma.nm.serviceproxy.Devolucion> CREATOR = new Parcelable.Creator<com.panzyma.nm.serviceproxy.Devolucion>() {
		@Override
		public com.panzyma.nm.serviceproxy.Devolucion createFromParcel(
				Parcel parcel) {
			return new Devolucion(parcel);
		}

		@Override
		public com.panzyma.nm.serviceproxy.Devolucion[] newArray(int size) {
			return new Devolucion[size];
		}
	};

	public Devolucion(Parcel parcel) {
		this.Id = parcel.readLong();
		this.Referencia = parcel.readInt();
		this.NumeroCentral = parcel.readInt();
		this.Fecha = parcel.readLong();
		this.ObjPedidoDevueltoID = parcel.readLong();
		this.NumeroPedidoDevuelto = parcel.readInt();
		this.NumeroFacturaDevuelta = parcel.readInt();
		this.ObjVendedorID = parcel.readLong();
		this.ObjClienteID = parcel.readLong();
		this.ObjSucursalID = parcel.readLong();
		this.NombreCliente = parcel.readString();
		this.ObjMotivoID = parcel.readLong();
		this.CodMotivo = parcel.readString();
		this.DescMotivo = parcel.readString();
		this.TipoTramite = parcel.readString();
		this.DeVencido = (parcel.readInt() == 1);
		this.Parcial = (parcel.readInt() == 1);
		this.AplicacionInmediata = (parcel.readInt() == 1);
		this.Nota = parcel.readString();
		this.Observacion = parcel.readString();
		this.Subtotal = parcel.readLong();
		this.Impuesto = parcel.readLong();
		this.MontoPromocion = parcel.readLong();
		this.MontoPromocionVen = parcel.readLong();
		this.MontoCargoAdm = parcel.readLong();
		this.MontoCargoAdmVen = parcel.readLong();
		this.MontoVinieta = parcel.readLong();
		this.Total = parcel.readLong();
		this.TotalVen = parcel.readLong();
		this.ObjEstadoID = parcel.readLong();
		this.DescEstado = parcel.readString();
		this.CodEstado = parcel.readString();
		this.ObjCausaEstadoID = parcel.readLong();
		this.DescCausaEstado = parcel.readString();
		this.Especial = (parcel.readInt() == 1);
		this.MontoCargoVendedor = parcel.readLong();
		this.MontoBonif = parcel.readLong();
		this.MontoBonifVen = parcel.readLong();
		this.ImpuestoVen = parcel.readLong();
		this.FechaEnviada = parcel.readLong();
		this.ClaveAutorizaAplicacionInmediata = parcel.readString();

		Parcelable[] parcelableArray = parcel
				.readParcelableArray(com.panzyma.nm.serviceproxy.DevolucionProducto.class
						.getClassLoader());
		if (parcelableArray != null) {
			ProductosDevueltos = Arrays.copyOf(parcelableArray,
					parcelableArray.length,
					com.panzyma.nm.serviceproxy.DevolucionProducto[].class);
		}

		this.PedidoTienePromociones = (parcel.readInt() == 1);
		this.PedidoYaDevuelto = (parcel.readInt() == 1);
		this.ReferenciaNC = parcel.readInt();
		this.PreRegistro = (parcel.readInt() == 1);
		this.OffLine = (parcel.readInt() == 1);
		this.FechaFacturacion = parcel.readLong();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(this.Id);
		parcel.writeInt(this.Referencia);
		parcel.writeInt(this.NumeroCentral);
		parcel.writeLong(this.Fecha);
		parcel.writeLong(this.ObjPedidoDevueltoID);
		parcel.writeInt(this.NumeroPedidoDevuelto);
		parcel.writeInt(this.NumeroFacturaDevuelta);
		parcel.writeLong(this.ObjVendedorID);
		parcel.writeLong(this.ObjClienteID);
		parcel.writeLong(this.ObjSucursalID);
		parcel.writeString(this.NombreCliente);
		parcel.writeLong(this.ObjMotivoID);
		parcel.writeString(this.CodMotivo);
		parcel.writeString(this.DescMotivo);
		parcel.writeString(this.TipoTramite);
		parcel.writeInt((this.DeVencido ? 1 : 0));
		parcel.writeInt((this.Parcial ? 1 : 0));
		parcel.writeInt((this.AplicacionInmediata ? 1 : 0));
		parcel.writeString(this.Nota);
		parcel.writeString(this.Observacion);
		parcel.writeLong(this.Subtotal);
		parcel.writeLong(this.Impuesto);
		parcel.writeLong(this.MontoPromocion);
		parcel.writeLong(this.MontoPromocionVen);
		parcel.writeLong(this.MontoCargoAdm);
		parcel.writeLong(this.MontoCargoAdmVen);
		parcel.writeLong(this.MontoVinieta);
		parcel.writeLong(this.Total);
		parcel.writeLong(this.TotalVen);
		parcel.writeLong(this.ObjEstadoID);
		parcel.writeString(this.DescEstado);
		parcel.writeString(this.CodEstado);
		parcel.writeLong(this.ObjCausaEstadoID);
		parcel.writeString(this.DescCausaEstado);
		parcel.writeInt((this.Especial ? 1 : 0));
		parcel.writeLong(this.MontoCargoVendedor);
		parcel.writeLong(this.MontoBonif);
		parcel.writeLong(this.MontoBonifVen);
		parcel.writeLong(this.ImpuestoVen);
		parcel.writeLong(this.FechaEnviada);
		parcel.writeString(this.ClaveAutorizaAplicacionInmediata);
		// parcel.writeParcelableArray(ProductosDevueltos, flags);
		List<DevolucionProducto> dev_prod = new ArrayList<DevolucionProducto>(Arrays.asList(ProductosDevueltos));
		parcel.writeTypedList(dev_prod);		
		parcel.writeInt((this.PedidoTienePromociones ? 1 : 0));
		parcel.writeInt((this.PedidoYaDevuelto ? 1 : 0));
		parcel.writeInt(this.ReferenciaNC);
		parcel.writeInt((this.PreRegistro ? 1 : 0));
		parcel.writeInt((this.OffLine ? 1 : 0));
		parcel.writeLong(this.FechaFacturacion);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
