package com.panzyma.nm.serviceproxy;

public class Promocion  {
    protected long Id;
    protected java.lang.String Codigo;
    protected java.lang.String Descripcion;
    protected boolean AplicaCredito;
    protected int FechaFin;
    protected java.lang.String MomentoAplicacion;
    protected java.lang.String TipoPromo;
    protected float MontoMinimo;
    protected java.lang.String TipoDescuento;
    protected float Descuento;
    protected boolean AplicacionMultiple;
    protected int CantidadMinimaItems;
    protected boolean CantidadMinimaBaseUnica;
    protected int CantidadMinimaBase;
    protected boolean CantidadPremioUnica;
    protected int CantidadPremio;
    protected java.lang.String ProductosOtorgadosPor;
    protected java.lang.String MontoEntregadoPor;
    protected java.lang.String DescripcionPromocion;
    protected java.lang.String CatClientes;
    protected java.lang.String TiposCliente;
    protected java.lang.String Sucursales;
    protected java.lang.String Ubicaciones;
    protected java.lang.String ProdsBase;
    protected java.lang.String ProdsPremio;
    protected boolean MontoBaseUnico;
    protected float MontoBaseMinimo;
    protected float MontoBaseMaximo;
    protected boolean MontoPremioUnico;
    protected float MontoPremio;
    
    public Promocion() {
    }
    
    public Promocion(long id, java.lang.String codigo, java.lang.String descripcion, boolean aplicaCredito, int fechaFin, java.lang.String momentoAplicacion, java.lang.String tipoPromo, float montoMinimo, java.lang.String tipoDescuento, float descuento, boolean aplicacionMultiple, int cantidadMinimaItems, boolean cantidadMinimaBaseUnica, int cantidadMinimaBase, boolean cantidadPremioUnica, int cantidadPremio, java.lang.String productosOtorgadosPor, java.lang.String montoEntregadoPor, java.lang.String descripcionPromocion, java.lang.String catClientes, java.lang.String tiposCliente, java.lang.String sucursales, java.lang.String ubicaciones, java.lang.String prodsBase, java.lang.String prodsPremio, boolean montoBaseUnico, float montoBaseMinimo, float montoBaseMaximo, boolean montoPremioUnico, float montoPremio) {
        this.Id = id;
        this.Codigo = codigo;
        this.Descripcion = descripcion;
        this.AplicaCredito = aplicaCredito;
        this.FechaFin = fechaFin;
        this.MomentoAplicacion = momentoAplicacion;
        this.TipoPromo = tipoPromo;
        this.MontoMinimo = montoMinimo;
        this.TipoDescuento = tipoDescuento;
        this.Descuento = descuento;
        this.AplicacionMultiple = aplicacionMultiple;
        this.CantidadMinimaItems = cantidadMinimaItems;
        this.CantidadMinimaBaseUnica = cantidadMinimaBaseUnica;
        this.CantidadMinimaBase = cantidadMinimaBase;
        this.CantidadPremioUnica = cantidadPremioUnica;
        this.CantidadPremio = cantidadPremio;
        this.ProductosOtorgadosPor = productosOtorgadosPor;
        this.MontoEntregadoPor = montoEntregadoPor;
        this.DescripcionPromocion = descripcionPromocion;
        this.CatClientes = catClientes;
        this.TiposCliente = tiposCliente;
        this.Sucursales = sucursales;
        this.Ubicaciones = ubicaciones;
        this.ProdsBase = prodsBase;
        this.ProdsPremio = prodsPremio;
        this.MontoBaseUnico = montoBaseUnico;
        this.MontoBaseMinimo = montoBaseMinimo;
        this.MontoBaseMaximo = montoBaseMaximo;
        this.MontoPremioUnico = montoPremioUnico;
        this.MontoPremio = montoPremio;
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
    
    public java.lang.String getDescripcion() {
        return Descripcion;
    }
    
    public void setDescripcion(java.lang.String descripcion) {
        this.Descripcion = descripcion;
    }
    
    public boolean isAplicaCredito() {
        return AplicaCredito;
    }
    
    public void setAplicaCredito(boolean aplicaCredito) {
        this.AplicaCredito = aplicaCredito;
    }
    
    public int getFechaFin() {
        return FechaFin;
    }
    
    public void setFechaFin(int fechaFin) {
        this.FechaFin = fechaFin;
    }
    
    public java.lang.String getMomentoAplicacion() {
        return MomentoAplicacion;
    }
    
    public void setMomentoAplicacion(java.lang.String momentoAplicacion) {
        this.MomentoAplicacion = momentoAplicacion;
    }
    
    public java.lang.String getTipoPromo() {
        return TipoPromo;
    }
    
    public void setTipoPromo(java.lang.String tipoPromo) {
        this.TipoPromo = tipoPromo;
    }
    
    public float getMontoMinimo() {
        return MontoMinimo;
    }
    
    public void setMontoMinimo(float montoMinimo) {
        this.MontoMinimo = montoMinimo;
    }
    
    public java.lang.String getTipoDescuento() {
        return TipoDescuento;
    }
    
    public void setTipoDescuento(java.lang.String tipoDescuento) {
        this.TipoDescuento = tipoDescuento;
    }
    
    public float getDescuento() {
        return Descuento;
    }
    
    public void setDescuento(float descuento) {
        this.Descuento = descuento;
    }
    
    public boolean isAplicacionMultiple() {
        return AplicacionMultiple;
    }
    
    public void setAplicacionMultiple(boolean aplicacionMultiple) {
        this.AplicacionMultiple = aplicacionMultiple;
    }
    
    public int getCantidadMinimaItems() {
        return CantidadMinimaItems;
    }
    
    public void setCantidadMinimaItems(int cantidadMinimaItems) {
        this.CantidadMinimaItems = cantidadMinimaItems;
    }
    
    public boolean isCantidadMinimaBaseUnica() {
        return CantidadMinimaBaseUnica;
    }
    
    public void setCantidadMinimaBaseUnica(boolean cantidadMinimaBaseUnica) {
        this.CantidadMinimaBaseUnica = cantidadMinimaBaseUnica;
    }
    
    public int getCantidadMinimaBase() {
        return CantidadMinimaBase;
    }
    
    public void setCantidadMinimaBase(int cantidadMinimaBase) {
        this.CantidadMinimaBase = cantidadMinimaBase;
    }
    
    public boolean isCantidadPremioUnica() {
        return CantidadPremioUnica;
    }
    
    public void setCantidadPremioUnica(boolean cantidadPremioUnica) {
        this.CantidadPremioUnica = cantidadPremioUnica;
    }
    
    public int getCantidadPremio() {
        return CantidadPremio;
    }
    
    public void setCantidadPremio(int cantidadPremio) {
        this.CantidadPremio = cantidadPremio;
    }
    
    public java.lang.String getProductosOtorgadosPor() {
        return ProductosOtorgadosPor;
    }
    
    public void setProductosOtorgadosPor(java.lang.String productosOtorgadosPor) {
        this.ProductosOtorgadosPor = productosOtorgadosPor;
    }
    
    public java.lang.String getMontoEntregadoPor() {
        return MontoEntregadoPor;
    }
    
    public void setMontoEntregadoPor(java.lang.String montoEntregadoPor) {
        this.MontoEntregadoPor = montoEntregadoPor;
    }
    
    public java.lang.String getDescripcionPromocion() {
        return DescripcionPromocion;
    }
    
    public void setDescripcionPromocion(java.lang.String descripcionPromocion) {
        this.DescripcionPromocion = descripcionPromocion;
    }
    
    public java.lang.String getCatClientes() {
        return CatClientes;
    }
    
    public void setCatClientes(java.lang.String catClientes) {
        this.CatClientes = catClientes;
    }
    
    public java.lang.String getTiposCliente() {
        return TiposCliente;
    }
    
    public void setTiposCliente(java.lang.String tiposCliente) {
        this.TiposCliente = tiposCliente;
    }
    
    public java.lang.String getSucursales() {
        return Sucursales;
    }
    
    public void setSucursales(java.lang.String sucursales) {
        this.Sucursales = sucursales;
    }
    
    public java.lang.String getUbicaciones() {
        return Ubicaciones;
    }
    
    public void setUbicaciones(java.lang.String ubicaciones) {
        this.Ubicaciones = ubicaciones;
    }
    
    public java.lang.String getProdsBase() {
        return ProdsBase;
    }
    
    public void setProdsBase(java.lang.String prodsBase) {
        this.ProdsBase = prodsBase;
    }
    
    public java.lang.String getProdsPremio() {
        return ProdsPremio;
    }
    
    public void setProdsPremio(java.lang.String prodsPremio) {
        this.ProdsPremio = prodsPremio;
    }
    
    public boolean isMontoBaseUnico() {
        return MontoBaseUnico;
    }
    
    public void setMontoBaseUnico(boolean montoBaseUnico) {
        this.MontoBaseUnico = montoBaseUnico;
    }
    
    public float getMontoBaseMinimo() {
        return MontoBaseMinimo;
    }
    
    public void setMontoBaseMinimo(float montoBaseMinimo) {
        this.MontoBaseMinimo = montoBaseMinimo;
    }
    
    public float getMontoBaseMaximo() {
        return MontoBaseMaximo;
    }
    
    public void setMontoBaseMaximo(float montoBaseMaximo) {
        this.MontoBaseMaximo = montoBaseMaximo;
    }
    
    public boolean isMontoPremioUnico() {
        return MontoPremioUnico;
    }
    
    public void setMontoPremioUnico(boolean montoPremioUnico) {
        this.MontoPremioUnico = montoPremioUnico;
    }
    
    public float getMontoPremio() {
        return MontoPremio;
    }
    
    public void setMontoPremio(float montoPremio) {
        this.MontoPremio = montoPremio;
    }
}
