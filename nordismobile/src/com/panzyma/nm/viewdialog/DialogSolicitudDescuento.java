package com.panzyma.nm.viewdialog;
     
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;    
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.DocumentoViewHolder;
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
	List<SolicitudDescuento> solicitudes;
	ReciboColector recibo;
	private GenericAdapter adapter;
	
	public DialogSolicitudDescuento(ViewRecibo _view,
									ArrayList<ReciboDetFactura> _facturas,
									ReciboColector _recibo) 
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
	        recibo=_recibo;
	        
        }catch (Exception e) { 
			e.printStackTrace(); 		  
		}	 
    }	
	
	public void initComponents()
	{ 	    
		if(facturas==null)
        	facturas=new ArrayList<ReciboDetFactura>();
		solicitudes=new ArrayList<SolicitudDescuento>();
	    lvfacturas = (ListView) findViewById(R.id.p_lvproducto);	
	    gridheader=(TextView) findViewById(R.id.p_textv_gridheader);  
	    gridheader.setText("FACTURAS A SOLICITAR DESCUENTO("+facturas.size()+")");
	    
	}
	

	@Override
	public boolean handleMessage(Message msg) {
		 
		switch (msg.what) 
		{
			case C_DATA: 
				establecerDatos( (ArrayList<SolicitudDescuento>) msg.obj);
				break ;
		}
		return false;
	} 
	
	private void establecerDatos(ArrayList<SolicitudDescuento> _solicitudes)
	{
		if(facturas==null || (facturas!=null && facturas.size()!=0))
			return ;
		boolean add=false;
		for(ReciboDetFactura f:facturas)
		{
			add=false;
			for(SolicitudDescuento s:_solicitudes)
			{
				if(f.getId()==s.getFacturaId())
				{
					solicitudes.add(s);
					add=true;
				}
				
			}
			if(!add)
			{
				solicitudes.add(new SolicitudDescuento(0,recibo.getId(),f.getId(),0.0f,"",DateUtil.getFecha()));
			}
			
		}
		
		solicitudes=new ArrayList<SolicitudDescuento>();
	    lvfacturas = (ListView) findViewById(R.id.p_lvproducto);	
	    gridheader=(TextView) findViewById(R.id.p_textv_gridheader);  
	    gridheader.setText("FACTURAS A SOLICITAR DESCUENTO("+facturas.size()+")");
		
		//adapter = new GenericAdapter(this, DocumentoViewHolder.class,solicitudes,  R.layout.list_row);
		lvfacturas.setAdapter(adapter);
		adapter.setSelectedPosition(0);
		
		if (solicitudes.size() > 0)
		//	documento_selected = documents.get(0);
		gridheader.setText("DOCUMENTOS A PAGAR(" + adapter.getCount() + ")");
	}
}
