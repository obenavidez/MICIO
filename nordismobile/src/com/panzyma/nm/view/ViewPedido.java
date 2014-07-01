package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.DELETE_ITEM_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.CustomDialog.OnActionButtonClickListener;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.fragments.CustomArrayAdapter;
import com.panzyma.nm.fragments.ListaFragment;
import com.panzyma.nm.interfaces.Filterable;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.Ventas;
import com.panzyma.nm.viewmodel.vmEntity;
import com.panzyma.nordismobile.R;

@SuppressWarnings("rawtypes")
public class ViewPedido extends ActionBarActivity implements ListaFragment.OnItemSelectedListener, Handler.Callback
{
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) 
	{
		// TODO Auto-generated method stub
		super.startActivityFromFragment(fragment, intent, requestCode);
	}

	private static final String TAG = ProductoView.class.getSimpleName();
	CustomArrayAdapter<vmEntity> customArrayAdapter;
	private SearchView searchView;
	int listFragmentId;
	int positioncache = -1;
	private Context context;
	private static final int NUEVO_PEDIDO = 0;
	private static final int EDITAR_PEDIDO = 1;
	private static final int BORRAR_PEDIDO=3;
	private static final int BORRAR_PEDIDO_FINALIZADO = 4;
	private static final int ANULAR_PEDIDO = 5;
	private String[] opcionesMenu;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	Intent intent;Bundle b;
	private CharSequence tituloSeccion;
	private CharSequence tituloApp;
	private NMApp nmapp;
	ProgressDialog pDialog;
	TextView gridheader;
	TextView footerView;

	private List<vmEntity> pedidos = new ArrayList<vmEntity>();
	vmEntity pedido_selected;
	ListaFragment<vmEntity> firstFragment;
	private ViewPedido vp;
	private BPedidoM bpm;

	@SuppressLint("CutPasteId")
	private void initComponent() {
		gridheader = (TextView) findViewById(R.id.ctextv_gridheader);
		gridheader.setText("Listado de Pedidos (0) ");
		footerView = (TextView) findViewById(R.id.ctxtview_enty);
		footerView.setVisibility(View.VISIBLE);
	}
	
	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.layout_client_fragment);

		initComponent();
		
		vp=this;
		
		gridheader.setVisibility(View.VISIBLE);

		opcionesMenu = new String[] { "Nuevo Pedido", "Editar Pedido",
				"Enviar Pedido", "Borrar Pedido","Borrar Pedidos Finalizados","Anular Pedido","Consultas de Ventas", "Cerrar" };
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// drawerLayout.openDrawer(Gravity.END);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		drawerList.setOnItemClickListener(new OnItemClickListener() {
		

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int pos =0;
				String state="";
				switch (position) 
				{
					case NUEVO_PEDIDO:
						intent = new Intent(ViewPedido.this, ViewPedidoEdit.class);
					    startActivityForResult(intent,1);// Activity is started with requestCode 2   
						break;
					case EDITAR_PEDIDO: 
						
						intent = new Intent(ViewPedido.this, ViewPedidoEdit.class);
						
						Pedido p = null;
						try {
							 p=Ventas.obtenerPedidoByID(pedido_selected.getId(), vp);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						b=new Bundle();
						b.putParcelable("pedido",p);
						intent.putExtras(b);
						
					    startActivityForResult(intent,1);// Activity is started with requestCode 2
						
						break;
					
					case BORRAR_PEDIDO:
						//SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
						pos = customArrayAdapter.getSelectedPosition();
						//OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
						pedido_selected = customArrayAdapter.getItem(pos);
						//OBTENER EL ESTADO DEL REGISTRO
						state = pedido_selected.getDescEstado(); 
						if("PORVALIDAR".equals(state) || "APROBADO".equals(state) ){
							Toast.makeText(getApplicationContext(),"No puede borrar pedidos por validar o aprobados.", Toast.LENGTH_SHORT).show();
							return;
						}
						nmapp.getController().getInboxHandler().sendEmptyMessage(ControllerProtocol.DELETE_DATA_FROM_LOCALHOST);
						//CERRAR EL MENU DEL DRAWER
						drawerLayout.closeDrawers();
						break;	
					case BORRAR_PEDIDO_FINALIZADO : 
						//SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
						pos = customArrayAdapter.getSelectedPosition();
						//OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
						pedido_selected = customArrayAdapter.getItem(pos);
						state = pedido_selected.getDescEstado();
						
						
						break;
					case ANULAR_PEDIDO : 
						//SELECCIONAR LA POSICION DEL RECIBO SELECCIONADO ACTUALMENTE
						pos = customArrayAdapter.getSelectedPosition();
						//OBTENER EL RECIBO DE LA LISTA DE RECIBOS DEL ADAPTADOR
						pedido_selected = customArrayAdapter.getItem(pos);
						//OBTENER EL ESTADO DEL REGISTRO
						state = pedido_selected.getDescEstado();
						
					    //VALIDAR QUE EL PEDIDO ESTÉ EN ESTADO NO ES APROBADO
						if ("APROBADO".compareTo(state) != 0) {
							Toast.makeText(getApplicationContext(),"Solo se pueden anular pedidos en estado de APROBADO.", Toast.LENGTH_SHORT).show();
							return;
						}
						//SI SE ESTÁ FUERA DE LA COBERTURA
						if(!NMNetWork.isPhoneConnected(context,nmapp.getController()) && !NMNetWork.CheckConnection(nmapp.getController()))
						{
				            Toast.makeText(getApplicationContext(),"La operación no puede ser realizada ya que está fuera de cobertura.", Toast.LENGTH_SHORT).show();
							return;
						}
						
						 try {
							 
						 }
						 catch(Exception ex) {
							 	ex.printStackTrace();
				            }
						break;
				}
	/*
				drawerList.setItemChecked(position, true);
				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);
				*/
	
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
				ActivityCompat.invalidateOptionsMenu(ViewPedido.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewPedido.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		nmapp = (NMApp) this.getApplicationContext();
		try {
			nmapp.getController().setEntities(this, bpm=new BPedidoM());
			nmapp.getController().addOutboxHandler(new Handler(this));
			nmapp.getController().getInboxHandler()
					.sendEmptyMessage(ControllerProtocol.LOAD_DATA_FROM_LOCALHOST);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// However, if we're being restored from a previous state,
		// then we don't need to do anything and should return or else
		// we could end up with overlapping fragments.
		if (savedInstanceState != null) {
			return;
		}

		// Create an instance of ExampleFragment
		firstFragment = new ListaFragment<vmEntity>();

		// In case this activity was started with special instructions from
		// an Intent,
		// pass the Intent's extras to the fragment as arguments
		firstFragment.setArguments(getIntent().getExtras());

		// Add the fragment to the 'fragment_container' FrameLayout
		if (findViewById(R.id.fragment_container) != null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		} else {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.item_client_fragment, firstFragment).commit();
		}
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		if (findViewById(R.id.fragment_container) != null) {
			customArrayAdapter = (CustomArrayAdapter<vmEntity>) ((Filterable) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container)).getAdapter();

		} else {
			customArrayAdapter = (CustomArrayAdapter<vmEntity>) ((Filterable) getSupportFragmentManager()
					.findFragmentById(R.id.item_client_fragment)).getAdapter();
		}

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

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) 
		{
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
			;
			break;
		case R.id.action_search:
			Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean menuAbierto = drawerLayout.isDrawerOpen(drawerList);

		if (menuAbierto)
			menu.findItem(R.id.action_search).setVisible(false);
		else
			menu.findItem(R.id.action_search).setVisible(true);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean handleMessage(Message msg) 
	{
		Boolean val = false;
		switch (msg.what) 
		{
			case C_DATA:
				establecer(msg);
				val= true;
				break;
			case DELETE_ITEM_FINISHED:
				customArrayAdapter.remove(pedido_selected);	
				customArrayAdapter.notifyDataSetChanged();
				buildCustomDialog("Nordis Movile",msg.obj.equals(1)?"Eliminado Satisfactoriamente":"Error al eliminar",ALERT_DIALOG).show();
				val=true;
				break;
			case ERROR:
				ErrorMessage error=((ErrorMessage)msg.obj);
				buildCustomDialog(error.getTittle(),error.getMessage()+error.getCause(),ALERT_DIALOG).show();	
				val=true;
				break;
		}
		return val;
	}

	@Override
	public void onItemSelected(Object obj, int position) {
		// TODO Auto-generated method stub
		pedido_selected = firstFragment.getAdapter().getItem(position);
		positioncache=position;
	}
	
	@SuppressWarnings("unchecked")
	private void establecer(Message msg) {
		pedidos = (ArrayList<vmEntity>) ((msg.obj == null) ? new ArrayList<vmEntity>()
				: msg.obj);
		gridheader.setVisibility(View.VISIBLE);
		gridheader.setText(String.format("Listado de Recibos (%s)",
				pedidos.size()));
		if (pedidos.size() == 0) 
		{
			TextView txtenty = (TextView) findViewById(R.id.ctxtview_enty);
			txtenty.setVisibility(View.VISIBLE);
		}
		firstFragment.setItems(pedidos);
		firstFragment.getAdapter().setSelectedPosition(0);
		positioncache = 0;
		if(pedidos.size() > 0)
			pedido_selected = firstFragment.getAdapter().getItem(0);
	}
	public vmEntity getPedidoSelected() {
		return pedido_selected;
	}	
	public BPedidoM getBridge() {
		// TODO Auto-generated method stub
		return bpm;
	}

	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		final CustomDialog dialog=new CustomDialog(this);
		dialog.setCancelable(true);
	    dialog.setCanceledOnTouchOutside(true);
	    dialog.setMessageType(type);
	    dialog.setTitulo(tittle);
	    dialog.setMensaje(msg);    
	    dialog.setOnActionDialogButtonClickListener
	    (
    		 new OnActionButtonClickListener()
    		 {
				@SuppressWarnings("static-access")
				@Override
				public void onButtonClick(View _dialog,int actionId) {									 
					if(actionId==CustomDialog.OK_BUTTOM ){
						dialog.dismiss();	
				}
			 }
			} 
	    );
	    return dialog;
	}
	
}
