


import at.rocworks.oa4j.base.JClient;
import at.rocworks.oa4j.base.JDpConnect;
import at.rocworks.oa4j.base.JDpHLGroup;
import at.rocworks.oa4j.base.JDpMsgAnswer;
import at.rocworks.oa4j.base.JManager;
import at.rocworks.oa4j.base.JDebug;

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
        JDebug.out.info("dpConnect...");
        final Counter c = new Counter();
        JDpConnect conn = JClient.dpConnect()
                .add("ExampleDP_Trend1.")
                .action((JDpMsgAnswer answer)->{
                    JDebug.out.info("--- ANSWER BEG ---");
                    JDebug.out.info(answer.toString());
                    JDebug.out.info("--- ANSWER END ---");
                })                
                .action((JDpHLGroup hotlink)->{
                    c.value+=hotlink.size();
                    //if (c.value % 1000 == 0) 
                    {
                        JDebug.out.info("--- HOTLINK BEG ---");
                        JDebug.out.info(hotlink.toString());
                        JDebug.out.info("--- HOTLINK END ---");
                    }
                })
                .connect();
        
        JDebug.out.info("sleep...");
        Thread.sleep(1000*60);
        JDebug.out.info("done");
        conn.disconnect();
        JDebug.out.info("end");
    }          
}

