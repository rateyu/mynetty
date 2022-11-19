package z.g.g.zigbee;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ZigbeeTest {

    public static void main(String[] args) {

        String topic = "zigbee2mqtt/light/set";
        String content = "{\"state\":\"TOGGLE\"}";
        int qos = 1;
        String broker = "tcp://192.168.1.13:1883";
//        String userName = "test";
//        String password = "test";
        String clientId = "pubClient";


//        String subTopic = "testtopic/#";
//        String pubTopic = "testtopic/1";
//        String content = "Hello World";
        qos = 2;
//        String broker = "tcp://broker.emqx.io:1883";
//        String clientId = "emqx_test";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttAsyncClient  client = new MqttAsyncClient (broker, clientId,persistence);

//            logger.info("Connected to the: "+ conf.getServerURI() + " Mqtt Server");
            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setUserName("emqx_test");
//            connOpts.setPassword("emqx_test_password".toCharArray());
//             保留会话
            connOpts.setCleanSession(true);
            IMqttToken token = client.connect(connOpts);
            token.waitForCompletion();
            // 设置回调
            client.setCallback(new OnMessageCallback());

            // 建立连接
            System.out.println("Connecting to broker: " + broker);
//            client.connect(connOpts);

            System.out.println("Connected");
            System.out.println("Publishing message: " + content);

            // 订阅
//            client.subscribe(subTopic);

            // 消息发布所需参数
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
            System.out.println("Message published");

            client.disconnect();
            System.out.println("Disconnected");
            client.close();
            System.exit(0);

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
