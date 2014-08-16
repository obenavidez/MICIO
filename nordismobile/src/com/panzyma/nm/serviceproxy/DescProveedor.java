package com.panzyma.nm.serviceproxy;

/*
 * DescProveedor.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */ 
public class DescProveedor {
    private long _idFactura;
    private long _idProveedor;
    private String _codProveedor;
    private float _montoFactProv;
    private float _porcMtoFact;
    private float _mtoAplicable;
    private float _porcDescPP;
    private float _montoDescPPProv;
        
    public DescProveedor() {    }
    
    public DescProveedor(long idFactura, long idProveedor, String codProveedor, float montoFactProv, float porcMtoFact, float mtoAplicable, float porcDescPP, float montoDescPPProv) {    
        setIdFactura(idFactura);
        setIdProveedor(idProveedor);
        setCodProveedor(codProveedor);
        setMontoFactProv(montoFactProv);
        setPorcMtoFact(porcMtoFact);
        setMtoAplicable(mtoAplicable);
        setPorcDescPP(porcDescPP);
        setMontoDescPPProv(montoDescPPProv);
    }
    
    public void setIdFactura(long idFactura) { _idFactura = idFactura; }
    public void setIdProveedor(long idProveedor) { _idProveedor = idProveedor; }
    public void setCodProveedor(String codProveedor) { _codProveedor = codProveedor; }
    public void setMontoFactProv(float montoFactProv) { _montoFactProv = montoFactProv; }
    public void setPorcMtoFact(float porcMtoFact) { _porcMtoFact = porcMtoFact; }
    public void setMtoAplicable(float mtoAplicable) { _mtoAplicable = mtoAplicable; }
    public void setPorcDescPP(float porcDescPP) { _porcDescPP = porcDescPP; }
    public void setMontoDescPPProv(float montoDescPPProv) { _montoDescPPProv = montoDescPPProv; }
    
    public long getIdFactura() { return _idFactura; }
    public long getIdProveedor() { return _idProveedor; }
    public String getCodProveedor() { return _codProveedor; }
    public float getMontoFactProv() { return _montoFactProv; }
    public float getPorcMtoFact() { return _porcMtoFact; }
    public float getMtoAplicable() { return _mtoAplicable; }
    public float getPorcDescPP() { return _porcDescPP; }
    public float getMontoDescPPProv() { return _montoDescPPProv; }
} 
