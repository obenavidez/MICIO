package com.panzyma.nm.fragments;

import java.util.Arrays;

import com.panzyma.nordismobile.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.panzyma.nm.view.ViewCliente;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.CNotaViewHolder;
import com.panzyma.nm.viewmodel.vmFicha;

@SuppressWarnings({"static-access","unused", "rawtypes" })
public class FichaClienteFragment extends Fragment {
	
	public final static String ARG_POSITION = "position";
	public final static String OBJECT = "cliente";
	int mCurrentPosition = -1;
	private vmFicha customer;
	private Context fcontext;  
	private GenericAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			customer = (vmFicha) savedInstanceState.getParcelable(OBJECT);
		}
		// Inflate the layout for this fragment		
		return inflater.inflate(R.layout.ficha_cliente, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		// During startup, check if there are arguments passed to the fragment.
		// onStart is a good place to do this because the layout has already
		// been
		// applied to the fragment at this point so we can safely call the
		// method
		// below that sets the article text.
		Bundle args = getArguments();
		if (args != null) {
			// Set article based on argument passed in
//			updateArticleView(args.getInt(ARG_POSITION));
			customer = (vmFicha) args.getParcelable(OBJECT);
			mCurrentPosition = args.getInt(ARG_POSITION);
			this.fcontext =getActivity().getApplicationContext();
			updateArticleView(mCurrentPosition ,customer);
			
		} else if (mCurrentPosition != -1) {
			// Set article based on saved instance state defined during
			// onCreateView
			updateArticleView(mCurrentPosition);
		}
	}

	public void updateArticleView(int position) {
		//R.id.article
		/*
		TextView codigoCliente = (TextView) getActivity().findViewById(R.id.gc_clientecod);
		TextView nombreCliente = (TextView) getActivity().findViewById(R.id.gc_clientenom);
		TextView sucursalCliente = (TextView) getActivity().findViewById(R.id.gc_clienteubi);
		
		codigoCliente.setText( Contenido.lista.get(position).getCodigo());
		nombreCliente.setText(Contenido.lista.get(position).getNombreCliente());
		sucursalCliente.setText(Contenido.lista.get(position).getUbicacion());
		*/
		mCurrentPosition = position;
	}

	public void updateArticleView(int position ,vmFicha obj) {

		((TextView) getActivity().findViewById(R.id.fctextv_detallecliente)).setText(obj.getNombreCliente());
		((TextView) getActivity().findViewById(R.id.fctextv_detallesucursal)).setText(obj.getNombreSucursal());
		((TextView) getActivity().findViewById(R.id.fctextv_detalledireccion)).setText(obj.getDireccionSucursal());
		((TextView) getActivity().findViewById(R.id.fctextv_detalletelefono)).setText(obj.getTelefono());
		((TextView) getActivity().findViewById(R.id.fctextv_detalletipo)).setText(obj.getTipo()); 
		((TextView) getActivity().findViewById(R.id.fctextv_detallecategoria)).setText(obj.getCategoria());
		((TextView) getActivity().findViewById(R.id.fctextv_detalleprecioventa)).setText(obj.getPrecioVenta());
		((TextView) getActivity().findViewById(R.id.fctextv_detallelimitecredito)).setText(String.valueOf(obj.getLimiteCredito())); 
		((TextView) getActivity().findViewById(R.id.fctextv_detalleplazocredito)).setText((String.valueOf(obj.getPlazoCredito())));
		((TextView) getActivity().findViewById(R.id.fctextv_detalleplazodescuento)).setText(String.valueOf(obj.getPlazoDescuento()));
		((TextView) getActivity().findViewById(R.id.fctextv_detalleminabono)).setText(String.valueOf(obj.getMontoMinimoAbono()));
		((TextView) getActivity().findViewById(R.id.fctextv_detalledescuento)).setText(obj.getDescuentos());  
		
		TextView gridheader=(TextView) getActivity().findViewById(R.id.fctextv_header2);
		TextView txtenty=(TextView) getActivity().findViewById(R.id.fctxtview_enty);
		ListView lvcnotas = (ListView) getActivity().findViewById(R.id.fclvnotas);
		
		if(obj.getNotas()!=null)
		{  
			String message = String.format("Notas del Cliente(%d)", obj.getNotas().length);
			gridheader.setText(message);
			adapter=new GenericAdapter(fcontext,CNotaViewHolder.class,Arrays.asList(obj.getNotas()),R.layout.grid_cnota);
			if(adapter!=null){
			lvcnotas.setAdapter(adapter);
			adapter.setSelectedPosition(position); }
			lvcnotas.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	            { 		  
	            	if((parent.getChildAt(mCurrentPosition))!=null)						            							            		
	            		(parent.getChildAt(mCurrentPosition)).setBackgroundResource(android.R.color.transparent);						            	 
	            	mCurrentPosition=position; 				
	            	adapter.setSelectedPosition(position);  
	            	view.setBackgroundDrawable(fcontext.getResources().getDrawable(R.drawable.action_item_selected));
	            }
	        }); 
	        
		}
		else
	    {
			gridheader.setText("Notas del Cliente(0)");
            txtenty.setVisibility(View.VISIBLE); 
            lvcnotas.setEmptyView(txtenty);  
	    } 
		

		mCurrentPosition = position;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current article selection in case we need to recreate the
		// fragment
		outState.putInt(ARG_POSITION, mCurrentPosition);
		outState.putParcelable(OBJECT, customer);
	}
}
