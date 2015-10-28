package com.panzyma.nm.serviceproxy;
 
public class DevolucionProducto 
{
	/**
	 * @param id
	 * @param objProductoID
	 * @param nombreProducto
	 * @param cantidadDevolver
	 * @param bonificacion
	 * @param bonificacionVen
	 * @param precio
	 * @param subtotal
	 * @param porcImpuesto
	 * @param impuesto
	 * @param total
	 * @param totalVen
	 * @param montoBonif
	 * @param montoBonifVen
	 * @param impuestoVen
	 * @param cantidadOrdenada
	 * @param cantidadBonificada
	 * @param cantidadPromocionada
	 * @param descuento
	 * @param totalProducto
	 * @param gravable
	 * @param deleted
	 * @param objProveedorID
	 * @param productoLotes
	 */
	public DevolucionProducto(long id, long objProductoID,
			String nombreProducto, int cantidadDevolver, int bonificacion,
			int bonificacionVen, long precio, long subtotal, long porcImpuesto,
			long impuesto, long total, long totalVen, long montoBonif,
			long montoBonifVen, long impuestoVen, int cantidadOrdenada,
			int cantidadBonificada, int cantidadPromocionada, long descuento,
			int totalProducto, boolean gravable, boolean deleted,
			long objProveedorID, DevolucionProductoLote[] productoLotes) {
		this.Id = id;
		this.ObjProductoID = objProductoID;
		this.NombreProducto = nombreProducto;
		this.cantidadDevolver = cantidadDevolver;
		this.bonificacion = bonificacion;
		this.bonificacionVen = bonificacionVen;
		this.precio = precio;
		this.subtotal = subtotal;
		this.porcImpuesto = porcImpuesto;
		this.impuesto = impuesto;
		this.total = total;
		this.totalVen = totalVen;
		this.montoBonif = montoBonif;
		this.montoBonifVen = montoBonifVen;
		this.impuestoVen = impuestoVen;
		this.cantidadOrdenada = cantidadOrdenada;
		this.cantidadBonificada = cantidadBonificada;
		this.cantidadPromocionada = cantidadPromocionada;
		this.descuento = descuento;
		this.totalProducto = totalProducto;
		this.gravable = gravable;
		this.deleted = deleted;
		this.objProveedorID = objProveedorID;
		this.productoLotes = productoLotes;
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
	 * @return the objProductoID
	 */
	public long getObjProductoID() {
		return ObjProductoID;
	}
	/**
	 * @param objProductoID the objProductoID to set
	 */
	public void setObjProductoID(long objProductoID) {
		this.ObjProductoID = objProductoID;
	}
	/**
	 * @return the nombreProducto
	 */
	public String getNombreProducto() {
		return NombreProducto;
	}
	/**
	 * @param nombreProducto the nombreProducto to set
	 */
	public void setNombreProducto(String nombreProducto) {
		this.NombreProducto = nombreProducto;
	}
	/**
	 * @return the cantidadDevolver
	 */
	public int getCantidadDevolver() {
		return cantidadDevolver;
	}
	/**
	 * @param cantidadDevolver the cantidadDevolver to set
	 */
	public void setCantidadDevolver(int cantidadDevolver) {
		this.cantidadDevolver = cantidadDevolver;
	}
	/**
	 * @return the bonificacion
	 */
	public int getBonificacion() {
		return bonificacion;
	}
	/**
	 * @param bonificacion the bonificacion to set
	 */
	public void setBonificacion(int bonificacion) {
		this.bonificacion = bonificacion;
	}
	/**
	 * @return the bonificacionVen
	 */
	public int getBonificacionVen() {
		return bonificacionVen;
	}
	/**
	 * @param bonificacionVen the bonificacionVen to set
	 */
	public void setBonificacionVen(int bonificacionVen) {
		this.bonificacionVen = bonificacionVen;
	}
	/**
	 * @return the precio
	 */
	public long getPrecio() {
		return precio;
	}
	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(long precio) {
		this.precio = precio;
	}
	/**
	 * @return the subtotal
	 */
	public long getSubtotal() {
		return subtotal;
	}
	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(long subtotal) {
		this.subtotal = subtotal;
	}
	/**
	 * @return the porcImpuesto
	 */
	public long getPorcImpuesto() {
		return porcImpuesto;
	}
	/**
	 * @param porcImpuesto the porcImpuesto to set
	 */
	public void setPorcImpuesto(long porcImpuesto) {
		this.porcImpuesto = porcImpuesto;
	}
	/**
	 * @return the impuesto
	 */
	public long getImpuesto() {
		return impuesto;
	}
	/**
	 * @param impuesto the impuesto to set
	 */
	public void setImpuesto(long impuesto) {
		this.impuesto = impuesto;
	}
	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}
	/**
	 * @return the totalVen
	 */
	public long getTotalVen() {
		return totalVen;
	}
	/**
	 * @param totalVen the totalVen to set
	 */
	public void setTotalVen(long totalVen) {
		this.totalVen = totalVen;
	}
	/**
	 * @return the montoBonif
	 */
	public long getMontoBonif() {
		return montoBonif;
	}
	/**
	 * @param montoBonif the montoBonif to set
	 */
	public void setMontoBonif(long montoBonif) {
		this.montoBonif = montoBonif;
	}
	/**
	 * @return the montoBonifVen
	 */
	public long getMontoBonifVen() {
		return montoBonifVen;
	}
	/**
	 * @param montoBonifVen the montoBonifVen to set
	 */
	public void setMontoBonifVen(long montoBonifVen) {
		this.montoBonifVen = montoBonifVen;
	}
	/**
	 * @return the impuestoVen
	 */
	public long getImpuestoVen() {
		return impuestoVen;
	}
	/**
	 * @param impuestoVen the impuestoVen to set
	 */
	public void setImpuestoVen(long impuestoVen) {
		this.impuestoVen = impuestoVen;
	}
	/**
	 * @return the cantidadOrdenada
	 */
	public int getCantidadOrdenada() {
		return cantidadOrdenada;
	}
	/**
	 * @param cantidadOrdenada the cantidadOrdenada to set
	 */
	public void setCantidadOrdenada(int cantidadOrdenada) {
		this.cantidadOrdenada = cantidadOrdenada;
	}
	/**
	 * @return the cantidadBonificada
	 */
	public int getCantidadBonificada() {
		return cantidadBonificada;
	}
	/**
	 * @param cantidadBonificada the cantidadBonificada to set
	 */
	public void setCantidadBonificada(int cantidadBonificada) {
		this.cantidadBonificada = cantidadBonificada;
	}
	/**
	 * @return the cantidadPromocionada
	 */
	public int getCantidadPromocionada() {
		return cantidadPromocionada;
	}
	/**
	 * @param cantidadPromocionada the cantidadPromocionada to set
	 */
	public void setCantidadPromocionada(int cantidadPromocionada) {
		this.cantidadPromocionada = cantidadPromocionada;
	}
	/**
	 * @return the descuento
	 */
	public long getDescuento() {
		return descuento;
	}
	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(long descuento) {
		this.descuento = descuento;
	}
	/**
	 * @return the totalProducto
	 */
	public int getTotalProducto() {
		return totalProducto;
	}
	/**
	 * @param totalProducto the totalProducto to set
	 */
	public void setTotalProducto(int totalProducto) {
		this.totalProducto = totalProducto;
	}
	/**
	 * @return the gravable
	 */
	public boolean isGravable() {
		return gravable;
	}
	/**
	 * @param gravable the gravable to set
	 */
	public void setGravable(boolean gravable) {
		this.gravable = gravable;
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
	 * @return the objProveedorID
	 */
	public long getObjProveedorID() {
		return objProveedorID;
	}
	/**
	 * @param objProveedorID the objProveedorID to set
	 */
	public void setObjProveedorID(long objProveedorID) {
		this.objProveedorID = objProveedorID;
	}
	/**
	 * @return the productoLotes
	 */
	public DevolucionProductoLote[] getProductoLotes() {
		return productoLotes;
	}
	/**
	 * @param productoLotes the productoLotes to set
	 */
	public void setProductoLotes(DevolucionProductoLote[] productoLotes) {
		this.productoLotes = productoLotes;
	}
	private long Id;
	private long ObjProductoID;
	private String NombreProducto;
	private int cantidadDevolver;
	private int bonificacion;
	private int bonificacionVen;
	private long precio;
	private long subtotal;
	private long porcImpuesto;
	private long impuesto;
	private long total;
	private long totalVen;
	private long montoBonif;
	private long montoBonifVen;
	private long impuestoVen;
	private int cantidadOrdenada;
	private int cantidadBonificada;
	private int cantidadPromocionada;
	private long descuento;
	private int totalProducto;
	private boolean gravable;
	private boolean deleted;
	private long objProveedorID;
	private DevolucionProductoLote[] productoLotes;
	
	public DevolucionProducto(){}
	 
}
