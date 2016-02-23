package com.panzyma.nm.view.adapter;
 
import java.util.LinkedList;

public class ExpandListGroup {
 
	private String Name;
	private LinkedList<ExpandListChild> Items;
	private Object obj;
	private Integer position;
	
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public ExpandListGroup(ExpandListGroup lg){
		this.Name=lg.getName();
		this.Items=lg.getItems();
		this.obj=lg.getObject();
	}
	
	public ExpandListGroup(){}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public LinkedList<ExpandListChild> getItems() {
		return Items;
	}
	public void setItems(LinkedList<ExpandListChild> Items) {
		this.Items = Items;
	}
	
	public Object getObject() {
		return this.obj;
	}
	public void setObject(Object _obj) {
		this.obj = _obj;
	}
}
