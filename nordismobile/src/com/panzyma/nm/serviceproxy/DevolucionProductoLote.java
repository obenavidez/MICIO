package com.panzyma.nm.serviceproxy;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class DevolucionProductoLote implements Parcelable, KvmSerializable, Serializable
{
	public DevolucionProductoLote(){}
	
	public DevolucionProductoLote(Parcel parcel){
		id = parcel.readLong( );
		objLoteID = parcel.readLong(   );
		numeroLote = parcel.readString(  );
		fechaVencimiento = parcel.readInt(   );
		cantidadDevuelta = parcel.readInt(  );
		fueraPolitica = (parcel.readInt() == 1) ;
		cantidadDespachada = parcel.readInt(  );
		deleted = (parcel.readInt() == 1);
	}
	
	public DevolucionProductoLote(DevolucionProductoLote lote)
	{		
		this.id = lote.getId();
		this.objLoteID = lote.getObjLoteID();
		this.numeroLote = lote.getNumeroLote();
		this.fechaVencimiento = lote.getFechaVencimiento();
		this.cantidadDevuelta = lote.getCantidadDevuelta();
		this.fueraPolitica = lote.isFueraPolitica();
		this.cantidadDespachada =lote.getCantidadDespachada();
		this.deleted = lote.isDeleted();
	}
	
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
	public long getFechaVencimiento() {
		return fechaVencimiento;
	}
	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(long fechaVencimiento) {
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
	private long fechaVencimiento;
	private int cantidadDevuelta;
	private boolean fueraPolitica;
	private int cantidadDespachada;
	private boolean deleted;
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Parcelable.Creator<DevolucionProductoLote> CREATOR  = new Parcelable.Creator<DevolucionProductoLote>() {	
	     @Override
		public DevolucionProductoLote createFromParcel(Parcel parcel) {
	          return new DevolucionProductoLote(parcel);
	     }	
	     @Override
		public DevolucionProductoLote[] newArray(int size) {
	          return new DevolucionProductoLote[size];
	     }
	};

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(  id );
		parcel.writeLong(  objLoteID );
		parcel.writeString( numeroLote );
		parcel.writeLong(  fechaVencimiento );
		parcel.writeInt( cantidadDevuelta );
		parcel.writeInt( fueraPolitica ? 1 : 0 );
		parcel.writeInt( cantidadDespachada );
		parcel.writeInt(  deleted ? 1 : 0 );		
	}

	@Override
	public Object getProperty(int index) 
	{	
		 switch(index)  
		 {
			 case 0: return  id;
			 case 1: return  objLoteID;
			 case 2: return  numeroLote;
			 case 3: return  fechaVencimiento;
			 case 4: return  cantidadDevuelta;
			 case 5: return  fueraPolitica;
			 case 6: return  cantidadDespachada;
			 case 7: return  deleted;
		 } 	
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "Id";
            _info.type = Long.class; break;
        case 1:
            _info.name = "ObjLoteID";
            _info.type = Long.class; break; 
        case 2:
            _info.name = "NumeroLote";
            _info.type = String.class; break;
        case 3:
            _info.name = "FechaVencimiento";
            _info.type = Long.class; break;
        case 4:
            _info.name = "CantidadDevuelta";
            _info.type = Integer.class; break;
        case 5:
            _info.name = "FueraPolitica";
            _info.type = Boolean.class; break;
        case 6:
            _info.name = "CantidadDespachada";
            _info.type = Integer.class; break;
        case 7:
            _info.name = "Deleted";
            _info.type = Boolean.class; break;
        } 
	}

	@Override
	public void setProperty(int _index, Object _obj) 
	{		 
        switch(_index)  
        {
	         case 0:id= Long.parseLong(_obj.toString()); break;
			 case 1:objLoteID=Long.parseLong(_obj.toString()); break;
			 case 2: numeroLote=(java.lang.String) _obj; break;
			 case 3: fechaVencimiento=Long.parseLong(_obj.toString()); break;
			 case 4:cantidadDevuelta=Integer.parseInt(_obj.toString()); break;
			 case 5: fueraPolitica= "true".equals(_obj.toString()); break;
			 case 6: cantidadDespachada=Integer.parseInt(_obj.toString()); break;
			 case 7: deleted="true".equals(_obj.toString()); break;
        }
		
	}
}
