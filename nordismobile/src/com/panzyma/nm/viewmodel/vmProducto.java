package com.panzyma.nm.viewmodel;

public class vmProducto { 
	private long id;
	private java.lang.String codigo;
	private java.lang.String nombre;
	private int disponible;
	private boolean checked = false; 
	
	public vmProducto(long id,java.lang.String codigo,java.lang.String nombre,int disponible)
	{ 
		this.id=id;
		this.codigo=codigo;
		this.nombre=nombre;
		this.disponible=disponible;
	}
	
    public void setId(long idProducto) 
    {
        this.id = idProducto;
    }

    public long getId() {
        return this.id;
    }

    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }

    public java.lang.String getCodigo() {
        return this.codigo;
    }
    
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }
    
    public java.lang.String getNombre() {
        return this.nombre;
    }
    
    public void setDisponible(java.lang.Integer disponible) {
        this.disponible = disponible;
    }
    
    public java.lang.Integer getDisponibilidad() {
        return this.disponible;
    } 
    
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public void toggleChecked() {
		checked = !checked;
	}
    
    public Object isMatch(CharSequence constraint)
    {
    	if (getNombre().toLowerCase().startsWith(constraint.toString()))
    		return true;
    	return false;
    }
}
