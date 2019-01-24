package org.wcl.quick.excel


import org.apache.poi.ss.usermodel.DateUtil
import org.omg.CORBA.SystemException
import org.wcl.quick.excel.enums.ExcelClassTypeEnum
import org.wcl.quick.excel.enums.ExcelExceptionEnum
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 类型转换器
 */
object ExcelClassConvert {
    /**
     * pattern强制转换方法类别
     */
    fun patternClassTypeConvert(field: Field, cellValue: String): Any {
        return patternResult(field.getAnnotation(ExcelAnnotation::class.java).pattern, cellValue, field.type)
    }

    /** 注解属性pattern的参数*/
    private val patternResult: (pattern: String, cellValue: String, classType: Class<*>) -> Any = { pattern: String, cellValue: String, classType: Class<*> ->
        when (classType) {
            ExcelClassTypeEnum.LOCALDATETIME_TYPE.classType -> LocalDateTime.parse(cellValue, DateTimeFormatter.ofPattern(pattern))
            ExcelClassTypeEnum.LOCALDATE_TYPE.classType -> LocalDate.parse(cellValue, DateTimeFormatter.ofPattern(pattern))
            ExcelClassTypeEnum.LOCALTIME_TYPE.classType -> LocalTime.parse(cellValue, DateTimeFormatter.ofPattern(pattern))
            ExcelClassTypeEnum.DATE_TYPE.classType -> SimpleDateFormat(pattern).parse(cellValue)
            else
            -> throw RuntimeException("${ExcelExceptionEnum.CLASS_CONVERT_ERROR.message} :$classType")
        }
    }

    /**
     * Common强制转换方法类别
     */
    fun commonClassTypeConvert(field: Field,cellValue: Any): Any {
        return commonResult(cellValue, field.type)
    }

    /** 未添加特殊注解的参数*/
    private val commonResult: (cellValue: Any,classType: Class<*>) -> Any = {cellValue: Any,classType: Class<*> ->
        when (classType) {
            /*************** 基本类型 **************/
            ExcelClassTypeEnum.INT_TYPE.classType -> cellValue.toString().toInt()
            ExcelClassTypeEnum.INT_WRAP_TYPE.classType -> cellValue.toString().toInt()
            ExcelClassTypeEnum.LONG_TYPE.classType -> cellValue.toString().toLong()
            ExcelClassTypeEnum.LONG_WRAP_TYPE.classType -> cellValue.toString().toLong()
            ExcelClassTypeEnum.DOUBLE_TYPE.classType -> cellValue.toString().toDouble()
            ExcelClassTypeEnum.DOUBLE_WRAP_TYPE.classType -> cellValue.toString().toDouble()
            ExcelClassTypeEnum.BOOLEAN_TYPE.classType -> cellValue.toString().toBoolean()
            ExcelClassTypeEnum.BOOLEAN_WRAP_TYPE.classType -> cellValue.toString().toBoolean()
            ExcelClassTypeEnum.FLOAT_TYPE.classType -> cellValue.toString().toFloat()
            ExcelClassTypeEnum.FLOAT_WRAP_TYPE.classType -> cellValue.toString().toFloat()
            ExcelClassTypeEnum.BYTE_TYPE.classType -> cellValue.toString().toByte()
            ExcelClassTypeEnum.BYTE_WRAP_TYPE.classType -> cellValue.toString().toByte()
            ExcelClassTypeEnum.SHORT_TYPE.classType -> cellValue.toString().toShort()
            ExcelClassTypeEnum.SHORT_WRAP_TYPE.classType -> cellValue.toString().toShort()
            ExcelClassTypeEnum.CHAR_TYPE.classType -> cellValue.toString().toCharArray()
            ExcelClassTypeEnum.CHAR_WRAP_TYPE.classType -> cellValue.toString().toCharArray()

            /*************** 复杂类型 **************/
            /** String*/
            ExcelClassTypeEnum.STRING_TYPE.classType -> cellValue.toString()
            /** BigDecimal*/
            ExcelClassTypeEnum.BIGDECIMAL_TYPE.classType -> cellValue.toString().toBigDecimal()
            /** LocalDateTime*/
            ExcelClassTypeEnum.LOCALDATETIME_TYPE.classType ->
                LocalDateTime.ofInstant(
                        DateUtil.getJavaDate(cellValue.toString().toDouble()).toInstant(), ZoneId.systemDefault())

            /** LocalDate*/
            ExcelClassTypeEnum.LOCALDATE_TYPE.classType ->
                LocalDateTime.ofInstant(
                        DateUtil.getJavaDate(cellValue.toString().toDouble()).toInstant(), ZoneId.systemDefault()).toLocalDate()

            /** LocalTime*/
            ExcelClassTypeEnum.LOCALTIME_TYPE.classType ->
                LocalDateTime.ofInstant(
                        DateUtil.getJavaDate(cellValue.toString().toDouble()).toInstant(), ZoneId.systemDefault()).toLocalTime()

            /** Date*/
            ExcelClassTypeEnum.DATE_TYPE.classType ->
                Date.from(DateUtil.getJavaDate(cellValue.toString().toDouble()).toInstant())

            else
            -> throw RuntimeException("${ExcelExceptionEnum.CLASS_CONVERT_ERROR.message} :$classType")
        }
    }
}