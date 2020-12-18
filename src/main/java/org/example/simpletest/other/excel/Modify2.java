package org.example.simpletest.other.excel;

import com.sun.corba.se.spi.ior.Writeable;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 修改点位数据
 *
 * @author zhuzhenjie
 * @since 2020/12/18
 */
public class Modify2 {

    public static void main(String[] args) {

        List<QidongDevice> devices = GetQidongDeviceFromExcel.devices();

        devices.forEach(System.out::println);

        Map<String, QidongDevice> nameDevice = devices.stream().collect(Collectors.toMap(QidongDevice::getDeviceName, Function.identity(), (a, b) -> a));

        try {
            File file = new File("C:\\Users\\winterfell\\Desktop\\镇污\\启东南阳点位表2.xls");
            Workbook workbook = Workbook.getWorkbook(file);
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file, workbook);

            Sheet sheet = workbook.getSheet(0);
            WritableSheet writableSheet = writableWorkbook.getSheet(0);

            Cell[] columnCellls = sheet.getColumn(1);

            for (int i = 1; i <= 500; i++) {

                Cell nameCell = sheet.getCell(2, i);
                String deviceNameFromExcel = StringUtils.trim(nameCell.getContents());

                Cell attrEnNameCell = sheet.getCell(4, i);
                String deviceAttrEnName = StringUtils.trim(attrEnNameCell.getContents());

                QidongDevice qidongDevice = nameDevice.get(deviceNameFromExcel);
                String deviceId = qidongDevice.getDeviceId();

                String newTranslate = deviceId + "&" + deviceAttrEnName;

                CellFormat oldCellFormat = columnCellls[i].getCellFormat();
                Label label = new Label(1, i, newTranslate, oldCellFormat);

                writableSheet.addCell(label);
            }

            System.out.println(columnCellls.length);

            writableWorkbook.write();
            writableWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
