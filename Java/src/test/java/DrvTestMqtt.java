import com.etm.api.driver.JDriverSimple;
import com.etm.api.driver.JDriverItem;
import com.etm.api.driver.JDriverItemList;
import com.etm.api.driver.JTransFloatVarJson;
import com.etm.api.driver.JTransIntegerVarJson;
import com.etm.api.driver.JTransTextVar;
import com.etm.api.jni.Transformation;
import com.etm.api.utils.Debug;
import java.util.logging.Level;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vogler
 */
public class DrvTestMqtt {
    public static void main(String[] args) throws Exception {                        
        new DrvTestMqtt().start(args);
    }            
    
    public void start(String[] args) {       
        try {
            MyDriver driver = new MyDriver(args);

            Debug.setOutput(driver.getLogDir()+"/"+driver.getManName());
            Debug.setLevel(Level.INFO);        

            driver.startup();             
        //simInputData(driver);
        } catch ( Exception ex ) {
            Debug.StackTrace(Level.SEVERE, ex);
        }
    }
    
//    public void simInputData(MyDriver driver) {
//        // test input in a loop
//        new Thread(()->{
//            Integer i=0;
//            while ( true ) {
//                if ( driver.readQueue.size() > 10 ) {
//                    try {
//                        //Debug.out.warning("Input queue size > 10!");
//                        Thread.sleep(100);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(MqttDriver.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                } else {
//                    JDriverItemCollection block=new JDriverItemCollection(100);                
//                    for ( int j=1; j<=block.getSize(); j++) {
//                        JDriverItem item = new JDriverItem();
//                        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
//                        buffer.putInt(++i);
//                        item.fromBytesDataOnly(buffer.array());
//                        block.addItem(item);                
//                    }
//                    driver.readQueue.add(block);                              
//                }
//            }
//        }).start();        
//    }           
    
    public class MyDriver extends JDriverSimple {         

        private MqttClient mqtt;
        private String url; // connect url (e.g. tcp://iot.eclipse.org)
        private String cid; // client id 
        
        public MyDriver(String[] args) throws Exception {
            super(args, 10);
            url="";
            cid="";
            for (int i=0; i<args.length; i++) {
                if (args[i].equals("-url") && args.length>i+1) {
                    url=args[i+1];
                }
                if (args[i].equals("-cid") && args.length>i+1) {
                    cid=args[i+1];
                }                
            }            
        }
        
        @Override
        public Transformation newTransformation(String name, int type) {
            switch (type) {
                case 1000:
                    return new JTransTextVar(name, type);
                case 1001:
                    return new JTransIntegerVarJson(name, type);
                case 1002:
                    return new JTransFloatVarJson(name, type);
                default:
                    Debug.out.log(Level.WARNING, "unhandled transformation type {0} for {1}", new Object[]{type, name});
                    return null;
            }
        }        

        @Override
        public boolean start() {
            try {
                Debug.out.log(Level.INFO, "connect to mqtt...{0}", System.getProperty("java.io.tmpdir"));
                MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir"));                           
                mqtt = new MqttClient(url, cid, dataStore);
               
                // receive values
                mqtt.setCallback(new MqttCallbackImpl());
                                
                // connect
                MqttConnectOptions options  = new MqttConnectOptions();     
                options.setAutomaticReconnect(true);
                mqtt.connect(options); 
                Debug.out.info("connect to mqtt...done");                                                                              
                
                // -------------------------------------------------------------
                // send values
//                new Thread(()->processOutput()).start();                   
                
                return super.start();
            } catch (MqttException ex) {
                Debug.StackTrace(Level.SEVERE, ex);
                return false;
            }
        }                

//        private void processOutput() {
//            while (true) {
//                try {
//                    JDriverItemList data;
//                    peekOutputBlock();
//                    if (!mqtt.isConnected()) {
//                        Debug.out.warning("mqtt writer: not connected!");
//                        continue;
//                    }
//                    data=takeOutputBlock();
//                    
//                    if ( data != null ) {
//                        JDriverItem item;
//                        while ((item=data.getNextItem())!=null) {
//                            MqttMessage message = new MqttMessage(item.getData());
//                            try {
//                                //Debug.out.info("publish "+item.toString());
//                                mqtt.publish(item.getName(), message);
//                            } catch (MqttException ex) {
//                                Debug.StackTrace(Level.SEVERE, ex);
//                            }
//                        }
//                    }
//                } catch (InterruptedException ex) {
//                    Debug.StackTrace(Level.SEVERE, ex);
//                }
//            }
//        }
        
        @Override
        public void sendOutputBlock(JDriverItemList data) {
            JDriverItem item;
            while ((item=data.pollFirst())!=null) {
                MqttMessage message = new MqttMessage(item.getData());
                try {
                    //Debug.out.info("publish "+item.toString());
                    mqtt.publish(item.getName(), message);
                } catch (MqttException ex) {
                    Debug.StackTrace(Level.SEVERE, ex);
                }
            }        
        }        

        @Override
        public void stop() {
            Debug.out.info("disconnect to mqtt...");
            try {
                mqtt.disconnect();
            } catch (MqttException ex) {
                Debug.StackTrace(Level.SEVERE, ex);
            }
            Debug.out.info("disconnect to mqtt...done");
        }                                                             
        
        @Override
        public boolean attachInput(String addr) {
            if ( mqtt != null && mqtt.isConnected() ) {
                try {
                    Debug.out.log(Level.INFO, "attachInput addr={0} ... subscribe", new Object[]{addr});
                    mqtt.subscribe(addr);
                    return true;
                } catch (MqttException ex) {
                    Debug.StackTrace(Level.SEVERE, ex);
                    return false;
                }
            } else {
                return false;                   
            }            
        }

        @Override
        public boolean detachInput(String addr) {
            if ( mqtt != null && mqtt.isConnected() ) {
                try {
                    Debug.out.log(Level.INFO, "detachInput addr={0} ... unsubscribe", new Object[]{addr});                                
                    mqtt.unsubscribe(addr);
                    return true;
                } catch (MqttException ex) {
                    Debug.StackTrace(Level.SEVERE, ex);
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean attachOutput(String addr) {
            if ( mqtt != null && mqtt.isConnected() ) {
                Debug.out.log(Level.INFO, "attachOutput addr={0} ... subscribe", new Object[]{addr});
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean detachOutput(String addr) {
            if ( mqtt != null && mqtt.isConnected() ) {
                Debug.out.log(Level.INFO, "detachOutput addr={0} ... unsubscribe", new Object[]{addr});
                return true;
            } else {
                return false;
            }                
        }        

        private class MqttCallbackImpl implements MqttCallbackExtended {

            public MqttCallbackImpl() {
            }

            @Override
            public void connectionLost(Throwable thrwbl) {
                Debug.out.info("mqtt connection lost");
                lostAllAddresses();
            }

            @Override
            public void messageArrived(String tag, MqttMessage mm) throws Exception {
                //Debug.out.log(Level.INFO, "{0} => {1}", new Object[]{tag, new String(mm.getPayload())});
                JDriverItem item = new JDriverItem(tag, mm.getPayload());
                sendInputBlock(new JDriverItemList(item));
                //Debug.out.log(Level.INFO, "{0} => done", new Object[]{tag});
                
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken imdt) {
            }

            @Override
            public void connectComplete(boolean reconnect, String url) {
                Debug.out.info("mqtt connection complete reconnect="+reconnect+" url="+url);
                if (reconnect) {
                    attachAddresses();
                }
            }
        }
    }             
}
