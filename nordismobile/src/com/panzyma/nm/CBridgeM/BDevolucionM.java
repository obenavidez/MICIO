package com.panzyma.nm.CBridgeM;

import static com.panzyma.nm.controller.ControllerProtocol.ERROR;

import java.util.HashMap;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.Processor;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;
import com.panzyma.nm.model.ModelDevolucion;
import com.panzyma.nm.model.ModelLogic;
import com.panzyma.nm.model.ModelPedido;
import com.panzyma.nm.model.ModelSolicitudDescuento;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Devolucion;
import com.panzyma.nm.serviceproxy.Pedido;

import android.os.Message;
import android.util.Log;

@SuppressWarnings("unchecked")
public class BDevolucionM extends BBaseM {

	private String TAG = BDevolucionM.class.getSimpleName();

	@Override
	public boolean handleMessage(Message msg) throws Exception {
		switch (msg.what) {
		case ControllerProtocol.OBTENERVALORCATALOGO:
			ObtenerValorCatalogo((String[]) msg.obj);
			break;
		case ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO:
			BuscarDevolucionDePedido(msg.obj);
			break;
		case ControllerProtocol.LOAD_PEDIDOS_FROM_LOCALHOST:
			ObtenerPedidosLocalmente(Long.valueOf(msg.getData()
					.get("objSucursalID").toString()));
			break;
		default:
			break;
		}
		return false;
	}

	private void ObtenerValorCatalogo(String... valores) {
		try {
			Processor.notifyToView(getController(),
					ControllerProtocol.OBTENERVALORCATALOGO, 0, 0,
					ModelLogic.getValorCatalogo(valores));
		} catch (Exception e) {
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(
						getController(),
						ERROR,
						0,
						0,
						new ErrorMessage(
								"Error interno trayendo datos desde BDD", e
										.toString(), "\n Causa: "
										+ e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void ObtenerPedidosLocalmente(long objSucursalID) {
		try {
			Processor
					.notifyToView(
							getController(),
							ControllerProtocol.ID_REQUEST_OBTENERPEDIDOS,
							0,
							0,
							ModelPedido
									.obtenerPedidosFacturados(new long[] { objSucursalID }));
		} catch (Exception e) {
			Log.e(TAG, "Error interno trayendo datos desde BDD", e);
			try {
				Processor.notifyToView(
						getController(),
						ERROR,
						0,
						0,
						new ErrorMessage(
								"Error interno trayendo datos desde BDD", e
										.toString(), "\n Causa: "
										+ e.getCause()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void BuscarDevolucionDePedido(Object obj) {
		HashMap<String, Long> parametros = (HashMap<String, Long>) obj;
		long idsucursal, nopedido, nofactura;
		idsucursal = parametros.get("idsucursal");
		nopedido = parametros.get("nopedido");
		nofactura = parametros.get("nofactura");

		String credenciales = "";
		Devolucion dev;
		Pedido pedido;
		credenciales = SessionManager.getCredentials();
		if (credenciales == "")
			return;
		try 
		{
			dev = ModelDevolucion.BuscarDevolucionDePedido(credenciales,
					idsucursal, nopedido, nofactura);
			pedido = obtenerPedido(dev.getObjPedidoDevueltoID());
			if (pedido != null)
				dev.setObjPedido(pedido);

			Processor.notifyToView(getController(),
					ControllerProtocol.BUSCARDEVOLUCIONDEPEDIDO, 0, 0, dev);

		} catch (Exception e) {
			Log.e(TAG, "Error interno trayendo datos desde el servidor", e);
			try {
				Processor
						.notifyToView(
								getController(),
								ERROR,
								0,
								0,
								new ErrorMessage(
										"Error interno trayendo datos desde el servidor",
										""
												+ ((e != null && e.toString() != null) ? e
														.toString() : ""),
										"\n Causa: "
												+ ""
												+ ((e != null && e.getCause() != null) ? e
														.getCause() : "")));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}
 
	private Pedido obtenerPedido(final long idPedido) throws Exception {
		return ModelPedido.getPedido(SessionManager.getCredentials(),
				idPedido);
	}

}
