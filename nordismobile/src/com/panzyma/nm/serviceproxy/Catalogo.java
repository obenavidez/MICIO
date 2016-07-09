package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Catalogo implements Parcelable {

	private int Id;
	private String NombreCatalogo;
	private List<ValorCatalogo> ValoresCatalogo;

	public Catalogo(){}
	
	public Catalogo(Parcel parcel) {
		this.Id = parcel.readInt();
		this.NombreCatalogo = parcel.readString();
		Parcelable[] parcelableArray = parcel
				.readParcelableArray(ValorCatalogo.class.getClassLoader());
		if (parcelableArray != null) {
			ValorCatalogo[] valores = Arrays.copyOf(parcelableArray,
					parcelableArray.length, ValorCatalogo[].class);
			ValoresCatalogo = Arrays.asList(valores);
		}
	}

	public Catalogo(String NombreCatalogo) {
		this.NombreCatalogo = NombreCatalogo;
	}

	public int getId() {
		return Id;
	}

	public String getNombreCatalogo() {
		return NombreCatalogo;
	}

	public List<ValorCatalogo> getValoresCatalogo() {
		return ValoresCatalogo;
	}

	public void setValoresCatalogo(List<ValorCatalogo> ValoresCatalogo) {
		this.ValoresCatalogo = ValoresCatalogo;
	}

	public static ArrayList<Catalogo> ParseArrayJSON_To_Entity(
			JSONArray arrayjson) throws Exception {
		ArrayList<Catalogo> valorescatologos = new ArrayList<Catalogo>();
		for (int i = 0; i < arrayjson.length(); i++) {
			JSONObject json = (JSONObject) arrayjson.get(i);
			valorescatologos.add(new Gson().fromJson(json.toString(),
					Catalogo.class));
		}
		return valorescatologos;
	}

	public static final Parcelable.Creator<Catalogo> CREATOR = new Parcelable.Creator<Catalogo>() {
		@Override
		public Catalogo createFromParcel(Parcel parcel) {
			return new Catalogo(parcel);
		}

		@Override
		public Catalogo[] newArray(int size) {
			return new Catalogo[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeInt(this.Id);
		parcel.writeString(this.NombreCatalogo);
		parcel.writeTypedList(this.ValoresCatalogo);
	}

}
