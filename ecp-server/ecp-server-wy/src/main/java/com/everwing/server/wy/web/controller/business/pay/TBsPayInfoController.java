package com.everwing.server.wy.web.controller.business.pay;


import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.entity.product.TProductOrderSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.annex.AnnexApi;
import com.everwing.coreservice.wy.api.business.pay.TBsPayInfoApi;
import com.everwing.coreservice.wy.api.configuration.project.TBsProjectApi;
import com.everwing.coreservice.wy.api.product.TProductOrderApi;
import com.everwing.coreservice.wy.api.sys.TSysProjectApi;
import com.everwing.server.wy.util.ReportUtils;
import com.everwing.server.wy.util.ServerSysConfig;
import com.everwing.utils.CreateFileUtil;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;


@Controller
@RequestMapping("/payInfos")
public class TBsPayInfoController {


	@Autowired
	private TBsPayInfoApi tBsPayInfoApi;

	@Autowired
	private FastDFSApi fastDFSApi;

	@Autowired
	private ServerSysConfig sysConfig;

	@Autowired
	private AnnexApi annexApi;

	@Autowired
	private TBsProjectApi tBsProjectApi;

	@Autowired
	private TSysProjectApi tSysProjectApi;

	@Autowired
	protected TProductOrderApi tProductOrderApi;

	@Autowired
	protected SpringRedisTools springRedisTools;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*****************************  聚合后分页    *****************************/

	@PostMapping("/listPage")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户支付/退款/减免: [客户维度]列表查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto listPage(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.listPage(ctx.getCompanyId(),info));
	}

	@PostMapping("/listPage4Building")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户支付/退款/减免: [建筑维度]列表查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto listPage4Building(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.listPage4Building(ctx.getCompanyId(),info));
	}

	/*****************************  历史数据分页    *****************************/
	@PostMapping("/listPageHistory4Building")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户支付/退款/减免: 流水列表按资产分页查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto listPageHistory4Building(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.listPageHistory4Building(ctx.getCompanyId(),info));
	}

	@PostMapping("/listPageHistory4Cust")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户支付/退款/减免: 流水列表按客户分页查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto listPageHistory4Cust(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.listPageHistory4Cust(ctx.getCompanyId(),info));
	}


	@PostMapping("/findByObj")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="支付: [客户维度]交费信息查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto findByObj(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.findByObj(ctx.getCompanyId(),info));
	}

	@PostMapping("/findByObj4Building")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="支付: [建筑维度]交费信息查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto findByObj4Building(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.findByObj4Building(ctx.getCompanyId(),info));
	}

	@PostMapping("/pay2Account/{singleStr}/{isNotSkipLateFee}")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="支付: 交费充值",operationType= OperationEnum.Modify)
	public @ResponseBody BaseDto pay2Account(HttpServletRequest req , @RequestBody TBsPayInfo info, @PathVariable String singleStr, @PathVariable String isNotSkipLateFee){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.pay2Account(ctx.getCompanyId(),info,singleStr,isNotSkipLateFee));
	}

	@PostMapping("/checkAccountExists/{singleStr}")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="支付: 检测账户是否存在",operationType= OperationEnum.Search)
	public @ResponseBody MessageMap checkAccountExists(HttpServletRequest req , @RequestBody TBsPayInfo info, @PathVariable String singleStr){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return this.tBsPayInfoApi.checkAccountExists(ctx.getCompanyId(),info,singleStr).getModel();
	}

	@PostMapping("/findHistory")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="支付: 交费历史数据查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto findHistory(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.findHistory(ctx.getCompanyId(),info));
	}

	@PostMapping("/backMoney")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="退款: 用户退款",operationType= OperationEnum.Modify)
	public @ResponseBody BaseDto backMoney(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.backMoney(ctx.getCompanyId(),info));
	}

	@PostMapping("/jmMoney")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="退款: 减免金额",operationType= OperationEnum.Modify)
	public @ResponseBody BaseDto jmMoney(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.jmMoney(ctx.getCompanyId(),info));
	}

	@PostMapping("/addInvoices")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户流水: 添加发票/小票",operationType= OperationEnum.Modify)
	public @ResponseBody BaseDto addInvoices(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.addInvoices(ctx.getCompanyId(),info));
	}

	@PostMapping("/rollback")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户流水: 回退",operationType= OperationEnum.Modify)
	public @ResponseBody BaseDto rollback(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsPayInfoApi.rollback(ctx.getCompanyId(),info));
	}

	/**
	 * @TODO 收费流水报表(个人)
	 * @param req
	 * @param info
	 * @return
	 */
	@PostMapping("/exportMyDatas")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户流水: 导出我的流水",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto exportMyDatas(HttpServletRequest req , @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();

		BaseDto returnDto = new BaseDto();
		//组装数据
		RemoteModelResult<BaseDto> rslt = this.tBsPayInfoApi.findDatas(ctx.getCompanyId(), info);

		if(rslt.isSuccess() && CommonUtils.isNotEmpty(rslt.getModel())){
			//生成报表

			TSysProjectSearch condition = new TSysProjectSearch();
			condition.setCode(info.getProjectId());
			RemoteModelResult<BaseDto> projectRslt = this.tSysProjectApi.findByCondition(ctx, condition);
			String projectId = "";
			String projectName = Constants.STR_EMPTY;
			if(projectRslt.isSuccess() && CommonUtils.isNotEmpty(projectRslt.getModel())){
				List<TSysProjectList> projects = projectRslt.getModel().getLstDto();
				projectId = projects.get(0).getProjectId();
				projectName = (CommonUtils.isNotEmpty(projects)) ? projects.get(0).getName() : Constants.STR_EMPTY;
			}


			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			String startTime = (null == info.getStartTime()) ? sdf.format(new Date()) : sdf.format(info.getStartTime());
			String endTime = (null == info.getEndTime()) ? sdf.format(new Date()) : sdf.format(info.getEndTime());

			JasperReportBuilder builder = ReportUtils.builder2PayInfo("流水报表",projectName,startTime,endTime);
			DRDataSource ds = new DRDataSource("house_code","batch_no","pay_type","amount","jm_remark","create_time");

			BaseDto dto = rslt.getModel();
			Map<Integer , List<TBsPayInfo>> infosMap = (Map<Integer, List<TBsPayInfo>>) dto.getObj();

			if(!CommonUtils.isEmpty(infosMap)){
				Map<Integer,Double> totalMap = initTotalMap();
				List<TBsPayInfo> infos = null;
				List<TBsPayInfo> cashList = new ArrayList<TBsPayInfo>();
				List<TBsPayInfo> wxList = new ArrayList<TBsPayInfo>();
				List<TBsPayInfo> mixList = new ArrayList<TBsPayInfo>();
				List<TBsPayInfo> zfbList = new ArrayList<TBsPayInfo>();
				List<TBsPayInfo> unionList = new ArrayList<TBsPayInfo>();
				List<TBsPayInfo> bankList = new ArrayList<TBsPayInfo>();
				List<TBsPayInfo> collList = new ArrayList<TBsPayInfo>();

				for(int i = 0 ; i <= 3 ; i++){
					infos = infosMap.get(i);
					if(CommonUtils.isEmpty(infos)){
						continue;
					}
					//0 收款  1 退款 2减免  3回退

					CommonUtils.clearLists(cashList,wxList,unionList,mixList,collList,bankList,zfbList);

					if(i != 2){
						for(TBsPayInfo pi : infos){
							//1 现金, 2 微信 , 3 银联 , 4 混合支付 , 5 托收 , 6 银行收款 , 7 支付宝
							switch (pi.getPayType()) {
								case 1: cashList.add(pi); break;
								case 2: wxList.add(pi); break;
								case 3: unionList.add(pi); break;
								case 4: mixList.add(pi); break;
								case 5: collList.add(pi); break;
								case 6: bankList.add(pi); break;
								case 7: zfbList.add(pi); break;
								default: break;
							}
						}
						oprData2Ds(cashList, 1, ds,totalMap, i);
						oprData2Ds(wxList, 2, ds,totalMap, i);
						oprData2Ds(unionList, 3, ds,totalMap, i);
						oprData2Ds(mixList, 4, ds,totalMap, i);
						oprData2Ds(collList, 5, ds,totalMap, i);
						oprData2Ds(bankList, 6, ds,totalMap, i);
						oprData2Ds(zfbList, 7, ds,totalMap, i);
					}else{
						oprData2Ds(infos, null, ds,totalMap, i);
					}
				}
				ds.add("总计","收款","",CommonUtils.getScaleString(totalMap.get(0), 2),"");
				ds.add("","退款","",CommonUtils.getScaleString(totalMap.get(1), 2),"");
				ds.add("","减免","",CommonUtils.getScaleString(totalMap.get(2), 2),"");
				ds.add("","回退","",CommonUtils.getScaleString(totalMap.get(3), 2),"");

				builder.setDataSource(ds);


				File file = null;
				String prefix = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
				String suffix = ".xls";
				OutputStream outputStream = null;
				try {
					String path = CreateFileUtil.createTempFile(prefix,suffix,null);
					outputStream = new FileOutputStream(new File(path));
					builder.toXlsx(outputStream);
				}  catch (Exception e) {
					returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"导出失败. "));
				} finally{
					if(outputStream != null){
						try {
							outputStream.close();
						} catch (IOException e) {

						}
					}

					if(file != null && file.exists() && file.isFile()){
						file.delete();
					}
				}
			}else{
				returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_WARNING,"未找到你的流水哦. "));
			}
		}else{
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_WARNING,"未找到你的流水哦. "));
		}
		return returnDto;
	}

	private void oprData2Ds(List<TBsPayInfo> infos, Integer i, DRDataSource ds,Map<Integer,Double> totalMap, Integer status) {

		if(CommonUtils.isNotEmpty(infos)){
			String payTypeStr = CommonUtils.null2String(Constants.payTypeMap.get(i));
			double subTotal = 0.0;
			String str = Constants.statusMap.get(status);
			for(TBsPayInfo pi : infos){
				double amount = CommonUtils.getSum(pi.getWyAmount(),pi.getBtAmount(),pi.getWaterAmount(),pi.getElectAmount(),pi.getCommonAmount());

				ds.add(pi.getBuildingCode(),pi.getBatchNo(),payTypeStr+str,CommonUtils.getScaleString(amount, 2),Constants.STR_EMPTY,sdf.format(pi.getCreateTime()));

				if(CommonUtils.null2Double(pi.getWyAmount()) > 0){
					ds.add(Constants.STR_EMPTY,Constants.STR_EMPTY,"物业管理费",CommonUtils.getScaleString(pi.getWyAmount(),2),pi.getJmRemark());
				}
				if(CommonUtils.null2Double(pi.getBtAmount()) > 0){
					ds.add(Constants.STR_EMPTY,Constants.STR_EMPTY,"本体基金",CommonUtils.getScaleString(pi.getBtAmount(),2),pi.getJmRemark());
				}
				if(CommonUtils.null2Double(pi.getWaterAmount()) > 0){
					ds.add(Constants.STR_EMPTY,Constants.STR_EMPTY,"水费",CommonUtils.getScaleString(pi.getWaterAmount(),2),pi.getJmRemark());
				}
				if(CommonUtils.null2Double(pi.getElectAmount()) > 0){
					ds.add(Constants.STR_EMPTY,Constants.STR_EMPTY,"电费",CommonUtils.getScaleString(pi.getElectAmount(),2),pi.getJmRemark());
				}
				if(CommonUtils.null2Double(pi.getCommonAmount()) > 0){
					ds.add(Constants.STR_EMPTY,Constants.STR_EMPTY,"通用账户",CommonUtils.getScaleString(pi.getCommonAmount(),2),pi.getJmRemark());
				}
				subTotal += amount;
				ds.add(Constants.STR_EMPTY,Constants.STR_EMPTY,Constants.STR_EMPTY,Constants.STR_EMPTY,Constants.STR_EMPTY);
			}

			totalMap.put(status, CommonUtils.getSum(totalMap.get(status),subTotal));

			ds.add("小计",payTypeStr+str,"",CommonUtils.getScaleString(subTotal, 2),"");
			ds.add(Constants.STR_EMPTY,Constants.STR_EMPTY,Constants.STR_EMPTY,Constants.STR_EMPTY,Constants.STR_EMPTY);
		}
	}

	private Map<Integer,Double> initTotalMap(){
		Map<Integer,Double> totalMap = new HashMap<Integer, Double>();
		totalMap.put(1, 0.0);
		totalMap.put(2, 0.0);
		totalMap.put(3, 0.0);
		totalMap.put(4, 0.0);
		return totalMap;
	}

	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Common,businessName="导出收款总报表",operationType= OperationEnum.Export)
	@RequestMapping(value = "/exportTotalDatas1",method = RequestMethod.POST)
	public @ResponseBody
	MessageMap exportTotalDatas1(@RequestBody Map condition) {
		MessageMap mm = new MessageMap();
		try {
			exportTotalDatasToXls(condition);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("导出失败："+e.getMessage());
		} catch (DRException e) {
			e.printStackTrace();
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("导出失败："+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("导出失败："+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("导出失败："+e.getMessage());
		}
		return mm;
	}

	private void exportTotalDatasToXls(Map condition) throws Exception {
		WyBusinessContext ctx = WyBusinessContext.getContext();

		String projectName = condition.containsKey("projectName") ? condition.get("projectName").toString() : "测试项目";
		String beginTime = condition.containsKey("beginTime") ? condition.get("beginTime").toString() : new DateTime().toString("yyyy-MM-dd");
		String endTime = condition.containsKey("endTime") ? condition.get("endTime").toString() : new DateTime().toString("yyyy-MM-dd");



		StyleBuilder boldCenteredStyle = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.penThin()).setBackgroundColor(Color.LIGHT_GRAY);
		StyleBuilder columnStyle  = stl.style(boldCenteredStyle).setBorder(stl.penThin()).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.MIDDLE);

		File file = null;
		String prefix = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
		String suffix = ".xls";
		String path = CreateFileUtil.createTempFile(prefix,suffix,null);
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(path));

			TextColumnBuilder<String> typeColumn      = col.column("类型",        "type",      type.stringType()).setWidth(100);
			TextColumnBuilder<String>	itemColumn = col.column("收款项目",  "item", type.stringType()).setWidth(300);
			TextColumnBuilder<String>    totalColumn  = col.column("金额",    "total_price",  type.stringType());

			JasperReportBuilder jrb = report();
			jrb
					.ignorePageWidth()
					.setColumnTitleStyle(columnTitleStyle)
					.setColumnStyle(columnStyle)
					.highlightDetailEvenRows()
					.columns(
							typeColumn,
							itemColumn,
							totalColumn
					)
					.title(
							cmp.text("收款总报表").setStyle(boldCenteredStyle),
							cmp.horizontalFlowList().add(cmp.text("项目：" + projectName).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
									.newRow()
									.add(cmp.text("日期从：" + beginTime).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
									.newRow()
									.add(cmp.text("日期至：" + endTime).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
					)
					.ignorePagination()
					.setDataSource(createTotalDatasDataSource(condition))
					.toXls(outputStream);

			file = new File(path);
			springRedisTools.addData(String.format(WyEnum.download_redis_file_by_loginname_key.getStringValue(),ctx.getLoginName()), FileUtils.readFileToByteArray(file));
		} finally {
			if(outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {

				}
			}

			if(file != null && file.exists() && file.isFile()){
				file.delete();
			}
		}
	}

	/**
	 * 创建产品收款报表数据源
	 * @return
	 */
	private JRDataSource createTotalDatasDataSource(Map condition) {
		WyBusinessContext ctx = WyBusinessContext.getContext();

		// 数据源
		DRDataSource dataSource = new DRDataSource("type","item","total_price");

		/**
		 * 获取前端参数
		 */
		String projectCode = condition.containsKey("projectId") ? condition.get("projectId").toString() : "1019";
		String beginTime = condition.containsKey("beginTime") ? condition.get("beginTime").toString() : new DateTime().toString("yyyy-MM-dd");
		String endTime = condition.containsKey("endTime") ? condition.get("endTime").toString() : new DateTime().toString("yyyy-MM-dd");

		/**
		 * 查询项目
		 */
		TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
		tSysProjectSearch.setLoginName(ctx.getLoginName());
		tSysProjectSearch.setCode(projectCode);
		TSysProjectList tSysProjectList = null;
		RemoteModelResult<BaseDto> remoteModelResultProject = tSysProjectApi.findByCondition(ctx,tSysProjectSearch);
		if(remoteModelResultProject.isSuccess()){
			BaseDto baseDto = remoteModelResultProject.getModel();
			List<TSysProjectList> tSysProjectLists = baseDto.getLstDto();
			if(CollectionUtils.isNotEmpty(tSysProjectLists)){
				tSysProjectList = tSysProjectLists.get(0);
			}
		}

		/**
		 * @@@上半部分：一次性收费
		 */
		TProductOrderSearch tProductOrderSearch = new TProductOrderSearch();
		tProductOrderSearch.setProjectId(tSysProjectList.getProjectId());
		tProductOrderSearch.setBeginTime(beginTime);
		tProductOrderSearch.setEndTime(endTime);
		RemoteModelResult<List<Map>> remoteModelResult =  tProductOrderApi.productCollectingReports(ctx,tProductOrderSearch);
		if(remoteModelResult.isSuccess()){
			List<Map> list = remoteModelResult.getModel();
			if(CollectionUtils.isNotEmpty(list)){
				double totalPriceDouble = 0;
				for (Map map : list) {
					String productName = CommonUtils.null2String(map.get("product_name"));
					BigDecimal totalPrice = new BigDecimal(0.00);
					if(map.containsKey("total_price")){
						totalPrice = new BigDecimal(Double.parseDouble(map.get("total_price").toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
						if("折扣小计".equals(productName)){
							totalPriceDouble -= totalPrice.doubleValue();
						}else{
							totalPriceDouble += totalPrice.doubleValue();
						}
					}
					dataSource.add("一次性收费",map.get("product_name"),totalPrice.toString());
				}
				dataSource.add("一次性收费小计","",new BigDecimal(totalPriceDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			}
		}


		/**
		 * @@@下半部分：周期性收费
		 */
		double chargeDouble = 0;
		double cashDouble = 0;
		double bankDouble = 0;
		double weixinpayDouble = 0;
		double alipayDouble = 0;
		double collDouble = 0;
		double allDouble = 0;

		TBsPayInfo tBsPayInfo = new TBsPayInfo();
		tBsPayInfo.setProjectId(projectCode);
		tBsPayInfo.setStartTime(new DateTime(beginTime).toDate());
		tBsPayInfo.setEndTime(new DateTime(endTime).toDate());
		RemoteModelResult<BaseDto> payInfosRslt = this.tBsPayInfoApi.findTotalDatasByObj(ctx.getCompanyId(), tBsPayInfo);
		if(payInfosRslt.isSuccess() && CommonUtils.isNotEmpty(payInfosRslt.getModel())){
			List<TBsPayInfo> infos = payInfosRslt.getModel().getLstDto();
			if(CommonUtils.isNotEmpty(infos)) {
				dataSource.add("","","");
				double totalPriceDouble = 0;
				for(TBsPayInfo in : infos){
					chargeDouble += in.getPayUnion();
					cashDouble += in.getPayCash();
					bankDouble += in.getPayBank();
					weixinpayDouble += in.getPayWx();
					alipayDouble += in.getPayZfb();
					collDouble += in.getPayColl();
					allDouble += in.getJmAmount();

					BigDecimal totalPrice  = new BigDecimal(in.getPayUnion() + in.getPayCash() + in.getPayBank() + in.getPayWx() + in.getPayZfb() + in.getPayColl() + in.getJmAmount()).setScale(2,BigDecimal.ROUND_HALF_UP);
					dataSource.add("周期性收费",Constants.feeTypeMap.get(in.getStatus()),totalPrice.toString());
					totalPriceDouble += totalPrice.doubleValue();
				}
				dataSource.add("周期性收费小计","",new BigDecimal(totalPriceDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			}
		}


		/**
		 * @@@底部：收款方式汇总
		 */
		// 一次性收费方式汇总
		RemoteModelResult<List<Map>> remoteModelResult4productPayTypeReports =  tProductOrderApi.productPayTypeReports(ctx,tProductOrderSearch);
		if(remoteModelResult4productPayTypeReports.isSuccess()){
			List<Map> list = remoteModelResult4productPayTypeReports.getModel();
			if(CollectionUtils.isNotEmpty(list)){
				Map map = list.get(0);
				if(map != null && map.size() > 0){
					if(map.containsKey("charge")){
						chargeDouble += Double.parseDouble(map.get("charge").toString());
					}

					if(map.containsKey("cash")){
						cashDouble += Double.parseDouble(map.get("cash").toString());
					}

					if(map.containsKey("bank")){
						bankDouble += Double.parseDouble(map.get("bank").toString());
					}

					if(map.containsKey("weixinpay")){
						weixinpayDouble += Double.parseDouble(map.get("weixinpay").toString());
					}

					if(map.containsKey("alipay")){
						alipayDouble += Double.parseDouble(map.get("alipay").toString());
					}
				}
			}
		}

		dataSource.add("","","");
		dataSource.add("按收款方式汇总","现金收款", new BigDecimal(cashDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		dataSource.add("按收款方式汇总","刷卡",new BigDecimal(chargeDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		dataSource.add("按收款方式汇总","银行收款",new BigDecimal(bankDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		dataSource.add("按收款方式汇总","微信",new BigDecimal(weixinpayDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		dataSource.add("按收款方式汇总","支付宝", new BigDecimal(alipayDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		dataSource.add("按收款方式汇总","托收", new BigDecimal(collDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		dataSource.add("按收款方式汇总","混合", new BigDecimal(allDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		dataSource.add("总计","", new BigDecimal(cashDouble + chargeDouble + bankDouble + weixinpayDouble + alipayDouble + collDouble + allDouble).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
		return dataSource;
	}





	/**
	 * @TODO 计费流水 / 账户余额报表
	 * @param req
	 * @param info
	 * @param type
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/exportBillingDatas/{type}")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户流水: 导出计费流水总报表",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto exportBillingDatas(HttpServletRequest req, @RequestBody TBsPayInfo info, @PathVariable Integer type){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		BaseDto returnDto = new BaseDto();
		MessageMap mm = new MessageMap();


		//导出所有的数据
		RemoteModelResult<BaseDto> rslt = this.tBsPayInfoApi.findExportBillingDatas(ctx.getCompanyId(),info, type);

		if(rslt.isSuccess() && CommonUtils.isNotEmpty(rslt.getModel())){
			BaseDto dataDto = rslt.getModel();
			Map<String,Object> resultMap = (Map<String, Object>) dataDto.getObj();

			String projectName = CommonUtils.null2String(resultMap.get("projectName"));
			String startTime = CommonUtils.null2String(resultMap.get("startTime"));
			String endTime = CommonUtils.null2String(resultMap.get("endTime"));

			String titleName = null;
			JasperReportBuilder builder = null;
			DRDataSource ds = null;
			if(type == 1){
				titleName = "计费流水报表";
				builder = ReportUtils.builder2Billing(titleName, projectName, startTime, endTime);
				ds = new DRDataSource("building_code","building_full_name","wy_amount","wy_late_fee","bt_amount","bt_late_fee","water_amount","water_late_fee","elect_amount","elect_late_fee","common_amount");
			}else{
				titleName = "账户余额报表";
				builder = ReportUtils.builder2AccountDatas(titleName, projectName, startTime, endTime);
				ds = new DRDataSource("building_code","wy_amount","wy_late_fee","bt_amount","bt_late_fee","water_amount","water_late_fee","elect_amount","elect_late_fee","common_amount","owed_total");
			}


			List<TBsPayInfo> infos = (List<TBsPayInfo>) resultMap.get("datas");
			if(CommonUtils.isNotEmpty(infos)){

				for(TBsPayInfo pi : infos){

					if(type == 1){
						ds.add(
								pi.getBuildingCode(),
								pi.getBuildingFullName(),
								CommonUtils.formatBigDecimal(pi.getWyAmount()),
								CommonUtils.formatBigDecimal(pi.getWyLateFee()),
								CommonUtils.formatBigDecimal(pi.getBtAmount()),
								CommonUtils.formatBigDecimal(pi.getBtLateFee()),
								CommonUtils.formatBigDecimal(pi.getWaterAmount()),
								CommonUtils.formatBigDecimal(pi.getWaterLateFee()),
								CommonUtils.formatBigDecimal(pi.getElectAmount()),
								CommonUtils.formatBigDecimal(pi.getElectLateFee()),
								CommonUtils.formatBigDecimal(pi.getCommonAmount())
						);
					}else{
						ds.add(
								pi.getBuildingCode(),
								CommonUtils.formatBigDecimal(pi.getWyAmount()),
								CommonUtils.formatBigDecimal(pi.getWyLateFee()),
								CommonUtils.formatBigDecimal(pi.getBtAmount()),
								CommonUtils.formatBigDecimal(pi.getBtLateFee()),
								CommonUtils.formatBigDecimal(pi.getWaterAmount()),
								CommonUtils.formatBigDecimal(pi.getWaterLateFee()),
								CommonUtils.formatBigDecimal(pi.getElectAmount()),
								CommonUtils.formatBigDecimal(pi.getElectLateFee()),
								CommonUtils.formatBigDecimal(pi.getCommonAmount()),
								getOwedMoney(pi)
						);
					}

				}
				builder.setDataSource(ds);


				File file = null;
				String prefix = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
				String suffix = ".xls";
				OutputStream outputStream = null;
				try {
					String path = CreateFileUtil.createTempFile(prefix,suffix,null);
					outputStream = new FileOutputStream(new File(path));
					builder.toXlsx(outputStream);

					file = new File(path);
					springRedisTools.addData(String.format(WyEnum.download_redis_file_by_loginname_key.getStringValue(),ctx.getLoginName()), FileUtils.readFileToByteArray(file));
				}  catch (Exception e) {
					mm.setFlag(MessageMap.INFOR_ERROR);
					mm.setMessage("导出失败："+e.getMessage());
				} finally{
					if(outputStream != null){
						try {
							outputStream.close();
						} catch (IOException e) {

						}
					}

					if(file != null && file.exists() && file.isFile()){
						file.delete();
					}
				}

			}else{
				mm.setFlag(MessageMap.INFOR_WARNING);
				mm.setMessage("未找到相应的数据,导出完成.");
			}
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage("调用服务异常, 导出失败, 请联系管理员");
		}
		returnDto.setMessageMap(mm);
		return returnDto;
	}

	/**
	 * @TODO 收款流水报表(所有)
	 * @param req
	 * @param info
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/exportPayInfoDatas")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Pay,businessName="账户流水: 导出收款流水总报表",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto exportPayInfoDatas(HttpServletRequest req, @RequestBody TBsPayInfo info){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		BaseDto returnDto = new BaseDto();
		MessageMap mm = new MessageMap();

		RemoteModelResult<BaseDto> rslt = this.tBsPayInfoApi.findExportPayInfoDatas(ctx.getCompanyId(), info);
		if(rslt.isSuccess() && CommonUtils.isNotEmpty(rslt.getModel())){


			Map<String,Object> rsltMap = (Map<String, Object>) (rslt.getModel().getObj());

			String titleName = "收费流水报表";
			String projectName = CommonUtils.null2String(rsltMap.get("projectName"));
			String startTime = CommonUtils.null2String(rsltMap.get("startTime"));
			String endTime = CommonUtils.null2String(rsltMap.get("endTime"));

			JasperReportBuilder builder = ReportUtils.builder2AllPayInfoDatas(titleName,projectName,startTime,endTime);

			List<TBsPayInfo> infos = (List<TBsPayInfo>) rsltMap.get("datas");
			if(CommonUtils.isNotEmpty(infos)){
				DRDataSource ds = new DRDataSource("building_code","batch_no","create_id",
						"wy_amount","wy_late_fee",
						"bt_amount","bt_late_fee",
						"water_amount","water_late_fee",
						"elect_amount","elect_late_fee",
						"common_amount",
						"pay_cash","total_late_fee",
						"status","pay_type",
						"payer_name","create_time","jm_remark"
				);

				for(TBsPayInfo in : infos){
					double thisTotalDouble = in.getWyAmount() + in.getBtAmount() + in.getWaterAmount() + in.getElectAmount() + in.getCommonAmount();
					double thisTotal = new BigDecimal(thisTotalDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					double thisTotalLateFee = new BigDecimal(in.getWyLateFee() + in.getBtLateFee() + in.getWaterLateFee() + in.getElectLateFee()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

					ds.add(
							in.getBuildingCode(),
							in.getBatchNo(),
							in.getCreateId(),
							CommonUtils.formatBigDecimal(in.getWyAmount()),
							CommonUtils.formatBigDecimal(in.getWyLateFee()),
							CommonUtils.formatBigDecimal(in.getBtAmount()),
							CommonUtils.formatBigDecimal(in.getBtLateFee()),
							CommonUtils.formatBigDecimal(in.getWaterAmount()),
							CommonUtils.formatBigDecimal(in.getWaterLateFee()),
							CommonUtils.formatBigDecimal(in.getElectAmount()),
							CommonUtils.formatBigDecimal(in.getElectLateFee()),
							CommonUtils.formatBigDecimal(in.getCommonAmount()),
							CommonUtils.formatBigDecimal(thisTotal),
							CommonUtils.formatBigDecimal(thisTotalLateFee),
							Constants.statusMap.get(in.getStatus()),
							Constants.payTypeMap.get(in.getPayType()),
							CommonUtils.null2String(in.getPayerName()),
							CommonUtils.getDateStr(in.getCreateTime()),
							CommonUtils.null2String(in.getJmRemark())
					);
				}

				builder.setDataSource(ds);

				File file = null;
				String prefix = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
				String suffix = ".xls";
				OutputStream outputStream = null;
				try {
					String path = CreateFileUtil.createTempFile(prefix,suffix,null);

					outputStream = new FileOutputStream(new File(path));
					builder.toXlsx(outputStream);
				}catch (Exception e) {
					mm.setFlag(MessageMap.INFOR_ERROR);
					mm.setMessage("导出失败："+e.getMessage());
				} finally{
					if(outputStream != null){
						try {
							outputStream.close();
						} catch (IOException e) {

						}
					}

					if(file != null && file.exists() && file.isFile()){
						file.delete();
					}
				}
			}else{
				mm.setFlag(MessageMap.INFOR_WARNING);
				mm.setMessage("未找到相应的收款流水, 导出完成.");
			}

		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage("调用服务异常, 导出失败, 请联系管理员");
		}
		returnDto.setMessageMap(mm);
		return returnDto;
	}

	private BigDecimal getOwedMoney(TBsPayInfo info){
		//只计算账户欠费  账户欠费包含违约金
		double d = 0.0;
		if(info == null) return CommonUtils.formatBigDecimal(d);
		if(info.getWyAmount() < 0) d += info.getWyAmount();
		if(info.getBtAmount() < 0) d += info.getBtAmount();
		if(info.getWaterAmount() < 0) d += info.getWaterAmount();
		if(info.getElectAmount() < 0) d += info.getElectAmount();

//		d = CommonUtils.getSum(d, -info.getWyLateFee(),-info.getBtLateFee(),-info.getWaterLateFee(),-info.getElectLateFee());
		return CommonUtils.formatBigDecimal(d);
	}
}
