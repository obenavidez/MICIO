package com.panzyma.nm.serviceproxy;

import java.util.List;

public class EncabezadoSolicitud {
	private long id;
	private long objReciboID;
	private String codigoEstado;
	private String descripcionEstado;
	private long fechaSolicitud;
	private List<SolicitudDescuento> detalles;

	public EncabezadoSolicitud() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EncabezadoSolicitud(long id, long objReciboID, String codigoEstado,
			String descripcionEstado, long fechaSolicitud) {
		super();
		this.id = id;
		this.objReciboID = objReciboID;
		this.codigoEstado = codigoEstado;
		this.descripcionEstado = descripcionEstado;
		this.fechaSolicitud = fechaSolicitud;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getObjReciboID() {
		return objReciboID;
	}

	public void setObjReciboID(long objReciboID) {
		this.objReciboID = objReciboID;
	}

	public String getCodigoEstado() {
		return codigoEstado;
	}

	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	public String getDescripcionEstado() {
		return descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	public long getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(long fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	
	public List<SolicitudDescuento> getDetalles() {
		return detalles;
	}
	

	public void setDetalles(List<SolicitudDescuento> detalles) {
		this.detalles = detalles;
	}

}
