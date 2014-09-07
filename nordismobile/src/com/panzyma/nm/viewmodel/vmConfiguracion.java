package com.panzyma.nm.viewmodel;

import com.panzyma.nm.serviceproxy.Impresora;

public class vmConfiguracion implements Comparable<vmConfiguracion> {

	

	private java.lang.String URL_SERVER;
	private java.lang.String DEVICE_ID;
	private java.lang.String ENTERPRISE;
	private java.lang.String NAME_USER;
	private int MAX_IDPEDIDO;
	private int MAX_IDRECIBO;
	private Impresora impresora;

	public static vmConfiguracion setConfiguration(java.lang.String url_server,
			java.lang.String device_prefix, java.lang.String enterprise,
			java.lang.String name_user, int max_idpedido, int max_idrecibo,Impresora _impresora) {
		vmConfiguracion vmonfig = new vmConfiguracion();
		vmonfig.setAppServerURL(url_server);
		vmonfig.setDeviceId(device_prefix);
		vmonfig.setEnterprise(enterprise);
		vmonfig.setNameUser(name_user);
		vmonfig.setMax_IdPedido(max_idpedido);
		vmonfig.setMax_Idrecibo(max_idrecibo);
		vmonfig.setImpresora(_impresora);
		return vmonfig;
	}

	public static vmConfiguracion setConfiguration(vmConfiguracion obj) 
	{
		return vmConfiguracion.setConfiguration(obj.getAppServerURL(),
				obj.getDeviceId(), obj.getEnterprise(), obj.getNameUser(),
				obj.getMax_IdPedido(), obj.getMax_Idrecibo(),obj.getImpresora());
	}
	
	public String setAppServerURL(String uri) {
		return this.URL_SERVER=uri;
	}
	
	public String getAppServerURL() {
		return this.URL_SERVER;
	}

	public String setDeviceId(String id_celular) {
		return this.DEVICE_ID=id_celular;
	}
	
	public String getDeviceId() {
		return this.DEVICE_ID;
	}

	public String setEnterprise(String empresa) {
		return this.ENTERPRISE=empresa;
	}
	
	public String getEnterprise() {
		return this.ENTERPRISE;
	}

	public String setNameUser(String nombre_usuario) {
		return this.NAME_USER=nombre_usuario;
	}
	
	public String getNameUser() {
		return this.NAME_USER;
	}

	public int setMax_IdPedido(int max_pedido) {
		return this.MAX_IDPEDIDO=max_pedido;
	}
	
	public int getMax_IdPedido() {
		return this.MAX_IDPEDIDO;
	}
	
	public int setMax_Idrecibo(int max_recibo) {
		return this.MAX_IDRECIBO=max_recibo;
	}

	public int getMax_Idrecibo() {
		return this.MAX_IDRECIBO;
	}
	public Impresora getImpresora() {
		return impresora;
	}

	public void setImpresora(Impresora impresora) {
		this.impresora = impresora;
	}
	@SuppressWarnings("static-access")
	@Override
	public int compareTo(vmConfiguracion obj) {
		if (this.getAppServerURL() == obj.getAppServerURL()
				&& this.getDeviceId() == obj.getDeviceId()
				&& this.getNameUser() == obj.getNameUser() 
				&& this.getImpresora().obtenerMac()==obj.getImpresora().obtenerMac())
			return 0;
		else
			return -1;
	}
}
