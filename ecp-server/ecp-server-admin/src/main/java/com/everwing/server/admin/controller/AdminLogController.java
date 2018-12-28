package com.everwing.server.admin.controller;

import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.server.admin.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adminLog")
public class AdminLogController extends BaseController {
	
	@GetMapping("/main")
	public String index() {
		return toAdminView("admin_log");
	}
	
	@PostMapping("/list")
	public @ResponseBody String list(PageBean pageBean) {
		addParamToPageBean(pageBean, "queryContent","timeStart","timeEnd");
		return renderAdminJson(commonAdminApi.listAdminLogByPage(pageBean));
	}

	

	@GetMapping("/export")
	public void save(PageBean pageBean, HttpServletResponse response) throws Exception {
		addParamToPageBean(pageBean, "queryContent","timeStart","timeEnd");
		RemoteModelResult<PageBean> listAdminLogByPage = commonAdminApi.listAdminLogByPage(pageBean);
		handleResult(listAdminLogByPage);
		//生成excel
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet("Sheet1");
		//加入表头
		ExcelUtils.addRow(sheet, 0, "日志时间","操作账号","日志内容","结果");
		List itemList = listAdminLogByPage.getModel().getItemList();
		for (int i = 0; i < itemList.size(); i++) {//填充数据
			Map<String,Object> dataMap = (Map<String, Object>) itemList.get(i);
			Date createTime = (Date) dataMap.get("createTime");
			String createTimeString = new DateTime(createTime.getTime()).toString("YYYY-MM-dd HH:mm:ss");
			String userName = (String) dataMap.get("operationUserName");
			String content = (String) dataMap.get("operationDescription");
			Boolean isSuccess = (Boolean) dataMap.get("isSuccess");
			String isSuccessString = isSuccess ? "成功" : "失败";
			ExcelUtils.addRow(sheet, i + 1, createTimeString/*日志时间*/, userName/*操作账号*/, content/*日志内容*/, isSuccessString/*结果*/);
		}
		response.setHeader("Content-disposition","attachment; filename=logs_"+ new DateTime(System.currentTimeMillis()).toString("YYYY-MM-dd_HH''mm''ss")+ ".xlsx");
		//输出到流
		wb.write(response.getOutputStream());
	}
	
	public static void main(String[] args) throws Exception {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet("monkong-1");
		ExcelUtils.addRow(sheet, 0, "asd","asdasd");
		
		FileOutputStream out = new FileOutputStream("/Users/MonKong/Desktop/test24.xlsx");
		wb.write(out);
		out.flush();
		out.close();
	}
}