package com.everwing.coreservice.wy.dao.mapper.account;

import com.everwing.coreservice.common.wy.entity.account.Account;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AccountMapper {
	/*查询所有的账户信息*/
	List<Account> listAllAccount() throws DataAccessException;
	/*分页查询账户信息*/
    List<Account> listPageAccount(Account account) throws DataAccessException;
    /*根据id查询账户信息*/
    Account getAccountById(String accountId) throws DataAccessException;
    /*删除账户信息*/
    int deleteAccount(String accountId) throws DataAccessException;
    /*新增账户信息*/
    int insertAccount(Account account) throws DataAccessException;
    /*修改账户信息*/
    int updateAccount(Account account) throws DataAccessException;
    /*根据客户id获取账户信息*/
    Account getAccountByCustId(String custId) throws DataAccessException;
    
    int deleteAccountByCustId(String custId) throws DataAccessException;
    
}