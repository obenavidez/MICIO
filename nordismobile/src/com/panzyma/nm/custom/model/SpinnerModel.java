package com.panzyma.nm.custom.model;

public class SpinnerModel {

	private long id = 0;
	private String codigo = "";
	private String descripcion = "";
	private Object obj ;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}	

}