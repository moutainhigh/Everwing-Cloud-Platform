package com.everwing.coreservice.wy.core.service.impl.configuration.project;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBuildingRelation;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareRelatedTask;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsShareRelatedTaskService;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsShareBuildingRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsShareRelatedTaskMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("tBsShareRelatedTaskServiceImpl")
public class TBsShareRelatedTaskServiceImpl implements TBsShareRelatedTaskService {

	private static Logger logger=Logger.getLogger(TBsShareRelatedTaskServiceImpl.class);
	
	private static final String LOG_STR = "当前时间 : %s , 项目  -> %s : %s";
	
	@Autowired
	private TBsShareRelatedTaskMapper tBsShareRelatedTaskMapper;
	@Autowired
	private TBsShareBuildingRelationMapper tBsShareBuildingRelationMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageShareTask(String companyId, TBsShareRelatedTask entity) {
		logger.info("==========listPageShareTask  statrt===========");
		MessageMap msg;
		if(CommonUtils.isEmpty(entity)){ 
			logger.info("==========请求参数为空===========");
			msg = new MessageMap(MessageMap.INFOR_ERROR,"请求参数不可为空");
			return new BaseDto<>(msg);
		}
		List<TBsShareRelatedTask> resultList=this.tBsShareRelatedTaskMapper.listPageShareTask(entity);
		BaseDto basedto=new BaseDto<>();
		basedto.setLstDto(resultList);
		basedto.setPage(entity.getPage());
		logger.info("=============查询成功");
		return basedto;
	}
	
	
	
	/**
	 * 新增修改共用
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addShareTask(String companyId, TBsShareRelatedTask entity) throws ECPBusinessException{
		logger.info("==========addShareTask  statrt===========");
		logger.info("==========参数信息："+entity.toString()+"===========");
		MessageMap msgMap = new MessageMap();
		List<TBsShareBuildingRelation> tbsRealistNew =null;
		if(CommonUtils.isEmpty(entity)){
			logger.info("==========请求参数为空===========");
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("参数为空，请重试");
			return msgMap;
		}else{
			 tbsRealistNew = entity.getTbsRealtionList();
			 if(CollectionUtils.isEmpty(tbsRealistNew)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("关联建筑不能为空");
					return msgMap;
				}
		}
		
		//如果是新增需要校验一下名称是否重复
		if(StringUtils.isBlank(entity.getId())){
			//同一个分摊下的分摊名不能相同
			if(StringUtils.isBlank(entity.getId())){  //编号为空则是新增，新增需要校验规则名称是否为重复
				TBsShareRelatedTask shareTask = tBsShareRelatedTaskMapper.selectTaskByBasicId(entity.getShareBasicsId(), entity.getShareTaskName());
				if(CommonUtils.isNotEmpty(shareTask)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("与已有分摊项名称重复，请修改!");
					return msgMap;
				}
			}
		}
		
		
		try {
			msgMap = saveScheme(entity);
		} catch (Exception e) {
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		logger.info("==========addShareBasic  end===========");
		return msgMap;
	}
	
	//执行保存
	@Transactional(rollbackFor=Exception.class)
	private MessageMap saveScheme(TBsShareRelatedTask entity) throws ECPBusinessException{
		MessageMap msgMap = new MessageMap();
//		List<String> tbsRealistOld = null;
		List<TBsShareBuildingRelation> machlist = new ArrayList<TBsShareBuildingRelation>(); //用来存放设置了sharetaskid的建筑关联对象
		try {
//			List<TBsShareBuildingRelation> tbsRealistOld= this.TBsShareBuildingRelationMapper.selectAssetsByRuleId(entity.getId());
			//
			if(StringUtils.isNotBlank(entity.getId())){
				//如果是修改需要删除其关联的建筑信息--后面再重新插入
				this.tBsShareBuildingRelationMapper.deleteRelationBuilding(entity.getId());
				 this.tBsShareRelatedTaskMapper.updateShareTask(entity);
			}
			
			if(StringUtils.isBlank(entity.getId())){ //新增
				 entity.setId(CommonUtils.getUUID());
				 this.tBsShareRelatedTaskMapper.insertShareTask(entity);
			 }
			
			//关联建筑信息
			List<TBsShareBuildingRelation> tbsReaNewlist = entity.getTbsRealtionList();
			int commitSize = 500;//默认每次提交数量
			for(TBsShareBuildingRelation tb : tbsReaNewlist){
				tb.setShareTaskId(entity.getId());
				machlist.add(tb);
			}
			//保存建筑关系
			logger.info("========分批保存关联信息=======");
			 int size = machlist.size();
			 if(size <= commitSize){
				 this.tBsShareBuildingRelationMapper.batchInsert(machlist);
	           }else{
	        	   if(size % commitSize == 0){
	        		   int count = size / commitSize;
	        		   for(int i=0;i<count;i++){
	                        int fromIndex = i * commitSize;
	                        int toIndex = (i+1) * commitSize;
	                        tBsShareBuildingRelationMapper.batchInsert(machlist.subList(fromIndex,toIndex));
	                    }
	        	   }else{
	        		   int endIndex = 0;
	                    int count = size / commitSize;
	                    for(int i=0;i<count;i++){
	                        int fromIndex = i * commitSize;
	                        int toIndex = (i+1) * commitSize;
	                        endIndex = toIndex;
	                        tBsShareBuildingRelationMapper.batchInsert(machlist.subList(fromIndex,toIndex));
	                    }
	                    tBsShareBuildingRelationMapper.batchInsert(machlist.subList(endIndex,size));
	        	   }
	           }
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			msgMap.setObj(entity.getId());//新插入规则的id，页面上需要使用
			msgMap.setMessage("保存成功!");
			return msgMap;
				
		} catch (Exception e) {
			logger.info(e.getMessage());
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("保存失败！");
			
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		
	}


	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto loadRelationTaskBuiling(String companyId, String projectId, String taskId) {
		
		logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), taskId , "开始查询此分摊任务下的已选择建筑信息."));
		MessageMap msg;
		if(CommonUtils.isEmpty(projectId) || CommonUtils.isEmpty(taskId)) {
			logger.info("********请求参数为空********");
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不可为空"));
		}
		List<Map<String, String>> result=tBsShareBuildingRelationMapper.loadRelationTaskBuiling(projectId, taskId);
		if(CommonUtils.isEmpty(result)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"无查询结果"));
		}
		msg=new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功");
		BaseDto baseDto=new BaseDto<>(result);
		baseDto.setMessageMap(msg);
		logger.info("*******查询成功********");
		return baseDto;
	}


	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap deleteTaskById(String companyId, String id) {
		MessageMap msg;
		tBsShareRelatedTaskMapper.deleteTaskById(id);
		msg=new MessageMap(MessageMap.INFOR_SUCCESS,"成功删除分摊任务项");
		return msg;
	}
	
	
}
