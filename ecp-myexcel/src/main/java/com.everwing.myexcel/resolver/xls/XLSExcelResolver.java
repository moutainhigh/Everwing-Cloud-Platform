package com.everwing.myexcel.resolver.xls;/**
 * Created by wust on 2018/1/14.
 */

import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelExportResult;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/14
 * @author wusongti@lii.com.cn
 */
public abstract class XLSExcelResolver implements ExcelResolver {
    @Override
    public ExcelImportResult readExcel() {
        return null;
    }

    @Override
    public ExcelExportResult createWorkbook() {
        return null;
    }

    @Override
    public void setCellValue(Cell cell, Object value) {

    }

    @Override
    public Object getCellValue(Cell cell) {
        return null;
    }

    @Override
    public boolean isRowEmpty(Row row) {
        return false;
    }
}
