package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ValorCatalogo implements KvmSerializable{

	public long Id;
    public String Codigo;
    public String Descripcion;
    
	public ValorCatalogo() { 
	}	
	
	public ValorCatalogo(long id, String codigo, String descripcion) {
		super();
		Id = id;
		Codigo = codigo;
		Descripcion = descripcion;
	}

	public long getId(){
		return Id;
	}
	public String getCodigo(){
		return Codigo;
	}
	public String getDescripcion(){
		return Descripcion;
	}
	
	public void setId(long _id){
		this.Id=_id;
	}
	public void setCodigo(String _codigo){
		this.Codigo=_codigo;
	}
	public void setDescripcion(String _descripcion){
		this.Descripcion=_descripcion;
	}

	@Override
	public Object getProperty(int index) {
		switch(index)  
		{
			case 0: return Id;
			case 1: return Codigo;
			case 2: return Descripcion;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) 
	{  
 	   switch(_index)  
 	   { 
	           case 0:
	        	   _info.name = "Id";
	               _info.type= PropertyInfo.LONG_CLASS;
	           case 1:
	        	   _info.name = "Codigo";
	               _info.type= PropertyInfo.STRING_CLASS;
	           case 2:
	        	   _info.name = "Descripcion";
	               _info.type= PropertyInfo.STRING_CLASS;
 	   }  
	}

	@Override
	public void setProperty(int arg0, Object arg1) {		 
		
	}
}
