package com.panzyma.nm.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BCobroM.Accion;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.CCobro;
import com.panzyma.nm.serviceproxy.CFormaPago;
import com.panzyma.nm.serviceproxy.CobroDetalle;
import com.panzyma.nm.view.adapter.ExpandListAdapter;
import com.panzyma.nm.view.adapter.ExpandListAdapter.OnGroupFinished;
import com.panzyma.nm.view.adapter.ExpandListChild;
import com.panzyma.nm.view.adapter.ExpandListGroup;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.adapter.SetViewHolderWLayout;
import com.panzyma.nm.view.viewholder.CobroViewHolder;
import com.panzyma.nm.view.viewholder.ListGroupHolder;
import com.panzyma.nm.view.viewholder.PagoViewHolder;
import com.panzyma.nordismobile.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

@InvokeBridge(bridgeName = "BCobroM")
public class consultaCobroFragment extends Fragment implements Handler.Callback {
	
	public String TAG=consultaCobroFragment.class.getSimpleName();
	private ProgressDialog waiting;
	private List<CCobro> cobros = null;
	ArrayList<CFormaPago> pagos = null;
	String titulo = "VENTAS";
	private GenericAdapter adapter = null;
	private ExpandListAdapter adapter3 = null;
	private GenericAdapter adapter2 = null;
	private ListView listaGenerica;
	private ListView listapagos;
	private TextView txtenty;
	private EditText txtFiltro;
	private TextView headerGrid;
	private TextView gridheader;
	private TextView txtview_entry;
	android.support.v7.app.ActionBar bar;
	private Button btnMenu;
	private QuickAction quickAction;
	private Display display;
	private static final int MOSTRAR_COBROS_DEL_DIA = 1;
	private static final int MOSTRAR_COBROS_SEMANA = 2;
	private static final int MOSTRAR_COBROS_MES = 3;
	private static final int IMPRIMIR = 6;
	private ActionMenu menuSelected;
	TextView txt_footer ;
	TextView txt_footer2 ;
	private ExpandableListView expItems;
	List<ExpandListGroup> lgroups = new LinkedList<ExpandListGroup>();
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	View ccf =null;
	
	public enum ActionMenu {
		COBROS_DIA, COBROS_SEMANA, COBROS_MES, IMPRIMIR 
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		
//		outState.putInt(ARG_POSITION, mCurrentPosition); 
//		outState.putParcelable("cliente", customer);
//		outState.putLong(ARG_SUCURSAL, sucursalID);
//		outState.putParcelable("detailcustomer", DetailCustomerSelected);
//		outState.putParcelable("cobros", (Parcelable) cobros);
//		outState.putParcelable("pagos", (Parcelable) pagos);
//		
		 Parcelable [] objects = new Parcelable[cobros.size()];
		 cobros.toArray(objects);
		 outState.putParcelableArray("cobros", objects);
		 
		 Parcelable [] objects2 = new Parcelable[pagos.size()];
		 pagos.toArray(objects2);
		 outState.putParcelableArray("pagos", objects2);
		 
		 
		 super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");
	}	
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
		 if (savedInstanceState != null) 
		    {
		    	 Parcelable [] objects = savedInstanceState.getParcelableArray("cobros");
		    	 cobros = new ArrayList<CCobro>( (Collection<? extends CCobro>) Arrays.asList(objects) ); 
		    	 
		    	 Parcelable [] objects2 = savedInstanceState.getParcelableArray("pagos");
		    	 pagos = new ArrayList<CFormaPago>( (Collection<? extends CFormaPago>) Arrays.asList(objects2) ); 
		    }
	}

//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) 
//	{
//	    super.onActivityCreated(savedInstanceState);
//	    if (savedInstanceState != null) 
//	    {
//	    	 Parcelable [] objects = savedInstanceState.getParcelableArray("cobros");
//	    	 cobros = new ArrayList<CCobro>( (Collection<? extends CCobro>) Arrays.asList(objects) ); 
//	    	 
//	    	 Parcelable [] objects2 = savedInstanceState.getParcelableArray("pagos");
//	    	 pagos = new ArrayList<CFormaPago>( (Collection<? extends CFormaPago>) Arrays.asList(objects2) ); 
//	    }
//	
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.cobro, container, false);
	}
	

	@Override
	public void onStart() {
		super.onStart();
		//getActivity().getFragmentManager().findFragmentById(R.id.)
		NMApp.controller.setView(this); 
		SessionManager.setContext(getActivity());
		initComponent();
		menuSelected = ActionMenu.COBROS_DIA;
		if(cobros==null){
			CargarCobros(Accion.COBROS_DEL_DIA.getActionCode());	
		}
		
	}
	
//	@Override
//	public void onDetach ()
//	{
//		Log.d(TAG, "OnDetach");
//		NMApp.getController().setView((Callback)getActivity()); 
//		super.onDetach();
//	}
//	
//	@Override
//    public void onStop() {
//        super.onStop(); 
//		NMApp.getController().setView((Callback)getActivity());  
//        Log.d(TAG, "onStop");
//    }
	
	private void CargarCobros(int opt){
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Obteniendo Información ...", true, false);
			
			Message msg = new Message(); 
			msg.what = opt ; 
			NMApp.getController().getInboxHandler().sendMessage(msg); 
		}
		catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	
	private void CargarPagos(int opt){
		try {
			Message msg = new Message(); 
			msg.what = opt; //Accion.PAGOS_DEL_MES.getActionCode();
			NMApp.getController().getInboxHandler().sendMessage(msg); 
		}
		catch (Exception e) {
				e.printStackTrace();
		}
	}


	@Override
	public boolean handleMessage(Message msg) {
		if( msg.what < 6) 
		{
			Accion response = Accion.toInt(msg.what);
			
			switch (response) {
				case COBROS_DEL_DIA: 
				case COBROS_DEL_SEMANA:
				case COBROS_DEL_MES:
					System.out.print("YA LLego");
					if(cobros!=null ) cobros.clear();
					cobros = (ArrayList<CCobro>)msg.obj;
					 CargarPagos(response.getActionCode()+3);
					break;
				case PAGOS_DEL_DIA :
				case PAGOS_DE_SEMANA :
				case PAGOS_DEL_MES :
					if(pagos!=null )pagos.clear();
					pagos = (ArrayList<CFormaPago>)msg.obj;
					 MostrarCobros();
					break;
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void MostrarCobros(){
		if (waiting != null) waiting.dismiss();

		
		ArrayList<SetViewHolderWLayout> layouts = new ArrayList<SetViewHolderWLayout>();
		layouts.add(new SetViewHolderWLayout(R.layout.list_group, ListGroupHolder.class, true,0));
		layouts.add(new SetViewHolderWLayout(R.layout.detalle_cobro, CobroViewHolder.class, false,0));
		layouts.add(new SetViewHolderWLayout(R.layout.detalle_pago, PagoViewHolder.class, false,0));
//		
		    
		adapter3 = new ExpandListAdapter(getActivity(), SetStandardGroups() , 
					new OnGroupFinished() {

					@Override
					public void onFinish(int grouposition, TextView footertitle ,TextView footer) {
						if(footer!=null){
							if(grouposition==0)
							   footer.setText(StringUtil.formatReal(getTotalCobros()));
							else
								footer.setText(StringUtil.formatReal(getTotalPagos()));
						}
					}

		}, layouts);
 
		expItems.setAdapter(adapter3);


		for (int g = 0; g < adapter3.getGroupCount(); g++) {
			expItems.expandGroup(g);
		}
	

		titulo = "MOSTRANDO COBRO ";
		if( menuSelected == ActionMenu.COBROS_DIA ) titulo += " DEL DIA ";
		else if( menuSelected == ActionMenu.COBROS_SEMANA ) titulo  += " DE LA SEMANA ";
		else if( menuSelected == ActionMenu.COBROS_MES ) titulo  += " DEL MES ";
	
		gridheader.setText(titulo + " (" + cobros.size() + ")");

	}
	
	private void initComponent(){
		
		expItems= (ExpandableListView) getActivity().findViewById(R.id.ExpList);
		
		txtenty = (TextView) getActivity().findViewById(R.id.ctxtview_enty);
		//headerGrid = (TextView) getActivity().findViewById(R.id.cxctextv_header2);
		btnMenu = (Button)getActivity().findViewById(R.id.btnMenu);
		//txtFiltro = (EditText) getActivity().findViewById(R.id.cxctextv_filtro);
		gridheader = (TextView)  getActivity().findViewById(R.id.ctextv_gridheader);

		if(txtFiltro!=null) {
			txtFiltro.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					List<CCobro> _cobros = cobros;
					List<CCobro> filtercobros = new ArrayList<CCobro>();
					//LIMPIAR LOS VENTAS FILTRADAS
					filtercobros.clear();
					
					if (_cobros.size() > 0){
						String query = s.toString();
						for(CCobro cobro : _cobros) {
							if( cobro.getNumeroCentral().contains(query)  || cobro.getNombreCliente().contains(query)|| cobro.getFecha().contains(query)){
								filtercobros.add(cobro);
							}
						}					
					}		
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
				@Override
				public void afterTextChanged(Editable s) {}
			});
		}

		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		initMenu();
	}
	
	
	public void mostrarMenu() {		
		quickAction.show(btnMenu, display, true);
	} 
	
	private void initMenu() {
		quickAction = new QuickAction(this.getActivity(), QuickAction.VERTICAL, 1);
		quickAction.addActionItem(new ActionItem(MOSTRAR_COBROS_DEL_DIA, "Mostrar Ventas del Día"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_COBROS_SEMANA, "Mostrar Ventas de la Semana"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_COBROS_MES, "Mostrar Ventas del Mes"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(IMPRIMIR, "Imprimir"));			
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, final int pos,
					int actionId) {

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ActionItem actionItem = quickAction.getActionItem(pos);

						switch (actionItem.getActionId()) {
						case MOSTRAR_COBROS_DEL_DIA:
							menuSelected = ActionMenu.COBROS_DIA;
							CargarCobros(Accion.COBROS_DEL_DIA.getActionCode());
							break;
						case MOSTRAR_COBROS_SEMANA:
							menuSelected = ActionMenu.COBROS_SEMANA;
							CargarCobros(Accion.COBROS_DEL_SEMANA.getActionCode());
							break;
						case MOSTRAR_COBROS_MES:
							menuSelected = ActionMenu.COBROS_MES;
							CargarCobros(Accion.COBROS_DEL_MES.getActionCode());
							break;
						case IMPRIMIR:
							menuSelected = ActionMenu.IMPRIMIR; 
							Imprimir(Accion.IMPRIMIR.getActionCode()); 
							break;							
						}
					}
				});

					}

				});
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
				quickAction.dismiss();
			}
		});

	}

	private float getTotalPagos(){
		float total = 0;
		for(CFormaPago item : pagos){
			total+= item.getMonto();
		}
		return total;
	}
	
	private float getTotalCobros(){
		float total = 0;
		for(CCobro item : cobros){
			total+= item.getTotalRecibo();
		}
		return total;
	}
	
	public List<ExpandListGroup> SetStandardGroups() {

		LinkedList<ExpandListGroup> _lgroups = new LinkedList<ExpandListGroup>();
		LinkedList<ExpandListChild> groupchild = null ;
		
		ExpandListGroup group = new ExpandListGroup();
		group.setName("RECIBOS");
		group.setPosition(0); // 0
		groupchild = new LinkedList<ExpandListChild>();

		for( CCobro item : cobros  ){
			ExpandListChild ch = new ExpandListChild();
			ch.setName(item.getNombreCliente());
			ch.setObject(item);
			ch.setParentposition(0);
			groupchild.add(ch);		
		}
		
		group.setItems(groupchild);
		_lgroups.add(group);
		
		/********** PAGOS *******************/
		ExpandListGroup group2 = new ExpandListGroup();
		group2.setPosition(0); // 1
		group2.setName("FORMAS DE PAGO");
		
		groupchild = new LinkedList<ExpandListChild>();
		
		for(CFormaPago item : pagos ){
			ExpandListChild ch = new ExpandListChild();
			ch.setName(item.getCodFormaPago());
			ch.setObject(item);
			ch.setParentposition(1);
			groupchild.add(ch); 
			
		}
		
		group2.setItems(groupchild);
		_lgroups.add(group2);

		
		return _lgroups;
	}

	private void Imprimir(int opt){
//		if(cobros.size() == 0){
//			AppDialog.showMessage(getActivity(),"Aviso","No hay datos que imprimir.",DialogType.DIALOGO_ALERTA);
//			return ;
//		}
		Message msg = new Message(); 
		msg.what = opt ;
		msg.obj = new CobroDetalle(cobros,pagos);
		NMApp.getController().getInboxHandler().sendMessage(msg); 
	}
}
