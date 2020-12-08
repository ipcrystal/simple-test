package org.example.simpletest.service.mqtt;

import org.example.simpletest.config.mqtt.MqttPublisherConfig;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * MQTT生产者消息发送接口
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@Component
@MessagingGateway(defaultRequestChannel = MqttPublisherConfig.CHANNEL_NAME_OUT)
public interface MqttPublisher {

    /**
     * 发布消息到broker
     *
     * @param payload 消息
     */
    void publishToBroker(String payload);

    /**
     * 发布消息到broker
     *
     * @param topic   主题
     * @param payload 消息
     */
    void publishToBroker(@Header(MqttHeaders.TOPIC) String topic,
                         String payload);


    /**
     * 发布消息到broker
     *
     * @param topic   主题
     * @param qos     qos
     * @param payload 消息
     */
    void publishToBroker(@Header(MqttHeaders.TOPIC) String topic,
                         @Header(MqttHeaders.QOS) int qos,
                         String payload);

}
