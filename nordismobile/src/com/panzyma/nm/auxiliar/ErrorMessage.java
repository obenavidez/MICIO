package com.panzyma.nm.auxiliar; 
public class ErrorMessage {
	
	private String title;
	private String message;
	private String cause;
	
    private static ErrorMessage error;
	
	public static ErrorMessage newInstance(String title,String message,String cause) 
	{
		if(error==null)
			error = new ErrorMessage(title, message, cause);
	    return error;
	}
	
	public ErrorMessage(String title,String message,String cause){
		this.title=title;this.message=message;this.cause=cause;
	}
	public void setTittle(String title){
		this.title=title;
	}
	public void setMessage(String message){
		this.message=message;
	}
	public void setCause(String cause){
		this.cause=cause;
	}
	public String getTittle(){
		return this.title;
	}
	public String getMessage(){
		return this.message;
	}
	public String getCause(){
		return this.cause;
	}
}
