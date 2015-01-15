package com.panzyma.nm.auxiliar;

import com.panzyma.nm.serviceproxy.Usuario;

public class Session {

	public Session(Usuario usuario, boolean loged, int started_session) {
		super();
		this.usuario = usuario;
		this.loged = loged;
		this.started_session = started_session;
	}
	
	public Session() {
		super(); 
	}
	
	public Session(Usuario usuario, boolean loged) {
		super();
		this.usuario = usuario; 
		this.loged = loged;
	}
	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		if(usuario==null)
			Usuario.get();
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the loged
	 */
	public boolean isLoged() {
		return loged;
	}
	/**
	 * @param loged the loged to set
	 */
	public void setLoged(boolean loged) {
		this.loged = loged;
	}
	/**
	 * @return the started_session
	 */
	public long getStarted_session() {
		return started_session;
	}
	/**
	 * @param started_session the started_session to set
	 */
	public void setStarted_session(long started_session) {
		this.started_session = started_session;
	}
	private Usuario usuario;
	private boolean loged;
	private long started_session; 
}
