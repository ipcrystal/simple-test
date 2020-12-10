package org.example.simpletest.other.id;

import java.util.concurrent.TimeUnit;

/**
 * 单机基于时间戳的ID生成器
 *
 * @author zhuzhenjie
 * @since 2020/12/10
 */
public class IDCreator {

    private IDCreator() {
    }

    /**
     * ID生成
     *
     * @param basicInfo
     * @return
     */
    public static String createID(BasicInfo basicInfo) {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long tm = System.currentTimeMillis();
        String basicAppName = basicInfo.getBasicAppName();
        return basicAppName + "-" + createID(tm);
    }

    /**
     * ID生成
     *
     * @param tm
     * @return
     */
    private static String createID(long tm) {
        return string10ToN(tm, 26);
    }

    /**
     * 进制特殊转换
     *
     * @param tenRadix
     * @param radix
     * @return
     */
    private static String string10ToN(long tenRadix, int radix) {
        if (radix <= 2 || radix > 26) {
            return "";
        }
        String code = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder buf = new StringBuilder();
        int remainder = 0;
        while (tenRadix != 0) {
            // 求余数
            remainder = (int) (tenRadix % radix);
            // 除以基数
            tenRadix = tenRadix / radix;
            // 保存余数，记得要倒叙排列
            buf.append(code.charAt(remainder));
        }
        // 倒叙排列
        buf.reverse();
        return buf.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(createID(BasicInfo.builder().basicAppName("qd").build()));
        }
    }
}
