package com.panzyma.nm.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class Adaptador extends BaseAdapter {

	private ArrayList<?> entradas;
	private int R_layout_IdView;
	private Context contexto;

	/**
	 * Adaptador para la lista de dise�o universal
	 * 
	 * @param contexto
	 *            El contexto de la aplicaci�n
	 * @param R_layout_IdView
	 *            Layout con las views que formar� un elemento de la lista
	 * @param entradas
	 *            ArrayList con los handlers de las entradas
	 */
	public Adaptador(Context contexto, int R_layout_IdView,
			ArrayList<?> entradas) {
		super();
		this.contexto = contexto;
		this.entradas = entradas;
		this.R_layout_IdView = R_layout_IdView;
	}

	@Override
	public View getView(int posicion, View view, ViewGroup pariente) {
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) contexto
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R_layout_IdView, null);
		}
		onEntrada(entradas.get(posicion), view);
		return view;
	}

	@Override
	public int getCount() {
		return entradas.size();
	}

	@Override
	public Object getItem(int posicion) {
		return entradas.get(posicion);
	}

	@Override
	public long getItemId(int posicion) {
		return posicion;
	}

	/**
	 * Devuelve cada una de las entradas con cada una de las vistas a la que
	 * debe de ser asociada
	 * 
	 * @param entrada
	 *            La entrada que ser� la asociada a la view. La entrada es del
	 *            tipo del paquete/handler
	 * @param view
	 *            View particular que contendr� los datos del paquete/handler
	 */
	public abstract void onEntrada(Object entrada, View view);

}
