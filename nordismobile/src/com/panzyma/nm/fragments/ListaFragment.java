package com.panzyma.nm.fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nordismobile.R;

@SuppressWarnings("unchecked")
@SuppressLint("NewApi")
public class ListaFragment<E> extends ListFragment implements Filterable {

	OnItemSelectedListener mCallback;
	private List<E> items;
	private CustomArrayAdapter<E> mAdapter;
	private int pos=0;


	public ListaFragment() {
	}

	public void setItems(List<E> items) {
		if (mAdapter == null) {
			mAdapter = new CustomArrayAdapter<E>(getActivity(),
					android.R.id.list, items);
			setListAdapter(mAdapter);
		}
		this.items = mAdapter.AddAllToListViewDataSource(items);
	}

	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnItemSelectedListener {
		/** Called by ListaCliente when a list item is selected */
		public void onItemSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create an array adapter for the list view, using the Ipsum headlines
		// array
		// setListAdapter(new ArrayAdapter<String>(getActivity(), layout,
		// Contenido.nombresClientes) );
		// items = (ArrayList<E>) dataSource.getData();
		// GlobalList.getMenuCategories().get(num).getMenu();
		mAdapter = new CustomArrayAdapter<E>(getActivity(), android.R.id.list,
				items);
		setListAdapter(mAdapter);

	}

	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected
		// list item
		// (We do this during onStart because at the point the listview is
		// available.)

		if (getFragmentManager().findFragmentById(R.id.ficha_client_fragment) != null) {
			getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Notify the parent activity of selected item
		mCallback.onItemSelected(position);
		//super.onListItemClick(l, v, position, id);
		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
		if(this.pos==position){
			this.pos = position;
			v.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_item_selected));
		}
		else {
			((ListView)(v.getParent())).getChildAt(this.pos).setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
			v.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_item_selected));
			this.pos = position;
		}
	}

	@Override
	public CustomArrayAdapter<E> getAdapter() {
		return mAdapter;
	}

}
