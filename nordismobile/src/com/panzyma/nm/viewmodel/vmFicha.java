package com.panzyma.nm.viewmodel;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

import com.panzyma.nm.interfaces.Item;
import com.panzyma.nm.serviceproxy.CNota;

public class vmFicha implements Parcelable, Item {

	public vmFicha(CNota[] notas, long idCliente, long idSucursal,
			String nombreCliente, String nombreSucursal,
			boolean creditoCentralizado, float limiteCredito,
			float saldoActual, float disponible, int plazoCredito,
			String precioVenta, String descuentos, String direccionSucursal,
			String tipo, String categoria, String telefono, int plazoDescuento,
			float montoMinimoAbono) {
		super();
		Notas = notas;
		IdCliente = idCliente;
		IdSucursal = idSucursal;
		NombreCliente = nombreCliente;
		NombreSucursal = nombreSucursal;
		CreditoCentralizado = creditoCentralizado;
		LimiteCredito = limiteCredito;
		SaldoActual = saldoActual;
		Disponible = disponible;
		PlazoCredito = plazoCredito;
		PrecioVenta = precioVenta;
		Descuentos = descuentos;
		DireccionSucursal = direccionSucursal;
		Tipo = tipo;
		Categoria = categoria;
		Telefono = telefono;
		PlazoDescuento = plazoDescuento;
		MontoMinimoAbono = montoMinimoAbono;
	}

	public CNota[] Notas;
	public long IdCliente;
	public long IdSucursal;
	public String NombreCliente;
	public String NombreSucursal;
	public boolean CreditoCentralizado;
	public float LimiteCredito;
	public float SaldoActual;
	public float Disponible;
	public int PlazoCredito;
	public String PrecioVenta;
	public String Descuentos;
	public String DireccionSucursal;
	public String Tipo;
	public String Categoria;
	public String Telefono;
	public int PlazoDescuento;
	public float MontoMinimoAbono;

	public vmFicha() {

	}

	public vmFicha(Parcel parcel) {
		readFromParcel(parcel);
	}

	private void readFromParcel(Parcel parcel) {
		IdCliente = parcel.readLong();
		IdSucursal = parcel.readLong();
		NombreCliente = parcel.readString();
		NombreSucursal = parcel.readString();
		CreditoCentralizado = parcel.readInt() == 1;
		LimiteCredito = parcel.readFloat();
		SaldoActual = parcel.readFloat();
		Disponible = parcel.readFloat();
		PlazoCredito = parcel.readInt();
		PrecioVenta = parcel.readString();
		Descuentos = parcel.readString();
		DireccionSucursal = parcel.readString();
		Tipo = parcel.readString();
		Categoria = parcel.readString();
		Telefono = parcel.readString();
		PlazoDescuento = parcel.readInt();
		MontoMinimoAbono = parcel.readFloat();

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
		public vmFicha createFromParcel(Parcel parcel) {
			return new vmFicha(parcel);
		}

		@Override
		public vmFicha[] newArray(int size) {
			return new vmFicha[size];
		}

	};

	public CNota[] getNotas() {
		return Notas;
	}

	public void setNotas(CNota[] notas) {
		this.Notas = notas;
	}

	public long getIdCliente() {
		return IdCliente;
	}

	public void setIdCliente(long idCliente) {
		this.IdCliente = idCliente;
	}

	public long getIdSucursal() {
		return IdSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.IdSucursal = idSucursal;
	}

	public java.lang.String getNombreCliente() {
		return NombreCliente;
	}

	public void setNombreCliente(java.lang.String nombreCliente) {
		this.NombreCliente = nombreCliente;
	}

	public java.lang.String getNombreSucursal() {
		return NombreSucursal;
	}

	public void setNombreSucursal(java.lang.String nombreSucursal) {
		this.NombreSucursal = nombreSucursal;
	}

	public boolean isCreditoCentralizado() {
		return CreditoCentralizado;
	}

	public void setCreditoCentralizado(boolean creditoCentralizado) {
		this.CreditoCentralizado = creditoCentralizado;
	}

	public float getLimiteCredito() {
		return LimiteCredito;
	}

	public void setLimiteCredito(float limiteCredito) {
		this.LimiteCredito = limiteCredito;
	}

	public float getSaldoActual() {
		return SaldoActual;
	}

	public void setSaldoActual(float saldoActual) {
		this.SaldoActual = saldoActual;
	}

	public float getDisponible() {
		return Disponible;
	}

	public void setDisponible(float disponible) {
		this.Disponible = disponible;
	}

	public int getPlazoCredito() {
		return PlazoCredito;
	}

	public void setPlazoCredito(int plazoCredito) {
		this.PlazoCredito = plazoCredito;
	}

	public java.lang.String getPrecioVenta() {
		return PrecioVenta;
	}

	public void setPrecioVenta(java.lang.String precioVenta) {
		this.PrecioVenta = precioVenta;
	}

	public java.lang.String getDescuentos() {
		return Descuentos;
	}

	public void setDescuentos(java.lang.String descuentos) {
		this.Descuentos = descuentos;
	}

	public java.lang.String getDireccionSucursal() {
		return DireccionSucursal;
	}

	public void setDireccionSucursal(java.lang.String direccionSucursal) {
		this.DireccionSucursal = direccionSucursal;
	}

	public java.lang.String getTipo() {
		return Tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.Tipo = tipo;
	}

	public java.lang.String getCategoria() {
		return Categoria;
	}

	public void setCategoria(java.lang.String categoria) {
		this.Categoria = categoria;
	}

	public java.lang.String getTelefono() {
		return Telefono;
	}

	public void setTelefono(java.lang.String telefono) {
		this.Telefono = telefono;
	}

	public int getPlazoDescuento() {
		return PlazoDescuento;
	}

	public void setPlazoDescuento(int plazoDescuento) {
		this.PlazoDescuento = plazoDescuento;
	}

	public float getMontoMinimoAbono() {
		return MontoMinimoAbono;
	}

	public void setMontoMinimoAbono(float montoMinimoAbono) {
		this.MontoMinimoAbono = montoMinimoAbono;
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
		parcel.writeLong(IdCliente);
		parcel.writeLong(IdSucursal);
		parcel.writeString(NombreCliente);
		parcel.writeString(NombreSucursal);
		parcel.writeInt(CreditoCentralizado ? 1 : 0);
		parcel.writeFloat(LimiteCredito);
		parcel.writeFloat(SaldoActual);
		parcel.writeFloat(Disponible);
		parcel.writeInt(PlazoCredito);
		parcel.writeString(PrecioVenta);
		parcel.writeString(Descuentos);
		parcel.writeString(DireccionSucursal);
		parcel.writeString(Tipo);
		parcel.writeString(Categoria);
		parcel.writeString(Telefono);
		parcel.writeInt(PlazoDescuento);
		parcel.writeFloat(MontoMinimoAbono);
		parcel.writeParcelableArray(Notas,flags);	

	}

}
