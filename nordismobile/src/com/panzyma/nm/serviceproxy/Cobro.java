package com.panzyma.nm.serviceproxy;

import com.panzyma.nm.auxiliar.StringUtil;

import android.content.Context;

public class Cobro {

	Context context ;
	
	
	 public static float getSaldoTotalCliente(Cliente cliente) {
		 float saldo = 0;
		 
		 if (cliente.getFacturasPendientes() != null) {
	            Factura[] ff = cliente.FacturasPendientes;
	            if ((ff != null) && (ff.length > 0)) {
	                for (int i=0; i < ff.length; i++) {
	                    Factura f = ff[i];
	                    saldo += f.getSaldo() + getInteresMoratorio(f.getFechaVencimiento(), f.getSaldo());                    
	                }
	            }
	        }
	        
	        if (cliente.getNotasDebitoPendientes() != null) {
	            CCNotaDebito[] dd = cliente.NotasDebitoPendientes;
	            if ((dd != null) && (dd.length > 0)) {
	                for (int i=0; i < dd.length; i++) {
	                    CCNotaDebito d = dd[i];
	                    saldo += d.getSaldo() + getInteresMoratorio(d.getFechaVence(), d.getSaldo());                    
	                }
	            }
	        }

		 return saldo;
	 }
	
	 
	 public static CCobro[] getConsultaCobro(boolean delDia, boolean deSemana, boolean deMes, int fechaInic, int fechaFin){
		 CCobro[] det = null;
		 
		 
		 
		 
		 
		 return det;
	 }
	 
	 
	 
	
	 public static float getInteresMoratorio(long iFechaVen, float saldo) {
		 float interes = 0;
		 
		 
					/*this.context
					.getSharedPreferences("SystemParams", android.content.Context.MODE_PRIVATE)
					.getString("PorcInteresMoratorio", "0"); */

		 /*
		 float porcInteresMoratorio = Float.parseFloat(Params.getValue("PorcInteresMoratorio"));        
	     if (porcInteresMoratorio == 0) return 0; 
	        
	     int diasDespuesVenceCalculaMora = Integer.parseInt(Params.getValue("DiasDespuesVenceCalculaMora"));
	     String sFechaVen = iFechaVen + ""; 
	     sFechaVen = sFechaVen.substring(0, 8);        
	     int fechaVen = Integer.parseInt(sFechaVen);
	     long fechaMora = DateUtil.addDays(DateUtil.getTime(fechaVen), diasDespuesVenceCalculaMora); */
		 
		 return StringUtil.round(interes, 2);
	 }
	
}
