package com.everwing.coreservice.wy.dao.mapper.account.relation;

import com.everwing.coreservice.common.wy.entity.account.relation.AccountRelation;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AccountRelationMapper {
	/*查询所有的账户关系信息*/
	List<AccountRelation> listAllAccountRelation() throws DataAccessException;
	/*分页查询账户关系信息*/
    List<AccountRelation> listPageAccountRelation(AccountRelation accountRelation) throws DataAccessException;
    /*根据id查询账户关系信息*/
    AccountRelation getAccountRelationById(String accountRelationId) throws DataAccessException;
    /*删除账户关系信息*/
    int deleteAccountRelation(String accountRelationId) throws DataAccessException;
    /*新增账户关系信息*/
    int insertAccountRelation(AccountRelation accountRelation) throws DataAccessException;
    /*修改账户关系信息*/
    int updateAccountRelation(AccountRelation accountRelation) throws DataAccessException;
    /*根据账户id获取账户关联信息*/
    List<AccountRelation> getAccountRelationByAccountId(String accountId) throws DataAccessException;
    
    /**
     * 批量插入关联表数据
     * 王洲
     * 2016.04.19
     * @param list
     * @return
     */
    int insertRelationList(List<AccountRelation> list) throws DataAccessException;
    
    /**
     * 根据个人账户id和资产账户id查询是否存在数据
     * 王洲
     * 2016.04.19
     * @param accountId
     * @param assetAccount
     * @return
     */
    AccountRelation getAssetRelationByParams(String accountId, String assetAccount) throws DataAccessException;
	void deleteByEntity(AccountRelation delArs) throws DataAccessException;
	
	int deleteAccountRelationByCustId(String custId) throws DataAccessException;
	
}