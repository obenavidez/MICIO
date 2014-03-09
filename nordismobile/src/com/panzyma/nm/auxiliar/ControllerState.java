package com.panzyma.nm.auxiliar;

import java.lang.reflect.InvocationTargetException;

import android.os.Message;

public interface ControllerState {
	boolean handleMessage(Message msg) throws SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
