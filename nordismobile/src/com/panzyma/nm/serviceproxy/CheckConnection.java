package com.panzyma.nm.serviceproxy;


public class CheckConnection {
    private boolean CheckConnectionResult; 
    public CheckConnection() {
    }
    
    public CheckConnection(boolean checkConnectionResult) {
        this.CheckConnectionResult = checkConnectionResult;
    }
    
    public boolean isCheckConnectionResult() {
        return CheckConnectionResult;
    }
    
    public void setCheckConnectionResult(boolean checkConnectionResult) {
        this.CheckConnectionResult = checkConnectionResult;
    }
}
