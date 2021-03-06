package com.panzyma.nm.view.adapter;

public class SetViewHolderWLayout {

	/**
	 * @return the layoutid
	 */
	public int getLayoutid() {
		return layoutid;
	}

	/**
	 * @param layoutid the layoutid to set
	 */
	public void setLayoutid(int layoutid) {
		this.layoutid = layoutid;
	}

	/**
	 * @return the viewHoder
	 */
	public Class<?> getViewHoder() {
		return viewHoder;
	}

	/**
	 * @param viewHoder the viewHoder to set
	 */
	public void setViewHoder(Class<?> viewHoder) {
		this.viewHoder = viewHoder;
	}
	
	/**
	 * @return the isParent
	 */
	public boolean isParent() {
		return isParent;
	}

	/**
	 * @param isParent the isParent to set
	 */
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	
	private int layoutid;
	private Class<?> viewHoder;
	private boolean isParent=false;
	private Integer parentposition;
	

	public SetViewHolderWLayout(int layoutid, Class<?> viewHoder, boolean isParent, Integer parentposition) {
		super();
		this.layoutid = layoutid;
		this.viewHoder = viewHoder;
		this.isParent = isParent;
		this.parentposition = parentposition;
	}

	public Integer getParentposition() {
		return parentposition;
	}

	public void setParentposition(Integer parentposition) {
		this.parentposition = parentposition;
	}

	public SetViewHolderWLayout(){}
	
 
	 
	
}
