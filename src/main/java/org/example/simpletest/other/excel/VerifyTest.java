package org.example.simpletest.other.excel;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 验证点位正确性
 *
 * @author zhuzhenjie
 * @since 2020/12/18
 */
public class VerifyTest {

    public static void main(String[] args) {
        Set<String> oldNames = getNameFromOld();
        Set<String> newNames = getNameFromNew();

        assert newNames != null;
        assert oldNames != null;

        oldNames.forEach(newNames::remove);

        System.out.println(newNames.size() == 0);

        newNames.forEach(System.out::println);
    }

    static Set<String> getNameFromOld() {
        try {
            Workbook workbook = Workbook.getWorkbook(new File("C:\\Users\\winterfell\\Desktop\\镇污\\启东南阳点位表2.xls"));
            Sheet sheet = workbook.getSheet(0);
            Cell[] columns = sheet.getColumn(0);
            Set<String> oldNames = new HashSet<>();
            for (int i = 1; i <= 500; i++) {
                oldNames.add(StringUtils.trim(columns[i].getContents()));
            }
            return oldNames;
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Set<String> getNameFromNew() {
        try {
            Workbook workbook = Workbook.getWorkbook(new File("C:\\Users\\winterfell\\Desktop\\镇污\\启动南阳监控点.xls"));
            Sheet sheet = workbook.getSheet(0);
            Cell[] columns = sheet.getColumn(0);
            Set<String> oldNames = new HashSet<>();
            for (int i = 1; i <= 500; i++) {
                oldNames.add(StringUtils.trim(columns[i].getContents()));
            }
            return oldNames;
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return null;
    }

}
