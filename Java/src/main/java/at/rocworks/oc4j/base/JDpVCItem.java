/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oc4j.base;

import java.util.Date;

import at.rocworks.oc4j.var.BitVar;
import at.rocworks.oc4j.var.DpIdentifierVar;
import at.rocworks.oc4j.var.IntegerVar;
import at.rocworks.oc4j.var.TimeVar;
import at.rocworks.oc4j.var.FloatVar;
import at.rocworks.oc4j.var.TextVar;
import at.rocworks.oc4j.var.Variable;
import at.rocworks.oc4j.var.VariableType;

/**
 *
 * @author vogler
 */
public class JDpVCItem {
    private final DpIdentifierVar dpid;
    private final Variable var;
            
    public JDpVCItem(DpIdentifierVar dpid, Variable var) {
        this.dpid = dpid;
        this.var = var;
    }
    
    public JDpVCItem(String dp, Variable var) {   
        this(new DpIdentifierVar(dp), var);
    }    
    
    public JDpVCItem(String dp, Double value) {
        this(dp, new FloatVar(value));
    }
    
    public JDpVCItem(String dp, Integer value) {
        this(dp, new IntegerVar(value));
    }   
    
    public JDpVCItem(String dp, Long value) {
        this(dp, new IntegerVar(value));
    }            
    
    public JDpVCItem(String dp, String value) {
        this(dp, new TextVar(value));
    }    
    
    public JDpVCItem(String dp, Boolean value) {
        this(dp, new BitVar(value));
    }  
    
    public JDpVCItem(String dp, Date value) {
        this(dp, new TimeVar(value));
    }       

    public DpIdentifierVar getDpIdentifier() {
        return dpid;
    }

    public String getDpName() {
        return dpid.getName();
    }

    public Variable getVariable() {
        return var;
    }

    public Object getValueObject() {
        return var.getValueObject();
    }
    
    public String getValueClassName() {
        return var.getValueClassName();
    }
    
    public VariableType isA() {
        return var.isA();
    }
    
    public int getVariableTypeAsNr() {
        return var.getVariableTypeAsNr();
    }

    @Override
    public String toString() {
        return this.dpid + ": " + this.var + " [" + this.var.isA()+"]";
    }
}
