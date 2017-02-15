/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etm.api.var;

/**
 *
 * @author vogler
 */
public class UIntegerVar extends IntegerVar  {
    
    public UIntegerVar(Integer value) {
        super(value);
    }
    
    public UIntegerVar(int value) {
        super(value);
    }  
    
    @Override
    public VariableType isA() {
        return VariableType.UIntegerVar;
    }           
    
}
