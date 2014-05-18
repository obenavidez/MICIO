package com.panzyma.nm.controller;
  
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.Map;

import com.panzyma.nm.Main;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

@SuppressLint("ParserError")
@SuppressWarnings({"hiding","unchecked","unused","rawtypes"})
public class Controller<T, U>
{
	T bridge;
	U view;
	private static final String TAG = Controller.class.getSimpleName(); 
	private final HandlerThread inboxHandlerThread;
	private final Handler inboxHandler; 
	
	Map<String,Handler> outboxHandlers=new HashMap<String,Handler>();
	private final List<T> bridges= new ArrayList<T>();
	private final List<U> views= new ArrayList<U>();
	 
	public Controller(T VbridgeM,U view) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{			 
		inboxHandlerThread = new HandlerThread("Controller Inbox="+view.getClass().getSimpleName()); 
		inboxHandlerThread.setPriority(Thread.MAX_PRIORITY);
		inboxHandlerThread.start();	 
		this.view=view;		
		this.bridge=(T) VbridgeM.getClass().getConstructor(this.getClass(),view.getClass()).newInstance(this,view);	
		inboxHandler = new Handler(inboxHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				try {
					Controller.this.handleMessage(msg);
				} catch (SecurityException e) { 
					e.printStackTrace();
				} catch (IllegalAccessException e) { 
					e.printStackTrace();
				} catch (InvocationTargetException e) { 
					e.printStackTrace();
				} catch (NoSuchMethodException e) { 
					e.printStackTrace();
				}
			}
		};
		Log.d("Controller","constructor Controller after inboxHandler y de instanciar BClienteM");
	}
 
	public Controller()
	{			 
		inboxHandlerThread = new HandlerThread("Controller Inbox");
		inboxHandlerThread.setPriority(Thread.MAX_PRIORITY);
		inboxHandlerThread.start();  
		inboxHandler = new Handler(inboxHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				try {
					Controller.this.handleMessage(msg);
				} catch (SecurityException e) { 
					e.printStackTrace();
				} catch (IllegalAccessException e) { 
					e.printStackTrace();
				} catch (InvocationTargetException e) { 
					e.printStackTrace();
				} catch (NoSuchMethodException e) { 
					e.printStackTrace();
				}
			}
		};
		Log.d("Controller","constructor Controller after inboxHandler y de instanciar BClienteM");
	}
	public  boolean hasEntities()
	{
		if(bridge!=null && this.view!=null)
			return true;
		return false;
	}	
	
	
	public  void setEntities(U _view,T _bridge) throws Exception
	{
		view=this.createObjectU(_view);		
		bridge=this.createObjectT(_bridge);
	}
	
	public  void setEntities(U _view,String _bridge) throws Exception
	{
		view=this.createObjectU(_view);		
		bridge=this.createObjectT(_bridge);
	}
	
	public U getView()
	{
		return view;
	}
	
	public T getBridge()
	{
		return bridge;
	}
	
	public  void disposeEntities()
	{
		view=null;		
		bridge=null;
	}
	
    public void removebridge(T _bridge)
    {
    	bridges.remove(_bridge);
    }
	
	public final void dispose() { 
		if(inboxHandlerThread.getLooper()!=null)
			inboxHandlerThread.getLooper().quit();
	}
	
	public final T createObjectT(String _bridge)
	{
		T obj = null;
		try 
		{
			int index=buscarObjeto(bridges,_bridge);
			if(index!=-1)
				obj=bridges.get(index);
			else{
				    Class theClass = Class.forName("com.panzyma.nm.CBridgeM."+_bridge);
					obj= (T) theClass.getClass().getConstructor(view.getClass()).newInstance(view);
					bridges.add(obj);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public final T createObjectT(T _bridge)
	{
		T obj = null;
		try 
		{
			int index=buscarObjeto(bridges,_bridge);
			if(index!=-1)
				obj=bridges.get(index);
			else{
					obj= (T) _bridge.getClass().getConstructor(view.getClass()).newInstance(view);
					bridges.add(obj);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public final U createObjectU(U _view)
	{
		U obj = null;
		try 
		{
			int index=buscarObjeto(views,_view);
			if(index!=-1)
				obj=views.get(index);
			else{
					obj= _view;
					views.add(obj);
				}
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return obj;
	}
	
    /**
     *Busca un objeto en una lista comparando unicamente su
     *posicion en memoria
     *@paramlista
     *@paramobj
     *@return
     */
    private static int buscarObjeto(List lista, Object obj){
    	String nclase=obj.getClass().getSimpleName().toString();
        for(int i=0;i<lista.size();i++){
        	String lclase=lista.get(i).getClass().getSimpleName().toString();
            if(lclase.equals(nclase)){
                return i;
            }
        }
        return -1;
    } 
	 
    
	public final Handler getInboxHandler() {
		
		return inboxHandler;
	}	   
	
	public final void addOutboxHandler(Handler handler) 
	{
		U _view=this.getView();
		
		
		if(_view==null)
			outboxHandlers.put(Main.TAG,handler);
		else
		{
			if(outboxHandlers.get(this.getView().getClass().getSimpleName())==null)
				outboxHandlers.put(this.getView().getClass().getSimpleName(),handler);
		}
	}	
	public final void removeOutboxHandler(String handler) {
		outboxHandlers.remove(handler);
	}
	public final void clearOutboxHandler() {
		outboxHandlers.clear();
	}
	/*
	public  List<Handler> getoutboxHandlers()
	{
		return outboxHandlers;
	}
	*/
	public  Map<String, Handler> getoutboxHandlers()
	{
		return outboxHandlers;
	}
	
	public final <T> void notifyOutboxHandlers(int what, int arg1, int arg2, ArrayList<T> obj) {
		if (outboxHandlers.isEmpty()) {
			Log.w(TAG, String.format("No outbox handler to handle outgoing message (%d)", what));
		} else {
			
			Object viewL=getView();
			Handler handler;
			if(viewL!=null) 
				 handler=outboxHandlers.get(viewL.getClass().getSimpleName());
			else
				handler=outboxHandlers.get(Main.TAG);
				
			if(handler!=null)
			{
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			}  
		}
	}
	public final void _notifyOutboxHandlers(int what, int arg1, int arg2, Object obj) {
		if (outboxHandlers.isEmpty()) {
			Log.w(TAG, String.format("No outbox handler to handle outgoing message (%d)", what));
		} else {
			Object viewL=getView();
			Handler handler;
			if(viewL!=null) 
				 handler=outboxHandlers.get(viewL.getClass().getSimpleName());
			else
				handler=outboxHandlers.get(Main.TAG);
				
			if(handler!=null)
			{
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			} 

		}
	}
	private void handleMessage(Message msg) throws SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Log.d(TAG, "Received message: " + msg);
		boolean result=(Boolean) this.bridge.getClass().getMethod("handleMessage", Message.class).invoke(bridge, msg);
		if (!result) {
			Log.w(TAG, "Unknown message: " + msg);
		}
	}
	  
 

}
