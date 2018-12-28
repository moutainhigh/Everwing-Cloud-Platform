package com.everwing.coreservice.wy.core.service.impl.business.pay;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.*;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;
import com.everwing.coreservice.common.wy.entity.delivery.TJgStaffGrop;
import com.everwing.coreservice.common.wy.entity.delivery.TJgTotalCalculation;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.fee.constant.*;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderCycleDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.dto.FrontOperaDto;
import com.everwing.coreservice.common.wy.service.business.pay.TBsPayInfoService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.mq.MqSender;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.fee.api.AcBusinessOperaDetailApi;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("tBsPayInfoService")
public class TBsPayInfoServiceImpl extends Resources implements TBsPayInfoService{

	private static final Logger logger = LogManager.getLogger(TBsPayInfoServiceImpl.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final String TIME_YMH_STR = "YYYY-MM-dd";





	private static final DecimalFormat df = new DecimalFormat("#.00");

	@Autowired
	private FastDFSApi fastDFSApi;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private AcBusinessOperaDetailApi acBusinessOperaDetailApi;






	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPage(String companyId, TBsPayInfo info) {

		List<TJgStaffGrop> grops = this.tJgStaffGropMapper.findCanPayRole(info.getCreateId());
		if(CommonUtils.isEmpty(grops)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有收银权限,请联系您的上级在 [基础配置] -> [银账交割] -> [组织结构设计] 中添加您为收银员/收银组长. "));
		}else{
			List<TBsPayInfo> infos = this.tBsPayInfoMapper.listPage(info);
			//在这里同时获取一下水电物业本体的税率和项目名称
			List<Map<String, String>> taxRateList = tBsChargingSchemeMapper.getTaxRateByProject(grops.get(0).getProjectId());
			Map<String, String> taxMap = getTaxRateInfoToMap(taxRateList,grops.get(0).getProjectName());
			if(CommonUtils.isEmpty(infos)){
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前客户[名下无资产], 或[资产都在您无收/退款权限的其他项目内],请核实并联系其他收银员进行收银. "));
			}else{
				BaseDto baseDto = new BaseDto<>();
				baseDto.setObj(taxMap);
				baseDto.setLstDto(infos);
				baseDto.setPage(info.getPage());
				return baseDto;
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPage4Building(String companyId, TBsPayInfo info) {
		List<TJgStaffGrop> grops = this.tJgStaffGropMapper.findCanPayRole(info.getCreateId());
		if(CommonUtils.isEmpty(grops)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有收银权限,请联系您的上级在 [基础配置] -> [银账交割] -> [组织结构设计] 中添加您为收银员/收银组长. "));
		}else{
			List<TBsPayInfo> infos = this.tBsPayInfoMapper.listPage4Building(info);
			//在这里同时获取一下水电物业本体的税率和项目名称
			if(CommonUtils.isEmpty(infos)){
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前[资产在您无收/退款权限的其他项目内],请核实并联系其他收银员进行收银. "));
			}else{

				List<TBsPayInfo> returnInfos = new ArrayList<TBsPayInfo>();

				//去除buildingCode为空的
				for(TBsPayInfo infor : infos){
					if(CommonUtils.isEmpty(infor) || CommonUtils.isEmpty(infor.getBuildingCode())){
						continue;
					}
					returnInfos.add(infor);
				}

				if(CommonUtils.isEmpty(returnInfos)){
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"该资产无对应的欠费信息, 或者是不存在账户. 请联系管理员. "));
				}

				List<Map<String, String>> taxRateList = tBsChargingSchemeMapper.getTaxRateByProject(grops.get(0).getProjectId());
				Map<String, String> taxMap = getTaxRateInfoToMap(taxRateList,grops.get(0).getProjectName());

				BaseDto baseDto = new BaseDto<>();
				baseDto.setObj(taxMap);
				baseDto.setLstDto(returnInfos);
				baseDto.setPage(info.getPage());
				return baseDto;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto findByObj4Building(String companyId, TBsPayInfo info) {
		TBsPayInfo currInfo = this.tBsPayInfoMapper.findSumByObjCurrent4Building(info);

		if(null == currInfo || CommonUtils.isEmpty(currInfo.getBuildingCode())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"该资产无对应的欠费信息, 或者是不存在账户. 请联系管理员. "));
		}

		//判断当前所选房屋是否处于托收待回盘状态, 若是,则返回信息, 以ERROR状态返回
		String fullNames = checkCollingDatas(info);
		if(CommonUtils.isNotEmpty(fullNames))
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "处于  [托收待回盘] 状态, 无法使用[交费], [减免], [退款] 以及 [回退] 等与账户有关功能, 请取消选择. "));

		//这里增加一个打印显示的内容
		List<String> buildingNames = this.tBsPayInfoMapper.findBuildingNames4Building(info);
//		findLateFee(currInfo);
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(currInfo);
		returnDto.setLstDto(buildingNames);
		return returnDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto findByObj4BuildingNew(String companyId, TBsPayInfo info) {
		TBsPayInfo currInfo = this.tBsPayInfoMapper.findSumByObjCurrent4BuildingNew(info);

		if(null == currInfo || CommonUtils.isEmpty(currInfo.getBuildingCode())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"该资产无对应的欠费信息, 或者是不存在账户. 请联系管理员. "));
		}

		//判断当前所选房屋是否处于托收待回盘状态, 若是,则返回信息, 以ERROR状态返回
		String fullNames = checkCollingDatas(info);
		if(CommonUtils.isNotEmpty(fullNames))
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "处于  [托收待回盘] 状态, 无法使用[交费], [减免], [退款] 以及 [回退] 等与账户有关功能, 请取消选择. "));

		//这里增加一个打印显示的内容
		List<String> buildingNames = this.tBsPayInfoMapper.findBuildingNames4Building(info);
//		findLateFee(currInfo);
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(currInfo);
		returnDto.setLstDto(buildingNames);
		return returnDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto findByObj(String companyId, TBsPayInfo info) {
		BaseDto returnDto = new BaseDto();

		TBsPayInfo currInfo = this.tBsPayInfoMapper.findSumByObjCurrent(info);

		if(null == currInfo){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"该资产无对应的欠费信息, 或者是不存在账户. 请联系管理员. "));
		}

		//判断当前所选房屋是否处于托收待回盘状态, 若是,则返回信息, 以ERROR状态返回
		String fullNames = checkCollingDatas(info);
		if(CommonUtils.isNotEmpty(fullNames))
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "处于  [托收待回盘] 状态, 无法使用[交费], [减免], [退款] 以及 [回退] 等与账户有关功能, 请取消选择. "));

		//这里增加一个打印显示的内容
		List<String> buildingNames = this.tBsPayInfoMapper.findBuildingNames(info);
		returnDto.setLstDto(buildingNames);
//		findLateFee(currInfo);
		returnDto.setObj(currInfo);
		return returnDto;
	}

	private void findLateFee(TBsPayInfo currInfo){
		if(currInfo == null) return;
		currInfo.setWyLateFee(this.tBsPayInfoMapper.findLateFee(currInfo.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_WY.getIntV()));
		currInfo.setBtLateFee(this.tBsPayInfoMapper.findLateFee(currInfo.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_BT.getIntV()));
		currInfo.setWaterLateFee(this.tBsPayInfoMapper.findLateFee(currInfo.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_WATER.getIntV()));
		currInfo.setElectLateFee(this.tBsPayInfoMapper.findLateFee(currInfo.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()));
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto pay2Account(String companyId, TBsPayInfo info, String singleStr, String isNotSkipLateFee) {
		if(null == info || CommonUtils.isEmpty(singleStr)){
			logger.warn("传入参数为空,交费充值失败.");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空,交费充值失败."));
    }
		//校验充值的钱是否相等
		double czAmount = CommonUtils.getSum(info.getWyAmount(),info.getBtAmount(),info.getWaterAmount(),info.getElectAmount(),info.getCommonAmount());
		double scAmount = CommonUtils.getSum(info.getPayCash(),info.getPayUnion(),info.getPayWx(),info.getPayZfb(),info.getPayBank());
		if(czAmount == 0){
			logger.warn("充值金额为空,请不要做冲零操作. ");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额为空,请不要做冲零操作. "));
		}
		if(scAmount == 0){
			logger.warn("实充金额为空,请不要做冲零操作. ");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "实充金额为空,请不要做冲零操作. "));
		}
		if(czAmount != scAmount){
			logger.warn("充值金额 :[{}]元, 与实充金额 :[{}]元不相等, 充值失败. 请检查.",czAmount,scAmount);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额 :[" + czAmount + "]元, 与实充金额 :[" + scAmount + "]元不相等, 充值失败. 请检查."));
		}

		List<String> buildingCodes = null;
		if(CommonUtils.isEquals(Constants.PAY_STR_ALL, singleStr)){
			//全交(客户角度)
			buildingCodes = this.personBuildingNewMapper.getBuildingCodesByCustIdAndChargerId(info.getCustId(),info.getCreateId());
		}else{
			//交部分(客户角度/建筑角度)
			buildingCodes = info.getBuildingCodes();
		}

		if(CommonUtils.isEmpty(buildingCodes)){
			logger.warn("未找到当前客户相应的建筑绑定关系, 交费充值失败. 数据:{}. ", info.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "未找到当前客户相应的建筑绑定关系, 交费充值失败. "));
		}

		TSysUserSearch condition = new TSysUserSearch();
		condition.setUserId(info.getCreateId());
		List<TSysUserList> userList = this.tSysUserMapper.findByCondition(condition);
		String staffName = (CommonUtils.isEmpty(userList)) ? null : userList.get(0).getStaffName();
		String staffNumber = (CommonUtils.isEmpty(userList)) ? "" : userList.get(0).getStaffNumber();

		//物业管理费, 本体基金, 水费, 电费一个一个账户来抵扣
		String buildingCode;
		String projectName = null;
		String projectId = null;
		List<TBsAssetAccountStream> insertSteamList = new ArrayList<TBsAssetAccountStream>();
		List<TBsPayInfo> insertInfos = new ArrayList<TBsPayInfo>();

		String tarId = CommonUtils.getUUID();
		info.setWyAmount(CommonUtils.null2Double(info.getWyAmount()));
		info.setBtAmount(CommonUtils.null2Double(info.getBtAmount()));
		info.setWaterAmount(CommonUtils.null2Double(info.getWaterAmount()));
		info.setElectAmount(CommonUtils.null2Double(info.getElectAmount()));
		info.setCommonAmount(CommonUtils.null2Double(info.getCommonAmount()));
		info.setPayCash(CommonUtils.null2Double(info.getPayCash()));
		info.setPayUnion(CommonUtils.null2Double(info.getPayUnion()));
		info.setPayWx(CommonUtils.null2Double(info.getPayWx()));
		info.setPayZfb(CommonUtils.null2Double(info.getPayZfb()));
		info.setPayBank(CommonUtils.null2Double(info.getPayBank()));
		Double payCash = info.getPayCash();
		Double payUnion = info.getPayUnion();
		Double payWx = info.getPayWx();
		Double payBank = info.getPayBank();
		Double payZfb = info.getPayZfb();

		List<String> types = new ArrayList<String>();
		if(info.getWyAmount() > 0) types.add(Constants.STR_ONE);
		if(info.getBtAmount() > 0) types.add(Constants.STR_TWO);
		if(info.getWaterAmount() > 0) types.add(Constants.STR_THREE);
		if(info.getElectAmount() > 0) types.add(Constants.STR_FOUR);
		if(info.getCommonAmount() > 0) types.add(Constants.STR_ZERO);

		//做一个校验
        boolean msgCheck = checkPayedAmount(companyId,buildingCodes.get(0),info);
        if( !msgCheck ){
            //校验没有通过
            logger.warn("专项账户存在交费大于欠费的情况, 交费充值失败. 数据:{}. ", info.getBuildingCodes());
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "未找到当前客户相应的建筑绑定关系, 交费充值失败. "));
        }


		//在这里作出一个调整就是一次交多个房的时候，交易流水号相同，之前不同，导致后面新账户无法实现关联（实际情况一次交多房很少）
		String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9);

		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据S（房号-- 老账户的buildingCode  每个账户的金额）

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("operateId", info.getCreateId());
		//paramMap.put("houseCodes",modifyHousetCode(buildingCodes));
		Map<String, String> amountMap = new HashMap<>();
		amountMap.put("wyAmount", info.getWyAmount().toString() );
		amountMap.put("btAmount", info.getBtAmount().toString());
		amountMap.put("waterAmount",info.getWaterAmount().toString());
		amountMap.put("electAmount",info.getElectAmount().toString());
		amountMap.put("commonAmount",info.getCommonAmount().toString());
		paramMap.put("amountMap", amountMap);
        paramMap.put("payType",info.getPayType());
		paramMap.put("batchNo", batchNo);
		logger.info("准备发送消息队列-- 老账户交费");
		mqSender.sendAcChargeDetailForBatchRecharge(paramMap,companyId);

		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据E


		for(int i = 0 ; i < buildingCodes.size() ; i++){
			boolean isLastFlag = false;
			buildingCode = buildingCodes.get(i);
			List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.findByBuildingCodeAndItems(buildingCode, types);

			if(CommonUtils.isEmpty(accounts))  continue;


			newBusinessAndOrder(accounts,info,companyId);

			TBsPayInfo insertInfo = new TBsPayInfo();
			insertInfo.setId(CommonUtils.getUUID());
			insertInfo.setBuildingCode(buildingCode);
			insertInfo.setCreateId(info.getCreateId());
			insertInfo.setCustId(info.getCustId());
			insertInfo.setCustName(info.getCustName());
			insertInfo.setProjectId(info.getProjectId());
			insertInfo.setModifyId(info.getModifyId());
			insertInfo.setCreateId(info.getCreateId());
			insertInfo.setJmRemark(info.getJmRemark());		//备注
			insertInfo.setPayType(info.getPayType());
			insertInfo.setPayerName(info.getPayerName());
			insertInfo.setStatus(info.getStatus());
			insertInfo.setRelationId(tarId);
			insertInfo.setBatchNo(batchNo);

			if(i == buildingCodes.size() - 1){
				//最后一个建筑, 若还有钱,都放入本建筑的账户
				isLastFlag = true;
				insertInfo.setPayCash(CommonUtils.null2Double(info.getPayCash()));
				insertInfo.setPayUnion(CommonUtils.null2Double(info.getPayUnion()));
				insertInfo.setPayWx(CommonUtils.null2Double(info.getPayWx()));
				insertInfo.setPayZfb(CommonUtils.null2Double(info.getPayZfb()));
				insertInfo.setPayBank(CommonUtils.null2Double(info.getPayBank()));
			}
			for(TBsAssetAccount account : accounts){
				TBsAssetAccountStream stream = accountRecharge(account, info, isLastFlag,insertInfo,buildingCodes.size(),isNotSkipLateFee);
				if(null != stream && CommonUtils.null2Double(stream.getChangMoney()) > 0){
					insertSteamList.add(stream);
				}
			}
			insertInfos.add(insertInfo);

			TSysProject project = this.tSysProjectMapper.findByCode(insertInfo.getProjectId());
			projectName = (null == project) ? null : project.getName();
			projectId = (null == project) ? null : project.getCode();
		}

		if(!insertSteamList.isEmpty()){
			this.tBsAssetAccountStreamMapper.batchInsert(insertSteamList);
		}
		if(!insertInfos.isEmpty()){
			this.tBsPayInfoMapper.batchInsert(insertInfos);
		}

		info.setProjectId(projectId);
		TJgAccountReceivable tar = payInfo2Tar(info, staffName,staffNumber,projectName, 2); //未结算
		if(null != tar){
			tar.setId(tarId);
			tar.setPayCash((null == payCash) ? Constants.STR_ZERO : payCash.toString());
			tar.setPayWx((null == payWx) ? Constants.STR_ZERO : payWx.toString());
			tar.setPayUnion((null == payUnion) ? Constants.STR_ZERO : payUnion.toString());
			tar.setAlipay((null == payZfb) ? Constants.STR_ZERO : payZfb.toString());
			tar.setBankReceipts((null == payBank)? Constants.STR_ZERO : payBank.toString());
			tar.setBusinessType(1);
			this.tJgAccountReceivableMapper.addAccountReceivable(tar);
		}

		//项目级别的缴费聚合  TBsProjectAutoFlushListener会自动刷新 这里不再刷新项目

		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"账户充值完成."));
	}


    /**
     * 后面专项账户去掉了，只有通用账户可以有余额，所以页面交钱不在允许多交专项账户的，2018-11-18 这里加一个校验
     * @param companyId 公司id
     * @param buildingCode 房号
     * @param entity  包含了支付信息的实体参数
     * @return 返回一个包含结果描述的信息
     */
    private boolean checkPayedAmount(String companyId,String buildingCode,TBsPayInfo entity){

//        MessageMap msg = new MessageMap(MessageMap.INFOR_SUCCESS,"校验成功，符合要求");
        // 1.根据房号查出真实的欠费
        Map<String,Double> accountMap = tBsAssetAccountMapper.getAccountInfoByCode(buildingCode);
        // 2校验欠费和交费的值
        if(CommonUtils.isNotEmpty( accountMap )){
            double wyA = accountMap.get("wyA");
            double btA = accountMap.get("btA");
            double waterA = accountMap.get("waterA");
            double electA = accountMap.get("electA");
            if(Math.abs(entity.getWyAmount()) > wyA
                || Math.abs(entity.getBtAmount()) > btA
                || Math.abs(entity.getWaterAmount()) > waterA
                || Math.abs(entity.getElectAmount()) > electA ){
                //有一个交费大于欠费都不可以
//                return new MessageMap(MessageMap.INFOR_ERROR,"存在专项交费金额大于账户欠费金额的情况，请确认");
                return false;
            }
        }
        // 3返回结果
        return true;
    }



	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto pay2AccountForWeiXin(String companyId, TBsPayInfo info, String isNotSkipLateFee) {
		if(null == info){
			logger.warn("传入参数为空,交费充值失败.");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空,交费充值失败."));
		}

		info.setPayType( PayChannel.SMALL_ROUTINE.getCode() );	//这里虽然确实是微信支付，但是小程序过来老账户的数据只是保证两边数据一致，并没有其他特殊意义
		
		//校验充值的钱是否相等
		double czAmount = CommonUtils.getSum(info.getWyAmount(),info.getBtAmount(),info.getWaterAmount(),info.getElectAmount(),info.getCommonAmount());
		double scAmount = CommonUtils.getSum(info.getPayCash(),info.getPayUnion(),info.getPayWx(),info.getPayZfb(),info.getPayBank());
		if(czAmount == 0){
			logger.warn("充值金额为空,请不要做冲零操作. ");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额为空,请不要做冲零操作. "));
		}
		if(scAmount == 0){
			logger.warn("实充金额为空,请不要做冲零操作. ");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "实充金额为空,请不要做冲零操作. "));
		}
		if(czAmount != scAmount){
			logger.warn("充值金额 :[{}]元, 与实充金额 :[{}]元不相等, 充值失败. 请检查.",czAmount,scAmount);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额 :[" + czAmount + "]元, 与实充金额 :[" + scAmount + "]元不相等, 充值失败. 请检查."));
		}

		List<String> buildingCodes = null;

		//交部分(客户角度/建筑角度)  合版修改
		//buildingCodes = modifyBuildingCodeFromHouseCode(info.getBuildingCodes());  //这里需要转换一下，因为微信过来的传递的是housecode,但是老账户的还是buildingcode

		if(CommonUtils.isEmpty(buildingCodes)){
			logger.warn("未找到当前客户相应的建筑绑定关系, 交费充值失败. 数据:{}. ", info.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "未找到当前客户相应的建筑绑定关系, 交费充值失败. "));
		}

		TSysUserSearch condition = new TSysUserSearch();
		condition.setUserId(info.getCreateId());
		List<TSysUserList> userList = this.tSysUserMapper.findByCondition(condition);
		String staffName = (CommonUtils.isEmpty(userList)) ? null : userList.get(0).getStaffName();
		String staffNumber = (CommonUtils.isEmpty(userList)) ? "" : userList.get(0).getStaffNumber();

		//物业管理费, 本体基金, 水费, 电费一个一个账户来抵扣
		String buildingCode;
		String projectName = null;
		String projectId = null;
		List<TBsAssetAccountStream> insertSteamList = new ArrayList<TBsAssetAccountStream>();
		List<TBsPayInfo> insertInfos = new ArrayList<TBsPayInfo>();

		String tarId = CommonUtils.getUUID();
		info.setWyAmount(CommonUtils.null2Double(info.getWyAmount()));
		info.setBtAmount(CommonUtils.null2Double(info.getBtAmount()));
		info.setWaterAmount(CommonUtils.null2Double(info.getWaterAmount()));
		info.setElectAmount(CommonUtils.null2Double(info.getElectAmount()));
		info.setCommonAmount(CommonUtils.null2Double(info.getCommonAmount()));
		info.setPayCash(CommonUtils.null2Double(info.getPayCash()));
		info.setPayUnion(CommonUtils.null2Double(info.getPayUnion()));
		info.setPayWx(CommonUtils.null2Double(info.getPayWx()));
		info.setPayZfb(CommonUtils.null2Double(info.getPayZfb()));
		info.setPayBank(CommonUtils.null2Double(info.getPayBank()));
		Double payCash = info.getPayCash();
		Double payUnion = info.getPayUnion();
		Double payWx = info.getPayWx();
		Double payBank = info.getPayBank();
		Double payZfb = info.getPayZfb();

		List<String> types = new ArrayList<String>();
		if(info.getWyAmount() > 0) types.add(Constants.STR_ONE);
		if(info.getBtAmount() > 0) types.add(Constants.STR_TWO);
		if(info.getWaterAmount() > 0) types.add(Constants.STR_THREE);
		if(info.getElectAmount() > 0) types.add(Constants.STR_FOUR);
		if(info.getCommonAmount() > 0) types.add(Constants.STR_ZERO);


		//在这里作出一个调整就是一次交多个房的时候，交易流水号相同，之前不同，导致后面新账户无法实现关联（实际情况一次交多房很少）
		String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9);

		for(int i = 0 ; i < buildingCodes.size() ; i++){
			boolean isLastFlag = false;
			buildingCode = buildingCodes.get(i);
			List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.findByBuildingCodeAndItems(buildingCode, types);

			if(CommonUtils.isEmpty(accounts))  continue;

			TBsPayInfo insertInfo = new TBsPayInfo();
			insertInfo.setId(CommonUtils.getUUID());
			insertInfo.setBuildingCode(buildingCode);
			insertInfo.setCreateId(info.getCreateId());
			insertInfo.setCustId(info.getCustId());
			insertInfo.setCustName(info.getCustName());
			insertInfo.setProjectId(info.getProjectId());
			insertInfo.setModifyId(info.getModifyId());
			insertInfo.setCreateId(info.getCreateId());
			insertInfo.setJmRemark(info.getJmRemark());		//备注
			insertInfo.setPayType(info.getPayType());
			insertInfo.setPayerName(info.getPayerName());
			insertInfo.setStatus(info.getStatus());
			insertInfo.setRelationId(tarId);
			insertInfo.setBatchNo(batchNo);

			if(i == buildingCodes.size() - 1){
				//最后一个建筑, 若还有钱,都放入本建筑的账户
				isLastFlag = true;
				insertInfo.setPayCash(CommonUtils.null2Double(info.getPayCash()));
				insertInfo.setPayUnion(CommonUtils.null2Double(info.getPayUnion()));
				insertInfo.setPayWx(CommonUtils.null2Double(info.getPayWx()));
				insertInfo.setPayZfb(CommonUtils.null2Double(info.getPayZfb()));
				insertInfo.setPayBank(CommonUtils.null2Double(info.getPayBank()));
			}
			for(TBsAssetAccount account : accounts){
				TBsAssetAccountStream stream = accountRecharge(account, info, isLastFlag,insertInfo,buildingCodes.size(),isNotSkipLateFee);
				if(null != stream && CommonUtils.null2Double(stream.getChangMoney()) > 0){
					insertSteamList.add(stream);
				}
			}
			insertInfos.add(insertInfo);

			TSysProject project = this.tSysProjectMapper.findByCode(insertInfo.getProjectId());
			projectName = (null == project) ? null : project.getName();
			projectId = (null == project) ? null : project.getCode();
		}

		if(!insertSteamList.isEmpty()){
			this.tBsAssetAccountStreamMapper.batchInsert(insertSteamList);
		}
		if(!insertInfos.isEmpty()){
			this.tBsPayInfoMapper.batchInsert(insertInfos);
		}

		//微信小程序过来的数据不过银账交割
		/*info.setProjectId(projectId);
		TJgAccountReceivable tar = payInfo2Tar(info, staffName,staffNumber,projectName, 2); //未结算
		if(null != tar){
			tar.setId(tarId);
			tar.setPayCash((null == payCash) ? Constants.STR_ZERO : payCash.toString());
			tar.setPayWx((null == payWx) ? Constants.STR_ZERO : payWx.toString());
			tar.setPayUnion((null == payUnion) ? Constants.STR_ZERO : payUnion.toString());
			tar.setAlipay((null == payZfb) ? Constants.STR_ZERO : payZfb.toString());
			tar.setBankReceipts((null == payBank)? Constants.STR_ZERO : payBank.toString());
			tar.setBusinessType(1);
			this.tJgAccountReceivableMapper.addAccountReceivable(tar);
		}*/

		//项目级别的缴费聚合  TBsProjectAutoFlushListener会自动刷新 这里不再刷新项目

		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"账户充值完成."));
	}


	private TJgAccountReceivable payInfo2Tar(TBsPayInfo info, String staffName,String staffNumber, String projectName, Integer status){
		if(info == null)
			return null;
		TJgAccountReceivable tar = new TJgAccountReceivable();
		tar.setOprId(info.getCreateId());
		tar.setOprName(staffName);
		tar.setProjectId(info.getProjectId());
		tar.setProjectName(projectName);
		tar.setPayedType(String.valueOf(info.getPayType()));

		tar.setPayerName(info.getPayerName());
		tar.setPayCash(df.format(CommonUtils.null2Double(info.getPayCash())));
		tar.setPayWx(df.format(CommonUtils.null2Double(info.getPayWx())));
		tar.setPayUnion(df.format(CommonUtils.null2Double(info.getPayUnion())));
		tar.setAlipay(df.format(CommonUtils.null2Double(info.getPayZfb())));
		tar.setBankReceipts(df.format(CommonUtils.null2Double(info.getPayBank())));
//		tar.setRelationId(info.getId());

		tar.setPayedTime(info.getCreateTime());
		tar.setStatus(status);	//未结算
		tar.setTradNo(sdf.format(new Date()).concat(SettlementEnum.BUSINESS_ACCOUNT_RECEIVABLE.getIntValue().toString()).concat(staffNumber));
		return tar;
	}

	private TJgTotalCalculation payInfoFinanceTar(TBsPayInfo info, String staffName, String staffNumber, String projectName, Integer status){
		if(info == null)
			return null;
		TJgStaffGrop tJgStaffGrop = tJgStaffGropMapper.getTJgStaffGropByUserId(info.getCreateId(),info.getProjectId());
		TJgTotalCalculation tar = new TJgTotalCalculation();
		tar.setOprId(info.getCreateId());
		tar.setOprName(staffName);
		tar.setProjectId(info.getProjectId());
		tar.setProjectName(projectName);
		//tar.setPayerName(info.getPayerName());
		tar.setCashTotal((CommonUtils.null2Double(info.getPayCash())));
		tar.setCashGaven(0.00);
		tar.setCashNotGive((CommonUtils.null2Double(info.getPayCash())));
		tar.setWxTotal((CommonUtils.null2Double(info.getPayWx())));
		tar.setWxGaven(0.00);
		tar.setAlipayGaven(CommonUtils.null2Double(info.getPayZfb()));
		tar.setAlipayGaven(0.00);
		tar.setAlipayNotGive(CommonUtils.null2Double(info.getPayZfb()));
		tar.setWxNotGive((CommonUtils.null2Double(info.getPayWx())));
		tar.setUnionTotal((CommonUtils.null2Double(info.getPayUnion())));
		tar.setUnionGaven(0.00);
		tar.setUnionNotGive((CommonUtils.null2Double(info.getPayUnion())));
		tar.setBankReceiptsTotal((CommonUtils.null2Double(info.getPayBank())));
		tar.setBankReceiptsGaven(0.00);
		tar.setBankReceiptsNotGive((CommonUtils.null2Double(info.getPayBank())));
		tar.setReceiveId(CommonUtils.isEmpty(tJgStaffGrop) ? "" : tJgStaffGrop.getId());
		tar.setReceiveName(CommonUtils.isEmpty(tJgStaffGrop) ? "" : tJgStaffGrop.getStaffName());
		//tar.setEndTime(sdf.format(info.getModifyTime()));
		//tar.setRelationId(info.getId());


		tar.setStatus(status);	//未结算
		//tar.setTradNo(sdf.format(new Date()).concat(SettlementEnum.BUSINESS_ACCOUNT_RECEIVABLE.getIntValue().toString()).concat(staffNumber));
		return tar;
	}

	/**
	 * @TODO 账户充值, 修改, 写入流水, 违约金抵扣
	 * @param account
	 * @param info
	 * @param isLastFlag
	 * @return
	 */
	private TBsAssetAccountStream accountRecharge(TBsAssetAccount account ,
												  TBsPayInfo info ,
												  boolean isLastFlag,
												  TBsPayInfo insertInfo,
												  int buildingCount,
												  String isNotSkipLateFee){
		if(account == null) return null;

		double amount = 0.0;
		double accountBalance = CommonUtils.null2Double(account.getAccountBalance());
		double taxRate = 0.0;
		if(BillingEnum.ACCOUNT_TYPE_COMMON.getIntV() != account.getType()){
			Double tr = this.tBsChargingSchemeMapper.findTaxRate(account.getType(), account.getBuildingCode());
			taxRate = (null == tr) ? 0 : tr.doubleValue();
		}

		insertInfo.setProjectId(account.getProjectId());
		if(!isLastFlag && accountBalance < 0){
			insertInfo.setPayCash(CommonUtils.null2Double(insertInfo.getPayCash()));
			double payCash = CommonUtils.null2Double(info.getPayCash());
			double balanceAbs = Math.abs(accountBalance);
			if(payCash >= balanceAbs) {   //支付的现金大于账户欠费金额 , 因为前面已经判断了账户金额为负数
				insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash() , balanceAbs));
				info.setPayCash(CommonUtils.getScaleNumber(payCash - balanceAbs,2));
			}else{
				insertInfo.setPayUnion(CommonUtils.null2Double(insertInfo.getPayUnion()));
				double payUnion = CommonUtils.null2Double(info.getPayUnion());
				if((payCash + payUnion) >= balanceAbs){   // 支付的现金加银联刷卡 大于账户欠费金额
					insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
					insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(), (balanceAbs - payCash)));
					info.setPayCash(0.0);
					info.setPayUnion(CommonUtils.getScaleNumber(payUnion - (balanceAbs - payCash),2));
				}else{
					insertInfo.setPayWx(CommonUtils.null2Double(insertInfo.getPayWx()));
					double payWx = CommonUtils.null2Double(info.getPayWx());
					if((payCash + payUnion + payWx) >= balanceAbs){  //支付的现金, 银联刷卡 , 微信支付大于账户欠费金额
						insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
						insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(),payUnion));
						insertInfo.setPayWx(CommonUtils.getSum(insertInfo.getPayWx() , (balanceAbs - payCash - payUnion)));
						info.setPayCash(0.0);
						info.setPayUnion(0.0);
						info.setPayWx(CommonUtils.getScaleNumber(payWx - (balanceAbs - payCash - payUnion),2));
					}else{

						//加上支付宝额度扣取
						insertInfo.setPayZfb(CommonUtils.null2Double(insertInfo.getPayZfb()));
						double payZfb = CommonUtils.null2Double(info.getPayZfb());
						if((payCash + payUnion + payWx + payZfb) >= balanceAbs){
							insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
							insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(),payUnion));
							insertInfo.setPayWx(CommonUtils.getSum(insertInfo.getPayWx(),payWx));
							insertInfo.setPayZfb(CommonUtils.getSum(insertInfo.getPayZfb() , (balanceAbs - payCash - payUnion - payWx)));
							info.setPayCash(0.0);
							info.setPayUnion(0.0);
							info.setPayWx(0.0);
							info.setPayZfb(CommonUtils.getScaleNumber(payZfb - (balanceAbs - payCash - payUnion - payWx),2));
						}else{
							//加上银行账号转款进行扣取
							insertInfo.setPayBank(CommonUtils.null2Double(insertInfo.getPayBank()));
							double payBank = CommonUtils.null2Double(info.getPayBank());
							if((payCash + payUnion + payWx + payZfb + payBank) >= balanceAbs){
								insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
								insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(),payUnion));
								insertInfo.setPayWx(CommonUtils.getSum(insertInfo.getPayWx(),payWx));
								insertInfo.setPayZfb(CommonUtils.getSum(insertInfo.getPayWx(),payZfb));
								insertInfo.setPayBank(CommonUtils.getSum(insertInfo.getPayBank(), (balanceAbs - payCash - payUnion - payWx - payZfb)));
								info.setPayCash(0.0);
								info.setPayUnion(0.0);
								info.setPayWx(0.0);
								info.setPayZfb(0.0);
								info.setPayBank(CommonUtils.getScaleNumber(payBank - (balanceAbs - payCash - payUnion - payWx - payZfb), 2));
							}
						}
					}
				}
			}
		}

		if(account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV() && info.getWyAmount() > 0 ){

			if(isLastFlag){ //最后一条数据, 则将所有的钱都转入到该账户
				amount = info.getWyAmount();
			}else{
				if(accountBalance > 0) return null;
				amount = ( Math.abs(accountBalance) >= info.getWyAmount() ) ? info.getWyAmount() : Math.abs(accountBalance);
			}
			account.setAccountBalance(accountBalance + amount);
			info.setWyAmount(info.getWyAmount() - amount);
			insertInfo.setWyAmount(amount);
			insertInfo.setWyTax(CommonUtils.getTax(amount, taxRate));
		}

		else if(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV() && info.getBtAmount() > 0){
			if(isLastFlag){
				amount = info.getBtAmount();
			}else{
				if(accountBalance > 0) return null;
				amount = (Math.abs(accountBalance) >= info.getBtAmount() ) ? info.getBtAmount() : Math.abs(accountBalance);
			}
			account.setAccountBalance(accountBalance + amount);
			info.setBtAmount(info.getBtAmount() - amount);
			insertInfo.setBtAmount(amount);
			insertInfo.setBtTax(CommonUtils.getTax(amount, taxRate));
		}

		else if(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV() && info.getWaterAmount() > 0){
			if(isLastFlag){
				amount = info.getWaterAmount();
			}else{
				if(accountBalance > 0) return null;
				amount = (Math.abs(accountBalance) >= info.getWaterAmount() ) ? info.getWaterAmount() : Math.abs(accountBalance);
			}
			account.setAccountBalance(accountBalance + amount);
			info.setWaterAmount(info.getWaterAmount() - amount);
			insertInfo.setWaterAmount(amount);
			insertInfo.setWaterTax(CommonUtils.getTax(amount, taxRate));
		}
		else if(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV() && info.getElectAmount() > 0){

			if(isLastFlag){
				amount = info.getElectAmount();
			}else{
				if(accountBalance > 0) return null;
				amount = (Math.abs(accountBalance) >= info.getElectAmount() ) ? info.getElectAmount() : Math.abs(accountBalance);
			}
			account.setAccountBalance(accountBalance + amount);
			info.setElectAmount(info.getElectAmount() - amount);
			insertInfo.setElectAmount(amount);
			insertInfo.setElectTax(CommonUtils.getTax(amount, taxRate));
		}
		else if(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV() && info.getCommonAmount() > 0){
			amount = info.getCommonAmount() / buildingCount;
			account.setAccountBalance(accountBalance + amount);
			insertInfo.setCommonAmount(amount);
		}

		TBsChargeBillHistory history = this.tBsChargeBillHistoryMapper.selectNotBillingByObj(account.getBuildingCode(),account.getType());

		//违约金抵扣
		oprLateFee(account, amount, history,isNotSkipLateFee, insertInfo);

		this.tBsAssetAccountMapper.update(account);

		//下月的分账单的上期已付更新, 本月分账单的缴费额更新
		if(history != null){
			//找到上期的history 即本月账单
			TBsChargeBillHistory lastHistory = this.tBsChargeBillHistoryMapper.findById(history.getLastBillId());
			if(null != lastHistory){
				double ncd = CommonUtils.calKf(lastHistory.getCurrentBillFee(), lastHistory.getCommonDesummoney(), lastHistory.getNoCommonDesummoney(), amount);
				lastHistory.setNoCommonDesummoney(ncd);
				this.tBsChargeBillHistoryMapper.updateBillHistory(lastHistory);
			}

			double lp = CommonUtils.calKf(history.getLastBillFee(), 0.0, history.getLastPayed(), amount);
			history.setLastPayed(lp);
			this.tBsChargeBillHistoryMapper.updateBillHistory(history);
		}

		return new TBsAssetAccountStream(CommonUtils.getUUID(),
				account.getId(),
				amount,
				new Date(),
				info.getModifyId(),
				info.getModifyId(),
				StreamEnum.purpose_pay_by_person.getV()	//业主交费
		);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void oprLateFee(TBsAssetAccount account,
							double amount,
							TBsChargeBillHistory history,
							String isNotSkipLateFee,
							TBsPayInfo info){
		//违约金抵扣
		boolean isTrue = (history != null && account.getType() != BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());


		List<TBsOwedHistory> histories = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
		if(CommonUtils.isNotEmpty(histories)){

			List<HashMap> lastOwedHistory = (isTrue) ? JSONObject.parseArray(history.getLastOwedInfo(), HashMap.class) : null;
			isTrue = CommonUtils.isNotEmpty(lastOwedHistory);

			double dkAmount = amount;

			//先抵扣违约金, 选择不跳过违约金缴纳
			if(CommonUtils.isEquals(Constants.STR_YES, isNotSkipLateFee)){
				double dkLateFee = 0.0;
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
						dkLateFee += dkAmount;
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
						dkLateFee += lateFee;
						if(oh.getOwedAmount() == 0){
							oh.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
							oh.setOwedEndTime(new Date());
						}
					}
				}

				dkLateFee = CommonUtils.getScaleNumber(dkLateFee, 2);
				switch (account.getType()) {
					case 1: info.setWyLateFee(dkLateFee); break;
					case 2: info.setBtLateFee(dkLateFee); break;
					case 3: info.setWaterLateFee(dkLateFee); break;
					case 4: info.setElectLateFee(dkLateFee); break;
					default:
						break;
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
									if(CommonUtils.isEmpty(oh.getTotalLateFee()))
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
						if(CommonUtils.isEmpty(oh.getTotalLateFee())){
							oh.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
						}
					}
				}
			}

			for(TBsOwedHistory oh : histories){
				this.tBsOwedHistoryMapper.update(oh);
			}

			if(isTrue){
				history.setLastOwedInfo(JSONArray.toJSONString(lastOwedHistory));
				this.tBsChargeBillHistoryMapper.updateBillHistory(history);
			}
		}
	}

	@Override
	public MessageMap checkAccountExists(String companyId, TBsPayInfo info,String singleStr) {

		if(null == info || CommonUtils.isEmpty(singleStr)){
			logger.warn("传入参数为空,交费充值失败.");
			return new MessageMap(MessageMap.INFOR_ERROR, "传入参数为空,交费充值失败.");
		}

		List<String> buildingCodes = null;
		if(CommonUtils.isEquals(Constants.PAY_STR_ALL, singleStr)){
			//全交(客户角度)
			buildingCodes = this.personBuildingNewMapper.getBuildingCodesByCustIdAndChargerId(info.getCustId(),info.getCreateId());
		}else{
			//交部分(客户角度/建筑角度)
			buildingCodes = info.getBuildingCodes();
		}

		if(CommonUtils.isEmpty(buildingCodes)){
			logger.warn("未找到当前客户相应的建筑绑定关系, 交费充值失败. 数据:{}. ", info.toString());
			return new MessageMap(MessageMap.INFOR_ERROR, "未找到当前客户相应的建筑绑定关系, 交费充值失败. ");
		}

		int totalCount = buildingCodes.size();
		int count = 0;
		String str = "";
		boolean flag = true;
		if(CommonUtils.null2Double(info.getWyAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndType(buildingCodes,BillingEnum.ACCOUNT_TYPE_WY.getIntV());
			if(count == 0){
				str += "所有资产不存在物业管理费账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在物业管理费账户,所交费用会进入其他资产物业管理费账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getBtAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndType(buildingCodes,BillingEnum.ACCOUNT_TYPE_BT.getIntV());
			if(count == 0){
				str += "所有资产不存在本体基金账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在本体基金账户,所交费用会进入其他资产本体基金账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getWaterAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndType(buildingCodes,BillingEnum.ACCOUNT_TYPE_WATER.getIntV());
			if(count == 0){
				str += "所有资产不存在水费账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在水费账户,所交费用会进入其他资产水费账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getElectAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndType(buildingCodes,BillingEnum.ACCOUNT_TYPE_ELECT.getIntV());
			if(count == 0){
				str += "所有资产不存在电费账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在电费账户,所交费用会进入其他资产电费账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getCommonAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndType(buildingCodes, BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());
			if(count == 0){
				str += "所有资产不存在通用账户账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在通用账户,所交费用会进入其他资产通用账户. \r\n";
			}
		}

		if(CommonUtils.isEmpty(str)){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"账户检测通过,当前可交费.");
		}else{
			return new MessageMap((!flag) ? MessageMap.INFOR_ERROR : MessageMap.INFOR_WARNING,str);
		}
	}

	@Override
	public MessageMap checkAccountExistsNew(String companyId, TBsPayInfo info,String singleStr) {

		if(null == info || CommonUtils.isEmpty(singleStr)){
			logger.warn("传入参数为空,交费充值失败.");
			return new MessageMap(MessageMap.INFOR_ERROR, "传入参数为空,交费充值失败.");
		}

		List<String> buildingCodes = null;
		if(CommonUtils.isEquals(Constants.PAY_STR_ALL, singleStr)){
			//全交(客户角度)
			buildingCodes = this.personBuildingNewMapper.getBuildingCodesByCustIdAndChargerId(info.getCustId(),info.getCreateId());
		}else{
			//交部分(客户角度/建筑角度)
			buildingCodes = info.getBuildingCodes();
		}

		if(CommonUtils.isEmpty(buildingCodes)){
			logger.warn("未找到当前客户相应的建筑绑定关系, 交费充值失败. 数据:{}. ", info.toString());
			return new MessageMap(MessageMap.INFOR_ERROR, "未找到当前客户相应的建筑绑定关系, 交费充值失败. ");
		}

		int totalCount = buildingCodes.size();
		int count = 0;
		String str = "";
		boolean flag = true;
		if(CommonUtils.null2Double(info.getWyAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndTypeNew(buildingCodes,BillingEnum.ACCOUNT_TYPE_WY.getIntV());
			if(count == 0){
				str += "所有资产不存在物业管理费账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在物业管理费账户,所交费用会进入其他资产物业管理费账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getBtAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndTypeNew(buildingCodes,BillingEnum.ACCOUNT_TYPE_BT.getIntV());
			if(count == 0){
				str += "所有资产不存在本体基金账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在本体基金账户,所交费用会进入其他资产本体基金账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getWaterAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndTypeNew(buildingCodes,BillingEnum.ACCOUNT_TYPE_WATER.getIntV());
			if(count == 0){
				str += "所有资产不存在水费账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在水费账户,所交费用会进入其他资产水费账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getElectAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndTypeNew(buildingCodes,BillingEnum.ACCOUNT_TYPE_ELECT.getIntV());
			if(count == 0){
				str += "所有资产不存在电费账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在电费账户,所交费用会进入其他资产电费账户. \r\n";
			}
		}
		if(CommonUtils.null2Double(info.getCommonAmount()) > 0){
			count = this.tBsAssetAccountMapper.getAccountsByBuildingCodesAndTypeNew(buildingCodes, BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());
			if(count == 0){
				str += "所有资产不存在通用账户账户,无法交费. \r\n";
				flag = false;
			}else if(count < totalCount){
				str += "部分资产不存在通用账户,所交费用会进入其他资产通用账户. \r\n";
			}
		}

		if(CommonUtils.isEmpty(str)){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"账户检测通过,当前可交费.");
		}else{
			return new MessageMap((!flag) ? MessageMap.INFOR_ERROR : MessageMap.INFOR_WARNING,str);
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto findHistory(String companyId,TBsPayInfo info) {
		return new BaseDto(this.tBsPayInfoMapper.listPageHistory(info), info.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageHistory4Building(String companyId, TBsPayInfo info) {
		List<TJgStaffGrop> grops = this.tJgStaffGropMapper.findCanPayRole(info.getCreateId());
		if(CommonUtils.isEmpty(grops)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有退款/减免权限 ,请联系您的上级在 [基础配置] -> [银账交割] -> [组织结构设计] 中添加您为收银员/收银组长. "));
		}else{
			List<TBsPayInfo> infos = this.tBsPayInfoMapper.listPageHistory4Building(info);
			addPath(infos);
			return new BaseDto(infos,info.getPage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageHistory4Cust(String companyId, TBsPayInfo info) {
		List<TJgStaffGrop> grops = this.tJgStaffGropMapper.findCanPayRole(info.getCreateId());
		if(CommonUtils.isEmpty(grops)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有退款/减免权限 ,请联系您的上级在 [基础配置] -> [银账交割] -> [组织结构设计] 中添加您为收银员/收银组长. "));
		}else{
			List<TBsPayInfo> infos = this.tBsPayInfoMapper.listPageHistory4Cust(info);
			addPath(infos);
			return new BaseDto(infos,info.getPage());
		}
	}

	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto backMoney(String companyId, TBsPayInfo info) {

		String buildingCode = info.getBuildingCode();
		List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.getAccountsByBuildingCode(buildingCode);
		if(CommonUtils.isEmpty(accounts)){
			logger.warn("未找到{}建筑对应的账户数据.",buildingCode);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到对应的账户数据."));
		}

		//判断该户是否处于托收期间
		info.setBuildingCodes(Arrays.asList(buildingCode));
		String fullNames = checkCollingDatas(info);
		if(CommonUtils.isNotEmpty(fullNames)){
			logger.warn("{}建筑处于托收状态,无法退款.",buildingCode);
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "处于  [托收待回盘] 状态 , 当前无法使用账户  [退款] 功能."));
		}

		boolean flag = true;
		String str = null;
		String accountStr = null;
		for(TBsAssetAccount account : accounts){
			double accountBalance = account.getAccountBalance();

			if( CommonUtils.isNotEmpty( account.getProjectName() ) ) {
				info.setProjectName( account.getProjectName() );
			}

			/*if(account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()
					&& info.getWyAmount() > 0
					&& CommonUtils.null2Double(info.getWyAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_WY;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getWyAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()
					&& info.getBtAmount() > 0
					&& CommonUtils.null2Double(info.getBtAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_BT;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getBtAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()
					&& info.getWaterAmount() > 0
					&& CommonUtils.null2Double(info.getWaterAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_WATER;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getWaterAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()
					&& info.getElectAmount() > 0
					&& CommonUtils.null2Double(info.getElectAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_ELECT;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getElectAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()
					&& info.getCommonAmount() > 0
					&& CommonUtils.null2Double(info.getCommonAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_COMMON;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getCommonAmount() + "] .";
				break;
			}*/
			//后面直接取消了专项账户，所以退费的话，专项账户不存在的，这里要改 2018-11-20
			if(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()
					&& info.getCommonAmount() > 0
					&& CommonUtils.null2Double(info.getCommonAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_COMMON;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getCommonAmount() + "] .";
				break;
			}

		}
		if(!flag){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "当前建筑下的 [" + str + "]余额为: [" + accountStr));
		}

		TSysUserSearch condition = new TSysUserSearch();
		condition.setUserId(info.getCreateId());
		List<TSysUserList> users = this.tSysUserMapper.findByCondition(condition);
		String staffName = (users.isEmpty()) ? Constants.STR_EMPTY : users.get(0).getStaffName();
		String staffNumber = (users.isEmpty()) ? Constants.STR_EMPTY : users.get(0).getStaffNumber();


		//经过校验
		//组装payInfo数据,组装银账交割数据, 组装account数据
		String projectName = null;
		for(TBsAssetAccount account : accounts){
			double backAmount = (account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()) ? info.getWyAmount() :
					(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()) ? info.getBtAmount() :
							(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()) ? info.getWaterAmount() :
									(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()) ? info.getElectAmount() :
											(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()) ? info.getCommonAmount() : 0;

			if(CommonUtils.isEmpty(backAmount))
				continue;
			account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) - CommonUtils.null2Double(backAmount));
			account.setModifyId(info.getCreateId());
			account.setModifyTime(new Date());
			info.setProjectId(account.getProjectId());
			projectName = account.getProjectName();

			this.tBsAssetAccountMapper.update(account);
			//插入流水
			this.tBsAssetAccountStreamMapper.singleInsert(
					new TBsAssetAccountStream(CommonUtils.getUUID(),
							account.getId(),
							-backAmount,
							new Date(),
							info.getCreateId(),
							staffName,
							StreamEnum.purpose_back.getV())  //退款
			);
		}


		//在这里作出一个调整就是一次交多个房的时候，交易流水号相同，之前不同，导致后面新账户无法实现关联（实际情况一次交多房很少）
		String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9);

		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据S（房号-- 老账户的buildingCode  每个账户的金额）

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("oprId", info.getCreateId());
		paramMap.put("projectId", info.getProjectId());
		paramMap.put("projectName", info.getProjectName());
		paramMap.put("oprDetailId", batchNo);
		//paramMap.put("houseCodeNew",buildingCodeModifyHoustCode(buildingCode));
		Map<String, String> amountMap = new HashMap<>();
		amountMap.put("wyAmount", info.getWyAmount().toString() );
		amountMap.put("btAmount", info.getBtAmount().toString());
		amountMap.put("waterAmount",info.getWaterAmount().toString());
		amountMap.put("electAmount",info.getElectAmount().toString());
		amountMap.put("commonAmount",info.getCommonAmount().toString());
		paramMap.put("amountMap", amountMap);
		mqSender.sendDepositAccountRefund(paramMap,companyId);
		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据E


		//组装payInfo数据, 退款不需要计算税金
		info.setId(CommonUtils.getUUID());
		info.setCreateTime(new Date());
		info.setModifyTime(new Date());
		info.setBatchNo(batchNo);
		info.setModifyId(info.getCreateId());
		info.setRelationId(CommonUtils.getUUID());
		info.setWyAmount(0.0);
		info.setBtAmount(0.0);
		info.setWaterAmount(0.0);
		info.setElectAmount(0.0);
		this.tBsPayInfoMapper.insert(info);
		List <AcBusinessOperaDetailDto> acBusinessOperaDetailDtoList= new ArrayList<>();
		acBusinessOperaDetailDtoList.add(getAcBusinessOperOperaDetailDto(info,BusinessType.REFUND,companyId));
		mqSender.sendBusinessOperas(acBusinessOperaDetailDtoList,companyId);
		//组装银账交割数据
		TJgAccountReceivable tar = payInfo2Tar(info, staffName, staffNumber, projectName, 2);
		tar.setId(info.getRelationId());
		tar.setBusinessType(4);
		this.tJgAccountReceivableMapper.addAccountReceivable(tar);

		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"退款成功"));
	}



	@Override
	public BaseDto jmMoney(String companyId, TBsPayInfo info) {

		String buildingCode = info.getBuildingCode();
		List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.getAccountsByBuildingCode(buildingCode);
		if(CommonUtils.isEmpty(accounts)){
			logger.warn("未找到{}建筑对应的账户数据.",buildingCode);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到对应的账户数据."));
		}

		for (TBsAssetAccount account:accounts ){
			if (account.getType()==0){
				continue;
			}

			if( CommonUtils.isNotEmpty( account.getProjectName() ) ) {
				info.setProjectName( account.getProjectName() );
			}

    //   判读是否减免本金
			/*if (account.getType()==1) {
				double jmff = this.tBsOwedHistoryMapper.findSumByBuildingCodeAndType(buildingCode,account.getType());
				if (jmff<info.getWyAmount()){
					logger.warn("{}物业减免金额大于违约金",buildingCode);
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"物业减免金额大于违约金"));
				}
			}
			if (account.getType()==2) {
				double jmff = this.tBsOwedHistoryMapper.findSumByBuildingCodeAndType(buildingCode, account.getType());
				if (jmff<info.getBtAmount()){
					logger.warn("{}本体减免金额大于违约金",buildingCode);
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"本体减免金额大于违约金"));
				}
			}
			if (account.getType()==3) {
				double jmff = this.tBsOwedHistoryMapper.findSumByBuildingCodeAndType(buildingCode, account.getType());
				if (jmff<info.getWaterAmount()){
					logger.warn("{}水费减免金额大于违约金",buildingCode);
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"水费减免金额大于违约金"));
				}
			}
			if (account.getType()==4) {
				double jmff = this.tBsOwedHistoryMapper.findSumByBuildingCodeAndType(buildingCode, account.getType());
				if (jmff<info.getElectAmount()){
					logger.warn("{}电费减免金额大于违约金",buildingCode);
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"电费减免金额大于违约金"));
				}
			}*/

		}

		//判断该户是否处于托收期间
		info.setBuildingCodes(Arrays.asList(buildingCode));
		String fullNames = checkCollingDatas(info);
		if(CommonUtils.isNotEmpty(fullNames)){
			logger.warn("{}建筑处于托收状态,无法退款.",buildingCode);
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "处于  [托收待回盘] 状态 , 当前无法使用账户  [减免] 功能."));
		}

		boolean flag = true;
		String accountStr = Constants.STR_EMPTY;

		for(TBsAssetAccount account : accounts){
			double accountBalance = account.getAccountBalance();

			if(account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()){
				if(CommonUtils.isNotEmpty(info.getWyAmount())){
					if(accountBalance >= 0){
						flag = false;
						accountStr = "物业管理费 >>> 账户余额: [" + accountBalance + "] 为正, 无法减免. ";
						break;
					}else if(CommonUtils.null2Double(info.getWyAmount()) > Math.abs(accountBalance)){
						flag = false;
						accountStr = "物业管理费 >>> 减免金额: [" + info.getWyAmount() + "] 超出最大可抵消余额: [" + accountBalance + "] , 减免失败, 请检查. ";
						break;
					}
				}
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()){
				if(CommonUtils.isNotEmpty(info.getBtAmount())){
					if(accountBalance >= 0){
						flag = false;
						accountStr = "本体基金 >>> 账户余额: [" + accountBalance + "] 为正, 无法减免. ";
						break;
					}else if(CommonUtils.null2Double(info.getBtAmount()) > Math.abs(accountBalance)){
						flag = false;
						accountStr = "本体基金 >>> 减免金额: [" + info.getBtAmount() + "] 超出最大可抵消余额: [" + accountBalance + "] , 减免失败, 请检查. ";
						break;
					}
				}
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()){
				if(CommonUtils.isNotEmpty(info.getWaterAmount())){
					if(accountBalance >= 0){
						flag = false;
						accountStr = "水费 >>> 账户余额: [" + accountBalance + "] 为正, 无法减免. ";
						break;
					}else if(CommonUtils.null2Double(info.getWaterAmount()) > Math.abs(accountBalance)){
						flag = false;
						accountStr = "水费 >>> 减免金额: [" + info.getWaterAmount() + "] 超出最大可抵消余额: [" + accountBalance + "] , 减免失败, 请检查. ";
						break;
					}
				}
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()){
				if(CommonUtils.isNotEmpty(info.getElectAmount())){
					if(accountBalance >= 0){
						flag = false;
						accountStr = "电费 >>> 账户余额: [" + accountBalance + "] 为正, 无法减免. ";
						break;
					}else if(CommonUtils.null2Double(info.getElectAmount()) > Math.abs(accountBalance)){
						flag = false;
						accountStr = "电费 >>> 减免金额: [" + info.getElectAmount() + "] 超出最大可抵消余额: [" + accountBalance + "] , 减免失败, 请检查. ";
						break;
					}
				}
			}
		}
		if(!flag)
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,accountStr));



		//账户扣减,以及流水插入
		TSysUserSearch condition = new TSysUserSearch();
		condition.setUserId(info.getCreateId());
		List<TSysUserList> users = this.tSysUserMapper.findByCondition(condition);
		String staffName = (users.isEmpty()) ? "" : users.get(0).getStaffName();
		for(TBsAssetAccount account : accounts){
			double jmAmount = (account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()) ? info.getWyAmount() :
					(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()) ? info.getBtAmount() :
							(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()) ? info.getWaterAmount() :
									(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()) ? info.getElectAmount() :
											(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()) ? info.getCommonAmount() : 0;

			if(CommonUtils.isEmpty(jmAmount))
				continue;

			account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) + CommonUtils.null2Double(jmAmount));
			account.setModifyId(info.getCreateId());
			account.setModifyTime(new Date());

			//减免违约金
			TBsChargeBillHistory history = this.tBsChargeBillHistoryMapper.selectNotBillingByObj(account.getBuildingCode(),account.getType());

			//减免, 不跳过违约金
			oprLateFee(account, jmAmount, history, Constants.STR_YES,info);

			this.tBsAssetAccountMapper.update(account);

			this.tBsAssetAccountStreamMapper.singleInsert(new TBsAssetAccountStream(CommonUtils.getUUID(),
							account.getId(),
							jmAmount,
							new Date(),
							info.getCreateId(),
							staffName,
							StreamEnum.purpose_jm.getV()  //减免
					)
			);
		}


		String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9);

		//这里添加减免违约金然后写入到新账户的额数据*******************S

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		//paramMap.put("houseCodeNew",buildingCodeModifyHoustCode(buildingCode) );
		paramMap.put("projectId",info.getProjectId() );
		paramMap.put("oprDetailId", batchNo);
		paramMap.put("projectName",info.getProjectName() );
		paramMap.put("oprId", (users.isEmpty()) ? "" : users.get(0).getUserId());

		Map<String, String> amountMap = new HashMap<>();
		amountMap.put("wyJmAmount", CommonUtils.null2String( info.getWyAmount() ));
		amountMap.put("btJmAmount", CommonUtils.null2String( info.getBtAmount() ));
		amountMap.put("waterJmAmount", CommonUtils.null2String( info.getWaterAmount() ));
		amountMap.put("electJmAmount", CommonUtils.null2String( info.getElectAmount() ));
		paramMap.put("amountMap",amountMap );
		mqSender.sendLateFeeReduction(paramMap,companyId);

		//这里添加减免违约金然后写入到新账户的额数据*******************E


		//插入payInfos
		info.setId(CommonUtils.getUUID());
		info.setCreateTime(new Date());
		info.setModifyTime(new Date());
		info.setModifyId(info.getCreateId());
		info.setBatchNo(batchNo);
		List <AcBusinessOperaDetailDto> acBusinessOperaDetailDtoList= new ArrayList<>();
		acBusinessOperaDetailDtoList.add(getAcBusinessOperOperaDetailDto(info,BusinessType.REGRESSES,companyId));
		mqSender.sendBusinessOperas(acBusinessOperaDetailDtoList,companyId);
		this.tBsPayInfoMapper.insert(info);

		//减免不需要插入到银账交割

		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"减免成功."));
	}


	private Map<String, String> getTaxRateInfoToMap(List<Map<String, String>> taxList,String projectName) {
		//每一条map数据为一个收费类型的费率（如果有的话）
		Map<String, String> raxmap=new HashMap<>();
		raxmap.put("projectName", projectName);
		if(CommonUtils.isNotEmpty(taxList) && taxList.size()>0) {
			for (Map<String, String> map : taxList) {
				raxmap.put(map.get("schemeType"), String.valueOf( map.get("taxTate") ));
			}
		}
		return raxmap;

	}

	private void addPath(List<TBsPayInfo> infos){
		if(CommonUtils.isEmpty(infos))
			return;
		for(TBsPayInfo info : infos){
			if(CommonUtils.isNotEmpty(info.getAnnexs())){
				for(Annex annex : info.getAnnexs()){
					annex.setAnnexAddress(this.fastDFSApi.getImgBaseUri() + annex.getAnnexAddress());
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto addInvoices(String companyId, TBsPayInfo info) {
		if(null == info || CommonUtils.isEmpty(info.getAnnexs())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空,添加发票失败."));
		}

		List<Annex> annexs = info.getAnnexs();
		for(Annex annex : annexs){
			annex.setAnnexId(CommonUtils.getUUID());
			annex.setAnnexTime(CommonUtils.getDateStr());
			annex.setFileType(AnnexEnum.annex_file_type_img.getStringV());
			annex.setIsUsed(AnnexEnum.annex_is_used_yes.getIntV());
			annex.setProjectId(info.getProjectId());
			annex.setRelationId(info.getId());
			this.annexMapper.insertAnnex(annex);
		}

		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"添加发票成功."));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto rollback(String companyId, TBsPayInfo info) {
		MessageMap msgMap = new MessageMap();
		msgMap.setFlag(MessageMap.INFOR_WARNING);
		if(null == info){
			msgMap.setMessage("传入参数为空,回退流水失败.");
			return new BaseDto(msgMap);
		}else{

			info = this.tBsPayInfoMapper.selectByPrimaryKey(info.getId());
			TJgAccountReceivable tar = this.tJgAccountReceivableMapper.findById(info.getRelationId());
			if(tar == null){
				msgMap.setMessage("未找到该笔流水对应的银账交割记录");
				return new BaseDto(msgMap);
			}else if(tar.getStatus() == SettlementEnum.status_completed.getIntValue()){
				msgMap.setMessage("本次流水已处于银账交割结算步骤, 无法退款. ");
				return new BaseDto(msgMap);
			}else if(tar.getStatus() == SettlementEnum.status_backed.getIntValue()){
				msgMap.setMessage("本次流水已处于银账交割退款完成步骤, 无法重复退款. ");
				return new BaseDto(msgMap);
			}else{
				//判断该户是否处于托收期间
				String buildingCode = info.getBuildingCode();
				info.setBuildingCodes(Arrays.asList(buildingCode));
				String fullNames = checkCollingDatas(info);
				if(CommonUtils.isNotEmpty(fullNames)){
					logger.warn("{}建筑处于托收状态,无法退款.",buildingCode);
					return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "处于  [托收待回盘] 状态 , 当前无法使用账户  [退款] 功能."));
				}

				TBsPayInfo tBsPayInfo= this.tBsPayInfoMapper.findToBatchNo(info.getBatchNo());
				if (tBsPayInfo!=null){
					List<TBsPayInfo> tBsPayInfoList =this.tBsPayInfoMapper.findToTimeAndBuildingCode(tBsPayInfo.getCreateTime(),tBsPayInfo.getBuildingCode());
					if (!CommonUtils.isEmpty(tBsPayInfoList)){

						List<TBsAssetAccount> accountList=this.tBsAssetAccountMapper.queryAccountSituationByBuildCode(info.getBuildingCode());
						for (TBsAssetAccount account :accountList){
							if (account.getType()==BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()){
								if (account.getAccountBalance()<info.getCommonAmount()){
									logger.warn("{}建筑之前有退款记录.账户余额小于回退金额",buildingCode);
									return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "通用账户账户之前有退款记录 余额小于回退金额, 当前无法使用账户  [退款] 功能."));
								}
							}else  if (account.getType()==BillingEnum.ACCOUNT_TYPE_BT.getIntV()){
								if (account.getAccountBalance()<info.getBtAmount()){
									logger.warn("{}建筑之前有退款记录.账户余额小于回退金额",buildingCode);
									return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "本体账户之前有退款记录 余额小于回退金额, 当前无法使用账户  [退款] 功能."));
								}
							}else  if (account.getType()==BillingEnum.ACCOUNT_TYPE_WATER.getIntV()){
								if (account.getAccountBalance()<info.getWyAmount()){
									logger.warn("{}建筑之前有退款记录.账户余额小于回退金额",buildingCode);
									return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "水费账户之前有退款记录 余额小于回退金额, 当前无法使用账户  [退款] 功能."));
								}
							}else  if (account.getType()==BillingEnum.ACCOUNT_TYPE_WY.getIntV()){
								if (account.getAccountBalance()<info.getWyAmount()){
									logger.warn("{}建筑之前有退款记录.账户余额小于回退金额",buildingCode);
									return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "物业账户之前有退款记录 余额小于回退金额, 当前无法使用账户  [退款] 功能."));
								}
							}else  if (account.getType()==BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()){
								if (account.getAccountBalance()<info.getElectAmount()){
									logger.warn("{}建筑之前有退款记录.账户余额小于回退金额",buildingCode);
									return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "电费账户之前有退款记录 余额小于回退金额, 当前无法使用账户  [退款] 功能."));
								}
							}

						}


					}
				}
				TSysUserSearch condition = new TSysUserSearch();
				condition.setUserId(info.getCreateId());
				List<TSysUserList> users = this.tSysUserMapper.findByCondition(condition);
				String staffName = (CollectionUtils.isEmpty(users)) ? Constants.STR_EMPTY : (null == users.get(0)) ? Constants.STR_EMPTY : users.get(0).getStaffName();

				//1. 银账交割交易流水修改为退款状态
				//1.1 判断本次退费是否为整笔流水?
				if(CommonUtils.getSum(tar.getPayCash(),tar.getPayUnion(),tar.getPayWx(),tar.getAlipay(),tar.getBankReceipts())
						!= CommonUtils.getSum(info.getPayCash(),info.getPayUnion(),info.getPayWx(),info.getPayZfb(),info.getPayBank())){
					//不相等, 原流水减相应的值,状态不变
					tar.setPayWx(String.valueOf(CommonUtils.getGap(tar.getPayWx(), info.getPayWx())));
					tar.setPayUnion(String.valueOf(CommonUtils.getGap(tar.getPayUnion(), info.getPayUnion())));
					tar.setPayCash(String.valueOf(CommonUtils.getGap(tar.getPayCash(), info.getPayCash())));
					tar.setAlipay(String.valueOf(CommonUtils.getGap(tar.getAlipay(), info.getPayZfb())));
					tar.setBankReceipts(String.valueOf(CommonUtils.getGap(tar.getBankReceipts(), info.getPayBank())));

					//新建一条退费银账交割流水,与支付流水对应上
					String staffNumber = (CollectionUtils.isEmpty(users)) ? Constants.STR_EMPTY : (null == users.get(0)) ? Constants.STR_EMPTY : users.get(0).getStaffNumber();

					TSysProject project = this.tSysProjectMapper.findByCode(info.getProjectId());

					TJgAccountReceivable newTar = payInfo2Tar(info, staffName, staffNumber, project.getName(), SettlementEnum.status_waiting.getIntValue());	//退费状态流水
					newTar.setId(CommonUtils.getUUID());
					newTar.setBusinessType(4);
					info.setRelationId(newTar.getId());

					this.tJgAccountReceivableMapper.addAccountReceivable(newTar);
					this.tJgAccountReceivableMapper.modify(tar);

				}else{
					//相等,不应该将该笔流水置为退款状态, 该笔流水直接删掉
//					tar.setStatus(SettlementEnum.status_backed.getIntValue());
					this.tJgAccountReceivableMapper.deleteById(tar.getId());
				}

				//2. 账户回退, 现金, 微信/支付宝, 刷卡回退
				List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.getAccountsByBuildingCode(info.getBuildingCode());
				for(TBsAssetAccount account : accounts){
					double balance = account.getAccountBalance();
					int type = account.getType();
					double amount = 0.0;
					if(type == BillingEnum.ACCOUNT_TYPE_WY.getIntV() && info.getWyAmount() > 0){
						amount = info.getWyAmount();

					}else if(type == BillingEnum.ACCOUNT_TYPE_BT.getIntV() && info.getBtAmount() > 0){
						amount = info.getBtAmount();

					}else if(type == BillingEnum.ACCOUNT_TYPE_WATER.getIntV() && info.getWaterAmount() > 0){
						amount = info.getWaterAmount();

					}else if(type == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV() && info.getElectAmount() > 0){
						amount = info.getElectAmount();

					}else if(type == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV() && info.getCommonAmount() > 0){
						amount = info.getCommonAmount();

					}

					if(CommonUtils.null2Double(amount) == 0) continue;   //回退的时候 如果出现无金额流水,不再往账户插入空流水

					account.setAccountBalance(balance - amount);
					this.tBsAssetAccountMapper.update(account);

					//3. 账户流水
					this.tBsAssetAccountStreamMapper.singleInsert(new TBsAssetAccountStream(
									CommonUtils.getUUID(),
									account.getId(),
									-amount,
									new Date(),
									info.getCreateId(),
									staffName,
									StreamEnum.purpose_rollback.getV()	//回滚
							)
					);

					//4. 欠费信息回写
					double dkAmount = amount;
					TBsChargeBillHistory history = this.tBsChargeBillHistoryMapper.selectNotBillingByObj(info.getBuildingCode(), type);
					if(null != history){

						TBsOwedHistory oh=tBsOwedHistoryMapper.findUpDateByAccountId(account.getId());
						Double lateFee = (type == 1) ? CommonUtils.null2Double(info.getWyLateFee()) :
								(type == 2) ? CommonUtils.null2Double(info.getBtLateFee()) :
										(type == 3) ? CommonUtils.null2Double(info.getWaterLateFee()) :
												(type == 4) ? CommonUtils.null2Double(info.getElectLateFee()) : 0.0;
						if(lateFee > 0){

							if(oh.getIsUsed() == BillingEnum.IS_USED_STOP.getIntV()){
								//交清时间为交费时间同一天
								if(CommonUtils.isEquals(CommonUtils.getDateStr(oh.getOwedEndTime(), TIME_YMH_STR),
										CommonUtils.getDateStr(info.getCreateTime(), TIME_YMH_STR))){
									//该笔做退回
									oh.setOwedEndTime(null);
									oh.setIsUsed(BillingEnum.IS_USED_USING.getIntV());

									oh.setTotalLateFee(oh.getTotalLateFee()+lateFee);
								}
							}else {
								oh.setTotalLateFee(oh.getTotalLateFee()+lateFee);
							}

							oh.setModifyId(info.getCreateId());
							this.tBsOwedHistoryMapper.update(oh);
						}
//						List<HashMap> lastOwedInfos = JSONObject.parseArray(history.getLastOwedInfo(), HashMap.class);
//						if(CommonUtils.isNotEmpty(lastOwedInfos)){
//							for(HashMap map : lastOwedInfos){
//
//								if(dkAmount <= 0) break;
//
//								TBsOwedHistory oh = this.tBsOwedHistoryMapper.findUsingDataById(CommonUtils.null2String(map.get("id")), null);
//
//								if(oh == null) continue;
//
//
//								Double lateFee = (type == 1) ? CommonUtils.null2Double(info.getWyLateFee()) :
//														(type == 2) ? CommonUtils.null2Double(info.getBtLateFee()) :
//															(type == 3) ? CommonUtils.null2Double(info.getWaterLateFee()) :
//																(type == 4) ? CommonUtils.null2Double(info.getElectLateFee()) : 0.0;
//
//								//如果违约金大于0的流水进行回退, 则需要对流水进行处理
//								//全部交清的,统统回退, 判断时间
//								//未交清的, 回退一部分
//								if(lateFee > 0){
//									if(oh.getIsUsed() == BillingEnum.IS_USED_STOP.getIntV()){
//										//交清时间为交费时间同一天
//										if(CommonUtils.isEquals(CommonUtils.getDateStr(oh.getOwedEndTime(), TIME_YMH_STR),
//												CommonUtils.getDateStr(info.getCreateTime(), TIME_YMH_STR))){
//											//该笔做退回
//											oh.setOwedEndTime(null);
//											oh.setIsUsed(BillingEnum.IS_USED_USING.getIntV());
//											oh.setTotalLateFee(lateFee);
//											double total = oh.getOwedAmount() + oh.getTotalLateFee();
//											dkAmount -= (dkAmount > total) ? total : dkAmount;
//
//											map.put("isUsed", Constants.IS_USED_USING);
//											map.put("owedEndTime", null);
//										}
//									}else{
//										//未全交清的,退回一部分
//										double dkLateFee = lateFee;
//										double dkOwedAmount = CommonUtils.null2Double(map.get("dkOwedAmount"));
//										Date oprTime = (CommonUtils.isEmpty(map.get("oprTime"))) ? null : new Date((Long)map.get("oprTime"));
//
//										//操作时间与充值是同一天
//										if(CommonUtils.isEquals(CommonUtils.getDateStr(oprTime, TIME_YMH_STR),
//												CommonUtils.getDateStr(info.getCreateTime(), TIME_YMH_STR))){
//
//											if(dkLateFee > 0){
//												dkAmount -= dkLateFee;
//												map.put("totalLateFee", CommonUtils.null2Double(map.get("totalLateFee")) + dkLateFee);
//												map.put("dkLateFee", 0.0);
//												oh.setTotalLateFee(oh.getTotalLateFee() + lateFee);
//											}
//											if(dkOwedAmount > 0){
//												dkOwedAmount = (dkAmount > dkOwedAmount) ? dkOwedAmount : dkAmount;
//												dkAmount -= dkOwedAmount;
//												map.put("owedAmount", CommonUtils.null2Double(map.get("owedAmount")) + dkOwedAmount);
//												map.put("dkOwedAmount", 0.0);
//												oh.setOwedAmount(oh.getOwedAmount() + dkOwedAmount);
//											}
//										}
//									}
//									oh.setModifyId(info.getCreateId());
//									this.tBsOwedHistoryMapper.update(oh);
//								}
//							}
//							history.setLastOwedInfo(JSONArray.toJSONString(lastOwedInfos));
//						}
					}

					//5. 本期账单和下期账单
					//判断账户扣减后是否小于等于0, 若小于等于0 , 表示充钱之前, 账户为负数
					if(null != history && type != BillingEnum.ACCOUNT_TYPE_COMMON.getIntV() && account.getAccountBalance() <= 0){
						double lastPayed = CommonUtils.null2Double(history.getLastPayed());
						if(lastPayed >= 0){
							history.setLastPayed((lastPayed >= amount) ? CommonUtils.getGap(lastPayed, amount) : 0);

							TBsChargeBillHistory lastHistory = this.tBsChargeBillHistoryMapper.findById(history.getLastBillId());
							if(null != lastHistory){
								double noCommonPayed = CommonUtils.null2Double(lastHistory.getNoCommonDesummoney());
								lastHistory.setNoCommonDesummoney((noCommonPayed >= amount) ? CommonUtils.getGap(noCommonPayed, amount) : 0 );
								this.tBsChargeBillHistoryMapper.updateBillHistory(lastHistory);
							}
							this.tBsChargeBillHistoryMapper.updateBillHistory(history);
						}
					}

					//6.本次支付流水状态修改
					info.setStatus(3); //改为退款状态
					info.setModifyTime(new Date());
					info.setModifyId(info.getCreateId());
					this.tBsPayInfoMapper.updateByPrimaryKey(info);


				}
			}
		}

		//******前台操作回退功能，写入数据到新账户S**********

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		//paramMap.put("houseCodeNew",buildingCodeModifyHoustCode(info.getBuildingCode()) );
		paramMap.put("projectId",info.getProjectId());
		paramMap.put("operaId", info.getBatchNo());
		Map<String, String> amountMap = new HashMap<>();
		amountMap.put("wyAmount", info.getWyAmount().toString() );
		amountMap.put("btAmount", info.getBtAmount().toString());
		amountMap.put("waterAmount",info.getWaterAmount().toString());
		amountMap.put("electAmount",info.getElectAmount().toString());
		amountMap.put("commonAmount",info.getCommonAmount().toString());
		amountMap.put("jmAmount",info.getJmAmount().toString());
		paramMap.put("amountMap", amountMap);
		mqSender.sendRollbackOperation(paramMap,companyId);


		List <AcBusinessOperaDetailDto> acBusinessOperaDetailDtoList= new ArrayList<>();
		acBusinessOperaDetailDtoList.add(getAcBusinessOperOperaDetailDto(info,BusinessType.REGRESSES,companyId));
		mqSender.sendBusinessOperas(acBusinessOperaDetailDtoList,companyId);
		//******前台操作回退功能，写入数据到新账户E**********
		msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		msgMap.setMessage("账户回退成功! ");
		return new BaseDto(msgMap);
	}

	@Override
	public BaseDto findDatas(String companyId, TBsPayInfo info) {

		Map<Integer, List<TBsPayInfo>> resultMap = new HashMap<Integer, List<TBsPayInfo>>();

		//0 收款  1 退款 2减免  3回退
		List<TBsPayInfo> infos = null;
		for(int i = 0 ; i <=3 ; i++){
			info.setStatus(i);

			infos = this.tBsPayInfoMapper.findDatas(info);
			if(!CommonUtils.isEmpty(infos)){
				resultMap.put(i, infos);
			}
		}
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(resultMap);
		return returnDto;
	}

	@Override
	public BaseDto findExportBillingDatas(String companyId, TBsPayInfo info, Integer type) {
		BaseDto returnDto = new BaseDto();
		Map<String,Object> resultMap = new HashMap<String,Object>();

		TSysProject project = this.tSysProjectMapper.findByCode(info.getProjectId());
		resultMap.put("projectName", (null == project) ? Constants.STR_EMPTY : project.getName());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		if(CommonUtils.isEmpty(info.getCreateTime())) info.setCreateTime(new Date());

		List<TBsPayInfo> infos = null;
		if(type == 1){
			//某月为周期的计费数据详情
			resultMap.put("startTime", sdf.format(CommonUtils.getFirstDayInMonth(info.getCreateTime())));
			resultMap.put("endTime", sdf.format(CommonUtils.getLastDayInMonth(info.getCreateTime())));
			infos = this.tBsPayInfoMapper.findBillingsDataByObj(info);
		}else{
			//当天的账户余额详情
			resultMap.put("startTime", sdf.format(info.getCreateTime()));
			resultMap.put("endTime", sdf.format(info.getCreateTime()));
			infos = this.tBsPayInfoMapper.findAccountDatasByObj(info);
		}

		resultMap.put("datas", infos);

		returnDto.setObj(resultMap);
		return returnDto;
	}

	@Override
	public BaseDto findExportPayInfoDatas(String companyId, TBsPayInfo info) {
		BaseDto returnDto = new BaseDto();
		Map<String,Object> resultMap = new HashMap<String,Object>();

		TSysProject project = this.tSysProjectMapper.findByCode(info.getProjectId());
		resultMap.put("projectName", (null == project) ? Constants.STR_EMPTY : project.getName());
		resultMap.put("startTime", CommonUtils.getDateStr(info.getStartTime(), "yyyy年MM月dd日"));
		resultMap.put("endTime", CommonUtils.getDateStr(info.getEndTime(), "yyyy年MM月dd日"));


		List<TBsPayInfo> infos = this.tBsPayInfoMapper.findExportPayInfoDatasByObj(info);

		resultMap.put("datas", infos);

		returnDto.setObj(resultMap);
		return returnDto;
	}

	@Override
	public BaseDto findTotalDatasByObj(String companyId, TBsPayInfo info) {
		//这里之前在导出的时候涉及到一个问题是：没有考虑退款这种操作对收银组长报表的影响   应该减掉退款这个情况
		List<TBsPayInfo> infos = new ArrayList();
		TBsPayInfo in = null;
		TBsPayInfo inRefund = null;
		for(int i = 0 ; i <= 4 ; i++){
			info.setPayType(i);
			in = this.tBsPayInfoMapper.findTotalDatasByObj(info);
			inRefund = tBsPayInfoMapper.findTotalDatasForRefund(info);
			if(CommonUtils.isNotEmpty( in )  && CommonUtils.isNotEmpty( inRefund ) ) {
				double payCash = CommonUtils.null2Double( in.getPayCash() );
				double payCashRefund = CommonUtils.null2Double( inRefund.getPayCash() );
				in.setPayCash(payCash-payCashRefund );

			}
			if(null != in){
				infos.add(in);
			}
		}
		return new BaseDto(infos, null);
	}

	@Override
	public BaseDto financeWXAppletsMoney(String companyId, TBsPayInfo info) {
		//这里导出是微信小程序
		Double wx=this.tBsPayInfoMapper.findWXAppletsMoney(info);
		return new BaseDto(wx);
	}


	private String checkCollingDatas(TBsPayInfo info){
		if(null == info) return null;

		List<TJgStaffGrop> grops = this.tJgStaffGropMapper.findCanPayRole(info.getCreateId());
		if(CommonUtils.isEmpty(grops)) return null;

		List<String> buildingCodes = null;

		if(CommonUtils.isEmpty(info.getCustType())){
			//从建筑角度进入, 直接获取buildingCode
			if(CommonUtils.isEmpty(info.getBuildingCode())) return null;
			buildingCodes = new ArrayList<String>();
			buildingCodes.add(info.getBuildingCode());
		}else{
			//从客户角度进入, 判断buildingCodes
			if(CommonUtils.isEmpty(info.getBuildingCodes())){
				//表示为全部缴纳
				buildingCodes = this.personBuildingNewMapper.getBuildingCodesByCustIdAndChargerId(info.getCustId(), info.getCreateId());
			}else{
				buildingCodes = info.getBuildingCodes();
			}
		}

		if(CommonUtils.isEmpty(buildingCodes)) return null;

		List<String> fullNames = null;
		String projectId = grops.get(0).getProjectId();
		TBcProject project = this.tBcProjectMapper.findByProjectId(projectId);
		if(project != null && project.getStatus() == CollectionEnum.status_on.getV()){

			Integer collectionType = null;
			if(collectionType == null && project.getWyStatus() == CollectionEnum.status_on.getV() && project.getWyType() != CollectionEnum.type_off.getV()){
				collectionType = project.getWyType();
			}else if(collectionType == null && project.getBtStatus() == CollectionEnum.status_on.getV() && project.getBtType() != CollectionEnum.type_off.getV()){
				collectionType = project.getBtType();
			}else if(collectionType == null && project.getWaterStatus() == CollectionEnum.status_on.getV() && project.getWaterType() != CollectionEnum.type_off.getV()){
				collectionType = project.getWaterType();
			}else if(collectionType == null && project.getElectStatus() == CollectionEnum.status_on.getV() && project.getElectType() != CollectionEnum.type_off.getV()){
				collectionType = project.getElectType();
			}
			if(collectionType != null) {
				TBcCollectionTotal total = this.tBcCollectionMapper.findRecentTotal(projectId, collectionType);

				if(CommonUtils.isEquals(Constants.STR_YES, total.getIsWaitBack())){
					//是待回盘状态,判断buildingCodes是否尚处于托收状态
					if(collectionType == CollectionEnum.type_union.getV()){
						//银联
						fullNames = this.tBcUnionBackBodyMapper.findCollingDatasByTotalId(total.getId(),buildingCodes);
					}else if(collectionType == CollectionEnum.type_jrl.getV()){
						//金融联
						fullNames = this.tBcJrlBodyMapper.findCollingDatasByTotalId(total.getId(),buildingCodes);
					}
				}
			}
		}
		return (CommonUtils.isEmpty(fullNames)) ? null : StringUtils.join(fullNames, Constants.STR_COMMA);
	}


	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto financePayAccount(String companyId, TBsPayInfo info, String singleStr, String isNotSkipLateFee) {
		if(null == info || CommonUtils.isEmpty(singleStr)){
			logger.warn("传入参数为空,交费充值失败.");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空,交费充值失败."));
		}

		//校验充值的钱是否相等
		double czAmount = CommonUtils.getSum(info.getWyAmount(),info.getBtAmount(),info.getWaterAmount(),info.getElectAmount(),info.getCommonAmount());
		double scAmount = CommonUtils.getSum(info.getPayCash(),info.getPayUnion(),info.getPayWx(),info.getPayZfb(),info.getPayBank());
		if(czAmount == 0){
			logger.warn("充值金额为空,请不要做冲零操作. ");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额为空,请不要做冲零操作. "));
		}
		if(scAmount == 0){
			logger.warn("实充金额为空,请不要做冲零操作. ");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "实充金额为空,请不要做冲零操作. "));
		}
		if(czAmount != scAmount){
			logger.warn("充值金额 :[{}]元, 与实充金额 :[{}]元不相等, 充值失败. 请检查.",czAmount,scAmount);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额 :[" + czAmount + "]元, 与实充金额 :[" + scAmount + "]元不相等, 充值失败. 请检查."));
		}

		List<String> buildingCodes = null;
		if(CommonUtils.isEquals(Constants.PAY_STR_ALL, singleStr)){
			//全交(客户角度)
			buildingCodes = this.personBuildingNewMapper.getBuildingCodesByCustIdAndChargerId(info.getCustId(),info.getCreateId());
		}else{
			//交部分(客户角度/建筑角度)
			buildingCodes = info.getBuildingCodes();
		}

		if(CommonUtils.isEmpty(buildingCodes)){
			logger.warn("未找到当前客户相应的建筑绑定关系, 交费充值失败. 数据:{}. ", info.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "未找到当前客户相应的建筑绑定关系, 交费充值失败. "));
		}

		TSysUserSearch condition = new TSysUserSearch();
		condition.setUserId(info.getCreateId());
		List<TSysUserList> userList = this.tSysUserMapper.findByCondition(condition);
		String staffName = (CommonUtils.isEmpty(userList)) ? null : userList.get(0).getStaffName();
		String staffNumber = (CommonUtils.isEmpty(userList)) ? "" : userList.get(0).getStaffNumber();

		//物业管理费, 本体基金, 水费, 电费一个一个账户来抵扣
		String buildingCode;
		String projectName = null;
		String projectId = null;
		List<TBsAssetAccountStream> insertSteamList = new ArrayList<TBsAssetAccountStream>();
		List<TBsPayInfo> insertInfos = new ArrayList<TBsPayInfo>();

		String tarId = CommonUtils.getUUID();
		info.setWyAmount(CommonUtils.null2Double(info.getWyAmount()));
		info.setBtAmount(CommonUtils.null2Double(info.getBtAmount()));
		info.setWaterAmount(CommonUtils.null2Double(info.getWaterAmount()));
		info.setElectAmount(CommonUtils.null2Double(info.getElectAmount()));
		info.setCommonAmount(CommonUtils.null2Double(info.getCommonAmount()));
		info.setPayCash(CommonUtils.null2Double(info.getPayCash()));
		info.setPayUnion(CommonUtils.null2Double(info.getPayUnion()));
		info.setPayWx(CommonUtils.null2Double(info.getPayWx()));
		info.setPayZfb(CommonUtils.null2Double(info.getPayZfb()));
		info.setPayBank(CommonUtils.null2Double(info.getPayBank()));
		Double payCash = info.getPayCash();
		Double payUnion = info.getPayUnion();
		Double payWx = info.getPayWx();
		Double payBank = info.getPayBank();
		Double payZfb = info.getPayZfb();

		List<String> types = new ArrayList<String>();
		if(info.getWyAmount() > 0) types.add(Constants.STR_ONE);
		if(info.getBtAmount() > 0) types.add(Constants.STR_TWO);
		if(info.getWaterAmount() > 0) types.add(Constants.STR_THREE);
		if(info.getElectAmount() > 0) types.add(Constants.STR_FOUR);
		if(info.getCommonAmount() > 0) types.add(Constants.STR_ZERO);


		//在这里作出一个调整就是一次交多个房的时候，交易流水号相同，之前不同，导致后面新账户无法实现关联（实际情况一次交多房很少）
		String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9);

		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据S（房号-- 老账户的buildingCode  每个账户的金额）

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("operateId", info.getCreateId());
		//paramMap.put("houseCodes", modifyHousetCode(buildingCodes));
		Map<String, String> amountMap = new HashMap<>();
		amountMap.put("wyAmount", info.getWyAmount().toString() );
		amountMap.put("btAmount", info.getBtAmount().toString());
		amountMap.put("waterAmount",info.getWaterAmount().toString());
		amountMap.put("electAmount",info.getElectAmount().toString());
		amountMap.put("commonAmount",info.getCommonAmount().toString());
		paramMap.put("amountMap", amountMap);
		paramMap.put("batchNo", batchNo);


		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据E



		for(int i = 0 ; i < buildingCodes.size() ; i++){
			boolean isLastFlag = false;
			buildingCode = buildingCodes.get(i);
			List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.findByBuildingCodeAndItems(buildingCode, types);
			getProject(info.getBuildingCode());
			if(CommonUtils.isEmpty(accounts))  continue;

			newBusinessAndOrder(accounts,info,companyId);

			TBsPayInfo insertInfo = new TBsPayInfo();
			insertInfo.setId(CommonUtils.getUUID());
			insertInfo.setBuildingCode(buildingCode);
			insertInfo.setCreateId(info.getCreateId());
			insertInfo.setCustId(info.getCustId());
			insertInfo.setCustName(info.getCustName());
			insertInfo.setProjectId(info.getProjectId());
			insertInfo.setModifyId(info.getModifyId());
			insertInfo.setCreateId(info.getCreateId());
			insertInfo.setJmRemark(info.getJmRemark());		//备注
			insertInfo.setPayType(info.getPayType());
			insertInfo.setPayerName(info.getPayerName());
			insertInfo.setStatus(info.getStatus());
			insertInfo.setRelationId(tarId);
			insertInfo.setBatchNo(batchNo);

			if(i == buildingCodes.size() - 1){
				//最后一个建筑, 若还有钱,都放入本建筑的账户
				isLastFlag = true;
				insertInfo.setPayCash(CommonUtils.null2Double(info.getPayCash()));
				insertInfo.setPayUnion(CommonUtils.null2Double(info.getPayUnion()));
				insertInfo.setPayWx(CommonUtils.null2Double(info.getPayWx()));
				insertInfo.setPayZfb(CommonUtils.null2Double(info.getPayZfb()));
				insertInfo.setPayBank(CommonUtils.null2Double(info.getPayBank()));
			}
			for(TBsAssetAccount account : accounts){
				TBsAssetAccountStream stream = accountRecharge(account, info, isLastFlag,insertInfo,buildingCodes.size(),isNotSkipLateFee);
				if(null != stream && CommonUtils.null2Double(stream.getChangMoney()) > 0){
					insertSteamList.add(stream);
				}
			}
			insertInfos.add(insertInfo);

			TSysProject project = this.tSysProjectMapper.findByCode(insertInfo.getProjectId());
			projectName = (null == project) ? null : project.getName();
			projectId = (null == project) ? null : project.getCode();
		}

		mqSender.sendAcChargeDetailForBatchRecharge(paramMap,companyId);

		if(!insertSteamList.isEmpty()){
			this.tBsAssetAccountStreamMapper.batchInsert(insertSteamList);
		}
		if(!insertInfos.isEmpty()){
			this.tBsPayInfoMapper.batchInsert(insertInfos);
		}

		info.setProjectId(projectId);
		TJgTotalCalculation tar = payInfoFinanceTar(info, staffName,staffNumber,projectName, 3); //未结算
		//TJgStaffGrop tJgStaffGrop = tJgStaffGropMapper.getTJgStaffGropByUserId(info.getCreateId(),info.getProjectId());
		String totalNum = getTradNo(info.getCreateId(),SettlementEnum.BUSINESS_ACCOUNT_Finance_CN.getIntValue().toString());
		if(null != tar){
			tar.setId(tarId);
//			tar.setReceiveId(CommonUtils.isEmpty(tJgStaffGrop) ? "" : tJgStaffGrop.getId());
//			tar.setReceiveName(CommonUtils.isEmpty(tJgStaffGrop) ? "" : tJgStaffGrop.getStaffName());
			tar.setTotalNum(totalNum);
			this.tJgTotalCalculationMapper.addTotalCalculation(tar);
		}

		//项目级别的缴费聚合  TBsProjectAutoFlushListener会自动刷新 这里不再刷新项目

		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"账户充值完成."));
	}


	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto financeBackMoney(String companyId, TBsPayInfo info) {

		String buildingCode = info.getBuildingCode();
		List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.getAccountsByBuildingCode(buildingCode);
		if(CommonUtils.isEmpty(accounts)){
			logger.warn("未找到{}建筑对应的账户数据.",buildingCode);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到对应的账户数据."));
		}

		//判断该户是否处于托收期间
		info.setBuildingCodes(Arrays.asList(buildingCode));
		String fullNames = checkCollingDatas(info);
		if(CommonUtils.isNotEmpty(fullNames)){
			logger.warn("{}建筑处于托收状态,无法退款.",buildingCode);
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullNames + "处于  [托收待回盘] 状态 , 当前无法使用账户  [退款] 功能."));
		}

		boolean flag = true;
		String str = null;
		String accountStr = null;
		for(TBsAssetAccount account : accounts){
			double accountBalance = account.getAccountBalance();

			if( CommonUtils.isNotEmpty( account.getProjectName() ) ) {
				info.setProjectName( account.getProjectName() );
			}

			if(account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()
					&& info.getWyAmount() > 0
					&& CommonUtils.null2Double(info.getWyAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_WY;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getWyAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()
					&& info.getBtAmount() > 0
					&& CommonUtils.null2Double(info.getBtAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_BT;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getBtAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()
					&& info.getWaterAmount() > 0
					&& CommonUtils.null2Double(info.getWaterAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_WATER;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getWaterAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()
					&& info.getElectAmount() > 0
					&& CommonUtils.null2Double(info.getElectAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_ELECT;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getElectAmount() + "] .";
				break;
			}else if(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()
					&& info.getCommonAmount() > 0
					&& CommonUtils.null2Double(info.getCommonAmount()) > accountBalance){
				flag = false;
				str = Constants.BILLING_COMMON;
				accountStr = accountBalance + "], 小于实际退费 : [" + info.getCommonAmount() + "] .";
				break;
			}
		}
		if(!flag){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "当前建筑下的 [" + str + "]余额为: [" + accountStr));
		}

		TSysUserSearch condition = new TSysUserSearch();
		condition.setUserId(info.getCreateId());
		List<TSysUserList> users = this.tSysUserMapper.findByCondition(condition);
		String staffName = (users.isEmpty()) ? Constants.STR_EMPTY : users.get(0).getStaffName();
		String staffNumber = (users.isEmpty()) ? Constants.STR_EMPTY : users.get(0).getStaffNumber();


		//经过校验
		//组装payInfo数据,组装银账交割数据, 组装account数据
		String projectName = null;
		for(TBsAssetAccount account : accounts){
			double backAmount = (account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()) ? info.getWyAmount() :
					(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()) ? info.getBtAmount() :
							(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()) ? info.getWaterAmount() :
									(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()) ? info.getElectAmount() :
											(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()) ? info.getCommonAmount() : 0;

			if(CommonUtils.isEmpty(backAmount))
				continue;
			account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) - CommonUtils.null2Double(backAmount));
			account.setModifyId(info.getCreateId());
			account.setModifyTime(new Date());
			info.setProjectId(account.getProjectId());
			projectName = account.getProjectName();

			this.tBsAssetAccountMapper.update(account);
			//插入流水
			this.tBsAssetAccountStreamMapper.singleInsert(
					new TBsAssetAccountStream(CommonUtils.getUUID(),
							account.getId(),
							-backAmount,
							new Date(),
							info.getCreateId(),
							staffName,
							StreamEnum.purpose_back.getV())  //退款
			);
		}


		//在这里作出一个调整就是一次交多个房的时候，交易流水号相同，之前不同，导致后面新账户无法实现关联（实际情况一次交多房很少）
		String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9)
				+ (new Random()).nextInt(9);

		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据S（房号-- 老账户的buildingCode  每个账户的金额）

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("oprId", info.getCreateId());
		paramMap.put("projectId", info.getProjectId());
		paramMap.put("projectName", info.getProjectName());
		paramMap.put("oprDetailId", batchNo);
		//paramMap.put("houseCodeNew",buildingCodeModifyHoustCode(buildingCode));
		Map<String, String> amountMap = new HashMap<>();
		amountMap.put("wyAmount", info.getWyAmount().toString() );
		amountMap.put("btAmount", info.getBtAmount().toString());
		amountMap.put("waterAmount",info.getWaterAmount().toString());
		amountMap.put("electAmount",info.getElectAmount().toString());
		amountMap.put("commonAmount",info.getCommonAmount().toString());
		paramMap.put("amountMap", amountMap);
		mqSender.sendDepositAccountRefund(paramMap,companyId);
		//*******后面开始老账户的充值操作，具体这里不关心，这里收集新账户需要的数据E


		//组装payInfo数据, 退款不需要计算税金
		info.setId(CommonUtils.getUUID());
		info.setCreateTime(new Date());
		info.setModifyTime(new Date());
		info.setModifyId(info.getCreateId());
		info.setBatchNo(batchNo);
		info.setRelationId(CommonUtils.getUUID());
		this.tBsPayInfoMapper.insert(info);

		//组装银账交割数据
		TJgTotalCalculation tar = payInfoFinanceTar(info, staffName, staffNumber, projectName, 3);
		//TJgStaffGrop tJgStaffGrop = tJgStaffGropMapper.getTJgStaffGropByUserId(info.getCreateId(),info.getProjectId());
		String totalNum = getTradNo(info.getCreateId(),SettlementEnum.BUSINESS_ACCOUNT_Finance_CN.getIntValue().toString());
		String tarId = CommonUtils.getUUID();

		if( tar != null){
			tar.setId(tarId);
//			tar.setReceiveId(CommonUtils.isEmpty(tJgStaffGrop) ? "" : tJgStaffGrop.getId());
//			tar.setReceiveName(CommonUtils.isEmpty(tJgStaffGrop) ? "" :tJgStaffGrop.getStaffName());
			tar.setTotalNum(totalNum);
			this.tJgTotalCalculationMapper.addTotalCalculation(tar);
		}


		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"退款成功"));
	}


	public static String getTradNo(String userId,String type) {
		// 当前时间的时分秒 + 用户的staff_number
		StringBuilder tranNo=new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		tranNo.append(""+year);
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month < 10) {
			tranNo.append("0"+month);
		}else {
			tranNo.append(""+month);
		}
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(day < 10) {
			tranNo.append("0"+day);
		}else {
			tranNo.append(""+day);
		}
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(hour < 10) {
			tranNo.append("0"+hour);
		}else {
			tranNo.append(""+hour);
		}
		int minute = calendar.get(Calendar.MINUTE);
		if(minute < 10) {
			tranNo.append("0"+minute);
		}else {
			tranNo.append(""+minute);
		}
		int second = calendar.get(Calendar.SECOND);
		if(second < 10) {
			tranNo.append("0"+second);
		}else {
			tranNo.append(""+second);
		}
		tranNo.append(type);
		tranNo.append(userId);
		return tranNo.toString();
	}


	private void newBusinessAndOrder(List<TBsAssetAccount>accounts, TBsPayInfo info,String companyId) {
		List<AcBusinessOperaDetailDto> acBusinessOperaDtoList =new ArrayList<>();
		List<AcOrderCycleDetailDto> acOrderCycleDetailDtoList=new ArrayList<>();
		List<AcOrderCycleDetailDto> acOrderCycleDetailDtoPrestoredList=new ArrayList<>();
		AcOrderDto acOrderDto1=new  AcOrderDto();
		AcOrderCycleDetailDto acOrderCycleDetailDto1=new AcOrderCycleDetailDto();
		AcBusinessOperaDetailDto acBusinessOperaDetailDto1=new AcBusinessOperaDetailDto();
		AcBusinessOperaDetailDto acBusinessOperaDetailDto=new AcBusinessOperaDetailDto();
	//	List<AcBusinessOperaDetailDto> acBusinessOperaDetailDtoList=new ArrayList<>();
		FrontOperaDto frontOperaDto=new  FrontOperaDto();
		AcOrderDto	acOrderDtoPrestored=new AcOrderDto();






		TBsPayInfo tBsPay =new TBsPayInfo();

		TBsPayInfo prestoredPay=new TBsPayInfo();

		tBsPay.setProjectId(info.getProjectId());
		tBsPay.setProjectName(info.getProjectName());
		tBsPay.setPayType(info.getPayType());
		tBsPay.setCreateId(info.getCreateId());
		tBsPay.setCreateTime(new Date());

		tBsPay.setBuildingCode(info.getBuildingCode());
		prestoredPay.setBuildingCode(info.getBuildingCode());
		if (info.getBuildingCode()==null){
			tBsPay.setBuildingCode(info.getBuildingCodes().get(0));
			prestoredPay.setBuildingCode(info.getBuildingCodes().get(0));
		}
		tBsPay.setPayerName(info.getPayerName());
		tBsPay.setProjectId(info.getProjectId());
		tBsPay.setCreateId(info.getCreateId());
		prestoredPay.setProjectId(info.getProjectId());
		prestoredPay.setProjectName(info.getProjectName());
		prestoredPay.setPayType(info.getPayType());
		prestoredPay.setCreateId(info.getCreateId());


		prestoredPay.setPayerName(info.getPayerName());
		prestoredPay.setCreateTime(tBsPay.getCreateTime());
		prestoredPay.setCreateId(info.getCreateId());
		prestoredPay.setProjectId(info.getProjectId());
		acOrderDto1=getAcOrderDto(info);
		double fee=0.0;
		double prestored=0.0;
		for (TBsAssetAccount account : accounts) {

			if (account.getType() == 1) {
				double difference = info.getWyAmount()+CommonUtils.getScaleNumber(account.getAccountBalance(),2);
				if (difference < 0) {
					tBsPay.setWyAmount(info.getWyAmount());
					fee+=tBsPay.getWyAmount();
				} else {
					if (account.getAccountBalance() > 0 || account.getAccountBalance() == 0) {
						prestoredPay.setWyAmount(info.getWyAmount());
						prestored+=info.getWyAmount();
					} else {
						tBsPay.setWyAmount(0-CommonUtils.getScaleNumber(account.getAccountBalance(),2));
						fee+=tBsPay.getWyAmount();
						if (difference > 0) {
							prestoredPay.setWyAmount(difference);
							prestored+=difference;

						}
					}
				}
			} else if (account.getType() == 2) {
				double difference = info.getBtAmount()+CommonUtils.getScaleNumber(account.getAccountBalance(),2);
				if (difference < 0) {
					tBsPay.setBtAmount(info.getBtAmount());
					fee+=info.getBtAmount();
				} else {
					if (account.getAccountBalance() > 0 || account.getAccountBalance() == 0) {
						prestoredPay.setBtAmount(info.getBtAmount());
						prestored+=info.getBtAmount();
					} else {
						tBsPay.setBtAmount(0-CommonUtils.getScaleNumber(account.getAccountBalance(),2));
						fee+=tBsPay.getBtAmount();
						if (difference > 0) {
							prestoredPay.setBtAmount(difference);
							prestored+=difference;

						}
					}
				}

			} else if (account.getType() == 3) {
				double difference = info.getWaterAmount()+ CommonUtils.getScaleNumber(account.getAccountBalance(),2);
				if (difference < 0) {
					tBsPay.setWaterAmount(info.getWaterAmount());
					fee+=info.getWaterAmount();
				} else {
					if (account.getAccountBalance() > 0 || account.getAccountBalance() == 0) {
						prestoredPay.setWaterAmount(info.getWaterAmount());
						prestored+=info.getWaterAmount();
					} else {

						tBsPay.setWaterAmount(0-CommonUtils.getScaleNumber(account.getAccountBalance(),2));
						fee+=tBsPay.getWaterAmount();
						if (difference > 0) {
							prestoredPay.setWaterAmount(difference);
							prestored+=difference;
						}
					}
				}
			} else if (account.getType() == 4) {

				double difference = info.getElectAmount()+ CommonUtils.getScaleNumber(account.getAccountBalance(),2);
				if (difference < 0) {
					tBsPay.setElectAmount(info.getElectAmount());
					fee+=info.getElectAmount();
				} else {
					if (account.getAccountBalance() > 0 || account.getAccountBalance() == 0) {
						prestoredPay.setElectAmount(info.getElectAmount());
						prestored+=info.getElectAmount();
					} else {
						//电费取取绝对值
						tBsPay.setElectAmount(CommonUtils.getScaleNumber(Math.abs(account.getAccountBalance()),2));

						fee+=tBsPay.getElectAmount();
						if (difference > 0) {
							prestoredPay.setElectAmount(difference);
							prestored+=difference;

						}
					}
				}
			}
			else if (account.getType() == 0) {
				prestoredPay.setCommonAmount(info.getCommonAmount());
				prestored+=info.getCommonAmount();

			}
			if (account.getType()!=0){
				//0为缴费
				//acCycleOrderDetailDto=getacCycleOrderDetailDto(tBsPay,account,BusinessType.PAYMENT,companyId);
				acOrderCycleDetailDto1=getacOrderCycleDetailDto(tBsPay,account,BusinessType.PAYMENT);
				if (acOrderCycleDetailDto1!=null){
					acOrderCycleDetailDtoList.add(acOrderCycleDetailDto1);

				}
			}
			//1为预存
			acOrderCycleDetailDto1=getacOrderCycleDetailDto(prestoredPay,account,BusinessType.PRESTORE);
			if (acOrderCycleDetailDto1!=null){
				acOrderDtoPrestored=getAcOrderDto(info);
				acOrderCycleDetailDtoPrestoredList.add(acOrderCycleDetailDto1);


			}
		}
		acOrderDto1.setOrderCycleDetailList(acOrderCycleDetailDtoList);
		acOrderDto1.setAmount(CommonUtils.getScaleNumber(
				fee,2));
		acBusinessOperaDetailDto1=getAcBusinessOperOperaDetailDto(tBsPay,  BusinessType.PAYMENT,companyId);
		List<FrontOperaDto>frontOperaDtoList=new ArrayList<>();
		if (acBusinessOperaDetailDto1!=null){
		acBusinessOperaDtoList.add(acBusinessOperaDetailDto1);
		frontOperaDto.setCompanyId(companyId);
		frontOperaDto.setAcOrderDto(acOrderDto1);
		frontOperaDto.setBusinessOperaDetailDto(acBusinessOperaDetailDto1);

		frontOperaDtoList.add(frontOperaDto);

		RemoteModelResult<String> result = acBusinessOperaDetailApi.createOperaDetail(companyId,acBusinessOperaDetailDto1);
		String operaId = result.getModel();

		acOrderDto1.setOperaId(operaId);
		}
		acBusinessOperaDetailDto=getAcBusinessOperOperaDetailDto(prestoredPay,BusinessType. PRESTORE,companyId);
		if (acBusinessOperaDetailDto!=null){
			FrontOperaDto frontOperaDtoPrestored=new  FrontOperaDto();
			acBusinessOperaDtoList.add(acBusinessOperaDetailDto);
			acOrderDtoPrestored.setOrderCycleDetailList(acOrderCycleDetailDtoPrestoredList);
			acOrderDtoPrestored.setAmount(CommonUtils.getScaleNumber(
					prestored,2));
			RemoteModelResult<String> result1 = acBusinessOperaDetailApi.createOperaDetail(companyId,acBusinessOperaDetailDto);
			 String operaId1 = result1.getModel();
			acOrderDtoPrestored.setOperaId(operaId1);
			frontOperaDtoPrestored.setCompanyId(companyId);
			frontOperaDtoPrestored.setAcOrderDto(acOrderDtoPrestored);
			frontOperaDtoPrestored.setBusinessOperaDetailDto(acBusinessOperaDetailDto);

			frontOperaDtoList.add(frontOperaDtoPrestored);
		}


		mqSender.sendBusinessOperas(acBusinessOperaDtoList,companyId);
		mqSender.sendCycleOrderDetail(frontOperaDtoList,companyId);
	}

	private AcBusinessOperaDetailDto getAcBusinessOperOperaDetailDto(TBsPayInfo tBsPay, BusinessType payment,String companyId) {
		AcBusinessOperaDetailDto acBusinessOperaDetailDto =new AcBusinessOperaDetailDto();
		acBusinessOperaDetailDto.setOperationId(tBsPay.getCreateId());
		acBusinessOperaDetailDto.setOperationTime(tBsPay.getCreateTime());
		acBusinessOperaDetailDto.setBusinessType(payment.getCode());
		double late=0.0;
		if (!CommonUtils.isEmpty(tBsPay.getWyAmount())){
			late+=tBsPay.getWyAmount();
		}
		if (!CommonUtils.isEmpty(tBsPay.getBtAmount())){
			late+=tBsPay.getBtAmount();
		}
		if (!CommonUtils.isEmpty(tBsPay.getWaterAmount())){
			late+=tBsPay.getWaterAmount();
		}
		if (!CommonUtils.isEmpty(tBsPay.getElectAmount())){
			late+=tBsPay.getElectAmount();
		}
		if (!CommonUtils.isEmpty(tBsPay.getCommonAmount())){
			late+= tBsPay.getCommonAmount();
		}
		if (late>0){
			acBusinessOperaDetailDto.setAmount(CommonUtils.getScaleNumber(late,2));

			if(tBsPay.getPayType()==null){

			}else {
				if (tBsPay.getPayType()==3){
					acBusinessOperaDetailDto.setClientType(ClientType.POS.getCode());
				}else {
					acBusinessOperaDetailDto.setClientType(ClientType.PC.getCode());
				}
			}
			String project=getProject(tBsPay.getBuildingCode());
			acBusinessOperaDetailDto.setProjectId(project);
			acBusinessOperaDetailDto.setProjectName(this.tBcProjectMapper.findByProjectId(project).getProjectName());
			acBusinessOperaDetailDto.setCompanyId(companyId);

			return acBusinessOperaDetailDto;
		}else {
			return null;
		}

	}

	private  String getProject(String buildingCode ){
		return this.tcBuildingMapper.loadBuildingByBuildingCode(buildingCode).getProjectId();
	}



	private AcOrderCycleDetailDto getacOrderCycleDetailDto(TBsPayInfo payInfo, TBsAssetAccount account, BusinessType  payment) {
		AcOrderCycleDetailDto acOrderCycleDetailDto= new AcOrderCycleDetailDto();
		double fee=0.0;
		acOrderCycleDetailDto.setDepositType(AccountType.SPECIAL.getCode());

		if (account.getType()==1){
			if (!CommonUtils.isEmpty(payInfo.getWyAmount())){
				fee=payInfo.getWyAmount();
			}
		}else if (account.getType()==2){
			if (!CommonUtils.isEmpty(payInfo.getBtAmount())){
				fee=payInfo.getBtAmount();}
		}else if (account.getType()==3){
			if (!CommonUtils.isEmpty(payInfo.getWaterAmount())){
				fee=payInfo.getWaterAmount();
			}
		}else if (account.getType()==4){
			if (!CommonUtils.isEmpty(payInfo.getElectAmount())){
				fee=payInfo.getElectAmount();
			}
		}else if (account.getType()==0){
			if (!CommonUtils.isEmpty(payInfo.getCommonAmount())){
				acOrderCycleDetailDto.setDepositType(AccountType.COMMON.getCode());
				fee=payInfo.getCommonAmount();
			}
		}
		if (fee==0){
			return null;
		}
		if (payment==BusinessType.PAYMENT){

			List<TBsOwedHistory>tBsOwedHistoryList=this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
			acOrderCycleDetailDto.setBusinessType(BusinessType.PAYMENT.getCode());
			if (!CommonUtils.isEmpty(tBsOwedHistoryList)){

				double latefee=0.0;
				for (TBsOwedHistory owedHistory:tBsOwedHistoryList){
					latefee +=CommonUtils.getScaleNumber(owedHistory.getTotalLateFee(),2);
				}
				double fal=fee-latefee;
				if (fal>0){
					acOrderCycleDetailDto.setLateAmount(CommonUtils.getScaleNumber(latefee,2));
				}else {
					acOrderCycleDetailDto.setLateAmount(CommonUtils.getScaleNumber(fee,2));
				}
				fee=fal;
			}
		}else {
			acOrderCycleDetailDto.setBusinessType(BusinessType.PRESTORE.getCode());
		}
		acOrderCycleDetailDto.setDetailAmount((CommonUtils.getScaleNumber(fee,2)));
		//acOrderCycleDetailDto.setHouseCodeNew(buildingCodeModifyHoustCode(payInfo.getBuildingCode()));
		acOrderCycleDetailDto.setAccountType(account.getType());
		return acOrderCycleDetailDto;
	}







	//	private AcCycleOrderDetailDto getacCycleOrderDetailDto(TBsPayInfo payInfo, TBsAssetAccount account, BusinessType payment,String companyId ) {
//		AcCycleOrderDetailDto acCycleOrderDetailDto=new AcCycleOrderDetailDto();
//		double fee=0.0;
//		acCycleOrderDetailDto.setAccountType(AccountType.SPECIAL);
//		if (account.getType()==1){
//			if (!CommonUtils.isEmpty(payInfo.getWyAmount())){
//				fee=payInfo.getWyAmount();
//			}
//		}else if (account.getType()==2){
//			if (!CommonUtils.isEmpty(payInfo.getBtAmount())){
//				fee=payInfo.getBtAmount();}
//		}else if (account.getType()==3){
//			if (!CommonUtils.isEmpty(payInfo.getWaterAmount())){
//				fee=payInfo.getWaterAmount();
//			}
//		}else if (account.getType()==4){
//			if (!CommonUtils.isEmpty(payInfo.getElectAmount())){
//				fee=payInfo.getElectAmount();
//			}
//		}else if (account.getType()==0){
//			if (!CommonUtils.isEmpty(payInfo.getCommonAmount())){
//				acCycleOrderDetailDto.setAccountType(AccountType.COMMON);
//				fee=payInfo.getCommonAmount();
//			}
//		}
//		if (fee==0){
//			return null;
//		}
//		acCycleOrderDetailDto.setOperatorType(OperatorType.FRONT_DESK);
//		if (payment==BusinessType.PAYMENT){
//
//			List<TBsOwedHistory>tBsOwedHistoryList=this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
//			acCycleOrderDetailDto.setBusinessType(BusinessType.PAYMENT);
//			if (!CommonUtils.isEmpty(tBsOwedHistoryList)){
//
//				double latefee=0.0;
//				for (TBsOwedHistory owedHistory:tBsOwedHistoryList){
//					latefee +=CommonUtils.getScaleNumber(owedHistory.getTotalLateFee(),2);
//				}
//				double fal=fee-latefee;
//				if (fal>0){
//				//	acCycleOrderDetailDto.setLateAmount(CommonUtils.getScaleNumber(fee,2));
//				}else {
//				//	acCycleOrderDetailDto.setLateAmount(fee);
//				}
//				fee=fal;
//			}
//
//		}else {
//			acCycleOrderDetailDto.setBusinessType(BusinessType.PRESTORE);
//
//		}
//		acCycleOrderDetailDto.setAmount(new BigDecimal(fee).setScale(2,BigDecimal.ROUND_HALF_UP));
//		//acCycleOrderDetailDto.setHouseCodeNew(buildingCodeModifyHoustCode(payInfo.getBuildingCode()));
//		acCycleOrderDetailDto.setPayStateEnum(AcPayStateEnum.PAYED);
//		acCycleOrderDetailDto.setChargingType(ChargingType.getChargingTypeByCode(account.getType()));
//
//		acCycleOrderDetailDto.setPayer(payInfo.getPayerName());
//		//acCycleOrderDetailDto.setPayerMobile(personBuildingNewMapper.getCustId(payInfo.getBuildingCode(),payInfo.getPayerName()));
//		if (payInfo.getPayType()==3){
//			acCycleOrderDetailDto.setClientType(ClientType.POS);
//		}else {
//			acCycleOrderDetailDto.setClientType(ClientType.PC);
//		}
//		acCycleOrderDetailDto.setAcOrderStateEnum(AcOrderStateEnum.FINISHED);
//		acCycleOrderDetailDto.setOrderTypeEnum(AcOrderTypeEnum.CYCLE);
//		acCycleOrderDetailDto.setPayTime(payInfo.getCreateTime());
//		acCycleOrderDetailDto.setOperationDetailId(payInfo.getCreateId());
//		acCycleOrderDetailDto.setCompanyId(companyId);
//		return acCycleOrderDetailDto;
//	}
	private AcOrderDto getAcOrderDto(TBsPayInfo info) {
		AcOrderDto acOrderDto=new  AcOrderDto();
		acOrderDto.setPayer(info.getPayerName());

		//acOrderDto.setHouseCodeNew(buildingCodeModifyHoustCode(info.getBuildingCode()));
		if(null!=personBuildingNewMapper.getCustId(info.getBuildingCode(),info.getPayerName())){
			acOrderDto.setPayerMobile(personBuildingNewMapper.getCustId(info.getBuildingCode(),info.getPayerName()));
		}

		acOrderDto.setOrderState(AcOrderStateEnum.FINISHED.getState());
		acOrderDto.setPayState(AcPayStateEnum.PAYED.getState());
		acOrderDto.setOrderType(AcOrderTypeEnum.CYCLE.getType());
		acOrderDto.setUpdateTime(new Date());

		acOrderDto.setPaymentTime(new Date());
		acOrderDto.setPaymentCahnnel(info.getPayType());
		acOrderDto.setIsRcorded(RcordedType.IS_RCORDEDTYPE.getCode());

		return  acOrderDto;
	}
}
