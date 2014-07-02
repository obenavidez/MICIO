package com.panzyma.nm.auxiliar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler; 

public class BluetoothComunication { 
		
		// android built in classes for bluetooth operations
		BluetoothAdapter mBluetoothAdapter;
		BluetoothSocket mmSocket;
		BluetoothDevice mmDevice;
		
		OutputStream mmOutputStream;
		InputStream mmInputStream;
		Thread workerThread;
		
		byte[] readBuffer;
		int readBufferPosition;
		int counter;
		volatile boolean stopWorker;
 
	 public	BluetoothComunication(){
			
			try {
				findBT();
				openBT();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		/*
		 * This will find a bluetooth printer device
		 */
		void findBT() {

			try {
				mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

				if (mBluetoothAdapter == null) {
					//myLabel.setText("No bluetooth adapter available");
				}

				if (!mBluetoothAdapter.isEnabled()) {
					Intent enableBluetooth = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					//startActivityForResult(enableBluetooth, 0);
				}

				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
						.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						
						// MP300 is the name of the bluetooth printer device
						if (device.getName().equals("Zebra Ismael")) {
							mmDevice = device;
							break;
						}
					}
				}
				//myLabel.setText("Bluetooth Device Found");
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * Tries to open a connection to the bluetooth printer device
		 */
		void openBT() throws IOException {
			try {
				// Standard SerialPortService ID
				UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
				mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
				mmSocket.connect();
				mmOutputStream = mmSocket.getOutputStream();
				mmInputStream = mmSocket.getInputStream();

				beginListenForData();

				//myLabel.setText("Bluetooth Opened");
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * After opening a connection to bluetooth printer device, 
		 * we have to listen and check if a data were sent to be printed.
		 */
		void beginListenForData() {
			try {
				final Handler handler = new Handler();
				
				// This is the ASCII code for a newline character
				final byte delimiter = 10;

				stopWorker = false;
				readBufferPosition = 0;
				readBuffer = new byte[1024];
				
				workerThread = new Thread(new Runnable() {
					@Override
					public void run() {
						while (!Thread.currentThread().isInterrupted()
								&& !stopWorker) {
							
							try {
								
								int bytesAvailable = mmInputStream.available();
								if (bytesAvailable > 0) {
									byte[] packetBytes = new byte[bytesAvailable];
									mmInputStream.read(packetBytes);
									for (int i = 0; i < bytesAvailable; i++) {
										byte b = packetBytes[i];
										if (b == delimiter) {
											byte[] encodedBytes = new byte[readBufferPosition];
											System.arraycopy(readBuffer, 0,
													encodedBytes, 0,
													encodedBytes.length);
											final String data = new String(
													encodedBytes, "US-ASCII");
											readBufferPosition = 0;

											handler.post(new Runnable() {
												@Override
												public void run() {
													//myLabel.setText(data);
												}
											});
										} else {
											readBuffer[readBufferPosition++] = b;
										}
									}
								}
								
							} catch (IOException ex) {
								stopWorker = true;
							}
							
						}
					}
				});

				workerThread.start();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * This will send data to be printed by the bluetooth printer
		 */
		public void sendData(String _smg) throws IOException {
			try {
				
				// the text typed by the user
				String msg =_smg;// myTextbox.getText().toString();
				//msg += "\n";
				
				mmOutputStream.write(msg.getBytes());
				
				// tell the user data were sent
				//myLabel.setText("Data Sent");
				
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * Close the connection to bluetooth printer.
		 */
	public void closeBT() throws IOException {
			try {
				stopWorker = true;
				mmOutputStream.close();
				mmInputStream.close();
				mmSocket.close();
				//myLabel.setText("Bluetooth Closed");
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
}
