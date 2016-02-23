package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.panzyma.nm.viewmodel.vmFicha;

import android.os.Parcel;
import android.os.Parcelable;

public class CCobro  implements Parcelable , KvmSerializable {
	
		private java.lang.String numeroCentral;
		private java.lang.String nombreCliente;
	    private java.lang.String fecha;
	    private float totalRecibo;
	    private java.lang.String referencia;
	
	 	public CCobro() {
	    }
	    
	    public CCobro(java.lang.String numeroCentral, java.lang.String nombreCliente, java.lang.String fecha, float totalRecibo, java.lang.String referencia) {
	        this.numeroCentral = numeroCentral;
	        this.nombreCliente = nombreCliente;
	        this.fecha = fecha;
	        this.totalRecibo = totalRecibo;
	        this.referencia = referencia;
	    }
	    
	    public java.lang.String getNumeroCentral() {
	        return numeroCentral;
	    }
	    
	    public void setNumeroCentral(java.lang.String numeroCentral) {
	        this.numeroCentral = numeroCentral;
	    }
	    
	    public java.lang.String getNombreCliente() {
	        return nombreCliente;
	    }
	    
	    public void setNombreCliente(java.lang.String nombreCliente) {
	        this.nombreCliente = nombreCliente;
	    }
	    
	    public java.lang.String getFecha() {
	        return fecha;
	    }
	    
	    public void setFecha(java.lang.String fecha) {
	        this.fecha = fecha;
	    }
	    
	    public float getTotalRecibo() {
	        return totalRecibo;
	    }
	    
	    public void setTotalRecibo(float totalRecibo) {
	        this.totalRecibo = totalRecibo;
	    }
	    
	    public java.lang.String getReferencia() {
	        return referencia;
	    }
	    
	    public void setReferencia(java.lang.String referencia) {
	        this.referencia = referencia;
	    }

		@Override
		public Object getProperty(int index) {
			switch (index) {
			case 0:
				return getNumeroCentral();
				
			case 1:
				return  getNombreCliente();
			
			case 2:
				return  getFecha();
				
			case 3:
				return  getTotalRecibo();
				
			case 4:
				return  getReferencia();
				
			}
			return null;
		}

		@Override
		public int getPropertyCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
			switch (_index) 
			{
			 case 0:
	               _info.name = "numeroCentral";
	               _info.type = java.lang.String.class; 
	               break;
			 case 1:
	               _info.name = "nombreCliente";
	               _info.type = java.lang.String.class; 
	               break;
			 case 2:
	               _info.name = "fecha";
	               _info.type = java.lang.String.class; 
	               break;
			 case 3:
	               _info.name = "totalRecibo";
	               _info.type = java.lang.Float.class; 
	               break;
			 case 4:
	               _info.name = "referencia";
	               _info.type = java.lang.String.class; 
	               break;
	               
			}
			
		}

		@Override
		public void setProperty(int _index, Object _obj) {
			switch (_index) 
			{
			 case 0:
				 numeroCentral = (String) _obj;
	              
	               break;
			 case 1:
				 nombreCliente = (String) _obj;
	               
	               break;
			 case 2:
				 fecha = (String) _obj;
	              
	               break;
			 case 3:
				 totalRecibo = Float.parseFloat(_obj.toString());
	               
	               break;
			 case 4:
				 referencia = (String) _obj;
	            
	               break;
	               
			}
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
		public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

			@Override
			public vmFicha createFromParcel(Parcel parcel) {
				return new vmFicha(parcel);
			}

			@Override
			public vmFicha[] newArray(int size) {
				return new vmFicha[size];
			}

		};
}
