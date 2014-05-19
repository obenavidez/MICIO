package com.panzyma.nm.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.panzyma.nm.interfaces.Item;
import com.panzyma.nm.serviceproxy.CNota;

public class vmFicha implements Parcelable, Item {

	
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
    
    public vmFicha (){
    	
    }
    

	
    public  CNota[] getNotas() {
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
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
