package com.everwing.coreservice.wy.core.service.impl.building;/**
 * Created by wust on 2017/8/4.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.order.TcOrderChangeAssetComplaint;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.common.wy.entity.property.property.ProprietorInfo;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistory;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistorySearch;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.service.building.PropertyService;
import com.everwing.coreservice.common.wy.service.configuration.tbcassetacount.TBsAssetAccountServie;
import com.everwing.coreservice.common.wy.service.cust.CollectionService;
import com.everwing.coreservice.common.wy.service.order.TcOrderComplaintService;
import com.everwing.coreservice.common.wy.service.personbuilding.PersonbuildingService;
import com.everwing.coreservice.common.wy.service.sys.TSysUserService;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterrelation.TcMeterRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingschedule.TcReadingScheduleMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.TBcCollectionMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.jrl.TBcJrlBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.back.TBcUnionBackBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.project.TBcProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.TBsAssetAccountMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust.EnterpriseCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgStaffGropMapper;
import com.everwing.coreservice.wy.dao.mapper.personbuilding.PersonBuildingNewMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TPropertyChangingHistoryMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/4
 * @author wusongti@lii.com.cn
 */
@Service("propertyServiceImpl")
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private TcReadingScheduleMapper tcReadingScheduleMapper;

    @Autowired
    private TcReadingTaskMapper tcReadingTaskMapper;
    
    @Autowired
    private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;

    @Autowired
    private TcMeterRelationMapper tcMeterRelationMapper;
    
    @Autowired
    private TcMeterDataMapper tcMeterDataMapper;
    
    @Autowired
    private EnterpriseCustNewMapper enterpriseCustNewMapper;
    
    @Autowired
    private TBsAssetAccountMapper tBsAssetAccountMapper;
    
    @Autowired
    private TBcJrlBodyMapper tBcJrlBodyMapper;
    
    @Autowired
    private TBcUnionBackBodyMapper tBcUnionBackBodyMapper;
    
    @Autowired
    private TJgStaffGropMapper tJgStaffGropMapper;
    
    @Autowired
    private PersonBuildingNewMapper personBuildingNewMapper;
    
    @Autowired
    private TBcProjectMapper tBcProjectMapper;
    
    @Autowired
    private TBcCollectionMapper tBcCollectionMapper;
 
//    @Autowired
//    private TcBuildingMapper tcBuildingMapper;
    @Autowired
    private TPropertyChangingHistoryMapper tPropertyChangingHistoryMapper;
    
    private static final Logger log = Logger.getLogger(PropertyServiceImpl.class);

    @Override
    public BaseDto listPageChangingHistory(String companyId, TPropertyChangingHistorySearch tPropertyChangingHistorySearch) {
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(tPropertyChangingHistoryMapper.listPage(tPropertyChangingHistorySearch));
        baseDto.setPage(tPropertyChangingHistorySearch.getPage());
        return baseDto ;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public MessageMap insertChangingHistory(String companyId, TPropertyChangingHistory tPropertyChangingHistory) throws ECPBusinessException{
    	 //新业主要绑定资产;旧业主需要解绑
    	MessageMap mm = new MessageMap();
    	try {
    		
    		String fullNames = checkCollingDatas(tPropertyChangingHistory.getProjectId(), tPropertyChangingHistory.getBuildingCode());
    		if(CommonUtils.isNotEmpty(fullNames)){
    			log.warn(fullNames + " ->> " + tPropertyChangingHistory.getBuildingCode() + " : 处于  [托收待回盘] 状态, 无法办理过户. 请等待回盘完成后再操作. ");
    			return new MessageMap(MessageMap.INFOR_ERROR,fullNames + " : 处于  [托收待回盘] 状态 , 为防止对老业主产生不必要的扣费, 当前无法办理过户操作. 请等待回盘后再进行此操作. ");
    		}
    		
    		 tPropertyChangingHistoryMapper.insert(tPropertyChangingHistory);
    		 
    		 PersonbuildingService personbuildingService = (PersonbuildingService)SpringContextHolder.getBean("personbuildingService");
    		 //解除旧业主绑定的托收
    		 CollectionService collectionService  =(CollectionService)SpringContextHolder.getBean("collectionService");
    		 //解绑旧客户的资产，可能有多个客户
    		 List<PersonBuildingNew> personBuildingOlds = new ArrayList<PersonBuildingNew>();
    		 String oldCustId= tPropertyChangingHistory.getOldHolder();
    		 if(StringUtils.isNotBlank(oldCustId)){
    			 String[] oldCustIds = oldCustId.split(",");
    			 for(int i=0;i<oldCustIds.length;i++){
    	    		 PersonBuildingNew personBuildingOld = new PersonBuildingNew();
    	    		 //这里需要去判断是企业客户还是个人客户
    	    		 EnterpriseCustNew  enterpriseCustNew = enterpriseCustNewMapper.getEnterpriseCustById(oldCustIds[i]);
    	    		 if(CommonUtils.isNotEmpty(enterpriseCustNew)){//说明未企业客户
    	    			 personBuildingOld.setEnterpriseId(oldCustIds[i]);
    	    		 }else{//个人客户
    	    			 personBuildingOld.setCustId(oldCustIds[i]);
    	    		 }
    	    		 personBuildingOld.setBuildingId(tPropertyChangingHistory.getBuildingId());
    	    		 personBuildingOld.setBuildingCode(tPropertyChangingHistory.getBuildingCode());
    	    		 personBuildingOlds.add(personBuildingOld);
    	    		 //解绑阶业主绑定的托收
    	    		 collectionService.unEffectiveByCustIdAndBuildingCode(companyId,oldCustIds[i], tPropertyChangingHistory.getBuildingCode());
    			 }
    		 }
    		 personbuildingService.relieveAssetBinding(companyId, personBuildingOlds);
    		 
    		 //绑定新客户的资产,只能选择一个客户
    		 List<PersonBuildingNew> personBuildingNews = new ArrayList<PersonBuildingNew>();
    		 PersonBuildingNew personBuildingNew = new PersonBuildingNew();
    		 if(StringUtils.isBlank(tPropertyChangingHistory.getEnterpriseNewId())){
    			 personBuildingNew.setCustId(tPropertyChangingHistory.getNewHolder()); //个人客户
        		 personBuildingNew.setCustType("业主");
    		 }else{
    			 personBuildingNew.setEnterpriseId(tPropertyChangingHistory.getEnterpriseNewId()); //企业客户
    		 }
    		 personBuildingNew.setBuildingCode(tPropertyChangingHistory.getBuildingCode());
    		 personBuildingNew.setBuildingId(tPropertyChangingHistory.getBuildingId());
    		 
    		 personBuildingNews.add(personBuildingNew);
			WyBusinessContext ctx = WyBusinessContext.getContext();
    		 personbuildingService.assetBinding(ctx, personBuildingNews);
    		 
    		 
    		
    		 
    		 mm.setFlag(MessageMap.INFOR_SUCCESS);
    	     return mm;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
    }

    @Override
    public List<ProprietorInfo> getProprietorInfoByBuildingCode(String companyId, String buildingCode) {
        return tPropertyChangingHistoryMapper.getProprietorInfoByBuildingCode(buildingCode);
    }

    
    @Override
    public BaseDto listPageCustomerInEntery(String companyId, CustomerSearch customerSearch) {
    	BaseDto baseDto = new BaseDto<>();
    	baseDto.setLstDto(tPropertyChangingHistoryMapper.listPageCustomerInEntery(customerSearch));
    	baseDto.setPage(customerSearch.getPage());
    	return baseDto ;
    }


    /**
     *
     * @param companyId
     * @param type  0水表，1电表
     * @param tcReadingTask
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap requestReadingMeter(String companyId,String projectId, int type, TcReadingTask tcReadingTask) {
        MessageMap mm = new MessageMap();
        TcReadingSchedule tcReadingScheduleSearch = new TcReadingSchedule();
        tcReadingScheduleSearch.setProjectId(projectId);
        tcReadingScheduleSearch.setMeterType(type);
        List<TcReadingSchedule> tcReadingSchedules = tcReadingScheduleMapper.findUsingSchedule(tcReadingScheduleSearch);
        if(CollectionUtils.isNotEmpty(tcReadingSchedules)){
            String id = UUID.randomUUID().toString();
            TcReadingSchedule tcReadingSchedule = tcReadingSchedules.get(0);
            tcReadingTask.setId(id);
            tcReadingTask.setScheduleCode(tcReadingSchedule.getScheduleCode());
            tcReadingTask.setScheduleId(tcReadingSchedule.getId());
            tcReadingTask.setScheduleContent(tcReadingSchedule.getScheduleName());
            tcReadingTask.setTaskContent("资产变更抄表申请");
            tcReadingTask.setMeterType(type);
            tcReadingTask.setStatus(1);// 执行中
            tcReadingTask.setAuditStatus(1);// 审核完成
            tcReadingTask.setStartTime(new Date());
            tcReadingTask.setEndTime(new Date());
            tcReadingTask.setAuditStartTime(new Date());
            tcReadingTask.setAuditEndTime(new Date());
            tcReadingTask.setCreateTime(new Date());
            tcReadingTaskMapper.insert(tcReadingTask);


            /**
             * 记录抄表位置
             */
            TcMeterRelation tcMeterRelation = new TcMeterRelation();
            tcMeterRelation.setType(3);// 任务
            tcMeterRelation.setBuildingCode(tcReadingTask.getReadingPosition());
            tcMeterRelation.setRelationId(id);
            tcMeterRelationMapper.add(tcMeterRelation);
        }else {
            mm.setMessage("没有一个有效的计划");
            throw new ECPBusinessException("没有一个有效的计划");
        }
        return mm;
    }

	@Override
	public BaseDto requestReadingMeterNew(WyBusinessContext ctx, TcOrderChangeAssetComplaint tcOrderChangeAssetComplaint) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			if(CommonUtils.isEmpty(tcOrderChangeAssetComplaint)){
				msgMap.setMessage(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的参数为空!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			TSysUserService tSysUserService = (TSysUserService)SpringContextHolder.getBean("tSysUserServiceImpl");
			String readingPersonId = tcOrderChangeAssetComplaint.getReadingPersonId();
			if(StringUtils.isNotBlank(readingPersonId)){
				TSysUserSearch tSysUserSearch = new TSysUserSearch();
				tSysUserSearch.setLoginName(readingPersonId);
				List<TSysUserList>  tSysUserLists =  tSysUserService.findByCondition(ctx,tSysUserSearch);
				if(CollectionUtils.isNotEmpty(tSysUserLists)){
					tcOrderChangeAssetComplaint.setReadingPersonId(tSysUserLists.get(0).getUserId());
				}else{
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("没有该抄表员，请重新输入!");
					baseDto.setMessageMap(msgMap);
					return baseDto;
				}
			}else{
				msgMap.setMessage(MessageMap.INFOR_ERROR);
				msgMap.setMessage("抄表员不能为空，请重新输入!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			TcOrderComplaintService tcOrderComplaintService = (TcOrderComplaintService)SpringContextHolder.getBean("tcOrderComplaintServiceImpl");
			baseDto = tcOrderComplaintService.insertAssetsChange(ctx, tcOrderChangeAssetComplaint);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}

	@Override
	public BaseDto getBillByBuildCode(WyBusinessContext ctx, String buildCode) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String,Object>  wyMap = this.tBsChargeBillHistoryMapper.getBillByBuildCode("1",ctx.getProjectId(), buildCode);//物业管理费
			Map<String,Object> btMap = this.tBsChargeBillHistoryMapper.getBillByBuildCode("2",ctx.getProjectId(), buildCode); //本体
			Map<String,Object> waterMap = this.tBsChargeBillHistoryMapper.getBillByBuildCode("3",ctx.getProjectId(), buildCode); //水费
			Map<String,Object> electMap = this.tBsChargeBillHistoryMapper.getBillByBuildCode("4",ctx.getProjectId(), buildCode); //电费
			
			if(CommonUtils.isEmpty(wyMap)){
				wyMap = new HashMap<String,Object>();
			}
			wyMap.put("colum", "物业管理费");
			if(CommonUtils.isEmpty(btMap)){
				btMap = new HashMap<String,Object>();
			}
			btMap.put("colum", "本体基金");
			if(CommonUtils.isEmpty(waterMap)){
				waterMap = new HashMap<String,Object>();
			}
			waterMap.put("colum", "水费");
			if(CommonUtils.isEmpty(electMap)){
				electMap = new HashMap<String,Object>();
			}
			electMap.put("colum", "电费");
			//对于水电表还需要去查询最后抄表时间和抄表用量
			Map<String,Object> waterParamMap = new HashMap<String,Object>();
			waterParamMap.put("projectId", ctx.getProjectId());
			waterParamMap.put("buildCode", buildCode);
			waterParamMap.put("type", 3);
			Map<String,Object> waterDataMap =  this.tcMeterDataMapper.findNewBybuildCodeAndProId(waterParamMap);//水表的最后一次抄表数据
			
			Map<String,Object> electParamMap = new HashMap<String,Object>();
			electParamMap.put("projectId", ctx.getProjectId());
			electParamMap.put("buildCode", buildCode);
			electParamMap.put("type", 4);
			Map<String,Object> electDataMap =  this.tcMeterDataMapper.findNewBybuildCodeAndProId(electParamMap);//电表的最后一次抄表数据
			
			//这里还需要查询各个账户余额
			TBsAssetAccount wyAccount = tBsAssetAccountMapper.lookupByBuildCodeAndType(buildCode,1); 
			if(CommonUtils.isNotEmpty(wyAccount)){
				wyMap.put("accountBalance", wyAccount.getAccountBalance());
			}else{
				wyMap.put("accountBalance", 0);
			}
			TBsAssetAccount btAccount =tBsAssetAccountMapper.lookupByBuildCodeAndType(buildCode,2); 
			if(CommonUtils.isNotEmpty(btAccount)){
				btMap.put("accountBalance", wyAccount.getAccountBalance());
			}else{
				btMap.put("accountBalance", 0);
			}
			TBsAssetAccount waterAccount= tBsAssetAccountMapper.lookupByBuildCodeAndType(buildCode,3);
			if(CommonUtils.isNotEmpty(waterAccount)){
				waterMap.put("accountBalance", waterAccount.getAccountBalance());
			}else{
				waterMap.put("accountBalance", 0);
			}
			TBsAssetAccount electAccount= tBsAssetAccountMapper.lookupByBuildCodeAndType(buildCode,4);
			if(CommonUtils.isNotEmpty(electAccount)){
				electMap.put("accountBalance", electAccount.getAccountBalance());
			}else{
				electMap.put("accountBalance", 0);
			}
			if(CommonUtils.isNotEmpty(waterDataMap)){
				waterMap.put("readingTime", waterDataMap.get("readingTime"));
				waterMap.put("useCount", waterDataMap.get("useCount"));
				waterMap.put("totalReading", waterDataMap.get("totalReading"));
				
			}
			
			if(CommonUtils.isNotEmpty(electDataMap)){
				electMap.put("readingTime", electDataMap.get("readingTime"));
				electMap.put("useCount", electDataMap.get("useCount"));
				electMap.put("totalReading", electDataMap.get("totalReading"));
			}
			list.add(wyMap);
			list.add(btMap);
			list.add(waterMap);
			list.add(electMap);
			
			baseDto.setLstDto(list);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 手动计费
	 */
	public BaseDto manualBill(WyBusinessContext ctx,String buildCode,String buildName)throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			TBsAssetAccountServie tBsAssetAccountServie = (TBsAssetAccountServie)SpringContextHolder.getBean("tBsAssetAccountServieImpl");
			String error = tBsAssetAccountServie.checkNoBillOrNoAudit(ctx, buildCode, buildName);
			if(StringUtils.isBlank(error)){
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(error);
			}
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	private String checkCollingDatas(String projectId, String buildingCode){
		if(CommonUtils.paramsHasNull(projectId,buildingCode)) return null;
		
		List<String> buildingCodes = Arrays.asList(buildingCode);
		
		List<String> fullNames = null;
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

	@Override
	public BaseDto checkCollingDatas(WyBusinessContext ctx, String buildingCode,String projectId) {
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(checkCollingDatas(projectId, buildingCode));
		return returnDto;
	}
}
