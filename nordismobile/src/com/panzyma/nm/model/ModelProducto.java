package com.panzyma.nm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.ksoap2.serialization.SoapObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.comunicator.AppNMComunication;
import com.panzyma.nm.auxiliar.NMConfig; 
import com.panzyma.nm.datastore.DatabaseProvider; 
import com.panzyma.nm.datastore.DatabaseProvider.Helper;
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
	   //return  NMComunicacion.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetProductosPaged+"/"+Credentials+"/"+UsuarioVendedor+"/"+page+"/"+rowpage);
	   return AppNMComunication.InvokeService2(NMConfig.URL2+NMConfig.MethodName.GetProductosPaged+"/"+Credentials+"/"+UsuarioVendedor+"/"+page+"/"+rowpage);
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
	        	   	        	  
	        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.PermiteDevolucion));            		
	        	   producto.setPermiteDevolucion(value==1?true:false);
	        	   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.LoteRequerido));            		
	        	   producto.setLoteRequerido(value==1?true:false);
	        	   producto.setObjProveedorID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Producto.ObjProveedorID))));
	        	   producto.setDiasAntesVen(cur.getInt(cur.getColumnIndex(NMConfig.Producto.DiasAntesVen)));
	        	   producto.setDiasDespuesVen(cur.getInt(cur.getColumnIndex(NMConfig.Producto.DiasDespuesVen)));
	        	   
	        	   
	           }while (cur.moveToNext());
	           
	           producto.setListaLotes(getLotesByProducto(content,producto.getId()));
	           
	           
          }
	   
	return producto;
	   
   }
   
   
   public synchronized static Lote[] getLotesByProducto(ContentResolver content,long ObjProductoID)throws Exception{
	   int cont=0;
	    Uri uri=Uri.parse(DatabaseProvider.CONTENT_URI_LOTE+""/*+"/"+String.valueOf(ObjProductoID)*/); 
		Cursor cur = content.query(uri,
		        null, //Columnas a devolver
		        "ObjProductoID=?",       //Condición de la query
		        new String[]{String.valueOf(ObjProductoID)} ,       //Argumentos variables de la query
   	        null); 
		Lote[] a_lote=new Lote[cur.getCount()];
		
		if (cur.moveToFirst()) 
		 {  
			 
	           do{ 
	        	   
	        	 a_lote[cont]=new Lote(
				        		   cur.getLong(cur.getColumnIndex(NMConfig.Producto.Lote.Id)),
				        		   cur.getString(cur.getColumnIndex(NMConfig.Producto.Lote.NumeroLote)),
				        		   cur.getInt(cur.getColumnIndex(NMConfig.Producto.Lote.FechaVencimiento))
				        		   ); 
	        	 cont++;
	           }while (cur.moveToNext());
	     }   
	   return a_lote.length==0?null:a_lote;
	   
	   
   }
   
   public synchronized static Lote[] getLotesByProducto(SQLiteDatabase bd ,long ObjProductoID)throws Exception{
	   int cont=0;
	    
		 StringBuilder query = new StringBuilder();
			query.append(" SELECT * ");
			query.append(" FROM Lote l ");
			query.append(" WHERE l.ObjProductoID= "+ ObjProductoID); 
			
		Cursor cur = DatabaseProvider.query(bd, query.toString()); 
		Lote[] a_lote=new Lote[cur.getCount()];
		
		if (cur.moveToFirst()) 
		 {  
			 
	           do{ 
	        	   
	        	 a_lote[cont]=new Lote(
				        		   cur.getLong(cur.getColumnIndex(NMConfig.Producto.Lote.Id)),
				        		   cur.getString(cur.getColumnIndex(NMConfig.Producto.Lote.NumeroLote)),
				        		   cur.getInt(cur.getColumnIndex(NMConfig.Producto.Lote.FechaVencimiento))
				        		   ); 
	        	 cont++;
	           }while (cur.moveToNext());
	     }   
	   return a_lote.length==0?null:a_lote;
	   
	   
   }
     
   public  static ArrayList<Producto> getArrayProductoFromLocalHost(Context content)throws Exception
	{  	 
		 int count=0;
		 ArrayList<Producto> a_prod=new ArrayList<Producto>(); 
		 SQLiteDatabase bd = Helper.getDatabase(content);
		 StringBuilder query; 
		try 
		{
			
			query = new StringBuilder();
			query.append(" SELECT * ");
			query.append(" FROM Producto p ");
			query.append(" WHERE p.Disponible <> 0  "); 
			Cursor cur = DatabaseProvider.query(bd, query.toString());
		 

			 if (cur.moveToFirst()) 
			 {  
				
				   do{
					   int value;
					   Producto producto=new Producto();
					   producto.setId(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Producto.Id))));
					   producto.setCodigo(cur.getString(cur.getColumnIndex(NMConfig.Producto.Codigo)));
					   producto.setNombre(cur.getString(cur.getColumnIndex(NMConfig.Producto.Nombre)));
					   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.EsGravable));            		
					   producto.setEsGravable(value==1?true:false);
					   producto.setListaPrecios(cur.getString(cur.getColumnIndex(NMConfig.Producto.ListaPrecios)));
					   producto.setListaBonificaciones(cur.getString(cur.getColumnIndex(NMConfig.Producto.ListaBonificaciones)));
					   producto.setCatPrecios(cur.getString(cur.getColumnIndex(NMConfig.Producto.CatPrecios)));
					   producto.setDisponible(cur.getInt(cur.getColumnIndex(NMConfig.Producto.Disponible)));
					   	        	  
					   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.PermiteDevolucion));            		
					   producto.setPermiteDevolucion(value==1?true:false);
					   value=cur.getInt(cur.getColumnIndex(NMConfig.Producto.LoteRequerido));            		
					   producto.setLoteRequerido(value==1?true:false);
					   producto.setObjProveedorID(Long.parseLong(cur.getString(cur.getColumnIndex(NMConfig.Producto.ObjProveedorID))));
					   producto.setDiasAntesVen(cur.getInt(cur.getColumnIndex(NMConfig.Producto.DiasAntesVen)));
					   producto.setDiasDespuesVen(cur.getInt(cur.getColumnIndex(NMConfig.Producto.DiasDespuesVen)));			   
					   
			           producto.setListaLotes(getLotesByProducto(bd,producto.getId()));
			           a_prod.add(producto);
				   }while (cur.moveToNext());
		           
			 }
			   //Sorting
			   Collections.sort(a_prod, new Comparator<Producto>() {
			           @Override
			           public int compare(Producto  producto1, Producto  producto2)
			           {
	
			               return  producto1.getNombre().compareTo(producto2.getNombre());
			           }
			       });
			   
		} catch (Exception e) {
			Log.d(ModelValorCatalogo.class.getName(), e.getMessage());
		} finally {
			if( bd != null )
			{	
				if(bd.isOpen())				
					bd.close();
				bd = null;
			}
		} 
		return a_prod;
	}
   
   public  static int UpdateProducto(ContentResolver content,SoapObject obj)throws Exception
   {  
	    ContentValues values = new ContentValues();
	    Uri uri=null;  
		for(int a=0;a<obj.getPropertyCount();a++)
		{
			SoapObject obj2=(SoapObject) obj.getProperty(a);   
			uri=Uri.parse(DatabaseProvider.CONTENT_URI_PRODUCTO+"/"+obj2.getPropertySafelyAsString("IdProducto")); 
			values.put("Disponible",Integer.valueOf(obj2.getPropertySafelyAsString("Disponible").toString()));
			content.update(uri, values, null, null);
		}		
		return 1; 
	   
   }    
   

}
