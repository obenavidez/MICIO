package com.panzyma.nm.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.interfaces.IFilterabble;
import com.panzyma.nm.interfaces.IPredicate;
import com.panzyma.nm.interfaces.Predicate;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.PrecioProducto;
import com.panzyma.nm.serviceproxy.Producto;

public class DevolucionBL 
{

	/*
	 * Devuelve el precio de un producto dada la cantidad que se está ordenando
	 */
	public static float getPrecioProducto(Producto prod, long idTipoPrecio,
			int cantidad) 
	{
		ArrayList<PrecioProducto> precios = parseListaPrecios(prod,idTipoPrecio); 
		PrecioProducto p =(precios!=null && precios.size()!=0)? precios.get(0):null ;
		if (precios.size() > 1) 
		{
			for (int i = 0; i < precios.size(); i++) 
			{
				p = precios.get(i);
				if ((cantidad >= p.getMinimo()) && (cantidad <= p.getMaximo()))
					break; // Salir del ciclo
			}
		}

		return (p!=null)?p.getPrecio():0f;
	}
	
	public static ArrayList<PrecioProducto> parseListaPrecios(Producto prod,
			long idTipoPrecio) {
		ArrayList<PrecioProducto> pp = new ArrayList<PrecioProducto>();
		String tipoPrecio = idTipoPrecio + "";
		String[] listaPrecios = StringUtil.split(prod.getListaPrecios(), "|");
		for (int i = 0; i < listaPrecios.length; i++) {
			String[] precioProd = StringUtil.split(listaPrecios[i], ",");

			if ((idTipoPrecio == 0)
					|| (tipoPrecio.compareTo(precioProd[0]) == 0)) {
				PrecioProducto p = new PrecioProducto();
				p.setObjTipoPrecioID(idTipoPrecio);
				p.setMinimo(Integer.parseInt(precioProd[1]));
				p.setMaximo(Integer.parseInt(precioProd[2]));
				p.setPrecio(Float.parseFloat(precioProd[3]));
				p.setDescTipoPrecio(precioProd[4]);
				pp.add(p);
			}
		}

		return pp;
	}

	public static float getPorcImpuesto(Pedido pedido,long idproducto)
	{
		for(DetallePedido dtp:pedido.getDetalles())
			if(dtp.getObjProductoID()==idproducto)
				return dtp.getPorcImpuesto();
		return 0.f;
	}
	
	public static int CalcularBonificacion(int CantidadDevolver, int CantidadOrdenada, int CantidadBonificada)
	{
		Double proporcion =( Double.valueOf(CantidadBonificada) / Double.valueOf(CantidadOrdenada))+new Double(1);
		Double CantBonif = new Double(CantidadDevolver)-(new Double(CantidadDevolver)/proporcion) ;
		int result;
		if (CantBonif == 0d)
		{
			result = 0;
		}
		else
		{
			
			Double decimalBonif =CantBonif-CantBonif.intValue();
			String DecimalRedondeoBonif =NMApp.getContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("DecimalRedondeoBonif","0");
			if (decimalBonif >= Double.valueOf(DecimalRedondeoBonif)) 
				CantBonif+=1;  
			result =CantBonif.intValue();
		}
		return result;
	}

	
	/*
	 * Devuelve la bonificación de un producto dada la cantidad que se está
	 * ordenando y la categoría de cliente
	 */
	public static Bonificacion getBonificacion2(Producto prod,
			long idCatCliente, int cantidad) {
		ArrayList<Bonificacion> bonificaciones = DevolucionBL.parseListaBonificaciones(prod,
				idCatCliente);
		if (bonificaciones == null)
			return null;

		Bonificacion bb = null;
		for (int i = bonificaciones.size() - 1; i >= 0; i--) {
			Bonificacion b = bonificaciones.get(i);
			if (cantidad >= b.getCantidad()) {
				bb = b; // Encontrada
				break; // Salir del ciclo
			}
		}

		// Si se encontró bonificación aplicada
		// Recalcular cantidad real a aplicar para la cantidad ordenada
		// Recordar traer valor de parámetro DecimalRedondeoBonif (por el
		// momento se usa 0.8)
		if (bb != null) {
			float decimalRedondeoBonif = 0.8F;
			float cb = bb.getCantBonificacion();
			float c = bb.getCantidad();
			float factor = (cb / c)+new Float(1.00);
			float cant =cantidad-( cantidad/factor) ;
			String sCant = String.valueOf(cant);
			String[] arrCant = StringUtil.split(sCant, ".");
			String sInt = arrCant[0];
			String sDec = "0.0";

			int cantReal = Integer.parseInt(sInt);
			if (arrCant.length > 1) {
				sDec = "0." + arrCant[1];
				float decimalPart = Float.parseFloat(sDec);
				if (decimalPart >= decimalRedondeoBonif)
					cantReal = cantReal + 1;
			}
			bb.setCantBonificacion(cantReal);
		}

		return bb;
	} 
	
	
	/*
	 * Devuelve la bonificación de un producto dada la cantidad que se está
	 * ordenando y la categoría de cliente
	 */
	public static Bonificacion getBonificacion(Producto prod,
			long idCatCliente, int cantidad) {
		ArrayList<Bonificacion> bonificaciones = DevolucionBL.parseListaBonificaciones(prod,
				idCatCliente);
		if (bonificaciones == null)
			return null;

		Bonificacion bb = null;
		for (int i = bonificaciones.size() - 1; i >= 0; i--) {
			Bonificacion b = bonificaciones.get(i);
			if (cantidad >= b.getCantidad()) {
				bb = b; // Encontrada
				break; // Salir del ciclo
			}
		}

		// Si se encontró bonificación aplicada
		// Recalcular cantidad real a aplicar para la cantidad ordenada
		// Recordar traer valor de parámetro DecimalRedondeoBonif (por el
		// momento se usa 0.8)
		if (bb != null) {
			float decimalRedondeoBonif = 0.8F;
			float cb = bb.getCantBonificacion();
			float c = bb.getCantidad();
			float factor = cb / c;
			float cant = factor * cantidad;
			String sCant = String.valueOf(cant);
			String[] arrCant = StringUtil.split(sCant, ".");
			String sInt = arrCant[0];
			String sDec = "0.0";

			int cantReal = Integer.parseInt(sInt);
			if (arrCant.length > 1) {
				sDec = "0." + arrCant[1];
				float decimalPart = Float.parseFloat(sDec);
				if (decimalPart >= decimalRedondeoBonif)
					cantReal = cantReal + 1;
			}
			bb.setCantBonificacion(cantReal);
		}

		return bb;
	} 
	
	public static ArrayList<Bonificacion> parseListaBonificaciones(
			Producto prod, long idCatCliente) 
			{
		if (prod.getListaBonificaciones() == null)
			return null;
		if (prod.getListaBonificaciones() == "")
			return null;

		ArrayList<Bonificacion> abon = new ArrayList<Bonificacion>();
		String catCLiente = idCatCliente + "";
		String[] listaBonif = StringUtil.split(prod.getListaBonificaciones(),
				"|");
		for (int i = 0; i < listaBonif.length; i++)
		{
			if(!listaBonif[0].equals(""))
			{	
				String[] bonifProd = StringUtil.split(listaBonif[i], ",");
		
				if(bonifProd!=null && bonifProd.length!=0 && bonifProd[0]!="")
				if ((idCatCliente == 0) || (catCLiente.compareTo(bonifProd[1]) == 0)) {
					Bonificacion b = new Bonificacion();
					b.setObjBonificacionID(Long.parseLong(bonifProd[0]));
					b.setObjCategoriaClienteID(idCatCliente);
					b.setCantidad(Integer.parseInt(bonifProd[2]));
					b.setCantBonificacion(Integer.parseInt(bonifProd[3]));
					b.setCategoriaCliente(bonifProd[4]);
					abon.add(b);
				}
			}
			else
				break;
		}

		return abon;
	}
	
	public static double CalMontoPromocion(Cliente cliente, Pedido pedido,List<DevolucionProducto> detalledev, boolean isTotalDev)
	{
		
		if(pedido.getPromocionesAplicadas()==null || pedido.getPromocionesAplicadas().length==0)
			return 0.d;
		if(isTotalDev)
			return DeducirMontoDevPromocion(pedido,detalledev,isTotalDev);
		 
		//productos que se está devolviendo el total facturado ó el total ordenado y parte de lo entregado en concepto de bonificación ó 
        //en concepto de promoción.		
		List<DevolucionProducto> productosadevueltos = (List<DevolucionProducto>) Predicate.filter(detalledev,null,
	    new IPredicate<DevolucionProducto,DevolucionProducto>() 
	    {
			@Override
	        public boolean apply(DevolucionProducto dp,
					List<DevolucionProducto>... objList) 
	        {
	        	return dp.getCantidadDevolver()>=dp.getCantidadOrdenada();
	        }
	    });
		 //Crear vista de los productos que el cliente se está quedando con cierta cantidad de la ordenada
		List<DevolucionProducto> productosdevparcial = (List<DevolucionProducto>) Predicate.filter(detalledev,null,
	    new IPredicate<DevolucionProducto,DevolucionProducto>() 
	    {  
			@Override
			public boolean apply(DevolucionProducto dp,
					List<DevolucionProducto>... objList) {
				return  dp.getCantidadDevolver()<dp.getCantidadOrdenada() && dp.getCantidadDevolver()>0;
			} 
	    });
		
		//productos que estan el pedido y no en los productos a devolver
		List<DetallePedido> detalleprodpedido = (List<DetallePedido>) Predicate.filter(Arrays.asList(pedido.getDetalles()),productosadevueltos,
	    new IPredicate<DetallePedido,DevolucionProducto>() 
	    {
			@Override
			public boolean apply(DetallePedido detallepedido,
					List<DevolucionProducto>... objList) 
			{
				for(DevolucionProducto dprod:objList[0])
				{
					if(dprod.getObjProductoID()==detallepedido.getObjProductoID())
						return false;
				}				
				return true;
			} 
	       
	    }); 
		
		for(DevolucionProducto dpparcial:productosdevparcial)
		{
			DetallePedido  detp=Predicate.find(detalleprodpedido, dpparcial.getObjProductoID(), new IFilterabble<DetallePedido>() {

				@Override
				public boolean search(DetallePedido detp, long ID) { 
					return detp.getObjPedidoID()==ID;
				}
			});
			//Actualizar la cantidad con la que se queda el cliente 
			detp.setCantidadOrdenada(dpparcial.getCantidadOrdenada()-dpparcial.getCantidadDevolver());
			               
            //dr["CantidadOrdenada"] = Convert.ToInt32(drw["PCantidadOrdenada"]) - Convert.ToInt32(drw["DCantidadDevolver"]);

            //Actualizar el precio con la nueva cantidad ordenada. Si el pedido es generado a partir de una liciatación, 
            //se mantiene el precio de licitación
			
			
			detp.setPrecio(GetPrecioProducto(
					detp.getObjProductoID(),
					cliente.getObjPrecioVentaID(),
					detp.getCantidadOrdenada(),
					detp.getBonifEditada(),
					cliente.getObjTipoClienteID()));  
			
			float prcImp = Float.valueOf((NMApp.getContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("PorcentajeImpuesto", "0")));
            //Calcular los nuevos totales del cada detalle del pedido
			detp.setDescuento(0.f);
			detp.setSubtotal(detp.getCantidadOrdenada()*detp.getPrecio()); 
            //Calcular el nuevo impuesto solo a los productos que se les habia calculado
            if (detp.getImpuesto()> 0) {
            	 detp.setPorcImpuesto(prcImp);
            	detp.setImpuesto((detp.getSubtotal()* prcImp)/100);
            }
            detp.setTotal(detp.getSubtotal()+detp.getImpuesto()); 
			//Si sé está devolviendo todos los productos del pedido menos los entregados en concepto 
            //promoción y que no estaban en el detalle del pedido, deducir monto por promoción
           //linea 1908 falta de la  1883 a la 1907 en CalMontoPromocion de la desktop
			
            
		}
		if (productosadevueltos==null || productosadevueltos.size() == 0)
             return DeducirMontoDevPromocion(pedido, detalledev);
            
		return 0.d;
	}
	
	public static float GetPrecioProducto(long productID,long precioventaID,int cantidadOrdenada,boolean viabonif,long tipoclienteID)
	{
		return 0.f;
		
	}
	
	/*private static double AplicarPromociones(Pedido pedido,List<DetallePedido> detp,List<DevolucionProducto>  detdp )
	{
		List<PedidoPromocion>pp=Arrays.asList(pedido.getPromocionesAplicadas());
		if(pp==null)
			return DeducirMontoDevPromocion(pedido, detdp);	
		
	}*/
	
	public static double DeducirMontoDevPromocion(Pedido pedido,List<DevolucionProducto> detalledev)
	{
		double monto=0;
		monto+=pedido.getDescuento(); 
		//productos que estan el pedido y no en los productos a devolver
		List<DetallePedido> prodpromocionados = (List<DetallePedido>) Predicate.filter(Arrays.asList(pedido.getDetalles()),null,
	    new IPredicate<DetallePedido,DevolucionProducto>() 
	    {
			@Override
			public boolean apply(DetallePedido detpedido,
					List<DevolucionProducto>... objList) 
			{				 			
				return detpedido.getCantidadPromocion()>0 && detpedido.getObjProductoID()!=0;
			} 
	       
	    }); 
		monto += DeducirMontoPromocion(pedido, detalledev, prodpromocionados);

        //
        return monto;
	}
	
	public static double DeducirMontoDevPromocion(Pedido pedido,List<DevolucionProducto> detalledev, boolean isTotalDev)
	{
		double monto=0d;
		if(isTotalDev)
		{
			monto+=pedido.getDescuento();
		}
		for(int a=0;a<detalledev.size();a++)
		{
			DevolucionProducto _dp=detalledev.get(a);
			monto+=_dp.getCantidadPromocionada()*_dp.getPrecio();
		 
			if(!isTotalDev)
			{
				monto+=CalcDescProporcional(
				_dp.getCantidadOrdenada(),
				_dp.getCantidadBonificada(),
				_dp.getCantidadPromocionada(),
				_dp.getCantidadDevolver(),
				_dp.getDescuento());
			}
		}
		return monto;
	}
	
	private static double CalcDescProporcional(int cantOrdenada,
											 int cantBonificada,
											 int cantPromocion,
											 int cantDevolucion,
											 long descuento){
		if(cantOrdenada==0 || descuento==0)
			return 0;
		
		double descProporcional;//descuento proporcional aplicado a canti a devolver
		double cantDevolver=0;//sin bonif ni promociones
		
		if(cantDevolucion<=cantOrdenada)
			cantDevolver=cantDevolucion;
		if(cantDevolucion>=(cantOrdenada+cantBonificada))
			cantDevolver=cantDevolucion-cantBonificada;
		
		if(cantDevolucion>=(cantOrdenada+cantBonificada+cantPromocion))
			cantDevolver=cantDevolucion-cantBonificada-cantPromocion;
		
		descProporcional=descuento/cantOrdenada;
		descProporcional=descProporcional*cantDevolver;
		
		return descProporcional;
	}

	private static double DeducirMontoPromocion(Pedido pedido,List<DevolucionProducto> detalledev,List<DetallePedido> prodpromocionados)
	{
		/*
		 * 
		 * /// <summary>
	        /// Calcula el monto a deducir por promoción (Ninguna promoción aplica)
	        /// </summary>
	        /// <param name="pedido">Pedido a devolver</param>
	        /// <param name="dtDevDetalles">Detalles de la devolución</param>
	        /// <returns></returns>
	        private static decimal DeducirMontoPromocion(SfaPedido pedido, DataTable dtDevDetalles, DataTable dtProductoPromo)
	        {
	            decimal monto = 0;
	            decimal precio = 0;

	            //Apartir de los productos promocionados y los detalle de la devolución,deducir el monto total de los productos otorgados en promoción
	            //y que no se están devolviendo
	            dtDevDetalles.PrimaryKey = new DataColumn[] { dtDevDetalles.Columns["PobjProductoID"] };
	            dtDevDetalles.DefaultView.Sort = "PobjProductoID";

	            //Verificar cada producto promocionado
	            SccCliente cliente = db.GetObject<SccCliente>(pedido.objClienteID);
	            int cantDeducir;
	            foreach (DataRow drPP in dtProductoPromo.Rows)
	            {
	                //
	                cantDeducir = 0;
	                precio = 0;
	                DataRow drDD = dtDevDetalles.Rows.Find(drPP["objProductoID"]);

	                //Si no sé está devolviendo, calcular el monto por el total promocionado
	                if (drDD == null)
	                {
	                    //Cantidad a deducir:
	                    cantDeducir = Convert.ToInt32(drPP["Promocion"]);

	                    //Precio a aplicar:
	                    //Si el producto no pertenecia al detalle del pedido,calcular el monto con el precio de venta 
	                    //definido para el cliente
	                    if (Convert.ToDecimal(drPP["MontoPrecioEditado"]) == 0)
	                        precio = InventarioBL.GetPrecioProducto(Convert.ToInt64(drPP["objProductoID"]), cliente.objPrecioVentaID, cantDeducir, false, cliente.objTipoClienteID);
	                    //Si no, se calcula con el precio con que se facturó
	                    else
	                        precio = Convert.ToDecimal(drPP["MontoPrecioEditado"]);
	                }
	                //si no, calcular el monto por la cantidad que no se está devolviendo
	                else
	                {
	                    //Cantidad a deducir:
	                    int cantDevolver = Convert.ToInt32(drDD["DCantidadDevolver"]);
	                    int cantFacturado = Convert.ToInt32(drDD["PCantidadOrdenada"]) + Convert.ToInt32(drDD["PCantidadBonificadaEditada"]);
	                    int cantPromo = Convert.ToInt32(drDD["PPromocion"]);

	                    //
	                    if (cantDevolver <= cantFacturado)
	                        cantDeducir = cantPromo;
	                    else
	                        cantDeducir = (cantFacturado + cantPromo) - cantDevolver;

	                    //
	                    if (cantDeducir == 0 && Convert.ToInt32(drDD["PCantidadOrdenada"]) != 0)
	                        cantDeducir = cantPromo;

	                    //Precio a aplicar:
	                    //Si el producto no pertenecia al detalle del pedido,calcular el monto con el precio de venta 
	                    //definido para el cliente
	                    if (Convert.ToDecimal(drDD["PMontoPrecioEditado"]) == 0)
	                        precio = InventarioBL.GetPrecioProducto(Convert.ToInt64(drDD["PobjProductoID"]), cliente.objPrecioVentaID, cantDeducir, false, cliente.objTipoClienteID);
	                    //Si no, se calcula con el precio con que se facturó
	                    else
	                        precio = Convert.ToDecimal(drDD["PMontoPrecioEditado"]);

	                }

	                //
	                monto += cantDeducir * precio;
	            }

	            //
	            return monto;
	        }
		 * */
		return 0.d;
	}
 
}
