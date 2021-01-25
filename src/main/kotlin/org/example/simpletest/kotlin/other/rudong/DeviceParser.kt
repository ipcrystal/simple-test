@file:Suppress("NAME_SHADOWING")

package org.example.simpletest.kotlin.other.rudong

import jxl.Cell
import jxl.Sheet
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import org.apache.commons.lang3.StringUtils
import org.example.simpletest.kotlin.getLogger
import org.springframework.core.io.ClassPathResource
import java.io.File

/**
 * 如东设备解析
 *
 * @author zhuzhenjie
 * @since 2021/1/18
 */
private val log = getLogger("rudong-device-parses")


private val xlsNames = listOf(
    "如东_栟茶", "如东_丰利",
    "如东_河口", "如东_洋口", "如东_袁庄"
)

private const val tmpFolder = "D:/tmp/project_rudong/"


fun main() {
//    init()
//    xlsNames.forEach { createTransXls(createBasicXls(onePlantDevice(it), it), it) }

    xlsNames.forEach { translate(getTranslateMap(it), it) }
}


fun translate(trans: Map<String, String>, xlsName: String) {
    if (trans.isEmpty()) return
    val path = "${tmpFolder}${xlsName}_basic.xls"
    try {
        val file = File(path)
        val wb = Workbook.getWorkbook(file)
        val wwb = Workbook.createWorkbook(file, wb)

        val wSheet = wwb.getSheet(0)

        val cnColNum = 2
        val enColNum = 3
        IntRange(1, wSheet.rows - 1).forEach {
            val cell = wSheet.getCell(cnColNum, it)
            val cn = cell.contents.trim()

            val en = if (StringUtils.isNotBlank(cn) && trans.containsKey(cn)) trans[cn] else StringUtils.EMPTY

            if (StringUtils.isNotBlank(en)) {
                wSheet.addCell(Label(enColNum, it, en))
            }

        }
        wwb.write()
        wwb.close()
        wb.close()
    } catch (e: Exception) {
        log.error(StringUtils.EMPTY, e)
    }
}

fun getTranslateMap(xlsName: String): Map<String, String> {
    val classpath = "project_rudong/xls/translate/${xlsName}_translate.xls"
    val wb = Workbook.getWorkbook(ClassPathResource(classpath).inputStream)
    val st = wb.getSheet(0)
    val map: MutableMap<String, String> = mutableMapOf<String, String>()
    IntRange(0, st.rows - 1).forEach { map[st.getCell(0, it).contents.trim()] = st.getCell(1, it).contents.trim() }
    return map
}

/**
 *
 * 新建需要翻译的英文部分
 */
fun createTransXls(cnSet: Set<String>?, plantName: String) {
    if (null == cnSet || cnSet.isEmpty()) {
        return
    }
    val folder = File("${tmpFolder}translate")
    if (!folder.exists()) {
        folder.mkdirs()
    }
    val tmpPath = "${tmpFolder}translate/${plantName}_translate.xls"
    try {
        val file = File(tmpPath)
        file.createNewFile()
        val wb = Workbook.createWorkbook(file)
        val sheet = wb.createSheet("sheet1", 0)

        val cnCol = 0
        var rowBegin = 0
        cnSet.forEach { sheet.addCell(Label(cnCol, rowBegin++, it)) }

        wb.write()
        wb.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 *
 */
fun createBasicXls(deviceInfos: Map<String, List<DeviceTmp>>?, plantName: String): Set<String> {
    if (deviceInfos == null || deviceInfos.isEmpty()) {
        return emptySet()
    }
    val tmpPath = "${tmpFolder}${plantName}_basic.xls"
    try {
        val file = File(tmpPath)
        file.createNewFile()
        val wb: WritableWorkbook = Workbook.createWorkbook(file)
        // 创建sheet
        val sheet: WritableSheet = wb.createSheet("sheet1", 0)

        val groupCol = 0
        val nameCol = 1
        val monitorItemCnCol = 2

        // 设置title
        sheet.addCell(Label(groupCol, 0, "设备名称"))
        sheet.addCell(Label(nameCol, 0, "监测项全名"))
        sheet.addCell(Label(monitorItemCnCol, 0, "监测项中文名"))
        sheet.addCell(Label(3, 0, "监测项英文名"))

        var begin = 1

        val cnSet = mutableSetOf<String>()

        deviceInfos.forEach { (_, deviceTmpList) ->
            deviceTmpList.forEach {
                val deviceGroup = it.group
                val deviceName = it.name
                val insertRowNum = begin++
                try {
                    sheet.addCell(Label(groupCol, insertRowNum, deviceGroup))
                    sheet.addCell(Label(nameCol, insertRowNum, deviceName))

                    // 监测项目中文名
                    fun String.addInSet(): String {
                        cnSet.add(this)
                        return this
                    }

                    val monitorItemCn = if (deviceName.contains(deviceGroup) && deviceName.length > deviceGroup.length)
                        deviceName.replace(deviceGroup, StringUtils.EMPTY).addInSet() else StringUtils.EMPTY

                    sheet.addCell(Label(monitorItemCnCol, insertRowNum, monitorItemCn))
                } catch (e: Exception) {
                    log.error(StringUtils.EMPTY, e)
                }
            }
        }
        wb.write()
        wb.close()
        return cnSet.filter { StringUtils.isNotBlank(it) }.toHashSet()
    } catch (e: Exception) {
        e.printStackTrace()
        return emptySet()
    }
}


fun onePlantDevice(xlsName: String): MutableMap<String, MutableList<DeviceTmp>>? {
    try {
        val classPath = "project_rudong/xls/${xlsName}.xls"
        val wb: Workbook = Workbook.getWorkbook(ClassPathResource(classPath).inputStream)
        val sheet: Sheet = wb.getSheet(0)
        val rows = sheet.rows

        val all: MutableList<DeviceTmp> = mutableListOf()

        // 第一行是表头
        for (i in 1 until rows) {
            val rows: Array<Cell> = sheet.getRow(i)
            all.add(DeviceTmp(rows[1].contents.trim(), rows[0].contents.trim()))
        }
        // kotlin 写法
        return all.groupByTo(HashMap(), { it.group })
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun init() {
    val basicFolder = File(tmpFolder)
    if (!basicFolder.exists()) {
        basicFolder.mkdirs()
    }
}

data class DeviceTmp(val group: String, val name: String)
