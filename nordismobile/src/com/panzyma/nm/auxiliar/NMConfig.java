package com.panzyma.nm.auxiliar;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.model.ModelConfiguracion;
 
 
public class NMConfig
{     
	//public static String URL_SERVER="http://www.panzyma.com/nordisserverprod";
	//public static String URL_SERVER2="http://www.panzyma.com/SimfacProd/SimfacService.svc";
	//public static String URL = "http://www.panzyma.com/nordisservertest/mobileservice.asmx";
    //public static String URL2 ="http://www.panzyma.com/SimfacService/SimfacService.svc/";	     
	//public static String URL = "http://192.168.1.100/NordisServer/mobileservice.asmx";
    //public static String URL2 ="http://192.168.1.110:8080/Servicios/SimfacService.svc/";
	public static final String NAME_SPACE = "http://www.panzyma.com/";
	public static String URL = ModelConfiguracion.getURL_SERVER(NMApp.getContext());
    public static String URL2 =ModelConfiguracion.getURL_SERVER2(NMApp.getContext());
    
    public static class MethodName
    { 	
    	public static final String CheckConnection="CheckConnection";
    	public static final String LoginUser="LoginUser";
    	public static final String getDataConfiguration="getDataConfiguration";
    	public static final String UserHasRol="UserHasRol";
    	public static final String GetDevicePrefix="getDevicePrefix"; 
    	public static final String GetDatosUsuario="GetDatosUsuario";
    	public static final String GetCliente="GetCliente"; 
	    public static final String GetClientesPaged = "GetClientesPaged";
	    public static final String GetCCCliente="GetCCCliente";
	    public static final String TraerFacturasCliente="TraerFacturasCliente";
	    public static final String GetProductosPaged="GetProductosPaged"; 
	    public static final String GetParams="GetParams";
	    public static final String GetValoresCatalogo="GetValoresCatalogo";
	    public static final String GetTasasDeCambio="GetTasasDeCambio";
	    public static final String GetPromocionesPaged="GetPromocionesPaged";
	    public static final String TraerDisponibilidadProductos="TraerDisponibilidadProductos";
	    public static final String EnviarPedido="EnviarPedido";
		public static final String TraerPedidosCliente = "TraerPedidosCliente";
		public static final String TraerRColCliente = "TraerRColCliente";
		public static final String TraerNotasDebitoCliente = "TraerNotasDebitoCliente";
		public static final String TraerNotasCreditoCliente = "TraerNotasCreditoCliente";
		public static final String GetVentaVendedor = "GetVentaVendedor";	
		public static final String AnularPedido = "AnularPedido";
		public static final String EnviarRecibo="EnviarRecibo";
		public static final String SolicitarDescuento="SolicitarDescuento";
		public static final String VerificarAutorizacionDescuento="VerificarAutorizacionDescuento";
	}
	
    public static final class Cliente
	{  		
		public static final String Credentials="Credentials";
		public static final String UsuarioVendedor="Credentials";
		public static final String Page="UsuarioVendedor";
		public static final String RowsPerPage="RowsPerPage";     		
		
		public static final java.lang.String IdCliente="IdCliente";
		public static final java.lang.String NombreCliente="NombreCliente";
		public static final java.lang.String IdSucursal="IdSucursal";
		public static final java.lang.String Codigo="Codigo";
		public static final java.lang.String CodTipoPrecio="CodTipoPrecio";
		public static final java.lang.String DesTipoPrecio="DesTipoPrecio";
		public static final java.lang.String objPrecioVentaID="objPrecioVentaID";
		public static final java.lang.String objCategoriaClienteID="objCategoriaClienteID";
		public static final java.lang.String objTipoClienteID="objTipoClienteID";
		public static final java.lang.String AplicaBonificacion="AplicaBonificacion";
		public static final java.lang.String PermiteBonifEspecial="PermiteBonifEspecial";
		public static final java.lang.String PermitePrecioEspecial="PermitePrecioEspecial";
		public static final java.lang.String UG="UG";
		public static final java.lang.String Ubicacion="Ubicacion";
		public static final java.lang.String NombreLegalCliente="NombreLegalCliente"; 
		public static final java.lang.String AplicaOtrasDeducciones="AplicaOtrasDeducciones";
		public static final java.lang.String MontoMinimoAbono="MontoMinimoAbono";
		public static final java.lang.String PlazoDescuento="PlazoDescuento";
		public static final java.lang.String PermiteDevolucion="PermiteDevolucion";
		public static final java.lang.String objSucursalID="objSucursalID";
		public static final class Factura
		{
			public static final java.lang.String Id="Id";
			public static final java.lang.String NombreSucursal="NombreSucursal";
			public static final java.lang.String NoFactura="NoFactura";	    
		    public static final java.lang.String Tipo="Tipo";
		    public static final java.lang.String NoPedido="NoPedido";
		    public static final java.lang.String CodEstado="CodEstado";
		    public static final java.lang.String Estado="Estado";
		    public static final java.lang.String Fecha="Fecha";
		    public static final java.lang.String FechaVencimiento="FechaVencimiento";
		    public static final java.lang.String FechaAppDescPP="FechaAppDescPP";
		    public static final java.lang.String Dias="Dias";
		    public static final java.lang.String TotalFacturado="TotalFacturado";
		    public static final java.lang.String Abonado="Abonado";
		    public static final java.lang.String Descontado="Descontado";
		    public static final java.lang.String Retenido="Retenido";
		    public static final java.lang.String Otro="Otro";
		    public static final java.lang.String Saldo="Saldo";
		    public static final java.lang.String Exenta="Exenta";	   
		    public static final java.lang.String SubtotalFactura="SubtotalFactura";
		    public static final java.lang.String DescuentoFactura="DescuentoFactura";
		    public static final java.lang.String ImpuestoFactura="ImpuestoFactura";
		    public static final java.lang.String PuedeAplicarDescPP="PuedeAplicarDescPP"; 
		    public static final java.lang.String objFacturaID="objFacturaID";
		    
		    public static final class PromocionCobro
		    {
				public static final java.lang.String FacturasAplicacion="FacturasAplicacion";
				public static final java.lang.String TipoDescuento="TipoDescuento";
				public static final java.lang.String Descuento="Descuento";
		    }
		    public static final class MontoProveedor
		    { 
				public static final java.lang.String ObjProveedorID="ObjProveedorID";
		        public static final java.lang.String CodProveedor="CodProveedor";
		        public static final java.lang.String Monto="Monto";
		    }
		    
		}
		public static final class CCNotaCredito
		{
			public static final java.lang.String Id="Id";
			public static final java.lang.String NombreSucursal="NombreSucursal";
			public static final java.lang.String Estado="Estado";
			public static final java.lang.String Numero="Numero";
			public static final java.lang.String Fecha="Fecha";
			public static final java.lang.String FechaVence="FechaVence";
			public static final java.lang.String Concepto="Concepto";
			public static final java.lang.String Monto="Monto";
			public static final java.lang.String NumRColAplic="NumRColAplic";
			public static final java.lang.String CodEstado="CodEstado";
			public static final java.lang.String Descripcion="Descripcion";
			public static final java.lang.String Referencia="Referencia";
			public static final java.lang.String CodConcepto="CodConcepto"; 
		}
		public static final class CCNotaDebito
		{
			public static final java.lang.String Id="Id";
			public static final java.lang.String NombreSucursal="NombreSucursal";
			public static final java.lang.String Estado="Estado";
			public static final java.lang.String Numero="Numero";
			public static final java.lang.String Fecha="Fecha";
			public static final java.lang.String FechaVence="FechaVence";
			public static final java.lang.String Dias="Dias";
			public static final java.lang.String Concepto="Concepto";
			public static final java.lang.String Monto="Monto";
			public static final java.lang.String MontoAbonado="MontoAbonado";
			public static final java.lang.String Saldo="Saldo";
			public static final java.lang.String CodEstado="CodEstado";
		    public static final java.lang.String Descripcion="Descripcion"; 
		} 
		public static final class DescuentoProveedor
		{
			public static final java.lang.String ObjProveedorID="ObjProveedorID";
			public static final java.lang.String PrcDescuento="PrcDescuento";  
		} 
		
	}

	public static final class Pedido
	{
		public static final java.lang.String Id="Id";
		public static final java.lang.String NumeroMovil="NumeroMovil";
		public static final java.lang.String NumeroCentral="NumeroCentral";
		public static final java.lang.String Tipo="Tipo";
		public static final java.lang.String Fecha="Fecha";
		public static final java.lang.String objClienteID="objClienteID";
		public static final java.lang.String NombreCliente="NombreCliente";
		public static final java.lang.String objSucursalID="objSucursalID";
		public static final java.lang.String NombreSucursal="NombreSucursal";
		public static final java.lang.String objTipoPrecioVentaID="objTipoPrecioVentaID";
		public static final java.lang.String CodTipoPrecio="CodTipoPrecio";
		public static final java.lang.String DescTipoPrecio="DescTipoPrecio";
		public static final java.lang.String objVendedorID="objVendedorID";
		public static final java.lang.String BonificacionEspecial="BonificacionEspecial";
		public static final java.lang.String BonificacionSolicitada="BonificacionSolicitada";
		public static final java.lang.String PrecioEspecial="PrecioEspecial";
		public static final java.lang.String PrecioSolicitado="PrecioSolicitado";
		public static final java.lang.String PedidoCondicionado="PedidoCondicionado";
		public static final java.lang.String Condicion="Condicion";
		public static final java.lang.String Subtotal="Subtotal";
		public static final java.lang.String Descuento="Descuento";
		public static final java.lang.String Impuesto="Impuesto";
		public static final java.lang.String Total="Total";
		public static final java.lang.String objEstadoID="objEstadoID";
		public static final java.lang.String CodEstado="CodEstado";
		public static final java.lang.String DescEstado="DescEstado";
		public static final java.lang.String objCausaEstadoID="objCausaEstadoID";
		public static final java.lang.String CodCausaEstado="CodCausaEstado";
		public static final java.lang.String DescCausaEstado="DescCausaEstado";
		public static final java.lang.String NombreVendedor="NombreVendedor";		
		public static final class DetallePedido
		{
			public static final java.lang.String Id="Id";
		    public static final java.lang.String objPedidoID="objPedidoID";
		    public static final java.lang.String objProductoID="objProductoID";
		    public static final java.lang.String codProducto="CodProducto";
		    public static final java.lang.String nombreProducto="NombreProducto";
		    public static final java.lang.String cantidadOrdenada="CantidadOrdenada";
		    public static final java.lang.String cantidadBonificada="CantidadBonificada";
		    public static final java.lang.String objBonificacionID="objBonificacionID";
		    public static final java.lang.String bonifEditada="BonifEditada";
		    public static final java.lang.String cantidadBonificadaEditada="CantidadBonificadaEditada";
		    public static final java.lang.String precio="Precio";
		    public static final java.lang.String montoPrecioEditado="MontoPrecioEditado";
		    public static final java.lang.String precioEditado="PrecioEditado";
		    public static final java.lang.String subtotal="Subtotal";
		    public static final java.lang.String descuento="Descuento";
		    public static final java.lang.String porcImpuesto="PorcImpuesto";
		    public static final java.lang.String impuesto="Impuesto";
		    public static final java.lang.String total="Total";
		    public static final java.lang.String cantidadDespachada="CantidadDespachada";
		    public static final java.lang.String cantidadADespachar="CantidadADespachar";
		    public static final java.lang.String cantidadPromocion="CantidadPromocion";
		}
		public static final class PedidoPromocion
		{
			public static final java.lang.String objPromocionID="objPromocionID";
			public static final java.lang.String objPedidoID="objPedidoID";
			public static final java.lang.String descuento="Descuento";
			public static final class PedidoPromocionDetalle
			{
				public static final java.lang.String objProductoID="objProductoID";
				public static final java.lang.String objPromocionID="objPromocionID";
				public static final java.lang.String objPedidoID="objPedidoID";		
				public static final java.lang.String descuento="Descuento";		
				public static final java.lang.String nombreProducto="NombreProducto";
				public static final java.lang.String cantidadEntregada="CantidadEntregada";
				
			}
			public static final java.lang.String codigoPromocion="CodigoPromocion";
			public static final java.lang.String nombrePromocion="NombrePromocion";
		} 
		public static final java.lang.String Nota="Nota";
		public static final java.lang.String Exento="Exento";
		public static final java.lang.String AutorizacionDGI="AutorizacionDGI";
	}
	
	public static final class Producto
	{
		 public static final java.lang.String Id="Id";
		 public static final java.lang.String Codigo="Codigo";
		 public static final java.lang.String Nombre="Nombre";
		 public static final java.lang.String EsGravable="EsGravable";
		 public static final java.lang.String ListaPrecios="ListaPrecios";
		 public static final java.lang.String ListaBonificaciones="ListaBonificaciones";
		 public static final java.lang.String CatPrecios="CatPrecios";
		 public static final java.lang.String Disponible="Disponible"; 
		 public static final java.lang.String PermiteDevolucion="PermiteDevolucion";
		 public static final java.lang.String LoteRequerido="LoteRequerido";
		 public static final java.lang.String ObjProveedorID="ObjProveedorID";
		 public static final java.lang.String DiasAntesVen="DiasAntesVen";
		 public static final java.lang.String DiasDespuesVen="DiasDespuesVen";
		 public static final java.lang.String ObjProductoID="ObjProductoID";
		 public static final class Lote
		 {
			public static final java.lang.String Id="Id";
			public static final java.lang.String NumeroLote="NumeroLote";
			public static final java.lang.String FechaVencimiento="FechaVencimiento";
		 }
	}
	
	public static final class Usuario
	{
		 public static final java.lang.String Id="Id";
		 public static final java.lang.String Login="Login";
		 public static final java.lang.String Nombre="Nombre";
		 public static final java.lang.String Sexo="Sexo";
		 public static final java.lang.String AccedeModuloPedidos="AccedeModuloPedidos"; 
		 public static final java.lang.String PuedeEditarPrecioAbajo="PuedeEditarPrecioAbajo";
		 public static final java.lang.String PuedeEditarPrecioArriba="PuedeEditarPrecioArriba";
		 public static final java.lang.String PuedeEditarBonifAbajo="PuedeEditarBonifAbajo"; 
		 public static final java.lang.String PuedeEditarBonifArriba="PuedeEditarBonifArriba";
		 public static final java.lang.String IsAdmin="IsAdmin";
		 public static final java.lang.String PuedeCrearPedido="PuedeCrearPedido";
		 public static final java.lang.String PuedeConsultarPedido="PuedeConsultarPedido";
		 public static final java.lang.String Codigo="Codigo";
		 public static final java.lang.String PuedeEditarDescPP="PuedeEditarDescPP";
	}
	
	public static final class Catalogo
	{
		public static final java.lang.String Id="Id";
		public static final java.lang.String NombreCatalogo="NombreCatalogo";
		
		
		public static final class ValorCatalogo 
		{
			public static final java.lang.String Id="Id";
			public static final java.lang.String objCatalogoID="objCatalogoID"; 
			public static final java.lang.String Codigo="Codigo";
			public static final java.lang.String Descripcion="Descripcion";
		}		
	}
	
	public static final class TasaCambio
	{
		public static final java.lang.String CodMoneda="CodMoneda";
		public static final java.lang.String Fecha="Fecha";
		public static final java.lang.String Tasa="Tasa";
	}
	
	public static final class Promocion
	{
		public static final java.lang.String  Id="Id";
	    public static final java.lang.String  Codigo="Codigo";
	    public static final java.lang.String  Descripcion="Descripcion";
	    public static final java.lang.String  AplicaCredito="AplicaCredito";
	    public static final java.lang.String  FechaFin="FechaFin";
	    public static final java.lang.String  MomentoAplicacion="MomentoAplicacion";
	    public static final java.lang.String  TipoPromo="TipoPromo";
	    public static final java.lang.String  MontoMinimo="MontoMinimo";
	    public static final java.lang.String  TipoDescuento="TipoDescuento";
	    public static final java.lang.String  Descuento="Descuento";
	    public static final java.lang.String  AplicacionMultiple="AplicacionMultiple";
	    public static final java.lang.String  CantidadMinimaItems="CantidadMinimaItems";
	    public static final java.lang.String  CantidadMinimaBaseUnica="CantidadMinimaBaseUnica";
	    public static final java.lang.String  CantidadMinimaBase="CantidadMinimaBase";
	    public static final java.lang.String  CantidadPremioUnica="CantidadPremioUnica";
	    public static final java.lang.String  CantidadPremio="CantidadPremio";
	    public static final java.lang.String  ProductosOtorgadosPor="ProductosOtorgadosPor";
	    public static final java.lang.String  MontoEntregadoPor="MontoEntregadoPor";
	    public static final java.lang.String  DescripcionPromocion="DescripcionPromocion";
	    public static final java.lang.String  CatClientes="CatClientes";
	    public static final java.lang.String  TiposCliente="TiposCliente";
	    public static final java.lang.String  Sucursales="Sucursales";
	    public static final java.lang.String  Ubicaciones="Ubicaciones";
	    public static final java.lang.String  ProdsBase="ProdsBase";
	    public static final java.lang.String  ProdsPremio="ProdsPremio";
	    public static final java.lang.String  MontoBaseUnico="MontoBaseUnico";
	    public static final java.lang.String  MontoBaseMinimo="MontoBaseMinimo";
	    public static final java.lang.String  MontoBaseMaximo="MontoBaseMaximo";
	    public static final java.lang.String  MontoPremioUnico="MontoPremioUnico";
	    public static final java.lang.String  MontoPremio="MontoPremio";
		
	}
	
	public static final class Recibo {
		
		public static final java.lang.String ID = "id";
		public static final java.lang.String NUMERO = "numero";
		public static final java.lang.String FECHA = "fecha";
		public static final java.lang.String NOTAS = "notas";
		public static final java.lang.String TOTAL_RECIBO = "totalRecibo";
		public static final java.lang.String TOTAL_FACTURAS = "totalFacturas";
		public static final java.lang.String TOTAL_NOTAS_DEBITO = "totalND";
		public static final java.lang.String TOTAL_INTERES = "totalInteres";
		public static final java.lang.String SUBTOTAL = "subTotal";
		public static final java.lang.String TOTAL_DESCUENTO = "totalDesc";
		public static final java.lang.String TOTAL_RETENIDO = "totalRetenido";
		public static final java.lang.String TOTAL_OTRAS_DEDUCCIONES = "totalOtrasDed";
		public static final java.lang.String TOTAL_NOTAS_CREDITO = "totalNC";
		public static final java.lang.String REFERENCIA = "referencia";
		public static final java.lang.String CLIENTE_ID = "objClienteID";
		public static final java.lang.String SUCURSAL_ID = "objSucursalID";
		public static final java.lang.String NOMBRE_CLIENTE = "nombreCliente";
		public static final java.lang.String COLECTOR_ID = "objColectorID";
		public static final java.lang.String APLICA_DESCUENTO_OCASIONAL = "aplicaDescOca";
		public static final java.lang.String CLAVE_AUTORIZA_DeSCUENTO_OCASIONAL = "claveAutorizaDescOca";
		public static final java.lang.String PORCENTAJE_DESCUENTO_OCASIONAL_COLECTOR = "porcDescOcaColector";
		public static final java.lang.String ESTADO_ID = "objEstadoID";
		public static final java.lang.String CODIGO_ESTADO = "codEstado";
		public static final java.lang.String DESCRICION_ESTADO = "descEstado";
		public static final java.lang.String TOTAL_DESCUENTO_OCASIONAL = "totalDescOca";
		public static final java.lang.String TOTAL_DESCUENTO_PROMOCION = "totalDescPromo";
		public static final java.lang.String TOTAL_DESCUENTO_PRONTO_PAGO = "totalDescPP";
		public static final java.lang.String TOTAL_IMPUESTO_PROPORCIONAL = "totalImpuestoProporcional";
		public static final java.lang.String TOTAL_IMPUESTO_EXONERADO = "totalImpuestoExonerado";
		public static final java.lang.String EXENTO = "exento";
		public static final java.lang.String AUTORIZA_DGI = "autorizacionDGI";
		
		public static final class DetalleFactura {
			public static final java.lang.String ID = "id";
			public static final java.lang.String FACTURA_ID = "objFacturaID";
			public static final java.lang.String RECIBO_ID = "objReciboID";			
			public static final java.lang.String MONTO = "monto";
			public static final java.lang.String ESABONO = "esAbono";
			public static final java.lang.String MONTO_DESCUENTO_ESPECIFICO = "montoDescEspecifico";
			public static final java.lang.String MONTO_DESCUENTO_OCASIONAL = "montoDescOcasional";
			public static final java.lang.String MONTO_RETENCION = "montoRetencion";
			public static final java.lang.String MONTO_IMPUESTO = "montoImpuesto";
			public static final java.lang.String MONTO_INTERES = "montoInteres";
			public static final java.lang.String MONTO_NETO = "montoNeto";
			public static final java.lang.String MONTO_OTRAS_DEDUCCIONES = "montoOtrasDeducciones";
			public static final java.lang.String MONTO_DESCUENTO_PROMOCION = "montoDescPromocion";
			public static final java.lang.String PORCENTAJE_DESCUENTO_OCASIONAL = "porcDescOcasional";
			public static final java.lang.String PORCENTAJE_DESCUENTO_PROMOCION = "porcDescPromo";
			public static final java.lang.String NUMERO = "numero";
			public static final java.lang.String FECHA = "fecha";
			public static final java.lang.String FECHA_VENCE = "fechaVence";
			public static final java.lang.String FECHA_APLICA_DESCUENTO_PRONTO_PAGO = "fechaAplicaDescPP";
			public static final java.lang.String SUB_TOTAL = "subTotal";
			public static final java.lang.String IMPUESTO = "impuesto";
			public static final java.lang.String TOTAL_FACTURA = "totalfactura";
			public static final java.lang.String SALDO_FACTURA = "saldofactura";
			public static final java.lang.String INTERES_MORATORIO = "interesMoratorio";
			public static final java.lang.String SALDO_TOTAL = "saldoTotal";
			public static final java.lang.String MONTO_IMPUESTO_EXONERADO = "montoImpuestoExento";
			public static final java.lang.String MONTO_DESCUENTO_ESPECIFICO_CALCULADO = "montoDescEspecificoCalc";
		}
		
		public static final class DetalleNotaDebito {
			public static final java.lang.String ID = "id";
			public static final java.lang.String NOTADEBITO_ID = "objNotaDebitoID";
			public static final java.lang.String RECIBO_ID = "objReciboID";
			public static final java.lang.String MONTO_INTERES = "montoInteres";
			public static final java.lang.String ESABONO = "esAbono";
			public static final java.lang.String MONTO_PAGAR = "montoPagar";
			public static final java.lang.String NUMERO = "numero";
			public static final java.lang.String FECHA = "fecha";
			public static final java.lang.String FECHA_VENCE = "fechaVence";
			public static final java.lang.String MONTO_ND = "montoND";
			public static final java.lang.String SALDO_ND = "saldoND";
			public static final java.lang.String INTERES_MORATORIO = "interesMoratorio";
			public static final java.lang.String SALDO_TOTAL = "saldoTotal";
			public static final java.lang.String MONTO_NETO = "montoNeto";				
		}
		
		public static final class DetalleNotaCredito {
			public static final java.lang.String ID = "id";
			public static final java.lang.String NOTACREDITO_ID = "objNotaCreditoID";
			public static final java.lang.String RECIBO_ID = "objReciboID";
			public static final java.lang.String MONTO = "monto";	
			public static final java.lang.String NUMERO = "numero";
			public static final java.lang.String FECHA = "fecha";
			public static final java.lang.String FECHA_VENCE = "fechaVence";	
		}
		
		public static final class DetalleFormaPago {
			public static final java.lang.String ID = "Id";
			public static final java.lang.String RECIBO_ID = "objReciboID";
			public static final java.lang.String FORMA_PAGO_ID = "ObjFormaPagoID";
			public static final java.lang.String COD_FORMA_PAGO = "CodFormaPago";
			public static final java.lang.String DESC_FORMA_PAGO = "DescFormaPago";
			public static final java.lang.String NUMERO = "Numero";
			public static final java.lang.String MONEDA_ID = "ObjMonedaID";
			public static final java.lang.String COD_MONEDA = "CodMoneda";
			public static final java.lang.String DESC_MONEDA = "DescMoneda";
			public static final java.lang.String MONTO = "Monto";
			public static final java.lang.String MONTO_NACIONAL = "MontoNacional";
			public static final java.lang.String ENTIDAD_ID = "ObjEntidadID";
			public static final java.lang.String COD_ENTIDAD = "CodEntidad";
			public static final java.lang.String DESC_ENTIDAD = "DescEntidad";
			public static final java.lang.String FECHA = "Fecha";
			public static final java.lang.String SERIE_BILLETES = "SerieBilletes";
			public static final java.lang.String TASA_CAMBIO = "TasaCambio";
		}
	}

}
