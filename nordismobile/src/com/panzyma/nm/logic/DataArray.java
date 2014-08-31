package com.panzyma.nm.logic;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import com.panzyma.nm.serviceproxy.DetallePedido;

public class DataArray extends Vector<DetallePedido> implements KvmSerializable {

	@Override
	public Object getProperty(int index) {
		return this.get(index);
	}

	@Override
	public int getPropertyCount() {		
		return this.size();
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo info) {
		info.name = "Detalles";
        info.type = new DetallePedido().getClass();
	}

	@Override
	public void setProperty(int arg0, Object value) {
		//SoapObject soapObject = new SoapObject(NMConfig.NAME_SPACE,"Detalles");
        //soapObject = (SoapObject) value;
		DetallePedido dp = (DetallePedido)value;
        DetallePedido daten = new DetallePedido();        
        /*daten.setProperty(0,soapObject.getProperty(NMConfig.Pedido.DetallePedido.Id));
        daten.setProperty(1,soapObject.getProperty(NMConfig.Pedido.DetallePedido.objPedidoID));
        daten.setProperty(2,soapObject.getProperty(NMConfig.Pedido.DetallePedido.objProductoID));  	
	   	daten.setProperty(3,soapObject.getProperty(NMConfig.Pedido.DetallePedido.codProducto));
	   	daten.setProperty(4,soapObject.getProperty(NMConfig.Pedido.DetallePedido.nombreProducto));
	   	daten.setProperty(5,soapObject.getProperty(NMConfig.Pedido.DetallePedido.cantidadOrdenada));
	   	daten.setProperty(6,soapObject.getProperty(NMConfig.Pedido.DetallePedido.cantidadBonificada));
	   	daten.setProperty(7,soapObject.getProperty(NMConfig.Pedido.DetallePedido.objBonificacionID));
	   	daten.setProperty(8,soapObject.getProperty(NMConfig.Pedido.DetallePedido.bonifEditada)); 
	   	daten.setProperty(9,soapObject.getProperty(NMConfig.Pedido.DetallePedido.cantidadBonificadaEditada));
	   	daten.setProperty(10,soapObject.getProperty(NMConfig.Pedido.DetallePedido.precio));
	   	daten.setProperty(11,soapObject.getProperty(NMConfig.Pedido.DetallePedido.montoPrecioEditado));
	   	daten.setProperty(12,soapObject.getProperty(NMConfig.Pedido.DetallePedido.precioEditado));
	   	daten.setProperty(13,soapObject.getProperty(NMConfig.Pedido.DetallePedido.subtotal));
	   	daten.setProperty(14,soapObject.getProperty(NMConfig.Pedido.DetallePedido.descuento)); 
	   	daten.setProperty(15,soapObject.getProperty(NMConfig.Pedido.DetallePedido.porcImpuesto));
	   	daten.setProperty(16,soapObject.getProperty(NMConfig.Pedido.DetallePedido.impuesto));
	   	daten.setProperty(17,soapObject.getProperty(NMConfig.Pedido.DetallePedido.total)); 
	   	daten.setProperty(18,soapObject.getProperty(NMConfig.Pedido.DetallePedido.cantidadDespachada));
	   	daten.setProperty(19,soapObject.getProperty(NMConfig.Pedido.DetallePedido.cantidadADespachar));
	   	daten.setProperty(20,soapObject.getProperty(NMConfig.Pedido.DetallePedido.cantidadPromocion));*/
	   	
	   	daten.setProperty(0,dp.getId());
        daten.setProperty(1,dp.getObjPedidoID());
        daten.setProperty(2,dp.getObjProductoID());  	
	   	daten.setProperty(3,dp.getCodProducto());
	   	daten.setProperty(4,dp.getNombreProducto());
	   	daten.setProperty(5,dp.getCantidadOrdenada());
	   	daten.setProperty(6,dp.getCantidadBonificada());
	   	daten.setProperty(7,dp.getObjBonificacionID());
	   	daten.setProperty(8,dp.getBonifEditada()); 
	   	daten.setProperty(9,dp.getCantidadBonificadaEditada());
	   	daten.setProperty(10,dp.getPrecio());
	   	daten.setProperty(11,dp.getMontoPrecioEditado());
	   	daten.setProperty(12,dp.getPrecioEditado());
	   	daten.setProperty(13,dp.getSubtotal());
	   	daten.setProperty(14,dp.getDescuento()); 
	   	daten.setProperty(15,dp.getPorcImpuesto());
	   	daten.setProperty(16,dp.getImpuesto());
	   	daten.setProperty(17,dp.getTotal()); 
	   	daten.setProperty(18,dp.getCantidadDespachada());
	   	daten.setProperty(19,dp.getCantidadADespachar());
	   	daten.setProperty(20,dp.getCantidadPromocion());
        this.add(daten);
	}

}
