package com.panzyma.nm.serviceproxy;
 

public class DisponibilidadProducto {
    protected long IdProducto;
    protected int Disponible;
    
    public DisponibilidadProducto() {
    }
    
    public DisponibilidadProducto(long idProducto, int disponible) {
        this.IdProducto = idProducto;
        this.Disponible = disponible;
    }
    
    public long getIdProducto() {
        return IdProducto;
    }
    
    public void setIdProducto(long idProducto) {
        this.IdProducto = idProducto;
    }
    
    public int getDisponible() {
        return Disponible;
    }
    
    public void setDisponible(int disponible) {
        this.Disponible = disponible;
    }
}
