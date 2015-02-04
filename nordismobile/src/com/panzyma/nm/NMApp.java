package com.panzyma.nm;
 

import com.panzyma.nm.auxiliar.AutenticationType;
import com.panzyma.nm.auxiliar.NMNetWork;
import com.panzyma.nm.auxiliar.ThreadPool;
import com.panzyma.nm.auxiliar.UserSessionManager;
import com.panzyma.nm.controller.Controller;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
@SuppressWarnings({"rawtypes"})
public class NMApp extends Application{ 	 
	
	public static  Controller controller;
	private static ThreadPool pool;
	public static AutenticationType tipoAutenticacion = AutenticationType.REMOTE;
	
	public static lifecycle ciclo;
	
	public enum lifecycle  {
		ONCREATE,
		ONSTART,
		ONRESUME,
		ONPAUSE,
		ONSTOP,
		ONRESTART;
	}
	
	public enum Modulo {
		HOME,
		CLIENTE,
		PRODUCTO,
		PEDIDO,
		RECIBO,
		CONFIGURACION,
		DEVOLUCION
	} 
	
	public static Context ctx;
	
	public static Modulo modulo;
	
	@Override
	public void onCreate() {
		super.onCreate();
		controller = new Controller();
		pool = new ThreadPool(5);
		setContext(this);
		NMNetWork.getDeviceId(this);
	}
	
	public static void setContext(Activity... actividad) {
		if(actividad!=null && actividad.length!=0)
				ctx=actividad[0];
	}
	
	public static Context setContext(NMApp app) {
		return ctx =app;
	}
	
	public static Context getContext() {
//		if(controller!=null && controller.getView()!=null)
//			return (Context) controller.getView();
//		else
			return ctx;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) { 
		super.onConfigurationChanged(newConfig);
	}
	
	public static Controller getController(){
		return controller;
	}
	
	public static ThreadPool getThreadPool()
	{
		return pool;
	}
	@Override
	public void onLowMemory() { 
		super.onLowMemory();
	}

	@Override
	public void onTerminate() { 
		super.onTerminate();
	}
	
	public static void killApp(boolean killSafely) {
        if (killSafely) {
            /*
             * Notify the system to finalize and collect all objects of the app
             * on exit so that the virtual machine running the app can be killed
             * by the system without causing issues. NOTE: If this is set to
             * true then the virtual machine will not be killed until all of its
             * threads have closed.
             */
            System.runFinalizersOnExit(true);

            /*
             * Force the system to close the app down completely instead of
             * retaining it in the background. The virtual machine that runs the
             * app will be killed. The app will be completely created as a new
             * app in a new virtual machine running in a new process if the user
             * starts the app again.
             */
            System.exit(0);
        } else {
            /*
             * Alternatively the process that runs the virtual machine could be
             * abruptly killed. This is the quickest way to remove the app from
             * the device but it could cause problems since resources will not
             * be finalized first. For example, all threads running under the
             * process will be abruptly killed when the process is abruptly
             * killed. If one of those threads was making multiple related
             * changes to the database, then it may have committed some of those
             * changes but not all of those changes when it was abruptly killed.
             */
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }
 
}
