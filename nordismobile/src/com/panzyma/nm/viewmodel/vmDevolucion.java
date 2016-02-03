package com.panzyma.nm.viewmodel;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.interfaces.DevolucionItem; 

public class vmDevolucion  implements Parcelable , DevolucionItem  {

	private long id;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	private int NumeroCentral;
	private String fecha;
	private String cliente;
	private Float  total;
	private String Estado;
	private long cliente_id;
	private boolean OffLine;
//	private int customer_id;
	private long idSucursal;

	
	
	public vmDevolucion(){}
		
	public long getCliente_id() {
		return cliente_id;
	}

	public void setCliente_id(long cliente_id) {
		this.cliente_id = cliente_id;
	}

//	public int getCustomer_id() {
//		return customer_id;
//	}
//
//	public void setCustomer_id(int customer_id) {
//		this.customer_id = customer_id;
//	}

	public vmDevolucion(long id, int numeroCentral,  String fecha, String cliente,
			Float total, String estado, long cliente_id,boolean offline , long idSucursal) {
		super();
		this.id = id;
		NumeroCentral = numeroCentral;
		this.fecha = fecha;
		this.cliente = cliente;
		this.total = total;
		Estado = estado;
		this.cliente_id = cliente_id;
		this.OffLine = offline;
		this.idSucursal = idSucursal;
//		this.customer_id=1002548;
	}


	public boolean isOffLine() {
		return OffLine;
	}

	public void setOffLine(boolean offLine) {
		OffLine = offLine;
	}

	public int getNumeroCentral() {
		return NumeroCentral;
	}




	public void setNumeroCentral(int numeroCentral) {
		NumeroCentral = numeroCentral;
	}




	public String getFecha() {
		return this.fecha;
	}




	public void setFecha(String fecha) {
		this.fecha = fecha;
	}




	public String getCliente() {
		return cliente;
	}




	public void setCliente(String cliente) {
		this.cliente = cliente;
	}




	public Float getTotal() {
		return total;
	}




	public void setTotal(Float total) {
		this.total = total;
	}




	public String getEstado() {
		return Estado;
	}




	public void setEstado(String estado) {
		Estado = estado;
	}




	

	/*
	 * (non-Javadoc)
	 * @Override
	 */

	
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Object isMatch(CharSequence constraint) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getItemNumero() {
		// TODO Auto-generated method stub
		return String.valueOf(getNumeroCentral());
	}


	@Override
	public String getItemfecha() {
		//return String.valueOf(getFecha());
		//DateFormat df =  DateFormat.getDateInstance(DateFormat.MEDIUM); 
		return getFecha();
	}


	@Override
	public String getItemCliente() {
		// TODO Auto-generated method stub
		return getCliente();
	}


	@Override
	public String getItemTotal() {
		// TODO Auto-generated method stub
		return StringUtil.formatReal(getTotal());
	}
	
	
	public String getItemTotalformato() {
		// TODO Auto-generated method stub
		return StringUtil.formatReal(getTotal());
	}

	@Override
	public String getItemEstado() {
		// TODO Auto-generated method stub
		return getEstado();
	}

	@Override
	public boolean getItemOffline() {
		
		return this.OffLine;
	}

//	@Override
//	public long getItemCustomerid() {
//		return this.customer_id;
//	}

	@Override
	public long getItemsucursalid() {
		return getIdSucursal();
	}

	public long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
	}

}
