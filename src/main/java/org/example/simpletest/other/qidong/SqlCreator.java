package org.example.simpletest.other.qidong;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.simpletest.other.excel.GetQidongDeviceFromExcel;
import org.example.simpletest.other.excel.QidongDevice;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于mqtt的信息生成sql
 *
 * @author zhuzhenjie
 * @since 2020/12/14
 */
public class SqlCreator {

    static List<QidongDevice> DEVICES;
    static List<String> deviceNameSort;

    static String sqlInsert = "insert into fa_sewage_plant_device (id,app_id,device_id,device_name,monitor_items,publish_topic,subscribe_topic) values\r\n";
    static String sqlFormat = "(primary_id,1335095064002912258,'device_id','device_name','monitor_items','publish_topic','subscribe_topic')";

    static {
        DEVICES = GetQidongDeviceFromExcel.devices();
        try {
            List<String> lines = IOUtils.readLines(new BufferedReader(new InputStreamReader(new ClassPathResource("qd_ny.txt").getInputStream())));
            deviceNameSort = lines.stream().map(line -> StringUtils.split(line, "__")).map(split -> split[0]).distinct().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Map<String, QidongDevice> nameDeviceMap = DEVICES.stream().collect(Collectors.toMap(QidongDevice::getDeviceName, Function.identity(), (a, b) -> a));

        String append = deviceNameSort.stream().map(nameDeviceMap::get)
                .map(device -> {
                    String deviceId = device.getDeviceId();
                    String deviceName = device.getDeviceName();
                    List<QidongDevice.DeviceAttribute> deviceAttributeList = device.getDeviceAttributeList();
                    JSONArray jsonArray = new JSONArray();
                    deviceAttributeList.stream().map(deviceAttribute -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("fbox", deviceAttribute.getFboxAttrName());
                        jsonObject.put("en", deviceAttribute.getAttrENName());
                        jsonObject.put("cn", deviceAttribute.getAttrCNName());
                        return jsonObject;
                    }).forEach(jsonArray::add);
                    String monitorAddr = jsonArray.toJSONString();

                    String publishTopic = device.getMqttTopic();
                    String subscribeTopic = device.getMqttTopic() + "/" + "subscribe";
                    return sqlFormat
                            .replace("primary_id", String.valueOf(IdWorker.getId()))
                            .replace("device_id", deviceId)
                            .replace("device_name", deviceName)
                            .replace("monitor_items", monitorAddr)
                            .replace("publish_topic", publishTopic)
                            .replace("subscribe_topic", subscribeTopic);
                }).collect(Collectors.joining(",\r\n"));

        System.out.println(sqlInsert + append);

    }

}
