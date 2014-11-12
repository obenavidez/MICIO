package com.panzyma.nm.serviceproxy;

public class CProducto {
	
//	protected com.panzyma.nm.serviceproxy.ArrayOfCNota Notas;

	protected CNota[]  Notas;
	protected long Id;
    protected java.lang.String Codigo;
	protected java.lang.String Nombre;
    protected java.lang.String Registro;
    protected java.lang.String NombreComercial;
    protected java.lang.String NombreGenerico;
    protected java.lang.String Proveedor;
    protected java.lang.String Categoria;
    protected java.lang.String FormaFarmaceutica;
    protected java.lang.String AccionFarmacologica;
    protected java.lang.String TipoProducto;
    protected java.lang.String Especialidades;
	
	 public CProducto() {
		    this.Codigo="";
		    this.Nombre="";
		    this.Registro="";
		    this.NombreComercial="";
		    this.NombreGenerico="";
		    this.Proveedor="";
		    this.Categoria="";
		    this.FormaFarmaceutica="";
		    this.AccionFarmacologica="";
		    this.TipoProducto="";
		    this.Especialidades="";
	 }
	 
	 
	 public long getId() {
	        return Id;
	    }
	    
	    public void setId(long id) {
	        this.Id = id;
	    }
	    
	    public java.lang.String getCodigo() {
	        return Codigo;
	    }
	    
	    public void setCodigo(java.lang.String codigo) {
	        this.Codigo = codigo;
	    }
	    
	    public java.lang.String getNombre() {
	        return Nombre;
	    }
	    
	    public void setNombre(java.lang.String nombre) {
	        this.Nombre = nombre;
	    }
	    
	    public java.lang.String getRegistro() {
	        return Registro;
	    }
	    
	    public void setRegistro(java.lang.String registro) {
	        this.Registro = registro;
	    }
	    
	    public java.lang.String getNombreComercial() {
	        return NombreComercial;
	    }
	    
	    public void setNombreComercial(java.lang.String nombreComercial) {
	        this.NombreComercial = nombreComercial;
	    }
	    
	    public java.lang.String getNombreGenerico() {
	        return NombreGenerico;
	    }
	    
	    public void setNombreGenerico(java.lang.String nombreGenerico) {
	        this.NombreGenerico = nombreGenerico;
	    }
	    
	    public java.lang.String getProveedor() {
	        return Proveedor;
	    }
	    
	    public void setProveedor(java.lang.String proveedor) {
	        this.Proveedor = proveedor;
	    }
	    
	    public java.lang.String getCategoria() {
	        return Categoria;
	    }
	    
	    public void setCategoria(java.lang.String categoria) {
	        this.Categoria = categoria;
	    }
	    
	    public java.lang.String getFormaFarmaceutica() {
	        return FormaFarmaceutica;
	    }
	    
	    public void setFormaFarmaceutica(java.lang.String formaFarmaceutica) {
	        this.FormaFarmaceutica = formaFarmaceutica;
	    }
	    
	    public java.lang.String getAccionFarmacologica() {
	        return AccionFarmacologica;
	    }
	    
	    public void setAccionFarmacologica(java.lang.String accionFarmacologica) {
	        this.AccionFarmacologica = accionFarmacologica;
	    }
	    
	    public java.lang.String getTipoProducto() {
	        return TipoProducto;
	    }
	    
	    public void setTipoProducto(java.lang.String tipoProducto) {
	        this.TipoProducto = tipoProducto;
	    }
	    
	    public java.lang.String getEspecialidades() {
	        return Especialidades;
	    }
	    
	    public void setEspecialidades(java.lang.String especialidades) {
	        this.Especialidades = especialidades;
	    }
	    public CNota[] getNotas() {
			return Notas;
		}
		public void setNotas(CNota[] notas) {
			Notas = notas;
		}
		
}
