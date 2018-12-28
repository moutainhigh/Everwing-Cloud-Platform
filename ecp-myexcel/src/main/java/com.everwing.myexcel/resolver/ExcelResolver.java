package com.everwing.myexcel.resolver;/**
 * Created by wust on 2017/5/9.
 */

import com.everwing.myexcel.result.ExcelExportResult;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * Function:Excel处理器抽象产品，具体产品可基于很多种实现，比如POI
 * Reason:一个Excel处理器应该包括最基本的功能：读取Excel内容和创建Excel
 * Date:2017/5/9
 * @author wusongti@lii.com.cn
 */
public interface ExcelResolver {

    /**
     * 读取excel内容
     * @return ExcelImportResult
     */
    ExcelImportResult readExcel();

    /**
     * 创建工作薄
     * @return ExcelExportResult
     */
    ExcelExportResult createWorkbook();

    // 设置单元格值
    void setCellValue(Cell cell, Object value);

    // 获取单元格值
    Object getCellValue(Cell cell);

    /**
     * 判断是否是空行
     * @param row
     * @return
     */
    boolean isRowEmpty(Row row);
}
