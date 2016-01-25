package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.serviceproxy.DevolucionProducto;
import com.panzyma.nm.serviceproxy.DevolucionProductoLote;
import com.panzyma.nm.view.ViewDevolucionEdit;
import com.panzyma.nm.view.adapter.ExpandListAdapter;
import com.panzyma.nm.view.adapter.ExpandListChild;
import com.panzyma.nm.view.adapter.ExpandListGroup;
import com.panzyma.nm.view.adapter.SetViewHolderWLayout;
import com.panzyma.nm.view.viewholder.ProductoLoteDetalleViewHolder;
import com.panzyma.nm.view.viewholder.ProductoLoteViewHolder;
import com.panzyma.nm.viewdialog.DevolucionProductoCantidad.escucharModificacionProductoLote;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

@SuppressWarnings({ "rawtypes", "unused", "unchecked", "deprecation" })
@SuppressLint("InflateParams")
public class ProductoDevolucion extends DialogFragment 
{
	Handler.Callback parent;
	public static ProductoDevolucion pd;
	List<DevolucionProducto> dev_prod;
	private ExpandableListView lvdevproducto; 
	private ExpandListAdapter adapter;
	protected int[] childpositioncache = new int[2];
	protected ExpandListGroup dvselected;
	private ExpandListChild childselected;

	public static ProductoDevolucion newInstance(Handler.Callback _parent,
			List<DevolucionProducto> dev_prod) {
		if (pd == null)
			pd = new ProductoDevolucion(_parent, dev_prod);
		return pd;
	}

	public ProductoDevolucion() {
	};

	public ProductoDevolucion(Handler.Callback _parent,
			List<DevolucionProducto> _dev_prod) {
		parent = _parent;
		dev_prod = _dev_prod;
	};

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int visible = View.VISIBLE;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.productodevolucion, null);
		lvdevproducto = (ExpandableListView) view.findViewById(R.id.ExpList);
		builder.setView(view);
		initComponent();
		return builder.create();
	}

	
	 @Override
		public void onDismiss(DialogInterface dialog) { 
	    	NMApp.getController().setView(parent); 
			super.onDismiss(dialog);
		}
	
	public void initComponent() {
		if (adapter == null) 
		{
			ArrayList<SetViewHolderWLayout> layouts = new ArrayList<SetViewHolderWLayout>();
			layouts.add(new SetViewHolderWLayout(R.layout.detalle_productolote,
					ProductoLoteViewHolder.class, true,0));
			layouts.add(new SetViewHolderWLayout(R.layout.detalle_loteproducto,
					ProductoLoteDetalleViewHolder.class, false,0));
			try 
			{
				adapter = new ExpandListAdapter(((ViewDevolucionEdit) parent),SetStandardGroups(), layouts);
				lvdevproducto.setAdapter(adapter);
				lvdevproducto.expandGroup(0);
				lvdevproducto.expandGroup(1);
				lvdevproducto
						.setOnChildClickListener(new OnChildClickListener() {

							@Override
							public boolean onChildClick(
									ExpandableListView _parent, View v,
									int groupPosition, int childPosition,
									long id) {
								int flatpost;
								int ajustPos;
								if (_parent == null)
									return false;

								if (childpositioncache != null
										&& childpositioncache.length != 0) {
									long value = ExpandableListView
											.getPackedPositionForChild(
													childpositioncache[0],
													childpositioncache[1]);
									flatpost = _parent
											.getFlatListPosition(value);
									ajustPos = flatpost
											- _parent.getFirstVisiblePosition();
									View oldview = _parent.getChildAt(ajustPos);
									((ProductoLoteDetalleViewHolder) oldview
											.getTag()).tboxlote.getText();
									if (oldview != null
											&& oldview.getTag() != null
											&& oldview.getTag() instanceof ProductoLoteDetalleViewHolder)
										oldview.setBackgroundDrawable(((ViewDevolucionEdit) parent)
												.getResources().getDrawable(
														R.color.Terracota));
									((ProductoLoteDetalleViewHolder) oldview
											.getTag()).tboxlote.getText();
								}
								v.setSelected(true);
								v.setBackgroundDrawable(((ViewDevolucionEdit) parent)
										.getResources().getDrawable(
												R.color.LighBlueMarine));
								childpositioncache[0] = groupPosition;
								childpositioncache[1] = childPosition;
								
								childselected=(ExpandListChild) adapter.getChild(childpositioncache[0], childpositioncache[1]);
								
								return true;
							}
						});

				lvdevproducto.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(
									AdapterView<?> _parent, View view,
									int position, long id) 
							{
								ExpandableListView elv;
								if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
								{

									if (view.getTag() instanceof ProductoLoteDetalleViewHolder) 
									{

										elv = (ExpandableListView) _parent;
										ProductoLoteDetalleViewHolder pld = (ProductoLoteDetalleViewHolder) view.getTag();

										int flatpost;
										int ajustPos;
										if (elv == null)
											return false;

										if (childpositioncache != null && childpositioncache.length != 0) 
										{
											long value = ExpandableListView
													.getPackedPositionForChild(
															childpositioncache[0],
															childpositioncache[1]);
											flatpost = elv
													.getFlatListPosition(value);
											ajustPos = flatpost - elv.getFirstVisiblePosition();
											View oldview = elv.getChildAt(ajustPos);
											((ProductoLoteDetalleViewHolder) oldview
													.getTag()).tboxlote
													.getText();
											if (oldview != null
													&& oldview.getTag() != null
													&& oldview.getTag() instanceof ProductoLoteDetalleViewHolder)
												oldview.setBackgroundDrawable(((ViewDevolucionEdit) parent)
														.getResources()
														.getDrawable(
																R.color.Terracota));

										}
										view.setSelected(true);
										view.setBackgroundDrawable(((ViewDevolucionEdit) parent)
												.getResources().getDrawable(
														R.color.LighBlueMarine));

										childpositioncache[0] = ExpandableListView.getPackedPositionGroup(id);
										childpositioncache[1] = ExpandableListView.getPackedPositionChild(id);
										 
										childselected=(ExpandListChild) adapter.getChild(childpositioncache[0], childpositioncache[1]);
										ExpandListGroup groupselected=(ExpandListGroup) adapter.getGroup(childpositioncache[0]);
										EditarProductoLote(groupselected.getName(),childselected);
										
									}

									return true;
								}
								return false;
							}
						});

			} catch (Exception e) {
				// TODO: handle exception
			}
		} else
			adapter.notifyDataSetChanged();
	}
	
	private void EditarProductoLote(String productname,ExpandListChild _childselected) { 
		FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
		android.support.v4.app.Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialogNotaRecibo");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DevolucionProductoCantidad newFragment = DevolucionProductoCantidad.newInstance(productname,(DevolucionProductoLote) _childselected.getObject());
		newFragment.obtenerProductoLoteModificado(new escucharModificacionProductoLote() {
			
			@Override
			public void onButtonClick(DevolucionProductoLote plote) { 
				DevolucionProductoLote lote=plote;
			}
		});
		newFragment.show(ft, "dialogDevolucionProductoCantidad");
	}
	 
	public List<ExpandListGroup> SetStandardGroups() 
	{
		List<ExpandListGroup> lgroups = new ArrayList<ExpandListGroup>();
		
		LinkedList<ExpandListChild> groupchild;

		for (DevolucionProducto dp : dev_prod) 
		{
			ExpandListGroup group = new ExpandListGroup();
			groupchild = new LinkedList<ExpandListChild>();
			group.setName(dp.getNombreProducto());
			group.setObject(dp);
			for (DevolucionProductoLote dpl : dp.getProductoLotes()) 
			{
				ExpandListChild ch = new ExpandListChild();
				ch.setName(dpl.getNumeroLote());
				ch.setObject(dpl);
				groupchild.add(ch);
			}
			group.setItems(groupchild);
			lgroups.add(group);
		}
		return lgroups;
	}

}
