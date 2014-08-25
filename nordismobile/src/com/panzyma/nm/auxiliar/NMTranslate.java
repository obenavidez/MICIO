package com.panzyma.nm.auxiliar;

import java.lang.reflect.Array;
import java.lang.reflect.Field; 
import java.lang.reflect.Type; 
import java.util.ArrayList;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.google.gson.Gson;

@SuppressWarnings({"unchecked","unused"})
public class NMTranslate 
{   
	
	
	public static <U> U ToObject(JSONObject json,U unknowclass) throws Exception
	{		
		return (U) unknowclass.getClass().getMethod("ParseJSON_To_Entity",JSONObject.class).invoke(unknowclass,json);	
	}

	public static <U> ArrayList<U> ToCollection(org.json.JSONArray json,U unknowclass) throws Exception
	{		
		    ArrayList<U> objL=new ArrayList<U>();
	    	for(int i = 0;i<json.length();i++)
			{
				JSONObject obj=(JSONObject) json.get(i);
				objL.add((U) new Gson().fromJson(obj.toString(), unknowclass.getClass()));
			}  
	    	return objL;  
		//return (ArrayList<U>) unknowclass.getClass().getMethod("ParseArrayJSON_To_Entity",JSONArray.class).invoke(unknowclass,json);	
	}
	
	public synchronized static SoapObject ToObject(Object obj,String NAME_SPACE,String METHOD_NAME) throws Exception 
	{	 
		Field[] fields; 
    	Type type;   
    	Object d;
    	if(obj==null)
    		return null;
    	
    	SoapObject _obj=(SoapObject) obj;
    	fields=(obj.getClass().getDeclaredFields().length !=0)? obj.getClass().getDeclaredFields():obj.getClass().getFields();
		SoapObject rs=new SoapObject(NAME_SPACE,METHOD_NAME);
    	for(int i=0;i<fields.length;i++)
		{   
		        type=fields[i].getType();
		        if(type==android.os.Parcelable.Creator.class)
		        { 
		        	System.out.println(type.toString());
		        }  
		        else if(type==String.class)
		        { 
		        	d=_obj.getProperty(fields[i].getName());
		        	rs.addProperty(fields[i].getName(),_obj.getProperty(fields[i].getName()));
		        } 		        
		    	else if(type==Integer.class || type==Integer.TYPE)
		    		System.out.println(type.toString());
		    	else if(type==Float.class || type==Float.TYPE)
		    		System.out.println(type.toString());
		    	else if(type==Boolean.class || type==Boolean.TYPE)
		    		System.out.println(type.toString());
		    	else if(type==Long.class || type==Long.TYPE)System.out.println(type.toString());

		        
		        
		} 
    	
    	return null;
	}
	
	public synchronized static <U, T> U ToObject(Object obj,U unknowclass) throws Exception 
	{	    	 
    	Field[] fields; 
    	Type type;
    	PropertyInfo pInfo=new PropertyInfo();      
		fields=(unknowclass.getClass().getDeclaredFields().length !=0)? unknowclass.getClass().getDeclaredFields():unknowclass.getClass().getFields();
		
		if (obj != null) 
		{
			   
				for(int i=0;i<fields.length;i++)
				{   
				        type=fields[i].getType();  
					    Object value=((SoapObject) obj).getProperty(fields[i].getName()); 
					    fields[i].setAccessible(true); 
					    if(value!=null)
					    {
							    if(value.getClass()==org.ksoap2.serialization.SoapPrimitive.class)
							    {
							    	if(type==String.class)
							    		fields[i].set(unknowclass,value.toString());
							    	else if(type==Integer.class || type==Integer.TYPE)
								    	fields[i].set(unknowclass,(Integer.valueOf(value.toString())));
							    	else if(type==Float.class || type==Float.TYPE)
								    	fields[i].set(unknowclass,(Float.valueOf(value.toString())));
							    	else if(type==Boolean.class || type==Boolean.TYPE)
								    	fields[i].set(unknowclass,(Boolean.valueOf(value.toString())));
							    	else if(type==Long.class || type==Long.TYPE)
								    	fields[i].set(unknowclass,(Long.valueOf(value.toString()))); 
							    }
							    else if(value.getClass()==org.ksoap2.serialization.SoapObject.class)
							    {   
							    	SoapObject soap=(SoapObject) value;
							    	if(soap.getPropertyCount()!=0)
							    	{									    		 
								    	soap.getPropertyInfo(0, pInfo);
								    	Class<?> c=Class.forName("com.panzyma.nm.serviceproxy."+pInfo.getName()); 
								    	ArrayList<?> o=ToCollection(soap,c);
								    	T[] objrs =(T[]) Array.newInstance(c, o.size());  
								    	objrs=o.toArray(objrs); 
								    	fields[i].set(unknowclass,objrs); 	
							    	}			    	 
							    }
					    }
					 
				}
		}    
	   return unknowclass;
	}

	public synchronized static <U, T> ArrayList<T> ToCollection(Object object,Class<T> unKnowClass) throws Exception 
	{ 
    	Field[] fields;
    	SoapObject obj=(SoapObject) object;
        ArrayList<T> convertedObjects=new ArrayList<T>(obj.getPropertyCount());  
    	for(int a=0;a<obj.getPropertyCount();a++)
		{
    		SoapObject obj2=(SoapObject) obj.getProperty(a);  
    		convertedObjects.add(a, unKnowClass.newInstance()); 
    		fields=(convertedObjects.get(a).getClass().getDeclaredFields().length!=0)?convertedObjects.get(a).getClass().getDeclaredFields():convertedObjects.get(a).getClass().getFields();
    		 
    		for(int i=0;i<fields.length;i++)
			{  
    		 
    			Type type=fields[i].getType();
                fields[i].setAccessible(true); 
                Object value=obj2.getProperty(fields[i].getName());  
                if(value!=null)
                {
	                
                	if(value.getClass()==org.ksoap2.serialization.SoapPrimitive.class)
	                {
		    			if(type.equals(String.class))  					    				
		    		        fields[i].set(convertedObjects.get(a),value.toString());	 
		    			else if(type==Integer.class || type==Integer.TYPE)
					    	fields[i].set(convertedObjects.get(a),(Integer.valueOf(value.toString())));
				    	else if(type==Float.class || type==Float.TYPE)
					    	fields[i].set(convertedObjects.get(a),(Float.valueOf(value.toString())));
				    	else if(type==Boolean.class || type==Boolean.TYPE)
					    	fields[i].set(convertedObjects.get(a),(Boolean.valueOf(value.toString())));
				    	else if(type==Long.class || type==Long.TYPE)
					    	fields[i].set(convertedObjects.get(a),(Long.valueOf(value.toString())));
	    			} 
			    	else if(value.getClass()==org.ksoap2.serialization.SoapObject.class)
			    	{ 
			    		
				    	if(((SoapObject) value)!=null)
				    	{
					    		
				    		if(((SoapObject) value).getPropertyCount()!=0)
					    	{
					    			PropertyInfo pInfo=new PropertyInfo();  
						    		SoapObject arraysoap=(SoapObject) value;
						    		arraysoap.getPropertyInfo(0, pInfo); 
						    		Class<?> c= Class.forName("com.panzyma.nm.serviceproxy."+pInfo.getName());
						    		
						    		if(arraysoap.getProperty(0).getClass()==org.ksoap2.serialization.SoapObject.class) 
						    		{
						    			
										 U[] clasx =(U[]) Array.newInstance(c, arraysoap.getPropertyCount()); 
										 
								    	for(int e=0;e<arraysoap.getPropertyCount();e++)
								    	{ 								    		 
								    		clasx[e]=(U) c.newInstance();
								    		clasx[e]=(U) ToObject((SoapObject)arraysoap.getProperty(e),c.newInstance()); 
								    	}
								    	
								    	fields[i].set(convertedObjects.get(a),(Array.getLength(clasx)!=0)?clasx:null);
						    		}
						    		else
						    		{  
								    	Object objL =ToObject(arraysoap,c.newInstance()); 
								    	fields[i].set(convertedObjects.get(a),(Array.getLength(objL)!=0)?objL:null);
						    		}
						    		
					        } 
				    		
				    	}
						 
			        } 
                	
                }    			
			}
		} 
		return convertedObjects;				
	}

 
	
}
