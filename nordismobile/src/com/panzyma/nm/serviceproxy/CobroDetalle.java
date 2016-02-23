package com.panzyma.nm.serviceproxy;

import java.util.List;

public class CobroDetalle {

	
	private List<CCobro> cobros ;
	private List<CFormaPago> formapagos;
	
	
	public CobroDetalle(List<CCobro> cobros ,  List<CFormaPago> formaspagos){
		this.cobros = cobros;
		this.formapagos = formaspagos;
	}


	public List<CCobro> getCobros() {
		return cobros;
	}


	public void setCobros(List<CCobro> cobros) {
		this.cobros = cobros;
	}


	public List<CFormaPago> getFormapagos() {
		return formapagos;
	}


	public void setFormapagos(List<CFormaPago> formapagos) {
		this.formapagos = formapagos;
	}
	
}
