package com.panzyma.nm.viewdialog;
 
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.panzyma.nm.serviceproxy.Cliente;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nordismobile.R;

public class DialogCondicionesNotas extends Dialog 
{
	private Pedido pedido;
	private Cliente cliente;
	
	private EditText etnotas;
	private EditText etbespecial;
	private EditText etpcondicionado;
	private EditText etpespecial;
	
	public interface OnButtonClickListener {
		public abstract void onButtonClick(Pedido pedido);
	}

	private OnButtonClickListener mButtonClickListener;
	private Button btnok;
	
    public void setOnDialogCNButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 

	public DialogCondicionesNotas(ViewPedidoEdit vpe,Pedido p,Cliente c, int theme) {
		super(vpe,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setContentView(R.layout.condiciones_notas); 
        pedido=p;
        cliente=c; 
        initComponent();
        
	}
	
	private void initComponent()
	{		
		etnotas=(EditText)findViewById(R.id.et_notas);
		etbespecial=(EditText)findViewById(R.id.et_bespecial);
		etpcondicionado=(EditText)findViewById(R.id.et_pcondicionado);
		etpespecial=(EditText)findViewById(R.id.et_pespecial);
		btnok=((Button)findViewById(R.id.btn_ok)); 
		
        String nota = "";
        if(pedido.getNota() != null) 
        	nota = pedido.getNota().trim();      
        
        if (!(cliente.getPermiteBonifEspecial())) {
        	etbespecial.setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.tv_bespecial)).setVisibility(View.INVISIBLE);
        }
        etpcondicionado.setEnabled(pedido.getCodEstado()== "REGISTRADO"); 
        
        if (!(cliente.getPermitePrecioEspecial())) 
        {
        	etpespecial.setVisibility(View.INVISIBLE);            
        	((TextView)findViewById(R.id.tv_pespecial)).setVisibility(View.INVISIBLE); 
        } 
        etpespecial.setEnabled(pedido.getCodEstado() == "REGISTRADO");
        btnok.setOnClickListener(new View.OnClickListener() 
		{ 
    	    @Override
			public void onClick(View v) 
			{   
    	    	pedido.setNota(etnotas.getText().toString().trim());        
    	        pedido.setBonificacionEspecial(false);
    	        pedido.setBonificacionSolicitada("");            
    	        if (cliente.getPermiteBonifEspecial()) 
    	        {
    	            pedido.setBonificacionEspecial(true);
    	            if (etbespecial.getText().toString().trim().compareTo("") == 0) 
    	            	pedido.setBonificacionEspecial(false);
    	            pedido.setBonificacionSolicitada(etbespecial.getText().toString().trim());
    	        }
    	        
    	        pedido.setPedidoCondicionado(true);
    	        pedido.setCondicion("");
    	        if (etpcondicionado.getText().toString().trim().compareTo("") == 0) 
    	        	pedido.setPedidoCondicionado(false);
    	        pedido.setCondicion(etpcondicionado.getText().toString().trim());
    	        
    	        pedido.setPrecioEspecial(false);
    	        pedido.setPrecioSolicitado("");
    	        if (cliente.getPermitePrecioEspecial()) {  
    	            pedido.setPrecioEspecial(true);    
    	            if (etpespecial.getText().toString().trim().compareTo("") == 0) 
    	            	pedido.setPrecioEspecial(false);
    	            pedido.setPrecioSolicitado(etpespecial.getText().toString().trim());
    	        }
	    		mButtonClickListener.onButtonClick(pedido);  
	    		dismiss();
			} 
		}
	    );
	}	 
	 

	 
}
