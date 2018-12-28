/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2016 Ricardo Mariaca
 * http://www.dynamicreports.org
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

package gettingstarted;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.constant.ImageType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * 简单导出
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class SimpleReport_Step01 {

	public SimpleReport_Step01() {
		//template();
		//build();
		exportToXls();
		//exportToPdf();
		//exportToImge();
	}

	private void template(){
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File("D:\\dynamicreports-exportToXls.xls"));
			report().setTemplateDesign(new File("D://Coffee.jrxml")).toXls(outputStream);
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void build() {
		try {
			JasperReportBuilder j = report();
			j.columns(//add columns
					//            title,     field name     data type
					col.column("Item",       "item",      type.stringType()),
					col.column("Quantity",   "quantity",  type.integerType()),
					col.column("Unit price", "unitprice", type.bigDecimalType()))
					.title(cmp.text("Getting started1"));

			  j.columns(//add columns
			  	//            title,     field name     data type
			  	col.column("Item",       "item",      type.stringType()),
			  	col.column("Quantity",   "quantity",  type.integerType()),
			  	col.column("Unit price", "unitprice", type.bigDecimalType()))
			  .title(cmp.text("Getting started"))//shows report title
			  .pageFooter(cmp.pageXofY())//shows number of page at page footer
			  .setDataSource(createDataSource())//set datasource

			  .show();//create and show report
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出xls格式的excel
	 */
	private void exportToXls(){
		StyleBuilder boldStyle         = stl.style().boldItalic();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
				.setBorder(stl.penThin())
				.setBackgroundColor(Color.LIGHT_GRAY);

		StyleBuilder columnStyle  = stl.style(boldCenteredStyle)
				.setBorder(stl.penThin());

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File("D:\\dynamicreports-exportToXls.xls"));

			report()//create new report design
					.ignorePageWidth()
					.setColumnTitleStyle(columnTitleStyle)
					.setColumnStyle(columnStyle)
					.columns(//add columns
							//            title,     field name     data type
							col.column("Item",       "item",      type.stringType()).setWidth(200),
							col.column("Quantity",   "quantity",  type.integerType()).setWidth(200),
							col.column("Unit price", "unitprice", type.bigDecimalType()).setWidth(300))
					.title(cmp.text("Getting started"))//shows report title
					.ignorePagination()
					//.pageFooter(cmp.pageXofY())//shows number of page at page footer
					.setDataSource(createDataSource())//set datasource
					.toXls(outputStream);
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出PDF
	 */
	private void exportToPdf(){
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File("D:\\dynamicreports-exportToPdf.pdf"));
			report()//create new report design
					.columns(//add columns
							//            title,     field name     data type
							col.column("Item",       "item",      type.stringType()),
							col.column("Quantity",   "quantity",  type.integerType()),
							col.column("Unit price", "unitprice", type.bigDecimalType()))
					.title(cmp.text("Getting started"))//shows report title
					.pageFooter(cmp.pageXofY())//shows number of page at page footer
					.setDataSource(createDataSource())//set datasource
					.toPdf(outputStream);
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出图片
	 */
	private void exportToImge(){
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File("D:\\dynamicreports-exportToImage.jpg"));
			report()//create new report design
					.columns(//add columns
							//            title,     field name     data type
							col.column("Item",       "item",      type.stringType()),
							col.column("Quantity",   "quantity",  type.integerType()),
							col.column("Unit price", "unitprice", type.bigDecimalType()))
					.title(cmp.text("Getting started"))//shows report title
					.pageFooter(cmp.pageXofY())//shows number of page at page footer
					.setDataSource(createDataSource())//set datasource
					.toImage(outputStream, ImageType.PNG);
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建数据源
	 * @return
	 */
	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
		for (int i = 0;i<50;i++){
			for (int j = 0;j<2;j++){
				dataSource.add("Notebook" + i, 1, new BigDecimal(500));
				dataSource.add("DVD" + i, 5, new BigDecimal(30));
				dataSource.add("DVD" + i, 1, new BigDecimal(28));
				dataSource.add("DVD" + i, 5, new BigDecimal(32));
				dataSource.add("Book" + i, 3, new BigDecimal(11));
				dataSource.add("Book" + i, 1, new BigDecimal(15));
				dataSource.add("Book" + i, 5, new BigDecimal(10));
				dataSource.add("Book" + i, 8, new BigDecimal(9));
			}
		}

		return dataSource;
	}

	public static void main(String[] args) {
		new SimpleReport_Step01();
	}
}