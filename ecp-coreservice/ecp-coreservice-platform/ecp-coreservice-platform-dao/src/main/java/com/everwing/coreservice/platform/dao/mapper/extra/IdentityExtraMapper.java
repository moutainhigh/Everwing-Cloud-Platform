package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.generated.AccountIdentity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IdentityExtraMapper {

    @Insert("insert into account_identity(id,account_id,identity_code,identity_type,real_name,mobile,create_time)values(UUID(),#{accountId},#{identityCode},#{identityType},#{realName},#{mobile},now())")
    int insert(AccountIdentity accountIdentity);

    @Select("SELECT a.account_id accountId,a.id identityId,a.identity_type identityType,a.real_name realName,a.mobile FROM account_identity a WHERE a.id=#{identityId}")
    Map<String,String> selectById(String identityId);

    @Select("SELECT a.account_id accountId,a.id identityId,a.identity_code identityCode,a.identity_type identityType,a.real_name realName,a.mobile FROM account_identity a WHERE a.account_id=#{accountId}")
    List<Map<String,String>> selectByAccountId(String accountId);

    @Select("select count(1) from account_identity where account_id=#{accountId}")
    int checkExists(@Param("accountId") String accountId);

    @Select("select count(1) from account_identity where mobile=#{mobile}")
    int checkMobile(@Param("mobile") String mobile);

}
