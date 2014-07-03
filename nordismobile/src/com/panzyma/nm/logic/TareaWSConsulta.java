package com.panzyma.nm.logic;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.panzyma.nm.serviceproxy.Pedido;
import android.os.AsyncTask;
import android.util.Log;

//Tarea Asíncrona para llamar al WS de consulta en segundo plano
public class TareaWSConsulta extends AsyncTask<String, Integer, Boolean> {

	private String credenciales;
	private Pedido pedido;

	public TareaWSConsulta(String credenciales, Pedido pedido) {
		this.credenciales = credenciales;
		this.pedido = pedido;
	}

	protected Boolean doInBackground(String... params) {

		boolean resul = true;

		final String NAMESPACE = "http://panzyma.com/";
		final String URL = "http://panzyma.com/nordisserverdev/mobileservice.asmx";
		final String METHOD_NAME = "EnviarPedido";
		final String SOAP_ACTION = "http://panzyma.com/EnviarPedido";

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		PropertyInfo cr = new PropertyInfo();
		cr.setName("Credentials");
		cr.setValue(this.credenciales);
		cr.setType(String.class);
		request.addProperty(cr);
		
		PropertyInfo p = new PropertyInfo();
        p.setName("pedido");
        p.setValue(pedido);
        p.setType(pedido.getClass()); 
        request.addProperty(p);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		
		envelope.addMapping(NAMESPACE, "Pedido", pedido.getClass());

		HttpTransportSE transporte = new HttpTransportSE(URL);

		try {
			transporte.call(SOAP_ACTION, envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();

			Log.i("Resultado", resSoap.toString());

		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			resul = false;
		}

		return resul;
	}

	protected void onPostExecute(Boolean result) {

		if (result) {

		} else {

		}
	}
}