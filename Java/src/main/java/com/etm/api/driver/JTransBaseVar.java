/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etm.api.driver;

import com.etm.api.jni.Transformation;
import com.etm.api.var.VariableType;
import com.etm.api.utils.Debug;
import java.util.logging.Level;

/**
 *
 * @author vogler
 */
public abstract class JTransBaseVar extends Transformation {
    private final int itemSize;
    private final VariableType varType;    
    
    public JTransBaseVar(String name, int type, VariableType varType, int itemSize) {
        super(name, type);
        this.varType = varType;
        this.itemSize = itemSize;
        //Debug.out.log(Level.INFO, "JTransformationBaseVar: name={0} type={1} var={2} size={3}", new Object[]{name, type, varType, itemSize});   
        //Debug.sleep(100);         
    }       

    @Override
    public int itemSize() {
        //Debug.out.log(Level.INFO, "itemSize");
        //Debug.sleep(100);
        return itemSize;
    }        

    @Override
    public int getVariableTypeAsInt() {
//        Debug.out.log(Level.INFO, "getVariableTypeAsInt");
//        Debug.sleep(100);
        return varType.value;
    }    
    
    public VariableType getVariableType() {
        return varType;
    }
    
    @Override
    public void delete() {
        //Debug.out.log(Level.INFO, "delete {0}", getName());
    }
}