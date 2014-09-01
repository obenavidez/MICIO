package com.panzyma.nm.view.adapter;
import java.util.ArrayList;

import com.panzyma.nm.custom.model.SpinnerModel;
import com.panzyma.nordismobile.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
/***** Adapter class extends with ArrayAdapter ******/
public class CustomAdapter extends ArrayAdapter<SpinnerModel> {
     
    private Activity activity;
    @SuppressWarnings("rawtypes")
	private ArrayList data;
    SpinnerModel tempValues=null;
    LayoutInflater inflater;
     
    /*************  CustomAdapter Constructor *****************/
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public CustomAdapter(
                          Context activitySpinner, 
                          int textViewResourceId,   
                          ArrayList objects
                         ) 
     {
        super(activitySpinner, textViewResourceId, objects);
         
        /********** Take passed values **********/
        activity = (Activity) activitySpinner;
        data     = objects;       
    
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
      }
 
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
	public SpinnerModel getItem(int position) {
		// TODO Auto-generated method stub
		return (SpinnerModel) data.get(position);
	}

	// This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {
 
        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);
         
        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (SpinnerModel) data.get(position);
         
        TextView codigo      = (TextView)row.findViewById(R.id.txtCodigo);
        TextView descripcion = (TextView)row.findViewById(R.id.txtDescripcion);        
         
        if( position == 0 ){
             
            // Default selected Spinner item 
            codigo.setText("Seleccione un elemento");
            descripcion.setText("");
        }
        else
        {
            // Set values for spinner each row 
            codigo.setText(tempValues.getCodigo());
            descripcion.setText(tempValues.getDescripcion());           
             
        }   
 
        return row;
      }
 }