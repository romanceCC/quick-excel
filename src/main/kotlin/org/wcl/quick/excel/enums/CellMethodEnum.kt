package org.wcl.quick.excel.enums

/**
 * cell取值方法枚举类
 */
enum class CellMethodEnum(val method: String) {
    BOOLEAN_CELL_VALUE("getBooleanCellValue"),
    DATE_CELL_VALUE("getDateCellValue"),
    ERROR_CELL_VALUE("getErrorCellValue"),
    NUMERIC_CELL_VALUE("getNumericCellValue"),
    RICH_CELL_VALUE("getRichStringCellValue"),
    STRING_CELL_VALUE("getStringCellValue");
}