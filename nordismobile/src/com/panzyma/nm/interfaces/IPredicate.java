package com.panzyma.nm.interfaces;

import java.util.Collection; 
import java.util.List;

public interface IPredicate<T, Z> 
{ 
	boolean apply(T type ,List<Z>... objList);  
}
