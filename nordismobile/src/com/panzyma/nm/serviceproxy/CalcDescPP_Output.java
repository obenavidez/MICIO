package com.panzyma.nm.serviceproxy;


import java.util.*;
 
public class CalcDescPP_Output {
    private float _montoAplicarDescPP;
    private float _totalNCAplicar;
    private float _mtoTotalFacturasCancelar;
    private float _mtoTotalFacturasAplicar;
    private Vector _detalleFacturas;
    
    public CalcDescPP_Output() {    }
    
    public CalcDescPP_Output(float montoAplicarDescPP, float totalNCAplicar, float _mtoTotalFacturasCancelar, float _mtoTotalFacturasAplicar) {    
        setMontoAplicarDescPP(montoAplicarDescPP);
        setTotalNCAplicar(totalNCAplicar);
        setMtoTotalFacturasCancelar(_mtoTotalFacturasCancelar);
        setMtoTotalFacturasAplicar(_mtoTotalFacturasAplicar);
        _detalleFacturas = new Vector();
    }
    
    public void setMontoAplicarDescPP(float montoAplicarDescPP) { _montoAplicarDescPP = montoAplicarDescPP; }
    public void setTotalNCAplicar(float totalNCAplicar) { _totalNCAplicar = totalNCAplicar; }
    public void setMtoTotalFacturasCancelar(float mtoTotalFacturasCancelar) { _mtoTotalFacturasCancelar = mtoTotalFacturasCancelar; }
    public void setMtoTotalFacturasAplicar(float mtoTotalFacturasAplicar) { _mtoTotalFacturasAplicar = mtoTotalFacturasAplicar; }
    public void setDetalleFacturas(Vector detalleFacturas) { _detalleFacturas = detalleFacturas; }
    
    public float getMontoAplicarDescPP() { return _montoAplicarDescPP; } 
    public float gettotalNCAplicar() { return _totalNCAplicar; } 
    public float getMtoTotalFacturasCancelar() { return _mtoTotalFacturasCancelar; } 
    public float getMtoTotalFacturasAplicar() { return _mtoTotalFacturasAplicar; } 
    public Vector getDetalleFacturas() { return _detalleFacturas; } 
    
    public void addDetalleFactura(DescFactura descFac) { _detalleFacturas.addElement(descFac); }
} 
