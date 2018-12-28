package com.everwing.coreservice.platform.dao.mapper.generated;

import com.everwing.coreservice.common.platform.entity.generated.AccountIdentity;
import com.everwing.coreservice.common.platform.entity.generated.AccountIdentityExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountIdentityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    long countByExample(AccountIdentityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int deleteByExample(AccountIdentityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int insert(AccountIdentity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int insertSelective(AccountIdentity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    List<AccountIdentity> selectByExample(AccountIdentityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    AccountIdentity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AccountIdentity record, @Param("example") AccountIdentityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AccountIdentity record, @Param("example") AccountIdentityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AccountIdentity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_identity
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AccountIdentity record);
}