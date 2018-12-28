package com.everwing.coreservice.wy.dao.mapper.annex;

import com.everwing.coreservice.common.wy.entity.annex.Annex;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface AnnexMapper {

	int insertAnnex(Annex annex) throws DataAccessException;//添加附件信息

	int insertAnnexUUID(Annex annex) throws DataAccessException;//添加附件信息(UUID)

	List<Annex> getAnnexbyRelationId(String relationId) throws DataAccessException;//通过关系id获取附件

	int deleteAnnex(String pactId) throws DataAccessException; //删除合同文件

	int updateAnnex(Annex annex) throws DataAccessException;//更新文件

	Annex findAnnexByRelationIdAndAddress(String relationId,String annexAddress) throws DataAccessException;//根据id和地址获取对象

	int deleteAnnexByRelationId(String relationId) throws DataAccessException;//根据关联id删除附件;
	
	List<Annex> getAnnexByPactId() throws DataAccessException;//根据合同id获取附件
	
	/**
	 * 根据关联ID和附件名称查询附件是否已经存在
	 */
	Annex getListAnnexByReIdAndName(String relationId,String annexName,String annexType);
	
	/**
	 * 分页查询
	 */
	List<Annex> listPageEnclosure(Annex annex);
	
	/**
	 * 分页查询图片附件信息
	 */
	String getUploadFileId(String annexId);
	
	/**
	 * 根据附件编号组查询出来
	 */
	List<Annex> getByAnnexIds(List<String> annexIds);
	
	/**
	 * 根据附件编号组删除
	 */
	int delByAnnexIds(List<String> annexIds);
	
	/**
	 * 选择ID
	 * @param annex
	 * @return
	 */
	Map<String,Object> selectIdPerYear(Annex annex);
	
	/**
	 * 查账单模式
	 */
	Map<String,Object> selectBillTypePerYear(Annex annex);
	Annex findById(String annexId);
	
	/**
	 * 根据关联id查询最近的一条数据
	 * @param id
	 * @return
	 */
	Annex findCurrentAnnexByRleationId(String id);
	
	List<Annex> findByMd5(String md5);
	
	void batchAdd(List<Annex> annexs) throws DataAccessException;
}
