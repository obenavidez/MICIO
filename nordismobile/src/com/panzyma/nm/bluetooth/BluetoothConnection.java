package com.panzyma.nm.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import com.panzyma.nm.NMApp;
import com.panzyma.nm.auxiliar.ErrorMessage;
import com.panzyma.nm.auxiliar.SessionManager;
import com.panzyma.nm.controller.ControllerProtocol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothConnection 
{
	byte[] buffer;
	private final BluetoothAdapter mAdapter;
	private final BluetoothDevice mmDevice;
	public static HiloCliente hiloCliente;
	public static HiloConexion hiloConexion;
	public String TAG;
	public Object datos;

	public enum ESTADO_BLUETOOTH 
	{
		REALIZANDO_CONEXION, CONECTADO, DESCONECTADO, NINGUNO
	}

	public static ESTADO_BLUETOOTH estado_bluetooth;
	// Unique UUID for this application, you may use different
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public BluetoothConnection(Object _datos) throws Exception {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		// Get a BluetoothSocket for a connection with the given BluetoothDevice
		if (mAdapter == null)
			throw new Exception(
					"El Dispositivo no soporta comunicación bluetooth");
		if (!mAdapter.isEnabled())
			throw new Exception("bluetooth deshabilitado");
		mmDevice = findBT();
		if (mmDevice == null)
			throw new Exception("El Dispositivo no soporta comunicación bluetooth");
		if (_datos == null)
			throw new Exception("No hay datos que enviar al dispositivo bluetooth");
		
		datos=_datos; 
		solicitarConexion(mmDevice);

	}

	
	// Instancia un hilo conector
	public synchronized void solicitarConexion(BluetoothDevice dispositivo) throws Exception {
		Log.d("solicitarConexion()", "Iniciando metodo");
		// Comprobamos si existia un intento de conexion en curso.
		// Si es el caso, se cancela y se vuelve a iniciar el proceso
		if (estado_bluetooth == ESTADO_BLUETOOTH.REALIZANDO_CONEXION) {
			if (hiloCliente != null) {
				hiloCliente.cancelarConexion();
				hiloCliente = null;
			}
		}

		// Si existia una conexion abierta, se cierra y se inicia una nueva
		if (hiloConexion != null) {
			hiloConexion.cancelarConexion();
			hiloConexion = null;
		}

		// Se instancia un nuevo hilo conector, encargado de solicitar una
		// conexion
		// al servidor, que sera la otra parte.
		hiloCliente = new HiloCliente(dispositivo, getDatos());
		hiloCliente.start();
		estado_bluetooth = ESTADO_BLUETOOTH.REALIZANDO_CONEXION;
	}

	@SuppressWarnings("static-access")
	public BluetoothDevice findBT() {
		Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getAddress().equals(
						SessionManager.getImpresora().obtenerMac())) {
					return device;
				}

			}
		}
		return null;
	}

	// Sincroniza el objeto con el hilo HiloConexion e invoca a su metodo
	// escribir()
	// para enviar el mensaje a traves del flujo de salida del socket.
	public void enviar(Object buffer) {
		this.datos = buffer;

	}

	public Object getDatos(){
		return this.datos;
	}

	public static  void cerrarConexion() throws Exception
	{
		if (hiloCliente != null) 
		{
				hiloCliente.cancelarConexion();
				hiloCliente = null;
		} 

		// Si existia una conexion abierta, se cierra y se inicia una nueva
		if (hiloConexion != null) 
		{
			hiloConexion.cancelarConexion();
			hiloConexion = null;
		}

	}
	
	public synchronized void realizarConexion(BluetoothSocket socket,
			BluetoothDevice dispositivo, Object datos) 
	{
		try 
		{
			hiloConexion = new HiloConexion(socket, datos);	
			hiloConexion.start();
		} catch (Exception e) { 
			NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
		}
	
	}

	// Hilo encargado de solicitar una conexion a un dispositivo que este
	// corriendo un
	// HiloServidor.
	private class HiloCliente extends Thread {
		private final BluetoothDevice dispositivo;
		private final BluetoothSocket socket;
		private final Object datos;

		public HiloCliente(BluetoothDevice dispositivo, Object _datos) throws Exception 
		{

			BluetoothSocket tmpSocket = null;
			this.dispositivo = dispositivo;
			this.datos = _datos;
			// Obtenemos un socket para el dispositivo con el que se quiere
			// conectar 
			tmpSocket = dispositivo.createRfcommSocketToServiceRecord(MY_UUID); 

			socket = tmpSocket;
		}

		public void run() {
			setName("HiloCliente");
			if (mAdapter.isDiscovering())
				mAdapter.cancelDiscovery();

			try {
				socket.connect();
				estado_bluetooth = ESTADO_BLUETOOTH.REALIZANDO_CONEXION;
			} catch (IOException e) 
			{
				Log.e(TAG, "HiloCliente.run(): socket.connect(): Error realizando la conexion", e);
				try 
				{
					NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("","Error al tratar de comunicarse con la impresora",(e.getMessage()==null)?"":e.getMessage().toString()));
					socket.close();
				} catch (IOException inner) {
					NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
					Log.e(TAG, "HiloCliente.run(): Error cerrando el socket", inner);
				}
				
				estado_bluetooth = ESTADO_BLUETOOTH.NINGUNO;
				return;
			}

			// Reiniciamos el hilo cliente, ya que no lo necesitaremos mas
			synchronized (BluetoothConnection.this) {
				hiloCliente = null;
			}

			// Realizamos la conexion
			realizarConexion(socket, dispositivo, datos);
		}

		public void cancelarConexion() throws Exception {
			try {
				socket.close();
			} catch (Exception e) 
			{ 
				Log.e(TAG,"HiloCliente.cancelarConexion(): Error al cerrar el socket",e);
				throw new Exception("HiloCliente.cancelarConexion(): Error al cerrar el socket",e);
				
			}
			estado_bluetooth = ESTADO_BLUETOOTH.NINGUNO;
		}
	}

	// Hilo encargado de mantener la conexion y realizar las lecturas y
	// escrituras
	// de los mensajes intercambiados entre dispositivos.
	private class HiloConexion extends Thread {
		private final BluetoothSocket socket; // Socket
		private final InputStream inputStream; // Flujo de entrada (lecturas)
		private final OutputStream outputStream; // Flujo de salida (escrituras)
		private Object datos;

		public HiloConexion(BluetoothSocket socket, Object _datos) throws Exception {
			Log.d("HiloConexion.new()", "Iniciando metodo");
			this.socket = socket;
			this.datos = _datos;
			setName(socket.getRemoteDevice().getName() + " ["
					+ socket.getRemoteDevice().getAddress() + "]");

			// Se usan variables temporales debido a que los atributos se
			// declaran como final
			// no seria posible asignarles valor posteriormente si fallara esta
			// llamada
			InputStream tmpInputStream = null;
			OutputStream tmpOutputStream = null;

			// Obtenemos los flujos de entrada y salida del socket. 
			tmpInputStream = socket.getInputStream();
			tmpOutputStream = socket.getOutputStream(); 

			inputStream = tmpInputStream;
			outputStream = tmpOutputStream;
			
		}

		// Metodo principal del hilo, encargado de realizar las lecturas
		public void run() 
		{
			
			try 
			{
				Log.d("HiloConexion.run()", "Iniciando metodo");
				byte[] buffer = new byte[1024];
				int bytes=0;
				estado_bluetooth = ESTADO_BLUETOOTH.CONECTADO;
				//escribir(datos.toString().getBytes());
				escribir(datos.toString().getBytes());
				//cerrar la comunicacion con el dispositivo bluetooth
				BluetoothConnection.cerrarConexion();
				// Mientras se mantenga la conexion el hilo se mantiene en espera
				// ocupada
				// leyendo del flujo de entrada
				while (estado_bluetooth == ESTADO_BLUETOOTH.CONECTADO) 
				{
					
						// Leemos del flujo de entrada del socket
						bytes = inputStream.read(buffer);
						Log.d("HiloConexion.run(): leyendo entrada comunicación bluetooth ",((buffer!=null && buffer.length!=0)?buffer+"":"buffer VACIO"));
						// Enviamos la informacion a la actividad a traves del
						// handler.
						// El metodo handleMessage sera el encargado de recibir el
						// mensaje
						// y mostrar los datos recibidos en el TextView
						// handler.obtainMessage(MSG_LEER, bytes, -1,
						// buffer).sendToTarget();
						sleep(500);
					
				}
			} catch (Exception e) {
				NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
				Log.e(TAG,"HiloConexion.run(): Error al realizar la lectura",e);
			}
		}

		public void escribir(byte[] buffer) {
			Log.d("HiloConexion.escribir()", "Iniciando metodo");
			try 
			{
				
				// Escribimos en el flujo de salida del socket, asi enviando datos a la impresora.
				outputStream.write(buffer);
				outputStream.flush();
				Thread.sleep(2000);
				// Enviamos la informacion a la actividad a traves del handler.
				// El metodo handleMessage sera el encargado de recibir el
				// mensaje y mostrar los datos enviados
				
				NMApp.getController().notifyOutboxHandlers(ControllerProtocol.NOTIFICATION, 0, 0, "Los datos fueron enviados al dispositivo");
				
			} catch (Exception e) 
			{
				NMApp.getController().notifyOutboxHandlers(ControllerProtocol.ERROR, 0, 0,ErrorMessage.newInstance("",e.getMessage(),(e.getCause()==null)?"":e.getCause().toString()));
				Log.e(TAG,"HiloConexion.escribir(): Error al realizar la escritura",e);
			}
		}

		public void cancelarConexion() throws Exception 
		{
			Log.d("HiloConexion.cancelarConexion()", "Iniciando metodo");
			try {
				// Forzamos el cierre del socket
				socket.close();

				// Cambiamos el estado del servicio
				estado_bluetooth = ESTADO_BLUETOOTH.NINGUNO;
			} catch (IOException e) {
				Log.e(TAG,"HiloConexion.cerrarConexion(): Error al cerrar la conexion",e); 
				throw new Exception("HiloConexion.cerrarConexion(): Error al cerrar la conexion",e);
			}
		}
	}

}