/**
 * @Title: PersonCustNewMapper.java
 * @Package com.flf.mapper
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件开发有限公司
 * 
 * @author wangtao
 * @date 2015-5-25 下午1:21:29
 * @version V1.0
 */

package com.everwing.coreservice.wy.dao.mapper.cust.person;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.dto.CustAccountDto;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewImportList;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: PersonCustNewMapper
 * @Description: TODO
 * @author wangyong
 * @date 2015-5-25 下午1:21:29
 *
 */

public interface PersonCustNewMapper {

	
	/*
	 *  客户信息表
	 */
    List<PersonCustNew> listPagePersonCustNew(PersonCustNew personCustNew) throws DataAccessException;//分页查询显示客户信息
    List<PersonCustNew> listPagePersonCustNewBySearch(PersonCustNew personCustNew) throws DataAccessException;//分页查询显示客户信息
    List<PersonCustNew> listAllPersonCustNew() throws DataAccessException; //查询客户信息
    PersonCustNew getPersonCustNewById(String custId) throws DataAccessException;//根据id查询客户信息
    List<PersonCustNew> listPagePersonByIds(List<String> ids) throws DataAccessException;
    int insertPersonCustNew(PersonCustNew personCustNew) throws DataAccessException; //添加客户信息
    
    List<PersonCustNew> getPersonFuzzy(String type) throws DataAccessException;
    
    List<Map> listPageDatas(PersonCustNew person) throws DataAccessException;
    

    int insertPersonCustNewUUID(PersonCustNew personCustNew) throws DataAccessException; //添加客户信息主键不自动生成
    
    int updatePersonCustNew(PersonCustNew personCustNew) throws DataAccessException; //修改客户信息
    int deletePersonCustNew(String custId) throws DataAccessException; //删除客户信息
    
    List<PersonCustNew> listPagePersonCustByBuildingCode(PersonCustNew cust) throws DataAccessException;  // 通过BuildingCode查询客户信息
    
    List<PersonCustNew> findPersonCustNewByhouseId(String houseId) throws DataAccessException;  // 通过房屋id查询客户信息
    
    List<PersonCustNew>  findEnterpriseCustByIdRestful(String EnterpriseCustId) throws DataAccessException;//通过企业id查询所有企业下的员工信息
    
    List<PersonCustNew> getPersonCustNewByHouseId(String houseId) throws DataAccessException;  // 通过房屋id查询客户信息 
    
    List<PersonCustNew> getPersonCustNewByStallNewId(String houseId) throws DataAccessException;  // 通过车位id查询客户信息 
    
//    List<PersonCustNew> getSearchPersonCust(SearchPersonAndEnterprise searchPersonAndEnterprise) throws DataAccessException;  // 通过条件查询客户信息
    
    List<PersonCustNew> listPagePersonCustByCondition(PersonCustNew personCustNew) throws DataAccessException;//个人客户管理按条件检索
    
    PersonCustNew getPersonCustNewbyNameAndCardAndNum(String name,String cardNum,String phoneNum) throws DataAccessException; //根据姓名电话身份证号查询客户信息
    
    PersonCustNew getPersonCustById(String custId) throws DataAccessException;
    
    List<PersonCustNew> listPagelistSearchPersonCust(PersonCustNew custNew) throws DataAccessException;//根据搜索条件分页查询客户信息
    
    List<PersonCustNew> listPersonCustNewByBuildingStructureId(String buildingStructureId) throws DataAccessException;//根据建筑结构id查询出个人客户

    List<PersonCustNew> getSelectPersonNew(String buildingStructureid) throws DataAccessException;  //根据建筑结构id获取客户信息
    
    List<PersonCustNew> listPagePersonCustNewByAllSearch(PersonCustNew personCustNew) throws DataAccessException;
    
    String selectCustCode() throws DataAccessException;
    
    List<PersonCustNew> selectPersonCust(String buildingStructureId) throws DataAccessException;//根据建筑结构id查询有效业主信息
    
    List<PersonCustNew> listAllPersonCustNewList(PersonCustNew personCustNew) throws DataAccessException;
    
    /**
     * 根据证件号和姓名查询人员信息主键
     * @author xiaocong
     * @date 上午10:00:10
     * @Description:TODO
     * @return
     * @updateby
     */
    String getCustId(String cardNum,String name) throws DataAccessException;
    
    int getCountByNoOrPhone(PersonCustNew personCustNew) throws DataAccessException;
    
    /**
     * 根据公司id查询cust集合
     * @param companyId
     * @return
     */
	List<PersonCustNew> selectCustListByCompanyId(String companyId) throws DataAccessException;
	
	
	/**
	 * 查询编辑是是否重复复，除开自己
	 * @param personCustNew
	 * @return
	 */
	int getCountByNoOrPhoneById(PersonCustNew personCustNew) throws DataAccessException;
	
	/**
	 * 获取最后一个个人客户的信息
	 * @return
	 */
	PersonCustNew getLastPersonCustNew() throws DataAccessException;
	
	/**
	 * 根据客户id的集合查询客户信息
	 * @param custIdList
	 * @return
	 */
	List<PersonCustNew> getPersonCustListByCustIdList(List<String> custIdList) throws DataAccessException;
	
	/**
	 * 根据houseId以及客户类型查询客户信息
	 * @param map
	 * @return
	 */
	List<PersonCustNew> selectPersonCustNewByHouseIdAndCustType(Map<String, Object> map) throws DataAccessException;
	
	/**
	 * 根据客户ID查询客户信息
	 * @param custId
	 * @return
	 */
	PersonCustNew selectCustInfoById(String custId) throws DataAccessException;
	
	/**
	 * 根据CompanyId查询客户信息
	 * @param companyId
	 * @return
	 */
	List<PersonCustNew> searchAllPersonCustNewByCompanyId(String companyId) throws DataAccessException;
	
	List<PersonCustNew>listPersonCustByAll(String all) throws DataAccessException;
	
	PersonCustNew getPersonCustNewVIPbyId(String customerId) throws DataAccessException;
	
	List<PersonCustNew> ListPagefindEnterpriseCustByIdRestful(PersonCustNew personCustNew) throws DataAccessException;
	
	//张三,李四,王五
	PersonCustNew findNamesByBuildingCode(String buildingCode);
	PersonCustNew findBySomeParams(@Param("name") String name, @Param("numStr") String numStr);
	//查询cust
	List<PersonCustNew> queryCustByNameOrPhone(PersonCustNew queryCustByNameOrPhone) throws DataAccessException;

    void resetMobileByPhone(String mobile, String oldMobile);

	/**
	 * 根据姓名,身份证号,注册号码查询
	 * @param personCustNew
	 * @return
	 */
	List<PersonCustNew> getPersonCustNewByName(PersonCustNew personCustNew) throws DataAccessException;

	/**
	 * 批量导入
	 * @param list
	 * @return
	 */
	int batchInsert(List<PersonCustNewImportList> list);

	CustAccountDto queryCustAccountInfoById(String id);
}
