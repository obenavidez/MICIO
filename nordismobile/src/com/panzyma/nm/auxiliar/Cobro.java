package com.panzyma.nm.auxiliar;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import android.content.ContentResolver;
import android.content.Context;

import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.CalcDescOca_Output;
import com.panzyma.nm.serviceproxy.CalcDescPP_Output;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DescFactura;
import com.panzyma.nm.serviceproxy.DescProveedor;
import com.panzyma.nm.serviceproxy.DescuentoProveedor;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.MontoProveedor;
import com.panzyma.nm.serviceproxy.PromocionCobro;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;


public class Cobro 
{ 
	public static boolean validaAplicDescOca(Context cnt,ReciboColector recibo)
    {
        //Validar que de las facturas marcadas y que se están cancelando (no es abono)
        //exista al menos una vencida
        if (recibo.getFacturasRecibo() == null || recibo.getFacturasRecibo().size()==0) return false;
        
        ArrayList<ReciboDetFactura> _ff = recibo.getFacturasRecibo();
        if ((_ff == null) || (_ff.size() == 0)) return false;
        
        boolean puedeAplicarDescOca = false;
        int diasAplicaMora = Integer.parseInt((String) Cobro.getParametro(cnt,"HolguraDiasAplicarDescPP"));
        for(int i = 0; i < _ff.size(); i++) 
        {
            ReciboDetFactura fac = _ff.get(i);            
            //Tiene que estar seleccionada y cancelándose
            if (!fac.isEsAbono()) 
            {
                //Ver si factura ya está vencida
                long fechaVence = fac.getFechaAplicaDescPP();
                long fechaNoAplicaDescPP = DateUtil.addDays(fechaVence, diasAplicaMora);
                if (fechaNoAplicaDescPP < DateUtil.getTime())
                {
                    puedeAplicarDescOca = true;
                    break;
                }
            }
        }
        
        return puedeAplicarDescOca;//quitar true
    } //ValidaAplicDescOca
	
	public static float getTotalPagoRecibo(ReciboColector rcol) 
	{
        if (rcol.getFormasPagoRecibo().size() == 0) return 0;
        
        ArrayList<ReciboDetFormaPago> fp = rcol.getFormasPagoRecibo();         
        float totalPago = 0;
        for(int i=0; i < fp.size(); i++) 
        	totalPago += fp.get(i).getMontoNacional();
        
        return StringUtil.round(totalPago, 2);
    }
	public static String getNumeroRecibo(Context cnt,int numero) 
	{         
        int cr = Integer.parseInt(cnt.getApplicationContext()
									.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE)
									.getString("CerosRellenoReciboCol", "0"));
        char[] num = new char[cr];
        for(int i=0; i < cr; i++) num[i] = '0';
        
        String strNum = new String(num);
        strNum = strNum + numero;
        return strNum.substring(strNum.length() - cr, strNum.length());
    }

	public static String getMoneda(Context cnt) 
	{         
         return (cnt.getApplicationContext()
									.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE)
									.getString("MonedaNacional", "--")).toString(); 
    }
	/*
	public static void calcularDetFacturasRecibo(Context cnt,ReciboColector rcol, Cliente cliente, boolean blnCalcDesc) {        
        if (rcol.getFacturasRecibo().size() == 0) return;        
        ArrayList<ReciboDetFactura> _ff =  rcol.getFacturasRecibo();
        if (_ff == null) return;
        
        //Inicializar descuento PP en cero en todas las facturas
        for(int i=0; i < _ff.size(); i++) {
            _ff.get(i).setMontoDescEspecificoCalc(0);       
            _ff.get(i).setMontoDescEspecifico(0);       
        }
        
        if (blnCalcDesc)
        {
            //Llamar al cálculo del descuento PP (MontoDescEspecifico)
            CalcDescPP_Output outDPP = calcularDescPP(cnt,rcol, cliente);

            Hashtable descFacturas = new Hashtable();
            for(int i=0; i < outDPP.getDetalleFacturas().size(); i++) {
                DescFactura df = (DescFactura)outDPP.getDetalleFacturas().elementAt(i);
                descFacturas.put(df.getIdFactura() + "", new Float(df.getMontoDescPP()));
            }
            
            //Aplicarlo a las facturas
            for(int i=0; i < _ff.size(); i++) {
                ReciboDetFactura _f =  _ff.get(i);
                if (descFacturas.containsKey(_f.getObjFacturaID() + "")) {
                    _f.setMontoDescEspecificoCalc(Float.parseFloat(descFacturas.get(_f.getObjFacturaID() + "") + ""));                    
                    _f.setMontoDescEspecifico(_f.getMontoDescEspecificoCalc());
                }
            }
        } //if (blnCalcDesc)  
        
        //Llamar al cálculo del descuento ocasional y de promociones    
        calcularDescuentoOcasional(cnt,rcol, cliente);
                
        //Otros cálculos
        for(int i=0; i < _ff.size(); i++) {
            ReciboDetFactura _f =_ff.get(i);
            
            //Si no es cancelación, no hay descuento pronto pago
            if (_f.isEsAbono()) _f.setMontoDescEspecifico(0);
            
            //Monto Impuesto
            _f.setMontoImpuesto((_f.getMonto() * _f.getImpuesto()) / _f.getTotalfactura());
            _f.setMontoImpuestoExento(0);
            if (!_f.isEsAbono() && rcol.isExento()) _f.setMontoImpuestoExento(_f.getImpuesto());
                            
            //Monto Neto
            _f.setMontoNeto(_f.getMonto() - _f.getMontoInteres() - _f.getMontoDescEspecifico() - _f.getMontoDescOcasional() - _f.getMontoDescPromocion() - _f.getMontoRetencion() - _f.getMontoOtrasDeducciones() - _f.getMontoImpuestoExento());
        }
                      
    }
	
	public static CalcDescPP_Output calcularDescPP(Context cnt,ReciboColector rcol, Cliente cliente) {
        //Creando objeto a devolver
        CalcDescPP_Output out = new CalcDescPP_Output(0, 0, 0, 0);
        if (rcol.getFacturasRecibo().size() == 0) return out;
        
        ArrayList<ReciboDetFactura> facturasRCol =rcol.getFacturasRecibo();
        if (facturasRCol == null) return out;
        
        //Calcular el monto total de las facturas sobre el cual se aplicará el descuento PP
        for(int i=0; i < facturasRCol.size(); i++) {
            ReciboDetFactura facRCol = facturasRCol.get(i);
            
            //Traer objeto factura en 
            Factura fac = getFacturaCliente(cliente, facRCol.getObjFacturaID());
            if (fac == null) continue;
            if (facRCol.isEsAbono()) continue; //Si no es factura cancelada, saltarla
            
            //Incrementar monto de facturas a cancelar
            out.setMtoTotalFacturasCancelar(out.getMtoTotalFacturasCancelar() + facRCol.getSubTotal());
            
            //Calculando fecha de vencimiento más el tiempo de gracia para aplicar descuento PP
            long fechavencePP = DateUtil.getTime(facRCol.getFechaAplicaDescPP());
            int HolgDiasAPp = Integer.parseInt(Cobro.getParametro(cnt,"HolguraDiasAplicarDescPP")+"");
            fechavencePP = DateUtil.addDays(fechavencePP, HolgDiasAPp); //Agregar tiempo de gracia
            
            //Si la fecha no se ha pasado, se toma en cuenta
            if (fechavencePP >= DateUtil.getTime()) {
                //Validar que no tenga promoción aplicada o
                //si tiene promoción aplicada que ésta indique
                //que se debe aplicar descuento PP                
                if (!fac.getPuedeAplicarDescPP()) continue;
                
                //Agregar subtotal de la factura
                out.setMtoTotalFacturasAplicar(out.getMtoTotalFacturasAplicar() + facRCol.getSubTotal());
                
                //Agregar la factura al datatable de facturas
                DescFactura descFac = new DescFactura(facRCol.getObjFacturaID(), facRCol.getNumero(), facRCol.getSubTotal(), 0, 0);
                
                //Agregar los proveedores de la factura con sus montos
                MontoProveedor[] montosProv = fac.getDetalleMontoProveedor();                
                float prcMtoFactAcumulado = 0;
                for (int j = 0; j < montosProv.length; j++)
                {
                    MontoProveedor mp = montosProv[j];
                    DescProveedor dp = new DescProveedor(facRCol.getObjFacturaID(), mp.getObjProveedorID(), mp.getCodProveedor(), mp.getMonto(), 0, 0, 0, 0);
                    
                    //Calculando porcentaje de monto de proveedor respecto a monto de factura
                    float prcMtoFact = StringUtil.round(mp.getMonto()/facRCol.getSubTotal() * 100, 2);                    
                    if (j == montosProv.length - 1)
                        prcMtoFact = 100 - prcMtoFactAcumulado;
                    else
                        prcMtoFactAcumulado += prcMtoFact;
                    dp.setPorcMtoFact(prcMtoFact);
                    
                    //Asignando el porcentaje de descuento que da el proveedor
                    dp.setPorcDescPP(GetDescPP_Proveedor(cliente, mp.getObjProveedorID()));                    
                    
                    //Agregarlo a la colección de descuentos por proveedor en la factura
                    descFac.addDescProveedor(dp);
                } //for
                
                //Agregarlo a la colección de facturas de la salida
                out.addDetalleFactura(descFac);                
            } //Si la fecha no se ha pasado, se toma en cuenta
        } //Calcular el monto total de las facturas sobre el cual se aplicará el descuento PP
        
        //Si hay un monto a aplicar
        if (out.getMtoTotalFacturasAplicar() > 0) {
            //Calcular monto total de notas de crédito a aplicar
            //Solo aplica el monto proporcional a las facturas vigentes que se están cancelando
            float totalNC = getTotalNC_RCol(rcol);
            
            //Aplicar solo el monto porcentual respecto a las facturas vigentes
            out.setTotalNCAplicar(StringUtil.round(totalNC * (out.getMtoTotalFacturasAplicar()/out.getMtoTotalFacturasCancelar()), 2));
            
            //Calculando el porcentaje que representa el monto de cada factura con respecto al monto total de las facturas
            //Igual para los proveedores de cada factura
            float porcAcumuladoFacts = 0;
            for (int i = 0; i < out.getDetalleFacturas().size(); i++)
            {
                DescFactura df = (DescFactura)out.getDetalleFacturas().elementAt(i);
                float prcFact = StringUtil.round(df.getMontoFact()/out.getMtoTotalFacturasAplicar()*100, 2);

                if (i == out.getDetalleFacturas().size() - 1)
                    prcFact = 100 - porcAcumuladoFacts;
                else
                    porcAcumuladoFacts += prcFact;
                df.setPorcMtoTotal(prcFact);
                
                //Gruardar cambio
                out.getDetalleFacturas().setElementAt(df, i);
            } //Calculando el porcentaje que representa el monto de cada factura con respecto al monto total de las facturas
            
            //Monto de descuento a aplicar = Monto de facturas - montos de notas de crédito
            out.setMontoAplicarDescPP(out.getMtoTotalFacturasAplicar() - out.gettotalNCAplicar());
            
            //Calculando el monto de descuento a nivel de proveedores
            for (int i = 0; i < out.getDetalleFacturas().size(); i++)
            {
                DescFactura df = (DescFactura)out.getDetalleFacturas().elementAt(i);
                Vector descuentosProv = df.getDescuentosProveedor();  
                
                for (int j = 0; j < descuentosProv.size(); j++)
                {
                    DescProveedor descProv = (DescProveedor)descuentosProv.elementAt(j);                    
                    float prcProv = StringUtil.round(descProv.getMontoFactProv()*(df.getPorcMtoTotal()/100)/df.getMontoFact()*100, 2);
                    float desc = StringUtil.round(out.getMontoAplicarDescPP()*(prcProv/100)*(descProv.getPorcDescPP()/100), 2);
                        
                    descProv.setMtoAplicable(StringUtil.round(out.getMontoAplicarDescPP()*(prcProv/100), 2));
                    descProv.setMontoDescPPProv(desc);
                    
                    //Guardar cambios
                    descuentosProv.setElementAt(descProv, j);
                }
                //Guardar cambios
                out.getDetalleFacturas().setElementAt(df, i);            
            } //Calculando el monto de descuento a nivel de proveedores
                
            //Sumarizando los  montos de descuento PP a las facturas            
            for (int i = 0; i < out.getDetalleFacturas().size(); i++)
            {
                DescFactura df = (DescFactura)out.getDetalleFacturas().elementAt(i);
                Vector descuentosProv = df.getDescuentosProveedor();  
                float total = 0;
                for (int j = 0; j < descuentosProv.size(); j++)
                {
                    DescProveedor descProv = (DescProveedor)descuentosProv.elementAt(j);
                    total += descProv.getMontoDescPPProv();
                }
                //Guardar cambios
                df.setMontoDescPP(total);
                out.getDetalleFacturas().setElementAt(df, i);     
            } //Sumarizando los  montos de descuento PP a las facturas
        } //Si hay un monto a aplicar
    
        return out;
    }
	*/
	/*
	 public static CalcDescOca_Output calcularDescuentoOcasional(Context cnt,ReciboColector rcol, Cliente cliente) {
	        CalcDescOca_Output out = new CalcDescOca_Output(0, 0);        
	        if (rcol.getFacturasRecibo().size() == 0) return out;
	        
	        ArrayList<ReciboDetFactura> facturasRCol = rcol.getFacturasRecibo();
	        if (facturasRCol == null) return out;
	        
	        //Montos para cálculo
	        float mtoTotalFacturas = 0; //Facturas seleccionadas para cancelar        
	        float mtoFacturasVigentes = 0; //Facturas seleccionadas para cancelar vigentes
	        float mtoFacturasVencidas = 0; //Facturas seleccionadas para cancelar vencidas
	        float mtoTotalNC = 0; //Total de notas de crédito
	        float mtoNcProporcionalVigente = 0; //Monto de notas de crédito proporcional al total de facturas vigentes
	        float mtoNcProporcionalVencidas = 0; //Monto de notas de crédito proporcional al total de facturas vencidas
	        
	        //Parámetro para determinar si una factura está vencida y ya pasó el periódo de gracia
	        int HolgDiasAPp = Integer.parseInt(Cobro.getParametro(cnt,"HolguraDiasAplicarDescPP")+"");
	        
	        //Calculando montos de facturas
	        for(int i=0; i < facturasRCol.size(); i++) 
	        {                
	            ReciboDetFactura facRCol =  facturasRCol.get(i);
	            
	            //Tiene que estar seleccionada y cancelándose
	            if (facRCol.isEsAbono()) {
	                facRCol.setPorcDescPromo(0);
	                facRCol.setMontoDescPromocion(0);
	                continue;
	            }
	            
	            //Calculando fecha de vencimiento más el tiempo de holgura para aplicar descuento PP
	            long fechaVencePP = DateUtil.getTime(facRCol.getFechaAplicaDescPP());                        
	            fechaVencePP = DateUtil.addDays(fechaVencePP, HolgDiasAPp); //Agregar tiempo de holgura
	            boolean tieneDescOcaPromo = false; //Determina si la factura tiene descuento ocasional por promoción
	            boolean tieneDescOcaUsr = false; //Determina si la factura tiene descuento ocasional por negociación del usuario con el cliente
	    
	            //Tiene que tener descuento ocasional por promoción
	            //o tiene que tener descuento ocasional por negociación (ingresado por el usuario)
	            float prcDescOca = facRCol.getPorcDescOcasional();
	            tieneDescOcaUsr = (prcDescOca > 0);
	            
	            //Ver si el pedido de la factura tiene asociada una promoción aplicable
	            //al momento de cobro, traer lista de promociones aplicadas al pedido de la factura
	            Factura fac = getFacturaCliente(cliente, facRCol.getObjFacturaID());
	                        
	            //Averiguar si hay descuento por promoción que aplicar
	            if ((fac != null) && (fac.getDetallePromocionCobro() != null) && (fac.getDetallePromocionCobro() != null)) {
	                PromocionCobro[] promociones = fac.getDetallePromocionCobro();
	                
	                //Para cada promoción
	                for(int j=0; j < promociones.length; j++) {
	                    PromocionCobro promo = promociones[j];
	                    
	                    //Aplica si el descuento aplica a todas las facturas (vencidas y vigentes)
	                    tieneDescOcaPromo = (promo.getFacturasAplicacion().compareTo("T") == 0);
	                    
	                    //ó si la factura actual está vencida y la promoción solo aplica a vencidas
	                    if (!tieneDescOcaPromo)
	                        tieneDescOcaPromo = ((fechaVencePP < DateUtil.getTime()) && ((promo.getFacturasAplicacion().compareTo("V") == 0)));

	                    //ó si la factura actual está vigente y la promoción solo aplica a vigentes (Corrientes)
	                    if (!tieneDescOcaPromo)
	                        tieneDescOcaPromo = ((fechaVencePP >= DateUtil.getTime()) && (promo.getFacturasAplicacion().compareTo("C") == 0));
	                        
	                    if (tieneDescOcaPromo) //Si se le debe aplicar descuento por promoción
	                    {
	                        //Inicializando valores
	                        facRCol.setPorcDescPromo(0);
	                        facRCol.setMontoDescPromocion(0);
	                
	                        //Si es porcentaje, fijar porcentaje en la factura y calcular posteriormente
	                        if (promo.getTipoDescuento().compareTo("P") == 0)
	                            facRCol.setPorcDescPromo(promo.getDescuento());                            
	                        else //Es un monto específico
	                            facRCol.setMontoDescPromocion(promo.getDescuento());                            
	                        break;    
	                    } //Si se le debe aplicar descuento por promoción            
	                } //Para cada promoción
	            } //Averiguar si hay descuento por promoción que aplicar
	            
	            float totalFact = facRCol.getSubTotal();
	            mtoTotalFacturas += totalFact;

	            //Si la fecha no se ha pasado, se suma a total de facturas vigentes, en caso contario a las vencidas
	            if (fechaVencePP >= DateUtil.getTime())
	                mtoFacturasVigentes += totalFact;
	            else
	                mtoFacturasVencidas += totalFact;
	        } //Calculando montos de facturas
	        
	        //Si hay monto que procesar
	        if (mtoTotalFacturas > 0)
	        {
	            //Calculando montos de notas de crédito
	            mtoTotalNC = getTotalNC_RCol(rcol);
	            
	            //Monto porcentual respecto a las facturas vigentes
	            mtoNcProporcionalVigente = StringUtil.round(mtoTotalNC * (mtoFacturasVigentes / mtoTotalFacturas), 2);

	            //Monto porcentual respecto a las facturas vencidas
	            mtoNcProporcionalVencidas = StringUtil.round(mtoTotalNC * (mtoFacturasVencidas / mtoTotalFacturas), 2);
	            
	            //Monto aplicable final para vencidas o vigentes
	            float mtoAplicarVigente = mtoFacturasVigentes - mtoNcProporcionalVigente;
	            float mtoAplicarVencidas = mtoFacturasVencidas - mtoNcProporcionalVencidas;
	            
	            //Realizar aplicación de los porcentajes de decuentos ocasionales 
	            //(tanto por promoción como por negociación del usuario)
	            //Calculando montos de facturas
	            for(int i=0; i < facturasRCol.size(); i++) {                
	                ReciboDetFactura facRCol = facturasRCol.get(i);
	                float descOca = 0;
	                float descPromo = 0;
	                
	                //Tiene que estar seleccionada y cancelándose
	                if (facRCol.isEsAbono()) {
	                    facRCol.setMontoDescOcasional(descOca);
	                    facRCol.setMontoDescPromocion(descPromo);
	                    continue;
	                }
	                
	                //Ver si la factura está vencida o no
	                //Calculando fecha de vencimiento más el tiempo de gracia para aplicar descuento PP
	                long fechaVencePP = DateUtil.getTime(facRCol.getFechaAplicaDescPP());                        
	                fechaVencePP = DateUtil.addDays(fechaVencePP, HolgDiasAPp); //Agregar tiempo de holgura                            
	                boolean vencida = (fechaVencePP < DateUtil.getTime());
	                
	                //Montos a aplicar a la factura
	                float mtoTotalFacturasAplicar = (vencida ? mtoFacturasVencidas : mtoFacturasVigentes);
	                float mtoAplicar = (vencida ? mtoAplicarVencidas : mtoAplicarVigente);
	                float mtoFact = facRCol.getSubTotal();
	                
	                //Ver si hay que aplicar descuento ocasional
	                if (facRCol.getPorcDescOcasional() > 0)
	                    descOca = StringUtil.round((mtoFact / mtoTotalFacturasAplicar) * (mtoAplicar * (facRCol.getPorcDescOcasional() / 100)), 2);
	                    
	                //Ver si hay que aplicar descuento por promoción
	                descPromo = facRCol.getMontoDescPromocion();
	                if (facRCol.getPorcDescPromo() > 0)
	                    descPromo = StringUtil.round((mtoFact / mtoTotalFacturasAplicar) * (mtoAplicar * (facRCol.getPorcDescPromo() / 100)), 2);
	                
	                //Salvar los descuentos en la factura
	                facRCol.setMontoDescOcasional(descOca);
	                facRCol.setMontoDescPromocion(descPromo);
	            } //Realizar aplicación de los porcentajes de decuentos ocasionales 
	            
	        } //Si hay monto que procesar
	        
	        out.setMtoFacturasVencidas(mtoFacturasVencidas);
	        out.setMtoNcProporcionalVencidas(mtoNcProporcionalVencidas);
	        out.mtoFacturasVencidas = mtoFacturasVencidas;
	        out.mtoFacturasVigentes = mtoFacturasVigentes;
	        out.mtoTotalFacturas = mtoTotalFacturas;
	        out.mtoTotalNC = mtoTotalNC;
	        out.mtoNcProporcionalVigente = mtoNcProporcionalVigente;
	        
	        return out;
	    } //calcularDescuentoOcasional
	*/
	private static float GetDescPP_Proveedor(Cliente cliente, long idProveedor) {
        if (cliente.getDescuentosProveedor() == null) return 0;
        
        DescuentoProveedor[] descuentos = cliente.getDescuentosProveedor();
        if (descuentos == null) return 0;
        
        for(int i=0; i < descuentos.length; i++) {
            DescuentoProveedor desc = descuentos[i];
            if (desc.getObjProveedorID() == idProveedor) return desc.getPrcDescuento();
        }
        
        return 0;
    }
	
	private static float getTotalNC_RCol(ReciboColector rcol) {
        float totalNC = 0;
        
        if (rcol.getNotasCreditoRecibo().size() != 0) {
            ArrayList<ReciboDetNC> _cc =  rcol.getNotasCreditoRecibo();
            if (_cc != null) {
                for(int i=0; i < _cc.size(); i++) totalNC += _cc.get(i).getMonto();                
            }          
        } 
        
        return StringUtil.round(totalNC, 2);
    }
	
	private static Factura getFacturaCliente(Cliente cliente, long idFactura) {
        if (cliente.getFacturasPendientes() == null) return null;
        Factura[] facturas = cliente.getFacturasPendientes();
        if (facturas == null) return null;
        
        for(int i=0; i < facturas.length; i++) {
            Factura fac = facturas[i];
            
            if (fac.getId() == idFactura) return fac;
        }
        
        return null;
    }
	
	public static Object getParametro(Context cnt,String propiedad) 
	{         
         return ( (cnt.getApplicationContext()
									.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE)
									.getString(propiedad, "0"))); 
    }
	
	public static int cantFacturas(ReciboColector recibo) {
        int count = 0;
        if (recibo.getFacturasRecibo().size() != 0) 
             count +=recibo.getFacturasRecibo().size(); 
        return count;
    } 
	
    public static int cantFPs(ReciboColector recibo) {
        int count = 0;
        if (recibo.getFormasPagoRecibo().size() != 0) 
        		count += recibo.getFormasPagoRecibo().size(); 
        return count;
    }
    
    public static int cantNDs(ReciboColector recibo) {
        int count = 0;        
        if (recibo.getNotasDebitoRecibo().size() != 0)
        	count += recibo.getNotasDebitoRecibo().size(); 
        return count;
    }
    
    public static int  cantNCs(ReciboColector recibo) {
        int count = 0;        
        if (recibo.getNotasCreditoRecibo().size() != 0)
        	count += recibo.getNotasCreditoRecibo().size(); 
        return count;
    }


    public static int FacturaEstaEnOtroRecibo(ContentResolver content,long idFactura, boolean agregando) {
    	int referencia =0;
    			
        ArrayList<ReciboColector> list = ModelRecibo.getArrayRecibosFromLocalHost(content);
        if(list== null) 
        	return 0;
        
        for(int r=0; r< list.size(); r++) {
            ReciboColector rowrecibo = list.get(r);
            if(rowrecibo.getFacturasRecibo()==null) continue;
            
            if("REGISTRADO".equals(rowrecibo.getDescEstado()) || (agregando && "PAGADO_OFFLINE".equals(rowrecibo.getDescEstado()))) {
                ArrayList<ReciboDetFactura> facturas= rowrecibo.getFacturasRecibo();
                
                for(ReciboDetFactura f : facturas) {
                    if (f.getObjFacturaID() == idFactura){ 
                    	referencia=rowrecibo.getReferencia();
                    	return referencia;
                    }
                }
            }
        }
        return referencia;
    }
    //Verifica si una nota de débito ya está incluida en un recibo local    
    public static int NDEstaEnOtroRecibo(ContentResolver content,long idND, boolean agregando) {
    	int referencia =0;
    	ArrayList<ReciboColector> list = ModelRecibo.getArrayRecibosFromLocalHost(content);
        if(list.size()==0) return 0;
        for(int r=0; r< list.size(); r++) {
            ReciboColector rowrecibo = list.get(r);
            if(rowrecibo.getNotasDebitoRecibo()==null) continue;
            
            if("REGISTRADO".equals(rowrecibo.getDescEstado()) || (agregando && "PAGADO_OFFLINE".equals(rowrecibo.getDescEstado()))) {
                ArrayList<ReciboDetND> notasdebito= rowrecibo.getNotasDebitoRecibo();
                for(ReciboDetND n : notasdebito) {
                    if (n.getObjNotaDebitoID() == idND) return referencia=rowrecibo.getReferencia();
                }
            }
        }
    	 return referencia;
    }
    
    public static float getInteresMoratorio(Context cnt,long iFechaVen, float saldo) {
    	float interes = 0;
    	String interesmoratorio=cnt.getApplicationContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("PorcInteresMoratorio", "0");
    	float porcInteresMoratorio = Float.parseFloat(interesmoratorio);    
    	if (porcInteresMoratorio == 0) return 0;
    	
    	int diasDespuesVenceCalculaMora = Integer.parseInt(interesmoratorio);
    	String sFechaVen = iFechaVen + "";
        sFechaVen = sFechaVen.substring(0, 8);        
        int fechaVen = Integer.parseInt(sFechaVen);
        long fechaMora = DateUtil.addDays(DateUtil.getTime(fechaVen), diasDespuesVenceCalculaMora);
    	
        long fechaActual = DateUtil.getTime(DateUtil.getToday());        
        if (fechaMora < fechaActual)
            interes = saldo * porcInteresMoratorio / 100;
        
    	return StringUtil.round(interes, 2);
    }
    
    public static void ActualizaMtoNetoFacturasrecibo(ReciboColector rcol) {
        if (rcol.getFacturasRecibo() == null) return;        
        ReciboDetFactura[] _ff = getArraOfFacturaDetalle( rcol.getFacturasRecibo() );
        if (_ff == null) return;
        
        //Inicializar descuento PP en cero en todas las facturas
        for(int i=0; i < _ff.length; i++) {
            ReciboDetFactura _f = _ff[i];
            _f.setMontoNeto(_f.getMonto() - _f.getMontoInteres() - _f.getMontoDescEspecifico() - _f.getMontoDescOcasional() - _f.getMontoDescPromocion() - _f.getMontoRetencion() - _f.getMontoOtrasDeducciones() - _f.getMontoImpuestoExento());
        }        
   }
    

    public static void ActualizaTotalFacturas(ReciboColector rcol) {
    	if (rcol.getFacturasRecibo() == null) return;        
        ReciboDetFactura[] _ff = getArraOfFacturaDetalle( rcol.getFacturasRecibo() );
        if (_ff == null) return;
        
        rcol.setTotalFacturas(0.00F);
      //Inicializar descuento PP en cero en todas las facturas
        for(int i=0; i < _ff.length; i++) {
            ReciboDetFactura _f = _ff[i];
            rcol.setTotalFacturas(rcol.getTotalFacturas() + _f.getMonto() );
        }  
    	
    }
    
    public static void ActualizaTotalNotasDebito(ReciboColector rcol) {    	    
        //ReciboDetND[] _ff = getArraOfNotasDebitoDetalle( rcol.getNotasDebitoRecibo() );
        if (rcol == null) return;   
        if (rcol.getNotasDebitoRecibo() == null || rcol.getNotasDebitoRecibo().size() == 0) return;    
        rcol.setTotalND(0.00F);
      //Inicializar descuento PP en cero en todas ls facturas
        for(ReciboDetND nd : rcol.getNotasDebitoRecibo()) {
        	ReciboDetND _f = nd;
            rcol.setTotalND(rcol.getTotalND() + _f.getMonto() );
        }  
    	
    }
  
	private static ReciboDetFactura[] getArraOfFacturaDetalle(List<ReciboDetFactura> objList) {
		   ReciboDetFactura[] list = new ReciboDetFactura[objList.size()];
		   
		   for(int rdf = 0; ( objList != null && rdf < objList.size() ) ; rdf++  ) {
			   list[rdf] = objList.get(rdf);
		   }
		   return list;
	 }
    
    public static void calcularDetFacturasRecibo(Context cnt,ReciboColector rcol, Cliente cliente, boolean blnCalcDesc) {  
        if (rcol.getFacturasRecibo() == null) return;        
        ArrayList<ReciboDetFactura> facturas = rcol.getFacturasRecibo();
        if (facturas == null) return;
        
        //Inicializar descuento PP en cero en todas las facturas
        for (ReciboDetFactura reciboDetFactura : facturas) {
			reciboDetFactura.setMontoDescEspecificoCalc(0);
			reciboDetFactura.setMontoDescEspecifico(0);
		}
        
        if (blnCalcDesc)
        {
        	//Llamar al cálculo del descuento PP (MontoDescEspecifico)
            CalcDescPP_Output outDPP = calcularDescPP(cnt,rcol, cliente);
            Hashtable descFacturas = new Hashtable();
            
            for(int i=0; i < outDPP.getDetalleFacturas().size(); i++) {
                DescFactura df = (DescFactura)outDPP.getDetalleFacturas().elementAt(i);
                descFacturas.put(df.getIdFactura() + "", new Float(df.getMontoDescPP()));
            }
            //Aplicarlo a las facturas
            for (ReciboDetFactura reciboDetFactura : facturas) {
            	if (descFacturas.containsKey(reciboDetFactura.getObjFacturaID() + "")) {
            		reciboDetFactura.setMontoDescEspecificoCalc(Float.parseFloat(descFacturas.get(reciboDetFactura.getObjFacturaID() + "") + ""));  
            		reciboDetFactura.setMontoDescEspecifico(reciboDetFactura.getMontoDescEspecificoCalc());
            	}
			}
        } //if (blnCalcDesc)  
        
        //Llamar al cálculo del descuento ocasional y de promociones    
        calcularDescuentoOcasional(cnt,rcol, cliente);
        
        for (ReciboDetFactura reciboDetFactura : facturas) {
        	 //Si no es cancelación, no hay descuento pronto pago
            if (reciboDetFactura.isEsAbono()) reciboDetFactura.setMontoDescEspecifico(0);
            
            //Monto Impuesto
            reciboDetFactura.setMontoImpuesto((reciboDetFactura.getMonto() * reciboDetFactura.getImpuesto()) / reciboDetFactura.getTotalfactura());
            reciboDetFactura.setMontoImpuestoExento(0);
            if (!reciboDetFactura.isEsAbono() && rcol.isExento()) reciboDetFactura.setMontoImpuestoExento(reciboDetFactura.getImpuesto());
            
            //Monto Neto
            reciboDetFactura.setMontoNeto(reciboDetFactura.getMonto() - reciboDetFactura.getMontoInteres() - reciboDetFactura.getMontoDescEspecifico() - reciboDetFactura.getMontoDescOcasional() - reciboDetFactura.getMontoDescPromocion() - reciboDetFactura.getMontoRetencion() - reciboDetFactura.getMontoOtrasDeducciones() - reciboDetFactura.getMontoImpuestoExento());
		}   
    }
    
    public static CalcDescPP_Output calcularDescPP(Context cnt,ReciboColector rcol, Cliente cliente) {
    	//Creando objeto a devolver
        CalcDescPP_Output out = new CalcDescPP_Output(0, 0, 0, 0);
        
        ArrayList<ReciboDetFactura> facturasRCol = rcol.getFacturasRecibo();
        if(facturasRCol== null) return out;
        
        for (ReciboDetFactura reciboDetFactura : facturasRCol) {
        	//Traer objeto factura en 
            Factura fac = getFacturaCliente(cliente, reciboDetFactura.getObjFacturaID());
            if (fac == null) continue;
            if (reciboDetFactura.isEsAbono()) continue; //Si no es factura cancelada, saltarla
            
            //Incrementar monto de facturas a cancelar
            out.setMtoTotalFacturasCancelar(out.getMtoTotalFacturasCancelar() + reciboDetFactura.getSubTotal());
            
            //Calculando fecha de vencimiento más el tiempo de gracia para aplicar descuento PP
            long fechavencePP = DateUtil.getTime(reciboDetFactura.getFechaAplicaDescPP());
            String days=cnt.getApplicationContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("HolguraDiasAplicarDescPP", "0");
            int HolgDiasAPp = Integer.parseInt(days);
            fechavencePP = DateUtil.addDays(fechavencePP, HolgDiasAPp); //Agregar tiempo de gracia
            //Si la fecha no se ha pasado, se toma en cuenta
            if (fechavencePP >= DateUtil.getTime()) {
            	//Validar que no tenga promoción aplicada o
                //si tiene promoción aplicada que ésta indique
                //que se debe aplicar descuento PP                
                if (!fac.getPuedeAplicarDescPP()) continue;
            	
              //Agregar subtotal de la factura
              out.setMtoTotalFacturasAplicar(out.getMtoTotalFacturasAplicar() + reciboDetFactura.getSubTotal());
            	
            //Agregar la factura al datatable de facturas
            DescFactura descFac = new DescFactura(reciboDetFactura.getObjFacturaID(), reciboDetFactura.getNumero(), reciboDetFactura.getSubTotal(), 0, 0);
            
            //Agregar los proveedores de la factura con sus montos
            MontoProveedor[] montosProv = fac.getDetalleMontoProveedor();                
            float prcMtoFactAcumulado = 0;
            for (int j = 0; j < montosProv.length; j++)
            {
            	MontoProveedor mp = montosProv[j];
                DescProveedor dp = new DescProveedor(reciboDetFactura.getObjFacturaID(), mp.getObjProveedorID(), mp.getCodProveedor(), mp.getMonto(), 0, 0, 0, 0);
                //Calculando porcentaje de monto de proveedor respecto a monto de factura
                float prcMtoFact = StringUtil.round(mp.getMonto()/reciboDetFactura.getSubTotal() * 100, 2);   
                if (j == montosProv.length - 1)
                    prcMtoFact = 100 - prcMtoFactAcumulado;
                else
                    prcMtoFactAcumulado += prcMtoFact;
                dp.setPorcMtoFact(prcMtoFact);
                
                //Asignando el porcentaje de descuento que da el proveedor
                dp.setPorcDescPP(GetDescPP_Proveedor(cliente, mp.getObjProveedorID()));                    
                
                //Agregarlo a la colección de descuentos por proveedor en la factura
                descFac.addDescProveedor(dp);
            }
            //Agregarlo a la colección de facturas de la salida
            out.addDetalleFactura(descFac); 
            }//Si la fecha no se ha pasado, se toma en cuenta
         }//Calcular el monto total de las facturas sobre el cual se aplicará el descuento PP
        
        //Si hay un monto a aplicar
        if (out.getMtoTotalFacturasAplicar() > 0) {
        	//Calcular monto total de notas de crédito a aplicar
            //Solo aplica el monto proporcional a las facturas vigentes que se están cancelando
            float totalNC = getTotalNC_RCol(rcol);
            //Aplicar solo el monto porcentual respecto a las facturas vigentes
            out.setTotalNCAplicar(StringUtil.round(totalNC * (out.getMtoTotalFacturasAplicar()/out.getMtoTotalFacturasCancelar()), 2));
            //Calculando el porcentaje que representa el monto de cada factura con respecto al monto total de las facturas
            //Igual para los proveedores de cada factura
            float porcAcumuladoFacts = 0;
            for (int i = 0; i < out.getDetalleFacturas().size(); i++)
            {
                DescFactura df = (DescFactura)out.getDetalleFacturas().elementAt(i);
                float prcFact = StringUtil.round(df.getMontoFact()/out.getMtoTotalFacturasAplicar()*100, 2);

                if (i == out.getDetalleFacturas().size() - 1)
                    prcFact = 100 - porcAcumuladoFacts;
                else
                    porcAcumuladoFacts += prcFact;
                df.setPorcMtoTotal(prcFact);
                
                //Gruardar cambio
                out.getDetalleFacturas().setElementAt(df, i);
            } //Calculando el porcentaje que representa el monto de cada factura con respecto al monto total de las facturas
            
            //Monto de descuento a aplicar = Monto de facturas - montos de notas de crédito
            out.setMontoAplicarDescPP(out.getMtoTotalFacturasAplicar() - out.gettotalNCAplicar());
            
            //Calculando el monto de descuento a nivel de proveedores
            for (int i = 0; i < out.getDetalleFacturas().size(); i++)
            {
                DescFactura df = (DescFactura)out.getDetalleFacturas().elementAt(i);
                Vector descuentosProv = df.getDescuentosProveedor();  
                
                for (int j = 0; j < descuentosProv.size(); j++)
                {
                    DescProveedor descProv = (DescProveedor)descuentosProv.elementAt(j);                    
                    float prcProv = StringUtil.round(descProv.getMontoFactProv()*(df.getPorcMtoTotal()/100)/df.getMontoFact()*100, 2);
                    float desc = StringUtil.round(out.getMontoAplicarDescPP()*(prcProv/100)*(descProv.getPorcDescPP()/100), 2);
                        
                    descProv.setMtoAplicable(StringUtil.round(out.getMontoAplicarDescPP()*(prcProv/100), 2));
                    descProv.setMontoDescPPProv(desc);
                    
                    //Guardar cambios
                    descuentosProv.setElementAt(descProv, j);
                }
                //Guardar cambios
                out.getDetalleFacturas().setElementAt(df, i);            
            } //Calculando el monto de descuento a nivel de proveedores
            
            //Sumarizando los  montos de descuento PP a las facturas            
            for (int i = 0; i < out.getDetalleFacturas().size(); i++)
            {
                DescFactura df = (DescFactura)out.getDetalleFacturas().elementAt(i);
                Vector descuentosProv = df.getDescuentosProveedor();  
                float total = 0;
                for (int j = 0; j < descuentosProv.size(); j++)
                {
                    DescProveedor descProv = (DescProveedor)descuentosProv.elementAt(j);
                    total += descProv.getMontoDescPPProv();
                }
                //Guardar cambios
                df.setMontoDescPP(total);
                out.getDetalleFacturas().setElementAt(df, i);     
            } //Sumarizando los  montos de descuento PP a las facturas
            
        } //Si hay un monto a aplicar
        return out;
    }
   
    public static CalcDescOca_Output calcularDescuentoOcasional(Context cnt,ReciboColector rcol, Cliente cliente) {
    	   CalcDescOca_Output out = new CalcDescOca_Output(0, 0);
    	   if (rcol.getFacturasRecibo() == null) return out;
    	   
    	   ArrayList<ReciboDetFactura> facturasRCol = rcol.getFacturasRecibo();
           if (facturasRCol == null) return out;
           
           //Montos para cálculo
           float mtoTotalFacturas = 0; //Facturas seleccionadas para cancelar        
           float mtoFacturasVigentes = 0; //Facturas seleccionadas para cancelar vigentes
           float mtoFacturasVencidas = 0; //Facturas seleccionadas para cancelar vencidas
           float mtoTotalNC = 0; //Total de notas de crédito
           float mtoNcProporcionalVigente = 0; //Monto de notas de crédito proporcional al total de facturas vigentes
           float mtoNcProporcionalVencidas = 0; //Monto de notas de crédito proporcional al total de facturas vencidas
           
           String days=cnt.getApplicationContext().getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("HolguraDiasAplicarDescPP", "0");
           //Parámetro para determinar si una factura está vencida y ya pasó el periódo de gracia
           int HolgDiasAPp = Integer.parseInt(days);
           //Calculando montos de facturas
           for (ReciboDetFactura reciboDetFactura : facturasRCol) {
			
        	   //Tiene que estar seleccionada y cancelándose
               if (reciboDetFactura.isEsAbono()) {
            	   reciboDetFactura.setPorcDescPromo(0);
            	   reciboDetFactura.setMontoDescPromocion(0);
                   continue;
               }
               //Calculando fecha de vencimiento más el tiempo de holgura para aplicar descuento PP
               long fechaVencePP = DateUtil.getTime(reciboDetFactura.getFechaAplicaDescPP());                        
               fechaVencePP = DateUtil.addDays(fechaVencePP, HolgDiasAPp); //Agregar tiempo de holgura
               boolean tieneDescOcaPromo = false; //Determina si la factura tiene descuento ocasional por promoción
               boolean tieneDescOcaUsr = false; //Determina si la factura tiene descuento ocasional por negociación del usuario con el cliente
               
               //Tiene que tener descuento ocasional por promoción
               //o tiene que tener descuento ocasional por negociación (ingresado por el usuario)
               float prcDescOca = reciboDetFactura.getPorcDescOcasional();
               tieneDescOcaUsr = (prcDescOca > 0);
               
               //Ver si el pedido de la factura tiene asociada una promoción aplicable
               //al momento de cobro, traer lista de promociones aplicadas al pedido de la factura
               Factura fac = getFacturaCliente(cliente, reciboDetFactura.getObjFacturaID());
               //Averiguar si hay descuento por promoción que aplicar
               if ((fac != null) && (fac.getDetallePromocionCobro() != null) && (fac.getDetallePromocionCobro() != null)) {
            	   PromocionCobro[] promociones = fac.getDetallePromocionCobro();
                   //Para cada promoción
                   for(int j=0; j < promociones.length; j++) {
                	   PromocionCobro promo = promociones[j];
                  	   //Aplica si el descuento aplica a todas las facturas (vencidas y vigentes)
                       tieneDescOcaPromo = (promo.getFacturasAplicacion().compareTo("T") == 0);
                       //ó si la factura actual está vencida y la promoción solo aplica a vencidas
                       if (!tieneDescOcaPromo)
                           tieneDescOcaPromo = ((fechaVencePP < DateUtil.getTime()) && ((promo.getFacturasAplicacion().compareTo("V") == 0)));
                       
                       //ó si la factura actual está vigente y la promoción solo aplica a vigentes (Corrientes)
                       if (!tieneDescOcaPromo)
                           tieneDescOcaPromo = ((fechaVencePP >= DateUtil.getTime()) && (promo.getFacturasAplicacion().compareTo("C") == 0));
                       
                       if (tieneDescOcaPromo) //Si se le debe aplicar descuento por promoción
                       {
                    	   //Inicializando valores
                    	   reciboDetFactura.setPorcDescPromo(0);
                    	   reciboDetFactura.setMontoDescPromocion(0);
                   
                           //Si es porcentaje, fijar porcentaje en la factura y calcular posteriormente
                           if (promo.getTipoDescuento().compareTo("P") == 0)
                        	   reciboDetFactura.setPorcDescPromo(promo.getDescuento());                            
                           else //Es un monto específico
                        	   reciboDetFactura.setMontoDescPromocion(promo.getDescuento());                            
                           break; 
                       } //Si se le debe aplicar descuento por promoción            
                   } //Para cada promoción
               } //Averiguar si hay descuento por promoción que aplicar
               
               float totalFact = reciboDetFactura.getSubTotal();
               mtoTotalFacturas += totalFact;
               
               //Si la fecha no se ha pasado, se suma a total de facturas vigentes, en caso contario a las vencidas
               if (fechaVencePP >= DateUtil.getTime())
                   mtoFacturasVigentes += totalFact;
               else
                   mtoFacturasVencidas += totalFact;
           } //Calculando montos de facturas
    	   
           //Si hay monto que procesar
           if (mtoTotalFacturas > 0)
           {
               //Calculando montos de notas de crédito
               mtoTotalNC = getTotalNC_RCol(rcol);
               
               //Monto porcentual respecto a las facturas vigentes
               mtoNcProporcionalVigente = StringUtil.round(mtoTotalNC * (mtoFacturasVigentes / mtoTotalFacturas), 2);

               //Monto porcentual respecto a las facturas vencidas
               mtoNcProporcionalVencidas = StringUtil.round(mtoTotalNC * (mtoFacturasVencidas / mtoTotalFacturas), 2);
               
               //Monto aplicable final para vencidas o vigentes
               float mtoAplicarVigente = mtoFacturasVigentes - mtoNcProporcionalVigente;
               float mtoAplicarVencidas = mtoFacturasVencidas - mtoNcProporcionalVencidas;
               
               //Realizar aplicación de los porcentajes de decuentos ocasionales 
               //(tanto por promoción como por negociación del usuario)
               //Calculando montos de facturas
              for (ReciboDetFactura reciboDetFactura : facturasRCol) {
				
            	  float descOca = 0;
                  float descPromo = 0;
                  
                  //Tiene que estar seleccionada y cancelándose
                  if (reciboDetFactura.isEsAbono()) {
                	  reciboDetFactura.setMontoDescOcasional(descOca);
                	  reciboDetFactura.setMontoDescPromocion(descPromo);
                      continue;
                  }
                  
                  //Ver si la factura está vencida o no
                  //Calculando fecha de vencimiento más el tiempo de gracia para aplicar descuento PP
                  long fechaVencePP = DateUtil.getTime(reciboDetFactura.getFechaAplicaDescPP());                        
                  fechaVencePP = DateUtil.addDays(fechaVencePP, HolgDiasAPp); //Agregar tiempo de holgura                            
                  boolean vencida = (fechaVencePP < DateUtil.getTime());
                  
                  //Montos a aplicar a la factura
                  float mtoTotalFacturasAplicar = (vencida ? mtoFacturasVencidas : mtoFacturasVigentes);
                  float mtoAplicar = (vencida ? mtoAplicarVencidas : mtoAplicarVigente);
                  float mtoFact = reciboDetFactura.getSubTotal();
                  
                  //Ver si hay que aplicar descuento ocasional
                  if (reciboDetFactura.getPorcDescOcasional() > 0)
                      descOca = StringUtil.round((mtoFact / mtoTotalFacturasAplicar) * (mtoAplicar * (reciboDetFactura.getPorcDescOcasional() / 100)), 2);
                      
                  //Ver si hay que aplicar descuento por promoción
                  descPromo = reciboDetFactura.getMontoDescPromocion();
                  if (reciboDetFactura.getPorcDescPromo() > 0)
                      descPromo = StringUtil.round((mtoFact / mtoTotalFacturasAplicar) * (mtoAplicar * (reciboDetFactura.getPorcDescPromo() / 100)), 2);
                  
                  //Salvar los descuentos en la factura
                  reciboDetFactura.setMontoDescOcasional(descOca);
                  reciboDetFactura.setMontoDescPromocion(descPromo);
              } //Realizar aplicación de los porcentajes de decuentos ocasionales 
              
           } //Si hay monto que procesar
           
           out.setMtoFacturasVencidas(mtoFacturasVencidas);
           out.setMtoNcProporcionalVencidas(mtoNcProporcionalVencidas);
           out.mtoFacturasVencidas = mtoFacturasVencidas;
           out.mtoFacturasVigentes = mtoFacturasVigentes;
           out.mtoTotalFacturas = mtoTotalFacturas;
           out.mtoTotalNC = mtoTotalNC;
           out.mtoNcProporcionalVigente = mtoNcProporcionalVigente;
           
    	   return out;
    }
    

}
