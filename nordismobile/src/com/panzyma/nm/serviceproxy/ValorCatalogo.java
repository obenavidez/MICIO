package com.panzyma.nm.serviceproxy;

public class ValorCatalogo {

	private long Id;
    private String Codigo;
    private String Descripcion;
    
	public ValorCatalogo() { 
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
}
