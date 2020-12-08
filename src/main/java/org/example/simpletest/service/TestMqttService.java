package org.example.simpletest.service;

/**
 * TestMqttService
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
public interface TestMqttService {

    /**
     * 发布到默认的topic
     *
     * @param msg 消息
     * @return
     */
    boolean publishToDefaultTopic(String msg);

    /**
     * 发布消息
     *
     * @param topic
     * @param msg
     * @return
     */
    boolean publish(String topic, String msg);

    /**
     * 发布消息
     *
     * @param topic
     * @param qos
     * @param msg
     * @return
     */
    boolean publish(String topic, Integer qos, String msg);

}
