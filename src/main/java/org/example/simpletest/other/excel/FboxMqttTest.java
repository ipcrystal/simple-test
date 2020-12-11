package org.example.simpletest.other.excel;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    static List<QidongDevice> DEVICES;
    static String[] TOPICS;

    private static final String DEVICE_ID = "device_id";
    private static final String TIME = "time";

    static {
        DEVICES = GetQidongDeviceFromExcel.devices();
        TOPICS = DEVICES.stream().map(QidongDevice::getMqttTopic).toArray(String[]::new);
    }

    public static void main(String[] args) {
        // step1: 创建mqtt客户端
        MqttClient fboxTestClient = null;
        try {
            fboxTestClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
            Map<String, QidongDevice> topicDeviceMap = DEVICES.stream().collect(Collectors.toMap(QidongDevice::getMqttTopic, Function.identity(), (a, b) -> a));
            // step2: 设置mqtt回调
            fboxTestClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("connection lost", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    log.info("------------------------------------------------------");
                    QidongDevice device = topicDeviceMap.get(topic);
                    log.info("TOPIC   =  {} | deviceName = {}", topic, device.getDeviceName());
                    // 验证正确性
                    String payload = new String(message.getPayload());
                    JSONObject json = JSONObject.parseObject(payload);
                    String deviceId = json.getString(DEVICE_ID);
                    // 正确性校验
                    boolean correctness = topic.endsWith(deviceId) && Objects.nonNull(json.get(TIME));
                    log.info("correctness = {} | message =  {}", correctness, payload);
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
