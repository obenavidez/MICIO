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
		this.CantidadDevolver = cantidadDevolver;
		this.Bonificacion = bonificacion;
		this.BonificacionVen = bonificacionVen;
		this.Precio = precio;
		this.Subtotal = subtotal;
		this.PorcImpuesto = porcImpuesto;
		this.Impuesto = impuesto;
		this.Total = total;
		this.TotalVen = totalVen;
		this.MontoBonif = montoBonif;
		this.MontoBonifVen = montoBonifVen;
		this.ImpuestoVen = impuestoVen;
		this.CantidadOrdenada = cantidadOrdenada;
		this.CantidadBonificada = cantidadBonificada;
		this.CantidadPromocionada = cantidadPromocionada;
		this.Descuento = descuento;
		this.TotalProducto = totalProducto;
		this.Gravable = gravable;
		this.Deleted = deleted;
		this.ObjProveedorID = objProveedorID;
		this.ProductoLotes = productoLotes;
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
		return CantidadDevolver;
	}

	/**
	 * @param cantidadDevolver
	 *            the cantidadDevolver to set
	 */
	public void setCantidadDevolver(int cantidadDevolver) {
		this.CantidadDevolver = cantidadDevolver;
	}

	/**
	 * @return the bonificacion
	 */
	public int getBonificacion() {
		return Bonificacion;
	}

	/**
	 * @param bonificacion
	 *            the bonificacion to set
	 */
	public void setBonificacion(int bonificacion) {
		this.Bonificacion = bonificacion;
	}

	/**
	 * @return the bonificacionVen
	 */
	public int getBonificacionVen() {
		return BonificacionVen;
	}

	/**
	 * @param bonificacionVen
	 *            the bonificacionVen to set
	 */
	public void setBonificacionVen(int bonificacionVen) {
		this.BonificacionVen = bonificacionVen;
	}

	/**
	 * @return the precio
	 */
	public long getPrecio() {
		return Precio;
	}

	/**
	 * @param precio
	 *            the precio to set
	 */
	public void setPrecio(long precio) {
		this.Precio = precio;
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
	 * @return the porcImpuesto
	 */
	public long getPorcImpuesto() {
		return PorcImpuesto;
	}

	/**
	 * @param porcImpuesto
	 *            the porcImpuesto to set
	 */
	public void setPorcImpuesto(long porcImpuesto) {
		this.PorcImpuesto = porcImpuesto;
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
	 * @return the cantidadOrdenada
	 */
	public int getCantidadOrdenada() {
		return CantidadOrdenada;
	}

	/**
	 * @param cantidadOrdenada
	 *            the cantidadOrdenada to set
	 */
	public void setCantidadOrdenada(int cantidadOrdenada) {
		this.CantidadOrdenada = cantidadOrdenada;
	}

	/**
	 * @return the cantidadBonificada
	 */
	public int getCantidadBonificada() {
		return CantidadBonificada;
	}

	/**
	 * @param cantidadBonificada
	 *            the cantidadBonificada to set
	 */
	public void setCantidadBonificada(int cantidadBonificada) {
		this.CantidadBonificada = cantidadBonificada;
	}

	/**
	 * @return the cantidadPromocionada
	 */
	public int getCantidadPromocionada() {
		return CantidadPromocionada;
	}

	/**
	 * @param cantidadPromocionada
	 *            the cantidadPromocionada to set
	 */
	public void setCantidadPromocionada(int cantidadPromocionada) {
		this.CantidadPromocionada = cantidadPromocionada;
	}

	/**
	 * @return the descuento
	 */
	public long getDescuento() {
		return Descuento;
	}

	/**
	 * @param descuento
	 *            the descuento to set
	 */
	public void setDescuento(long descuento) {
		this.Descuento = descuento;
	}

	/**
	 * @return the totalProducto
	 */
	public int getTotalProducto() {
		return TotalProducto;
	}

	/**
	 * @param totalProducto
	 *            the totalProducto to set
	 */
	public void setTotalProducto(int totalProducto) {
		this.TotalProducto = totalProducto;
	}

	/**
	 * @return the gravable
	 */
	public boolean isGravable() {
		return Gravable;
	}

	/**
	 * @param gravable
	 *            the gravable to set
	 */
	public void setGravable(boolean gravable) {
		this.Gravable = gravable;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return Deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.Deleted = deleted;
	}

	/**
	 * @return the objProveedorID
	 */
	public long getObjProveedorID() {
		return ObjProveedorID;
	}

	/**
	 * @param objProveedorID
	 *            the objProveedorID to set
	 */
	public void setObjProveedorID(long objProveedorID) {
		this.ObjProveedorID = objProveedorID;
	}

	/**
	 * @return the productoLotes
	 */
	public DevolucionProductoLote[] getProductoLotes() {
		return ProductoLotes;
	}

	/**
	 * @param productoLotes
	 *            the productoLotes to set
	 */
	public void setProductoLotes(DevolucionProductoLote[] productoLotes) {
		this.ProductoLotes = productoLotes;
	}

	private long Id;
	private long ObjProductoID;
	private String NombreProducto;
	private int CantidadDevolver;
	private int Bonificacion;
	private int BonificacionVen;
	private long Precio;
	private long Subtotal;
	private long PorcImpuesto;
	private long Impuesto;
	private long Total;
	private long TotalVen;
	private long MontoBonif;
	private long MontoBonifVen;
	private long ImpuestoVen;
	private int CantidadOrdenada;
	private int CantidadBonificada;
	private int CantidadPromocionada;
	private long Descuento;
	private int TotalProducto;
	private boolean Gravable;
	private boolean Deleted;
	private long ObjProveedorID;
	private DevolucionProductoLote[] ProductoLotes;

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
			return CantidadDevolver;
		case 4:
			return Bonificacion;
		case 5:
			return BonificacionVen;
		case 6:
			return new Long(Precio);
		case 7:
			return Subtotal;
		case 8:
			return new Long(PorcImpuesto);
		case 9:
			return Impuesto;
		case 10:
			return Total;
		case 11:
			return new Long(TotalVen);
		case 12:
			return MontoBonif;
		case 13:
			return MontoBonifVen;
		case 14:
			return ImpuestoVen;
		case 15:
			return CantidadOrdenada;
		case 16:
			return CantidadBonificada;
		case 17:
			return CantidadPromocionada;
		case 18:
			return Descuento;
		case 19:
			return (TotalProducto);
		case 20:
			return new Boolean(Gravable);
		case 21:
			return new Boolean(Deleted);
		case 22:
			return ObjProveedorID;
		case 23: 
			
			 	SoapObject item = new SoapObject("","ProductoLotes");;
				for (DevolucionProductoLote ppd : ProductoLotes) 
				{ 
					SoapObject detalle = new SoapObject("","DevolucionProductoLote");
					int cont = ppd.getPropertyCount();  
					for (int i = 0; i < ppd.getPropertyCount(); i++) 
					{
						PropertyInfo info = new PropertyInfo();
						ppd.getPropertyInfo(i, null, info);
						detalle.addProperty(info.name, ppd.getProperty(i));
					} 
					if(detalle!=null)
						item.addSoapObject(detalle);
				} 
				return item;
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
				if (ProductoLotes != null && ProductoLotes.length > 0) {
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
			CantidadDevolver = Integer.parseInt(_obj.toString());
			break;
		case 4:
			Bonificacion = Integer.parseInt(_obj.toString());
			break;
		case 5:
			BonificacionVen = Integer.parseInt(_obj.toString());
			break;
		case 6:
			Precio = Long.parseLong(_obj.toString());
			break;
		case 7:
			Subtotal = Long.parseLong(_obj.toString());
			break;
		case 8:
			PorcImpuesto = Long.parseLong(_obj.toString());
			break;
		case 9:
			Impuesto = Long.parseLong(_obj.toString());
			break;
		case 10:
			Total = Long.parseLong(_obj.toString());
			break;
		case 11:
			TotalVen = Long.parseLong(_obj.toString());
			break;
		case 12:
			MontoBonif = Long.parseLong(_obj.toString());
			break;
		case 13:
			MontoBonifVen = Long.parseLong(_obj.toString());
			break;
		case 14:
			ImpuestoVen = Long.parseLong(_obj.toString());
			break;
		case 15:
			CantidadOrdenada = Integer.parseInt(_obj.toString());
		case 16:
			CantidadBonificada = Integer.parseInt(_obj.toString());
		case 17:
			CantidadPromocionada = Integer.parseInt(_obj.toString());
		case 18:
			Descuento = Long.parseLong(_obj.toString());
			break;
		case 19:
			TotalProducto = Integer.parseInt(_obj.toString());
		case 20:
			Gravable = "true".equals(_obj.toString());
			break;
		case 21:
			Deleted = "true".equals(_obj.toString());
			break;
		case 22:
			ObjProveedorID = Long.parseLong(_obj.toString());
			break;
		case 23:
			ProductoLotes = (DevolucionProductoLote[]) _obj;
			break;
		}
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(this.Id);
		parcel.writeLong(this.ObjProductoID);
		parcel.writeString(this.NombreProducto);
		parcel.writeInt(this.CantidadDevolver);
		parcel.writeInt(this.Bonificacion);
		parcel.writeInt(this.BonificacionVen);
		parcel.writeLong(this.Precio);
		parcel.writeLong(this.Subtotal);
		parcel.writeLong(this.PorcImpuesto);
		parcel.writeLong(this.Impuesto);
		parcel.writeLong(this.Total);
		parcel.writeLong(this.TotalVen);
		parcel.writeLong(this.MontoBonif);
		parcel.writeLong(this.MontoBonifVen);
		parcel.writeLong(this.ImpuestoVen);
		parcel.writeInt(this.CantidadOrdenada);
		parcel.writeInt(this.CantidadBonificada);
		parcel.writeInt(this.CantidadPromocionada);
		parcel.writeLong(this.Descuento);
		parcel.writeInt(this.TotalProducto);
		parcel.writeInt(this.Gravable ? 1 : 0);
		parcel.writeInt(this.Deleted ? 1 : 0);
		parcel.writeLong(this.ObjProveedorID);
		parcel.writeParcelableArray(this.ProductoLotes, flags);
		//parcel.writeList(Arrays.asList(this.productoLotes));
	}
	
	public DevolucionProducto(Parcel parcel) {
		this.Id = parcel.readLong();
		this.ObjProductoID = parcel.readLong();
		this.NombreProducto = parcel.readString();
		this.CantidadDevolver = parcel.readInt();
		this.Bonificacion = parcel.readInt();
		this.BonificacionVen = parcel.readInt();
		this.Precio = parcel.readLong();
		this.Subtotal = parcel.readLong();
		this.PorcImpuesto = parcel.readLong();
		this.Impuesto = parcel.readLong();
		this.Total = parcel.readLong();
		this.TotalVen = parcel.readLong();
		this.MontoBonif = parcel.readLong();
		this.MontoBonifVen = parcel.readLong();
		this.ImpuestoVen = parcel.readLong();
		this.CantidadOrdenada = parcel.readInt();
		this.CantidadBonificada = parcel.readInt();
		this.CantidadPromocionada = parcel.readInt();
		this.Descuento = parcel.readLong();
		this.TotalProducto = parcel.readInt();
		this.Gravable = parcel.readInt() == 1;
		this.Deleted = parcel.readInt() == 1;
		this.ObjProveedorID = parcel.readLong();
		
		Parcelable[] parcelableArray = parcel.readParcelableArray(com.panzyma.nm.serviceproxy.DevolucionProductoLote.class.getClassLoader());
		if (parcelableArray != null) {
			ProductoLotes = Arrays.copyOf(parcelableArray, parcelableArray.length, com.panzyma.nm.serviceproxy.DevolucionProductoLote[].class);		   
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
