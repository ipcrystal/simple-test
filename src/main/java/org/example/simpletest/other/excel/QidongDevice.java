package org.example.simpletest.other.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author zhuzhenjie
 * @since 2020/12/11
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class QidongDevice {

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备属性列表
     */
    private List<DeviceAttribute> deviceAttributeList;

    /**
     * mqtt的topic
     */
    private String mqttTopic;

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    @Data
    public static class DeviceAttribute {

        /**
         * 对应fbox属性的名称
         */
        private String fboxAttrName;
        /**
         * 监测属性中文名称
         */
        private String attrCNName;
        /**
         * 监测属性英文名称
         */
        private String attrENName;
    }
}
