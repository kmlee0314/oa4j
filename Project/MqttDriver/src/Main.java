import java.util.logging.Level;

import at.rocworks.oa4j.driver.*;
import at.rocworks.oa4j.jni.Transformation;
import at.rocworks.oa4j.base.JDebug;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 * Created by vogler on 3/25/2017.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new Main().start(args);
    }

    public void start(String[] args) {
        try {
            MqttDriver driver = new MqttDriver(args);
            JDebug.setLevel(Level.INFO);
            driver.startup();
            JDebug.out.info("ok");
        } catch ( Exception ex ) {
            JDebug.StackTrace(Level.SEVERE, ex);
        }
    }

    public class MqttDriver extends JDriverSimple {

        private MqttClient mqtt;
        private String url; // connect url (e.g. tcp://iot.eclipse.org)
        private String cid; // client id
        private Boolean json = false;

        public MqttDriver(String[] args) throws Exception {
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
                if (args[i].equals("-json")) {
                    json=true;
                }
            }
        }

        @Override
        public Transformation newTransformation(String name, int type) {
            switch (type) {
                case 1000:
                    return new JTransTextVar(name, type);
                case 1001:
                    return json ? new JTransIntegerVarJson(name, type) : new JTransIntegerVar(name, type);
                case 1002:
                    return json ? new JTransFloatVarJson(name, type) : new JTransFloatVar(name, type);
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
