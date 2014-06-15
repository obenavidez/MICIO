package com.panzyma.nm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nordismobile.R;

@SuppressWarnings("unchecked")
@SuppressLint("NewApi")
public class ListaFragment<E> extends ListFragment implements Filterable {

	OnItemSelectedListener mCallback;
	private List<E> items;
	private CustomArrayAdapter<E> mAdapter = null;
	private int pos = 0;
	private Activity activity;

	public ListaFragment() {
	}

	public void setItems(List<E> items) {
		if (mAdapter == null) {
			mAdapter = new CustomArrayAdapter<E>( getActivity(), android.R.id.list, items);
			setListAdapter(mAdapter);
		} else {			
			mAdapter.items = items;
		}
		mAdapter.notifyDataSetChanged();
	}
	 
	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnItemSelectedListener<E> {
		/** Called by ListaCliente when a list item is selected */
		public void onItemSelected(E obj, int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
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
		 
		if (getFragmentManager().findFragmentById(R.id.dynamic_fragment) != null) {
			//getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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

	@SuppressWarnings("deprecation")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Notify the parent activity of selected item
		mCallback.onItemSelected(getAdapter().getItems().get(position), position);
		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
		mAdapter.setSelectedPosition(position);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public CustomArrayAdapter<E> getAdapter() {
		return mAdapter; 
	}  
	
	public void setAdapter(CustomArrayAdapter<E> adapter) {
		this.mAdapter = adapter; 
	}
	
}
