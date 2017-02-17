package com.etm.api.utils;

import java.util.logging.Level;
import java.util.logging.Logger; 

public class SemaphoreDigital {

    private boolean signal;
    
    public SemaphoreDigital(boolean signal) {
        this.signal=signal;
    }
    
    public SemaphoreDigital() {
        this.signal=false;
    }

    public synchronized void request() {
        while (!signal) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaphoreDigital.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        signal = false;
    }
    
    public synchronized void dispatch() {
        signal = true;
        this.notify();
    }    
    
    public synchronized boolean request(int timeout) {
        if (timeout <= 0) {
            SemaphoreDigital.this.request();
            return false; // no timeout
        } else {
            boolean timedout = false;
            while (!signal && !timedout) {
                try {
                    this.wait(timeout);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SemaphoreDigital.class.getName()).log(Level.SEVERE, null, ex);
                }
                timedout = !signal;
            }
            signal = false;
            return timedout;
        }
    }    
    
    public synchronized boolean read() {
        return signal;
    }
    
    public synchronized void sendTrue() {
        signal = true;
        this.notifyAll();
    }    
    
    public synchronized void sendFalse() {
        signal = false;
        this.notifyAll();
    }         
    
    public synchronized void awaitFalse() {
        while (signal) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaphoreDigital.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized void awaitTrue() {
        while (!signal) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaphoreDigital.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }   
       
}
