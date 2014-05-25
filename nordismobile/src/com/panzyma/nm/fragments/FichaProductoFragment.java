package com.panzyma.nm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.viewmodel.vmProducto;
import com.panzyma.nordismobile.R;

public class FichaProductoFragment extends Fragment {

	public final static String ARG_POSITION = "position";
	public final static String OBJECT = "product";
	private Producto producto;
	int mCurrentPosition = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			producto = (Producto) savedInstanceState.getParcelable(OBJECT);
		}

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.view_producto_detalle, container,
				false);
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
			producto = (Producto) args.getParcelable(OBJECT);
			mCurrentPosition = args.getInt(ARG_POSITION);
			updateArticleView(producto, mCurrentPosition);
		} else if (mCurrentPosition != -1) {
			// Set article based on saved instance state defined during
			// onCreateView
			// updateArticleView(mCurrentPosition);
			updateArticleView(producto, mCurrentPosition);
		}
	}

	public void updateArticleView(int position) {
		// R.id.article
		TextView codigo = (TextView) getActivity().findViewById(
				R.id.txt_view_product_codigo);
		TextView descripcion = (TextView) getActivity().findViewById(
				R.id.txt_view_product_nombre);
		TextView existencia = (TextView) getActivity().findViewById(
				R.id.txt_view_product_disponibilidad);

		codigo.setText(Contenido.lista.get(position).getCodigo());
		descripcion.setText(Contenido.lista.get(position).getNombreCliente());
		existencia.setText(Contenido.lista.get(position).getUbicacion());

		mCurrentPosition = position;
	}

	public void updateArticleView(Producto obj, int position) {
		// R.id.article
		TextView codigo = (TextView) getActivity().findViewById(R.id.txt_view_product_codigo);
		TextView descripcion = (TextView) getActivity().findViewById(R.id.txt_view_product_nombre);
		TextView existencia = (TextView) getActivity().findViewById(R.id.txt_view_product_disponibilidad);

		codigo.setText(obj.getCodigo());
		descripcion.setText(obj.getNombre());
		existencia.setText(String.valueOf(obj.getDisponible()));

		mCurrentPosition = position;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the current article selection in case we need to recreate the
		// fragment
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putParcelable(OBJECT, producto);
	}

}
