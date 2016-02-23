package com.panzyma.nm.view.adapter;

public class ExpandListChild {

	
	public ExpandListChild(){}
	

	public ExpandListChild(ExpandListChild obj) {
		super();
		Name = obj.getName();
		Tag =obj.getTag();
		this.obj =obj.getObject();
	}
	
	private String Name;
	private String Tag;
	private Integer parentposition;
	
	public ExpandListChild(String name, String tag, Integer parentposition, Object obj) {
		super();
		Name = name;
		Tag = tag;
		this.parentposition = parentposition;
		this.obj = obj;
	}


	public Integer getParentposition() {
		return parentposition;
	}


	public void setParentposition(Integer parentposition) {
		this.parentposition = parentposition;
	}

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
