package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class ReciboDetND implements KvmSerializable,Documento, Parcelable {

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

	@Override
	public Object getProperty(int _index) {
        switch(_index)  
        {
        	case 0: return new Long(Id);
        	case 1: return new Long(ObjNotaDebitoID);
        	case 2: return new Long(ObjReciboID);
        	case 3: return MontoInteres;
        	case 4: return EsAbono;
        	case 5: return MontoPagar;
        	case 6: return Numero;
        	case 7: return Fecha;
        	case 8: return new Long(FechaVence);
        	case 9: return MontoND;
        	case 10: return SaldoND;
        	case 11: return InteresMoratorio;
        	case 12: return SaldoTotal;
        	case 13: return MontoNeto; 
        }
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 14;
	}

	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
		switch(_index)  
		{
	        case 0:
	            _info.name = "Id";
	            _info.type = Long.class; 
	            break;
	        case 1:
	            _info.name = "ObjNotaDebitoID";
	            _info.type = Long.class; 
	            break;
	        case 2:
	            _info.name = "ObjReciboID";
	            _info.type = Long.class; 
	            break;
	        case 3:
	            _info.name = "MontoInteres";
	            _info.type = Float.class; 
	            break;
	        case 4:
	            _info.name = "EsAbono";
	            _info.type = Boolean.class; 
	            break;
	        case 5:
	            _info.name = "MontoPagar";
	            _info.type = Float.class; 
	            break;
	        case 6:
	            _info.name = "Numero";
	            _info.type = String.class; 
	            break;
	        case 7:
	            _info.name = "Fecha";
	            _info.type = Long.class; 
	            break;
	        case 8:
	            _info.name = "FechaVence";
	            _info.type = Long.class; 
	            break;
	        case 9:
	            _info.name = "MontoND";
	            _info.type = Float.class; 
	            break;
	        case 10:
	            _info.name = "SaldoND";
	            _info.type = Float.class; 
	            break;
	        case 11:
	            _info.name = "InteresMoratorio";
	            _info.type = Float.class; 
	            break;
	        case 12:
	            _info.name = "SaldoTotal";
	            _info.type = Float.class; 
	            break;
	        case 13:
	            _info.name = "MontoNeto";
	            _info.type = Float.class; 
	            break;
		}
		
	}

	@Override
	public void setProperty(int _index, Object obj) 
	{
        switch(_index)  
        {
        	case 0: Id = Long.parseLong(obj.toString()); 
        				 break;
        	case 1: ObjNotaDebitoID = Long.parseLong(obj.toString()); 
			 			break;
        	case 2: ObjReciboID = Long.parseLong(obj.toString()); 
 						break;
        	case 3: MontoInteres = Float.valueOf(obj.toString()); 
						break;
        	case 4: EsAbono = Boolean.valueOf(obj.toString()); 
						break;
        	case 5: MontoPagar = Float.valueOf(obj.toString()); 
						break;
        	case 6: Numero = (obj.toString()); 
						break;
        	case 7: Fecha = Long.parseLong(obj.toString()); 
						break;
        	case 8: FechaVence = Long.parseLong(obj.toString()); 
						break;
        	case 9: MontoND = Float.valueOf(obj.toString()); 
						break;
        	case 10: SaldoND = Float.valueOf(obj.toString()); 
						break;
        	case 11: InteresMoratorio = Float.valueOf(obj.toString()); 
						break;
        	case 12: SaldoTotal = Float.valueOf(obj.toString()); 
						break;
        	case 13: MontoNeto = Float.valueOf(obj.toString()); 
						break;
        }
	}

	@Override
	public float getDescuento() {
		return 0.0F;
	}

}
