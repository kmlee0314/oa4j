/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oc4j.jni;

import at.rocworks.oc4j.var.TimeVar;
import at.rocworks.oc4j.var.DpIdentifierVar;
import at.rocworks.oc4j.var.Variable;

/**
 *
 * @author vogler
 */
public class AnswerGroup extends Malloc {    
    public AnswerGroup() {
        super();
    }
    
    public AnswerGroup(long cptr) {
        super.setPointer(cptr);
    }    
    
    public native boolean insertItem(DpIdentifierVar id, Variable value, TimeVar time);
    
    @Override
    protected native long malloc();

    @Override
    protected native void free(long cptr);    
}
