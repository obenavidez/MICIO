package com.panzyma.nm.serviceproxy;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 

 
public final class DescuentoProveedor implements KvmSerializable, Parcelable{
 
	private long ObjProveedorID;
    private float PrcDescuento;

    public DescuentoProveedor(Parcel parcel){ 	   
	 	   readFromParcel(parcel);
	}
    
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(ObjProveedorID); 
	    parcel.writeFloat(PrcDescuento);
	}
    
    private void readFromParcel(Parcel parcel) {
    	ObjProveedorID = parcel.readLong();
    	PrcDescuento = parcel.readFloat();
    }
    
    public DescuentoProveedor() {
    	this.ObjProveedorID=0;
   	    this.PrcDescuento=(float) 0.0;
    } 
    public DescuentoProveedor(long objProveedorID,float prcDescuento)
    {
    	 this.ObjProveedorID=objProveedorID;
    	 this.PrcDescuento=prcDescuento;
    }
    public void setObjProveedorID(long ObjProveedorID) {
        this.ObjProveedorID = ObjProveedorID;
    }

    public long getObjProveedorID() {
        return this.ObjProveedorID;
    }

    public void setPrcDescuento(float prcDescuento) {
        this.PrcDescuento = prcDescuento;
    }

    public float getPrcDescuento( ) {
        return this.PrcDescuento;
    }

    @Override
	public int getPropertyCount() {
        return 2;
    }

    
	@SuppressLint("UseValueOf")
	@Override
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return new Long(ObjProveedorID);
        case 1: return new Float(PrcDescuento);
        }
        return null;
    }

	@Override
	public void setProperty(int _index, Object _obj) {
        switch(_index)  {
        case 0: ObjProveedorID = Long.parseLong(_obj.toString()); break;
        case 1: PrcDescuento = Float.parseFloat(_obj.toString()); break;
        }
    }

    
	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
        switch(_index)  {
        case 0:
            _info.name = "ObjProveedorID";
            _info.type = Long.class; break;
        case 1:
            _info.name = "PrcDescuento";
            _info.type = Float.class; break;
        }
    }

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() 
	{

		@Override
		public DescuentoProveedor createFromParcel(Parcel parcel) {
			return new DescuentoProveedor(parcel);
		}

		@Override
		public DescuentoProveedor[] newArray(int size) {
			return new DescuentoProveedor[size];
		}

	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
