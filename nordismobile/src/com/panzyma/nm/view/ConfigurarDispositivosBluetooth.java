package com.panzyma.nm.view;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.AppDialog;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.menu.ActionItem;
import com.panzyma.nm.menu.QuickAction;
import com.panzyma.nm.serviceproxy.Impresora;
import com.panzyma.nm.view.adapter.GenericAdapter;
import com.panzyma.nm.view.viewholder.BluetoothDeviceHolder;
import com.panzyma.nm.view.viewholder.ClienteViewHolder;
import com.panzyma.nordismobile.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("ShowToast")
@SuppressWarnings("unused")
public class ConfigurarDispositivosBluetooth extends Activity implements
		Handler.Callback {

	private ProgressDialog mProgressDlg;

	private ViewConfiguracion parent;

	private ListView lvdispositivos;

	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

	private BluetoothAdapter mBluetoothAdapter;

	private ToggleButton mActivateBtn;

	private Button mScanBtn;

	private Button mCancelBtn;

	private BluetoothDevice device;

	private ConfigurarDispositivosBluetooth context;

	private Object lock; 

	private GenericAdapter adapter;

	private Intent intent;
 
 
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
		context = this;
		mActivateBtn = (ToggleButton) findViewById(R.id.btn_activatebluetooth);
		mScanBtn = (Button) findViewById(R.id.btn_scan);
		mCancelBtn = (Button) findViewById(R.id.btn_cancel);
		lvdispositivos = (ListView) findViewById(R.id.lv_bluetoothdevices);
		adapter = new GenericAdapter(this, BluetoothDeviceHolder.class,
				mDeviceList, R.layout.list_item_device);
		lvdispositivos.setAdapter(adapter);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FINISH_ACTIVITY();
			}
		});

		lvdispositivos
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						device = mDeviceList.get(position);
						establerComunicacion(
								"Esta es la Impresora de Trabajo?",
								"Confirme x favor....", device);
						return false;
					}
				});
		mProgressDlg = new ProgressDialog(this);
		mProgressDlg.setMessage("Buscando dispositivo");
		mProgressDlg.setCancelable(false);
		mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
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
						mActivateBtn.setChecked(false);
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
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
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
	public boolean handleMessage(Message msg) {
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
					showToast("bluetooth habilitado");
					showEnabled();
					mBluetoothAdapter.startDiscovery();
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				mDeviceList = new ArrayList<BluetoothDevice>();
				mProgressDlg.show();
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				mProgressDlg.dismiss();
				fillgriddevice();
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				final int state = intent
						.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,
								BluetoothDevice.ERROR);
				final int prevState = intent.getIntExtra(
						BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE,
						BluetoothDevice.ERROR);

				if (state == BluetoothDevice.BOND_BONDED
						&& prevState == BluetoothDevice.BOND_BONDING) {
					showToast("Vinculado");
					synchronized (lock) {
						lock.notify();
					}
				} else if (state == BluetoothDevice.BOND_NONE
						&& prevState == BluetoothDevice.BOND_BONDED) {
					showToast("desvinculado");
				}
				adapter.notifyDataSetChanged();
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mDeviceList.add(device);
				showToast("dispositivo encontrado ==> " + device.getName());
			}
		}
	};

	private void vincularDispositivo(BluetoothDevice device) {
		try {
			Method method = device.getClass().getMethod("createBond",
					(Class[]) null);
			method.invoke(device, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void desvincularDispositivo(BluetoothDevice device) {
		try {
			Method method = device.getClass().getMethod("removeBond",
					(Class[]) null);
			method.invoke(device, (Object[]) null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showEnabled() {
		mActivateBtn.setEnabled(true);
		mScanBtn.setEnabled(true);
	}

	private void showDisabled() { 
		 mScanBtn.setEnabled(false);
	}

	private void showUnsupported() {
		showToast("Bluetooth is unsupported by this device");
		mActivateBtn.setChecked(false);
		mScanBtn.setEnabled(false);
	}

	private void fillgriddevice() {
		runOnUiThread(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				adapter.setItems(mDeviceList);
				adapter.notifyDataSetChanged();

			}
		});

	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestcode, resultcode, data);
		try {
			if (android.app.Activity.RESULT_CANCELED == resultcode)
				mActivateBtn.setChecked(false);
			else
				mActivateBtn.setChecked(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	public void establerComunicacion(final String titulo, final String mensaje,
			BluetoothDevice... _device) {

		try {
			((NMApp) getApplication()).getThreadPool().execute(new Runnable() {

				@Override
				public void run() {

					try {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								AppDialog
										.showMessage(
												context,
												titulo,
												mensaje,
												AppDialog.DialogType.DIALOGO_CONFIRMACION,
												new AppDialog.OnButtonClickListener() {
													@Override
													public void onButtonClick(
															AlertDialog _dialog,
															int actionId) 
													{
														if (AppDialog.OK_BUTTOM == actionId) 
														{
															if (!(device!=null && device.getBondState() == BluetoothDevice.BOND_BONDED)) 
																vincularDispositivo(device); 
															
														}
														synchronized (lock) {
																lock.notify();
														} 
													}
												});
							}
						});

						synchronized (lock) {
							try {
								lock.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					} catch (Exception e) {
					}
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void FINISH_ACTIVITY(boolean... withresult) 
	{
		int requescode = 1;
		if (mProgressDlg != null)
			mProgressDlg.dismiss();
		if(withresult.length>0 && withresult[0])
		{
			intent = new Intent();
			Bundle b = new Bundle();
			b.putParcelable("impresora",Impresora.nuevaIntacia(device.getName(),device.getAddress(),device.getBondState()));
			Log.d(ConfigurarDispositivosBluetooth.this.getClass().getSimpleName(), "Activity quitting");
			setResult(requescode, intent);
		}
		finish();
	}

}
