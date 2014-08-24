package com.panzyma.nm.serviceproxy;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public final class MontoProveedor implements KvmSerializable, Parcelable {

	private long ObjProveedorID;
	private float Monto;
	private java.lang.String CodProveedor;

	public MontoProveedor() {

	}

	public MontoProveedor(Parcel parcel) {
		readFromParcel(parcel);
	}

	private void readFromParcel(Parcel parcel) {
		ObjProveedorID = parcel.readLong();
		Monto = parcel.readFloat();
		CodProveedor = parcel.readString();
	}

	public void setObjProveedorID(long objProveedorID) {
		this.ObjProveedorID = objProveedorID;
	}

	public long getObjProveedorID() {
		return this.ObjProveedorID;
	}

	public void setMonto(float monto) {
		this.Monto = monto;
	}

	public float getMonto() {
		return this.Monto;
	}

	public void setCodProveedor(java.lang.String codProveedor) {
		this.CodProveedor = codProveedor;
	}

	public java.lang.String getCodProveedor() {
		return this.CodProveedor;
	}

	@Override
	public int getPropertyCount() {
		return 3;
	}

	@SuppressLint("UseValueOf")
	@Override
	public Object getProperty(int _index) {
		switch (_index) {
		case 0:
			return new Long(ObjProveedorID);
		case 1:
			return new Float(Monto);
		case 2:
			return CodProveedor;
		}
		return null;
	}

	@Override
	public void setProperty(int _index, Object _obj) {
		switch (_index) {
		case 0:
			ObjProveedorID = Long.parseLong(_obj.toString());
			break;
		case 1:
			Monto = Float.parseFloat(_obj.toString());
			break;
		case 2:
			CodProveedor = (java.lang.String) _obj;
			break;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
		switch (_index) {
		case 0:
			_info.name = "ObjProveedorID";
			_info.type = Long.class;
			break;
		case 1:
			_info.name = "Monto";
			_info.type = Float.class;
			break;
		case 2:
			_info.name = "CodProveedor";
			_info.type = java.lang.String.class;
			break;
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public MontoProveedor createFromParcel(Parcel parcel) {
			return new MontoProveedor(parcel);
		}

		public MontoProveedor[] newArray(int size) {
			return new MontoProveedor[size];
		}

	};

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(ObjProveedorID);
		parcel.writeFloat(Monto);
		parcel.writeString(CodProveedor);

	}

}
