package org.example.simpletest.kotlin.other.rudong

import jxl.Workbook
import org.apache.commons.lang3.StringUtils
import org.example.simpletest.kotlin.getLogger
import java.io.File

/**
 * @author zhuzhenjie
 * @since 2021/1/20
 */

private val log = getLogger("gen-sql")

private val xlsNames = listOf(
    "如东_栟茶", "如东_丰利",
    "如东_河口", "如东_洋口", "如东_袁庄"
)

private const val appId = 1347007805571477505L

fun main() {

    xlsNames.forEach {
        val path = "D:/tmp/project_rudong/color/${it}_basic.xls"
        try {
            val wb = Workbook.getWorkbook(File(path))
            val sheet = wb.getSheet(0)
            val rowsNum = sheet.rows

            val deviceIdSet = IntRange(1, rowsNum - 1)
                .map { sheet.getCell(4, it).contents.trim() }
                .distinct().toSet()

            val idDeviceMap = mutableMapOf<String, Device>()

            deviceIdSet.forEach { idDeviceMap[it] = Device(StringUtils.EMPTY, StringUtils.EMPTY) }

            // 设备赋值
            IntRange(1, rowsNum - 1).forEach {
                val deviceId = sheet.getCell(4, it).contents.trim()
                val device = idDeviceMap[deviceId]
                if (device != null) {
                    device.id = deviceId
                    val deviceName = sheet.getCell(0, it).contents.trim()
                    device.name = deviceName
                    val fbox = sheet.getCell(1, it).contents.trim()
                    val cn = sheet.getCell(2, it).contents.trim()
                    val en = sheet.getCell(3, it).contents.trim()
                    device.monitorItems.add(MonitorItem(en, cn, fbox))
                }
            }
            println(sqlTemplate(it, idDeviceMap.values.toList()))
        } catch (e: Exception) {
            log.error(StringUtils.EMPTY, e)
        }
    }


}

private fun sqlTemplate(xlsName: String, deviceList: List<Device>): String {

    val plantId: Long = when (xlsName) {
        "如东_栟茶" -> 1346725453569720339
        "如东_丰利" -> 1346737157041029121
        "如东_河口" -> 1346737157045223426
        // 外农？
        "如东_洋口" -> 1346737157049417729
        "如东_袁庄" -> 1346725453569720338
        else -> 0L
    }

    val insert =
        "insert into fa_sewage_plant_device (id,device_id,device_name,monitor_items,mqtt_info,kafka_info,app_id,plant_id) values"
    val appendFun = fun(id: String, name: String, monitorItems: List<MonitorItem>): String {
        val monitorItemJson = "'${
            monitorItems.map { "{\"en\",\"${it.en}\",\"cn\":\"${it.cn}\",\"fbox\":\"${it.fbox}\",\"threshold\":\"{}\"}" }
                .joinToString(
                    separator = ",",
                    prefix = "[",
                    postfix = "]"
                )
        }'"
        return "('${id}','${name}',${monitorItemJson},'{}','{}',$appId,$plantId"
    }
    val append = deviceList.map { appendFun(it.id, it.name, it.monitorItems) }
        .joinToString(separator = ",\r\n")
    return """
         |$insert
         |$append
    """.trimIndent().trimMargin()
}


private data class Device(
    var id: String,
    var name: String,
    var monitorItems: MutableList<MonitorItem> = mutableListOf()
)

private data class MonitorItem(
    var en: String,
    var cn: String,
    var fbox: String,
)
