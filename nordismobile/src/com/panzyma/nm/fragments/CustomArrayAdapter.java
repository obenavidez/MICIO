package com.panzyma.nm.fragments;

import java.io.Serializable;
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

import com.panzyma.nm.NMApp;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.interfaces.DevolucionItem;
import com.panzyma.nm.interfaces.Item;
import com.panzyma.nordismobile.R;

@SuppressLint("DefaultLocale") 
public class CustomArrayAdapter<E> extends ArrayAdapter<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1251056210535658217L;
	private boolean SpecialItem;
	

	@Override
	public void remove(E object) {
		items.remove(object);
	}

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
	
	 @Override
	 public void notifyDataSetChanged() 
	 {
	    super.notifyDataSetChanged();  
//	    if(NMApp.getController().getView()!=null && (NMApp.getController().getView() instanceof vCliente || NMApp.getController().getView() instanceof ProductoView))
	    	NMApp.getController().notifyOutboxHandlers(ControllerProtocol.UPDATE_LISTVIEW_HEADER, 0, 0,1); 
	 }

	public CustomArrayAdapter(Context context, int textViewResourceId, List<E> objects) {
		super(context, textViewResourceId, objects = (objects==null)? new ArrayList<E>():objects);		
		this.context = context;
		items = objects;
	}
	public CustomArrayAdapter(Context context, int textViewResourceId, List<E> objects, boolean ViewHolder_Devoluciones) {
		super(context, textViewResourceId, objects = (objects==null)? new ArrayList<E>():objects);		
		this.context = context;
		items = objects;
		SpecialItem = ViewHolder_Devoluciones;
	}
	
	public void setData(List<E> data) {        
        items.clear();
        if (data != null) {
            for (E appEntry : data) {                
                items.add(appEntry);
            }
        }
    }
	
	public void setData(List<E> data, boolean ViewHolder_Devoluciones) {   
		SpecialItem = ViewHolder_Devoluciones;
        items.clear();
        if (data != null) {
            for (E appEntry : data) {                
                items.add(appEntry);
            }
        }
    }

	
	
	@Override
	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		ViewHolderDevolucion holderdevolucion = null;
		Item rowItem =null;
		DevolucionItem Item=null; 
		
		
	   if(SpecialItem)
		   Item  = (DevolucionItem)getItem(position);
	   else 
		   rowItem = (Item)getItem(position);
	   

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) 
		{

			 if(SpecialItem)
			 {
				 convertView = mInflater.inflate(R.layout.list_row_devolucion, null);
				 holderdevolucion = new ViewHolderDevolucion();
				 holderdevolucion.txtNumero = (TextView) convertView.findViewById(R.id.Numero);
				 holderdevolucion.txtfecha = (TextView) convertView.findViewById(R.id.fecha);
				 holderdevolucion.txtcustomer = (TextView) convertView.findViewById(R.id.customer);
				 holderdevolucion.txtmonto = (TextView) convertView.findViewById(R.id.totalmonto);
				 holderdevolucion.txtestado = (TextView) convertView.findViewById(R.id.Estado);
				 convertView.setTag(R.layout.list_row_devolucion,holderdevolucion);
			 }
			 else 
			 {
				convertView = mInflater.inflate(R.layout.list_row, null);
				holder = new ViewHolder();

				holder.txtName = (TextView) convertView.findViewById(R.id.menu_name);
				holder.txtDescription = (TextView) convertView.findViewById(R.id.description);
				holder.txtExtraInfo = (TextView) convertView.findViewById(R.id.price); 
				convertView.setTag(R.layout.list_row,holder);
			 }
		} 
		
			 if(SpecialItem)
			 {
				 holderdevolucion = ((ViewHolderDevolucion) convertView.getTag(R.layout.list_row_devolucion));
				 holderdevolucion.txtNumero.setText(Item.getItemNumero());
				 holderdevolucion.txtfecha.setText(Item.getItemfecha());
				 holderdevolucion.txtcustomer.setText(Item.getItemCliente());
				 holderdevolucion.txtmonto.setText(Item.getItemTotal());
				 holderdevolucion.txtestado.setText(Item.getItemEstado());
				 if(Item.getItemOffline()){
					 holderdevolucion.txtNumero.setTextColor(convertView.getResources().getColor(R.color.Red));
					 holderdevolucion.txtfecha.setTextColor(convertView.getResources().getColor(R.color.Red));
					 holderdevolucion.txtcustomer.setTextColor(convertView.getResources().getColor(R.color.Red));
					 holderdevolucion.txtmonto.setTextColor(convertView.getResources().getColor(R.color.Red));
					 holderdevolucion.txtestado.setTextColor(convertView.getResources().getColor(R.color.Red));
				 }
			 }
			 else 
			 {
				 holder = ((ViewHolder) convertView.getTag(R.layout.list_row));
				 holder.txtName.setText(rowItem.getItemName());
				 holder.txtDescription.setText(rowItem.getItemDescription());
				 holder.txtExtraInfo.setText(" | "+rowItem.getItemCode() + " CD");
			 }
		
		

		 if (position == selectedPos) {
 	         convertView.setBackgroundColor(convertView.getResources().getColor(R.color.Gold));
			 //convertView.setBackgroundDrawable(convertView.getResources().getDrawable(R.drawable.action_item_selected));
		    }
		    else {
		    	
		        convertView.setBackgroundColor(convertView.getResources().getColor(R.color.White));
		    } 
		 
		return convertView;
	}
	
	public List<E> AddAllToListViewDataSource(List<E> obj)
	{
		items = obj.subList(0, obj.size() - 1);
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
		if ( items.size() > 0 )
			return items.get(position);
		else 
			return null;
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

				if (mOriginalValues == null && items!=null)
					mOriginalValues = new ArrayList<E>(items); 
				if (constraint == null || constraint.length() == 0) {
					// setear los valores originales a regresar
					results.count = mOriginalValues.size();
					results.values = mOriginalValues;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < mOriginalValues.size(); i++) 
					{
						try 
						{
							E data = mOriginalValues.get(i);
							Object obj = null;
							if(SpecialItem==false)
								 obj = ((Item) data).isMatch(constraint);
							else 
								 obj = ((DevolucionItem) data).isMatch(constraint);
							if (Boolean.valueOf(obj.toString()))
								FilteredArrList.add(data);
							results.count = FilteredArrList.size();
							results.values = FilteredArrList;

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (results==null || (results!=null && results.values==null) || (results!=null &&  results.values!=null && ((List<E>)results.values).size()==0))
                    {
                    	// setear los valores originales a returnar  
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
            		}
				}

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) 
			{
//				items = (List<E>) results.values;
//				
//				notifyDataSetChanged(); // notificar al base adapter que hay
//										// nuevo valores que han sido filtrados
//				clear();
//				for (int i = 0; i < items.size(); i++) {
//					add(items.get(i));
//				}
//				notifyDataSetInvalidated();
				
				 items =  (results!=null && results.values!=null)?(List<E>)results.values:new ArrayList<E>(); //contiene los datos filtrados				 
                 notifyDataSetChanged();  //notificar al base adapter que hay nuevo valores que han sido filtrados
				
			}

		};

		return filter;
	}
	
	
	/* private view holder class */
	private class ViewHolder { 
		TextView txtName;
		TextView txtDescription;
		TextView txtExtraInfo;
	}

	private class ViewHolderDevolucion { 
		TextView txtNumero;
		TextView txtfecha;
		TextView txtcustomer;
		TextView txtmonto;
		TextView txtestado;
	}

}
