package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;
 

public class Lote implements KvmSerializable, Parcelable 
{
	
	 public long Id;
	 public java.lang.String NumeroLote;
	 public int FechaVencimiento;
	 
	 public Lote() {
	 }
	 
	 public Lote(long id, java.lang.String numeroLote, int fechaVencimiento) {
	     this.Id = id;
	     this.NumeroLote = numeroLote;
	     this.FechaVencimiento = fechaVencimiento;
	 }
	 
	 public Lote(Parcel parcel) {
		readFromParcel(parcel);		
	}
	 
	 private void readFromParcel(Parcel parcel) {
		 Id = parcel.readLong();
		 NumeroLote =	parcel.readString();
		 FechaVencimiento =	parcel.readInt();
	 }

	public long getId() {
	     return Id;
	 }
	 
	 public void setId(long id) {
	     this.Id = id;
	 }
	 
	 public java.lang.String getNumeroLote() {
	     return NumeroLote;
	 }
	 
	 public void setNumeroLote(java.lang.String numeroLote) {
	     this.NumeroLote = numeroLote;
	 }
	 
	 public int getFechaVencimiento() {
	     return FechaVencimiento;
	 }
	 
	 public void setFechaVencimiento(int fechaVencimiento) {
	     this.FechaVencimiento = fechaVencimiento;
	 }
	
	@Override
	public Object getProperty(int _index) {
		switch(_index)  {
        case 0: return Long.valueOf(Id); 
        case 1: return NumeroLote;
        case 2: return Integer.valueOf(FechaVencimiento); 
        }
        return null;
	}
	
	@Override
	public int getPropertyCount() { 
		return 3;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) { 
		switch(_index)  
		{
	        case 0:
	            _info.name = "Id";
	            _info.type = Long.class; break;
	        case 1:
	            _info.name = "NumeroLote";
	            _info.type = java.lang.String.class; break;
	        case 2:
	            _info.name = "FechaVencimiento";
	            _info.type = Integer.class; break;
		}
	}
	
	@Override
	public void setProperty(int _index, Object _obj) 
	{ 
		switch(_index)  
		{
	        case 0:  Id=Long.parseLong(_obj.toString()); 
	        case 1:  NumeroLote=_obj.toString();
	        case 2:  FechaVencimiento=Integer.parseInt(_obj.toString()); 
        }
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(Id);
		parcel.writeString(NumeroLote);
		parcel.writeInt(FechaVencimiento);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Lote createFromParcel(Parcel parcel) {
			return new Lote(parcel);
		}

		@Override
		public Lote[] newArray(int size) {
			return new Lote[size];
		}
	};
	
}

