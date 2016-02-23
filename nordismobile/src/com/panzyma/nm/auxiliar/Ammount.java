package com.panzyma.nm.auxiliar;

public class Ammount {

	private AmmountType ammountType;
	private float value;
	private boolean evaluar;

	public Ammount(AmmountType ammountType, float value) {
		super();
		this.ammountType = ammountType;
		this.value = value;
	}

	public Ammount(AmmountType ammountType, float value, boolean evaluar) {
		super();
		this.ammountType = ammountType;
		this.value = value;
		this.evaluar = evaluar;
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

	public boolean isEvaluar() {
		return evaluar;
	}

	public void setEvaluar(boolean evaluar) {
		this.evaluar = evaluar;
	}	

}
