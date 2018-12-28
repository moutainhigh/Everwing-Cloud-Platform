package com.everwing.coreservice.wy.core.service.impl.business.watermeter;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcMeterBuilding;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.service.business.watermeter.TcWaterMeterService;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterOperRecordMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tcWaterMeterServiceImpl")
public class TcWaterMeterServiceImpl implements TcWaterMeterService{

	
	@Autowired
	private TcWaterMeterMapper tcWaterMeterMapper;
	@Autowired
	private TcWaterMeterOperRecordMapper tcWaterMeterOperRecordMapper;
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;

	private static final Logger logger=Logger.getLogger(TcWaterMeterServiceImpl.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto<String, Object> listPageWaterMeterInfos(String companyId,TcWaterMeter tcWaterMeter) {
		//查询直接返回结果集
		List<TcWaterMeter> resultList=this.tcWaterMeterMapper.listPageWaterMeterInfos(tcWaterMeter);
		return new BaseDto(resultList,tcWaterMeter.getPage());
	}

	
	/**
	 * 新增业务调整，新建水表和关联收费对象 关联关系表   (多对多关联关系单独存放)
	 * 1.新增基础数据信息
	 * 2.新增水表和关联收费对象的关联关系信息
	 */
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap addWaerMeterInfo(String companyId, TcWaterMeter tcWaterMeter) {
		
		if(CommonUtils.isEmpty(tcWaterMeter) || CommonUtils.isEmpty(tcWaterMeter.getRelationBuilding())) {
			return new MessageMap(MessageMap.INFOR_ERROR,"参数信息不可以为空");
		}
		//添加校验,一个建筑只能关联一个有效的水表信息
		if( !checkWaterMeterByBuilding(tcWaterMeter.getRelationBuilding()) ) {
			return new MessageMap(MessageMap.INFOR_ERROR,"此建筑资产已经关联了水表信息，请重新选择");
		}
		int num=tcWaterMeterMapper.addWaerMeterInfo(tcWaterMeter);
		if(num>0){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"插入水表基础数据成功");
		}else{
			return new MessageMap(MessageMap.INFOR_ERROR,"插入水表基础数据失败");
		}
	}

	/**
	 * @describe 校验在新增，或修改水表基础数据信息时，一个建筑智能关联一个水表信息
	 * @return
	 */
	public boolean checkWaterMeterByBuilding(String relationBuildingCode) {
		int num=tcWaterMeterMapper.checkWaterMeterByBuilding(relationBuildingCode);
		if(num > 0) return false;
		return true;
	}
	
	
	/**
	 * 1.修改水表信息基础数据信息
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap updateWaerMeterInfo(String companyId, TcWaterMeter tcWaterMeter) {
		
		
		if(CommonUtils.isEmpty(tcWaterMeter.getCode())){
			return new MessageMap(MessageMap.INFOR_WARNING,"请检查参数信息");
		}
		//添加校验,一个建筑只能关联一个有效的水表信息
		if( !checkWaterMeterByBuilding(tcWaterMeter.getRelationBuilding()) ) {
			return new MessageMap(MessageMap.INFOR_ERROR,"此建筑资产已经关联了水表信息，请重新选择");
		}
		int num=tcWaterMeterMapper.updateWaerMeterInfo(tcWaterMeter);
		if(num>0){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"修改水表基础数据成功");
		}else{
			return new MessageMap(MessageMap.INFOR_ERROR,"修改水表基础数据失败");
		}
	}


	/**
	 * 更换操作
	 * 1：停用更换前的水表
	 * 2:启用更换的水表
	 * 4:插入更换操作信息记录表
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class) 
	public MessageMap replaceWaerMeterByOne(String companyId, TcHydroMeterOperationRecord tcHydroMeterOperationRecord) {
		
		if(CommonUtils.isEmpty(tcHydroMeterOperationRecord.getReplaceBeforeCode()) || CommonUtils.isEmpty(tcHydroMeterOperationRecord.getReplaceAfterCode())){
			return new MessageMap(MessageMap.INFOR_ERROR,"参数异常，请检查参数信息");
		}
		//TODO 此处不对更换前读数与更换后读数用CommonUtils.isEmpty进行判断, iSEmpty会判断==0的情况
		/*if(CommonUtils.isEmpty(tcHydroMeterOperationRecord.getReadingBefore()) || CommonUtils.isEmpty(tcHydroMeterOperationRecord.getReadingAfter())){
			return new MessageMap(MessageMap.INFOR_ERROR,"参数异常，请检查参数信息");
		}*/
		//禁用被替换的水表
		TcWaterMeter tcWaterMeter=new TcWaterMeter();
		tcWaterMeter.setCode(tcHydroMeterOperationRecord.getReplaceBeforeCode());//被替换的水表编号
		tcWaterMeter.setState(1);//禁用
		int num= tcWaterMeterMapper.startStopWaerMeterByOne(tcWaterMeter);
		//水表初始化读数（如果此表还没有抄过表就更换了，就会使用到）
		tcWaterMeter=tcWaterMeterMapper.selectTcWaterMeterByCode(tcWaterMeter);
		Double initAmount =tcWaterMeter.getInitAmount();
		if( num<= 0)  return new MessageMap(MessageMap.INFOR_ERROR,"修改更换前水表信息出错");
		
		//启用新换的水表
		tcWaterMeter.setCode(tcHydroMeterOperationRecord.getReplaceAfterCode());//替换的水表编号
		tcWaterMeter.setState(0);//启用
		//String newId=tcWaterMeterMapper.selectTcWaterMeterByCode(tcWaterMeter).getId();
		num= tcWaterMeterMapper.startStopWaerMeterByOne(tcWaterMeter);
		if(num <= 0) return new MessageMap(MessageMap.INFOR_ERROR,"修改更换后水表信息出错");
		
		//修改更换后水表的关联收费对象，保持两表的关联收费对象一致
		tcWaterMeterMapper.updateWaterMeterForReplace(tcWaterMeter.getRelationBuilding(),tcHydroMeterOperationRecord.getReplaceAfterCode());
		
		//获取更换前水表的最新读数（通过抄表记录查询）
		TcMeterData tcMerterData=new TcMeterData();
		tcMerterData.setMeterCode(tcHydroMeterOperationRecord.getReplaceBeforeCode());
		tcMerterData=tcMeterDataMapper.getLastData(tcMerterData);
		Double lastAmount=initAmount;
		
		if(CommonUtils.isNotEmpty(tcMerterData) && CommonUtils.isNotEmpty(tcMerterData.getTotalReading())){
			lastAmount=tcMerterData.getTotalReading();
		}
		tcHydroMeterOperationRecord.setUsedAmount(tcHydroMeterOperationRecord.getReadingBefore() - lastAmount);
		
		//需要修改抄表数据表,当前最新一批
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("beforeCode", tcHydroMeterOperationRecord.getReplaceBeforeCode());
		paramMap.put("afterCode", tcHydroMeterOperationRecord.getReplaceAfterCode());       //修改表编号为最新.
		paramMap.put("meterType", 0);														//表类型
		paramMap.put("useCount", tcHydroMeterOperationRecord.getUsedAmount());              //本次换表时,使用的数量
		paramMap.put("lastTotalReading", tcHydroMeterOperationRecord.getReadingAfter());    //上次读数为更换后的读数
		paramMap.put("remark", "更换: 原表".concat(tcHydroMeterOperationRecord.getReplaceBeforeCode()));
		this.tcMeterDataMapper.replaceMeter(paramMap);   //替换掉当前时间段所在周期内   与 替换前表编号有关的所有抄表数据
		
		
		//插入操作记录信息
		num= tcWaterMeterOperRecordMapper.addWaerMeterOperRecord(tcHydroMeterOperationRecord);
		if( num >0 )  return new MessageMap(MessageMap.INFOR_SUCCESS,"更换水表操作成功");
		return new MessageMap(MessageMap.INFOR_ERROR,"插入操作信息失败");
	}

	
	/**
	 * 启用和禁用水表
	 * 1:修改当前相关水表的状态（禁用或启用）
	 * 2:写一条操作记录信息
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap startStopWaerMeterByOne(String companyId,TcHydroMeterOperationRecord tcHydroMeterOperationRecord,String meterId,int state) {
		
		if(CommonUtils.isEmpty(meterId))
			return new MessageMap(MessageMap.INFOR_ERROR,"参数不允许为空");
		TcWaterMeter tcWaterMeter=new TcWaterMeter();
		tcWaterMeter.setCode(meterId);
		tcWaterMeter.setState(state);
		int num=tcWaterMeterMapper.startStopWaerMeterByOne(tcWaterMeter);
		if(num<=0) return new MessageMap(MessageMap.INFOR_ERROR,"修改水表状态失败");
		
		num=tcWaterMeterOperRecordMapper.addWaerMeterOperRecord(tcHydroMeterOperationRecord);
		if(num>0){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"操作成功");
		}else{
			return new MessageMap(MessageMap.INFOR_ERROR,"操作失败");
		}
	}
	

	@Override
	public BaseDto<String, Object> loadEnclosureInfoByid(String companyId, TcWaterMeter tcWaterMeter) {
		return null;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto<String, Object> listPageLoadMeterReadingRecordById(String companyId, TcWaterMeter tcWaterMeter) {
		if(CommonUtils.isEmpty(tcWaterMeter.getCode())){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "参数为空，请传递参数信息"));
		}
		TcMeterData tcMerterData=new TcMeterData();
		tcMerterData.setMeterCode(tcWaterMeter.getCode());
		tcMerterData.setPage(tcWaterMeter.getPage());
		List<TcMeterData> dataList= this.tcMeterDataMapper.listPageHistories(tcMerterData);
		return new BaseDto(dataList, tcMerterData.getPage());
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseDto<String, Object> listPageloadWaterMeterForChange(String companyId, TcWaterMeter tcWaterMeter) {
		/**
		 * 更换水表提供选择更换的水表   条件
		 * 1：目前是停用状态    2：关联收费对象应该是一样的（待定）
		 */
		return new BaseDto(this.tcWaterMeterMapper.listPageloadWaterMeterForChange(tcWaterMeter),tcWaterMeter.getPage());
	}

	/**
	 * 1.删除关联关系
	 * 2.删除水表基础数据信息
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap deleteWaterMeterInfos(String companyId, String ids) {
		int num=tcWaterMeterMapper.deleteWaterMeterInfos(CommonUtils.str2List(ids, ","));
		if(num>0){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"成功删除"+num+"条说表基础数据信息");	
		}else{
			return new MessageMap(MessageMap.INFOR_ERROR,"执行删除水表信息失败");	
		}
	}

	@Override
	public MessageMap checkWaterMeterCode(String companyId, String code) {
		int num=this.tcWaterMeterMapper.checkWaterMeterCode(code);
		if(num>0) return new MessageMap(MessageMap.INFOR_ERROR,"水表编号已存在，请修改后提交");
		return new MessageMap(MessageMap.INFOR_SUCCESS,"水表编号符合要求");
	}



	/**
	 * 通过tcwatermeter对象得到一个关联关系对象 信息
	 */
	public List<TcMeterRelation> getTcMeterfeeobjList(TcWaterMeter tcWaterMeter){
		List<TcMeterRelation> tcMeterfeeList=new ArrayList<>();
		//分割 RelationBuilding字段
		if(CommonUtils.isNotEmpty(tcWaterMeter.getRelationBuilding())){
			String [] relationBuildingCodes=tcWaterMeter.getRelationBuilding().split(",");
			for (String string : relationBuildingCodes) {
				TcMeterRelation tcMeterfeeobj=new TcMeterRelation();
				tcMeterfeeobj.setBuildingCode(string);
				tcMeterfeeobj.setType(0);
				tcMeterfeeobj.setRelationId(tcWaterMeter.getId());
				tcMeterfeeList.add(tcMeterfeeobj);
			}
		}
		return tcMeterfeeList;
	}
	
	

	




	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto countMeters(String companyId, TcWaterMeter entity) {
		BaseDto dto = new BaseDto(this.tcWaterMeterMapper.countMeters(entity));
		return dto;
	}

	@Override
	public BaseDto<String, Object> listPageWaterMeterByLevel(String companyId, TcWaterMeter tcWaterMeter) {
		//查询直接返回结果集
		List<TcWaterMeter> resultList=this.tcWaterMeterMapper.listPageWaterMeterByLevel(tcWaterMeter);
		return new BaseDto(resultList,tcWaterMeter.getPage());
	}


	@Override
	public BaseDto<String, Object> loadBuildingAndMeter(String companyId, String projectId, String meterType) {
		// TODO Auto-generated method stub
		MessageMap mm=new MessageMap();
		if(CommonUtils.isEmpty(projectId) || CommonUtils.isEmpty(meterType)) {
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("请求参数不允许有空值");
		}
		//根据项目id和表类型查询树信息
		TcWaterMeter entity=new TcWaterMeter();
		entity.setProjectId(projectId);
		entity.setState(Integer.parseInt(meterType));//没有meterType字段，暂时先用state替代一下
		List<TcMeterBuilding> result =this.tcWaterMeterMapper.getBuildingAndMeter(entity);
		BaseDto baseDto=new BaseDto<>(result);
		mm.setFlag(MessageMap.INFOR_SUCCESS);
		mm.setMessage("查询成功");
		baseDto.setMessageMap(mm);
		return baseDto;
	}
	
	@Override
	public List<TcWaterMeter> findsByBuildingCode(String companyId,String buildingCode) {
		List<TcWaterMeter> resultList=this.tcWaterMeterMapper.findsByBuildingCode(buildingCode);
		return resultList;
	}

	@Override
	public BaseDto listPageWaterMeterByBuildingCode(WyBusinessContext ctx, TcWaterMeter tcWaterMeter) {
		List<TcWaterMeter> list =tcWaterMeterMapper.listPageWaterMeterByBuildingCode(tcWaterMeter);
		BaseDto baseDto = new BaseDto<>();
		baseDto.setLstDto(list);
		baseDto.setPage(tcWaterMeter.getPage());
		return baseDto;
	}


}
