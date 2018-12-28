package com.everwing.coreservice.wy.core.utils;

import com.everwing.coreservice.wy.core.request.ExcelInfo;
import com.everwing.coreservice.wy.core.request.Reflections;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class Excel {

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public  InputStream createExcelInputStream(List<ExcelInfo> l)throws IOException{
		HSSFWorkbook workbook = new HSSFWorkbook(); //产生工作簿对象
		for(int i=0;i<l.size();i++){
			HSSFSheet sheet = workbook.createSheet(); //产生工作表对象
			sheet.setDefaultColumnWidth((short)20);
			workbook.setSheetName(i,l.get(i).getSheetName());//创建一个工作簿
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell;
		     
		    HSSFCellStyle cellStyle2 = workbook.createCellStyle();  //用于将一个单元格变为文本格式
            HSSFDataFormat format = workbook.createDataFormat();  
            cellStyle2.setDataFormat(format.getFormat("@"));  
			for(int j=0;j<l.get(i).getTitles().length;j++){
				    cell = row.createCell((short) j);
				    setHeadCellStyle(workbook, cell);
				    cell.setCellValue(l.get(i).getTitles()[j]);
			} 
			if(l.get(i).getList()!=null && l.get(i).getList().size()>0){
				if(l.get(i).getList().get(0) instanceof HashMap){
					List<HashMap> mapList = l.get(i).getList();
					for(int m=0;m<mapList.size();m++){
						row = sheet.createRow(m+1);
						for (int j=0;j<l.get(i).getFields().length;j++) {
							cell = row.createCell((short) j);
							//if(mapList.get(m).get(l.get(i).getFields()[j])=="cardNum"||mapList.get(m).get(l.get(i).getFields()[j]).equals("cardNum")){			
				            cell.setCellStyle(cellStyle2); 
				            cell.setCellType(HSSFCell.CELL_TYPE_STRING);  
							//}
							cell.setCellValue(String.valueOf(mapList.get(m).get(l.get(i).getFields()[j])));
						}
					}
				}else{
					List list = l.get(i).getList();
					for(int m=0;m<list.size();m++){
						row = sheet.createRow(m+1);
						for (int j=0;j<l.get(i).getFields().length;j++) {
						
							cell = row.createCell((short) j);					
							//if(l.get(i).getFields()[j]=="cardNum"||l.get(i).getFields()[j].equals("cardNum")){
						            cell.setCellStyle(cellStyle2);  					          
						            cell.setCellType(HSSFCell.CELL_TYPE_STRING);  						
							//}
							Object temp = Reflections.invokeGetter(list.get(m),l.get(i).getFields()[j]);
							if(temp!=null){
									if(Reflections.invokeGetter(list.get(m),l.get(i).getFields()[j])instanceof Integer){
									cell.setCellValue(Reflections.invokeGetter(list.get(m),l.get(i).getFields()[j]).hashCode());
							}else{
								cell.setCellValue(Reflections.invokeGetter(list.get(m),l.get(i).getFields()[j]).toString());
							}
						}else{
							cell.setCellValue("");
						  }
						}
					}
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		os.close();
		return is;
	}
	
	
	/**
	 * 
	 * setHeadCellStyle:设置标题样式. <br/>
	 *
	 * @author wust
	 * @param wb
	 * @param headCell
	 * @since
	 */
	private void setHeadCellStyle(HSSFWorkbook wb, HSSFCell headCell) {
        HSSFFont font = wb.createFont();// 创建字体样式
        font.setFontName("Verdana");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN); // 左边边框
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        cellStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边边框颜色
        cellStyle.setBorderTop(CellStyle.BORDER_THIN); // 上边边框
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边边框颜色
        
        headCell.setCellStyle(cellStyle);
        headCell.setCellType(HSSFCell.CELL_TYPE_STRING);  
    }
}
