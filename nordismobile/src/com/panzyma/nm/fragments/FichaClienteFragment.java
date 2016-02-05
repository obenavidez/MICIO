package com.panzyma.nm.fragments;

import static com.panzyma.nm.controller.ControllerProtocol.C_FICHACLIENTE;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_FICHACLIENTE_FROM_SERVER;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import java.util.Arrays;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.controller.ControllerProtocol;
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
	private vmFicha DetailCustomerSelected;
	int mCurrentPosition = -1;
	long sucursalID;
	private vmFicha customer; 
	private GenericAdapter adapter;
	Message ms = new  Message();
	View _view;
	static ProgressDialog pDialog;
	private static CustomDialog dlg;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_POSITION, mCurrentPosition); 
		outState.putParcelable("cliente", customer);
		outState.putLong(ARG_SUCURSAL, sucursalID);
		outState.putParcelable("detailcustomer", DetailCustomerSelected); 
		Log.d(TAG, "onSaveInstanceState");
	}	

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
	    super.onActivityCreated(savedInstanceState);
	    if (savedInstanceState != null) 
	    {	       
	      DetailCustomerSelected = savedInstanceState.getParcelable("detailcustomer");
	  	  mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
	  	  sucursalID = savedInstanceState.getLong(ARG_SUCURSAL);
	    }
	    Log.d(TAG, "onActivityCreated");
	}
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        
		_view = inflater.inflate(R.layout.ficha_cliente, container, false);
	
        // Find your buttons in view, set up onclicks, set up callbacks to your parent fragment or activity here.
        
        // You can create ViewHolder or separate method for that.
        // example of accessing views: TextView textViewExample = (TextView) view.findViewById(R.id.text_view_example);
        // textViewExample.setText("example");
		 Log.d(TAG, "onCreateView");
        return _view;
    }
	
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        LayoutInflater inflater = LayoutInflater.from(getActivity());
	        populateViewForOrientation(inflater, (ViewGroup) getView());
	 }
	 
	 private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
	        viewGroup.removeAllViewsInLayout();

	        _view= inflater.inflate(R.layout.ficha_cliente, viewGroup);
	        if(DetailCustomerSelected!=null){
	        	Log.d(TAG, "DetailCustomerSelected No es Null");
	        	//((TextView) _view.findViewById(R.id.fctextv_detallesucursal)).setText(DetailCustomerSelected.getNombreSucursal());
	        	updateArticleView(this.mCurrentPosition,DetailCustomerSelected);
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
				if(DetailCustomerSelected!=null){
					updateArticleView(this.mCurrentPosition,DetailCustomerSelected);
				}
				else{
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
			Log.d(TAG, "onStart");
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
				DetailCustomerSelected = ((vmFicha)((msg.obj==null)?new vmFicha():msg.obj));
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
	 
	 @SuppressWarnings("unchecked")
	 public void updateArticleView(int _position ,vmFicha ficha) {

			final vmFicha obj = ficha;
			final int position = _position;
			
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() { 
					((TextView) _view.findViewById(R.id.fctextv_detallecliente)).setText(obj.getNombreCliente());
					((TextView) _view.findViewById(R.id.fctextv_detallesucursal)).setText(obj.getNombreSucursal());
					((TextView) _view.findViewById(R.id.fctextv_detalledireccion)).setText(obj.getDireccionSucursal());
					((TextView) _view.findViewById(R.id.fctextv_detalletelefono)).setText(obj.getTelefono());
					((TextView) _view.findViewById(R.id.fctextv_detalletipo)).setText(obj.getTipo()); 
					((TextView) _view.findViewById(R.id.fctextv_detallecategoria)).setText(obj.getCategoria());
					((TextView) _view.findViewById(R.id.fctextv_detalleprecioventa)).setText(obj.getPrecioVenta());
					((TextView) _view.findViewById(R.id.fctextv_detallelimitecredito)).setText(String.valueOf(obj.getLimiteCredito())); 
					((TextView) _view.findViewById(R.id.fctextv_detalleplazocredito)).setText((String.valueOf(obj.getPlazoCredito())));
					((TextView) _view.findViewById(R.id.fctextv_detalleplazodescuento)).setText(String.valueOf(obj.getPlazoDescuento()));
					((TextView) _view.findViewById(R.id.fctextv_detalleminabono)).setText(String.valueOf(obj.getMontoMinimoAbono()));
					((TextView) _view.findViewById(R.id.fctextv_detalledescuento)).setText(obj.getDescuentos());
					
					TextView gridheader=(TextView)_view.findViewById(R.id.fctextv_header2);
					TextView txtenty=(TextView) _view.findViewById(R.id.fctxtview_enty);
					ListView lvcnotas = (ListView) _view.findViewById(R.id.fclvnotas);
					
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
			});
			
			
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
		@Override
		public void onDetach ()
		{
			Log.d(TAG, "OnDetach");
			android.support.v7.app.ActionBar a =((ActionBarActivity)getActivity()).getSupportActionBar();
			a.show();
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
}