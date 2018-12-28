package com.everwing.coreservice.wy.core.service.impl.configuration.bc.collection;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.AnnexEnum;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.cust.TBankInfo;
import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookup;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupSearch;
import com.everwing.coreservice.common.wy.service.configuration.bc.collection.TBcCollectionService;
import com.everwing.coreservice.common.wy.utils.SysConfig;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.mq.MqSender;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 合版有改动buldingcode转housecode已删除请相关人员确认
 */
@Service("tBcCollectionService")
public class TBcCollectionServiceImpl extends Resources implements TBcCollectionService{

	private static final Logger logger = LogManager.getLogger(TBcCollectionServiceImpl.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static final int commit_amount = 500 ;
	
	private static final char CHAR_SPACE = ' ';
	
	private static final String JRL_RESP_CODE = "--------";
	
	@Value("${queue.wy2Wy.coll.body.batch.insert.key}")
	private String route_key_coll_body_batch_insert;
	
	@Value("${queue.wy2Wy.back.to.account.key}")
	private String route_key_back_to_account;
	
	@Autowired
	private FastDFSApi fastDFSApi;
	
	@Resource
	@Qualifier("sysConfig")
	private SysConfig sysConfig;
	
	@Resource
	@Qualifier("redisDataOperator")
	private SpringRedisTools springRedisTools;
	
	@Value("${queue.wy2Wy.backFileImport.key}")
	private String route_key_import_file;
	
	@Autowired
	private MqSender mqSender;

	/** 
	 * @TODO 分页查询 [银联托收] 母表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageUnionCollHeads(String companyId,TBcUnionCollectionHead head) {
		return new BaseDto(this.tBcUnionCollectionHeadMapper.listPage(head),head.getPage());
	}

	/**
	 * @TODO 分页查询 [银联托收] 子表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageCollBodyInfos(String companyId,TBcUnionCollectionBody body) {
		return new BaseDto(this.tBcUnionCollectionBodyMapper.listPage(body),body.getPage());
	}

	/**
	 * @TODO 分页查询 [银联回盘] 母表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageUnionBackHeads(String companyId,TBcUnionBackHead head) {
		return new BaseDto(this.tBcUnionBackHeadMapper.listPage(head),head.getPage());
	}

	/**
	 * @TODO 分页查询 [银联回盘] 母表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageUnionBackbodies(String companyId,TBcUnionBackBody body) {
		return new BaseDto(this.tBcUnionBackBodyMapper.listPage(body), body.getPage());
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto genCollTxtFile(String companyId,TBcUnionCollectionHead paramHead) {
		
		if(CommonUtils.isEmpty(paramHead.getTotalId()) || CommonUtils.isEmpty(paramHead.getTotalId())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空,无法导出"));
		}
		
		TBcCollectionTotal total = this.tBcCollectionMapper.findById(paramHead.getTotalId());
		if(total == null || total.getCollectionStatus() == CollectionEnum.collection_status_complete.getV()){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"该次托收已经完成,无法再继续导出托收文件."));
		}
		
		List<TBcUnionCollectionHead> heads = this.tBcUnionCollectionHeadMapper.findAllByObj(paramHead);
		if(CommonUtils.isEmpty(heads)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前未找到可供导出的头文件数据"));
		}
		
		TBcUnionCollectionHead head = heads.get(0);
		
		if(CommonUtils.isEmpty(head.getShopNum())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前头文件缺失商户号,提出失败."));
		}
		
		if(CommonUtils.isEmpty(head.getBodies())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前所有托收都已经成功回盘, 无需生成新的托收文件. "));
		}
		
		if(head.getShopNum().length() != 15){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前头文件商户号不为定长15位,请修改商户号, 提出失败."));
		}
		
		if(head.getTotalAmount() > 1000000000){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前头文件总金额必须少于12位, 提出失败. "));
		}
		
		//设置提出时间
		total.setCollectionTime(new Date());
		this.tBcCollectionMapper.update(total);
		
		TBcProject project = this.tBcProjectMapper.findByProjectId(head.getProjectId());
		String privateZoneStr = (null != project && CommonUtils.isNotEmpty(project.getUnionPrivateZone())) ? project.getUnionPrivateZone() : Constants.STR_ZERO;
		
		//获取银联的阈值,如果超过阈值,则跳过  1.8可用lambda表达式filter过滤
		TSysLookupSearch condition = new TSysLookupSearch();
		condition.setCode(CollectionEnum.threshold_union_code_in_lookup.getStringV());
		condition.setLan("zh_CN");
		List<TSysLookup> lookups = this.tSysLookupMapper.findLookupByCondtion(condition);
		double threshold = (CommonUtils.isEmpty(lookups) || CommonUtils.isEmpty(lookups.get(0))) ? 0.0 : CommonUtils.null2Double(lookups.get(0).getName());
		List<TBcUnionCollectionBody> bodies = new ArrayList<TBcUnionCollectionBody>();
		double totalAmount = 0.0;
		
		if(CommonUtils.isNotEmpty(head.getBodies())){
			for(TBcUnionCollectionBody body : head.getBodies()){
				if(threshold == 0 || (threshold > 0 && CommonUtils.null2Double(body.getAmount()) < threshold)){
				     //无阈值                              有阈值 且 当前托收金额少于阈值 可以计入
					bodies.add(body);
					totalAmount = CommonUtils.getSum(totalAmount,body.getAmount());
				}
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuffer sb = new StringBuffer();
		sb.append(head.getShopNum()).append(Constants.STR_VERT_LINE)
		  .append(StringUtils.leftPad(head.getBatchNo(), 6,'0')).append(Constants.STR_VERT_LINE)
		  .append(bodies.size()).append(Constants.STR_VERT_LINE)
		  .append((int)(totalAmount * 100));
		
		
		//TODO
		String currDateStr = sdf.format(new Date());
		if(CommonUtils.isNotEmpty(bodies)){
			sb.append("\n");
			for(int i = 0; i < bodies.size() ; i++){
				TBcUnionCollectionBody body = bodies.get(i);
				if(null == body){
					continue;
				}
			//	if(CommonUtils.null2Double(body.getAmount()) >= threshold) continue;
				body.setCreateTime(new Date());
				this.tBcUnionCollectionBodyMapper.update(body);
				sb.append(currDateStr).append(Constants.STR_VERT_LINE)							//交易日期  20170911
				  .append(body.getOrderNo()).append(Constants.STR_VERT_LINE)					//订单号   16位
				  .append(body.getBankNo()).append(Constants.STR_VERT_LINE)						//银行代号
				  .append(body.getIsCard()).append(Constants.STR_VERT_LINE)						//卡折标记
				  .append(body.getBankCardNo()).append(Constants.STR_VERT_LINE)					//卡号	25位
				  .append(body.getIdCardMasterName()).append(Constants.STR_VERT_LINE)			//卡主名字
				  .append(body.getIdCardType()).append(Constants.STR_VERT_LINE)					//卡主证件类型
				  .append(body.getIdCardNo()).append(Constants.STR_VERT_LINE)					//卡主证件号
				  .append((int)(CommonUtils.null2Double(body.getAmount()*100)))					//金额,以分为单位,此处乘以100
				  .append(Constants.STR_VERT_LINE).append(Constants.STR_ZERO)					//用途
				  .append(Constants.STR_VERT_LINE).append(privateZoneStr);						//私有域
				
				if(i < bodies.size() - 1){
					sb.append("\n");
				}
			}
		}
		//1个txt文件包含头体
		String fileName =  head.getShopNum() + Constants.STR_UNDERLINE 
				+ currDateStr + Constants.STR_UNDERLINE
				+ head.getBatchNo() + Constants.STR_UNDERLINE
				+ "Q.txt";
		
		try {
			RemoteModelResult<UploadFile> rslt = this.fastDFSApi.uploadFile(new ByteArrayInputStream(sb.toString().getBytes("GBK")), fileName);
			
			if(rslt.isSuccess() && rslt.getModel() != null){
				UploadFile upf = rslt.getModel();
				Annex annex = new Annex(upf, AnnexEnum.annex_type_txt.getIntV(), head.getId(), head.getProjectId());
				this.annexMapper.insertAnnex(annex);
				
				//将批次号加1
				Integer batchNo = CommonUtils.null2Int(head.getBatchNo()) + 1;
				head.setBatchNo(StringUtils.leftPad(batchNo + "", 6,'0'));
				this.tBcUnionCollectionHeadMapper.update(head);

				return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"文件生成完成."));
			}else{
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,rslt.getMsg()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件上传到FastDFS文件服务器失败. 异常原因:{},数据:{}",e,sb.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"文件上传到文件服务器失败."));
		} 
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto exportCollTxtFile(String companyId,TBcUnionCollectionHead paramHead , Integer type) {
		BaseDto dto = new BaseDto();
		MessageMap msgMap = null;
		String str = (type == 0) ? "银联" : "金融联";
		if(CommonUtils.isEmpty(paramHead.getId()) || CommonUtils.isEmpty(paramHead.getTotalId())){
			msgMap = new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空,无法导出");
		}else{
			
			TBcCollectionTotal total = this.tBcCollectionMapper.findById(paramHead.getTotalId());
			if(total == null || total.getCollectionStatus() == CollectionEnum.collection_status_complete.getV()){
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"该次托收已经完成,无法再继续导出托收文件."));
			}
			
			Annex annex = this.annexMapper.findCurrentAnnexByRleationId(paramHead.getId());
			if(null != annex){
				dto.setObj(annex);
				msgMap = new MessageMap(MessageMap.INFOR_SUCCESS,"获取托收文件成功.");
				
				//修改该total为待回盘状态
				total.setCollectionStatus(CollectionEnum.collection_status_wait_back.getV());
				total.setIsWaitBack(Constants.STR_YES);
				this.tBcCollectionMapper.update(total);
					
			}else{
				logger.warn("{}托收文件提出: 未找到可供提出的托收文件, 传入数据:{}",str,paramHead.toString());
				msgMap = new MessageMap(MessageMap.INFOR_WARNING,"未找到可供提出的托收文件");
			}
		}
		dto.setMessageMap(msgMap);
		return dto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageJrlHeads(String companyId, TBcJrlHead head) {
		return new BaseDto(this.tBcJrlHeadMapper.listPage(head), head.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageJrlBodies(String companyId, TBcJrlBody body) {
		return new BaseDto(this.tBcJrlBodyMapper.listPage(body),body.getPage());
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto genJrlCollZipFile(String companyId, TBcJrlHead paramHead) {
		MessageMap msgMap = new MessageMap(MessageMap.INFOR_WARNING,null);
		if(CommonUtils.isEmpty(paramHead.getId()) || CommonUtils.isEmpty(paramHead.getTotalId())){
			msgMap.setMessage("金融联文件生成: [传入参数为空] , 生成失败.");
		}else{
			
			List<TBcJrlHead> heads = this.tBcJrlHeadMapper.findByObj(paramHead);
			
			if(CommonUtils.isEmpty(heads) || null == heads.get(0)){
				msgMap.setMessage("金融联文件生成: [当前未找到头文件] , 生成失败.");
				return new BaseDto(msgMap);
			}
			
			
			TBcJrlHead head = heads.get(0);
			if(CommonUtils.isEmpty(head.getEnterpriseNo()) || CommonUtils.isEmpty(head.getBankNo())){
				msgMap.setMessage("金融联文件生成: [头文件的企业代号/银行代码为空] , 生成失败.");
				return new BaseDto(msgMap);
			}
			
			if(CommonUtils.isEmpty(head.getBodies())){
				msgMap.setMessage("金融联文件生成: [未找到可供托收的数据] , 生成失败.");
				return new BaseDto(msgMap);
			}

				
			head.setRequestDate(new Date());
			head.setPayDate(new Date());
			this.tBcJrlHeadMapper.update(head); 
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			DecimalFormat df = new DecimalFormat("0.00");
			String dateStr = sdf.format(new Date());
			
			//金融联必须为GBK格式
			byte[] b = null;
			int bodiesSize = 0;
			double totalAmount = 0.0;
			//获取金融联托收阈值
			TSysLookupSearch condition = new TSysLookupSearch();
			condition.setCode(CollectionEnum.threshold_jrl_code_in_lookup.getStringV());
			condition.setLan("zh_CN");
			List<TSysLookup> lookups = this.tSysLookupMapper.findLookupByCondtion(condition);
			double threshold = (CommonUtils.isEmpty(lookups) || CommonUtils.isEmpty(lookups.get(0))) ? 0.0 : CommonUtils.null2Double(lookups.get(0).getName());
			
			if(CommonUtils.isNotEmpty(head.getBodies())){
				for(TBcJrlBody body : head.getBodies()){
					if(body == null) continue;
					if(body.getAmount() >= threshold) continue;   //过滤超过金融联托收阈值的数据
					totalAmount += CommonUtils.null2Double(body.getAmount());
					bodiesSize ++;
				}
				totalAmount = CommonUtils.getScaleNumber(totalAmount, 2);
			}
			b = addStr2ByteArr(b, head.getBusinessType(), 3);										//业务类型 , 批量代付303, 批量代收304
			b = addStr2ByteArr(b, StringUtils.rightPad(head.getEnterpriseNo(), 14, CHAR_SPACE), 14);//企业代码
			b = addStr2ByteArr(b, StringUtils.rightPad(head.getBankNo(), 14, CHAR_SPACE), 14);		//收款银行号
			b = addStr2ByteArr(b, StringUtils.rightPad(head.getFeeNo(), 5, CHAR_SPACE), 5);			//费项代码
			b = addStr2ByteArr(b, StringUtils.rightPad(head.getFileName(), 8, CHAR_SPACE), 8);		//文件名
			b = addStr2ByteArr(b, head.getFeeType(), 3);											//费种 CNY:人民币 , HKD: 港币 , USD: 美元
			b = addStr2ByteArr(b, StringUtils.leftPad(bodiesSize + "", 8, '0'), 8);		//总笔数,不足8位左补0	
			b = addStr2ByteArr(b, StringUtils.leftPad(totalAmount + "", 16, '0'), 16); //总金额
			b = addStr2ByteArr(b, StringUtils.rightPad("", 24, CHAR_SPACE), 24);						//成功笔数 8位空格 , 成功金额: 16个空格
			b = addStr2ByteArr(b, head.getBusinessNo(), 5);											//业务类型: 30301:批量代付 , 30401: 批量代收
			b = addStr2ByteArr(b, dateStr, 8);														//委托日期
//			b = addStr2ByteArr(b, sdf.format(head.getRequestDate()), 8);							//委托日期
			b = addStr2ByteArr(b, dateStr, 8);														//应付款日期
//			b = addStr2ByteArr(b, sdf.format(head.getPayDate()), 8);								//应付款日期
			b = addStr2ByteArr(b, StringUtils.rightPad("", 8 + 32 + 120, CHAR_SPACE), 160);			//回执日期: 8个空格 , 代办账号 32个空格 , 代办账户名 120个空格
			b = addStr2ByteArr(b, "\n", 1);
			

			if(CommonUtils.isNotEmpty(head.getBodies())){
				
				for (int i = 0; i < head.getBodies().size(); i++) {
					
					TBcJrlBody body = head.getBodies().get(i);
					if(body.getAmount() >= threshold) continue;
					
					b = addStr2ByteArr(b, StringUtils.leftPad(body.getDetailNo(), 8, '0'), 8);									//详情号
					b = addStr2ByteArr(b, StringUtils.rightPad(CommonUtils.null2String(body.getAgreementNo()), 32, CHAR_SPACE), 32);	//协议编号
					b = addStr2ByteArr(b, StringUtils.leftPad(df.format(body.getAmount()), 16, '0'), 16);								//金额
					b = addStr2ByteArr(b, CommonUtils.null2String(body.getBankType()), 2);												//行别
					b = addStr2ByteArr(b, StringUtils.rightPad(CommonUtils.null2String(body.getBankCode()), 14, CHAR_SPACE), 14);		//行号
					b = addStr2ByteArr(b, StringUtils.rightPad(CommonUtils.null2String(body.getAccount()), 32, CHAR_SPACE), 32);		//账户号
					b = addStr2ByteArr(b, StringUtils.rightPad(CommonUtils.null2String(body.getAccountName()), 120, CHAR_SPACE), 120);	//收款账户户主名称
					b = addStr2ByteArr(b, StringUtils.rightPad("", 120, CHAR_SPACE), 120);												//附言
					b = addStr2ByteArr(b, StringUtils.rightPad(CommonUtils.null2String(body.getBankRespCode()), 8, CHAR_SPACE), 8);		//银行返回码
					b = addStr2ByteArr(b, StringUtils.rightPad("", 100 + 120, CHAR_SPACE), 220);										//附加信息, 100个空格, 银行返回附言 120个空格
					b = addStr2ByteArr(b, "\n", 1);
				}
			}
			
			
			try {
				String fileName = head.getFileName() + ".txt";
				RemoteModelResult<UploadFile> upf = this.fastDFSApi.uploadFile(new ByteArrayInputStream(b),fileName);
				if(upf.isSuccess() && upf.getModel() != null){
					Annex annex = new Annex(upf.getModel(),AnnexEnum.annex_type_txt.getIntV(),head.getId(),head.getProjectId());
					this.annexMapper.insertAnnex(annex);
					msgMap.setMessage("金融联头文件生成: [文件生成完成 ] . ");
					msgMap.setFlag(MessageMap.INFOR_SUCCESS);
					
					//批次号+1 
					String batchNo = head.getFileName().substring(3);  // 文件名: 三位企业代码 + 00 + 批次号  共八位
					batchNo = head.getFileName().substring(0, 3).concat(StringUtils.leftPad((CommonUtils.null2Int(batchNo) + 1) + "", 5, '0'));
					head.setFileName(batchNo);
					this.tBcJrlHeadMapper.update(head);
					
					//设置提出时间
					TBcCollectionTotal total = this.tBcCollectionMapper.findById(head.getTotalId());
					if(null != total){
						total.setCollectionTime(new Date());
						this.tBcCollectionMapper.update(total);
					}
					
				}else{
					msgMap.setMessage("金融联头文件生成: [文件上传失败] , 生成失败.");
					logger.warn("金融联头文件生成: [文件上传失败] , 生成失败.");
				}
				return new BaseDto(msgMap);
			} catch (Exception e) {
				logger.error("金融联头文件生成: [文件上传失败] , 生成失败.");
				msgMap.setMessage("金融联头文件生成: [文件上传失败] , 生成失败.");
				throw new ECPBusinessException(ReturnCode.WY_BUSINESS_FILE_UPLOAD_FAILED);
			}
		}
		return new BaseDto(msgMap);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void genColl(String companyId, TBcProject project, String totalId, Integer isStopCommon) {
		
		//生成托收数据:

		//项目的托收方式 银联/金融联
		int collectionType = getCollectionType(project);
		if(collectionType  < 0){	//不含未开启的托收
			logger.warn("托收数据生成: 托收数据生成失败, 原因:[当前项目未开启托收/收费项全未开启托收]. 数据:{}.", (project == null) ? "项目数据为空" : project.toString());
			return ;
		}
		//判断本月的collection_total是否生成,若未生成,则生成一张新的
		TBcCollectionTotal total = this.tBcCollectionMapper.findCurrTotal(project.getProjectId(),collectionType);
		if(total == null){
			total = new TBcCollectionTotal();
			total.setId(CommonUtils.getUUID());
			total.setProjectId(project.getProjectId());
			total.setCollectionType(collectionType);
			total.setFamilyCount((collectionType == CollectionEnum.type_jrl.getV()) ? project.getJrlCount() : project.getUnionCount());
			total.setTotalAmount(0.0);   //总金额
			total.setCollectionStatus(CollectionEnum.colleciton_status_wait.getV());	//待托收
			total.setCollectionTime(null);
			total.setBackTime(null);
			total.setCreateId(AUTO_GENER);
			total.setCreateTime(new Date());
			total.setModifyId(AUTO_GENER);
			total.setModifyTime(new Date());
			this.tBcCollectionMapper.insert(total);
		}
		
		//找出本月的托收头文件
		TBcUnionCollectionHead unionHead = null;
		TBcJrlHead jrlHead = null;
		String headId = null;
		if(collectionType == CollectionEnum.type_union.getV()){
			TBcUnionCollectionHead paramObj = new TBcUnionCollectionHead();
			paramObj.setTotalId(total.getId());
			unionHead = getUnionHead(paramObj,project);
			headId = unionHead.getId();
		}else if(collectionType == CollectionEnum.type_jrl.getV()){
			TBcJrlHead paramObj = new TBcJrlHead();
			paramObj.setTotalId(total.getId());
			jrlHead = getJrlHead(paramObj,project);
			headId = jrlHead.getId();
		}else{
			logger.warn("托收数据生成: 托收数据生成失败, 原因:[当前托收类型不为银联/金融联]. 数据:{}.", collectionType);
			return;
		}
		
		TBsChargeBillTotal totalBill = this.tBsChargeBillTotalMapper.selectById(totalId);
		if(CommonUtils.isEmpty(totalBill)){
			logger.warn("托收数据生成: 托收数据生成失败, 原因:[当前项目未找到可供生成的总账单]. 数据:{}.", project.toString());
			return;
		}
		
		//找出项目下的所有开通托收的建筑
		List<TBcCollection> collections = this.collectionMapper.findByBillTotalId(totalId,totalBill.getProjectId());
		if(CommonUtils.isEmpty(collections)){
			//未找到可托收的数据
			logger.warn("托收数据生成: 托收数据生成失败, 原因:[当前项目未找到开通托收的建筑]. 数据:{}.", project.toString());
			return;
		}
		
		List<TBcUnionCollectionBody> insertUnionBodies = new ArrayList<TBcUnionCollectionBody>();
		List<TBcJrlBody> insertJrlBodies = new ArrayList<TBcJrlBody>();
		int i = 1;
		for(TBcCollection c : collections){
			//对每户进行账单抽取
			double owedAmount = 0.0;
			TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(c.getRelateBuildingCode(), totalBill.getType());
			if(null == account){
				logger.warn("托收数据生成: 部分托收数据生成失败, 原因:[当前建筑未找到可供生成的账户]. 建筑code:{}, 建筑名:{}.", c.getRelateBuildingCode(), c.getRelateBuildingFullName());
				continue;
			}
			if(CommonUtils.null2Double(account.getAccountBalance()) >= 0){
				logger.warn("托收数据生成: 部分托收数据生成失败, 原因:[本账户余额非负数, 不需要进行托收]. 建筑code:{}, 建筑名:{}.", c.getRelateBuildingCode(), c.getRelateBuildingFullName());
				continue;
			}
			
			owedAmount = CommonUtils.getScaleNumber(Math.abs(account.getAccountBalance()),2);
			
			if(owedAmount > 0){
				
				if(collectionType == CollectionEnum.type_union.getV()){
					//银联
					//构建成银联托收子文件数据
					genUnionBody(headId,account,c,i,owedAmount,project);
				}else if(collectionType == CollectionEnum.type_jrl.getV()){
					//金融联
					//构建成金融联托收子文件数据
					genJrlBody(headId,account,c,i,owedAmount);
				}
				
			}
			i++;
		}
		
		Double tta = 0.0;
		Integer familyCount = 0;
		if(collectionType == CollectionEnum.type_union.getV()){
			//聚合并更新头文件
			Integer count = this.tBcUnionCollectionBodyMapper.countByHeadId(headId); 
			Double tAmount = CommonUtils.null2Double(this.tBcUnionCollectionBodyMapper.sumAmountByHeadId(headId)); 
			unionHead.setTotalCount(count);
			unionHead.setTotalAmount(tAmount);
			
			this.tBcUnionCollectionHeadMapper.update(unionHead);
			
			tta = CommonUtils.null2Double(this.tBcUnionCollectionBodyMapper.sumTotalAmountByTotalId(total.getId()));
			familyCount = this.tBcUnionBackBodyMapper.countTotalCountByTotalId(total.getId()); 
			
		}else if(collectionType == CollectionEnum.type_jrl.getV()){
			Integer count = this.tBcJrlBodyMapper.countByHeadIdAndType(jrlHead.getId(), CollectionEnum.jrl_is_collection.getV());
			Double tAmount = this.tBcJrlBodyMapper.sumAmountByHeadIdAndType(jrlHead.getId(),CollectionEnum.jrl_is_collection.getV());
			
			jrlHead.setTotalAmount(tAmount);
			jrlHead.setTotalCount(count);
			this.tBcJrlHeadMapper.update(jrlHead);
			
			tta = CommonUtils.null2Double(this.tBcJrlBodyMapper.sumTotalAmountByTotalId(total.getId()));
			familyCount = this.tBcJrlBodyMapper.countTotalCountByTotalId(total.getId());
		}
		
		total.setFamilyCount(familyCount);
		total.setTotalAmount(tta);
		this.tBcCollectionMapper.update(total);
		
		//把之前的都置为完成状态
		this.tBcCollectionMapper.completeLastTotal();
	}
	
	private void send2mq(String companyId, List<?> objs,String clazzName){
		MqEntity e = new MqEntity();
		e.setCompanyId(companyId);
		e.setData(objs);
		e.setSupAttr(clazzName);
		this.amqpTemplate.convertAndSend(route_key_coll_body_batch_insert, e);
		objs.clear();
		
		//投递至CollBodiesListener 进行批量插入
	}
	
	/**
	 * @TODO 生成银联子文件
	 * @return
	 */
	private void genUnionBody(String headId ,
						      TBsAssetAccount account , 
						      TBcCollection c,
						      int batchNo,
						      double owedAmount,
						      TBcProject project){
		TBcUnionCollectionBody param = new TBcUnionCollectionBody();
		param.setBuildingCode(account.getBuildingCode());
		param.setHeadId(headId);
		List<TBcUnionCollectionBody> bodies = this.tBcUnionBackBodyMapper.findByObj(param); 
		if(CommonUtils.isEmpty(bodies)){
			TBcUnionCollectionBody body = new TBcUnionCollectionBody();
			TBankInfo bankInfo = this.tBankInfoMapper.selectByPrimaryKey(c.getCreateBank());
			if(null == bankInfo)
				return;
			body.setId(CommonUtils.getUUID());
			body.setHeadId(headId);
			body.setBillId(account.getId());
			body.setBuildingCode(c.getRelateBuildingCode());
			body.setBuildingFullName(c.getRelateBuildingFullName());
			body.setProjectId(account.getProjectId());
			body.setOrderNo(StringUtils.leftPad(c.getContractNo(), 16, "0"));   //TODO 20180122 更改为合约号
			body.setBankNo(bankInfo.getBankNo());
			body.setIsCard(CollectionEnum.type_is_card.getV());
			body.setBankCardNo(c.getCardNum());
			body.setIdCardMasterName(c.getCustName());
			//个人客户拿出来, 0为身份证 , 1为护照 转换成 托收使用的 01: 身份证, 03: 护照
			body.setIdCardType(( 0 == CommonUtils.null2Int(c.getCardType()) ) ? CollectionEnum.id_card_type_is_id.getStringV() : CollectionEnum.id_card_type_is_port.getStringV());
			body.setIdCardNo(c.getCardNo());
			
			body.setPrivateZone(CommonUtils.null2String(project.getUnionPrivateZone()));
			body.setUse("0");
			body.setCreateId(AUTO_GENER);
			body.setModifyId(AUTO_GENER);
			body.setCreateTime(new Date());
			body.setModifyTime(new Date());
			
			setUnionAmount(body, account.getType(), owedAmount);
			
			body.setAmount(owedAmount); //本期总账单 , 第一条数据 就加上手续费   之后的费种都不用再加手续费
			body.setServiceAmount(1.0);
			this.tBcUnionCollectionBodyMapper.insert(body); 
		}else{
			TBcUnionCollectionBody body = bodies.get(0);
			setUnionAmount(body, account.getType(), owedAmount);
			body.setAmount(CommonUtils.null2Double(body.getAmount()) + owedAmount);
			this.tBcUnionCollectionBodyMapper.update(body);
		}
	}
	
	private void setUnionAmount(TBcUnionCollectionBody collBody, Integer type , double amount){
			if(collBody != null){
				switch (type) {
					case 1:
						collBody.setWyAmount(amount);
						break;
					case 2:
						collBody.setBtAmount(amount);
						break;
					case 3:
						collBody.setWaterAmount(amount);
						break;
					case 4:
						collBody.setElectAmount(amount);
						break;
					default:
						break;
				}
			}
	}
	
	private void setJrlAmount(TBcJrlBody body,  Integer type , double amount){
		if(body != null){
			switch (type) {
				case 1:
					body.setWyAmount(amount);
					break;
				case 2:
					body.setBtAmount(amount);
					break;
				case 3:
					body.setWaterAmount(amount);
					break;
				case 4:
					body.setElectAmount(amount);
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * @TODO 生成金融联子文件
	 * @return
	 */
	private void genJrlBody(String headId ,
							      TBsAssetAccount account , 
							      TBcCollection c, 
							      int batchNo,
							      double owedAmount){
		
		//判断是否已经存在子文件
		TBcJrlBody param = new TBcJrlBody();
		param.setBuildingCode(account.getBuildingCode());
		param.setHeadId(headId);
		List<TBcJrlBody> bodies = this.tBcJrlBodyMapper.findByObj(param);
		TBankInfo info = this.tBankInfoMapper.selectByPrimaryKey(c.getCreateBank());
		if(info == null){
			return;
		}
		if(CommonUtils.isEmpty(bodies)){
			TBcJrlBody body = new TBcJrlBody();
			body.setId(CommonUtils.getUUID());
			body.setHeadId(headId);
			body.setBillId(account.getId());
			body.setBuildingCode(c.getRelateBuildingCode());
			body.setBuildingFullName(c.getRelateBuildingFullName());
			body.setProjectId(account.getProjectId());
			body.setType(CollectionEnum.jrl_is_collection.getV());
			body.setDetailNo(batchNo+"");
//			body.setAgreementNo(Constants.STR_EMPTY);
			body.setAgreementNo(c.getContractNo());		//合约号. 后台自动生成
			body.setAmount(owedAmount);
			body.setBankType( (CollectionEnum.jrl_is_local.getV() == info.getIsLocal()) ? info.getBankType() : "00");
			body.setBankCode( (CollectionEnum.jrl_is_local.getV() == info.getIsLocal()) ? Constants.STR_EMPTY : info.getBankNo());
			
			body.setBankRespCode(JRL_RESP_CODE);
			body.setAccount(c.getCardNum()); 
			body.setAccountName(c.getCustName());
			body.setCreateId(AUTO_GENER);
			body.setCreateTime(new Date());
			body.setModifyId(AUTO_GENER);
			body.setModifyTime(new Date());
			
			setJrlAmount(body, account.getType(), owedAmount);
			
			this.tBcJrlBodyMapper.insert(body); 
			
//			insertJrlBodies.add(body);
		}else{
			TBcJrlBody body = bodies.get(0);
			setJrlAmount(body, account.getType(), owedAmount);
			body.setAmount(CommonUtils.null2Double(body.getAmount()) + owedAmount);
			this.tBcJrlBodyMapper.update(body);
		}
	}
	
	/**
	 * @TODO 获取金融联头文件
	 * @param paramHead
	 * @param project
	 * @return
	 */
	private TBcJrlHead getJrlHead(TBcJrlHead paramHead , TBcProject project){
		List<TBcJrlHead> heads = this.tBcJrlHeadMapper.findByObj(paramHead);
		if(CommonUtils.isEmpty(heads)){
			
			//获取本物业公司的总共批次号
			String fileName = this.tBcJrlHeadMapper.findBatchNo(project.getProjectId());
			if(CommonUtils.isEmpty(fileName)){
				fileName = "000";
			}
			String batchNo = project.getEnterpriseNo().substring(project.getEnterpriseNo().length() - 3)
							.concat("00")
							.concat(StringUtils.leftPad((CommonUtils.null2Int(fileName) + 1) + "", 3,'0'));
			paramHead.setId(CommonUtils.getUUID());
			paramHead.setCollId(paramHead.getId());
			paramHead.setType(CollectionEnum.jrl_is_collection.getV());
			paramHead.setProjectId(project.getProjectId());
			paramHead.setProjectName(project.getProjectName());
			paramHead.setBusinessType(CollectionEnum.jrl_business_type.getStringV());
			paramHead.setEnterpriseNo(project.getEnterpriseNo());
			paramHead.setBankNo(project.getJrlWaitBankNo());
			paramHead.setFeeNo(CollectionEnum.jrl_fee_no.getStringV());		//CNY
			paramHead.setFileName(batchNo);		//企业号后三位 + 00 + 批次号
			paramHead.setFeeType(CollectionEnum.jrl_fee_type_RMB.getStringV());
			paramHead.setTotalCount(project.getJrlCount());
			paramHead.setTotalAmount(0.0);
			paramHead.setCompleteCount(0);
			paramHead.setCompleteAmount(0.0);
			paramHead.setBusinessNo(CollectionEnum.jrl_business_no_RMB.getStringV());	//30401
			paramHead.setRequestDate(new Date());
			paramHead.setPayDate(new Date());
			paramHead.setResponseDate(null);
			paramHead.setChargeAccount(null);
			paramHead.setChargeAccountName(null);
			paramHead.setCreateId(AUTO_GENER);
			paramHead.setCreateTime(new Date());
			paramHead.setModifyId(AUTO_GENER);
			paramHead.setModifyTime(new Date());
			this.tBcJrlHeadMapper.insert(paramHead);
			return paramHead;
		}else{
			return heads.get(0);
		}
	}
	
	/**
	 * @TODO 获取银联头文件
	 * @param paramObj
	 * @param project
	 * @return
	 */
	private TBcUnionCollectionHead getUnionHead(TBcUnionCollectionHead paramObj,TBcProject project){
		List<TBcUnionCollectionHead> heads = this.tBcUnionCollectionHeadMapper.findAllByObj(paramObj);
		if(CommonUtils.isEmpty(heads)){
			paramObj.setId(CommonUtils.getUUID());
			paramObj.setProjectId(project.getProjectId());
			paramObj.setProjectName(project.getProjectName());
			paramObj.setShopNum(project.getShopNum());
			paramObj.setTotalAmount(0.0);
			paramObj.setTotalCount(project.getUnionCount());
			paramObj.setType(0);
			paramObj.setCreateId(AUTO_GENER);
			paramObj.setModifyId(AUTO_GENER);
			paramObj.setCreateTime(new Date());
			paramObj.setModifyTime(new Date());
			this.tBcUnionCollectionHeadMapper.insert(paramObj);
			return paramObj;
		}else{
			return heads.get(0);
		}
	}
	
	private int getCollectionType(TBcProject project){
		if(null == project)
			return -1;
		if(project.getWyStatus() == CollectionEnum.status_on.getV() && project.getWyType() != CollectionEnum.type_off.getV()){
			return project.getWyType();
		}else if(project.getBtStatus() == CollectionEnum.status_on.getV() && project.getBtType() != CollectionEnum.type_off.getV()){
			return project.getBtType();
		}else if(project.getWaterStatus() == CollectionEnum.status_on.getV() && project.getWaterType() != CollectionEnum.type_off.getV()){
			return project.getWaterType();
		}else if(project.getElectStatus() == CollectionEnum.status_on.getV() && project.getElectType() != CollectionEnum.type_off.getV()){
			return project.getElectType();
		}else{
			return -1;
		}
	
	}
	
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("unchecked")
	@Override
	public void batchInsertBodies(String companyId, List<?> list) {
		
		if(CommonUtils.isNotEmpty(list)){
			String clazzName = list.get(0).getClass().getName();
		
			if(CommonUtils.isEquals(clazzName, TBcUnionCollectionBody.class.getName())){
				
				this.tBcUnionCollectionBodyMapper.batchInsert((ArrayList<TBcUnionCollectionBody>) list);
				
			}else if(CommonUtils.isEquals(clazzName, TBcJrlBody.class.getName())){
				
				this.tBcJrlBodyMapper.batchInsert((ArrayList<TBcJrlBody>) list);
				
			}
			
		}
		
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap importFile(String companyId, String fileContent, String fileName , Integer flag , String totalId, String projectId , String userId) {
		MessageMap msgMap = null;
		
		if(CommonUtils.isEmpty(fileContent)){
			return new MessageMap(MessageMap.INFOR_WARNING,"上传文件为空,导入完成.");
		}else{
			TBcProject project = this.tBcProjectMapper.findByProjectId(projectId);

			//根据totalId判断当前total总单的状态.
			TBcCollectionTotal total = this.tBcCollectionMapper.findById(totalId);
			if(total == null || total.getCollectionStatus() != CollectionEnum.collection_status_wait_back.getV()){
				logger.warn("{}回盘文件导入 : 总单为空,或当前总单的状态不为待回盘状态. 总单id:{}, 总单数据:{}.",(flag == 0) ? "银联":"金融联", totalId , (null == total) ? "" : total.toString());
				return new MessageMap(MessageMap.INFOR_WARNING,"回盘文件导入: 当前总单不存在, 或当前不为待回盘状态, 此次回盘失败. ");
			}
			
			String s = "";
			int completeCount = 0;
			double completeAmount = 0.0;
			MqEntity me = new MqEntity();
			me.setCompanyId(companyId);
			me.setOpr(flag);
			me.setProjectId(projectId);
			me.setUserId(userId);
			
			if(flag == 0){
				//银联
				//s = 头文件的标识
				//判断该文件是否导入过. 然后开始解析文件, 写入数据 , 并计入账户
				s = CommonUtils.null2String(fileName);	//银联回盘文件当做唯一标识
				if(!s.toUpperCase().endsWith("P.TXT")){
					logger.warn("银联回盘文件导入 : 回盘文件后缀名不正确,导入失败. 文件名:{}.",fileName);
					return new MessageMap(MessageMap.INFOR_WARNING,"回盘文件后缀名不正确,导入失败.");
				}
				String unionFileNames = (String) this.springRedisTools.getByKey(s);
				TBcUnionBackHead paramHead = new TBcUnionBackHead();
				paramHead.setFileName(s);
				List<TBcUnionBackHead> heads = this.tBcUnionBackHeadMapper.findByObj(paramHead);
				if(!heads.isEmpty()  || (unionFileNames != null &&  unionFileNames.contains(s))){
					logger.warn("银联回盘文件导入 : 当前文件已经导入过,无法再次导入, 文件名:{}.",fileName);
					msgMap = new MessageMap(MessageMap.INFOR_WARNING,"当前银联回盘文件已经导入过,无法再次导入.");
					return msgMap;
				}
				
				//开始解析
				String[] strs = fileContent.split("\n");
				if(CommonUtils.isNotEmpty(strs)){
					String lineStr = "";
					List<TBcUnionBackBody> bodies = new ArrayList<TBcUnionBackBody>();
					TBcUnionBackHead head = new TBcUnionBackHead();
					head.setId(CommonUtils.getUUID());
					head.setTotalId(totalId);
					head.setFileName(s);
					head.setType(CollectionEnum.type_union.getV());
					boolean isTrueFormatter = false;

					TBcUnionCollectionHead cHead = this.tBcUnionCollectionHeadMapper.findTheEarliestHeadByTotalId(totalId);
					
					String cHeadId = (null != cHead) ? cHead.getId() : null;
					
					int totalCount = strs.length - 1;		//总户数
					double totalAmount = 0.0;				//总金额
					
					for(int i = 0 ; i < strs.length; i++){
						//去除最后一行签名
						lineStr = strs[i];
						
						if(i < strs.length - 1){
							TBcUnionBackBody body = str2UnionBody(lineStr, head.getId(),projectId,userId);
							if(null != body){
								
								TBcUnionCollectionBody cBody = this.tBcUnionCollectionBodyMapper.findByHeadIdAndOrderId(cHeadId, body.getOrderNo());
								if(null != cBody){
									body.setBuildingCode(cBody.getBuildingCode());
									body.setBuildingFullName(cBody.getBuildingFullName());
									body.setWyAmount(cBody.getWyAmount());
									body.setBtAmount(cBody.getBtAmount());
									body.setWaterAmount(cBody.getWaterAmount());
									body.setElectAmount(cBody.getElectAmount());
									body.setCustName(cBody.getIdCardMasterName());
								}
								bodies.add(body);
								
								totalAmount += CommonUtils.null2Double(body.getAmount());	//总金额
								
								if(CommonUtils.isEquals(body.getTradeStatus(), CollectionEnum.union_back_file_trade_status_success.getStringV())
										&& CommonUtils.isEquals(body.getRespCode(), CollectionEnum.union_back_file_resp_success.getStringV())
										){
									completeCount ++;
									completeAmount += CommonUtils.null2Double(body.getAmount());
								}
							}
						}else{
//							最后一行
							isTrueFormatter = str2UnionHead(strs[i],head,userId);
						}
					}
					
					if(isTrueFormatter){
						//注入到unionHead  最后一行无法读取出
						head.setTotalCount(totalCount);	//总笔数
						head.setTotalAmount(totalAmount);	//总金额
						head.setCompleteCount(completeCount);	//完成笔数
						head.setCompleteAmount(completeAmount);	//完成金额
						
						//更新到total, 并更新is_wait_back字段,表示已经回盘
						updateTotalInfo(completeCount, completeAmount, totalId, CollectionEnum.type_union.getV());
						
						//更新到托收头文件
						updateCollectionHeadInfo(cHeadId,completeCount, completeAmount,CollectionEnum.type_union.getV());
						
						//开始入库
						this.tBcUnionBackHeadMapper.insert(head);
						this.tBcUnionBackBodyMapper.batchInsert(bodies);
						this.springRedisTools.addData(s, s, 300, TimeUnit.SECONDS);
						
						
						//投递至消息队列 BackListener 进行抵扣
						logger.info("银联回盘文件导入 : 开始组装数据到消息队列, 进行账户数据计算. ");
						me.setData(bodies);
						me.setSupAttr(JSON.toJSONString(head));
						this.amqpTemplate.convertAndSend(route_key_back_to_account, me);
						logger.info("银联回盘文件导入 : 数据组装完成, 发往消息队列 进行账户数据计算完成. routekey:{}, 数据:{}", route_key_back_to_account, me.toString());
						
						logger.info("银联回盘文件导入 : 文件读取完成, 回盘成功户数: {}户, 回盘成功金额: {}元, 开始异步进行账户抵扣. ", completeCount, completeAmount);
						return new MessageMap(MessageMap.INFOR_SUCCESS,"文件读取完成, 回盘成功户数:  [" + completeCount + "], 回盘成功金额:  [" + completeAmount + "] ,开始异步进行账户抵扣, 请稍后查看. ");
					}else{
						logger.warn("银联回盘文件导入 : 回盘文件格式不正确 , 银联回盘文件导入失败. ");
						return new MessageMap(MessageMap.INFOR_WARNING,"文件格式不正确, 银联回盘文件导入失败.");
					}
				}else{
					logger.warn("银联回盘文件导入 : 传入回盘文件数据为空, 银联回盘文件导入失败. ");
					return new MessageMap(MessageMap.INFOR_WARNING,"传入文件数据为空, 银联回盘文件导入失败. ");
				}
			}else{
				//金融联
				//s = 头文件的标识
				String[] strs = fileContent.split("\n");
				if(CommonUtils.isEmpty(strs)){
					logger.warn("金融联回盘文件导入 : 文件数据为空, 导入失败.");
					return new MessageMap(MessageMap.INFOR_WARNING,"传入金融联回盘文件为空, 导入失败. ");
				}
				
				TBcJrlHead paramHead = new TBcJrlHead();
				TBcJrlHead head = new TBcJrlHead();
				List<TBcJrlBody> bodies = new ArrayList<TBcJrlBody>();
				String headId = CommonUtils.getUUID();
				head.setProjectId(projectId);
				head.setTotalId(totalId);
				head.setProjectName(project.getProjectName());
				head.setId(headId);
				head.setType(CollectionEnum.jrl_is_back.getV());
				boolean isTrueFormatter = false;
				
				String cHeadId = null;
				for(int i = 0 ; i < strs.length ; i++){
					
					String lineStr = strs[i];
					if(i == 0){
						//首行,头文件
						isTrueFormatter = str2JrlHead(lineStr, head,userId);
						
						s = head.getFileName();
						paramHead.setFileName(head.getFileName());
						paramHead.setType(CollectionEnum.jrl_is_back.getV());
						List<TBcJrlHead> heads = this.tBcJrlHeadMapper.findByObj(paramHead);
						String jrlFileNames =  (String) this.springRedisTools.getByKey(s);
						
						if(!heads.isEmpty() || 
								(CommonUtils.isNotEmpty(jrlFileNames) && jrlFileNames.contains(head.getFileName())) ){
							logger.warn("金融联回盘文件导入 : 当前金融联回盘文件已经导入过, 无法重复导入. 文件名:{}.", head.getFileName());
							return new MessageMap(MessageMap.INFOR_WARNING,"当前金融联回盘文件已经导入过,无法继续导入.");
						}
						
						if(isTrueFormatter){
							TBcJrlHead cHead = this.tBcJrlHeadMapper.findByTotalIdAndType(head.getTotalId(), CollectionEnum.jrl_is_collection.getV());
							cHeadId = (null != cHead) ? cHead.getId() : Constants.STR_EMPTY; 
						}
						
					}else{
						TBcJrlBody body = str2JrlBody(lineStr,headId,projectId,userId);
						
						if(body != null){
							TBcJrlBody cBody = this.tBcJrlBodyMapper.findByHeadIdAndAgreementNo(cHeadId, body.getAgreementNo()); 
							if(cBody != null){
								body.setBuildingCode(cBody.getBuildingCode());
								body.setBuildingFullName(cBody.getBuildingFullName());
								body.setBillId(cBody.getBillId());
								body.setWyAmount(cBody.getWyAmount());
								body.setBtAmount(cBody.getBtAmount());
								body.setWaterAmount(cBody.getWaterAmount());
								body.setElectAmount(cBody.getElectAmount());
								body.setAccountName(cBody.getAccountName());
							}
							
							bodies.add(body);
							if(CommonUtils.isEquals(body.getBankRespCode(), CollectionEnum.jrl_back_file_trade_status_success.getStringV())){
								completeCount ++;
								completeAmount += CommonUtils.null2Double(body.getAmount());
							}
						}
					}
				}
				if(isTrueFormatter){
					
					//更新到total , 并更新is_wait_back字段,表示已经回盘
					updateTotalInfo(completeCount, completeAmount, totalId,CollectionEnum.type_jrl.getV());
					
					//更新到托收头文件
					updateCollectionHeadInfo(cHeadId,completeCount, completeAmount,CollectionEnum.type_jrl.getV());
					
					this.springRedisTools.addData(s,s, 300, TimeUnit.SECONDS);
					this.tBcJrlHeadMapper.insert(head);
					this.tBcJrlBodyMapper.batchInsert(bodies);
					
					//投递至消息队列 BackListener
					logger.info("银联回盘文件导入 : 开始组装数据到消息队列, 进行账户数据计算. ");
					me.setData(bodies);
					me.setSupAttr(JSON.toJSONString(head));
					this.amqpTemplate.convertAndSend(route_key_back_to_account, me);
					logger.info("银联回盘文件导入 : 数据组装完成, 发往消息队列 进行账户数据计算完成. routekey:{}, 数据:{}", route_key_back_to_account, me.toString());
					
					logger.info("金融联回盘文件导入 : 金融联回盘文件读取完成 , 银行扣取成功户数: {}户, 扣取成功金额: {}元. 开始异步进行账户抵扣. ",completeCount , completeAmount);
					return new MessageMap(MessageMap.INFOR_SUCCESS,"文件读取完成, 回盘成功户数:  [" + completeCount + "], 回盘成功金额: [" + completeAmount + "] ,开始异步进行账户抵扣, 请稍后查看. ");
				}else{
					logger.warn("金融联回盘文件导入 : 文件格式不正确, 金融联回盘文件导入失败. ");
					return new MessageMap(MessageMap.INFOR_WARNING,"文件格式不正确, 金融联回盘文件导入失败.");
				}
			}
		}
	}
	
	/**
	 * @TODO 更新数据到Total
	 * @param completeCount
	 * @param completeAmout
	 * @param totalId
	 */
	private void updateTotalInfo(int completeCount , double completeAmout, String totalId, Integer type){
		TBcCollectionTotal total = this.tBcCollectionMapper.findById(totalId);
		if(null != totalId){
			total.setCompleteAmount(CommonUtils.getSum(total.getCompleteAmount(),completeAmout));
			total.setCompleteCount(((Double)CommonUtils.getSum(total.getCompleteCount(),completeCount)).intValue());
			total.setIsWaitBack(Constants.STR_NO);
			if(total.getCompleteCount() == total.getFamilyCount()){
				total.setCollectionStatus(CollectionEnum.collection_status_complete.getV());
			}
			this.tBcCollectionMapper.update(total);
		}
//		this.tBcCollectionMapper.updateCountAndAmountById(completeCount, completeAmout, totalId , type);  
	}
	
	
	private void updateCollectionHeadInfo(String headId,int completeCount , double completeAmout, Integer type){
		if(null == headId) return;
		if(type == CollectionEnum.type_jrl.getV()){
			TBcJrlHead param = new TBcJrlHead();
			param.setId(headId);
			List<TBcJrlHead> heads = this.tBcJrlHeadMapper.findByObj(param);
			if(CommonUtils.isEmpty(heads) ||  CommonUtils.isEmpty(heads.get(0))) return;
			
			TBcJrlHead head = heads.get(0);
			head.setCompleteAmount(CommonUtils.getSum(head.getCompleteAmount(),completeAmout));
			head.setCompleteCount(((Double) CommonUtils.getSum(head.getCompleteCount(),completeCount)).intValue());
			
			this.tBcJrlHeadMapper.update(head);
		}else{
			//银联头文件内 无完成金额与完成户数
			/*TBcUnionCollectionHead param = new TBcUnionCollectionHead();
			param.setId(headId);
			List<TBcUnionCollectionHead> heads = this.tBcUnionCollectionHeadMapper.findAllByObj(param);
			if(CommonUtils.isEmpty(heads) || CommonUtils.isEmpty(heads.get(0))) return;
			
			TBcUnionCollectionHead head = heads.get(0);
			
			*/
		}
	}
	
	
	/**
	 * @TODO 字符串转换成金融联回盘子文件
	 * @param headId
	 * @param projectId
	 * @return
	 */
	private TBcJrlBody str2JrlBody(String str, String headId,String projectId, String userId) {
		TBcJrlBody body = null;
		if(CommonUtils.isNotEmpty(str) && str.length() < 572){
			
			byte[] b;
			try {
				b = str.getBytes("GBK");
			} catch (UnsupportedEncodingException e) {
				b = null;
			}
			
			//TODO 在托收文件生成导出的时候, 已经进行了过滤, 过滤掉回盘成功的文件, 所以现在能回盘的都是之前尚未回盘成功的数据. 
			body = new TBcJrlBody();
			
			body.setId(CommonUtils.getUUID());
			body.setHeadId(headId);
			body.setProjectId(projectId);
			body.setDetailNo(byte2Str(b, 0, 8));	//明细序号
			body.setAgreementNo(byte2Str(b, 8, 40)); //协议编号
			body.setAmount(CommonUtils.null2Double(byte2Str(b, 40, 56)));	//交易金额
			body.setBankType(byte2Str(b, 56, 58));	//行别
			body.setBankCode(byte2Str(b, 58, 72));	//行号
			body.setAccount(byte2Str(b, 72, 104));	//账号
			body.setAccountName(byte2Str(b, 104, 224));	//户名
			body.setPs(byte2Str(b, 224, 344));	//附言
			body.setBankRespCode(byte2Str(b, 344, 352));	//银行返回码
			body.setRemark(byte2Str(b, 352, 452));	//附加信息
			body.setBankRespPs(byte2Str(b, 452, 572));	//银行返回附言
			body.setCreateId(userId);
			body.setModifyId(userId);
			body.setCreateTime(new Date());
			body.setModifyTime(new Date());
			body.setType(CollectionEnum.jrl_is_back.getV());
		}
		return body;
	}
	

	/**
	 * @TODO 字符串转换成金融联回盘头文件
	 * @param lineStr
	 * @param head
	 * @return
	 */
	private boolean str2JrlHead(String lineStr, TBcJrlHead head, String userId) {
		if(CommonUtils.isEmpty(lineStr) || lineStr.length() < 276){
			return false;
		}
		byte[] b;
		try {
			b = lineStr.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			b = null;
		}
		head.setBusinessType(byte2Str(b,   0,   3));			//业务类型
		head.setEnterpriseNo(byte2Str(b,   3,  17));			//企业代码
		head.setBankNo(		 byte2Str(b,  17,  31));			//代办银行号
		head.setFeeNo(		 byte2Str(b,  31,  36));			//费项代码
		head.setFileName(	 byte2Str(b,  36,  44));			//交易文件名
		head.setFeeType(	 byte2Str(b,  44,  47));			//币种
		head.setTotalCount(CommonUtils.null2Int(byte2Str(b, 47, 55)));		//总笔数
		head.setTotalAmount(CommonUtils.null2Double(byte2Str(b, 55, 71)));	//总金额
		head.setCompleteCount(CommonUtils.null2Int(byte2Str(b, 71,  79)));	//成功笔数
		head.setCompleteAmount(CommonUtils.null2Double(byte2Str(b, 79, 95)));	//成功金额
		head.setBusinessNo(  byte2Str(b, 95, 100));		//业务编码
		head.setRequestDate( CommonUtils.getDate(byte2Str(b, 100, 108), Constants.STR_YYYYMMDD));	//委托日期
		head.setPayDate( CommonUtils.getDate(byte2Str(b, 108, 116), Constants.STR_YYYYMMDD));		//应划款日期
		head.setResponseDate( CommonUtils.getDate(byte2Str(b, 116, 124), Constants.STR_YYYYMMDD));	//回执日期
		head.setChargeAccount(byte2Str(b, 124, 156));	//代办账号
		head.setChargeAccountName(byte2Str(b, 156, 276));	//代办账户户名
		head.setCreateId(userId);
		head.setCreateTime(new Date());
		head.setModifyId(userId);
		head.setModifyTime(new Date());
		return true;
	}
	
	/**
	 * @TODO 字符串截取
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private String byte2Str(byte[] b , int startIndex , int endIndex){
		if(null == b || b.length < startIndex || b.length < endIndex){
			return Constants.STR_EMPTY;
		}
		try {
			return new String(Arrays.copyOfRange(b, startIndex, endIndex) , "GBK").trim();
		} catch (UnsupportedEncodingException e) {
			return Constants.STR_EMPTY;
		}
	}

	/**
	 * @TODO 行字符串转换成银联回盘文件头
	 * @param head
	 */
	private boolean str2UnionHead(String str, TBcUnionBackHead head, String userId) {
		if(CommonUtils.isEmpty(str) || str.split("\\|").length != 4){
			return false;
		}

		String[] strs = str.split("\\|");
		head.setTotalCount(CommonUtils.null2Int(strs[0]));	//总笔数
		head.setTotalAmount(CommonUtils.null2Double(strs[1]));	//总金额
		head.setCompleteCount(CommonUtils.null2Int(strs[2]));	//完成笔数
//		head.setCompleteAmount(CommonUtils.null2Double(strs[3]));	//完成金额
		head.setCreateId(userId);
		head.setCreateTime(new Date());
		head.setModifyId(userId);
		head.setModifyTime(new Date());
		head.setType(CollectionEnum.type_union.getV());
		return true;
	}

	/**
	 * @TODO 行字符串转换成银联回盘文件体
	 * @param str
	 * @param headId
	 * @return
	 */
	private TBcUnionBackBody str2UnionBody(String str , String headId , String projectId, String userId){
		TBcUnionBackBody body = null;
		if(CommonUtils.isNotEmpty(str)){
			
			String[] strs = str.split("\\|");
			if(CommonUtils.isNotEmpty(strs) && strs.length >= 8){
				int length = strs.length;
				
//				String orderNo = strs[2];
//				body = this.tBcUnionBackBodyMapper.findByHeadIdAndOrderNo(headId,orderNo); 
				
//				if(body == null){  // 上次不存在, 新建一个body, 并设置id
				body = new TBcUnionBackBody();
				body.setId(CommonUtils.getUUID());
				body.setHeadId(headId);			//头表id
				body.setProjectId(projectId);
				body.setCreateId(userId);
				body.setCreateTime(new Date());
//				}
				
				body.setBankNo(strs[0]);		//银行代号
				body.setTradeDate(CommonUtils.getDate(strs[1], Constants.STR_YYYYMMDD));	//交易日期
				body.setOrderNo(strs[2]);		//订单号
				body.setTradeStatus(strs[3]);	//交易状态 1001代扣成功, 其余都为代扣失败
				body.setRespCode(strs[4]);		//响应码 00代扣成功,其余为失败    
				body.setIsBankCard(CommonUtils.null2Int(strs[5]));  //卡折类型 , 0 卡, 1 折
				body.setBankCardNo(strs[6]);	//卡号/折号
				body.setAmount(new BigDecimal(strs[7]).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue());	//卡折类型, 以分为单位
				body.setUse((length >= 9) ? strs[8] : "");
				body.setPrivateZone((length >= 10) ? CommonUtils.null2String(strs[9]).replaceAll("\\r", "") : "");
				body.setModifyId(userId);
				body.setModifyTime(new Date());
			}
		}
		return body;
	}
	
	/**
	 * @TODO 将字符串转换成一定格式的数组,加入到字节数组
	 * @param b
	 * @param str
	 * @return
	 */
	private byte[] addStr2ByteArr(byte[] b , String str, int length){
		if( b == null ){
			b = new byte[0];
		}
		try {
			byte[] strByte = str.getBytes("GBK");
			strByte = ArrayUtils.subarray(strByte, 0, length);
			b = ArrayUtils.addAll(b, strByte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return b;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void backUnionData2Account(String companyId, TBcUnionBackHead head, List<TBcUnionBackBody> bodies) {
		//找到头单
		TBcUnionCollectionHead cHead = this.tBcUnionCollectionHeadMapper.findByTotalId(head.getTotalId()); 
		if(cHead == null){
			logger.warn("银联数据回盘至账户: 未找到对应的托收头单, 执行完成.");
			return;
		}
		if(CollectionUtils.isEmpty(bodies)){
			logger.warn("银联数据回盘至账户: 传入回盘子文件为空, 执行完成");
			return;
		}
		TBcUnionCollectionBody cb = null;
		for(TBcUnionBackBody bb : bodies){
			
			if(!CommonUtils.isEquals(bb.getRespCode(), CollectionEnum.union_back_file_resp_success.getStringV())
					|| !CommonUtils.isEquals(bb.getTradeStatus(), CollectionEnum.union_back_file_trade_status_success.getStringV())
					){
				logger.warn("银联数据回盘至账户:  此笔数据银行托收不成功, 此单无法回盘计算入账户. 数据: {}.", bb.getOrderNo(), bb.toString());
				continue;
			}
			
			cb = this.tBcUnionCollectionBodyMapper.findByHeadIdAndOrderId(cHead.getId(), bb.getOrderNo());
			
			if(cb == null){
				logger.warn("银联数据回盘至账户: 流水号: {} , 未找到对应的托收单, 此单回盘失败. ", bb.getOrderNo());
				continue;
			}
			bb.setWyAmount(CommonUtils.null2Double(cb.getWyAmount()));
			bb.setBtAmount(CommonUtils.null2Double(cb.getBtAmount()));
			bb.setWaterAmount(CommonUtils.null2Double(cb.getWaterAmount()));
			bb.setElectAmount(CommonUtils.null2Double(cb.getElectAmount()));
			bb.setServiceAmount(CommonUtils.null2Double(cb.getServiceAmount()));
			bb.setBuildingCode(cb.getBuildingCode());
			bb.setBuildingFullName(cb.getBuildingFullName());
			bb.setBillId(cb.getBillId());
			bb.setCustName(cb.getIdCardMasterName());
			this.tBcUnionBackBodyMapper.update(bb);
			
			
			//计算入 本次分账单 , 账户余额 , 以及抵扣欠费信息
			otherBilling(cb.getIdCardMasterName(), bb.getBuildingCode(),CommonUtils.null2Double(bb.getAmount()),cb.getBuildingFullName(),
					bb.getWyAmount(),
					bb.getBtAmount(),
					bb.getWaterAmount(),
					bb.getElectAmount(),
					companyId);
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void backJrlData2Account(String companyId, TBcJrlHead head, List<TBcJrlBody> bodies) {
	
		if(CommonUtils.isEmpty(bodies)){
			logger.warn("金融联数据回盘至账户: 传入回盘子文件为空, 执行完成");
			return;
		}
		
		TBcJrlHead cHead = this.tBcJrlHeadMapper.findByTotalIdAndType(head.getTotalId(),CollectionEnum.jrl_is_collection.getV()); 
		if(null == cHead){
			logger.warn("金融联数据回盘至账户: 未找到对应的托收头文件, 执行完成. 传入数据:{}.", head.toString());
			return;
		}
		
		int rowCount = 1;
		for(TBcJrlBody body : bodies){
			rowCount ++;
			if(body == null){
				logger.warn("金融联数据回盘至账户: 第{}行, 此笔数据为空, 无法回盘.",rowCount);
				continue;
			}
			
			if(!CommonUtils.isEquals(body.getBankRespCode(), CollectionEnum.jrl_back_file_trade_status_success.getStringV())){
				logger.warn("金融联数据回盘至账户: 此笔数据银行托收不成功, 无法回盘计算入本账户 . 数据:{}.", body.toString());
				continue;
			}
			
			//找到对应的托收文件体
			TBcJrlBody cBody = this.tBcJrlBodyMapper.findByHeadIdAndAgreementNo(cHead.getId(), body.getAgreementNo());
			if(cBody == null){
				logger.warn("金融联数据回盘至账户: 此笔数据未找到对应的托收子数据 , 无法回盘 . 数据:{}.", body.toString());
				continue;
			}
			
			body.setWyAmount(CommonUtils.null2Double(cBody.getWyAmount()));
			body.setBtAmount(CommonUtils.null2Double(cBody.getBtAmount()));
			body.setWaterAmount(CommonUtils.null2Double(cBody.getWaterAmount()));
			body.setElectAmount(CommonUtils.null2Double(cBody.getElectAmount()));
			body.setBuildingCode(cBody.getBuildingCode());
			body.setBuildingFullName(cBody.getBuildingFullName());
			body.setBillId(cBody.getBillId());
			body.setModifyTime(new Date());
			
			otherBilling(cBody.getAccountName(), body.getBuildingCode(), CommonUtils.null2Double(body.getAmount()),cBody.getBuildingFullName(),
					body.getWyAmount(),
					body.getBtAmount(),
					body.getWaterAmount(),
					body.getElectAmount(),
					companyId);

			this.tBcJrlBodyMapper.update(body);

		}
			
	}
	
	
	/**
	 * @TODO 将托收成功数据计算入 本次分单 , 账户余额, 以及抵扣欠费数据
	 * @param buildingCode
	 * @param amount
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void otherBilling(String custName, 
							  String buildingCode, 
							  Double amount, 
							  String buildingFullName,
							  Double wyAmount,
							  Double btAmount,
							  Double waterAmount,
							  Double electAmount,
							  String companyId
			){

		logger.info("数据回盘计入账户： 开始计算数据入账户余额。 buildingCode:{},amount:{}",buildingCode, amount);
		//获取本次分账单 , 个人账户  以及 欠费信息
		TBcCollection c = this.collectionMapper.findByBuildingCode(buildingCode);
		if(c == null){
			logger.warn("数据回盘计入账户: 该资产尚未开通托收 , buildingCode:{} ", buildingCode);
			return;
		}
		
		List<String> itemList = CommonUtils.str2List(c.getChargingItems(), Constants.STR_COMMA);
		List<String> items = new ArrayList<String>();
		
		//TODO 极大可能是因为没有找到账单 才导致除了物业管理费 其他的都没有进入循环,  故没有计入  因为分开托收  计费项未修改导致
		if(wyAmount > 0 && itemList.contains(BillingEnum.ACCOUNT_TYPE_WY.getIntV().toString())) items.add(BillingEnum.ACCOUNT_TYPE_WY.getIntV().toString());
		if(btAmount > 0 && itemList.contains(BillingEnum.ACCOUNT_TYPE_BT.getIntV().toString())) items.add(BillingEnum.ACCOUNT_TYPE_BT.getIntV().toString());
		if(waterAmount > 0 && itemList.contains(BillingEnum.ACCOUNT_TYPE_WATER.getIntV().toString())) items.add(BillingEnum.ACCOUNT_TYPE_WATER.getIntV().toString());
		if(electAmount > 0 && itemList.contains(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV().toString())) items.add(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV().toString());
		
		List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findByBuildingCodeAndItems(buildingCode,items);
		List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.findByBuildingCodeAndItems(buildingCode,items);
		if(CommonUtils.isEmpty(accounts)){
			logger.warn("数据回盘计入账户: 未找到 buildingCode:{} 对应的账户信息, 此笔数据回盘失败. ", buildingCode);
			return;
		}
		
		TBsProject project = this.tBsProjectMapper.findCurrentProjectByBuildingCode(buildingCode); 
		boolean isStopCommonFlag = (project != null && project.getCommonStatus() == 1);   // 1为通用账户关闭
		
		double syAmount = 0.0;
		
		TBsPayInfo info = new TBsPayInfo();
		info.setId(CommonUtils.getUUID());
		info.setAssetNo(buildingCode);
		info.setBuildingCode(buildingCode);
		info.setBuildingFullName(buildingFullName);
		info.setCreateId(Constants.STR_AUTO_GENER);
		info.setCreateTime(new Date());
		info.setModifyId(Constants.STR_AUTO_GENER);
		info.setModifyTime(new Date());
		info.setPayerName(custName);
		info.setPayType(5);  			//银联托收类型
		info.setRelationId(CommonUtils.getUUID());
		info.setStatus(0);				//收款
		
		//在这里作出一个调整就是一次交多个房的时候，交易流水号相同，之前不同，导致后面新账户无法实现关联（实际情况一次交多房很少）
		String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9);
		info.setBatchNo(batchNo);


		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据S（房号-- 老账户的buildingCode  每个账户的金额）
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("operateId", "system");
		List<String> buildingCodes = new ArrayList<String>();
		//托收只可以单房操作
		buildingCodes.add(buildingCode);
//		paramMap.put("houseCodes", modifyHousetCode(buildingCodes));
		Map<String, String> amountMap = new HashMap<>();
		amountMap.put("wyAmount", CommonUtils.isEmpty(wyAmount) ? "0.00" : wyAmount.toString() );
		amountMap.put("btAmount", CommonUtils.isEmpty(btAmount) ? "0.00" : btAmount.toString() );
		amountMap.put("waterAmount", CommonUtils.isEmpty(waterAmount) ? "0.00" : waterAmount.toString() );
		amountMap.put("electAmount", CommonUtils.isEmpty(electAmount) ? "0.00" : electAmount.toString() );
		amountMap.put("commonAmount","0.00");//托收是不存在通用账户有钱的
		paramMap.put("amountMap", amountMap);
		paramMap.put("payType",5);
		paramMap.put("batchNo", batchNo);
		mqSender.sendAcChargeDetailForBatchRecharge(paramMap,companyId);

		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据E


		double jnAmount = amount;
		for(int i = 0 ; i < accounts.size() ; i++){
			TBsAssetAccount account = accounts.get(i);
			
			info.setProjectId(account.getProjectId());
			
			boolean flag = false;
			/**
			 * 1 . 欠费流水 是已经计入了账户余额中的, 若发生欠费, 则账户余额为负 , 两边需要分开抵扣 , 而不是用同一笔钱去陆续抵扣
			 */
			if(CommonUtils.isNotEmpty(histories)){
				for(TBsChargeBillHistory history : histories){
					if(jnAmount <= 0) break;
					if(account.getType() == history.getType()){
						flag = true;
						//同一类型的账户与账单
						TBsChargeBillHistory nextHistory = this.tBsChargeBillHistoryMapper.selectNotBillingByObj(history.getBuildingCode(), account.getType());
						
						//抵扣掉违约金
						dkOwed(jnAmount, nextHistory, account);
						
						//按照账单欠费来充值 TODO
						double owedAmount = 0.0;
						if(isStopCommonFlag){
							//本期账单 - 非通用账户抵扣 , 未开启通用账户
							owedAmount = CommonUtils.getScaleNumber(history.getCurrentBillFee() - CommonUtils.null2Double(history.getNoCommonDesummoney()),2);
						}else{
							//本期总账单 - 非通用账户抵扣 - 通用账户抵扣  , 开启通用账户
							owedAmount = CommonUtils.getScaleNumber(history.getCurrentBillFee() - CommonUtils.null2Double(history.getCommonDesummoney()) 
																     - CommonUtils.null2Double(history.getNoCommonDesummoney()),2);
						}
						
						if(jnAmount >= owedAmount){
							jnAmount = CommonUtils.getScaleNumber(jnAmount - owedAmount, 2);
						}else{
							owedAmount = jnAmount;
							jnAmount = 0;
						}
						
						
						if(nextHistory != null){
							double lp = CommonUtils.calKf(nextHistory.getLastBillFee(), 0.0, nextHistory.getLastPayed(), owedAmount);
							nextHistory.setLastPayed(lp);
							this.tBsChargeBillHistoryMapper.updateBillHistory(nextHistory);
						}
						// 本期账单要将托收的金额加入到已扣金额 , 原因: 已经计入了账户余额, 若不加入, 则重新计费的时候, 费用不等.
						// 原账户余额 100 ,本月账单500 , 扣取100 , 账户欠费400
						// 托收400, 账户充值后 余额为0
						// 重新计费: 
						//		先退费 ,退取两次扣款 100 + 400 = 500  余额为500
						// 再重新计费, 本月账单500 , 扣取后 , 余额0. 正解
						double ncd = CommonUtils.calKf(history.getCurrentBillFee(), history.getCommonDesummoney(), history.getNoCommonDesummoney(), owedAmount);
						history.setNoCommonDesummoney(ncd);
						//开始账户添加扣取的钱
						double tsAmount = 0.0;
						if(account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()){
							tsAmount = wyAmount;
						}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()){
							tsAmount = btAmount;
						}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()){
							tsAmount = waterAmount;
						}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()){
							tsAmount = electAmount;
						}
						
						tsAmount = CommonUtils.null2Double(tsAmount);
						
						account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) + tsAmount);
						
						//分单更新账户余额状态
						history.setAccountBalance(account.getAccountBalance());
						this.tBsChargeBillHistoryMapper.updateBillHistory(history);
						
						
						TBsAssetAccountStream stream = new TBsAssetAccountStream(account.getId(), tsAmount, StreamEnum.purpose_pay_by_collection.getV());	//托收充值
						
						this.tBsAssetAccountStreamMapper.singleInsert(stream);
						this.tBsAssetAccountMapper.update(account);
						
						addPayInfo(buildingCode,account.getType(),tsAmount,info);
						break;
					}
					
				}
				if(!flag){ //未找到该账户对应的账单 如重庆的无水电费账单,水电费金额为导入,产生金额
					//直接向账户添加扣取的钱
					Double tsAmount = oprAccount(account, info);
					
					//加入到TBsPayInfo
					addPayInfo(buildingCode,account.getType(),tsAmount,info);
				}
			}else{
				//未找到对应的账单,直接往账户充值
				logger.warn("账户回盘: 未找到buildingCode:{} 资产对应的账单 .",buildingCode);
				Double tsAmount = oprAccount(account, info);
				addPayInfo(buildingCode,account.getType(),tsAmount,info);
			}
		}
		this.tBsPayInfoMapper.insert(info);
	}
	
	private Double oprAccount(TBsAssetAccount account, TBsPayInfo info){
		//开始账户添加扣取的钱
		Double tsAmount = 0.0;
		if(account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()){
			tsAmount = info.getWyAmount();
		}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()){
			tsAmount = info.getBtAmount();
		}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()){
			tsAmount = info.getWaterAmount();
		}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()){
			tsAmount = info.getElectAmount();
		}
		tsAmount = CommonUtils.null2Double(tsAmount);
		
		account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) + tsAmount);
		
		TBsAssetAccountStream stream = new TBsAssetAccountStream(account.getId(), tsAmount, StreamEnum.purpose_pay_by_collection.getV());	//托收充值
		
		this.tBsAssetAccountStreamMapper.singleInsert(stream);
		this.tBsAssetAccountMapper.update(account);
		
		return tsAmount;
	}
	
	private void addPayInfo(String buildingCode, 
			   				Integer accountType,
			   				double amount,
			   				TBsPayInfo info){
		amount = Math.abs(amount);
		
		Double taxRate = this.tBsChargingSchemeMapper.findTaxRate(accountType, buildingCode);
		taxRate = (taxRate == null) ? 0 : taxRate;
		
		double tax = CommonUtils.getTax(amount, taxRate);

		if(accountType == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()) info.setCommonAmount(amount);
		else if(accountType == BillingEnum.ACCOUNT_TYPE_WY.getIntV()) {
			info.setWyAmount(amount);
			info.setWyTax(tax);
		}else if(accountType == BillingEnum.ACCOUNT_TYPE_BT.getIntV()){
			info.setBtAmount(amount);
			info.setBtTax(tax);
		}else if(accountType == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()){
			info.setWaterAmount(amount);
			info.setWaterTax(tax);
		}else if(accountType == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()){
			info.setElectAmount(amount);
			info.setElectTax(tax);
		}
		info.setPayColl(CommonUtils.getScaleNumber(CommonUtils.null2Double(info.getPayColl()) + amount, 2));
	}
	
	
	@SuppressWarnings("unchecked")
	private double dkOwed(double jnAmount, 
						TBsChargeBillHistory nextHistory, 
						TBsAssetAccount account){
		 
		//违约金抵扣
		double dkAmount = jnAmount;
		
		boolean isTrue = (nextHistory != null && account.getType() != BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()); 
		
		List<TBsOwedHistory> histories = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
		if(CommonUtils.isNotEmpty(histories)){
			
			
			List<HashMap> lastOwedHistory = (isTrue) ? JSONObject.parseArray(nextHistory.getLastOwedInfo(), HashMap.class) : null;
		    isTrue = CommonUtils.isNotEmpty(lastOwedHistory);
	    
		    //先抵扣违约金
			for(TBsOwedHistory oh : histories){
			    	
			    	if(null == oh){
						continue;
					}
					if(dkAmount <= 0 ) break;
				
					double lateFee = CommonUtils.null2Double(oh.getTotalLateFee());
			    	
					if(lateFee > dkAmount) {
						oh.setTotalLateFee(CommonUtils.getScaleNumber(lateFee - dkAmount, 2));
						
						//违约金没有抵扣完
						if(isTrue){
							for(HashMap m : lastOwedHistory){
								if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
									m.put("totalLateFee", oh.getTotalLateFee());
									m.put("dkLateFee", CommonUtils.getScaleNumber(dkAmount + CommonUtils.null2Double(m.get("dkLateFee")), 2));
									m.put("oprTime", new Date());
									m.put("isUsed", Constants.IS_USED_USING);
									break;
								}
							}
						}
						dkAmount = 0.0;
					}else{
						oh.setTotalLateFee(0.0);
						dkAmount = CommonUtils.getScaleNumber(dkAmount - lateFee, 2);
						//违约金抵扣完成
						if(isTrue){
							for(HashMap m : lastOwedHistory){
								if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
									m.put("totalLateFee", oh.getTotalLateFee());
									m.put("dkLateFee", CommonUtils.getScaleNumber(lateFee + CommonUtils.null2Double(m.get("dkLateFee")), 2));
									m.put("oprTime", new Date());
									m.put("owedAmount", oh.getOwedAmount());
									if(oh.getOwedAmount() == 0){
										m.put("isUsed", Constants.IS_USED_STOP);
										m.put("owedEndTime", new Date());
										
									}else{
										m.put("isUsed", Constants.IS_USED_USING);
									}
									break;
								}
							}
						}
						if(oh.getOwedAmount() == 0){
							oh.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
							oh.setOwedEndTime(new Date());
						}
					}
			    }
		    
		    
		    	//再抵扣本金
			    if(dkAmount > 0){
			    	for(TBsOwedHistory oh : histories){
						if(dkAmount <= 0 ) break;
						
						double owedAmount = CommonUtils.null2Double(oh.getOwedAmount());
						if(owedAmount > dkAmount){
							//欠费金额大于当前可抵扣金额
							oh.setOwedAmount(CommonUtils.getScaleNumber(owedAmount - dkAmount, 2));
							if(isTrue){
								for(HashMap m : lastOwedHistory){
									if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
										m.put("owedAmount", oh.getOwedAmount());
										m.put("isUsed", Constants.IS_USED_USING);
										m.put("dkOwedAmount", CommonUtils.getScaleNumber(dkAmount + CommonUtils.null2Double(m.get("dkOwedAmount")),2));
										m.put("oprTime", new Date());
										break;
									}
								}
							}
							dkAmount = 0.0;
						}else{
							//欠费金额小于当前可抵扣金额, 即抵扣完成
							if(isTrue){
								for(HashMap m : lastOwedHistory){
									if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
										m.put("owedAmount", 0.0);
										m.put("dkOwedAmount", CommonUtils.getScaleNumber(oh.getOwedAmount() + CommonUtils.null2Double(m.get("dkOwedAmount")),2));
										m.put("isUsed", Constants.IS_USED_STOP);
										m.put("owedEndTime", new Date());
										m.put("oprTime", new Date());
										break;
									}
								}
							}
							
							oh.setOwedAmount(0.0);
							dkAmount = CommonUtils.getScaleNumber(dkAmount - owedAmount, 2);
							oh.setOwedEndTime(new Date());
							oh.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
						}
			    	}
			    }										
			    for(TBsOwedHistory oh : histories){
			    	this.tBsOwedHistoryMapper.update(oh);
			    }
			    
			    if(isTrue){
			    	nextHistory.setLastOwedInfo(JSONArray.toJSONString(lastOwedHistory));
			    }
			}
		
		return dkAmount;
	}

	 /**
	  * 在这里对项目级别进行托收数据的生成
	  */
	 @SuppressWarnings("rawtypes")
	 @Override
	 public BaseDto genCollByManaul(String companyId, String projectId, Integer isStopCommon) {
	//  int CollectionEnum.common_account_using.getV()
	  TBcProject project = this.tBcProjectMapper.findById(projectId);
	  //查询出本次需要进行生成托收数据的计费总表id
	  List<String> totalIds = tBcProjectMapper.getChargeTotalIds(project.getProjectId());
	  for (String totalId : totalIds) {
	   genColl(companyId, project, totalId, CollectionEnum.common_account_using.getV());
	  }
	  
	  return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"手动触发托收数据完成"));
	 }

	@Override
	public MessageMap importFileByQueue(String companyId, String fileContent,String fileName, Integer flag, String totalId, String projectId,String userId) {
		
		MqEntity entity = new MqEntity();
		entity.setCompanyId(companyId);
		entity.setProjectId(projectId);
		entity.setProjectName(totalId);
		entity.setData(fileContent);
		entity.setOpr(flag);
		entity.setUserId(userId);
		entity.setSupAttr(fileName);
		this.amqpTemplate.convertAndSend(route_key_import_file, entity);
		
		return new MessageMap(MessageMap.INFOR_SUCCESS,"文件导入中,请稍后查看");
	}

	@Override
	public BaseDto findDatas(String companyId, String projectId,Date createTime, Integer collectionType) {
		BaseDto returnDto = new BaseDto();
		TBcCollectionTotal total = this.tBcCollectionMapper.findByProjectIdAndTime(projectId,createTime);
		if(total != null){
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(collectionType == CollectionEnum.type_jrl.getV()){
				//金融联
				resultMap = this.tBcJrlBodyMapper.getSuccessDatasByTotalId(total.getId());
			}else if(collectionType == CollectionEnum.type_union.getV()){
				//银联
				resultMap = this.tBcUnionBackBodyMapper.getSuccessDatasByTotalId(total.getId());
			}
			
			resultMap.put("collectionTime", total.getCollectionTime());
			
			returnDto.setObj(resultMap);
		}
		
		return returnDto;
	}

	
	/**
	 * 为银联添加手续费,必须要在托收数据生成之后才行,和以前不同了
	 *  调整后价格为1000元以下1元/笔，1000元（含）-5000元 1.5元/笔，5000元（含）以上2元/笔
	 */
	@Override
	public MessageMap separateAddServiceChargeForUnion(String companyId, String projectId) {
		// 1. 查询出本本周期的托收数据，根据项目id
		List<TBcUnionCollectionBody> collectList = tBcUnionCollectionBodyMapper.getThisCollectionInfo(projectId);
		if( CommonUtils.isEmpty( collectList ) ) return new MessageMap(MessageMap.INFOR_WARNING,"没找到可用托收数据！");
		
		List< TBcUnionCollectionBody > updateList = new ArrayList<>();
		
		//单个循环进行增加操作
		for (TBcUnionCollectionBody tBcUnionCollectionBody : collectList) {
			
			if ( 0 < tBcUnionCollectionBody.getAmount() && tBcUnionCollectionBody.getAmount() < 1000 ) {
				tBcUnionCollectionBody.setAmount( tBcUnionCollectionBody.getAmount() + 1 );
			}else if ( 1000 <= tBcUnionCollectionBody.getAmount() && tBcUnionCollectionBody.getAmount() < 5000 ) {
				tBcUnionCollectionBody.setAmount( tBcUnionCollectionBody.getAmount() + 1.5 );
			}else {
				tBcUnionCollectionBody.setAmount( tBcUnionCollectionBody.getAmount() + 2 );
			}
			updateList.add( tBcUnionCollectionBody );
			if( updateList.size() == commit_amount ) {
				//批量更新
				tBcUnionCollectionBodyMapper.batchUpdate(updateList);
				//清空
				updateList.clear();
			}
		}
		//数量较少
		tBcUnionCollectionBodyMapper.batchUpdate(updateList);
		return new MessageMap(MessageMap.INFOR_SUCCESS,"添加托收手续费成功");
	}

}
