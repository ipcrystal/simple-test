package org.example.simpletest.other.excel;

import com.google.common.collect.Maps;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhuzhenjie
 * @since 2020/12/11
 */
public class TranslateQidong {

    static void translateAndSet(Map<String, String> paramTranslateMap) {
        Workbook points = null;
        try {
            points = Workbook.getWorkbook(new File("D:/tmp/project_qidong/启东南阳点位表.xls"));

            // 创建可读写的 workbook
            WritableWorkbook writablePoints = Workbook.createWorkbook(new File("D:/tmp/project_qidong/启东南阳点位表.xls"), points);
            WritableSheet sheet = writablePoints.getSheet(0);

            int rows = sheet.getRows();
            for (int i = 1; i < rows; i++) {
                Cell[] pointRow = sheet.getRow(i);
                String fullName = StringUtils.trim(pointRow[0].getContents());
                System.out.println("开始处理 ---> " + fullName);
                String[] split = StringUtils.split(fullName, "__");

                String deviceName = split[0];
                // 设备名称设值
                int deviceNameColumn = 1;
                CellFormat deviceNameCellFormat = pointRow[deviceNameColumn].getCellFormat();
                Label deviceNameLabel = new Label(deviceNameColumn, i, deviceName, deviceNameCellFormat);
                sheet.addCell(deviceNameLabel);

                System.out.println("设备名称 = " + deviceName);

                // 设备属性中文名称
                int deviceParamCnNameColumn = 2;
                String deviceParamCnName = split[1];
                CellFormat deviceParamCnNameCellFormat = pointRow[deviceParamCnNameColumn].getCellFormat();
                Label deviceParamCnNameLabel = new Label(deviceParamCnNameColumn, i, deviceParamCnName, deviceParamCnNameCellFormat);
                sheet.addCell(deviceParamCnNameLabel);
                System.out.println("属性中文 = " + deviceParamCnName);

                // 设备属性翻译设值
                int deviceParamTranslateColumn = 3;
                String translateResult = paramTranslateMap.get(deviceParamCnName);
                CellFormat deviceParamTranslateCellFormat = pointRow[deviceParamTranslateColumn].getCellFormat();
                Label deviceParamTranslateLabel = new Label(deviceParamTranslateColumn, i, translateResult, deviceParamTranslateCellFormat);
                sheet.addCell(deviceParamTranslateLabel);
                System.out.println("属性翻译 = " + translateResult);

            }

            // 重要的两行
            writablePoints.write();
            writablePoints.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Map<String, String> paramTranslateMap() {
        Workbook translate = null;
        try {
            translate = Workbook.getWorkbook(new File("D:/tmp/project_qidong/启东南阳翻译.xls"));
            Sheet sheet = translate.getSheet(0);
            int rows = sheet.getRows();
            Map<String, String> paramTranslateMap = Maps.newHashMap();
            for (int i = 0; i < rows; i++) {
                Cell[] row = sheet.getRow(i);
                paramTranslateMap.put(StringUtils.trim(row[0].getContents()), StringUtils.trim(row[1].getContents()));
            }
            return paramTranslateMap;
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(translate)) {
                translate.close();
            }
        }
        return Maps.newHashMap();
    }

    public static void main(String[] args) {
//        paramTranslateMap().forEach((k, v) -> System.out.println(k + " = " + v));
//
//        translateAndSet(paramTranslateMap());

        try {
            Workbook workbook = Workbook.getWorkbook(new File("D:/tmp/project_qidong/启东南阳点位表.xls"));
            Sheet sheet = workbook.getSheet(0);
            Cell[] column = sheet.getColumn(4);

            System.out.println(column.length);

        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }


    }

}
