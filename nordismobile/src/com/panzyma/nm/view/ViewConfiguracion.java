package com.panzyma.nm.view;

import static com.panzyma.nm.controller.ControllerProtocol.C_UPDATE_FINISHED;
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.C_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.ID_CERRAR;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SALVAR_CONFIGURACION;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CLIENTES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PARAMETROS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PRODUCTOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PROMOCIONES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_TODOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SETTING_BLUETOOTHDEVICE;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_SETTING;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;
import static com.panzyma.nm.controller.ControllerProtocol.SAVE_DATA_FROM_LOCALHOST;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.AppDialog.DialogType;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.NumberUtil;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.DataConfigurationResult;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.view.adapter.InvokeBridge;
import com.panzyma.nm.viewmodel.vmConfiguracion;
import com.panzyma.nordismobile.R;

@SuppressLint("ShowToast")
@SuppressWarnings({ "rawtypes", "unused", "static-access" })
@InvokeBridge(bridgeName = "BConfiguracionM")
public class ViewConfiguracion extends ActionBarActivity implements
		Handler.Callback {
	String TAG = ViewConfiguracion.class.getSimpleName();
	QuickAction quickAction;
	Display display;
	ProgressDialog pd;
	ViewConfiguracion context;
	boolean isEditActive;
	private EditText txtURL;
	private EditText txtURL2;
	private EditText txtEmpresa;
	private EditText txtUsuario;
	private EditText txtDispositivoID;
	private String user_name;
	private String passwd;
	private String enterprise;
	private String url_server;
	private String url_server2;
	private String deviceid;
	private boolean onRestart;
	private boolean onPause;
	private boolean loginAsAdmin = false;
	private DataConfigurationResult settingdata;
	private static CustomDialog dlg;
	static Object lockC = new Object();
	private Intent intento;
	private BConfiguracionM bcm;
	private EditText txtImpresora;
	public static int RESULTADO_IMPRESORA = 1;
	private Impresora impresora;
	private vmConfiguracion oldata;
	private vmConfiguracion configuracion;
	DrawerLayout drawerLayout;
	ListView drawerList;
	ActionBarDrawerToggle drawerToggle;
	String[] opcionesMenu;
	CharSequence tituloSeccion;
	CharSequence tituloApp;
	private static final int SALVAR_CONFIGURACION = 0;
	private static final int SINCRONIZAR_PARAMETROS = 1;
	private static final int SINCRONIZAR_CATALOGOS = 2;
	private static final int SINCRONIZAR_CLIENTES = 3;
	private static final int SINCRONIZAR_PRODUCTOS = 4;
	private static final int SINCRONIZAR_PROMOCIONES = 5;
	private static final int SINCRONIZAR_TODO = 6;
	private static final int CONFIGURAR_IMPRESORA = 7;
	private static final int LOGIN_WITH_ADMIN = 8;
	private static final int CERRAR = 9;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_configuracion);
		isEditActive = getIntent().getExtras().getBoolean("isEditActive");
		try {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			context = this;
			UserSessionManager.setContext(this);
			NMApp.getController().setView(this);
			NMApp.getController().getInboxHandler().sendEmptyMessage(LOAD_DATA);
			getDeviceId(this);
			configuracion = new vmConfiguracion();
			WindowManager wm = (WindowManager) this.getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();
			pd = ProgressDialog.show(this, "Espere por favor",
					"Trayendo Info...", true, false);
			initComponents();
			CreateMenu();
		} catch (Exception e) {
			e.printStackTrace();
			dlg = (CustomDialog) dialog("Error Message", e.getMessage()
					+ "\n Cause:" + e.getCause(), ALERT_DIALOG);
			dlg.show();
		}
	}

	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(android.content.Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();

	}

	public String getTBoxUserName() {
		return this.txtUsuario.getText().toString();
	}

	public void setUserName(String user) {
		this.user_name = user;
	}

	public String getUserName() {
		return this.user_name;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public String getTBoxEnterprise() {
		return this.txtEmpresa.getText().toString();
	}

	public String getEnterprise() {
		return this.enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public String getTBoxUrlServer() {
		return this.txtURL.getText().toString();
	}

	public String getUrlServer() {
		return this.url_server;
	}

	public void setUrlServer(String url_server) {
		this.url_server = url_server;
	}

	public String getTBoxUrlServer2() {
		return this.txtURL2.getText().toString();
	}

	public String getUrlServer2() {
		return this.url_server2;
	}

	public void setUrlServer2(String url_server) {
		this.url_server2 = url_server;
	}

	public void setDeviceId(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getDeviceId() {
		return this.deviceid;
	}

	public void setImpresora(Impresora _impresora) {
		this.impresora = _impresora;
		SessionManager.setImpresora(impresora);
	}

	public Impresora getImpresora() {
		return this.impresora;
	}

	public void ocultarDialogos() {
		if (dlg != null)
			dlg.dismiss();
		if (pd != null)
			pd.dismiss();
	}

	@Override
	public boolean handleMessage(final Message msg) {
		Log.d(TAG, "Received message: " + msg);
		ocultarDialogos();
		switch (msg.what) {
		case C_UPDATE_FINISHED:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(context, "", msg.obj.toString(),
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
			break;
		case C_DATA:
			setData((vmConfiguracion) msg.obj);
			break;
		case ControllerProtocol.NOTIFICATION:
			showStatus(msg.obj.toString(), true);
			break;
		case ControllerProtocol.NOTIFICATION_DIALOG2:
			showStatus(msg.obj.toString());
			break;

		case ControllerProtocol.ERROR:
			AppDialog.showMessage(context,
					((ErrorMessage) msg.obj).getTittle(),
					((ErrorMessage) msg.obj).getMessage(),
					DialogType.DIALOGO_ALERTA);
			break;
		case ControllerProtocol.SALVARCONFIGURACIONANTESDESALIR:
			finish();
			break;
		case ControllerProtocol.UPDATE_OBJ_VIEW:
			if (msg.obj instanceof vmConfiguracion) {
				configuracion = (vmConfiguracion) msg.obj;
				configuracion.setOldData(configuracion);
				actualizarOnUIDevicePrefix(Integer.parseInt(configuracion
						.getDeviceId()));
			}
			break;
		}
		return false;
	}

	public void actualizarOnUIDevicePrefix(final int deviceprefix) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				txtDispositivoID.setText("" + deviceprefix);
			}
		});
		setDeviceId("" + deviceprefix);
	}

	private void setData(vmConfiguracion setting) {
		setImpresora(setting.getImpresora());
		configuracion = vmConfiguracion.setConfiguration(setting);
		txtURL.setText(setting.getAppServerURL());
		txtURL2.setText(setting.getAppServerURL2());
		txtEmpresa.setText(setting.getEnterprise());
		txtUsuario.setText(setting.getNameUser());
		txtDispositivoID.setText(setting.getDeviceId());
		txtImpresora.setText(setting.getImpresora().obtenerNombre());
		this.setUrlServer(setting.getAppServerURL());
		this.setDeviceId(setting.getEnterprise());
		this.setUserName(setting.getNameUser());
		SessionManager.setContext(context);
		UserSessionManager.setContext(this);
		configuracion.setOldData(configuracion);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Button Menu = (Button) findViewById(R.id.btn_menu);
			quickAction.show(Menu, display, true);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FINISH_ACTIVITY();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void initComponents() {
		initMenu();
		txtURL = (EditText) findViewById(R.id.cfgtextv_detalleurlws);
		txtURL2 = (EditText) findViewById(R.id.cfgtextv_detalleurlws2);
		txtEmpresa = (EditText) findViewById(R.id.cfgtextv_detallecodempresa);
		txtUsuario = (EditText) findViewById(R.id.cfgtextv_detalleuser);
		txtDispositivoID = (EditText) findViewById(R.id.cfgtextv_detalledeviceid);
		txtImpresora = (EditText) findViewById(R.id.cf_bluetoothprinter);
		txtURL.setEnabled(isEditActive);
		txtURL2.setEnabled(isEditActive);
		txtEmpresa.setEnabled(isEditActive);
		txtUsuario.setEnabled(isEditActive);

		pd.dismiss();
	}

	public void initMenu() {
		quickAction = new QuickAction(this, QuickAction.VERTICAL, 1);
		quickAction.addActionItem((new ActionItem(ID_SALVAR_CONFIGURACION,
				"Salvar Configuración")));
		quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PARAMETROS,
				"Sincronizar Parametros")));
		quickAction
				.addActionItem((new ActionItem(ID_SINCRONIZE_CATALOGOSBASICOS,
						"Sincronizar Catalogos Básicos")));
		quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_CLIENTES,
				"Sincronizar Clientes")));
		quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PRODUCTOS,
				"Sincronizar Productos")));
		quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_PROMOCIONES,
				"Sincronizar Promociones")));
		quickAction.addActionItem(null);
		quickAction.addActionItem((new ActionItem(ID_SINCRONIZE_TODOS,
				"Sincronizar Todo")));
		quickAction.addActionItem(null);
		quickAction.addActionItem((new ActionItem(ID_SETTING_BLUETOOTHDEVICE,
				"Configurar Impresora")));
		quickAction.addActionItem(null);
		// quickAction.addActionItem((new ActionItem(LOGIN_WITH_ADMIN,
		// "Autenticar como Admin.")));
		// quickAction.addActionItem(null);
		quickAction.addActionItem((new ActionItem(ID_CERRAR, "Cerrar")));

		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						final Controller controller = NMApp.getController();
						ActionItem actionItem = quickAction.getActionItem(pos);
						if (actionId != ID_SETTING_BLUETOOTHDEVICE)
							if (!validar())
								return;

						if (actionId == ID_SALVAR_CONFIGURACION)
							salvarConfiguracion();
						else if (actionId == ID_SINCRONIZE_PARAMETROS) {
							controller.getInboxHandler().sendEmptyMessage(
									ID_SINCRONIZE_PARAMETROS);

						} else if (actionId == ID_SINCRONIZE_CATALOGOSBASICOS) {
							controller.getInboxHandler().sendEmptyMessage(
									ID_SINCRONIZE_CATALOGOSBASICOS);

						} else if (actionId == ID_SINCRONIZE_CLIENTES) {
							controller.getInboxHandler().sendEmptyMessage(
									ID_SINCRONIZE_CLIENTES);

						} else if (actionId == ID_SINCRONIZE_PRODUCTOS) {
							controller.getInboxHandler().sendEmptyMessage(
									ID_SINCRONIZE_PRODUCTOS);

						} else if (actionId == ID_SINCRONIZE_PROMOCIONES) {
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putString("LoginUsuario", txtUsuario.getText()
									.toString());
							msg.setData(b);
							msg.what = ID_SINCRONIZE_PROMOCIONES;
							controller.getInboxHandler().sendMessage(msg);

						} else if (actionId == ID_SINCRONIZE_TODOS) {
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
							controller.getInboxHandler().sendEmptyMessage(
									ID_SINCRONIZE_TODOS);

						} else if (actionId == ID_SETTING_BLUETOOTHDEVICE) {
							intento = new Intent(ViewConfiguracion.this,
									ConfigurarDispositivosBluetooth.class);
							startActivityForResult(intento, 0);
						} else if (actionId == LOGIN_WITH_ADMIN) {
							loginAsAdmin = true;
							dialogLoginAdmin();
						} else if (actionId == ID_CERRAR)
							FINISH_ACTIVITY();

					}
				});

		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});

	}

	public void dialogLoginAdmin() {
		try {
			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						NMApp.modulo = NMApp.Modulo.CONFIGURACION;
						if (SessionManager.SignIn(true)) {
							loginAsAdmin = true;
						}
					} catch (Exception e) {
						NMApp.modulo = NMApp.Modulo.HOME;
						e.printStackTrace();
						dialog("", SessionManager.getErrorAuntentication(),
								ALERT_DIALOG);
					}

				}

			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean salvarConfiguracion(int... _what) {
		if (validar()) {
			setEnterprise(txtEmpresa.getText().toString());
			setUserName(txtUsuario.getText().toString());
			setUrlServer(txtURL.getText().toString());
			setUrlServer2(txtURL2.getText().toString());
			setDeviceId(txtDispositivoID.getText().toString());

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString("URL", txtURL.getText().toString());
			b.putString("URL2", txtURL2.getText().toString());
			b.putString("Empresa", txtEmpresa.getText().toString());
			b.putString("Credentials", "");
			b.putString("LoginUsuario", txtUsuario.getText().toString());
			b.putParcelable("impresora", getImpresora());
			b.putString("PIN", NMNetWork.getDeviceId(context));
			b.putBoolean("confirm", true);
			msg.setData(b);
			msg.what = _what.length != 0 ? _what[0] : LOAD_SETTING;
			NMApp.getController().getInboxHandler().sendMessage(msg);
		}
		return false;
	}

	public boolean validar() {

		if (txtURL.getText().toString().trim().length() == 0) {
			txtURL.setError("Ingrese el nombre o IP del servidor SIMFAC.");
			txtURL.requestFocus();
			return false;
		} else if (txtURL2.getText().toString().trim().length() == 0) {
			txtURL2.setError("Ingrese el nombre o IP del servidor SIMFAC.");
			txtURL2.requestFocus();
			return false;
		} else if (txtEmpresa.getText().toString().trim().length() == 0) {
			txtEmpresa.setError("Ingrese el código de la empresa.");
			txtEmpresa.requestFocus();
			return false;
		}
		// else if (txtImpresora.getText().toString().trim().length()==0){
		// txtImpresora.setError("Configure Impresora.");
		// txtImpresora.requestFocus();
		// return false;
		// }
		else if (txtUsuario.getText().toString().trim().length() == 0) {
			txtUsuario.setError("Ingrese el nombre del usuario.");
			txtUsuario.requestFocus();
			return false;
		}
		// else if (getImpresora()==null || (getImpresora()!=null &&
		// getImpresora().obtenerNombre().trim()=="")){
		// txtUsuario.setError("No se ha configurado impresora de trabajo.");
		// return false;
		// }
		return true;
	}

	@Override
	protected void onPause() {
		NMApp.ciclo = NMApp.lifecycle.ONPAUSE;
		super.onPause();
	}

	@Override
	protected void onStop() {
		NMApp.ciclo = NMApp.lifecycle.ONSTOP;
		super.onStop();
	}

	@Override
	protected void onRestart() {
		NMApp.ciclo = NMApp.lifecycle.ONRESTART;
		super.onRestart();
	}

	@Override
	protected void onResume() {
		NMApp.modulo = NMApp.Modulo.CONFIGURACION;
		ocultarDialogos();
		if (NMApp.ciclo == NMApp.lifecycle.ONPAUSE
				|| NMApp.ciclo == NMApp.lifecycle.ONRESTART) {
			NMApp.getController().setView(this);
			SessionManager.setContext(this);
			UserSessionManager.setContext(this);
		}
		super.onResume();
	}

	private void FINISH_ACTIVITY() {

		ocultarDialogos();
		Log.d(TAG, "Activity quitting");
		if (configuracion.hasModified(configuracion.getOldData())) {

			AppDialog.showMessage(this, "",
					"¿Desea guardar la información antes de salir?",
					AppDialog.DialogType.DIALOGO_CONFIRMACION,
					new AppDialog.OnButtonClickListener() {
						@Override
						public void onButtonClick(AlertDialog _dialog,
								int actionId) {

							if (AppDialog.OK_BUTTOM == actionId) {
								try {
									salvarConfiguracion(ControllerProtocol.SALVARCONFIGURACIONANTESDESALIR);
								} catch (Exception e) {
									NMApp.getController()
											.notifyOutboxHandlers(
													ControllerProtocol.ERROR,
													0,
													0,
													new ErrorMessage(
															"Error guardando Configuración",
															e.getMessage(),
															e.getMessage()));
								}

							} else
								finish();

							_dialog.dismiss();

						}

					});

		} else
			finish();
	}

	public Dialog dialog(String tittle, String msg, int type) {
		return new CustomDialog(this, tittle, msg, false, type);
	}

	public String getLoginUsuario() {
		return txtUsuario.getText().toString();
	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestcode, resultcode, data);
		try {
			NMApp.getController().setView(this);
			if (RESULTADO_IMPRESORA == resultcode && data != null)
				actualizarImpresoraEnPantalla((Impresora) data
						.getParcelableExtra("impresora"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityFromFragment(fragment, intent, requestCode);
	}

	private BConfiguracionM getBridge() {
		return bcm;
	}

	public void actualizarImpresoraEnPantalla(final Impresora dispositivo) {
		setImpresora(dispositivo);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				txtImpresora.setText(Impresora.obtenerNombre());
			}
		});
	}

	public void showStatus(final String mensaje, boolean... confirmacion) {
		ocultarDialogos();

		if (confirmacion.length != 0 && confirmacion[0]) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(context, "", mensaje,
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
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dlg = new CustomDialog(context, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	}

	public void actualizarIdDispositivo(final String id) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				txtDispositivoID.setText(id);
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.action_search).setVisible(true);
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			item.getItemId();
			return true;
		}
		return true;
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

	public void CreateMenu() {
		// Obtenemos las opciones desde el recurso
		opcionesMenu = getResources().getStringArray(
				R.array.configurationoptions);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Buscamos nuestro menu lateral
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1,
				opcionesMenu));

		// Añadimos Funciones al menú laterak
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				drawerList.setItemChecked(position, true);
				drawerLayout.closeDrawers();
				tituloSeccion = opcionesMenu[position];
				// Ponemos el titulo del Menú
				getSupportActionBar().setTitle(tituloSeccion);
				Controller controller = NMApp.getController();
				switch (position) {
				case SALVAR_CONFIGURACION:
					salvarConfiguracion();
					break;
				case SINCRONIZAR_PARAMETROS:
					controller.getInboxHandler().sendEmptyMessage(
							ID_SINCRONIZE_PARAMETROS);
					break;
				case SINCRONIZAR_CATALOGOS:
					controller.getInboxHandler().sendEmptyMessage(
							ID_SINCRONIZE_CATALOGOSBASICOS);
					break;
				case SINCRONIZAR_CLIENTES:
					controller.getInboxHandler().sendEmptyMessage(
							ID_SINCRONIZE_CLIENTES);
					break;
				case SINCRONIZAR_PRODUCTOS:
					controller.getInboxHandler().sendEmptyMessage(
							ID_SINCRONIZE_PRODUCTOS);
					break;
				case SINCRONIZAR_PROMOCIONES:
					SinCronizar_Promociones();
					break;
				case SINCRONIZAR_TODO:
					controller.getInboxHandler().sendEmptyMessage(
							ID_SINCRONIZE_TODOS);
					break;
				case CONFIGURAR_IMPRESORA:
					Set_BlueTooth();
					break;
				case LOGIN_WITH_ADMIN:
					dialogLoginAdmin();
					break;
				case CERRAR:
					FINISH_ACTIVITY();
					break;
				}
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
				ActivityCompat.invalidateOptionsMenu(ViewConfiguracion.this);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(ViewConfiguracion.this);

			}
		};

		// establecemos el listener para el dragable ....
		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private void SinCronizar_Promociones() {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString("LoginUsuario", txtUsuario.getText().toString());
		msg.setData(b);
		msg.what = ID_SINCRONIZE_PROMOCIONES;
		NMApp.getController().getInboxHandler().sendMessage(msg);
	}

	private void Set_BlueTooth() {
		intento = new Intent(ViewConfiguracion.this,
				ConfigurarDispositivosBluetooth.class);
		startActivityForResult(intento, 0);
	}

}
