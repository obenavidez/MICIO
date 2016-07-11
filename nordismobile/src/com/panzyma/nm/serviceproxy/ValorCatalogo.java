package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class ValorCatalogo implements KvmSerializable, Parcelable {

	public long Id;
    public String Codigo;
    public String Descripcion;
    
	public ValorCatalogo() { 
	}	
	
	public ValorCatalogo(long id, String codigo, String descripcion) {
		super();
		Id = id;
		Codigo = codigo;
		Descripcion = descripcion;
	}

	public ValorCatalogo(Parcel parcel) {
		this.Id = parcel.readLong();
		this.Codigo = parcel.readString();
		this.Descripcion = parcel.readString();
	}

	public long getId(){
		return Id;
	}
	public String getCodigo(){
		return Codigo;
	}
	public String getDescripcion(){
		return Descripcion;
	}
	
	public void setId(long _id){
		this.Id=_id;
	}
	public void setCodigo(String _codigo){
		this.Codigo=_codigo;
	}
	public void setDescripcion(String _descripcion){
		this.Descripcion=_descripcion;
	}

	@Override
	public Object getProperty(int index) {
		switch(index)  
		{
			case 0: return Id;
			case 1: return Codigo;
			case 2: return Descripcion;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) 
	{  
 	   switch(_index)  
 	   { 
	           case 0:
	        	   _info.name = "Id";
	               _info.type= PropertyInfo.LONG_CLASS;
	           case 1:
	        	   _info.name = "Codigo";
	               _info.type= PropertyInfo.STRING_CLASS;
	           case 2:
	        	   _info.name = "Descripcion";
	               _info.type= PropertyInfo.STRING_CLASS;
 	   }  
	}

	@Override
	public void setProperty(int arg0, Object arg1) {		 
		
	}
	
	public static final Parcelable.Creator<ValorCatalogo> CREATOR = new Parcelable.Creator<ValorCatalogo>() {
		@Override
		public ValorCatalogo createFromParcel(
				Parcel parcel) {
			return new ValorCatalogo(parcel);
		}

		@Override
		public ValorCatalogo[] newArray(int size) {
			return new ValorCatalogo[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(this.Id);
		parcel.writeString(this.Codigo);
		parcel.writeString(this.Descripcion);		
	}
}
