package com.panzyma.nm.viewdialog;
 
import com.panzyma.nm.view.ViewPedidoEdit; 
import com.panzyma.nordismobile.R;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ExonerarImpuesto extends Dialog
{

	private EditText etauto_dgi;
	private Button btnok,btncancel; 

	
	public interface OnButtonClickListener {
		public abstract void onButtonClick(String exoneracion);
	}

	private OnButtonClickListener mButtonClickListener; 
	
    public void setOnDialog_EDGI_ButtonClickListener(OnButtonClickListener listener) {
		mButtonClickListener = listener;
	} 
	
	public ExonerarImpuesto(ViewPedidoEdit vpe) {
		super(vpe,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setContentView(R.layout.exonerar_impuesto);  
        initComponent(); 
	}

	private void initComponent() 
	{
		// TODO Auto-generated method stub
		etauto_dgi=(EditText)findViewById(R.id.et_autorizaciondgi);
		btnok=((Button)findViewById(R.id.btn_ok)); 
		btncancel=((Button)findViewById(R.id.btn_cancel)); 
		
		btnok.setOnClickListener(new View.OnClickListener() 
		{ 
    	    @Override
			public void onClick(View v) 
			{ 
    	    	mButtonClickListener.onButtonClick(etauto_dgi.getText().toString());
    	    	dismiss();
			}
    	    
		}
		);
		btncancel.setOnClickListener(new View.OnClickListener() 
		{ 
    	    @Override
			public void onClick(View v) 
			{ 
    	    	dismiss();
			}
    	    
		}
		);
	}

	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {        	
    	  	dismiss();
            return true;
	    }
        return super.onKeyUp(keyCode, event); 
    } 
	 
}
