package org.example.simpletest.kotlin.other.hsst.stationsql

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * 测试展示异常数据生成
 *
 * @author zhuzhenjie
 * @since 2021/4/15
 */

private val sqlDateFormatter = SimpleDateFormat("yyyy-MM-dd HH:37:06")

private val idCreatorFormat = SimpleDateFormat("yyyyMMdd")

private val common = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

private enum class StationType {
    AIR,
    WATER
}

fun main() {

    val start = "2021-04-15 00:00:00"
    val end = "2021-04-15 15:00:00"

    printSql(getDate(start), getDate(end), StationType.AIR)

    println("-- ################################################################################")

    printSql(getDate(start), getDate(end), StationType.WATER)
}


/**
 * Print
 *
 * @param startTime
 * @param endTime
 * @param hourInterval
 */
private fun printSql(startTime: Date, endTime: Date, stationType: StationType, hourInterval: Int = 1) {
    if (hourInterval < 1) {
        return
    }
    var index = 0

    var pStartTime = startTime

    while (pStartTime < endTime) {
        if (DateUtils.addHours(pStartTime, hourInterval) >= endTime) {
            // 说明是最后一个
            printFinalUnNormal(pStartTime, index + 1, stationType)
            break
        }
        printNormal(pStartTime, index + 1, stationType)
        println()
        index++
        pStartTime = DateUtils.addHours(pStartTime, hourInterval)
    }
}

/**
 * 打印正常数据
 *
 * @param date
 * @param index
 */
private fun printNormal(date: Date, index: Int, stationType: StationType) {
    val dateStr: String = sqlDateFormatter.format(date)
    val idStr = createId(date, index)

    if (stationType == StationType.AIR) {
        println(
            """
                |-- 空气监测数据 正常 $dateStr  $index
                |INSERT INTO "HSSTDEV"."AM_ST_R"("STCD", "SPT", "HUMIDITY", "TEMPERATURE", "SPEED", "DIRECTION", "CO2", "SO2", "PM2_5", "PM10", "OXYGEN", "NO", "CO", "O3", "PRESSURE") VALUES
                |('10001314', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), '26.287', '19.203', '3.6', '109.44', '434', '2.43', '12.905', '11.389', NULL, NULL, NULL, NULL, NULL);
                |
                |INSERT INTO "HSSTDEV"."AIR_WARNING"("ID", "STCD", "SPT", "AQ_LEVEL", "AQI", "DEFINE_FACTORS", "AIR_WARNING_LEVEL", "STATUS", "END_DATE", "DISTRIBUTE_ID", "DEAL_ID", "AIR_STATUS")
                |VALUES ('$idStr', '10001314', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), '一级', '18', 'pm2.5', '0', '0', NULL, NULL, NULL, '优级');
            """.trimIndent().trimMargin()
        )
    } else if (stationType == StationType.WATER) {
        println(
            """
                |-- 水质监测数据 正常 $dateStr  $index
                |INSERT INTO "HSSTDEV"."WM_ST_R"
                |("STCD", "SPT", "WL", "WT", "PH", "COND", "REDOX", "TURB", "DOX", "NH3N", "COD") 
                |VALUES 
                |('10001896', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), NULL, '15.70', '7.26', NULL, NULL, NULL, '5.88', '0.67', NULL);
                |
                |INSERT INTO "HSSTDEV"."WATER_WARNING"
                |("ID", "STCD", "SPT", "WATER_TYPE", "DEFINE_FACTORS", "WATER_WARNING_LEVEL", "STATUS", "END_DATE", "DISTRIBUTE_ID", "DEAL_ID") 
                |VALUES ('$idStr', '10001896', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), 'Ⅲ类', 'dox,nh3n', '0', '0', NULL, NULL, NULL);
            """.trimIndent().trimMargin()
        )
    } else {
        println("-- other type")
    }
}

/**
 * 打印最后的数据 超标数据
 *
 * @param date
 * @param index
 */
private fun printFinalUnNormal(date: Date, index: Int, stationType: StationType) {
    val dateStr = sqlDateFormatter.format(date)
    val idStr = createId(date, index)
    val dispatchId = createDispatchId(date)
    val exceptedDateStr = sqlDateFormatter.format(DateUtils.addDays(date, index))

    if (StationType.AIR == stationType) {
        println(
            """
                |-- 空气监测数据 告警 $dateStr $index
                |INSERT INTO "HSSTDEV"."AM_ST_R"("STCD", "SPT", "HUMIDITY", "TEMPERATURE", "SPEED", "DIRECTION", "CO2", "SO2", "PM2_5", "PM10"
                |, "OXYGEN", "NO", "CO", "O3", "PRESSURE") 
                |VALUES ('10001314', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), '24.67', '14.2', '0', '0', '431.572', '1.061', '81.9', '167.13'
                |, NULL, NULL, NULL, NULL, NULL);
                |
                |INSERT INTO "HSSTDEV"."AIR_WARNING"("ID", "STCD", "SPT", "AQ_LEVEL", "AQI", "DEFINE_FACTORS", "AIR_WARNING_LEVEL", "STATUS", "END_DATE", "DISTRIBUTE_ID", "DEAL_ID", "AIR_STATUS")
                | VALUES ('$idStr', '10001314', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), '三级', '109', 'pm2.5,pm10', '1', '0', NULL, NULL, NULL, '轻度污染');
                | 
                |
                |INSERT INTO "HSSTDEV"."AIR_WARNING_DISPATCH"
                |("ID",
                | "STCD", 
                | "START_SPT",
                | "END_SPT",
                | "AIR_TYPE", 
                | "AQI", 
                | "DEFINE_FACTORS",
                | "AIR_WARNING_LEVEL",
                | "DISPATCH_SIGN",
                | "STATUS",
                | "OPERATION_DEPARTMENT_ID",
                | "AUTO_DISPATCH",
                | "EXPECTED_TIME")
                | VALUES
                |('$dispatchId', 
                |'10001314',
                | TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'),
                | TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'),
                | '三级',
                | '109', 
                | 'pm2.5,pm10',
                | '2', 
                | '1',
                | 'UP',
                | '3',
                | '1', 
                | TO_DATE ( '$exceptedDateStr', 'SYYYY-MM-DD HH24:MI:SS' )
                | );
            """.trimIndent().trimMargin()
        )
    } else if (StationType.WATER == stationType) {
        println(
            """
                |-- 水质监测数据 告警 $dateStr  $index
                |INSERT INTO "HSSTDEV"."WM_ST_R"
                |("STCD", "SPT", "WL", "WT", "PH", "COND", "REDOX", "TURB", "DOX", "NH3N", "COD") 
                |VALUES 
                |('10001896', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), NULL, '15.60', '7.39', NULL, NULL, NULL, '1.92', '1.6', NULL);
                |
                |
                |INSERT INTO "HSSTDEV"."WATER_WARNING"
                |("ID", "STCD", "SPT", "WATER_TYPE", "DEFINE_FACTORS", "WATER_WARNING_LEVEL", "STATUS", "END_DATE", "DISTRIBUTE_ID", "DEAL_ID") 
                |VALUES ('$idStr', '10001896', TO_DATE('$dateStr', 'SYYYY-MM-DD HH24:MI:SS'), '劣Ⅴ类', 'dox,nh3n', '1', '0', NULL, NULL, NULL);
                |
                |
                |INSERT INTO "HSSTDEV"."WATER_WARNING_DISPATCH" (
                |"ID",
                |"STCD",
                |"START_SPT",
                |"END_SPT",
                |"WATER_TYPE",
                |"DEFINE_FACTORS",
                |"WATER_WARNING_LEVEL",
                |"DISPATCH_SIGN",
                |"STATUS",
                |"OPERATION_DEPARTMENT_ID",
                |"AUTO_DISPATCH",
                |"EXPECTED_TIME"
                |)
                |VALUES
                |(
                |'$dispatchId',
                |'10001896',
                |TO_DATE ( '$dateStr', 'SYYYY-MM-DD HH24:MI:SS' ),
                |TO_DATE ( '$dateStr', 'SYYYY-MM-DD HH24:MI:SS' ),
                |'劣Ⅴ类',
                |'dox,nh3n',
                |'2',
                |'1',
                |'UP',
                |'3',
                |'1',
                |TO_DATE ( '$exceptedDateStr', 'SYYYY-MM-DD HH24:MI:SS' )
                |);
            """.trimIndent().trimMargin()
        )
    } else {
        println("-- other type")
    }

}

/**
 * 生成ID
 *
 * @param date
 * @param index
 * @return 生成的ID
 */
private fun createId(date: Date, index: Int): String {
    val idxStr = if (index < 10) "0$index" else index.toString()
    return "1${idCreatorFormat.format(date)}$idxStr"
}


/**
 * Create dispatch id
 *
 * @param date
 * @return dispatch id
 */
private fun createDispatchId(date: Date): String = "1${idCreatorFormat.format(date)}"

/**
 * Get date
 *
 * @param dateStr
 * @return
 */
private fun getDate(dateStr: String): Date {
    if (StringUtils.isBlank(dateStr)) {
        return Date()
    }
    return try {
        common.parse(dateStr)
    } catch (e: Exception) {
        e.printStackTrace()
        Date()
    }
}
