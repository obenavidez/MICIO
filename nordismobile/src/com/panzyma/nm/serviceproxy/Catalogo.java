package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class Catalogo {

	 private int Id;
	 private String NombreCatalogo;
     private List<ValorCatalogo> ValoresCatalogo;
     
	public Catalogo() { 
	}

    public int getId(){
    	return Id;
    }
	
    public String getNombreCatalogo(){
    	return NombreCatalogo;
    }
    public List<ValorCatalogo> getValoresCatalogo(){
    	return ValoresCatalogo;
    }
    public static ArrayList<Catalogo> ParseArrayJSON_To_Entity(JSONArray arrayjson) throws Exception
	{
		ArrayList<Catalogo> valorescatologos=new ArrayList<Catalogo>();
		for(int i = 0;i<arrayjson.length();i++)
		{
			JSONObject json=(JSONObject) arrayjson.get(i);
			valorescatologos.add(new Gson().fromJson(json.toString(), Catalogo.class));
		}
		return valorescatologos;
	} 
    
}
