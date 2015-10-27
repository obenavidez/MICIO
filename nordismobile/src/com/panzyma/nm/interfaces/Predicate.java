package com.panzyma.nm.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.panzyma.nm.serviceproxy.DetallePedido;
import com.panzyma.nm.serviceproxy.DevolucionProducto;

public class Predicate  
{ 
	
	@SuppressWarnings("unchecked")
	public static <T, Z> Collection<T> filter(Collection<T> target,List<Z> target2, IPredicate<T,Z> predicate) {
	    Collection<T> result = new ArrayList<T>();
	    for (T element : target) {
	        if (predicate.apply(element,target2)) 
	        {
	            result.add(element);
	        }
	    }
	    return result;
	}
  
}
