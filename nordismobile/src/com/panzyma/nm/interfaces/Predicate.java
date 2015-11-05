package com.panzyma.nm.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Predicate  
{ 
	
	@SuppressWarnings("unchecked")
	public static <T, Z> Collection<T> filter(Collection<T> target,List<Z> target2, IPredicate<T,Z> predicate) 
	{
	    Collection<T> result = new ArrayList<T>();
	    for (T element : target) {
	        if (predicate.apply(element,target2)) 
	        {
	            result.add(element);
	        }
	    }
	    return result;
	}
	 
	public static  <T> T find(List<T> target, long ID, IFilterabble<T> predicate) 
	{	    
	    for (T element : target) 
	    {
	        if (predicate.search(element,ID))  
	        	return element; 
	    }
	    return null;
	}  
  
}
