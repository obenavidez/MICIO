package com.panzyma.nm.viewdialog;

import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("ValidFragment")
public class DialogoConfirmacion extends DialogFragment {	
	
	private View view;
	private EditText numeroFactura;
	private EditText saldo;
	private EditText monto;
	private EditText interes;
	private Factura factura;
	private Pagable eventPago;
	
	public interface Pagable {
		public void onPagarFactura(Float montoAbonado); 
	}

	public DialogoConfirmacion(Factura factura) {
		super();
		this.factura = factura;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.layout_detalle_pago_recibo, null);
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				eventPago.onPagarFactura(getMontoAbonado());			
			}
		});
		builder.setNegativeButton("CANCELAR", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();				
			}
		} );

		initComponents();
		return builder.create();
	}

	private void initComponents() {
		// obtención de las views		
		numeroFactura = (EditText) view.findViewById(R.id.txtNoFactura);
		saldo = (EditText) view.findViewById(R.id.txtSaldo);
		monto = (EditText) view.findViewById(R.id.txtMonto);
		interes = (EditText) view.findViewById(R.id.txtInteres);
		// establecer valores		
		numeroFactura.setText(factura.getNoFactura());
		saldo.setText(String.valueOf(factura.getSaldo()));
		interes.setText("0.00");
		monto.setText(String.valueOf(factura.getSaldo()));
	}
	
	private float getMontoAbonado(){
		return Float.parseFloat(monto.getText().toString());
	}
	
	public void setActionPago(Pagable actionPago) {
		this.eventPago = actionPago;
	}	

}