package com.panzyma.nm.viewdialog;
 
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.EncabezadoSolicitud;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; 
import android.widget.TextView;
@InvokeBridge(bridgeName = "BReciboM")
public class AplicarDescuentoOcasional extends DialogFragment implements Handler.Callback
{
	private static AplicarDescuentoOcasional ado;
	private EncabezadoSolicitud solicitud;
	Message msg;
	public static AplicarDescuentoOcasional newInstance(ReciboColector recibo) 
	{
		if(ado==null)
			ado = new AplicarDescuentoOcasional(); 
	    Bundle args = new Bundle();
	    args.putParcelable("recibo", recibo); 
	    ado.setArguments(args);
	    return ado;
	}
	RespuestaAlAplicarDescOca mylisterner;
	public interface RespuestaAlAplicarDescOca {
		public abstract void onButtonClick(Float percentcollector,String clave);
	}
	public void escucharRespuestaAplicarDescOca(RespuestaAlAplicarDescOca listener){
		this.mylisterner=listener;
	}
	private ViewReciboEdit parent;
	private View view;
	private EditText tbox_discoutnkey;
	private ReciboColector recibo;
	private EditText tbox_collectorpercent;
	private String _clave;
	private Float percentcollector=0.0f;
	private NMApp nmapp;
	private TextView tview_discoutnkey;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);   
		NMApp.getController().setView(this);
		recibo=getArguments().getParcelable("recibo");
		_clave=""; 
        msg = new Message(); 
		msg.what=ControllerProtocol.OBTENERDESCUENTO;
		msg.obj=recibo.getId();
		NMApp.getController().getInboxHandler().sendMessage(msg); 
	}
	
	@SuppressLint("InflateParams") 
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
		parent=(ViewReciboEdit) getActivity();
		NMApp.getController().setView(this);
    	AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
    	LayoutInflater inflater = parent.getLayoutInflater();
		view = inflater.inflate(R.layout.oca_discount_dialog, null); 
		tbox_collectorpercent =(EditText) view.findViewById(R.id.editpercent);
		tbox_collectorpercent.setText("0");
		
		if(!SessionManager.isPhoneConnected())
		{
			tbox_discoutnkey =(EditText) view.findViewById(R.id.editkey);
			tbox_discoutnkey.setVisibility(View.VISIBLE);
			tview_discoutnkey =(TextView) view.findViewById(R.id.txtkey);
			tview_discoutnkey.setVisibility(View.VISIBLE);
		} 
		builder.setTitle("Aplicar Descuento Ocasional");
		builder.setView(view);
		builder.setPositiveButton("AGREGAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
			}
		});
		builder.setNegativeButton("CANCELAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});
        return builder.create();   	
    } 
	
	
	@Override
	public void onStart()
	{
	    super.onStart();    
	    AlertDialog d = (AlertDialog)getDialog();
	    if(d != null)
	    {
	        Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
	        positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {                	
                	if ( isValid() ) {
                		mylisterner.onButtonClick(percentcollector, tbox_discoutnkey.getText().toString().trim());
                		dismiss();
                	}
                }
            });
	    }
	} 
	
	private boolean isValid()
	{
		
	       //Si el porcentaje es < 100, pedir clave
	       if (tbox_collectorpercent.getText().toString().trim().equals("")) {
	    	   tbox_collectorpercent.setError("Ingrese porcentaje asumido por colector."); 
	    	   tbox_collectorpercent.requestFocus();
	           return false;
	       }
	       
	       percentcollector = Float.parseFloat(tbox_collectorpercent.getText().toString().trim());
	        
	       //Validar que prc sea entre 0 y 100
	       if (percentcollector < 0) {
	    	   tbox_collectorpercent.setError("El porcentaje debe ser mayor o igual a cero."); 
	    	   tbox_collectorpercent.requestFocus();
	    	//   AppDialog.showMessage(parent,"","El porcentaje debe ser mayor o igual a cero.",DialogType.DIALOGO_ALERTA);
	           return false;
	       }
	        
	       if (percentcollector > 100) {
	    	   tbox_collectorpercent.setError("El porcentaje debe ser menor o igual a 100."); 
	    	   tbox_collectorpercent.requestFocus();
	    	 //  AppDialog.showMessage(parent,"","El porcentaje debe ser menor o igual a 100.",DialogType.DIALOGO_ALERTA);
	           return false;
	       }
	        
	       //Si el porcentaje es menor de 100, pedir clave
	       if (percentcollector < 100) 
	       {
	    	   if(tbox_discoutnkey!=null)
	    	   {
	    		   _clave=tbox_discoutnkey.getText().toString().trim();
	                if (_clave.compareTo("") == 0) 
	                {
	                	_clave="";
	                	tbox_discoutnkey.setError("Favor ingresar clave de autorización.");
	                	tbox_discoutnkey.requestFocus();
	                	//AppDialog.showMessage(parent,"","Favor ingresar clave de autorización.",DialogType.DIALOGO_ALERTA);
	                    return false;
	                }   
	    		   
	    	   } else 
		           {
	    		   		if(!SessionManager.isPhoneConnected())
	    		   			return false;
			        	msg = new Message();
			   			Bundle b = new Bundle();
			   			b.putParcelable("recibo",recibo); 
			   			msg.setData(b);
			   			msg.what=ControllerProtocol.APLICAR_DESCUENTO;
			   			NMApp.getController().getInboxHandler().sendMessage(msg);  
			   			return false;
		           }    	  
	       }
	       return true;
	}
	
	
	//1: válida, 0: inválida, -1: cancelado
    private boolean verificarAutorizacionDesc(String resp) 
    { 
        //0: No se ha solicitado autorización
        //1: La solicitud aún no ha sido autorizada
        //2: Error al lado del servidor
        //Valor diferente: Clave de Autorización        
        try 
        { 
            if (resp.compareTo("0") == 0) {
            	AppDialog.showMessage(parent,"","No se ha solicitado autorización.",DialogType.DIALOGO_ALERTA);
                return false;
            }
            
            if (resp.compareTo("1") == 0) {
            	AppDialog.showMessage(parent,"","La solicitud aún no ha sido autorizada.",DialogType.DIALOGO_ALERTA);
                return false;
            }
            
            if (resp.compareTo("2") == 0) {
            	AppDialog.showMessage(parent,"","Error de procesamiento en el servidor.",DialogType.DIALOGO_ALERTA);
                return false;
            }
            
            if (resp.compareTo("3") == 0) {
            	AppDialog.showMessage(parent,"","La solicitud ha sido denegada.",DialogType.DIALOGO_ALERTA);
                return false;
            }
            if(solicitud!=null)
            {
            	solicitud.setCodigoEstado(DialogSolicitudDescuento.DOC_STATUS_APROBADO);
            	solicitud.setDescripcionEstado(DialogSolicitudDescuento.DESC_DOC_STATUS_APROBADO);
            	_clave = resp;
            	msg = new Message();
	   			msg.what=ControllerProtocol.ACTUALIZARDESCUENTO;
	   		    msg.obj=solicitud;
	   			NMApp.getController().getInboxHandler().sendMessage(msg);    
            	return false;
            }
            _clave = resp;
            return true;    
        }
        catch(Exception ex) {
        	AppDialog.showMessage(parent,"","Error: " + ex.toString(),DialogType.DIALOGO_ALERTA);     
        }
        return  false;
    } //verificarClaveDescOca

    
    @Override
	public void onDismiss(DialogInterface dialog) { 
    	NMApp.getController().setView(parent);
		super.onDismiss(dialog);
	}
    
	@Override
	public boolean handleMessage(Message msg) 
	{ 
		switch (msg.what) 
		{
			case ControllerProtocol.REQUEST_APLICAR_DESCUENTO:	
					verificarAutorizacionDesc(msg.obj.toString());				
				break;
			case ControllerProtocol.OBTENERDESCUENTO:
				if(msg.obj!=null)
					solicitud=(EncabezadoSolicitud) (msg.obj);
				break;
			case ControllerProtocol.REQUEST_ACTUALIZARDESCUENTO:
				if(msg.obj!=null && msg.obj instanceof EncabezadoSolicitud)
				{
					mylisterner.onButtonClick(percentcollector,_clave);
					dismiss();
				}
				break;
			case ControllerProtocol.ERROR:
				AppDialog.showMessage(parent,((ErrorMessage)msg.obj).getTittle(),((ErrorMessage)msg.obj).getMessage()+"\n"+((ErrorMessage)msg.obj).getCause(),DialogType.DIALOGO_ALERTA);
				break;
			default:
				break;
		}
		return false;
	}
    
	
	
}
