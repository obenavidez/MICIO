package com.panzyma.nm.auxiliar;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Moneda {

	private static Locale locale = new Locale("es", "NI");

	private Moneda() {
	}

	public static String getSimbol() {
		//return Currency.getInstance(locale).getCurrencyCode();
		return "$C";
	}

	public static Locale getLocale() {
		return locale;
	}

	public static String toCurrency(double valor) {
		return getSimbol() + " "
				+ String.format(Moneda.getLocale(), "%,.2f", valor);
	}

}
