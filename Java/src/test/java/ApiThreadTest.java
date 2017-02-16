


import com.etm.api.base.JManagerClient;
import com.etm.api.base.JDpHLGroup;
import com.etm.api.base.JDpMsgAnswer;
import com.etm.api.base.JDpQueryConnect;
import com.etm.api.base.JDpVCItem;
import com.etm.api.base.JManager;
import com.etm.api.var.DpIdentifierVar;
import com.etm.api.var.DynVar;
import com.etm.api.utils.Debug;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vogler
 */
public class ApiThreadTest {
    public static void main(String[] args) throws Exception {
        // add path to WCCOAjava.dll to your path environment!
        // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err         
        JManager m = new JManager();
        m.setProjName("BigData99").setManNum(10).setLoopWaitUSec(1000).init().start();
        new ApiThreadTest().run();
        m.stop();   
    }
    
    private volatile int c_dpGet=0;
    private volatile int c_dpNames=0;
    private volatile int c_dpConnect=0;
    private volatile int c_dpGetComment=0;
    
    private void run() throws InterruptedException {
        doDpQueryConnect();
        
        int delay = 0;
        
        //new Thread(()->doDpNames(delay)).start();
        
        //new Thread(()->doDpGet(delay)).start();
        //new Thread(()->doDpGet(delay)).start();
        
        //new Thread(()->doDpGetComment(delay)).start();
        int k=5;
        while ( true ) {
            Debug.out.log(Level.INFO, "dpGet {0} dpNames {1} dpConnect {2} dpGetComment {3}", 
                    new Object[] {c_dpGet/k, c_dpNames/k, c_dpConnect/k, c_dpGetComment/k});
            c_dpGet=c_dpNames=c_dpConnect=c_dpGetComment=0;
            Thread.sleep(k*1000);
        }
    }

    private void doDpGet(int delay) {
        List<Integer> x = new ArrayList<>();
        for ( int i=1; i<=100; i++ ) x.add(i);
        while ( true ) {
            x.parallelStream().forEach((i)->{
                JManagerClient.dpGet()
                        .add("System1:Test_1_"+String.valueOf(i)+".Value:_online.._value")
                        .action((JDpMsgAnswer answer)->{
                            c_dpGet++;
                            //Debug.out.info(answer.toString());
                        })
                        .await();
            });
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(ApiThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void doDpGetComment(int delay) {
        while ( true ) {
            DpIdentifierVar dpid = new DpIdentifierVar("System1:ExampleDP_Result.");
            String comment = JManagerClient.dpGetComment(dpid).toString();
            //Debug.out.info("Comment: "+comment);
            c_dpGetComment++;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(ApiThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void doDpNames(int delay) {
        while ( true ) {
            //Debug.out.info("--- DPNAMES BEG ---");        
            List<String> dps1 = Arrays.asList(JManagerClient.dpNames("ExampleDP*"));
            c_dpNames++;
            try {
                //dps1.forEach((dp)->Debug.out.info(dp));
                //Debug.out.info("--- DPNAMES END ---");
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(ApiThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }

    
    public void doDpQueryConnect() {
        Debug.out.info("dpQueryConnect...");
        JDpQueryConnect conn = JManagerClient.dpQueryConnectSingle("SELECT '_online.._value','_online.._stime' FROM 'Test*.**'")
                .action((JDpMsgAnswer answer)->{
//                    Debug.out.info("--- ANSWER BEG ---");
//                    Debug.out.info(answer.toString());
//                    Debug.out.info("--- ANSWER END ---");
                    
                })
                .action((JDpHLGroup hotlink)->{
                    {                                                
                        //Debug.out.log(Level.INFO, "--- HOTLINK BEG --- {0}", hotlink.getNumberOfItems());
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
                            c_dpConnect+=list.size();

//                            for ( int i=1; i<list.size(); i++ ) {
//                                // one data item in the list is also a list 
//                                DynVar row = (DynVar)list.get(i);                                
//                                if ( row.size() == 0 ) continue;
//                                
//                                // the row contains the selected columns/values in a list
//                                String dp = row.get(0).toString();    // column zero is always the dp
//                                Variable value = row.get(1);          // column one is the first colum '_online.._value'
//                                TimeVar stime = (TimeVar) row.get(2); // column two is the second colum '_online.._stime'                                      
//                                
//                                
//                                //Debug.out.log(Level.INFO, "dp={0} value={1} stime={2}", new Object[]{dp, value.toString(), stime.toString()});
//                            }
                        }
                        //Debug.out.log(Level.INFO, "--- HOTLINK END --- ");                        
                    }
                })
                .connect();         
    }
}