package com.panzyma.nm.viewmodel;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.panzyma.nm.auxiliar.DateUtil.*;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.interfaces.DevolucionItem;
import com.panzyma.nordismobile.R.color; 

public class vmDevolucion  implements Parcelable , DevolucionItem  {

	private long id;
	private int NumeroCentral;
	private Date fecha;
	private String cliente;
	private Float  total;
	private String Estado;
	private long cliente_id;
	private boolean OffLine;
	
	
	public vmDevolucion(){}
		
	public vmDevolucion(long id, int numeroCentral,  Date fecha, String cliente,
			Float total, String estado, long cliente_id,boolean offline) {
		super();
		this.id = id;
		NumeroCentral = numeroCentral;
		this.fecha = fecha;
		this.cliente = cliente;
		this.total = total;
		Estado = estado;
		this.cliente_id = cliente_id;
		this.OffLine = offline;
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




	public Date getFecha() {
		return this.fecha;
	}




	public void setFecha(Date fecha) {
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		return sdf.format(getFecha());
	}


	@Override
	public String getItemCliente() {
		// TODO Auto-generated method stub
		return getCliente();
	}


	@Override
	public String getItemTotal() {
		// TODO Auto-generated method stub
		return "C$".concat(String.valueOf(getTotal()));
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

}
