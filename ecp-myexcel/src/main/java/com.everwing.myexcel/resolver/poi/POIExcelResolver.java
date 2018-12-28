package com.everwing.myexcel.resolver.poi;/**
 * Created by wust on 2017/8/7.
 */

import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelExportResult;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.util.Date;

/**
 *
 * Function:基于POI实现的Excel处理器
 * Reason:这只是POI的抽象产品，该产品还有导入导出具体产品
 * Date:2017/8/7
 * @author wusongti@lii.com.cn
 */
public abstract class POIExcelResolver implements ExcelResolver {
    protected ExcelDefinitionReader definitionReader;

    @Override
    public ExcelImportResult readExcel() {
        return null;
    }

    @Override
    public ExcelExportResult createWorkbook() {
        return null;
    }

    /**
     * 默认实现
     * @param cell
     * @param value
     */
    @Override
    public void setCellValue(Cell cell, Object value) {
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Number) {
                cell.setCellValue(Double.parseDouble(String.valueOf(value)));
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    /**
     * 默认实现
     * @param cell
     * @return
     */
    @Override
    public Object getCellValue(Cell cell) {
        Object value = null;
        if (null != cell) {
            switch (cell.getCellType()) {
                // 空白
                case Cell.CELL_TYPE_BLANK:
                    break;

                // Boolean
                case Cell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;

                // 错误格式
                case Cell.CELL_TYPE_ERROR:
                    break;

                // 公式
                case Cell.CELL_TYPE_FORMULA:
                    Workbook wb = cell.getSheet().getWorkbook();
                    CreationHelper crateHelper = wb.getCreationHelper();
                    FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                    value = getCellValue(evaluator.evaluateInCell(cell));
                    break;

                // 数值
                case Cell.CELL_TYPE_NUMERIC:
                    // 处理日期格式
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        value = cell.getDateCellValue();
                    } else {
                        value = NumberToTextConverter.toText(cell.getNumericCellValue());
                    }
                    break;

                case Cell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;

                default:
                    value = null;
            }
        }
        return value;
    }

    @Override
    public boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);

            if(cell == null){
                return true;
            }

            DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
            String cellValueString = formatter.formatCellValue(cell); //Returns the formatted value of a cell as a String regardless of the cell type.

            if("null".equalsIgnoreCase(cellValueString)){
                return true;
            }

            if (StringUtils.isNotBlank(cellValueString.replaceAll(" ",""))) {
                return false;
            }
        }
        return true;
    }
}
