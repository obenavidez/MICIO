package com.panzyma.nm.viewdialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Pedido;
import com.panzyma.nm.serviceproxy.Promocion;
import com.panzyma.nm.view.ViewPedidoEdit;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.PromocionesViewHolder;
import com.panzyma.nordismobile.R;

public class DialogPromociones extends Dialog{

	

	private Pedido pedido;
	
	private TextView tv_codigo;
	private TextView tv_promocion;

	private View gridDetallePromociones;
	ViewPedidoEdit parent;
	private GenericAdapter adapter;
	List<com.panzyma.nm.serviceproxy.Promocion> Lpromociones=new ArrayList<com.panzyma.nm.serviceproxy.Promocion>();
    Promocion promocionseleccionada;
	private Button btnaceptar;

	private OnButtonClickHandler mButtonClickListener;

	private ListView gridpromociones;

	private TextView tvdpromociones;
	
	private final Handler handler=new Handler();

	private QuickAction quickAction;
	
	private static final int ID_APLICARPROMOCIONES = 1;
	private static final int ID_CERRAR = 2;
	
	ArrayList<Promocion> promociones;
	
	public interface OnButtonClickHandler {
		public abstract void onButtonClick(Promocion promocion);
	}
	
    public void setOnDialogPromocionesButtonClickListener(OnButtonClickHandler listener) {
		mButtonClickListener = listener;
	} 

	public DialogPromociones(ViewPedidoEdit vpe,Pedido p,ArrayList<Promocion> _promociones, int theme) {
		super(vpe,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        setContentView(R.layout.promociones); 
        pedido=p; 
        parent=vpe;
        promociones=_promociones;
        initComponent();
        
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponent() { 
		gridDetallePromociones=findViewById(R.id.pgrilla);
		tvdpromociones=(TextView)findViewById(R.id.tv_desc_promocion);
		gridpromociones=((ListView)gridDetallePromociones.findViewById(R.id.data_items));
		btnaceptar=((Button)findViewById(R.id.btn_ok));
		btnaceptar.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) 
			{ 
				mButtonClickListener.onButtonClick(promocionseleccionada);		
				dismiss();
			}});
		((Button)findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {  	
				dismiss();
			}});
		gridpromociones.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				promocionseleccionada=(Promocion) adapter.getItem(position);
				adapter.setSelectedPosition(position);
				
				try 
				{ 					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							tvdpromociones.setText(promocionseleccionada.getDescripcionPromocion());
						}
					});
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		 }); 
		 
		Lpromociones=promociones;  
		adapter=new GenericAdapter(parent,PromocionesViewHolder.class,Lpromociones,R.layout.aplicarpromociones);				 
		gridpromociones.setAdapter(adapter);   
		
		
	} 
	
	public void initMenu()
	{
		quickAction = new QuickAction(parent, QuickAction.VERTICAL, 1);		
		if(Lpromociones.size()!=0)
		{
			quickAction.addActionItem(new ActionItem(ID_APLICARPROMOCIONES,"Aplicar Promoción Seleccionada"));
			quickAction.addActionItem(null);
		}
		quickAction.addActionItem(new ActionItem(ID_CERRAR, "Cerrar"));		
	}

}
