package com.panzyma.nm.model;

import java.util.ArrayList;

import org.json.JSONArray;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.panzyma.nm.auxiliar.NMComunicacion;
import com.panzyma.nm.auxiliar.NMConfig; 
import com.panzyma.nm.datastore.DatabaseProvider;
import com.panzyma.nm.serviceproxy.Lote;
import com.panzyma.nm.serviceproxy.Producto;
import com.panzyma.nm.viewmodel.vmProducto;
@SuppressWarnings({"unused"})
public class ModelProducto 
{
	static Object lock=new Object();
    static String TAG=ModelProducto.class.getSimpleName();
   public ModelProducto() {}
    
   public synchronized static JSONArray getArrayProductoFromServer(String Credentials,String UsuarioVendedor,Integer page,Integer rowpage)throws Exception
   {
	   return  NMComunicacion.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetProductosPaged+"/"+Credentials+"/"+UsuarioVendedor+"/"+page+"/"+rowpage);
	  	  //return  NMTranslate.ToCollection(NMComunicacion.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetProductosPaged+"/"+Credentials+"/"+UsuarioVendedor+"/"+page+"/"+rowpage),new Producto());	
   }   
   
   public  static ArrayList<vmProducto> getArrayCustomerFromLocalHost(ContentResolver content)throws Exception
	{  		 
		 String[] projection = new String[] {NMConfig.Producto.Id,
				 							 NMConfig.Producto.Codigo,
				 							 NMConfig.Producto.Nombre,
				 							 NMConfig.Producto.Disponible};
		 int count=0;
		 ArrayList<vmProducto> a_vmprod=new ArrayList<vmProducto>(); 
		 Cursor cur = content.query(DatabaseProvider.CONTENT_URI_PRODUCTO,
							        projection, //Columnas a devolver
							        null,       //Condición de la query
							        null,       //Argumentos variables de la query
							        null);  
		 if (cur.moveToFirst()) 
		 {  
	            do{ 
	            	a_vmprod.add(new vmProducto( Long.parseLong(cur.getString(cur.getColumnIndex(projection[0]))),
	            								 cur.getString(cur.getColumnIndex(projection[1])),
	            								 cur.getString(cur.getColumnIndex(projection[2])),
	            								 Integer.parseInt(cur.getString(cur.getColumnIndex(projection[3]))))
	            	                           );
	            }while (cur.moveToNext());
		 }   
		 
		 return a_vmprod;
	}
   
   public synchronized static void saveProductos(final JSONArray objL,Context cnt,int page)throws Exception
   {   
	   DatabaseProvider.RegistrarProductos(objL, cnt,page); 
	   Log.d(TAG, "testeererer");
   }
   
   public synchronized static void saveProductos(final JSONArray objL,Context cnt)throws Exception
   {   
	   DatabaseProvider.RegistrarProductos(objL, cnt); 
	   Log.d(TAG, "testeererer");
   }
   
   public synchronized static Producto getProductoByID(ContentResolver content,long idproducto)throws Exception
   {
	   
	    Producto producto=new Producto(); 
		Uri uri=Uri.parse(DatabaseProvider.CONTENT_URI_PRODUCTO+"/"+String.valueOf(idproducto)); 
		Cursor cur = content.query(uri,
		        null, //Columnas a devolver
		        null,       //Condición de la query
		        null,       //Argumentos variables de la query
		        null); 
		if (cur.moveToFirst()) 
		 {  
			 
	           do{
	        	   int value;
	        	   producto.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Producto.Id))));
	        	   producto.setCodigo(cur.getString(cur.getColumnIndex(NMConfig.Producto.Codigo)));
	        	   producto.setNombre(cur.getString(cur.getColumnIndex(NMConfig.Producto.Nombre)));
	        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.EsGravable));            		
	        	   producto.setEsGravable(value==1?true:false);
	        	   producto.setListaPrecios(cur.getString(cur.getColumnIndex(NMConfig.Producto.ListaPrecios)));
	        	   producto.setListaBonificaciones(cur.getString(cur.getColumnIndex(NMConfig.Producto.ListaBonificaciones)));
	        	   producto.setCatPrecios(cur.getString(cur.getColumnIndex(NMConfig.Producto.CatPrecios)));
	        	   producto.setDisponible(cur.getInt(cur.getColumnIndex(NMConfig.Producto.Disponible)));
	        	   producto.setListaLotes(getLotesByProducto(content,producto.getId()));	        	  
	        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.PermiteDevolucion));            		
	        	   producto.setPermiteDevolucion(value==1?true:false);
	        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.LoteRequerido));            		
	        	   producto.setLoteRequerido(value==1?true:false);
	        	   producto.setObjProveedorID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Producto.ObjProveedorID))));
	        	   producto.setDiasAntesVen(cur.getInt(cur.getColumnIndex(NMConfig.Producto.DiasAntesVen)));
	        	   producto.setDiasDespuesVen(cur.getInt(cur.getColumnIndex(NMConfig.Producto.DiasDespuesVen)));
	        	   
	        	   
	           }while (cur.moveToNext());
	           
          }
	   
	return null;
	   
   }
   
   public synchronized static Lote[] getLotesByProducto(ContentResolver content,long ObjProductoID)throws Exception{
	
	   
	    Lote lote=new Lote(); 
		Uri uri=Uri.parse(DatabaseProvider.CONTENT_URI_LOTE+"/"+String.valueOf(ObjProductoID)); 
		Cursor cur = content.query(uri,
		        null, //Columnas a devolver
		        null,       //Condición de la query
		        null,       //Argumentos variables de la query
   	        null); 
		if (cur.moveToFirst()) 
		 {  
			 
	           do{
	        	   int value;
	        	   lote.setId(cur.getLong(cur.getColumnIndex(NMConfig.Producto.Lote.Id)));
	        	   lote.setNumeroLote(cur.getString(cur.getColumnIndex(NMConfig.Producto.Lote.NumeroLote)));
	        	   lote.setFechaVencimiento(cur.getInt(cur.getColumnIndex(NMConfig.Producto.Lote.FechaVencimiento)));
	        	   
	           }while (cur.moveToNext());
	     }   
	   return null;
	   
	   
   }
}
