package org.example.simpletest.entites.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * MqttPublishMsgDTO
 *
 * @author zhuzhenjie
 * @since 2020/12/8
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class MqttPublishMsgDTO {

    @NotNull(groups = {B.class, C.class}, message = "topic不能为空")
    private String topic;

    @NotNull(groups = {C.class}, message = "qos必须定义")
    @Size(min = 1, max = 3, message = "qos范围为[{min},{max}]")
    private Integer qos;

    @NotNull(groups = {A.class, B.class, C.class}, message = "mqtt内容不能为空")
    private String payload;

    public interface A {
    }

    public interface B {
    }

    public interface C {
    }
}
