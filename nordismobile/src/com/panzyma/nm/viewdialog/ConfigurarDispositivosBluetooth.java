package com.panzyma.nm.viewdialog;

import java.util.ArrayList;

import com.panzyma.nm.view.ViewConfiguracion;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.BluetoothDeviceHolder;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nordismobile.R;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressWarnings("unused")
public class ConfigurarDispositivosBluetooth extends FragmentActivity implements
		Handler.Callback {
	private ProgressDialog mProgressDlg;
	private static ConfigurarDispositivosBluetooth cdb = new ConfigurarDispositivosBluetooth();

	public static ConfigurarDispositivosBluetooth newInstance() {
		if (cdb == null)
			cdb = new ConfigurarDispositivosBluetooth();
		return cdb;
	}

	private ViewConfiguracion parent;

	private ListView lvdispositivos;
	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	private BluetoothAdapter mBluetoothAdapter;
	private ToggleButton mActivateBtn;
	private Button mScanBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_dialog);
		initComponent();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponent() {
		parent = (ViewConfiguracion) getParent();
		mActivateBtn = (ToggleButton) findViewById(R.id.btn_activatebluetooth);
		mScanBtn = (Button) findViewById(R.id.btn_scan);
		lvdispositivos = (ListView) findViewById(R.id.lv_bluetoothdevices);

		adapter=new GenericAdapter(this,BluetoothDeviceHolder.class,mDeviceList,R.layout.list_item_device);	
		lvdispositivos.setAdapter(adapter);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mProgressDlg = new ProgressDialog(this);
		mProgressDlg.setMessage("Scanning...");
		mProgressDlg.setCancelable(false);
		mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						mBluetoothAdapter.cancelDiscovery();
					}
				});
		if (mBluetoothAdapter == null) {
			showUnsupported();
		} else {
			mActivateBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mBluetoothAdapter.isEnabled()) {
						mBluetoothAdapter.disable();
						showDisabled();
					} else {
						Intent intent = new Intent(
								BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(intent, 1000);
					}
				}
			});
			mScanBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mBluetoothAdapter.startDiscovery();
				}
			});

			if (mBluetoothAdapter.isEnabled()) {
				showEnabled();
			} else {
				showDisabled();
			}
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onPause() {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}
		}

		super.onPause();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public boolean handleMessage(Message msg) 
	{ 
		return false;
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				final int state = intent.getIntExtra(
						BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

				if (state == BluetoothAdapter.STATE_ON) {
					showToast("Enabled");
					showEnabled();
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				mDeviceList = new ArrayList<BluetoothDevice>();
				mProgressDlg.show();
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				mProgressDlg.dismiss();

				// Intent newIntent = new Intent(MainActivity.this,
				// DeviceListActivity.class);
				//
				// newIntent.putParcelableArrayListExtra("device.list",
				// mDeviceList);
				//
				// startActivity(newIntent);
				fillgriddevice();
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				mDeviceList.add(device);
				
				showToast("Found device " + device.getName());
			}
		}
	};
	private GenericAdapter adapter;

	private void showEnabled() {
		mActivateBtn.setEnabled(true);
		mScanBtn.setEnabled(true);
	}

	private void showDisabled() {
		mActivateBtn.setChecked(false);
		mScanBtn.setEnabled(false);
	}

	private void showUnsupported() {
		showToast("Bluetooth is unsupported by this device");
		mActivateBtn.setChecked(false);
		mScanBtn.setEnabled(false);
	}

	private void fillgriddevice()
	{
		runOnUiThread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				
				adapter.setItems(mDeviceList);
				adapter.notifyDataSetChanged();

			}
		});
		
	}
}
