package com.panzyma.nm.serviceproxy;
 
import org.json.JSONObject;

import com.google.gson.Gson; 

public class LoginUserResult {
 
	private int AutenticateRS;
	private boolean IsAdmin;
	
	public LoginUserResult(){}
	
	public LoginUserResult(int autenticateRS,boolean isAdmin)
	{
		this.AutenticateRS=autenticateRS;
		this.IsAdmin=isAdmin;		
	}
	
	public int getAuntenticateRS()
	{
		return this.AutenticateRS;
	}
	
	public boolean IsAdmin()
	{
		return this.IsAdmin;
	}	
	public static LoginUserResult ParseJSON_To_Entity(JSONObject json) throws Exception
	{  
		return new Gson().fromJson(json.get("LoginUserResult").toString(), LoginUserResult.class);
	}
}
