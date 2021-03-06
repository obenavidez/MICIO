package com.panzyma.nm.serviceproxy;

public class SolicitudDescuento 
{
	private long id;
	private long encabezadoSolicitudId;
	private long facturaId;
	private float porcentaje;
	private String justificacion;
	private long fecha;
	private String docstatus;
   

	protected Factura factura;
	
	public SolicitudDescuento(long id, long reciboId, long facturaId,
			float porcentaje, String justificacion, long fecha) {
		super();
		this.id = id;
		this.encabezadoSolicitudId = reciboId;
		this.facturaId = facturaId;
		this.porcentaje = porcentaje;
		this.justificacion = justificacion;
		this.fecha = fecha;
	}

	public SolicitudDescuento(long id, long encabezadoSolicitudId,long reciboId, long facturaId,
			float porcentaje, String justificacion, long fecha,Factura _factura) {
		super();
		this.id = id;
		this.encabezadoSolicitudId = reciboId;
		this.facturaId = facturaId;
		this.porcentaje = porcentaje;
		this.justificacion = justificacion;
		this.fecha = fecha;
		this.factura = _factura;
	}

	public SolicitudDescuento(long id, long encabezadoSolicitudId,long reciboId, long facturaId,
			float porcentaje, String justificacion, long fecha,Factura _factura,String _docstatus) {
		super();
		this.id = id;
		this.encabezadoSolicitudId = reciboId;
		this.facturaId = facturaId;
		this.porcentaje = porcentaje;
		this.justificacion = justificacion;
		this.fecha = fecha;
		this.factura = _factura;
		this.docstatus=_docstatus;
	}
	
	public SolicitudDescuento() {
		// TODO Auto-generated constructor stub
	}
	
	public void setStatus(String status)
	{
		this.docstatus=status;
	}
	
	public String getStatus()
	{
		return this.docstatus;
	}
	
	public void setFactura(Factura fact)
	{
		factura=fact;
	}

	public Factura getFactura()
	{
		return factura;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEncabezadoSolicitudId() {
		return encabezadoSolicitudId;
	}

	public void setEncabezadoSolicitudId(long encabezadoSolicitudId) {
		this.encabezadoSolicitudId = encabezadoSolicitudId;
	}

	public long getFacturaId() {
		return facturaId;
	}

	public void setFacturaId(long facturaId) {
		this.facturaId = facturaId;
	}

	public float getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(float porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getJustificacion() {
		return justificacion;
	}

	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}

}
