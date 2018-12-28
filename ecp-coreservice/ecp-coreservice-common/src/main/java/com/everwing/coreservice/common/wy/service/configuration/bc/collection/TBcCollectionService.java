package com.everwing.coreservice.common.wy.service.configuration.bc.collection;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;

import java.util.Date;
import java.util.List;


public interface TBcCollectionService { 

	/**
	 * @TODO 分页查询银联托收头文件
	 * @param companyId
	 * @param head
	 * @return
	 */
	BaseDto listPageUnionCollHeads(String companyId, TBcUnionCollectionHead head);

	/**
	 * @TODO 分页查询银联托收子文件
	 * @param companyId
	 * @param body
	 * @return
	 */
	BaseDto listPageCollBodyInfos(String companyId, TBcUnionCollectionBody body);

	/**
	 * @TODO 分页查询银联回盘头文件
	 * @param companyId
	 * @param head
	 * @return
	 */
	BaseDto listPageUnionBackHeads(String companyId, TBcUnionBackHead head);

	/**
	 * @TODO 分页查询银联回盘子文件
	 * @param companyId
	 * @param body
	 * @return
	 */
	BaseDto listPageUnionBackbodies(String companyId, TBcUnionBackBody body);

	/**
	 * @TODO 生成银联txt托收文件
	 * @param companyId
	 * @param head
	 * @return
	 */
	BaseDto genCollTxtFile(String companyId, TBcUnionCollectionHead head);

	/**
	 * @TODO 导出托收文件(银联/金融联)
	 * @param companyId
	 * @param head
	 * @param type
	 * @return
	 */
	BaseDto exportCollTxtFile(String companyId, TBcUnionCollectionHead head , Integer type);

	/**
	 * @TODO 分页查询金融联头文件
	 * @param companyId
	 * @param head
	 * @return
	 */
	BaseDto listPageJrlHeads(String companyId, TBcJrlHead head);

	/**
	 * @TODO 分页查询金融联子文件
	 * @param companyId
	 * @param body
	 * @return
	 */
	BaseDto listPageJrlBodies(String companyId, TBcJrlBody body);

	/**
	 * @TODO 生成金融联托收的zip文件
	 * @param companyId
	 * @param head
	 * @return
	 */
	BaseDto genJrlCollZipFile(String companyId, TBcJrlHead head);

	/**
	 * 生成托收数据
	 * @param companyId
	 * @param project
	 * @param totalId
	 * @param isStopCommon
	 */
	void genColl(String companyId,TBcProject project,String totalId, Integer isStopCommon);

	void batchInsertBodies(String companyId, List<?> list);

	MessageMap importFile(String companyId, String fileContent,String fileName, Integer flag , String totalId, String projectId, String userId);

	void backUnionData2Account(String companyId, TBcUnionBackHead head,List<TBcUnionBackBody> bodies);

	void backJrlData2Account(String companyId, TBcJrlHead head,List<TBcJrlBody> bodies);

	BaseDto genCollByManaul(String companyId, String projectId,Integer isStopCommon);

	MessageMap importFileByQueue(String companyId, String fileContent,String fileName, Integer flag, String totalId, String projectId,String userId);

	BaseDto findDatas(String companyId, String projectId, Date createTime,Integer collectionType);

	/**
	 * 单独加一个为重庆银联添加手续费的方法
	 * @param companyId
	 * @param projectId
	 * @return
	 */
	MessageMap separateAddServiceChargeForUnion(String companyId,String projectId);
	
	
}
