/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oc4j.var;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author vogler
 */
public abstract class Variable implements Serializable {

    public abstract String formatValue();

    public abstract VariableType isA();

    public abstract Object getValueObject();

    @Override
    public String toString() {
        return formatValue();
    }

    public String getValueClassName() {
        return getValueObject().getClass().getSimpleName();
    }

    public int getVariableTypeAsNr() {
        return isA().value;
    }

    public static Variable newVariable(Object value) {
        if (value instanceof String) {
            return new TextVar((String) value);
        } else if (value instanceof Double) {
            return new FloatVar((Double) value);
        } else if (value instanceof BigDecimal) {
            return new FloatVar(((BigDecimal) value).doubleValue());            
        } else if (value instanceof Float) {
            return new FloatVar(((Float) value).doubleValue());
        } else if (value instanceof Integer) {
            return new IntegerVar((Integer) value);
        } else if (value instanceof Long) {
            return new LongVar((Long) value);             
        } else if (value instanceof Boolean) {
            return new BitVar((Boolean) value);
        } else if (value instanceof Character) {
            return new CharVar((Character) value);
        } else if (value instanceof Timestamp) {
            return new TimeVar(((Timestamp) value).getTime());
        } else if (value instanceof Date) {
            return new TimeVar(((Date) value).getTime());            
        } else {
            // TODO newVariable DynTypes
            throw new UnsupportedOperationException("Type "+value.getClass().getName()+" ["+value.toString()+"] not supported yet."); //To change body of generated methods, choose Tools | Templates.            
        }
    }

    public Bit32Var        getBit32Var() { return this instanceof Bit32Var ? (Bit32Var)this : null; }
    public Bit64Var        getBit64Var() { return this instanceof Bit64Var ? (Bit64Var)this : null; }    
    public BitVar          getBitVar() { return this instanceof  BitVar ? (BitVar)this : null; }    
    public CharVar         getCharVar() { return this instanceof CharVar ? (CharVar)this : null; }
    public DpIdentifierVar getDpIdentifierVar() { return this instanceof DpIdentifierVar ? (DpIdentifierVar)this : null; }
    public DynVar          getDynVar() { return this instanceof DynVar ? (DynVar)this : null; }
    public FloatVar        getFloatVar() { return this instanceof FloatVar ? (FloatVar)this : null; }
    public IntegerVar      getIntegerVar() { return this instanceof IntegerVar ? (IntegerVar)this : null; }
    public LongVar         getLongVar() { return this instanceof LongVar ? (LongVar)this : null; }
    public LangTextVar     getLangTextVar() { return this instanceof LangTextVar ? (LangTextVar)this : null; }
    public TextVar         getTextVar() { return this instanceof TextVar ? (TextVar)this : null; }
    public TimeVar         getTimeVar() { return this instanceof TimeVar ? (TimeVar)this : null; }
    public UIntegerVar     getUIntegerVar() { return this instanceof UIntegerVar? (UIntegerVar)this : null; }    

    public static Variable newBit32Var(long value) {return new Bit32Var(value);}        
    public static Variable newBit64Var(long value) {return new Bit64Var(value);}            
    public static Variable newBitVar(boolean value) {return new BitVar(value);}    
    public static Variable newBitVar(int value) {return new BitVar(value);}    
    public static Variable newCharVar(char value) {return new CharVar(value);}
    public static Variable newDpIdentifierVar(String value) {return new DpIdentifierVar(value);}                 
    public static Variable newDynVar() {return new DynVar();}    
    public static Variable newFloatVar(double value) { return new FloatVar(value);}
    public static Variable newFloatVar(float value) {return new FloatVar(value);}
    public static Variable newIntegerVar(int value) {return new IntegerVar(value);}
    public static Variable newIntegerVar(long value) {return new IntegerVar(value);}    
    public static Variable newLangTextVar() { return new LangTextVar(); }        
    public static Variable newTextVar(String value) { return new TextVar(value); }    
    public static Variable newTimeVar(long value) {return new TimeVar(value);}        
    public static Variable newTimeVar(Date value) {return new TimeVar(value);}    
    public static Variable newUIntegerVar(int value) {return new UIntegerVar(value);}
    public static Variable newLongVar(long value) {return new LongVar(value);}
    
    public static <T> T nvl(T a, T b) {
        return (a == null || a.toString().isEmpty())?b:a;
    }

}
