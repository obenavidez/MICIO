package com.panzyma.nm.fragments;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.viewmodel.vmRecibo;
import com.panzyma.nordismobile.R;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FichaReciboFragment extends Fragment {
	
	public final static String ARG_POSITION = "position";
	public final static String OBJECT = "recibo";
	private vmRecibo recibo;
	int mCurrentPosition = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			recibo = (vmRecibo) savedInstanceState.getParcelable(OBJECT);
		}

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.view_producto_detalle, container,
				false);
	}
	
	@Override
    public void onStop() {
        super.onStop();
        NMApp.getController().setView((Callback)getActivity()); 
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
			// updateArticleView(args.getInt(ARG_POSITION));
			recibo = (vmRecibo) args.getParcelable(OBJECT);
			mCurrentPosition = args.getInt(ARG_POSITION);
			updateArticleView(recibo, mCurrentPosition);
		} else if (mCurrentPosition != -1) {
			// Set article based on saved instance state defined during
			// onCreateView
			// updateArticleView(mCurrentPosition);
			updateArticleView(recibo, mCurrentPosition);
		}
	}

	public void updateArticleView(int position) {
		// R.id.article
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

		mCurrentPosition = position;
	}

	public void updateArticleView(vmRecibo obj, int position) {
		// R.id.article
		/*TextView codigo = (TextView) getActivity().findViewById(R.id.txt_view_product_codigo);
		TextView descripcion = (TextView) getActivity().findViewById(R.id.txt_view_product_nombre);
		TextView existencia = (TextView) getActivity().findViewById(R.id.txt_view_product_disponibilidad);

		codigo.setText(obj.getCodigo());
		descripcion.setText(obj.getNombre());
		existencia.setText(obj.getDisponibilidad().toString());*/

		mCurrentPosition = position;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the current article selection in case we need to recreate the
		// fragment
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putParcelable(OBJECT, recibo);
	}


}
