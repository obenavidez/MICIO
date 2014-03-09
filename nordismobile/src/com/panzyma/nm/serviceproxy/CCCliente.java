package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 



@SuppressWarnings("rawtypes")
public class CCCliente  implements KvmSerializable
{
	 
	//public static Class CCCliente_Class=CCCliente.class;
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
    public CCCliente()
	{ 
    	
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
	public Object getProperty(int arg0) {

		switch(arg0)
        {
        case 0:
            return Notas;
        case 1:
            return IdCliente;
        case 2:
            return IdSucursal;
        case 3:
            return NombreCliente;
        case 4:
            return NombreSucursal;
        case 5:
            return CreditoCentralizado;
        case 6:
            return LimiteCredito;
        case 7:
            return SaldoActual;
        case 8:
            return Disponible;
        case 9:
            return PlazoCredito;
        case 10:
            return PrecioVenta;
        case 11:
            return Descuentos;
        case 12:
            return DireccionSucursal;
        case 13:
            return Tipo;
        case 14:
            return Categoria;
        case 15:
            return Telefono;
        case 16:
            return PlazoDescuento;
        case 17:
            return  MontoMinimoAbono; 
        }
		
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 18;
	}
	 
	@Override
	public void getPropertyInfo(int ind, Hashtable ht, PropertyInfo info) {
		switch(ind)
        {
	        
	        case 0: 
	            info.type =CNota[].class;
	            info.name = "CNota"; 
            break;
            case 1:
	            info.type = PropertyInfo.LONG_CLASS;
	            info.name = "IdCliente";
	            break;
	        case 2:
	            info.type = PropertyInfo.LONG_CLASS;
	            info.name = "IdSucursal";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "NombreCliente";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "NombreSucursal";
	            break;
	        case 5:
	            info.type = PropertyInfo.BOOLEAN_CLASS;
	            info.name = "CreditoCentralizado";
	            break;
	        case 6:
	            info.type = Float.class;
	            info.name = "LimiteCredito";
	            break;
	        case 7:
	            info.type = Float.class;
	            info.name = "SaldoActual";
	            break;
	        case 8:
	            info.type = Float.class;
	            info.name = "Disponible";
	            break;
	        case 9:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "PlazoCredito";
	            break;
	        case 10:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "PrecioVenta" ;
	            break;
	        case 11:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "Descuentos";
	            break;
	        
	        case 12:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "DireccionSucursal";
	            break;
	        case 13:
	            info.type =PropertyInfo.STRING_CLASS;
	            info.name = "Tipo";
	            break;
	        case 14:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "Categoria";
	            break;
	        case 15:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "Telefono";
	            break;
	        case 16:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "PlazoDescuento";
	            break;
	        case 17:
	            info.type = Float.class;
	            info.name = "MontoMinimoAbono";
	            break;
	        default:break;
        }
	}
	
	@Override
	public void setProperty(int ind, Object val) {
		switch(ind)
        {
	    
	            
	        case 0:
	        	Notas =(CNota[])val; 
	            break;
	        case 1:
	        	IdCliente = Long.parseLong(val.toString()); 
	            break;
	        case 2:
	        	IdSucursal = Long.parseLong(val.toString());  
	            break;
	        case 3:
	        	NombreCliente = String.valueOf(val.toString());  
	            break;
	        case 4:
	        	NombreSucursal =String.valueOf(val.toString()); 
	            break;
	        case 5:
	        	CreditoCentralizado = Boolean.parseBoolean(val.toString()); 
	            break;
	        case 6:
	        	LimiteCredito = Float.parseFloat(val.toString()); 
	            break;
	        case 7:
	        	SaldoActual = Float.parseFloat(val.toString());
	            break;
	        case 8:
	        	Disponible =  Float.parseFloat(val.toString());
	            break;
	        case 9:
	        	PlazoCredito = Integer.parseInt(val.toString()); 
	            break;
	        case 10:
	        	PrecioVenta = String.valueOf(val.toString()); 
	        	  
	            break;
	        case 11:
	        	Descuentos = String.valueOf(val.toString());
	        	 
	            break;
	        case 12:
	        	DireccionSucursal =String.valueOf(val.toString());
	            break;
	        case 13:
	        	Tipo = String.valueOf(val.toString()); 
	            break;
	       
	        case 14:
	        	Categoria = String.valueOf(val.toString()); 
	            break;
	        case 15:
	        	Telefono = String.valueOf(val.toString());
	            break;
	        case 16:
	        	
	        	PlazoDescuento = Integer.parseInt(val.toString());
	            break;
	        case 17:
	        	
	        	MontoMinimoAbono =  Float.parseFloat(val.toString());
	            break;
	        default:break;
	            
        }
	}



}