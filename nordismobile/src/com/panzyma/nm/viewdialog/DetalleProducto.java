package com.panzyma.nm.viewdialog;

import static com.panzyma.nm.controller.ControllerProtocol.NOTIFICATION_DIALOG;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.auxiliar.StringUtil;
import com.panzyma.nm.logic.DevolucionBL;
import com.panzyma.nm.serviceproxy.Bonificacion;
import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.PrecioProducto;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.serviceproxy.Usuario;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nordismobile.R;

public class DetalleProducto extends Dialog{

	private ViewPedidoEdit parent; 
	EditText tboxdisponible;
	EditText tboxcantidad;
	EditText tboxproducto;
	private CheckBox chkvprecio;
	private Button btnaceptar;
	private Button btncancelar;
 
	private CustomDialog dlg;
	
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
	private Usuario usuario;
	
	public interface OnButtonClickHandler {
		public abstract void onButtonClick(DetallePedido det_p, boolean btn);
	}

	public void setOnDialogDetalleProductButtonClickListener(
			OnButtonClickHandler listener) {
		mButtonClickListener = listener;
	}

	public DetalleProducto(ViewPedidoEdit cnt, Producto prod, long idCategCliente,
			long idTipoPrecio, long idTipoCliente, boolean exonerado) {
		super(cnt, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.detalle_prod_seleccionado);
		_producto = prod;
		_idCategCliente = idCategCliente;
		_idTipoPrecio = idTipoPrecio;
		_nuevo = true;
		_exonerado = exonerado;
		_idTipoCliente = idTipoCliente;
		parent =cnt;
		usuario=SessionManager.getLoginUser();
		initComponents();		
	}

	public DetalleProducto(ViewPedidoEdit cnt, Producto prod,DetallePedido det, long idCategCliente,
			long idTipoPrecio, long idTipoCliente, boolean exonerado) {
		super(cnt, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.detalle_prod_seleccionado);
		_producto = prod;
		_idCategCliente = idCategCliente;
		_idTipoPrecio = idTipoPrecio; 
		_exonerado = exonerado;
		_idTipoCliente = idTipoCliente;
		parent = cnt;
		_det=det;
		usuario=SessionManager.getLoginUser();
		initComponents();
		
	} 
	
	public boolean getCambioCantidad() {
		return _cambioCantidad;
	}

	//
	private void initComponents()
	{
		this.setCancelable(true);
		
		// Via precio se habilita si codigo de tipo de cliente al que se está
		// facturando es igual
		// al parámetro CodTipoClienteMayorista
		if (_idTipoCliente == Long.valueOf(parent.getSharedPreferences("SystemParams", android.content.Context.MODE_PRIVATE).getString("IdTipoClienteMayorista", "0"))) 
		{
			chkViaPrecio = ((CheckBox) findViewById(R.id.chkviaprecio));
			chkViaPrecio.setVisibility(android.view.View.VISIBLE);
		}		
		
		tboxproducto = ((EditText) findViewById(R.id.et_producto));
		tboxproducto.setText(_producto.getNombre());
		
		tboxcantidad = ((EditText) findViewById(R.id.etcantidad));
		tboxCantBonificada = ((EditText) findViewById(R.id.etbonif));
		((TextView) findViewById(R.id.tv_precio)).setVisibility(View.VISIBLE);
		tboxPrecio = ((EditText) findViewById(R.id.et_precio));
		tboxPrecio.setVisibility(View.VISIBLE);
		tboxPrecio.setEnabled(false);
		// Recalcular precio
		long idTP = _idTipoPrecio;
		if ((chkViaPrecio != null) && (!chkViaPrecio.isChecked()))
			idTP = parent.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getLong("IdTipoPrecioGeneral", 0);

		float precio = getPrecioProducto(_producto, idTP,0); 
		tboxPrecio.setText(precio+"");
		if(_det!=null)
		{
			tboxcantidad.setText(""+_det.getCantidadOrdenada());
			tboxCantBonificada.setText(""+_det.getCantidadBonificadaEditada());
			tboxPrecio.setText(""+_det.getMontoPrecioEditado());
			tboxPrecio.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tvbonif)).setVisibility(View.VISIBLE);
			tboxCantBonificada.setVisibility(View.VISIBLE);
			tboxCantBonificada.setEnabled(usuario.isPuedeEditarBonifAbajo()|| usuario.isPuedeEditarBonifArriba());
			tboxPrecio.setEnabled(usuario.isPuedeEditarPrecioAbajo() || usuario.isPuedeEditarPrecioArriba());
		}
		 
		tboxdisponible = ((EditText) findViewById(R.id.etdisponible));
		tboxdisponible.setText(""+_producto.Disponible);
		
		btnaceptar = ((Button) findViewById(R.id.btn_ok));
		btnaceptar.setOnClickListener(clicklistener);
		btncancelar = ((Button) findViewById(R.id.btn_cancel));
		btncancelar.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
			}

		});

	}

	@Override
	public void dismiss() 
   	{   
		super.dismiss();
	}
	
	
	android.view.View.OnClickListener clicklistener=new android.view.View.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{

			if (isValidInformation()) 
			{

				int nuevaCantidadOrdenada = Integer.parseInt(tboxcantidad.getText().toString());
				 
				if (_nuevo) 
				{
					_det = new DetallePedido();
					_det.setObjProductoID(_producto.getId());
					_det.setCodProducto(_producto.getCodigo());
					_det.setNombreProducto(_producto.getNombre());
				}
				if (!_nuevo) 
				{

					if (nuevaCantidadOrdenada != _det.getCantidadOrdenada())
					{
						_cambioCantidad = true;

						// Recalcular la bonificación
						_det.setCantidadBonificada(0);
						_det.setCantidadBonificadaEditada(0);
						_det.setObjBonificacionID(0);
						_det.setBonifEditada(false);

						if ((chkViaPrecio == null) || (!chkViaPrecio.isChecked())) 
						{
							//Bonificacion b=DevolucionBL.getBonificacion3(_producto, _idCategCliente, nuevaCantidadOrdenada);
							Bonificacion b = getBonificacion(_producto,_idCategCliente, nuevaCantidadOrdenada);
							if (b != null) 
							{
								_det.setCantidadBonificada(b.getCantBonificacion());
								_det.setCantidadBonificadaEditada(b.getCantBonificacion());
								_det.setObjBonificacionID(b.getObjBonificacionID());
								_det.setBonifEditada(false);
							}
						}

						// Recalcular precio
						long idTP = _idTipoPrecio;
						if ((chkViaPrecio != null) && (!chkViaPrecio.isChecked()))
							idTP = parent.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getLong("IdTipoPrecioGeneral", 0);

						float precio = getPrecioProducto(_producto, idTP,nuevaCantidadOrdenada);
						_det.setPrecio(precio);
						_det.setMontoPrecioEditado(precio);
						_det.setPrecioEditado(false);
					}
					else 
					{
						float precio = Float.valueOf(tboxPrecio.getText().toString());
						if (precio == 0F) {
							showStatus("El precio debe ser mayor que cero.", true); 
							return;
						}

						// Validar que la edición de la cantidad bonificada
						// sea válida
						int nuevaBonif = Integer.parseInt(tboxCantBonificada.getText().toString());

						if (!parent.getSharedPreferences("LoginUser",android.content.Context.MODE_PRIVATE).getBoolean("isPuedeEditarBonifAbajo",false)) 
						{
							int bonifProd = 0;
							if ((chkViaPrecio == null)|| (!chkViaPrecio.isChecked())) 
							{
								//Bonificacion b=DevolucionBL.getBonificacion3(_producto, _idCategCliente, nuevaCantidadOrdenada);
								Bonificacion b = getBonificacion(_producto,_idCategCliente,nuevaCantidadOrdenada);
								if (b != null)
									bonifProd = b.getCantBonificacion();
							}

							if (nuevaBonif < bonifProd) 
							{
								tboxCantBonificada.setText(_det.getCantidadBonificadaEditada()+ "");
								showStatus("Permisos insuficientes para bajar la cantidad bonificada.", true); 
								return;
							}
						}

						if (!parent.getSharedPreferences("LoginUser",android.content.Context.MODE_PRIVATE).getBoolean("isPuedeEditarBonifArriba",false)) 
						{
							int bonifProd = 0;
							if ((chkViaPrecio == null) || (!chkViaPrecio.isChecked())) 
							{
								//Bonificacion b=DevolucionBL.getBonificacion3(_producto, _idCategCliente, nuevaCantidadOrdenada);
								Bonificacion b = getBonificacion(_producto,_idCategCliente,nuevaCantidadOrdenada);
								if (b != null)
									bonifProd = b.getCantBonificacion();
							}

							if (nuevaBonif > bonifProd) 
							{
								tboxCantBonificada.setText(_det.getCantidadBonificadaEditada()+ "");
								showStatus("Permisos insuficientes para subir la cantidad bonificada.", true); 
								return;
							}
						}

						// Validar que la edición del precio sea válida
						if (!parent.getSharedPreferences("LoginUser",android.content.Context.MODE_PRIVATE).getBoolean("isPuedeEditarPrecioAbajo",false)) 
						{
							// Calcular precio
							long idTP = _idTipoPrecio;
							if ((chkViaPrecio != null) && (!chkViaPrecio.isChecked()))
								idTP = Long.parseLong(parent.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("IdTipoPrecioGeneral","0"));
							float precioProd = getPrecioProducto(_producto,idTP, nuevaCantidadOrdenada);

							if (precio < precioProd) 
							{
								tboxPrecio.setText(_det.getMontoPrecioEditado() + "");
								showStatus("Permisos insuficientes para bajar el precio.", true);  
								return;
							}
						}

						if (!parent.getSharedPreferences("LoginUser",android.content.Context.MODE_PRIVATE).getBoolean("isPuedeEditarPrecioArriba",false)) 
						{
							// Calcular precio
							long idTP = _idTipoPrecio;
							if ((chkViaPrecio != null) && (!chkViaPrecio.isChecked()))
								idTP = Long.parseLong(parent.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("IdTipoPrecioGeneral","0"));
							float precioProd = getPrecioProducto(_producto,idTP, nuevaCantidadOrdenada);

							if (precio > precioProd) 
							{
								tboxPrecio.setText(_det.getMontoPrecioEditado() + "");
								showStatus("Permisos insuficientes para subir el precio.", true);  
								return;
							}
						}

						_det.setCantidadBonificada(nuevaBonif);
						_det.setCantidadBonificadaEditada(Integer.parseInt(tboxCantBonificada.getText().toString()));
						_det.setPrecio(precio);
						_det.setMontoPrecioEditado(precio);
					}

				} 
				else 
				{
					// Calcular la bonificación
					_det.setCantidadBonificada(0);
					_det.setCantidadBonificadaEditada(0);
					_det.setObjBonificacionID(0);
					_det.setBonifEditada(false);

					if ((chkViaPrecio == null)|| (!chkViaPrecio.isChecked())) 
					{
						//Bonificacion b=DevolucionBL.getBonificacion3(_producto, _idCategCliente, nuevaCantidadOrdenada);
						Bonificacion b = getBonificacion(_producto,_idCategCliente, nuevaCantidadOrdenada);
						if (b != null) 
						{
							_det.setCantidadBonificada(b.getCantBonificacion());
							_det.setCantidadBonificadaEditada(b.getCantBonificacion());
							_det.setObjBonificacionID(b.getObjBonificacionID());
							_det.setBonifEditada(false);
						}
					}

					// Calcular precio
					long idTP = _idTipoPrecio;
					if ((chkViaPrecio != null) && (!chkViaPrecio.isChecked()))
						idTP = Long.parseLong(parent.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("IdTipoPrecioGeneral","0"));

					float precio = getPrecioProducto(_producto, idTP,nuevaCantidadOrdenada);
					_det.setPrecio(precio);
					_det.setMontoPrecioEditado(precio);
					_det.setPrecioEditado(false);
				}

				_det.setCantidadOrdenada(nuevaCantidadOrdenada);

				_det.setBonifEditada(false);
				_det.setPrecioEditado(false);
				if (_det.getCantidadBonificada() != _det.getCantidadBonificadaEditada())
						_det.setBonifEditada(true);
				if (_det.getPrecio() != _det.getMontoPrecioEditado())
						_det.setPrecioEditado(true);

				_det.setDescuento(0F);
				_det.setImpuesto(0F);
				_det.setPorcImpuesto(0F);
				_det.setSubtotal(_det.getCantidadOrdenada() * _det.getPrecio());
				if (_producto.isEsGravable() && !_exonerado) 
				{

					float prcImp = Float.valueOf((parent.getSharedPreferences("SystemParams",android.content.Context.MODE_PRIVATE).getString("PorcentajeImpuesto", "0")));
					_det.setImpuesto(_det.getSubtotal() * prcImp / 100);
					_det.setPorcImpuesto(prcImp);
				}
				_det.setTotal(_det.getSubtotal() + _det.getImpuesto() - _det.getDescuento());
				if(((!_nuevo) && _cambioCantidad) ||  _nuevo)
					mButtonClickListener.onButtonClick(_det, true);
				dismiss();
			}
		}
	};
	
	public void ocultarDialogos() {
		if (dlg != null && dlg.isShowing())
			dlg.dismiss(); 
	}
	
	public void showStatus(final String mensaje, boolean... confirmacion) {

		ocultarDialogos();
		if (confirmacion.length != 0 && confirmacion[0]) {
			parent.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AppDialog.showMessage(parent, "", mensaje,
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
			parent.runOnUiThread(new Runnable() {
				

				@Override
				public void run() {
					dlg = new CustomDialog(parent, mensaje, false,
							NOTIFICATION_DIALOG);
					dlg.show();
				}
			});
		}
	} 

	public static ArrayList<Bonificacion> parseListaBonificaciones(
			Producto prod, long idCatCliente) 
			{
		if (prod.getListaBonificaciones() == null)
			return null;
		if (prod.getListaBonificaciones() == "")
			return null;

		ArrayList<Bonificacion> abon = new ArrayList<Bonificacion>();
		String catCLiente = idCatCliente + "";
		String[] listaBonif = StringUtil.split(prod.getListaBonificaciones(),
				"|");
		for (int i = 0; i < listaBonif.length; i++)
		{
			if(!listaBonif[0].equals(""))
			{	
				String[] bonifProd = StringUtil.split(listaBonif[i], ",");
		
				if(bonifProd!=null && bonifProd.length!=0 && bonifProd[0]!="")
				if ((idCatCliente == 0) || (catCLiente.compareTo(bonifProd[1]) == 0)) {
					Bonificacion b = new Bonificacion();
					b.setObjBonificacionID(Long.parseLong(bonifProd[0]));
					b.setObjCategoriaClienteID(idCatCliente);
					b.setCantidad(Integer.parseInt(bonifProd[2]));
					b.setCantBonificacion(Integer.parseInt(bonifProd[3]));
					b.setCategoriaCliente(bonifProd[4]);
					abon.add(b);
				}
			}
			else
				break;
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
			Bonificacion b = bonificaciones.get(i);
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
			int cantidad) 
	{
		ArrayList<PrecioProducto> precios = parseListaPrecios(prod,idTipoPrecio); 
		PrecioProducto p =(precios!=null && precios.size()!=0)? precios.get(0):null ;
		if (precios.size() > 1) 
		{
			for (int i = 0; i < precios.size(); i++) 
			{
				p = precios.get(i);
				if ((cantidad >= p.getMinimo()) && (cantidad <= p.getMaximo()))
					break; // Salir del ciclo
			}
		}

		return (p!=null)?p.getPrecio():null;
	}

	public boolean isValidInformation() {
		if (tboxproducto.getText().toString().trim().length() == 0) {
			tboxproducto
					.setError("Debe haber primero seleccionado un producto.");
			tboxproducto.requestFocus();
			return false;
		} else if (tboxcantidad.getText().toString().trim().length() == 0 || Integer.parseInt(tboxcantidad.getText().toString().trim())== 0) {
			tboxcantidad
					.setError("La Cantidad a comprar debe ser mayor que cero");
			tboxcantidad.requestFocus();
			return false;
		}else if (Integer.parseInt(tboxcantidad.getText().toString())>_producto.Disponible) {
			tboxcantidad.setError("La cantidad a ordernar debe ser menor a la cantidad existente("+_producto.Disponible+")");
			tboxcantidad.requestFocus();
			return false;
		} 
		
		return true;
	}

}
