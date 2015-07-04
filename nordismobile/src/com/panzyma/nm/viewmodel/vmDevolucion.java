package com.panzyma.nm.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.panzyma.nm.interfaces.Item;

public class vmDevolucion  implements Parcelable, Item  {

	public vmDevolucion(){
		super();
	}
	
	@Override
	public Object isMatch(CharSequence constraint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getItemDescription() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getItemCode() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
