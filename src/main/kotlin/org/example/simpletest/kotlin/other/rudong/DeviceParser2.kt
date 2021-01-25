package org.example.simpletest.kotlin.other.rudong

import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableWorkbook
import org.apache.commons.lang3.StringUtils
import org.example.simpletest.kotlin.getLogger
import org.example.simpletest.other.id.IDCreator
import java.io.File

/**
 * @author zhuzhenjie
 * @since 2021/1/20
 */

private val log = getLogger("device-parser-2")

private val xlsNames = listOf(
    "如东_栟茶", "如东_丰利",
    "如东_河口", "如东_洋口", "如东_袁庄"
)

fun main() {
    xlsNames.forEach(::addDeviceId)
}

fun addDeviceId(xlsName: String) {
    val xlsPath = "D:/tmp/project_rudong/color/${xlsName}_basic.xls"
    try {
        val wb: Workbook = Workbook.getWorkbook(File(xlsPath))
        val wwb: WritableWorkbook = Workbook.createWorkbook(File(xlsPath), wb)
        val sheet = wwb.getSheet(0)
        val deviceNames: List<String> = IntRange(1, sheet.rows - 1)
            .map { sheet.getCell(0, it).contents.trim() }
            .distinct()
        val nameToIdMap: MutableMap<String, String> = mutableMapOf()

        deviceNames.forEach {
            nameToIdMap[it] = IDCreator.createId(
                when (xlsName) {
                    "如东_栟茶" -> "rd-bs-"
                    "如东_丰利" -> "rd-fl-"
                    "如东_河口" -> "rd-hk-"
                    "如东_洋口" -> "rd-yk-"
                    "如东_袁庄" -> "rd-yz-"
                    else -> ""
                }
            )
        }
        IntRange(1, sheet.rows - 1).forEach {
            val deviceName = sheet.getCell(0, it).contents.trim()
            val deviceId = nameToIdMap[deviceName]
            log.info("deviceName: $deviceName deviceId: $deviceId")
            val deviceIdCol = 4
            sheet.addCell(Label(deviceIdCol, it, deviceId))
        }

        wwb.write()
        wwb.close()

    } catch (e: Exception) {
        log.error(StringUtils.EMPTY, e)
    }
}