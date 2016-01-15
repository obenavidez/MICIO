package com.panzyma.nm.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.ksoap2.serialization.PropertyInfo;

import com.comunicator.AppNMComunication;
import com.comunicator.Parameters;
import com.panzyma.nm.auxiliar.NMConfig;
import com.panzyma.nm.auxiliar.NMTranslate;
import com.panzyma.nm.serviceproxy.CCobro;
import com.panzyma.nm.serviceproxy.CFormaPago;


public class ModelCobro {

	
	 public synchronized static ArrayList<CCobro> getConsultaCobro(String credenciales, boolean delDia, boolean deSemana, boolean deMes, int fechaInic, int fechaFin){
		 
		 Parameters params = new Parameters(
				 			(new String[] { "Credentials","delDia", "deSemana", "deMes", "fechaInic", "fechaFin"}),
				 			(new Object[] { credenciales, delDia, deSemana, deMes, fechaInic, fechaFin }), 
				 			(new Type[] {   PropertyInfo.STRING_CLASS, PropertyInfo.BOOLEAN_CLASS, PropertyInfo.BOOLEAN_CLASS, PropertyInfo.BOOLEAN_CLASS,PropertyInfo.LONG_CLASS, PropertyInfo.LONG_CLASS }));
		 try 
		 {
				Object cobros = AppNMComunication.InvokeMethod( params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,NMConfig.MethodName.GetCobroColector);
				return NMTranslate.ToCollection(cobros, CCobro.class);
		 } 
		 catch (Exception e) {
				e.printStackTrace();
				return null;
		 }
	 }
	
	 public synchronized static ArrayList<CFormaPago> getConsultaPagos(String credenciales, boolean delDia, boolean deSemana, boolean deMes, int fechaInic, int fechaFin){
		 
		 Parameters params = new Parameters(
		 			(new String[] { "Credentials","delDia", "deSemana", "deMes", "fechaInic", "fechaFin"}),
		 			(new Object[] { credenciales, delDia, deSemana, deMes, fechaInic, fechaFin }), 
		 			(new Type[] {   PropertyInfo.STRING_CLASS, PropertyInfo.BOOLEAN_CLASS, PropertyInfo.BOOLEAN_CLASS, PropertyInfo.BOOLEAN_CLASS,PropertyInfo.LONG_CLASS, PropertyInfo.LONG_CLASS }));
			try 
			{
					Object pagos = AppNMComunication.InvokeMethod( params.getParameters(), NMConfig.URL, NMConfig.NAME_SPACE,NMConfig.MethodName.GetPagosColector);
					return NMTranslate.ToCollection(pagos, CFormaPago.class);
			} 
			catch (Exception e) {
					e.printStackTrace();
					return null;
			}
		 
	 }
	
	
	
	
}
