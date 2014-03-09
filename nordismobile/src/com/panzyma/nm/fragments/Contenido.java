package com.panzyma.nm.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.panzyma.nm.serviceproxy.Cliente;

public class Contenido {
	
	public static String[] nombresClientes;

	public static List<Cliente> lista = new ArrayList<Cliente>();

	/**
	 * Donde se asigna el identificador a cada entrada de la lista
	 */
	public static Map<String, Cliente> ENTRADAS_LISTA_HASHMAP = new HashMap<String, Cliente>();

	/**
	 * Creamos estáticamente las entradas
	 */
	static {
		aniadirEntrada(new Cliente(Long.valueOf(0), "Osmar David Venavidez","01", "Ciudad San Sebastian"));
		aniadirEntrada(new Cliente(Long.valueOf(1), "José Iván Rostran","02", "El Riguero"));
		aniadirEntrada(new Cliente(Long.valueOf(2), "Josué Alexander Herrera","03", "La Fuente"));
	}
	
	static {
		int contador = 0;
		nombresClientes = new String[lista.size()];
		for(Cliente c : lista){
			nombresClientes[contador++] = new String(c.getNombreCliente());
		}
		
	}

	/**
	 * Añade una entrada a la lista
	 * 
	 * @param entrada
	 *            Elemento que añadimos a la lista
	 */
	private static void aniadirEntrada(Cliente entrada) {
		lista.add(entrada);
		ENTRADAS_LISTA_HASHMAP.put(String.valueOf(entrada.getIdCliente()), entrada);
	}
	
}
