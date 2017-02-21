


import at.rocworks.oa4j.base.JClient;
import at.rocworks.oa4j.base.JDpConnect;
import at.rocworks.oa4j.base.JDpHLGroup;
import at.rocworks.oa4j.base.JDpMsgAnswer;
import at.rocworks.oa4j.base.JManager;
import at.rocworks.oa4j.utils.Debug;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vogler
 */
public class ApiTestDpConnect {    
    
    private class Counter {
        public volatile int value = 0;
    }
    
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.init(args).start();
        new ApiTestDpConnect().run();        
        m.stop();
    }
    
    public void run() throws InterruptedException {        
        Debug.out.info("dpConnect...");
        final Counter c = new Counter();
        JDpConnect conn = JClient.dpConnect()
                .add("ExampleDP_Trend1.")
                .action((JDpMsgAnswer answer)->{
                    Debug.out.info("--- ANSWER BEG ---");
                    Debug.out.info(answer.toString());
                    Debug.out.info("--- ANSWER END ---");
                })                
                .action((JDpHLGroup hotlink)->{
                    c.value+=hotlink.size();
                    //if (c.value % 1000 == 0) 
                    {
                        Debug.out.info("--- HOTLINK BEG ---");
                        Debug.out.info(hotlink.toString());
                        Debug.out.info("--- HOTLINK END ---");
                    }
                })
                .connect();
        
        Debug.out.info("sleep...");
        Thread.sleep(1000*60);
        Debug.out.info("done");
        conn.disconnect();
        Debug.out.info("end");   
    }          
}

