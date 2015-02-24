package com.panzyma.nm.viewdialog;
     
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import static com.panzyma.nm.controller.ControllerProtocol.SEND_DATA_FROM_SERVER;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;    
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelRecibo;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.view.ViewRecibo;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.DocumentoViewHolder;
import com.panzyma.nm.view.viewholder.SolicitudDescuentoViewHolder;
import com.panzyma.nm.viewdialog.DialogProducto.OnButtonClickListener;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.Dialog; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;  
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@InvokeBridge(bridgeName = "BSolicitudDescuentoM")
public class DialogSolicitudDescuento extends Dialog  implements Handler.Callback{

	ViewReciboEdit parent;
	private ListView lvfacturas;
	private TextView gridheader;
	List<Factura> facturas;
	List<SolicitudDescuento> solicitudes;
	SolicitudDescuento solicitudseleccionada;
	ReciboColector recibo;
	private GenericAdapter adapter;
	
	private int positioncache;
	
	private OnButtonClickListener mButtonClickListener;
	private TextView txtentymsg;
	private Button btnaceptar;
	private Button btncancelar;
	
	public interface OnButtonClickListener {
		public abstract void onButtonClick(String notasolicituddescuento);
	}
	
    public void setOnDialogSDButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
	
	public DialogSolicitudDescuento(ViewReciboEdit _view,
									List<Factura> _facturas,
									ReciboColector _recibo) 
    {    
    	super(_view.getContext(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);          
        try 
        {    	        
        	setContentView(R.layout.solicituddescuento);   
			parent=_view;      	 
	        NMApp.getController().setView(this);	        
	        recibo=_recibo;
	        Message msg = new Message();
			Bundle b = new Bundle();
			b.putLong("idrecibo", recibo.getId()); 
			msg.setData(b);
			msg.what=LOAD_DATA_FROM_LOCALHOST;
			NMApp.getController().getInboxHandler().sendMessage(msg);   
	        facturas=_facturas;
	        initComponents();
	        
        }catch (Exception e) { 
			e.printStackTrace(); 		  
		}	 
    }	
	
	public void initComponents()
	{ 	    
		if(facturas==null)
        	facturas=new ArrayList<Factura>();		
		solicitudes=new ArrayList<SolicitudDescuento>();
		
	    lvfacturas = (ListView) findViewById(R.id.sd_lvfacturas);	
	    gridheader=(TextView) findViewById(R.id.sd_gridheader);  
	    //txtentymsg=(TextView) findViewById(R.id.sd_txtview_enty);  
	    gridheader.setText("FACTURAS A SOLICITAR DESCUENTO("+facturas.size()+")");	    
	    
//	    lvfacturas.setOnItemClickListener(new OnItemClickListener() 
//        {
//            @SuppressLint("NewApi")
//			@Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
//            { 		 
//            	if(positioncache < 0 && adapter != null && adapter.getCount() > 0)
//            		positioncache = 0;
//            	if((parent.getChildAt(positioncache))!=null)						            							            		
//            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
//            	positioncache=position;				            	
//            	solicitudseleccionada=(SolicitudDescuento) adapter.getItem(position);	
//            	adapter.setSelectedPosition(position); 
//            	view.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.action_item_selected));		
//            	adapter.notifyDataSetChanged();
//            }
//        }); 	
//	    
//	    lvfacturas.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				if(positioncache < 0 && adapter != null && adapter.getCount() > 0)
//            		positioncache = 0;
//            	if((parent.getChildAt(positioncache))!=null)						            							            		
//            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
//            	positioncache=position;				            	
//            	solicitudseleccionada=(SolicitudDescuento) adapter.getItem(position);	
//            	adapter.setSelectedPosition(position); 
//            	view.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.action_item_selected));		
//            	adapter.notifyDataSetChanged();
//				return false;
//			}
//		});	
	    
	    btnaceptar = ((Button) findViewById(R.id.btn_ok));
		btnaceptar.setOnClickListener(new android.view.View.OnClickListener() 
		{
			@Override
			public void onClick(View _v) 
			{
				int childCount = lvfacturas.getChildCount();

			    for (int i = 0; i < childCount; i++)
			    {			    	
			        View v =lvfacturas.getChildAt(i);
			        EditText d=((EditText) v.findViewById(R.id.descuento));
			        EditText j=((EditText) v.findViewById(R.id.justificacion));
			        
			        String td=d.getText().toString().trim();
			        String tj=j.getText().toString().trim();
			        
			        if(td.equals("") && tj.equals(""))
			        	continue;
			        	
			        if(td.equals("") && !tj.equals(""))
			        {
			        	d.setError("Debe ingresar el descuento..."); 
			        	break;
			        } 
			        else if(!td.equals("") && tj.equals(""))
			        {
			        	j.setError("Debe justificar el descuento..."); 
			        	break;
			        } 
//			        else
//			        {
//			        	SolicitudDescuento sd=(SolicitudDescuento) lvfacturas.getItemAtPosition(i);
//			        	sd.setPorcentaje(Float.valueOf(td));
//			        	sd.setJustificacion(tj);
//      		        	solicitudes.add(i, sd);
//			        }
			    }
			 
			}

		});
		btncancelar = ((Button) findViewById(R.id.btn_cancel));
		btncancelar.setOnClickListener(new android.view.View.OnClickListener() 
		{
			@Override
			public void onClick(View v) {

				dismiss();
			}

		});
	    
	}
	
	private void enviarSolicitud()
	{
		Message msg = new Message(); 
		msg.obj=solicitudes;
		msg.what=ControllerProtocol.SEND_DATA_FROM_SERVER;
		NMApp.getController().getInboxHandler().sendMessage(msg); 
	}

	@Override
	public boolean handleMessage(Message msg) {
		 
		switch (msg.what) 
		{
			case C_DATA: 
				establecerDatos((msg.obj!=null)? (List<SolicitudDescuento>) msg.obj:new ArrayList<SolicitudDescuento>());
				break ;
			case SEND_DATA_FROM_SERVER:
				break;
		}
		return false;
	} 
	
	private void establecerDatos(List<SolicitudDescuento> _solicitudes)
	{
		if(facturas==null || (facturas!=null && facturas.size()==0))
			return ;
		boolean add=false;
		for(Factura f:facturas)
		{
			add=false;
			if(_solicitudes!=null && _solicitudes.size()!=0)
			{
				for(SolicitudDescuento s:_solicitudes)
				{
					if(f.getId()==s.getFacturaId())
					{
						solicitudes.add(s);
						add=true;
					}					
				}
			}
				
			if(!add)
			{
				solicitudes.add(new SolicitudDescuento(0,recibo.getId(),f.getId(),0.0f,"",DateUtil.getFecha(),f));
			}
			
		}		 
	    gridheader.setText("FACTURAS A SOLICITAR DESCUENTO("+facturas.size()+")");		
	    if(adapter==null){
	    	adapter = new GenericAdapter(parent, SolicitudDescuentoViewHolder.class,solicitudes,  R.layout.list_row2);
			lvfacturas.setAdapter(adapter);
	    }
	    else
	    	adapter.notifyDataSetChanged();
	    	
		//adapter.setSelectedPosition(0);
		
		if (solicitudes.size() ==0) 
			txtentymsg.setVisibility(View.VISIBLE);
		gridheader.setText("DOCUMENTOS A PAGAR(" + adapter.getCount() + ")");
	}
}
