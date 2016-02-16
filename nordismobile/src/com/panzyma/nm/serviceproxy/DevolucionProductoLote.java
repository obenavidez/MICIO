package com.panzyma.nm.serviceproxy;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class DevolucionProductoLote implements Parcelable, KvmSerializable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 778881152231333633L;

	public DevolucionProductoLote(){}
	
	public DevolucionProductoLote(Parcel parcel){
		Id = parcel.readLong( );
		ObjLoteID = parcel.readLong(   );
		NumeroLote = parcel.readString(  );
		FechaVencimiento = parcel.readInt(   );
		CantidadDevuelta = parcel.readInt(  );
		FueraPolitica = (parcel.readInt() == 1) ;
		CantidadDespachada = parcel.readInt(  );
		Deleted = (parcel.readInt() == 1);
	}
	
	public DevolucionProductoLote(DevolucionProductoLote lote)
	{		
		this.Id = lote.getId();
		this.ObjLoteID = lote.getObjLoteID();
		this.NumeroLote = lote.getNumeroLote();
		this.FechaVencimiento = lote.getFechaVencimiento();
		this.CantidadDevuelta = lote.getCantidadDevuelta();
		this.FueraPolitica = lote.isFueraPolitica();
		this.CantidadDespachada =lote.getCantidadDespachada();
		this.Deleted = lote.isDeleted();
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
	 * @return the objLoteID
	 */
	public long getObjLoteID() {
		return ObjLoteID;
	}
	/**
	 * @param objLoteID the objLoteID to set
	 */
	public void setObjLoteID(long objLoteID) {
		this.ObjLoteID = objLoteID;
	}
	/**
	 * @return the numeroLote
	 */
	public String getNumeroLote() {
		return NumeroLote;
	}
	/**
	 * @param numeroLote the numeroLote to set
	 */
	public void setNumeroLote(String numeroLote) {
		this.NumeroLote = numeroLote;
	}
	/**
	 * @return the fechaVencimiento
	 */
	public long getFechaVencimiento() {
		return FechaVencimiento;
	}
	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(long fechaVencimiento) {
		this.FechaVencimiento = fechaVencimiento;
	}
	/**
	 * @return the cantidadDevuelta
	 */
	public int getCantidadDevuelta() {
		return CantidadDevuelta;
	}
	/**
	 * @param cantidadDevuelta the cantidadDevuelta to set
	 */
	public void setCantidadDevuelta(int cantidadDevuelta) {
		this.CantidadDevuelta = cantidadDevuelta;
	}
	/**
	 * @return the fueraPolitica
	 */
	public boolean isFueraPolitica() {
		return FueraPolitica;
	}
	/**
	 * @param fueraPolitica the fueraPolitica to set
	 */
	public void setFueraPolitica(boolean fueraPolitica) {
		this.FueraPolitica = fueraPolitica;
	}
	/**
	 * @return the cantidadDespachada
	 */
	public int getCantidadDespachada() {
		return CantidadDespachada;
	}
	/**
	 * @param cantidadDespachada the cantidadDespachada to set
	 */
	public void setCantidadDespachada(int cantidadDespachada) {
		this.CantidadDespachada = cantidadDespachada;
	}
	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return Deleted;
	}
	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.Deleted = deleted;
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
		this.Id = id;
		this.ObjLoteID = objLoteID;
		this.NumeroLote = numeroLote;
		this.FechaVencimiento = fechaVencimiento;
		this.CantidadDevuelta = cantidadDevuelta;
		this.FueraPolitica = fueraPolitica;
		this.CantidadDespachada = cantidadDespachada;
		this.Deleted = deleted;
	}
	private long Id;
	private long ObjLoteID;
	private String NumeroLote;
	private long FechaVencimiento;
	private int CantidadDevuelta;
	private boolean FueraPolitica;
	private int CantidadDespachada;
	private boolean Deleted;
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() {	
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
		parcel.writeLong(  Id );
		parcel.writeLong(  ObjLoteID );
		parcel.writeString( NumeroLote );
		parcel.writeLong(  FechaVencimiento );
		parcel.writeInt( CantidadDevuelta );
		parcel.writeInt( FueraPolitica ? 1 : 0 );
		parcel.writeInt( CantidadDespachada );
		parcel.writeInt(  Deleted ? 1 : 0 );		
	}

	@Override
	public Object getProperty(int index) 
	{	
		 switch(index)  
		 {
			 case 0: return  Id;
			 case 1: return  ObjLoteID;
			 case 2: return  NumeroLote;
			 case 3: return  FechaVencimiento;
			 case 4: return  CantidadDevuelta;
			 case 5: return  FueraPolitica;
			 case 6: return  CantidadDespachada;
			 case 7: return  Deleted;
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
	         case 0:Id= Long.parseLong(_obj.toString()); break;
			 case 1:ObjLoteID=Long.parseLong(_obj.toString()); break;
			 case 2: NumeroLote=(java.lang.String) _obj; break;
			 case 3: FechaVencimiento=Long.parseLong(_obj.toString()); break;
			 case 4:CantidadDevuelta=Integer.parseInt(_obj.toString()); break;
			 case 5: FueraPolitica= "true".equals(_obj.toString()); break;
			 case 6: CantidadDespachada=Integer.parseInt(_obj.toString()); break;
			 case 7: Deleted="true".equals(_obj.toString()); break;
        }
		
	}
}
