package com.panzyma.nm.fragments;

import static com.panzyma.nm.controller.ControllerProtocol.*;

import java.util.Arrays;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.serviceproxy.CProducto;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.CNotaViewHolder;
import com.panzyma.nordismobile.R;

import static com.panzyma.nm.controller.ControllerProtocol.LOAD_ITEM_FROM_LOCALHOST;

@InvokeBridge(bridgeName = "BProductoM")
public class FichaProductoFragment extends Fragment implements Handler.Callback {

	public final static String ARG_POSITION = "position";
	public final static String ARG_PRODUCTO = "idProducto";
	//private Producto producto;
	int mCurrentPosition = -1;
	private static final String TAG = FichaProductoFragment.class.getSimpleName();
	Message ms = new  Message();
	static ProgressDialog pDialog;
	long productoID;
	private GenericAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.view_producto_detalle, container,false);
	}

	@Override
	public void onStart() {
		super.onStart();
		NMApp.controller.setView(this);
		Bundle args = getArguments();
//		android.support.v7.app.ActionBar a =((ActionBarActivity)getActivity()).getSupportActionBar();
//		a.hide();
		if (args != null) 
		{
			productoID = args.getLong(ARG_PRODUCTO);
			mCurrentPosition = args.getInt(ARG_POSITION);
			ms.what=LOAD_ITEM_FROM_LOCALHOST; 
			ms.obj = productoID;
			NMApp.getController().getInboxHandler().sendMessage(ms); 
			
			pDialog = new ProgressDialog(getActivity());
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Buscando Detalle del Producto...");
			pDialog.setCancelable(false);
			pDialog.show();
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

//	public void updateArticleView(int position) {
//		// R.id.article
//		TextView codigo = (TextView) getActivity().findViewById(
//				R.id.txt_view_product_codigo);
//		TextView descripcion = (TextView) getActivity().findViewById(
//				R.id.txt_view_product_nombre);
//		TextView existencia = (TextView) getActivity().findViewById(
//				R.id.txt_view_product_disponibilidad);
//
//		codigo.setText(Contenido.lista.get(position).getCodigo());
//		descripcion.setText(Contenido.lista.get(position).getNombreCliente());
//		existencia.setText(Contenido.lista.get(position).getUbicacion());
//
//		mCurrentPosition = position;
//	}
//
//	public void updateArticleView(Producto obj, int position) {
//		// R.id.article
//		TextView codigo = (TextView) getActivity().findViewById(R.id.txt_view_product_codigo);
//		TextView descripcion = (TextView) getActivity().findViewById(R.id.txt_view_product_nombre);
//		TextView existencia = (TextView) getActivity().findViewById(R.id.txt_view_product_disponibilidad);
//
//		codigo.setText(obj.getCodigo());
//		descripcion.setText(obj.getNombre());
//		existencia.setText(String.valueOf(obj.getDisponible()));
//
//		mCurrentPosition = position;
//	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putLong(ARG_PRODUCTO, productoID);
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

	public void SetFichaProducto(CProducto producto ){ 
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
				String message = String.format("Notas del Producto(%d)", producto.getNotas().getCNota().length);
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
		switch (msg.what) 
		{ 
			case ID_SINCRONIZE_PRODUCTO : 
				CProducto fichaproducto = ((CProducto)((msg.obj==null)?new CProducto():msg.obj));
				SetFichaProducto(fichaproducto);
				if (pDialog != null && pDialog.isShowing())
					pDialog.dismiss();
			break;
			case NOTIFICATION_DIALOG: 
				AppDialog.showMessage(getActivity(),msg.obj.toString(),DialogType.DIALOGO_ALERTA);
				break;
		}
		return false;
	}

}
