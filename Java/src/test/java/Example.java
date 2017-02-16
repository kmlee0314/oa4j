
import com.etm.api.base.JDpConnect;
import com.etm.api.base.JDpHLGroup;
import com.etm.api.base.JDpMsgAnswer;
import com.etm.api.base.JManager;
import com.etm.api.base.JManagerClient;
import com.etm.api.utils.Debug;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vogler
 */
public class Example {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.init(args).start();
        new Example().run();        
        m.stop();
    }   
    
    public void run() throws InterruptedException {        
        Debug.out.info("dpConnect...");
        JDpConnect conn = JManagerClient.dpConnect()
                .add("ExampleDP_Trend1.:_online.._value")     
                .action((JDpMsgAnswer answer)->{
                    Debug.out.info("--- ANSWER BEG ---");
                    Debug.out.info(answer.toString());
                    Debug.out.info("--- ANSWER END ---");
                })                
                .action((JDpHLGroup hotlink)->{
                    Debug.out.info("--- HOTLINK BEG ---");
                    Debug.out.info(hotlink.toString());
                    Debug.out.info("--- HOTLINK END ---");
 
                })
                .connect();
        
        Debug.out.info("sleep...");
        Thread.sleep(1000*60);
        Debug.out.info("done");
        conn.disconnect();
        Debug.out.info("end");   
    }              
}
