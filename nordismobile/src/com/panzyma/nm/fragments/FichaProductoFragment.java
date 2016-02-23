package com.panzyma.nm.fragments;

import static com.panzyma.nm.controller.ControllerProtocol.*;

import java.util.Arrays;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.AppDialog.DialogType; 
import com.panzyma.nm.serviceproxy.CProducto;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.CNotaViewHolder;
import com.panzyma.nordismobile.R;

@InvokeBridge(bridgeName = "BProductoM")
public class FichaProductoFragment extends Fragment implements Handler.Callback {

	private static final String TAG = FichaProductoFragment.class.getSimpleName();
	public final static String ARG_POSITION = "position";
	public final static String ARG_PRODUCTO = "idProducto";
	public final static String ARG_CONECCTION = "conecction";
	//private Producto producto;
	View _view;
	private CProducto fichaproducto;
	int mCurrentPosition = -1;
	Message ms = new  Message();
	static ProgressDialog pDialog;
	long productoID;
	private GenericAdapter adapter;
	private boolean conecction = false;
	android.support.v7.app.ActionBar bar;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putLong(ARG_PRODUCTO, productoID);
		outState.putParcelable("detail", fichaproducto); 
		Log.d(TAG, "onSaveInstanceState");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
	    super.onActivityCreated(savedInstanceState);
	    if (savedInstanceState != null) 
	    {	       
	      fichaproducto = savedInstanceState.getParcelable("detail");
	  	  mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
	  	  productoID = savedInstanceState.getLong(ARG_PRODUCTO);
	    }
	    Log.d(TAG, "onActivityCreated");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		_view= inflater.inflate(R.layout.view_producto_detalle, container,false);
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
	        _view= inflater.inflate(R.layout.view_producto_detalle, viewGroup);
	        if(fichaproducto!=null){
	        	Log.d(TAG, "fichaproducto No es Null");
	        	//((TextView) _view.findViewById(R.id.fctextv_detallesucursal)).setText(DetailCustomerSelected.getNombreSucursal());
	        	updateArticleView(this.mCurrentPosition,fichaproducto);
	        }
	}
	
	@Override
	public void onStart() {
		super.onStart();
		NMApp.controller.setView(this);
		Bundle args = getArguments();
		bar =((ActionBarActivity)getActivity()).getSupportActionBar();
		bar.hide();
		if (args != null) 
		{
			if(fichaproducto!=null){
				updateArticleView(this.mCurrentPosition,fichaproducto);
			}
			else {
				Bundle data = new Bundle();
				productoID = args.getLong(ARG_PRODUCTO);
				mCurrentPosition = args.getInt(ARG_POSITION);
				ms.what=LOAD_FICHAPRODUCTO_FROM_SERVER;
				data.putLong(ARG_PRODUCTO, productoID);
				ms.setData(data);
				NMApp.getController().getInboxHandler().sendMessage(ms); 
				pDialog = new ProgressDialog(getActivity());
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setMessage("Buscando Detalle del Producto...");
				pDialog.setCancelable(false);
				pDialog.show();
			}
			Log.d(TAG, "onStart");
		}
		
		
//		// below that sets the article text.
//		Bundle args = getArguments();
//		if (args != null) {
//			// Set article based on argument passed in
//			// updateArticleView(args.getInt(ARG_POSITION));
//			producto = (Producto) args.getParcelable(OBJECT);
//			mCurrentPosition = args.getInt(ARG_POSITION);
//			ms.what=LOAD_ITEM_FROM_LOCALHOST; 
//			ms.obj = producto.Id;
//			NMApp.controller.setView(this);
//			NMApp.getController().getInboxHandler().sendMessage(ms); 
//
//			//updateArticleView(producto, mCurrentPosition);
//		} else if (mCurrentPosition != -1) {
//			// Set article based on saved instance state defined during
//			// onCreateView
//			// updateArticleView(mCurrentPosition);
//			updateArticleView(producto, mCurrentPosition);
//		}
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
        NMApp.getController().setView((Callback)getActivity()); 
        Log.d(TAG, "onStop");
    }

	public void SetFichaProducto(CProducto producto){ 
		if(producto!=null){
			
			TextView name = (TextView) getActivity().findViewById(R.id.txt_view_product_producto);
			name.setText(producto.getNombre());
			TextView proveedor = (TextView) getActivity().findViewById(R.id.txt_view_product_proveedor);
			proveedor.setText(producto.getProveedor());
			TextView generico = (TextView) getActivity().findViewById(R.id.txt_view_product_nombre_generico);
			generico.setText(producto.getNombreGenerico());
			TextView forma = (TextView) getActivity().findViewById(R.id.txt_view_product_forma);
			forma.setText(producto.getFormaFarmaceutica());
			TextView accion = (TextView) getActivity().findViewById(R.id.txt_view_product_accion);
			accion.setText(producto.getAccionFarmacologica());
			TextView tipo = (TextView) getActivity().findViewById(R.id.txt_view_product_tipo);
			tipo.setText(producto.getTipoProducto());
			TextView categoria = (TextView) getActivity().findViewById(R.id.txt_view_product_categoria);
			categoria.setText(producto.getCategoria());
			TextView especialidades = (TextView) getActivity().findViewById(R.id.txt_view_product_especiales);
			especialidades.setText(producto.getEspecialidades());
			
			TextView gridheader=(TextView) getActivity().findViewById(R.id.fctextv_header2);
			TextView txtenty=(TextView) getActivity().findViewById(R.id.fctxtview_enty);
			ListView lvcnotas = (ListView) getActivity().findViewById(R.id.fclvnotas);
			if(producto.getNotas()!=null){
				String message = String.format("Notas del Producto(%d)", producto.getNotas().length);
				adapter=new GenericAdapter(NMApp.getContext(),CNotaViewHolder.class,Arrays.asList(producto.getNotas()),R.layout.grid_cnota);
				if(adapter!=null){
					lvcnotas.setAdapter(adapter);
					adapter.setSelectedPosition(0); 
				}
			}
			else
		    {
				gridheader.setText("Notas del Producto(0)");
	            txtenty.setVisibility(View.VISIBLE); 
	            lvcnotas.setEmptyView(txtenty);  
		    } 
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();

		switch (msg.what) 
		{ 
			case ID_SINCRONIZE_PRODUCTO : 
				fichaproducto = ((CProducto)((msg.obj==null)?new CProducto():msg.obj));
				updateArticleView(this.mCurrentPosition,fichaproducto);
				//SetFichaProducto(fichaproducto);
//				if (pDialog != null && pDialog.isShowing())
//					pDialog.dismiss();
			break;
			case NOTIFICATION_DIALOG: 
				AppDialog.showMessage(getActivity(),msg.obj.toString(),DialogType.DIALOGO_ALERTA);
				break;
			case ERROR:
				if (pDialog != null && pDialog.isShowing())
					pDialog.dismiss();
				
				AppDialog.showMessage(getActivity(),msg.obj.toString(),DialogType.DIALOGO_ALERTA);
				
				break;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void updateArticleView(int _position ,CProducto producto) {
		final CProducto obj =producto;
		final int position = _position;
		
//		getActivity().runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
				if(obj!=null){
					
					Log.d("OBJECTO", obj.toString());
					
					
					TextView name = (TextView) getActivity().findViewById(R.id.txt_view_product_producto);
					name.setText(obj.getNombre());
					TextView proveedor = (TextView) getActivity().findViewById(R.id.txt_view_product_proveedor);
					proveedor.setText(obj.getProveedor());
					TextView generico = (TextView) getActivity().findViewById(R.id.txt_view_product_nombre_generico);
					generico.setText(obj.getNombreGenerico());
					TextView forma = (TextView) getActivity().findViewById(R.id.txt_view_product_forma);
					forma.setText(obj.getFormaFarmaceutica());
					TextView accion = (TextView) getActivity().findViewById(R.id.txt_view_product_accion);
					accion.setText(obj.getAccionFarmacologica());
					TextView tipo = (TextView) getActivity().findViewById(R.id.txt_view_product_tipo);
					tipo.setText(obj.getTipoProducto());
					TextView categoria = (TextView) getActivity().findViewById(R.id.txt_view_product_categoria);
					categoria.setText(obj.getCategoria());
					TextView especialidades = (TextView) getActivity().findViewById(R.id.txt_view_product_especiales);
					especialidades.setText(obj.getEspecialidades());
					
//					((TextView) _view.findViewById(R.id.txt_view_product_producto)).setText(obj.getNombre());
//					((TextView) _view.findViewById(R.id.txt_view_product_proveedor)).setText(obj.getProveedor());
//					((TextView) _view.findViewById(R.id.txt_view_product_nombre_generico)).setText(obj.getNombreGenerico());
//					((TextView) _view.findViewById(R.id.txt_view_product_forma)).setText(obj.getFormaFarmaceutica());
//					((TextView) _view.findViewById(R.id.txt_view_product_accion)).setText(obj.getAccionFarmacologica()); 
//					((TextView) _view.findViewById(R.id.txt_view_product_tipo)).setText(obj.getTipoProducto());
//					((TextView) _view.findViewById(R.id.txt_view_product_categoria)).setText(obj.getCategoria());
//					((TextView) _view.findViewById(R.id.txt_view_product_especiales)).setText(obj.getEspecialidades());
					
					TextView gridheader=(TextView) getActivity().findViewById(R.id.fctextv_header2);
					TextView txtenty=(TextView) getActivity().findViewById(R.id.fctxtview_enty);
					ListView lvcnotas = (ListView) getActivity().findViewById(R.id.fclvnotas);
					if(obj.getNotas()!=null){
						String message = String.format("Notas del Producto(%d)", obj.getNotas().length);
						adapter=new GenericAdapter(NMApp.getContext(),CNotaViewHolder.class,Arrays.asList(obj.getNotas()),R.layout.grid_cnota);
						if(adapter!=null){
							lvcnotas.setAdapter(adapter);
							adapter.setSelectedPosition(0); 
						}
					}
					else{
						gridheader.setText("Notas del Producto(0)");
			            txtenty.setVisibility(View.VISIBLE); 
			            lvcnotas.setEmptyView(txtenty);  
				    } 
				}
//			}
//		});
	}
	
	
}
