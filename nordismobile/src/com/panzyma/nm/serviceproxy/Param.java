package com.panzyma.nm.serviceproxy;
import java.util.*; 

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson; 

public class Param  {
    protected java.lang.String Nombre;
    protected java.lang.String Valor;
    
    public Param() {
    }
    
    public Param(java.lang.String Nombre, java.lang.String valor) {
        this.Nombre = Nombre;
        this.Valor = valor;
    }
    
    public java.lang.String getNombre() {
        return Nombre;
    }
    
    public void setNombre(java.lang.String nombre) {
        this.Nombre = nombre;
    }
    
    public java.lang.String getValor() {
        return Valor;
    }
    
    public void setValor(java.lang.String valor) {
        this.Valor = valor;
    } 
    
    public static ArrayList<Param> ParseArrayJSON_To_Entity(JSONArray arrayjson) throws Exception
    {
    	ArrayList<Param> params=new ArrayList<Param>();
    	for(int i = 0;i<arrayjson.length();i++)
		{
			JSONObject json=(JSONObject) arrayjson.get(i);
			params.add(new Gson().fromJson(json.toString(), Param.class));
		}
		return params;
    }
} 
