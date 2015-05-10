package com.panzyma.nm.serviceproxy;

public class DevolucionProductoLote
{
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the objLoteID
	 */
	public long getObjLoteID() {
		return objLoteID;
	}
	/**
	 * @param objLoteID the objLoteID to set
	 */
	public void setObjLoteID(long objLoteID) {
		this.objLoteID = objLoteID;
	}
	/**
	 * @return the numeroLote
	 */
	public String getNumeroLote() {
		return numeroLote;
	}
	/**
	 * @param numeroLote the numeroLote to set
	 */
	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}
	/**
	 * @return the fechaVencimiento
	 */
	public int getFechaVencimiento() {
		return fechaVencimiento;
	}
	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(int fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	/**
	 * @return the cantidadDevuelta
	 */
	public int getCantidadDevuelta() {
		return cantidadDevuelta;
	}
	/**
	 * @param cantidadDevuelta the cantidadDevuelta to set
	 */
	public void setCantidadDevuelta(int cantidadDevuelta) {
		this.cantidadDevuelta = cantidadDevuelta;
	}
	/**
	 * @return the fueraPolitica
	 */
	public boolean isFueraPolitica() {
		return fueraPolitica;
	}
	/**
	 * @param fueraPolitica the fueraPolitica to set
	 */
	public void setFueraPolitica(boolean fueraPolitica) {
		this.fueraPolitica = fueraPolitica;
	}
	/**
	 * @return the cantidadDespachada
	 */
	public int getCantidadDespachada() {
		return cantidadDespachada;
	}
	/**
	 * @param cantidadDespachada the cantidadDespachada to set
	 */
	public void setCantidadDespachada(int cantidadDespachada) {
		this.cantidadDespachada = cantidadDespachada;
	}
	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}
	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	/**
	 * @param id
	 * @param objLoteID
	 * @param numeroLote
	 * @param fechaVencimiento
	 * @param cantidadDevuelta
	 * @param fueraPolitica
	 * @param cantidadDespachada
	 * @param deleted
	 */
	public DevolucionProductoLote(long id, long objLoteID, String numeroLote,
			int fechaVencimiento, int cantidadDevuelta, boolean fueraPolitica,
			int cantidadDespachada, boolean deleted) {
		super();
		this.id = id;
		this.objLoteID = objLoteID;
		this.numeroLote = numeroLote;
		this.fechaVencimiento = fechaVencimiento;
		this.cantidadDevuelta = cantidadDevuelta;
		this.fueraPolitica = fueraPolitica;
		this.cantidadDespachada = cantidadDespachada;
		this.deleted = deleted;
	}
	private long id;
	private long objLoteID;
	private String numeroLote;
	private int fechaVencimiento;
	private int cantidadDevuelta;
	private boolean fueraPolitica;
	private int cantidadDespachada;
	private boolean deleted;
}