package com.panzyma.nm.fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nordismobile.R;

@SuppressWarnings("unchecked")
@SuppressLint({ "NewApi", "ValidFragment" })
public class ListaFragment<E> extends ListFragment implements Filterable,
		Parcelable {

	transient OnItemSelectedListener mCallback;
	private transient List<E> items;
	private transient CustomArrayAdapter<E> mAdapter = null;
	private int pos = 0;
	private Activity activity;

	public ListaFragment() {
	}

	public void setItems(List<E> items) {
		if (mAdapter == null) {
			Activity a=getActivity();
			if(a==null)
				return;
			mAdapter = new CustomArrayAdapter<E>(a,
					android.R.id.list, items);
		} else {
			mAdapter.setData(items);
		}
		setListAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
	public void setItems(List<E> items, boolean use_special_holder) {
		if (mAdapter == null) {
			Activity a=getActivity();
			if(a==null)
				return;
			mAdapter = new CustomArrayAdapter<E>(a,android.R.id.list, items,use_special_holder);
		} else {
			mAdapter.setData(items,use_special_holder);
		}
		setListAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	

	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnItemSelectedListener<E> {
		/** Called by ListaCliente when a list item is selected */
		public void onItemSelected(E obj, int position);
	}

	/*
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) {
	 * 
	 * mAdapter = new CustomArrayAdapter<E>(getActivity(), android.R.id.list,
	 * items); setListAdapter(mAdapter);
	 * 
	 * return super.onCreateView(inflater, container, savedInstanceState); }
	 */

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
			// getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
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

	@SuppressWarnings("deprecation")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Notify the parent activity of selected item
		mCallback.onItemSelected(getAdapter().getItems().get(position),
				position);
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public ListaFragment(Parcel in) {
	}

	public static final Parcelable.Creator<ListaFragment> CREATOR = new Parcelable.Creator<ListaFragment>() {
		@Override
		public ListaFragment createFromParcel(Parcel in) {
			return new ListaFragment(in);
		}

		@Override
		public ListaFragment[] newArray(int size) {
			return new ListaFragment[size];
		}
	};

}