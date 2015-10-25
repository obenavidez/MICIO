package com.panzyma.nm.view.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.panzyma.nm.view.viewholder.ProductoLoteDetalleViewHolder;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class ExpandListAdapter<E, V> extends BaseExpandableListAdapter {

	private Context context; 
	private List<E> groups;//List<E> items;	
	private ArrayList<SetViewHolderWLayout> holderloyout;//layoutchild  
	SetViewHolderWLayout parentlayout;
	SetViewHolderWLayout childlayout; 
	int grouposition;
	int childposition; 
	
	public ExpandListAdapter(Context _context, List<E> _groups,ArrayList<SetViewHolderWLayout> ... _holderloyout) 
	{
		this.context = _context; 
		this.groups = _groups;
		this.holderloyout=_holderloyout[0] ;
		getParentView();
		getChildView();
	}    
	
	
	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		LinkedList<ExpandListChild> chList = ((ExpandListGroup)groups.get(groupPosition)).getItems();
		return chList.get(childPosition);
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub 
		return (groups!=null)?groups.size():0;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
	    return childPosition;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		LinkedList<ExpandListChild> chList = ((ExpandListGroup)groups.get(groupPosition)).getItems();

		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return  groups.get(groupPosition);
	} 
	public Object getData() {
		// TODO Auto-generated method stub
		return  groups;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
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
	
 
	@SuppressLint("ShowToast") @SuppressWarnings("unchecked")
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) 
	{
		V viewHolder = null; 
		try 
		{
			ExpandListGroup _group = (ExpandListGroup) getGroup(groupPosition);

			if (view == null) { 
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(parentlayout.getLayoutid(), null);
				viewHolder=(V) (parentlayout.getViewHoder().newInstance()); 
				invokeView(view,viewHolder);
				view.setTag(viewHolder);		
				
			}
			else
				viewHolder=(V)view.getTag(); 
			
			 viewHolder.getClass().getMethod("mappingData",Object.class).invoke(viewHolder,_group);

			 
		} catch (Exception e) {
			// TODO: handle exception
		}				
		return view;
	}
	 
	@SuppressWarnings("unchecked")
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		
		V viewHolder = null; 
		final ViewGroup _parent;
		try 
		{			
			
			_parent=parent;
			ExpandListChild child = (ExpandListChild) getChild(groupPosition, childPosition);
			if (view == null) 
			{ 
				 
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(childlayout.getLayoutid(), null); 
				viewHolder=(V) (childlayout.getViewHoder().newInstance()); 
				invokeView(view,viewHolder);    		
				view.setTag(viewHolder);	
				
				if(view.isSelected()) 
					view.setBackgroundDrawable(context.getResources().getDrawable(R.color.LighBlueMarine)); 
				else
					view.setBackgroundDrawable(context.getResources().getDrawable(R.color.Terracota));
				
				
			} 
			else
				viewHolder=(V)view.getTag();
			
			viewHolder.getClass().getMethod("mappingData",Object.class).invoke(viewHolder,child);
			grouposition=groupPosition;
			childposition=childPosition;
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
		return true;
	}

	public void setSelectedPosition(int position) {
		
	}
	 

}
