


import com.etm.api.base.JManagerClient;
import com.etm.api.base.JDpHLGroup;
import com.etm.api.base.JDpMsgAnswer;
import com.etm.api.base.JDpQueryConnect;
import com.etm.api.base.JDpVCItem;
import com.etm.api.base.JManager;
import com.etm.api.var.DynVar;
import com.etm.api.var.TimeVar;
import com.etm.api.var.Variable;
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
public class ApiTestDpQueryConnect {

    private class Counter {
        public volatile int value = 0;
    }    
    
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.setProjName("BigData99").setManNum(10).init().start();
        Debug.setOutput(m.getLogFile());
        new ApiTestDpQueryConnect().run();
        m.stop();
        
    }
    
    final Counter c = new Counter();
    Date t1 = new Date();
    Date t2 = new Date();
    
    
    public void run() throws InterruptedException {        
        Debug.out.info("dpQueryConnect...");
        JDpQueryConnect conn = JManagerClient.dpQueryConnectSingle("SELECT '_online.._value','_online.._stime' FROM 'Epics_*.Input'")
                .action((JDpMsgAnswer answer)->{
//                    Debug.out.info("--- ANSWER BEG ---");
//                    Debug.out.info(answer.toString());
//                    Debug.out.info("--- ANSWER END ---");
                    
                })
                .action((JDpHLGroup hotlink)->{                
                    //printHotlink(hotlink);
                    printStatistics(hotlink);
                })
                .connect();
        
        Debug.out.info("sleep...");
        Thread.sleep(1000*10);
        Debug.out.info("done");
        conn.disconnect();
        Debug.out.info("end");   
        Thread.sleep(1000*10);        
    }              
    
    private void printStatistics(JDpHLGroup hotlink) {
        if ( hotlink.getNumberOfItems() > 0 ) {
            JDpVCItem data = hotlink.getItem(1);            
            DynVar list = (DynVar)data.getVariable();                            
            c.value+=list.size();
                
            t2=new Date();
            long ms;
            if ( (ms=t2.getTime()-t1.getTime()) >= 1000 && c.value > 0) {
                Debug.out.log(Level.INFO, "v/s: {0}", c.value/(ms/1000));
                t1=t2;
                c.value=0;
            }
        }
    }

    private void printHotlink(JDpHLGroup hotlink) {
        Debug.out.log(Level.INFO, "--- HOTLINK BEG --- {0}", hotlink.getNumberOfItems());
        // first item is the header, it is a dyn of the selected attributes
        if ( hotlink.getNumberOfItems() > 0 ) {
            // second item contains the result data
            JDpVCItem data = hotlink.getItem(1);
            
            // the data item is a list of list
            // row 1: dpname | column-1 | column-2 | ...
            // row 2: dpname | column-1 | column-2 | ...
            // .....
            // row n: dpname | column-1 | column-2 | ...
            DynVar list = (DynVar)data.getVariable();
            for ( int i=1; i<list.size(); i++ ) {
                // one data item in the list is also a list
                DynVar row = (DynVar)list.get(i);
                if ( row.size() == 0 ) continue;
                
                // the row contains the selected columns/values in a list
                String dp = row.get(0).toString();    // column zero is always the dp
                Variable value = row.get(1);          // column one is the first colum '_online.._value'
                TimeVar stime = (TimeVar) row.get(2); // column two is the second colum '_online.._stime'
                
                Debug.out.log(Level.INFO, "dp={0} value={1} stime={2}", new Object[]{dp, value.toString(), stime.toString()});
            }
        }
        Debug.out.log(Level.INFO, "--- HOTLINK END --- ");
    }
}