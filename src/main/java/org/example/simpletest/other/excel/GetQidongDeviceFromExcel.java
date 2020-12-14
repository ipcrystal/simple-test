package org.example.simpletest.other.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhuzhenjie
 * @since 2020/12/11
 */
public class GetQidongDeviceFromExcel {

    public static void main(String[] args) {
        devices().forEach(System.out::println);
    }


    public static List<QidongDevice> devices() {
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new ClassPathResource("project_qidong/启东南阳点位表.xls").getInputStream());
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            List<QidongDevice> qidongDeviceListTmp = Lists.newArrayList();
            for (int i = 1; i < rows; i++) {
                Cell[] row = sheet.getRow(i);
                String deviceName = StringUtils.trim(row[1].getContents());
                String deviceId = StringUtils.trim(row[4].getContents());
                String mqttTopic = StringUtils.trim(row[5].getContents());
//                System.out.println("deviceName = " + deviceName + "\tdeviceId = " + deviceId + "\tmqttTopic = " + mqttTopic);
                qidongDeviceListTmp.add(QidongDevice.builder().deviceName(deviceName).deviceId(deviceId).mqttTopic(mqttTopic).build());
            }

            // 数据压缩
            Map<String, QidongDevice> deviceNameMap = Maps.newTreeMap();
            for (QidongDevice tmpDevice : qidongDeviceListTmp) {
                String deviceName = tmpDevice.getDeviceName();
                QidongDevice device = deviceNameMap.get(deviceName);
                if (Objects.isNull(device)) {
                    device = QidongDevice.builder().deviceAttributeList(Lists.newArrayList()).build();
                    deviceNameMap.put(deviceName, device);
                }

                String tmpDeviceName = tmpDevice.getDeviceName();
                if (StringUtils.isNotBlank(tmpDeviceName)) {
                    device.setDeviceName(tmpDeviceName);
                }

                String tmpDeviceId = tmpDevice.getDeviceId();
                if (StringUtils.isNotBlank(tmpDeviceId)) {
                    device.setDeviceId(tmpDeviceId);
                }

                String tmpDeviceMqttTopic = tmpDevice.getMqttTopic();
                if (StringUtils.isNotBlank(tmpDeviceMqttTopic)) {
                    device.setMqttTopic(tmpDeviceMqttTopic);
                }
            }

            // 属性设值
            for (int i = 1; i < rows; i++) {
                Cell[] row = sheet.getRow(i);
                String deviceName = StringUtils.trim(row[1].getContents());
                QidongDevice device = deviceNameMap.get(deviceName);

                String attrCNName = StringUtils.trim(row[2].getContents());
                String attrENName = StringUtils.trim(row[3].getContents());

                device.getDeviceAttributeList().add(QidongDevice.DeviceAttribute.builder().attrCNName(attrCNName).attrENName(attrENName).build());
            }

            return Lists.newArrayList(deviceNameMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(workbook)) {
                workbook.close();
            }
        }
        return Lists.newArrayList();
    }
}
