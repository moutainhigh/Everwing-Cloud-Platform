package com.everwing.coreservice.wy.core.service.impl.configuration.project;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsShareBasicService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsShareBasicsInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsShareBuildingRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsShareRelatedTaskMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("tBsShareBasicServiceImpl")
public class TBsShareBasicServiceImpl extends Resources implements TBsShareBasicService {

	private static Logger logger=Logger.getLogger(TBsShareBasicServiceImpl.class);
	
	@SuppressWarnings("unused")
	private static final String LOG_STR = "当前时间 : %s , 项目  -> %s : %s";
	
	@Autowired
	private TBsShareBasicsInfoMapper tBsShareBasicsInfoMapper;
	
	@Autowired
	private TBsShareBuildingRelationMapper shareBuildingRelationMapper;
	
	@Autowired
	private TBsShareRelatedTaskMapper shareRelatedTaskMapper;
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addShareBasic(String companyId, TBsShareBasicsInfo entity) {
		logger.info("==========addShareBasic  statrt===========");
		logger.info("==========参数信息："+entity.toString()+"===========");
		if(CommonUtils.isEmpty(entity)){
			logger.info("==========请求参数为空===========");
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不可为空");
		}
		
		//分新增和保存
		if(CommonUtils.isNotEmpty(entity.getId())){
			//修改
			this.tBsShareBasicsInfoMapper.updateShareBasic(entity);
			logger.info("================修改分摊信息成功===============");
			MessageMap msg = new MessageMap(MessageMap.INFOR_SUCCESS,"修改分摊成功！");
			msg.setObj(entity.getId());
			return msg;
		}
		
		//分摊校验分摊名是否相同
		TBsShareBasicsInfo sbinfo=this.tBsShareBasicsInfoMapper.getShareBasicInfoOne(entity);
		if(CommonUtils.isNotEmpty(sbinfo)){
			logger.info("==========分摊名已存在=========");
			return new MessageMap(MessageMap.INFOR_ERROR,"分摊名已存在，请修改！");
		}
		
		//因为这里需要得到插入的id，所以采用在外面生成的方法
		String id=CommonUtils.getUUID();
		entity.setId(id);
		int num=this.tBsShareBasicsInfoMapper.insertShare(entity);
		if(num == 1){
			logger.info("==========新增分摊成功=========");
			MessageMap msg = new MessageMap(MessageMap.INFOR_SUCCESS,"新增分摊成功！");
			msg.setObj(id);
			return msg;
		}
		//校验插入的数据是否符合要求
		logger.info("==========addShareBasic  end===========");
		return new MessageMap(MessageMap.INFOR_ERROR,"新增分摊信息失败");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageShareInfos(String companyId, TBsShareBasicsInfo entity) {
		logger.info("==========listPageShareInfos  statrt===========");
		MessageMap msg;
		if(CommonUtils.isEmpty(entity)){
			logger.info("==========请求参数为空===========");
			msg = new MessageMap(MessageMap.INFOR_ERROR,"请求参数不可为空");
			return new BaseDto<>(msg);
		}
		List<TBsShareBasicsInfo> resultList=this.tBsShareBasicsInfoMapper.listPageShareInfos(entity);
		BaseDto basedto=new BaseDto<>();
		basedto.setLstDto(resultList);
		basedto.setPage(entity.getPage());
		logger.info("=============查询成功");
		return basedto;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap deleteShareBsic(String companyId, String shareId) {
		if(CommonUtils.isEmpty(shareId)) {
			return new MessageMap(MessageMap.INFOR_ERROR,"执行对分摊方案的删除请求参数为空");
		}
		//查询此方案是否已经分摊
		int isShared=this.tBsShareBasicsInfoMapper.checkIsHasShared(shareId);
		if(isShared > 0) {
			return new MessageMap(MessageMap.INFOR_ERROR,"没有符合条件的可删除方案信息");
		}
		//执行删除操作（删除关联建筑信息表，删除分摊任务表,删除方案信息表）
		this.shareBuildingRelationMapper.deleteByShareBasicId(shareId);
		this.shareRelatedTaskMapper.deleteTaskByShareId(shareId);
		this.tBsShareBasicsInfoMapper.deleteShareBasicById(shareId);
		return new MessageMap(MessageMap.INFOR_SUCCESS,"删除操作执行成功");
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap innvalidShareBasic(String companyId, String shareId) {
		if(CommonUtils.isEmpty(shareId)) {
			return new MessageMap(MessageMap.INFOR_ERROR,"执行对分摊方案的失效请求参数为空");
		}
		//直接执行失效处理
		this.tBsShareBasicsInfoMapper.innvalidShareBasic(shareId);
		return new MessageMap(MessageMap.INFOR_SUCCESS,"失效执行成功");
	}
	
	
	
	
	
}
