package com.panzyma.nm.auxiliar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID; 

import com.panzyma.nm.NMApp;
import com.panzyma.nm.controller.Controller;
import com.panzyma.nm.controller.ControllerProtocol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent; 
import android.os.Handler;

public class BluetoothComunication {

	
	static BluetoothAdapter mBluetoothAdapter;
	static BluetoothSocket mmSocket;
	static BluetoothDevice mmDevice;

	static OutputStream mmOutputStream;
	static InputStream mmInputStream;
	static Thread workerThread;

	static byte[] readBuffer;
	static int readBufferPosition;
	static int counter;
	static volatile boolean stopWorker; 
	
	private static BluetoothComunication cpp = new BluetoothComunication();

	public static BluetoothComunication newInstance() {
		if (cpp == null)
			cpp = new BluetoothComunication();
		return cpp;
	}

	public BluetoothComunication() {

		try 
		{
			findBT();
			openBT();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "static-access", "unused" })
	/*
	 * Buscar el dispositivo bluetooth
	 */
	public static void findBT() 
	{

		try 
		{
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			if (mBluetoothAdapter == null) {
				NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
						"Error con el dispositivo Bluetooth",
						"El Dispositivo no soporta comunicación bluetooth",""));
			}

			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				// startActivityForResult(enableBluetooth, 0);
			}

			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0) 
			{
				for (BluetoothDevice device : pairedDevices) {
 
					if (device.getAddress().equals(SessionManager.getImpresora().obtenerMac())) {
						mmDevice = device;
						break;
					}
				}
			} 
		} catch (Exception e) {
			NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"Error al intentar buscar el dispositivo bluetooth",
					e.getMessage(),e.getCause().toString())); 
				closeBT(); 
		}
	}

	/*
	 * Abrir conexion con la impresora
	 */
	public static void openBT() throws IOException 
	{
		try {
			// Standard SerialPortService ID
			UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
			mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			mmSocket.connect();
			mmOutputStream = mmSocket.getOutputStream();
			mmInputStream = mmSocket.getInputStream();

//			beginListenForData();
		
		} catch (Exception e)
		{
			closeBT();
			NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"Error al intentar abrir comunicación con el dispositivo bluetooth",
					e.getMessage(),e.getCause().toString()));
		}
	}

	/*
	 * Despues de abrir la conexion con la impresora, debemos escuchar o verificar si los datos fueron enviados a la impresora
	 */
	public static void beginListenForData() 
	{
		try 
		{
			final Handler handler = new Handler();

			// This is the ASCII code for a newline character
			final byte delimiter = 10;

			stopWorker = false;
			readBufferPosition = 0;
			readBuffer = new byte[1024];
			NMApp.getThreadPool().execute(new Runnable() 
			{
				@Override
				public void run() 
				{
					while (!Thread.currentThread().isInterrupted() && !stopWorker) {

						try 
						{

							int bytesAvailable = mmInputStream.available();
							if (bytesAvailable > 0) 
							{
								byte[] packetBytes = new byte[bytesAvailable];
								mmInputStream.read(packetBytes);
								for (int i = 0; i < bytesAvailable; i++) 
								{
									byte b = packetBytes[i];
									if (b == delimiter) 
									{
										byte[] encodedBytes = new byte[readBufferPosition];
										System.arraycopy(readBuffer, 0,encodedBytes, 0,encodedBytes.length);
										final String data = new String(encodedBytes, "US-ASCII");
										readBufferPosition = 0;

										handler.post(new Runnable() 
										{
											@Override
											public void run() 
											{ 
													//closeBT(); 
											}
										});
									} else 
									{
										readBuffer[readBufferPosition++] = b;
									}
								}
							}

						} catch (Exception e) {
							stopWorker = true;  
								closeBT();
								NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
										"Error al escuchar comunicación con el dispositivo bluetooth",
										e.getMessage(),e.getCause().toString())); 
						}

					}
				}
			}); 
		} catch (Exception e) {  
				closeBT(); 
		}
	}

	/*
	 * Enviar a imprimir datos a la impresora
	 */
	public void sendData(String _smg) throws IOException 
	{
		try 
		{			
			String msg = _smg; 
			if(mmOutputStream!=null)
			{
				mmOutputStream.write(msg.getBytes()); 
				closeBT();
			}
			else
			{
				findBT();
				openBT();
				if(mmOutputStream!=null)
					mmOutputStream.write(msg.getBytes()); 
				
			}
		
		} catch (Exception e) {
			closeBT();
			e.printStackTrace();
			NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"Error al enviar los datos al dispositivo bluetooth",
					e.getMessage(),e.getCause().toString()));
		}
	}

	/*
	 *Cerrar la comunicación con la impresora.
	 */
	public static void closeBT() 
	{
		try 
		{
			stopWorker = true;
			if(mmOutputStream!=null)
				mmOutputStream.close();
			if(mmInputStream!=null)
				mmInputStream.close();
			if(mmSocket!=null)
				mmSocket.close(); 
		} catch (Exception e) {
			e.printStackTrace();
			NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,new ErrorMessage(
					"Error al cerrar la comunicación con dispositivo bluetooth",
					e.getMessage(),e.getCause().toString()));
		} 
	}

}
