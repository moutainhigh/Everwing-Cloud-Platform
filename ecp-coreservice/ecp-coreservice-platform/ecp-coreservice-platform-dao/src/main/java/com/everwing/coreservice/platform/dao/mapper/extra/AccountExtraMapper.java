package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.platform.dao.mapper.extra.provider.AccountProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AccountExtraMapper {
	int batchInsert(List<Account> list);

	int batchCancel(List<Account> list);

	int checkAccountNames(@Param("accountList") List<Account> accountList,
			@Param("type") Integer type);

	@Delete("delete from account where account_id = #{accountId}")
	int deleteById(@Param("accountId") String accountId);
	
	@Select("select * from account where  account_name = (select max(account_name) from account where type=#{type} and state=1) and type=#{type} and state=1")
	@ResultMap("com.everwing.coreservice.platform.dao.mapper.generated.AccountMapper.BaseResultMap")
	Account queryMaxAccountName(@Param("type") int type);

	@Update("update account set password=#{password} where account_id=#{accountId}")
    int updateAccountById(@Param("accountId") String accountId,@Param("password") String password);

	@Select("SELECT c.building_code " +
			"  from person_cust a " +
			"    LEFT JOIN person_building b on a.cust_id=b.cust_id " +
			"    LEFT JOIN tc_building c ON b.building_id=c.id AND c.building_type='house' " +
			" WHERE a.register_phone=#{mobile}")
    List<String> selectAccountBuildingR(String mobile);

	@InsertProvider(type = AccountProvider.class,method = "batchInsertAccountHouse")
	void batchInsertAccountHouse(@Param("paramList") List<Map<String, String>> paramList);

	@Delete("delete from account_and_house where mobile=#{mobile}")
	void delUserHouse(String mobile);

	@SelectProvider(type = AccountProvider.class,method = "selectAccountCodesAndCodesByCustIdsAndBIds")
    List<Map<String,String>> selectAccountCodesAndCodesByCustIdsAndBIds(@Param("custIdList") List<String> custIdList,@Param("buildingIdList") List<String> buildingIdList);

	@Select("select building_code from account_and_house where mobile=#{mobile}")
	List<String> selectCodesByMobile(String mobile);

	@DeleteProvider(type = AccountProvider.class,method = "delUserHouse")
    void delUserHouseByCode(List<Map<String, String>> value);

	@Select("select company_id from account where mobile=#{mobile}")
    String selectCompanyIdByMobile(@Param("mobile") String mobile);

	@Update("update account set company_id=#{companyId} where account_name=#{accountName} and type=#{type}")
	int updateCompanyIdByANT(@Param("accountName") String accountName,@Param("type") int type,@Param("companyId") String companyId);

	@Update("update account SET account_name=#{mobile},mobile=#{mobile} where account_id=#{accountId}")
    int updateMobile(@Param("accountId") String accountId, @Param("mobile") String mobile);

	@Select("select count(1) from account where mobile=#{mobile}")
    int checkMobileExists(@Param("mobile")String mobile);

	@Update("update person_cust SET register_phone=#{mobile} where register_phone=#{OldMobile}")
	int updatepersonMobile(@Param("mobile") String mobile ,@Param("OldMobile") String OldMobile);

	@Select("select count(1) from person_cust where register_phone=#{mobile}")
	int checkMobilePerson(@Param("mobile")String mobile);
}