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
	protected float totalFacturaOrigen; 
	
	public float getTotalFacturaOrigen() {
		return totalFacturaOrigen;
	}

	public void setTotalFacturaOrigen(float totalFacturaOrigen) {
		this.totalFacturaOrigen = totalFacturaOrigen;
	}

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

	@Override
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

	@Override
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
	     @Override
		public ReciboDetFactura createFromParcel(Parcel parcel) {
	          return new ReciboDetFactura(parcel);
	     }
	
	     @Override
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
	public Object getProperty(int _index) { 
		switch(_index)  
		{
			case 0: return new Long(Id);
			case 1: return new Long(ObjFacturaID);
			/*case 2: return new Long(ObjReciboID);*/
			case 2: return Monto;
			case 3: return (EsAbono)?1:0;
			case 4: return MontoDescEspecifico;
			case 5: return MontoDescOcasional;
			case 6: return MontoRetencion;
			case 7: return MontoImpuesto;
			case 8: return MontoInteres;
			case 9:return MontoNeto;
			case 10:return MontoOtrasDeducciones;
			case 11:return MontoDescPromocion;
			case 12:return PorcDescOcasional;
			case 13:return PorcDescPromo;
			case 14:return Numero;
			case 15: return new Long(Fecha);
			case 16: return new Long(FechaVence);
			case 17: return new Long(FechaAplicaDescPP);
			case 18: return SubTotal;
			case 19: return Impuesto;
			case 20: return Totalfactura;
			case 21: return Saldofactura;
			case 22:return InteresMoratorio;
			case 23:return SaldoTotal;
			case 24:return MontoImpuestoExento;
			case 25:return MontoDescEspecificoCalc; 
		} 		
		return null;
	}

	@Override
	public int getPropertyCount() { 
		return 26;
	}

	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) { 
		switch(_index)  
		{
	        case 0:
	            _info.name = "Id";
	            _info.type = Long.class; 
	            break;
	        case 1:
	            _info.name = "ObjFacturaID";
	            _info.type = Long.class; 
	            break;
	        /*case 2:
	            _info.name = "ObjReciboID";
	            _info.type = Long.class; 
	            break;*/
	        case 2:
	            _info.name = "Monto";
	            _info.type = Float.class; 
	            break;
	        case 3:
	            _info.name = "EsAbono";
	            _info.type = Boolean.class; 
	            break;
	        case 4:
	            _info.name = "MontoDescEspecifico";
	            _info.type = Float.class; 
	            break;
	        case 5:
	            _info.name = "MontoDescOcasional";
	            _info.type = Float.class; 
	            break;
	        case 6:
	            _info.name = "MontoRetencion";
	            _info.type = Float.class; 
	            break;
	        case 7:
	            _info.name = "MontoImpuesto";
	            _info.type = Float.class; 
	            break;
	        case 8:
	            _info.name = "MontoInteres";
	            _info.type = Float.class; 
	            break;
	        case 9:
	            _info.name = "MontoNeto";
	            _info.type = Float.class; 
	            break;
	        case 10:
	            _info.name = "MontoOtrasDeducciones";
	            _info.type = Float.class; 
	            break;
	        case 11:
	            _info.name = "MontoDescPromocion";
	            _info.type = Float.class; 
	            break;
	        case 12:
	            _info.name = "PorcDescOcasional";
	            _info.type = Float.class; 
	            break;
	        case 13:
	            _info.name = "PorcDescPromo";
	            _info.type = Float.class; 
	            break;
	        case 14:
	            _info.name = "Numero";
	            _info.type = String.class; 
	            break;
	        case 15:
	            _info.name = "Fecha";
	            _info.type = Long.class; 
	            break;
	        case 16:
	            _info.name = "FechaVence";
	            _info.type = Long.class; 
	            break;
	        case 17:
	            _info.name = "FechaAplicaDescPP";
	            _info.type = Long.class; 
	            break;
	        case 18:
	            _info.name = "SubTotal";
	            _info.type = Float.class; 
	            break;
	        case 19:
	            _info.name = "Impuesto";
	            _info.type = Float.class; 
	            break;
	        case 20:
	            _info.name = "Totalfactura";
	            _info.type = Float.class; 
	            break;
	        case 21:
	            _info.name = "Saldofactura";
	            _info.type = Float.class; 
	            break;
	        case 22:
	            _info.name = "InteresMoratorio";
	            _info.type = Float.class; 
	            break;
	        case 23:
	            _info.name = "SaldoTotal";
	            _info.type = Float.class; 
	            break;
	        case 24:
	            _info.name = "MontoImpuestoExento";
	            _info.type = Float.class; 
	            break;
	        case 25:
	            _info.name = "MontoDescEspecificoCalc";
	            _info.type = Float.class; 
	            break;
		}
	}

	@Override
	public void setProperty(int _index, Object obj) {
        switch(_index)  
        {
	        case 0: Id = Long.parseLong(obj.toString()); break;
	        case 1: ObjFacturaID = Long.parseLong(obj.toString()); break;
	        case 2: ObjReciboID = Long.parseLong(obj.toString()); break;
	        case 3: Monto = Float.parseFloat(obj.toString()); break;
	        case 4: EsAbono = "true".equals(obj.toString()); break;
	        case 5: MontoDescEspecifico = Float.parseFloat(obj.toString()); break;
	        case 6: MontoDescOcasional = Float.parseFloat(obj.toString()); break;
	        case 7: MontoRetencion = Float.parseFloat(obj.toString()); break;
	        case 8: MontoImpuesto =Float.parseFloat(obj.toString()); break;
	        case 9: MontoInteres =Float.parseFloat(obj.toString()); break;
	        case 10: MontoNeto =Float.parseFloat(obj.toString()); break;
	        case 11: MontoOtrasDeducciones = Float.parseFloat(obj.toString()); break;
	        case 12: MontoDescPromocion = Float.parseFloat(obj.toString()); break;
	        case 13: PorcDescOcasional =Float.parseFloat(obj.toString()); break;
	        case 14: PorcDescPromo = Float.parseFloat(obj.toString()); break;
	        case 15: Numero =  (obj.toString()); break;
	        case 16: Fecha = Long.parseLong(obj.toString()); break;
	        case 17: FechaVence = Long.parseLong(obj.toString()); break;
	        case 18: FechaAplicaDescPP = Long.parseLong(obj.toString()); break;       
	        case 19: SubTotal = Float.parseFloat(obj.toString()); break;  
	        case 20: Impuesto = Float.parseFloat(obj.toString()); break;
	        case 21: Totalfactura = Float.parseFloat(obj.toString()); break;
	        case 22: Saldofactura = Float.parseFloat(obj.toString()); break; 
	        case 23: InteresMoratorio = Float.parseFloat(obj.toString()); break;
	        case 24: SaldoTotal = Float.parseFloat(obj.toString()); break;
	        case 25: MontoImpuestoExento = Float.parseFloat(obj.toString()); break;
	        case 26: MontoDescEspecificoCalc = Float.parseFloat(obj.toString()); break;
        }
	}

	@Override
	public float getDescuento() {
		// TODO Auto-generated method stub
		return 0;
	}

}
