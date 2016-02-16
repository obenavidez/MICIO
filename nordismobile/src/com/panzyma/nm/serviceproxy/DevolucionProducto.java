package com.panzyma.nm.serviceproxy;

import java.util.Arrays;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import android.os.Parcel;
import android.os.Parcelable;

public class DevolucionProducto implements KvmSerializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5389784331024111849L;

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
	 * @param id
	 *            the id to set
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
	 * @param objProductoID
	 *            the objProductoID to set
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
	 * @param nombreProducto
	 *            the nombreProducto to set
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
	 * @param cantidadDevolver
	 *            the cantidadDevolver to set
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
	 * @param bonificacion
	 *            the bonificacion to set
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
	 * @param bonificacionVen
	 *            the bonificacionVen to set
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
	 * @param precio
	 *            the precio to set
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
	 * @param subtotal
	 *            the subtotal to set
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
	 * @param porcImpuesto
	 *            the porcImpuesto to set
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
	 * @param impuesto
	 *            the impuesto to set
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
	 * @param total
	 *            the total to set
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
	 * @param totalVen
	 *            the totalVen to set
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
	 * @param montoBonif
	 *            the montoBonif to set
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
	 * @param montoBonifVen
	 *            the montoBonifVen to set
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
	 * @param impuestoVen
	 *            the impuestoVen to set
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
	 * @param cantidadOrdenada
	 *            the cantidadOrdenada to set
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
	 * @param cantidadBonificada
	 *            the cantidadBonificada to set
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
	 * @param cantidadPromocionada
	 *            the cantidadPromocionada to set
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
	 * @param descuento
	 *            the descuento to set
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
	 * @param totalProducto
	 *            the totalProducto to set
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
	 * @param gravable
	 *            the gravable to set
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
	 * @param deleted
	 *            the deleted to set
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
	 * @param objProveedorID
	 *            the objProveedorID to set
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
	 * @param productoLotes
	 *            the productoLotes to set
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

	public DevolucionProducto() {
	}

	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return new Long(Id);
		case 1:
			return ObjProductoID;
		case 2:
			return NombreProducto;
		case 3:
			return cantidadDevolver;
		case 4:
			return bonificacion;
		case 5:
			return bonificacionVen;
		case 6:
			return new Long(precio);
		case 7:
			return subtotal;
		case 8:
			return new Long(porcImpuesto);
		case 9:
			return impuesto;
		case 10:
			return total;
		case 11:
			return new Long(totalVen);
		case 12:
			return montoBonif;
		case 13:
			return montoBonifVen;
		case 14:
			return impuestoVen;
		case 15:
			return cantidadOrdenada;
		case 16:
			return cantidadBonificada;
		case 17:
			return cantidadPromocionada;
		case 18:
			return descuento;
		case 19:
			return (totalProducto);
		case 20:
			return new Boolean(gravable);
		case 21:
			return new Boolean(deleted);
		case 22:
			return objProveedorID;
		case 23:
			if (productoLotes != null && productoLotes.length > 0) {
				SoapObject _detalle = null;
				for (DevolucionProductoLote ppd : productoLotes) {
					_detalle = new SoapObject("", "DevolucionProductoLote");
					for (int i = 0; i < ppd.getPropertyCount(); i++) {
						PropertyInfo info = new PropertyInfo();
						ppd.getPropertyInfo(i, null, info);
						_detalle.addProperty(info.name, ppd.getProperty(i));
					}
				}
				return _detalle;
			}
			break;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 24;
	}

	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
		try {
			switch (_index) {

			case 0:
				_info.name = "Id";
				_info.type = java.lang.Long.class;
				break;
			case 1:
				_info.name = "ObjProductoID";
				_info.type = java.lang.Long.class;
				break;
			case 2:
				_info.name = "NombreProducto";
				_info.type = java.lang.String.class;
				break;
			case 3:
				_info.name = "CantidadDevolver";
				_info.type = java.lang.Integer.class;
				break;
			case 4:
				_info.name = "Bonificacion";
				_info.type = java.lang.Integer.class;
				break;
			case 5:
				_info.name = "BonificacionVen";
				_info.type = java.lang.Integer.class;
				break;
			case 6:
				_info.name = "Precio";
				_info.type = java.lang.Long.class;
				break;
			case 7:
				_info.name = "Subtotal";
				_info.type = java.lang.Long.class;
				break;
			case 8:
				_info.name = "PorcImpuesto";
				_info.type = java.lang.Long.class;
				break;
			case 9:
				_info.name = "Impuesto";
				_info.type = java.lang.Long.class;
				break;
			case 10:
				_info.name = "Total";
				_info.type = java.lang.Long.class;
				break;
			case 11:
				_info.name = "TotalVen";
				_info.type = java.lang.Long.class;
				break;
			case 12:
				_info.name = "MontoBonif";
				_info.type = java.lang.Long.class;
				break;
			case 13:
				_info.name = "MontoBonifVen";
				_info.type = java.lang.Long.class;
				break;
			case 14:
				_info.name = "ImpuestoVen";
				_info.type = java.lang.Long.class;
				break;
			case 15:
				_info.name = "CantidadOrdenada";
				_info.type = java.lang.Integer.class;
				break;
			case 16:
				_info.name = "CantidadBonificada";
				_info.type = java.lang.Integer.class;
				break;
			case 17:
				_info.name = "CantidadPromocionada";
				_info.type = java.lang.Integer.class;
				break;
			case 18:
				_info.name = "Descuento";
				_info.type = java.lang.Long.class;
				break;
			case 19:
				_info.name = "TotalProducto";
				_info.type = java.lang.Integer.class;
				break;
			case 20:
				_info.name = "Gravable";
				_info.type = java.lang.Boolean.class;
				break;
			case 21:
				_info.name = "Delete";
				_info.type = java.lang.Boolean.class;
				break;
			case 22:
				_info.name = "ObjProveedorID";
				_info.type = java.lang.Long.class;
				break;
			case 23:
				if (productoLotes != null && productoLotes.length > 0) {
					_info.name = "ProductoLotes";
					_info.type = DevolucionProductoLote[].class;
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void setProperty(int _index, Object _obj) {
		switch (_index) {
		case 0:
			Id = Long.parseLong(_obj.toString());
			break;
		case 1:
			ObjProductoID = Long.parseLong(_obj.toString());
			break;
		case 2:
			NombreProducto = (String) _obj;
			break;
		case 3:
			cantidadDevolver = Integer.parseInt(_obj.toString());
			break;
		case 4:
			bonificacion = Integer.parseInt(_obj.toString());
			break;
		case 5:
			bonificacionVen = Integer.parseInt(_obj.toString());
			break;
		case 6:
			precio = Long.parseLong(_obj.toString());
			break;
		case 7:
			subtotal = Long.parseLong(_obj.toString());
			break;
		case 8:
			porcImpuesto = Long.parseLong(_obj.toString());
			break;
		case 9:
			impuesto = Long.parseLong(_obj.toString());
			break;
		case 10:
			total = Long.parseLong(_obj.toString());
			break;
		case 11:
			totalVen = Long.parseLong(_obj.toString());
			break;
		case 12:
			montoBonif = Long.parseLong(_obj.toString());
			break;
		case 13:
			montoBonifVen = Long.parseLong(_obj.toString());
			break;
		case 14:
			impuestoVen = Long.parseLong(_obj.toString());
			break;
		case 15:
			cantidadOrdenada = Integer.parseInt(_obj.toString());
		case 16:
			cantidadBonificada = Integer.parseInt(_obj.toString());
		case 17:
			cantidadPromocionada = Integer.parseInt(_obj.toString());
		case 18:
			descuento = Long.parseLong(_obj.toString());
			break;
		case 19:
			totalProducto = Integer.parseInt(_obj.toString());
		case 20:
			gravable = "true".equals(_obj.toString());
			break;
		case 21:
			deleted = "true".equals(_obj.toString());
			break;
		case 22:
			objProveedorID = Long.parseLong(_obj.toString());
			break;
		case 23:
			productoLotes = (DevolucionProductoLote[]) _obj;
			break;
		}
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(this.Id);
		parcel.writeLong(this.ObjProductoID);
		parcel.writeString(this.NombreProducto);
		parcel.writeInt(this.cantidadDevolver);
		parcel.writeInt(this.bonificacion);
		parcel.writeInt(this.bonificacionVen);
		parcel.writeLong(this.precio);
		parcel.writeLong(this.subtotal);
		parcel.writeLong(this.porcImpuesto);
		parcel.writeLong(this.impuesto);
		parcel.writeLong(this.total);
		parcel.writeLong(this.totalVen);
		parcel.writeLong(this.montoBonif);
		parcel.writeLong(this.montoBonifVen);
		parcel.writeLong(this.impuestoVen);
		parcel.writeInt(this.cantidadOrdenada);
		parcel.writeInt(this.cantidadBonificada);
		parcel.writeInt(this.cantidadPromocionada);
		parcel.writeLong(this.descuento);
		parcel.writeInt(this.totalProducto);
		parcel.writeInt(this.gravable ? 1 : 0);
		parcel.writeInt(this.deleted ? 1 : 0);
		parcel.writeLong(this.objProveedorID);
		parcel.writeParcelableArray(this.productoLotes, flags);
		//parcel.writeList(Arrays.asList(this.productoLotes));
	}
	
	public DevolucionProducto(Parcel parcel) {
		this.Id = parcel.readLong();
		this.ObjProductoID = parcel.readLong();
		this.NombreProducto = parcel.readString();
		this.cantidadDevolver = parcel.readInt();
		this.bonificacion = parcel.readInt();
		this.bonificacionVen = parcel.readInt();
		this.precio = parcel.readLong();
		this.subtotal = parcel.readLong();
		this.porcImpuesto = parcel.readLong();
		this.impuesto = parcel.readLong();
		this.total = parcel.readLong();
		this.totalVen = parcel.readLong();
		this.montoBonif = parcel.readLong();
		this.montoBonifVen = parcel.readLong();
		this.impuestoVen = parcel.readLong();
		this.cantidadOrdenada = parcel.readInt();
		this.cantidadBonificada = parcel.readInt();
		this.cantidadPromocionada = parcel.readInt();
		this.descuento = parcel.readLong();
		this.totalProducto = parcel.readInt();
		this.gravable = parcel.readInt() == 1;
		this.deleted = parcel.readInt() == 1;
		this.objProveedorID = parcel.readLong();
		
		Parcelable[] parcelableArray = parcel.readParcelableArray(com.panzyma.nm.serviceproxy.DevolucionProductoLote.class.getClassLoader());
		if (parcelableArray != null) {
			productoLotes = Arrays.copyOf(parcelableArray, parcelableArray.length, com.panzyma.nm.serviceproxy.DevolucionProductoLote[].class);		   
		}
	}
	
	public static final Parcelable.Creator<com.panzyma.nm.serviceproxy.DevolucionProducto> CREATOR  = new Parcelable.Creator<com.panzyma.nm.serviceproxy.DevolucionProducto> (){
		@Override
		public com.panzyma.nm.serviceproxy.DevolucionProducto  createFromParcel(Parcel parcel) {
			return new DevolucionProducto(parcel);
		}

		@Override
		public com.panzyma.nm.serviceproxy.DevolucionProducto[] newArray(int size) {
			return new DevolucionProducto[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
