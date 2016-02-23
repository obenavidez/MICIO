package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.panzyma.nm.auxiliar.NMConfig;
import android.os.Parcel;
import android.os.Parcelable;

public class ReciboColector implements KvmSerializable,Parcelable {

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
	protected ArrayList<ReciboDetFactura> DetalleFacturas = new ArrayList<ReciboDetFactura>(); 
	protected ArrayList<ReciboDetND> DetalleNotasDebito = new ArrayList<ReciboDetND>();
	protected ArrayList<ReciboDetNC> DetalleNotasCredito = new ArrayList<ReciboDetNC>(); 
	protected ArrayList<ReciboDetFormaPago> DetalleFormasPago = new ArrayList<ReciboDetFormaPago>(); 
	
	protected ReciboColector oldata;
	
	public ReciboColector() {
		super();		
	}
	
	public ReciboColector(ReciboColector rc) {
		super();
		this.Id = rc.getId();
		this.Numero =rc.getNumero();
		this.Fecha = rc.getFecha();
		this.Notas = rc.getNotas();
		this.TotalRecibo =rc.getTotalRecibo();
		this.TotalFacturas = rc.getTotalFacturas();
		this.TotalND = rc.getTotalND();
		this.TotalInteres = rc.getTotalInteres();
		this.SubTotal = rc.getSubTotal();
		this.TotalDesc = rc.getTotalDesc();
		this.TotalRetenido = rc.getTotalRetenido();
		this.TotalOtrasDed =rc.getTotalOtrasDed();
		this.TotalNC = rc.getTotalNC();
		this.Referencia = rc.getReferencia();
		this.ObjClienteID = rc.getObjClienteID();
		this.ObjSucursalID = rc.getObjSucursalID();
		this.NombreCliente = rc.getNombreCliente();
		this.ObjColectorID = rc.getObjColectorID();
		this.AplicaDescOca = rc.isAplicaDescOca();
		this.ClaveAutorizaDescOca = rc.getClaveAutorizaDescOca();
		this.PorcDescOcaColector = rc.getPorcDescOcaColector();
		this.ObjEstadoID = rc.getObjEstadoID();
		this.CodEstado = rc.getCodEstado();
		this.DescEstado = rc.getDescEstado();
		this.TotalDescOca = rc.getTotalDescOca();
		this.TotalDescPromo = rc.getTotalDescPromo();
		this.TotalDescPP = rc.getTotalDescPP();
		this.TotalImpuestoProporcional = rc.getTotalImpuestoProporcional();
		this.TotalImpuestoExonerado = rc.getTotalImpuestoExonerado();
		this.Exento = rc.isExento();
		this.AutorizacionDGI = rc.getAutorizacionDGI();
		this.DetalleFacturas=rc.getFacturasRecibo();
		this.DetalleNotasCredito=rc.getNotasCreditoRecibo();
		this.DetalleNotasDebito=rc.getNotasDebitoRecibo();
		this.DetalleFormasPago=rc.getFormasPagoRecibo(); 
	}

	public ReciboColector(long id, int numero, long fecha, String notas,
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
	
	public void setOldData(ReciboColector rc)
	{
		oldata=new ReciboColector(rc);
	}

	public ReciboColector getOldData()
	{
		return oldata;
	}
	
	public boolean hasModified(Object obj) 
    { 
		if(obj==null && this!=null)
			return true; 
		if (getClass() != obj.getClass())
			return false;
		ReciboColector other = (ReciboColector) obj; 
		
		if(getObjSucursalID()!=other.getObjSucursalID())
			return true;
		if(getTotalRecibo()!=other.getTotalRecibo())
			return true; 
		
		ArrayList<ReciboDetFactura> df =other.getFacturasRecibo(); 
		ArrayList<ReciboDetND> dnd =other.getNotasDebitoRecibo();
		ArrayList<ReciboDetNC> dnc = other.getNotasCreditoRecibo(); 
		ArrayList<ReciboDetFormaPago> dnp = other.getFormasPagoRecibo(); 
		
		ArrayList<ReciboDetFactura> df2=getFacturasRecibo(); 
		ArrayList<ReciboDetND> dnd2 =getNotasDebitoRecibo();
		ArrayList<ReciboDetNC> dnc2 = getNotasCreditoRecibo(); 
		ArrayList<ReciboDetFormaPago> dnp2 =getFormasPagoRecibo();
		
		
		if(df==null && df2!=null)
			return true;		
		if(dnd==null && dnd2!=null)
			return true;
		if(dnc==null && dnc2!=null)
			return true;
		if(dnp==null && dnp2!=null)
			return true;		
		if(df.size()!=df2.size())
			return true;	
		if(dnd.size()!=dnd2.size())
			return true;	
		if(dnc.size()!=dnc2.size())
			return true;	
		if(dnp.size()!=dnp.size())
			return true;	
		if(!(getNotas().trim().equals(other.getNotas().trim())))
			return true;
		return false;
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
		return DetalleFacturas;
	}

	public void setFacturasRecibo(ArrayList<ReciboDetFactura> facturasRecibo) {
		this.DetalleFacturas = facturasRecibo;
	}

	public ArrayList<ReciboDetNC> getNotasCreditoRecibo() {
		return DetalleNotasCredito;
	}

	public void setNotasCreditoRecibo(ArrayList<ReciboDetNC> notasCreditoRecibo) {
		this.DetalleNotasCredito = notasCreditoRecibo;
	}

	public ArrayList<ReciboDetND> getNotasDebitoRecibo() {
		return DetalleNotasDebito;
	}

	public void setNotasDebitoRecibo(ArrayList<ReciboDetND> notasDebitoRecibo) {
		this.DetalleNotasDebito = notasDebitoRecibo;
	} 

	public ReciboColector(Parcel parcel){ 	   
 	   readFromParcel(parcel);
	}
	
	public ArrayList<ReciboDetFormaPago> getFormasPagoRecibo() {
		return DetalleFormasPago;
	}

	public void setFormasPagoRecibo(ArrayList<ReciboDetFormaPago> formasPagoRecibo) {
		this.DetalleFormasPago = formasPagoRecibo;
	}
	public float getFormasPagoMonto(){
		float montoformapago =0;
		for (ReciboDetFormaPago detalle : this.DetalleFormasPago) {
			montoformapago+= detalle.Monto;
		}
		return montoformapago;
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
		this.cliente=parcel.readParcelable(Cliente.class.getClassLoader());
		
		Parcelable[] parcelableArray = parcel.readParcelableArray(ReciboDetFactura.class.getClassLoader()); 
		if (parcelableArray != null) {
			DetalleFacturas.clear();
			Object [] list = Arrays.copyOf(parcelableArray, parcelableArray.length, ReciboDetFactura[].class);
			for(Object obj: list){
				DetalleFacturas.add( (ReciboDetFactura) obj);
			}
		}
		
		parcelableArray = parcel.readParcelableArray(ReciboDetND.class.getClassLoader()); 
		if (parcelableArray != null) {
			DetalleNotasDebito.clear();
			Object [] list = Arrays.copyOf(parcelableArray, parcelableArray.length, ReciboDetND[].class);
			for(Object obj: list){
				DetalleNotasDebito.add( (ReciboDetND) obj);
			}
		}
		
		parcelableArray = parcel.readParcelableArray(ReciboDetNC.class.getClassLoader()); 
		if (parcelableArray != null) {
			DetalleNotasCredito.clear();
			Object [] list = Arrays.copyOf(parcelableArray, parcelableArray.length, ReciboDetNC[].class);
			for(Object obj: list){
				DetalleNotasCredito.add( (ReciboDetNC) obj);
			}
		}
		
		parcelableArray = parcel.readParcelableArray(ReciboDetFormaPago.class.getClassLoader()); 
		if (parcelableArray != null) 
		{
			DetalleFormasPago.clear();
			Object [] list = Arrays.copyOf(parcelableArray, parcelableArray.length, ReciboDetFormaPago[].class);
			for(Object obj: list){
				DetalleFormasPago.add( (ReciboDetFormaPago) obj);
			}
		}
	}

	public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() 
	{

        @Override
		public ReciboColector createFromParcel(Parcel parcel) {
             return new ReciboColector(parcel);
        }

        @Override
		public ReciboColector[] newArray(int size) {
             return new ReciboColector[size];
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
		parcel.writeParcelable(getCliente(), flags);
		parcel.writeParcelableArray(getArrayFacturas(), flags);
		parcel.writeParcelableArray(getArrayNotasDebito(), flags);
		parcel.writeParcelableArray(getArrayNotasCredito(), flags);		
		parcel.writeParcelableArray(getArrayFormasPago(), flags);	
	}
	
	private ReciboDetFactura [] getArrayFacturas(){
		ReciboDetFactura [] facturas = new ReciboDetFactura[ DetalleFacturas.size()]; 
		int index = 0;
		for(ReciboDetFactura factura : DetalleFacturas){
			facturas[index++] = factura;
		}
		return facturas;
	}
	
	private ReciboDetND [] getArrayNotasDebito(){
		ReciboDetND [] notasDebito = new ReciboDetND[ DetalleNotasDebito.size()]; 
		int index = 0;
		for(ReciboDetND factura : DetalleNotasDebito){
			notasDebito[index++] = factura;
		}
		return notasDebito;
	}
	
	private ReciboDetNC [] getArrayNotasCredito(){
		ReciboDetNC [] notasCredito = new ReciboDetNC[ DetalleNotasCredito.size()]; 
		int index = 0;
		for(ReciboDetNC factura : DetalleNotasCredito){
			notasCredito[index++] = factura;
		}
		return notasCredito;
	}

	private ReciboDetFormaPago [] getArrayFormasPago(){
		ReciboDetFormaPago [] formaspago = new ReciboDetFormaPago[ DetalleFormasPago.size()]; 
		int index = 0;
		for(ReciboDetFormaPago pago : DetalleFormasPago){
			formaspago[index++] = pago;
		}
		return formaspago;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public Object getProperty(int index) 
	{
		switch(index)  
		{
			case 0:
					if (DetalleFacturas != null && DetalleFacturas.size() > 0) 
					{
						SoapObject _detallefacturas=new SoapObject(NMConfig.NAME_SPACE, "DetalleFacturas");
						for(ReciboDetFactura rf:DetalleFacturas)
			    		{
							SoapObject item=new SoapObject(NMConfig.NAME_SPACE, "ReciboDetFactura");
			    			int cont=rf.getPropertyCount();
			    			for(int i=0;i<cont;i++)
			    			{
			    				PropertyInfo info=new PropertyInfo();
			    				rf.getPropertyInfo(i, null, info);
			    				info.setNamespace(NMConfig.NAME_SPACE);
			    				item.addProperty(info.name,rf.getProperty(i));
			    			}
			    			_detallefacturas.addSoapObject(item);
			    		}
						return _detallefacturas;
					}
				    break;
			case 1:
					if (DetalleNotasDebito != null && DetalleNotasDebito.size() > 0) 
					{
						SoapObject _detallenotasdebito=new SoapObject(NMConfig.NAME_SPACE, "DetalleNotasDebito");
						for(ReciboDetND rnd:DetalleNotasDebito)
			    		{
							SoapObject item=new SoapObject(NMConfig.NAME_SPACE, "ReciboDetND");
			    			int cont=rnd.getPropertyCount();
			    			for(int i=0;i<cont;i++)
			    			{
			    				PropertyInfo info=new PropertyInfo();
			    				rnd.getPropertyInfo(i, null, info);
			    				info.setNamespace(NMConfig.NAME_SPACE);
			    				item.addProperty(info.name,rnd.getProperty(i));
			    			}
			    			_detallenotasdebito.addSoapObject(item);
			    		}
						return _detallenotasdebito;
					}
				    break;
			case 2:
					if (DetalleNotasCredito != null && DetalleNotasCredito.size() > 0) 
					{
						SoapObject _detallenotascredito=new SoapObject(NMConfig.NAME_SPACE, "DetalleNotasCredito");
						for(ReciboDetNC rnc:DetalleNotasCredito)
			    		{
							SoapObject item=new SoapObject(NMConfig.NAME_SPACE, "ReciboDetNC");
			    			int cont=rnc.getPropertyCount();
			    			for(int i=0;i<cont;i++)
			    			{
			    				PropertyInfo info=new PropertyInfo();
			    				rnc.getPropertyInfo(i, null, info);
			    				info.setNamespace(NMConfig.NAME_SPACE);
			    				item.addProperty(info.name,rnc.getProperty(i));
			    			}
			    			_detallenotascredito.addSoapObject(item);
			    		}
						return _detallenotascredito;
					}
				    break;
			case 3:	
					if (DetalleFormasPago != null && DetalleFormasPago.size() > 0) 
					{
						SoapObject _detalleformaspago=new SoapObject(NMConfig.NAME_SPACE, "DetalleFormasPago");
						for(ReciboDetFormaPago rfp:DetalleFormasPago)
			    		{
							SoapObject item=new SoapObject(NMConfig.NAME_SPACE, "ReciboDetFormaPago");
			    			int cont=rfp.getPropertyCount();
			    			for(int i=0;i<cont;i++)
			    			{
			    				PropertyInfo info=new PropertyInfo();
			    				rfp.getPropertyInfo(i, null, info);
			    				info.setNamespace(NMConfig.NAME_SPACE);
			    				item.addProperty(info.name,rfp.getProperty(i));
			    			}
			    			_detalleformaspago.addSoapObject(item);
			    		}
						return _detalleformaspago;
					}
				    break;
        	case 4: return new Long(Id);
        	case 5: return new Integer(Numero);
        	case 6: return new Long(Fecha);
        	case 7: return Notas;
        	case 8: return TotalRecibo;
        	case 9: return TotalFacturas;
        	case 10:return TotalND;
        	case 11:return TotalInteres;
        	case 12:return SubTotal;
        	case 13: return TotalDesc;
        	case 14: return TotalRetenido;
        	case 15: return TotalOtrasDed;
        	case 16:return TotalNC;
        	case 17:return new Integer(Referencia);
        	case 18: return new Long(ObjClienteID);
        	case 19: return new Long(ObjSucursalID);
        	case 20: return NombreCliente;
        	case 21: return new Long(ObjColectorID);
        	case 22: return new Boolean(AplicaDescOca);
        	case 23: return ClaveAutorizaDescOca;
        	case 24: return PorcDescOcaColector;
        	case 25: return new Long(ObjEstadoID);
        	case 26: return CodEstado;
        	case 27: return DescEstado;
        	case 28: return TotalDescOca;
        	case 29: return TotalDescPromo;
        	case 30: return TotalDescPP;
        	case 31: return TotalImpuestoProporcional;
        	case 32: return TotalImpuestoExonerado;
        	case 33: return Exento;
        	case 34: return AutorizacionDGI;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 35;
	}

	@Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
		try 
		   {	   
	    	   switch(_index)  
	    	   { 
		           case 0:
		        	   _info.name = "DetalleFacturas";
		               _info.type= ReciboDetFactura[].class;
//		               if(DetalleFacturas!= null && DetalleFacturas.size() > 0) 
//		               {
//		        		   _info.type = PropertyInfo.VECTOR_CLASS;
//		                   _info.name = "DetalleFacturas";
//		                   PropertyInfo arrayType = new PropertyInfo();
//		                   arrayType.name = "ReciboDetFactura";
//		                   arrayType.type =  ReciboDetFactura[].class;               
//		                   _info.elementType = arrayType;
//		        	   }     
		               break;
		           case 1: 
		        	   _info.name = "DetalleNotasDebito";
		               _info.type= ReciboDetND[].class;
//		               if(DetalleNotasDebito!= null && DetalleNotasDebito.size() > 0) 
//		               {
//		        		   _info.type = PropertyInfo.VECTOR_CLASS;
//		                   _info.name = "DetalleNotasDebito";
//		                   PropertyInfo arrayType = new PropertyInfo();
//		                   arrayType.name = "ReciboDetND";
//		                   arrayType.type =  ReciboDetND[].class;               
//		                   _info.elementType = arrayType;
//		        	   }    
		               break;
		           case 2: 
		        	   _info.name = "DetalleNotasCredito";
		               _info.type= ReciboDetNC[].class;
//		               if(DetalleNotasCredito!= null && DetalleNotasCredito.size() > 0) 
//		               {
//		        		   _info.type = PropertyInfo.VECTOR_CLASS;
//		                   _info.name = "DetalleNotasCredito";
//		                   PropertyInfo arrayType = new PropertyInfo();
//		                   arrayType.name = "ReciboDetNC";
//		                   arrayType.type =  ReciboDetNC[].class;               
//		                   _info.elementType = arrayType;
//		        	   }        	   		               
		               break; 
		           case 3: 		        
		        	   _info.name = "DetalleFormasPago";
		               _info.type= ReciboDetFormaPago[].class;
//		               if(DetalleFormasPago!= null && DetalleFormasPago.size() > 0) 
//		               {
//		        		   _info.type = PropertyInfo.VECTOR_CLASS;
//		                   _info.name = "DetalleFormasPago";
//		                   PropertyInfo arrayType = new PropertyInfo();
//		                   arrayType.name = "ReciboDetFormaPago";
//		                   arrayType.type =  ReciboDetFormaPago[].class;               
//		                   _info.elementType = arrayType;
//		        	   }        	   		               
		               break;
		           case 4:
		               _info.name = "Id";
		               _info.type = Long.class; 
		               break;
		           case 5:
		               _info.name = "Numero";
		               _info.type = Integer.class; 
		               break;
		           case 6:
		               _info.name = "Fecha";
		               _info.type = Long.class; 
		               break;
		           case 7:
		               _info.name = "Notas";
		               _info.type = String.class; 
		               break;
		           case 8:
		               _info.name = "TotalRecibo";
		               _info.type = Float.class; 
		               break;
		           case 9:
		               _info.name = "TotalFacturas";
		               _info.type = Float.class; 
		               break;
		           case 10:
		               _info.name = "TotalND";
		               _info.type = Float.class; 
		               break;
		           case 11:
		               _info.name = "TotalInteres";
		               _info.type = Float.class; 
		               break;
		           case 12:
		               _info.name = "SubTotal";
		               _info.type = Float.class; 
		               break;
		           case 13:
		               _info.name = "TotalDesc";
		               _info.type = Float.class; 
		               break;
		           case 14:
		               _info.name = "TotalRetenido";
		               _info.type = Float.class; 
		               break;
		           case 15:
		               _info.name = "TotalOtrasDed";
		               _info.type = Float.class; 
		               break;
		           case 16:
		               _info.name = "TotalNC";
		               _info.type = Float.class; 
		               break;
		           case 17:
		               _info.name = "Referencia";
		               _info.type = Integer.class; 
		               break;
		           case 18:
		               _info.name = "ObjClienteID";
		               _info.type = Long.class; 
		               break;
		           case 19:
		               _info.name = "ObjSucursalID";
		               _info.type = Long.class; 
		               break;
		           case 20:
		               _info.name = "NombreCliente";
		               _info.type = String.class; 
		               break;
		           case 21:
		               _info.name = "ObjColectorID";
		               _info.type = Long.class; 
		               break;
		           case 22:
		               _info.name = "AplicaDescOca";
		               _info.type = Boolean.class; 
		               break;
		           case 23:
		               _info.name = "ClaveAutorizaDescOca";
		               _info.type = String.class; 
		               break;
		           case 24:
		               _info.name = "PorcDescOcaColector";
		               _info.type = Float.class; 
		               break;
		           case 25:
		               _info.name = "ObjEstadoID";
		               _info.type = Long.class; 
		               break;
		           case 26:
		               _info.name = "CodEstado";
		               _info.type = String.class; 
		               break;
		           case 27:
		               _info.name = "DescEstado";
		               _info.type = String.class; 
		               break;
		           case 28:
		               _info.name = "TotalDescOca";
		               _info.type = Float.class; 
		               break;
		           case 29:
		               _info.name = "TotalDescPromo";
		               _info.type = Float.class; 
		               break;
		           case 30:
		               _info.name = "TotalDescPP";
		               _info.type = Float.class; 
		               break;
		           case 31:
		               _info.name = "TotalImpuestoProporcional";
		               _info.type = Float.class; 
		               break;
		           case 32:
		               _info.name = "TotalImpuestoExonerado";
		               _info.type = Float.class; 
		               break;
		           case 33:
		               _info.name = "Exento";
		               _info.type = Boolean.class; 
		               break;
		           case 34:
		               _info.name = "AutorizacionDGI";
		               _info.type = String.class; 
		               break;
		               
	    	   }
		   }catch(Exception e){
			   
		   }
		
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
