package com.everwing.server.wy.util;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

public class ReportUtils {
	
	/*********************************   整体文字风格       *********************************/
	private static StyleBuilder boldStyle = stl.style().bold();
	private static StyleBuilder rightStyle = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
	private static StyleBuilder amountStyle = stl.style(rightStyle).setFontSize(12);
	private static StyleBuilder centerStyle = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
	private static StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
	private static StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);
	
	private static StyleBuilder titleStyle = stl.style().setFontSize(12);
	private static StyleBuilder titleFontStyle = stl.style().bold().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setFontSize(20);
	
	public static JasperReportBuilder getInstance(){
		return report();
	}
	
	public static JasperReportBuilder getCommonBuilder(String title, String projectName,String startTime,String endTime){
		JasperReportBuilder builder = getInstance();
		
		builder.setColumnTitleStyle(columnTitleStyle)
		   .highlightDetailEvenRows()
		   .setPageMargin(margin().setLeft(0).setRight(0))
		   .setSummaryStyle(boldStyle)
		   .setIgnorePagination(true)   //不重复打印表头
		   .title(cmp.text(title).setStyle(titleFontStyle).setFixedHeight(25),cmp.horizontalList()
		  		.add(cmp.text("管理处: " + projectName).setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
		  		.newRow()
		  		.add(cmp.text("日期从: " + startTime).setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
		  		.newRow()
		  		.add(cmp.text("日期至: " + endTime).setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
		  		.newRow()
		  		.add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen1Point())).setFixedHeight(20))
		  	);
	
		return builder;
	}
	
	/**
	 * @TODO 个人收费流水报表
	 * @param title
	 * @return
	 */
	public static JasperReportBuilder builder2PayInfo(String title,String projectName,String startTime, String endTime){
		JasperReportBuilder builder = getCommonBuilder(title, projectName, startTime, endTime);

		
		TextColumnBuilder<String> houseCode = col.column("房屋名", "house_code", type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String> batchNo = col.column("收款编号",   "batch_no",  type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String> payType = col.column("支付类型", "pay_type", type.stringType());
		TextColumnBuilder<String> amount = col.column("总金额", "amount", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> remark = col.column("备注", "jm_remark", type.stringType());
		TextColumnBuilder<String> createTime = col.column("操作时间", "create_time", type.stringType());
		
		builder.columns(houseCode, batchNo, payType, amount, remark,createTime);
		return builder;
	}
	
	/**
	 * @TODO 收费总报表, 含一次性与周期性报表
	 * @param title
	 * @param projectName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static JasperReportBuilder builder2TotalDatas(String title, String projectName,String startTime,String endTime){
		JasperReportBuilder builder = getCommonBuilder(title,projectName,startTime,endTime);
		
		//定义表头 , 固定房屋编码宽度
		int itemWidth = 80;
		//收款项目
		TextColumnBuilder<String> item = col.column("", "Item", type.stringType()).setStyle(centerStyle).setFixedWidth(itemWidth);
		TextColumnBuilder<String> cash = col.column("现金收款", "Cash", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> union = col.column("银联刷卡", "Union", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> weChat = col.column("微信", "WeChat", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> alipay = col.column("支付宝", "Alipay", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> bank = col.column("银行收款", "Bank", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> collection = col.column("托收", "Collection", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> mix = col.column("混合收款", "mix", type.stringType()).setStyle(amountStyle);
		TextColumnBuilder<String> sumTotal = col.column("合计", "sumTotal", type.stringType()).setStyle(amountStyle);
		
		builder.columns(item,cash,union,weChat,alipay,bank,collection,mix,sumTotal);
		return builder;
	}
	
	/**
	 * @TODO 计费流水报表
	 * @param title
	 * @param projectName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static JasperReportBuilder builder2Billing(String title, String projectName, String startTime , String endTime){
		JasperReportBuilder builder = getCommonBuilder(title,projectName,startTime,endTime);
		
		int houseCodeWidth = 90;
		int otherColumnWidth = 60;
		int lateFeeColumnWidth = 50;
		
		TextColumnBuilder<String> houseCode = col.column("房屋账户", "building_code", type.stringType()).setStyle(boldStyle).setFixedWidth(houseCodeWidth);
		TextColumnBuilder<String> fullName = col.column("房屋全名", "building_full_name", type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<BigDecimal> wyAmount = col.column("物业管理费", "wy_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> wyLateFee = col.column("违约金", "wy_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> btAmount = col.column("本体基金", "bt_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> btLateFee = col.column("违约金", "bt_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> waterAmount = col.column("水费", "water_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> waterLateFee = col.column("违约金", "water_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> electAmount = col.column("电费", "elect_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> electLateFee = col.column("违约金", "elect_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> totalFee = col.column("合计", "common_amount", type.bigDecimalType()).setStyle(amountStyle);
		
		builder.columns(houseCode,fullName,wyAmount,wyLateFee,btAmount,btLateFee,waterAmount,waterLateFee,electAmount,electLateFee,totalFee)
			   .subtotalsAtSummary(
					   					sbt.sum(wyAmount),
					   					sbt.sum(wyLateFee),
					   					sbt.sum(btAmount),
					   					sbt.sum(btLateFee),
					   					sbt.sum(waterAmount),
					   					sbt.sum(waterLateFee),
					   					sbt.sum(electAmount),
					   					sbt.sum(electLateFee),
					   					sbt.sum(totalFee)
					   				);
		return builder;
	}

	/**
	 * @TODO 账户流水报表
	 * @param title
	 * @param projectName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static JasperReportBuilder builder2AccountDatas(String title, String projectName, String startTime , String endTime){
		
		JasperReportBuilder builder = getCommonBuilder(title,projectName,startTime,endTime);
		
		int houseCodeWidth = 85;
		
					
		TextColumnBuilder<String> houseCode = col.column("房屋账户", "building_code", type.stringType()).setStyle(boldStyle).setFixedWidth(houseCodeWidth);
		TextColumnBuilder<String> batchNo = col.column("流水号", "batch_no", type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String> createId = col.column("收款人", "create_id", type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<BigDecimal> wyAmount = col.column("物业管理费", "wy_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> wyLateFee = col.column("违约金", "wy_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> btAmount = col.column("本体基金", "bt_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> btLateFee = col.column("违约金", "bt_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> waterAmount = col.column("水费", "water_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> waterLateFee = col.column("违约金", "water_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> electAmount = col.column("电费", "elect_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> electLateFee = col.column("违约金", "elect_late_fee", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> commonAmount = col.column("通用账户", "common_amount", type.bigDecimalType()).setStyle(amountStyle);
		TextColumnBuilder<BigDecimal> owedTotal = col.column("欠费总计", "owed_total", type.bigDecimalType()).setStyle(amountStyle);
		
		builder.columns(houseCode,wyAmount,wyLateFee,btAmount,btLateFee,waterAmount,waterLateFee,electAmount,electLateFee,commonAmount,owedTotal)
			   .subtotalsAtSummary(
					   					sbt.sum(wyAmount),
					   					sbt.sum(wyLateFee),
					   					sbt.sum(btAmount),
					   					sbt.sum(btLateFee),
					   					sbt.sum(waterAmount),
					   					sbt.sum(waterLateFee),
					   					sbt.sum(electAmount),
					   					sbt.sum(electLateFee),
					   					sbt.sum(commonAmount),
					   					sbt.sum(owedTotal)
					   				);
		
		
		
		return builder;
	}
	
	/**
	 * @TODO 所有收费流水报表
	 * @param title
	 * @param projectName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static JasperReportBuilder builder2AllPayInfoDatas(String title, String projectName, String startTime , String endTime){
		
			JasperReportBuilder builder = getCommonBuilder(title,projectName,startTime,endTime);
	
			int houseCodeWidth = 100;
			
						
			TextColumnBuilder<String> houseCode = col.column("房屋账户", "building_code", type.stringType()).setStyle(centerStyle).setFixedWidth(houseCodeWidth);
			TextColumnBuilder<String> batchNo = col.column("流水号", "batch_no", type.stringType()).setStyle(centerStyle);
			TextColumnBuilder<String> createId = col.column("收款人", "create_id", type.stringType()).setStyle(centerStyle);
			TextColumnBuilder<BigDecimal> wyAmount = col.column("物业管理费", "wy_amount", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> wyLateFee = col.column("违约金", "wy_late_fee", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> btAmount = col.column("本体基金", "bt_amount", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> btLateFee = col.column("违约金", "bt_late_fee", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> waterAmount = col.column("水费", "water_amount", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> waterLateFee = col.column("违约金", "water_late_fee", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> electAmount = col.column("电费", "elect_amount", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> electLateFee = col.column("违约金", "elect_late_fee", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> commonAmount = col.column("通用账户", "common_amount", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> totalAmount = col.column("收费总计", "pay_cash", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<BigDecimal> totalLateFee = col.column("违约金总计", "total_late_fee", type.bigDecimalType()).setStyle(amountStyle);
			TextColumnBuilder<String> status = col.column("流水状态", "status", type.stringType()).setStyle(centerStyle);
			TextColumnBuilder<String> payType = col.column("支付类型", "pay_type", type.stringType()).setStyle(centerStyle);
			TextColumnBuilder<String> payerName = col.column("付款人", "payer_name", type.stringType()).setStyle(centerStyle);
			TextColumnBuilder<String> createTime = col.column("创建时间", "create_time", type.stringType()).setStyle(centerStyle);
			TextColumnBuilder<String> remark = col.column("备注", "jm_remark", type.stringType()).setStyle(centerStyle);
			
			builder.columns(houseCode,batchNo,createId,wyAmount,wyLateFee,btAmount,btLateFee,waterAmount,waterLateFee,electAmount,electLateFee,commonAmount,
					totalAmount,totalLateFee,status,payType,payerName,createTime,remark)
				   .subtotalsAtSummary(
						   					sbt.sum(wyAmount),
						   					sbt.sum(wyLateFee),
						   					sbt.sum(btAmount),
						   					sbt.sum(btLateFee),
						   					sbt.sum(waterAmount),
						   					sbt.sum(waterLateFee),
						   					sbt.sum(electAmount),
						   					sbt.sum(electLateFee),
						   					sbt.sum(commonAmount),
						   					sbt.sum(totalAmount),
						   					sbt.sum(totalLateFee)
						   				);
			return builder;
	
	}
	
}
