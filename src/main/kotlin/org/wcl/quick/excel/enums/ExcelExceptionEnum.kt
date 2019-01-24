package org.wcl.quick.excel.enums

/**
 * ExcelUtil异常枚举
 */
enum class ExcelExceptionEnum constructor(val code: String, val message: String) {
    FILE_TYPE_ERROR("10", "文件格式不正确,请上传正确的文件格式(xls,xlsx)"),
    FILE_CONTENT_NULL_ERROR("12", "文件内容不能为空"),
    CLASS_ERROR("20", "解析Excel找不到实体类对应的类类型,请检查"),
    CLASS_CONVERT_ERROR("21", "Excel数据类型转换时找不到类型,请检查"),
    ;
}
