/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oc4j.jni;

import java.io.IOException;
import at.rocworks.oc4j.utils.Debug;

/**
 *
 * @author vogler
 */
public class ExternHdl {
      
    public static native String apiGetLogDir();
    public static native int apiGetManType();        
    public static native int apiGetManNum();       
    
    public static void init() throws IOException {
        String name;
        int type = apiGetManType();
        switch (type) {
            case 4: name="ui"; break;
            case 5: name="ctrl"; break;
            case 7: name="api"; break;
            default: name="java";
        }
        Debug.setOutput(apiGetLogDir()+"/WCCOA"+name+apiGetManNum()+"java");
        Debug.out.info("java ExternHdl init done. ");
    }            
}