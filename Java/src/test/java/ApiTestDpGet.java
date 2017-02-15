


import com.etm.api.base.JManagerClient;
import com.etm.api.base.JDpMsgAnswer;
import com.etm.api.base.JManager;
import com.etm.api.var.VariablePtr;
import com.etm.api.utils.Debug;
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
public class ApiTestDpGet {
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.setProjName("BigData99").setManNum(10).init().start();
        new ApiTestDpGet().run();
        m.stop();        
    }      
    
    private void run() throws InterruptedException {
        Debug.out.info("--- DPGET BEG ---");
        JManagerClient.dpGet()
                .add("System1:ExampleDP_Trend1.:_online.._value")
                .add("System1:ExampleDP_SumAlert.:_online.._value")
                .action((JDpMsgAnswer answer)->{
                    Debug.out.info("--- ANSWER BEG ---");
                    Debug.out.info(answer.toString());
                    Debug.out.info("--- ANSWER END ---");
                })       
                .await();
        Debug.out.info("--- DPGET END ---");
        
        
        
        Debug.out.info("--- DPGET BEG ---");
        JDpMsgAnswer answer = JManagerClient.dpGet()
                .add("System1:ExampleDP_Trend1.:_online.._value")
                .add("System1:ExampleDP_SumAlert.:_online.._value")
                .await();       
        Debug.out.log(Level.INFO, "ret={0}", answer.getRetCode());
        answer.getItems().forEach((vc)->{Debug.out.info(vc.toString());});
        Debug.out.info("--- DPGET END ---");                
        
        
        
        Debug.out.info("--- DPGET BEG ---");
        VariablePtr res1 = new VariablePtr();
        VariablePtr res2 = new  VariablePtr();        
        JManagerClient.dpGet()
                .add("System1:ExampleDP_Trend1.:_online.._value", res1)
                .add("System1:ExampleDP_SumAlert.:_online.._value", res2)
                .await();
        Debug.out.log(Level.INFO, "res1 is a {0} value={1}", new Object[] {res1.get().isA(), res1.get()});
        Debug.out.log(Level.INFO, "res2 is a {0} value={1}", new Object[] {res2.get().isA(), res2.get()});
        Debug.out.info("--- DPGET END ---");      
        
        
//        int i=0;
//        String oldc="", newc="";
//        while ( true ) {
//            JDpId dpid = new JDpId("System1:ExampleDP_Result.");
//            newc=JClient.dpGetComment(dpid).toString();
//            if ( !newc.equals(oldc) && ++i % 1000 == 0 ) {
//                Debug.out.log(Level.INFO, "comment: {0} {1}", new Object[] {i, newc});
//                oldc=newc;
//            }
//        }


    }       
    

}
