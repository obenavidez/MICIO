
package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SALVAR_CONFIGURACION;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CATALOGOSBASICOS2;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_CLIENTES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PARAMETROS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PRODUCTOS;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_PROMOCIONES;
import static com.panzyma.nm.controller.ControllerProtocol.ID_SINCRONIZE_TODOS;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA;
import static com.panzyma.nm.controller.ControllerProtocol.LOAD_SETTING;
import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.os.Message;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelCliente;
import com.panzyma.nm.model.ModelConfiguracion;
import com.panzyma.nm.model.ModelProducto;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.DataConfigurationResult;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.viewmodel.vmConfiguracion;
 
public class BConfiguracionM extends BBaseM {

	String TAG = BConfiguracionM.class.getSimpleName();
	ArrayList<Cliente> LCliente;
	JSONArray productos;
	JSONArray promociones;
	Object lock = new Object();
	Usuario user;
	public int ON_ID_Handler;

	/*
	 * 
	 * Message msg = new Message(); Bundle b = new Bundle();
	 * b.putString("URL",txtURL.getText().toString());
	 * b.putString("URL2",txtURL2.getText().toString());
	 * b.putString("Empresa",txtEmpresa.getText().toString());
	 * b.putString("Credentials",SessionManager.getCredenciales());
	 * b.putString("LoginUsuario", txtUsuario.getText().toString());
	 * b.putParcelable("impresora", getImpresora());
	 * b.putString("PIN",NMNetWork.getDeviceId(context));
	 * 
	 * (final String Url,final String Url2,final String Empresa,final String
	 * Credentials, final String LoginUsuario, final String PIN, final Impresora
	 * dispositivo) throws Exception
	 */

	public boolean handleMessage(Message msg) throws Exception 
	{ 
		
		makeAction(msg.what, msg);
		// switch (msg.what) {
		//
		// case LOAD_DATA:
		// LOAD_DATA();
		//
		// break;
		// case LOAD_SETTING:
		//
		// try
		// {
		// GET_DATACONFIGURATION(b.get("URL").toString(),
		// b.get("URL2").toString(),
		// b.get("Empresa").toString(),
		// b.get("Credentials").toString(),
		// b.get("LoginUsuario").toString(),
		// b.get("PIN").toString(),
		// (b.getParcelable("impresora")!=null)?(Impresora)b.getParcelable("impresora"):null,
		// b.getBoolean("confirm",true));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// break;
		// case ID_SALVAR_CONFIGURACION:
		// SaveLocalHost(b);
		// break;
		// case ControllerProtocol.ID_RESETEAR_CONFIGURACION:
		// SaveLocalHost(b);
		// break;
		// case ID_SINCRONIZE_PARAMETROS:
		// SINCRONIZE_PARAMETROS();
		// break;
		// case ID_SINCRONIZE_CATALOGOSBASICOS:
		// SINCRONIZE_CATALOGOSBASICOS();
		// break;
		// case ID_SINCRONIZE_CLIENTES:
		// SINCRONIZE_CLIENTES();
		// break;
		// case ID_SINCRONIZE_PRODUCTOS:
		// SINCRONIZE_PRODUCTOS();
		// break;
		// case ID_SINCRONIZE_PROMOCIONES:
		// SINCRONIZE_PROMOCIONES();
		// break;
		// case ID_SINCRONIZE_TODOS:
		// SINCRONIZE_TODOS();
		// break;
		// case ControllerProtocol.SALVARCONFIGURACIONANTESDESALIR:
		// SAVEBEFORE(b.get("URL").toString(),
		// b.get("URL2").toString(),
		// b.get("Empresa").toString(),
		// b.get("Credentials").toString(),
		// b.get("LoginUsuario").toString(),
		// b.get("PIN").toString(),
		// (b.getParcelable("impresora")!=null)?(Impresora)b.getParcelable("impresora"):null);
		//
		// break;
		// }
		return false;
	}

	public boolean makeAction(int idaction, Message... _msg) throws Exception 
	{ 
		Message msg=(_msg!=null && _msg.length!=0)? _msg[0]:null;		
		Bundle b = (msg!=null)? msg.getData():null;
		ON_ID_Handler =idaction;
		switch (ON_ID_Handler) 
		{

		case LOAD_DATA:
			LOAD_DATA();

			break;
		case LOAD_SETTING:

			try 
			{
				GET_DATACONFIGURATION(
						b.get("URL").toString(),
						b.get("URL2").toString(),
						b.get("Empresa").toString(),
						b.get("Credentials").toString(),
						b.get("LoginUsuario").toString(),
						b.get("PIN").toString(),
						(b.getParcelable("impresora") != null) ? (Impresora) b
								.getParcelable("impresora") : null,
						b.getBoolean("confirm", true));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case ID_SALVAR_CONFIGURACION:
			vmConfiguracion c = ModelConfiguracion.getVMConfiguration(getContext()); 
			c.setAppServerURL(b.get("URL").toString());
			c.setAppServerURL2(b.get("URL2").toString());
			c.setNameUser(b.get("LoginUsuario").toString());
			c.setEnterprise(b.get("Empresa").toString()); 
			SaveLocalHost(c);
			
			break;
		case ControllerProtocol.ID_RESETEAR_CONFIGURACION:			
			saveLocalHost(b);
			if(msg.arg1!=0 && msg.arg1!=ID_SALVAR_CONFIGURACION)
				makeAction(msg.arg1);
			else
				Processor.notifyToView(NMApp.getController(),
						ControllerProtocol.C_UPDATE_FINISHED, 0, 0,
						"Se guardó la configuración correctamente.");
			
			break;
		case ID_SINCRONIZE_PARAMETROS:
			SINCRONIZE_PARAMETROS();
			break;
		case ID_SINCRONIZE_CATALOGOSBASICOS:
			SINCRONIZE_CATALOGOSBASICOS();
			break;
		case ID_SINCRONIZE_CLIENTES:
			SINCRONIZE_CLIENTES();
			break;
		case ID_SINCRONIZE_PRODUCTOS:
			SINCRONIZE_PRODUCTOS();
			break;
		case ID_SINCRONIZE_PROMOCIONES:
			SINCRONIZE_PROMOCIONES();
			break;
		case ID_SINCRONIZE_TODOS:
			SINCRONIZE_TODOS();
			break;
		case ControllerProtocol.SALVARCONFIGURACIONANTESDESALIR:
			SAVEBEFORE(
					b.get("URL").toString(),
					b.get("URL2").toString(),
					b.get("Empresa").toString(),
					b.get("Credentials").toString(),
					b.get("LoginUsuario").toString(),
					b.get("PIN").toString(),
					(b.getParcelable("impresora") != null) ? (Impresora) b
							.getParcelable("impresora") : null);

			break;
		}
		return true;
	}

	private void LOAD_DATA() {
		try {
			Processor.send_DataSourceToView(
					ModelConfiguracion.getVMConfiguration(getContext()),
					getController());
		} catch (Exception e) {
			try {
				Processor
						.notifyToView(
								getController(),
								ERROR,
								0,
								0,
								"CheckConnection :"
										+ "\n error en la comunicación con el servidor de aplicaciones \n Causa:"
										+ e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	public static Usuario GET_DATAUSER(String Credentials, String LoginUsuario)
			throws Exception {
		return ModelConfiguracion.getDatosUsuario(Credentials, LoginUsuario);
	}

	public static int GET_DEVISE_PREFIX(String Credentials, String PIN)
			throws Exception {
		return ModelConfiguracion.get_DEVISE_PREFIX(Credentials, PIN);
	}

	private void SINCRONIZE_PARAMETROS() 
	{
		try 
		{

			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try 
					{
						final String credentials = SessionManager.getCredenciales();
						if (credentials.trim() != "") {
							// user =
							// GET_DATAUSER(SessionManager.getCredentials(),view.getUserName());
							// ModelConfiguracion.saveUser(view, user);
							onSave_From_LocalHost(ModelConfiguracion.getSystemPerams(credentials),ID_SINCRONIZE_PARAMETROS, 0);

						} else
							getController()._notifyOutboxHandlers(0, 0, 0, 0);

					} catch (Exception e) {
						e.printStackTrace();
						try {
							Processor
									.notifyToView(
											getController(),
											ERROR,
											0,
											1,
											new ErrorMessage(
													"Error en la sincronización de clientes con el servidor",
													e.getMessage(),
													"\n Causa: " + e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			Processor.notifyToView(getController(),
					ControllerProtocol.NOTIFICATION_DIALOG2, 0, 0,
					"Sincronizando parametros del sistema");

		} catch (Exception e) {
			getController()._notifyOutboxHandlers(0, 0, 0, 0);
		}

	}

	private void SINCRONIZE_CATALOGOSBASICOS() {
		try {
			final String credentials = SessionManager.getCredenciales();
			if (credentials.trim() != "") {
				NMApp.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						try {
							onSave_From_LocalHost(
									ModelConfiguracion
											.getValoresCatalogo(
													credentials,
													"FormaPago-Moneda-EntidadBancaria-MotivoDevolucionNoVencidos-EstadoDevolucion-CausaEstadoDevolucion-TipoPrecio"),
									ID_SINCRONIZE_CATALOGOSBASICOS, 0);
							Processor.notifyToView(getController(),
									ControllerProtocol.NOTIFICATION_DIALOG2, 0,
									0, "Sincronizando Tasas de Cambio");
							onSave_From_LocalHost(ModelConfiguracion
									.getTasasDeCambio(credentials),
									ID_SINCRONIZE_CATALOGOSBASICOS2, 0);
						} catch (Exception e) {
							e.printStackTrace();
							try {
								Processor
										.notifyToView(
												getController(),
												ERROR,
												0,
												1,
												new ErrorMessage(
														"Error en la sincronización de clientes con el servidor",
														e.getMessage(),
														"\n Causa: "
																+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				});

				Processor
						.notifyToView(getController(),
								ControllerProtocol.NOTIFICATION_DIALOG2, 0, 0,
								"Sincronizando Catálogo FormaPago-Moneda-EntidadBancaria-Devoluciones");
			} else
				getController()._notifyOutboxHandlers(0, 0, 0, 0);

		} catch (Exception e) {

		}

	}

	public static void SAVEBEFORE(final String Url, final String Url2,
			final String Empresa, final String Credentials,
			final String LoginUsuario, final String PIN,
			final Impresora dispositivo) {

		try {

			DataConfigurationResult res = ModelConfiguracion
					.getDataConfiguration(Url2, Credentials, LoginUsuario, PIN);

			if (res.get_error() == null) {
				Processor.notifyToView(NMApp.getController(),
						ControllerProtocol.NOTIFICATION_DIALOG2, 0, 0,
						"Salvando configuración.");
				vmConfiguracion setting = vmConfiguracion.setConfiguration(Url,
						Url2, String.valueOf(res.get_devicePrefix()), Empresa,
						res.get_userInfo().getLogin(), res.get_maxIdPedido(),
						res.get_maxIdRecibo()
						,res.getMaxIdDevolucionV(),res.getMaxIdDevolucionNV(),dispositivo);
				ModelConfiguracion.saveConfiguration(NMApp.getContext(),
						setting);
				ModelConfiguracion.saveUser(NMApp.getContext(),
						res.get_userInfo());
				SessionManager.setImpresora(dispositivo);
				SessionManager.setLoguedUser(res.userInfo);
				Processor.notifyToView(NMApp.getController(),
						ControllerProtocol.SALVARCONFIGURACIONANTESDESALIR, 0,
						0, 0);

			} else
				throw new Exception(res.get_error());

		} catch (Exception e) {
			try {
				Processor.notifyToView(
						NMApp.getController(),
						ERROR,
						0,
						1,
						new ErrorMessage(
								"error en la comunicacion con el servidor", e
										.getMessage() + "\r\n", ""));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public static void GET_DATACONFIGURATION(final String Url,
			final String Url2, final String Empresa, final String Credentials,
			final String LoginUsuario, final String PIN,
			final Impresora dispositivo, final boolean... mode) {
		try {

			final String credenciales = (Credentials != null && Credentials != "") ? Credentials: SessionManager.getCredenciales();
			if (credenciales.trim() != "") {

				DataConfigurationResult res;
				try { 
					res = ModelConfiguracion.getDataConfiguration(Url2,
							Credentials, LoginUsuario,PIN);/*PIN*/

					if (res.get_error() == null) {
						if (res.get_devicePrefix() < 0) {
							SessionManager.setValidPrefix(false);
						} else {
							SessionManager.setValidPrefix(true);
							Processor.notifyToView(NMApp.getController(),
									ControllerProtocol.NOTIFICATION_DIALOG2, 0,
									0, "Salvando configuración.");

							vmConfiguracion setting = vmConfiguracion
									.setConfiguration(Url, Url2, String
											.valueOf(res.get_devicePrefix()),
											Empresa, res.get_userInfo()
													.getLogin(), res
													.get_maxIdPedido(), res
													.get_maxIdRecibo(),res.getMaxIdDevolucionV(),res.getMaxIdDevolucionNV(),
											dispositivo);
							ModelConfiguracion.saveConfiguration(
									NMApp.getContext(), setting);
							ModelConfiguracion.saveUser(NMApp.getContext(),
									res.get_userInfo());
							SessionManager.setImpresora(dispositivo);
							SessionManager.setLoguedUser(res.userInfo);
							Processor.notifyToView(NMApp.getController(),
									ControllerProtocol.UPDATE_OBJ_VIEW, 0, 0,
									setting);
							if (mode == null
									|| (mode != null && mode.length == 0))
								Processor
										.notifyToView(
												NMApp.getController(),
												ControllerProtocol.NOTIFICATION_DIALOG2,
												0, 0,
												"Configuración guardada exitosamente...");
							else
								Processor.notifyToView(NMApp.getController(),
										ControllerProtocol.NOTIFICATION, 0, 0,
										"Configuración guardada exitosamente.");
						}

					} else
						throw new Exception(res.get_error());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			try {
				Processor.notifyToView(
						NMApp.getController(),
						ERROR,
						0,
						1,
						new ErrorMessage(
								"error en la comunicacion con el servidor", e
										.getMessage() + "\r\n", ""));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private void SINCRONIZE_CLIENTES() {
		try {
			final String credentials = SessionManager.getCredenciales();

			if (credentials.trim() != "") {

				NMApp.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						try {

							Integer page = 1;
							while (true) {
								JSONArray modelcliente = ModelCliente
										.getArrayCustomerFromServer2(
												credentials, SessionManager
														.getLoginUser()
														.getLogin(), page,50);
								if (modelcliente.length() != 0) {
									onSave_From_LocalHost(modelcliente,
											ID_SINCRONIZE_CLIENTES, page);
									Processor
											.notifyToView(
													getController(),
													ControllerProtocol.NOTIFICATION_DIALOG2,
													0, 0,
													"Sincronizando Clientes \npágina:"
															+ page.toString());
									page++;
								} else {
									synchronized (lock) {
										lock.notify();
									}
									break;
								}

							}

							if (ON_ID_Handler != ID_SINCRONIZE_TODOS)
								Processor
										.notifyToView(
												getController(),
												ControllerProtocol.NOTIFICATION,
												0, 1,
												"Los clientes han sido sincronizados exitosamente");

						} catch (Exception e) {
							e.printStackTrace();
							try {
								Processor
										.notifyToView(
												getController(),
												ERROR,
												0,
												1,
												new ErrorMessage(
														"Error en la sincronización de clientes con el servidor",
														e.getMessage(),
														"\n Causa: "
																+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				});
				Processor.notifyToView(getController(),
						ControllerProtocol.NOTIFICATION_DIALOG2, 0, 0,
						"Sincronizando Clientes");
			} else
				getController()._notifyOutboxHandlers(0, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void SINCRONIZE_PRODUCTOS() {
		try {

			final String Credentials = SessionManager.getCredenciales();
			if (Credentials.trim() != "") {
				NMApp.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						try {
							Integer page = 1;
							while (true) {
								JSONArray modelproducto = ModelProducto
										.getArrayProductoFromServer(
												Credentials, SessionManager
														.getLoginUser()
														.getLogin(), page, 50);
								if (modelproducto.length() != 0) {
									onSave_From_LocalHost(modelproducto,
											ID_SINCRONIZE_PRODUCTOS, page);

									Processor
											.notifyToView(
													getController(),
													ControllerProtocol.NOTIFICATION_DIALOG2,
													0, 0,
													"Sincronizando Productos \npágina:"
															+ page.toString());
									page++;
								} else {
									synchronized (lock) {
										lock.notify();
									}
									break;
								}
							}
							if (ON_ID_Handler != ID_SINCRONIZE_TODOS)
								Processor
										.notifyToView(
												getController(),
												ControllerProtocol.NOTIFICATION,
												0, 1,
												"Los productos han sido sincronizados exitosamente");
						} catch (Exception e) {
							e.printStackTrace();
							try {
								Processor
										.notifyToView(
												getController(),
												ERROR,
												0,
												1,
												new ErrorMessage(
														"Error en la sincronización de produstos con el servidor",
														e.getMessage(),
														"\n Causa: "
																+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}

				});
				Processor.notifyToView(getController(),
						ControllerProtocol.NOTIFICATION_DIALOG2, 0, 0,
						"Sincronizando Productos");
			} else
				getController()._notifyOutboxHandlers(0, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void SINCRONIZE_PROMOCIONES() {
		try {

			final String credentials = SessionManager.getCredenciales();
			if (credentials.trim() != "") {
				NMApp.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						try {
							Integer page = 1;
							while (true) {
								JSONArray lpromocion = ModelConfiguracion
										.getPromocionesPaged(credentials,
												SessionManager.getLoginUser()
														.getLogin(), page, 30);
								if (lpromocion.length() != 0) {
									onSave_From_LocalHost(lpromocion,
											ID_SINCRONIZE_PROMOCIONES, page);

									Processor
											.notifyToView(
													getController(),
													ControllerProtocol.NOTIFICATION_DIALOG2,
													0, 0,
													"Sincronizando Promociones \npágina:"
															+ page.toString());
									page++;
								} else {
									synchronized (lock) {
										lock.notify();
									}
									break;
								}
								if (ON_ID_Handler != ID_SINCRONIZE_TODOS)
									Processor
											.notifyToView(getController(),
													NOTIFICATION, 0, 1,
													"Promociones fueron sincronizadas con exito.");

							}

						} catch (Exception e) {
							e.printStackTrace();
							try {
								Processor
										.notifyToView(
												getController(),
												ERROR,
												0,
												1,
												new ErrorMessage(
														"Error en la sincronización de clientes con el servidor",
														e.getMessage(),
														"\n Causa: "
																+ e.getCause()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				});

				Processor.notifyToView(getController(),
						ControllerProtocol.NOTIFICATION_DIALOG2, 0, 0,
						"Sincronizando Promociones");
			} else
				getController()._notifyOutboxHandlers(0, 0, 0, 0);

		} catch (Exception e) {

		}

	}
	
	private void sincronizarPendientesDeEnvio() {		
		try {
			BDevolucionM.EnviarDevolucion(NMApp.getContext()
					.getApplicationContext());
		} catch (Exception e1) {
		}
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void SINCRONIZE_TODOS() {
		try {			
			
			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					
					//Antes de sincronizar, enviar los documuentos pagados fuera de linea
					sincronizarPendientesDeEnvio();

					SINCRONIZE_PARAMETROS();
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					SINCRONIZE_CATALOGOSBASICOS();
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					SINCRONIZE_CLIENTES();
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					SINCRONIZE_PRODUCTOS();
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					SINCRONIZE_PROMOCIONES();
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						Processor.notifyToView(getController(),
								ControllerProtocol.NOTIFICATION, 0, 1,
								"Catalogos sincronizados exitosamente");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private synchronized void onSave_From_LocalHost(final JSONArray objL,
			final int ID, final int page) {

		try {

			NMApp.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						switch (ID) {
						case ID_SINCRONIZE_PARAMETROS:
							ModelConfiguracion.saveSystemParam(getContext(),
									objL);

							if (ON_ID_Handler != ID_SINCRONIZE_TODOS)
								Processor
										.notifyToView(
												getController(),
												ControllerProtocol.NOTIFICATION,
												0, 0,
												"Los Parametros del sistema fueron sincronizados con exito.");
							synchronized (lock) {
								try {
									lock.notify();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							break;
						case ID_SINCRONIZE_CATALOGOSBASICOS:
							ModelConfiguracion.saveValorCatalogoSystem(
									getContext(), objL);
							break;

						case ID_SINCRONIZE_CATALOGOSBASICOS2:
							ModelConfiguracion.saveTasasDeCambio(getContext(),
									objL);
							if (ON_ID_Handler != ID_SINCRONIZE_TODOS)
								Processor
										.notifyToView(
												getController(),
												ControllerProtocol.NOTIFICATION,
												0, 0,
												"Los Catalogos Básicos del sistema fueron sincronizados con exito.");
							synchronized (lock) {
								try {
									lock.notify();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							break;

						case ID_SINCRONIZE_CLIENTES:
							ModelCliente.saveClientes(objL, getContext(), page);
							break;

						case ID_SINCRONIZE_PRODUCTOS:
							ModelProducto.saveProductos(objL, getContext(),
									page);
							break;

						case ID_SINCRONIZE_PROMOCIONES:
							ModelConfiguracion.savePromociones(getContext(),
									objL, page);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						try {
							Processor
									.notifyToView(
											getController(),
											ERROR,
											0,
											1,
											new ErrorMessage(
													"Error interno salva guardando datos en la BDD",
													e.toString(), "\n Causa: "
															+ e.getCause()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

private void SaveLocalHost(vmConfiguracion c){
		
		try 
		{
			ModelConfiguracion.saveConfiguration(NMApp.getContext(),c);
			Processor.notifyToView(NMApp.getController(),ControllerProtocol.C_UPDATE_FINISHED,0, 0,"Se guardó la configuración correctamente.");
		} 
		catch (Exception e) 
		{
			try 
			{
				Processor
						.notifyToView(NMApp.getController(),
								ERROR,
								0,
								1,
								new ErrorMessage(
										"error en la comunicacion con el servidor",
										e.getMessage()
												+ "\r\n",
										""));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void saveLocalHost(Bundle b) {

		try {
			vmConfiguracion setting = vmConfiguracion.setConfiguration(
					b.get("URL").toString(), b.get("URL2").toString(),
					"0", // prefijo
					b.get("Empresa").toString(),
					null, // userinfo
					0, // maxIdPedido
					0, // maxIdRecibo
					0, //maxIdDevolucionV
					0, //maxIdDevolucionNV
					(b.getParcelable("impresora") != null) ? (Impresora) b
							.getParcelable("impresora") : null);
			ModelConfiguracion.saveConfiguration(NMApp.getContext(), setting);
			setting.setNameUser(b.get("LoginUsuario").toString());

			if (b.getBoolean("reset")) 
			{
				SessionManager.setValidPrefix(false);
				ModelConfiguracion.removeSharedPreference("LoginUser");
				SessionManager.setImpresora((b.getParcelable("impresora") != null) ? (Impresora) b.getParcelable("impresora") : null);
				SessionManager.setLoguedUser(null);				
				UserSessionManager.removeSession();
			}
			Processor.notifyToView(NMApp.getController(),
					ControllerProtocol.C_DATA, 0, 0,
					setting);
		} catch (Exception e) {
			try {
				Processor.notifyToView(
						NMApp.getController(),
						ERROR,
						0,
						1,
						new ErrorMessage(
								"error en la comunicacion con el servidor", e
										.getMessage() + "\r\n", ""));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
	}

}
