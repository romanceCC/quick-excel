package com.github.romancecc.quick.excel

/**
 * 辅助处理Excel解析
 * 如果字段不使用此注解将不会解析单元格数据到该字段
 */
@Target(AnnotationTarget.FIELD)
@Retention
annotation class ExcelAnnotation(
        /**
         * 标注该属性的顺序
         */
        val rank: Int,
        /**
         * Excel文件为日期格式的字符串,使用此注解
         * 先按照日期格式解析,如果出现IllegalStateException转换异常则解析为数字格式日期
         * 注 : 如确定时间格式必定是数字格式,可不必使用此注解,避免不必要的开销
         */
        val pattern: String = ""
)
