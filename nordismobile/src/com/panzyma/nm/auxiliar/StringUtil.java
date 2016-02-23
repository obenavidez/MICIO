package com.panzyma.nm.auxiliar;

import java.util.*;

/**
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StringUtil {
	StringUtil() {
	}

	public static String formatInt(long number) {
		return formatInt(String.valueOf(number));
	}	
	
	public static String[] formatClienteSucursal(String cadena) {
		String[] _cadena = new String[2];
		_cadena[0]=cadena.substring(0,cadena.indexOf("/"));
		_cadena[1]=cadena.substring(cadena.indexOf("/"),cadena.length());
		return _cadena;
	}	

	private static String formatInt(String str) {
		if (str.length() < 4)
			return str;
		return formatInt(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	public static String formatReal(float number) {
		String s = String.valueOf(number);
		return formatReal(s);
	}

	public static String formatReal(double number) {
		String s = String.valueOf(number);
		return formatReal(s);
	}

	public static String formatReal(String s) {
		// String s = String.valueOf(number);

		int punto = s.indexOf('.');

		if (punto == -1)
			return formatInt(s) + ".00";

		String intPart = s.substring(0, punto);
		String decPart = s.substring(punto + 1);

		if (decPart.length() == 1)
			decPart = decPart + "0";
		if (decPart.length() >= 2)
			decPart = decPart.substring(0, 2);

		decPart = decPart.substring(decPart.length() - 2);
		return formatInt(intPart) + "." + decPart;
	}

	public static String formatReal4(float number) {
		String s = String.valueOf(number);
		return formatReal4(s);
	}

	public static String formatReal4(String s) {
		int punto = s.indexOf('.');

		if (punto == -1)
			return formatInt(s) + ".0000";

		String intPart = s.substring(0, punto);
		String decPart = s.substring(punto + 1);

		if (decPart.length() == 1)
			decPart = decPart + "000";
		if (decPart.length() == 2)
			decPart = decPart + "00";
		if (decPart.length() == 3)
			decPart = decPart + "0";
		if (decPart.length() >= 4)
			decPart = decPart.substring(0, 4);

		return formatInt(intPart) + "." + decPart;
	}

	public static String[] split(String inString, String delimeter) {
		String[] retAr;
		try {

			Vector vec = new Vector();
			int indexA = 0;
			int indexB = inString.indexOf(delimeter);

			while (indexB != -1) {
				vec.addElement(new String(inString.substring(indexA, indexB)));
				indexA = indexB + delimeter.length();
				indexB = inString.indexOf(delimeter, indexA);
			}
			vec.addElement(new String(inString.substring(indexA,
					inString.length())));
			retAr = new String[vec.size()];
			for (int i = 0; i < vec.size(); i++) {
				retAr[i] = vec.elementAt(i).toString();
			}
		} catch (Exception e) {
			String[] ar = { e.toString() };
			return ar;
		}
		return retAr;
	}

	public static boolean strContains(String strComma, String strBuscar) {
		String[] strElems = split(strComma, ",");
		for (int i = 0; i < strElems.length; i++) {
			String elem = strElems[i];
			if (elem.compareTo(strBuscar) == 0)
				return true;
		}
		return false;
	}

	public static String replace(final String aInput, final String aOldPattern,
			final String aNewPattern) {
		final StringBuffer result = new StringBuffer();
		int startIdx = 0;
		int idxOld = 0;
		while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
			result.append(aInput.substring(startIdx, idxOld));
			result.append(aNewPattern);
			startIdx = idxOld + aOldPattern.length();
		}
		result.append(aInput.substring(startIdx));
		return result.toString();
	}

	public static float round(float value, int decimalPlace) {
		double valor = value;
		return (float) round(valor, decimalPlace);

		/*
		 * float factor = 1.0f; if (decimalPlace > 0) { for (int i = 0; i <
		 * decimalPlace; i++) { factor *= 10.0f; } } else { for (int i = 0; i <
		 * -decimalPlace; i++) { factor /= 10.0f; } }
		 * 
		 * return (float)((int)(value * factor + 0.5) / factor);
		 */
	}

	public static double round(double value, int decimalPlace) {
		double factor = 1.0f;
		if (decimalPlace > 0) {
			for (int i = 0; i < decimalPlace; i++) {
				factor *= 10.0f;
			}
		} else {
			for (int i = 0; i < -decimalPlace; i++) {
				factor /= 10.0f;
			}
		}
		double v = value * factor + 0.5;
		return (long) (v) / factor;
	}
}