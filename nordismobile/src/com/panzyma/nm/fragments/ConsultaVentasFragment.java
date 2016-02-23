package com.panzyma.nm.fragments;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp; 
import com.panzyma.nm.CBridgeM.BVentaM.Petition;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.CVenta;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.view.viewholder.VentaViewHolder;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
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
import android.widget.ListView;
import android.widget.TextView;
@InvokeBridge(bridgeName = "BVentaM")
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

	private static CustomDialog dlg;
	private boolean delDia = false;
	private boolean deSemana = true;
	private boolean delMes = false;
	private int fechaInic = 0;
	private int fechaFin = 0;
	private int mCurrentPosition = -1;
	private long objSucursalID;	
	private ListView listaGenerica;
	private TextView txtenty;
	private EditText txtFiltro;
	private TextView headerGrid;
	private TextView gridheader;
	private QuickAction quickAction;
	private Button btnMenu;
	private Display display;
	private ActionMenu menuSelected;
	private ProgressDialog waiting;
	private List<CVenta> ventas = new ArrayList<CVenta>();
	
	private Context fcontext = null;
	private GenericAdapter adapter = null;
	private NMApp nmapp;

	public String TAG=ConsultaVentasFragment.class.getSimpleName();
	
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
		NMApp.controller.setView(this);
		SessionManager.setContext(getActivity());
		menuSelected = ActionMenu.VENTAS_DIA;
		initComponents();
		if (args != null) {
			objSucursalID = args.getLong(SUCURSAL_ID);
			mCurrentPosition = args.getInt(ARG_POSITION);
			cargarVentas();
		} else if (mCurrentPosition != -1) {
			cargarVentas();
		}
	}

	 
	
	@Override
	public void onDetach ()
	{
		Log.d(TAG, "OnDetach");
		NMApp.getController().setView((Callback)getActivity()); 
		super.onDetach();
	}
	
	@Override
    public void onStop() {
        super.onStop(); 
		NMApp.getController().setView((Callback)getActivity());  
        Log.d(TAG, "onStop");
    }
	 
	
	private void cargarVentas() {		
		cargarVentasDelDia();
	}

	private void initComponents() {
		Activity actividad = getActivity();
		listaGenerica = (ListView) actividad.findViewById(R.id.cxclvgeneric);
		txtenty = (TextView) actividad.findViewById(R.id.ctxtview_enty);
		headerGrid = (TextView) actividad.findViewById(R.id.cxctextv_header2);
		btnMenu = (Button)actividad.findViewById(R.id.btnMenu);
		txtFiltro = (EditText) actividad.findViewById(R.id.cxctextv_filtro);
		
		txtFiltro.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				List<CVenta> vntas = ventas;	
				List<CVenta> filterVentas = new ArrayList<CVenta>();
				//LIMPIAR LOS VENTAS FILTRADAS
				filterVentas.clear();
				if (vntas.size() > 0){
					String docNumerToFound = s.toString();
					for(CVenta venta : vntas) {
						if( venta.getNumeroCentral().contains(docNumerToFound) 
								|| venta.getNombreCliente().contains(docNumerToFound)
								|| venta.getFecha().contains(docNumerToFound)){
							filterVentas.add(venta);
						}
					}					
					gridheader.setText(String.format("VENTAS DEL DIA (%s)", filterVentas.size()));
					adapter.setItems(filterVentas);
					adapter.notifyDataSetChanged();
				}				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		gridheader = (TextView) actividad.findViewById(R.id.ctextv_gridheader);
		//gridheader.setVisibility(View.INVISIBLE);
		
		gridheader.setText("VENTAS DEL DIA (0)");
		
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
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo ventas Cliente...", true, false);
			NMApp.getController().getInboxHandler().sendEmptyMessage(Petition.VENTAS_DEL_DIA.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//LLAMA AL SERVICIO WEB PARA TRAER LAS VENTAS DE LA SEMANA DEL SERVIDOR PANZYMA
	public void cargarVentasDeSemana() {
		try {
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo ventas Cliente...", true, false);
			NMApp.getController()
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
			waiting = ProgressDialog.show(getActivity(), "Espere por favor", "Trayendo ventas Cliente...", true, false);
			NMApp.getController()
					.getInboxHandler()
					.sendEmptyMessage(
							Petition.VENTAS_DEL_MES.getActionCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarVentas(ArrayList<CVenta> ventas){
		
		String title = "VENTAS%s ";
		if( menuSelected == ActionMenu.VENTAS_DIA ) title = String.format(title, "DEL DIA ");
		else if( menuSelected == ActionMenu.VENTAS_SEMANA ) title = String.format(title, "DE SEMANA ");
		else if( menuSelected == ActionMenu.VENTAS_MES ) title = String.format(title, "DEL MES ");
		
		title += "(%s)"; 
		
		if (ventas != null && ventas.size() > 0) {
			this.ventas.clear();
			this.ventas = ventas;
			adapter = new GenericAdapter<CVenta, VentaViewHolder>(
					this.getActivity().getApplicationContext(),
					VentaViewHolder.class,
					ventas,
					R.layout.detalle_venta);			
			txtenty.setVisibility(View.INVISIBLE);
			gridheader.setText(String.format(title, ventas.size()));
			listaGenerica.setAdapter(adapter);
		} else {
			gridheader.setText(String.format(title,0));
			txtenty.setText("No existen registros");
			txtenty.setVisibility(View.VISIBLE);
			if(adapter != null) {
				adapter.clearItems();
				adapter.notifyDataSetChanged();
			}			
		}
		if (waiting != null)
			waiting.dismiss();
	} 
	
	private void initMenu() {
		quickAction = new QuickAction(this.getActivity(), QuickAction.VERTICAL, 1);
		quickAction.addActionItem(new ActionItem(MOSTRAR_VENTAS_DEL_DIA,
				"Mostrar Ventas del Día"));
		quickAction.addActionItem(new ActionItem(MOSTRAR_VENTAS_SEMANA,
				"Mostrar Ventas de la Semana"));
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
	public boolean handleMessage(Message msg) 
	{
		ocultarDialogos();
		if( msg.what < 3) {
			Petition response = Petition.toInt(msg.what);
			switch (response) {
			case VENTAS_DEL_DIA:
			case VENTAS_DEL_SEMANA:
			case VENTAS_DEL_MES:
				mostrarVentas(  ( msg.obj == null ? new ArrayList<CVenta>() :  ((ArrayList<CVenta>)msg.obj) ) );
				break;		
			}
		}
		switch (msg.what) 
		{
			case ERROR:
			ErrorMessage error = ((ErrorMessage) msg.obj);
			showStatus(error.getMessage()+ error.getCause(), true);  
			break;
		}
	
		
		
		
		return false;
	}

	public void ocultarDialogos() {
		if (dlg != null)
			dlg.dismiss();
		if(waiting!=null)
			waiting.dismiss();
	}
	
	public void showStatus(final String mensaje, boolean... confirmacion) {

		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(getActivity(), "", mensaje,
							AppDialog.DialogType.DIALOGO_ALERTA,
							new AppDialog.OnButtonClickListener() {
								@Override
								public void onButtonClick(AlertDialog _dialog,
										int actionId) {

									if (AppDialog.OK_BUTTOM == actionId) {
										_dialog.dismiss();
									}
								}
							});
				}
			});
		} else {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dlg = new CustomDialog(getActivity(), mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
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
