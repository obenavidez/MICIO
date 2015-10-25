package com.panzyma.nm.serviceproxy;

public class Devolucion {
	
	public Devolucion(){
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
	 * @param id the id to set
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
	 * @param referencia the referencia to set
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
	 * @param numeroCentral the numeroCentral to set
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
	 * @param fecha the fecha to set
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
	 * @param objPedidoDevueltoID the objPedidoDevueltoID to set
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
	 * @param numeroPedidoDevuelto the numeroPedidoDevuelto to set
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
	 * @param numeroFacturaDevuelta the numeroFacturaDevuelta to set
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
	 * @param objVendedorID the objVendedorID to set
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
	 * @param objClienteID the objClienteID to set
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
	 * @param objSucursalID the objSucursalID to set
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
	 * @param nombreCliente the nombreCliente to set
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
	 * @param objMotivoID the objMotivoID to set
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
	 * @param codMotivo the codMotivo to set
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
	 * @param descMotivo the descMotivo to set
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
	 * @param tipoTramite the tipoTramite to set
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
	 * @param deVencido the deVencido to set
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
	 * @param parcial the parcial to set
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
	 * @param aplicacionInmediata the aplicacionInmediata to set
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
	 * @param nota the nota to set
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
	 * @param observacion the observacion to set
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
	 * @param subtotal the subtotal to set
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
	 * @param impuesto the impuesto to set
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
	 * @param montoPromocion the montoPromocion to set
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
	 * @param montoPromocionVen the montoPromocionVen to set
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
	 * @param montoCargoAdm the montoCargoAdm to set
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
	 * @param montoCargoAdmVen the montoCargoAdmVen to set
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
	 * @param montoVinieta the montoVinieta to set
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
	 * @param total the total to set
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
	 * @param totalVen the totalVen to set
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
	 * @param objEstadoID the objEstadoID to set
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
	 * @param descEstado the descEstado to set
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
	 * @param codEstado the codEstado to set
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
	 * @param objCausaEstadoID the objCausaEstadoID to set
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
	 * @param descCausaEstado the descCausaEstado to set
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
	 * @param especial the especial to set
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
	 * @param montoCargoVendedor the montoCargoVendedor to set
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
	 * @param montoBonif the montoBonif to set
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
	 * @param montoBonifVen the montoBonifVen to set
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
	 * @param impuestoVen the impuestoVen to set
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
	 * @param claveAutorizaAplicacionInmediata the claveAutorizaAplicacionInmediata to set
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
	 * @param fechaEnviada the fechaEnviada to set
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
	 * @param productosDevueltos the productosDevueltos to set
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
	 * @param pedidoTienePromociones the pedidoTienePromociones to set
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
	 * @param pedidoYaDevuelto the pedidoYaDevuelto to set
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
	 * @param referenciaNC the referenciaNC to set
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
	 * @param preRegistro the preRegistro to set
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
	 * @param offLine the offLine to set
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
	 * @param fechaFacturacion the fechaFacturacion to set
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
	 * @param objPedido the objPedido to set
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
	 * @param olddata the olddata to set
	 */
	public void setOlddata(Devolucion olddata) {
		this.olddata = olddata;
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
	

	
}
