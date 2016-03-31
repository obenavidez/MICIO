package com.panzyma.nm.serviceproxy;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

import com.panzyma.nm.interfaces.Item;

public class CProducto implements Parcelable, Item {
	
//	protected com.panzyma.nm.serviceproxy.ArrayOfCNota Notas;

	protected CNota[]  Notas;
	protected long Id;
    protected java.lang.String Codigo;
	protected java.lang.String Nombre;
    protected java.lang.String Registro;
    protected java.lang.String NombreComercial;
    protected java.lang.String NombreGenerico;
    protected java.lang.String Proveedor;
    protected java.lang.String Categoria;
    protected java.lang.String FormaFarmaceutica;
    protected java.lang.String AccionFarmacologica;
    protected java.lang.String TipoProducto;
    protected java.lang.String Especialidades;
	
	 public CProducto() {
		    this.Codigo="";
		    this.Nombre="";
		    this.Registro="";
		    this.NombreComercial="";
		    this.NombreGenerico="";
		    this.Proveedor="";
		    this.Categoria="";
		    this.FormaFarmaceutica="";
		    this.AccionFarmacologica="";
		    this.TipoProducto="";
		    this.Especialidades="";
	 }
	 public CProducto(Parcel parcel) {
			readFromParcel(parcel);
	}
	private void readFromParcel(Parcel parcel) {
		Id =parcel.readLong();
	   	Codigo =parcel.readString();
		Nombre =parcel.readString();
	   	Registro =parcel.readString();
	   	NombreComercial =parcel.readString();
	   	NombreGenerico =parcel.readString();
	   	Proveedor =parcel.readString();
	   	Categoria =parcel.readString();
	   	FormaFarmaceutica =parcel.readString();
	   	AccionFarmacologica =parcel.readString();
	   	TipoProducto =parcel.readString();
	   	Especialidades =parcel.readString();
		
		Parcelable[] parcelableArray = parcel.readParcelableArray(CNota.class
				.getClassLoader());
		if (parcelableArray != null) {
			Notas = new CNota[parcelableArray.length];
			Notas = Arrays.copyOf(parcelableArray, parcelableArray.length,
					CNota[].class);
		}
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

			@Override
			public CProducto createFromParcel(Parcel parcel) {
				return new CProducto(parcel);
			}

			@Override
			public CProducto[] newArray(int size) {
				return new CProducto[size];
			}
	 };
	 
	 public long getId() {
	        return Id;
	    }
	    
	    public void setId(long id) {
	        this.Id = id;
	    }
	    
	    public java.lang.String getCodigo() {
	        return Codigo;
	    }
	    
	    public void setCodigo(java.lang.String codigo) {
	        this.Codigo = codigo;
	    }
	    
	    public java.lang.String getNombre() {
	        return Nombre;
	    }
	    
	    public void setNombre(java.lang.String nombre) {
	        this.Nombre = nombre;
	    }
	    
	    public java.lang.String getRegistro() {
	        return Registro;
	    }
	    
	    public void setRegistro(java.lang.String registro) {
	        this.Registro = registro;
	    }
	    
	    public java.lang.String getNombreComercial() {
	        return NombreComercial;
	    }
	    
	    public void setNombreComercial(java.lang.String nombreComercial) {
	        this.NombreComercial = nombreComercial;
	    }
	    
	    public java.lang.String getNombreGenerico() {
	        return NombreGenerico;
	    }
	    
	    public void setNombreGenerico(java.lang.String nombreGenerico) {
	        this.NombreGenerico = nombreGenerico;
	    }
	    
	    public java.lang.String getProveedor() {
	        return Proveedor;
	    }
	    
	    public void setProveedor(java.lang.String proveedor) {
	        this.Proveedor = proveedor;
	    }
	    
	    public java.lang.String getCategoria() {
	        return Categoria;
	    }
	    
	    public void setCategoria(java.lang.String categoria) {
	        this.Categoria = categoria;
	    }
	    
	    public java.lang.String getFormaFarmaceutica() {
	        return FormaFarmaceutica;
	    }
	    
	    public void setFormaFarmaceutica(java.lang.String formaFarmaceutica) {
	        this.FormaFarmaceutica = formaFarmaceutica;
	    }
	    
	    public java.lang.String getAccionFarmacologica() {
	        return AccionFarmacologica;
	    }
	    
	    public void setAccionFarmacologica(java.lang.String accionFarmacologica) {
	        this.AccionFarmacologica = accionFarmacologica;
	    }
	    
	    public java.lang.String getTipoProducto() {
	        return TipoProducto;
	    }
	    
	    public void setTipoProducto(java.lang.String tipoProducto) {
	        this.TipoProducto = tipoProducto;
	    }
	    
	    public java.lang.String getEspecialidades() {
	        return Especialidades;
	    }
	    
	    public void setEspecialidades(java.lang.String especialidades) {
	        this.Especialidades = especialidades;
	    }
	    public CNota[] getNotas() {
			return Notas;
		}
		public void setNotas(CNota[] notas) {
			Notas = notas;
		}


		@Override
		public Object isMatch(CharSequence constraint) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public String getItemName() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public String getItemDescription() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public String getItemCode() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public void writeToParcel(Parcel parcel, int flags) {
			parcel.writeLong(Id);
			parcel.writeString(Codigo);
			parcel.writeString(Nombre);
			parcel.writeString(Registro);
			parcel.writeString(NombreComercial);
			parcel.writeString(NombreGenerico);
			parcel.writeString(Proveedor);
			parcel.writeString(Categoria);
			parcel.writeString(FormaFarmaceutica);
			parcel.writeString(AccionFarmacologica);
			parcel.writeString(TipoProducto);
			parcel.writeString(Especialidades);
			parcel.writeParcelableArray(Notas,flags);
		}
		@Override
		public String getItemCodeStado() {
			// TODO Auto-generated method stub
			return null;
		}
		
}
