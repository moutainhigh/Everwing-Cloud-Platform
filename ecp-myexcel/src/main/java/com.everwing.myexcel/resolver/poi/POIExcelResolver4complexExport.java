package com.everwing.myexcel.resolver.poi;/**
 * Created by wust on 2018/1/15.
 */

import com.everwing.myexcel.ExcelParameters;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.exception.MyExcelException;
import com.everwing.myexcel.result.ExcelExportResult;
import com.everwing.myexcel.util.MyExcelUtils;
import com.everwing.myexcel.xmlobject.complex.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:基于POI实现的Excel处理器：负责报表导出产品实现
 * Reason:
 * Date:2018/1/15
 * @author wusongti@lii.com.cn
 */
public abstract class POIExcelResolver4complexExport extends POIExcelResolver{
    protected ExcelParameters excelParameters;

    @Override
    public ExcelExportResult createWorkbook() {
        super.definitionReader = getExcelDefinition();
        return doCreateWorkbook();
    }


    /**
     * 创建工作薄
     * @return
     */
    private ExcelExportResult doCreateWorkbook() throws MyExcelException {
        ExcelExportResult excelExportResult = new ExcelExportResult();

        Workbook workbook = new HSSFWorkbook();

        XMLExcel4Export xmlExcel4Export = (XMLExcel4Export) super.definitionReader.registerConfigDefinitions();
        if(xmlExcel4Export.isEmpty()){
            throw new MyExcelException("创建工作薄失败，没有读取到配置信息");
        }

        List<XMLSheet4Export> xmlSheetList = xmlExcel4Export.getXmlSheet4ExportList();

        // 遍历sheet
        for (int i = 0; i < xmlSheetList.size(); i++) {
            XMLSheet4Export xmlSheet = xmlSheetList.get(i);

            Sheet sheet = workbook.createSheet();
            sheet.setDefaultColumnWidth(15);
            workbook.setSheetName(i, xmlSheet.getAttributeLabel());

            XMLComplexReport4Export xmlComplexReport4Export = xmlSheet.getXmlComplexReport4Export();
            if (xmlComplexReport4Export == null) {
                continue;
            }

            List<XMLRow4Export> xmlRow4ExportList = xmlComplexReport4Export.getXmlRow4ExportList();
            if (CollectionUtils.isEmpty(xmlRow4ExportList)) {
                throw new MyExcelException("导出失败，配置是空的");
            }

            Map<String, Object> businessDatas = getBusinessData();

            int rowIndex = -1;
            for (XMLRow4Export xmlRow4Export : xmlRow4ExportList) {
                rowIndex ++;
                List<XMLCell4Export> xmlCell4Exports = xmlRow4Export.getXmlCell4ExportList();
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(xmlCell4Exports)){
                    int columnIndex = 0;
                    for (XMLCell4Export xmlCell4Export : xmlCell4Exports) {
                        String rowspanStr = xmlCell4Export.getRowspan();
                        String colspanStr = xmlCell4Export.getColspan();


                        /**
                         * 合并单元格
                         */
                        if(StringUtils.isNotBlank(rowspanStr) && StringUtils.isNotBlank(colspanStr)){
                            /*
                             * 设定合并单元格区域范围
                             *  firstRow  0-based
                             *  lastRow   0-based
                             *  firstCol  0-based
                             *  lastCol   0-based
                             */
                            CellRangeAddress cra = new CellRangeAddress(rowIndex, (rowIndex +Integer.parseInt(rowspanStr) - 1), columnIndex, (columnIndex + Integer.parseInt(colspanStr) - 1));
                            /*RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet); // 下边框
                            RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet); // 左边框
                            RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet); // 有边框
                            RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet); // 上边框*/
                            sheet.addMergedRegion(cra);
                        }else if(StringUtils.isNotBlank(rowspanStr)){
                            /*
                             * 设定合并单元格区域范围
                             *  firstRow  0-based
                             *  lastRow   0-based
                             *  firstCol  0-based
                             *  lastCol   0-based
                             */
                            CellRangeAddress cra = new CellRangeAddress(rowIndex, (rowIndex +Integer.parseInt(rowspanStr) - 1), columnIndex, columnIndex);
                            /*RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet); // 下边框
                            RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet); // 左边框
                            RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet); // 有边框
                            RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet); // 上边框*/
                            sheet.addMergedRegion(cra);
                        }else if(StringUtils.isNotBlank(colspanStr)){
                            /*
                             * 设定合并单元格区域范围
                             *  firstRow  0-based
                             *  lastRow   0-based
                             *  firstCol  0-based
                             *  lastCol   0-based
                             */
                            CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, columnIndex, (columnIndex + Integer.parseInt(colspanStr) - 1));
                            /*RegionUtil.setBorderBottom(1, cra, sheet,workbook); // 下边框
                            RegionUtil.setBorderLeft(1, cra, sheet,workbook); // 左边框
                            RegionUtil.setBorderRight(1, cra, sheet,workbook); // 有边框
                            RegionUtil.setBorderTop(1, cra, sheet,workbook); // 上边框*/
                            sheet.addMergedRegion(cra);
                        }

                        /**
                         * 创建单元格
                         */
                        Row row = sheet.getRow(rowIndex) == null ? sheet.createRow(rowIndex) : sheet.getRow(rowIndex);
                        Cell cell = row.createCell(columnIndex++);
                        setCellStyle(workbook, cell, IndexedColors.WHITE.getIndex(), Font.COLOR_NORMAL, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);

                        String cellText = xmlCell4Export.getCellText();
                        if(StringUtils.isNotBlank(cellText) && cellText.startsWith("{{")){
                            if(!CollectionUtils.isEmpty(businessDatas)){
                                String key = xmlCell4Export.getCellText().replaceAll("\\{","").replaceAll("\\}","").replaceAll(" ","");
                                cell.setCellValue(MyExcelUtils.null2String(businessDatas.get(key)));
                            }else{
                                cell.setCellValue(MyExcelUtils.null2String(xmlCell4Export.getCellText()));
                            }
                        }else{
                            cell.setCellValue(MyExcelUtils.null2String(xmlCell4Export.getCellText()));
                        }
                    }
                }
            }
        }
        excelExportResult.setWorkbook(workbook);
        return excelExportResult;
    }


    private void setCellStyle(Workbook wb, Cell headCell,short fillForegroundColor,short textColor,HorizontalAlignment horizontalAlignment,VerticalAlignment verticalAlignment) {
       /* Font font = wb.createFont();// 创建字体样式
        font.setFontName("Verdana");
        font.setColor(textColor);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(fillForegroundColor);
        cellStyle.setBorderBottom((short) 1); // 底部边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        cellStyle.setBorderLeft((short) 1); // 左边边框
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        cellStyle.setBorderRight((short) 1); // 右边边框
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边边框颜色
        cellStyle.setBorderTop((short) 1); // 上边边框
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边边框颜色
        cellStyle.setAlignment(horizontalAlignment);
        cellStyle.setVerticalAlignment(verticalAlignment);

        headCell.setCellStyle(cellStyle);
        headCell.setCellType(CellType.STRING);*/
    }

    /**
     * 获取定义对象，工厂方法（将对象的创建延迟给子类）
     */
    protected abstract ExcelDefinitionReader getExcelDefinition();

    /**
     * 获取业务数据
     * @return
     */
    protected abstract Map<String, Object> getBusinessData();
}
