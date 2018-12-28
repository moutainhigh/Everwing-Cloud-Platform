package com.everwing.coreservice.wy.dao.mapper.configuration.project;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;

import java.util.List;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsShareBasicsInfoMapper {

	List<TBsShareBasicsInfo> listPageShareInfos(TBsShareBasicsInfo entity);
	
	int insertShare(TBsShareBasicsInfo tBsShareBasicsInfo);
	
	/**
	 * 根据id批量修改
	 * @return
	 */
	int batchDelShares(List<String> ids);
	
	TBsShareBasicsInfo getShareBasicInfoOne(TBsShareBasicsInfo entity);
	
	int updateShareBasic(TBsShareBasicsInfo entity);
	
	List<TBsShareBasicsInfo> getUsedShareInfo (String projectId,String shareType);
	
	int updateShareBasicInfo(List<String> ids);
	
	int checkIsHasShared(String shareId);
	
	int deleteShareBasicById(String id);
	
	int innvalidShareBasic(String id);
	
}
