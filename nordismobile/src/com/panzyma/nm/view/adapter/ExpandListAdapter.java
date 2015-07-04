package com.panzyma.nm.view.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList; 
 

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter; 

public class ExpandListAdapter<E, V> extends BaseExpandableListAdapter {

	private Context context; 
	private ArrayList<E> groups;//List<E> items;	
	private ArrayList<SetViewHolderWLayout> holderloyout;//layoutchild  
	SetViewHolderWLayout parentlayout;
	SetViewHolderWLayout childlayout;
	public ExpandListAdapter(Context _context, ArrayList<E> _groups,ArrayList<SetViewHolderWLayout> ... _holderloyout) 
	{
		this.context = _context; 
		this.groups = _groups;
		this.holderloyout=_holderloyout[0] ;
		getParentView();
		getChildView();
	}    
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return  groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		//ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
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
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) 
	{
		V viewHolder = null; 
		try 
		{
			
			if (view == null) { 
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(parentlayout.getLayoutid(), null);
				viewHolder=(V) (parentlayout.getViewHoder().newInstance()); 
				invokeView(view,viewHolder);
				view.setTag(viewHolder);		
				
			}
			else
				viewHolder=(V)view.getTag();
			
			 viewHolder.getClass().getMethod("mappingData",Object.class).invoke(viewHolder,groups.get(groupPosition));

			 
		} catch (Exception e) {
			// TODO: handle exception
		}				
		return view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		
		V viewHolder = null; 
		try 
		{			
			if (view == null) { 
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(childlayout.getLayoutid(), null); 
				viewHolder=(V) (childlayout.getViewHoder().newInstance()); 
				invokeView(view,viewHolder);
				view.setTag(viewHolder);				
			}
			else
				viewHolder=(V)view.getTag();
			
			 viewHolder.getClass().getMethod("mappingData",Object.class,int.class).invoke(viewHolder,groups.get(groupPosition),childPosition);
			 
		} catch (Exception e) {
			// TODO: handle exception
		}				
		return view;
	}
	

	public SetViewHolderWLayout getParentView()
	{
		if( parentlayout==null){
			for(SetViewHolderWLayout hl:holderloyout)
				if(hl.isParent()){
					parentlayout=hl;
					break;
				}
		} 
		return parentlayout; 
	}
	
	public SetViewHolderWLayout getChildView()
	{
		if( childlayout==null)
		{
			for(SetViewHolderWLayout hl:holderloyout)
				if(!hl.isParent()){
					childlayout=hl;
					break;
				}
		} 
		return childlayout; 
	}
	
	
	 

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	 

}
