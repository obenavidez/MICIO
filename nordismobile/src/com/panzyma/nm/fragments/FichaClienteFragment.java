package com.panzyma.nm.fragments;



import com.panzyma.nordismobile.R;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.viewmodel.vmFicha;

public class FichaClienteFragment extends Fragment {
	
	public final static String ARG_POSITION = "position";
	public final static String OBJECT = "cliente";
	int mCurrentPosition = -1;
	private vmFicha customer;
	
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
			updateArticleView(mCurrentPosition ,customer);
			
		} else if (mCurrentPosition != -1) {
			// Set article based on saved instance state defined during
			// onCreateView
			updateArticleView(mCurrentPosition);
		}
	}

	public void updateArticleView(int position) {
		//R.id.article
		/*
		TextView codigoCliente = (TextView) getActivity().findViewById(R.id.gc_clientecod);
		TextView nombreCliente = (TextView) getActivity().findViewById(R.id.gc_clientenom);
		TextView sucursalCliente = (TextView) getActivity().findViewById(R.id.gc_clienteubi);
		
		codigoCliente.setText( Contenido.lista.get(position).getCodigo());
		nombreCliente.setText(Contenido.lista.get(position).getNombreCliente());
		sucursalCliente.setText(Contenido.lista.get(position).getUbicacion());
		*/
		mCurrentPosition = position;
	}

	public void updateArticleView(int position ,vmFicha obj) {
		// R.id.article
		//TextView customername = (TextView) getActivity().findViewById(R.id.fctextv_detallecliente);
		//TextView sucursal = (TextView) getActivity().findViewById(R.id.fctextv_detallesucursal);
		
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
		//customername.setText(obj.NombreCliente);
		//sucursal.setText(obj.);

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
}
