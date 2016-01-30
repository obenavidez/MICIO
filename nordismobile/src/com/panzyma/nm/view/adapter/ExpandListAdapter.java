package com.panzyma.nm.view.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.panzyma.nm.auxiliar.AppDialog.OnDismissDialogListener;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class ExpandListAdapter<E, V> extends BaseExpandableListAdapter {

	private Context context; 
	private List<E> groups;//List<E> items;	
	private ArrayList<SetViewHolderWLayout> holderloyout;//layoutchild  
	SetViewHolderWLayout parentlayout; 
	SetViewHolderWLayout childlayout; 
	Map achildlayout;
	int grouposition = -1;
	int childposition; 
	private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    ArrayList<ParentChildPosition> layouts=null;
    int groupPosition_cache =-1;
    private OnGroupFinished ongroupfinish;
    
    public ExpandListAdapter(Context _context, List<E> _groups,OnGroupFinished callback ,ArrayList<SetViewHolderWLayout> ... _holderloyout ) 
	{
		this.context = _context; 
		this.groups = _groups;
		this.holderloyout=_holderloyout[0] ;
		parentlayout = null;
		layouts = null;
		getParentView(); 
		this.ongroupfinish = callback;
	} 
    
    
	public ExpandListAdapter(Context _context, List<E> _groups,ArrayList<SetViewHolderWLayout> ... _holderloyout) 
	{
		this.context = _context; 
		this.groups = _groups;
		this.holderloyout=_holderloyout[0] ;
		parentlayout = null;
		layouts = null;
		getParentView(); 
	}    
	
	
	public Object getChild(int groupPosition) {
		// TODO Auto-generated method stub
		LinkedList<ExpandListChild> chList = ((ExpandListGroup)groups.get(groupPosition)).getItems();
		return chList;
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
	
 
	@Override
	@SuppressLint("ShowToast") @SuppressWarnings("unchecked")
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) 
	{
		V viewHolder = null; 
		try 
		{
			ExpandListGroup _group = (ExpandListGroup) getGroup(groupPosition);

			if (view == null) { 
				LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	 
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		
		V viewHolder = null; 
		final ViewGroup _parent;
		try 
		{			
			_parent=parent;
			ExpandListChild child = (ExpandListChild) getChild(groupPosition, childPosition);
			SetViewHolderWLayout layout= (SetViewHolderWLayout) layouts.get(parentlayout.getParentposition()).getItem(groupPosition);
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(layout.getLayoutid(), null); 
			viewHolder=(V) (layout.getViewHoder().newInstance()); 
			invokeView(view,viewHolder);    		
			view.setTag(viewHolder);	

			if(view.isSelected()) 
				view.setBackgroundDrawable(context.getResources().getDrawable(R.color.LighBlueMarine)); 
			else
				view.setBackgroundDrawable(context.getResources().getDrawable(R.color.White));

			viewHolder=(V)view.getTag();
			
			viewHolder.getClass().getMethod("mappingData",Object.class).invoke(viewHolder,child);
			grouposition=groupPosition;
			childposition=childPosition;
			
			if(isLastChild ){
				TableRow row =(TableRow)view.findViewById(R.id.fila_footer);
				if(row!=null){ 
					row.setVisibility(View.VISIBLE);
					TextView footer = (TextView)(row.findViewById(R.id.footer_txt));
					if(footer!=null) {
						if(ongroupfinish!=null){
							ongroupfinish.onFinish(grouposition, footer,footer);
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}				
		return view;
	}
	
	public SetViewHolderWLayout getParentView()
	{
		if( parentlayout==null)
		{
			layouts=new ArrayList<ParentChildPosition>();
			
			for(SetViewHolderWLayout hl:holderloyout)
			{
				if(hl.isParent())
				{
					ParentChildPosition obj=new ParentChildPosition();
					obj.setAprent(hl);
				    obj.setAchild(getChildView(hl));
				    layouts.add(obj);
					parentlayout=hl;
					break;
				}else{
					
				}
			}
		} 
		return parentlayout; 
	}
	
	public ArrayList<SetViewHolderWLayout> getChildView(SetViewHolderWLayout parent)
	{ 
		ArrayList<SetViewHolderWLayout> rs = null;
		rs=new ArrayList<SetViewHolderWLayout>();
		for(SetViewHolderWLayout hl:holderloyout)
		{
			
			if(!hl.isParent() && hl.getParentposition()==parent.getParentposition())			
			{ 
				rs.add(hl);
			}
		} 
		return rs; 
	}
 
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setSelectedPosition(int position) {
		
	}
	 
	
	
	class ParentChildPosition
	{
		ParentChildPosition(){
			
		}
		public ParentChildPosition(SetViewHolderWLayout aprent, ArrayList<SetViewHolderWLayout> achild) {
			super();
			this.aprent = aprent;
			this.achild = achild;
		}
		public SetViewHolderWLayout getAprent() {
			return aprent;
		}
		public void setAprent(SetViewHolderWLayout aprent) {
			this.aprent = aprent;
		}
		public ArrayList<SetViewHolderWLayout> getAchild() {
			return achild;
		}
		public void setAchild(ArrayList<SetViewHolderWLayout> achild) {
			this.achild = achild;
		}
		public SetViewHolderWLayout getItem(int position){
			return achild.get(position);
		}
		private SetViewHolderWLayout aprent;
		private ArrayList<SetViewHolderWLayout> achild;
	}
	
	public void updateData(List<E> groups) {
	    this.groups = groups;
	}


	
	public interface OnGroupFinished {
		public abstract void onFinish(int grouposition ,TextView footertitle,TextView footer);
	}
	
}
