package com.panzyma.nm.auxiliar;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;   

@SuppressWarnings({"rawtypes","unchecked"})
class PoolObject {
      

	private static HashMap<Class, LinkedList<Object>> createdObjects = new HashMap<Class, LinkedList<Object>>();
    private static HashMap<Class, LinkedList<Integer>> freeObjects = new HashMap<Class, LinkedList<Integer>>();
 
    /**
     * Obtiene un objeto creado si hubiese o uno nuevo si no existe ninguno disponible
     * @param Clase<T> 
     * @return T
    **/ 
    
	public static <T extends Object> T get(Class<T> clase) {
        try {
        	
        	T obj = null; 
            LinkedList objetos = createdObjects.get(clase);
            LinkedList<Integer> libres = freeObjects.get(clase);
            if (objetos == null) {
                objetos = new LinkedList();
                createdObjects.put(clase, objetos);
                libres = new LinkedList<Integer>();
                freeObjects.put(clase, libres);
            }
            
            if (libres.size() <= 0) {
                obj = clase.newInstance();
                objetos.add(obj);
            } else {
                obj =resetObject((T) objetos.get(libres.pop().intValue()));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     *Obtenemos un objeto del Pool
     */
	 
    public static void free(Object obj)
    {    	
        LinkedList objetos= createdObjects.get(obj.getClass());
        LinkedList<Integer> libres = freeObjects.get(obj.getClass());
        int index=buscarObjeto(objetos,obj);
        if(index<0){
            return;
        }
        libres.add(new Integer(index));
    }
 
    /**
     *Libera todos los objetos de tipo “a”[Clase]dellistado
     */
    public static void freeObjects(Class a){
        createdObjects.remove(a);
        freeObjects.remove(a);
    }
 
    /**
     *Busca un objeto en una lista comparando unicamente su
     *posicion en memoria
     *@paramlista
     *@paramobj
     *@return
     */
    private static int buscarObjeto(List lista, Object obj){
        for(int i=0;i<lista.size();i++){
            if(lista.get(i)==obj){
                return i;
            }
        }
        return -1;
    } 
    
	public static <T extends Object> T resetObject(T obj)
    {
    	Field fields[] = obj.getClass().getFields(); 
	    for (Field field : fields)
			try {
				field.set(obj,null);
			}  catch (Exception e) { 
				e.printStackTrace();
			}  
    	return obj;
    }
    
}