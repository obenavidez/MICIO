package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CNota implements KvmSerializable 
{ 
	public java.lang.String Fecha;
    public java.lang.String ElaboradaPor;
    public java.lang.String TextoNota;
    public java.lang.String Concepto;
   
    
    public CNota()
	{		
		Fecha = "";
		ElaboradaPor ="";
		TextoNota ="";
		Concepto = "";
	} 
    
    public CNota(java.lang.String fecha, java.lang.String elaboradaPor, java.lang.String textoNota, java.lang.String concepto) {
        this.Fecha = fecha;
        this.ElaboradaPor = elaboradaPor;
        this.TextoNota = textoNota;
        this.Concepto = concepto;
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
	
	
	

}
