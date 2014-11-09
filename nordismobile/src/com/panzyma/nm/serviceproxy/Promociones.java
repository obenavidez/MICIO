package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import android.content.ContentResolver;
import android.database.Cursor;

import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.model.ModelCliente;

public class Promociones {

	public static ArrayList<com.panzyma.nm.serviceproxy.Promocion> getPromocionesAplican(Pedido p,ContentResolver content) throws Exception {
		ArrayList<com.panzyma.nm.serviceproxy.Promocion> vecP = new ArrayList<com.panzyma.nm.serviceproxy.Promocion>();
		ArrayList<com.panzyma.nm.serviceproxy.Promocion> promos = getPromociones(content);
		Cliente clie = ModelCliente.getClienteBySucursalID(content,p.getObjSucursalID(), 0);

		Vector idsExcluir = new Vector();
		if (p.getPromocionesAplicadas() != null) 
		{
			for (int i = 0; i < p.getPromocionesAplicadas().length; i++)
				idsExcluir.addElement(p.getPromocionesAplicadas()[i]
						.getObjPromocionID() + "");
		}
		// Buscar promociones que apliquen según datos del cliente
		for (int i = 0; i < promos.size(); i++) 
		{
			Promocion prom = promos.get(i);

			// Si hay que excluir la promoción, saltarla
			if (idsExcluir.contains(prom.getId() + ""))
				continue;

			// Si promoción pedido es al crédito y la promoción no a plica a
			// pedidos al crédito: no aplica
			if ((p.getTipo().compareTo("CR") == 0) && (!prom.isAplicaCredito()))
				continue; // no aplica

			// Ver si coincide la categoría del cliente
			if (!(prom.getCatClientes().trim().equals(""))) {
				String strCatClie = "*" + clie.getObjCategoriaClienteID() + "*";
				if (!StringUtil.strContains(prom.getCatClientes(), strCatClie))
					continue; // no aplica
			}

			// Ver si coincide el tipo de cliente
			if (!(prom.getTiposCliente().trim().equals(""))) {
				String strTipoClie = "*" + clie.getObjTipoClienteID() + "*";
				if (!StringUtil.strContains(prom.getTiposCliente(), strTipoClie))
					continue; // no aplica
			}

			// Ver si coincide el id de la sucursal
			if (!(prom.getSucursales().trim().equals(""))){
				String strSucursal = "*" + clie.getIdSucursal() + "*";
				if (!StringUtil.strContains(prom.getSucursales(), strSucursal))
					continue; // no aplica
			}

			// Ver si coincide la ubicación geográfica
			if (!(prom.getUbicaciones().trim().equals(""))) {
				// Para cada ubicación del cliente comprobar si hay coincidencia
				String[] ubics = StringUtil.split(clie.getUG(), "*");
				boolean aplica = false;
				for (int j = 0; j < ubics.length; j++) {
					String strUbicacion = ubics[j];
					if (StringUtil.strContains(prom.getUbicaciones(),
							strUbicacion)) {
						aplica = true; // Se encontró (aplica)
						break; // Quebrar ciclo
					}
				}
				if (!aplica)
					continue; // no aplica
			}

			// Si la promoción es de tipo Monto - Descuento
			// o la promoción es de tipo Monto - Producto
			if ((prom.getTipoPromo().compareTo("MONTO-DESC") == 0)
					|| (prom.getTipoPromo().compareTo("MONTO-PROD") == 0)) {
				// Si el monto del pedido es menor al monto
				// mínimo de la promoción, la promoción no aplica
				if (p.getSubtotal() < prom.getMontoMinimo())
					continue; // La promoción no aplica, ir a la siguiente
			}

			// Si la promoción es de tipo Producto - Descuento o Producto -
			// Producto
			if ((prom.getTipoPromo().compareTo("PROD-DESC") == 0)
					|| (prom.getTipoPromo().compareTo("PROD-PROD") == 0)) {
				int contCantidad = 0; // Contador de productos
				int contUnidades = 0; // Contador de unidades ordenadas

				// Trayendo productos base de la promoción
				Vector prodsBase = parseProdsBase(prom);

				// Si hay definida una cantidad mínima de items a comprar,
				// verificar
				if (prom.getCantidadMinimaItems() > 0) {
					// Contar la cantidad productos en el detalle del pedido
					// que también están en los productos promocionados
					contCantidad = 0;
					for (int j = 0; j < prodsBase.size(); j++) {
						ProdBase pb = (ProdBase) prodsBase.elementAt(j);
						if (getProdEnDetPedido(pb.getObjProductoID(), p) != null)
							contCantidad++;
					}

					// Y si la cuenta es menor a la cantidad requerida por
					// la promoción, la promoción no aplica, en caso contrario
					// seguir
					if (contCantidad < prom.getCantidadMinimaItems())
						continue; // La promoción no aplica, ir a la siguiente
				}

				// Si hay una cantidad mínima global de productos definida
				if (prom.isCantidadMinimaBaseUnica()) {
					// Sumar la cantidad de productos comprados en el detalle
					// del pedido
					// que también están en los productos promocionados
					contUnidades = 0;
					for (int j = 0; j < prodsBase.size(); j++) {
						ProdBase pb = (ProdBase) prodsBase.elementAt(j);
						DetallePedido dp = getProdEnDetPedido(
								pb.getObjProductoID(), p);
						if (dp != null)
							contUnidades += dp.getCantidadOrdenada();
					}

					// Y si el total es menor que la cantidad configurada en la
					// promoción,
					// la promoción no aplica, en caso contrario seguir
					if (contUnidades < prom.getCantidadMinimaBase())
						continue; // La promoción no aplica, ir a la siguiente
				} // if (promo.CantidadMinimaBaseUnica)

				// Si hay un rango de monto global definido
				if (prom.isMontoBaseUnico()) {
					// Si no hay monto máximo definido
					if (prom.getMontoBaseMaximo() == 0) {
						// Si el monto del pedido es menor que el monto mínimo
						// requerido
						// en la promoción, la promoción no aplica
						if (p.getSubtotal() < prom.getMontoBaseMinimo())
							continue; // La promoción no aplica, ir a la
										// siguiente
					} else // Si hay monto máximo definido
					{
						// Si el monto del pedido es menor que el monto mínimo
						// requerido en la promoción
						// o mayor o igual al monto máximo, la promoción no
						// aplica
						if ((p.getSubtotal() < prom.getMontoBaseMinimo())
								|| (p.getSubtotal() >= prom
										.getMontoBaseMaximo()))
							continue; // La promoción no aplica, ir a la
										// siguiente
					}
				} // //Si hay un rango de monto global definido

				int contadorProdsAplican = 0;
				// Para cada producto base de la promoción, contar los que
				// cumplan con su cantidad
				// o monto si estos fueron definidos
				for (int j = 0; j < prodsBase.size(); j++) {
					boolean aplica = true;
					ProdBase pb = (ProdBase) prodsBase.elementAt(j);

					// Si el producto no existe en el detalle del pedido, no se
					// cuenta
					DetallePedido dp = getProdEnDetPedido(
							pb.getObjProductoID(), p);
					if (dp == null)
						aplica = false;
					else {
						// Si el producto tiene definida una cantidad mínima
						if (pb.getCantidadMinima() > 0) {
							// Si la cantidad ordenada en el pedido es menor a
							// esta cantidad mínima, el producto no se cuenta
							if (dp.getCantidadOrdenada() < pb
									.getCantidadMinima())
								aplica = false;
						} else // El producto tiene definido un monto mínimo
						{
							// Si el monto máximo tiene valor
							if (pb.getMontoMaximo() > 0) {
								// Si el monto comprado del producto es menor al
								// monto mínimo y mayor o igual al monto máximo
								if ((dp.getSubtotal() < pb.getMontoMinimo())
										|| (dp.getSubtotal() >= pb
												.getMontoMaximo()))
									aplica = false;
							} else {
								if (pb.getMontoMinimo() > 0) {
									// Si el monto comprado del producto es
									// menor al monto mínimo
									if (dp.getSubtotal() < pb.getMontoMinimo())
										aplica = false;
								}
							}
						}
					}

					if (aplica)
						contadorProdsAplican++; // El producto aplica
				} // foreach

				// Si la cantidad de productos que cumplieron es menor a la
				// cantindad mínima requerida, la promoción no aplica
				int cantMinima = prodsBase.size();
				if (prom.getCantidadMinimaItems() > 0)
					cantMinima = prom.getCantidadMinimaItems();

				if (contadorProdsAplican < cantMinima)
					continue; // Esta promoción no aplica

			} // Si la promoción es de tipo Producto - Descuento o Producto -
				// Producto

			// Agregar promoción que aplica
			vecP.add(prom);
		}

		return vecP;
	}

	public static ArrayList<com.panzyma.nm.serviceproxy.Promocion> getPromociones(ContentResolver content) 
	{
		ArrayList<com.panzyma.nm.serviceproxy.Promocion> aprom = new ArrayList<com.panzyma.nm.serviceproxy.Promocion>();
		Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PROMOCION,
			        null, //Columnas a devolver
			        null,       //Condición de la query
			        null,       //Argumentos variables de la query
			        null);
		if (cur.moveToFirst()) 
		{
			do {
				
				aprom.add(new com.panzyma.nm.serviceproxy.Promocion(
						Long.parseLong(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.Id))), 
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.Codigo)), 
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.Descripcion)), 
						(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.AplicaCredito)).equals("1")), 
						Integer.parseInt(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.FechaFin))), 
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MomentoAplicacion)), 
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.TipoPromo)), 
						Float.parseFloat(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MontoMinimo))), 
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.TipoDescuento)), 
						Float.parseFloat(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.Descuento))), 
						(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.AplicacionMultiple)).equals("1")), 
						Integer.parseInt(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.CantidadMinimaItems))), 
						(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.CantidadMinimaBaseUnica)).equals("1")), 
						Integer.parseInt(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.CantidadMinimaBase))), 
						(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.CantidadPremioUnica)).equals("1")), 
						Integer.parseInt(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.CantidadPremio))), 
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.ProductosOtorgadosPor)),  
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MontoEntregadoPor)),
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.DescripcionPromocion)),
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.CatClientes)),
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.TiposCliente)),  
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.Sucursales)),
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.Ubicaciones)),
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.ProdsBase)), 
						cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.ProdsPremio)), 
						(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MontoBaseUnico)).equals("1")),					
						Float.parseFloat(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MontoBaseMinimo))),						
						Float.parseFloat(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MontoBaseMaximo))),					 
						(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MontoPremioUnico)).equals("1")),
						Float.parseFloat(cur.getString(cur.getColumnIndex(com.panzyma.nm.auxiliar.NMConfig.Promocion.MontoPremio)))
						));
 
			} while (cur.moveToNext());
		}
		return aprom;
	}

	private static Vector parseProdsBase(Promocion promo) {
        if (promo.getProdsBase().trim() == "") return null;
        
        Vector vec = new Vector();
        String[] prodsBase = StringUtil.split(promo.getProdsBase(), "|");
        
        for(int i=0; i<prodsBase.length; i++) {
            ProdBase pb = new ProdBase();
            //String[] strPb = StringUtil.split(prodsBase[i], ",");
            String[] strPb = StringUtil.split(prodsBase[i], ",");
            pb.setObjProductoID(Long.parseLong(strPb[0]));
            pb.setCantidadMinima(Integer.parseInt(strPb[1]));
            pb.setMontoMinimo(Float.parseFloat(strPb[2]));
            pb.setMontoMaximo(Float.parseFloat(strPb[3]));
            pb.setAplicaBonificacion(false);
            if (strPb[4].toUpperCase() == "TRUE") pb.setAplicaBonificacion(true);
            pb.setTipoDescuento(strPb[5]);
            pb.setDescuento(Float.parseFloat(strPb[6]));
            vec.addElement(pb);
        }        
        return vec;
    }
	
	private static DetallePedido getProdEnDetPedido(long idProd, Pedido p)
    {
        DetallePedido[] dp = p.getDetalles();
        for(int i=0; i<dp.length; i++) {
            if (dp[i].getObjProductoID() == idProd) return dp[i];
        }
        return null;
    }
	
	public static void DesaplicarPromociones(Pedido pedido) {
        //Primero eliminamos del pedido todos los descuentos y cantidades dadas en promoción
        pedido.setDescuento(0F);        
        
        DetallePedido[] dets =pedido.getDetalles();
        if (dets == null) return;
        
        for (int i = 0; i < dets.length; i++) {            
            dets[i].setDescuento(0F);
            dets[i].setCantidadPromocion(0);
            dets[i].setImpuesto((dets[i].getPorcImpuesto() / 100) * (dets[i].getSubtotal() - dets[i].getDescuento()));
            dets[i].setTotal(dets[i].getSubtotal() - dets[i].getDescuento() + dets[i].getImpuesto());
        }
        
        //Dejando en el pedido solo aquellos productos que fueron agregados por el usuario (CantidadOrdenada > 0)
        //Y restaurar las bonificaciones a su valor original
        Vector vecDets = new Vector();
        for (int i = 0; i < dets.length; i++) {            
            DetallePedido d = dets[i];
            if (d.getCantidadOrdenada() > 0) vecDets.addElement(d);
        }
        
        DetallePedido[] nuevosDets = new DetallePedido[vecDets.size()];
        for (int i = 0; i < vecDets.size(); i++) {            
            DetallePedido d = (DetallePedido)vecDets.elementAt(i);
            d.setCantidadBonificadaEditada(d.getCantidadBonificada());
            nuevosDets[i] = d;
        }
        
        pedido.setDetalles(nuevosDets);
        pedido.setPromocionesAplicadas(null);
    }

	public static void ActualizaPedidoDePromociones(Pedido pedido) 
	 {
	        DistribuirDescuentosPromos(pedido);
	        
	        PedidoPromocion[] pps = pedido.getPromocionesAplicadas();
	        if (pps == null) return;
	        
	        DetallePedido[] detalles = pedido.getDetalles();
	        if (detalles == null) return;
	        
	        //Dejando en el pedido solo aquellos productos que fueron agregados por el usuario (CantidadOrdenada > 0)
	        Vector vecDets = new Vector();
	        for (int i = 0; i < detalles.length; i++) {            
	            DetallePedido d = detalles[i];
	            if (d.getCantidadOrdenada() > 0) vecDets.addElement(d);
	        }
	        
	        DetallePedido[] nuevosDets = new DetallePedido[vecDets.size()];
	        for (int i = 0; i < vecDets.size(); i++) {            
	            DetallePedido d = (DetallePedido)vecDets.elementAt(i);
	            nuevosDets[i] = d;
	        }
	        
	        pedido.setDetalles(nuevosDets);
	        
	        //Crear un único vector con todos los productos aplicados
	        Vector vecProdsProm = new Vector();
	        for(int i=0; i < pps.length; i++) {
	            PedidoPromocionDetalle[] ppds = pps[i].getDetalles();
	            if (ppds == null) continue;
	            
	            for(int j=0; j<ppds.length; j++) {
	                PedidoPromocionDetalle ppd = ppds[j];
	                
	                //Buscarlo en el vector
	                int idxBuscado = -1;                
	                PedidoPromocionDetalle ppdBusc = null;
	                for(int k=0; k < vecProdsProm.size(); k++) {
	                    ppdBusc = (PedidoPromocionDetalle)vecProdsProm.elementAt(k);
	                    if (ppdBusc.getObjProductoID() == ppd.getObjProductoID()) {
	                        idxBuscado = k;
	                        break;
	                    }
	                }
	                                
	                if (idxBuscado >= 0) {                 
	                    //Si ya existía, solo acumularle las cantidades y descuentos
	                    ppdBusc = (PedidoPromocionDetalle)vecProdsProm.elementAt(idxBuscado);
	                    ppdBusc.setCantidadEntregada(ppdBusc.getCantidadEntregada() + ppd.getCantidadEntregada());
	                    //ppdBusc.setDescuento(ppdBusc.getDescuento() + ppd.getDescuento());
	                    vecProdsProm.setElementAt(ppdBusc, idxBuscado);
	                }
	                else {
	                    //vecProdsProm.addElement(ppd);
	                                        
	                    //Si no se encuentra, insertarlo nuevo (insertar clon, para no sobreescribir datos originales)
	                    PedidoPromocionDetalle clonPpd = new PedidoPromocionDetalle();
	                    clonPpd.setCantidadEntregada(ppd.getCantidadEntregada());
	                    clonPpd.setDescuento(ppd.getDescuento());
	                    clonPpd.setNombreProducto(ppd.getNombreProducto());
	                    clonPpd.setObjProductoID(ppd.getObjProductoID());
	                    vecProdsProm.addElement(clonPpd);
	                }                
	            }           
	        }
	        
	        //Actualizando detalle del pedido de acuerdo a los detalles de productos aplicados en promoción
	        if (vecProdsProm.size() == 0) return;
	        Vector vecNuevosDet = new Vector();
	        for(int i=0; i < vecProdsProm.size(); i++) {
	            PedidoPromocionDetalle ppd = (PedidoPromocionDetalle)vecProdsProm.elementAt(i);
	            
	            //Buscar el producto en el detalle:
	            //Si existe, actualizar la cantidad de promoción
	            //Si no existe, insertarlo con cantidad ordenada cero
	             DetallePedido d = null;
	             DetallePedido[] dets = pedido.getDetalles();
	             for(int j=0; j < dets.length; j++) {
	                 DetallePedido dp = dets[j];
	                 if (dp.getObjProductoID() == ppd.getObjProductoID()) {
	                     d = dp;
	                     break;
	                 }
	             }
	             
	             if (d != null) {
	                //Actualizar cantida entregada en promoción
	                if (ppd.getCantidadEntregada() > 0) d.setCantidadPromocion(ppd.getCantidadEntregada());                
	             }
	             else {
	                 //Insertar nuevo producto en detalle con cantidad ordenada = 0
	                 if (ppd.getCantidadEntregada() > 0) {
	                    d = new DetallePedido();
	                    d.setNombreProducto(ppd.getNombreProducto());
	                    d.setCantidadOrdenada(0);
	                    d.setCantidadBonificada(0);
	                    d.setCantidadBonificadaEditada(0);
	                    d.setPrecio(0F);
	                    d.setMontoPrecioEditado(0F);
	                    d.setBonifEditada(false);
	                    d.setPrecioEditado(false);
	                    d.setSubtotal(0F);
	                    d.setDescuento(0F);
	                    d.setImpuesto(0F);
	                    d.setTotal(0F);
	                    d.setCantidadPromocion(ppd.getCantidadEntregada());
	                    
	                    vecNuevosDet.addElement(d);
	                }
	             }             
	        }
	        
	        if (vecNuevosDet.size() > 0) {
	            DetallePedido[] nuevoDetalle = new DetallePedido[pedido.getDetalles().length + vecNuevosDet.size()];
	            
	            //Insertando detalles existentes
	            for(int i = 0; i < pedido.getDetalles().length; i++) {
	                nuevoDetalle[i] = pedido.getDetalles()[i];
	            }
	            
	            //Insertando detalles nuevos
	            for(int i = 0; i < vecNuevosDet.size(); i++) {
	                 nuevoDetalle[pedido.getDetalles().length + i] = (DetallePedido)vecNuevosDet.elementAt(i);
	            }
	            
	            pedido.setDetalles(nuevoDetalle);
	        }
	        
	        //Recalculando impuesto y total
	        for (int i = 0; i < detalles.length; i++) {            
	            DetallePedido d = detalles[i];
	            d.setImpuesto((d.getSubtotal() - d.getDescuento()) * d.getPorcImpuesto() / 100);
	            d.setTotal(d.getSubtotal() -  d.getDescuento() + d.getImpuesto());                       
	        }
	    }

	public static void DistribuirDescuentosPromos(Pedido pedido)
	    {        
	        //En esta tabla Hash guardaremos la lista de productos en el pedido
	        //(id del producto, detalle pedido) para búsqueda fácil y rápida
	        Hashtable dicProdsPedido = new Hashtable();
	        for(int i=0; i < pedido.getDetalles().length; i++) {
	            DetallePedido pd = pedido.getDetalles()[i];
	            dicProdsPedido.put(pd.getObjProductoID() + "", pd);
	            pd.setDescuento(0F);
	        }
	        
	        if (pedido.getPromocionesAplicadas() == null) return;
	        
	        //Distribuir descuentos detallados por producto
	        float descuentoPorProducto = 0;
	        for(int i=0; i < pedido.getPromocionesAplicadas().length; i++) {
	            PedidoPromocion pp = pedido.getPromocionesAplicadas()[i];
	            for(int j=0; j < pp.getDetalles().length; j++) {
	                PedidoPromocionDetalle ppd = pp.getDetalles()[j];
	                if (ppd.getDescuento() > 0) {
	                    DetallePedido pd = (DetallePedido)dicProdsPedido.get(ppd.getObjProductoID() + "");
	                    pd.setDescuento(pd.getDescuento() + ppd.getDescuento());
	                    descuentoPorProducto += ppd.getDescuento();
	                }
	            }
	        }
	        
	        //Distribuir descuento faltante
	        float descuentoFaltante = pedido.getDescuento() - descuentoPorProducto;
	        if (descuentoFaltante > 0)
	        {
	            float prcDesc = descuentoFaltante / pedido.getSubtotal();
	            float sumaDesc = 0;
	            int i = 0;
	            
	            for (Enumeration e = dicProdsPedido.keys(); e.hasMoreElements() ;) {
	                float desc = 0;
	                DetallePedido pd = (DetallePedido)dicProdsPedido.get(e.nextElement());
	                if (i == dicProdsPedido.size() - 1)
	                    desc = descuentoFaltante - sumaDesc;
	                else 
	                    desc = pd.getSubtotal() * prcDesc;
	                pd.setDescuento(pd.getDescuento() + desc);
	                sumaDesc += desc;
	                i++;
	            }
	     
	            //Actualizando los descuentos en el detalle del pedido
	            for(int k=0; k < pedido.getDetalles().length; k++) {
	                DetallePedido pd = pedido.getDetalles()[k];
	                DetallePedido pd2 = (DetallePedido)dicProdsPedido.get(pd.getObjProductoID() + "");
	                pd.setDescuento(pd2.getDescuento());
	                pd.setImpuesto((pd.getPorcImpuesto() / 100) * (pd.getSubtotal() - pd.getDescuento()));
	                pd.setTotal(pd.getSubtotal() - pd.getDescuento() + pd.getImpuesto());
	            }
	        }
	    }

	public static void aplicarPromocion(Pedido pedido, Promocion promo,ContentResolver content)
	 {       
	        //Calculando factor de aplicación múltiple
	        int K = getFactorAplicacionMultiple(pedido, promo);        
	        String cantidadesDistribuidas = "";
	        
	        //Trayendo los productos a entregar en la promoción
	        Vector prodsPremio = parseProdsPremio(promo);
	        
	        //Si la promoción requiere distribución de cantidad o monto global, 
	        //llamar a formulario de distribución
	        if (prodsPremio != null) {
	            if (((promo.getTipoPromo().compareTo("PROD-PROD") == 0) || (promo.getTipoPromo().compareTo("MONTO-PROD") == 0)) && ((promo.isCantidadPremioUnica() || promo.isMontoPremioUnico())))
	            {
	                if (prodsPremio.size() > 1) return; //VER ESTE CASO PARA DESPUES: Distribución Manual
	                
	                ProdPremio prodPremio = (ProdPremio)prodsPremio.elementAt(0);
	                
	                if (promo.getProductosOtorgadosPor().compareTo("C") == 0) {
	                    int cantDistribuir = promo.getCantidadPremio() * K;
	                    cantidadesDistribuidas = prodPremio.getObjProductoID() + "," + cantDistribuir;
	                }            
	                else { //Entregados por monto
	                    float montoDistribuir = promo.getMontoPremio() * K;
	                    if (promo.getMontoEntregadoPor().compareTo("P") == 0)
	                        montoDistribuir = pedido.getSubtotal() * promo.getMontoPremio() / 100;
	                    Producto prod = null;
						try {
							prod = BPedidoM.getProductoByID(content,prodPremio.getObjProductoID());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                    
	                    float precioProm = Ventas.getPrecioPromedioProducto(prod, pedido.getObjTipoPrecioVentaID());
	                    
	                    int cantDistribuir = (int)Math.floor(montoDistribuir / precioProm);
	                    cantidadesDistribuidas = prodPremio.getObjProductoID() + "," + cantDistribuir;
	                }
	            }
	        }
	                    
	        //Si hay cantidades distribuidas, parsearlas
	        Hashtable dicCantDist = new Hashtable();
	        if (cantidadesDistribuidas != "")
	        {
	            String[] strCantDist = StringUtil.split(cantidadesDistribuidas, ";");
	            for (int i=0; i<strCantDist.length; i++) {
	                String[] strCant = StringUtil.split(strCantDist[i], ",");                
	                String idProdPremio = strCant[0];
	                String cant = strCant[1];
	                if (Integer.parseInt(cant) > 0) dicCantDist.put(idProdPremio, cant);
	            }
	        }
	        
	        //Creando nuevo registro de promoción aplicada al pedido
	        PedidoPromocion pp = new PedidoPromocion();        
	        pp.setObjPromocionID(promo.getId());
	        pp.setCodigoPromocion(promo.getCodigo());
	        pp.setNombrePromocion(promo.getDescripcion());
	        pp.setDescuento(0);
	        
	        //Vector en el que se irán guardando los detalles de la promoción aplicada
	        Vector detPromoAplic = new Vector();
	        
	        //Aplicando promoción de tipo Monto - Descuento
	        if (promo.getTipoPromo().compareTo("MONTO-DESC") == 0)
	        {
	            //Calculando descuento
	            if (promo.getTipoDescuento().compareTo("P") == 0)
	                pp.setDescuento(pedido.getSubtotal() * promo.getDescuento() / 100);
	            else
	                pp.setDescuento(K * promo.getDescuento());
	                
	            //Sumar descuento al pedido si la aplicación es al momento de la facturación
	            if (promo.getMomentoAplicacion().compareTo("F") == 0)
	                pedido.setDescuento(pedido.getDescuento() + pp.getDescuento());
	        }
	        
	        //Aplicando promoción de tipo Monto - Producto y Producto - Producto        
	        if ((promo.getTipoPromo().compareTo("MONTO-PROD") == 0) || (promo.getTipoPromo().compareTo("PROD-PROD") == 0))
	        {
	            //Para cada producto premio insertar su detalle
	            for(int j=0; j<prodsPremio.size(); j++) {
	                //Detalle de producto premio
	                ProdPremio prodPremio = (ProdPremio)prodsPremio.elementAt(j);

	                //Creando detalle de producto en promoción aplicada                
	                PedidoPromocionDetalle ppd = new PedidoPromocionDetalle();
	                ppd.setObjProductoID(prodPremio.getObjProductoID());
	                ppd.setNombreProducto(prodPremio.getNombreProducto());
	                ppd.setCantidadEntregada(0);
	                ppd.setDescuento(0);                
	                
	                //Si los productos son entregados por cantidad
	                if (promo.getProductosOtorgadosPor().compareTo("C") == 0)
	                {
	                    if (promo.isCantidadPremioUnica()) //Aplicar lo que el usuario haya distribuido
	                    {
	                        String idProdPremio = prodPremio.getObjProductoID() + "";                        
	                        if (dicCantDist.containsKey(idProdPremio)) {
	                            String strCant = dicCantDist.get(idProdPremio) + "";
	                            int cant = Integer.parseInt(strCant);
	                            ppd.setCantidadEntregada(cant); //ya trae aplicado K
	                        }
	                    }
	                    else //Aplicar lo que ya está configurado en la promoción                            
	                        ppd.setCantidadEntregada(K * prodPremio.getCantidad());                            
	                }

	                //Si los productos se entregan por monto
	                if (promo.getProductosOtorgadosPor().compareTo("M") == 0) 
	                {                            
	                    if (promo.isMontoPremioUnico()) //Aplicar lo que el usuario haya distribuido
	                    {
	                        String idProdPremio = prodPremio.getObjProductoID() + "";                        
	                        if (dicCantDist.containsKey(idProdPremio)) {
	                            String strCant = dicCantDist.get(idProdPremio) + "";
	                            int cant = Integer.parseInt(strCant);
	                            ppd.setCantidadEntregada(cant); //ya trae aplicado K
	                        }
	                    }
	                    else //Aplicar lo que ya está configurado en la promoción
	                    {   
	                        /////////////////////////////////////////////
	                        /////////////////////////////////////////////
	                        //Traer el precio del producto  
	                        Producto prod = null;
							try {
								prod = BPedidoM.getProductoByID(content,prodPremio.getObjProductoID());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}                            
	                        float P = Ventas.getPrecioPromedioProducto(prod, pedido.getObjTipoPrecioVentaID());

	                        //Calculando la cantidad a entregar en base al monto y el precio
	                        int C = (int)(Math.floor(prodPremio.getMonto() / P));

	                        //Calculando la cantidad a entregar                            
	                        ppd.setCantidadEntregada(K * C);
	                    }                            
	                }

	                if (ppd.getCantidadEntregada() > 0)                
	                    detPromoAplic.addElement(ppd);                
	            } //Para cada producto premio insertar su detalle
	        } //Aplicando promoción de tipo Monto - Producto y Producto - Producto
	        
	        //Aplicando promoción de tipo Producto - Descuento
	        if (promo.getTipoPromo().compareTo("PROD-DESC") == 0)
	        {
	            //Trayendo los productos base de la promoción
	            Vector prodsBase = parseProdsBase(promo);

	            //Para cada producto promocionado insertar su detalle y su descuento
	            for(int j=0; j<prodsBase.size(); j++) {            
	                //Detalle de producto base
	                ProdBase prodBase = (ProdBase)prodsBase.elementAt(j);

	                //Si el producto base existe en el detalle del pedido               
	                DetallePedido dp = getProdEnDetPedido(prodBase.getObjProductoID(), pedido);
	                if (dp != null)
	                {                    
	                    //Creando detalle de producto en promoción aplicada                
	                    PedidoPromocionDetalle ppd = new PedidoPromocionDetalle();
	                    ppd.setObjProductoID(prodBase.getObjProductoID());
	                    ppd.setNombreProducto(dp.getNombreProducto());
	                    ppd.setCantidadEntregada(0);
	                    ppd.setDescuento(0);
	                
	                    if (prodBase.getTipoDescuento().compareTo("P") == 0) 
	                        ppd.setDescuento(dp.getSubtotal() * prodBase.getDescuento() / 100);
	                    else
	                        ppd.setDescuento(K * prodBase.getDescuento());

	                    //Sumar descuento al pedido
	                    pedido.setDescuento(pedido.getDescuento() + ppd.getDescuento());

	                    //Sumar descuento a promoción aplicada
	                    pp.setDescuento(pp.getDescuento() + ppd.getDescuento());

	                    //Insertar el detalle                                 
	                    detPromoAplic.addElement(ppd);     
	                }
	            }
	        } //Aplicando promoción de tipo Producto - Descuento
	        
	        //Desaplicar bonificaciones en caso que no se aplique bonificación
	        if ((promo.getTipoPromo().compareTo("PROD-PROD") == 0) || (promo.getTipoPromo().compareTo("PROD-DESC") == 0))
	        { 
	            //Trayendo los productos base de la promoción
	            Vector prodsBase = parseProdsBase(promo);

	            //Para cada producto en el detalle de la promoción
	            DetallePedido[] detsPedido = pedido.getDetalles();
	            for(int j=0; j<detsPedido.length;j++) {
	                DetallePedido dp = detsPedido[j];
	                
	                //Buscar el producto en los productos base
	                for(int k=0; k<prodsBase.size(); k++) {
	                    ProdBase pb = (ProdBase)prodsBase.elementAt(k);
	                    
	                    //Si se encuentra
	                    if (pb.getObjProductoID() == dp.getObjProductoID()) {                        
	                        //Si no aplica bonificación, ponerla en cero
	                        if (!pb.getAplicaBonificacion()) {
	                            dp.setCantidadBonificadaEditada(0);                        
	                            detsPedido[j] = dp; //Actualizarlo en el detalle del pedido
	                        }
	                        break; //Quiebra el ciclo
	                    }                    
	                }
	            }
	        } //Desaplicar bonificaciones en caso que no se aplique bonificación
	        
	        //Insertar detalles de promoción a la promoción aplicada
	        PedidoPromocionDetalle[] arrPPD = new PedidoPromocionDetalle[detPromoAplic.size()];
	        for(int j=0; j<detPromoAplic.size(); j++) {
	            PedidoPromocionDetalle ppd = (PedidoPromocionDetalle)detPromoAplic.elementAt(j);
	            arrPPD[j] = ppd;
	        }
	        pp.setDetalles(arrPPD);
	        
	        //Insertar promoción aplicada al pedido
	        Vector vecPP = new Vector();
	        PedidoPromocion[] arrPP = pedido.getPromocionesAplicadas();
	        if (arrPP != null) {
	            for(int j=0; j<arrPP.length; j++) { vecPP.addElement(arrPP[j]); }
	        }
	        vecPP.addElement(pp);
	        
	        arrPP = new PedidoPromocion[vecPP.size()];
	        for(int j=0; j<vecPP.size(); j++) {
	            PedidoPromocion ppTmp = (PedidoPromocion)vecPP.elementAt(j);
	            arrPP[j] = ppTmp;
	        }
	        pedido.setPromocionesAplicadas(arrPP);
	    } //aplicarPromocion

	private static int getFactorAplicacionMultiple(Pedido pedido, Promocion promo) 
	 {
	        int K = 1; //Cantidad de veces que aplica la promoción: por defecto aplica una vez
	        int contUnidades = 0; //Contador de unidades ordenadas
	            
	        //Si la promoción aplica múltiples veces
	        //Calcular cuantas veces aplica                    
	        if (promo.isAplicacionMultiple())
	        {
	            //Si la promoción es de tipo MONTO-DESC y el descuento es dado por monto o la promoción es de tipo MONTO-PROD
	            if (((promo.getTipoPromo().compareTo("MONTO-DESC") == 0) && (promo.getTipoPromo().compareTo("M") == 0)) || (promo.getTipoPromo().compareTo("MONTO-PROD") == 0))
	            {
	                //Calcular cantidad de veces que el monto de la promoción cabe dentro del monto del pedido
	                K = (int)(Math.floor(pedido.getSubtotal() / promo.getMontoMinimo()));
	            }

	            //Si la promoción es de tipo PROD-DESC o PROD-PROD
	            if ((promo.getTipoPromo().compareTo("PROD-DESC") == 0) || (promo.getTipoPromo().compareTo("PROD-PROD") == 0))
	            {
	                int kk = 999999; //Variable auxiliar para determinar el valor de K
	                int ki = 0; //Otra variable auxiliar

	                //Trayendo los productos promocionados
	                Vector prodsBase = parseProdsBase(promo);

	                //Si hay una cantidad mínima de unidades compradas definida y es mayor que cero
	                if ((promo.isCantidadMinimaBaseUnica()) && (promo.getCantidadMinimaBase() > 0))
	                {
	                    //Contar la cantidad de unidades compradas
	                    for(int j=0; j<prodsBase.size(); j++) {
	                        ProdBase pb = (ProdBase)prodsBase.elementAt(j);
	                        DetallePedido dp = getProdEnDetPedido(pb.getObjProductoID(), pedido);
	                        if (dp != null) contUnidades += dp.getCantidadOrdenada();                        
	                    }
	                    
	                    //Calcular cuantas veces esta cantidad de unidades compradas está contenida
	                    //dentro de la cantidad mínima requerida en la promoción
	                    ki = (int)(Math.floor(contUnidades / promo.getCantidadMinimaBase()));

	                    //Si el valor calculado es menor que el valor actual, pasa a ser el valor actual
	                    if ((ki < kk) && (ki > 0)) kk = ki;
	                }

	                //Si hay un monto mínimo definido y es mayor que cero
	                if ((promo.isMontoBaseUnico()) && (promo.getMontoBaseMinimo() > 0))
	                {
	                    //Calcular cuantas veces este monto está contenido
	                    //dentro del monto mínimo requerido en la promoción
	                    ki = (int)(Math.floor(pedido.getSubtotal() / promo.getMontoBaseMinimo()));

	                    //Si el valor calculado es menor que el valor actual, pasa a ser el valor actual
	                    if ((ki < kk) && (ki > 0)) kk = ki;
	                }

	                //Si se han detallado cantidades o montos por producto promocionado
	                if ((!promo.isCantidadMinimaBaseUnica()) || (!promo.isMontoBaseUnico())) {
	                    //Para cada producto promocionado, averiguar cuantas veces cabe
	                    //su monto o cantidad ordenada en el monto o cantidad de la promoción
	                    for(int j=0; j<prodsBase.size(); j++) {
	                        //Detalle de producto base
	                        ProdBase pb = (ProdBase)prodsBase.elementAt(j);
	                        DetallePedido dp = getProdEnDetPedido(pb.getObjProductoID(), pedido);
	                        
	                        //Si el producto base existe en el pedido
	                        if (dp != null) {
	                            //Calcular cuantas veces la cantidad ordenada cabe en la cantidad mínima en la promoción
	                            if (pb.getCantidadMinima() > 0)
	                                ki = (int)(Math.floor(dp.getCantidadOrdenada() / pb.getCantidadMinima()));

	                            //O calcular cuantas veces el monto ordenado cabe en el monto mínimo en la promoción
	                            if (pb.getMontoMinimo() > 0)
	                                ki = (int)(Math.floor(dp.getSubtotal() / pb.getMontoMinimo()));
	                        }
	                        
	                        //Si el valor calculado es menor que el valor actual, pasa a ser el valor actual
	                        if ((ki < kk) && (ki > 0)) kk = ki;
	                    }
	                } //Si se han detallado cantidades o montos por producto promocionado

	                K = kk;
	            } //Si la promoción es de tipo PROD-DESC o PROD-PROD
	        } //Si la promoción aplica múltiples veces

	        return K;    
	    }

	private static Vector parseProdsPremio(Promocion promo) 
	 {
	        if (promo.getProdsPremio().trim() == "") return null;
	        
	        Vector vec = new Vector();
	        String[] prods = StringUtil.split(promo.getProdsPremio(), "|");
	        
	        if(prods==null || (prods!=null && prods.length==0) || (prods!=null && prods.length!=0 && prods[0].equals("")))
	        	return vec;
	        
	        for(int i=0; i<prods.length; i++) 
	        {
	            ProdPremio pp = new ProdPremio();
	            //String[] strPP = StringUtil.split(prods[i], ",");
	            String[] strPP = StringUtil.split(prods[i], ",");
	            pp.setObjProductoID(0);
	            if (strPP[0].trim().compareTo("") != 0)
	                pp.setObjProductoID(Long.parseLong(strPP[0]));
	            pp.setCantidad(Integer.parseInt(strPP[1]));
	            pp.setMonto(Float.parseFloat(strPP[2]));
	            pp.setNombreProducto(strPP[3]);
	            vec.addElement(pp);
	        }        
	        return vec;
	  }
 
}
