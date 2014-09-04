package com.panzyma.nm.serviceproxy;
 
import android.os.Parcel;
import android.os.Parcelable;

public final class Impresora implements Parcelable
{
    static String nombre;
	static String mac;
	static int estado;
	private static Impresora impresora;
	
	public static Impresora nuevaIntacia(String _nombre,String _mac, int _estado)
	{
		if(impresora==null)
			impresora=new Impresora(_nombre,_mac,_estado);
		return impresora;
	}
	
	public static Impresora nuevaIntacia(Parcel parcel)
	{
 		if(impresora==null)
 			impresora=new Impresora(parcel.readString(),parcel.readString(),parcel.readInt());
		return impresora;
	}
	
	public static final Parcelable.Creator CREATOR  = new Parcelable.Creator() {
        @Override
		public Impresora createFromParcel(Parcel parcel) {
             return Impresora.nuevaIntacia(parcel);
        }

        @Override
		public Impresora[] newArray(int size) {
             return new Impresora[size];
        }
	      	 
	      	 
	}; 
	
	Impresora(String _nombre,String _mac, int _estado)
	{
		nombre=_nombre;
		mac=_mac;
		estado=_estado;
	}
	
	public static String establecerNombre(String _nombre){
		return nombre=_nombre;
	}
	
	public static String obtenerNombre(){
		return nombre;
	}
	
	public static String establecerMac(String _mac){
		return mac=_mac;
	}
	
	public static String obtenerMac(){
		return mac;
	}
	
	public static int establecerEstado(int _estado){
		return estado=_estado;
	}
	
	public static int obtenerEstado(){
		return estado;
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) 
	{	 		
		parcel.writeString(obtenerNombre());
		parcel.writeString(obtenerMac());
		parcel.writeInt(obtenerEstado()); 
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@SuppressWarnings("unused")
	private  void readFromParcel(Parcel parcel) 
    {  
		establecerNombre(parcel.readString());
		establecerMac(parcel.readString());
		establecerEstado(parcel.readInt()); 		  
	}
}
