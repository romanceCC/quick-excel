package com.github.romancecc.quick.excel


import org.apache.commons.lang3.text.WordUtils
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.web.multipart.MultipartFile
import com.github.romancecc.quick.excel.enums.ExcelClassTypeEnum
import com.github.romancecc.quick.excel.enums.ExcelExceptionEnum
import com.github.romancecc.quick.excel.enums.ExcelVersionEnum
import java.io.InputStream
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import java.util.stream.Collectors


object QuickExcelUtil {
    private const val SET = "set"

    /**
     * 获取文件版本对应的对象
     *
     * @param stream  创建 Workbook 对象需要的 输入流
     * @param suffix 文件后缀
     * @return 返回对应版本的Workbook对象
     */
    fun getWorkbook(stream: InputStream, suffix: String): Workbook {
        return Class.forName(ExcelVersionEnum.suffixOf(suffix).classType)
                .getConstructor(InputStream::class.java).newInstance(stream) as Workbook
    }

    /**
     * 获取文件版本对应的对象
     * @param data 直接上传的MultipartFile类型文件
     * @return 返回对应版本的Workbook对象
     */
    fun getWorkbook(data: MultipartFile): Workbook {
        return getWorkbook(data.inputStream, getFileSuffix(data))
    }

    /**
     * 需要解析的类 必需 使用注解@ExcelAnnotation(rank = Int)指定实体类属性和Excel对应的顺序
     * 会过滤没有添加注解的变量(不解析)
     * @param data 需要解析的Workbook工作簿文件
     * @param target 解析目标类型
     * @param sheetIndex 解析第几页为对象(sheetIndex = 1: 第一页 >>> 默认第一页)
     * @param startRowsIndex 从第几行开始解析(startRowsIndex = 1: 第一行 >>> 默认第二行)
     * @param <T> target的类类型
     * @return 返回当前sheet解析为的对象集合
     */
    fun <T> resolve(data: Workbook, target: Class<T>, sheetIndex: Int = 1, startRowsIndex: Int = 2): List<T> {
        val result = ArrayList<T>()
        /** 获取需要解析的sheet */
        val sheet = data.getSheetAt(sheetIndex - 1)
        /** 获取sheet总行数 */
        val numberOfRows = sheet.physicalNumberOfRows
        /** 获取所有添加注解的属性,并按照注解值排序 */
        val fieldList = disposeAnnotationField(target.declaredFields)
        /** 循环总行数,一行解析为一个对象(下标为0开始,执行-1)*/
        for (j in (startRowsIndex - 1) until numberOfRows) {
            /** 解析目标对象创建*/
            val any = target.newInstance()
            /** 循环单元格,每个单元格解析为目标对象的一个属性*/
            for (i in fieldList.indices) {
                /** 获取单元格cell*/
                val cell = sheet.getRow(j).getCell(i)
                /** 获取当前的Field*/
                val field = fieldList[i]
                /** 单元格判空,空跳过该次循环*/
                try {
                    takeUnless { isEmpty(cell, field) } ?: continue
                } catch (e: IllegalStateException) {
                    continue
                }
                /** 解析单元格为具体参数*/
                val parameters = parseCell(field, cell)
                /** 构造并使用set方法 */
                val method = generateSet(target, field)
                /** 执行方法依次添加对应的数据到目标对象*/
                method.invoke(any, parameters)
            }
            result.add(any)
        }
        return result
    }

    /**
     * 判断单元格是否为空
     * @return 空 : true <-> 非空 : false
     */
    private fun isEmpty(cell: Cell, field: Field): Boolean {
        return CellType.BLANK == cell.cellTypeEnum || CellType.STRING == cell.cellTypeEnum && String::class.java.typeName != field.genericType.typeName && cell.stringCellValue.trim() == ""
    }

    /**
     * 传入属性构造出对应set方法
     * @param targetClassType 需要构造的set方法的类
     * @param field 类中的属性
     * @return set方法
     */
    private fun generateSet(targetClassType: Class<*>, field: Field): Method {
        return if (field.name.length > 2 && field.name.substring(0, 2) == "is" && Character.isUpperCase(field.name.substring(2, 3).toCharArray()[0])) {
            targetClassType.getDeclaredMethod(SET + field.name.substring(2), field.type)
        } else {
            targetClassType.getDeclaredMethod(SET + WordUtils.capitalize(field.name), field.type)
        }
    }

    /**
     * 获取MultipartFile类型文件后缀
     * @param data 上传的MultipartFile文件
     */
    private fun getFileSuffix(data: MultipartFile): String {
        val fileName = data.originalFilename
        return fileName.substring(fileName.lastIndexOf(".") + 1)
    }

    /**
     * 文件校验
     * (1 . 验证是否为空文件)
     * @param data 上传的MultipartFile文件
     */
    fun checkFile(data: MultipartFile) {
        /** 验证文件是否为空*/
        takeUnless { data.isEmpty }
                ?: throw RuntimeException(ExcelExceptionEnum.FILE_CONTENT_NULL_ERROR.message)
    }

    /**
     * Excel单元格处理器(分发到对应的具体处理器中)
     * @param field 单元格对应的Field
     * @param cell 需要解析的单元格
     * @return 返回单元格对应字段的参数
     */
    private fun parseCell(field: Field, cell: Cell): Any {
        return when (field.getAnnotation(ExcelAnnotation::class.java).pattern) {
            "" -> parseCommonCell(field, cell)
            else -> try {
                parsePatternCell(field, cell)
            } catch (e: IllegalStateException) {
                parseCommonCell(field, cell)
            }
        }
    }

    /**
     * Common的 Excel单元格处理器
     */
    private fun parseCommonCell(field: Field, cell: Cell): Any {
        ExcelClassTypeEnum.values().forEach {
            if (field.genericType.typeName == it.classType.typeName) {
                cell.setCellType(it.cellType)
                // 执行cell获取值方法取得参数执行强转并创建对象返回
                return ExcelClassConvert.commonClassTypeConvert(field, Cell::class.java.getMethod(it.method).invoke(cell))
            }
        }
        throw RuntimeException(ExcelExceptionEnum.CLASS_ERROR.message)
    }

    /**
     * 含有pattern注解属性的 Excel单元格处理器
     */
    private fun parsePatternCell(field: Field, cell: Cell): Any {
        return ExcelClassConvert.patternClassTypeConvert(field, cell.stringCellValue)
    }

    /**
     * 获取并处理注解中标注的内容
     * @param fields 所有Field
     * @return 过滤掉未标记注解的参数并按照注解顺序返回成员变量Field
     */
    private fun disposeAnnotationField(fields: Array<Field>): List<Field> {
        // 过滤不带有ExcelAnnotation注解的Field
        val fieldList = fields.toList()
                .stream()
                .filter { it.getAnnotation(ExcelAnnotation::class.java) != null }
                .collect(Collectors.toList())
        // 排序
        fieldList.sortWith(Comparator.comparingInt { m -> m.getAnnotation(ExcelAnnotation::class.java).rank })
        return fieldList
    }

}
