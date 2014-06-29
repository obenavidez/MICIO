package com.panzyma.nm.viewdialog;

import com.panzyma.nm.auxiliar.ActionType;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.Documento;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;

@SuppressLint("ValidFragment")
public class DialogoConfirmacion extends DialogFragment {	
	
	private View view;
	private EditText numero;
	private EditText saldo;
	private EditText monto;
	private EditText interes;
	private EditText retencion;
	private TableRow rowRetencion;
	private Documento document;	
	private Pagable eventPago;
	private ActionType actionType;
		
	public interface Pagable {
		public void onPagarEvent(Float montoAbonado); 
	}

	public DialogoConfirmacion(Documento documento, ActionType actionType) {
		super();
		this.document = documento;
		this.actionType = actionType;
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
				eventPago.onPagarEvent(getMontoAbonado());			
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
		numero = (EditText) view.findViewById(R.id.txtNoFactura);
		saldo = (EditText) view.findViewById(R.id.txtSaldo);
		monto = (EditText) view.findViewById(R.id.txtMonto);
		interes = (EditText) view.findViewById(R.id.txtInteres);
		rowRetencion = (TableRow) view.findViewById(R.id.tableRowLdpr);
		retencion = (EditText) view.findViewById(R.id.txtRetencion);
		
		monto.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				float nSaldo = 0.00f, 
						nTotalDocumento = 0.00f;
				String monto = arg0.toString().trim().equals("") ? "0.00" : arg0.toString();
				if(document instanceof ReciboDetFactura)
					nTotalDocumento = ((ReciboDetFactura)document).getTotalfactura();
				else if (document instanceof ReciboDetND)
					nTotalDocumento = ((ReciboDetND)document).getMontoND();
				nSaldo = nTotalDocumento - Float.parseFloat(String.valueOf(monto)) ;
				saldo.setText(StringUtil.formatReal(nSaldo));				
			}
			
		});
		
		// establecer valores		
		numero.setText(document.getNumero());
		saldo.setText(String.valueOf(document.getSaldo()));
		interes.setText("0.00");
		monto.setText(String.valueOf(document.getMonto()));
		retencion.setText(String.valueOf(document.getRetencion()));
		//SI ESTAMOS ANTE UNA FACTURA Y ESTAMOS EDITANDO EL ITEM
		if ( document instanceof ReciboDetFactura ){
			if( this.actionType == ActionType.ADD )
				rowRetencion.setVisibility(View.INVISIBLE);
			else 
				rowRetencion.setVisibility(View.VISIBLE);
			
		} 
	}
	
	private float getMontoAbonado(){
		return Float.parseFloat(monto.getText().toString());
	}
	
	public void setActionPago(Pagable actionPago) {
		this.eventPago = actionPago;
	}	

}