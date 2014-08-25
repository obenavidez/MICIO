package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo; 
 
public class RespuestaEnviarRecibo implements KvmSerializable {
 public ValorCatalogo NuevoEstado;
 public int NumeroCentral;
 
 public RespuestaEnviarRecibo() {
 }
 
 public RespuestaEnviarRecibo(ValorCatalogo nuevoEstado, int numeroCentral) {
     this.NuevoEstado = nuevoEstado;
     this.NumeroCentral = numeroCentral;
 }
 
 public ValorCatalogo getNuevoEstado() {
     return NuevoEstado;
 }
 
 public void setNuevoEstado(ValorCatalogo nuevoEstado) {
     this.NuevoEstado = nuevoEstado;
 }
 
 public int getNumeroCentral() {
     return NumeroCentral;
 }
 
 public void setNumeroCentral(int numeroCentral) {
     this.NumeroCentral = numeroCentral;
 }

@Override
public Object getProperty(int index) 
{
	switch(index)  
	{
		case 0: return NuevoEstado;
		case 1: return NumeroCentral;		
	}
	return null;
}

@Override
public int getPropertyCount() {
	// TODO Auto-generated method stub
	return 2;
}

@Override
public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) 
{ 
 	   switch(_index)  
 	   { 
	           case 0:
	        	   _info.name = "NuevoEstado";
	               _info.type= ValorCatalogo.class;
	               break;
	           case 1:
	        	   _info.name = "NumeroCentral";
	               _info.type= PropertyInfo.INTEGER_CLASS;
	               break;
 	   }  
}

@Override
public void setProperty(int arg0, Object arg1) {
	// TODO Auto-generated method stub
	
}
}

