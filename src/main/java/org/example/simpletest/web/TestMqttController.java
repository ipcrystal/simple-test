package org.example.simpletest.web;

import org.example.simpletest.entites.dto.MqttPublishMsgDTO;
import org.example.simpletest.service.TestMqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestMqttController
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@RequestMapping("/testMqtt")
@RestController
@Validated
public class TestMqttController {

    @Autowired
    private TestMqttService testMqttService;

    /**
     * 发布到默认主题
     *
     * @param msgDTO {@link MqttPublishMsgDTO}
     * @return true
     */
    @PostMapping("/publishToDefaultTopic")
    public boolean publishToDefaultTopic(@RequestBody @Validated(MqttPublishMsgDTO.A.class) MqttPublishMsgDTO msgDTO) {
        String msg = msgDTO.getPayload();
        return testMqttService.publishToDefaultTopic(msg);
    }

    /**
     * 发布到自定义主题
     *
     * @param msgDTO {@link MqttPublishMsgDTO}
     * @return true
     */
    @PostMapping("/publishToDefineTopic")
    public boolean publishToDefineTopic(@RequestBody @Validated(MqttPublishMsgDTO.B.class) MqttPublishMsgDTO msgDTO) {
        String topic = msgDTO.getTopic();
        String msg = msgDTO.getPayload();
        return testMqttService.publish(topic, msg);
    }

    /**
     * 发布到自定义主题
     *
     * @param msgDTO {@link MqttPublishMsgDTO}
     * @return true
     */
    @PostMapping("/publish")
    public boolean publish(@RequestBody @Validated(MqttPublishMsgDTO.C.class) MqttPublishMsgDTO msgDTO) {
        String topic = msgDTO.getTopic();
        Integer qos = msgDTO.getQos();
        String msg = msgDTO.getPayload();
        return testMqttService.publish(topic, qos, msg);
    }

}
