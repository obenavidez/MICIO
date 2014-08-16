package com.panzyma.nm.serviceproxy;

import java.util.Vector;

/*
 * DescFactura.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */
  
public class DescFactura {
    private long _idFactura;
    private String _numFactura;
    private float _montoFact;
    private float _porcMtoTotal;
    private float _montoDescPP;
    private Vector _descuentosProveedor;
    
    public DescFactura() {    
    }
    
    public DescFactura(long idFactura, String numFactura, float montoFact, float porcMtoTotal, float montoDescPP) {    
        setIdFactura(idFactura);
        setNumFactura(numFactura);
        setMontoFact(montoFact);
        setPorcMtoTotal(porcMtoTotal);
        setMontoDescPP(montoDescPP);
        _descuentosProveedor = new Vector();
    }
    
    public void setIdFactura(long idFactura) { _idFactura = idFactura; }
    public void setNumFactura(String numFactura) { _numFactura = numFactura; }
    public void setMontoFact(float montoFact) { _montoFact = montoFact; }
    public void setPorcMtoTotal(float porcMtoTotal) { _porcMtoTotal = porcMtoTotal; }
    public void setMontoDescPP(float montoDescPP) { _montoDescPP = montoDescPP; }
    public void setDescuentosProveedor(Vector descuentosProveedor) { _descuentosProveedor = descuentosProveedor; }
    
    public long getIdFactura() { return _idFactura; }
    public String getNumFactura() { return _numFactura; }
    public float getMontoFact() { return _montoFact; }
    public float getPorcMtoTotal() { return _porcMtoTotal; }
    public float getMontoDescPP() { return _montoDescPP; }    
    public Vector getDescuentosProveedor() { return _descuentosProveedor; }
    
    public void addDescProveedor(DescProveedor descProv) { _descuentosProveedor.addElement(descProv); }
} 
