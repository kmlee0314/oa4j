


import com.etm.api.base.JManagerClient;
import com.etm.api.base.JManager;
import com.etm.api.utils.Debug;
import java.util.Arrays;
import java.util.List;
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
public class ApiTestDpNames {
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.setProjName("BigData99").setManNum(10).init().start();
        new ApiTestDpNames().run();        
        m.stop();
    }    
    
    public void run() throws InterruptedException {               
        // variant 1
        Debug.out.info("--- DPNAMES BEG ---");        
        List<String> dps1 = Arrays.asList(JManagerClient.dpNames("ExampleDP*"));
        dps1.forEach((dp)->Debug.out.info(dp));
        Debug.out.info("--- DPNAMES END ---");        
        
        // variant 2
        Debug.out.info("--- DPNAMES BEG ---");        
        String[] dps2 = JManagerClient.dpNames("*");   
        Debug.out.log(Level.INFO, "found {0} datapoints.", dps2.length);
        Debug.out.info("--- DPNAMES END ---");        

        // variant 3 with dpType
        Debug.out.info("--- DPNAMES BEG ---");        
        String[] dps3 = JManagerClient.dpNames("*", "ExampleDP_Float");
        Debug.out.log(Level.INFO, "found {0} datapoints.", (dps3==null ? "no" : dps3.length));
        List<String> lst3 = Arrays.asList(dps3);
        lst3.forEach((dp)->Debug.out.info(dp));
        Debug.out.info("--- DPNAMES END ---");        
    }              
}
