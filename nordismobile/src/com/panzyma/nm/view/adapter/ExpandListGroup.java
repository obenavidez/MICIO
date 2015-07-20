package com.panzyma.nm.view.adapter;

import java.util.ArrayList;

public class ExpandListGroup {
 
	private String Name;
	private ArrayList<ExpandListChild> Items;
	private Object obj;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public ArrayList<ExpandListChild> getItems() {
		return Items;
	}
	public void setItems(ArrayList<ExpandListChild> Items) {
		this.Items = Items;
	}
	
	public Object getObject() {
		return this.obj;
	}
	public void setObject(Object _obj) {
		this.obj = _obj;
	}
}
