package com.panzyma.nm.fragments;

import static com.panzyma.nm.controller.ControllerProtocol.C_FICHACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_FICHACLIENTE_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.interfaces.GenericDocument;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.CNotaViewHolder;
import com.panzyma.nm.viewmodel.vmFicha;
import com.panzyma.nordismobile.R;

@SuppressLint({ "ValidFragment", "NewApi" })
@SuppressWarnings({"unused", "rawtypes" })
@InvokeBridge(bridgeName = "BClienteM")
public class FichaClienteFragment extends Fragment implements Handler.Callback {
	
	private static final String TAG = FichaClienteFragment.class.getSimpleName();
	public final static String ARG_POSITION = "position";
	public final static String ARG_SUCURSAL = "sucursal";
	public final static String OBJECT = "cliente";
	int mCurrentPosition = -1;
	private vmFicha customer; 
	private GenericAdapter adapter;
	long sucursalID;
	Message ms = new  Message();
	static ProgressDialog pDialog;
	private BClienteM bcm;
	private static CustomDialog dlg;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putLong(ARG_SUCURSAL, sucursalID);
		outState.putParcelable("cliente", customer);
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    if (savedInstanceState != null) {	       
	      customer = savedInstanceState.getParcelable("cliente");
	  	  mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
	  	  sucursalID = savedInstanceState.getLong(ARG_SUCURSAL);
	    }
	}

	@Override
	public void onStart() {	
		super.onStart();
		NMApp.controller.setView(this); 
		Bundle args = getArguments();
		android.support.v7.app.ActionBar a =((ActionBarActivity)getActivity()).getSupportActionBar();
		a.hide();
		
		if (args != null) 
		{
			sucursalID = args.getLong(ARG_SUCURSAL);
			mCurrentPosition = args.getInt(ARG_POSITION);
			ms.what=LOAD_FICHACLIENTE_FROM_SERVER; 
			ms.obj = sucursalID;
			NMApp.getController().getInboxHandler().sendMessage(ms); 
			 
			pDialog = new ProgressDialog(getActivity());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Buscando ficha del Cliente...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.ficha_cliente, container, false);
	}

	@Override
	public boolean handleMessage(Message msg) {
		ocultarDialogos();
		switch (msg.what) 
		{
		
		case ControllerProtocol.NOTIFICATION:
			showStatus(msg.obj.toString(), true);
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			showStatus(msg.obj.toString());
			break;
		case C_FICHACLIENTE: 
			vmFicha DetailCustomerSelected = ((vmFicha)((msg.obj==null)?new vmFicha():msg.obj));
			updateArticleView(this.mCurrentPosition,DetailCustomerSelected);
			break;
		case ControllerProtocol.ERROR:
			AppDialog.showMessage(getActivity(),msg.obj.toString(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			break;
		}
		return false;
	}
	
	public void ocultarDialogos() {
		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
	}
	
	public void showStatus(final String mensaje, boolean... confirmacion) {

		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(getActivity(), "", mensaje,
							AppDialog.DialogType.DIALOGO_ALERTA,
							new AppDialog.OnButtonClickListener() {
								@Override
								public void onButtonClick(AlertDialog _dialog,
										int actionId) {

									if (AppDialog.OK_BUTTOM == actionId) {
										_dialog.dismiss();
									}
								}
							});
				}
			});
		} else {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dlg = new CustomDialog(getActivity(), mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void updateArticleView(int position ,vmFicha obj) {

		((TextView) getActivity().findViewById(R.id.fctextv_detallecliente)).setText(obj.getNombreCliente());
		((TextView) getActivity().findViewById(R.id.fctextv_detallesucursal)).setText(obj.getNombreSucursal());
		((TextView) getActivity().findViewById(R.id.fctextv_detalledireccion)).setText(obj.getDireccionSucursal());
		((TextView) getActivity().findViewById(R.id.fctextv_detalletelefono)).setText(obj.getTelefono());
		((TextView) getActivity().findViewById(R.id.fctextv_detalletipo)).setText(obj.getTipo()); 
		((TextView) getActivity().findViewById(R.id.fctextv_detallecategoria)).setText(obj.getCategoria());
		((TextView) getActivity().findViewById(R.id.fctextv_detalleprecioventa)).setText(obj.getPrecioVenta());
		((TextView) getActivity().findViewById(R.id.fctextv_detallelimitecredito)).setText(String.valueOf(obj.getLimiteCredito())); 
		((TextView) getActivity().findViewById(R.id.fctextv_detalleplazocredito)).setText((String.valueOf(obj.getPlazoCredito())));
		((TextView) getActivity().findViewById(R.id.fctextv_detalleplazodescuento)).setText(String.valueOf(obj.getPlazoDescuento()));
		((TextView) getActivity().findViewById(R.id.fctextv_detalleminabono)).setText(String.valueOf(obj.getMontoMinimoAbono()));
		((TextView) getActivity().findViewById(R.id.fctextv_detalledescuento)).setText(obj.getDescuentos());  
		
		TextView gridheader=(TextView) getActivity().findViewById(R.id.fctextv_header2);
		TextView txtenty=(TextView) getActivity().findViewById(R.id.fctxtview_enty);
		ListView lvcnotas = (ListView) getActivity().findViewById(R.id.fclvnotas);
		
		if(obj.getNotas()!=null)
		{  
			String message = String.format("Notas del Cliente(%d)", obj.getNotas().length);
			gridheader.setText(message);
			adapter=new GenericAdapter(NMApp.getContext(),CNotaViewHolder.class,Arrays.asList(obj.getNotas()),R.layout.grid_cnota);
			if(adapter!=null){
				lvcnotas.setAdapter(adapter);
				adapter.setSelectedPosition(position); 
			}
			lvcnotas.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	            { 		  
	            	if((parent.getChildAt(mCurrentPosition))!=null)						            							            		
	            		(parent.getChildAt(mCurrentPosition)).setBackgroundResource(android.R.color.transparent);						            	 
	            	mCurrentPosition=position; 				
	            	adapter.setSelectedPosition(position);  
	            	view.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.action_item_selected));
	            }
	        }); 
		}
		else
	    {
			gridheader.setText("Notas del Cliente(0)");
            txtenty.setVisibility(View.VISIBLE); 
            lvcnotas.setEmptyView(txtenty);  
	    } 
		

		mCurrentPosition = position;
	}

	@Override
	public void onDetach ()
	{
		Log.d(TAG, "OnDetach");
		NMApp.getController().setView((Callback)getActivity()); 
		super.onDetach();
	}
	
	@Override
    public void onStop() {
        super.onStop();
        ocultarDialogos();
        NMApp.getController().setView((Callback)getActivity()); 
        Log.d(TAG, "onStop");
    }
	

  /*
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			customer = (vmFicha) savedInstanceState.getParcelable(OBJECT);
		}
		// Inflate the layout for this fragment		
		return inflater.inflate(R.layout.ficha_cliente, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		// During startup, check if there are arguments passed to the fragment.
		// onStart is a good place to do this because the layout has already
		// been
		// applied to the fragment at this point so we can safely call the
		// method
		// below that sets the article text.
		Bundle args = getArguments();
		if (args != null) {
			// Set article based on argument passed in
//			updateArticleView(args.getInt(ARG_POSITION));
			customer = (vmFicha) args.getParcelable(OBJECT);
			mCurrentPosition = args.getInt(ARG_POSITION);
			this.fcontext =getActivity().getApplicationContext();
			updateArticleView(mCurrentPosition ,customer);
			
		} else if (mCurrentPosition != -1) {
			// Set article based on saved instance state defined during
			// onCreateView
			updateArticleView(mCurrentPosition);
		}
	}
*/
	
	/*
	public void updateArticleView(int position ,vmFicha obj) {

		((TextView) getActivity().findViewById(R.id.fctextv_detallecliente)).setText(obj.getNombreCliente());
		((TextView) getActivity().findViewById(R.id.fctextv_detallesucursal)).setText(obj.getNombreSucursal());
		((TextView) getActivity().findViewById(R.id.fctextv_detalledireccion)).setText(obj.getDireccionSucursal());
		((TextView) getActivity().findViewById(R.id.fctextv_detalletelefono)).setText(obj.getTelefono());
		((TextView) getActivity().findViewById(R.id.fctextv_detalletipo)).setText(obj.getTipo()); 
		((TextView) getActivity().findViewById(R.id.fctextv_detallecategoria)).setText(obj.getCategoria());
		((TextView) getActivity().findViewById(R.id.fctextv_detalleprecioventa)).setText(obj.getPrecioVenta());
		((TextView) getActivity().findViewById(R.id.fctextv_detallelimitecredito)).setText(String.valueOf(obj.getLimiteCredito())); 
		((TextView) getActivity().findViewById(R.id.fctextv_detalleplazocredito)).setText((String.valueOf(obj.getPlazoCredito())));
		((TextView) getActivity().findViewById(R.id.fctextv_detalleplazodescuento)).setText(String.valueOf(obj.getPlazoDescuento()));
		((TextView) getActivity().findViewById(R.id.fctextv_detalleminabono)).setText(String.valueOf(obj.getMontoMinimoAbono()));
		((TextView) getActivity().findViewById(R.id.fctextv_detalledescuento)).setText(obj.getDescuentos());  
		
		TextView gridheader=(TextView) getActivity().findViewById(R.id.fctextv_header2);
		TextView txtenty=(TextView) getActivity().findViewById(R.id.fctxtview_enty);
		ListView lvcnotas = (ListView) getActivity().findViewById(R.id.fclvnotas);
		
		if(obj.getNotas()!=null)
		{  
			String message = String.format("Notas del Cliente(%d)", obj.getNotas().length);
			gridheader.setText(message);
			adapter=new GenericAdapter(fcontext,CNotaViewHolder.class,Arrays.asList(obj.getNotas()),R.layout.grid_cnota);
			if(adapter!=null){
			lvcnotas.setAdapter(adapter);
			adapter.setSelectedPosition(position); }
			lvcnotas.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	            { 		  
	            	if((parent.getChildAt(mCurrentPosition))!=null)						            							            		
	            		(parent.getChildAt(mCurrentPosition)).setBackgroundResource(android.R.color.transparent);						            	 
	            	mCurrentPosition=position; 				
	            	adapter.setSelectedPosition(position);  
	            	view.setBackgroundDrawable(fcontext.getResources().getDrawable(R.drawable.action_item_selected));
	            }
	        }); 
	        
		}
		else
	    {
			gridheader.setText("Notas del Cliente(0)");
            txtenty.setVisibility(View.VISIBLE); 
            lvcnotas.setEmptyView(txtenty);  
	    } 
		

		mCurrentPosition = position;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current article selection in case we need to recreate the
		// fragment
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putParcelable(OBJECT, customer);
	}
	*/
}
