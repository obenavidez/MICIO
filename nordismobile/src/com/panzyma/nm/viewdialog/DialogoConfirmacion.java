package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BLogicM;
import com.panzyma.nm.CBridgeM.BLogicM.Result;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.auxiliar.ActionType;
import com.panzyma.nm.auxiliar.Ammount;
import com.panzyma.nm.auxiliar.AmmountType;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.controller.ControllerProtocol;
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
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
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
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogoConfirmacion extends DialogFragment implements Callback {	
	
	private View view;
	private EditText numero;
	private EditText saldo;
	private EditText monto;
	private EditText interes;
	private EditText retencion;
	private EditText descuento;
	private TextView titulo;
	private TableRow rowRetencion;
	private TableRow rowDescuento;
	private Documento document;	
	private Pagable eventPago;
	private ActionType actionType;
	private boolean editDescuento;
	private float montoAbonado;
	private NMApp nmapp;
		
	public interface Pagable {
		public void onPagarEvent(List<Ammount> montos);
	}

	public DialogoConfirmacion(Documento documento, ActionType actionType,boolean... editDescuento) {
		super();
		this.document = documento;
		this.actionType = actionType;
		this.editDescuento = editDescuento.length > 0 && editDescuento[0];
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
				List<Ammount> montos = new ArrayList<Ammount>();
				montos.add(new Ammount(AmmountType.ABONADO_OTROS_RECIBOS, montoAbonado, !editDescuento ) );
				montos.add(new Ammount(AmmountType.ABONADO, getMontoAbonado(), !editDescuento ) );
				montos.add(new Ammount(AmmountType.RETENIDO, getMontoRetenido(), !editDescuento) );
				montos.add(new Ammount(AmmountType.DESCONTADO, getMontoDescontado(), editDescuento));
				eventPago.onPagarEvent(montos);			
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

	@SuppressWarnings("unchecked")
	private void initComponents() {
		// obtenci�n de las views		
		numero = (EditText) view.findViewById(R.id.txtNoFactura);
		saldo = (EditText) view.findViewById(R.id.txtSaldo);
		monto = (EditText) view.findViewById(R.id.txtMonto);
		interes = (EditText) view.findViewById(R.id.txtInteres);
		rowRetencion = (TableRow) view.findViewById(R.id.tableRowLdpr);
		rowDescuento = (TableRow) view.findViewById(R.id.tableRowDescuento);
		retencion = (EditText) view.findViewById(R.id.txtRetencion);
		descuento = (EditText) view.findViewById(R.id.txtDescuento);
		titulo = (TextView) view.findViewById(R.id.txtTitulo);
		
		titulo.setText("Detalle Abono Documento");
		
		nmapp = (NMApp) this.getActivity().getApplicationContext();
		
		if( document instanceof ReciboDetFactura) {
			try {
				
				Message msg = new Message();
				msg.what = ControllerProtocol.LOAD_ABONOS_FACTURA_EN_OTROS_RECIBOS;
				Bundle params = new Bundle();
				ReciboDetFactura fac = (ReciboDetFactura) document.getObject();
				params.putLong("objFacturaID", fac.getObjFacturaID());
				params.putLong("objReciboID", fac.getObjReciboID());
				msg.setData(params);
				nmapp.getController().removeViewByName(DialogoConfirmacion.class.toString());
				nmapp.getController().removeBridgeByName(BLogicM.class.toString());
				nmapp.getController().setEntities(this, new BLogicM());
				nmapp.getController().addOutboxHandler(new Handler(this));
				nmapp.getController()
						.getInboxHandler()
						.sendMessage(msg);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}		
		
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
				nSaldo = nTotalDocumento - Float.parseFloat(String.valueOf(monto.replace(",", ""))) ;
				saldo.setText(StringUtil.formatReal(nSaldo));				
			}
			
		});
		
		// establecer valores		
		numero.setText(document.getNumero());
		saldo.setText(StringUtil.formatReal(document.getSaldo()));
		interes.setText("0.00");
		monto.setText(StringUtil.formatReal((document.getMonto())));
		retencion.setText(StringUtil.formatReal(document.getRetencion()));
		descuento.setText(StringUtil.formatReal(document.getRetencion()));
		//SI ESTAMOS ANTE UNA FACTURA Y ESTAMOS EDITANDO EL ITEM
		if ( document instanceof ReciboDetFactura ){
			if( this.actionType == ActionType.ADD ) {
				rowRetencion.setVisibility(View.GONE);
				rowDescuento.setVisibility(View.GONE);
			}
			else {
				rowRetencion.setVisibility(View.VISIBLE);
				rowDescuento.setVisibility(editDescuento ? View.VISIBLE : View.GONE);
			}				
			
			if (this.editDescuento) {	
				titulo.setText("Editando descuento");
				monto.setEnabled(false);
				retencion.setEnabled(false);
			}
			
		} 
	}
	
	private float getMontoAbonado(){
		String value = monto.getText().toString().trim();
		return Float.parseFloat(value == "" ? "0.00" : value.replace(",", "") );
	}
	
	private float getMontoRetenido(){
		String value = retencion.getText().toString().trim();
		return Float.parseFloat(value == "" ? "0.00" : value.replace(",", "") );
	}
	
	private float getMontoDescontado(){
		String value = descuento.getText().toString().trim();
		return Float.parseFloat(value == "" ? "0.00" : value.replace(",", "") );
	}
	
	public void setActionPago(Pagable actionPago) {
		this.eventPago = actionPago;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public boolean handleMessage(Message arg0) {
		Result respuesta = Result.toInt(arg0.what);
		switch(respuesta){
		case ABONOS_FACTURAS_OTROS_RECIBOS:
			montoAbonado = (Float)arg0.obj;
			break;
		}
		return false;
	}	

}