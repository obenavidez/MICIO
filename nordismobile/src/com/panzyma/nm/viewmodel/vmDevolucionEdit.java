package com.panzyma.nm.viewmodel;

import java.util.ArrayList;
import java.util.List;

import com.panzyma.nm.serviceproxy.Catalogo;
import com.panzyma.nm.serviceproxy.Devolucion;

public class vmDevolucionEdit {

	/**
	 * @param motivodev
	 * @param devolucion
	 */
	public vmDevolucionEdit(Devolucion devolucion,List<Catalogo> motivodev) {
		super();
		this.motivodev = motivodev;
		this.devolucion = devolucion;
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
	public void setMotivodev(ArrayList<Catalogo> motivodev) {
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
	
}
