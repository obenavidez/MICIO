package com.panzyma.nm.serviceproxy;


public class CalcDescOca_Output {
    private float _mtoFacturasVencidas; //Facturas seleccionadas para cancelar vencidas
    private float _mtoNcProporcionalVencidas; //Monto de notas de crédito proporcional al total de facturas vencidas
    public float mtoTotalFacturas; //Facturas seleccionadas para cancelar        
    public float mtoFacturasVigentes; //Facturas seleccionadas para cancelar vigentes
    public float mtoFacturasVencidas; //Facturas seleccionadas para cancelar vencidas
    public float mtoTotalNC; //Total de notas de crédito
    public float mtoNcProporcionalVigente; //Monto de notas de crédito proporcional al total de facturas vigentes
            
    public CalcDescOca_Output() {    }
    
    public CalcDescOca_Output(float mtoFacturasVencidas, float mtoNcProporcionalVencidas) {    
        _mtoFacturasVencidas = mtoFacturasVencidas;
        _mtoNcProporcionalVencidas = mtoNcProporcionalVencidas;
    }
    
    public void setMtoFacturasVencidas(float mtoFacturasVencidas) { _mtoFacturasVencidas = mtoFacturasVencidas; }
    public void setMtoNcProporcionalVencidas(float mtoNcProporcionalVencidas) { _mtoNcProporcionalVencidas = mtoNcProporcionalVencidas; }
    
    public float getMtoFacturasVencidas() { return _mtoFacturasVencidas; }
    public float getMtoNcProporcionalVencidas() { return _mtoNcProporcionalVencidas; }
} 
