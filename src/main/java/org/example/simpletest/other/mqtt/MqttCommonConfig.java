package org.example.simpletest.other.mqtt;

/**
 * @author zhuzhenjie
 * @since 2020/12/8
 */
public interface MqttCommonConfig {

    /**
     * mqtt服务器地址
     */
    String BROKER = "tcp://47.101.184.101:1883";

    String USERNAME = "test";
    String PASSWORD = "test";

    Integer QOS = 1;

    String TEST_TOPIC = "mqtt/test";
}
