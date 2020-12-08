package org.example.simpletest.other.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 发布者
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@Slf4j
public class Publisher {

    static String CLIENT_ID = "publisher-" + UUID.randomUUID().toString();

    public static void main(String[] args) {

        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient publishClient = null;

        try {

            // 创建客户端
            publishClient = new MqttClient(MqttCommonConfig.BROKER, CLIENT_ID, persistence);
            // 创建链接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // 在重新启动和重新连接时不记住状态
            connOpts.setCleanSession(true);

            // 设置连接的用户名
            connOpts.setUserName(MqttCommonConfig.USERNAME);
            connOpts.setPassword(MqttCommonConfig.PASSWORD.toCharArray());

            // 建立连接
            publishClient.connect(connOpts);

            for (int i = 0; i < 30; i++) {
                TimeUnit.SECONDS.sleep(3);

                String newContent = "hello world" + "-" + UUID.randomUUID().toString();
                log.info("[PUBLISH] {}", newContent);

                // 创建消息
                MqttMessage message = new MqttMessage(newContent.getBytes());

                message.setQos(MqttCommonConfig.QOS);

                // 发布消息
                publishClient.publish(MqttCommonConfig.TEST_TOPIC, message);
            }

        } catch (MqttException me) {
            log.info("reason | {}", me.getReasonCode());
            log.info("msg | {}", me.getMessage());
            log.info("loc | {}", me.getLocalizedMessage());
            log.error(StringUtils.EMPTY, me);

        } catch (InterruptedException e) {
            log.error(StringUtils.EMPTY, e);
        } finally {
            if (Objects.nonNull(publishClient)) {
                try {
                    publishClient.disconnect();
                    // 断开连接
                    // 关闭客户端
                    publishClient.close();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
/*
发布步骤
1. 创建 client
2. 设置连接参数 并 connect
3. 创建消息 发布消息
 */