package com.panzyma.nm.serviceproxy;

import org.json.JSONObject;

import com.google.gson.Gson;

public class DataConfigurationResult 
{
	public Usuario userInfo;
    public int devicePrefix;
    public int maxIdPedido;
    public int maxIdRecibo;
    public int maxIdDevolucionV;
    /**
	 * @return the maxIdDevolucionV
	 */
	public int getMaxIdDevolucionV() {
		return maxIdDevolucionV;
	}

	/**
	 * @param maxIdDevolucionV the maxIdDevolucionV to set
	 */
	public void setMaxIdDevolucionV(int maxIdDevolucionV) {
		this.maxIdDevolucionV = maxIdDevolucionV;
	}

	/**
	 * @return the maxIdDevolucionNV
	 */
	public int getMaxIdDevolucionNV() {
		return maxIdDevolucionNV;
	}

	/**
	 * @param maxIdDevolucionNV the maxIdDevolucionNV to set
	 */
	public void setMaxIdDevolucionNV(int maxIdDevolucionNV) {
		this.maxIdDevolucionNV = maxIdDevolucionNV;
	}

	public int maxIdDevolucionNV;
    public String error;
    
	public DataConfigurationResult(){}
	
	public DataConfigurationResult(Usuario _userInfo, int _devicePrefix,int _maxIdPedido,int _maxIdRecibo,
			int _maxIdDevolucionV,int _maxIdDevolucionNV,String _error)
	{
		this.userInfo=_userInfo;
	    this.devicePrefix=_devicePrefix;
	    this.maxIdPedido=_maxIdPedido;
	    this.maxIdRecibo=_maxIdRecibo;
	    this.maxIdDevolucionV=_maxIdDevolucionV;
	    this.maxIdDevolucionNV=_maxIdDevolucionNV;
	    this.error=_error; 
	}
	
	public Usuario get_userInfo()
	{
		return this.userInfo;
	}
	
	public int get_devicePrefix()
	{
		return this.devicePrefix;
	}
	
	public int get_maxIdPedido()
	{
		return this.maxIdPedido;
	}	
	
	public int get_maxIdRecibo()
	{
		return this.maxIdRecibo;
	}	
	
	public String get_error()
	{
		return this.error;
	}	
	
	public static DataConfigurationResult ParseJSON_To_Entity(JSONObject json) throws Exception
	{  
          return new Gson().fromJson(json.get("getDataConfigurationResult").toString(), DataConfigurationResult.class);
	}

}
