package com.everwing.coreservice.wy.core.service.impl.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsChargingTypeService;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.utils.FormulaCalculationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("tBsChargingTypeServiceImpl")
public class TBsChargingTypeServiceImpl implements TBsChargingTypeService {

	private static final Logger logger = Logger.getLogger(TBsChargingTypeServiceImpl.class); 
	
	@Autowired
	private TBsChargeTypeMapper tBsChargeTypeMapper;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageChargingType(WyBusinessContext ctx,
			TBsChargeType tBsChargeType) throws ECPBusinessException{
		 BaseDto basDTo = new BaseDto();
		 MessageMap msgMap = new MessageMap();
		try {
			List<TBsChargeType> listDto = tBsChargeTypeMapper.listPageChargingType(tBsChargeType);
			basDTo.setLstDto(listDto);
			basDTo.setPage(tBsChargeType.getPage());
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			basDTo.setMessageMap(msgMap);
			return basDTo;
		} catch (Exception e) {
			logger.info("异常:"+e.getMessage());
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		
	}
	
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto addChargingType(WyBusinessContext ctx,
			TBsChargeType tBsChargeType) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			if(StringUtils.isBlank(tBsChargeType.getId())){ //新增
				//校验费用项名称
				TBsChargeType tbs = this.tBsChargeTypeMapper.getByRuleIdAndName(tBsChargeType.getChargingRuleId(), tBsChargeType.getChargingName());
				if(CommonUtils.isNotEmpty(tbs)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("收费新细项名称已存在，请重新填写!");
				}else{
					tBsChargeType.setCreateBy(ctx.getUserId());
					tBsChargeType.setCreateTime(new Date());
					this.tBsChargeTypeMapper.insert(tBsChargeType);
					msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				}
			}else{//修改
				tBsChargeType.setUpdateBy(ctx.getUserId());
				tBsChargeType.setUpdateTime(new Date());
				String formulaInfoValue=tBsChargeType.getFormulaInfoValue();
				if(StringUtils.isNotBlank(formulaInfoValue)){
					//保存公式 需要运算公式进行校验
					Double useCount=10.0;
					Double peakCount=10.0;
					Double vallCount=10.0;
					Double commCount=10.0;
					Object obj= FormulaCalculationUtil.waterElectCalculation(formulaInfoValue, useCount, peakCount, vallCount, commCount);
					if(CommonUtils.isEmpty(obj)){
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("输入的公式有误!");
					}else{
						this.tBsChargeTypeMapper.updateChargeType(tBsChargeType);
						msgMap.setFlag(MessageMap.INFOR_SUCCESS);
					}
				}else{
					this.tBsChargeTypeMapper.updateChargeType(tBsChargeType);
					msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				}
			}
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			logger.info("保存费用项失败:"+e.getMessage());
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto batchDel(WyBusinessContext ctx, TBsChargeType tBsChargeType) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			this.tBsChargeTypeMapper.batchDel(tBsChargeType.getIds());
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			logger.info("删除费用类型失败:"+e.getMessage());
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto testOpeation(WyBusinessContext ctx, String formulaValue,
			String billingFeeItemTestValue) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		//一个公式只能有一个计费项目
		try {
//			Calculator calculator = new Calculator(); //弃用MATLAB来进行计算，改用解析脚本
			if(StringUtils.isBlank(formulaValue)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的公式为空!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			if(StringUtils.isBlank(billingFeeItemTestValue)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的计费项目的值为空!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			String[] arrstr = formulaValue.split(" ");
			formulaValue="";
			for(int i=0;i<arrstr.length;i++){
				if(arrstr[i].equals("$Count")){
					arrstr[i]=billingFeeItemTestValue;
				}
				if(arrstr[i].equals("$PeakCount")){
					arrstr[i]=billingFeeItemTestValue;
				}
				if(arrstr[i].equals("$VallCount")){
					arrstr[i]=billingFeeItemTestValue;
				}
				if(arrstr[i].equals("$CommCount")){
					arrstr[i]=billingFeeItemTestValue;
				}
				formulaValue = formulaValue+arrstr[i];
			}
			if(StringUtils.isBlank(formulaValue) || StringUtils.isBlank(billingFeeItemTestValue)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的公式或者计费项目的值不能为空");
			}else{
				Object obj = FormulaCalculationUtil.numericalCalculation(formulaValue);
				if(CommonUtils.isEmpty(obj)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("计算公式有误,不能计算!");
				}else{
					if(obj.toString().equals("Infinity")){
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("计算公式有误，除数不能为0!");
					}else{
						msgMap.setFlag(MessageMap.INFOR_SUCCESS);
						baseDto.setObj(String.valueOf(obj));
					}
				}
			}
//			Object[] obj = calculator.formulaCalculation(1, formulaValue,Double.parseDouble(billingFeeItemTestValue));
			baseDto.setMessageMap(msgMap);
		  return baseDto;
		} catch (ECPBusinessException e) {
			logger.info(getLogStr(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	private String getLogStr(String error){
		return String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),error);
	}
	
	
	
	

}
