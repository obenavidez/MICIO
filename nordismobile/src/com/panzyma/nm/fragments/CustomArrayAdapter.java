package com.panzyma.nm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.panzyma.nm.interfaces.Item;
import com.panzyma.nordismobile.R;

@SuppressLint("DefaultLocale") 
public class CustomArrayAdapter<E> extends ArrayAdapter<E> {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

    Context context;
	List<E> mOriginalValues;
	List<E> items;
    int selectedPos=0;
    
	public List<E> getItems() {
		return items;
	}

	public CustomArrayAdapter(Context context, int textViewResourceId, List<E> objects) {
		super(context, textViewResourceId, objects = (objects==null)? new ArrayList<E>():objects);		
		this.context = context;
		items = objects;
	}

	/* private view holder class */
	private class ViewHolder { 
		TextView txtName;
		TextView txtDescription;
		TextView txtExtraInfo;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		Item rowItem = (Item)getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();

			holder.txtName = (TextView) convertView.findViewById(R.id.menu_name);
			holder.txtDescription = (TextView) convertView.findViewById(R.id.description);
			holder.txtExtraInfo = (TextView) convertView.findViewById(R.id.price); 
			convertView.setTag(holder);

		} else
			holder = ((ViewHolder) convertView.getTag());

		holder.txtName.setText(rowItem.getItemName());
		holder.txtDescription.setText(rowItem.getItemDescription());
		holder.txtExtraInfo.setText(" | "+rowItem.getItemCode() + " CD"); 

		return convertView;
	}
	
	public List<E> AddAllToListViewDataSource(List<E> obj)
	{
		items.addAll(obj); 
		this.notifyDataSetChanged();
		return items;
	} 
	
	public void clearItems()
	{ 
		items.clear();
	}
	
	public void setSelectedPosition(int pos)
	{
		selectedPos = pos;  
	}	
   
	public int getSelectedPosition(){
		return selectedPos;
	}    
	
	@Override
	public E getItem(int position) {
		return items.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("DefaultLocale") 
	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressLint("DefaultLocale") @Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				List<E> FilteredArrList = new ArrayList<E>();

				if (mOriginalValues == null)
					mOriginalValues = new ArrayList<E>(items); 
				if (constraint == null || constraint.length() == 0) {
					// setear los valores originales a regresar
					results.count = mOriginalValues.size();
					results.values = mOriginalValues;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < mOriginalValues.size(); i++) {
						try {
							E data = mOriginalValues.get(i);
							Object obj = ((Item) data).isMatch(constraint); 
							if (Boolean.valueOf(obj.toString()))
								FilteredArrList.add(data);
							results.count = FilteredArrList.size();
							results.values = FilteredArrList;

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				items = (List<E>) results.values;
				
				notifyDataSetChanged(); // notificar al base adapter que hay
										// nuevo valores que han sido filtrados
				clear();
				for (int i = 0; i < items.size(); i++) {
					add(items.get(i));
				}
				notifyDataSetInvalidated();
			}

		};

		return filter;
	}

}
