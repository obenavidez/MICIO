package com.panzyma.nm.serviceproxy;

import android.os.Parcel;
import android.os.Parcelable;

public class ReciboDetNC implements Documento, Parcelable {

	protected long Id;
	protected long ObjNotaCreditoID;
	protected long ObjReciboID;
	protected float Monto;
	protected java.lang.String Numero;
	protected long Fecha;
	protected long FechaVence;	

	public ReciboDetNC() {
		super();
	}	

	public ReciboDetNC(long id, long objNotaCreditoID, float monto,
			String numero, long fecha, long fechaVence) {
		super();
		this.Id = id;
		this.ObjNotaCreditoID = objNotaCreditoID;
		this.Monto = monto;
		this.Numero = numero;
		this.Fecha = fecha;
		this.FechaVence = fechaVence;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public long getObjReciboID() {
		return ObjReciboID;
	}

	public void setObjReciboID(long objReciboID) {
		this.ObjReciboID = objReciboID;
	}

	public long getObjNotaCreditoID() {
		return ObjNotaCreditoID;
	}

	public void setObjNotaCreditoID(long objNotaCreditoID) {
		this.ObjNotaCreditoID = objNotaCreditoID;
	}

	public float getMonto() {
		return Monto;
	}

	public void setMonto(float monto) {
		this.Monto = monto;
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

	@Override
	public long id() {		
		return getId();
	}

	@Override
	public long getFechaDocumento() {		
		return getFecha();
	}

	@Override
	public String getTipo() {		
		return "Nota Crédito";
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
	
	public ReciboDetNC(Parcel parcel){ 	   
 	   readFromParcel(parcel);
	 }
	 
	 private void readFromParcel(Parcel parcel) {
		this.Id = parcel.readLong();
		this.ObjNotaCreditoID = parcel.readLong();
		this.ObjReciboID = parcel.readLong();
		this.Monto = parcel.readFloat();
		this.Numero = parcel.readString();
		this.Fecha = parcel.readLong();
		this.FechaVence = parcel.readLong();	
	}

	public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() {	
	     public ReciboDetNC createFromParcel(Parcel parcel) {
	          return new ReciboDetNC(parcel);
	     }	
	     public ReciboDetNC[] newArray(int size) {
	          return new ReciboDetNC[size];
	     }
	};
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(  Id );
		parcel.writeLong(  ObjNotaCreditoID );
		parcel.writeLong(  ObjReciboID );
		parcel.writeFloat( Monto );
		parcel.writeString( Numero );
		parcel.writeLong(  Fecha );
		parcel.writeLong(  FechaVence );		
	}

}
