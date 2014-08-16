package com.panzyma.nm.auxiliar;

import java.util.ArrayList;

import android.content.Context;

import com.panzyma.nm.serviceproxy.Recibo;
import com.panzyma.nm.serviceproxy.ReciboDetFormaPago;

public class Cobro {

	public static float getTotalPagoRecibo(Recibo rcol) {
		if (rcol.getFormasPagoRecibo() == null)
			return 0;

		ArrayList<ReciboDetFormaPago> fp = rcol.getFormasPagoRecibo();
		if (fp == null)
			return 0;

		float totalPago = 0;
		for (int i = 0; i < fp.size(); i++)
			totalPago += fp.get(i).getMontoNacional();

		return StringUtil.round(totalPago, 2);
	}

	public static Object getParametro(Context cnt, String propiedad) {
		return ((cnt.getApplicationContext().getSharedPreferences(
				"SystemParams", android.content.Context.MODE_PRIVATE)
				.getString(propiedad, "--")));
	}

}
