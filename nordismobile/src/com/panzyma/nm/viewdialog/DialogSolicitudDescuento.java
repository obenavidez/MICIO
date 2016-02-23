package com.panzyma.nm.viewdialog;
     
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;
import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;    
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.DateUtil;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.DescuentoProveedor;
import com.panzyma.nm.serviceproxy.EncabezadoSolicitud;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.SolicitudDescuentoViewHolder;
import com.panzyma.nordismobile.R;

import android.app.Dialog; 
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;  
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@InvokeBridge(bridgeName = "BSolicitudDescuentoM")
public class DialogSolicitudDescuento extends Dialog  implements Handler.Callback{

	ViewReciboEdit parent;
	ProgressDialog pDialog;
	List<Factura> facturas;
	EncabezadoSolicitud solicitud;
	List<SolicitudDescuento> detallesolicitud; 
	ReciboColector recibo;
	DescuentoProveedor[] ldp;
	DescuentoProveedor dp;
	private GenericAdapter adapter;
	
	private int positioncache;
	
	private OnButtonClickListener mButtonClickListener;
	
	private ListView lvfacturas;
	private CheckBox cboxall;
	private TextView gridheader;
	private Button btnaceptar;
	private Button btncancelar;
	
	public static String DOC_STATUS_REGISTRADO="REGISTRADO";
	public static String DOC_STATUS_ENVIADO="ENVIADO";	
	public static String DOC_STATUS_APROBADO="APROBADO";
	public static String DOC_STATUS_ANULADO="ANULADO";
	
	public static String DESC_DOC_STATUS_REGISTRADO="Registrada";
	public static String DESC_DOC_STATUS_ENVIADO="Enviada";	
	public static String DESC_DOC_STATUS_APROBADO="Aprobada";
	public static String DESC_DOC_STATUS_ANULADO="Anulada";
	
	public static String TAG=DialogSolicitudDescuento.class.getSimpleName();
	
	public static String justificacionglobal="";
	public static float porcentajeglobal=0.0f;
	
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
	        ldp=_recibo.getCliente().getDescuentosProveedor();
	        initComponents();
	        
	        
        }catch (Exception e) { 
			e.printStackTrace(); 		  
		}	 
    }	
	
	public void initComponents()
	{ 	    
		if(facturas==null)
        	facturas=new ArrayList<Factura>();		
		detallesolicitud=new ArrayList<SolicitudDescuento>();
		solicitud=new EncabezadoSolicitud();
		
		cboxall=(CheckBox) findViewById(R.id.cbox_all);   
	    lvfacturas = (ListView) findViewById(R.id.sd_lvfacturas);	
	    gridheader=(TextView) findViewById(R.id.sd_gridheader);   
	    gridheader.setText("FACTURAS A SOLICITAR DESCUENTO("+facturas.size()+")");	     
	    
	    if(facturas!=null && facturas.size()>1)
	    {
	    	cboxall.setVisibility(View.VISIBLE);
	    	cboxall.setOnClickListener(new View.OnClickListener() 
	    	{
				
				@Override
				public void onClick(View v) 
				{ 
					if (!((CheckBox) v).isChecked())
					{ 
					    porcentajeglobal=0.0f;
					    justificacionglobal="";
						return;
					}
						
					int childCount = lvfacturas.getChildCount(); 
					boolean OK=false;
				    for (int i = 0; i < childCount; i++)
				    {			    	
				        View doc =lvfacturas.getChildAt(i);
				        SolicitudDescuento sd=(SolicitudDescuento) lvfacturas.getItemAtPosition(i);
				        EditText d=((EditText) doc.findViewById(R.id.descuento));
				        EditText j=((EditText) doc.findViewById(R.id.justificacion));
				        
				        String td=d.getText().toString().trim();
				        String tj=j.getText().toString().trim();
				        float pd=Float.parseFloat((td.equals(""))?"0.0F":td);
				        
				        if(td.equals("") && tj.equals(""))
				        	continue;	        	
				        if(td.equals("") && !tj.equals(""))
				        	continue;	
				        else if(!td.equals("") && tj.equals("")) 
				        	continue;
				        else if (pd>dp.getPrcDescuento())
				        	{
					        	d.setError("El %descuento no debe ser mayor %"+ dp.getPrcDescuento()+" ..."); 
					        	d.requestFocus(); 
					        	((CheckBox) v).setChecked(false);
					        	return;
				        	}			        	
				        else if (pd<0.0F) 
				        {
				        	d.setError("El %descuento debe ser mayor que 0 ..."); 
				        	d.requestFocus();
				        	((CheckBox) v).setChecked(false);
				        	return;
				        } 
				       OK= true;
				       porcentajeglobal=pd;
				       justificacionglobal=tj;
				       break;
				    } 
					if(!OK){
						((CheckBox) v).setChecked(false);
						porcentajeglobal=0.0f;
					    justificacionglobal="";
						AppDialog.showMessage(parent,"Aviso!!!",
								"Debe de especificar el % y justificación en cualquiera de item de la lista y este se le aplicara a todos",
								DialogType.DIALOGO_ALERTA);
						}
					}
			});
	    }    
	    
	    btnaceptar = ((Button) findViewById(R.id.btn_ok));
		btnaceptar.setOnClickListener(new android.view.View.OnClickListener() 
		{
			@Override
			public void onClick(View _v) 
			{
				int childCount = lvfacturas.getChildCount(); 
				int error=0;
				
				if(cboxall.isChecked() && porcentajeglobal>0.0f && (!"".equals(justificacionglobal)))
					enviarSolicitud(true);		        
				else
				{
					cboxall.setChecked(false);
					for (int i = 0; i < childCount; i++)
				    {			    	
				        View v =lvfacturas.getChildAt(i);
				        SolicitudDescuento sd=(SolicitudDescuento) lvfacturas.getItemAtPosition(i);
				        EditText d=((EditText) v.findViewById(R.id.descuento));
				        EditText j=((EditText) v.findViewById(R.id.justificacion));
				        
				        String td=d.getText().toString().trim();
				        String tj=j.getText().toString().trim();
				        float pd=Float.parseFloat((td.equals(""))?"0.0F":td);
				        
				        if(td.equals("") && tj.equals(""))
				        	continue;	        	
				        if(td.equals("") && !tj.equals(""))
				        {
				        	d.setError("Debe ingresar el descuento..."); 
				        	d.requestFocus();			 
				        	error=1;
				        	return;
				        } 
				        else if(!td.equals("") && tj.equals(""))
				        {
				        	j.setError("Debe justificar el descuento..."); 
				        	j.requestFocus(); 		
				        	
				        	error=1;
				        	return;
				        }else if (pd>dp.getPrcDescuento())
				        {
				        	d.setError("El %descuento no debe ser mayor %"+ dp.getPrcDescuento()+" ..."); 
				        	d.requestFocus(); 
				        	error=1;
				        	return;
				        }else if (pd<0.0F)
				        {
				        	d.setError("El %descuento debe ser mayor que 0 ..."); 
				        	d.requestFocus(); 
				        	error=1;
				        	return;
				        } 
				        solicitud.getDetalles().get(i).setPorcentaje(Float.parseFloat(td));
				        solicitud.getDetalles().get(i).setJustificacion(tj);
				    } 
					enviarSolicitud();
					
				}
			    
			}

		});
		btnaceptar.setVisibility(View.VISIBLE);
		btncancelar = ((Button) findViewById(R.id.btn_cancel));
		btncancelar.setOnClickListener(new android.view.View.OnClickListener() 
		{
			@Override
			public void onClick(View v) {

				dismiss();
			}

		});   
		  
		float largest=Float.MIN_VALUE;
        for(int i =0;i<ldp.length;i++) 
        {        	
            if(ldp[i].getPrcDescuento() > largest) 
            {   
            	dp=new DescuentoProveedor();
            	dp = ldp[i]; 
            	largest=dp.getPrcDescuento();
            }
        } 
	}
	
	private void enviarSolicitud(boolean...otherprocess)
	{
		if(!SessionManager.isPhoneConnected3())
		{
			AppDialog.showMessage(parent,"Error de conexión", " Dispositivo Fuera de linea", DialogType.DIALOGO_ALERTA);
			return;
		} 
		if(!DOC_STATUS_REGISTRADO.equals(solicitud.getCodigoEstado()))
			return;
		ArrayList<Object> data=null;
		if(otherprocess!=null && otherprocess.length!=0 && otherprocess[0])
		{
			data=new ArrayList<Object>();
			data.add(porcentajeglobal);
			data.add(justificacionglobal);
			data.add(solicitud);
		}		
		Message msg = new Message(); 
		msg.obj=(data!=null)?data:solicitud;
		msg.what=ControllerProtocol.SOLICITAR_DESCUENTO;
		NMApp.getController().getInboxHandler().sendMessage(msg); 
		showProgress();
	}

	@Override
	public boolean handleMessage(Message msg) 
	{		 
		switch (msg.what) 
		{
			case C_DATA: 
				establecerDatos((msg.obj!=null)? (EncabezadoSolicitud) msg.obj:null);
				break; 
			case ControllerProtocol.REQUEST_SOLICITUD_DESCUENTO:
				pDialog.hide();
				mButtonClickListener.onButtonClick((String) msg.obj);
				dismiss();
				break;
			case ControllerProtocol.C_QUIT :
				pDialog.hide();
				dismiss();
				break;
			case ControllerProtocol.ERROR:
				AppDialog.showMessage(parent, ((ErrorMessage) msg.obj).getTittle(),
						((ErrorMessage) msg.obj).getMessage(),
						DialogType.DIALOGO_ALERTA);
		}
		return false;
	} 
	
	private void establecerDatos(EncabezadoSolicitud _solicitud)
	{		
		List<SolicitudDescuento> _solicitudes=((_solicitud!=null)?_solicitud.getDetalles():null);
		
		if(facturas==null || (facturas!=null && facturas.size()==0))
			return ;
		boolean add=false;
		solicitud=_solicitud; 
		if(solicitud==null) 
			solicitud=new EncabezadoSolicitud(0,recibo.getId(),DOC_STATUS_REGISTRADO,DESC_DOC_STATUS_REGISTRADO,DateUtil.getFecha());			
			 
		for(Factura f:facturas)
		{
			add=false;	
			if(solicitud!=null)
			{
				if(_solicitudes!=null && _solicitudes.size()!=0)
				{
					for(SolicitudDescuento s:_solicitudes)
					{
						if(f.getId()==s.getFacturaId())
						{ 
							s.setStatus(solicitud.getCodigoEstado());
							s.setFactura(f);
							detallesolicitud.add(s);							
							add=true;
							break;
						}					
					}
				}					
				if(!add && DOC_STATUS_REGISTRADO.equals(solicitud.getCodigoEstado())) 
					detallesolicitud.add(new SolicitudDescuento(0,solicitud.getId(),recibo.getId(),f.getId(),0.0f,"",DateUtil.getFecha(),f,solicitud.getCodigoEstado())); 
			}else		
				detallesolicitud.add(new SolicitudDescuento(0,solicitud.getId(),recibo.getId(),f.getId(),0.0f,"",DateUtil.getFecha(),f,solicitud.getCodigoEstado()));
			
		}
		if(solicitud!=null)
			solicitud.setDetalles(detallesolicitud);
	    gridheader.setText("FACTURAS A SOLICITAR DESCUENTO("+facturas.size()+")");		
	    if(adapter==null){
	    	adapter = new GenericAdapter(parent, SolicitudDescuentoViewHolder.class,detallesolicitud,  R.layout.list_row2);
			lvfacturas.setAdapter(adapter);
	    }
	    else
	    	adapter.notifyDataSetChanged(); 
	    
	    if(!DOC_STATUS_REGISTRADO.equals(solicitud.getCodigoEstado()))
	    	btnaceptar.setVisibility(View.GONE);
	    solicitud.setRecibo(recibo);
		gridheader.setText("DOCUMENTOS A PAGAR(" + adapter.getCount() + ")");
	}
	
	@Override
	public void dismiss() {
		FINISH_ACTIVITY();
		super.dismiss();
	}

	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {        	
		  	dismiss();
	        return true;
	    }
	    return super.onKeyUp(keyCode, event); 
	} 
	
	private void FINISH_ACTIVITY()
	{
		NMApp.getController().setView(parent);
		Log.d(TAG, "Activity quitting");  
	}
	
	private void showProgress()
	{
		pDialog = new ProgressDialog(parent);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Procesando...");
		pDialog.setCancelable(false);
		pDialog.show();
	}
}
