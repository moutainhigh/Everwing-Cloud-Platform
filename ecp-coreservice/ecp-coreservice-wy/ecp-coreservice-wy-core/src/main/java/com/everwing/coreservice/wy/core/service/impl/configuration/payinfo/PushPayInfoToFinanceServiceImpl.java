package com.everwing.coreservice.wy.core.service.impl.configuration.payinfo;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.everwing.coreservice.common.utils.RateUtils;
import com.everwing.coreservice.common.wy.fee.entity.AcCommonAccountDetail;
import com.everwing.coreservice.common.wy.fee.entity.AcSpecialDetail;
import com.everwing.coreservice.wy.dao.mapper.account.AcCommonAccountDetailMapper;
import com.everwing.coreservice.wy.dao.mapper.account.AcSpecialDetailMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.jrl.TBcJrlBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.back.TBcUnionBackBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysProjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.json.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.MD5Utils;
import com.everwing.coreservice.common.wy.common.enums.PushFinceInfoEnum;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.entity.configuration.payinfo.PushPayInfoToFinance;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.service.configuration.payinfo.PushPayInfoToFinanceService;
import com.everwing.coreservice.wy.core.utils.HttpClientUtil;
import com.everwing.coreservice.wy.dao.mapper.account.pay.TBsPayInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.payinfo.PushPayInfoToFinanceMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgAccountReceivableMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TProductOrderMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;

/**
 * 推送财务数据接口：

	1.创建推送信息表
	2.查询各个分类的最后推送时间作为参数查询最新的需要推送的数据信息
		现在是按照公司（项目）级别进行消息队列处理
		每个项目的情况又可以按照（物业，本体，水费，电费这些数据量很大的区分开）进行消息队列的处理，获取好数据之后进行httpclient请求进行推送
 * @author qhc
 * @date 2017-10-30
 */
@Service("pushPayInfoToFinanceServiceImpl")
public class PushPayInfoToFinanceServiceImpl implements PushPayInfoToFinanceService{

	private final static Logger logger=Logger.getLogger(PushPayInfoToFinanceServiceImpl.class);
	
	private final static int PUSH_NUMBER = 500;
	
	//物业收费项目，现在只是部分，以后有了新的直接添加即可 物业，本体，水，电，交费，产品，退款，托收
	private static enum TollProjectEnum {pay,product,collection,dikou};
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	
	//消息队列 route_key 声明处
	@Value("${queue.task2Wy.push.pay.info.project.key}")
	private String owner_pay_info_project_key; //按项目进行推送路由
	
	@Value("${queue.task2Wy.push.pay.info.type.key}")
	private String owner_pay_info_type_key;    //按类型进行推送路由
	
	@Value("${queue.task2Wy.push.pay.info.batch.key}")
	private String owner_pay_info_batch_key;    //分批推送路由
	
	@Value("${wy.push.info.finance}")
	private  String client_address_for_push_info;
	
	@Autowired
	private TSysProjectMapper tSysProjectMapper;
	
	@Autowired
	private PushPayInfoToFinanceMapper pushPayInfoToFinanceMapper;
	
	@Autowired
	private TProductOrderMapper tProductOrderMapper;
	
	@Autowired
	private TBsPayInfoMapper tBsPayInfoMapper;
	
	@Autowired
	private TcBuildingMapper tcBuildingMapper;

	@Autowired
	private AcCommonAccountDetailMapper acCommonAccountDetailMapper;

	@Autowired
	private AcSpecialDetailMapper acSpecialDetailMapper;

	@Autowired
	private TJgAccountReceivableMapper tJgAccountReceivableMapper;

	@Autowired
	private TBcJrlBodyMapper tBcJrlBodyMapper;

	@Autowired
	private TBcUnionBackBodyMapper tBcUnionBackBodyMapper;
	
	private static String groupId; //用于推送WC数据的分组id  由日期的字符串组成
	private final static int INTERVALDAYS = 2 ; //一个批次推送的间隔天数

	
	/**
	 * 通过公司查询出项目集合，   按项目进行单个推送处理
	 * @param companyId 公司id用来定位数据源
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto doPushPayInfo(String companyId) {
		
		MessageMap mm=new MessageMap();
		
		logger.info("*********开始推送财务数据的处理，公司id："+companyId+"********");
		
		//这里暂时按照为桃源居这边的项目服务，所以这里按项目来做
		TSysProjectSearch condition=new TSysProjectSearch();
		
		List<TSysProjectList> projectList=this.tSysProjectMapper.findByCondition(condition);
		
		for ( int i= 0;i< projectList.size();i++) {
			
			TSysProjectList tSysProject = projectList.get(i);
			
			logger.info("*********按照公司进行财务数据推送 Start***********");
			
			if( CommonUtils.isNotEmpty( tSysProject.getProjectId() ) ) {
				
				MqEntity entity=new MqEntity();
				
				entity.setCompanyId(companyId);
				
				entity.setProjectId(tSysProject.getCode()+","+tSysProject.getName());
				
				logger.info("*********发送至消息队列:"+owner_pay_info_project_key+"**********");
				
				this.amqpTemplate.convertAndSend(owner_pay_info_project_key, entity);
				
				logger.info("********项目:"+ tSysProject.getProjectId() +"的推送财务信息已发送至消息队列，请关注********");
				
			}
		}
		
		return new BaseDto(mm);
	}
	
	
	
	/**
	 * 按照项目执行推送业主交费信息到财务的操作
	 * 对具体项目的处理
	 * @param companyId  公司id
	 * @param projectId 项目编号
	 * @projectName 项目名称，推送时会用到
	 * @param status 推送状态标识
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto doPushPayInfoToFinance(String companyId,String projectId,String projectName,String status) {
		
		logger.info("*********开始推送财务数据的处理，项目id："+projectId+"********");

		if(CommonUtils.isEmpty(projectId)) {
			logger.info("**********项目信息为空，无法进行数据推送 end**********");
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR, "项目信息为空无法进行推送"));
		}
		
		
		/**
		 * 这里作出调整数据源为：提交到财务的收费数据信息，托收，退款等数据信息。需要分开获取收银的明细和托收的明细
		 * 已经交到财务待审核的收费数据信息  （这里面包含了交费，产品，退费和退回的，其中交费的又包含了物业，本体，水费，电费）
		 * 对具体项目进行有关费用的推送操作数据很多，所以分类型进行处理。按照现有类型进行推送
		 */
		
		groupId = CommonUtils.getDateStr(new Date(), "yyyyMMdd");
		
		for (TollProjectEnum e : TollProjectEnum.values()) {
			
			MqEntity entity=new MqEntity();
			entity.setCompanyId(companyId);
			entity.setProjectId(projectId+","+e.toString()+","+projectName);
			logger.info("*******数据类型"+e.toString()+"数据组装完成，发送到消息队列Start******");
			
			this.amqpTemplate.convertAndSend(owner_pay_info_type_key, entity);
			
			logger.info("*******数据类型"+e.toString()+"数据组装完成，发送到消息队列 end******");
		}
		
		return new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS, "项目信息进行数据推送到财务正在处理，请稍后查看结果"));
		
	}

	
	

	/**
	 * 前面区分了   公司  -- 项目  这里按照同一个项目中不同数据类型进行获取推送
 	 * @param companyId 公司id
	 * @param projectId 公司id
	 * @param type 定位数据类型
	 * @param projectName 项目名称
	 * @param status 推送状态
	 */
	@SuppressWarnings({"rawtypes" })
	@Override
	public BaseDto doPushPayInfoByType(String companyId, String projectId, String type,String projectName,String status) {

		PushPayInfoToFinance entity = new PushPayInfoToFinance(companyId,projectId,projectName,type,null,null);
		//根据type值判断body的数据来源
		if( "pay".equals( type ) ) {
			//交费信息
			assemeblyPayInfo(companyId,projectId,type,projectName,status);
		}else if ( "product".equals( type ) ) {
			//产品信息,产品中又细分为产品和押金
			pushProductData(companyId,projectId,type,projectName);
		}else if ( "collection".equals(type) ) {
			//托收因为不进交割所以直接去pay——info查询	
			pushCollectionData(companyId,projectId,type,projectName);
		} else if("dikou".equals(type)){
			//抵扣分为通用和专项二大类，然后又分为 物业，本体，水，电
			pushDiKouData(companyId,projectId,type,projectName,status);
		}
		
		logger.info("***********本次推送成功,推送类型："+type+" end **********");
		return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"本次推送："+type+" 数据失败"));
	}
	

	
	
	
	/**
	 * 数据类型为 pay ，获取数据并返回
	 * @param companyId 公司编码
	 * @param projectId 项目编码
	 * @param type 数据类型
	 * @param projectName 项目名称
	 * @return 请求body的json
	 */
	public String assemeblyPayInfo(String companyId, String projectId, String type,String projectName,String status){

		/**
		 * 用户缴费信息数据 (已经被财务审核的数据)
		 * 这里注意每一条数据是可以同时缴纳四种费用，但我需要的是每种类型单独为一条数据，进行费率的计算.
		 */
		Map<String, String> paramMap=new HashMap<>();
		//获取数据前需要先得到上次推送的时间
//		PushPayInfoToFinance entity=new PushPayInfoToFinance();
//		entity.setProjectId(projectId);
//		entity.setTollProject(type);
//
//
//		paramMap.put("projectId", projectId );
//		paramMap.put("lastPushDate",lastPushDate );
//		paramMap = this.pushPayInfoToFinanceMapper.getLastPushInfo(paramMap);
//

		paramMap.put("projectId", projectId);
		paramMap.put("tollProject", type);

		paramMap = getLastPushTime(paramMap);



		//如果查询不到最后的推送时间，那就是第一次推送，不需要时间参数
//		String lastPushDate;
//		if( CommonUtils.isNotEmpty(entity) && CommonUtils.isNotEmpty(entity.getCreateTime())) {
//			lastPushDate = CommonUtils.getDateStr(entity.getCreateTime(), "yyyy-mm-dd HH:mm:ss");
//		}else {
//			lastPushDate=null;
//		}


//		paramMap.put("businessType","1");
		//获取数据集合(做出了调整 2018-03-02 获取提交到财务的交费数据,并且财务已经审核了)
		List<TBsPayInfo> resultList = this.tBsPayInfoMapper.getPushDataFroFinece(paramMap);
		//Boolean result = checkPushData(paramMap,type,projectId);
		//处理数据集合
		String bodyInfo;
		if( CommonUtils.isNotEmpty(resultList) && resultList.size() > 0 ) {
			logger.info("********查询到了数据，进行请求body处理********");
			bodyInfo = handlerBodyInfoPay(companyId, projectId, type,projectName,resultList);
			return bodyInfo;
		}else {
			logger.info("**********收费项目 为 "+type+" 的没有可用于推送的数据信息***********");
			return null;
		}
	}


	/**
	 * 类型为dikou，获取返回数据
 	 * @param companyId
	 * @param projectId
	 * @param type
	 * @param projectName
	 * @param status
	 * @return
	 */
  public String pushDiKouData(String companyId, String projectId, String type,String projectName,String status){

	  Map<String, String> paramMap=new HashMap<>();
	  paramMap.put("projectId", projectId);
	  paramMap.put("tollProject", type);
	  //获取当天数据
	  paramMap = getLastPushTime(paramMap);
	  List<AcCommonAccountDetail> conmmonAccountList =  acCommonAccountDetailMapper.getPushDataForCommonAccount(paramMap);
	  List<AcSpecialDetail> specialDetailsList =  acSpecialDetailMapper.getPushDataForSpcialAccount(paramMap);
	  String bodyInfo;
	  if( (CommonUtils.isNotEmpty(conmmonAccountList) && conmmonAccountList.size() > 0 ) || (CommonUtils.isNotEmpty(specialDetailsList) && specialDetailsList.size() > 0 )) {
		  logger.info("********查询到了数据，进行请求body处理********");
		  bodyInfo = handlerBodyDiKou(companyId, projectId, type,projectName,conmmonAccountList,specialDetailsList);
		  return bodyInfo;
	  }else {
		  logger.info("**********收费项目 为 "+type+" 的没有可用于推送的数据信息***********");
		  return null;
	  }

  }















	/**
	 * 数据类型为 pay ，获取数据并返回
	 * @param companyId 公司编码
	 * @param projectId 项目编码
	 * @param type 数据类型
	 * @param projectName 项目名称
	 * @return 请求body的json
	 */
	/*public String assemblyBillingData(String companyId, String projectId, String type,String projectName){
		
		//组装物业计费数据  
		Map<String, String> paramMap=new HashMap<>();
		paramMap.put("projectId", projectId);
		paramMap.put("type", getTypeInfo(type));
		
		//获取数据前需要先得到上次推送的时间
		PushPayInfoToFinance entity=new PushPayInfoToFinance();
		entity.setProjectId(projectId);
		entity.setTollProject(type);
		entity.setTollType(3+"");
		entity = this.pushPayInfoToFinanceMapper.getLastPushInfo(entity);
		//如果查询不到最后的推送时间，那就是第一次推送，不需要时间参数
		String lastPushDate;
		if( CommonUtils.isNotEmpty(entity) && CommonUtils.isNotEmpty(entity.getCreateTime())) {
			lastPushDate = CommonUtils.getDateStr(entity.getCreateTime(), "yyyy-mm-dd HH:mm:ss");
		}else {
			lastPushDate=null;
		}
		paramMap.put("lastPushDate", lastPushDate);
		List<PushPayInfoToFinance> bodyList = this.tBsChargeBillHistoryMapper.getBillingDataForFinece(paramMap);
		String bodyInfo;
		if( CommonUtils.isNotEmpty(bodyList) && bodyList.size() > 0 ) {
			bodyInfo = HandleBodyListInfo(companyId,projectId,type,bodyList,projectName);
		}else {
			logger.info("********收费项目 为 "+type+" 的没有可用于推送的数据信息***********");
			return null;
		}
		
		return bodyInfo;

	}*/

	
	
	/**
	 * 数据类型为 product ，获取数据并返回
	 * @param companyId 公司编码
	 * @param projectId 项目编码
	 * @param type 数据类型
	 * @param projectName 项目名称
	 * @return 请求body的json
	 */
	public void  pushProductData(String companyId, String projectId, String type,String projectName) {
		//1.根据项目id查询成功的产品订单信息
		Map<String, String> paramMap=new HashMap<>();
		paramMap.put("projectId", projectId);
		paramMap.put("tollProject", type);
		
		paramMap = getLastPushTime(paramMap);
		
		List<Map<String, Object>> bodyList = tProductOrderMapper.getProductInfos(paramMap);//bodyList包含了产品和押金的合并信息，还需要后面处理

		//Boolean result = checkPushData(paramMap,type,projectId);
		
		if( CommonUtils.isNotEmpty(bodyList) && bodyList.size() > 0 ) {
			 HandleBodyListForProduct(companyId,projectId,type,bodyList,projectName);
		}else {
			logger.info("********"+projectId+"收费项目 为 "+type+" 的没有可用于推送的数据信息***********");
		}
		
	}
	
	
	
	/**
	 * 查询系统进行托收的数据信息推送WC，托收和周期性收费不同，托收不进银账交割
	 * @param companyId 公司编码
	 * @param projectId 项目编码
	 * @param type 数据类型
	 * @param projectName 项目名称
	 */
	public void pushCollectionData(String companyId, String projectId, String type,String projectName) {
		
		//1.根据项目id查询成功的产品订单信息
		Map<String, String> paramMap=new HashMap<>();
		paramMap.put("projectId", projectId );
		paramMap.put("tollProject",type);
		paramMap = getLastPushTime(paramMap);
		
		//获取数据集合(做出了调整 2018-03-02 获取提交到财务的交费数据,并且财务已经审核了)
		List<TBsPayInfo> resultList = this.tBsPayInfoMapper.getPushCollectDataToFinece(paramMap);
		//处理数据集合
		if( CommonUtils.isNotEmpty(resultList) && resultList.size() > 0 ) {
			logger.info("********查询到了数据，进行请求body处理********");
			handlerBodyInfoPay(companyId, projectId, type,projectName,resultList);
		}else logger.info("**********收费项目 为 "+type+" 的没有可用于推送的数据信息***********");
	}
	
	
	
	
	/**
	 * 之前规划的是第一次推送之前没有推送的所有的数据，但是第一次数据量太大，WC那边压大
	 * 修改为每次都推送一天的数据，这样可以给定groupId
	 * @param companyId  公司id
	 * @param projectId  项目id
	 * @param type 业务类型
	 * @return  
	 */
	public Map<String, String> getLastPushTime(Map<String, String> paraMap) {

		PushPayInfoToFinance entity = this.pushPayInfoToFinanceMapper.getLastPushInfo(paraMap);//如果查询不到最后的推送时间，那就是第一次推送，不需要时间参数
		String lastPushDate , intervalDays;  // 查询推送数据的区间条件

		if( CommonUtils.isNotEmpty(entity) && CommonUtils.isNotEmpty(entity.getCreateTime())) {

			lastPushDate = CommonUtils.getDateStr(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
			//如果上次推送时间notNull说明需要给定截止时间
			intervalDays = CommonUtils.getDateStr(CommonUtils.addDay(entity.getCreateTime(), INTERVALDAYS), "yyyy-MM-dd HH:mm:ss") ;

		}else {
			//如果是null说明是第一次，查看了生产数据最早的一天   2018-01-04 但是每个项目不一样,这里就推送一个以2018-01-10为界限作为结束日期
			intervalDays="2018-10-31 00:00:00";
			//并且终结日期不跟，也就是推送十号以前的数据,然后再一次推送一天的数据
			lastPushDate = "2018-08-01 00:00:00";
		}

		paraMap.put("lastPushDate", lastPushDate);
		paraMap.put("intervalDays", intervalDays);
		return paraMap;
	}
	
	

	/**
	 * 获取请求头信息
	 * @param status 需要传递给WC数据的状态标识
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> assemblyHeaderData(String projectId,String batch){
		
		//组装请求头数据，包括加密
		Map<String, Object> headerMap=new HashMap();
		headerMap.put("user", "adminUser");
		headerMap.put("password",MD5Utils.getMD5("admin123456"));
		long timeMill = new Date().getTime();
		headerMap.put("timeMill", timeMill);
		String sign= "adminUser_"+MD5Utils.getMD5("admin123456")+"_"+timeMill;
		sign=MD5Utils.getMD5(sign);
		logger.info("*******头部分签名信息为"+sign+"*******");
		headerMap.put("sign", sign);
		headerMap.put("groupId", groupId);
		headerMap.put("batch", batch);
		logger.info("*******请求头内容信息:"+headerMap.toString()+"*****");
		//projectId中包含wc需要的结束标识
		if(CommonUtils.isNotEmpty( projectId ) && projectId.contains(",")) {
			String [] columnInfos = projectId.split(",");
			String status = columnInfos[columnInfos.length - 1];
			if( "stop".equals( status )) {
				headerMap.put("status", "stop");
				logger.info("*************这是本次推送的最后一个批次的数据,时间"+CommonUtils.getDateStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"************");
			}
		}
		
		return headerMap;

	}
	

//	public String getTypeInfo(String type) {
//		if("wy".equals(type)) {
//			return "1";
//		}else if ("bt".equals(type)) {
//			return "2";
//		}else if ("water".equals(type)) {
//			return "3";
//		}else if ("elect".equals(type)) {
//			return "4";
//		}else if("product".equals(type)) {
//			return "5";
//		}else if("pay".equals(type)) {
//			return "6";
//		}else {
//			return "0";
//		}
//	}
	
//	public String getTollType( String mode ) {
//		if("cash".equals(mode)) {
//			return "现金";
//		}else if ("charge".equals(mode)) {
//			return "刷卡";
//		}else if ("alipay".equals(mode)) {
//			return "支付宝";
//		}else if ("weixinpay".equals(mode)) {
//			return "微信";
//		}
//		return "未知";
//	}
//
//	public String getProjectInfo(String type) {
//		if("wy".equals(type)) {
//			return "管理费";
//		}else if ("bt".equals(type)) {
//			return "本体基金";
//		}else if ("water".equals(type)) {
//			return "水费";
//		}else if ("elect".equals(type)) {
//			return "电费";
//		}else if("pay".equals(type)) {
//			return "业主交费";
//		}else if ("product".equals(type)) {
//			return "产品购买";
//		}else {
//			return "";
//		}
//
//	}
	
	
	
/*	public String HandleBodyListInfo(String companyId, String projectId, String type,List<PushPayInfoToFinance> bodyList,String projectName) {
		for (PushPayInfoToFinance pushPayInfoToFinance : bodyList) {
			pushPayInfoToFinance.setCompany(projectName);
			pushPayInfoToFinance.setProjectName(projectName);
			pushPayInfoToFinance.setProjectId(projectId);
			pushPayInfoToFinance.setTollProject( getProjectInfo(type));
			pushPayInfoToFinance.setTollType("应收");//应收，还是计费状态
			pushPayInfoToFinance.setTariff( pushPayInfoToFinance.getTariff() + "%" );
			pushPayInfoToFinance.setTollMode(null);//应收不存在付款方式
		}
		
		// 3 : 这里是应付，所以totalType  对应物业存储的值  3
		String bodyInfo = TransInfoForPush(bodyList,companyId,type,3,projectId,null);

		return bodyInfo;
	}*/
	
	
	@SuppressWarnings("unchecked")
	public String HandleBodyListForProduct(String companyId, String projectId, String type,List<Map<String, Object>> bodyList,String projectName) {
		
		logger.info("**********开始获取推送产品信息********");
		List<PushPayInfoToFinance> depositPushList = new ArrayList<>();//押金推送数据
		List<PushPayInfoToFinance> productPushList = new ArrayList<>();//产品推送数据
		
		for (Map<String, Object> productMap : bodyList) {
			//根据具体的订单信息查询   在这里区分押金(税率都是0)和产品    2018010413375079176
//			String batchNo = productMap;//订单号
//			String orderId = productMap.getId();//订单id
			if(CommonUtils.isEmpty( productMap.get("productCommon") ))  continue;
			if(CommonUtils.isEmpty( productMap.get("productType") )) continue;
			if(CommonUtils.isEmpty( productMap.get("createTime") )) continue;
			
			Map<String, String> productCommon  =  com.alibaba.fastjson.JSON.parseObject( String.valueOf( productMap.get("productCommon") ),Map.class ); 
			
			//订单信息描述
			if( CommonUtils.isEmpty( productCommon ) ) continue;
			logger.info("**********产品信息内容为："+productCommon.toString()+"********");
			
			if( CommonUtils.isEmpty(productCommon.get( "buildingCode") ) ) continue;//没有buildingCode就无法拿到houseCode，所以数据没用
			
			//根据buildingCOde查询对应的houseCOde
			//String houseCode  = tcBuildingMapper.getHouseCodeByBuildingCode( productCommon.get( "buildingCode") );
			//if( CommonUtils.isEmpty(houseCode) ) continue;  //houseCode是财务要求必填字段
			double deposit = CommonUtils.null2Double(productMap.get("totalAmount"));  //这个金额代表的是退押金的金额
			if( deposit > 0 ) {
				// 存在押金项
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance(projectName,projectId,projectName,
						String.valueOf(productMap.get("productName")),PushFinceInfoEnum.TOLL_TYPE_CASH.getValue(),"退费");
				pushPayInfoToFinance.setTariff("0%");
				pushPayInfoToFinance.setTaxMoney(0);
				//pushPayInfoToFinance.setHousesCode(houseCode);
				pushPayInfoToFinance.setTollDate( String.valueOf( productMap.get("createTime")).substring(0,10) );
				pushPayInfoToFinance.setIncomeMoney(deposit);
				depositPushList.add(pushPayInfoToFinance);
			}
			
			//产品的
			double payAmount =  CommonUtils.null2Double(productMap.get("orderAmount"));
			if( payAmount > 0 ) {
				//产品的推送信息组装
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance(projectName,projectId,projectName,
						String.valueOf(productMap.get("productName")) , PushFinceInfoEnum.getName(String.valueOf(productMap.get("payType"))),"收费");
				//pushPayInfoToFinance.setHousesCode(houseCode);
				pushPayInfoToFinance.setTollDate( String.valueOf( productMap.get("createTime")).substring(0,10) );
				pushPayInfoToFinance.setIncomeMoney(payAmount);
				//获取税率和税金6%(还有的是 0.05)
					System.out.println(productCommon.get("taxrate") );
					String rateinfo  = productCommon.get("taxrate");
					if( CommonUtils.isNotEmpty(rateinfo) && rateinfo.contains("%")) {
						rateinfo = rateinfo.substring(0,rateinfo.indexOf("%"));
//						StringUtils.substringBefore(rateinfo,);
					}
					double rate = CommonUtils.null2Double( rateinfo );
				
				
				
				//税金改为收入金额\(1+税率)*税率
				if(rate > 0) {
					double amount1 = BigDecimalUtils.mul(payAmount,rate);
					double amount2 = BigDecimalUtils.add(1, rate);
					double rateAmount = BigDecimalUtils.div(amount1, amount2, 2);
					pushPayInfoToFinance.setTariff(rate*100 + "%");
					pushPayInfoToFinance.setTaxMoney(rateAmount);
					
				}else {
					pushPayInfoToFinance.setTariff("0%");
					pushPayInfoToFinance.setTaxMoney(0);
				}
				productPushList.add(pushPayInfoToFinance);
				
			}
			
			
		}
		
		
		TransInfoForPush(productPushList,companyId,type,1,projectId) ;
		
		TransInfoForPush(depositPushList,companyId,type,1,projectId) ;
		return null;
	}
		


	/**
	 * 产品我们配置好了产品类型，这里根据配置的产品类型获取财务配置的收费科目名称
	 * @return
	 */
	public String getProjectNameByProductType (String productType) {
		
		//产品类型
//		protected static final String PRODUCTTYPE_BUILDINGLEASE = "buildingLease";
//	    protected static final String PRODUCTTYPE_CARPARKSCARD = "carParksCard";
//	    protected static final String PRODUCTTYPE_FIXEDCARPARK = "fixedCarPark";
//	    protected static final String PRODUCTTYPE_ENTRANCEGUARDCARD  = "entranceGuardCard";
//	    protected static final String PRODUCTTYPE_DECORATIONSERVICE = "decorationService";
//	    protected static final String PRODUCTTYPE_COMMONSERVICE = "commonService";
		
		if( "buildingLease".equals("productType") )  return "buildingLease";
		else if ( "carParksCard".equals("productType") ) return "carParksCard";
		else if ( "fixedCarPark".equals("productType") ) return "fixedCarPark";
		else if ( "entranceGuardCard".equals("productType") ) return "entranceGuardCard";
		else if ( "decorationService".equals("productType") ) return "decorationService";
		else if ( "commonService".equals("productType") ) return "commonService";
		else return "continue"; //没有找到对应的关系
		
	}
	
	
	
	/**
	 * 这个方法事后来加的，推送数据到财务需要数据的字段和他要求的格式一样才行，所以在这里转一下
	 * @param bodyList 数据库得到的推送数据集合
	 * @param type 数据类型
	 * @return
	 */
	public String TransInfoForPush(List<PushPayInfoToFinance> bodyList,String companyId,String totallProject,int totalType,String projectId){
		
		List<Map<String, Object>> tranList=new ArrayList<>();
		
		//这里为了存储传递的数据，所以每一次算一个批次号
		
		String  batch = getBatchNo(totallProject);
		List<PushPayInfoToFinance> insertList = new ArrayList<>();
		
		for (PushPayInfoToFinance entity : bodyList) {
			Map<String, Object> dataM=new HashMap<String, Object>();
			
			entity.setProjectName(  PushFinceInfoEnum.getName( entity.getProjectName() ) );
			dataM.put("company", entity.getProjectName());
			dataM.put("currency", entity.getCurrency());
			dataM.put("housesCode", entity.getHousesCode());
			dataM.put("incomeMoney", entity.getIncomeMoney());
			dataM.put("tariff",entity.getTariff());
			dataM.put("taxMoney",entity.getTaxMoney());
			dataM.put("tollDate", entity.getTollDate());
			dataM.put("tollMode", entity.getTollMode());
			dataM.put("tollProject",entity.getTollProject());
			dataM.put("tollType",entity.getTollType());
			entity.setBatch(batch);
			tranList.add(dataM);
			insertList.add(entity);
			if( tranList.size() == PUSH_NUMBER ) {
				//500条推送一次     算作一个批次号
				//这里为了存储传递的数据，所以每一次算一个批次号
				pushPayInfoToFinanceMapper.batchInsert(insertList);	
				processingDataForMq(tranList,dataM,companyId,totallProject,totalType,projectId,batch);
				
				tranList.clear();
				insertList.clear();
				batch = getBatchNo(totallProject);
			}
		}
		//pay 目前在最后一个批次,结束时需要给定一个    stop  到  WC
		if ( tranList.size() > 0 ) {
				pushPayInfoToFinanceMapper.batchInsert(insertList);
				processingDataForMq(tranList,tranList.get(0),companyId,totallProject,totalType,projectId,batch);

			tranList.clear();
		}
		
		return null;
	}
	
	
	
	public String getBatchNo(String totallProject) {
		String batchNo = totallProject + CommonUtils.getDateStr(new Date(), "yyyyMMddHHmmss");
        Random random=new Random();
        for(int i=0;i<4;i++){
        	batchNo+=random.nextInt(10);
        }
        return batchNo;
    }
	
	
	
	
	
	/**
	 * 加工数据，进行加密，并推送给消息队列
	 * @param tranList 推送集合
	 * @param dataM 包含几个字段信息
	 * @param companyId 公司id
	 * @param totalProject 物业收费项目
	 * @param totalType 收费类型
	 * @param projectId 项目id
	 * @param batch 每一批次的批次号
	 */
	public void processingDataForMq( List<Map<String, Object>> tranList,
									 Map<String, Object> dataM,
									 String companyId,String totalProject,
									 int totalType, 
									 String projectId,
									 String batch) {
		//现在把body加密的这些过程提取出来作为一个方法，提供给消息队列调用，五百条一次请求WC系统
		//String companyId,String projectId,String projectName
		Map<String , Object> bodyMap=new HashMap<>();
		bodyMap.put("data", tranList);
		String bodyInfo="";
		try {
			bodyInfo = JSON.json(bodyMap);
			logger.info("***********body内容的json串为"+bodyInfo+"**********");
			bodyInfo=Base64.encodeBase64String( bodyInfo.getBytes("UTF-8") );
			logger.info("***********加密后的body内容为"+bodyInfo+"************");
		} catch (IOException e) {
			logger.error(e);
			logger.warn("**********转换body出现异常***********");
		}
		
		MqEntity entity=new MqEntity();
		entity.setCompanyId(companyId);
		entity.setData(bodyInfo);
		//这些是存放推送后物业需要存储的信息，和wc需要的数据有点不同
		entity.setProjectId(dataM.get("company")+","+projectId+","+totalProject+","+totalType+","+batch);
		
		//分批次推送至WC
		logger.info("*********发送至消息队列:"+owner_pay_info_batch_key+"**********");
		this.amqpTemplate.convertAndSend(owner_pay_info_batch_key, entity);
		logger.info("*********财务数据成功推送至MQ**********");
	}
	
	
	
	
	public Object assemblyProductData(){
		//TODO组装产品部分的数据信息
		//这里需要注意的是几个字段的获取方式(订单部分表数据)
		return null;
	}

	public Object assemmblyBackDeposit(){
		//TODO组装退押金部分的数据信息
		return null;
	
	}
	
	

	/**
	 * 获取到业主的缴费集合进行处理（缴费信息是一条数据包含 wy，bt，water，elect   但推送财务需要分为四条数据）
	 * @param companyId
	 * @param projectId
	 * @param type
	 * @param resultList 查询到的业主的缴费记录信息
	 * @return
	 */
	public String handlerBodyInfoPay(String companyId, String projectId, String type,String projectName,List<TBsPayInfo> resultList) {
		List<PushPayInfoToFinance> bodyList=new ArrayList<>();
		
		Map<String, String> taxRateMap = new HashMap<>();
		//税率这里不再采用反推的方式，在这里通过项目新信息进行查询
		List<Map<String,String>> rateList = pushPayInfoToFinanceMapper.getSchemeTaxRate(projectId);
		if( CommonUtils.isNotEmpty(rateList) ) {
			for (Map<String, String> rateMap : rateList) {
				if( "wy".equals(rateMap.get("type")) ) {
					taxRateMap.put("wy", ((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}else if ("bt".equals(rateMap.get("type")) ) {
					taxRateMap.put("bt", ((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}else if ( "water".equals(rateMap.get("type")) ) {
					taxRateMap.put("water",((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}else if ( "elect".equals(rateMap.get("type")) ) {
					taxRateMap.put("elect", ((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}
			}
		}
		
		if ( CommonUtils.isEmpty( taxRateMap.get("wy") ) ) taxRateMap.put("wy","0%");
		if ( CommonUtils.isEmpty( taxRateMap.get("bt") ) ) taxRateMap.put("bt","0%");
		if ( CommonUtils.isEmpty( taxRateMap.get("water") ) ) taxRateMap.put("water","0%");
		if ( CommonUtils.isEmpty( taxRateMap.get("elect") ) ) taxRateMap.put("elect","0%");
		
		
		//这里调整一下，之前按照一个收费的大类型来做，现在 通过     水费  + 应收   来确定是计费   通过   水费  + 收费  来确定是  收费
		//所以要给清楚类型
		for (TBsPayInfo tBsPayInfo : resultList) {
			
			if( 4 == tBsPayInfo.getPayType() ) continue;//之前存在一批混合支付的数据，这里过滤掉
			
			if( !( 0 == tBsPayInfo.getStatus() || 1 == tBsPayInfo.getStatus() ) ) continue;

			String tollType = (0 == tBsPayInfo.getStatus() ? "收费" : "退费");
			
			
			//将tBsPayInfo中的wy，bt，water，elect分开,取业主交费 > 0情况
			if( null != tBsPayInfo.getWyAmount() && tBsPayInfo.getWyAmount() > 0 )  {
				
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance(projectName,projectId,projectName,
						PushFinceInfoEnum.PROJECT_INFO_WY.getValue(),PushFinceInfoEnum.getName(""+tBsPayInfo.getPayType()),tollType);
				
				pushPayInfoToFinance.setTollDate(getDateStr(tBsPayInfo.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(tBsPayInfo.getBuildingCode());//资产信息
				
				//计算税金和税率
				double  wyAmount =  tBsPayInfo.getWyAmount() ;
				double taxWy=  ( null == tBsPayInfo.getWyTax() ? 0.0 : tBsPayInfo.getWyTax() ) ;
				pushPayInfoToFinance.setIncomeMoney(wyAmount);
				pushPayInfoToFinance.setTaxMoney(taxWy);
				pushPayInfoToFinance.setTariff(taxRateMap.get("wy"));
				bodyList.add(pushPayInfoToFinance);
//				addDataToList(wyAmount,taxWy,pushPayInfoToFinance,bodyList); //弃用
				
			}
			
			if(  null != tBsPayInfo.getBtAmount() && tBsPayInfo.getBtAmount() > 0 ) {
				
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance(projectName,projectId,projectName,
						PushFinceInfoEnum.PROJECT_INFO_BT.getValue(),PushFinceInfoEnum.getName(""+tBsPayInfo.getPayType()),tollType);
				
				pushPayInfoToFinance.setTollDate(getDateStr(tBsPayInfo.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(tBsPayInfo.getBuildingCode());//资产信息
				
				//计算税金和税率
				double  btAmount =  tBsPayInfo.getBtAmount();
				
				//税钱这一栏ye增加了处理   （如果是退费一样是需要有税费的，我们系统并有计算出这个，所以要计算出来）
				
				double taxBt= ( null == tBsPayInfo.getBtTax() ? 0.0 : tBsPayInfo.getBtTax() ) ;//税钱
				pushPayInfoToFinance.setIncomeMoney(btAmount);
				pushPayInfoToFinance.setTaxMoney(taxBt);
				pushPayInfoToFinance.setTariff(taxRateMap.get("bt"));
				bodyList.add(pushPayInfoToFinance);
			}
			if( null != tBsPayInfo.getWaterAmount() && tBsPayInfo.getWaterAmount() > 0 ) {
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance(projectName,projectId,projectName,
						PushFinceInfoEnum.PROJECT_INFO_WATER.getValue(),PushFinceInfoEnum.getName(""+tBsPayInfo.getPayType()),tollType);
				
				pushPayInfoToFinance.setTollDate(getDateStr(tBsPayInfo.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(tBsPayInfo.getBuildingCode());//资产信息
				
				double  waterAmount =  tBsPayInfo.getWaterAmount();
				double taxWater = ( null == tBsPayInfo.getWaterTax() ? 0.0 : tBsPayInfo.getWaterTax() ) ;//税钱
				pushPayInfoToFinance.setIncomeMoney(waterAmount);
				pushPayInfoToFinance.setTaxMoney(taxWater);
				pushPayInfoToFinance.setTariff(taxRateMap.get("water"));
				bodyList.add(pushPayInfoToFinance);
			}
			if( null != tBsPayInfo.getElectAmount() && tBsPayInfo.getElectAmount() > 0 ) {
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance(projectName,projectId,projectName,
						PushFinceInfoEnum.PROJECT_INFO_ELECT.getValue(),PushFinceInfoEnum.getName(""+tBsPayInfo.getPayType()),tollType);
				
				pushPayInfoToFinance.setTollDate(getDateStr(tBsPayInfo.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(tBsPayInfo.getBuildingCode());//资产信息
				
				double  electAmount =  tBsPayInfo.getElectAmount() ;
				double taxElect= ( null == tBsPayInfo.getElectTax() ? 0.0 : tBsPayInfo.getElectTax() ) ;//税钱
				pushPayInfoToFinance.setIncomeMoney(electAmount);
				pushPayInfoToFinance.setTaxMoney(taxElect);
				pushPayInfoToFinance.setTariff(taxRateMap.get("elect"));
				bodyList.add(pushPayInfoToFinance);
			}
			if ( null != tBsPayInfo.getCommonAmount() && tBsPayInfo.getCommonAmount() > 0 ) {
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance(projectName,projectId,projectName,
						PushFinceInfoEnum.PROJECT_INFO_COMMON.getValue(),PushFinceInfoEnum.getName(""+tBsPayInfo.getPayType()),tollType);
				pushPayInfoToFinance.setTollDate(getDateStr(tBsPayInfo.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(tBsPayInfo.getBuildingCode());//资产信息
				//通用账户
				double commonAmount =  tBsPayInfo.getCommonAmount();
				pushPayInfoToFinance.setIncomeMoney(commonAmount);
				pushPayInfoToFinance.setTaxMoney(0.0);
				pushPayInfoToFinance.setTariff("0%");
				bodyList.add(pushPayInfoToFinance);
			}
			
		}
		
		String bodyInfo = TransInfoForPush(bodyList,companyId,type,1,projectId) ;
		
		return bodyInfo;
	}

	/**
	 * 获取到抵扣信息，分为二大类，通用抵扣和专项抵扣，每项又分为水，电，物业，本体类型
	 * @param companyId
	 * @param projectId
	 * @param type
	 * @param projectName
	 * @param conmmonAccountList
	 * @param specialDetailsList
	 * @return
	 */
	public String handlerBodyDiKou(String companyId, String projectId, String type,String projectName,
								   List<AcCommonAccountDetail> conmmonAccountList,
								   List<AcSpecialDetail> specialDetailsList){

		//封装的推送对象，把各个类型的数据封装成推送对象
		List<PushPayInfoToFinance> bodyList=new ArrayList<>();
		Map<String, String> taxRateMap = new HashMap<>();
		Map<String, Double> taxRate = new HashMap<>();
		//税率这里不再采用反推的方式，在这里通过项目新信息进行查询
		List<Map<String,String>> rateList = pushPayInfoToFinanceMapper.getSchemeTaxRate(projectId);

		if( CommonUtils.isNotEmpty(rateList) ) {

			for (Map<String, String> rateMap : rateList) {
				if( "wy".equals(rateMap.get("type")) ) {
					taxRateMap.put("wy", ((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}else if ("bt".equals(rateMap.get("type")) ) {
					taxRateMap.put("bt", ((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}else if ( "water".equals(rateMap.get("type")) ) {
					taxRateMap.put("water",((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}else if ( "elect".equals(rateMap.get("type")) ) {
					taxRateMap.put("elect", ((CommonUtils.isEmpty( rateMap.get("rate") ) ? 0 : rateMap.get("rate"))  + "%") );
				}
			}
		}

		if ( CommonUtils.isEmpty( taxRateMap.get("wy") ) ) taxRateMap.put("wy","0%");
		if ( CommonUtils.isEmpty( taxRateMap.get("bt") ) ) taxRateMap.put("bt","0%");
		if ( CommonUtils.isEmpty( taxRateMap.get("water") ) ) taxRateMap.put("water","0%");
		if ( CommonUtils.isEmpty( taxRateMap.get("elect") ) ) taxRateMap.put("elect","0%");

		List<Map<String,Double>> rateDoubleList = pushPayInfoToFinanceMapper.getSchemeTaxRateDouble(projectId);

		if( CommonUtils.isNotEmpty(rateDoubleList) ) {

			for (Map<String, Double> rateMap : rateDoubleList) {
				if( "wy".equals(rateMap.get("type")) ) {
					taxRate.put("wy",  rateMap.get("rate") );
				}else if ("bt".equals(rateMap.get("type")) ) {
					taxRate.put("bt",   rateMap.get("rate"));
				}else if ( "water".equals(rateMap.get("type")) ) {
					taxRate.put("water",    rateMap.get("rate"));
				}else if ( "elect".equals(rateMap.get("type")) ) {
					taxRate.put("elect",   rateMap.get("rate"));
				}
			}
		}

		if ( CommonUtils.isEmpty( taxRate.get("wy") ) ) taxRate.put("wy",0.0);
		if ( CommonUtils.isEmpty( taxRate.get("bt") ) ) taxRate.put("bt",0.0);
		if ( CommonUtils.isEmpty( taxRate.get("water") ) ) taxRate.put("water",0.0);
		if ( CommonUtils.isEmpty( taxRate.get("elect") ) ) taxRate.put("elect",0.0);


		//这里循环通用抵扣集合，把物业 本体 水 电各个类型分开 封装到推送对象里面
		for(AcCommonAccountDetail acCommonAccountDetail:conmmonAccountList){
			if(acCommonAccountDetail.getDikouType()==1 && acCommonAccountDetail.getChangeAmount().doubleValue()>0){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_WY.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acCommonAccountDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acCommonAccountDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  commonWyAmount =  acCommonAccountDetail.getChangeAmount().doubleValue() ;
				//税金
				BigDecimal  taxCommonWyAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("wy")/100)),acCommonAccountDetail.getChangeAmount());
				pushPayInfoToFinance.setTaxMoney(taxCommonWyAmount.doubleValue());
				pushPayInfoToFinance.setIncomeMoney(commonWyAmount);
				pushPayInfoToFinance.setTariff(taxRateMap.get("wy"));
				bodyList.add(pushPayInfoToFinance);
			}
			if(acCommonAccountDetail.getDikouType()==2 && acCommonAccountDetail.getChangeAmount().doubleValue()>0){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_BT.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acCommonAccountDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acCommonAccountDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  commonBtAmount =  acCommonAccountDetail.getChangeAmount().doubleValue() ;
				BigDecimal  taxCommonBtAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("bt")/100)),acCommonAccountDetail.getChangeAmount());
				pushPayInfoToFinance.setTaxMoney(taxCommonBtAmount.doubleValue());
				pushPayInfoToFinance.setIncomeMoney(commonBtAmount);
				pushPayInfoToFinance.setTariff(taxRateMap.get("bt"));
				bodyList.add(pushPayInfoToFinance);
			}
			if(acCommonAccountDetail.getDikouType()==3 && acCommonAccountDetail.getChangeAmount().doubleValue()>0){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_WATER.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acCommonAccountDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acCommonAccountDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  commonWaterAmount =  acCommonAccountDetail.getChangeAmount().doubleValue() ;
				BigDecimal  taxCommonWaterAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("water")/100)),acCommonAccountDetail.getChangeAmount());
				pushPayInfoToFinance.setIncomeMoney(commonWaterAmount);
				pushPayInfoToFinance.setTaxMoney(taxCommonWaterAmount.doubleValue());
				pushPayInfoToFinance.setTariff(taxRateMap.get("water"));
				bodyList.add(pushPayInfoToFinance);
			}
			if(acCommonAccountDetail.getDikouType()==4 && acCommonAccountDetail.getChangeAmount().doubleValue()>0){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_ELECT.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acCommonAccountDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acCommonAccountDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  commonElectAmount =  acCommonAccountDetail.getChangeAmount().doubleValue();
				BigDecimal  taxCommonElectAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("elect")/100)),acCommonAccountDetail.getChangeAmount());
				pushPayInfoToFinance.setTaxMoney(taxCommonElectAmount.doubleValue());
				pushPayInfoToFinance.setIncomeMoney(commonElectAmount);
				pushPayInfoToFinance.setTariff(taxRateMap.get("elect"));
				bodyList.add(pushPayInfoToFinance);
			}
		}

		//循环专项账户的抵扣明细，同样分为水，物业，本体，水电，封装到推送对象
		for(AcSpecialDetail acSpecialDetail:specialDetailsList){
			if(acSpecialDetail.getAccountType()==1 && acSpecialDetail.getChangeAmount().doubleValue()>0  ){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_WY.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acSpecialDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acSpecialDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  specialWyAmount =  acSpecialDetail.getChangeAmount().doubleValue() ;
				BigDecimal  taxSpecialWyAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("wy")/100)),acSpecialDetail.getChangeAmount());
				pushPayInfoToFinance.setTaxMoney(taxSpecialWyAmount.doubleValue());
				pushPayInfoToFinance.setTariff(taxRateMap.get("wy"));
				pushPayInfoToFinance.setIncomeMoney(specialWyAmount);
				bodyList.add(pushPayInfoToFinance);
			}
			if(acSpecialDetail.getAccountType()==2 && acSpecialDetail.getChangeAmount().doubleValue()>0){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_BT.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acSpecialDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acSpecialDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  specialBtAmount =  acSpecialDetail.getChangeAmount().doubleValue() ;
				BigDecimal  taxSpecialBtAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("bt")/100)),acSpecialDetail.getChangeAmount());
				pushPayInfoToFinance.setTaxMoney(taxSpecialBtAmount.doubleValue());
				pushPayInfoToFinance.setTariff(taxRateMap.get("bt"));
				pushPayInfoToFinance.setIncomeMoney(specialBtAmount);
				bodyList.add(pushPayInfoToFinance);
			}
			if(acSpecialDetail.getAccountType()==3 && acSpecialDetail.getChangeAmount().doubleValue()>0){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_WATER.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acSpecialDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acSpecialDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  spcialWaterAmount =  acSpecialDetail.getChangeAmount().doubleValue() ;
				BigDecimal  taxSpecialWaterAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("water")/100)),acSpecialDetail.getChangeAmount());
				pushPayInfoToFinance.setTaxMoney(taxSpecialWaterAmount.doubleValue());
				pushPayInfoToFinance.setTariff(taxRateMap.get("water"));
				pushPayInfoToFinance.setIncomeMoney(spcialWaterAmount);
				bodyList.add(pushPayInfoToFinance);
			}
			if(acSpecialDetail.getAccountType()==4 && acSpecialDetail.getChangeAmount().doubleValue()>0){
				PushPayInfoToFinance pushPayInfoToFinance = new PushPayInfoToFinance();
				//TODO
				pushPayInfoToFinance.setTollType("收费");
				pushPayInfoToFinance.setTollProject("收入");
				pushPayInfoToFinance.setProjectId(projectId);
				pushPayInfoToFinance.setProjectName(projectName);
				pushPayInfoToFinance.setTollProject(PushFinceInfoEnum.PROJECT_INFO_ELECT.getValue());
				pushPayInfoToFinance.setTollDate(getDateStr(acSpecialDetail.getCreateTime()));
				pushPayInfoToFinance.setHousesCode(acSpecialDetail.getHouseCodeNew());
				pushPayInfoToFinance.setCompany(projectName);
				pushPayInfoToFinance.setCurrency("RMB");
				//抵扣金额
				double  specialElectAmount =  acSpecialDetail.getChangeAmount().doubleValue() ;
				BigDecimal  taxSpecialElectAmount = calculateRate(new BigDecimal(String.valueOf(taxRate.get("elect")/100)),acSpecialDetail.getChangeAmount());
				pushPayInfoToFinance.setTaxMoney(taxSpecialElectAmount.doubleValue());
				pushPayInfoToFinance.setTariff(taxRateMap.get("elect"));
				pushPayInfoToFinance.setIncomeMoney(specialElectAmount);
				bodyList.add(pushPayInfoToFinance);
			}

		}
		String bodyInfo = TransInfoForPush(bodyList,companyId,type,1,projectId) ;

		return bodyInfo;
	}


  //计算税金
	public BigDecimal calculateRate(BigDecimal rate,BigDecimal money){
		BigDecimal rateFee=new BigDecimal(0);
		if(rate!=null) {
			if (!rate.equals(new BigDecimal(0))) {
				rateFee = money.divide((new BigDecimal(1).add(rate)), 2, BigDecimal.ROUND_HALF_DOWN).multiply(rate);
			}
		}
		return rateFee;
	}










	/**
	 * 把推送的数据和交割的数据，各个推送项之和做比对，查询数据是否准确，如果准确则推送 ，不准确不推送
	 */
	public boolean checkPushData( Map<String, String> paramMap,String type,String projectId){

		if("pay".equals(type)){
			// 支付统计 按支付类型
			Map<String,Double> payMap = tBsPayInfoMapper.getSumDataFroFinece(paramMap);
			if(CommonUtils.isEmpty(payMap) || payMap.size()==0){
				return false;
			}
			Double payWyAmount = payMap.get("wyAmount");
			Double payBtAmount = payMap.get("btAmount");
			Double payWaterAmount = payMap.get("waterAmount");
			Double payElectAmount = payMap.get("electAmount");
			Double payCommonAmount = payMap.get("commonAmount");
			Double payAllAmount = payWyAmount+payBtAmount+payWaterAmount+payElectAmount+payCommonAmount;
			//银账交割 按类型

			Map<String,Double> tgMap = tJgAccountReceivableMapper.sumPaymentInfoForPush(paramMap);
			if(CommonUtils.isEmpty(tgMap) || tgMap.size()==0){
				return false;
			}

			Double tgAmount =  tgMap.get("amount");

			if(!tgAmount.equals(payAllAmount)){
				return false;
			}



		}
		//产品
		if("product".equals(type)){
			// 支付统计 按支付类型
			//Map<String,Double> productMap = tProductOrderMapper.getProductSum(paramMap);
//			if(CommonUtils.isEmpty(productMap) || productMap.size()==0){
//				return false;
//			}
//			Double productOrderAllAmount = productMap.get("orderAmount");
//			Double productTotalAmount = productMap.get("totalAmount");
//			Double productAmount = productOrderAllAmount+productTotalAmount;
			//银账交割 按类型

			Map<String,Double> tgMap = tJgAccountReceivableMapper.sumProductfoForPush(paramMap);
			if(CommonUtils.isEmpty(tgMap) || tgMap.size()==0){
				return false;
			}
			Double tgAmount = tgMap.get("amount");
//			if( !productAmount.equals(tgAmount)){
//				return false;
//			}


		}
		//托收

		/*if("collection".equals(type)){
			// 按物业本体水电
			Map<String,Double> collectMap  = tBsPayInfoMapper.getSumCollectFroFinece(paramMap);
			Double wyAmount = collectMap.get("wyAmount");
			Double btAmount = collectMap.get("btAmount");
			Double waterAmount = collectMap.get("waterAmount");
			Double electAmount = collectMap.get("electAmount");
			Double collAmount = wyAmount+btAmount+waterAmount+electAmount;
			if(projectId!="1000"){
				Map<String,Double> jrltMap  = tBcJrlBodyMapper.getSuccessDatasForPush(paramMap);
				Double jrlAmount = jrltMap.get("amount");
				if(collAmount!=jrlAmount){
					return false;
				}

			} else{
				Map<String,Double> unionMap = tBcUnionBackBodyMapper.getSuccessUnionForPush(paramMap);
				Double unionWyAmount = collectMap.get("wy_amount");
				Double unionBtAmount = collectMap.get("bt_amount");
				Double unionWaterAmount = collectMap.get("water_amount");
				Double unionElectAmount = collectMap.get("elect_amount");
				Double unionAmount = unionWyAmount+unionBtAmount+unionWaterAmount+unionElectAmount;
				if(unionAmount!=collAmount){
					return false;
				}

			}


		}*/


		return true;


	}






	public String getDateStr(Date date) {
		String dateStr = CommonUtils.getDateStr(date,"yyyy-MM-dd");
		if( CommonUtils.isEmpty(dateStr) ) {
			return "";
		}else {
			return dateStr;
		}
	}
	
	
	
	/**
	 * 推送业主交费数据信息的税率和税金的算法      -- 弃用
	 * @param amount
	 * @param taxAmount
	 * @param entity
	 * @param bodyList
	 */
	public void addDataToList(double amount,double taxAmount,PushPayInfoToFinance entity,List<PushPayInfoToFinance> bodyList) {
		//这里手动计算一下税率     税金 / (交费 - 税金) 
		if(taxAmount <= 0) {
			//没有税金
			entity.setTariff("0%");//税率--这里如果税率是没有的就约定给   0%
			entity.setTaxMoney(0);
		}else {
			double taxRate=BigDecimalUtils.div(taxAmount,(amount - taxAmount) , 2);
			entity.setTariff( BigDecimalUtils.round(taxRate*100,1) + "%" );//税率
			entity.setTaxMoney(taxAmount);
		}
		entity.setIncomeMoney(amount);
		bodyList.add(entity);
	}
	
	
	

	
	/**
	 * 分批次讲数据推送给WC放入消息队列
	 * @param encryptedData 要推送的数据，加密后的
	 * @param companyId 公司id
	 * @param type 类型
	 */
	@SuppressWarnings("unchecked")
	public void pushDataToWC(String companyId,String encryptedData,String projectId) {
		
		logger.info("*********开始进行分批次推送数据********");
		if( CommonUtils.isEmpty(encryptedData) ) {
			return;
		}
		
		//传递过来的参数信息
		PushPayInfoToFinance entity = getPushPayInfo(projectId);
		
		Map<String, Object> headerMap=new HashMap<>();
		headerMap =  assemblyHeaderData(projectId,entity.getBatch());
		
		if( CommonUtils.isEmpty(headerMap) ) {
			logger.info("***********组装请求头信息出错***********");
			return;
		}
		
		Map<String, String> requestMap=new HashMap<>();
		requestMap.put("body", encryptedData);
		
		
		
		try {
			
			//进行推送(暂时先放着，对接的时候处理)
			/*Map<String, String> resultMap=new HashMap<>();
			resultMap.put("message", "数据迁移成功gonggonggong！");
			resultMap.put("status", "success");*/
			String responseInfo = HttpClientUtil.getInstance().sendHttpPost(client_address_for_push_info, requestMap,headerMap);
			logger.info("**********响应数据信息为："+responseInfo+"**********");
//			responseInfo = com.alibaba.fastjson.JSON.toJSONString(resultMap);
			
			Map<String, String> responseMap=com.alibaba.fastjson.JSON.parseObject(responseInfo, Map.class);
			//根据相应内容进行本系统回写
			String batch = responseMap.get("batch");
			if( CommonUtils.isNotEmpty(responseMap) )  {
				// "message":"公司信息有误","index":1,"status":"error","code":"20100","batch":null
				String status = responseMap.get("status");
				
				if( "success".equals(status) ) {
					//推送成功
					entity.setResult("success");
				}else {
					entity.setResult("error");
				}
				entity.setMessage(responseMap.get("message"));
				
				// "message":"公司信息有误","index":1,"status":"error","code":"20100","batch":null
				
			}else {
				//请求没有响应按照推送失败处理
				entity.setResult("error");
				entity.setMessage("未得到响应信息");
			}
			
			//这里不再插入一条数据，进行一个修改，对批量数据进行修改，成功和失败进行记录，便于后续对失败的数据进行跟踪处理
			
			
			//本系统回写
//			entity.setId( CommonUtils.getUUID() );
			entity.setBatch(batch);
			pushPayInfoToFinanceMapper.updataPushPayInfo(entity);
//					this.pushPayInfoToFinanceMapper.addPushPayInfo(entity);
//			if( num > 0 ) logger.info("***********本次推送成功!***********");
//			else logger.info("***********本次推送成功!回写本地异常***********");
		} catch (Exception e) {
			logger.error(e);
			logger.warn("******解析请求信息出现异常******");
		}
	}
	
	
	/**
	 * 从之前组装的数据中获取 PushPayInfoToFinance 对象
	 * @param projectId  这里融合了多个参数信息这里做一个截取操作，以供后面使用
	 *        公司id，项目id,数据科目（财务）,类型,传递数据的批次号
	 */
	private PushPayInfoToFinance getPushPayInfo(String projectId) {
		// projectId 包含多个物业需要存储的字段
		PushPayInfoToFinance entity = null;
		if(CommonUtils.isNotEmpty( projectId ) && projectId.contains(",")) {
			String [] columnInfos = projectId.split(","); 
			entity = new PushPayInfoToFinance(null,columnInfos[1],columnInfos[0],columnInfos[2],null,columnInfos[3]);
			entity.setBatch(columnInfos[4]);
		}
		return entity;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static void main(String[] args) {
				
		   
		//String.valueOf() * S2.5 
//		double currentFee = BigDecimalUtils.mul(97.41,2.5);
//		System.out.println(currentFee);
//		double result = new BigDecimal(String.valueOf(currentFee)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
//		System.out.println(result);
//		System.out.println(PushFinceInfoEnum.PROJECT_INFO_BT.getIndex());
//		System.out.println();
//		STRING NAME = PUSHFINCEINFOENUM.GETNAME("WY");
//		STRING TIE="2019-02-03 12:25:25";
//		SYSTEM.OUT.PRINTLN(TIE.SUBSTRING(0,10));
//		SYSTEM.OUT.PRINTLN(PUSHFINCEINFOENUM.PROJECT_INFO_REFUND.GETVALUE());
//		int batch_no = CommonUtils.getDate("");
		System.out.println( CommonUtils.getDateStr(CommonUtils.addDay(new Date(), 3), "yyyy-MM-dd HH:mm:ss"));
//		
//		System.out.println((0.3/(1+0.07))*0.07);
//		
//		
//		System.out.println( BigDecimalUtils.div(0.02,(0.3 - 0.02) , 2));
//		System.out.println(0.07*100);
//		String ss  = name;
	
		/*Map<String, String> paraMap=new HashMap<>();
		Map<String, Object> headerMap=new HashMap();
		headerMap.put("user", "adminUser");
		headerMap.put("password","a66abb5684c45962d887564f08346e8d");
		System.out.println(MD5Utils.getMD5("admin123456"));
		long timeMill = new Date().getTime();
		headerMap.put("timeMill", timeMill+"");
		System.out.println(timeMill);
		String sign= "adminUser_a66abb5684c45962d887564f08346e8d_"+timeMill;
		sign=MD5Utils.getMD5(sign);
		System.out.println(sign);
		headerMap.put("sign", sign);
		
		headerMap.put("status", "stop");
		headerMap.put("groupId", "20171121171735");
		
		
		List<Map<String, Object>> bodyList=new ArrayList<>();
		Map<String, Object> dataM1=new HashMap<String, Object>();
		dataM1.put("company", "桃源居管理处");
		dataM1.put("currency", "RMB");
		dataM1.put("housesCode", "210711010012901");
		dataM1.put("incomeMoney", 100.0);
		dataM1.put("tariff", "0%");
		dataM1.put("taxMoney",0);
		dataM1.put("tollDate", "2017-12-19");
		dataM1.put("tollMode", "现金");
		dataM1.put("tollProject", "水费");
		dataM1.put("tollType", "应收");
		
		bodyList.add(dataM1);
		
		Map<String, Object> bodyMap=new HashMap<>();
		bodyMap.put("data", bodyList);
		String bodyInfo="";
		
		try {
			bodyInfo = JSON.json(bodyMap);
			logger.info("***********body内容的json串为"+bodyInfo+"**********");
			bodyInfo=Base64.encodeBase64String(bodyInfo.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, String> requestMap=new HashMap<>();
		requestMap.put("body", bodyInfo);
		
		String responseInfo = HttpClientUtil.getInstance().sendHttpPost("http://10.1.20.107:8999/portalservice/property/importData.do",requestMap, headerMap);
		*/
		
	}



	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	@Override
	public BaseDto tellWCPushEnd(String companyId) {
		// 告知WC系统推送数据结束
		Map<String, String> paraMap=new HashMap<>();
		Map<String, Object> headerMap=new HashMap();
		headerMap.put("user", "adminUser");
		headerMap.put("password","a66abb5684c45962d887564f08346e8d");
		long timeMill = new Date().getTime();
		headerMap.put("timeMill", timeMill+"");
		String sign= "adminUser_a66abb5684c45962d887564f08346e8d_"+timeMill;
		sign=MD5Utils.getMD5(sign);
		headerMap.put("sign", sign);
		headerMap.put("status", "pool");
		
		headerMap.put("groupId", CommonUtils.getDateStr(new Date(), "yyyyMMdd"));
		
		Map<String, String> requestMap=new HashMap<>();
		requestMap.put("body", "");
		
		String responseInfo = HttpClientUtil.getInstance().sendHttpPost(client_address_for_push_info,requestMap, headerMap);
		logger.info("==========="+responseInfo+"===========");
		logger.info("==========告知WC推送结束成功========");
		
		return new BaseDto<>();
	}
	
	
	
}
