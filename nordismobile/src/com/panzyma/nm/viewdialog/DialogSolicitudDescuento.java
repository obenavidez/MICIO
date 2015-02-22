package com.panzyma.nm.viewdialog;
     
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import java.util.ArrayList;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.NMConfig.SolicitudDescuento;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R;

import android.app.Dialog; 
import android.os.Handler;
import android.os.Message;  
import android.widget.ListView;
import android.widget.TextView;

@InvokeBridge(bridgeName = "BSolicitudDescuentoM")
public class DialogSolicitudDescuento extends Dialog  implements Handler.Callback{

	ViewRecibo parent;
	private ListView lvfacturas;
	private TextView gridheader;
	ArrayList<ReciboDetFactura> facturas;
	public DialogSolicitudDescuento(ViewRecibo _view,
									ArrayList<ReciboDetFactura> _facturas,
									ReciboColector recibo) 
    {    
    	super(_view,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);          
        try 
        {    	        
        	setContentView(R.layout.solicituddescuento);   
			parent=_view;      	 
	        NMApp.getController().setView(this);
	        NMApp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA_FROM_LOCALHOST);
	        if(_facturas!=null)
	        	facturas=_facturas;
	        
        }catch (Exception e) { 
			e.printStackTrace(); 		  
		}	 
    }	
	
	public void initComponents()
	{ 	    
		if(facturas==null)
        	facturas=new ArrayList<ReciboDetFactura>();
	    lvfacturas = (ListView) findViewById(R.id.p_lvproducto);	
	    gridheader=(TextView) findViewById(R.id.p_textv_gridheader);  
	    gridheader.setText("FACTURAS A SOLICITAR DESCUENTO("+facturas.size()+")");
	    
	}
	

	@Override
	public boolean handleMessage(Message msg) {
		 
		switch (msg.what) 
		{
			case C_DATA: 
				break;
		}
		return false;
	} 
	
	private void establecerDatos(ArrayList<SolicitudDescuento> solicitudes)
	{
		for(SolicitudDescuento s:solicitudes)
		{
			//if()
		}
	}
}
