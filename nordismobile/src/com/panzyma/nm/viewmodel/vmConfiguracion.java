package com.panzyma.nm.viewmodel;

import com.panzyma.nm.serviceproxy.Impresora;

public class vmConfiguracion{
	
	private java.lang.String URL_SERVER;
	private java.lang.String URL_SERVER2;
	private java.lang.String DEVICE_ID;
	private java.lang.String ENTERPRISE;
	private java.lang.String NAME_USER;
	private int MAX_IDPEDIDO;
	private int MAX_IDRECIBO;
	private int MAX_IDDEVOLUCIONV;
	private int MAX_IDDEVOLUCIONNV;
	
	public vmConfiguracion(){
		
	}
	/**
	 * @return the mAX_IDDEVOLUCIONV
	 */
	public int getMAX_IDDEVOLUCIONV() {
		return MAX_IDDEVOLUCIONV;
	}

	/**
	 * @param mAX_IDDEVOLUCIONV the mAX_IDDEVOLUCIONV to set
	 */
	public void setMAX_IDDEVOLUCIONV(int mAX_IDDEVOLUCIONV) {
		MAX_IDDEVOLUCIONV = mAX_IDDEVOLUCIONV;
	}

	/**
	 * @return the mAX_IDDEVOLUCIONNV
	 */
	public int getMAX_IDDEVOLUCIONNV() {
		return MAX_IDDEVOLUCIONNV;
	}

	/**
	 * @param mAX_IDDEVOLUCIONNV the mAX_IDDEVOLUCIONNV to set
	 */
	public void setMAX_IDDEVOLUCIONNV(int mAX_IDDEVOLUCIONNV) {
		MAX_IDDEVOLUCIONNV = mAX_IDDEVOLUCIONNV;
	}

	private Impresora impresora;

	vmConfiguracion oldata;
	/**
	 * @param uRL_SERVER
	 * @param uRL_SERVER2
	 * @param dEVICE_ID
	 * @param eNTERPRISE
	 * @param nAME_USER
	 * @param mAX_IDPEDIDO
	 * @param mAX_IDRECIBO
	 * @param impresora
	 */ 
	public static vmConfiguracion setConfiguration(java.lang.String url_server,java.lang.String url_server2,
			java.lang.String device_prefix, java.lang.String enterprise,
			java.lang.String name_user, int max_idpedido, int max_idrecibo,
			int max_devolucionv, int max_devolucionnv,Impresora _impresora) {
		
		vmConfiguracion vmonfig = new vmConfiguracion();
		vmonfig.setAppServerURL(url_server);
		vmonfig.setAppServerURL2(url_server2);
		vmonfig.setDeviceId(device_prefix);
		vmonfig.setEnterprise(enterprise);
		vmonfig.setNameUser(name_user);
		vmonfig.setMax_IdPedido(max_idpedido);
		vmonfig.setMax_Idrecibo(max_idrecibo);
		vmonfig.setMAX_IDDEVOLUCIONV(max_devolucionv);
		vmonfig.setMAX_IDDEVOLUCIONNV(max_devolucionnv);
		vmonfig.setImpresora(_impresora);
		return vmonfig;
	}
	
	public void setOldData(vmConfiguracion config)
	{
		oldata=setConfiguration(config);
	}
	
	public vmConfiguracion getOldData()
	{
		return oldata;
	}
	
	 @SuppressWarnings("unused")
	public boolean hasModified(Object obj) 
    { 
    	if(obj==null && this!=null)
			return true;
		if (getClass() != obj.getClass())
			return false;
		vmConfiguracion other = (vmConfiguracion) obj;
		if(other==null && this!=null)
			return true;	  

		if(!(getAppServerURL().equals(other.getAppServerURL())))
				return true;
		if(!(getAppServerURL2().equals(other.getAppServerURL2())))
			return true;
		if(!(getDeviceId().equals(other.getDeviceId())))
			return true;
		if(!(getEnterprise().equals(other.getEnterprise())))
			return true; 		
		if(!(getNameUser().equals(other.getNameUser())))
			return true; 		
		
		Impresora printer=getImpresora();
		Impresora _printer=other.getImpresora();
		 
		if((printer==null && _printer!=null) || ((printer!=null && _printer==null)))
			return true;
		if (Impresora.obtenerMac()!=Impresora.obtenerMac())
			return true;
		
		return false;
    }

	public static vmConfiguracion setConfiguration(vmConfiguracion obj) 
	{
		return vmConfiguracion.setConfiguration(obj.getAppServerURL(),obj.getAppServerURL2(),
				obj.getDeviceId(), obj.getEnterprise(), obj.getNameUser(),
				obj.getMax_IdPedido(), obj.getMax_Idrecibo(),obj.getMAX_IDDEVOLUCIONV(),obj.getMAX_IDDEVOLUCIONNV(),obj.getImpresora());
	}
	
	public String setAppServerURL(String uri) {
		return this.URL_SERVER=uri;
	}
	
	public String getAppServerURL() {
		return this.URL_SERVER;
	}
	
	public String setAppServerURL2(String uri) {
		return this.URL_SERVER2=uri;
	}
	
	public String getAppServerURL2() {
		return this.URL_SERVER2;
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
}
