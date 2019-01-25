package com.github.romancecc.quick.excel.enums


/**
 * Excel 版本与对象
 */
enum class ExcelVersionEnum constructor(val suffix: String, val classType: String) {
    EXCEL_XLSX("xlsx", "org.apache.poi.xssf.usermodel.XSSFWorkbook"),
    EXCEL_XLS("xls", "org.apache.poi.hssf.usermodel.HSSFWorkbook");

    companion object {
        private val CACHE = HashMap<String, ExcelVersionEnum>()
        fun suffixOf(suffix: String): ExcelVersionEnum {
            return CACHE[suffix]?:throw RuntimeException(ExcelExceptionEnum.FILE_TYPE_ERROR.message)
        }

        init {
            ExcelVersionEnum.values().forEach {
                CACHE[it.suffix] = it
            }
        }
    }

}
