package com.panzyma.nm.viewmodel;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.interfaces.Item;

public class vmRecibo implements Item, Parcelable {

	protected int id;
	protected int numero;
	protected long fecha;
	protected float totalRecibo;
	protected java.lang.String nombreCliente;
	protected java.lang.String descEstado;
	protected java.lang.String codEstado;
	
	public java.lang.String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(java.lang.String codEstado) {
		this.codEstado = codEstado;
	}

	protected long objSucursalID;

	public vmRecibo(int id, int numero, long fecha, float totalRecibo,
			String nombreCliente, String descEstado,java.lang.String _codEstado, long objSucursalID) {
		super();
		this.id = id;
		this.numero = numero;
		this.fecha = fecha;
		this.totalRecibo = totalRecibo;
		this.nombreCliente = nombreCliente;
		this.descEstado = descEstado;
		this.codEstado=_codEstado;
		this.objSucursalID = objSucursalID;
	}
	
	public void setRecibo(int id, int numero, long fecha, float totalRecibo,
			String nombreCliente, String descEstado,java.lang.String _codEstado, long objSucursalID) {		
		this.id = id;
		this.numero = numero;
		this.fecha = fecha;
		this.totalRecibo = totalRecibo;
		this.nombreCliente = nombreCliente;
		this.descEstado = descEstado;
		this.codEstado=_codEstado;
		this.objSucursalID = objSucursalID;
	}
	
	public vmRecibo(int id){
		this.id = id;
	}

	public vmRecibo() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}

	public float getTotalRecibo() {
		return totalRecibo;
	}

	public void setTotalRecibo(float totalRecibo) {
		this.totalRecibo = totalRecibo;
	}

	public java.lang.String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(java.lang.String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public java.lang.String getDescEstado() {
		return descEstado;
	}

	public void setDescEstado(java.lang.String descEstado) {
		this.descEstado = descEstado;
	}

	@Override
	public Object isMatch(CharSequence constraint) {
		if (String.valueOf(getNumero()).toLowerCase()
				.startsWith(constraint.toString()))
			return true;
		return false;
	}

	public long getObjSucursalID() {
		return objSucursalID;
	}

	public void setObjSucursalID(long objSucursalID) {
		this.objSucursalID = objSucursalID;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);
		parcel.writeInt(numero);
		parcel.writeLong(fecha);
		parcel.writeFloat(totalRecibo);
		parcel.writeString( nombreCliente );
		parcel.writeString( descEstado );
		parcel.writeString( codEstado );
	}
	
	public vmRecibo(Parcel parcel) {
		super();
		readFromParcel(parcel);
	}
	
	public static ArrayList<vmRecibo> arrayParcelToArrayRecibo(Parcel[] parcels) {
		ArrayList<vmRecibo> recibos = new ArrayList<vmRecibo>();
		for(Parcel p : parcels) {
			vmRecibo recibe = new vmRecibo(p);
			recibos.add(recibe);
		}
		return recibos;
	}
	
	private void readFromParcel(Parcel parcel) {
		id = parcel.readInt();
		numero = parcel.readInt();
		fecha = parcel.readLong();
		totalRecibo = parcel.readFloat();
		nombreCliente = parcel.readString();
		descEstado = parcel.readString();
		codEstado = parcel.readString();
	}

	public static final Parcelable.Creator<vmRecibo> CREATOR = new Parcelable.Creator<vmRecibo>() {
		@Override
		public vmRecibo createFromParcel(Parcel in) {
			return new vmRecibo(in);
		}

		@Override
		public vmRecibo[] newArray(int size) {
			return new vmRecibo[size];
		}
	};	

	@Override
	public String getItemName() {
		return getNumero() + " - " + getNombreCliente();
	}

	@Override
	public String getItemDescription() {
		return "Fecha: " + DateUtil.idateToStr( getFecha() ) + ", Total: " + getTotalRecibo()
				+ ", Estado: " + getDescEstado();
	}

	@Override
	public String getItemCode() {
		return "REF: " + getNumero();
	}

}
