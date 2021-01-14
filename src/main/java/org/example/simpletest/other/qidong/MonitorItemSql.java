package org.example.simpletest.other.qidong;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author zhuzhenjie
 * @since 2021/1/13
 */
public class MonitorItemSql {

    static String before = "insert into fa_monitor_item_basic_info (id,monitor_item_en,item_type,threshold,app_id) values";

    static String afterTemplate = "(#id,'#monitorItemEn',#itemType,'{}','#appId')";

    /**
     * 启东 怀远 如东
     */
    static List<String> appIdList = Lists.newArrayList("1329633558142885889", "1335095064002912258", "1347007805571477505");

    public static void main(String[] args) {
        try {
            Workbook workbook = Workbook.getWorkbook(new ClassPathResource("project_qidong/启东南阳翻译2_monitorItem.xls").getInputStream());
            Sheet sheet = workbook.getSheet(0);
            Cell[] colsEn = sheet.getColumn(1);
            Cell[] colsItemType = sheet.getColumn(2);
//            List<String> ens = Stream.of(cols).map(Cell::getContents).map(String::trim).collect(Collectors.toList());
            List<MonitorItemInfo> infos = Lists.newArrayList();
            for (int i = 0; i < colsEn.length; i++) {
                infos.add(MonitorItemInfo.builder().en(colsEn[i].getContents().trim()).itemType(Integer.valueOf(colsItemType[i].getContents().trim())).build());
            }

            AtomicReference<Integer> id = new AtomicReference<>(10000);
            appIdList.forEach(appId -> {
                System.out.println();

                Set<String> ens = Sets.newHashSet();
                String sql = before + "\r\n" + infos.stream()
                        .filter(info -> {
                            boolean flag = !ens.contains(info.getEn());
                            ens.add(info.getEn());
                            return flag;
                        })
                        .map(info -> afterTemplate
                                .replace("#id", id.getAndSet(id.get() + 1) + "")
                                .replace("#monitorItemEn", info.getEn())
                                .replace("#itemType", info.getItemType() + "")
                                .replace("#appId", appId)
                        ).collect(Collectors.joining(",\r\n"));

                System.out.print(sql);
                System.out.println(";");

                System.out.println();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Builder
    @Data
    static class MonitorItemInfo {
        private String en;
        private Integer itemType;
    }

}
