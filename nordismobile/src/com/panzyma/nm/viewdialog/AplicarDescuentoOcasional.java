package com.panzyma.nm.viewdialog;
 
import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.Cobro;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.ViewReciboEdit;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; 

public class AplicarDescuentoOcasional extends DialogFragment
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

	private ViewReciboEdit parent;
	private View view;
	private EditText tbox_discoutnkey;
	private ReciboColector recibo;
	private EditText tbox_collectorpercent;
	private String _clave;
	private NMApp nmapp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		recibo=getArguments().getParcelable("recibo");
	}
	
	@SuppressLint("InflateParams") 
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	parent=(ViewReciboEdit) getActivity();
    	AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
    	LayoutInflater inflater = parent.getLayoutInflater();
		view = inflater.inflate(R.layout.oca_discount_dialog, null);
		tbox_discoutnkey =(EditText) view.findViewById(R.id.editkey);
		tbox_collectorpercent =(EditText) view.findViewById(R.id.editpercent);
		if(!SessionManager.isPhoneConnected())
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
				dialog.cancel();
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
	        Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
	        positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {	 
                }
            });
	    }
	}
	
	private void validar()
	{
		
	       //Si el porcentaje es < 100, pedir clave
	       if (tbox_collectorpercent.getText().toString().trim() == "") {
	          // Dialog.alert("Ingrese porcentaje asumido por colector.");
	           return;
	       }
	       
	       float prc = Float.parseFloat(tbox_collectorpercent.getText().toString().trim());
	       //Validar que prc sea entre 0 y 100
	       if (prc < 0) {
	          // Dialog.alert("El porcentaje debe ser mayor o igual a cero.");
	           return;
	       }
	        
	       if (prc > 100) {
	           //Dialog.alert("El porcentaje debe ser menor o igual a 100.");
	           return;
	       }
	        
	       //Si el porcentaje es menor de 100, pedir clave
	       if (prc < 100) 
	       {
	           if (tbox_discoutnkey != null && tbox_discoutnkey.isShown()) 
	           {
	        	   _clave=tbox_discoutnkey.getText().toString().trim();
	                if (_clave.compareTo("") == 0) 
	                {
	                	_clave="";
	                   // Dialog.alert("Favor ingresar clave de autorización.");
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
		   			nmapp.getController().getInboxHandler().sendMessage(msg);     		
	           }
	            
	           //Validar que clave sea válida
	           //if (verificarClaveDescOca(txtClave.getText().trim()) != 1) return false;
	       }  
	}
	
	
	//1: válida, 0: inválida, -1: cancelado
    private boolean verificarAutorizacionDesc(String resp) {
        //No hay forma de verificar si se está fuera de cobertura
        if (!SessionManager.isPhoneConnected()) return true; 
        
        //0: No se ha solicitado autorización
        //1: La solicitud aún no ha sido autorizada
        //2: Error al lado del servidor
        //Valor diferente: Clave de Autorización        
        try 
        { 
            if (resp.compareTo("0") == 0) {
                //Dialog.alert("No se ha solicitado autorización.");
                return false;
            }
            
            if (resp.compareTo("1") == 0) {
                //Dialog.alert("La solicitud aún no ha sido autorizada.");
                return false;
            }
            
            if (resp.compareTo("2") == 0) {
                //Dialog.alert("Error de procesamiento en el servidor.");
                return false;
            }
            
            if (resp.compareTo("3") == 0) {
                //Dialog.alert("La solicitud ha sido denegada.");
                return false;
            }
            
            _clave = resp;
            return true;    
        }
        catch(Exception ex) {
            //Dialog.alert("Error: " + ex.toString());            
        }
        return  false;
    } //verificarClaveDescOca
    
	
	
}
