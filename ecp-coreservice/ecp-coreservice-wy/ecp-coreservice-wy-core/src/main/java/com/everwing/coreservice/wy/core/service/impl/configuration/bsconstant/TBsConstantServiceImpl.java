package com.everwing.coreservice.wy.core.service.impl.configuration.bsconstant;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.bsconstant.TBsConstant;
import com.everwing.coreservice.common.wy.service.configuration.bsconstant.TBsConstantService;
import com.everwing.coreservice.wy.dao.mapper.configuration.bsconstant.TBsConstantMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("tBsConstantServiceImpl")
public class TBsConstantServiceImpl implements TBsConstantService{

	private static final Logger log = Logger.getLogger(TBsConstantServiceImpl.class);
	
	@Autowired
	 TBsConstantMapper tBsConstantMapper;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto singleAdd(WyBusinessContext ctx, TBsConstant tBsConstant) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			if(CommonUtils.isEmpty(tBsConstant)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的常量信息不能为空!请检查");
			}else{
					if(StringUtils.isBlank(tBsConstant.getId())){
						TBsConstant tbs = this.tBsConstantMapper.getTBsConByName(tBsConstant.getBillConstantName(), tBsConstant.getProjectId());
						tBsConstant.setCreateTime(new Date());
						tBsConstant.setCreateId(ctx.getUserId());
						tBsConstant.setCreateName(ctx.getLoginName());
						if(!CommonUtils.isEmpty(tbs)){
							msgMap.setFlag(MessageMap.INFOR_ERROR);
							msgMap.setMessage("该常量名已存在;请换一个常量名称");
						}else{
							tBsConstantMapper.singleAdd(tBsConstant); //新增
							msgMap.setFlag(MessageMap.INFOR_SUCCESS);
						}
						
					}else{
						//修改
						tBsConstant.setLastUpdateTime(new Date());
						tBsConstant.setLastUpdateId(ctx.getUserId());
						tBsConstant.setLastUpdateName(ctx.getLoginName());
						tBsConstantMapper.updateConstant(tBsConstant);
						msgMap.setFlag(MessageMap.INFOR_SUCCESS);
					}
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("新增保存失败");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

	@Override
	public BaseDto listPageConstants(WyBusinessContext ctx,
			TBsConstant tBsConstant) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			List<TBsConstant> lstDto = this.tBsConstantMapper.listPageConstants(tBsConstant);
			baseDto.setLstDto(lstDto);
			baseDto.setPage(tBsConstant.getPage());
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto singleDel(WyBusinessContext ctx, String id) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			this.tBsConstantMapper.delConstant(id);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		
	}
	
	private String getLogStr(String error){
		return String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),error);
	}

	@Override
	public BaseDto getConstantsByProIdAndType(WyBusinessContext ctx,
			TBsConstant tBsConstant) {
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			List<TBsConstant>  lstDto = tBsConstantMapper.getConstantsByProIdAndType(tBsConstant);
			baseDto.setLstDto(lstDto);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

}
