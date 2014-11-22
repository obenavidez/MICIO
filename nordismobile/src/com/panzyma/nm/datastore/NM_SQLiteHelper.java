package com.panzyma.nm.datastore;
 
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
 
public class NM_SQLiteHelper extends SQLiteOpenHelper 
{
	private static  String DATABASE_NAME = "SIMFAC";
	Context mycontext;
    String sqlCliente=       "CREATE TABLE IF NOT EXISTS Cliente (IdCliente BLOB PRIMARY KEY  NOT NULL  UNIQUE , NombreCliente TEXT, IdSucursal BLOB NOT NULL,"+ 
						 	 "Codigo TEXT, CodTipoPrecio TEXT, DesTipoPrecio TEXT, objPrecioVentaID BLOB,  objCategoriaClienteID BLOB,"+
						     "objTipoClienteID BLOB, AplicaBonificacion INTEGER, PermiteBonifEspecial INTEGER, PermitePrecioEspecial INTEGER, UG TEXT,"+
						     "Ubicacion TEXT, NombreLegalCliente TEXT, AplicaOtrasDeducciones INTEGER, MontoMinimoAbono FLOAT, PlazoDescuento INTEGER, PermiteDevolucion INTEGER);";
    
	    String sqlFactura=   "CREATE TABLE IF NOT EXISTS Factura (Id BLOB PRIMARY KEY  NOT NULL UNIQUE,NombreSucursal TEXT,"+
	                         "NoFactura TEXT,Tipo TEXT,NoPedido TEXT,CodEstado TEXT,"+
						     "Estado TEXT,Fecha TEXT,FechaVencimiento TEXT,FechaAppDescPP TEXT,"+
						     "Dias INTEGER,TotalFacturado FLOAT,Abonado FLOAT,Descontado FLOAT,"+
						     "Retenido FLOAT,Otro FLOAT,Saldo FLOAT,Exenta INTEGER,SubtotalFactura FLOAT,"+
						     "DescuentoFactura FLOAT,ImpuestoFactura FLOAT,PuedeAplicarDescPP INTEGER,objSucursalID BLOB,"+
	    				     "FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlPromocionCobro="CREATE TABLE IF NOT EXISTS PromocionCobro (FacturasAplicacion TEXT, TipoDescuento TEXT, Descuento TEXT," +
    						 "objFacturaID BLOB, FOREIGN KEY(objFacturaID) REFERENCES Factura(Id));";
    
    String sqlMontoProveedor="CREATE TABLE IF NOT EXISTS MontoProveedor (_ID INTEGER PRIMARY KEY AUTOINCREMENT,ObjProveedorID BLOB, " +
    						 "CodProveedor TEXT, Monto FLOAT, objFacturaID BLOB, FOREIGN KEY(objFacturaID) REFERENCES Factura(Id));";
    
    String sqlCCNotaCredito= "CREATE TABLE IF NOT EXISTS CCNotaCredito (Id BLOB PRIMARY KEY  NOT NULL , NombreSucursal TEXT,"+
    						 "Estado TEXT, Numero TEXT, Fecha TEXT, FechaVence TEXT,"+
    						 "Concepto TEXT, Monto FLOAT, NumRColAplic TEXT, CodEstado TEXT,"+ 
    						 "Descripcion TEXT, Referencia INTEGER, CodConcepto TEXT, objSucursalID BLOB,"+
    					     "FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlCCNotaDebito=  "CREATE TABLE IF NOT EXISTS CCNotaDebito (Id BLOB PRIMARY KEY  NOT NULL, NombreSucursal TEXT,"+
    					     "Estado TEXT, Numero TEXT, Fecha TEXT, FechaVence TEXT, Dias INTEGER,"+ 
    					     "Concepto TEXT, Monto FLOAT, MontoAbonado FLOAT, Saldo FLOAT, CodEstado TEXT,"+ 
    					     "Descripcion TEXT, objSucursalID BLOB,"+
    					     "FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlDescuentoProveedor="CREATE TABLE IF NOT EXISTS DescuentoProveedor (_ID INTEGER PRIMARY KEY AUTOINCREMENT,ObjProveedorID BLOB, PrcDescuento FLOAT," +
    							 "objSucursalID BLOB,FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal));";
    
    String sqlLote=              "CREATE TABLE IF NOT EXISTS Lote (Id BLOB,NumeroLote TEXT, FechaVencimiento INTEGER,ObjProductoID BLOB,FOREIGN KEY(ObjProductoID) REFERENCES Producto(Id));";
    
    String sqlProducto=          "CREATE TABLE IF NOT EXISTS Producto (Id BLOB PRIMARY KEY  NOT NULL,Codigo TEXT, Nombre TEXT," +
			 												"EsGravable INTEGER,ListaPrecios TEXT,ListaBonificaciones TEXT,CatPrecios TEXT,Disponible INTEGER,"+
			 												"PermiteDevolucion INTEGER,LoteRequerido INTEGER,ObjProveedorID BLOB,DiasAntesVen INTEGER,DiasDespuesVen INTEGER );";
    
    String sqlCatalogo=          "CREATE TABLE IF NOT EXISTS Catalogo (Id INTEGER PRIMARY KEY NOT NULL,NombreCatalogo TEXT);";
    
    String sqlValorCatalogo=     "CREATE TABLE IF NOT EXISTS ValorCatalogo (Id INTEGER PRIMARY KEY,Codigo TEXT, Descripcion TEXT,"+
    		                     "objCatalogoID INTEGER,FOREIGN KEY(objCatalogoID) REFERENCES Catalogo(Id));";
    
    String sqlTasaCambio=        "CREATE TABLE IF NOT EXISTS TasaCambio(Id INTEGER PRIMARY KEY AUTOINCREMENT,CodMoneda TEXT,Fecha INTEGER,Tasa FLOAT); ";
    
    String sqlPromocion=         "CREATE TABLE IF NOT EXISTS Promocion(Id BLOB PRIMARY KEY,Codigo TEXT,Descripcion TEXT,AplicaCredito INTEGER,FechaFin INTEGER,"+
    							 "MomentoAplicacion TEXT,TipoPromo TEXT,MontoMinimo FLOAT,TipoDescuento TEXT,Descuento FLOAT,AplicacionMultiple INTEGER,CantidadMinimaItems INTEGER,"+
    							 "CantidadMinimaBaseUnica INTEGER,CantidadMinimaBase INTEGER,CantidadPremioUnica INTEGER, CantidadPremio INTEGER,ProductosOtorgadosPor TEXT,"+
    							 "MontoEntregadoPor TEXT,DescripcionPromocion TEXT,CatClientes TEXT,TiposCliente TEXT,Sucursales TEXT,Ubicaciones TEXT,ProdsBase TEXT,ProdsPremio TEXT,"+
    							 "MontoBaseUnico INTEGER,MontoBaseMinimo INTEGER,MontoBaseMaximo INTEGER,MontoPremioUnico INTEGER,MontoPremio FLOAT); ";

    String sqlPedido=         "CREATE TABLE IF NOT EXISTS Pedido("
    		+ "Id INTEGER PRIMARY KEY AUTOINCREMENT, "
    		+ "NumeroMovil INTEGER,"
    		+ "NumeroCentral INTEGER,"
    		+ "Tipo TEXT,"
    		+ "Fecha INTEGER,"
    		+ "objClienteID BLOB,"
			+ "NombreCliente TEXT,"
			+ "objSucursalID BLOB,"
			+ "NombreSucursal TEXT,"
			+ "objTipoPrecioVentaID BLOB,"
			+ "CodTipoPrecio TEXT,"
			+ "DescTipoPrecio TEXT,"
			+ "objVendedorID BLOB,"
			+ "BonificacionEspecial INTEGER,"
			+ "BonificacionSolicitada TEXT, "
			+ "PrecioEspecial INTEGER,"
			+ "PrecioSolicitado TEXT,"
			+ "PedidoCondicionado INTEGER,"
			+ "Condicion TEXT,"
			+ "Subtotal FLOAT,"
			+ "Descuento FLOAT,"
			+ "Impuesto FLOAT,"
			+ "Total FLOAT,"
			+ "objEstadoID BLOB,"
			+ "CodEstado TEXT,"
			+ "DescEstado TEXT,"
			+ "objCausaEstadoID BLOB,"
			+ "CodCausaEstado TEXT,"
			+ "DescCausaEstado TEXT,"
			+ "NombreVendedor TEXT,"
			+ "Nota TEXT,"
			+ "Exento INTEGER,"
			+ "AutorizacionDGI TEXT, " 
			+ "FOREIGN KEY(objClienteID) REFERENCES Cliente(IdCliente), " 
			+ "FOREIGN KEY(objSucursalID) REFERENCES Cliente(IdSucursal) "
			+ "); " ;

    String sqlDetallePedido = "CREATE TABLE IF NOT EXISTS PedidoDetalle (" +
                              "       Id INTEGER PRIMARY KEY AUTOINCREMENT,       " +
                              "       objPedidoID BLOB,       " +
                              "       objProductoID BLOB, " + 
                              "       CodProducto TEXT, " + 
                              "       NombreProducto TEXT, " +
                              "       CantidadOrdenada INTEGER, " + 
                              "       CantidadBonificada INTEGER, " + 
                              "       objBonificacionID BLOB, " + 
                              "       BonifEditada INTEGER,  " +
                              "       CantidadBonificadaEditada INTEGER,  " +
                              "       Precio FLOAT, " +
                              "       MontoPrecioEditado FLOAT, " + 
                              "       PrecioEditado INTEGER, " + 
                              "       Subtotal FLOAT, " + 
                              "       Descuento FLOAT, " + 
                              "       PorcImpuesto FLOAT, " + 
                              "       Impuesto FLOAT, " + 
                              "       Total FLOAT, " + 
                              "       CantidadDespachada INTEGER, " + 
                              "       CantidadADespachar INTEGER, " + 
                              "       CantidadPromocion INTEGER, " + 
                              "       FOREIGN KEY(objPedidoID) REFERENCES Pedido(Id), " + 
                              "       FOREIGN KEY(objProductoID) REFERENCES Producto(Id) " + 
                              ");" ;
    
    String sqlPedidoPromocion = "CREATE TABLE IF NOT EXISTS PedidoPromocion (" + 
            "       Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "       objPromocionID BLOB,       " +
            "       objPedidoID BLOB,       " +
            "       Descuento FLOAT, " +  
            "       CodigoPromocion TEXT, " +
            "       NombrePromocion TEXT, " +  
            "       FOREIGN KEY(objPromocionID) REFERENCES Promocion(Id), " +
            "       FOREIGN KEY(objPedidoID) REFERENCES Pedido(Id) " + 
            ");" ;
    String sqlPedidoPromocionDetalle = "CREATE TABLE IF NOT EXISTS PedidoPromocionDetalle (" + 
    		"       Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
    		"       objProductoID BLOB,       " +
            "       objPromocionID BLOB,       " + 
            "       objPedidoID BLOB,       " +
            "       Descuento FLOAT, " +  
            "       NombreProducto TEXT, " +
            "       CantidadEntregada INTEGER, " +  
            "       FOREIGN KEY(objPromocionID) REFERENCES Promocion(Id), " + 
            "       FOREIGN KEY(objProductoID) REFERENCES Producto(Id) " +
            "       FOREIGN KEY(objPedidoID) REFERENCES Pedido(Id) " + 
            ");" ;
    
    String sqlUsuario =     "CREATE TABLE IF NOT EXISTS Usuario(" +
							"       Id BLOB PRIMARY KEY,       " +
							"       Login TEXT,       " +
							"       Nombre TEXT, " + 
							"       Sexo TEXT, " + 
							"       AccedeModuloPedidos INTEGER, " + 
							"       PuedeEditarPrecioAbajo INTEGER, " + 
							"       PuedeEditarPrecioArriba INTEGER, " + 
							"       PuedeEditarBonifAbajo INTEGER,  " +
							"       PuedeEditarBonifArriba INTEGER,  " +
							"       IsAdmin INTEGER, " +
							"       PuedeCrearPedido INTEGER, " + 
							"       PuedeConsultarPedido INTEGER, " + 
							"       Codigo TEXT, " + 
							"       PuedeEditarDescPP INTEGER " +   
							");" ;
    
    String sqlRecibo = "CREATE TABLE IF NOT EXISTS Recibo ("
					+ "       id INTEGER PRIMARY KEY AUTOINCREMENT,  "
					+ "       numero INTEGER,       " 
					+ "       fecha TEXT, "
					+ "       notas TEXT, " 
					+ "       totalRecibo FLOAT, "
					+ "       totalFacturas FLOAT, "
					+ "       totalND FLOAT, "
					+ "       totalInteres FLOAT,  "
					+ "       subTotal FLOAT,  "
					+ "       totalDesc FLOAT, " 
					+ "       totalRetenido FLOAT, "
					+ "       totalOtrasDed FLOAT, " 
					+ "       totalNC FLOAT, "
					+ "       referencia INTEGER, "
					+ "       objClienteID BLOB, " 
					+ "       objSucursalID BLOB, "
					+ "       nombreCliente TEXT, "
					+ "       objColectorID BLOB, "
					+ "       aplicaDescOca INTEGER, "
					+ "       claveAutorizaDescOca TEXT, "
					+ "       porcDescOcaColector FLOAT, "
					+ "       objEstadoID BLOB, "
					+ "       codEstado TEXT, "
					+ "       descEstado TEXT, "
					+ "       totalDescOca FLOAT, " 
					+ "       totalDescPromo FLOAT, " 
					+ "       totalDescPP FLOAT, " 
					+ "       totalImpuestoProporcional FLOAT, " 
					+ "       totalImpuestoExonerado FLOAT, " 
					+ "       exento FLOAT, "
					+ "       autorizacionDGI FLOAT, "
					+ "       FOREIGN KEY(objClienteID) REFERENCES Cliente(IdCliente) "  
					+ ");";
    
    String sqlReciboDetalleFatura = "CREATE TABLE IF NOT EXISTS ReciboDetalleFactura ("
			+ "       id INTEGER PRIMARY KEY AUTOINCREMENT,  "
    		+ "       objFacturaID BLOB, "		
    		+ "       objReciboID BLOB, "		
			+ "       monto FLOAT, "
			+ "       esAbono INTEGER, "
			+ "       montoDescEspecifico FLOAT, "
			+ "       montoDescOcasional FLOAT,  "
			+ "       montoRetencion FLOAT,  "
			+ "       montoImpuesto FLOAT, " 
			+ "       montoInteres FLOAT, "
			+ "       montoNeto FLOAT, " 
			+ "       montoOtrasDeducciones FLOAT, "
			+ "       montoDescPromocion FLOAT, "
			+ "       porcDescOcasional FLOAT, " 
			+ "       porcDescPromo FLOAT, "
			+ "       numero TEXT, "
			+ "       fecha BLOB, "
			+ "       fechaVence BLOB, "
			+ "       fechaAplicaDescPP BLOB, "			
			+ "       subTotal FLOAT, "
			+ "       impuesto FLOAT, "
			+ "       totalfactura FLOAT, "
			+ "       saldofactura FLOAT, "
			+ "       interesMoratorio FLOAT, " 
			+ "       saldoTotal FLOAT, " 
			+ "       montoImpuestoExento FLOAT, " 
			+ "       montoDescEspecificoCalc FLOAT, " 			
            + "       FOREIGN KEY(objReciboID) REFERENCES Recibo(id) " 
			+ ");";
    
    String sqlReciboDetalleNotaDebito = "CREATE TABLE IF NOT EXISTS ReciboDetalleNotaDebito ("
			+ "       id INTEGER PRIMARY KEY AUTOINCREMENT,  "
    		+ "       objNotaDebitoID BLOB, "	
    		+ "       objReciboID BLOB, "	
			+ "       montoInteres FLOAT, "
			+ "       esAbono INTEGER, "
			+ "       montoPagar FLOAT, "		
			+ "       numero TEXT, "
			+ "       fecha BLOB, "
			+ "       fechaVence BLOB, "
			+ "       montoND FLOAT, "			
			+ "       saldoND FLOAT, "
			+ "       interesMoratorio FLOAT, "
			+ "       saldoTotal FLOAT, "
			+ "       montoNeto FLOAT, "			 			
            + "       FOREIGN KEY(objReciboID) REFERENCES Recibo(id) " 
			+ ");";
    
    String sqlReciboDetalleNotaCredito = "CREATE TABLE IF NOT EXISTS ReciboDetalleNotaCredito ("
			+ "       id INTEGER PRIMARY KEY AUTOINCREMENT,  "
    		+ "       objNotaCreditoID BLOB, "	
    		+ "       objReciboID BLOB, "	
			+ "       monto FLOAT, "				
			+ "       numero TEXT, "
			+ "       fecha BLOB, "
			+ "       fechaVence BLOB, "						 			
            + "       FOREIGN KEY(objReciboID) REFERENCES Recibo(id) " 
			+ ")";
    
    String sqlReciboDetalleFormaPago = "CREATE TABLE IF NOT EXISTS ReciboDetalleFormaPago ("
			+ "       Id INTEGER PRIMARY KEY AUTOINCREMENT,  "
			+ "       objReciboID BLOB, "
    		+ "       ObjFormaPagoID BLOB, "	
    		+ "       CodFormaPago TEXT, "	
			+ "       DescFormaPago TEXT, "				
			+ "       Numero TEXT, "
			+ "       ObjMonedaID BLOB, "			
			+ "       CodMoneda TEXT, "	
			+ "       DescMoneda TEXT, "
			+ "       Monto FLOAT, "
			+ "       MontoNacional FLOAT, "
			+ "       ObjEntidadID BLOB, "			
			+ "       CodEntidad TEXT, "
			+ "       DescEntidad TEXT, "
			+ "       Fecha BLOB, "	
			+ "       SerieBilletes TEXT, "
			+ "       TasaCambio FLOAT, "			
            + "       FOREIGN KEY(objReciboID) REFERENCES Recibo(id) " 
			+ ");";
    
    String sqlDrop_Cliente=			  "DROP TABLE IF EXISTS Cliente";
    String sqlDrop_Factura=			  "DROP TABLE IF EXISTS Factura";
    String sqlDrop_PromocionCobro=	  "DROP TABLE IF EXISTS PromocionCobro";
    String sqlDrop_MontoProveedor=	  "DROP TABLE IF EXISTS MontoProveedor";
    String sqlDrop_CCNotaCredito=	  "DROP TABLE IF EXISTS CCNotaCredito";
    String sqlDrop_CCNotaDebito=	  "DROP TABLE IF EXISTS CCNotaDebito";
    String sqlDrop_DescuentoProveedor="DROP TABLE IF EXISTS DescuentoProveedor";  
    String sqlDrop_Producto=		  "DROP TABLE IF EXISTS Producto";
	String sqlDrop_Lote=			  "DROP TABLE IF EXISTS Lote";
	String sqlDrop_Catalogo=		  "DROP TABLE IF EXISTS Catalogo";
	String sqlDrop_ValorCatalogo=	  "DROP TABLE IF EXISTS ValorCatalogo";
	String sqlDrop_TasaCambio=	      "DROP TABLE IF EXISTS TasaCambio";
	String sqlDrop_Promocion=	      "DROP TABLE IF EXISTS Promocion";
	String sqlDrop_Usuario=	          "DROP TABLE IF EXISTS Usuario";
	String sqlDrop_Recibo =           "DROP TABLE IF EXISTS Recibo";
    String sqlDrop_ReciboDetalleFactura = "DROP TABLE IF EXISTS ReciboDetalleFactura"; 
    String sqlDrop_ReciboDetalleND = "DROP TABLE IF EXISTS ReciboDetalleNotaDebito";
    String sqlDrop_ReciboDetalleNC = "DROP TABLE IF EXISTS ReciboDetalleNotaCredito";
    String sqlDrop_ReciboDetalleFormaPago = "DROP TABLE IF EXISTS ReciboDetalleFormaPago ";
    
    
    String sqlDrop_Pedido = "DROP TABLE IF EXISTS Pedido";
    String sqlDrop_PedidoDetalle = "DROP TABLE IF EXISTS PedidoDetalle";
    String sqlDrop_PedidoPromocion = "DROP TABLE IF EXISTS PedidoPromocion";
    String sqlDrop_PedidoPromocionDetalle = "DROP TABLE IF EXISTS PedidoPromocionDetalle";
	
    String sqlDeleteDesProv=  		"DELETE FROM DescuentoProveedor";
    String sqlDeleteND=       		"DELETE FROM CCNotaDebito";
    String sqlDeleteNC=      		"DELETE FROM CCNotaCredito";
    String sqlDeleteMontProv= 		"DELETE FROM MontoProveedor";
    String sqlDeletePromCobro=		"DELETE FROM PromocionCobro";
    String sqlDeleteFactura=  		"DELETE FROM Factura";
    String sqlDeleteCliente=  		"DELETE FROM Cliente"; 
    String sqlDeleteProducto= 		"DELETE FROM Producto";
    String sqlDeleteLote=     		"DELETE FROM Lote";
    String sqlDeleteCatalogo= 		"DELETE FROM Catalogo";
    String sqlDeleteValorCatalogo=  "DELETE FROM ValorCatalogo";
    String sqlDeleteTasaCambio=     "DELETE FROM TasaCambio";
    String sqlDeletePromocion=      "DELETE FROM Promocion";
    String sqlDelete_Usuario=	    "DELETE FROM Usuario";
    String sqlDelete_Recibo =       "DELETE FROM  Recibo";
    String sqlDelete_ReciboDetalleFactura = "DELETE FROM  ReciboDetalleFactura"; 
    String sqlDelete_ReciboDetalleND = "DELETE FROM ReciboDetalleNotaDebito";
    String sqlDelete_ReciboDetalleNC = "DELETE FROM ReciboDetalleNotaCredito";
    
    
    String sqlDelete_Pedido =       "DELETE FROM  Pedido";
    String sqlDelete_PedidoDetalle = "DELETE FROM  PedidoDetalle"; 
    String sqlDelete_PedidoPromocion = "DELETE FROM PedidoPromocion";
    String sqlDelete_PedidoPromocionDetalle = "DELETE FROM PedidoPromocionDetalle";
    String sqlDelete_ReciboDetalleFormaPago = "DROP TABLE IF EXISTS ReciboDetalleFormaPago ";

    
    public NM_SQLiteHelper(Context contexto, String nombre, CursorFactory factory, int version) 
    { 
        super(contexto, nombre, factory, version);
        this.mycontext=contexto;
        DATABASE_NAME=nombre;
    } 
    @Override
    public void onCreate(SQLiteDatabase db) 
    { 
    	CREATE_TABLES(db);
    }
    
    @Override
	public synchronized void close() {
		 
		super.close();
	}
    
	@Override
	public void onOpen(SQLiteDatabase db) {
		 
		super.onOpen(db);
	}
	
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva)
    {
    	DROP_TABLES(db);
        CREATE_TABLES(db); 
    }
    
    public void CREATE_TABLES(SQLiteDatabase db)
    { 
    	try 
    	{ 
    		if(!db.isOpen())
    			db=this.mycontext.openOrCreateDatabase("data/data/"+mycontext.getPackageName()+"/databases/"+DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);    		      		    
		    db.execSQL(sqlCliente);
			db.execSQL(sqlFactura);
			db.execSQL(sqlPromocionCobro);
			db.execSQL(sqlMontoProveedor);
			db.execSQL(sqlCCNotaCredito);
			db.execSQL(sqlCCNotaDebito);
			db.execSQL(sqlDescuentoProveedor); 
			db.execSQL(sqlProducto);  
			db.execSQL(sqlLote);
			db.execSQL(sqlCatalogo);
			db.execSQL(sqlValorCatalogo);
			db.execSQL(sqlTasaCambio);
			db.execSQL(sqlPromocion);
			db.execSQL(sqlUsuario);
			db.execSQL(sqlPedido);
			db.execSQL(sqlDetallePedido);
			db.execSQL(sqlPedidoPromocion);
			db.execSQL(sqlPedidoPromocionDetalle);
			db.execSQL(sqlRecibo);
			db.execSQL(sqlReciboDetalleFatura);
			db.execSQL(sqlReciboDetalleNotaDebito);
			db.execSQL(sqlReciboDetalleNotaCredito);
			db.execSQL(sqlReciboDetalleFormaPago);
        } 
    	catch (SQLException e) 
        {
            e.printStackTrace();
        }
    	catch(Exception e)
    	{
    	    e.printStackTrace();  
    	} 

    }
    
    public void DROP_TABLES(SQLiteDatabase db)
    {
    	try 
    	{  
    		db.execSQL(sqlDrop_Cliente);
            db.execSQL(sqlDrop_Factura);
            db.execSQL(sqlDrop_PromocionCobro);
            db.execSQL(sqlDrop_MontoProveedor);
            db.execSQL(sqlDrop_CCNotaCredito);
            db.execSQL(sqlDrop_CCNotaDebito);
            db.execSQL(sqlDrop_DescuentoProveedor);  
            db.execSQL(sqlDrop_Lote); 
            db.execSQL(sqlDrop_Producto);
            db.execSQL(sqlDrop_Catalogo);
			db.execSQL(sqlDrop_ValorCatalogo);
			db.execSQL(sqlDrop_TasaCambio);
			db.execSQL(sqlDrop_Promocion);
			db.execSQL(sqlDrop_Usuario);
			db.execSQL(sqlDrop_Recibo);
			db.execSQL(sqlDrop_ReciboDetalleFactura);
			db.execSQL(sqlDrop_ReciboDetalleND);
			db.execSQL(sqlDrop_ReciboDetalleNC);			
			db.execSQL(sqlDrop_Pedido);
			db.execSQL(sqlDrop_PedidoDetalle);
			db.execSQL(sqlDrop_PedidoPromocion);
			db.execSQL(sqlDrop_PedidoPromocionDetalle);
			db.execSQL(sqlDrop_ReciboDetalleFormaPago);
        } 
    	catch (SQLException e) 
        {
            e.printStackTrace();
        }
    	catch(Exception e)
    	{
    	    e.printStackTrace();  
    	}   	  
    	
    }
}
 
