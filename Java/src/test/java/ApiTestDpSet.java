


import com.etm.api.base.JManagerClient;
import com.etm.api.base.JManager;
import com.etm.api.var.FloatVar;
import com.etm.api.var.TextVar;
import com.etm.api.utils.Debug;
import java.util.Date;
import java.util.logging.Level;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vogler
 */
public class ApiTestDpSet {
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.setProjName("BigData99").setManNum(10).init().start();
        new ApiTestDpSet().run();
        m.stop();        
    }      
    
    private void run() throws InterruptedException {
        int ret;
        
        Debug.out.info("--- DPSET BEG ---");                
        ret = JManagerClient.dpSet()
                .add("System1:ExampleDP_Trend1.:_original.._value", new FloatVar(Math.random()))
                .add("System1:ExampleDP_SumAlert.:_original.._value", new TextVar("hello world"))
                .await()
                .getRetCode();
        Debug.out.log(Level.INFO, "retCode={0}", ret);        
        Debug.out.info("--- DPSET END ---");                              
        
        Thread.sleep(1000);
        
        Debug.out.info("--- DPSETTIMED BEG ---");    
        Date t = new Date(new Date().getTime()+10000);
        ret = JManagerClient.dpSet()
                .timed(t)
                .add("System1:ExampleDP_Trend1.:_original.._value", new FloatVar(Math.random()))
                .add("System1:ExampleDP_SumAlert.:_original.._value", new TextVar("hello timed world "+t.toString()))
                .await()
                .getRetCode();
        Debug.out.log(Level.INFO, "retCode={0}", ret);        
        Debug.out.info("--- DPSETTIMED END ---");              
    }    
}
