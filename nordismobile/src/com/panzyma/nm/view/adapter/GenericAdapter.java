package com.panzyma.nm.view.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.viewdialog.DialogSolicitudDescuento;
import com.panzyma.nordismobile.R;

@SuppressWarnings("unused")
public class GenericAdapter<E, V> extends BaseAdapter implements Filterable {
	   
	private int layoutid;
	private Class<V> viewclass;
	private LayoutInflater inflater; 
	private List<E> items;
	private List<E> mOriginalValues;
	private Context context;
	private int selectedPos=0; 
	private int positionCache;
	
	public GenericAdapter(Context c,Class<V> viewclass,List<E> items,int... layoutid) { 		 
		this.context=c;
		this.inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		this.items = items; 
		this.viewclass=viewclass;
		this.layoutid=layoutid[0]; 
		notifyDataSetChanged();
	}	
	public List<E> AddAllToListViewDataSource(List<E> obj)
	{ 
		//items.add(obj.get(obj.size()-1)); 
		this.notifyDataSetChanged();
		return items;
	} 
	
	public List<E> setItems(List<E> list)
	{ 
		this.items = list;
		notifyDataSetChanged();
		return items;
	} 
	
	
	public List<E> Add(E obj)
	{ 
		items.add(obj); 
		this.notifyDataSetChanged();
		return items;
	} 
	public List<E> getData()
	{
		return this.items;
	}
	@Override
	public int getCount() {		
		return items.size();
	}
	public void clearItems()
	{ 
		items.clear();
		notifyDataSetChanged();
	}
	public void setSelectedPosition(int pos)
	{
		selectedPos = pos;
	}	
    public int getSelectedPosition(){
		return selectedPos;
	}   
    
    public void setPositionCache(int posicion){
    	this.positionCache = posicion;
    }
    
    public int getPositionCache() {
    	return this.positionCache;
    }
    
	@Override
	public Object getItem(int position) {
		return items.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	protected void invokeView(View v,V viewHolder){
		try {
			Field fs[] = viewHolder.getClass().getFields();
			for (Field f : fs) {
				InvokeView a = f.getAnnotation(InvokeView.class);
				if(a!=null)
				{
					int id = a.viewId();
					f.set(viewHolder, v.findViewById(id));
				} 

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertview, ViewGroup parent) 
	{   
		V viewHolder = null;
		try
		{
			if (null== convertview)
			{
				convertview=this.inflater.inflate(layoutid,null);
				viewHolder=viewclass.newInstance(); 
				invokeView(convertview,viewHolder);
				convertview.setTag(viewHolder);				
			}
			else
				viewHolder=(V)convertview.getTag();
			
			if(getSelectedPosition() == position) {
				if( NMApp.getController().getView()!=null && NMApp.getController().getView() instanceof DialogSolicitudDescuento)
					convertview.setBackgroundResource(android.R.color.transparent); 
				else
				convertview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.action_item_selected));
			} else {
				convertview.setBackgroundResource(android.R.color.transparent); 
			}				
		    
		    viewHolder.getClass().getMethod("mappingData",Object.class).invoke(viewHolder,items.get(position));
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		return convertview;
	} 
	
	 @Override
	 public void notifyDataSetChanged() 
	 {
	    super.notifyDataSetChanged();  
//	    if(NMApp.getController().getView()!=null && (NMApp.getController().getView() instanceof DialogCliente || NMApp.getController().getView() instanceof DialogProducto))
	    	NMApp.getController().notifyOutboxHandlers(ControllerProtocol.UPDATE_LISTVIEW_HEADER, 0, 0,1); 
	 }
	
	
	@Override
	public Filter getFilter() {

		Filter filter = new Filter() 
        {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) { 
				
				 FilterResults results = new FilterResults(); 
                 List<E> FilteredArrList = new ArrayList<E>(); 
                 if (mOriginalValues  == null && items!=null) 
                	 mOriginalValues  = new ArrayList<E>(items); // guardar los datos originales en  mOriginalValues
                 if (constraint == null || constraint.length() == 0) 
                 {
                     // setear los valores originales a returnar  
                     results.count = mOriginalValues.size();
                     results.values = mOriginalValues;
                 } 
                 else 
                 {
                     constraint = constraint.toString().toLowerCase();
                     for (int i = 0; i < mOriginalValues.size(); i++) 
                     {
                    	try 
                    	{ 
                    		E data = mOriginalValues.get(i);                    		
							Object obj=data.getClass().getMethod("isMatch", CharSequence.class).invoke(data, constraint);
							if(Boolean.valueOf(obj.toString()))
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
			protected void publishResults(CharSequence constraint, FilterResults results) 
			{				
				 items =  (results!=null && results.values!=null)?(List<E>)results.values:new ArrayList<E>(); //contiene los datos filtrados				 
                 notifyDataSetChanged();  //notificar al base adapter que hay nuevo valores que han sido filtrados
			}
			
        };
		
		
		return filter;
	}  
  
	public List<E> getOriginal(){
		return this.mOriginalValues;
	}
	
	
	
}
