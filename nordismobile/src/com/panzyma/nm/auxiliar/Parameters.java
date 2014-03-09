package com.panzyma.nm.auxiliar;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.ksoap2.serialization.PropertyInfo;

public class Parameters extends PropertyInfo
{ 
	private static final long serialVersionUID = 1L;
	ArrayList<Parameters> arrayparams=new ArrayList<Parameters>();
	String[] paramname; 
	Object[] values; 
	Type[] type;
	public Parameters(){}
	public Parameters(String[] _paramname,Object[] _values,Type[] _type)
	{
		this.paramname=_paramname;
		this.values=_values;
		this.type=_type;
	}
	 
	public ArrayList<Parameters> getParameters()
	{
		for(int i=0;i<values.length;i++)
		{
			Parameters params=new Parameters();
			params.setName(this.paramname[i]);
			params.setValue(this.values[i]);
			params.setType(this.type[i]);  
			arrayparams.add(params); 
		} 
		return arrayparams;
	}
	public void setValues(Object[] _values){
		this.values=_values;
	}
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		super.setName(name);
	}

	@Override
	public void setType(Object type) {
		// TODO Auto-generated method stub
		super.setType(type);
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
		super.setValue(value);
	}

	
}
