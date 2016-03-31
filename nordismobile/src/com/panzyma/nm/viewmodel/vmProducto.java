package com.panzyma.nm.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.panzyma.nm.interfaces.Item;

public class vmProducto implements Item, Parcelable {

	private long id;
	private java.lang.String codigo;
	private java.lang.String nombre;
	private int disponible;
	private boolean checked = false;

	public vmProducto(long id, java.lang.String codigo,
			java.lang.String nombre, int disponible) {
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.disponible = disponible;
	}

	public void setId(long idProducto) {
		this.id = idProducto;
	}

	public long getId() {
		return this.id;
	}

	public void setCodigo(java.lang.String codigo) {
		this.codigo = codigo;
	}

	public java.lang.String getCodigo() {
		return this.codigo;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getNombre() {
		return this.nombre;
	}

	public void setDisponible(java.lang.Integer disponible) {
		this.disponible = disponible;
	}

	public java.lang.Integer getDisponibilidad() {
		return this.disponible;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void toggleChecked() {
		checked = !checked;
	}

	@Override
	public Object isMatch(CharSequence constraint) {
		if (getNombre().toLowerCase().startsWith(constraint.toString()))
			return true;
		return false;
	}

	@Override
	public String getItemName() {
		return getNombre();
	}

	@Override
	public String getItemDescription() {
		return "Existencias: " + getDisponibilidad();
	}

	@Override
	public String getItemCode() {
		return getCodigo();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public String getItemCodeStado() {
		// TODO Auto-generated method stub
		return null;
	}
}
