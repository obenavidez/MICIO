package com.panzyma.nm.fragments;

import java.util.ArrayList;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BLogicM;
import com.panzyma.nm.CBridgeM.BVentaM;
import com.panzyma.nm.CBridgeM.BVentaM.Petition;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.CVenta;
import com.panzyma.nm.serviceproxy.Factura;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.FacturaViewHolder;
import com.panzyma.nm.view.viewholder.VentaViewHolder;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ConsultaVentasFragment extends Fragment implements
		Handler.Callback {

	public enum ActionMenu {
		VENTAS_DIA, VENTAS_SEMANA, VENTAS_MES, IMPRIMIR
	}

	public final static String ARG_POSITION = "position";
	public final static String SUCURSAL_ID = "sucursalID";
	private static final int MOSTRAR_VENTAS_DEL_DIA = 1;
	private static final int MOSTRAR_VENTAS_SEMANA = 2;
	private static final int MOSTRAR_VENTAS_MES = 3;
	private static final int IMPRIMIR = 4;

	private boolean delDia = false;
	private boolean deSemana = true;
	private boolean delMes = false;
	private int fechaInic = 0;
	private int fechaFin = 0;
	private int mCurrentPosition = -1;
	private long objSucursalID;	
	private ListView listaGenerica;
	private TextView txtenty;
	private TextView headerGrid;
	private QuickAction quickAction;
	private Button btnMenu;
	private Display display;
	private ActionMenu menuSelected;
	
	private Context fcontext = null;
	private GenericAdapter adapter = null;
	private NMApp nmapp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			objSucursalID = savedInstanceState.getLong(SUCURSAL_ID);
		}
		return inflater.inflate(R.layout.ventas, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		initComponents();
		if (args != null) {
			objSucursalID = args.getLong(SUCURSAL_ID);
			mCurrentPosition = args.getInt(ARG_POSITION);
			cargarVentas();
		} else if (mCurrentPosition != -1) {
			cargarVentas();
		}
	}

	private void cargarVentas() {		
		
	}

	private void initComponents() {
		Activity actividad = getActivity();
		listaGenerica = (ListView) actividad.findViewById(R.id.cxclvgeneric);
		txtenty = (TextView) actividad.findViewById(R.id.ctxtview_enty);
		headerGrid = (TextView) actividad.findViewById(R.id.cxctextv_header2);
		btnMenu = (Button)actividad.findViewById(R.id.btnMenu);
		
		WindowManager wm = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		
		initMenu();
	}
	
	public void mostrarMenu() {		
		quickAction.show(btnMenu, display, true);
	} 

	//LLAMA AL SERVICIO WEB PARA TRAER LAS VENTAS DEL DIA DEL SERVIDOR PANZYMA
	public void cargarVentasDelDia() {
		try {
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(BVentaM.class.toString());
			nmapp.getController().setEntities(this, new BVentaM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(Petition.VENTAS_DEL_DIA.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LAS VENTAS DE LA SEMANA DEL SERVIDOR PANZYMA
	public void cargarVentasDeSemana() {
		try {
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(BVentaM.class.toString());
			nmapp.getController().setEntities(this, new BVentaM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							Petition.VENTAS_DEL_SEMANA.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LAS VENTAS DEL MES DEL SERVIDOR PANZYMA
	public void cargarVentasDeMes() {
		try {
			nmapp = (NMApp) this.getActivity().getApplication();
			nmapp.getController().removebridgeByName(BVentaM.class.toString());
			nmapp.getController().setEntities(this, new BVentaM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							Petition.VENTAS_DEL_MES.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarVentas(ArrayList<CVenta> ventas){
		
		String title = "Ventas %s ";
		if( menuSelected == ActionMenu.VENTAS_DIA ) title = String.format(title, "del día ");
		else if( menuSelected == ActionMenu.VENTAS_SEMANA ) title = String.format(title, "de semana ");
		else if( menuSelected == ActionMenu.VENTAS_MES ) title = String.format(title, "del mes ");
		
		title += "(%s)"; 
		
		if (ventas != null && ventas.size() > 0) {
			adapter = new GenericAdapter<CVenta, VentaViewHolder>(
					this.getActivity().getApplicationContext(),
					VentaViewHolder.class,
					ventas,
					R.layout.detalle_venta);			
			txtenty.setVisibility(View.INVISIBLE);
			headerGrid.setText(String.format(title, ventas.size()));
			listaGenerica.setAdapter(adapter);
		} else {
			headerGrid.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			adapter.clearItems();
			adapter.notifyDataSetChanged();
		}
	} 
	
	private void initMenu() {
		quickAction = new QuickAction(this.getActivity(), QuickAction.VERTICAL, 1);
		quickAction.addActionItem(new ActionItem(MOSTRAR_VENTAS_DEL_DIA,
				"Mostrar Ventas del Día"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_VENTAS_SEMANA,
				"Mostrar VEntas de la Semana"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_VENTAS_MES,
				"Mostrar Ventas del Mes"));
		quickAction.addActionItem(null);
		quickAction.addActionItem(new ActionItem(IMPRIMIR,
				"Imprimir"));			

		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, final int pos,
							int actionId) {

						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ActionItem actionItem = quickAction.getActionItem(pos);
								
								switch (actionItem.getActionId()) {
								case MOSTRAR_VENTAS_DEL_DIA:
									menuSelected = ActionMenu.VENTAS_DIA;
									cargarVentasDelDia();
									break;
								case MOSTRAR_VENTAS_SEMANA:
									menuSelected = ActionMenu.VENTAS_SEMANA;
									cargarVentasDeSemana();
									break;
								case MOSTRAR_VENTAS_MES:
									menuSelected = ActionMenu.VENTAS_MES;
									cargarVentasDeMes();
									break;
								case IMPRIMIR:
									menuSelected = ActionMenu.IMPRIMIR;
									
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
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		Petition response = Petition.toInt(msg.what);
		switch (response) {
		case VENTAS_DEL_DIA:
		case VENTAS_DEL_SEMANA:
		case VENTAS_DEL_MES:
			mostrarVentas(((ArrayList<CVenta>)msg.obj));
			break;		
		}
		return false;
	}

	public boolean isDelDia() {
		return delDia;
	}

	public void setDelDia(boolean delDia) {
		this.delDia = delDia;
	}

	public boolean isDeSemana() {
		return deSemana;
	}

	public void setDeSemana(boolean deSemana) {
		this.deSemana = deSemana;
	}

	public boolean isDelMes() {
		return delMes;
	}

	public void setDelMes(boolean delMes) {
		this.delMes = delMes;
	}

	public int getFechaInic() {
		return fechaInic;
	}

	public void setFechaInic(int fechaInic) {
		this.fechaInic = fechaInic;
	}

	public int getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(int fechaFin) {
		this.fechaFin = fechaFin;
	}

}
