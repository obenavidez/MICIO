package com.panzyma.nm.serviceproxy; 

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
 






import java.util.Arrays;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.panzyma.nm.interfaces.Item;
  
public class Pedido  implements KvmSerializable,Item,Parcelable,Cloneable{  
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
	 
		return super.clone();
	}

	private long Id;
    private int NumeroMovil;
    private int NumeroCentral;
    private java.lang.String Tipo; 
    private int Fecha; 
    private long objClienteID; 
    private java.lang.String NombreCliente;
    private long objSucursalID;
    private java.lang.String NombreSucursal;
    private long objTipoPrecioVentaID;
    private java.lang.String CodTipoPrecio;
    private java.lang.String DescTipoPrecio;
    private long objVendedorID;
    private boolean BonificacionEspecial;
    private java.lang.String BonificacionSolicitada;
    private boolean PrecioEspecial;
    private java.lang.String PrecioSolicitado;
    private boolean PedidoCondicionado;
    private java.lang.String Condicion;
    private float Subtotal;
    private float Descuento;
    private float Impuesto;
    private float Total;
    private long objEstadoID;
    private java.lang.String CodEstado;
    private java.lang.String DescEstado;
    private long objCausaEstadoID;
    private java.lang.String CodCausaEstado;
    private java.lang.String DescCausaEstado;
    private java.lang.String NombreVendedor;
    private DetallePedido[] Detalles;
    private PedidoPromocion[] PromocionesAplicadas;
    private java.lang.String Nota;
    private boolean Exento;
    private java.lang.String AutorizacionDGI;
  
    protected Pedido oldata;
    
    public Pedido() { ;; }
    /**
	 * @param Pedido
	 */
	public Pedido(Pedido _pedido) 
	{
		this.Id = _pedido.getId();
		this.NumeroMovil = _pedido.getNumeroMovil();
		this.NumeroCentral =_pedido.getNumeroCentral();
		this.Tipo = _pedido.getTipo();
		this.Fecha = getFecha();
		this.objClienteID = _pedido.getObjClienteID();
		this.NombreCliente = _pedido.getNombreCliente();
		this.objSucursalID = _pedido.getObjSucursalID();
		this.NombreSucursal = _pedido.getNombreSucursal();
		this.objTipoPrecioVentaID =_pedido.getObjTipoPrecioVentaID();
		this.CodTipoPrecio = _pedido.getCodTipoPrecio();
		this.DescTipoPrecio = _pedido.getDescTipoPrecio();
		this.objVendedorID = _pedido.getObjVendedorID();
		this.BonificacionEspecial =_pedido.getBonificacionEspecial();
		this.BonificacionSolicitada = _pedido.getBonificacionSolicitada();
		this.PrecioEspecial = _pedido.getPrecioEspecial();
		this.PrecioSolicitado = _pedido.getPrecioSolicitado();
		this.PedidoCondicionado = _pedido.getPedidoCondicionado();
		this.Condicion = _pedido.getCondicion();
		this.Subtotal = _pedido.getSubtotal();
		this.Descuento = _pedido.getDescuento();
		this.Impuesto = _pedido.getImpuesto();
		this.Total = _pedido.getTotal();
		this.objEstadoID = _pedido.getObjEstadoID();
		this.CodEstado = _pedido.getCodEstado();
		this.DescEstado = _pedido.getDescEstado();
		this.objCausaEstadoID = _pedido.getObjCausaEstadoID();
		this.CodCausaEstado = _pedido.getCodCausaEstado();
		this.DescCausaEstado = _pedido.getDescCausaEstado();
		this.NombreVendedor = _pedido.getNombreVendedor();
		this.Detalles = _pedido.getDetalles();
		this.PromocionesAplicadas = _pedido.getPromocionesAplicadas();
		this.Nota = _pedido.getNota();
		this.Exento = _pedido.isExento();
		this.AutorizacionDGI = _pedido.getAutorizacionDGI();
	}
    
	public void setOldData(Pedido _pedido)
	{
		oldata=new Pedido(_pedido);
	}
	
	public Pedido getOldData()
	{
		return oldata;
	}
	
    public void setId(long id) {
        this.Id = id;
    }

    public long getId() {
        return this.Id;
    }

    public void setNumeroMovil(int numeroMovil) {
        this.NumeroMovil = numeroMovil;
    }

    public int getNumeroMovil() {
        return this.NumeroMovil;
    }

    public void setNumeroCentral(int numeroCentral) {
        this.NumeroCentral = numeroCentral;
    }

    public int getNumeroCentral() {
        return this.NumeroCentral;
    }

    public void setTipo(java.lang.String tipo) {
        this.Tipo = tipo;
    }

    public java.lang.String getTipo() {
        return this.Tipo;
    }

    public void setFecha(int fecha) {
        this.Fecha = fecha;
    }

    public int getFecha() {
        return this.Fecha;
    }

    public void setObjClienteID(long objClienteID) {
        this.objClienteID = objClienteID;
    }

    public long getObjClienteID() {
        return this.objClienteID;
    }

    public void setNombreCliente(java.lang.String nombreCliente) {
        this.NombreCliente = nombreCliente;
    }

    public java.lang.String getNombreCliente() {
        return this.NombreCliente;
    }

    public void setObjSucursalID(long objSucursalID) {
        this.objSucursalID = objSucursalID;
    }

    public long getObjSucursalID() {
        return this.objSucursalID;
    }

    public void setNombreSucursal(java.lang.String nombreSucursal) {
        this.NombreSucursal = nombreSucursal;
    }

    public java.lang.String getNombreSucursal() {
        return this.NombreSucursal;
    }

    public void setObjTipoPrecioVentaID(long objTipoPrecioVentaID) {
        this.objTipoPrecioVentaID = objTipoPrecioVentaID;
    }

    public long getObjTipoPrecioVentaID() {
        return this.objTipoPrecioVentaID;
    }

    public void setCodTipoPrecio(java.lang.String codTipoPrecio) {
        this.CodTipoPrecio = codTipoPrecio;
    }

    public java.lang.String getCodTipoPrecio() {
        return this.CodTipoPrecio;
    }

    public void setDescTipoPrecio(java.lang.String descTipoPrecio) {
        this.DescTipoPrecio = descTipoPrecio;
    }

    public java.lang.String getDescTipoPrecio() {
        return this.DescTipoPrecio;
    }

    public void setObjVendedorID(long objVendedorID) {
        this.objVendedorID = objVendedorID;
    }

    public long getObjVendedorID() {
        return this.objVendedorID;
    }

    public void setBonificacionEspecial(boolean bonificacionEspecial) {
        this.BonificacionEspecial = bonificacionEspecial;
    }

    public boolean getBonificacionEspecial() {
        return this.BonificacionEspecial;
    }

    public void setBonificacionSolicitada(java.lang.String bonificacionSolicitada) {
        this.BonificacionSolicitada = bonificacionSolicitada;
    }

    public java.lang.String getBonificacionSolicitada() {
        return this.BonificacionSolicitada;
    }

    public void setPrecioEspecial(boolean precioEspecial) {
        this.PrecioEspecial = precioEspecial;
    }

    public boolean getPrecioEspecial() {
        return this.PrecioEspecial;
    }

    public void setPrecioSolicitado(java.lang.String precioSolicitado) {
        this.PrecioSolicitado = precioSolicitado;
    }

    public java.lang.String getPrecioSolicitado() {
        return this.PrecioSolicitado;
    }

    public void setPedidoCondicionado(boolean pedidoCondicionado) {
        this.PedidoCondicionado = pedidoCondicionado;
    }

    public boolean getPedidoCondicionado() {
        return this.PedidoCondicionado;
    }

    public void setCondicion(java.lang.String condicion) {
        this.Condicion = condicion;
    }

    public java.lang.String getCondicion() {
        return this.Condicion;
    }

    public void setSubtotal(float subtotal) {
        this.Subtotal = subtotal;
    }

    public float getSubtotal() {
        return this.Subtotal;
    }

    public void setDescuento(float descuento) {
        this.Descuento = descuento;
    }

    public float getDescuento() {
        return this.Descuento;
    }

    public void setImpuesto(float impuesto) {
        this.Impuesto = impuesto;
    }

    public float getImpuesto() {
        return this.Impuesto;
    }

    public void setTotal(float total) {
        this.Total = total;
    }

    public float getTotal() {
        return this.Total;
    }

    public void setObjEstadoID(long objEstadoID) {
        this.objEstadoID = objEstadoID;
    }

    public long getObjEstadoID() {
        return this.objEstadoID;
    }

    public void setCodEstado(java.lang.String codEstado) {
        this.CodEstado = codEstado;
    }

    public java.lang.String getCodEstado() {
        return this.CodEstado;
    }

    public void setDescEstado(java.lang.String descEstado) {
        this.DescEstado = descEstado;
    }

    public java.lang.String getDescEstado() {
        return this.DescEstado;
    }

    public void setObjCausaEstadoID(long objCausaEstadoID) {
        this.objCausaEstadoID = objCausaEstadoID;
    }

    public long getObjCausaEstadoID() {
        return this.objCausaEstadoID;
    }

    public void setCodCausaEstado(java.lang.String codCausaEstado) {
        this.CodCausaEstado = codCausaEstado;
    }

    public java.lang.String getCodCausaEstado() {
        return this.CodCausaEstado;
    }

    public void setDescCausaEstado(java.lang.String descCausaEstado) {
        this.DescCausaEstado = descCausaEstado;
    }

    public java.lang.String getDescCausaEstado() {
        return this.DescCausaEstado;
    }

    public void setNombreVendedor(java.lang.String nombreVendedor) {
        this.NombreVendedor = nombreVendedor;
    }

    public java.lang.String getNombreVendedor() {
        return this.NombreVendedor;
    }

    public void setDetalles(DetallePedido[] detalles) {
        this.Detalles = detalles;
    }

    public DetallePedido[] getDetalles() {
    	 return (this.Detalles!=null)?this.Detalles:new DetallePedido[0]; 
    }

    public void setPromocionesAplicadas(PedidoPromocion[] promocionesAplicadas) {
        this.PromocionesAplicadas = promocionesAplicadas;
    }

    public PedidoPromocion[] getPromocionesAplicadas() {
    	
        return (this.PromocionesAplicadas!=null)?this.PromocionesAplicadas:new PedidoPromocion[0];
    }

    public void setNota(java.lang.String nota) {
        this.Nota = nota;
    }

    public java.lang.String getNota() {
        return this.Nota;
    }

    public void setExento(boolean exento) {
        this.Exento = exento;
    }

    public boolean isExento() {
        return this.Exento;
    }

    public void setAutorizacionDGI(java.lang.String autorizacionDGI) {
        this.AutorizacionDGI = autorizacionDGI;
    }

    public java.lang.String getAutorizacionDGI() {
        return this.AutorizacionDGI;
    }

    @Override
	public int getPropertyCount() {
        return 35;
    }
    
    @SuppressWarnings("unused")
	public boolean hasModified(Object obj) 
    { 
    	if(obj==null && this!=null)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if(other==null && this!=null)
			return true;	
		
		DetallePedido[] detother=other.getDetalles();
		DetallePedido[] det=getDetalles();
		
		if(getObjSucursalID()!=other.getObjSucursalID())
			return true;
		if(!(getTipo().equals(other.getTipo())))
			return true;
		if(getTotal()!=other.getTotal())
			return true;
		if(detother==null && det!=null)
			return true;		
		if(detother.length!=det.length)
			return true;	
		
		return false;
    }
     
    
    @SuppressLint("UseValueOf")
	@Override 
	public Object getProperty(int _index) {
        switch(_index)  {
        case 0: return new Long(Id);
        case 1: return new Integer(NumeroMovil);
        case 2: return new Integer(NumeroCentral);
        case 3: return Tipo;
        case 4: return new Integer(Fecha);
        case 5: return new Long(objClienteID);
        case 6: return NombreCliente;
        case 7: return new Long(objSucursalID);
        case 8: return NombreSucursal;
        case 9: return new Long(objTipoPrecioVentaID);
        case 10: return CodTipoPrecio;
        case 11: return DescTipoPrecio;
        case 12: return new Long(objVendedorID);
        case 13: return new Boolean(BonificacionEspecial);
        case 14: return BonificacionSolicitada;
        case 15: return new Boolean(PrecioEspecial);
        case 16: return PrecioSolicitado;
        case 17: return new Boolean(PedidoCondicionado);
        case 18: return Condicion; 
        case 19: return Subtotal;
        case 20: return (Descuento);
        case 21: return (Impuesto); 
        case 22: return Total;
        case 23: return new Long(objEstadoID);
        case 24: return CodEstado;
        case 25: return DescEstado;
        case 26: return new Long(objCausaEstadoID);
        case 27: return CodCausaEstado;
        case 28: return DescCausaEstado;
        case 29: return NombreVendedor;
	    case 30: 
			if (Detalles != null && Detalles.length > 0) 
			{	    		
	    	
	    		SoapObject _detalle=new SoapObject("", "");
	    		
	    		if(Detalles!=null)
		        {
	    			for(DetallePedido dp:Detalles)
		    		{
		    			SoapObject item=new SoapObject("", "DetallePedido");
		    			int cont=dp.getPropertyCount();
		    			for(int i=0;i<cont;i++)
		    			{
		    				PropertyInfo info=new PropertyInfo();
		    				dp.getPropertyInfo(i, null, info);
		    				item.addProperty(info.name,dp.getProperty(i));
		    			}		    	
		    			_detalle.addSoapObject(item);
		    		}	  
		        }	  
		          		  	
	    		return _detalle;
	    	}
	    	
	    	break;	    	
        case 31: 
        	
			if (PromocionesAplicadas != null && PromocionesAplicadas.length > 0) 
			{
				SoapObject _promocionesaplicadas = new SoapObject("","");
				for (PedidoPromocion pa : PromocionesAplicadas)
				{
					SoapObject item = new SoapObject("","PromocionesAplicadas");
					int cont = pa.getPropertyCount();
					for (int i = 0; i < cont; i++) 
					{
						PropertyInfo info = new PropertyInfo();
						pa.getPropertyInfo(i, null, info);
						if ("Detalles" == info.name)
							item.addSoapObject((SoapObject) pa.getProperty(i));
						else
							item.addProperty(info.name, pa.getProperty(i));
					}
					_promocionesaplicadas.addSoapObject(item);
				} 
			    return _promocionesaplicadas;        		
        	}
        	break;  
        case 32: return Nota;
        case 33: return new Boolean(Exento);
        case 34: return AutorizacionDGI;
        }
        return null;
    }
     
    
    @Override
	public void setProperty(int _index, Object _) {
        switch(_index)  {
        case 0: Id = Long.parseLong(_.toString()); break;
        case 1: NumeroMovil = Integer.parseInt(_.toString()); break;
        case 2: NumeroCentral = Integer.parseInt(_.toString()); break;
        case 3: Tipo = (java.lang.String) _; break;
        case 4: Fecha = Integer.parseInt(_.toString()); break;
        case 5: objClienteID = Long.parseLong(_.toString()); break;
        case 6: NombreCliente = (java.lang.String) _; break;
        case 7: objSucursalID = Long.parseLong(_.toString()); break;
        case 8: NombreSucursal = (java.lang.String) _; break;
        case 9: objTipoPrecioVentaID = Long.parseLong(_.toString()); break;
        case 10: CodTipoPrecio = (java.lang.String) _; break;
        case 11: DescTipoPrecio = (java.lang.String) _; break;
        case 12: objVendedorID = Long.parseLong(_.toString()); break;
        case 13: BonificacionEspecial = "true".equals(_.toString()); break;
        case 14: BonificacionSolicitada = (java.lang.String) _; break;
        case 15: PrecioEspecial = "true".equals(_.toString()); break;
        case 16: PrecioSolicitado = (java.lang.String) _; break;
        case 17: PedidoCondicionado = "true".equals(_.toString()); break;
        case 18: Condicion = (java.lang.String) _; break;       
        case 19: Subtotal = Float.parseFloat(_.toString()); break;  
        case 20: Descuento = Float.parseFloat(_.toString()); break;
        case 21: Impuesto = Float.parseFloat(_.toString()); break;
        case 22: Total = Float.parseFloat(_.toString()); break; 
        case 23: objEstadoID = Long.parseLong(_.toString()); break;
        case 24: CodEstado = (java.lang.String) _; break;
        case 25: DescEstado = (java.lang.String) _; break;
        case 26: objCausaEstadoID = Long.parseLong(_.toString()); break;
        case 27: CodCausaEstado = (java.lang.String) _; break;
        case 28: DescCausaEstado = (java.lang.String) _; break;
        case 29: NombreVendedor = (java.lang.String) _; break;
        case 30: Detalles = (DetallePedido[]) _;break;
        case 31: PromocionesAplicadas = (PedidoPromocion[]) _; break; 
        case 33: Nota = (java.lang.String) _; break;
        case 34: Exento = "true".equals(_.toString()); break;
        case 35: AutorizacionDGI = (java.lang.String) _; break;
        }
    }    
    
    
    @Override
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
       try {   	   
    	   
    	   switch(_index)  {
           case 0:
               _info.name = "Id";
               _info.type = Long.class; break;
           case 1:
               _info.name = "NumeroMovil";
               _info.type = Integer.class; break;
           case 2:
               _info.name = "NumeroCentral";
               _info.type = Integer.class; break;
           case 3:
               _info.name = "Tipo";
               _info.type = java.lang.String.class; break;
           case 4:
               _info.name = "Fecha";
               _info.type = Integer.class; break;
           case 5:
               _info.name = "objClienteID";
               _info.type = Long.class; break;
           case 6:
               _info.name = "NombreCliente";
               _info.type = java.lang.String.class; break;
           case 7:
               _info.name = "objSucursalID";
               _info.type = Long.class; break;
           case 8:
               _info.name = "NombreSucursal";
               _info.type = java.lang.String.class; break;
           case 9:
               _info.name = "objTipoPrecioVentaID";
               _info.type = Long.class; break;
           case 10:
               _info.name = "CodTipoPrecio";
               _info.type = java.lang.String.class; break;
           case 11:
               _info.name = "DescTipoPrecio";
               _info.type = java.lang.String.class; break;
           case 12:
               _info.name = "objVendedorID";
               _info.type = Long.class; break;
           case 13:
               _info.name = "BonificacionEspecial";
               _info.type = Boolean.class; break;
           case 14:
               _info.name = "BonificacionSolicitada";
               _info.type = java.lang.String.class; break;
           case 15:
               _info.name = "PrecioEspecial";
               _info.type = Boolean.class; break;
           case 16:
               _info.name = "PrecioSolicitado";
               _info.type = java.lang.String.class; break;
           case 17:
               _info.name = "PedidoCondicionado";
               _info.type = Boolean.class; break;
           case 18:
               _info.name = "Condicion";
               _info.type = java.lang.String.class; break;
           case 19:
               _info.name = "Subtotal";
               _info.type = Float.class; break;    
           case 20:
               _info.name = "Descuento";
               _info.type = Float.class; break;  
           case 21:
               _info.name = "Impuesto";
               _info.type = Float.class; break;  
           case 22:
               _info.name = "Total";
               _info.type = Float.class; break;  
           case 23:
               _info.name = "objEstadoID";
               _info.type = Long.class; break;
           case 24:
               _info.name = "CodEstado";
               _info.type = java.lang.String.class; break;
           case 25:
               _info.name = "DescEstado";
               _info.type = java.lang.String.class; break;
           case 26:
               _info.name = "objCausaEstadoID";
               _info.type = Long.class; break;
           case 27:
               _info.name = "CodCausaEstado";
               _info.type = java.lang.String.class; break;
           case 28:
               _info.name = "DescCausaEstado";
               _info.type = java.lang.String.class; break;
           case 29:
               _info.name = "NombreVendedor";
               _info.type = java.lang.String.class; break;
           case 30:
               _info.name = "Detalles";
               _info.type= DetallePedido[].class;
               break;
           case 31:
        	   if(PromocionesAplicadas!= null && PromocionesAplicadas.length > 0) 
        	   {        		   
                   _info.name = "PROMOCIONES_AP";
                   _info.type=PedidoPromocion[].class;
        	   }        	  
               break; 
           case 32:
               _info.name = "Nota";
               _info.type = java.lang.String.class; break;
           case 33:
               _info.name = "Exento";
               _info.type = Boolean.class; break;
           case 34:
               _info.name = "AutorizacionDGI";
               _info.type = java.lang.String.class; break;
           }
    	   
		
	} catch (Exception e) {
		e.printStackTrace();
	}
    	
    	
    }

    @Override
	public Object isMatch(CharSequence constraint) {
		if (String.valueOf(getNumeroMovil()).toLowerCase().startsWith(constraint.toString()))
			return true;
		else if (String.valueOf(getNombreSucursal()).toLowerCase().startsWith(constraint.toString()))
			return true;
		return false;
	}
	
    @Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return getNumeroMovil()+"-"+getNombreCliente()+"/"+getNombreSucursal();
	}
	
    @Override
	public String getItemDescription() {
		// TODO Auto-generated method stub
		return "Fecha: " + getFecha() + ", Total: " + getTotal()
				+ ", Estado: " + getDescEstado();
	}
	
    @Override
	public String getItemCode() {
		// TODO Auto-generated method stub
		return ""+getNumeroMovil();
	}

    public Pedido(Parcel parcel){
    	   
    	   readFromParcel(parcel);
    }
    
    public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() {

        @Override
		public Pedido createFromParcel(Parcel parcel) {
             return new Pedido(parcel);
        }

        @Override
		public Pedido[] newArray(int size) {
             return new Pedido[size];
        }
      	 
      	 
   };
    
   @Override
	public void writeToParcel(Parcel parcel, int flags) {	
		parcel.writeLong(Id);
		parcel.writeInt(NumeroMovil);
		parcel.writeInt(NumeroCentral);		
		parcel.writeString(Tipo);
		parcel.writeInt(Fecha);
		parcel.writeLong(objClienteID);
		parcel.writeString(NombreCliente);
		parcel.writeLong(objSucursalID);
		parcel.writeString(NombreSucursal);
		parcel.writeLong(objTipoPrecioVentaID);
		parcel.writeString(CodTipoPrecio);
		parcel.writeString(DescTipoPrecio);
		parcel.writeLong(objVendedorID);
		parcel.writeInt((BonificacionEspecial==true)?1:0);
		parcel.writeString(BonificacionSolicitada);
		parcel.writeInt((PrecioEspecial==true)?1:0);
		parcel.writeString(PrecioSolicitado);
		parcel.writeInt((PedidoCondicionado==true)?1:0);
		parcel.writeString(Condicion);
		parcel.writeFloat(Subtotal);
		parcel.writeFloat(Descuento);
		parcel.writeFloat(Impuesto);
		parcel.writeFloat(Total);
		parcel.writeLong(objEstadoID);
		parcel.writeString(CodEstado);
		parcel.writeString(DescEstado);
		parcel.writeLong(objCausaEstadoID);
		parcel.writeString(CodCausaEstado);
		parcel.writeString(DescCausaEstado);
		parcel.writeString(NombreVendedor);
		parcel.writeParcelableArray(Detalles, flags);
		parcel.writeParcelableArray(PromocionesAplicadas,flags);
		parcel.writeString(Nota);
		parcel.writeString(AutorizacionDGI);
		  
	}
	
    private void readFromParcel(Parcel parcel) 
    {
    	this.Id = parcel.readLong();
		this.NumeroMovil=parcel.readInt();
		this.NumeroCentral=parcel.readInt();
		this.Tipo=parcel.readString();
		this.Fecha=parcel.readInt();
		this.objClienteID = parcel.readLong();
		this.NombreCliente=parcel.readString();
		this.objSucursalID = parcel.readLong();
		this.NombreSucursal=parcel.readString();
		this.objTipoPrecioVentaID= parcel.readLong();
		this.CodTipoPrecio=parcel.readString();
		this.DescTipoPrecio=parcel.readString();
		this.objVendedorID= parcel.readLong();
		this.BonificacionEspecial = ( parcel.readInt()==1)?true:false;
		this.BonificacionSolicitada=parcel.readString();
		this.PrecioEspecial = (parcel.readInt()==1)?true:false;
		this.PrecioSolicitado=parcel.readString();
		this.PedidoCondicionado = (parcel.readInt()==1)?true:false;
		this.Condicion=parcel.readString();
		this.Subtotal=parcel.readFloat();
		this.Descuento=parcel.readFloat();
		this.Impuesto=parcel.readFloat();
		this.Total=parcel.readFloat();
		this.objEstadoID= parcel.readLong();
		this.CodEstado=parcel.readString();
		this.DescEstado=parcel.readString();
		this.objCausaEstadoID= parcel.readLong();
		this.CodCausaEstado=parcel.readString();
		this.DescCausaEstado=parcel.readString();
		this.NombreVendedor=parcel.readString();
		
		Parcelable[] parcelableArray = parcel.readParcelableArray(DetallePedido.class.getClassLoader()); 
		if (parcelableArray != null) {
			this.Detalles = Arrays.copyOf(parcelableArray, parcelableArray.length, DetallePedido[].class);
		}
		
		parcelableArray = parcel.readParcelableArray(PedidoPromocion.class.getClassLoader()); 
		if (parcelableArray != null) {
			this.PromocionesAplicadas = Arrays.copyOf(parcelableArray, parcelableArray.length, PedidoPromocion[].class);
		}
		this.Nota=parcel.readString();
		this.AutorizacionDGI=parcel.readString();
	}
   
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getItemCodeStado() {
		return getCodEstado();
	}
}
