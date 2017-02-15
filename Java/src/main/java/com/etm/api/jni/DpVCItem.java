/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etm.api.jni;

import com.etm.api.var.DpIdentifierVar;
import com.etm.api.var.Variable;


/**
 *
 * @author vogler
 */
public class DpVCItem extends Malloc {

    public DpVCItem() {
        super();
    }
    
    public DpVCItem(long cptr) {
        super.setPointer(cptr);
    }      
    
    @Override
    protected native long malloc();

    @Override
    protected native void free(long cptr);
    
    public native String toDebug(int level);        
    
    public native DpIdentifierVar getDpIdentifier();
    public native boolean setDpIdentifier(DpIdentifierVar dpid);
    
    public native Variable getValue();
    public native boolean setValue(Variable value);
}
