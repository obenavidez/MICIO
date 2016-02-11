package com.panzyma.nm.viewdialog;

import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.view.ViewDevolucionEdit; 
import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment; 
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button; 
import android.widget.TextView;

public class DialogCosteoDevolucion extends DialogFragment {

	public static final String FRAGMENT_TAG = "DialogCosteoDevolucion";

	public static DialogCosteoDevolucion nr = new DialogCosteoDevolucion(); 
	private ViewDevolucionEdit parent;  
	private TextView txtSistema_SubTotal;
	private TextView txtSistema_Bonificacion;
	private TextView txtSistema_Impuesto;
	private TextView txtSistema_Promocion;
	private TextView txtSistema_CargoAdm;
	private TextView txtSistema_Total;
	private TextView txtVendedor_SubTotal;
	private TextView txtVendedor_Bonificacion;
	private TextView txtVendedor_Impuesto;
	private TextView txtVendedor_Promocion;
	private TextView txtVendedor_CargoAdm;
	private TextView txtVendedor_Total;
	private TextView txtVendedor_vinetas;
	private TextView txtCargo_al_vendedor;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		LayoutInflater inflater = parent.getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(parent); 
		
		View view = inflater.inflate(R.layout.costeodevolucion, null); 
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
                  
            }
		});
		
		Dialog dialog = builder.create(); 
		initComponent(view);
        return dialog;		
	}
	
	public void initComponent(View view) {
		this.parent.CalMontoCargoVendedor();
		this.parent.CalMontoPromocion();
		this.parent.CalTotalDevolucion();
		txtSistema_SubTotal = (TextView) view.findViewById(R.id.cxctextv_subtsys);
		txtSistema_Bonificacion = (TextView) view.findViewById(R.id.cxctextv_bonifsys);
		txtSistema_Impuesto = (TextView) view.findViewById(R.id.cxctextv_impsys);
		txtSistema_Promocion = (TextView) view.findViewById(R.id.cxctext_promsys);
		txtSistema_CargoAdm = (TextView) view.findViewById(R.id.cxctextv_cargadmsys);
		txtSistema_Total = (TextView) view.findViewById(R.id.cxctext_totalsys);
		txtVendedor_SubTotal = (TextView) view.findViewById(R.id.cxctextv_subtvend);
		txtVendedor_Bonificacion = (TextView) view.findViewById(R.id.cxctextv_bonifvend);
		txtVendedor_Impuesto = (TextView) view.findViewById(R.id.cxctextv_impvend);
		txtVendedor_Promocion = (TextView) view.findViewById(R.id.cxctext_promvend);
		txtVendedor_CargoAdm = (TextView) view.findViewById(R.id.cxctextv_cargadmvend);
		txtVendedor_Total = (TextView) view.findViewById(R.id.cxctext_totalvend);
		txtVendedor_vinetas = (TextView) view.findViewById(R.id.cxctext_vinievend);
		txtCargo_al_vendedor = (TextView) view.findViewById(R.id.cxctext_cargvend);
		///////
		txtSistema_SubTotal.setText(StringUtil.formatReal(this.parent.getCosteoMontoSubTotal().doubleValue()/100.00));
		txtVendedor_SubTotal.setText(StringUtil.formatReal(this.parent.getCosteoMontoSubTotal().doubleValue()/100.00));
		
		txtSistema_Bonificacion.setText(StringUtil.formatReal(this.parent.getCosteoMontoBonificacion().doubleValue()/100.00));
		txtVendedor_Bonificacion.setText(StringUtil.formatReal(this.parent.getCosteoMontoBonificacionVen().doubleValue()/100.00));
		
		txtSistema_Impuesto.setText(StringUtil.formatReal(this.parent.getCosteoMontoImpuesto().doubleValue()/100.00));
		txtVendedor_Impuesto.setText(StringUtil.formatReal(this.parent.getCosteoMontoImpuestoVen().doubleValue()/100.00));
		
		txtSistema_Promocion.setText(StringUtil.formatReal(this.parent.getCosteoMontoPromocion().doubleValue()/100.00));
		txtVendedor_Promocion.setText(StringUtil.formatReal(this.parent.getCosteoMontoPromocionVen().doubleValue()/100.00));
		
		txtSistema_CargoAdm.setText(StringUtil.formatReal(this.parent.getCosteoMontoCargoAdministrativo().doubleValue()/100.00));
		txtVendedor_CargoAdm.setText(StringUtil.formatReal(this.parent.getCosteoMontoCargoAdministrativoVen().doubleValue()/100.00));
		
		txtSistema_Total.setText(StringUtil.formatReal(this.parent.getCosteoMontoTotal().doubleValue()/100.00));
		txtVendedor_Total.setText(StringUtil.formatReal(this.parent.getCosteoMontoTotalVen().doubleValue()/100.00));
		
		txtVendedor_vinetas.setText(StringUtil.formatReal(this.parent.getCosteoMontoVinieta().doubleValue()/100.00));
		txtCargo_al_vendedor.setText(StringUtil.formatReal(this.parent.getCosteoMontoCargoVen().doubleValue()/100.00));		
	}
	
	public static DialogCosteoDevolucion newInstance(
			ViewDevolucionEdit _parent) {
		if (nr == null)
			nr = new DialogCosteoDevolucion(); 
		nr.setParent(_parent);
		return nr;
	}
	
	public void setParent(ViewDevolucionEdit _parent) {
		this.parent = _parent;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) 
   	{   		
		super.onDismiss(dialog);
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
                		dismiss();   			
                }
            });    
	        
	    }
	}
}
