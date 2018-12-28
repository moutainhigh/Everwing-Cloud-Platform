package com.everwing.coreservice.wy.core.service.impl.configuration.latefee;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;
import com.everwing.coreservice.common.wy.service.configuration.latefee.LateFeeService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.core.utils.BillingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("lateFeeService")
public class LateFeeServiceImpl extends Resources implements LateFeeService{

	
	private static final Logger logger = LogManager.getLogger(LateFeeServiceImpl.class);
	
	@Value("${queue.wy2wyBilling.wy.manual.key}")
	private String wy_billing_key;					//消息队列 对BillingSupEntity里的数据进行处理
	
	/**
	 * @TODO 计算违约金
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto billLateFee(String companyId, TBsProject project) {
		
		//查找该本项目下 , 所有的有效欠费数据
		
		List<TBsOwedHistory> updateOhList = new ArrayList<TBsOwedHistory>();
		
		boolean isCollectioning = false;
		boolean isJrl = false;
		String totalId = null;
		TBcProject tBcProject = this.tBcProjectMapper.findByProjectId(project.getProjectId());
		if(tBcProject != null && tBcProject.getStatus() == CollectionEnum.status_on.getV()){
			
			Integer collectionType = null;
			if(collectionType == null && tBcProject.getWyStatus() == CollectionEnum.status_on.getV() && tBcProject.getWyType() != CollectionEnum.type_off.getV()){
				collectionType = tBcProject.getWyType();
			}else if(collectionType == null && tBcProject.getBtStatus() == CollectionEnum.status_on.getV() && tBcProject.getBtType() != CollectionEnum.type_off.getV()){
				collectionType = tBcProject.getBtType();
			}else if(collectionType == null && tBcProject.getWaterStatus() == CollectionEnum.status_on.getV() && tBcProject.getWaterType() != CollectionEnum.type_off.getV()){
				collectionType = tBcProject.getWaterType();
			}else if(collectionType == null && tBcProject.getElectStatus() == CollectionEnum.status_on.getV() && tBcProject.getElectType() != CollectionEnum.type_off.getV()){
				collectionType = tBcProject.getElectType();
			}
			
			TBcCollectionTotal total = this.tBcCollectionMapper.findRecentTotal(project.getProjectId(), collectionType);
			totalId = total.getId();
			if(CommonUtils.isEquals(Constants.STR_YES, total.getIsWaitBack())){
				//是待回盘状态,判断buildingCodes是否尚处于托收状态
				isCollectioning = true;
				isJrl = collectionType == CollectionEnum.type_jrl.getV();
			}
		}
		
		TBsOwedHistory paramObj = new TBsOwedHistory();
		paramObj.setProjectId(project.getProjectId());
		paramObj.setIsUsed(BillingEnum.IS_USED_USING.getIntV());
		List<TBsOwedHistory> histories = new ArrayList<TBsOwedHistory>();
		if(!isCollectioning){  	//非托收期间,所有数据
			histories = this.tBsOwedHistoryMapper.findUsingDatas(paramObj);
		}else{
			//托收期间,分金融联和银联来过滤
			if(isJrl){//金融联
				histories = this.tBsOwedHistoryMapper.findNotOnJrlCollingDatas(project.getProjectId(),totalId);
			}else{//银联
				histories = this.tBsOwedHistoryMapper.findNotOnUnionCollingDatas(project.getProjectId(),totalId);
			}
		}
		
		if(CommonUtils.isEmpty(histories)){
			logger.warn("违约金计算: 当前项目下未找到可计算违约金的欠费数据. 数据: {}. ",project.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前项目下未找到可计算违约金的欠费数据. "));
		}
		
		for(TBsOwedHistory oh : histories){
			
			//判断当前账户的building_code是否处于托收期间, 如果处于托收期间,则不再对该账户进行违约金计算
			//托收-回盘至少需要一天的调节时间,在这一天会产生违约金,导致回盘时账户无法抹平
			
			
			double lateFee = 0.0;
			if(oh.getCalculationType() == BillingEnum.SCHEME_CLAC_TYPE_SIMPLE.getIntV()){
				//单利模式的违约金计算   本金*利率即可, 违约金再累加
				lateFee = Math.abs(oh.getOwedAmount()) * (oh.getProportion() / 1000);
				oh.setTotalLateFee(oh.getTotalLateFee() + lateFee);
				
				
			}else if(oh.getCalculationType() == BillingEnum.SCHEME_CLAC_TYPE_CPX.getIntV()){
				//复利模式的违约金计算 本利和*利率
				lateFee = (oh.getTotalLateFee() + Math.abs(oh.getOwedAmount())) * (oh.getProportion() / 1000 );
				oh.setTotalLateFee(oh.getTotalLateFee() + lateFee);
				
			}
			//将违约金计入到账户余额
			this.tBsAssetAccountMapper.addLateFee(oh.getAccountId(), lateFee);
			
			//插入违约金流水
			if(CommonUtils.null2Double(lateFee) > 0){
				this.tBsAssetAccountStreamMapper.singleInsert(new TBsAssetAccountStream(oh.getAccountId(), -lateFee, StreamEnum.purpose_billing_by_latefee.getV()));
			}
			updateOhList.add(oh);
			
			if(updateOhList.size() >= BATCH_UPDATE_COUNT){
				BillingUtils.sendUpdateList(updateOhList, wy_billing_key, companyId, amqpTemplate);
			}
		}
		if(!updateOhList.isEmpty()){
			BillingUtils.sendUpdateList(updateOhList, wy_billing_key, companyId, amqpTemplate);
		}
		
		return new BaseDto(new MessageMap(null,"项目[" + project.getProjectName() + "]违约金计算完成."));
	}

	
	private void sendMessage(BillingSupEntity se, String companyId){
		MqEntity e = new MqEntity();
		e.setCompanyId(companyId);
		e.setData(se);
		this.amqpTemplate.convertAndSend(wy_billing_key, e);
	}
}
