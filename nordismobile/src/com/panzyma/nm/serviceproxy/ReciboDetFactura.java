package com.panzyma.nm.serviceproxy;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class ReciboDetFactura implements KvmSerializable,Documento, Parcelable {

	protected long Id;
	protected long ObjFacturaID;
	protected long ObjReciboID;
	protected float Monto;
	protected boolean EsAbono;
	protected float MontoDescEspecifico;
	protected float MontoDescOcasional;
	protected float MontoRetencion;
	protected float MontoImpuesto;
	protected float MontoInteres;
	protected float MontoNeto;
	protected float MontoOtrasDeducciones;
	protected float MontoDescPromocion;
	protected float PorcDescOcasional;
	protected float PorcDescPromo;
	protected java.lang.String Numero;
	protected long Fecha;
	protected long FechaVence;
	protected long FechaAplicaDescPP;
	protected float SubTotal;
	protected float Impuesto;
	protected float Totalfactura;
	protected float Saldofactura;
	protected float InteresMoratorio;
	protected float SaldoTotal;
	protected float MontoImpuestoExento;
	protected float MontoDescEspecificoCalc;
	
	public ReciboDetFactura() {
		super();
	}

	public ReciboDetFactura(long id, long objFacturaID, float monto,
			boolean esAbono, float montoDescEspecifico,
			float montoDescOcasional, float montoRetencion,
			float montoImpuesto, float montoInteres, float montoNeto,
			float montoOtrasDeducciones, float montoDescPromocion,
			float porcDescOcasional, float porcDescPromo, String numero,
			long fecha, long fechaVence, long fechaAplicaDescPP,
			float subTotal, float impuesto, float totalfactura,
			float saldofactura, float interesMoratorio, float saldoTotal,
			float montoImpuestoExento, float montoDescEspecificoCalc) {
		super();
		this.Id = id;
		this.ObjFacturaID = objFacturaID;
		this.Monto = monto;
		this.EsAbono = esAbono;
		this.MontoDescEspecifico = montoDescEspecifico;
		this.MontoDescOcasional = montoDescOcasional;
		this.MontoRetencion = montoRetencion;
		this.MontoImpuesto = montoImpuesto;
		this.MontoInteres = montoInteres;
		this.MontoNeto = montoNeto;
		this.MontoOtrasDeducciones = montoOtrasDeducciones;
		this.MontoDescPromocion = montoDescPromocion;
		this.PorcDescOcasional = porcDescOcasional;
		this.PorcDescPromo = porcDescPromo;
		this.Numero = numero;
		this.Fecha = fecha;
		this.FechaVence = fechaVence;
		this.FechaAplicaDescPP = fechaAplicaDescPP;
		this.SubTotal = subTotal;
		this.Impuesto = impuesto;
		this.Totalfactura = totalfactura;
		this.Saldofactura = saldofactura;
		this.InteresMoratorio = interesMoratorio;
		this.SaldoTotal = saldoTotal;
		this.MontoImpuestoExento = montoImpuestoExento;
		this.MontoDescEspecificoCalc = montoDescEspecificoCalc;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public long getObjFacturaID() {
		return ObjFacturaID;
	}

	public void setObjFacturaID(long objFacturaID) {
		this.ObjFacturaID = objFacturaID;
	}
	
	public long getObjReciboID() {
		return ObjReciboID;
	}

	public void setObjReciboID(long objReciboID) {
		this.ObjReciboID = objReciboID;
	}

	public float getMonto() {
		return Monto;
	}

	public void setMonto(float monto) {
		this.Monto = monto;
	}

	public boolean isEsAbono() {
		return EsAbono;
	}

	public void setEsAbono(boolean esAbono) {
		this.EsAbono = esAbono;
	}

	public float getMontoDescEspecifico() {
		return MontoDescEspecifico;
	}

	public void setMontoDescEspecifico(float montoDescEspecifico) {
		this.MontoDescEspecifico = montoDescEspecifico;
	}

	public float getMontoDescOcasional() {
		return MontoDescOcasional;
	}

	public void setMontoDescOcasional(float montoDescOcasional) {
		this.MontoDescOcasional = montoDescOcasional;
	}

	public float getMontoRetencion() {
		return MontoRetencion;
	}

	public void setMontoRetencion(float montoRetencion) {
		this.MontoRetencion = montoRetencion;
	}

	public float getMontoImpuesto() {
		return MontoImpuesto;
	}

	public void setMontoImpuesto(float montoImpuesto) {
		this.MontoImpuesto = montoImpuesto;
	}

	public float getMontoInteres() {
		return MontoInteres;
	}

	public void setMontoInteres(float montoInteres) {
		this.MontoInteres = montoInteres;
	}

	public float getMontoNeto() {
		return MontoNeto;
	}

	public void setMontoNeto(float montoNeto) {
		this.MontoNeto = montoNeto;
	}

	public float getMontoOtrasDeducciones() {
		return MontoOtrasDeducciones;
	}

	public void setMontoOtrasDeducciones(float montoOtrasDeducciones) {
		this.MontoOtrasDeducciones = montoOtrasDeducciones;
	}

	public float getMontoDescPromocion() {
		return MontoDescPromocion;
	}

	public void setMontoDescPromocion(float montoDescPromocion) {
		this.MontoDescPromocion = montoDescPromocion;
	}

	public float getPorcDescOcasional() {
		return PorcDescOcasional;
	}

	public void setPorcDescOcasional(float porcDescOcasional) {
		this.PorcDescOcasional = porcDescOcasional;
	}

	public float getPorcDescPromo() {
		return PorcDescPromo;
	}

	public void setPorcDescPromo(float porcDescPromo) {
		this.PorcDescPromo = porcDescPromo;
	}

	public java.lang.String getNumero() {
		return Numero;
	}

	public void setNumero(java.lang.String numero) {
		this.Numero = numero;
	}

	public long getFecha() {
		return Fecha;
	}

	public void setFecha(long fecha) {
		this.Fecha = fecha;
	}

	public long getFechaVence() {
		return FechaVence;
	}

	public void setFechaVence(long fechaVence) {
		this.FechaVence = fechaVence;
	}

	public long getFechaAplicaDescPP() {
		return FechaAplicaDescPP;
	}

	public void setFechaAplicaDescPP(long fechaAplicaDescPP) {
		this.FechaAplicaDescPP = fechaAplicaDescPP;
	}

	public float getSubTotal() {
		return SubTotal;
	}

	public void setSubTotal(float subTotal) {
		this.SubTotal = subTotal;
	}

	public float getImpuesto() {
		return Impuesto;
	}

	public void setImpuesto(float impuesto) {
		this.Impuesto = impuesto;
	}

	public float getTotalfactura() {
		return Totalfactura;
	}

	public void setTotalFactura(float totalfactura) {
		this.Totalfactura = totalfactura;
	}

	public float getSaldofactura() {
		return Saldofactura;
	}

	public void setSaldoFactura(float saldofactura) {
		this.Saldofactura = saldofactura;
	}

	public float getInteresMoratorio() {
		return InteresMoratorio;
	}

	public void setInteresMoratorio(float interesMoratorio) {
		this.InteresMoratorio = interesMoratorio;
	}

	public float getSaldoTotal() {
		return SaldoTotal;
	}

	public void setSaldoTotal(float saldoTotal) {
		this.SaldoTotal = saldoTotal;
	}

	public float getMontoImpuestoExento() {
		return MontoImpuestoExento;
	}

	public void setMontoImpuestoExento(float montoImpuestoExento) {
		this.MontoImpuestoExento = montoImpuestoExento;
	}

	public float getMontoDescEspecificoCalc() {
		return MontoDescEspecificoCalc;
	}

	public void setMontoDescEspecificoCalc(float montoDescEspecificoCalc) {
		this.MontoDescEspecificoCalc = montoDescEspecificoCalc;
	}

	@Override
	public long id() {
		// TODO Auto-generated method stub
		return getId();
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return "Factura";
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public long getFechaDocumento() {
		// TODO Auto-generated method stub
		return getFecha();
	}

	@Override
	public float getSaldo() {
		// TODO Auto-generated method stub
		return getSaldofactura();
	}

	@Override
	public float getRetencion() {
		// TODO Auto-generated method stub
		return getMontoRetencion();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public ReciboDetFactura(Parcel parcel){ 	   
 	   readFromParcel(parcel);
	}
 
	private void readFromParcel(Parcel parcel) {
		this.Id = parcel.readLong();
		this.ObjFacturaID = parcel.readLong();
		this.ObjReciboID = parcel.readLong();
		this.Monto = parcel.readFloat();
		this.EsAbono = parcel.readInt() == 1;
		this.MontoDescEspecifico = parcel.readFloat();
		this.MontoDescOcasional = parcel.readFloat();
		this.MontoRetencion = parcel.readFloat();
		this.MontoImpuesto = parcel.readFloat();
		this.MontoInteres = parcel.readFloat();
		this.MontoNeto = parcel.readFloat();
		this.MontoOtrasDeducciones = parcel.readFloat();
		this.MontoDescPromocion = parcel.readFloat();
		this.PorcDescOcasional = parcel.readFloat();
		this.PorcDescPromo = parcel.readFloat();
		this.Numero = parcel.readString();
		this.Fecha = parcel.readLong();
		this.FechaVence = parcel.readLong();
		this.FechaAplicaDescPP = parcel.readLong();
		this.SubTotal = parcel.readFloat();
		this.Impuesto = parcel.readFloat();
		this.Totalfactura = parcel.readFloat();
		this.Saldofactura = parcel.readFloat();
		this.InteresMoratorio = parcel.readFloat();
		this.SaldoTotal = parcel.readFloat();
		this.MontoImpuestoExento = parcel.readFloat();
		this.MontoDescEspecificoCalc = parcel.readFloat();
	}

	public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() {	
	     public ReciboDetFactura createFromParcel(Parcel parcel) {
	          return new ReciboDetFactura(parcel);
	     }
	
	     public ReciboDetFactura[] newArray(int size) {
	          return new ReciboDetFactura[size];
	     }
	};

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(  Id );
		parcel.writeLong(  ObjFacturaID );
		parcel.writeLong(  ObjReciboID );
		parcel.writeFloat(  Monto );
		parcel.writeInt( EsAbono ? 1 : 0 );
		parcel.writeFloat(  MontoDescEspecifico );
		parcel.writeFloat(  MontoDescOcasional );
		parcel.writeFloat(  MontoRetencion );
		parcel.writeFloat(  MontoImpuesto );
		parcel.writeFloat(  MontoInteres );
		parcel.writeFloat(  MontoNeto );
		parcel.writeFloat(  MontoOtrasDeducciones );
		parcel.writeFloat(  MontoDescPromocion );
		parcel.writeFloat(  PorcDescOcasional );
		parcel.writeFloat(  PorcDescPromo );
		parcel.writeString( Numero );
		parcel.writeLong(  Fecha );
		parcel.writeLong(  FechaVence );
		parcel.writeLong(  FechaAplicaDescPP );
		parcel.writeFloat(  SubTotal );
		parcel.writeFloat(  Impuesto );
		parcel.writeFloat(  Totalfactura );
		parcel.writeFloat(  Saldofactura );
		parcel.writeFloat(  InteresMoratorio );
		parcel.writeFloat(  SaldoTotal );
		parcel.writeFloat(  MontoImpuestoExento );
		parcel.writeFloat(  MontoDescEspecificoCalc );
		
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 27;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
