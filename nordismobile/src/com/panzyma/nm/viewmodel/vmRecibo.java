package com.panzyma.nm.viewmodel;

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
	protected long objSucursalID;

	public vmRecibo(int id, int numero, long fecha, float totalRecibo,
			String nombreCliente, String descEstado, long objSucursalID) {
		super();
		this.id = id;
		this.numero = numero;
		this.fecha = fecha;
		this.totalRecibo = totalRecibo;
		this.nombreCliente = nombreCliente;
		this.descEstado = descEstado;
		this.objSucursalID = objSucursalID;
	}
	
	public void setRecibo(int id, int numero, long fecha, float totalRecibo,
			String nombreCliente, String descEstado, long objSucursalID) {		
		this.id = id;
		this.numero = numero;
		this.fecha = fecha;
		this.totalRecibo = totalRecibo;
		this.nombreCliente = nombreCliente;
		this.descEstado = descEstado;
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
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

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
