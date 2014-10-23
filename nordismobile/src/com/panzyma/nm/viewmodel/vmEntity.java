package com.panzyma.nm.viewmodel;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.panzyma.nm.interfaces.Item;

public class vmEntity implements Item, Parcelable, Serializable {

	public vmEntity() {
	}

	public vmEntity(long id, int numero, long fecha, float total,
			String nombre, String descEstado, String codEstado) {
		super();
		this.id = id;
		this.numero = numero;
		this.fecha = fecha;
		this.total = total;
		this.nombre = nombre;
		this.descEstado = descEstado;
	}

	public vmEntity(Parcel parcel) {
		super();
		readFromParcel(parcel);
	}

	private void readFromParcel(Parcel parcel) {
		id = parcel.readLong();
		numero = parcel.readInt();
		fecha = parcel.readLong();
		total = parcel.readFloat();
		nombre = parcel.readString();
		codEstado = parcel.readString();
		descEstado = parcel.readString();
	}

	protected long id;
	protected int numero;
	protected long fecha;
	protected float total;
	protected java.lang.String nombre;
	protected java.lang.String codEstado;
	protected java.lang.String descEstado;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getDescEstado() {
		return descEstado;
	}

	public void setDescEstado(java.lang.String descEstado) {
		this.descEstado = descEstado;
	}

	public java.lang.String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(java.lang.String codEstado) {
		this.codEstado = codEstado;
	}

	@Override
	public Object isMatch(CharSequence constraint) {
		if (String.valueOf(getNumero()).toLowerCase()
				.contains(constraint.toString()))
			return true;
		else if (String.valueOf(getNombre()).toLowerCase()
				.contains(constraint.toString()))
			return true;
		else if (String.valueOf(getId()).toLowerCase()
				.contains(constraint.toString()))
			return true;
		return false;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<vmEntity> CREATOR = new Parcelable.Creator<vmEntity>() {
		@Override
		public vmEntity createFromParcel(Parcel in) {
			return new vmEntity(in);
		}

		@Override
		public vmEntity[] newArray(int size) {
			return new vmEntity[size];
		}
	};

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(id);
		parcel.writeInt(numero);
		parcel.writeLong(fecha);
		parcel.writeFloat(total);
		parcel.writeString(nombre);
		parcel.writeString(codEstado);
		parcel.writeString(descEstado);

	}

	@Override
	public String getItemName() {
		return getNumero() + " - " + getNombre();
	}

	@Override
	public String getItemDescription() {
		return "Fecha: " + getFecha() + ", Total: " + getTotal() + ", Estado: "
				+ getDescEstado();
	}

	@Override
	public String getItemCode() {
		return "REF: " + getNumero();
	}

}
