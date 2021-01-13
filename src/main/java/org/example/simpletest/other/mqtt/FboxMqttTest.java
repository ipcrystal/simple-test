package org.example.simpletest.other.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

/**
 * 繁易 mqtt 测试
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@Slf4j
public class FboxMqttTest {

    static String BROKER = "tcp://47.101.184.101:1883";
    static String CLIENT_ID = "fboxtest-" + UUID.randomUUID().toString();
    static String[] TOPICS = {
            "/topic/js/nt/qidong/nanyang/+",
    };

    public static void main(String[] args) {
        // step1: 创建mqtt客户端
        MqttClient fboxTestClient = null;
        try {
            fboxTestClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());

            // step2: 设置mqtt回调
            fboxTestClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("connection lost", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    log.info("------------------------------------------------------");
                    log.info("TOPIC   -> {}", topic);
//                    log.info("QOS     -> {}", message.getQos());
                    log.info("message -> {}", new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("delivery complete {}", token.isComplete());
                }
            });

            // step3：设置mqtt连接参数 并连接
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            fboxTestClient.connect(options);

            // step4 : mqtt订阅主题
            fboxTestClient.subscribe(TOPICS);

        } catch (MqttException e) {
            log.error(StringUtils.EMPTY, e);
        } finally {
            log.info("subscriber is running ...");
        }
    }
}
