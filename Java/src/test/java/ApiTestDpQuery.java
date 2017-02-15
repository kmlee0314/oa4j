


import com.etm.api.base.JManagerClient;
import com.etm.api.base.JDpMsgAnswer;
import com.etm.api.base.JManager;
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
public class ApiTestDpQuery {
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.setProjName("BigData99").setManNum(10).init().start();
        new ApiTestDpQuery().run();        
        Debug.out.info("done");
        m.stop();
    }    
    
    public void run() throws InterruptedException {        
        Debug.out.info("dpQuery...");      
        JManagerClient.dpQuery("SELECT '_online.._value','_online.._stime' FROM 'Test*.**'")
                .action((JDpMsgAnswer answer)->{
                    Debug.out.info(answer.toString());                    
                })
                .await();
        Debug.out.info("dpQuery...done");
    }                  
}
