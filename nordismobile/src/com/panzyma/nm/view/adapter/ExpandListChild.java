package com.panzyma.nm.view.adapter;

public class ExpandListChild {

	
	public ExpandListChild(){}
	
	/**
	 * @param name
	 * @param tag
	 * @param obj
	 */
	public ExpandListChild(String name, String tag, Object obj) {
		super();
		Name = name;
		Tag = tag;
		this.obj = obj;
	}
	public ExpandListChild(ExpandListChild obj) {
		super();
		Name = obj.getName();
		Tag =obj.getTag();
		this.obj =obj.getObject();
	}
	
	private String Name;
	private String Tag;
	
	private Object obj; 
	
	public String getName() {
		return Name;
	}
	public void setName(String Name) {
		this.Name = Name;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String Tag) {
		this.Tag = Tag;
	}
	
	public Object getObject() {
		return this.obj;
	}
	public void setObject(Object _obj) {
		this.obj = _obj;
	} 
}
