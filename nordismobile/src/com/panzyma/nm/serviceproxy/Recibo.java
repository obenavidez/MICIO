package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Recibo implements Parcelable {

	protected long id;
	protected int numero;
	protected long fecha;
	protected java.lang.String notas;
	protected float totalRecibo;
	protected float totalFacturas;
	protected float totalND;
	protected float totalInteres;
	protected float subTotal;
	protected float totalDesc;
	protected float totalRetenido;
	protected float totalOtrasDed;
	protected float totalNC;
	protected int referencia;
	protected long objClienteID;
	protected long objSucursalID;
	protected java.lang.String nombreCliente;
	protected long objColectorID;
	protected boolean aplicaDescOca;
	protected java.lang.String claveAutorizaDescOca;
	protected float porcDescOcaColector;
	protected long objEstadoID;
	protected java.lang.String codEstado;
	protected java.lang.String descEstado;
	protected float totalDescOca;
	protected float totalDescPromo;
	protected float totalDescPP;
	protected float totalImpuestoProporcional;
	protected float totalImpuestoExonerado;
	protected boolean exento;
	protected java.lang.String autorizacionDGI;
	
	protected ArrayList<ReciboDetFactura> facturasRecibo = new ArrayList<ReciboDetFactura>(); 
	protected ArrayList<ReciboDetNC> notasCreditoRecibo = new ArrayList<ReciboDetNC>();
	protected ArrayList<ReciboDetND> notasDebitoRecibo = new ArrayList<ReciboDetND>();
	
	public Recibo() {
		super();		
	}

	public Recibo(long id, int numero, long fecha, String notas,
			float totalRecibo, float totalFacturas, float totalND,
			float totalInteres, float subTotal, float totalDesc,
			float totalRetenido, float totalOtrasDed, float totalNC,
			int referencia, long objClienteID, long objSucursalID,
			String nombreCliente, long objColectorID, boolean aplicaDescOca,
			String claveAutorizaDescOca, float porcDescOcaColector,
			long objEstadoID, String codEstado, String descEstado,
			float totalDescOca, float totalDescPromo, float totalDescPP,
			float totalImpuestoProporcional, float totalImpuestoExonerado,
			boolean exento, String autorizacionDGI) {
		super();
		this.id = id;
		this.numero = numero;
		this.fecha = fecha;
		this.notas = notas;
		this.totalRecibo = totalRecibo;
		this.totalFacturas = totalFacturas;
		this.totalND = totalND;
		this.totalInteres = totalInteres;
		this.subTotal = subTotal;
		this.totalDesc = totalDesc;
		this.totalRetenido = totalRetenido;
		this.totalOtrasDed = totalOtrasDed;
		this.totalNC = totalNC;
		this.referencia = referencia;
		this.objClienteID = objClienteID;
		this.objSucursalID = objSucursalID;
		this.nombreCliente = nombreCliente;
		this.objColectorID = objColectorID;
		this.aplicaDescOca = aplicaDescOca;
		this.claveAutorizaDescOca = claveAutorizaDescOca;
		this.porcDescOcaColector = porcDescOcaColector;
		this.objEstadoID = objEstadoID;
		this.codEstado = codEstado;
		this.descEstado = descEstado;
		this.totalDescOca = totalDescOca;
		this.totalDescPromo = totalDescPromo;
		this.totalDescPP = totalDescPP;
		this.totalImpuestoProporcional = totalImpuestoProporcional;
		this.totalImpuestoExonerado = totalImpuestoExonerado;
		this.exento = exento;
		this.autorizacionDGI = autorizacionDGI;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}

	public java.lang.String getNotas() {
		return notas;
	}

	public void setNotas(java.lang.String notas) {
		this.notas = notas;
	}

	public float getTotalRecibo() {
		return totalRecibo;
	}

	public void setTotalRecibo(float totalRecibo) {
		this.totalRecibo = totalRecibo;
	}

	public float getTotalFacturas() {
		return totalFacturas;
	}

	public void setTotalFacturas(float totalFacturas) {
		this.totalFacturas = totalFacturas;
	}

	public float getTotalND() {
		return totalND;
	}

	public void setTotalND(float totalND) {
		this.totalND = totalND;
	}

	public float getTotalInteres() {
		return totalInteres;
	}

	public void setTotalInteres(float totalInteres) {
		this.totalInteres = totalInteres;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public float getTotalDesc() {
		return totalDesc;
	}

	public void setTotalDesc(float totalDesc) {
		this.totalDesc = totalDesc;
	}

	public float getTotalRetenido() {
		return totalRetenido;
	}

	public void setTotalRetenido(float totalRetenido) {
		this.totalRetenido = totalRetenido;
	}

	public float getTotalOtrasDed() {
		return totalOtrasDed;
	}

	public void setTotalOtrasDed(float totalOtrasDed) {
		this.totalOtrasDed = totalOtrasDed;
	}

	public float getTotalNC() {
		return totalNC;
	}

	public void setTotalNC(float totalNC) {
		this.totalNC = totalNC;
	}

	public int getReferencia() {
		return referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}

	public long getObjClienteID() {
		return objClienteID;
	}

	public void setObjClienteID(long objClienteID) {
		this.objClienteID = objClienteID;
	}

	public long getObjSucursalID() {
		return objSucursalID;
	}

	public void setObjSucursalID(long objSucursalID) {
		this.objSucursalID = objSucursalID;
	}

	public java.lang.String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(java.lang.String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public long getObjColectorID() {
		return objColectorID;
	}

	public void setObjColectorID(long objColectorID) {
		this.objColectorID = objColectorID;
	}

	public boolean isAplicaDescOca() {
		return aplicaDescOca;
	}

	public void setAplicaDescOca(boolean aplicaDescOca) {
		this.aplicaDescOca = aplicaDescOca;
	}

	public java.lang.String getClaveAutorizaDescOca() {
		return claveAutorizaDescOca;
	}

	public void setClaveAutorizaDescOca(java.lang.String claveAutorizaDescOca) {
		this.claveAutorizaDescOca = claveAutorizaDescOca;
	}

	public float getPorcDescOcaColector() {
		return porcDescOcaColector;
	}

	public void setPorcDescOcaColector(float porcDescOcaColector) {
		this.porcDescOcaColector = porcDescOcaColector;
	}

	public long getObjEstadoID() {
		return objEstadoID;
	}

	public void setObjEstadoID(long objEstadoID) {
		this.objEstadoID = objEstadoID;
	}

	public java.lang.String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(java.lang.String codEstado) {
		this.codEstado = codEstado;
	}

	public java.lang.String getDescEstado() {
		return descEstado;
	}

	public void setDescEstado(java.lang.String descEstado) {
		this.descEstado = descEstado;
	}

	public float getTotalDescOca() {
		return totalDescOca;
	}

	public void setTotalDescOca(float totalDescOca) {
		this.totalDescOca = totalDescOca;
	}

	public float getTotalDescPromo() {
		return totalDescPromo;
	}

	public void setTotalDescPromo(float totalDescPromo) {
		this.totalDescPromo = totalDescPromo;
	}

	public float getTotalDescPP() {
		return totalDescPP;
	}

	public void setTotalDescPP(float totalDescPP) {
		this.totalDescPP = totalDescPP;
	}

	public float getTotalImpuestoProporcional() {
		return totalImpuestoProporcional;
	}

	public void setTotalImpuestoProporcional(float totalImpuestoProporcional) {
		this.totalImpuestoProporcional = totalImpuestoProporcional;
	}

	public float getTotalImpuestoExonerado() {
		return totalImpuestoExonerado;
	}

	public void setTotalImpuestoExonerado(float totalImpuestoExonerado) {
		this.totalImpuestoExonerado = totalImpuestoExonerado;
	}

	public boolean isExento() {
		return exento;
	}

	public void setExento(boolean exento) {
		this.exento = exento;
	}

	public java.lang.String getAutorizacionDGI() {
		return autorizacionDGI;
	}

	public void setAutorizacionDGI(java.lang.String autorizacionDGI) {
		this.autorizacionDGI = autorizacionDGI;
	}

	public ArrayList<ReciboDetFactura> getFacturasRecibo() {
		return facturasRecibo;
	}

	public void setFacturasRecibo(ArrayList<ReciboDetFactura> facturasRecibo) {
		this.facturasRecibo = facturasRecibo;
	}

	public ArrayList<ReciboDetNC> getNotasCreditoRecibo() {
		return notasCreditoRecibo;
	}

	public void setNotasCreditoRecibo(ArrayList<ReciboDetNC> notasCreditoRecibo) {
		this.notasCreditoRecibo = notasCreditoRecibo;
	}

	public ArrayList<ReciboDetND> getNotasDebitoRecibo() {
		return notasDebitoRecibo;
	}

	public void setNotasDebitoRecibo(ArrayList<ReciboDetND> notasDebitoRecibo) {
		this.notasDebitoRecibo = notasDebitoRecibo;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}	

}
