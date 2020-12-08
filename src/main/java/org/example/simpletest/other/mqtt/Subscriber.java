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

import java.util.Objects;
import java.util.UUID;

/**
 * 订阅者
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@Slf4j
public class Subscriber {

    static String CLIENT_ID = "subscriber-" + UUID.randomUUID().toString();

    public static void main(String[] args) {

        MqttClient subscribeClient = null;
        try {
            subscribeClient = new MqttClient(MqttCommonConfig.BROKER, CLIENT_ID, new MemoryPersistence());

            // 设置回调函数
            subscribeClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.info("connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    log.info("===== ===== ===== ===== =====");
                    log.info("TOPIC   -> {}", topic);
                    log.info("QOS     -> {}", message.getQos());
                    log.info("message -> {}", new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("delivery complete {}", token.isComplete());
                }
            });

            MqttConnectOptions options = new MqttConnectOptions();

            options.setCleanSession(true);
            options.setUserName(MqttCommonConfig.USERNAME);
            options.setPassword(MqttCommonConfig.PASSWORD.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);

            subscribeClient.connect(options);

            subscribeClient.subscribe(MqttCommonConfig.TEST_TOPIC, MqttCommonConfig.QOS);

        } catch (MqttException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(subscribeClient)) {
                try {
                    subscribeClient.close();
                } catch (MqttException e) {
                    log.error(StringUtils.EMPTY, e);
                }
            }
        }

    }
}
/*
订阅步骤
1. 创建 client
2. 设置回调
3. 设置连接参数并且连接
4. 订阅某个主题
 */
