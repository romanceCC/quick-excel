package org.wcl.quick.excel.enums

import org.apache.poi.ss.usermodel.CellType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

/**
 * 类类型和cellType关系关联
 */
enum class ExcelClassTypeEnum(val classType: Class<*>, val method: String, val cellType: CellType) {

    INT_TYPE(Int::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    INT_WRAP_TYPE(Int::class.javaObjectType, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    LONG_TYPE(Long::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    LONG_WRAP_TYPE(Long::class.javaObjectType, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    DOUBLE_TYPE(Double::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    DOUBLE_WRAP_TYPE(Double::class.javaObjectType, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    BOOLEAN_TYPE(Boolean::class.java, CellMethodEnum.BOOLEAN_CELL_VALUE.method, CellType.BOOLEAN),

    BOOLEAN_WRAP_TYPE(Boolean::class.javaObjectType, CellMethodEnum.BOOLEAN_CELL_VALUE.method, CellType.BOOLEAN),

    BYTE_TYPE(Byte::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    BYTE_WRAP_TYPE(Byte::class.javaObjectType, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    SHORT_TYPE(Short::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    SHORT_WRAP_TYPE(Short::class.javaObjectType, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    FLOAT_TYPE(Float::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    FLOAT_WRAP_TYPE(Float::class.javaObjectType, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    CHAR_TYPE(Char::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    CHAR_WRAP_TYPE(Char::class.javaObjectType, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    STRING_TYPE(String::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    BIGDECIMAL_TYPE(BigDecimal::class.java, CellMethodEnum.STRING_CELL_VALUE.method, CellType.STRING),

    DATE_TYPE(Date::class.java, CellMethodEnum.NUMERIC_CELL_VALUE.method, CellType.NUMERIC),

    LOCALDATETIME_TYPE(LocalDateTime::class.java, CellMethodEnum.NUMERIC_CELL_VALUE.method, CellType.NUMERIC),

    LOCALDATE_TYPE(LocalDate::class.java, CellMethodEnum.NUMERIC_CELL_VALUE.method, CellType.NUMERIC),

    LOCALTIME_TYPE(LocalTime::class.java, CellMethodEnum.NUMERIC_CELL_VALUE.method, CellType.NUMERIC),
    ;
}
