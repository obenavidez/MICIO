package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Recibo implements Parcelable {

	protected long Id;
	protected int Numero;
	protected long Fecha;
	protected java.lang.String Notas;
	protected float TotalRecibo;
	protected float TotalFacturas;
	protected float TotalND;
	protected float TotalInteres;
	protected float SubTotal;
	protected float TotalDesc;
	protected float TotalRetenido;
	protected float TotalOtrasDed;
	protected float TotalNC;
	protected int Referencia;
	protected long ObjClienteID;
	protected long ObjSucursalID;
	protected java.lang.String NombreCliente;
	protected long ObjColectorID;
	protected boolean AplicaDescOca;
	protected java.lang.String ClaveAutorizaDescOca;
	protected float PorcDescOcaColector;
	protected long ObjEstadoID;
	protected java.lang.String CodEstado;
	protected java.lang.String DescEstado;
	protected float TotalDescOca;
	protected float TotalDescPromo;
	protected float TotalDescPP;
	protected float TotalImpuestoProporcional;
	protected float TotalImpuestoExonerado;
	protected boolean Exento;
	protected java.lang.String AutorizacionDGI;
	protected Cliente cliente;
	
	protected ArrayList<ReciboDetFactura> facturasRecibo = new ArrayList<ReciboDetFactura>(); 
	protected ArrayList<ReciboDetNC> notasCreditoRecibo = new ArrayList<ReciboDetNC>();
	protected ArrayList<ReciboDetND> notasDebitoRecibo = new ArrayList<ReciboDetND>();
	protected ArrayList<ReciboDetFormaPago> formasPagoRecibo = new ArrayList<ReciboDetFormaPago>();
	
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
		this.Id = id;
		this.Numero = numero;
		this.Fecha = fecha;
		this.Notas = notas;
		this.TotalRecibo = totalRecibo;
		this.TotalFacturas = totalFacturas;
		this.TotalND = totalND;
		this.TotalInteres = totalInteres;
		this.SubTotal = subTotal;
		this.TotalDesc = totalDesc;
		this.TotalRetenido = totalRetenido;
		this.TotalOtrasDed = totalOtrasDed;
		this.TotalNC = totalNC;
		this.Referencia = referencia;
		this.ObjClienteID = objClienteID;
		this.ObjSucursalID = objSucursalID;
		this.NombreCliente = nombreCliente;
		this.ObjColectorID = objColectorID;
		this.AplicaDescOca = aplicaDescOca;
		this.ClaveAutorizaDescOca = claveAutorizaDescOca;
		this.PorcDescOcaColector = porcDescOcaColector;
		this.ObjEstadoID = objEstadoID;
		this.CodEstado = codEstado;
		this.DescEstado = descEstado;
		this.TotalDescOca = totalDescOca;
		this.TotalDescPromo = totalDescPromo;
		this.TotalDescPP = totalDescPP;
		this.TotalImpuestoProporcional = totalImpuestoProporcional;
		this.TotalImpuestoExonerado = totalImpuestoExonerado;
		this.Exento = exento;
		this.AutorizacionDGI = autorizacionDGI;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public int getNumero() {
		return Numero;
	}

	public void setNumero(int numero) {
		this.Numero = numero;
	}

	public long getFecha() {
		return Fecha;
	}

	public void setFecha(long fecha) {
		this.Fecha = fecha;
	}

	public java.lang.String getNotas() {
		return Notas;
	}

	public void setNotas(java.lang.String notas) {
		this.Notas = notas;
	}

	public float getTotalRecibo() {
		return TotalRecibo;
	}

	public void setTotalRecibo(float totalRecibo) {
		this.TotalRecibo = totalRecibo;
	}

	public float getTotalFacturas() {
		return TotalFacturas;
	}

	public void setTotalFacturas(float totalFacturas) {
		this.TotalFacturas = totalFacturas;
	}

	public float getTotalND() {
		return TotalND;
	}

	public void setTotalND(float totalND) {
		this.TotalND = totalND;
	}

	public float getTotalInteres() {
		return TotalInteres;
	}

	public void setTotalInteres(float totalInteres) {
		this.TotalInteres = totalInteres;
	}

	public float getSubTotal() {
		return SubTotal;
	}

	public void setSubTotal(float subTotal) {
		this.SubTotal = subTotal;
	}

	public float getTotalDesc() {
		return TotalDesc;
	}

	public void setTotalDesc(float totalDesc) {
		this.TotalDesc = totalDesc;
	}

	public float getTotalRetenido() {
		return TotalRetenido;
	}

	public void setTotalRetenido(float totalRetenido) {
		this.TotalRetenido = totalRetenido;
	}

	public float getTotalOtrasDed() {
		return TotalOtrasDed;
	}

	public void setTotalOtrasDed(float totalOtrasDed) {
		this.TotalOtrasDed = totalOtrasDed;
	}

	public float getTotalNC() {
		return TotalNC;
	}

	public void setTotalNC(float totalNC) {
		this.TotalNC = totalNC;
	}

	public int getReferencia() {
		return Referencia;
	}

	public void setReferencia(int referencia) {
		this.Referencia = referencia;
	}

	public long getObjClienteID() {
		return ObjClienteID;
	}

	public void setObjClienteID(long objClienteID) {
		this.ObjClienteID = objClienteID;
	}

	public long getObjSucursalID() {
		return ObjSucursalID;
	}

	public void setObjSucursalID(long objSucursalID) {
		this.ObjSucursalID = objSucursalID;
	}

	public java.lang.String getNombreCliente() {
		return NombreCliente;
	}

	public void setNombreCliente(java.lang.String nombreCliente) {
		this.NombreCliente = nombreCliente;
	}

	public long getObjColectorID() {
		return ObjColectorID;
	}

	public void setObjColectorID(long objColectorID) {
		this.ObjColectorID = objColectorID;
	}

	public boolean isAplicaDescOca() {
		return AplicaDescOca;
	}

	public void setAplicaDescOca(boolean aplicaDescOca) {
		this.AplicaDescOca = aplicaDescOca;
	}

	public java.lang.String getClaveAutorizaDescOca() {
		return ClaveAutorizaDescOca;
	}

	public void setClaveAutorizaDescOca(java.lang.String claveAutorizaDescOca) {
		this.ClaveAutorizaDescOca = claveAutorizaDescOca;
	}

	public float getPorcDescOcaColector() {
		return PorcDescOcaColector;
	}

	public void setPorcDescOcaColector(float porcDescOcaColector) {
		this.PorcDescOcaColector = porcDescOcaColector;
	}

	public long getObjEstadoID() {
		return ObjEstadoID;
	}

	public void setObjEstadoID(long objEstadoID) {
		this.ObjEstadoID = objEstadoID;
	}

	public java.lang.String getCodEstado() {
		return CodEstado;
	}

	public void setCodEstado(java.lang.String codEstado) {
		this.CodEstado = codEstado;
	}

	public java.lang.String getDescEstado() {
		return DescEstado;
	}

	public void setDescEstado(java.lang.String descEstado) {
		this.DescEstado = descEstado;
	}

	public float getTotalDescOca() {
		return TotalDescOca;
	}

	public void setTotalDescOca(float totalDescOca) {
		this.TotalDescOca = totalDescOca;
	}

	public float getTotalDescPromo() {
		return TotalDescPromo;
	}

	public void setTotalDescPromo(float totalDescPromo) {
		this.TotalDescPromo = totalDescPromo;
	}

	public float getTotalDescPP() {
		return TotalDescPP;
	}

	public void setTotalDescPP(float totalDescPP) {
		this.TotalDescPP = totalDescPP;
	}

	public float getTotalImpuestoProporcional() {
		return TotalImpuestoProporcional;
	}

	public void setTotalImpuestoProporcional(float totalImpuestoProporcional) {
		this.TotalImpuestoProporcional = totalImpuestoProporcional;
	}

	public float getTotalImpuestoExonerado() {
		return TotalImpuestoExonerado;
	}

	public void setTotalImpuestoExonerado(float totalImpuestoExonerado) {
		this.TotalImpuestoExonerado = totalImpuestoExonerado;
	}

	public boolean isExento() {
		return Exento;
	}

	public void setExento(boolean exento) {
		this.Exento = exento;
	}

	public java.lang.String getAutorizacionDGI() {
		return AutorizacionDGI;
	}

	public void setAutorizacionDGI(java.lang.String autorizacionDGI) {
		this.AutorizacionDGI = autorizacionDGI;
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
	
	public ArrayList<ReciboDetFormaPago> getFormasPagoRecibo() {
		return formasPagoRecibo;
	}

	public void setFormasPagoRecibo(ArrayList<ReciboDetFormaPago> formasPagoRecibo) {
		this.formasPagoRecibo = formasPagoRecibo;
	}

	public Recibo(Parcel parcel){ 	   
 	   readFromParcel(parcel);
	}

	private void readFromParcel(Parcel parcel) {
		this.Id = parcel.readLong(  );
		this.Numero = parcel.readInt(  );
		this.Fecha = parcel.readLong(  );
		this.Notas = parcel.readString(  );
		this.TotalRecibo = parcel.readFloat(  );
		this.TotalFacturas = parcel.readFloat(  );
		this.TotalND = parcel.readFloat(  );
		this.TotalInteres = parcel.readFloat(  );
		this.SubTotal = parcel.readFloat(  );
		this.TotalDesc = parcel.readFloat(  );
		this.TotalRetenido = parcel.readFloat(  );
		this.TotalOtrasDed = parcel.readFloat(  );
		this.TotalNC = parcel.readFloat(  );
		this.Referencia = parcel.readInt(  );
		this.ObjClienteID = parcel.readLong(  );
		this.ObjSucursalID = parcel.readLong(  );
		this.NombreCliente = parcel.readString(  );
		this.ObjColectorID = parcel.readLong(  );
		this.AplicaDescOca = parcel.readInt(  ) == 1;
		this.ClaveAutorizaDescOca = parcel.readString(  );
		this.PorcDescOcaColector = parcel.readFloat(  );
		this.ObjEstadoID = parcel.readLong(  );
		this.CodEstado = parcel.readString(  );
		this.DescEstado = parcel.readString(  );
		this.TotalDescOca = parcel.readFloat(  );
		this.TotalDescPromo = parcel.readFloat(  );
		this.TotalDescPP = parcel.readFloat(  );
		this.TotalImpuestoProporcional = parcel.readFloat(  );
		this.TotalImpuestoExonerado = parcel.readFloat(  );
		this.Exento = parcel.readInt(  ) == 1;
		this.AutorizacionDGI = parcel.readString(  );
		
		Parcelable[] parcelableArray = parcel.readParcelableArray(ReciboDetFactura.class.getClassLoader()); 
		if (parcelableArray != null) {
			facturasRecibo.clear();
			Object [] list = Arrays.copyOf(parcelableArray, parcelableArray.length, ReciboDetFactura[].class);
			for(Object obj: list){
				facturasRecibo.add( (ReciboDetFactura) obj);
			}
		}
		
		parcelableArray = parcel.readParcelableArray(ReciboDetND.class.getClassLoader()); 
		if (parcelableArray != null) {
			notasDebitoRecibo.clear();
			Object [] list = Arrays.copyOf(parcelableArray, parcelableArray.length, ReciboDetND[].class);
			for(Object obj: list){
				notasDebitoRecibo.add( (ReciboDetND) obj);
			}
		}
		
		parcelableArray = parcel.readParcelableArray(ReciboDetNC.class.getClassLoader()); 
		if (parcelableArray != null) {
			notasCreditoRecibo.clear();
			Object [] list = Arrays.copyOf(parcelableArray, parcelableArray.length, ReciboDetNC[].class);
			for(Object obj: list){
				notasCreditoRecibo.add( (ReciboDetNC) obj);
			}
		}
	}

	public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() {

        public Recibo createFromParcel(Parcel parcel) {
             return new Recibo(parcel);
        }

        public Recibo[] newArray(int size) {
             return new Recibo[size];
        }
      	 
      	 
   };
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}	

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong( Id );
		parcel.writeInt( Numero );
		parcel.writeLong( Fecha );
		parcel.writeString( Notas );
		parcel.writeFloat( TotalRecibo );
		parcel.writeFloat( TotalFacturas );
		parcel.writeFloat( TotalND );
		parcel.writeFloat( TotalInteres );
		parcel.writeFloat( SubTotal );
		parcel.writeFloat( TotalDesc );
		parcel.writeFloat( TotalRetenido );
		parcel.writeFloat( TotalOtrasDed );
		parcel.writeFloat( TotalNC );
		parcel.writeInt( Referencia );
		parcel.writeLong( ObjClienteID );
		parcel.writeLong( ObjSucursalID );
		parcel.writeString( NombreCliente );
		parcel.writeLong( ObjColectorID );
		parcel.writeInt( AplicaDescOca ? 1 : 0  );
		parcel.writeString( ClaveAutorizaDescOca );
		parcel.writeFloat( PorcDescOcaColector );
		parcel.writeLong( ObjEstadoID );
		parcel.writeString( CodEstado );
		parcel.writeString( DescEstado );
		parcel.writeFloat( TotalDescOca );
		parcel.writeFloat( TotalDescPromo );
		parcel.writeFloat( TotalDescPP );
		parcel.writeFloat( TotalImpuestoProporcional );
		parcel.writeFloat( TotalImpuestoExonerado );
		parcel.writeInt( Exento ? 1 : 0 );
		parcel.writeString( AutorizacionDGI );
		parcel.writeParcelableArray(getArrayFacturas(), flags);
		parcel.writeParcelableArray(getArrayNotasDebito(), flags);
		parcel.writeParcelableArray(getArrayNotasCredito(), flags);		
	}
	
	private ReciboDetFactura [] getArrayFacturas(){
		ReciboDetFactura [] facturas = new ReciboDetFactura[ facturasRecibo.size()]; 
		int index = 0;
		for(ReciboDetFactura factura : facturasRecibo){
			facturas[index++] = factura;
		}
		return facturas;
	}
	
	private ReciboDetND [] getArrayNotasDebito(){
		ReciboDetND [] notasDebito = new ReciboDetND[ notasDebitoRecibo.size()]; 
		int index = 0;
		for(ReciboDetND factura : notasDebitoRecibo){
			notasDebito[index++] = factura;
		}
		return notasDebito;
	}
	
	private ReciboDetNC [] getArrayNotasCredito(){
		ReciboDetNC [] notasCredito = new ReciboDetNC[ notasCreditoRecibo.size()]; 
		int index = 0;
		for(ReciboDetNC factura : notasCreditoRecibo){
			notasCredito[index++] = factura;
		}
		return notasCredito;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
