package com.everwing.coreservice.wy.core.service.impl.delivery;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.SettlementEnum;
import com.everwing.coreservice.common.wy.entity.delivery.TJgStaffGrop;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.service.delivery.TJgStaffGropService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgStaffGropMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/***
 * @describe  银账交割的员工组表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgStaffGropServiceImpl")
public class TJgStaffGropServiceImpl extends Resources implements TJgStaffGropService{
	
	private final static Logger logger=Logger.getLogger(TJgStaffGropServiceImpl.class) ;//LogManager.getLogger(TJgStaffGropServiceImpl.class);
	
	@Autowired
	private TJgStaffGropMapper tJgStaffGropMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto addStaffGrop(String companyId, TJgStaffGrop entity) {
		// TODO 增加一条员工组信息，这里同一个user_id只能出现一次
		
		//判断 若当前增加的是收银员或者收银组长, 判断他是不是在其他项目内也充当了收银员或收银组长
		if(entity.getRoleLevel() == SettlementEnum.ROLE_LEVEL_SYY.getIntValue() || entity.getRoleLevel() == SettlementEnum.ROLE_LEVEL_SYZZ.getIntValue()){
			String projectId = this.tJgStaffGropMapper.notExistsInOtherProject(entity.getUserId(), entity.getProjectId());
			if(CommonUtils.isNotEmpty(projectId)){
				TSysProject project = this.tSysProjectMapper.findByCode(projectId);
				return new BaseDto(
						new MessageMap(
								MessageMap.INFOR_WARNING, 
								"当前员工: [" + entity.getStaffName() + "], 在项目: [" + project.getName() + "] 中担任了收银员/收银组长, 无法在当前项目: [" + entity.getProjectName() + "] 中收银"));
			}
			
		}
		
		entity.setId(CommonUtils.getUUID());
		this.tJgStaffGropMapper.addStaffGrop(entity);
		
		BaseDto returnDto = new BaseDto();
		returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"创建成功."));
		returnDto.setLstDto(this.tJgStaffGropMapper.findByObj(entity));
		return returnDto;
	}

	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto getPidInfoByUserId(String companyId, String userId,String projectId) {
		// 根据当前登录用户的userid查询上级信息
		MessageMap msg=new MessageMap();
		if(CommonUtils.isEmpty(userId)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"查询参数不能为空"));
		}
		TJgStaffGrop result=this.tJgStaffGropMapper.getPidInfoByUserId(userId,projectId);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setObj(result);
		msg.setFlag(MessageMap.INFOR_SUCCESS);
		msg.setMessage("查询成功");
		baseDto.setMessageMap(msg);
		return baseDto;
	}



	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto getMyselfInfoByUserId(String companyId, String userId,String projectId) {
		//  根据当前登录用户的userid查询自身角色信息
		MessageMap msg=new MessageMap();
		if(CommonUtils.isEmpty(userId)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"查询参数不能为空"));
		}
		TJgStaffGrop result=this.tJgStaffGropMapper.getMyselfInfoByUserId(userId,projectId);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setObj(result);
		msg.setFlag(MessageMap.INFOR_SUCCESS);
		msg.setMessage("查询成功");
		baseDto.setMessageMap(msg);
		return baseDto;
	}


	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto getStaffGroupInfo(String companyId, String userId,String projectId) {
		// 查询组织结构
		Map<String, String> resultMap=this.tJgStaffGropMapper.getStaffGroupInfo(userId,projectId);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setObj(resultMap);
		return baseDto;
	}



	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto delById(String companyId, String id) {
		if(id == null || id.isEmpty()){
			logger.warn("删除银账交割组织成员: 传入数据为空,无法删除.");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入数据为空,无法删除该成员"));
		}
		
		if(this.tJgStaffGropMapper.deleteById(id) > 0){
			logger.info("删除银账交割组织成员: 删除成功. ");
			return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"删除成功."));
		}else{
//			logger.warn("删除银账交割组织成员: 删除失败 , 该id不存在对应的数据. id:{}",id);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"删除失败,该id不存在对应的数据. "));
		}
	}


	/**
	 * 查询某节点的父节点以及所有子节点
	 */
	@SuppressWarnings({"rawtypes" })
	@Override
	public BaseDto findInfos(String companyId, TJgStaffGrop entity) {
		if(CommonUtils.paramsHasNull(entity.getProjectId())){
//			logger.warn("查询节点: 传入数据为空,查询失败. 传入参数:{}",entity.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空,查询失败."));
		}
		JSONObject obj = new JSONObject();
		
		TJgStaffGrop group = this.tJgStaffGropMapper.findFirstNode();
		if(group == null){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到父节点"));
		}
		
		group.setProjectId(entity.getProjectId());
		rcsFindChildren(group, obj);
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(obj);
		returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功."));
		return returnDto;
	}
	
	private void rcsFindChildren(TJgStaffGrop grop, JSONObject object){
		if(null != grop){
			
			List<TJgStaffGrop> groups = this.tJgStaffGropMapper.findChildrenByObj(grop);
			if(CommonUtils.isNotEmpty(groups)){
				JSONArray array = new JSONArray();
				for(TJgStaffGrop g : groups){
					JSONObject obj = new JSONObject();
					obj.put("id", g.getId());
					obj.put("name", g.getRoleName() + "_" + g.getStaffName());
					obj.put("data", null);
					array.add(obj);
					rcsFindChildren(g, obj);
				}
				object.put("children", array);
			}
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto loadInfosToTree(String companyId, String projectId) {
		BaseDto dto = new BaseDto();
		if(CommonUtils.isEmpty(projectId)){
			dto.setMessageMap(new MessageMap(MessageMap.INFOR_WARNING,"传入项目id为空,查询失败."));
		}else{
			dto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
			dto.setLstDto(this.tJgStaffGropMapper.loadInfosToTree(projectId));
		}
		return dto;
	}

	
	@SuppressWarnings({ "rawtypes" })
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto delStaffGropAndChildren(String companyId, String id) {
		
		if(CommonUtils.isEmpty(id)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空,删除失败."));
		}
		
		TJgStaffGrop paramObj = new TJgStaffGrop();
		paramObj.setId(id);
		String idStr = this.tJgStaffGropMapper.findCListStr(paramObj);
		int rsltCount = 0;
		if(CommonUtils.isNotEmpty(idStr)){
			if(idStr.startsWith(Constants.STR_COMMA)){
				idStr = idStr.substring(1);
			}
			
			List<String> ids = CommonUtils.str2List(idStr, Constants.STR_COMMA);
			rsltCount = this.tJgStaffGropMapper.batchDel(ids); 
		}
		
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS, "删除成功. 共计删除[" + rsltCount + "]条数据."));
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto getProjectListForRole(String companyId, String userId) {
		if(CommonUtils.isEmpty(userId)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不可为空"));
		}
		List<TJgStaffGrop> resultList=this.tJgStaffGropMapper.getProjectListForRole(userId);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setLstDto(resultList);
		baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		return baseDto;
	}
	
	
}
