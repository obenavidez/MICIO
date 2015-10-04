package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BLogicM.Result;
import com.panzyma.nm.auxiliar.ActionType;
import com.panzyma.nm.auxiliar.Ammount;
import com.panzyma.nm.auxiliar.AmmountType;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.auxiliar.Util;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.serviceproxy.Documento; 
import com.panzyma.nm.serviceproxy.ReciboColector;
import com.panzyma.nm.serviceproxy.ReciboDetFactura;
import com.panzyma.nm.serviceproxy.ReciboDetNC;
import com.panzyma.nm.serviceproxy.ReciboDetND;
import com.panzyma.nm.serviceproxy.SolicitudDescuento;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
@InvokeBridge(bridgeName = "BReciboM")
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
	private ReciboColector recibo;
	private SolicitudDescuento solicitud;
		
	public interface Pagable {
		public void onPagarEvent(List<Ammount> montos);
	}

	public DialogoConfirmacion(Documento documento, ReciboColector recibo, ActionType actionType,SolicitudDescuento _sd,boolean... editDescuento) {
		super();
		this.document = documento;
		this.recibo = recibo;
		this.actionType = actionType;
		this.editDescuento = editDescuento.length > 0 && editDescuento[0];
		this.solicitud=_sd;
	}
	
	public DialogoConfirmacion(Documento documento, ReciboColector recibo, ActionType actionType,boolean... editDescuento) {
		super();
		this.document = documento;
		this.recibo = recibo;
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
			public void onClick(DialogInterface dialog, int which) 
			{
				
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
                	if(editDescuento)
    				{
    											
    						if(getMontoDescontado()<=((solicitud!=null)?solicitud.getPorcentaje():recibo.getPorcDescOcaColector())){;;}
    						else
    							{
    								descuento.setError("El descuento debe ser menor o igual a "+((solicitud!=null)?solicitud.getPorcentaje():recibo.getPorcDescOcaColector())+"% ..."); 
    								descuento.requestFocus(); 
    								return;
    							}
    					 
    				}
    				List<Ammount> montos = new ArrayList<Ammount>();
    				montos.add(new Ammount(AmmountType.ABONADO_OTROS_RECIBOS,Util.Numero.redondear( montoAbonado , 2), !editDescuento ) );
    				montos.add(new Ammount(AmmountType.ABONADO, Util.Numero.redondear(getMontoAbonado(), 2) , !editDescuento ) );
    				montos.add(new Ammount(AmmountType.RETENIDO,Util.Numero.redondear( getMontoRetenido(), 2) , !editDescuento) );
    				montos.add(new Ammount(AmmountType.DESCONTADO, Util.Numero.redondear(getMontoDescontado(), 2) , editDescuento));
    				eventPago.onPagarEvent(montos);		
    				dismiss();
                }
            });
	    }
	} 

	@SuppressWarnings({ "unchecked", "static-access" })
	private void initComponents() {
		// obtención de las views		
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
		
		monto.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) 
			{
				
				Long documentID = null, reciboID = null, documentType = null;
				
				if(document instanceof ReciboDetFactura){
					ReciboDetFactura fac = (ReciboDetFactura) document.getObject();
					documentID = fac.getObjFacturaID();
					reciboID = fac.getObjReciboID();
					documentType = 10L;
				} else if (document instanceof ReciboDetND) {
					ReciboDetND nd = (ReciboDetND) document.getObject();
					documentID = nd.getObjNotaDebitoID();
					reciboID = nd.getObjReciboID();
					documentType = 20L;
				} else if (document instanceof ReciboDetNC) {
					ReciboDetNC nc = (ReciboDetNC) document.getObject();
					documentID = nc.getObjNotaCreditoID();
					reciboID = nc.getObjReciboID();
					documentType = 30L;
				}
				LoadDataToUI loadData = new LoadDataToUI(arg0.toString().trim());
				loadData.execute(new Long[]{documentID,reciboID,documentType});
			}
			
		});
		
		// establecer valores		
		numero.setText(document.getNumero());
		saldo.setText(StringUtil.formatReal(document.getSaldo()));
		interes.setText("0.00");
		//monto.setText(StringUtil.formatReal((document.getMonto())));	
		switch(actionType){
		case ADD: 
			monto.setText(StringUtil.formatReal(document.getSaldo())); 
			break;
		case EDIT:
			monto.setText(StringUtil.formatReal(document.getMonto()));
			break;
		default:
			break;
		}		
		retencion.setText(StringUtil.formatReal(document.getRetencion()));
		descuento.setText(StringUtil.formatReal(document.getRetencion()));
		//SI ESTAMOS ANTE UNA FACTURA Y ESTAMOS EDITANDO EL ITEM
		if ( document instanceof ReciboDetFactura )
		{
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
				descuento.setText("" +((ReciboDetFactura)document.getObject()).getPorcDescOcasional() );
				
				descuento.requestFocus(); 
			}			
		} 
		else if( document instanceof ReciboDetND ) 
		{
			if( this.actionType == ActionType.ADD ) {
				rowRetencion.setVisibility(View.GONE);
				rowDescuento.setVisibility(View.GONE);
			}
			/*rowRetencion.setVisibility(View.GONE);
			rowDescuento.setVisibility(View.GONE);*/
		} 
		else if ( document instanceof ReciboDetNC ) 
		{
			if( this.actionType == ActionType.ADD ) {
				rowRetencion.setVisibility(View.GONE);
				rowDescuento.setVisibility(View.GONE);
			}
			monto.setEnabled(false);
			/*rowRetencion.setVisibility(View.GONE);
			rowDescuento.setVisibility(View.GONE);*/
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

	public class LoadDataToUI extends AsyncTask<Long, Void, Float > {

		private String monto = "0";			
		
		public LoadDataToUI(String monto){
			this.monto = monto;
		}
		
		@Override
		protected Float doInBackground(Long... params) 
		{			
			return ModelLogic.getAbonosEnOtrosRecibos(DialogoConfirmacion.this.getActivity(), params[0], params[1], params[2]);
		}

		@Override
		protected void onPostExecute(Float result) {
			float nSaldo = 0.00f, 
				  nTotalDocumento = 0.00f,
				  nAbonadoEnOtrosRecibos = result;			
			montoAbonado = nAbonadoEnOtrosRecibos;
			if(document instanceof ReciboDetFactura) {
				nTotalDocumento = ((ReciboDetFactura)document).getTotalFacturaOrigen();				
			} else if (document instanceof ReciboDetND){
				nTotalDocumento = ((ReciboDetND)document).getMontoND();
			} else if (document instanceof ReciboDetNC) {
				nTotalDocumento = ((ReciboDetNC)document).getMonto();
			}			
			nSaldo = Util.Numero.redondear(nTotalDocumento,2) -Util.Numero.redondear(nAbonadoEnOtrosRecibos,2); 
			monto  = ( monto.toString().trim().length() == 0 ? "0" : monto);
			nSaldo = Util.Numero.redondear(nSaldo,2) - Util.Numero.redondear(Float.parseFloat(String.valueOf(monto.replace(",", ""))),2) ;			
			saldo.setText(StringUtil.formatReal(nSaldo));		
			super.onPostExecute(result);
		}
		
	}
	
}