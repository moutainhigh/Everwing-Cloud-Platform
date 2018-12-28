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

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * 合并表头行和列示例
 */
public class SimpleReport_Step13 {

	public SimpleReport_Step13() {
		build();
	}

	private void build() {
		CurrencyType currencyType = new CurrencyType();

		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		StyleBuilder columnTitleStyle  = stl.style().setBorder(stl.penThin()).setBackgroundColor(Color.LIGHT_GRAY);
		StyleBuilder columnStyle  = stl.style().setBorder(stl.penThin()).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.MIDDLE);

		TextColumnBuilder<String>     houseCode      = col.column("房屋编码",       "houseCode",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     qu      = col.column("区",       "qu",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     qi      = col.column("期",       "q",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     dong      = col.column("栋",       "dong",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     buildingCodeFullName      = col.column("房屋地址",       "buildingCodeFullName",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     wyFee     = col.column("物业管理费用(每月)",       "wyFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     name     = col.column("业主名字",       "name",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     zxPhone   = col.column("注册电话",       "zxPhone",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     lxPhone   = col.column("联系电话",       "lxPhone",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     bankName   = col.column("托收银行",       "bankName",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     bankAccount   = col.column("托收银行",       "bankAccount",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     cardName   = col.column("持卡人名字",       "cardName",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     wylastBillFee     = col.column("本金",       "wylastBillFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     wyDelayFee     = col.column("违约金",       "wyDelayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     wyBillingFee     = col.column("计费金额",       "wyBillingFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     wyPayFee     = col.column("付款金额",       "wyPayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     wyRefundFee     = col.column("退款金额",       "wyRefundFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     wyReliefFee     = col.column("减免金额",       "wyReliefFee",      type.stringType()).setStyle(boldStyle);

		TextColumnBuilder<String>     btlastBillFee      = col.column("本金",       "btlastBillFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     btDelayFee     = col.column("违约金",       "btDelayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     btBillingFee     = col.column("计费金额",       "btBillingFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     btPayFee     = col.column("付款金额",       "btPayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     btRefundFee     = col.column("退款金额",       "btRefundFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     btReliefFee     = col.column("减免金额",       "btReliefFee",      type.stringType()).setStyle(boldStyle);

		TextColumnBuilder<String>     waterlastBillFee     = col.column("本金",       "waterlastBillFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     waterDelayFee     = col.column("违约金",       "waterDelayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     waterBillingFee     = col.column("计费金额",       "waterBillingFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     waterPayFee     = col.column("付款金额",       "waterPayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     waterRefundFee     = col.column("退款金额",       "waterRefundFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     waterReliefFee     = col.column("减免金额",       "waterReliefFee",      type.stringType()).setStyle(boldStyle);

		TextColumnBuilder<String>     elelastBillFee     = col.column("本金",       "elelastBillFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     eleDelayFee     = col.column("违约金",       "eleDelayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     eleBillingFee     = col.column("计费金额",       "eleBillingFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     elePayFee     = col.column("付款金额",       "elePayFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     eleRefundFee     = col.column("退款金额",       "eleRefundFee",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     eleReliefFee     = col.column("减免金额",       "eleReliefFee",      type.stringType()).setStyle(boldStyle);

		TextColumnBuilder<String>     total      = col.column("合计",       "total",      type.stringType()).setStyle(boldStyle);
		TextColumnBuilder<String>     lastMonth      = col.column("管理费欠费月份数测算",       "lastMonth",      type.stringType()).setStyle(boldStyle);



		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File("D:\\myReport.xls"));

			report()//create new report design
					.ignorePageWidth()
					.setColumnTitleStyle(columnTitleStyle)
					.highlightDetailEvenRows()
					.title(
							cmp.text("崔缴报表").setStyle(boldCenteredStyle),
							cmp.horizontalFlowList().add(cmp.text("项目：测试项目").setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
									.newRow()
									.add(cmp.text("统计截至日期：2018-4-30").setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
									.newRow()
									.add(cmp.text("报表输出时间：2018-05-01 11:39:16").setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
					)
					.columns(
							houseCode.setStyle(columnStyle),
							qu.setStyle(columnStyle),
							qi.setStyle(columnStyle),
							dong.setStyle(columnStyle),
							buildingCodeFullName.setStyle(columnStyle),
							wyFee.setStyle(columnStyle),
							name.setStyle(columnStyle),
							zxPhone.setStyle(columnStyle),
							lxPhone.setStyle(columnStyle),
							bankName.setStyle(columnStyle),
							bankAccount.setStyle(columnStyle),
							cardName.setStyle(columnStyle),
							wylastBillFee.setStyle(columnStyle),
							wyDelayFee.setStyle(columnStyle),
							wyBillingFee.setStyle(columnStyle),
							wyPayFee.setStyle(columnStyle),
							wyRefundFee.setStyle(columnStyle),
							wyReliefFee.setStyle(columnStyle),
							btlastBillFee.setStyle(columnStyle),
							btDelayFee.setStyle(columnStyle),
							btBillingFee.setStyle(columnStyle),
							btPayFee.setStyle(columnStyle),
							btRefundFee.setStyle(columnStyle),
							btReliefFee.setStyle(columnStyle),
							waterlastBillFee.setStyle(columnStyle),
							waterDelayFee.setStyle(columnStyle),
							waterBillingFee.setStyle(columnStyle),
							waterPayFee.setStyle(columnStyle),
							waterRefundFee.setStyle(columnStyle),
							waterReliefFee.setStyle(columnStyle),
							elelastBillFee.setStyle(columnStyle),
							eleDelayFee.setStyle(columnStyle),
							eleBillingFee.setStyle(columnStyle),
							elePayFee.setStyle(columnStyle),
							eleRefundFee.setStyle(columnStyle),
							eleReliefFee.setStyle(columnStyle),
							total.setStyle(columnStyle),
							lastMonth.setStyle(columnStyle)
                    )
					.columnGrid(
					  houseCode, qu, qi, dong, buildingCodeFullName, wyFee, name, zxPhone, lxPhone, bankName, bankAccount, cardName,

							houseCode, qu, qi, dong, buildingCodeFullName, wyFee, name, zxPhone, lxPhone, bankName, bankAccount, cardName,

							grid.titleGroup("管理费账户余额", wylastBillFee,  wyDelayFee,wyBillingFee,wyPayFee,wyRefundFee,wyReliefFee),
							grid.titleGroup("本体基金账户余额", btlastBillFee, btDelayFee,btBillingFee,btPayFee,btRefundFee,btReliefFee),
							grid.titleGroup("水费账户余额", waterlastBillFee, waterDelayFee,waterBillingFee,waterPayFee,waterRefundFee,waterReliefFee),
							grid.titleGroup("电费账户余额", elelastBillFee, eleDelayFee,eleBillingFee,elePayFee,eleRefundFee,eleReliefFee),
							total,
							lastMonth
			  )
			  .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))//shows number of page at page footer
			  .setDataSource(createDataSource())//set datasource
			  .show().toXls(outputStream);
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private class CurrencyType extends BigDecimalType {
		private static final long serialVersionUID = 1L;

		@Override
		public String getPattern() {
			return "$ #,###.00";
		}
	}

	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("houseCode", "qu", "qi", "dong", "buildingCodeFullName", "wyFee", "name", "zxPhone", "lxPhone", "bankName",
				"bankAccount", "cardName",
				"wylastBillFee","wyDelayFee","wyBillingFee","wyPayFee","wyRefundFee","wyReliefFee",
				"btlastBillFee", "btDelayFee","btBillingFee","btPayFee","btRefundFee","btReliefFee",
				"waterlastBillFee", "waterDelayFee", "waterBillingFee","waterPayFee","waterRefundFee","waterReliefFee",
				"elelastBillFee", "eleDelayFee","eleBillingFee","elePayFee","eleRefundFee","eleReliefFee",
				"total","lastMonth"


		);
		dataSource.add("Notebook1","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook",
                "Notebook","Notebook","Notebook1","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook9","Notebook10","Notebook11");
		dataSource.add("Notebook2","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook",
                "Notebook","Notebook","Notebook1","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook","Notebook");


		return dataSource;
	}

	public static void main(String[] args) {
		new SimpleReport_Step13();
	}
}