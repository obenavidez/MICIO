package com.panzyma.nm.view.viewholder;
 
import android.bluetooth.BluetoothDevice;
import android.widget.Button;
import android.widget.TextView;
 



import com.panzyma.nm.view.adapter.InvokeView;
import com.panzyma.nordismobile.R;

public class BluetoothDeviceHolder {

	@InvokeView(viewId = R.id.btn_pair)
	public Button btn;
	@InvokeView(viewId = R.id.tv_name)
	public TextView divicename;
	@InvokeView(viewId = R.id.tv_address)
	public TextView macdivice;
	 
	public void mappingData(Object entity)
	{	
		final BluetoothDevice entidad=(BluetoothDevice)entity;		
		divicename.setText(entidad.getName());     
        macdivice.setText(entidad.getAddress());
        btn.setText((entidad.getBondState() == BluetoothDevice.BOND_BONDED) ?"Desvincular":"Vincular"); 
	}
}
