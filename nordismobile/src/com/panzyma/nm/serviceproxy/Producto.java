package com.panzyma.nm.serviceproxy;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.google.gson.Gson;
import com.panzyma.nm.interfaces.Item;

public class Producto implements KvmSerializable,Item {
	public long Id;
	public java.lang.String Codigo;
	public java.lang.String Nombre;
	public boolean EsGravable;
	public java.lang.String ListaPrecios;
	public java.lang.String ListaBonificaciones;
	public java.lang.String CatPrecios;
	public int Disponible;
	public Lote[] ListaLotes;
	public boolean PermiteDevolucion;
	public boolean LoteRequerido;
	public long ObjProveedorID;
	public int DiasAntesVen;
	public int DiasDespuesVen;

	public Producto() {
	}

	public Producto(long id, java.lang.String codigo, java.lang.String nombre,
			boolean EsGravable, java.lang.String listaPrecios,
			java.lang.String listaBonificaciones, java.lang.String catPrecios,
			int disponible, Lote[] listaLotes, boolean permiteDevolucion,
			boolean loteRequerido, long objProveedorID, int diasAntesVen,
			int diasDespuesVen) {
		this.Id = id;
		this.Codigo = codigo;
		this.Nombre = nombre;
		this.EsGravable = EsGravable;
		this.ListaPrecios = listaPrecios;
		this.ListaBonificaciones = listaBonificaciones;
		this.CatPrecios = catPrecios;
		this.Disponible = disponible;
		this.ListaLotes = listaLotes;
		this.PermiteDevolucion = permiteDevolucion;
		this.LoteRequerido = loteRequerido;
		this.ObjProveedorID = objProveedorID;
		this.DiasAntesVen = diasAntesVen;
		this.DiasDespuesVen = diasDespuesVen;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = (int) (hash * 17 + Id); 
		hash = hash * 13 + ((Codigo == null) ? 0 : Codigo.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		if (Id != other.Id)
			return false;
		if (Codigo == null) {
			if (other.Codigo != null)
				return false;
		} else if (!Codigo.equals(other.Codigo))
			return false;
		return true;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public java.lang.String getCodigo() {
		return Codigo;
	}

	public void setCodigo(java.lang.String codigo) {
		this.Codigo = codigo;
	}

	public java.lang.String getNombre() {
		return Nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.Nombre = nombre;
	}

	public boolean isEsGravable() {
		return EsGravable;
	}

	public void setEsGravable(boolean EsGravable) {
		this.EsGravable = EsGravable;
	}

	public java.lang.String getListaPrecios() {
		return ListaPrecios;
	}

	public void setListaPrecios(java.lang.String listaPrecios) {
		this.ListaPrecios = listaPrecios;
	}

	public java.lang.String getListaBonificaciones() {
		return ListaBonificaciones;
	}

	public void setListaBonificaciones(java.lang.String listaBonificaciones) {
		this.ListaBonificaciones = listaBonificaciones;
	}

	public java.lang.String getCatPrecios() {
		return CatPrecios;
	}

	public void setCatPrecios(java.lang.String catPrecios) {
		this.CatPrecios = catPrecios;
	}

	public int getDisponible() {
		return Disponible;
	}

	public void setDisponible(int disponible) {
		this.Disponible = disponible;
	}

	public Lote[] getListaLotes() {
		return ListaLotes;
	}

	public void setListaLotes(Lote[] listaLotes) {
		this.ListaLotes = listaLotes;
	}

	public boolean isPermiteDevolucion() {
		return PermiteDevolucion;
	}

	public void setPermiteDevolucion(boolean permiteDevolucion) {
		this.PermiteDevolucion = permiteDevolucion;
	}

	public boolean isLoteRequerido() {
		return LoteRequerido;
	}

	public void setLoteRequerido(boolean loteRequerido) {
		this.LoteRequerido = loteRequerido;
	}

	public long getObjProveedorID() {
		return ObjProveedorID;
	}

	public void setObjProveedorID(long objProveedorID) {
		this.ObjProveedorID = objProveedorID;
	}

	public int getDiasAntesVen() {
		return DiasAntesVen;
	}

	public void setDiasAntesVen(int diasAntesVen) {
		this.DiasAntesVen = diasAntesVen;
	}

	public int getDiasDespuesVen() {
		return DiasDespuesVen;
	}

	public void setDiasDespuesVen(int diasDespuesVen) {
		this.DiasDespuesVen = diasDespuesVen;
	}

	@Override
	public Object getProperty(int _index) {
		switch (_index) {
		case 0:
			return Long.valueOf(Id);
		case 1:
			return Codigo;
		case 2:
			return Nombre;
		case 3:
			return Boolean.valueOf(EsGravable);
		case 4:
			return ListaPrecios;
		case 5:
			return ListaBonificaciones;
		case 6:
			return CatPrecios;
		case 7:
			return Integer.valueOf(Disponible);
		case 8:
			return ListaLotes;
		case 9:
			return Boolean.valueOf(PermiteDevolucion);
		case 10:
			return Boolean.valueOf(LoteRequerido);
		case 11:
			return Long.valueOf(ObjProveedorID);
		case 12:
			return Integer.valueOf(DiasAntesVen);
		case 13:
			return Integer.valueOf(DiasDespuesVen);

		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 14;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int _index, Hashtable _table, PropertyInfo _info) {
		switch (_index) {
		case 0:
			_info.name = "Id";
			_info.type = Long.class;
			break;
		case 1:
			_info.name = "Codigo";
			_info.type = java.lang.String.class;
			break;
		case 2:
			_info.name = "Nombre";
			_info.type = java.lang.String.class;
			break;
		case 3:
			_info.name = "EsGravable";
			_info.type = java.lang.Boolean.class;
			break;
		case 4:
			_info.name = "ListaPrecios";
			_info.type = java.lang.String.class;
			break;
		case 5:
			_info.name = "ListaBonificaciones";
			_info.type = java.lang.String.class;
			break;
		case 6:
			_info.name = "CatPrecios";
			_info.type = java.lang.String.class;
			break;
		case 7:
			_info.name = "Disponible";
			_info.type = java.lang.Integer.class;
			break;
		case 8:
			_info.name = "ListaLotes";
			_info.type = Lote[].class;
			break;
		case 9:
			_info.name = "PermiteDevolucion";
			_info.type = java.lang.Boolean.class;
			break;
		case 10:
			_info.name = "LoteRequerido";
			_info.type = java.lang.Boolean.class;
			break;
		case 11:
			_info.name = "ObjProveedorID";
			_info.type = java.lang.Long.class;
			break;
		case 12:
			_info.name = "DiasAntesVen";
			_info.type = java.lang.Integer.class;
			break;
		case 13:
			_info.name = "DiasDespuesVen";
			_info.type = java.lang.Integer.class;
			break;
		}

	}

	@Override
	public void setProperty(int _index, Object _obj) {
		switch (_index) {
		case 0:
			Id = Long.parseLong(_obj.toString());
			break;
		case 1:
			Codigo = _obj.toString();
			break;
		case 2:
			Nombre = _obj.toString();
			break;
		case 3:
			EsGravable = Boolean.parseBoolean(_obj.toString());
			break;
		case 4:
			ListaPrecios = _obj.toString();
			break;
		case 5:
			ListaBonificaciones = _obj.toString();
			break;
		case 6:
			CatPrecios = _obj.toString();
			break;
		case 7:
			Disponible = Integer.parseInt(_obj.toString());
		case 8:
			ListaLotes = (Lote[]) _obj;
			break;
		case 9:
			PermiteDevolucion = Boolean.parseBoolean(_obj.toString());
		case 10:
			LoteRequerido = Boolean.parseBoolean(_obj.toString());
		case 11:
			ObjProveedorID = Long.parseLong(_obj.toString());
		case 12:
			DiasAntesVen = Integer.parseInt(_obj.toString());
		case 13:
			DiasDespuesVen = Integer.parseInt(_obj.toString());

		}
	}

	public static ArrayList<Producto> ParseArrayJSON_To_Entity(
			JSONArray arrayjson) throws Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		for (int i = 0; i < arrayjson.length(); i++) {
			JSONObject json = (JSONObject) arrayjson.get(i);
			productos.add(new Gson().fromJson(json.toString(), Producto.class));
		}
		return productos;
	}

	@Override
	public Object isMatch(CharSequence constraint) {
		if (this.getNombre().toLowerCase()
				.startsWith(constraint.toString()))
			return true;
		return false;
	}

	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getItemDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getItemCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
