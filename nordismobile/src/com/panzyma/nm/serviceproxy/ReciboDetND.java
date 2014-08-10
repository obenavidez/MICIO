package com.panzyma.nm.serviceproxy;

import android.os.Parcel;
import android.os.Parcelable;

public class ReciboDetND implements Documento, Parcelable {

	protected long Id;
	protected long ObjNotaDebitoID;
	protected long ObjReciboID;
	protected float MontoInteres;
	protected boolean EsAbono;
	protected float MontoPagar;
	protected java.lang.String Numero;
	protected long Fecha;
	protected long FechaVence;
	protected float MontoND;
	protected float SaldoND;
	protected float InteresMoratorio;
	protected float SaldoTotal;
	protected float MontoNeto;	

	public ReciboDetND() {
		super();
	}

	public ReciboDetND(long id, long objNotaDebitoID, float montoInteres,
			boolean esAbono, float montoPagar, String numero, long fecha,
			long fechaVence, float montoND, float saldoND,
			float interesMoratorio, float saldoTotal, float montoNeto) {
		super();
		this.Id = id;
		this.ObjNotaDebitoID = objNotaDebitoID;
		this.MontoInteres = montoInteres;
		this.EsAbono = esAbono;
		this.MontoPagar = montoPagar;
		this.Numero = numero;
		this.Fecha = fecha;
		this.FechaVence = fechaVence;
		this.MontoND = montoND;
		this.SaldoND = saldoND;
		this.InteresMoratorio = interesMoratorio;
		this.SaldoTotal = saldoTotal;
		this.MontoNeto = montoNeto;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public long getObjNotaDebitoID() {
		return ObjNotaDebitoID;
	}

	public void setObjNotaDebitoID(long objNotaDebitoID) {
		this.ObjNotaDebitoID = objNotaDebitoID;
	}	

	public long getObjReciboID() {
		return ObjReciboID;
	}

	public void setObjReciboID(long objReciboID) {
		this.ObjReciboID = objReciboID;
	}

	public float getMontoInteres() {
		return MontoInteres;
	}

	public void setMontoInteres(float montoInteres) {
		this.MontoInteres = montoInteres;
	}

	public boolean isEsAbono() {
		return EsAbono;
	}

	public void setEsAbono(boolean esAbono) {
		this.EsAbono = esAbono;
	}

	public float getMontoPagar() {
		return MontoPagar;
	}

	public void setMontoPagar(float montoPagar) {
		this.MontoPagar = montoPagar;
	}

	public java.lang.String getNumero() {
		return Numero;
	}

	public void setNumero(java.lang.String numero) {
		this.Numero = numero;
	}

	public long getFecha() {
		return Fecha;
	}

	public void setFecha(long fecha) {
		this.Fecha = fecha;
	}

	public long getFechaVence() {
		return FechaVence;
	}

	public void setFechaVence(long fechaVence) {
		this.FechaVence = fechaVence;
	}

	public float getMontoND() {
		return MontoND;
	}

	public void setMontoND(float montoND) {
		this.MontoND = montoND;
	}

	public float getSaldoND() {
		return SaldoND;
	}

	public void setSaldoND(float saldoND) {
		this.SaldoND = saldoND;
	}

	public float getInteresMoratorio() {
		return InteresMoratorio;
	}

	public void setInteresMoratorio(float interesMoratorio) {
		this.InteresMoratorio = interesMoratorio;
	}

	public float getSaldoTotal() {
		return SaldoTotal;
	}

	public void setSaldoTotal(float saldoTotal) {
		this.SaldoTotal = saldoTotal;
	}

	public float getMontoNeto() {
		return MontoNeto;
	}

	public void setMontoNeto(float montoNeto) {
		this.MontoNeto = montoNeto;
	}

	@Override
	public long id() {
		return getId();
	}

	@Override
	public float getMonto() {
		return getMontoPagar();
	}

	@Override
	public long getFechaDocumento() {
		return getFecha();
	}

	@Override
	public String getTipo() {
		return "Nota Débito";
	}

	@Override
	public Object getObject() {
		return this;
	}

	@Override
	public float getSaldo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRetencion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public ReciboDetND(Parcel parcel){  	   
  	   readFromParcel(parcel);
	}
  
	private void readFromParcel(Parcel parcel) {
		this.Id = parcel.readLong();
		this.ObjNotaDebitoID = parcel.readLong();
		this.ObjReciboID = parcel.readLong();
		this.MontoInteres = parcel.readFloat();
		this.EsAbono = parcel.readInt() == 1;
		this.MontoPagar = parcel.readFloat();
		this.Numero = parcel.readString();
		this.Fecha = parcel.readLong();
		this.FechaVence = parcel.readLong();
		this.MontoND = parcel.readFloat();
		this.SaldoND = parcel.readFloat();
		this.InteresMoratorio = parcel.readFloat();
		this.SaldoTotal = parcel.readFloat();
		this.MontoNeto = parcel.readFloat();		
	}

	public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() {	
	      public ReciboDetND createFromParcel(Parcel parcel) {
	           return new ReciboDetND(parcel);
	      }	
	      public ReciboDetND[] newArray(int size) {
	           return new ReciboDetND[size];
	      } 
	 };

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(  Id );
		parcel.writeLong(  ObjNotaDebitoID );
		parcel.writeLong(  ObjReciboID );
		parcel.writeFloat(  MontoInteres );
		parcel.writeInt( EsAbono ? 1 : 0 );
		parcel.writeFloat(  MontoPagar );
		parcel.writeString( Numero );
		parcel.writeLong(  Fecha );
		parcel.writeLong(  FechaVence );
		parcel.writeFloat(  MontoND );
		parcel.writeFloat(  SaldoND );
		parcel.writeFloat(  InteresMoratorio );
		parcel.writeFloat(  SaldoTotal );
		parcel.writeFloat(  MontoNeto );		
	}

}
