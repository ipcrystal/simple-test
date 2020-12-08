package org.example.simpletest.config.mqtt;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * MqttPublisherConfig
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@Configuration
public class MqttPublisherConfig {

    /**
     * 发布的bean的名称
     */
    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    /**
     * 客户端与服务器之前的连接意外中断，服务器将发布客户端的 “遗嘱” 消息
     */
    private static final byte[] WILL_DATA;

    static {
        WILL_DATA = "offline".getBytes();
    }

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.publisher.clientId}")
    private String clientId;

    @Value("${mqtt.publisher.defaultTopic}")
    private String defaultTopic;

    /**
     * mqtt连接选项
     *
     * @return
     */
    @Bean
    public MqttConnectOptions publisherMqttConnectionOption() {
        MqttConnectOptions options = new MqttConnectOptions();
        if (StringUtils.isNotBlank(username)) {
            options.setUserName(username);
        }
        if (StringUtils.isNotBlank(password)) {
            options.setPassword(password.toCharArray());
        }

        options.setServerURIs(StringUtils.split(broker, ","));
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
        options.setWill("/topic/will", WILL_DATA, 2, false);
        return options;
    }

    /**
     * MQTT客户端
     *
     * @return
     */
    @Bean
    public MqttPahoClientFactory publisherMqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(publisherMqttConnectionOption());
        return factory;
    }

    /**
     * mqtt信息通道 （生产者）
     *
     * @return
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt消息处理器 （生产者）
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                clientId,
                publisherMqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopic);
        return messageHandler;
    }

}
