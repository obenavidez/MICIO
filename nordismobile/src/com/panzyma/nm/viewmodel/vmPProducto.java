package com.panzyma.nm.viewmodel; 

public class vmPProducto {
    
	private long idproducto; 
	private String nomproducto;
	private Integer cantidad; 
	
	//private  static vmPProducto instance=null;
	
	public vmPProducto(){}
	
	public vmPProducto(long idproducto, String nomproducto, Integer cantidad) {
		super();
		this.idproducto = idproducto;
		this.nomproducto = nomproducto;
		this.cantidad = cantidad;
	}
	
	/*public static vmPProducto getInstance() {
		if (instance == null) {
			instance = new vmPProducto();
		}
		return instance;
	}*/
	
	public void setData(long idproducto, String nomproducto, Integer cantidad) { 
		this.idproducto = idproducto;
		this.nomproducto = nomproducto;
		this.cantidad = cantidad;
	}
	
	public long getIdproducto() {
		return idproducto;
	}
	public void setIdproducto(long idproducto) {
		this.idproducto = idproducto;
	}
	public String getNomproducto() {
		return nomproducto;
	}
	public void setNomproducto(String nomproducto) {
		this.nomproducto = nomproducto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}	
	 
	
}




