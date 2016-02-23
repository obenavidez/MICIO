package com.panzyma.nm.viewmodel;
 
import java.util.List;

import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Devolucion;

public class vmDevolucionEdit {

 
	/**
	 * @param motivodev
	 * @param devolucion
	 * @param cliente
	 */
	public vmDevolucionEdit(Devolucion devolucion,List<Catalogo> motivodev, 
			Cliente cliente) {
		super();
		this.motivodev = motivodev;
		this.devolucion = devolucion;
		this.cliente = cliente;
	}
	/**
	 * @return the motivodev
	 */
	public List<Catalogo> getMotivodev() {
		return motivodev;
	}
	/**
	 * @param motivodev the motivodev to set
	 */
	public void setMotivodev(List<Catalogo> motivodev) {
		this.motivodev = motivodev;
	}
	/**
	 * @return the devolucion
	 */
	public Devolucion getDevolucion() {
		return devolucion;
	}
	/**
	 * @param devolucion the devolucion to set
	 */
	public void setDevolucion(Devolucion devolucion) {
		this.devolucion = devolucion;
	}
	List<Catalogo> motivodev;
	Devolucion devolucion;
	Cliente cliente;
	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}
	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
