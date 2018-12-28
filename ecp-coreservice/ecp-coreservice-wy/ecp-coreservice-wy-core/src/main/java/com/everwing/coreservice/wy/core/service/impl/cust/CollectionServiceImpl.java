package com.everwing.coreservice.wy.core.service.impl.cust;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.dto.TBcCollectionDto;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.cust.TBankInfo;
import com.everwing.coreservice.common.wy.entity.cust.TBankInfoExample;
import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.service.cust.CollectionService;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.TBcCollectionMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.jrl.TBcJrlBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.back.TBcUnionBackBodyMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.project.TBcProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.CollectionExtraMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.CollectionMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.TBankInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 银行托收业务实现
 *
 * @author DELL shiny
 * @create 2017/9/14
 */
@Service("collectionService")
public class CollectionServiceImpl implements CollectionService {

    private static final Logger logger= LogManager.getLogger(CollectionServiceImpl.class);

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CollectionExtraMapper collectionExtraMapper;

    @Autowired
    private TBankInfoMapper tBankInfoMapper;

    @Autowired
    private TBcProjectMapper tBcProjectMapper;

    @Autowired
    private PersonCustNewMapper personCustNewMapper;
    
    @Autowired
    private TBcJrlBodyMapper tBcJrlBodyMapper;
    
    @Autowired
    private TBcCollectionMapper tBcCollectionMapper;
    
    @Autowired
    private TBcUnionBackBodyMapper tBcUnionBackBodyMapper;
    
    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Override
    public BaseDto queryListByBuildingCode(String companyId,TBcCollectionDto tBcCollectionDto) {
        return new BaseDto(collectionExtraMapper.listPageByBuildingCode(tBcCollectionDto),tBcCollectionDto.getPage());
    }

    @Override
    public BaseDto queryBanksByProjectId(String companyId, String projectId) {
        if(StringUtils.isNotEmpty(projectId)){
            //查询项目是金融联还是银联
            TBcProject tBcProject=tBcProjectMapper.findByProjectId(projectId);
            if(tBcProject==null){
                logger.info("根据projectId未找到project！");
                return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"数据错误！"));
            }else {
                Integer type=getBankUnionType(tBcProject);
                boolean isUnion=(type>0);
                if(type==2){//未开启托收
                    return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"资产所在项目未开启托收！"));
                }
                TBankInfoExample tBankInfoExample=new TBankInfoExample();
                TBankInfoExample.Criteria criteria=tBankInfoExample.createCriteria().andIsUnionEqualTo(isUnion);
                if(isUnion) {
                    //关闭金融联外地卡
                    criteria.andIsLocalEqualTo(false);
                }
                List<TBankInfo> tBankInfoList=tBankInfoMapper.selectByExample(tBankInfoExample);
                return new BaseDto(tBankInfoList,null);
            }
        }else {
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"数据错误！"));
        }
    }

    @Override
    public BaseDto queryChargingItemsByProjectId(String companyId, String projectId) {
        if(StringUtils.isNotEmpty(projectId)){
            //查询项目是金融联还是银联
            TBcProject tBcProject=tBcProjectMapper.findByProjectId(projectId);
            if(tBcProject==null) {
                logger.info("根据projectId未找到project！");
                return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "数据错误！"));
            }else{
                List<Integer> chargingItems=new ArrayList<>(4);
                if(tBcProject.getWyStatus()==0){
                    chargingItems.add(0);
                }
                if(tBcProject.getBtStatus()==0){
                    chargingItems.add(1);
                }
                if(tBcProject.getWaterStatus()==0){
                    chargingItems.add(2);
                }
                if(tBcProject.getElectStatus()==0){
                    chargingItems.add(3);
                }
                return new BaseDto(chargingItems,null);
            }
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"数据错误！"));
    }

    public String generateContractNo(){
        int hashCode=UUID.randomUUID().toString().hashCode();
        if(hashCode<0){
            hashCode=-hashCode;
        }
        return String.format("%010d",hashCode);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public BaseDto insert(String companyId,TBcCollection tBcCollection) {
        boolean isExists=checkExists(tBcCollection.getRelateBuildingCode(),tBcCollection.getChargingItems());
        if(isExists){
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"该建筑的收费项已存在！"));
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        tBcCollection.setId(UUID.randomUUID().toString());
        Date startTime=tBcCollection.getStartTime();
        Date date=new Date();
        String now=simpleDateFormat.format(date);
        String seted=simpleDateFormat.format(startTime);
        if(now.equals(seted)) {//如果是当前日期设置生效否则设置暂不生效
            tBcCollection.setStatus(1);
        }else {
            tBcCollection.setStatus(0);
        }
        //查询客户的证件类型和证件号码
        PersonCustNew personCustNew=personCustNewMapper.getPersonCustById(tBcCollection.getCustId());
        if(personCustNew!=null){
            if(StringUtils.isNotEmpty(personCustNew.getCardType())){
                tBcCollection.setCardType(Integer.valueOf(personCustNew.getCardType()));
            }
            String cardNum=personCustNew.getCardNum();
            if(StringUtils.isNotEmpty(cardNum)) {
                tBcCollection.setCardNo(cardNum.replace("S",""));
            }
            tBcCollection.setProjectId(tBcCollection.getProjectId());
        }
        //设置合同号
        tBcCollection.setContractNo(generateContractNo());
        int count=collectionMapper.insert(tBcCollection);
        if(count>0){
            TBankInfo tBankInfo=tBankInfoMapper.selectByPrimaryKey(tBcCollection.getCreateBank());
            Integer isUnion=tBankInfo.getIsUnion();
            if(isUnion==0){//更新项目下金融联或银联条数
                tBcProjectMapper.updateUnionCountByProjectId(tBcCollection.getProjectId());
            }else {
                tBcProjectMapper.updateJrlCountByProjectId(tBcCollection.getProjectId());
            }
            return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"托收办理成功！"));
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"托收办理失败！"));
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public BaseDto update(String companyId, TBcCollection tBcCollection) {
    	
    	//判断当前户是否处于托收待回盘状态
    	List<TcBuildingList> buildings = tcBuildingMapper.findByCode(null, tBcCollection.getRelateBuildingCode());
    	String projectId = (CommonUtils.isNotEmpty(buildings) && CommonUtils.isNotEmpty(buildings.get(0))) ? buildings.get(0).getProjectId() : "";

    	String fullName = checkCollingDatas(projectId, tBcCollection.getRelateBuildingCode());
    	if(CommonUtils.isNotEmpty(fullName)){
    		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullName + " : 处于  [托收待回盘] 状态 , 为防止托收数据变更之后无法回盘, 当前无法办理托收更改操作. 请等待回盘后再进行此操作. "));
    	}
        String updateBy= WyBusinessContext.getContext().getUserId();
    	tBcCollection.setUpdateBy(updateBy);
        int count=collectionMapper.updateByPrimaryKeySelective(tBcCollection);
        if(count>0){
            return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"更新成功!"));
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"更新失败!"));
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public BaseDto batchDelete(String companyId,String ids) {
    	
    	String fullNames = checkCollingDatasByCollectionIds(Arrays.asList(ids.split(",")));
    	if(CommonUtils.isNotEmpty(fullNames))
    		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, fullNames + " : 处于  [托收待回盘] 状态 , 为防止托收数据变更之后无法回盘, 当前无法办理托收删除操作. 请等待回盘后再进行此操作. "));
    	
        int count=collectionMapper.batchDelete(ids.split(","));
        if(count>0){
            return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"操作成功!"));
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"操作失败!"));
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public BaseDto batchEffective(String companyId, String ids) {
        int count=collectionMapper.batchEffective(ids.split(","));
        if(count>0){
            return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"操作成功!"));
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"操作失败!"));
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public BaseDto batchUnEffective(String companyId, String ids) {
    	
    	String fullNames = checkCollingDatasByCollectionIds(Arrays.asList(ids.split(",")));
    	if(CommonUtils.isNotEmpty(fullNames))
    		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, fullNames + " : 处于  [托收待回盘] 状态 , 为防止托收数据变更之后无法回盘, 当前无法办理托收失效操作. 请等待回盘后再进行此操作. "));
    	
    	
        int count=collectionMapper.batchUnEffective(ids.split(","));
        if(count>0){
            return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"操作成功!"));
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"操作失败!"));
    }

    @Override
    public BaseDto unEffectiveByCustIdAndBuildingCode(String companyId,String custId,String buildingCode) {
    	
    	List<TcBuildingList> buildings = tcBuildingMapper.findByCode(null, buildingCode);
    	
    	String projectId = (CommonUtils.isNotEmpty(buildings) && CommonUtils.isNotEmpty(buildings.get(0))) ? buildings.get(0).getProjectId() : "";
    	
    	//判断当前户是否处于托收待回盘状态
    	String fullName = checkCollingDatas(projectId, buildingCode);
    	if(CommonUtils.isNotEmpty(fullName)){
    		return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,fullName + " : 处于  [托收待回盘] 状态 , 为防止托收数据变更之后无法回盘, 当前无法办理托收失效操作. 请等待回盘后再进行此操作. "));
    	}
    	
        int count=collectionExtraMapper.updateStatusByCustIdAndBuildingCode(custId,buildingCode);
        if(count>0){
            return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"操作成功!"));
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"操作失败!"));
    }

    private Integer getBankUnionType(TBcProject tBcProject){
        Integer wyType=tBcProject.getWyType();
        Integer btType=tBcProject.getBtType();
        Integer eleType=tBcProject.getElectType();
        Integer waterType=tBcProject.getWaterType();
        return wyType==2?(btType==2?(eleType==2?(waterType==2?waterType:waterType):eleType):btType):wyType;
    }

    private boolean checkExists(String buildingCode,String chargingItems){
        int exists=collectionExtraMapper.checkExists(buildingCode,chargingItems);
        if(exists==1){
            return true;
        }
        return false;
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
    
    private String checkCollingDatasByCollectionIds(List<String> ids){
		if(CommonUtils.paramsHasNull(ids)) return null;
		
		List<String> fullNames = new ArrayList<String>();
		String fullName = "";
		for(String id : ids){
			TBcCollection collection = collectionMapper.selectByPrimaryKey(id);
			if(null != collection){
				fullName = checkCollingDatas(collection.getProjectId(), collection.getRelateBuildingCode());
				if(CommonUtils.isNotEmpty(fullName)) fullNames.add(fullName);
			}
		}
		return CommonUtils.isEmpty(fullNames) ? null : StringUtils.join(fullNames,Constants.STR_COMMA);
	}

}
