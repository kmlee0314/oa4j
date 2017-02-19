/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oc4j.driver;

import at.rocworks.oc4j.base.JDpAttrAddrDirection;
import at.rocworks.oc4j.jni.Driver;
import at.rocworks.oc4j.utils.Debug;
import at.rocworks.oc4j.var.DpIdentifierVar;
import at.rocworks.oc4j.var.Variable;

import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author vogler
 */
public abstract class JDriver extends Driver implements Runnable {
    private static JDriver instance = null; // Singleton
        
    private String args[];    
           
    private volatile boolean apiEnabled = false; 
    
    private String projName="<unknown>";
    private String projDir=".";        
    private String confDir=".";
    private int manNum=1;  
        
    public JDriver(String args[]) throws Exception {         
//        this.transformationFactory=factory;
        initArgs(args);
        initDriver();
    }
        
    public static JDriver getInstance() {
        return JDriver.instance;
    }    
    
    public String getProjPath() { return projDir; }
    private JDriver setProjPath(String projDir) { 
        this.projDir=projDir; 
        this.confDir=this.projDir+"/config";                 
        return this; 
    }        
    
    public JDriver setProjName(String projName) {
        this.projName=projName;
        return this;
    }
    
    public String getConfigDir() { return confDir; }
    public String getLogDir() { return apiGetLogPath(); }
    public String getLogFile() { return getLogDir()+getManName(); }
    
    public boolean isEnabled() { return apiEnabled; }    
    
    public int getManNum() { return manNum; }
    public JDriver setManNum(int manNum) { this.manNum=manNum; return this; }    
    
    public String[] getArgs() { return args; }
    private void initArgs(String args[]) throws Exception {   
        this.args=args;
        for ( int i=0; i<args.length; i++ ) { 
            // projDir & configDir
            if ( args[i].equals("-path") && args.length>i+1 ) {
                setProjPath(args[i+1]);        
            }
            
            if ( args[i].equals("-proj") && args.length>i+1 ) {
                setProjName(args[i+1]);
            }            
                        
            // managerNum
            if ( args[i].equals("-num") && args.length>i+1 ) {
                setManNum(Integer.valueOf(args[i+1]));
            }                    
        }        
    }    
    
    private void initDriver() throws Exception {
        if (JDriver.instance == null) {
            JDriver.instance = this;
        } else {
            throw new Exception("There can only be one driver!");
        }   
                
        apiEnabled=false;        
        String errmsg="";        
        try {   
            System.loadLibrary("WCCOAjavadrv");
            apiEnabled=true;
        } catch ( java.lang.UnsatisfiedLinkError ex ) {            
            errmsg=ex.getMessage();
        }

        // Set log file settings
        try {
            Debug.setOutput(getLogFile());
        } catch (IOException ex) {
            Debug.StackTrace(Level.SEVERE, ex);
        }

        if ( !apiEnabled ) {
            Debug.out.warning(errmsg);            
        } 
    }    
            
    public String getManName() {
        return "WCCOAjavadrv"+manNum;
    }    
    
    public void startup() {   
        if ( apiEnabled ) {
            new Thread(this).start();
        }
    }    

    @Override
    public void run() {
        apiStartup(args);
    }
    
//    @Override
//    public Transformation newTransformation(int type) {
//        return transformationFactory.newTransformation(type);
//    }    

    @Override
    public void answer4DpId(int index, Variable var) {
//        Debug.out.log(Level.INFO, "answer4DpId {0}: {1}", new Object[]{index, var.formatValue()});
    }

    @Override
    public void hotLink2Internal(int index, Variable var) {
//        Debug.out.log(Level.INFO, "hotLink2Internal {0}: {1}", new Object[]{index, var.formatValue()});
    }

    @Override
    public boolean initialize(String[] argv) {
        return true;
    }

    @Override
    public boolean start() {
        return true;
    }    
    
    @Override
    public void stop() {
    }    
//    
//    @Override
//    public HWObject workProc() {      
//        return null;
//    }            

// Communication Flow:
//    WCCOAjavadrv2:2016.09.09 15:06:57.160 com.etm.api.base.JTransformation.getVariableTypeAsInt        INFO   : getVariableTypeAsInt
//    WCCOAjavadrv2:2016.09.09 15:06:57.262 com.etm.api.base.JTransformation.toPeriph                    INFO   : toPeriph: dlen=4 var=100 subindex=0
//    WCCOAjavadrv2:2016.09.09 15:06:57.364 com.etm.api.base.JTransformation.toPeriph                    INFO   : toPeriph: done
//    WCCOAjavadrv2:2016.09.09 15:06:57.365 com.etm.api.base.JDriver.writeData                           INFO   : writeData: address=test1 subix=0 orgTime=Fri Sep 09 15:06:57 CEST 2016 data=[B@3c0a29df
//    WCCOAjavadrv2:2016.09.09 15:06:57.366 com.etm.api.base.JDriver.flushHW                             INFO   : flushHW

    @Override
    public void addDpPa(DpIdentifierVar dpid, String addr, byte direction) {
        addDpPa(dpid, addr, JDpAttrAddrDirection.values()[direction]);
    }
    @Override
    public void clrDpPa(DpIdentifierVar dpid, String addr, byte direction) {
        clrDpPa(dpid, addr, JDpAttrAddrDirection.values()[direction]);
    }
    
    protected abstract void addDpPa(DpIdentifierVar dpid, String addr, JDpAttrAddrDirection direction);
    protected abstract void clrDpPa(DpIdentifierVar dpid, String addr, JDpAttrAddrDirection direction);   
}
