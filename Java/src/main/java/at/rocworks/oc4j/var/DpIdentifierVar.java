/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oc4j.var;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 *
 * @author vogler
 */
public class DpIdentifierVar extends Variable implements Serializable {
    String value;
    
//    public DpIdentifierVar(JDpId value) {
//        this.value=value;
//    }
    
    public DpIdentifierVar() {
        this.value="";
    }
    
    public DpIdentifierVar(String name) {
//        this.value = new JDpId(name);
        this.value=name;
    }
        
//    public void setValue(JDpId value) {
//        this.value=value;
//    }
    
    public String getValue() {
        return this.value;
    }
    
    public String getName() {
        return this.value;
    }
    
    public void setName(String name) {
        this.value=name;
    }    
    
    @Override
    public String formatValue() {
        return this.value;
    }

    @Override
    public VariableType isA() {
        return VariableType.DpIdentifierVar;
    }
    
    public boolean isInternal() {
        return this.value.contains(":_");
    }
    
    @Override
    public Object getValueObject() {
        return value; 
    }
    
    public String getSystem() {
        Pattern p = Pattern.compile(":");
        String[] s = p.split(this.value);
        return s[0];
    }

    public String getDp() {
        Pattern p = Pattern.compile(":|\\.");
        String[] s = p.split(this.value);
        return s[1];
    }
    
    public String getDpEl() {
        Pattern p = Pattern.compile(":");
        String[] s = p.split(this.value);
        return s[1];
    }
    
    public String getSysDpEl() {
        Pattern p = Pattern.compile(":");
        String[] s = p.split(this.value);
        return s[0]+":"+s[1];
    }    

    public String getElement() {
        int start = this.value.indexOf('.') + 1;
        int end = this.value.indexOf(':', start);
        return (end < 0 ? this.value.substring(start) : this.value.substring(start, end));
    }

    public String getConfig() {
        Pattern p = Pattern.compile(":");
        String[] s = p.split(this.value);
        return s.length == 3 ? s[2] : "";
    }    
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DpIdentifierVar other = (DpIdentifierVar) obj;
        return this.value.equals(other.value);
    }
    
    public static DpIdentifierVar newDpIdentifier() {
        return new DpIdentifierVar();
    } 
    
    public static DpIdentifierVar newDpIdentifier(String dp) {
        return new DpIdentifierVar(dp);
    } 
            
    public static DpIdentifierVar[] newDpIdentifier(String[] dps) {
        DpIdentifierVar[] res;
        res = new DpIdentifierVar[dps.length];
        for ( int i=0; i<dps.length; i++)
            res[i]=new DpIdentifierVar(dps[i]);        
        return res;
    }    
}
