package com.panzyma.nm.serviceproxy;

import org.ksoap2.serialization.SoapObject;

public class ArrayOfCNota {
    protected  CNota[] CNota;
    
    public ArrayOfCNota() {
    }
    
    public  ArrayOfCNota(CNota[] cnota) {
    	this.CNota = cnota;
    }
    public ArrayOfCNota(SoapObject obj) {
    	 
    	CNota[] cnotas = new CNota[obj.getPropertyCount()];
		for (int i = 0; i < cnotas.length; i++) 
		{
			   SoapObject sic=(SoapObject)obj.getProperty(i);
	           CNota cnota = new CNota();
	           cnota.Fecha = (sic.getProperty(0).toString());
	           cnota.ElaboradaPor = sic.getProperty(1).toString();
	           cnota.TextoNota =(sic.getProperty(2).toString());
	           cnota.Concepto =(sic.getProperty(3).toString());
	           cnotas[i]=cnota; 
	    }    
		this.CNota=cnotas; 
    }
    public ArrayOfCNota getArrayOfCNota() {    	
    	ArrayOfCNota aocn=new ArrayOfCNota(); 
    	aocn.CNota=this.CNota;
       return aocn; 
    }
    public CNota[] getCNota() {    	
    	return this.CNota; 
    }
    public   void setCNota(CNota[] cnota) {
        CNota = cnota;
    }
    
 
}