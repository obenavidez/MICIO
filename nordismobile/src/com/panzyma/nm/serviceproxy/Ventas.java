package com.panzyma.nm.serviceproxy;

import static com.panzyma.nm.controller.ControllerProtocol.ID_REQUEST_SALVARPEDIDO;

import java.util.*;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.view.ViewPedidoEdit;

public class Ventas {
	Ventas() {
	}

	public static Vector parseListaPrecios(Producto prod, long idTipoPrecio) {
		Vector vec = new Vector();
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
				vec.addElement(p);
			}
		}

		return vec;
	}
	/*
	 * Devuelve el precio de un producto dada la cantidad que se está ordenando
	 */
	public static float getPrecioProducto(Producto prod, long idTipoPrecio,
			int cantidad) {
		Vector precios = parseListaPrecios(prod, idTipoPrecio);
		PrecioProducto p = (PrecioProducto) precios.elementAt(0);
		if (precios.size() > 1) {
			for (int i = 0; i < precios.size(); i++) {
				p = (PrecioProducto) precios.elementAt(i);
				if ((cantidad >= p.getMinimo()) && (cantidad <= p.getMaximo()))
					break; // Salir del ciclo
			}
		}

		return p.getPrecio();
	}

	/*
	 * Devuelve el precio promedio de un producto dado el tipo de precio
	 */
	public static float getPrecioPromedioProducto(Producto prod,
			long idTipoPrecio) {
		Vector precios = parseListaPrecios(prod, idTipoPrecio);
		if (precios.size() == 0)
			return 0F;

		float precio = 0F;
		for (int i = 0; i < precios.size(); i++) {
			PrecioProducto p = (PrecioProducto) precios.elementAt(i);
			precio += p.getPrecio();
		}
		precio = precio / precios.size();
		return precio;
	}

	public static Vector parseListaBonificaciones(Producto prod,
			long idCatCliente) {
		if (prod.getListaBonificaciones() == null)
			return null;
		if (prod.getListaBonificaciones() == "")
			return null;

		Vector vec = new Vector();
		String catCLiente = idCatCliente + "";
		String[] listaBonif = StringUtil.split(prod.getListaBonificaciones(),
				"|");
		for (int i = 0; i < listaBonif.length; i++) {
			String[] bonifProd = StringUtil.split(listaBonif[i], ",");

			if ((idCatCliente == 0)
					|| (catCLiente.compareTo(bonifProd[1]) == 0)) {
				Bonificacion b = new Bonificacion();
				b.setObjBonificacionID(Long.parseLong(bonifProd[0]));
				b.setObjCategoriaClienteID(idCatCliente);
				b.setCantidad(Integer.parseInt(bonifProd[2]));
				b.setCantBonificacion(Integer.parseInt(bonifProd[3]));
				b.setCategoriaCliente(bonifProd[4]);
				vec.addElement(b);
			}
		}

		return vec;
	}

	/*
	 * Devuelve la bonificación de un producto dada la cantidad que se está
	 * ordenando y la categoría de cliente
	 */
	public static Bonificacion getBonificacion(Producto prod,
			long idCatCliente, int cantidad) {
		Vector bonificaciones = parseListaBonificaciones(prod, idCatCliente);
		if (bonificaciones == null)
			return null;

		Bonificacion bb = null;
		for (int i = bonificaciones.size() - 1; i >= 0; i--) {
			Bonificacion b = (Bonificacion) bonificaciones.elementAt(i);
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

	public static String getNumeroPedido(int numero, Context cnt) {
		int cr = Integer.parseInt(cnt.getSharedPreferences("SystemParams",
				android.content.Context.MODE_PRIVATE).getString(
				"CerosRellenoNumRefPedido", "0"));

		char[] num = new char[cr];
		for (int i = 0; i < cr; i++)
			num[i] = '0';

		String strNum = new String(num);
		strNum = strNum + numero;
		return strNum.substring(strNum.length() - cr, strNum.length());
	}

	public static int getLastOrderId(Context cnt) {
		return cnt.getSharedPreferences("VConfiguracion",
				android.content.Context.MODE_PRIVATE).getInt(
				"max_idpedido", 0);
		
	}
	
	public static int getPrefijoIds(Context cnt){
		return Integer.parseInt(cnt.getSharedPreferences("VConfiguracion",
				android.content.Context.MODE_PRIVATE).getString(
				"device_id", "0")); 
	}
	
	public static int getMaxReciboId(Context cnt){
		return cnt.getSharedPreferences("VConfiguracion",
				android.content.Context.MODE_PRIVATE).getInt(
				"max_idrecibo",0); 
	}
	
	public static void setMaxReciboId(Context cnt, long value) {
		cnt.getSharedPreferences("VConfiguracion",
				android.content.Context.MODE_PRIVATE).edit().putLong("max_idrecibo", value);
	}

	@SuppressWarnings("deprecation")
    public static Pedido guardarPedido(Pedido pedido,ViewPedidoEdit vpe) throws Exception
    { 
        //Salvando el tipo de pedido (crédito contado)
        pedido.setTipo("CR"); 
    	if (vpe.getTipoVenta() == "CO")
			pedido.setTipo("CO");
       
		Date d = new Date(vpe.getFechaPedido());
        pedido.setFecha(DateUtil.d2i(d));
        Integer intId = 0;
        //Generar Id del pedido
        if (pedido.getNumeroMovil() == 0) 
        {            
            intId = Ventas.getLastOrderId(vpe.getApplicationContext());
            if (intId == null) 
                intId = new Integer(1);
            else
                intId = new Integer(intId.intValue() + 1);
           // DataStore.setLastOrderId(intId);
            Integer prefix = Ventas.getPrefijoIds(vpe.getApplicationContext());
            String strIdMovil = prefix.intValue() + "" + intId.intValue();
            int idMovil = Integer.parseInt(strIdMovil);
            pedido.setNumeroMovil(idMovil);
            pedido.setObjEstadoID(0);
            pedido.setObjCausaEstadoID(0);
            pedido.setCodEstado("REGISTRADO");
            pedido.setDescEstado("Elaboración");
            pedido.setCodCausaEstado("REGISTRADO");
            pedido.setDescCausaEstado("Registrado");
        }  
        pedido.setId(vpe.getBridge().RegistrarPedido(pedido,vpe.getContext()));
        vpe.getBridge().ActualizarSecuenciaPedido(intId,vpe.getContext());
        vpe.actualizarOnUINumRef(pedido);
             
        return pedido;
    } 
}
