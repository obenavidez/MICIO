package com.panzyma.nm.viewdialog;
 
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
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
@InvokeBridge(bridgeName = "BReciboM")
public class AplicarDescuentoOcasional extends DialogFragment implements Handler.Callback
{
	

	
	private static AplicarDescuentoOcasional ado;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		recibo=getArguments().getParcelable("recibo");
		_clave="";
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
		tbox_discoutnkey =(EditText) view.findViewById(R.id.editkey);
		tbox_collectorpercent =(EditText) view.findViewById(R.id.editpercent);
		tbox_collectorpercent.setText("0.0");
		if(SessionManager.isPhoneConnected())
			tbox_discoutnkey.setVisibility(View.GONE);
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
                	validar();
                }
            });
	    }
	} 
	
	private void validar()
	{
		
	       //Si el porcentaje es < 100, pedir clave
	       if (tbox_collectorpercent.getText().toString().trim().equals("")) {
	    	   AppDialog.showMessage(parent,"","Ingrese porcentaje asumido por colector.",DialogType.DIALOGO_ALERTA);
	           return;
	       }
	       
	       percentcollector = Float.parseFloat(tbox_collectorpercent.getText().toString().trim());
	        
	       //Validar que prc sea entre 0 y 100
	       if (percentcollector < 0) {
	    	   AppDialog.showMessage(parent,"","El porcentaje debe ser mayor o igual a cero.",DialogType.DIALOGO_ALERTA);
	           return;
	       }
	        
	       if (percentcollector > 100) {
	    	   AppDialog.showMessage(parent,"","El porcentaje debe ser menor o igual a 100.",DialogType.DIALOGO_ALERTA);
	           return;
	       }
	        
	       //Si el porcentaje es menor de 100, pedir clave
	       if (percentcollector < 100) 
	       {
	           if (tbox_discoutnkey != null && tbox_discoutnkey.isShown()) 
	           {
	        	   _clave=tbox_discoutnkey.getText().toString().trim();
	                if (_clave.compareTo("") == 0) 
	                {
	                	_clave="";
	                	AppDialog.showMessage(parent,"","Favor ingresar clave de autorizaci�n.",DialogType.DIALOGO_ALERTA);
	                    return;
	                } 
	            } 
	           else 
	           {
		        	Message msg = new Message();
		   			Bundle b = new Bundle();
		   			b.putParcelable("recibo",recibo); 
		   			msg.setData(b);
		   			msg.what=ControllerProtocol.APLICAR_DESCUENTO;
		   			NMApp.getController().getInboxHandler().sendMessage(msg);     		
	           }
	            
	           //Validar que clave sea v�lida
	           //if (verificarClaveDescOca(txtClave.getText().trim()) != 1) return false;
	       }  
	}
	
	
	//1: v�lida, 0: inv�lida, -1: cancelado
    private boolean verificarAutorizacionDesc(String resp) { 
        //0: No se ha solicitado autorizaci�n
        //1: La solicitud a�n no ha sido autorizada
        //2: Error al lado del servidor
        //Valor diferente: Clave de Autorizaci�n        
        try 
        { 
            if (resp.compareTo("0") == 0) {
            	AppDialog.showMessage(parent,"","No se ha solicitado autorizaci�n.",DialogType.DIALOGO_ALERTA);
                return false;
            }
            
            if (resp.compareTo("1") == 0) {
            	AppDialog.showMessage(parent,"","La solicitud a�n no ha sido autorizada.",DialogType.DIALOGO_ALERTA);
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
	public boolean handleMessage(Message msg) { 
		switch (msg.what) 
		{
			case ControllerProtocol.REQUEST_APLICAR_DESCUENTO:	
				if(verificarAutorizacionDesc(msg.obj.toString()))
				{
					mylisterner.onButtonClick(percentcollector,_clave);
					dismiss();
				}
				break;
			case ControllerProtocol.ERROR:
				AppDialog.showMessage(parent,"",msg.toString(),DialogType.DIALOGO_ALERTA);
				break;
			default:
				break;
		}
		return false;
	}
    
	
	
}
