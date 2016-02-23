package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class ReciboDetNC implements KvmSerializable,Documento, Parcelable {

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

	@Override
	public float getMonto() {
		return Monto;
	}

	public void setMonto(float monto) {
		this.Monto = monto;
	}

	@Override
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
		return Monto;
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
	     @Override
		public ReciboDetNC createFromParcel(Parcel parcel) {
	          return new ReciboDetNC(parcel);
	     }	
	     @Override
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

	@Override
	public Object getProperty(int _index) {
        switch(_index)  
        {
        	case 0: return new Long(Id);
        	case 1: return new Long(ObjNotaCreditoID);
        	case 2: return new Long(ObjReciboID);
        	case 3: return (Monto);
        	case 4: return (Numero);
        	case 5: return  new Long(Fecha);
        	case 6: return new Long(FechaVence);
        }
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 7;
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
	            _info.name = "ObjNotaCreditoID";
	            _info.type = Long.class; 
	            break;
	        case 2:
	            _info.name = "ObjReciboID";
	            _info.type = Long.class; 
	            break;
	        case 3:
	            _info.name = "Monto";
	            _info.type = Float.class; 
	            break;
	        case 4:
	            _info.name = "Numero";
	            _info.type = String.class; 
	            break;
	        case 5:
	            _info.name = "Fecha";
	            _info.type = Long.class; 
	            break;
	        case 6:
	            _info.name = "FechaVence";
	            _info.type = Long.class; 
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
        	case 1: ObjNotaCreditoID = Long.parseLong(obj.toString()); 
            			break;
        	case 2: ObjReciboID = Long.parseLong(obj.toString()); 
						break;						
        	case 3: Monto = Float.parseFloat(obj.toString()); 
						break;
        	case 4: Numero = (obj.toString()); 
						break;						
        	case 5: Fecha = Long.parseLong(obj.toString()); 
						break;
        	case 6: FechaVence = Long.parseLong(obj.toString()); 
						break;
        }
		
	}

	@Override
	public float getDescuento() {		
		return 0.0F;
	}

}
