/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etm.api.base;

import com.etm.api.var.DpIdentifierVar;
import com.etm.api.var.LangTextVar;

/**
 *
 * @author vogler
 */
public class JManagerClient {
    
    public static boolean isConnected() {
        return JManager.getInstance().isConnected();
    }
    
    // dpGet
    
    public static JDpGet dpGet() {
        return (new JDpGet());
    }
    
    // dpSet
    
    public static JDpSet dpSet() {
        return (new JDpSet());
    }    

    // dpConnect
            
    public static JDpConnect dpConnect() {
        return (new JDpConnect());
    }
    
    // alertConnect
            
    public static JAlertConnect alertConnect() {
        return (new JAlertConnect());
    }    
    
    // dpQuery
    
    public static JDpQuery dpQuery(String query) {
        return (new JDpQuery(query).send());
    }
    
    // dpQueryConnect
    
    public static JDpQueryConnect dpQueryConnectSingle(String query) {
        return (new JDpQueryConnectSingle(query));
    }
    
    public static JDpQueryConnect dpQueryConnectAll(String query) {
        return (new JDpQueryConnectAll(query));
    }

    // dpNames
    
    public static String[] dpNames(String pattern) {
        return (String[])JManager.getInstance().executeTask(()->{
            return JManager.getInstance().apiGetIdSet(pattern);
        });
    }
    
    public static String[] dpNames(String pattern, String type) {
        return (String[])JManager.getInstance().executeTask(()->{
            return JManager.getInstance().apiGetIdSetOfType(pattern, type);
        });
    }    
    
    // dpComment   
    
    public static LangTextVar dpGetComment(DpIdentifierVar dpid) {  
        return (LangTextVar)JManager.getInstance().executeTask(()->{
            return JManager.getInstance().apiDpGetComment(dpid);
        });
    }
}
