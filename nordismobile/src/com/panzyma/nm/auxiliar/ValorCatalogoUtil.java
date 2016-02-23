package com.panzyma.nm.auxiliar;

import java.util.List;

import com.panzyma.nm.serviceproxy.ValorCatalogo;

public class ValorCatalogoUtil {

	/***
	 * Obtiene un Valor de Catalogo por su código
	 * @param Lista de valores del catalogo
	 * @param codigo del valor a buscar
	 * @return id del catalogo a buscar
	 */
	public static long getValorCatalogoID(List<ValorCatalogo> valores,
			String valorCatalogoCode) {
		long id = 0;
		for (ValorCatalogo valor : valores)
			if (valorCatalogoCode.equals(valor.getCodigo())) {
				id = valor.getId();
				break;
			}

		return id;
	}

}
