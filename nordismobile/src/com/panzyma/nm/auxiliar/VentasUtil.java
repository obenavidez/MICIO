package com.panzyma.nm.auxiliar;

import android.content.Context;

public class VentasUtil {

	public static String getNumeroPedido(Context me, int numero) {
		int cr = Integer.parseInt(me
				.getApplicationContext()
				.getSharedPreferences("SystemParams",
						android.content.Context.MODE_PRIVATE)
				.getString("CerosRellenoNumRefPedido", "0"));

		char[] num = new char[cr];
		for (int i = 0; i < cr; i++)
			num[i] = '0';

		String strNum = new String(num);
		strNum = strNum + numero;
		return strNum.substring(strNum.length() - cr, strNum.length());
	}

}
