package com.panzyma.nm.auxiliar;

public class Ammount {
	
	private AmmountType ammountType;
	private float value;	

	public Ammount(AmmountType ammountType, float value) {
		super();
		this.ammountType = ammountType;
		this.value = value;
	}

	public AmmountType getAmmountType() {
		return ammountType;
	}

	public void setAmmountType(AmmountType ammountType) {
		this.ammountType = ammountType;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
