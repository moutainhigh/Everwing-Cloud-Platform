/**
 * @Title: EnterpriseCustNewNewMapper.java
 * @Package com.flf.mapper
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件开发有限公司
 * 
 * @author wangtao
 * @date 2015-5-25 下午1:04:39
 * @version V1.0
 */

package com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust;

import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewList;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewSearch;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.SearchPersonAndEnterprise;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * @ClassName: EnterpriseCustNewNewMapper
 * @Description: TODO
 * @author wangyong
 * @date 2015-5-25 下午1:04:39
 *
 */

public interface EnterpriseCustNewMapper {
	/*
	 *  企业客户表
	 */
    List<EnterpriseCustNew> listPageEnterpriseCustNew(EnterpriseCustNew enterpriseCustNew) throws DataAccessException;//分页查询显示所有企业客户信息
    EnterpriseCustNewSearch getEnterpriseCustNewById(String enterpriseId) throws DataAccessException;//根据id查询企业客户信息

    int insertEnterpriseCustNewUUID(EnterpriseCustNew enterpriseCustNew) throws DataAccessException; //不自动生成主键添加企业客户信息

    List<EnterpriseCustNew> findEnterpriseCustNew(EnterpriseCustNew enterpriseCustNew) throws DataAccessException; //根据条件查询企业客户信息
    
	List<EnterpriseCustNew> getenterpriseCustNewByHouseId(String houseId) throws DataAccessException;//根据房屋id查询公司信息
	
	List<EnterpriseCustNew> getenterpriseCustNewByStallNewId(String stallId) throws DataAccessException;//根据车位id查询公司信息
	
	List<EnterpriseCustNew> getSearchEnterprise(SearchPersonAndEnterprise searchPersonAndEnterprise) throws DataAccessException;

	List<EnterpriseCustNew> listEnterpriseCustNewByCondition(EnterpriseCustNew enterpriseCustNew) throws DataAccessException;//根据条件查询企业客户的相关信息


	List<EnterpriseCustNew> selectEnterpriseCust(String buildingStructureId) throws DataAccessException;//根据建筑结构id查询有效企业客户业主的信息
	List<EnterpriseCustNew> listEnterpriseCustNewListByCondition(EnterpriseCustNew enterpriseCustNew) throws DataAccessException;//导出用
	
	/**
	 * 根据企业名称和企业证件号查企业客户主键id
	 * @author xiaocong
	 * @date 上午10:16:35
	 * @Description:TODO
	 * @return
	 * @updateby
	 */
	String getEnterpriseId(String tradingNumber,String name) throws DataAccessException;
	List<EnterpriseCustNew> listPageEnterpriseCustNewHelp(EnterpriseCustNew enterpriseCustNew) throws DataAccessException;
	/**
	 * 根据企业名称模糊查询企业信息
	 * @param enterpriseCustNew
	 * @return
	 */
	List<EnterpriseCustNew> listPageEnterpriseByName(EnterpriseCustNew enterpriseCustNew) throws DataAccessException;
	
	/**
	 * 支持多个参数模糊查询
	 * @param enterpriseCustNew
	 * @return
	 */
	List<EnterpriseCustNew> listPageEnterpriseByParams(EnterpriseCustNew enterpriseCustNew) throws DataAccessException;
	
	List<EnterpriseCustNew> listPageEnterprise(EnterpriseCustNew enterpriseCustNew) throws DataAccessException;
	
	
	EnterpriseCustNew getEnterpriseCustById(String enterpriseId) throws DataAccessException;


	List<EnterpriseCustNew> getEnterpriseCustNewByName(EnterpriseCustNew enterpriseCustNew);



	/**  ------------------------------------------ 分割线 -----------------------------------------------------**/
	/**
	 * 通过企业名称 ，编号地址等条件查询业主的信息 分页
	 * @param enterpriseCustNewSearch
	 */
	List<EnterpriseCustNew> listPageEnterpriseCustNewByCondition(EnterpriseCustNewSearch enterpriseCustNewSearch) throws DataAccessException;//根据条件查询企业客户的相关信息

	/**
	 * 查询所有企业客户信息
	 * @return
	 * @throws DataAccessException
	 */
	List<EnterpriseCustNew> listAllEnterpriseCustNew() throws DataAccessException;

	/**
	 * 获取最大编号
	 * @return
	 */
	String selectCustCode();

	/**
	 * 添加企业客户信息
	 * @param enterpriseCustNew
	 * @return
	 */
	int insertEnterpriseCustNew(EnterpriseCustNew enterpriseCustNew);

	/**
	 * 根据id删除企业客户基本信息
	 * @param enterpriseId
	 */
	int deleteEnterpriseCustNew(String enterpriseId) throws DataAccessException;

	/**
	 * 修改企业客户基本信息
	 * @param enterpriseCustNew
	 */
	int updateEnterpriseCustNew(EnterpriseCustNew enterpriseCustNew) throws DataAccessException; //修改企业客户信息

	/**
	 * 根据姓名查询
	 * @param enterpriseCustNew
	 * @return
	 */
	List<EnterpriseCustNew> findByNameAndId(EnterpriseCustNew enterpriseCustNew);

	/**
	 * 根据企业资质编号查询
	 * @param name
	 * @param numStr
	 * @return
	 */
	EnterpriseCustNew findBySomeParams(@Param("name") String name, @Param("numStr") String numStr);


    List<EnterpriseCustNewList> findByCondition(EnterpriseCustNew enterpriseCustNew);

//	void batchInsert(List<EnterpriseCustNew> tcEnterpriseListNew);
//
//	void batchModify(List<EnterpriseCustNew> subList);

	int  selectByTradingNum(String tradingNumber) throws DataAccessException;

	int selectByName (String enterpriseName) throws  DataAccessException;

	void batchadd(List<EnterpriseCustNew> newList) throws  DataAccessException;
}
