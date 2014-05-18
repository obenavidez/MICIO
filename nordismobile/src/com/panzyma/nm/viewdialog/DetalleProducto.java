package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.LOAD_DATA_FROM_LOCALHOST;

import java.util.ArrayList;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.PProducto;
import com.panzyma.nm.serviceproxy.PrecioProducto;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.viewdialog.DialogProducto.OnButtonClickListener;
import com.panzyma.nm.viewmodel.vmPProducto;
import com.panzyma.nordismobile.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class DetalleProducto extends Dialog implements Handler.Callback {

	private Context mcontext;
	private NMApp nmapp;

	EditText tboxcantidad;
	EditText tboxproducto;
	private CheckBox chkvprecio;
	private Button btnaceptar;
	private Button btncancelar;

	private OnButtonClickHandler mButtonClickListener;
	private Producto _producto;
	private long _idCategCliente;
	private long _idTipoPrecio;
	private boolean _nuevo;
	private boolean _exonerado;
	private CheckBox chkViaPrecio;
	private long _idTipoCliente;
	private DetallePedido _det;
	DialogProducto _dp;
	private EditText tboxPrecio;
	private EditText txtCantBonificada;
	private EditText tboxCantBonificada;
	private boolean _cambioCantidad;

	public interface OnButtonClickHandler {
		public abstract void onButtonClick(DetallePedido det_p, boolean btn);
	}

	public void setOnDialogDetalleProductButtonClickListener(
			OnButtonClickHandler listener) {
		mButtonClickListener = listener;
	}

	public DetalleProducto(Context cnt, Producto prod, long idCategCliente,
			long idTipoPrecio, long idTipoCliente, boolean exonerado) {
		super(cnt, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.detalle_prod_seleccionado);
		_producto = prod;
		_idCategCliente = idCategCliente;
		_idTipoPrecio = idTipoPrecio;
		_nuevo = true;
		_exonerado = exonerado;
		_idTipoCliente = idTipoCliente;
		mcontext = this.getContext();
		initComponents();

	}

	public boolean getCambioCantidad() {
		return _cambioCantidad;
	}

	//
	private void initComponents() {
		// Via precio se habilita si codigo de tipo de cliente al que se está
		// facturando es igual
		// al parámetro CodTipoClienteMayorista
		if (_idTipoCliente == Long.valueOf(mcontext.getSharedPreferences(
				"SystemParams", android.content.Context.MODE_PRIVATE)
				.getString("IdTipoClienteMayorista", "0"))) {
			chkViaPrecio = ((CheckBox) findViewById(R.id.chkviaprecio));
			chkViaPrecio.setVisibility(android.view.View.VISIBLE);
		}
		this.setCancelable(true);
		tboxcantidad = ((EditText) findViewById(R.id.etcantidad));
		tboxproducto = ((EditText) findViewById(R.id.et_producto));
		tboxPrecio = ((EditText) findViewById(R.id.et_precio));
		tboxCantBonificada = ((EditText) findViewById(R.id.etbonif));
		tboxproducto.setText(_producto.getNombre());

		btnaceptar = ((Button) findViewById(R.id.btn_ok));
		btnaceptar.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{

				if (isValidInformation()) 
				{

					int nuevaCantidadOrdenada = Integer.parseInt(tboxcantidad
							.getText().toString());
					if (_nuevo) {
						_det = new DetallePedido();
						_det.setObjProductoID(_producto.getId());
						_det.setCodProducto(_producto.getCodigo());
						_det.setNombreProducto(_producto.getNombre());
					}
					if (!_nuevo) {

						if (nuevaCantidadOrdenada != _det.getCantidadOrdenada()) {
							_cambioCantidad = true;

							// Recalcular la bonificación
							_det.setCantidadBonificada(0);
							_det.setCantidadBonificadaEditada(0);
							_det.setObjBonificacionID(0);
							_det.setBonifEditada(false);

							if ((chkViaPrecio == null)
									|| (!chkViaPrecio.isChecked())) {
								Bonificacion b = getBonificacion(_producto,
										_idCategCliente, nuevaCantidadOrdenada);
								if (b != null) {
									_det.setCantidadBonificada(b
											.getCantBonificacion());
									_det.setCantidadBonificadaEditada(b
											.getCantBonificacion());
									_det.setObjBonificacionID(b
											.getObjBonificacionID());
									_det.setBonifEditada(false);
								}
							}

							// Recalcular precio
							long idTP = _idTipoPrecio;
							if ((chkViaPrecio != null)
									&& (!chkViaPrecio.isChecked()))
								idTP = mcontext.getSharedPreferences(
										"SystemParams",
										android.content.Context.MODE_PRIVATE)
										.getLong("IdTipoPrecioGeneral", 0);

							float precio = getPrecioProducto(_producto, idTP,
									nuevaCantidadOrdenada);
							_det.setPrecio(precio);
							_det.setMontoPrecioEditado(precio);
							_det.setPrecioEditado(false);
						} else {
							float precio = Float.valueOf(tboxPrecio.getText()
									.toString());
							if (precio == 0F) {
								// Dialog.alert("El precio debe ser mayor que cero.");
								return;
							}

							// Validar que la edición de la cantidad bonificada
							// sea válida
							int nuevaBonif = Integer
									.parseInt(tboxCantBonificada.getText()
											.toString());

							if (!mcontext.getSharedPreferences("LoginUser",
									android.content.Context.MODE_PRIVATE)
									.getBoolean("isPuedeEditarBonifAbajo",
											false)) {
								int bonifProd = 0;
								if ((chkViaPrecio == null)
										|| (!chkViaPrecio.isChecked())) {
									Bonificacion b = getBonificacion(_producto,
											_idCategCliente,
											nuevaCantidadOrdenada);
									if (b != null)
										bonifProd = b.getCantBonificacion();
								}

								if (nuevaBonif < bonifProd) {
									tboxCantBonificada.setText(_det
											.getCantidadBonificadaEditada()
											+ "");
									// Dialog.alert("Permisos insuficientes para bajar la cantidad bonificada.");
									return;
								}
							}

							if (!mcontext.getSharedPreferences("LoginUser",
									android.content.Context.MODE_PRIVATE)
									.getBoolean("isPuedeEditarBonifArriba",
											false)) {
								int bonifProd = 0;
								if ((chkViaPrecio == null)
										|| (!chkViaPrecio.isChecked())) {
									Bonificacion b = getBonificacion(_producto,
											_idCategCliente,
											nuevaCantidadOrdenada);
									if (b != null)
										bonifProd = b.getCantBonificacion();
								}

								if (nuevaBonif > bonifProd) {
									tboxCantBonificada.setText(_det
											.getCantidadBonificadaEditada()
											+ "");
									// Dialog.alert("Permisos insuficientes para subir la cantidad bonificada.");
									return;
								}
							}

							// Validar que la edición del precio sea válida
							if (!mcontext.getSharedPreferences("LoginUser",
									android.content.Context.MODE_PRIVATE)
									.getBoolean("isPuedeEditarPrecioAbajo",
											false)) {
								// Calcular precio
								long idTP = _idTipoPrecio;
								if ((chkViaPrecio != null)
										&& (!chkViaPrecio.isChecked()))
									idTP = Long
											.parseLong(mcontext
													.getSharedPreferences(
															"SystemParams",
															android.content.Context.MODE_PRIVATE)
													.getString(
															"IdTipoPrecioGeneral",
															"0"));
								float precioProd = getPrecioProducto(_producto,
										idTP, nuevaCantidadOrdenada);

								if (precio < precioProd) {
									tboxPrecio.setText(_det
											.getMontoPrecioEditado() + "");
									// Dialog.alert("Permisos insuficientes para bajar el precio.");
									return;
								}
							}

							if (!mcontext.getSharedPreferences("LoginUser",
									android.content.Context.MODE_PRIVATE)
									.getBoolean("isPuedeEditarPrecioArriba",
											false)) {
								// Calcular precio
								long idTP = _idTipoPrecio;
								if ((chkViaPrecio != null)
										&& (!chkViaPrecio.isChecked()))
									idTP = Long
											.parseLong(mcontext
													.getSharedPreferences(
															"SystemParams",
															android.content.Context.MODE_PRIVATE)
													.getString(
															"IdTipoPrecioGeneral",
															"0"));
								float precioProd = getPrecioProducto(_producto,
										idTP, nuevaCantidadOrdenada);

								if (precio > precioProd) {
									tboxPrecio.setText(_det
											.getMontoPrecioEditado() + "");
									// Dialog.alert("Permisos insuficientes para subir el precio.");
									return;
								}
							}

							_det.setCantidadBonificada(nuevaBonif);
							_det.setCantidadBonificadaEditada(Integer
									.parseInt(tboxCantBonificada.getText()
											.toString()));
							_det.setPrecio(precio);
							_det.setMontoPrecioEditado(precio);
						}

					} else {
						// Calcular la bonificación
						_det.setCantidadBonificada(0);
						_det.setCantidadBonificadaEditada(0);
						_det.setObjBonificacionID(0);
						_det.setBonifEditada(false);

						if ((chkViaPrecio == null)
								|| (!chkViaPrecio.isChecked())) {
							Bonificacion b = getBonificacion(_producto,
									_idCategCliente, nuevaCantidadOrdenada);
							if (b != null) {
								_det.setCantidadBonificada(b
										.getCantBonificacion());
								_det.setCantidadBonificadaEditada(b
										.getCantBonificacion());
								_det.setObjBonificacionID(b
										.getObjBonificacionID());
								_det.setBonifEditada(false);
							}
						}

						// Calcular precio
						long idTP = _idTipoPrecio;
						if ((chkViaPrecio != null)
								&& (!chkViaPrecio.isChecked()))
							idTP = Long
									.parseLong(mcontext
											.getSharedPreferences(
													"SystemParams",
													android.content.Context.MODE_PRIVATE)
											.getString("IdTipoPrecioGeneral",
													"0"));

						float precio = getPrecioProducto(_producto, idTP,
								nuevaCantidadOrdenada);
						_det.setPrecio(precio);
						_det.setMontoPrecioEditado(precio);
						_det.setPrecioEditado(false);
					}

					_det.setCantidadOrdenada(nuevaCantidadOrdenada);

					_det.setBonifEditada(false);
					_det.setPrecioEditado(false);
					if (_det.getCantidadBonificada() != _det
							.getCantidadBonificadaEditada())
						_det.setBonifEditada(true);
					if (_det.getPrecio() != _det.getMontoPrecioEditado())
						_det.setPrecioEditado(true);

					_det.setDescuento(0F);
					_det.setImpuesto(0F);
					_det.setPorcImpuesto(0F);
					_det.setSubtotal(_det.getCantidadOrdenada()
							* _det.getPrecio());
					if (_producto.isEsGravable() && !_exonerado) {

						float prcImp = Float.valueOf((mcontext
								.getSharedPreferences("SystemParams",
										android.content.Context.MODE_PRIVATE)
								.getString("PorcentajeImpuesto", "0")));
						_det.setImpuesto(_det.getSubtotal() * prcImp / 100);
						_det.setPorcImpuesto(prcImp);
					}
					_det.setTotal(_det.getSubtotal() + _det.getImpuesto()
							- _det.getDescuento());
					if(((!_nuevo) &&_cambioCantidad) || _nuevo)
						mButtonClickListener.onButtonClick(_det, true);
					dismiss();
				}
			}

		});
		btncancelar = ((Button) findViewById(R.id.btn_cancel));
		btncancelar.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
			}

		});

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	public static ArrayList<Bonificacion> parseListaBonificaciones(
			Producto prod, long idCatCliente) {
		if (prod.getListaBonificaciones() == null)
			return null;
		if (prod.getListaBonificaciones() == "")
			return null;

		ArrayList<Bonificacion> abon = new ArrayList<Bonificacion>();
		String catCLiente = idCatCliente + "";
		String[] listaBonif = StringUtil.split(prod.getListaBonificaciones(),
				"|");
		for (int i = 0; i < listaBonif.length; i++) {
			String[] bonifProd = StringUtil.split(listaBonif[i], ",");

			if ((idCatCliente == 0)
					|| (catCLiente.compareTo(bonifProd[1]) == 0)) {
				Bonificacion b = new Bonificacion();
				b.setObjBonificacionID(Long.parseLong(bonifProd[0]));
				b.setObjCategoriaClienteID(idCatCliente);
				b.setCantidad(Integer.parseInt(bonifProd[2]));
				b.setCantBonificacion(Integer.parseInt(bonifProd[3]));
				b.setCategoriaCliente(bonifProd[4]);
				abon.add(b);
			}
		}

		return abon;
	}

	/*
	 * Devuelve la bonificación de un producto dada la cantidad que se está
	 * ordenando y la categoría de cliente
	 */
	public static Bonificacion getBonificacion(Producto prod,
			long idCatCliente, int cantidad) {
		ArrayList<Bonificacion> bonificaciones = parseListaBonificaciones(prod,
				idCatCliente);
		if (bonificaciones == null)
			return null;

		Bonificacion bb = null;
		for (int i = bonificaciones.size() - 1; i >= 0; i--) {
			Bonificacion b = (Bonificacion) bonificaciones.get(i);
			if (cantidad >= b.getCantidad()) {
				bb = b; // Encontrada
				break; // Salir del ciclo
			}
		}

		// Si se encontró bonificación aplicada
		// Recalcular cantidad real a aplicar para la cantidad ordenada
		// Recordar traer valor de parámetro DecimalRedondeoBonif (por el
		// momento se usa 0.8)
		if (bb != null) {
			float decimalRedondeoBonif = 0.8F;
			float cb = bb.getCantBonificacion();
			float c = bb.getCantidad();
			float factor = cb / c;
			float cant = factor * cantidad;
			String sCant = String.valueOf(cant);
			String[] arrCant = StringUtil.split(sCant, ".");
			String sInt = arrCant[0];
			String sDec = "0.0";

			int cantReal = Integer.parseInt(sInt);
			if (arrCant.length > 1) {
				sDec = "0." + arrCant[1];
				float decimalPart = Float.parseFloat(sDec);
				if (decimalPart >= decimalRedondeoBonif)
					cantReal = cantReal + 1;
			}
			bb.setCantBonificacion(cantReal);
		}

		return bb;
	}

	public static ArrayList<PrecioProducto> parseListaPrecios(Producto prod,
			long idTipoPrecio) {
		ArrayList<PrecioProducto> pp = new ArrayList<PrecioProducto>();
		String tipoPrecio = idTipoPrecio + "";
		String[] listaPrecios = StringUtil.split(prod.getListaPrecios(), "|");
		for (int i = 0; i < listaPrecios.length; i++) {
			String[] precioProd = StringUtil.split(listaPrecios[i], ",");

			if ((idTipoPrecio == 0)
					|| (tipoPrecio.compareTo(precioProd[0]) == 0)) {
				PrecioProducto p = new PrecioProducto();
				p.setObjTipoPrecioID(idTipoPrecio);
				p.setMinimo(Integer.parseInt(precioProd[1]));
				p.setMaximo(Integer.parseInt(precioProd[2]));
				p.setPrecio(Float.parseFloat(precioProd[3]));
				p.setDescTipoPrecio(precioProd[4]);
				pp.add(p);
			}
		}

		return pp;
	}

	/*
	 * Devuelve el precio de un producto dada la cantidad que se está ordenando
	 */
	public static float getPrecioProducto(Producto prod, long idTipoPrecio,
			int cantidad) {
		ArrayList<PrecioProducto> precios = parseListaPrecios(prod,
				idTipoPrecio);
		PrecioProducto p = (PrecioProducto) precios.get(0);
		if (precios.size() > 1) {
			for (int i = 0; i < precios.size(); i++) {
				p = (PrecioProducto) precios.get(i);
				if ((cantidad >= p.getMinimo()) && (cantidad <= p.getMaximo()))
					break; // Salir del ciclo
			}
		}

		return p.getPrecio();
	}

	public boolean isValidInformation() {
		if (tboxproducto.getText().toString().trim().length() == 0) {
			tboxproducto
					.setError("Debe haber primero seleccionado un producto.");
			tboxproducto.requestFocus();
			return false;
		} else if (tboxcantidad.getText().toString().trim().length() == 0) {
			tboxcantidad
					.setError("La Cantidad a comprar debe ser mayor que cero");
			tboxcantidad.requestFocus();
			return false;
		}
		return true;
	}

}
