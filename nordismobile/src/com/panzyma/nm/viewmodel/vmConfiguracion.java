package com.panzyma.nm.viewmodel;

public class vmConfiguracion {
 
    private  java.lang.String URL_SERVER;
    private  java.lang.String DEVICE_ID; 
    private  java.lang.String ENTERPRISE;
    private  java.lang.String NAME_USER;
    private  java.lang.String PASSWORD;
    private int MAX_IDPEDIDO;
    private int MAX_IDRECIBO;
     
	public static vmConfiguracion setConfiguration(java.lang.String url_server,
						   java.lang.String device_prefix,
						   java.lang.String enterprise,
						   java.lang.String name_user,
						   java.lang.String passwd, 
						   int max_idpedido,
						   int max_idrecibo)
	{
		vmConfiguracion vmonfig=new vmConfiguracion();
		vmonfig.URL_SERVER=url_server;
		vmonfig.DEVICE_ID=device_prefix;
		vmonfig.ENTERPRISE=enterprise;
		vmonfig.NAME_USER=name_user;
		vmonfig.PASSWORD=passwd; 
		vmonfig.MAX_IDPEDIDO=max_idpedido;
		vmonfig.MAX_IDRECIBO=max_idrecibo;
		return vmonfig;
	}
	
	public  String getAppServerURL(){
		return URL_SERVER;
	}
	public  String getDeviceId(){
		return DEVICE_ID;
	}
	public  String getEnterprise(){
		return ENTERPRISE;
	}
	public  String getNameUser(){
		return NAME_USER;
	}
	public  String getPassword(){
		return PASSWORD;
	}
	public  int getMax_IdPedido(){
		return MAX_IDPEDIDO;
	}
	public  int getMax_Idrecibo(){
		return MAX_IDRECIBO;
	}
}
