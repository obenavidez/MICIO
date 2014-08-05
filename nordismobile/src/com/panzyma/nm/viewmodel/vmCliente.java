package com.panzyma.nm.viewmodel;

import com.panzyma.nm.interfaces.Item;

import android.os.Parcel;
import android.os.Parcelable;

public class vmCliente implements Parcelable, Item {
	public long IdCliente;
	public long IdSucursal;
	public java.lang.String NombreCliente;
	public java.lang.String Codigo;
	public java.lang.String Ubicacion;	
	
	public vmCliente() {
		super();
	}

	public vmCliente(long idCliente, long idSucursal,
			java.lang.String nombreCliente, java.lang.String codigo,
			java.lang.String ubicacion) {
		this.IdCliente = idCliente;
		this.IdSucursal = idSucursal;
		this.NombreCliente = nombreCliente;
		this.Codigo = codigo;
		this.Ubicacion = ubicacion;
	}

	public void setIdCliente(long idCliente) {
		this.IdCliente = idCliente;
	}

	public long getIdCliente() {
		return this.IdCliente;
	}

	public void setNombreCliente(java.lang.String nombreCliente) {
		this.NombreCliente = nombreCliente;
	}

	public java.lang.String getNombreCliente() {
		return this.NombreCliente;
	}

	public void setIdSucursal(long idSucursal) {
		this.IdSucursal = idSucursal;
	}

	public long getIdSucursal() {
		return this.IdSucursal;
	}

	public void setCodigo(java.lang.String codigo) {
		this.Codigo = codigo;
	}

	public java.lang.String getCodigo() {
		return this.Codigo;
	}

	public void setUbicacion(java.lang.String ubicacion) {
		this.Ubicacion = ubicacion;
	}

	public java.lang.String getUbicacion() {
		return this.Ubicacion;
	}

	@Override
	public Object isMatch(CharSequence constraint) {
		if (getNombreCliente().toLowerCase().contains(constraint.toString()))
			return true;
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeLong(IdCliente);
		parcel.writeLong(IdSucursal);
		parcel.writeString(NombreCliente);
		parcel.writeString(Codigo);
		parcel.writeString(Ubicacion);
	}

	private void readFromParcel(Parcel parcel) {
		this.IdCliente = parcel.readLong();
		this.IdSucursal = parcel.readLong();
		this.NombreCliente = parcel.readString();
		this.Codigo = parcel.readString();
		this.Ubicacion = parcel.readString();
	}

	public vmCliente(Parcel parcel) {
		super();
		readFromParcel(parcel);
	}

	public static final Parcelable.Creator<vmCliente> CREATOR = new Parcelable.Creator<vmCliente>() {
		@Override
		public vmCliente createFromParcel(Parcel in) {
			return new vmCliente(in);
		}

		@Override
		public vmCliente[] newArray(int size) {
			return new vmCliente[size];
		}
	};	

	@Override
	public String getItemName() {		
		return NombreCliente;
	}

	@Override
	public String getItemDescription() {
		// TODO Auto-generated method stub
		return Ubicacion;
	}

	@Override
	public String getItemCode() {
		// TODO Auto-generated method stub
		return Codigo;
	}

}
