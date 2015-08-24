package com.panzyma.nm.view;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.viewmodel.vmCliente;
import com.panzyma.nm.viewmodel.vmDevolucion;
import com.panzyma.nordismobile.R;

import static com.panzyma.nm.controller.ControllerProtocol.*;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@InvokeBridge(bridgeName = "BDevolucionM")
public class ViewDevoluciones extends ActionBarActivity implements ListaFragment.OnItemSelectedListener, Handler.Callback {

	
	//VARIABLES
	public enum FragmentActive {
		LIST,EDIT
	};

	private static final int NUEVO_DEVOLUCION = 0;
	private static final int ABRIR_DEVOLUCION = 1;
	private static final int BORRAR_DEVOLUCION = 2;
	private static final int ENVIAR_DEVOLUCION = 3;
	private static final int IMPRIMIR_COMPROBANTE = 4;
	private static final int BORRAR_ENVIADAS = 5;
	private static final int FICHA_DEL_CLIENTE = 6;
	private static final int CUENTAS_POR_COBRAR = 7;
	private static final int CERRAR = 8;
	String[] opcionesMenu;
	CharSequence tituloSeccion;
	CharSequence tituloApp;
	
	private FragmentActive fragmentActive = null;
	
	//Controles
	DrawerLayout drawerLayout;
	ListView drawerList;
	TextView gridheader;
	TextView footerView;
	ListaFragment<vmDevolucion> firstFragment;
	ActionBarDrawerToggle drawerToggle;
	SearchView searchView;
	CustomArrayAdapter customArrayAdapter;
	vmDevolucion item_selected = null;
	ProgressDialog pDialog;
	List<vmDevolucion> devoluciones = new ArrayList<vmDevolucion>();
	int posicion ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Establecemos nuestras variables de entorno
		NMApp.getController().setView(this);
		SessionManager.setContext(this);
		UserSessionManager.setContext(this);
		
		setContentView(R.layout.layout_client_fragment);
		
		Render_Menu();
		
		Load_Data(LOAD_DATA_FROM_LOCALHOST);
		
		fragmentActive = FragmentActive.LIST;
		
		firstFragment = new ListaFragment<vmDevolucion>();
		
		
		//Si es Phone
		if (findViewById(R.id.fragment_container) != null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
		}
		
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		boolean result = false;
		ArrayList<vmDevolucion> list = null;
		
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
		
		switch (msg.what) {
		
			case C_DATA:
				list = (ArrayList<vmDevolucion>) ((msg.obj == null) ? new ArrayList<vmDevolucion>() : msg.obj);
				SetList(list);
			
			break;
		
		}
		return result ;
	}
	
	@Override
	public void onItemSelected(Object obj, int position) {
		item_selected = (vmDevolucion)obj;
		 posicion = position;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.mcliente, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		if (searchItem != null) {
			searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
			if (fragmentActive == FragmentActive.LIST) {
				customArrayAdapter = ((Filterable) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container))
						.getAdapter();
																
				searchView.setOnQueryTextListener(new OnQueryTextListener() {
					@Override
					public boolean onQueryTextChange(String s) {
						customArrayAdapter.getFilter().filter(s);
						return false;
					}

					@Override
					public boolean onQueryTextSubmit(String s) {
						customArrayAdapter.getFilter().filter(s);
						return false;
					}
				});
			}
		}

		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return true;
	}

	
	private void Render_Menu(){
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(R.array.devoluciones_lista_options);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), android.R.layout.simple_list_item_1,opcionesMenu));
		
		// Añadimos Funciones al menú laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				drawerList.setItemChecked(position, true);
				
				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);
				
				// SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
				int pos = customArrayAdapter.getSelectedPosition();
				// OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
				item_selected = (vmDevolucion) customArrayAdapter.getItem(pos);
				
				switch (position) {
					case NUEVO_DEVOLUCION : 
						Intent intent = new Intent(ViewDevoluciones.this, ViewDevolucionEdit.class);
						startActivity(intent);
						break;
					case ABRIR_DEVOLUCION : 
						is_Item_selected();
						
						break;
					case BORRAR_DEVOLUCION:
						is_Item_selected();
						isOffLine();
						isEnviada();
						
						break;
					case ENVIAR_DEVOLUCION :
						is_Item_selected();
						
						break;
					case IMPRIMIR_COMPROBANTE:
						is_Item_selected();
						
						break;
					case BORRAR_ENVIADAS:
						is_Item_selected();
						
						break;
					case FICHA_DEL_CLIENTE:
						is_Item_selected();
						
						break;
					case CUENTAS_POR_COBRAR:
						is_Item_selected();
						
						break;
					case CERRAR :
						finish();
						break;
				
				}
				
				drawerLayout.closeDrawers();
			}
					
		});
		
		tituloSeccion = getTitle();
		tituloApp = getTitle();

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloSeccion);
				ActivityCompat.invalidateOptionsMenu(ViewDevoluciones.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewDevoluciones.this);

			}
		};
		
		// establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
	}
	
	private void is_Item_selected(){
		if (item_selected == null) {
			drawerLayout.closeDrawers();
			AppDialog.showMessage(getActionBar().getThemedContext(), "Información", "Seleccione un registro.",DialogType.DIALOGO_ALERTA);
			return;
		}
	}
	private void isOffLine(){
		if(item_selected.isOffLine()){
			drawerLayout.closeDrawers();
			AppDialog.showMessage(getActionBar().getThemedContext(), "Información", "El comprobante fue emitida offline.\n",DialogType.DIALOGO_ALERTA);
			return;
		}
	}
	private void isEnviada(){
		if(!item_selected.getEstado().equals("ENVIADA")){
			drawerLayout.closeDrawers();
			AppDialog.showMessage(getActionBar().getThemedContext(), "Información", "Este registro no tiene estado Enviado.",DialogType.DIALOGO_ALERTA);
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void Load_Data(int what) {
		try {

			NMApp.getController().getInboxHandler().sendEmptyMessage(what);
			pDialog = new ProgressDialog(this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Procesando...");
			pDialog.setCancelable(true);
			pDialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SetList(List<vmDevolucion> list) {
		devoluciones = list;
		firstFragment.setItems(devoluciones,true);
		gridheader.setText(String.format("LISTA DEVOLUCIONES (%s)", devoluciones.size()));
	}
	
	
}