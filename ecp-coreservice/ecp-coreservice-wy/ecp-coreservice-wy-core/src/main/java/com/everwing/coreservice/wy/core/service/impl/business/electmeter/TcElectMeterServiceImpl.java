package com.everwing.coreservice.wy.core.service.impl.business.electmeter;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.MeterEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ChangeElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterSearch;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.service.business.electmeter.TcElectMeterService;
import com.everwing.coreservice.common.wy.service.sys.TSysProjectService;
import com.everwing.coreservice.wy.core.utils.ToolKit;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterrelation.TcMeterRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterOperRecordMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tcElectMeterServiceImpl")
public class TcElectMeterServiceImpl implements TcElectMeterService {
	private static final Logger log = Logger.getLogger(TcElectMeterServiceImpl.class);
	@Autowired
	TcElectMeterMapper tcElectMeterMapper;
	@Autowired
	TcWaterMeterOperRecordMapper tcWaterMeterOperRecordMapper;
	@Autowired
	TcMeterRelationMapper tcMeterRelationMapper;
	@Autowired
	TcMeterDataMapper tcMeterDataMapper;
	@Autowired
	TSysProjectService tSysProjectService;
	@Autowired
	TcBuildingMapper tcBuildingMapper;
	@Autowired
	private TSysUserMapper tSysUserMapper;


	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto addelectmeter(String companyId,ElectMeter electMeter)throws ECPBusinessException{
		MessageMap msgMap = new MessageMap();
		try {
			if(null != electMeter){
				electMeter.setCompanycode(companyId);
				electMeter.setCreatetime(new Date());
				//后台校验电表编号是否重复
				String code = electMeter.getCode();
				if(StringUtils.isNotBlank(code)){
					ElectMeter e= tcElectMeterMapper.getElectMeterByCode(code,electMeter.getProjectId());
					if(!CommonUtils.isEmpty(e)){
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("您输入的电表编号已存在,请检查！");
					}else{
//						String relationbuilding = electMeter.getRelationbuilding();
//						electMeter.setRelationbuilding("");
					  //收费对象和表是一对一的关系，一个收费对象有且只有一个正在启用状态下的电表
						String projectId = electMeter.getProjectId();
						String reationId = electMeter.getRelationbuilding();
						if(StringUtils.isNotBlank(projectId) && StringUtils.isNotBlank(reationId)){
							ElectMeter elect = this.tcElectMeterMapper.getElectMeterByReationId(projectId, reationId);
							if(CommonUtils.isEmpty(elect)){ //说明该收费对象下还没有表//可以新增保存
								tcElectMeterMapper.addelectmeter(electMeter);
								msgMap.setFlag(MessageMap.INFOR_SUCCESS);
								msgMap.setMessage("新增保存成功!");
							}else{
								msgMap.setFlag(MessageMap.INFOR_ERROR);
								msgMap.setMessage("收费对象["+electMeter.getRelationbuildingName()+"],已经有表["+elect.getElectricitymetername()+"]存在!,不能保存！");
							}
						}else{
							log.info("传入的项目编号或者是收费对象为空!");
						}
						//房和表多对多的关系暂不考虑
//						ElectMeter newelect = tcElectMeterMapper.getElectMeterByCode(electMeter.getCode());
//						List<TcMeterRelation> relationList = ToolKit.createRelation(relationbuilding.split(","), newelect.getElectmeterId());
//						tcMeterRelationMapper.batchAdd(relationList); //新增
					}
				}else{
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("电表编号不能为空,请检查！");
				}
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的电表信息为空,请检查！");
			}
			return new BaseDto(msgMap);
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("保存失败!");
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}

	@Override
	public BaseDto listPageElectmeterByCompany(String companyId,
			ElectMeterSearch electMeterSearch) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			List<ElectMeter> electlists= tcElectMeterMapper.listPageElectmeterByCompany(electMeterSearch);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			msgMap.setMessage("查询成功!");
			baseDto.setLstDto(electlists);
			baseDto.setPage(electMeterSearch.getPage());
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("查询失败!");
			log.info(e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

	@Override
	public BaseDto startOrstopElectMeter(String companyId, ElectMeter electMeter) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		String flag ="";
		try {
			List<String> codes = electMeter.getCodes();
			Integer state = electMeter.getState();
			if(state==MeterEnum.METER_START.getIntValue()) flag="启用";
			if(state==MeterEnum.METER_STOP.getIntValue()) flag ="停用";
			if(codes !=null && codes.size()>0 && state != null){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("codes", codes);
				map.put("state", electMeter.getState());
				map.put("modifyId", electMeter.getModifyid());
				map.put("modifytime", new Date());
				this.tcElectMeterMapper.startOrstopElectMeter(map);
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				msgMap.setMessage(flag+"操作成功");
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("未选择要操作的电表!");
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage(flag+"操作失败");
			log.info(e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

	@Override
	public BaseDto editSave(String companyId, ElectMeter electMeter) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			if(null != electMeter){
				String code = electMeter.getCode();
				if(StringUtils.isNotBlank(code)){
					ElectMeter old = this.tcElectMeterMapper.getElectMeterByCode(code,electMeter.getProjectId());
					if(CommonUtils.isEmpty(old)){
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("修改的电表不存在,可能已经被删除!");
					}else{
						String projectId = electMeter.getProjectId();
						String reationId = electMeter.getRelationbuilding();
						if(StringUtils.isNotBlank(projectId) && StringUtils.isNotBlank(reationId)){
							ElectMeter elect = this.tcElectMeterMapper.getElectMeterByReationId(projectId, reationId);
							if(CommonUtils.isNotEmpty(elect)){
								if(electMeter.getCode().equals(elect.getCode())){//表编号不可能为空
									electMeter.setModifytime(new Date());
									electMeter.setModifyid(electMeter.getModifyid());
									this.tcElectMeterMapper.editSave(electMeter);
									msgMap.setFlag(MessageMap.INFOR_SUCCESS);
									msgMap.setMessage("修改成功!");
								}else{
									msgMap.setFlag(MessageMap.INFOR_ERROR);
									msgMap.setMessage("收费对象["+electMeter.getRelationbuildingName()+"],已经被表["+elect.getElectricitymetername()+"]占用!,不能修改！");
								}
							}else{
								electMeter.setModifytime(new Date());
								electMeter.setModifyid(electMeter.getModifyid());
								this.tcElectMeterMapper.editSave(electMeter);
								msgMap.setFlag(MessageMap.INFOR_SUCCESS);
								msgMap.setMessage("修改成功!");
							}
							
						}else{
							log.info("传入的项目编号或者是收费对象为空!");
						}
					}
					
				}else{
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("电表编号不能为空");
				}
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("没有需要保存的数据");
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("修改保存失败！");
			log.info(e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

	@Override
	public BaseDto delElect(String companyId, ElectMeter electMeter) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			if(null == electMeter){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("没有要操作的数据");
			}else{
				//表和房多对多的关系暂时不考虑
//				this.tcMeterRelationMapper.batchDelByElectMeterCodes(electMeter.getCodes()); //删除关联关系表的数据
				//根据项目来删除
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("list", electMeter.getCodes());
				paramMap.put("projectId", electMeter.getProjectId());
				this.tcElectMeterMapper.delElect(paramMap); //删除电表数据
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				msgMap.setMessage("删除成功!");
			}
			
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("删除失败!");
			log.info(e.getMessage());
		}
		
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

	/**
	 * 变更
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto changeElectMeter(String companyId, ChangeElectMeter changeElectMeter) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			ElectMeter beforChangeElectMeter = changeElectMeter.getBeforechangeelectricty();
			ElectMeter afterChangeElectMeter = changeElectMeter.getAfterelectricty();
			if(CommonUtils.isEmpty(beforChangeElectMeter) || CommonUtils.isEmpty(afterChangeElectMeter)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("变更前/变更后的数据不能为空!");
			}else{
				//操作前先查两个表是否存在
				ElectMeter afterMeter = this.tcElectMeterMapper.getElectMeterByCode(afterChangeElectMeter.getCode(),afterChangeElectMeter.getProjectId());
				ElectMeter beforeMeter = this.tcElectMeterMapper.getElectMeterByCode(beforChangeElectMeter.getCode(),beforChangeElectMeter.getProjectId());
				if(CommonUtils.isEmpty(afterMeter) || CommonUtils.isEmpty(beforeMeter)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("变更或者被变更的数据不存在,可能已经被删除");
				}else{
					//停用更改前的电表
					this.tcElectMeterMapper.editSave(new ElectMeter(beforChangeElectMeter.getCode(),beforChangeElectMeter.getLocation(),MeterEnum.METER_STOP.getIntValue()));
					//房和表多对多的关系暂不考虑
					//修改被替换的表的关联关系
//					Map<String,String> parameterMap = new HashMap<String,String>();
//					parameterMap.put("newId", afterMeter.getElectmeterId());
//					parameterMap.put("oldId", beforeMeter.getElectmeterId());
//					this.tcMeterRelationMapper.updateByrelationId(parameterMap);
					//删除替换表的关联关系
//					List<String> aftercodes=new ArrayList<String>();
//					aftercodes.add(afterChangeElectMeter.getCode());
//					this.tcMeterRelationMapper.batchDelByElectMeterCodes(aftercodes);
					
					//启动保存更改后的电表
					ElectMeter saveAfterElect = new ElectMeter();
					saveAfterElect.setState(MeterEnum.METER_START.getIntValue());//启用
					saveAfterElect.setRelationbuilding(beforeMeter.getRelationbuilding()); //变更后收费对象变为变更前的收费对象
					saveAfterElect.setRelationbuildingName(beforeMeter.getRelationbuildingName());
					this.tcElectMeterMapper.editSave(saveAfterElect);
					//计算变更前的电表用量(初始值+峰平谷)
					TcMeterData tcMeterData = new TcMeterData();
					tcMeterData.setMeterCode(beforeMeter.getCode());
					TcMeterData betcData = tcMeterDataMapper.getLastData(tcMeterData);
					TcHydroMeterOperationRecord tc = ToolKit.createTcHyMeterOpearRecord(beforChangeElectMeter,betcData,beforeMeter,afterChangeElectMeter);
					this.tcWaterMeterOperRecordMapper.addWaerMeterOperRecord(tc);
				}
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("变更操作失败!");
			log.info(e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto startOrStop(String companyId, ElectMeter electMeter) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			if(CommonUtils.isEmpty(electMeter)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("没有需要操作的数据!");
			}else{
				String code = electMeter.getCode();
				Integer state = electMeter.getState();
				ElectMeter oldMeter = this.tcElectMeterMapper.getElectMeterByCode(code,electMeter.getProjectId());
				if(CommonUtils.isEmpty(oldMeter)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("没该电表已经不存在,可能已经被删除!");
				}else{
					oldMeter.setState(state);
					oldMeter.setCreatetime(new Date());
					this.tcElectMeterMapper.editSave(oldMeter); //启停电表
					
					TcMeterData tcMeterData = new TcMeterData();
					tcMeterData.setMeterCode(oldMeter.getCode());
					TcMeterData betcData = tcMeterDataMapper.getLastData(tcMeterData);
					
					TcHydroMeterOperationRecord tc = ToolKit.createTcHyMeterOpearRecord(oldMeter,betcData,oldMeter,electMeter);
					if(state==MeterEnum.METER_START.getIntValue()) tc.setOperationType(MeterEnum.RECORD_OPEARTETYPE_START.getIntValue()); //启用操作
					if(state==MeterEnum.METER_STOP.getIntValue()) tc.setOperationType(MeterEnum.RECORD_OPEARTETYPE_STOP.getIntValue());//停用操作
					this.tcWaterMeterOperRecordMapper.addWaerMeterOperRecord(tc);
				}
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("启停用操作失败!");
			log.info(e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}


	
	
	
	/**
	 * 统计电表结果
	 */
	public BaseDto countMeters(String companyId,ElectMeter electMeter){
//		MessageMap msgMap = new MessageMap();
//		BaseDto baseDto = new BaseDto();
//		try {
//		List list =	this.tcElectMeterMapper.countMeters(electMeter);
//		
//		baseDto.setLstDto(list);
//		msgMap.setFlag(MessageMap.INFOR_SUCCESS);
//		msgMap.setMessage("统计电表成功!");
//		} catch (Exception e) {
//			log.info(e.getMessage());
//			msgMap.setFlag(MessageMap.INFOR_ERROR);
//			msgMap.setMessage("统计电表异常,请联系管理员!");
//		}
//		baseDto.setMessageMap(msgMap);
//		return baseDto;
		BaseDto dto = new BaseDto(this.tcElectMeterMapper.countMeters(electMeter));
		return dto;
	}

	@Override
	public BaseDto listPageFilterRelationBuild(WyBusinessContext ctx,
			TcBuildingSearch	entity) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			List<TcBuilding> resList= this.tcBuildingMapper.listPageFilterRelationBuild(entity);
			baseDto.setLstDto(resList);
			baseDto.setPage(entity.getPage());
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	@Override
	public List<ElectMeter> findsByBuildingCode(String companyId,String buildingCode) {
	
		List<ElectMeter> resultList=this.tcElectMeterMapper.findsByBuildingCode(buildingCode);
		return resultList;
	}

	@Override
	public BaseDto listPageElectMeterByBuildingCode(WyBusinessContext ctx, ElectMeterSearch electMeterSearch) {
		List<ElectMeter> list =tcElectMeterMapper.listPageElectMeterByBuildingCode(electMeterSearch);
		BaseDto baseDto = new BaseDto<>();
		baseDto.setLstDto(list);
		baseDto.setPage(electMeterSearch.getPage());
		return baseDto;
	}

}
