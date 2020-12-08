package org.example.simpletest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.simpletest.service.TestMqttService;
import org.example.simpletest.service.mqtt.MqttPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@Service
@Slf4j
public class TestMqttServiceImpl implements TestMqttService {

    @Autowired
    private MqttPublisher mqttPublisher;

    @Override
    public boolean publishToDefaultTopic(String msg) {
        log.info("publish to default topic ---> msg = {}", msg);
        mqttPublisher.publishToBroker(msg);
        return true;
    }

    @Override
    public boolean publish(String topic, String msg) {
        log.info("publish ---> msg = {}", msg);
        mqttPublisher.publishToBroker(topic, msg);
        return true;
    }

    @Override
    public boolean publish(String topic, Integer qos, String msg) {
        log.info("publish ---> qos = {} ,msg = {}", qos, msg);
        mqttPublisher.publishToBroker(topic, qos, msg);
        return true;
    }
}
