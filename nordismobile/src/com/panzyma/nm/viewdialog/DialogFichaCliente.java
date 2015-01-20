package com.panzyma.nm.viewdialog; 

 
import static com.panzyma.nm.controller.ControllerProtocol.ALERT_DIALOG;

import java.util.Arrays;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.panzyma.nm.auxiliar.CustomDialog;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.serviceproxy.CCCliente;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.CNotaViewHolder;
import com.panzyma.nordismobile.R;
@SuppressWarnings({"static-access","unused", "rawtypes" })
public class DialogFichaCliente extends Dialog
{  
	private Controller controller=null;  
	private Context mcontext;  
	private GenericAdapter adapter;
    private long sucursalId; 
    private CustomDialog dialog;
    private int positioncache=0;
    private String TAG=DialogFichaCliente.class.getSimpleName(); 
	
    public DialogFichaCliente(Context context, int theme,CCCliente obj,long sucursalId) 
	{
        super(context, theme);   
        
        try 
        {      
            setContentView(R.layout.ficha_cliente);   
            this.sucursalId=sucursalId; 
        	mcontext=getContext();  
        	LoadData(obj); 

        }catch (Exception e) { 
			e.printStackTrace();
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();			  
		}	 
      
    }	
	
	@SuppressWarnings("unchecked")
	public void LoadData(final CCCliente objL)
	{   
		try
		{ 
			ListView lvcnotas = (ListView) findViewById(R.id.fclvnotas);
			
			
			if(objL.getIdCliente()!=0 && objL.getIdSucursal()!=0)
			{  
				((TextView) findViewById(R.id.fctextv_detallecliente)).setText(objL.getNombreCliente());
				((TextView) findViewById(R.id.fctextv_detallesucursal)).setText(objL.getNombreSucursal());
				((TextView) findViewById(R.id.fctextv_detalledireccion)).setText(objL.getDireccionSucursal());
				((TextView) findViewById(R.id.fctextv_detalletelefono)).setText(objL.getTelefono());
				((TextView) findViewById(R.id.fctextv_detalletipo)).setText(objL.getTipo()); 
				((TextView) findViewById(R.id.fctextv_detallecategoria)).setText(objL.getCategoria());
				((TextView) findViewById(R.id.fctextv_detalleprecioventa)).setText(objL.getPrecioVenta());
				((TextView) findViewById(R.id.fctextv_detallelimitecredito)).setText(String.valueOf(objL.getLimiteCredito())); 
				((TextView) findViewById(R.id.fctextv_detalleplazocredito)).setText((String.valueOf(objL.getPlazoCredito())));
				((TextView) findViewById(R.id.fctextv_detalleplazodescuento)).setText(String.valueOf(objL.getPlazoDescuento()));
				((TextView) findViewById(R.id.fctextv_detalleminabono)).setText(String.valueOf(objL.getMontoMinimoAbono()));
				((TextView) findViewById(R.id.fctextv_detalledescuento)).setText(objL.getDescuentos()); 
				TextView gridheader=(TextView) findViewById(R.id.fctextv_header2);
			
				if(objL.getNotas()!=null)
				{  
					gridheader.setText("NOTAS CLIENTE("+objL.getNotas().length+")");
					adapter=new GenericAdapter(mcontext,CNotaViewHolder.class,Arrays.asList(objL.getNotas()),R.layout.grid_cnota);
					lvcnotas.setAdapter(adapter);
					adapter.setSelectedPosition(positioncache=0); 
					lvcnotas.setOnItemClickListener(new OnItemClickListener() 
			        {
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			            { 		  
			            	if((parent.getChildAt(positioncache))!=null)						            							            		
			            		(parent.getChildAt(positioncache)).setBackgroundResource(android.R.color.transparent);						            	 
			            	positioncache=position; 				
			            	adapter.setSelectedPosition(position);  
			            	view.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.action_item_selected));
			            }
			        }); 
				}
				else
			    {
					gridheader.setText("NOTAS CLIENTE(0)");
					Log.d(TAG,"ViewCliente setData enty"); 
					TextView txtenty=(TextView) findViewById(R.id.fctxtview_enty);
		            txtenty.setVisibility(View.VISIBLE); 
		            lvcnotas.setEmptyView(txtenty);  
			    } 
				
			} 
		}
		catch(Exception e)
		{ 
			buildCustomDialog("Error !!!","Error Message:"+e.getMessage()+"\n Cause:"+e.getCause(),ALERT_DIALOG).show();
		}  			
			
	}
	public long get_SucursalID()
	{ 
		return (sucursalId!=0)?sucursalId:null;
	}
	public  Dialog buildCustomDialog(String tittle,String msg,int type)
	{
		return new CustomDialog(getContext(),tittle,msg);	    
	}
	 
}
