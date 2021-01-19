package org.example.simpletest.other.rudong;

import com.google.common.collect.Lists;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhuzhenjie
 * @since 2021/1/18
 */
public class DeviceParser {


    static List<String> xlsNames = Lists.newArrayList(
            "如东_栟茶", "如东_丰利",
            "如东_河口", "如东_洋口", "如东_袁庄");

    public static void main(String[] args) {
//        xlsNames.stream().map(DeviceParser::onePlantDevice).forEach(DeviceParser::writeToNew);
        xlsNames.forEach(xlsName -> {
            writeToNew(onePlantDevice(xlsName), xlsName);
        });
    }

    @Builder
    @Data
    static class DeviceTmp {
        private String group;
        private String name;
    }

    static void writeToNew(Map<String, List<DeviceTmp>> deviceInfos, String plantName) {
        if (deviceInfos == null || deviceInfos.size() <= 0) {
            return;
        }
        String tmpPath = "D:/tmp/project_rudong/" + plantName + "_modify.xls";

        try {
            File file = new File(tmpPath);
            file.createNewFile();
            WritableWorkbook wb = Workbook.createWorkbook(file);
            // 创建sheet
            WritableSheet sheet = wb.createSheet("sheet", 0);

            int groupCol = 0;
            int nameCol = 1;
            int monitorItemCnCol = 2;

            Label group = new Label(groupCol, 0, "设备名称");
            Label monitorItemAll = new Label(nameCol, 0, "监测项全名");
            Label monitorItem = new Label(monitorItemCnCol, 0, "监测项中文");

            sheet.addCell(group);
            sheet.addCell(monitorItemAll);
            sheet.addCell(monitorItem);


            AtomicInteger begin = new AtomicInteger(1);
            deviceInfos.values().stream().flatMap(Collection::stream).forEach(
                    deviceTmp -> {
                        String deviceGroup = deviceTmp.getGroup();
                        String deviceName = deviceTmp.getName();
                        int row = begin.getAndIncrement();
                        Label tmpGroup = new Label(groupCol, row, deviceGroup);
                        Label tmpName = new Label(nameCol, row, deviceName);
                        try {
                            sheet.addCell(tmpGroup);
                            sheet.addCell(tmpName);

                            // 监测项的配置
                            String monitorItemCn = deviceName.contains(deviceGroup) ? deviceName.replace(deviceGroup, StringUtils.EMPTY) : StringUtils.EMPTY;
                            sheet.addCell(new Label(monitorItemCnCol, row, monitorItemCn));
                        } catch (WriteException e) {
                            e.printStackTrace();
                        }
                    }
            );

            wb.write();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static Map<String, List<DeviceTmp>> onePlantDevice(String xlsName) {
        try {
            String classpath = "project_rudong/xls/" + xlsName + ".xls";
            Workbook workbook = Workbook.getWorkbook(new ClassPathResource(classpath).getInputStream());
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            List<DeviceTmp> all = Lists.newArrayList();
            // 第一行是表头
            for (int i = 1; i < rows; i++) {
                Cell[] row = sheet.getRow(i);
                // 第一列是name 第二列是group
                all.add(DeviceTmp.builder()
                        .group(row[1].getContents().trim())
                        .name(row[0].getContents().trim())
                        .build());
            }
            return all.stream().collect(Collectors.groupingBy(DeviceTmp::getGroup));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
