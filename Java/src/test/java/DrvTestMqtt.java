import at.rocworks.oa4j.driver.JDriverSimple;
import at.rocworks.oa4j.driver.JDriverItem;
import at.rocworks.oa4j.driver.JDriverItemList;
import at.rocworks.oa4j.driver.JTransFloatVarJson;
import at.rocworks.oa4j.driver.JTransIntegerVarJson;
import at.rocworks.oa4j.driver.JTransTextVar;
import at.rocworks.oa4j.jni.Transformation;
import at.rocworks.oa4j.base.JDebug;
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
            JDebug.setLevel(Level.INFO);
            driver.startup();
        } catch ( Exception ex ) {
            JDebug.StackTrace(Level.SEVERE, ex);
        }
    }
    
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
                    JDebug.out.log(Level.WARNING, "unhandled transformation type {0} for {1}", new Object[]{type, name});
                    return null;
            }
        }        

        @Override
        public boolean start() {
            try {
                JDebug.out.log(Level.INFO, "connect to mqtt...{0}", System.getProperty("java.io.tmpdir"));
                MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir"));                           
                mqtt = new MqttClient(url, cid, dataStore);
               
                // receive values
                mqtt.setCallback(new MqttCallbackImpl());
                                
                // connect to mqtt
                MqttConnectOptions options  = new MqttConnectOptions();     
                options.setAutomaticReconnect(true);
                mqtt.connect(options);
                JDebug.out.info("connect to mqtt...done");
                
                return super.start();
            } catch (MqttException ex) {
                JDebug.StackTrace(Level.SEVERE, ex);
                return false;
            }
        }
        
        @Override
        public void sendOutputBlock(JDriverItemList data) {
            JDriverItem item;
            while ((item=data.pollFirst())!=null) {
                MqttMessage message = new MqttMessage(item.getData());
                try {
                    //JDebug.out.info("publish "+item.toString());
                    mqtt.publish(item.getName(), message);
                } catch (MqttException ex) {
                    JDebug.StackTrace(Level.SEVERE, ex);
                }
            }        
        }        

        @Override
        public void stop() {
            JDebug.out.info("disconnect to mqtt...");
            try {
                mqtt.disconnect();
            } catch (MqttException ex) {
                JDebug.StackTrace(Level.SEVERE, ex);
            }
            JDebug.out.info("disconnect to mqtt...done");
        }                                                             
        
        @Override
        public boolean attachInput(String addr) {
            if ( mqtt != null && mqtt.isConnected() ) {
                try {
                    JDebug.out.log(Level.INFO, "attachInput addr={0} ... subscribe", new Object[]{addr});
                    mqtt.subscribe(addr);
                    return true;
                } catch (MqttException ex) {
                    JDebug.StackTrace(Level.SEVERE, ex);
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
                    JDebug.out.log(Level.INFO, "detachInput addr={0} ... unsubscribe", new Object[]{addr});
                    mqtt.unsubscribe(addr);
                    return true;
                } catch (MqttException ex) {
                    JDebug.StackTrace(Level.SEVERE, ex);
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean attachOutput(String addr) {
            if ( mqtt != null && mqtt.isConnected() ) {
                JDebug.out.log(Level.INFO, "attachOutput addr={0} ... subscribe", new Object[]{addr});
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean detachOutput(String addr) {
            if ( mqtt != null && mqtt.isConnected() ) {
                JDebug.out.log(Level.INFO, "detachOutput addr={0} ... unsubscribe", new Object[]{addr});
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
                JDebug.out.info("mqtt connection lost");
                lostAllAddresses();
            }

            @Override
            public void messageArrived(String tag, MqttMessage mm) throws Exception {
                //JDebug.out.log(Level.INFO, "{0} => {1}", new Object[]{tag, new String(mm.getPayload())});
                JDriverItem item = new JDriverItem(tag, mm.getPayload());
                sendInputBlock(new JDriverItemList(item));
                //JDebug.out.log(Level.INFO, "{0} => done", new Object[]{tag});
                
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken imdt) {
            }

            @Override
            public void connectComplete(boolean reconnect, String url) {
                JDebug.out.info("mqtt connection complete reconnect="+reconnect+" url="+url);
                if (reconnect) {
                    attachAddresses();
                }
            }
        }
    }             
}
