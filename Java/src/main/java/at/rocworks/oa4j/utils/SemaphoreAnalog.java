package at.rocworks.oa4j.utils;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger; 

public class SemaphoreAnalog implements Serializable {

    private int value = 0;

    public synchronized void await(int value) {
        while (value>this.value) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaphoreAnalog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }   
    
    public synchronized void awaitLower(int value) {
        while (value<=this.value) { 
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaphoreAnalog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }      

    public synchronized int dispatch(int value) {
        this.value = value;
        this.notify();
        return this.value;
    }
    
    public synchronized int addOne() {
        this.value++;
        this.notify();
        return this.value;
    }    
    
    public synchronized int getOne() {
        while (this.value==0) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaphoreAnalog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.value--;
        this.notify();
        return this.value;
    }
    
    public synchronized int getValue() {
        return this.value;
    }
}