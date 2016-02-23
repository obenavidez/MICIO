package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class CNota implements KvmSerializable, Parcelable 
{ 
	public java.lang.String Fecha;
    public java.lang.String ElaboradaPor;
    public java.lang.String TextoNota;
    public java.lang.String Concepto;
    public java.lang.Long ProductoID;
    
    public CNota()
	{		
		Fecha = "";
		ElaboradaPor ="";
		TextoNota ="";
		Concepto = "";
		ProductoID=(long) 0;
		
	} 
    
    public CNota(java.lang.String fecha, java.lang.String elaboradaPor, java.lang.String textoNota, java.lang.String concepto,java.lang.Long productoId) {
        this.Fecha = fecha;
        this.ElaboradaPor = elaboradaPor;
        this.TextoNota = textoNota;
        this.Concepto = concepto;
        this.ProductoID = productoId;
    }
    
	public CNota(Parcel parcel) {
		readFromParcel(parcel);
	}

	private void readFromParcel(Parcel parcel) {
		Fecha = parcel.readString();
	    ElaboradaPor = parcel.readString();
	    TextoNota = parcel.readString();
	    Concepto = parcel.readString();	
	    ProductoID  = parcel.readLong();
	}

	@Override
	public Object getProperty(int arg0) {

		switch(arg0)
        {
        case 0:
            return Fecha;
        case 1:
            return ElaboradaPor;
        case 2:
            return TextoNota;
        case 3:
            return Concepto;
        case 4:
        	return ProductoID;
        }
		
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 3;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int ind, Hashtable ht, PropertyInfo info) {
		switch(ind)
        {
	        case 0:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "Fecha";
	            break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "ElaboradaPor";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "TextoNota";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "Concepto";
	            break;
	        case 4:
	            info.type = PropertyInfo.LONG_CLASS;
	            info.type = "ProductoID";
	            break;
	        default:break;
        }
	}
	
	@Override
	public void setProperty(int ind, Object val) {
		switch(ind)
        {
	        case 0:
	        	Fecha = String.valueOf(val.toString());
	            break;
	        case 1:
	        	ElaboradaPor = String.valueOf(val.toString());
	            break;
	        case 2:
	        	TextoNota = String.valueOf(val.toString());
	            break;
	        case 3:
	        	Concepto = String.valueOf(val.toString());
	            break;
	        case 4:
	        	ProductoID = Long.valueOf(val.toString());
	            break;
	        default:
	            break;
        }
	}

    public java.lang.String getFecha() {
        return Fecha;
    }
    
    public void setFecha(java.lang.String fecha) {
        this.Fecha = fecha;
    }
    
    public java.lang.String getElaboradaPor() {
        return ElaboradaPor;
    }
    
    public java.lang.Long getProductoID() {
		return ProductoID;
	}

	public void setProductoID(java.lang.Long productoID) {
		ProductoID = productoID;
	}

	public void setElaboradaPor(java.lang.String elaboradaPor) {
        this.ElaboradaPor = elaboradaPor;
    }
    
    public java.lang.String getTextoNota() {
        return TextoNota;
    }
    
    public void setTextoNota(java.lang.String textoNota) {
        this.TextoNota = textoNota;
    }
    
    public java.lang.String getConcepto() {
        return Concepto;
    }
    
    public void setConcepto(java.lang.String concepto) {
        this.Concepto = concepto;
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		 parcel.writeString(Fecha);
	     parcel.writeString(ElaboradaPor);
	     parcel.writeString(TextoNota);
	     parcel.writeString(Concepto);	
	     parcel.writeLong(ProductoID);	
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public CNota createFromParcel(Parcel parcel) {
			return new CNota(parcel);
		}

		@Override
		public CNota[] newArray(int size) {
			return new CNota[size];
		}

	};
	
	

}
